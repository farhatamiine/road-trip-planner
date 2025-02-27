package com.amine.roadtripplanner.Repositories;

import com.amine.roadtripplanner.Entities.Transport;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface TransportRepository extends MongoRepository<Transport, ObjectId> {
}
