package com.amine.roadtripplanner.Service;

import com.amine.roadtripplanner.Dto.request.InterestRequest;
import com.amine.roadtripplanner.Dto.response.InterestResponse;
import com.amine.roadtripplanner.Entities.Interest;
import com.amine.roadtripplanner.Repositories.InterestRepository;
import com.amine.roadtripplanner.enums.InterestType;
import org.bson.types.ObjectId;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class InterestServiceImpl implements InterestService {

    private final InterestRepository interestRepository;
    private final MongoTemplate mongoTemplate;

    public InterestServiceImpl(InterestRepository interestRepository, MongoTemplate mongoTemplate) {
        this.interestRepository = interestRepository;
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    @Transactional
    public InterestResponse createInterest(InterestRequest interestRequest) {
        // Convert request to entity
        Interest interest = InterestRequest.convertToEntity(interestRequest);

        // Set audit fields
        LocalDateTime now = LocalDateTime.now();
        interest.setCreatedAt(now);
        interest.setUpdatedAt(now);

        // Save interest
        Interest savedInterest = interestRepository.save(interest);

        return InterestResponse.fromInterest(savedInterest);
    }

    @Override
    public Optional<InterestResponse> getInterestById(ObjectId interestId) {
        return interestRepository.findById(interestId)
                .map(InterestResponse::fromInterest);
    }

    @Override
    public List<InterestResponse> getAllInterests() {
        List<Interest> interests = interestRepository.findAll();
        return InterestResponse.fromInterestList(interests);
    }

    @Override
    public List<InterestResponse> getInterestsByType(String interestType) {
        try {
            InterestType type = InterestType.valueOf(interestType.toUpperCase());
            List<Interest> interests = interestRepository.findByType(type);
            return InterestResponse.fromInterestList(interests);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid interest type: " + interestType);
        }
    }

    @Override
    public List<InterestResponse> searchInterestsByName(String nameQuery) {
        // Using MongoTemplate for more flexible queries
        Query query = new Query();
        query.addCriteria(Criteria.where("name").regex(nameQuery, "i"));

        List<Interest> interests = mongoTemplate.find(query, Interest.class);
        return InterestResponse.fromInterestList(interests);
    }

    @Override
    @Transactional
    public Optional<InterestResponse> updateInterest(ObjectId interestId, InterestRequest interestRequest) {
        return interestRepository.findById(interestId)
                .map(existingInterest -> {
                    // Update fields
                    existingInterest.setName(interestRequest.getName());
                    existingInterest.setDescription(interestRequest.getDescription());
                    existingInterest.setType(interestRequest.getType());
                    existingInterest.setEntryFee(interestRequest.getEntryFee());
                    existingInterest.setVisitDurationMinutes(interestRequest.getVisitDurationMinutes());
                    existingInterest.setBusinessHours(interestRequest.getBusinessHours());
                    existingInterest.setRequiresTickets(interestRequest.getRequiresTickets());
                    existingInterest.setTicketingUrl(interestRequest.getTicketingUrl());
                    existingInterest.setPopularityRanking(interestRequest.getPopularityRanking());
                    existingInterest.setNotes(interestRequest.getNotes());
                    existingInterest.setMustVisit(interestRequest.getMustVisit());

                    // Update audit field
                    existingInterest.setUpdatedAt(LocalDateTime.now());

                    // Save updated interest
                    Interest savedInterest = interestRepository.save(existingInterest);
                    return InterestResponse.fromInterest(savedInterest);
                });
    }

    @Override
    @Transactional
    public boolean deleteInterest(ObjectId interestId) {
        return interestRepository.findById(interestId)
                .map(interest -> {
                    // Check if the interest is referenced in any locations
                    // This would require additional repository methods or queries

                    interestRepository.delete(interest);
                    return true;
                })
                .orElse(false);
    }

    @Override
    public List<InterestResponse> getPopularInterests(int limit) {
        List<Interest> interests = interestRepository.findAll(
                        PageRequest.of(0, limit, Sort.by(Sort.Direction.ASC, "popularityRanking")))
                .getContent();

        return InterestResponse.fromInterestList(interests);
    }

    @Override
    public List<InterestResponse> getInterestsByCategory(String category) {
        // This implementation assumes a tags field exists or some similar categorization
        // You may need to adjust based on your actual data model
        Query query = new Query();
        query.addCriteria(Criteria.where("tags").is(category));

        List<Interest> interests = mongoTemplate.find(query, Interest.class);
        return InterestResponse.fromInterestList(interests);
    }

    @Override
    public List<InterestResponse> getMustVisitInterests() {
        List<Interest> interests = interestRepository.findByMustVisitTrue();
        return InterestResponse.fromInterestList(interests);
    }
}