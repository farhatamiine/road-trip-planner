package com.amine.roadtripplanner.Repositories;

import com.amine.roadtripplanner.Entities.Segment;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface SegmentRepository extends MongoRepository<Segment, ObjectId> {
}
