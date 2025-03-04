package com.amine.roadtripplanner.Service;

import com.amine.roadtripplanner.Dto.response.TripResponse;
import jakarta.validation.constraints.NotNull;
import org.bson.types.ObjectId;

import java.util.List;
import java.util.Optional;

//(planning trips, managing segments)
public interface TripPlanningService {
    Optional<List<TripResponse>> findUserTrips(@NotNull ObjectId userId);
}
