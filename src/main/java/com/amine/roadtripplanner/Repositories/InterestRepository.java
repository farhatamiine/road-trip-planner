package com.amine.roadtripplanner.Repositories;

import com.amine.roadtripplanner.Entities.Interest;
import com.amine.roadtripplanner.enums.InterestType;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface InterestRepository extends MongoRepository<Interest, ObjectId> {
    // Find by type
    List<Interest> findByType(InterestType type);

    // Find by must-visit flag
    List<Interest> findByMustVisitTrue();

    // Find by price range
    List<Interest> findByEntryFeeBetween(Double minFee, Double maxFee);

    // Find by visit duration (less than or equal to provided minutes)
    List<Interest> findByVisitDurationMinutesLessThanEqual(Integer maxDuration);

    // Find by name containing (case-insensitive)
    @Query("{ 'name': { $regex: ?0, $options: 'i' } }")
    List<Interest> findByNameContainingIgnoreCase(String name);

    // Find by popularity ranking ordered
    List<Interest> findAllByOrderByPopularityRankingAsc(Pageable pageable);

    // Find free attractions (entry fee is 0 or null)
    @Query("{ $or: [ { 'entryFee': 0 }, { 'entryFee': { $exists: false } }, { 'entryFee': null } ] }")
    List<Interest> findFreeAttractions();
}
