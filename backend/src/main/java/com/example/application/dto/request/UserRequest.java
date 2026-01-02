package com.example.application.dto.request;

import com.example.application.entity.Role;
import lombok.Data;

@Data
public class UserRequest {
    private String name;
    private String email;
    private String phoneNumber;
    private Long countryId;
    private Long stateId;
    private Long cityId;
    private Role role;
}
