package com.libr.mng.controller;


import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.libr.mng.dto.request.ChangePasswordRequestDTO;
import com.libr.mng.dto.request.ForgotPasswordRequestDTO;
import com.libr.mng.dto.request.LoginRequestDTO;
import com.libr.mng.dto.request.RegisterRequestDTO;
import com.libr.mng.dto.request.ResetPasswordDTO;
import com.libr.mng.dto.request.UpdateProfileRequestDTO;
import com.libr.mng.dto.request.VerifyOtpDTO;
import com.libr.mng.dto.response.LoginResponseDTO;
import com.libr.mng.service.AuthService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@Valid @RequestBody LoginRequestDTO request) {
        return ResponseEntity.ok(authService.login(request));
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@Valid @RequestBody RegisterRequestDTO request) {
        authService.register(request);
        return ResponseEntity.ok("Registration successful. Please login.");
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestBody ForgotPasswordRequestDTO request) {
        authService.sendResetOtp(request.getEmail());
        return ResponseEntity.ok(Map.of("message", "OTP sent to email"));
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<?> verifyOtp(@RequestBody VerifyOtpDTO request) {
        boolean valid = authService.verifyOtp(request.getEmail(), request.getOtp());
        if (valid) {
            return ResponseEntity.ok("OTP verified");
        } else {
            return ResponseEntity.badRequest().body("Invalid or expired OTP");
        }
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody ResetPasswordDTO request) {
        authService.resetPassword(request.getEmail(), request.getNewPassword());
        return ResponseEntity.ok("Password reset successfully");
    }

    @PostMapping("/change-password")
    public ResponseEntity<?> changePassword(Authentication authentication, @RequestBody ChangePasswordRequestDTO dto) {

        String email = authentication.getName();

        authService.changePassword(
                email,
                dto.getOldPassword(),
                dto.getNewPassword()
        );

        return ResponseEntity.ok(
                Map.of("message", "Password changed successfully")
        );
    }

    @GetMapping("/me")
    public ResponseEntity<?> getProfile(Authentication authentication) {

        String email = authentication.getName();

        return ResponseEntity.ok(authService.getProfile(email));
    }

    @PutMapping("/update-profile")
    public ResponseEntity<?> updateProfile(
            Authentication authentication,
            @RequestBody UpdateProfileRequestDTO dto
    ) {

        String email = authentication.getName();

        return ResponseEntity.ok(
                authService.updateProfile(email, dto)
        );
    }

    @PostMapping("/upload-image")
    public ResponseEntity<?> uploadImage(
            Authentication authentication,
            @RequestParam("file") MultipartFile file
    ) throws Exception {

        String email = authentication.getName();

        String imageName =
                authService.uploadProfileImage(email, file);

        return ResponseEntity.ok(
                Map.of("profileImage", imageName)
        );
    }
}
