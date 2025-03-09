package com.amine.roadtripplanner.Dto.request;

import com.amine.roadtripplanner.Entities.Interest;
import com.amine.roadtripplanner.enums.InterestType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.DayOfWeek;
import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InterestRequest {

    @NotBlank(message = "Name cannot be empty")
    private String name;

    private String description;

    @NotNull(message = "Interest type cannot be null")
    private InterestType type;

    private Double entryFee;

    @Min(value = 1, message = "Visit duration must be at least 1 minute")
    private Integer visitDurationMinutes;

    private Interest.BusinessHours businessHours;

    private Boolean requiresTickets;

    private String ticketingUrl;

    private Integer popularityRanking;

    private String notes;

    private Boolean mustVisit;

    /**
     * Converts an InterestRequest DTO to an Interest entity.
     * This method is used when creating a new Interest.
     *
     * @return a new Interest entity with data from this request
     */
    public static Interest convertToEntity(InterestRequest request) {
        return Interest.builder()
                .name(request.getName())
                .description(request.getDescription())
                .type(request.getType())
                .entryFee(request.getEntryFee())
                .visitDurationMinutes(request.getVisitDurationMinutes())
                .businessHours(request.getBusinessHours())
                .requiresTickets(request.getRequiresTickets())
                .ticketingUrl(request.getTicketingUrl())
                .popularityRanking(request.getPopularityRanking())
                .notes(request.getNotes())
                .mustVisit(request.getMustVisit())
                .build();
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BusinessHoursRequest {
        private Map<DayOfWeek, String> schedule;
        private String holidayHours;
        private List<String> closedDates;

        public static Interest.BusinessHours convertToEntity(BusinessHoursRequest request) {
            if (request == null) {
                return null;
            }

            Interest.BusinessHours businessHours = new Interest.BusinessHours();
            businessHours.setSchedule(request.getSchedule());
            businessHours.setHolidayHours(request.getHolidayHours());
            businessHours.setClosedDates(request.getClosedDates());

            return businessHours;
        }
    }
}