package com.amine.roadtripplanner.Repositories;

import com.amine.roadtripplanner.Entities.Location;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface LocationRepository extends MongoRepository<Location, ObjectId> {
}
