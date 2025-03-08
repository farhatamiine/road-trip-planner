package com.amine.roadtripplanner.Controller;

import com.amine.roadtripplanner.Dto.request.SegmentRequest;
import com.amine.roadtripplanner.Dto.response.ApiResponseWrapper;
import com.amine.roadtripplanner.Dto.response.SegmentResponse;
import com.amine.roadtripplanner.Service.SegmentService;
import jakarta.validation.Valid;
import org.bson.types.ObjectId;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/segments")
public class SegmentController {

    private final SegmentService segmentService;

    public SegmentController(SegmentService segmentService) {
        this.segmentService = segmentService;
    }


    // Create a new segment
    @PostMapping("{tripId}")
    public ResponseEntity<ApiResponseWrapper<SegmentResponse>> createSegment(@PathVariable ObjectId tripId,@Valid @RequestBody SegmentRequest request) {
        SegmentResponse segmentResponse = segmentService.createSegment(tripId,request);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponseWrapper.success(segmentResponse, "Segment created successfully"));
    }

//    // Get segment by ID
//    @GetMapping("/{segmentId}")
//    public ResponseEntity<ApiResponseWrapper<SegmentResponse>> getSegmentById(@PathVariable ObjectId segmentId) {
//    }
//
//    // Update segment
//    @PutMapping("/{segmentId}")
//    public ResponseEntity<ApiResponseWrapper<SegmentResponse>> updateSegment(
//            @PathVariable ObjectId segmentId,
//            @Valid @RequestBody SegmentRequest request) {
//    }
//
//    // Delete segment
//    @DeleteMapping("/{segmentId}")
//    public ResponseEntity<ApiResponseWrapper<Void>> deleteSegment(@PathVariable ObjectId segmentId) {
//    }
//
//    // Get segments by trip ID
//    @GetMapping("/trip/{tripId}")
//    public ResponseEntity<ApiResponseWrapper<List<SegmentResponse>>> getSegmentsByTripId(@PathVariable ObjectId tripId) {
//
//    }

    // Reorder segments within a trip
//    @PutMapping("/trip/{tripId}/reorder")
//    public ResponseEntity<ApiResponseWrapper<Void>> reorderSegments(
//            @PathVariable ObjectId tripId,
//            @Valid @RequestBody List<SegmentOrderRequest> orderRequests){}
//
//    // Get route statistics
//    @GetMapping("/trip/{tripId}/statistics")
//    public ResponseEntity<ApiResponseWrapper<RouteStatistics>> getRouteStatistics(@PathVariable ObjectId tripId){}

}
