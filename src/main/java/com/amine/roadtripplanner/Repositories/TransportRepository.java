package com.amine.roadtripplanner.Repositories;

import com.amine.roadtripplanner.Entities.Transport;
import com.amine.roadtripplanner.enums.TransportType;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TransportRepository extends MongoRepository<Transport, ObjectId> {

    // Find by type
    List<Transport> findByType(TransportType type);

    // Find by provider (case-insensitive)
    @Query("{ 'provider': { $regex: ?0, $options: 'i' } }")
    List<Transport> findByProviderContainingIgnoreCase(String provider);

    // Find by price range
    List<Transport> findByPriceBetween(Double minPrice, Double maxPrice);

    // Find available transports for a given time range
    List<Transport> findByDepartureTimeAfterAndArrivalTimeBefore(
            LocalDateTime departureTime, LocalDateTime arrivalTime);

    // Find transports by departure time range
    List<Transport> findByDepartureTimeBetween(
            LocalDateTime startTime, LocalDateTime endTime);

    // Find transports by arrival time range
    List<Transport> findByArrivalTimeBetween(
            LocalDateTime startTime, LocalDateTime endTime);

    // Find cheapest transport options by type
    List<Transport> findByTypeOrderByPriceAsc(TransportType type);

    // Find transports by booking reference
    Transport findByBookingReference(String bookingReference);

    // Count transports by type
    Long countByType(TransportType type);
}