package com.amine.roadtripplanner.Dto.request;

import com.amine.roadtripplanner.Entities.Transport;
import com.amine.roadtripplanner.enums.TransportType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransportRequest {

    @NotNull(message = "Transport type cannot be null")
    private TransportType type;

    private String provider;

    private String bookingReference;

    @NotNull(message = "Price cannot be null")
    @Min(value = 0, message = "Price must be non-negative")
    private Double price;

    @NotNull(message = "Departure time cannot be null")
    private LocalDateTime departureTime;

    @NotNull(message = "Arrival time cannot be null")
    private LocalDateTime arrivalTime;

    /**
     * Converts a TransportRequest DTO to a Transport entity.
     * This method is used when creating a new Transport.
     *
     * @return a new Transport entity with data from this request
     */
    public static Transport convertToEntity(TransportRequest request) {
        return Transport.builder()
                .type(request.getType())
                .provider(request.getProvider())
                .bookingReference(request.getBookingReference())
                .price(request.getPrice())
                .departureTime(request.getDepartureTime())
                .arrivalTime(request.getArrivalTime())
                .build();
    }
}