package com.amine.roadtripplanner.Dto.request;

import com.amine.roadtripplanner.Entities.Location;
import com.amine.roadtripplanner.Entities.Segment;
import com.amine.roadtripplanner.Entities.Transport;
import com.amine.roadtripplanner.Entities.Trip;
import com.amine.roadtripplanner.Exception.ResourceNotFoundException;
import com.amine.roadtripplanner.Repositories.LocationRepository;
import com.amine.roadtripplanner.Repositories.TransportRepository;
import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.mapping.DBRef;

import java.time.LocalDateTime;

@Builder
@Data
public class SegmentRequest {

    @NotNull(message = "Start location ID is required")
    private ObjectId startLocationId;

    @NotNull(message = "End location ID is required")
    private ObjectId endLocationId;

    private ObjectId transportId; // Optional if some segments don't use transport

    @NotNull(message = "Start time is required")
    @FutureOrPresent(message = "Start time must not be in the past")
    private LocalDateTime startTime;

    @NotNull(message = "End time is required")
    @Future(message = "End time must be in the future")
    private LocalDateTime endTime;

    @Min(value = 0, message = "Order must be at least 0")
    @Max(value = 10, message = "Order cannot be greater than 10")
    private Integer order;


    public static Segment convertToEntity(SegmentRequest request,
                                          LocationRepository locationRepo,
                                          TransportRepository transportRepo) {
        // Fetch entities from repositories
        Location startLocation = locationRepo.findById(request.getStartLocationId())
                .orElseThrow(() -> new ResourceNotFoundException("Location", request.getStartLocationId().toString()));

        Location endLocation = locationRepo.findById(request.getEndLocationId())
                .orElseThrow(() -> new ResourceNotFoundException("Location", request.getEndLocationId().toString()));

        // Transport might be optional
        Transport transport = null;
        if (request.getTransportId() != null) {
            transport = transportRepo.findById(request.getTransportId())
                    .orElseThrow(() -> new ResourceNotFoundException("Transport", request.getTransportId().toString()));
        }

        return Segment.builder()
                .startLocation(startLocation)
                .endLocation(endLocation)
                .transport(transport)
                .startTime(request.getStartTime())
                .endTime(request.getEndTime())
                .order(request.getOrder())
                .build();
    }



}
