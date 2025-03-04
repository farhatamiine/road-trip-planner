package com.amine.roadtripplanner.Controller;

import com.amine.roadtripplanner.Dto.request.TripRequest;
import com.amine.roadtripplanner.Dto.response.ApiResponseWrapper;
import com.amine.roadtripplanner.Dto.response.TripResponse;
import com.amine.roadtripplanner.Entities.Trip;
import com.amine.roadtripplanner.Entities.User;
import com.amine.roadtripplanner.Service.TripPlanningService;
import com.amine.roadtripplanner.enums.TripStatus;
import jakarta.validation.Valid;
import org.bson.types.ObjectId;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/trips")
public class TripController {

    private final TripPlanningService tripPlanningService;

    public TripController(TripPlanningService tripPlanningService) {
        this.tripPlanningService = tripPlanningService;
    }


    @PostMapping
    public ResponseEntity<ApiResponseWrapper<TripResponse>> createTrip(@RequestBody @Valid TripRequest tripRequest) {
        Trip tripEntity = TripRequest.convertToEntity(tripRequest);
        tripEntity.setTripStatus(TripStatus.PLANNED);
        Trip savedTrip = tripPlanningService.saveNewTrip(tripEntity);
        TripResponse tripResponse = TripResponse.fromTrip(savedTrip);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponseWrapper.success(tripResponse, "Trip created successfully"));
    }

    @DeleteMapping("{tripId}")
    public ResponseEntity<ApiResponseWrapper<TripResponse>> deleteTrip(@PathVariable ObjectId tripId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) authentication.getPrincipal();
        boolean deleted = tripPlanningService.deleteTrip(tripId, currentUser.getUserId());
        if (deleted) {
            return ResponseEntity.ok(ApiResponseWrapper.success(null, "Trip deleted successfully"));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponseWrapper.error("Trip not found or you don't have permission to delete it",
                            "TRIP_NOT_FOUND", null));
        }
    }
}
