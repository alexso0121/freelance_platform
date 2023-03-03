package com.example.OrderService.dto;

import lombok.Data;

import java.util.UUID;

//class for the response for displaying user profile
@Data
public class InfoResponse {

    private UUID id;
    private String username;
    private String fullName;
    private String email;
    private String address;
    private String skillSet;

    private String contact;
    private String cv;





}


