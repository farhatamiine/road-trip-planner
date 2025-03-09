package com.amine.roadtripplanner.Controller;

import com.amine.roadtripplanner.Dto.request.TransportRequest;
import com.amine.roadtripplanner.Dto.response.ApiResponseWrapper;
import com.amine.roadtripplanner.Dto.response.TransportResponse;
import com.amine.roadtripplanner.Service.TransportService;
import com.amine.roadtripplanner.enums.TransportType;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.bson.types.ObjectId;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/transports")
@Tag(name = "Transport Management", description = "APIs for managing transport options")
public class TransportController {

    private final TransportService transportService;

    public TransportController(TransportService transportService) {
        this.transportService = transportService;
    }

    @Operation(summary = "Create a new transport option")
    @PostMapping
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<ApiResponseWrapper<TransportResponse>> createTransport(
            @Valid @RequestBody TransportRequest transportRequest) {

        TransportResponse createdTransport = transportService.createTransport(transportRequest);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponseWrapper.success(createdTransport, "Transport created successfully"));
    }

    @Operation(summary = "Get transport by ID")
    @GetMapping("/{transportId}")
    public ResponseEntity<ApiResponseWrapper<TransportResponse>> getTransportById(
            @PathVariable ObjectId transportId) {

        return transportService.getTransportById(transportId)
                .map(transportResponse -> ResponseEntity.ok(
                        ApiResponseWrapper.success(transportResponse, "Transport found")))
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ApiResponseWrapper.error(
                                "Transport not found",
                                "TRANSPORT_NOT_FOUND",
                                "No transport found with ID: " + transportId)));
    }

    @Operation(summary = "Get all transports")
    @GetMapping
    public ResponseEntity<ApiResponseWrapper<List<TransportResponse>>> getAllTransports() {
        List<TransportResponse> transports = transportService.getAllTransports();

        return ResponseEntity.ok(
                ApiResponseWrapper.success(transports, "Transports retrieved successfully"));
    }

    @Operation(summary = "Get all transport types")
    @GetMapping("/types")
    public ResponseEntity<ApiResponseWrapper<List<String>>> getTransportTypes() {
        List<String> types = Arrays.stream(TransportType.values())
                .map(Enum::name)
                .collect(Collectors.toList());

        return ResponseEntity.ok(
                ApiResponseWrapper.success(types, "Transport types retrieved successfully"));
    }

    @Operation(summary = "Get transports by type")
    @GetMapping("/type/{typeName}")
    public ResponseEntity<ApiResponseWrapper<List<TransportResponse>>> getTransportsByType(
            @PathVariable String typeName) {

        try {
            List<TransportResponse> transports = transportService.getTransportsByType(typeName);

            return ResponseEntity.ok(
                    ApiResponseWrapper.success(transports, "Transports found for type: " + typeName));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponseWrapper.error(
                            "Invalid transport type",
                            "INVALID_TRANSPORT_TYPE",
                            e.getMessage()));
        }
    }

    @Operation(summary = "Search transports by provider")
    @GetMapping("/search")
    public ResponseEntity<ApiResponseWrapper<List<TransportResponse>>> searchTransports(
            @RequestParam String provider) {

        List<TransportResponse> transports = transportService.searchTransportsByProvider(provider);

        return ResponseEntity.ok(
                ApiResponseWrapper.success(transports, "Transports found for provider: " + provider));
    }

    @Operation(summary = "Filter transports by price range")
    @GetMapping("/filter/price")
    public ResponseEntity<ApiResponseWrapper<List<TransportResponse>>> filterByPrice(
            @RequestParam Double minPrice,
            @RequestParam Double maxPrice) {

        List<TransportResponse> transports = transportService.filterByPriceRange(minPrice, maxPrice);

        return ResponseEntity.ok(
                ApiResponseWrapper.success(transports,
                        "Transports found within price range: " + minPrice + " - " + maxPrice));
    }

    @Operation(summary = "Find available transports for a time range")
    @GetMapping("/availability")
    public ResponseEntity<ApiResponseWrapper<List<TransportResponse>>> findAvailableTransports(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime departureTime,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime arrivalTime) {

        List<TransportResponse> transports = transportService.findAvailableTransports(departureTime, arrivalTime);

        return ResponseEntity.ok(
                ApiResponseWrapper.success(transports,
                        "Available transports found for the specified time range"));
    }

    @Operation(summary = "Update a transport")
    @PutMapping("/{transportId}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<ApiResponseWrapper<TransportResponse>> updateTransport(
            @PathVariable ObjectId transportId,
            @Valid @RequestBody TransportRequest transportRequest) {

        return transportService.updateTransport(transportId, transportRequest)
                .map(updatedTransport -> ResponseEntity.ok(
                        ApiResponseWrapper.success(updatedTransport, "Transport updated successfully")))
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ApiResponseWrapper.error(
                                "Transport not found",
                                "TRANSPORT_NOT_FOUND",
                                "No transport found with ID: " + transportId)));
    }

    @Operation(summary = "Partially update a transport")
    @PatchMapping("/{transportId}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<ApiResponseWrapper<TransportResponse>> partialUpdateTransport(
            @PathVariable ObjectId transportId,
            @RequestBody Map<String, Object> updates) {

        return transportService.partialUpdateTransport(transportId, updates)
                .map(updatedTransport -> ResponseEntity.ok(
                        ApiResponseWrapper.success(updatedTransport, "Transport updated successfully")))
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ApiResponseWrapper.error(
                                "Transport not found",
                                "TRANSPORT_NOT_FOUND",
                                "No transport found with ID: " + transportId)));
    }

    @Operation(summary = "Delete a transport")
    @DeleteMapping("/{transportId}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<ApiResponseWrapper<Void>> deleteTransport(@PathVariable ObjectId transportId) {
        boolean deleted = transportService.deleteTransport(transportId);

        if (deleted) {
            return ResponseEntity.ok(
                    ApiResponseWrapper.success(null, "Transport deleted successfully"));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponseWrapper.error(
                            "Transport not found",
                            "TRANSPORT_NOT_FOUND",
                            "No transport found with ID: " + transportId));
        }
    }
}