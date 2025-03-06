package com.amine.roadtripplanner.Dto.request;

import com.amine.roadtripplanner.Entities.Trip;
import com.amine.roadtripplanner.enums.TripStatus;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class TripRequest {

    @NotBlank(message = "Trip name is required")
    @Size(min = 3, max = 100, message = "Trip name must be between 3 and 100 characters")
    private String tripName;

    @NotBlank(message = "Trip description is required")
    @Size(min = 10, max = 500, message = "Trip description must be between 10 and 500 characters")
    private String tripDescription;

    @NotNull(message = "Trip date is required")
    @Future(message = "Trip date must be in the future")
    private LocalDate tripDate;

    private TripStatus tripStatus = TripStatus.PLANNED;


    public static Trip convertToEntity(TripRequest tripRequest) {
        return Trip.builder()
                .tripDate(tripRequest.getTripDate())
                .tripDescription(tripRequest.getTripDescription())
                .tripName(tripRequest.getTripName())
                .tripStatus(tripRequest.getTripStatus())
                .build();
    }
}
