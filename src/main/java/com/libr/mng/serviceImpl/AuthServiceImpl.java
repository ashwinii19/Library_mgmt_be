package com.libr.mng.serviceImpl;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Random;

import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.libr.mng.dto.request.LoginRequestDTO;
import com.libr.mng.dto.request.RegisterRequestDTO;
import com.libr.mng.dto.request.UpdateProfileRequestDTO;
import com.libr.mng.dto.response.LoginResponseDTO;
import com.libr.mng.dto.response.ProfileResponseDTO;
import com.libr.mng.entity.User;
import com.libr.mng.repository.UserRepository;
import com.libr.mng.security.JwtTokenProvider;
import com.libr.mng.service.AuthService;
import org.thymeleaf.context.Context;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

	private final UserRepository userRepository;

	private final PasswordEncoder passwordEncoder;

	private final AuthenticationManager authenticationManager;

	private final JwtTokenProvider jwtTokenProvider;

	private final EmailService emailService;

	private final Cloudinary cloudinary;

	private final ModelMapper modelMapper;

	@Override
	public LoginResponseDTO login(LoginRequestDTO request) {

		Authentication auth = authenticationManager.authenticate(

				new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

		String token = jwtTokenProvider.generateToken(auth);

		User user = userRepository.findByEmail(request.getEmail())
				.orElseThrow(() -> new RuntimeException("User not found"));

		return LoginResponseDTO.builder()

				.userId(user.getId())

				.name(user.getName())

				.email(user.getEmail())

				.role(user.getRole())

				.token(token)

				.tokenType("Bearer")

				.build();
	}

	@Override
	public void register(RegisterRequestDTO request) {

		// Email duplicate check
		if (userRepository.existsByEmail(request.getEmail())) {

			throw new RuntimeException("Email already registered");
		}

		// Employee ID duplicate check
		if (userRepository.existsByEmployeeId(request.getEmployeeId())) {

			throw new RuntimeException("Employee ID already exists");
		}

		// Aurionpro email validation
		if (!request.getEmail().toLowerCase().endsWith("@aurionpro.com")) {

			throw new RuntimeException("Only Aurionpro employees can register");
		}

		User user = new User();

		user.setName(request.getName());

		user.setEmail(request.getEmail());

		user.setEmployeeId(request.getEmployeeId());

		user.setPassword(passwordEncoder.encode(request.getPassword()));

		user.setRole("EMPLOYEE");

		userRepository.save(user);
	}

	@Override
	public void sendResetOtp(String email) {

	    User user = userRepository.findByEmail(email)
	            .orElseThrow(() -> new RuntimeException("Email not found"));

	    String otp = String.format("%06d", new Random().nextInt(999999));

	    user.setResetOtp(otp);

	    user.setOtpExpiry(LocalDateTime.now().plusMinutes(10));

	    userRepository.save(user);

	    Context context = new Context();

	    context.setVariable("user", user);

	    context.setVariable("otp", otp);

	    emailService.sendHtmlEmail(
	            email,
	            "Password Reset OTP",
	            "otp-email",
	            context
	    );
	}

	@Override
	public boolean verifyOtp(String email, String otp) {

		User user = userRepository.findByEmail(email)

				.orElseThrow(() -> new RuntimeException("Email not found"));

		return user.getResetOtp() != null && user.getResetOtp().equals(otp)
				&& user.getOtpExpiry().isAfter(LocalDateTime.now());
	}

	@Override
	public void resetPassword(String email, String newPassword) {

		User user = userRepository.findByEmail(email)

				.orElseThrow(() -> new RuntimeException("Email not found"));

		user.setPassword(passwordEncoder.encode(newPassword));

		user.setResetOtp(null);

		user.setOtpExpiry(null);

		userRepository.save(user);
	}

	@Override
	public void changePassword(String email, String oldPassword, String newPassword) {

		User user = userRepository.findByEmail(email)

				.orElseThrow(() -> new RuntimeException("User not found"));

		if (!passwordEncoder.matches(oldPassword, user.getPassword())) {

			throw new RuntimeException("Old password is incorrect");
		}

		user.setPassword(passwordEncoder.encode(newPassword));

		userRepository.save(user);
	}

	@Override
	public ProfileResponseDTO getProfile(String email) {

		User user = userRepository.findByEmail(email).orElseThrow();

		return modelMapper.map(user, ProfileResponseDTO.class);
	}

	@Override
	public ProfileResponseDTO updateProfile(String email, UpdateProfileRequestDTO dto) {

		User user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));

		// Email cannot change
		if (dto.getEmail() != null && !dto.getEmail().equals(user.getEmail())) {

			throw new RuntimeException("Email cannot be changed");
		}

		// Employee ID cannot change
		if (dto.getEmployeeId() != null && !dto.getEmployeeId().equals(user.getEmployeeId())) {

			throw new RuntimeException("Employee ID cannot be changed");
		}

		// Role cannot change
		if (dto.getRole() != null && !dto.getRole().equals(user.getRole())) {

			throw new RuntimeException("Role cannot be changed");
		}

		// Only name can update
		if (dto.getName() != null && !dto.getName().isBlank()) {

			user.setName(dto.getName());
		}

		userRepository.save(user);

		return modelMapper.map(user, ProfileResponseDTO.class);
	}

	@Override
	public String uploadProfileImage(String email, MultipartFile file) throws Exception {

		User user = userRepository.findByEmail(email)

				.orElseThrow(() -> new RuntimeException("User not found"));

		Map uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.emptyMap());

		String imageUrl = uploadResult.get("secure_url").toString();

		user.setProfileImage(imageUrl);

		userRepository.save(user);

		return imageUrl;
	}
}