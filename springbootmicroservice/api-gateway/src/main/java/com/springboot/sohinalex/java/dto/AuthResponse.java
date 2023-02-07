package com.springboot.sohinalex.java.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@AllArgsConstructor
@NoArgsConstructor
@Data
public class AuthResponse {

    private Integer user_id;
    private String token;



}
