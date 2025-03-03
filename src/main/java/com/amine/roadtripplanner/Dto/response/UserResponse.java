package com.amine.roadtripplanner.Dto.response;


import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

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



}