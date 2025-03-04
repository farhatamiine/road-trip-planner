package com.amine.roadtripplanner.Entities;

import com.amine.roadtripplanner.enums.TripStatus;
import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Document(collection = "trips")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Trip {
    @Id
    private ObjectId tripId;
    @NonNull
    private String tripName;
    @NonNull
    private String tripDescription;
    @NonNull
    private LocalDate tripDate;
    private TripStatus tripStatus;
    @DBRef(lazy = true)
    private List<Segment> segmentList = new ArrayList<>();
}

