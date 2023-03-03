package com.example.OrderService.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

//class for handling request for api involving application
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApplicationRequest {
    private int order_id;
    private UUID poster_id;
    private UUID apply_id;
}