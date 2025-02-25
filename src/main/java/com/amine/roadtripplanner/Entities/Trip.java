package com.amine.roadtripplanner.Entities;

import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="trips")
@Data
public class Trip {
    @Id
    private ObjectId id;
    private String tripName;
}
