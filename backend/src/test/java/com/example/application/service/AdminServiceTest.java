package com.example.application.service;

import com.example.application.dto.request.UserRequest;
import com.example.application.dto.response.UserResponse;
import com.example.application.entity.*;
import com.example.application.repository.*;
import com.example.application.service.impl.AdminServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AdminServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private CountryRepository countryRepository;
    @Mock
    private StateRepository stateRepository;
    @Mock
    private CityRepository cityRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private EmailService emailService;

    @InjectMocks
    private AdminServiceImpl adminService;

    private User user;
    private UserRequest userRequest;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setName("Test User");
        user.setEmail("test@example.com");
        user.setRole(Role.USER);

        userRequest = new UserRequest();
        userRequest.setName("Test User");
        userRequest.setEmail("test@example.com");
        userRequest.setCountryId(1L);
        userRequest.setStateId(1L);
        userRequest.setCityId(1L);
        userRequest.setRole(Role.USER);
    }

    @Test
    void testGetAllUsers() {
        Page<User> page = new PageImpl<>(List.of(user));
        when(userRepository.findAll(any(Pageable.class))).thenReturn(page);

        Page<UserResponse> result = adminService.getAllUsers(null, PageRequest.of(0, 10));

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals("Test User", result.getContent().get(0).getName());
    }

    @Test
    void testCreateUser() {
        when(userRepository.existsByEmail(anyString())).thenReturn(false);
        when(countryRepository.findById(anyLong())).thenReturn(Optional.of(new Country()));
        when(stateRepository.findById(anyLong())).thenReturn(Optional.of(new State()));
        when(cityRepository.findById(anyLong())).thenReturn(Optional.of(new City()));
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPass");
        when(userRepository.save(any(User.class))).thenReturn(user);

        UserResponse response = adminService.createUser(userRequest);

        assertNotNull(response);
        assertEquals("Test User", response.getName());
        verify(emailService, times(1)).sendEmail(anyString(), anyString(), anyString());
    }

    @Test
    void testDeleteUser() {
        when(userRepository.existsById(1L)).thenReturn(true);
        doNothing().when(userRepository).deleteById(1L);

        assertDoesNotThrow(() -> adminService.deleteUser(1L));

        verify(userRepository, times(1)).deleteById(1L);
    }
}
