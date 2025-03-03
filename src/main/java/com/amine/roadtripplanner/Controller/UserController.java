package com.amine.roadtripplanner.Controller;


import com.amine.roadtripplanner.Dto.response.UserResponse;
import com.amine.roadtripplanner.Entities.User;
import com.amine.roadtripplanner.Service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.constraints.Email;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/user")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Operation(summary = "Get a user by email")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User found"),
            @ApiResponse(responseCode = "404", description = "User not found"),
            @ApiResponse(responseCode = "403", description = "Unauthorized access"),
            //@ApiResponse(responseCode = "429", description = "Too many requests")
    })

    @GetMapping("/email/{email}")
    @PreAuthorize("hasRole('ADMIN') or #email == authentication.principal.email")
    public ResponseEntity<UserResponse> getUserByEmail(@PathVariable @Email(message = "Email is not valid") String email) {
        User user = userService.findUserByEmail(email);
        UserResponse userResponse = UserResponse.builder()
                .id(user.getUserId() != null ? user.getUserId().toString() : null)
                .username(user.getUsername())
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .phone(user.getPhone())
                .address(user.getAddress())
                .city(user.getCity())
                .createdAt(user.getCreatedAt())
                .lastLoginAt(user.getLastLoginAt())
                .build();
        return ResponseEntity.ok(userResponse);
    }

    /*public ResponseEntity<List<UserResponse>> getAllUsers() {
        List<User> users = userService.findAllUsers();
        List<User> userResponses = new ArrayList<>();
        userResponses.addAll(users);
        for (User user : users) {

        }
    }*/
}
