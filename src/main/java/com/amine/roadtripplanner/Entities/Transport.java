package com.amine.roadtripplanner.Entities;

import com.amine.roadtripplanner.enums.TransportType;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "transports")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Transport {
    @Id
    private ObjectId transportId;
    @NotNull
    private TransportType type; // TRAIN, PLANE, BUS, etc.
    private String provider;
    private String bookingReference;
    @NotNull
    private Double price;
    @NotNull
    private LocalDateTime departureTime;
    @NotNull
    private LocalDateTime arrivalTime;
}
