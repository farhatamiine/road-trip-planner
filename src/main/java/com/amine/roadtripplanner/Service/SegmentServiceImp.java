package com.amine.roadtripplanner.Service;

import com.amine.roadtripplanner.Dto.request.SegmentRequest;
import com.amine.roadtripplanner.Dto.response.SegmentResponse;
import com.amine.roadtripplanner.Entities.Segment;
import com.amine.roadtripplanner.Entities.Trip;
import com.amine.roadtripplanner.Entities.User;
import com.amine.roadtripplanner.Exception.ResourceNotFoundException;
import com.amine.roadtripplanner.Repositories.*;
import com.amine.roadtripplanner.Security.SecurityUtils;
import org.bson.types.ObjectId;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SegmentServiceImp implements SegmentService {


    private final SegmentRepository segmentRepository;
    private final UserRepository userRepository;
    private final TransportRepository transportRepository;
    private final LocationRepository locationRepository;
    private final TripRepository tripRepository;

    private final SecurityUtils securityUtils;

    public SegmentServiceImp(SegmentRepository segmentRepository, UserRepository userRepository, TransportRepository transportRepository, LocationRepository locationRepository, TripRepository tripRepository, SecurityUtils securityUtils) {
        this.segmentRepository = segmentRepository;
        this.userRepository = userRepository;
        this.transportRepository = transportRepository;
        this.locationRepository = locationRepository;
        this.tripRepository = tripRepository;
        this.securityUtils = securityUtils;
    }

    @Transactional
    @Override
    public SegmentResponse createSegment(ObjectId tripId,SegmentRequest segmentRequest) {
        User user = securityUtils.getCurrentUser();
        Trip trip = tripRepository.findById(tripId)
                .orElseThrow(() -> new ResourceNotFoundException("Trip not found with id: " + tripId));

        // Verify ownership (additional security check)
        if (!userRepository.existsByUserIdAndTripListContaining(user.getUserId(), trip)) {
            throw new AccessDeniedException("You don't have permission to modify this trip");
        }
        validateSegmentReferences(segmentRequest);

        // Convert and save
        Segment segment = SegmentRequest.convertToEntity(segmentRequest,locationRepository,transportRepository);
        Segment savedSegment = segmentRepository.save(segment);

        // Add to trip and update
        trip.getSegmentList().add(savedSegment);
        tripRepository.save(trip);

        return SegmentResponse.fromSegment(savedSegment);
    }


    private void validateSegmentReferences(SegmentRequest segmentRequest) {
        // Validate that start location exists if ID provided
        if (segmentRequest.getStartLocationId() != null) {
            locationRepository.findById(segmentRequest.getStartLocationId())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Start location not found with id: " + segmentRequest.getStartLocationId()));
        }

        // Validate that end location exists if ID provided
        if (segmentRequest.getEndLocationId() != null) {
            locationRepository.findById(segmentRequest.getEndLocationId() )
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "End location not found with id: " + segmentRequest.getEndLocationId() ));
        }

        // Validate that transport exists if ID provided
        if (segmentRequest.getTransportId() != null) {
            transportRepository.findById(segmentRequest.getTransportId())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Transport not found with id: " + segmentRequest.getTransportId()));
        }

        // Additional validation for segment data
        if (segmentRequest.getStartTime() != null && segmentRequest.getEndTime() != null) {
            if (segmentRequest.getStartTime().isAfter(segmentRequest.getEndTime())) {
                throw new IllegalArgumentException(
                        "Start time must be before end time");
            }
        }
    }

    @Override
    public Optional<SegmentResponse> getSegmentById(ObjectId segmentId) {
        return Optional.empty();
    }


    @Override
    public Optional<SegmentResponse> updateSegment(ObjectId segmentId, SegmentRequest segmentRequest) {
        return Optional.empty();
    }


    @Override
    public boolean deleteSegment(ObjectId segmentId) {
        return false;
    }


    @Override
    public List<SegmentResponse> getSegmentsByTripId(ObjectId tripId) {
        return List.of();
    }


//    @Override
//    public boolean reorderSegments(ObjectId tripId, List<SegmentOrderRequest> orderRequests) {
//        return false;
//    }


    @Override
    public boolean addSegmentToTrip(ObjectId tripId, ObjectId segmentId) {
        return false;
    }


    @Override
    public boolean removeSegmentFromTrip(ObjectId tripId, ObjectId segmentId) {
        return false;
    }


//    @Override
//    public RouteStatistics calculateRouteStatistics(ObjectId tripId) {
//        return null;
//    }
}
