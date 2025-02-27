package com.amine.roadtripplanner.Entities;

import com.amine.roadtripplanner.Util.ValidDateRange;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "segments")
@Data
@ValidDateRange
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Segment {
    @Id
    private ObjectId segmentId;

    @DBRef
    private Location startLocation;
    @DBRef
    private Location endLocation;

    @DBRef
    private Transport transport;

    @NotNull
    private LocalDateTime startTime;

    @NotNull
    private LocalDateTime endTime;

    @Min(value = 0, message = "Order must be at least 0")
    @Max(value = 10, message = "Order cannot be greater than 100")
    private Integer order;

    @CreatedDate
    private LocalDateTime createdAt;
    @LastModifiedDate
    private LocalDateTime updatedAt;
    @Version
    private Long version;
}
