package com.amine.roadtripplanner.Service;


import com.amine.roadtripplanner.Dto.request.SegmentRequest;
import com.amine.roadtripplanner.Dto.response.SegmentResponse;
import com.amine.roadtripplanner.Entities.Segment;
import org.bson.types.ObjectId;

import java.util.List;
import java.util.Optional;

public interface SegmentService {
    SegmentResponse createSegment(ObjectId tripId,SegmentRequest segmentRequest);

    // Get segment by ID
    Optional<SegmentResponse> getSegmentById(ObjectId segmentId);

    // Update segment
    Optional<SegmentResponse> updateSegment(ObjectId segmentId, SegmentRequest segmentRequest);

    // Delete segment
    boolean deleteSegment(ObjectId segmentId);

    // Get segments by trip ID
    List<SegmentResponse> getSegmentsByTripId(ObjectId tripId);

    // Reorder segments within a trip
    //boolean reorderSegments(ObjectId tripId, List<SegmentOrderRequest> orderRequests);

    // Add segment to a trip
    boolean addSegmentToTrip(ObjectId tripId, ObjectId segmentId);

    // Remove segment from a trip
    boolean removeSegmentFromTrip(ObjectId tripId, ObjectId segmentId);

    // Calculate route statistics (total distance, duration, etc.)
   // RouteStatistics calculateRouteStatistics(ObjectId tripId);
}
