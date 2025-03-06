package com.amine.roadtripplanner.Controller;

import com.amine.roadtripplanner.Dto.request.TripRequest;
import com.amine.roadtripplanner.Dto.response.ApiResponseWrapper;
import com.amine.roadtripplanner.Dto.response.TripResponse;
import com.amine.roadtripplanner.Entities.Trip;
import com.amine.roadtripplanner.Entities.User;
import com.amine.roadtripplanner.Security.SecurityUtils;
import com.amine.roadtripplanner.Service.TripPlanningService;
import jakarta.validation.Valid;
import org.bson.types.ObjectId;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/trips")
public class TripController {

    private final TripPlanningService tripPlanningService;
    private final SecurityUtils securityUtils;

    public TripController(TripPlanningService tripPlanningService, SecurityUtils securityUtils) {
        this.tripPlanningService = tripPlanningService;
        this.securityUtils = securityUtils;
    }


    @PostMapping
    public ResponseEntity<ApiResponseWrapper<TripResponse>> createTrip(@RequestBody @Valid TripRequest tripRequest) {
        Trip savedTrip = tripPlanningService.saveNewTrip(tripRequest);
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

    @GetMapping("{tripId}")
    public ResponseEntity<ApiResponseWrapper<TripResponse>> findTripById(@PathVariable ObjectId tripId) {
        Optional<TripResponse> optionalTripResponse = tripPlanningService.getTripById(tripId);
        return optionalTripResponse.map(tripResponse ->
                        ResponseEntity.ok(ApiResponseWrapper.
                                success(tripResponse, "Trip found successfully")))
                .orElse(
                        ResponseEntity.status(HttpStatus.NOT_FOUND).
                                body(ApiResponseWrapper.
                                        error("Trip not found", "TRIP_NOT_FOUND", null)));
    }

    @PutMapping("/{tripId}")
    public ResponseEntity<ApiResponseWrapper<TripResponse>> updateTrip(
            @PathVariable String tripId,
            @RequestBody @Valid TripRequest request) {

        ObjectId objectId = new ObjectId(tripId);

        User currentUser = securityUtils.getCurrentUser();

        return tripPlanningService.updateTrip(request, objectId, currentUser.getUserId())
                .map(updatedTrip -> ResponseEntity.ok(
                        ApiResponseWrapper.success(updatedTrip, "Trip updated successfully")))
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ApiResponseWrapper.error("Trip not found or not authorized", "TRIP_NOT_FOUND", null)));
    }

    @PatchMapping("/{tripId}")
    public ResponseEntity<ApiResponseWrapper<TripResponse>> partialUpdateTrip(
            @PathVariable String tripId,
            @RequestBody Map<String, Object> updates) {

        ObjectId objectId = new ObjectId(tripId);

        User currentUser = securityUtils.getCurrentUser();

        return tripPlanningService.partialUpdateTrip(objectId, updates, currentUser.getUserId())
                .map(updatedTrip -> ResponseEntity.ok(
                        ApiResponseWrapper.success(updatedTrip, "Trip updated successfully")))
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ApiResponseWrapper.error("Trip not found or not authorized", "TRIP_NOT_FOUND", null)));
    }


}
