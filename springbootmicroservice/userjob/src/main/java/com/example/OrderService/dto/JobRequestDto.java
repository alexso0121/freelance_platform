package com.example.OrderService.dto;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class JobRequestDto {
    private int order_id;
    private UUID user_id;
    private String title;
    private String description;
    private String requirement;
    private double salary;
    private String contact;
    private int address_id;
    private int application_number;

}
