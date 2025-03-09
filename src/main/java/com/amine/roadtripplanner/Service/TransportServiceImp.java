package com.amine.roadtripplanner.Service;

import com.amine.roadtripplanner.Dto.request.TransportRequest;
import com.amine.roadtripplanner.Dto.response.TransportResponse;
import com.amine.roadtripplanner.Entities.Transport;
import com.amine.roadtripplanner.Repositories.TransportRepository;
import com.amine.roadtripplanner.enums.TransportType;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class TransportServiceImpl implements TransportService {

    private final TransportRepository transportRepository;
    private final MongoTemplate mongoTemplate;

    public TransportServiceImpl(TransportRepository transportRepository,
                                MongoTemplate mongoTemplate) {
        this.transportRepository = transportRepository;
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    @Transactional
    public TransportResponse createTransport(TransportRequest transportRequest) {
        // Convert request to entity
        Transport transport = TransportRequest.convertToEntity(transportRequest);

        // Save transport
        Transport savedTransport = transportRepository.save(transport);

        return TransportResponse.fromTransport(savedTransport);
    }

    @Override
    public Optional<TransportResponse> getTransportById(ObjectId transportId) {
        return transportRepository.findById(transportId)
                .map(TransportResponse::fromTransport);
    }

    @Override
    public List<TransportResponse> getAllTransports() {
        List<Transport> transports = transportRepository.findAll();
        return TransportResponse.fromTransportList(transports);
    }

    @Override
    public List<TransportResponse> getTransportsByType(String transportType) {
        try {
            TransportType type = TransportType.valueOf(transportType.toUpperCase());
            List<Transport> transports = transportRepository.findByType(type);
            return TransportResponse.fromTransportList(transports);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid transport type: " + transportType);
        }
    }

    @Override
    public List<TransportResponse> searchTransportsByProvider(String providerQuery) {
        // Using MongoTemplate for more flexible queries
        Query query = new Query();
        query.addCriteria(Criteria.where("provider").regex(providerQuery, "i"));

        List<Transport> transports = mongoTemplate.find(query, Transport.class);
        return TransportResponse.fromTransportList(transports);
    }

    @Override
    public List<TransportResponse> filterByPriceRange(Double minPrice, Double maxPrice) {
        List<Transport> transports = transportRepository
                .findByPriceBetween(minPrice, maxPrice);
        return TransportResponse.fromTransportList(transports);
    }

    @Override
    public List<TransportResponse> findAvailableTransports(LocalDateTime departureTime,
                                                           LocalDateTime arrivalTime) {
        List<Transport> transports = transportRepository
                .findByDepartureTimeAfterAndArrivalTimeBefore(departureTime, arrivalTime);
        return TransportResponse.fromTransportList(transports);
    }

    @Override
    @Transactional
    public Optional<TransportResponse> updateTransport(ObjectId transportId,
                                                       TransportRequest transportRequest) {
        return transportRepository.findById(transportId)
                .map(existingTransport -> {
                    // Update fields
                    existingTransport.setType(transportRequest.getType());
                    existingTransport.setProvider(transportRequest.getProvider());
                    existingTransport.setBookingReference(transportRequest.getBookingReference());
                    existingTransport.setPrice(transportRequest.getPrice());
                    existingTransport.setDepartureTime(transportRequest.getDepartureTime());
                    existingTransport.setArrivalTime(transportRequest.getArrivalTime());

                    // Save updated transport
                    Transport savedTransport = transportRepository.save(existingTransport);
                    return TransportResponse.fromTransport(savedTransport);
                });
    }

    @Override
    @Transactional
    public Optional<TransportResponse> partialUpdateTransport(ObjectId transportId,
                                                              Map<String, Object> updates) {
        return transportRepository.findById(transportId)
                .map(existingTransport -> {
                    // Apply updates for each field provided
                    if (updates.containsKey("type")) {
                        try {
                            TransportType type = TransportType
                                    .valueOf(((String) updates.get("type")).toUpperCase());
                            existingTransport.setType(type);
                        } catch (IllegalArgumentException e) {
                            throw new IllegalArgumentException("Invalid transport type");
                        }
                    }
                    if (updates.containsKey("provider")) {
                        existingTransport.setProvider((String) updates.get("provider"));
                    }
                    if (updates.containsKey("bookingReference")) {
                        existingTransport.setBookingReference((String) updates.get("bookingReference"));
                    }
                    if (updates.containsKey("price")) {
                        existingTransport.setPrice((Double) updates.get("price"));
                    }

                    // Handle datetime fields - might need string conversion depending on how updates are passed

                    // Save updated transport
                    Transport savedTransport = transportRepository.save(existingTransport);
                    return TransportResponse.fromTransport(savedTransport);
                });
    }

    @Override
    @Transactional
    public boolean deleteTransport(ObjectId transportId) {
        return transportRepository.findById(transportId)
                .map(transport -> {
                    // Check if the transport is referenced by any segments before deletion
                    transportRepository.delete(transport);
                    return true;
                })
                .orElse(false);
    }
}