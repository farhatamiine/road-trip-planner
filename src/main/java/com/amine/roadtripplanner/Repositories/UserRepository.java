package com.amine.roadtripplanner.Repositories;

import com.amine.roadtripplanner.Entities.Trip;
import com.amine.roadtripplanner.Entities.User;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends MongoRepository<User, ObjectId> {
    Optional<User> findByUsername(String username);

    Boolean existsByUsername(String username);

    Boolean existsByEmail(String email);

    Optional<User> findUserByEmail(String email);

    boolean existsByUserIdAndTripListContaining(ObjectId userId, Trip trip);
}
