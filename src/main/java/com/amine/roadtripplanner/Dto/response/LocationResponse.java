package com.amine.roadtripplanner.Dto.response;

import com.amine.roadtripplanner.Entities.Location;
import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Builder
@Data
public class LocationResponse {
    private String locationId; // Changed from ObjectId to String for client compatibility
    private String cityName;
    private String country;
    private List<AccommodationResponse> accommodations;
    private List<InterestResponse> interests;
    private Double latitude;
    private Double longitude;

    /**
     * Converts a Location entity to a LocationResponse DTO.
     * This method handles the conversion of nested collections like accommodations and interests.
     *
     * @param location the Location entity to convert
     * @return a LocationResponse DTO with all relevant location information
     */
    public static LocationResponse fromLocation(Location location) {
        if (location == null) {
            return null;
        }

        // Convert accommodations to response DTOs if they exist
        List<AccommodationResponse> accommodationResponses = location.getAccommodations() != null ?
                AccommodationResponse.fromAccommodationList(location.getAccommodations()) :
                new ArrayList<>();

        // Convert interests to response DTOs if they exist
        List<InterestResponse> interestResponses = location.getInterests() != null ?
                InterestResponse.fromInterestList(location.getInterests()) :
                new ArrayList<>();

        return LocationResponse.builder()
                .locationId(location.getLocationId() != null ?
                        location.getLocationId().toString() : null)
                .cityName(location.getCityName())
                .country(location.getCountry())
                .latitude(location.getLatitude())
                .longitude(location.getLongitude())
                .accommodations(accommodationResponses)
                .interests(interestResponses)
                .build();
    }

    /**
     * Converts a list of Location entities to a list of LocationResponse DTOs.
     *
     * @param locations the list of Location entities to convert
     * @return a list of LocationResponse DTOs
     */
    public static List<LocationResponse> fromLocationList(List<Location> locations) {
        return locations.stream()
                .map(LocationResponse::fromLocation)
                .collect(Collectors.toList());
    }
}