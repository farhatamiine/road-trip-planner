package com.amine.roadtripplanner.Dto.response;


import com.amine.roadtripplanner.Entities.User;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
public class UserResponse {
    private String id;
    private String username;
    private String email;
    private String firstName;
    private String lastName;
    private String phone;
    private String address;
    private String city;
    private LocalDateTime createdAt;
    private LocalDateTime lastLoginAt;
    private List<TripResponse> trips;

    /**
     * Converts a list of User entities to a list of UserResponse DTOs.
     * This is useful for API endpoints that need to return multiple users.
     *
     * @param users the list of User entities to convert
     * @return a list of UserResponse DTOs
     */
    public static List<UserResponse> fromUserList(List<User> users) {
        return users.stream()
                .map(UserResponse::fromUser)
                .collect(Collectors.toList());
    }

    /**
     * Converts a single User entity to a UserResponse DTO.
     * This method extracts only the necessary user information for client responses,
     * avoiding exposure of sensitive data like passwords or internal fields.
     *
     * @param user the User entity to convert
     * @return a UserResponse DTO with the user's public information
     */
    public static UserResponse fromUser(User user) {
        return UserResponse.builder()
                .id(user.getUserId() != null ? user.getUserId().toString() : null)
                .username(user.getUsername())
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .trips(TripResponse.fromTripList(user.getTripList()))
                .lastName(user.getLastName())
                .phone(user.getPhone())
                .address(user.getAddress())
                .city(user.getCity())
                .createdAt(user.getCreatedAt())
                .lastLoginAt(user.getLastLoginAt())
                .build();
    }

}