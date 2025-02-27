package com.amine.roadtripplanner.Repositories;

import com.amine.roadtripplanner.Entities.Accommodation;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface AccommodationRepository extends MongoRepository<Accommodation, ObjectId> {
}
