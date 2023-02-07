package com.example.OrderService.dto;

import lombok.Data;

@Data
public class AuthResponse {
    private String token;
    private int id;
}
