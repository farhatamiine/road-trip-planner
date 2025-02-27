package com.amine.roadtripplanner.Entities;

import com.amine.roadtripplanner.enums.InterestType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Document(collection = "interests")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Interest {
    @Id
    private ObjectId interestId;
    private String name;
    private String description;
    private InterestType type; // MUSEUM, RESTAURANT, LANDMARK, etc.
    private Double entryFee;
    private Integer visitDurationMinutes;
    private BusinessHours businessHours;
    private Boolean requiresTickets;
    private String ticketingUrl;
    private Integer popularityRanking; // e.g., TripAdvisor ranking
    private String notes;
    private Boolean mustVisit; // Flag for must-see attractions
    @CreatedDate
    private LocalDateTime createdAt;
    @LastModifiedDate
    private LocalDateTime updatedAt;
    @Version
    private Long version;

    @Data
    public static class BusinessHours {
        private Map<DayOfWeek, String> schedule; // e.g., "MONDAY": "9AM-5PM"
        private String holidayHours;
        private List<String> closedDates; // Special closing dates
    }

}


