package com.amine.roadtripplanner.Dto.response;


import com.amine.roadtripplanner.Entities.Trip;
import com.amine.roadtripplanner.enums.TripStatus;
import lombok.Builder;
import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.DBRef;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
public class TripResponse {
    private ObjectId tripId;
    private String tripName;
    private String tripDescription;
    private LocalDate tripDate;
    private TripStatus tripStatus;
    @DBRef(lazy = true)
    private List<SegmentResponse> segmentList;


    /**
     * Converts a list of Trip entities to a list of TripResponse DTOs.
     * This method provides a convenient way to transform multiple trip entities
     * at once, which is useful for endpoints that return collections of trips.
     *
     * @param trips the list of Trip entities to convert
     * @return a list of TripResponse DTOs
     */
    public static List<TripResponse> fromTripList(List<Trip> trips) {
        return trips.stream()
                .map(TripResponse::fromTrip)
                .collect(Collectors.toList());
    }

    public static TripResponse fromTrip(Trip trip) {
        // Convert segmentList to list of SegmentResponse DTOs if segments exist
        List<SegmentResponse> segmentResponses = trip.getSegmentList() != null ?
                trip.getSegmentList().stream()
                        .map(SegmentResponse::fromSegment)
                        .collect(Collectors.toList()) :
                null;

        return TripResponse.builder()
                .tripId(trip.getTripId() != null ? trip.getTripId() : null)
                .tripName(trip.getTripName())
                .tripDescription(trip.getTripDescription())
                .tripDate(trip.getTripDate())
                .tripStatus(trip.getTripStatus())
                .segmentList(segmentResponses)
                .build();
    }
}
