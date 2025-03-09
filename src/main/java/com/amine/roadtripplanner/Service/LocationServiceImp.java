package com.amine.roadtripplanner.Service;

import com.amine.roadtripplanner.Dto.request.LocationRequest;
import com.amine.roadtripplanner.Dto.response.LocationResponse;
import com.amine.roadtripplanner.Entities.Accommodation;
import com.amine.roadtripplanner.Entities.Interest;
import com.amine.roadtripplanner.Entities.Location;
import com.amine.roadtripplanner.Exception.ResourceNotFoundException;
import com.amine.roadtripplanner.Repositories.AccommodationRepository;
import com.amine.roadtripplanner.Repositories.InterestRepository;
import com.amine.roadtripplanner.Repositories.LocationRepository;
import com.amine.roadtripplanner.Security.SecurityUtils;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class LocationServiceImp implements LocationService {

    private final LocationRepository locationRepository;
    private final AccommodationRepository accommodationRepository;
    private final InterestRepository interestRepository;
    private final SecurityUtils securityUtils;
    private final MongoTemplate mongoTemplate;

    public LocationServiceImp(LocationRepository locationRepository, AccommodationRepository accommodationRepository, InterestRepository interestRepository, SecurityUtils securityUtils, MongoTemplate mongoTemplate) {
        this.locationRepository = locationRepository;
        this.accommodationRepository = accommodationRepository;
        this.interestRepository = interestRepository;
        this.securityUtils = securityUtils;
        this.mongoTemplate = mongoTemplate;
    }


    @Override
    @Transactional
    public LocationResponse createLocation(LocationRequest locationRequest) {
        // Convert request to entity
        Location location = LocationRequest.convertToEntity(locationRequest);

        // Set audit fields
        LocalDateTime now = LocalDateTime.now();
        location.setCreatedAt(now);
        location.setUpdatedAt(now);

        // Save location
        Location savedLocation = locationRepository.save(location);

        return LocationResponse.fromLocation(savedLocation);
    }

    @Override
    public Optional<LocationResponse> getLocationById(ObjectId locationId) {
        return locationRepository.findById(locationId)
                .map(LocationResponse::fromLocation);
    }

    @Override
    public List<LocationResponse> getLocationsByCountry(String country) {
        List<Location> locations = locationRepository.findByCountry(country);
        return LocationResponse.fromLocationList(locations);
    }

    @Override
    public List<LocationResponse> searchLocationsByCityName(String cityNameQuery) {
        // Using MongoTemplate for more flexible queries
        Query query = new Query();
        query.addCriteria(Criteria.where("cityName").regex(cityNameQuery, "i"));

        List<Location> locations = mongoTemplate.find(query, Location.class);
        return LocationResponse.fromLocationList(locations);
    }

    @Override
    @Transactional
    public Optional<LocationResponse> updateLocation(ObjectId locationId, LocationRequest locationRequest) {
        return locationRepository.findById(locationId)
                .map(existingLocation -> {
                    // Update fields
                    existingLocation.setCityName(locationRequest.getCityName());
                    existingLocation.setCountry(locationRequest.getCountry());
                    existingLocation.setLatitude(locationRequest.getLatitude());
                    existingLocation.setLongitude(locationRequest.getLongitude());

                    // Update audit field
                    existingLocation.setUpdatedAt(LocalDateTime.now());

                    // Save updated location
                    Location savedLocation = locationRepository.save(existingLocation);
                    return LocationResponse.fromLocation(savedLocation);
                });
    }

    @Override
    @Transactional
    public boolean deleteLocation(ObjectId locationId) {
        return locationRepository.findById(locationId)
                .map(location -> {
                    // Check if location is used in any segments before deletion
                    // This would require additional repository methods or queries

                    locationRepository.delete(location);
                    return true;
                })
                .orElse(false);
    }

    @Override
    @Transactional
    public boolean addAccommodationToLocation(ObjectId locationId, ObjectId accommodationId) {
        Location location = locationRepository.findById(locationId)
                .orElseThrow(() -> new ResourceNotFoundException("Location", locationId.toString()));

        Accommodation accommodation = accommodationRepository.findById(accommodationId)
                .orElseThrow(() -> new ResourceNotFoundException("Accommodation", accommodationId.toString()));

        location.getAccommodations().add(accommodation);
        locationRepository.save(location);

        return true;
    }

    @Override
    @Transactional
    public boolean addInterestToLocation(ObjectId locationId, ObjectId interestId) {
        Location location = locationRepository.findById(locationId)
                .orElseThrow(() -> new ResourceNotFoundException("Location", locationId.toString()));

        Interest interest = interestRepository.findById(interestId)
                .orElseThrow(() -> new ResourceNotFoundException("Interest", interestId.toString()));

        location.getInterests().add(interest);
        locationRepository.save(location);

        return true;
    }

    @Override
    @Transactional
    public boolean removeAccommodationFromLocation(ObjectId locationId, ObjectId accommodationId) {
        Location location = locationRepository.findById(locationId)
                .orElseThrow(() -> new ResourceNotFoundException("Location", locationId.toString()));

        boolean removed = location.getAccommodations().removeIf(
                accommodation -> accommodation.getAccommodationId().equals(accommodationId)
        );

        if (removed) {
            locationRepository.save(location);
        }

        return removed;
    }

    @Override
    @Transactional
    public boolean removeInterestFromLocation(ObjectId locationId, ObjectId interestId) {
        Location location = locationRepository.findById(locationId)
                .orElseThrow(() -> new ResourceNotFoundException("Location", locationId.toString()));

        boolean removed = location.getInterests().removeIf(
                interest -> interest.getInterestId().equals(interestId)
        );

        if (removed) {
            locationRepository.save(location);
        }

        return removed;
    }

    @Override
    public List<LocationResponse> getAllLocations() {
        List<Location> locations = locationRepository.findAll();
        return LocationResponse.fromLocationList(locations);
    }
}
