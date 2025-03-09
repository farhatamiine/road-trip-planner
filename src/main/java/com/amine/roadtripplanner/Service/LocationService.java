package com.amine.roadtripplanner.Service;

import com.amine.roadtripplanner.Dto.request.LocationRequest;
import com.amine.roadtripplanner.Dto.response.LocationResponse;
import org.bson.types.ObjectId;

import java.util.List;
import java.util.Optional;

//(managing locations, points of interest)
public interface LocationService {
    // Create a new location
    LocationResponse createLocation(LocationRequest locationRequest);

    // Get a location by ID
    Optional<LocationResponse> getLocationById(ObjectId locationId);

    // Get locations by criteria
    List<LocationResponse> getLocationsByCountry(String country);

    // Search locations by city name (partial match)
    List<LocationResponse> searchLocationsByCityName(String cityNameQuery);

    // Update a location
    Optional<LocationResponse> updateLocation(ObjectId locationId, LocationRequest locationRequest);

    // Delete a location
    boolean deleteLocation(ObjectId locationId);

    // Add an accommodation to a location
    boolean addAccommodationToLocation(ObjectId locationId, ObjectId accommodationId);

    // Add an interest to a location
    boolean addInterestToLocation(ObjectId locationId, ObjectId interestId);

    // Remove an accommodation from a location
    boolean removeAccommodationFromLocation(ObjectId locationId, ObjectId accommodationId);

    // Remove an interest from a location
    boolean removeInterestFromLocation(ObjectId locationId, ObjectId interestId);

    // Get all locations
    List<LocationResponse> getAllLocations();
}
