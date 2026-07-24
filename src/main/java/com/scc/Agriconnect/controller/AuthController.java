package com.scc.Agriconnect.controller;

import com.scc.Agriconnect.dto.*;
import com.scc.Agriconnect.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "Endpoints for user authentication and registration. Includes cooperative registration and login functionality.")
public class AuthController {

    private final AuthService authService;

    @Operation(
        summary = "Register new cooperative with president account",
        description = "Creates a new cooperative and registers the president user. The cooperative will be in PENDING status awaiting admin approval. Returns JWT access token upon successful registration."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Successfully registered cooperative and user",
            content = @Content(schema = @Schema(implementation = AuthResponse.class))
        ),
        @ApiResponse(responseCode = "400", description = "Invalid input data or validation error"),
        @ApiResponse(responseCode = "409", description = "Email or cooperative already exists")
    })
    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request) {
        return ResponseEntity.ok(authService.register(request));
    }

    @Operation(
        summary = "Login to the system",
        description = "Authenticates a user with email and password. Returns JWT access token that must be used for subsequent authenticated requests."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Successfully authenticated",
            content = @Content(schema = @Schema(implementation = AuthResponse.class))
        ),
        @ApiResponse(responseCode = "401", description = "Invalid credentials"),
        @ApiResponse(responseCode = "403", description = "Cooperative not approved or account inactive")
    })
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }

    @Operation(
        summary = "Request password reset email",
        description = "Sends a password reset link to the user's registered email address."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Password reset email sent if user exists"),
        @ApiResponse(responseCode = "400", description = "Invalid input or user email not found")
    })
    @PostMapping("/forgot-password")
    public ResponseEntity<Void> forgotPassword(@Valid @RequestBody ForgotPasswordRequest request) {
        authService.forgotPassword(request);
        return ResponseEntity.ok().build();
    }

    @Operation(
        summary = "Reset password using reset token",
        description = "Resets the user's password using the token provided in the password reset email."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Password reset successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid or expired token, or passwords do not match")
    })
    @PostMapping("/reset-password")
    public ResponseEntity<Void> resetPassword(@Valid @RequestBody ResetPasswordRequest request) {
        authService.resetPassword(request);
        return ResponseEntity.ok().build();
    }
}