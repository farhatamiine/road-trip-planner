package com.amine.roadtripplanner.Service;

import com.amine.roadtripplanner.Dto.request.TransportRequest;
import com.amine.roadtripplanner.Dto.response.TransportResponse;
import org.bson.types.ObjectId;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

//(managing transport options)
public interface TransportService {
    // Create a new transport
    TransportResponse createTransport(TransportRequest transportRequest);

    // Get a transport by ID
    Optional<TransportResponse> getTransportById(ObjectId transportId);

    // Get all transports
    List<TransportResponse> getAllTransports();

    // Get transports by type
    List<TransportResponse> getTransportsByType(String transportType);

    // Search transports by provider
    List<TransportResponse> searchTransportsByProvider(String providerQuery);

    // Filter transports by price range
    List<TransportResponse> filterByPriceRange(Double minPrice, Double maxPrice);

    // Find transports available for a given time range
    List<TransportResponse> findAvailableTransports(LocalDateTime departureTime,
                                                    LocalDateTime arrivalTime);

    // Update a transport
    Optional<TransportResponse> updateTransport(ObjectId transportId,
                                                TransportRequest transportRequest);

    // Partial update of a transport
    Optional<TransportResponse> partialUpdateTransport(ObjectId transportId,
                                                       Map<String, Object> updates);

    // Delete a transport
    boolean deleteTransport(ObjectId transportId);
}
