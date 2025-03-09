package com.amine.roadtripplanner.Controller;

import com.amine.roadtripplanner.Dto.request.AccommodationRequest;
import com.amine.roadtripplanner.Dto.response.AccommodationResponse;
import com.amine.roadtripplanner.Dto.response.ApiResponseWrapper;
import com.amine.roadtripplanner.Service.AccommodationService;
import com.amine.roadtripplanner.enums.AccommodationType;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.bson.types.ObjectId;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/accommodations")
@Tag(name = "Accommodation Management", description = "APIs for managing accommodations")
public class AccommodationController {

    private final AccommodationService accommodationService;

    public AccommodationController(AccommodationService accommodationService) {
        this.accommodationService = accommodationService;
    }

    @Operation(summary = "Create a new accommodation")
    @PostMapping
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<ApiResponseWrapper<AccommodationResponse>> createAccommodation(
            @Valid @RequestBody AccommodationRequest accommodationRequest) {

        AccommodationResponse createdAccommodation = accommodationService.createAccommodation(accommodationRequest);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponseWrapper.success(createdAccommodation, "Accommodation created successfully"));
    }

    @Operation(summary = "Get accommodation by ID")
    @GetMapping("/{accommodationId}")
    public ResponseEntity<ApiResponseWrapper<AccommodationResponse>> getAccommodationById(
            @PathVariable ObjectId accommodationId) {

        return accommodationService.getAccommodationById(accommodationId)
                .map(accommodationResponse -> ResponseEntity.ok(
                        ApiResponseWrapper.success(accommodationResponse, "Accommodation found")))
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ApiResponseWrapper.error(
                                "Accommodation not found",
                                "ACCOMMODATION_NOT_FOUND",
                                "No accommodation found with ID: " + accommodationId)));
    }

    @Operation(summary = "Get all accommodations")
    @GetMapping
    public ResponseEntity<ApiResponseWrapper<List<AccommodationResponse>>> getAllAccommodations() {
        List<AccommodationResponse> accommodations = accommodationService.getAllAccommodations();

        return ResponseEntity.ok(
                ApiResponseWrapper.success(accommodations, "Accommodations retrieved successfully"));
    }

    @Operation(summary = "Get all accommodation types")
    @GetMapping("/types")
    public ResponseEntity<ApiResponseWrapper<List<String>>> getAccommodationTypes() {
        List<String> types = Arrays.stream(AccommodationType.values())
                .map(Enum::name)
                .collect(Collectors.toList());

        return ResponseEntity.ok(
                ApiResponseWrapper.success(types, "Accommodation types retrieved successfully"));
    }

    @Operation(summary = "Get accommodations by type")
    @GetMapping("/type/{typeName}")
    public ResponseEntity<ApiResponseWrapper<List<AccommodationResponse>>> getAccommodationsByType(
            @PathVariable String typeName) {

        try {
            List<AccommodationResponse> accommodations = accommodationService.getAccommodationsByType(typeName);

            return ResponseEntity.ok(
                    ApiResponseWrapper.success(accommodations, "Accommodations found for type: " + typeName));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponseWrapper.error(
                            "Invalid accommodation type",
                            "INVALID_ACCOMMODATION_TYPE",
                            e.getMessage()));
        }
    }

    @Operation(summary = "Search accommodations by name")
    @GetMapping("/search")
    public ResponseEntity<ApiResponseWrapper<List<AccommodationResponse>>> searchAccommodations(
            @RequestParam String name) {

        List<AccommodationResponse> accommodations = accommodationService.searchAccommodationsByName(name);

        return ResponseEntity.ok(
                ApiResponseWrapper.success(accommodations, "Accommodations found for search: " + name));
    }

    @Operation(summary = "Filter accommodations by price range")
    @GetMapping("/filter/price")
    public ResponseEntity<ApiResponseWrapper<List<AccommodationResponse>>> filterByPrice(
            @RequestParam Double minPrice,
            @RequestParam Double maxPrice) {

        List<AccommodationResponse> accommodations = accommodationService.filterByPriceRange(minPrice, maxPrice);

        return ResponseEntity.ok(
                ApiResponseWrapper.success(accommodations,
                        "Accommodations found within price range: " + minPrice + " - " + maxPrice));
    }

    @Operation(summary = "Filter accommodations by minimum rating")
    @GetMapping("/filter/rating")
    public ResponseEntity<ApiResponseWrapper<List<AccommodationResponse>>> filterByRating(
            @RequestParam Integer minRating) {

        List<AccommodationResponse> accommodations = accommodationService.filterByRating(minRating);

        return ResponseEntity.ok(
                ApiResponseWrapper.success(accommodations,
                        "Accommodations found with rating >= " + minRating));
    }

    @Operation(summary = "Update an accommodation")
    @PutMapping("/{accommodationId}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<ApiResponseWrapper<AccommodationResponse>> updateAccommodation(
            @PathVariable ObjectId accommodationId,
            @Valid @RequestBody AccommodationRequest accommodationRequest) {

        return accommodationService.updateAccommodation(accommodationId, accommodationRequest)
                .map(updatedAccommodation -> ResponseEntity.ok(
                        ApiResponseWrapper.success(updatedAccommodation, "Accommodation updated successfully")))
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ApiResponseWrapper.error(
                                "Accommodation not found",
                                "ACCOMMODATION_NOT_FOUND",
                                "No accommodation found with ID: " + accommodationId)));
    }

    @Operation(summary = "Partially update an accommodation")
    @PatchMapping("/{accommodationId}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<ApiResponseWrapper<AccommodationResponse>> partialUpdateAccommodation(
            @PathVariable ObjectId accommodationId,
            @RequestBody Map<String, Object> updates) {

        return accommodationService.partialUpdateAccommodation(accommodationId, updates)
                .map(updatedAccommodation -> ResponseEntity.ok(
                        ApiResponseWrapper.success(updatedAccommodation, "Accommodation updated successfully")))
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ApiResponseWrapper.error(
                                "Accommodation not found",
                                "ACCOMMODATION_NOT_FOUND",
                                "No accommodation found with ID: " + accommodationId)));
    }

    @Operation(summary = "Delete an accommodation")
    @DeleteMapping("/{accommodationId}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<ApiResponseWrapper<Void>> deleteAccommodation(@PathVariable ObjectId accommodationId) {
        boolean deleted = accommodationService.deleteAccommodation(accommodationId);

        if (deleted) {
            return ResponseEntity.ok(
                    ApiResponseWrapper.success(null, "Accommodation deleted successfully"));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponseWrapper.error(
                            "Accommodation not found",
                            "ACCOMMODATION_NOT_FOUND",
                            "No accommodation found with ID: " + accommodationId));
        }
    }

    @Operation(summary = "Find accommodations with specific amenities")
    @GetMapping("/amenities")
    public ResponseEntity<ApiResponseWrapper<List<AccommodationResponse>>> findByAmenities(
            @RequestParam List<String> amenities) {

        List<AccommodationResponse> accommodations = accommodationService.findByAmenities(amenities);

        return ResponseEntity.ok(
                ApiResponseWrapper.success(accommodations,
                        "Accommodations found with specified amenities"));
    }
}