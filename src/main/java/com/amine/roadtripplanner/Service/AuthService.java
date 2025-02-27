package com.amine.roadtripplanner.Service;

import com.amine.roadtripplanner.Dto.request.LoginRequest;
import com.amine.roadtripplanner.Dto.request.RegisterRequest;
import com.amine.roadtripplanner.Dto.response.AuthResponse;

public interface AuthService {
    AuthResponse register(RegisterRequest registerRequest);
    AuthResponse login(LoginRequest loginRequest);
}
