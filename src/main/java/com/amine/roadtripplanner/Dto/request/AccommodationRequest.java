package com.amine.roadtripplanner.Dto.request;

import com.amine.roadtripplanner.Entities.Accommodation;
import com.amine.roadtripplanner.enums.AccommodationType;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccommodationRequest {

    @NotBlank(message = "Name cannot be empty")
    private String name;

    @NotBlank(message = "Address cannot be empty")
    private String address;

    @NotNull(message = "Accommodation type cannot be null")
    private AccommodationType type;

    @NotNull(message = "Price per night cannot be null")
    @Min(value = 0, message = "Price per night must be non-negative")
    private Double pricePerNight;

    private String bookingReference;

    private LocalDateTime checkInTime;

    private LocalDateTime checkOutTime;

    @Min(value = 1, message = "Rating must be at least 1")
    @Max(value = 5, message = "Rating cannot be greater than 5")
    private Integer rating;

    private String contactInfo;

    private String websiteUrl;

    private Map<String, String> amenities;

    private String notes;

    /**
     * Converts an AccommodationRequest DTO to an Accommodation entity.
     * This method is used when creating a new Accommodation.
     *
     * @return a new Accommodation entity with data from this request
     */
    public static Accommodation convertToEntity(AccommodationRequest request) {
        return Accommodation.builder()
                .name(request.getName())
                .address(request.getAddress())
                .type(request.getType())
                .pricePerNight(request.getPricePerNight())
                .bookingReference(request.getBookingReference())
                .checkInTime(request.getCheckInTime())
                .checkOutTime(request.getCheckOutTime())
                .rating(request.getRating())
                .contactInfo(request.getContactInfo())
                .websiteUrl(request.getWebsiteUrl())
                .amenities(request.getAmenities())
                .notes(request.getNotes())
                .build();
    }
}