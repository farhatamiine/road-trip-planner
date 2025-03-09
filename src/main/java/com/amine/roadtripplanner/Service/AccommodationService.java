package com.amine.roadtripplanner.Service;

import com.amine.roadtripplanner.Dto.request.AccommodationRequest;
import com.amine.roadtripplanner.Dto.response.AccommodationResponse;
import org.bson.types.ObjectId;

import java.util.List;
import java.util.Map;
import java.util.Optional;

//(finding, booking accommodations)
public interface AccommodationService {
    // Create a new accommodation
    AccommodationResponse createAccommodation(AccommodationRequest accommodationRequest);

    // Get an accommodation by ID
    Optional<AccommodationResponse> getAccommodationById(ObjectId accommodationId);

    // Get all accommodations
    List<AccommodationResponse> getAllAccommodations();

    // Get accommodations by type
    List<AccommodationResponse> getAccommodationsByType(String accommodationType);

    // Search accommodations by name
    List<AccommodationResponse> searchAccommodationsByName(String nameQuery);

    // Filter accommodations by price range
    List<AccommodationResponse> filterByPriceRange(Double minPrice, Double maxPrice);

    // Filter accommodations by rating
    List<AccommodationResponse> filterByRating(Integer minRating);

    // Update an accommodation
    Optional<AccommodationResponse> updateAccommodation(ObjectId accommodationId,
                                                        AccommodationRequest accommodationRequest);

    // Partial update of an accommodation
    Optional<AccommodationResponse> partialUpdateAccommodation(ObjectId accommodationId,
                                                               Map<String, Object> updates);

    // Delete an accommodation
    boolean deleteAccommodation(ObjectId accommodationId);

    // Find accommodations with specific amenities
    List<AccommodationResponse> findByAmenities(List<String> amenities);
}
