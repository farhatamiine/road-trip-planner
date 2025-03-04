package com.amine.roadtripplanner.Service;

import com.amine.roadtripplanner.Dto.response.TripResponse;
import com.amine.roadtripplanner.Entities.Trip;
import com.amine.roadtripplanner.Entities.User;
import com.amine.roadtripplanner.Repositories.SegmentRepository;
import com.amine.roadtripplanner.Repositories.TripRepository;
import com.amine.roadtripplanner.Repositories.UserRepository;
import org.bson.types.ObjectId;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
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

    /**
     * @param trip
     * @return
     */
    @Override
    @Transactional
    public Trip saveNewTrip(Trip trip) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) authentication.getPrincipal();
        Trip savedTrip = tripRepository.save(trip);
        currentUser.getTripList().add(savedTrip);
        userRepository.save(currentUser);
        return savedTrip;
    }

    /**
     * @param tripId
     * @param userId
     * @return
     */
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

}
