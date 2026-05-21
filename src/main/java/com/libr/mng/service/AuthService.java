package com.libr.mng.service;

import org.springframework.web.multipart.MultipartFile;

import com.libr.mng.dto.request.LoginRequestDTO;
import com.libr.mng.dto.request.RegisterRequestDTO;
import com.libr.mng.dto.request.UpdateProfileRequestDTO;
import com.libr.mng.dto.response.LoginResponseDTO;
import com.libr.mng.dto.response.ProfileResponseDTO;

public interface AuthService {
	
	LoginResponseDTO login(LoginRequestDTO request);

	void register(RegisterRequestDTO request);

	void sendResetOtp(String email);

	boolean verifyOtp(String email, String otp);

	void resetPassword(String email, String newPassword);

	void changePassword(String email, String oldPassword, String newPassword);

	ProfileResponseDTO getProfile(String email);

	ProfileResponseDTO updateProfile(String email, UpdateProfileRequestDTO dto);

	String uploadProfileImage(String email, MultipartFile file) throws Exception;
}
