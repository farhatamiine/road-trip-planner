package com.amine.roadtripplanner.Controller;

import com.amine.roadtripplanner.Dto.request.LocationRequest;
import com.amine.roadtripplanner.Dto.response.ApiResponseWrapper;
import com.amine.roadtripplanner.Dto.response.LocationResponse;
import com.amine.roadtripplanner.Exception.ResourceNotFoundException;
import com.amine.roadtripplanner.Service.LocationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.bson.types.ObjectId;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/location")
public class LocationController {

    private final LocationService locationService;

    public LocationController(LocationService locationService) {
        this.locationService = locationService;
    }

    @Operation(summary = "Create a new location")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Location created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request data"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden")
    })
    @PostMapping
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponseWrapper<LocationResponse>> createLocation(
            @Valid @RequestBody LocationRequest locationRequest) {

        LocationResponse createdLocation = locationService.createLocation(locationRequest);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponseWrapper.success(createdLocation, "Location created successfully"));
    }

    @Operation(summary = "Get location by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Location found"),
            @ApiResponse(responseCode = "404", description = "Location not found"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @GetMapping("/{locationId}")
    public ResponseEntity<ApiResponseWrapper<LocationResponse>> getLocationById(
            @PathVariable ObjectId locationId) {

        return locationService.getLocationById(locationId)
                .map(locationResponse -> ResponseEntity.ok(
                        ApiResponseWrapper.success(locationResponse, "Location found")))
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ApiResponseWrapper.error(
                                "Location not found",
                                "LOCATION_NOT_FOUND",
                                "No location found with ID: " + locationId)));
    }

    @Operation(summary = "Get all locations")
    @GetMapping
    public ResponseEntity<ApiResponseWrapper<List<LocationResponse>>> getAllLocations() {
        List<LocationResponse> locations = locationService.getAllLocations();

        return ResponseEntity.ok(
                ApiResponseWrapper.success(locations, "Locations retrieved successfully"));
    }

    @Operation(summary = "Search locations by city name")
    @GetMapping("/search")
    public ResponseEntity<ApiResponseWrapper<List<LocationResponse>>> searchLocations(
            @RequestParam String cityName) {

        List<LocationResponse> locations = locationService.searchLocationsByCityName(cityName);

        return ResponseEntity.ok(
                ApiResponseWrapper.success(locations, "Locations found for search: " + cityName));
    }

    @Operation(summary = "Get locations by country")
    @GetMapping("/country/{country}")
    public ResponseEntity<ApiResponseWrapper<List<LocationResponse>>> getLocationsByCountry(
            @PathVariable String country) {

        List<LocationResponse> locations = locationService.getLocationsByCountry(country);

        return ResponseEntity.ok(
                ApiResponseWrapper.success(locations, "Locations found for country: " + country));
    }

    @Operation(summary = "Update a location")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Location updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request data"),
            @ApiResponse(responseCode = "404", description = "Location not found"),
            @ApiResponse(responseCode = "403", description = "Forbidden")
    })
    @PutMapping("/{locationId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponseWrapper<LocationResponse>> updateLocation(
            @PathVariable ObjectId locationId,
            @Valid @RequestBody LocationRequest locationRequest) {

        return locationService.updateLocation(locationId, locationRequest)
                .map(updatedLocation -> ResponseEntity.ok(
                        ApiResponseWrapper.success(updatedLocation, "Location updated successfully")))
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ApiResponseWrapper.error(
                                "Location not found",
                                "LOCATION_NOT_FOUND",
                                "No location found with ID: " + locationId)));
    }

    @Operation(summary = "Delete a location")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Location deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Location not found"),
            @ApiResponse(responseCode = "403", description = "Forbidden")
    })
    @DeleteMapping("/{locationId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponseWrapper<Void>> deleteLocation(@PathVariable ObjectId locationId) {
        boolean deleted = locationService.deleteLocation(locationId);

        if (deleted) {
            return ResponseEntity.ok(
                    ApiResponseWrapper.success(null, "Location deleted successfully"));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponseWrapper.error(
                            "Location not found",
                            "LOCATION_NOT_FOUND",
                            "No location found with ID: " + locationId));
        }
    }

    @Operation(summary = "Add accommodation to location")
    @PostMapping("/{locationId}/accommodations/{accommodationId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponseWrapper<Void>> addAccommodationToLocation(
            @PathVariable ObjectId locationId,
            @PathVariable ObjectId accommodationId) {

        try {
            boolean added = locationService.addAccommodationToLocation(locationId, accommodationId);
            return ResponseEntity.ok(
                    ApiResponseWrapper.success(null, "Accommodation added to location successfully"));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponseWrapper.error(
                            "Resource not found",
                            e.getResourceType() + "_NOT_FOUND",
                            e.getMessage()));
        }
    }

    @Operation(summary = "Add interest to location")
    @PostMapping("/{locationId}/interests/{interestId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponseWrapper<Void>> addInterestToLocation(
            @PathVariable ObjectId locationId,
            @PathVariable ObjectId interestId) {

        try {
            boolean added = locationService.addInterestToLocation(locationId, interestId);
            return ResponseEntity.ok(
                    ApiResponseWrapper.success(null, "Interest added to location successfully"));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponseWrapper.error(
                            "Resource not found",
                            e.getResourceType() + "_NOT_FOUND",
                            e.getMessage()));
        }
    }

    @Operation(summary = "Remove accommodation from location")
    @DeleteMapping("/{locationId}/accommodations/{accommodationId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponseWrapper<Void>> removeAccommodationFromLocation(
            @PathVariable ObjectId locationId,
            @PathVariable ObjectId accommodationId) {

        try {
            boolean removed = locationService.removeAccommodationFromLocation(locationId, accommodationId);

            if (removed) {
                return ResponseEntity.ok(
                        ApiResponseWrapper.success(null, "Accommodation removed from location successfully"));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ApiResponseWrapper.error(
                                "Accommodation not found in location",
                                "ACCOMMODATION_NOT_FOUND",
                                "No accommodation with ID " + accommodationId + " found in location " + locationId));
            }
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponseWrapper.error(
                            "Resource not found",
                            e.getResourceType() + "_NOT_FOUND",
                            e.getMessage()));
        }
    }

    @Operation(summary = "Remove interest from location")
    @DeleteMapping("/{locationId}/interests/{interestId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponseWrapper<Void>> removeInterestFromLocation(
            @PathVariable ObjectId locationId,
            @PathVariable ObjectId interestId) {

        try {
            boolean removed = locationService.removeInterestFromLocation(locationId, interestId);

            if (removed) {
                return ResponseEntity.ok(
                        ApiResponseWrapper.success(null, "Interest removed from location successfully"));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ApiResponseWrapper.error(
                                "Interest not found in location",
                                "INTEREST_NOT_FOUND",
                                "No interest with ID " + interestId + " found in location " + locationId));
            }
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponseWrapper.error(
                            "Resource not found",
                            e.getResourceType() + "_NOT_FOUND",
                            e.getMessage()));
        }
    }
}
