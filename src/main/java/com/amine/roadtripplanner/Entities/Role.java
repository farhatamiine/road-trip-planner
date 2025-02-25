package com.amine.roadtripplanner.Entities;

import lombok.Getter;
import lombok.Setter;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@Getter
@Setter
public class Role {
    @Id
    private ObjectId id;
    private String roleName;
}
