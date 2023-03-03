package com.springboot.sohinalex.java.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;


@AllArgsConstructor
@NoArgsConstructor
@Data
public class AuthResponse {

    private UUID user_id;
    private String token;



}
