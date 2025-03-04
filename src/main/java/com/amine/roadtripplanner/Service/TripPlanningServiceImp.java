package com.amine.roadtripplanner.Service;

import com.amine.roadtripplanner.Dto.response.TripResponse;
import com.amine.roadtripplanner.Entities.Trip;
import com.amine.roadtripplanner.Entities.User;
import com.amine.roadtripplanner.Repositories.TripRepository;
import com.amine.roadtripplanner.Repositories.UserRepository;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TripPlanningServiceImp implements TripPlanningService {
    private final UserRepository userRepository;

    public TripPlanningServiceImp(UserRepository userRepository, TripRepository tripRepository) {
        this.userRepository = userRepository;
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
}
