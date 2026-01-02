package com.example.application.dto.response;

import com.example.application.entity.Role;
import lombok.Data;

@Data
public class UserResponse {
    private Long id;
    private String name;
    private String email;
    private String phoneNumber;
    private Long countryId;
    private String countryName;
    private Long stateId;
    private String stateName;
    private Long cityId;
    private String cityName;
    private Role role;
    private boolean passwordResetRequired;
}
