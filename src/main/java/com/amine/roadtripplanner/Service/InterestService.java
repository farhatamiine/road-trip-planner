package com.amine.roadtripplanner.Service;

import com.amine.roadtripplanner.Dto.request.InterestRequest;
import com.amine.roadtripplanner.Dto.response.InterestResponse;
import org.bson.types.ObjectId;

import java.util.List;
import java.util.Optional;

public interface InterestService {

    // Create a new interest
    InterestResponse createInterest(InterestRequest interestRequest);

    // Get an interest by ID
    Optional<InterestResponse> getInterestById(ObjectId interestId);

    // Get all interests
    List<InterestResponse> getAllInterests();

    // Get interests by type
    List<InterestResponse> getInterestsByType(String interestType);

    // Search interests by name (partial match)
    List<InterestResponse> searchInterestsByName(String nameQuery);

    // Update an interest
    Optional<InterestResponse> updateInterest(ObjectId interestId, InterestRequest interestRequest);

    // Delete an interest
    boolean deleteInterest(ObjectId interestId);

    // Get popular interests (based on ranking)
    List<InterestResponse> getPopularInterests(int limit);

    // Get interests with specific tags/category
    List<InterestResponse> getInterestsByCategory(String category);

    // Get must-visit interests
    List<InterestResponse> getMustVisitInterests();
}