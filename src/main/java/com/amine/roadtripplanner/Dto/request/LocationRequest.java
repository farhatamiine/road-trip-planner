package com.amine.roadtripplanner.Dto.request;

import com.amine.roadtripplanner.Entities.Location;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class LocationRequest {

    @NotBlank(message = "City name cannot be empty")
    private String cityName;

    @NotBlank(message = "Country cannot be empty")
    @Pattern(regexp = "^[A-Za-z\\s-]+$", message = "Country must contain only letters, spaces, and hyphens")
    private String country;

    private Double latitude;

    private Double longitude;

    /**
     * Converts a LocationRequest DTO to a Location entity.
     * This method is used when creating a new Location.
     *
     * @return a new Location entity with data from this request
     */
    public static Location convertToEntity(LocationRequest request) {
        return Location.builder()
                .cityName(request.getCityName())
                .country(request.getCountry())
                .latitude(request.getLatitude())
                .longitude(request.getLongitude())
                .build();
    }



}
