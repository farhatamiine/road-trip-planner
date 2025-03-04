package com.amine.roadtripplanner.Repositories;

import com.amine.roadtripplanner.Entities.Trip;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TripRepository extends MongoRepository<Trip, ObjectId> {
    //List<TripResponse> findTripByUserId(ObjectId userId);
}
