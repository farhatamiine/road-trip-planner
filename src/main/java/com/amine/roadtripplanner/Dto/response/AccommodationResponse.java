package com.amine.roadtripplanner.Dto.response;

import com.amine.roadtripplanner.Entities.Accommodation;
import com.amine.roadtripplanner.enums.AccommodationType;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Data
@Builder
public class AccommodationResponse {
    private String accommodationId;
    private String name;
    private String address;
    private AccommodationType type;
    private Double pricePerNight;
    private String bookingReference;
    private LocalDateTime checkInTime;
    private LocalDateTime checkOutTime;
    private Integer rating;
    private String contactInfo;
    private String websiteUrl;
    private Map<String, String> amenities;
    private String notes;

    /**
     * Converts an Accommodation entity to an AccommodationResponse DTO.
     *
     * @param accommodation the Accommodation entity to convert
     * @return an AccommodationResponse DTO
     */
    public static AccommodationResponse fromAccommodation(Accommodation accommodation) {
        return AccommodationResponse.builder()
                .accommodationId(accommodation.getAccommodationId() != null ?
                        accommodation.getAccommodationId().toString() : null)
                .name(accommodation.getName())
                .address(accommodation.getAddress())
                .type(accommodation.getType())
                .pricePerNight(accommodation.getPricePerNight())
                .bookingReference(accommodation.getBookingReference())
                .checkInTime(accommodation.getCheckInTime())
                .checkOutTime(accommodation.getCheckOutTime())
                .rating(accommodation.getRating())
                .contactInfo(accommodation.getContactInfo())
                .websiteUrl(accommodation.getWebsiteUrl())
                .amenities(accommodation.getAmenities())
                .notes(accommodation.getNotes())
                .build();
    }

    /**
     * Converts a list of Accommodation entities to a list of AccommodationResponse DTOs.
     *
     * @param accommodations the list of Accommodation entities to convert
     * @return a list of AccommodationResponse DTOs
     */
    public static List<AccommodationResponse> fromAccommodationList(List<Accommodation> accommodations) {
        return accommodations.stream()
                .map(AccommodationResponse::fromAccommodation)
                .collect(Collectors.toList());
    }
}