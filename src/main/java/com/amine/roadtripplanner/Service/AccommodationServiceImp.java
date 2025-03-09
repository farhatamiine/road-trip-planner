package com.amine.roadtripplanner.Service;

import com.amine.roadtripplanner.Dto.request.AccommodationRequest;
import com.amine.roadtripplanner.Dto.response.AccommodationResponse;
import com.amine.roadtripplanner.Entities.Accommodation;
import com.amine.roadtripplanner.Repositories.AccommodationRepository;
import com.amine.roadtripplanner.enums.AccommodationType;
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
public class AccommodationServiceImp implements AccommodationService {
    private final AccommodationRepository accommodationRepository;
    private final MongoTemplate mongoTemplate;

    public AccommodationServiceImp(AccommodationRepository accommodationRepository,
                                   MongoTemplate mongoTemplate) {
        this.accommodationRepository = accommodationRepository;
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    @Transactional
    public AccommodationResponse createAccommodation(AccommodationRequest accommodationRequest) {
        // Convert request to entity
        Accommodation accommodation = AccommodationRequest.convertToEntity(accommodationRequest);

        // Set audit fields
        LocalDateTime now = LocalDateTime.now();
        accommodation.setCreatedAt(now);
        accommodation.setUpdatedAt(now);

        // Save accommodation
        Accommodation savedAccommodation = accommodationRepository.save(accommodation);

        return AccommodationResponse.fromAccommodation(savedAccommodation);
    }

    @Override
    public Optional<AccommodationResponse> getAccommodationById(ObjectId accommodationId) {
        return accommodationRepository.findById(accommodationId)
                .map(AccommodationResponse::fromAccommodation);
    }

    @Override
    public List<AccommodationResponse> getAllAccommodations() {
        List<Accommodation> accommodations = accommodationRepository.findAll();
        return AccommodationResponse.fromAccommodationList(accommodations);
    }

    @Override
    public List<AccommodationResponse> getAccommodationsByType(String accommodationType) {
        try {
            AccommodationType type = AccommodationType.valueOf(accommodationType.toUpperCase());
            List<Accommodation> accommodations = accommodationRepository.findByType(type);
            return AccommodationResponse.fromAccommodationList(accommodations);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid accommodation type: " + accommodationType);
        }
    }

    @Override
    public List<AccommodationResponse> searchAccommodationsByName(String nameQuery) {
        // Using MongoTemplate for more flexible queries
        Query query = new Query();
        query.addCriteria(Criteria.where("name").regex(nameQuery, "i"));

        List<Accommodation> accommodations = mongoTemplate.find(query, Accommodation.class);
        return AccommodationResponse.fromAccommodationList(accommodations);
    }

    @Override
    public List<AccommodationResponse> filterByPriceRange(Double minPrice, Double maxPrice) {
        List<Accommodation> accommodations = accommodationRepository
                .findByPricePerNightBetween(minPrice, maxPrice);
        return AccommodationResponse.fromAccommodationList(accommodations);
    }

    @Override
    public List<AccommodationResponse> filterByRating(Integer minRating) {
        List<Accommodation> accommodations = accommodationRepository
                .findByRatingGreaterThanEqual(minRating);
        return AccommodationResponse.fromAccommodationList(accommodations);
    }

    @Override
    @Transactional
    public Optional<AccommodationResponse> updateAccommodation(ObjectId accommodationId,
                                                               AccommodationRequest accommodationRequest) {
        return accommodationRepository.findById(accommodationId)
                .map(existingAccommodation -> {
                    // Update fields
                    existingAccommodation.setName(accommodationRequest.getName());
                    existingAccommodation.setAddress(accommodationRequest.getAddress());
                    existingAccommodation.setType(accommodationRequest.getType());
                    existingAccommodation.setPricePerNight(accommodationRequest.getPricePerNight());
                    existingAccommodation.setBookingReference(accommodationRequest.getBookingReference());
                    existingAccommodation.setCheckInTime(accommodationRequest.getCheckInTime());
                    existingAccommodation.setCheckOutTime(accommodationRequest.getCheckOutTime());
                    existingAccommodation.setRating(accommodationRequest.getRating());
                    existingAccommodation.setContactInfo(accommodationRequest.getContactInfo());
                    existingAccommodation.setWebsiteUrl(accommodationRequest.getWebsiteUrl());
                    existingAccommodation.setAmenities(accommodationRequest.getAmenities());
                    existingAccommodation.setNotes(accommodationRequest.getNotes());

                    // Update audit field
                    existingAccommodation.setUpdatedAt(LocalDateTime.now());

                    // Save updated accommodation
                    Accommodation savedAccommodation = accommodationRepository.save(existingAccommodation);
                    return AccommodationResponse.fromAccommodation(savedAccommodation);
                });
    }

    @Override
    @Transactional
    public Optional<AccommodationResponse> partialUpdateAccommodation(ObjectId accommodationId,
                                                                      Map<String, Object> updates) {
        return accommodationRepository.findById(accommodationId)
                .map(existingAccommodation -> {
                    // Apply updates for each field provided
                    if (updates.containsKey("name")) {
                        existingAccommodation.setName((String) updates.get("name"));
                    }
                    if (updates.containsKey("address")) {
                        existingAccommodation.setAddress((String) updates.get("address"));
                    }
                    if (updates.containsKey("type")) {
                        try {
                            AccommodationType type = AccommodationType
                                    .valueOf(((String) updates.get("type")).toUpperCase());
                            existingAccommodation.setType(type);
                        } catch (IllegalArgumentException e) {
                            throw new IllegalArgumentException("Invalid accommodation type");
                        }
                    }
                    if (updates.containsKey("pricePerNight")) {
                        existingAccommodation.setPricePerNight((Double) updates.get("pricePerNight"));
                    }
                    if (updates.containsKey("bookingReference")) {
                        existingAccommodation.setBookingReference((String) updates.get("bookingReference"));
                    }
                    if (updates.containsKey("rating")) {
                        existingAccommodation.setRating((Integer) updates.get("rating"));
                    }
                    if (updates.containsKey("contactInfo")) {
                        existingAccommodation.setContactInfo((String) updates.get("contactInfo"));
                    }
                    if (updates.containsKey("websiteUrl")) {
                        existingAccommodation.setWebsiteUrl((String) updates.get("websiteUrl"));
                    }
                    if (updates.containsKey("notes")) {
                        existingAccommodation.setNotes((String) updates.get("notes"));
                    }

                    // Update audit field
                    existingAccommodation.setUpdatedAt(LocalDateTime.now());

                    // Save updated accommodation
                    Accommodation savedAccommodation = accommodationRepository.save(existingAccommodation);
                    return AccommodationResponse.fromAccommodation(savedAccommodation);
                });
    }

    @Override
    @Transactional
    public boolean deleteAccommodation(ObjectId accommodationId) {
        return accommodationRepository.findById(accommodationId)
                .map(accommodation -> {
                    // Implement any additional checks required before deletion
                    accommodationRepository.delete(accommodation);
                    return true;
                })
                .orElse(false);
    }

    @Override
    public List<AccommodationResponse> findByAmenities(List<String> amenities) {
        Query query = new Query();

        // Create criteria to match accommodations having all the specified amenities
        for (String amenity : amenities) {
            query.addCriteria(Criteria.where("amenities." + amenity).exists(true));
        }

        List<Accommodation> accommodations = mongoTemplate.find(query, Accommodation.class);
        return AccommodationResponse.fromAccommodationList(accommodations);
    }
}
