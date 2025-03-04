package com.amine.roadtripplanner.Dto.response;

import com.amine.roadtripplanner.Entities.Transport;
import com.amine.roadtripplanner.enums.TransportType;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
public class TransportResponse {
    private String transportId;
    private TransportType type;
    private String provider;
    private String bookingReference;
    private Double price;
    private LocalDateTime departureTime;
    private LocalDateTime arrivalTime;

    /**
     * Converts a Transport entity to a TransportResponse DTO.
     *
     * @param transport the Transport entity to convert
     * @return a TransportResponse DTO
     */
    public static TransportResponse fromTransport(Transport transport) {
        if (transport == null) {
            return null;
        }

        return TransportResponse.builder()
                .transportId(transport.getTransportId() != null ?
                        transport.getTransportId().toString() : null)
                .type(transport.getType())
                .provider(transport.getProvider())
                .bookingReference(transport.getBookingReference())
                .price(transport.getPrice())
                .departureTime(transport.getDepartureTime())
                .arrivalTime(transport.getArrivalTime())
                .build();
    }

    /**
     * Converts a list of Transport entities to a list of TransportResponse DTOs.
     *
     * @param transports the list of Transport entities to convert
     * @return a list of TransportResponse DTOs
     */
    public static List<TransportResponse> fromTransportList(List<Transport> transports) {
        return transports.stream()
                .map(TransportResponse::fromTransport)
                .collect(Collectors.toList());
    }
}