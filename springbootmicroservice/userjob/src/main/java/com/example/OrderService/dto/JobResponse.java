package com.example.OrderService.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

//response for jobs showing to the client
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JobResponse {

    private int order_id;
    private String title;
    private String description;
    private String requirement;
    private double salary;
    private String organization;
    private String date;
    private String region;
    private int application_number;




}