package com.amine.roadtripplanner.Controller;

import com.amine.roadtripplanner.Dto.request.InterestRequest;
import com.amine.roadtripplanner.Dto.response.ApiResponseWrapper;
import com.amine.roadtripplanner.Dto.response.InterestResponse;
import com.amine.roadtripplanner.Service.InterestService;
import com.amine.roadtripplanner.enums.InterestType;
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
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/interests")
@Tag(name = "Interest Management", description = "APIs for managing points of interest")
public class InterestController {

    private final InterestService interestService;

    public InterestController(InterestService interestService) {
        this.interestService = interestService;
    }

    @Operation(summary = "Create a new interest")
    @PostMapping
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<ApiResponseWrapper<InterestResponse>> createInterest(
            @Valid @RequestBody InterestRequest interestRequest) {

        InterestResponse createdInterest = interestService.createInterest(interestRequest);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponseWrapper.success(createdInterest, "Interest created successfully"));
    }

    @Operation(summary = "Get interest by ID")
    @GetMapping("/{interestId}")
    public ResponseEntity<ApiResponseWrapper<InterestResponse>> getInterestById(
            @PathVariable ObjectId interestId) {

        return interestService.getInterestById(interestId)
                .map(interestResponse -> ResponseEntity.ok(
                        ApiResponseWrapper.success(interestResponse, "Interest found")))
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ApiResponseWrapper.error(
                                "Interest not found",
                                "INTEREST_NOT_FOUND",
                                "No interest found with ID: " + interestId)));
    }

    @Operation(summary = "Get all interests")
    @GetMapping
    public ResponseEntity<ApiResponseWrapper<List<InterestResponse>>> getAllInterests() {
        List<InterestResponse> interests = interestService.getAllInterests();

        return ResponseEntity.ok(
                ApiResponseWrapper.success(interests, "Interests retrieved successfully"));
    }

    @Operation(summary = "Get all interest types")
    @GetMapping("/types")
    public ResponseEntity<ApiResponseWrapper<List<String>>> getInterestTypes() {
        List<String> types = Arrays.stream(InterestType.values())
                .map(Enum::name)
                .collect(Collectors.toList());

        return ResponseEntity.ok(
                ApiResponseWrapper.success(types, "Interest types retrieved successfully"));
    }

    @Operation(summary = "Get interests by type")
    @GetMapping("/type/{typeName}")
    public ResponseEntity<ApiResponseWrapper<List<InterestResponse>>> getInterestsByType(
            @PathVariable String typeName) {

        try {
            List<InterestResponse> interests = interestService.getInterestsByType(typeName);

            return ResponseEntity.ok(
                    ApiResponseWrapper.success(interests, "Interests found for type: " + typeName));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponseWrapper.error(
                            "Invalid interest type",
                            "INVALID_INTEREST_TYPE",
                            e.getMessage()));
        }
    }

    @Operation(summary = "Search interests by name")
    @GetMapping("/search")
    public ResponseEntity<ApiResponseWrapper<List<InterestResponse>>> searchInterests(
            @RequestParam String name) {

        List<InterestResponse> interests = interestService.searchInterestsByName(name);

        return ResponseEntity.ok(
                ApiResponseWrapper.success(interests, "Interests found for search: " + name));
    }

    @Operation(summary = "Update an interest")
    @PutMapping("/{interestId}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<ApiResponseWrapper<InterestResponse>> updateInterest(
            @PathVariable ObjectId interestId,
            @Valid @RequestBody InterestRequest interestRequest) {

        return interestService.updateInterest(interestId, interestRequest)
                .map(updatedInterest -> ResponseEntity.ok(
                        ApiResponseWrapper.success(updatedInterest, "Interest updated successfully")))
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ApiResponseWrapper.error(
                                "Interest not found",
                                "INTEREST_NOT_FOUND",
                                "No interest found with ID: " + interestId)));
    }

    @Operation(summary = "Delete an interest")
    @DeleteMapping("/{interestId}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<ApiResponseWrapper<Void>> deleteInterest(@PathVariable ObjectId interestId) {
        boolean deleted = interestService.deleteInterest(interestId);

        if (deleted) {
            return ResponseEntity.ok(
                    ApiResponseWrapper.success(null, "Interest deleted successfully"));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponseWrapper.error(
                            "Interest not found",
                            "INTEREST_NOT_FOUND",
                            "No interest found with ID: " + interestId));
        }
    }

    @Operation(summary = "Get popular interests")
    @GetMapping("/popular")
    public ResponseEntity<ApiResponseWrapper<List<InterestResponse>>> getPopularInterests(
            @RequestParam(defaultValue = "10") int limit) {

        List<InterestResponse> interests = interestService.getPopularInterests(limit);

        return ResponseEntity.ok(
                ApiResponseWrapper.success(interests, "Popular interests retrieved successfully"));
    }

    @Operation(summary = "Get must-visit interests")
    @GetMapping("/must-visit")
    public ResponseEntity<ApiResponseWrapper<List<InterestResponse>>> getMustVisitInterests() {
        List<InterestResponse> interests = interestService.getMustVisitInterests();

        return ResponseEntity.ok(
                ApiResponseWrapper.success(interests, "Must-visit interests retrieved successfully"));
    }
}