package com.amine.roadtripplanner.Repositories;

import com.amine.roadtripplanner.Entities.Location;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface LocationRepository extends MongoRepository<Location, ObjectId> {
    // Find by country
    List<Location> findByCountry(String country);

    // Find by city name (exact match)
    List<Location> findByCityName(String cityName);

    // Find by city name (case-insensitive, contains)
    @Query("{ 'cityName': { $regex: ?0, $options: 'i' } }")
    List<Location> findByCityNameContainingIgnoreCase(String cityName);

    // Find locations within a certain distance of coordinates
    @Query("{ 'coordinates': { $near: { $geometry: { type: 'Point', coordinates: [ ?0, ?1 ] }, $maxDistance: ?2 } } }")
    List<Location> findNearby(Double longitude, Double latitude, Double maxDistanceMeters);

    // Find locations with specific amenities
    List<Location> findByAccommodationsAmenitiesContaining(String amenity);
}
