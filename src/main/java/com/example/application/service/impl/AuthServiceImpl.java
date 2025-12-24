package com.example.application.service.impl;

import com.example.application.dto.request.LoginRequest;
import com.example.application.dto.request.ResetPasswordRequest;
import com.example.application.dto.request.SignupRequest;
import com.example.application.dto.response.LoginResponse;
import com.example.application.entity.City;
import com.example.application.entity.Country;
import com.example.application.entity.State;
import com.example.application.entity.User;
import com.example.application.exception.AppException;
import com.example.application.exception.ResourceNotFoundException;
import com.example.application.repository.CityRepository;
import com.example.application.repository.CountryRepository;
import com.example.application.repository.StateRepository;
import com.example.application.repository.UserRepository;
import com.example.application.service.AuthService;
import com.example.application.service.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final CountryRepository countryRepository;
    private final StateRepository stateRepository;
    private final CityRepository cityRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;

    @Override
    @Transactional
    public boolean registerUser(SignupRequest request) {
        log.info("Registering user with email: {}", request.getEmail());

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new AppException("Email already exists");
        }

        Country country = countryRepository.findById(request.getCountryId())
                .orElseThrow(() -> new ResourceNotFoundException("Country not found"));
        State state = stateRepository.findById(request.getStateId())
                .orElseThrow(() -> new ResourceNotFoundException("State not found"));
        City city = cityRepository.findById(request.getCityId())
                .orElseThrow(() -> new ResourceNotFoundException("City not found"));

        // Generate Random Password
        String rawPassword = generateRandomPassword();

        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPhoneNumber(request.getPhoneNumber());
        user.setCountry(country);
        user.setState(state);
        user.setCity(city);
        user.setPassword(passwordEncoder.encode(rawPassword));
        user.setFirstLogin(true);

        userRepository.save(user);

        // Send Email Async
        emailService.sendEmail(
                user.getEmail(),
                "Welcome to Our Platform - Your Login Credentials",
                "Hello " + user.getName() + ",\n\nYour account has been created. Here is your temporary password: "
                        + rawPassword + "\n\nPlease login and change it immediately.");

        return true;
    }

    @Override
    public LoginResponse login(LoginRequest request) {
        log.info("Logging in user: {}", request.getEmail());
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new AppException("Invalid email or password")); // Generic message for security

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new AppException("Invalid email or password");
        }

        if (user.isFirstLogin()) {
            return LoginResponse.builder()
                    .isAuthenticated(true)
                    .isFirstLogin(true)
                    .message("Password reset required")
                    .userId(user.getId())
                    .name(user.getName())
                    .build();
        }

        // Return success
        return LoginResponse.builder()
                .isAuthenticated(true)
                .isFirstLogin(false)
                .message("Login Successful")
                .userId(user.getId())
                .name(user.getName())
                .build();
    }

    @Override
    @Transactional
    public boolean resetPassword(ResetPasswordRequest request) {
        log.info("Resetting password for user: {}", request.getEmail());

        if (!request.getNewPassword().equals(request.getConfirmPassword())) {
            throw new AppException("New password and Confirm password do not match");
        }

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (!passwordEncoder.matches(request.getOldPassword(), user.getPassword())) {
            throw new AppException("Old password is incorrect");
        }

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        user.setFirstLogin(false);
        userRepository.save(user);

        return true;
    }

    private String generateRandomPassword() {
        return UUID.randomUUID().toString().substring(0, 8);
    }
}
