package com.amine.roadtripplanner.Dto.response;

import com.amine.roadtripplanner.Entities.Segment;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class SegmentResponse {

    private String segmentId;
    private LocationResponse startLocation;
    private LocationResponse endLocation;
    private TransportResponse transport;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Integer order;
    private LocalDateTime createdAt;

    public static SegmentResponse fromSegment(Segment segment) {
        return SegmentResponse.builder()
                .segmentId(segment.getSegmentId() != null ? segment.getSegmentId().toString() : null)
                .startLocation(segment.getStartLocation() != null ?
                        LocationResponse.fromLocation(segment.getStartLocation()) : null)
                .endLocation(segment.getEndLocation() != null ?
                        LocationResponse.fromLocation(segment.getEndLocation()) : null)
                .transport(segment.getTransport() != null ?
                        TransportResponse.fromTransport(segment.getTransport()) : null)
                .startTime(segment.getStartTime())
                .endTime(segment.getEndTime())
                .order(segment.getOrder())
                .build();
    }
}
