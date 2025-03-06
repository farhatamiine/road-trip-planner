package com.amine.roadtripplanner.Controller;

import com.amine.roadtripplanner.Dto.request.TripRequest;
import com.amine.roadtripplanner.Dto.response.TripResponse;
import com.amine.roadtripplanner.Entities.Trip;
import com.amine.roadtripplanner.Entities.User;
import com.amine.roadtripplanner.Security.SecurityUtils;
import com.amine.roadtripplanner.Service.TripPlanningService;
import com.amine.roadtripplanner.enums.TripStatus;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class TripControllerTest {


    @MockBean
    TripPlanningService tripPlanningService;

    @MockBean
    SecurityUtils securityUtils;

    @Autowired
    private MockMvc mvc;
    private ObjectMapper mapper;
    private ObjectId tripId;
    private ObjectId userId;
    private TripRequest tripRequest;
    private TripResponse tripResponse;
    private Trip savedTrip;
    private User mockUser;



    @BeforeEach
    void setUp() {
        mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());

        tripId = new ObjectId("507f1f77bcf86cd799439011");
        userId = new ObjectId("507f1f77bcf86cd799439013");

        // Create a mock user
        mockUser = new User();
        mockUser.setUserId(userId);
        mockUser.setUsername("test_user");

        tripRequest = TripRequest.builder()
                .tripDate(LocalDate.of(2025, 5, 20))
                .tripDescription("description")
                .tripName("Trip Name")
                .tripStatus(TripStatus.ACTIVE)
                .build();

        savedTrip = Trip.builder()
                .tripId(tripId)
                .tripDate(tripRequest.getTripDate())
                .tripDescription(tripRequest.getTripDescription())
                .tripName(tripRequest.getTripName())
                .tripStatus(tripRequest.getTripStatus())
                .build();

        tripResponse = TripResponse.builder()
                .tripId(tripId.toString())
                .tripDate(LocalDate.of(2025, 5, 20))
                .tripDescription("description")
                .tripStatus(TripStatus.PLANNED)
                .tripName("Trip Test Name")
                .segmentList(new ArrayList<>())
                .build();

        mockUser.getTripList().add(savedTrip);
    }

    @Test
    @WithMockUser("test_user")
    @DisplayName("Create - Should return 201 when request is valid")
    void shouldCreateTripWhenRequestIsValid() throws Exception {
        //Arrange
        when(tripPlanningService.saveNewTrip(any(TripRequest.class)))
                .thenReturn(savedTrip);
        //Act
        mvc.perform(post("/api/trips").contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(tripRequest)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.data.tripId").value(tripId.toString()))
                .andExpect(jsonPath("$.data.tripDate").value(LocalDate.of(2025, 5, 20).toString()))
                .andExpect(jsonPath("$.data.tripDescription").value(tripRequest.getTripDescription()))
                .andExpect(jsonPath("$.success").value(true))
        ;

        //Assert
        verify(tripPlanningService).saveNewTrip(any(TripRequest.class));
    }


    @Test
    @WithMockUser("test_user")
    @DisplayName("Create - Should return 400 when request is missing required fields")
    void shouldReturnBadRequestWhenTripRequiredFieldsAreMissing() throws Exception {
        //Arrange

        tripRequest.setTripDescription("he");

        when(tripPlanningService.saveNewTrip(any(TripRequest.class)))
                .thenReturn(savedTrip);
        //Act
        mvc.perform(post("/api/trips").contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(tripRequest)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("Validation failed"))
                .andExpect(jsonPath("$.error.code").value("VALIDATION_ERROR"));

        //Assert
        verify(tripPlanningService, never()).saveNewTrip(any(TripRequest.class));
    }

    @Test
    @WithMockUser("test_user")
    @DisplayName("Get - Should return 200 when ID exists")
    void shouldReturnTripWhenIdExist() throws Exception {
        //Arrange
        when(tripPlanningService.getTripById(eq(tripId)))
                .thenReturn(Optional.of(tripResponse));

        //Act
        mvc.perform(get("/api/trips/" + tripId).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.data.tripId").value(tripId.toString()))
                .andExpect(jsonPath("$.data.tripDate").value(LocalDate.of(2025, 5, 20).toString()))
                .andExpect(jsonPath("$.data.tripDescription").value(tripResponse.getTripDescription()));
        //Assert
        verify(tripPlanningService).getTripById(eq(tripId));
    }

    @Test
    @WithMockUser("test_user")
    @DisplayName("Get - Should return 404 when ID doesn't exist")
    void shouldReturnNotFoundWhenIdNotExist() throws Exception {
        //Arrange
        when(tripPlanningService.getTripById(eq(tripId)))
                .thenReturn(Optional.empty());

        //Act & Verify
        mvc.perform(get("/api/trips/" + tripId).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.error.code").value("TRIP_NOT_FOUND"))
                .andExpect(jsonPath("$.success").value("false"));

        verify(tripPlanningService).getTripById(eq(tripId));
    }

    @Test
    @DisplayName("Update - Should return 200 when update is successful")
    @WithMockUser(username = "test_user")
    void shouldUpdateTripWhenRequestIsValid() throws Exception {
        // Arrange
        when(securityUtils.getCurrentUser()).thenReturn(mockUser);

        when(tripPlanningService.updateTrip(any(TripRequest.class), eq(tripId), eq(mockUser.getUserId())))
                .thenReturn(Optional.of(tripResponse));

        // Act & Assert
        mvc.perform(put("/api/trips/" + tripId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(tripRequest)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success").value(true));

        // Verify
        verify(tripPlanningService).updateTrip(eq(tripRequest), eq(tripId), eq(userId));
        verify(securityUtils).getCurrentUser();
    }


    @Test
    @DisplayName("Authentication - Should return 401 when user is not authenticated")
    void shouldReturnUnauthorizedWhenUserIsNotAuthenticated() throws Exception {
        // Act & Assert
        mvc.perform(get("/api/trips/" + tripId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

}