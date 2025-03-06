package com.amine.roadtripplanner.Service;

import com.amine.roadtripplanner.Dto.request.TripRequest;
import com.amine.roadtripplanner.Dto.response.TripResponse;
import com.amine.roadtripplanner.Entities.Trip;
import com.amine.roadtripplanner.Entities.User;
import com.amine.roadtripplanner.Repositories.SegmentRepository;
import com.amine.roadtripplanner.Repositories.TripRepository;
import com.amine.roadtripplanner.Repositories.UserRepository;
import com.amine.roadtripplanner.enums.TripStatus;
import org.bson.types.ObjectId;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class TripPlanningServiceImp implements TripPlanningService {
    private final UserRepository userRepository;
    private final TripRepository tripRepository;
    private final SegmentRepository segmentRepository;

    public TripPlanningServiceImp(UserRepository userRepository, TripRepository tripRepository, TripRepository tripRepository1, SegmentRepository segmentRepository) {
        this.userRepository = userRepository;
        this.tripRepository = tripRepository1;
        this.segmentRepository = segmentRepository;
    }

    @Override
    public Optional<List<TripResponse>> findUserTrips(ObjectId userId) {
        Optional<User> userOptional = userRepository.findById(userId);

        // If user exists, access their trip list directly from the user entity
        return userOptional.map(user -> {
            List<Trip> trips = user.getTripList();
            return TripResponse.fromTripList(trips);
        });
    }


    @Override
    @Transactional
    public Trip saveNewTrip(TripRequest trip) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) authentication.getPrincipal();
        trip.setTripStatus(TripStatus.PLANNED);
        Trip savedTrip = tripRepository.save(TripRequest.convertToEntity(trip));
        currentUser.getTripList().add(savedTrip);
        userRepository.save(currentUser);
        return savedTrip;
    }


    @Override
    public boolean deleteTrip(ObjectId tripId, ObjectId userId) {
        Optional<Trip> tripOptional = tripRepository.findById(tripId);
        if (tripOptional.isEmpty()) {
            return false;
        }
        Trip trip = tripOptional.get();

        // Find the user who owns this trip
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isEmpty()) {
            return false;
        }

        User user = userOptional.get();

        // Check if trip belongs to this user (security check)
        if (!user.getTripList().contains(trip)) {
            return false;
        }
        user.getTripList().removeIf(t -> t.getTripId().equals(tripId));
        userRepository.save(user);
        // Remove segments but keep locations and related entities
        if (trip.getSegmentList() != null && !trip.getSegmentList().isEmpty()) {
            // Clear the references but don't delete the actual locations/transports
            trip.getSegmentList().forEach(segment -> {
                // Detach the segment from related entities
                segment.setStartLocation(null);
                segment.setEndLocation(null);
                segment.setTransport(null);

                // Save updated segment
                segmentRepository.save(segment);
            });
        }
        // Now delete the trip and its segments
        tripRepository.delete(trip);
        return true;
    }


    @Override
    public Optional<TripResponse> getTripById(ObjectId tripId) {
        return tripRepository.findById(tripId).map(TripResponse::fromTrip);
    }

    @Override
    @Transactional
    public Optional<TripResponse> updateTrip(TripRequest tripRequest, ObjectId tripId, ObjectId userId) {

        // First verify the trip exists and belongs to the user
        Optional<Trip> existingTripOpt = tripRepository.findById(tripId);
        if (existingTripOpt.isEmpty()) {
            return Optional.empty();
        }


        Trip existingTrip = existingTripOpt.get();

        // Check if user owns this trip
        // More efficient way to check ownership - use a query instead of loading all trips
        boolean tripBelongsToUser = userRepository.existsByUserIdAndTripListContaining(userId, existingTrip);
        if (!tripBelongsToUser) {
            return Optional.empty();
        }

        // Update existing entity properties
        existingTrip.setTripName(tripRequest.getTripName());
        existingTrip.setTripDescription(tripRequest.getTripDescription());
        existingTrip.setTripDate(tripRequest.getTripDate());
        existingTrip.setTripStatus(tripRequest.getTripStatus());

        // Save the updated entity
        Trip saved = tripRepository.save(existingTrip);

        return Optional.of(TripResponse.fromTrip(saved));
    }

    @Override
    @Transactional
    public Optional<TripResponse> partialUpdateTrip(ObjectId tripId, Map<String, Object> updates, ObjectId userId) {
        Optional<Trip> existingTripOpt = tripRepository.findById(tripId);

        if (existingTripOpt.isEmpty()) {
            return Optional.empty();
        }

        Trip existingTrip = existingTripOpt.get();

        // Check if user owns this trip
        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isEmpty() || userOpt.get().getTripList().stream()
                .noneMatch(t -> t.getTripId().equals(tripId))) {
            return Optional.empty();
        }

        // Apply updates
        if (updates.containsKey("tripName")) {
            existingTrip.setTripName((String) updates.get("tripName"));
        }

        if (updates.containsKey("tripDescription")) {
            existingTrip.setTripDescription((String) updates.get("tripDescription"));
        }

        if (updates.containsKey("tripDate")) {
            // Convert String to LocalDate
            String dateStr = (String) updates.get("tripDate");
            existingTrip.setTripDate(LocalDate.parse(dateStr));
        }

        if (updates.containsKey("tripStatus")) {
            String statusStr = (String) updates.get("tripStatus");
            try {
                TripStatus status = TripStatus.valueOf(statusStr);
                existingTrip.setTripStatus(status);
            } catch (IllegalArgumentException e) {
                // Invalid status, ignore this update
            }
        }

        // Save updated trip
        Trip saved = tripRepository.save(existingTrip);

        return Optional.of(TripResponse.fromTrip(saved));
    }

}
