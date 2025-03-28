package com.amine.roadtripplanner.Repositories;

import com.amine.roadtripplanner.Entities.Role;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends MongoRepository<Role, ObjectId> {

    Optional<Role> findRoleByName(String name);
}
