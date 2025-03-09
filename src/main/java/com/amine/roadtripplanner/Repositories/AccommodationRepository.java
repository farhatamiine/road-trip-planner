package com.amine.roadtripplanner.Repositories;

import com.amine.roadtripplanner.Entities.Accommodation;
import com.amine.roadtripplanner.enums.AccommodationType;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface AccommodationRepository extends MongoRepository<Accommodation, ObjectId> {
    // Find by type
    List<Accommodation> findByType(AccommodationType type);

    // Find by name containing (case-insensitive)
    @Query("{ 'name': { $regex: ?0, $options: 'i' } }")
    List<Accommodation> findByNameContainingIgnoreCase(String name);

    // Find by price range
    List<Accommodation> findByPricePerNightBetween(Double minPrice, Double maxPrice);

    // Find by minimum rating
    List<Accommodation> findByRatingGreaterThanEqual(Integer minRating);

    // Find available accommodations for given date range
    @Query("{ $and: [ " +
            "{ $or: [ { 'checkOutTime': { $exists: false } }, { 'checkOutTime': null }, { 'checkOutTime': { $lt: ?0 } } ] }, " +
            "{ $or: [ { 'checkInTime': { $exists: false } }, { 'checkInTime': null }, { 'checkInTime': { $gt: ?1 } } ] } " +
            "] }")
    List<Accommodation> findAvailableBetween(LocalDateTime startDate, LocalDateTime endDate);

    // Find by specific amenity
    @Query("{ 'amenities.?0': { $exists: true } }")
    List<Accommodation> findByAmenity(String amenityKey);

    // Find accommodations with rating and sorted by price
    List<Accommodation> findByRatingGreaterThanEqualOrderByPricePerNightAsc(Integer minRating);
}
