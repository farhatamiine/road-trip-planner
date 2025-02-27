package com.amine.roadtripplanner.Repositories;

import com.amine.roadtripplanner.Entities.Interest;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface InterestRepository extends MongoRepository<Interest, ObjectId> {
}
