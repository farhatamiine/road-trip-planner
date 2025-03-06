package com.amine.roadtripplanner.Service;

import com.amine.roadtripplanner.Dto.request.TripRequest;
import com.amine.roadtripplanner.Dto.response.TripResponse;
import com.amine.roadtripplanner.Entities.Trip;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.bson.types.ObjectId;

import java.util.List;
import java.util.Map;
import java.util.Optional;

//(planning trips, managing segments)
public interface TripPlanningService {
    Optional<List<TripResponse>> findUserTrips(@NotNull ObjectId userId);

    Trip saveNewTrip(@Valid TripRequest trip);

    boolean deleteTrip(@NotNull ObjectId tripId, ObjectId userId);

    Optional<TripResponse> getTripById(@NotNull ObjectId tripId);

    Optional<TripResponse> updateTrip(TripRequest updatedTrip, ObjectId tripId, ObjectId userId);

    Optional<TripResponse> partialUpdateTrip(ObjectId tripId, Map<String, Object> updates, ObjectId userId);
}
