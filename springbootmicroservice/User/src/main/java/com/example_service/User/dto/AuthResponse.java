package com.example_service.User.dto;

import lombok.Data;

@Data
public class AuthResponse {
    private String token;
    private int id;
}
