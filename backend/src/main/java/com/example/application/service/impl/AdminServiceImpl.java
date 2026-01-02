package com.example.application.service.impl;

import com.example.application.dto.request.UserRequest;
import com.example.application.dto.response.UserResponse;
import com.example.application.entity.User;
import com.example.application.entity.Role;
import com.example.application.entity.Country;
import com.example.application.entity.State;
import com.example.application.entity.City;
import com.example.application.exception.AppException;
import com.example.application.exception.ResourceNotFoundException;
import com.example.application.repository.UserRepository;
import com.example.application.repository.CountryRepository;
import com.example.application.repository.StateRepository;
import com.example.application.repository.CityRepository;
import com.example.application.service.AdminService;
import com.example.application.service.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

    private final UserRepository userRepository;
    private final CountryRepository countryRepository;
    private final StateRepository stateRepository;
    private final CityRepository cityRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;

    @Override
    public Page<UserResponse> getAllUsers(String search, Pageable pageable) {
        Page<User> users;
        if (search != null && !search.trim().isEmpty()) {
            users = userRepository.findByNameContainingIgnoreCaseOrEmailContainingIgnoreCase(search, search, pageable);
        } else {
            users = userRepository.findAll(pageable);
        }
        return users.map(this::mapToResponse);
    }

    @Override
    public UserResponse getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
        return mapToResponse(user);
    }

    @Override
    @Transactional
    public UserResponse createUser(UserRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new AppException("Email already exists");
        }

        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPhoneNumber(request.getPhoneNumber());
        setLocations(user, request);
        user.setRole(request.getRole() != null ? request.getRole() : Role.USER);

        String rawPassword = UUID.randomUUID().toString().substring(0, 8);
        user.setPassword(passwordEncoder.encode(rawPassword));
        user.setPasswordResetRequired(true);

        user = userRepository.save(user);

        // Async email sending is handled by implementation of EmailService hopefully,
        // if not it will block but that's ok for now
        try {
            emailService.sendEmail(
                    user.getEmail(),
                    "Account Created",
                    "Hello " + user.getName() + ",\n\nYour account has been created by Admin. Password: "
                            + rawPassword);
        } catch (Exception e) {
            log.error("Failed to send email to {}", user.getEmail(), e);
            // Don't fail the transaction just because email failed? Or should we?
            // Usually we don't want to rollback creation if email fails but password is
            // unknown then?
            // User can reset password. keeping it safely wrapped.
        }

        return mapToResponse(user);
    }

    @Override
    @Transactional
    public UserResponse updateUser(Long id, UserRequest request) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));

        user.setName(request.getName());
        user.setPhoneNumber(request.getPhoneNumber());

        if (!user.getEmail().equals(request.getEmail())) {
            if (userRepository.existsByEmail(request.getEmail())) {
                throw new AppException("Email already exists");
            }
            user.setEmail(request.getEmail());
        }

        setLocations(user, request);
        if (request.getRole() != null) {
            user.setRole(request.getRole());
        }

        user = userRepository.save(user);
        return mapToResponse(user);
    }

    @Override
    @Transactional
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new ResourceNotFoundException("User not found with id: " + id);
        }
        userRepository.deleteById(id);
    }

    private void setLocations(User user, UserRequest request) {
        Country country = countryRepository.findById(request.getCountryId())
                .orElseThrow(() -> new ResourceNotFoundException("Country not found"));
        State state = stateRepository.findById(request.getStateId())
                .orElseThrow(() -> new ResourceNotFoundException("State not found"));
        City city = cityRepository.findById(request.getCityId())
                .orElseThrow(() -> new ResourceNotFoundException("City not found"));

        user.setCountry(country);
        user.setState(state);
        user.setCity(city);
    }

    private UserResponse mapToResponse(User user) {
        UserResponse response = new UserResponse();
        response.setId(user.getId());
        response.setName(user.getName());
        response.setEmail(user.getEmail());
        response.setPhoneNumber(user.getPhoneNumber());
        response.setCountryId(user.getCountry() != null ? user.getCountry().getId() : null);
        response.setCountryName(user.getCountry() != null ? user.getCountry().getName() : null);
        response.setStateId(user.getState() != null ? user.getState().getId() : null);
        response.setStateName(user.getState() != null ? user.getState().getName() : null);
        response.setCityId(user.getCity() != null ? user.getCity().getId() : null);
        response.setCityName(user.getCity() != null ? user.getCity().getName() : null);
        response.setRole(user.getRole());
        response.setPasswordResetRequired(user.isPasswordResetRequired());
        return response;
    }
}
