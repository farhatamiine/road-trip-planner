package com.amine.roadtripplanner.Entities;

import com.amine.roadtripplanner.enums.AccommodationType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.Version;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.Map;

@Document(collection = "accommodations")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Accommodation {
    @Id
    private ObjectId accommodationId;
    private String name;
    private String address;
    private AccommodationType type; // HOTEL, AIRBNB, HOSTEL, etc.
    private Double pricePerNight;
    private String bookingReference;
    private LocalDateTime checkInTime;
    private LocalDateTime checkOutTime;
    private Integer rating; // 1-5 stars
    private String contactInfo;
    private String websiteUrl;
    private Map<String, String> amenities; // e.g., "wifi": "Free", "parking": "Paid"
    private String notes;
    @CreatedDate
    private LocalDateTime createdAt;
    @LastModifiedDate
    private LocalDateTime updatedAt;
    @Version
    private Long version;
}

