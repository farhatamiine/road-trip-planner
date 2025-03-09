package com.amine.roadtripplanner.Entities;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Document(collection = "locations")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Location {
    @Id
    private ObjectId locationId;

    @NotBlank(message = "City name cannot be empty")
    private String cityName;
    @Indexed(unique = true)
    private String country;

    @DBRef(lazy = true)

    private List<Accommodation> accommodations;
    @DBRef(lazy = true)

    private List<Interest> interests;

    private Double latitude;
    private Double longitude;
    @CreatedDate
    private LocalDateTime createdAt;
    @LastModifiedDate
    private LocalDateTime updatedAt;
    @Version
    private Long version;
}