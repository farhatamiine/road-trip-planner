package com.amine.roadtripplanner.Controller;


import com.amine.roadtripplanner.Dto.response.ApiResponseWrapper;
import com.amine.roadtripplanner.Dto.response.TripResponse;
import com.amine.roadtripplanner.Dto.response.UserResponse;
import com.amine.roadtripplanner.Entities.User;
import com.amine.roadtripplanner.Service.TripPlanningService;
import com.amine.roadtripplanner.Service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import org.bson.types.ObjectId;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/user")
public class UserController {
    private final UserService userService;
    private final TripPlanningService tripPlanningService;

    public UserController(UserService userService, TripPlanningService tripPlanningService) {
        this.userService = userService;
        this.tripPlanningService = tripPlanningService;
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
        UserResponse userResponse = UserResponse.fromUser(user);
        System.out.println(SecurityContextHolder.getContext().getAuthentication());
        return ResponseEntity.ok(userResponse);
    }


    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/all")
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        List<User> users = userService.findAllUsers();
        return ResponseEntity.ok(UserResponse.fromUserList(users));
    }

    @PreAuthorize("hasRole('ADMIN') or #username == authentication.principal.username")
    @GetMapping("/user/{username}")
    public ResponseEntity<UserResponse> getUserByUsername(@PathVariable @NotNull String username) {
        User user = userService.findUserByUsername(username);
        return ResponseEntity.ok(UserResponse.fromUser(user));
    }

    @PreAuthorize("hasRole('ADMIN') or #userId.toString().equals(authentication.principal.userId.toString())")
    @GetMapping("/{userId}/trips")
    public ResponseEntity<ApiResponseWrapper<List<TripResponse>>> getUserTrips(@PathVariable @NotNull ObjectId userId) {
        return tripPlanningService.findUserTrips(userId)
                .map(trips -> ResponseEntity.ok(
                        ApiResponseWrapper.success(trips, "User trips retrieved successfully")))
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ApiResponseWrapper.error("User not found", "USER_NOT_FOUND",
                                "No user exists with ID " + userId)));
    }
}
