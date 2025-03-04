package com.amine.roadtripplanner.Service;

import com.amine.roadtripplanner.Dto.response.TripResponse;
import com.amine.roadtripplanner.Entities.Trip;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.bson.types.ObjectId;

import java.util.List;
import java.util.Optional;

//(planning trips, managing segments)
public interface TripPlanningService {
    Optional<List<TripResponse>> findUserTrips(@NotNull ObjectId userId);

    Trip saveNewTrip(@Valid Trip trip);

    boolean deleteTrip(@NotNull ObjectId tripId, ObjectId userId);

}
