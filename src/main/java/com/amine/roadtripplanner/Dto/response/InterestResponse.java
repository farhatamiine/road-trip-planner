package com.amine.roadtripplanner.Dto.response;

import com.amine.roadtripplanner.Entities.Interest;
import com.amine.roadtripplanner.enums.InterestType;
import lombok.Builder;
import lombok.Data;

import java.time.DayOfWeek;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Data
@Builder
public class InterestResponse {
    private String interestId;
    private String name;
    private String description;
    private InterestType type;
    private Double entryFee;
    private Integer visitDurationMinutes;
    private BusinessHoursResponse businessHours;
    private Boolean requiresTickets;
    private String ticketingUrl;
    private Integer popularityRanking;
    private String notes;
    private Boolean mustVisit;

    /**
     * Converts an Interest entity to an InterestResponse DTO.
     *
     * @param interest the Interest entity to convert
     * @return an InterestResponse DTO
     */
    public static InterestResponse fromInterest(Interest interest) {
        return InterestResponse.builder()
                .interestId(interest.getInterestId() != null ?
                        interest.getInterestId().toString() : null)
                .name(interest.getName())
                .description(interest.getDescription())
                .type(interest.getType())
                .entryFee(interest.getEntryFee())
                .visitDurationMinutes(interest.getVisitDurationMinutes())
                .businessHours(BusinessHoursResponse.fromBusinessHours(interest.getBusinessHours()))
                .requiresTickets(interest.getRequiresTickets())
                .ticketingUrl(interest.getTicketingUrl())
                .popularityRanking(interest.getPopularityRanking())
                .notes(interest.getNotes())
                .mustVisit(interest.getMustVisit())
                .build();
    }

    /**
     * Converts a list of Interest entities to a list of InterestResponse DTOs.
     *
     * @param interests the list of Interest entities to convert
     * @return a list of InterestResponse DTOs
     */
    public static List<InterestResponse> fromInterestList(List<Interest> interests) {
        return interests.stream()
                .map(InterestResponse::fromInterest)
                .collect(Collectors.toList());
    }

    @Data
    @Builder
    public static class BusinessHoursResponse {
        private Map<DayOfWeek, String> schedule;
        private String holidayHours;
        private List<String> closedDates;

        /**
         * Converts Interest.BusinessHours entity to BusinessHoursResponse DTO.
         *
         * @param businessHours the BusinessHours entity to convert
         * @return a BusinessHoursResponse DTO
         */
        public static BusinessHoursResponse fromBusinessHours(Interest.BusinessHours businessHours) {
            if (businessHours == null) {
                return null;
            }

            return BusinessHoursResponse.builder()
                    .schedule(businessHours.getSchedule())
                    .holidayHours(businessHours.getHolidayHours())
                    .closedDates(businessHours.getClosedDates())
                    .build();
        }
    }
}