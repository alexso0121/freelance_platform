package com.example.OrderService.dto;

import lombok.Data;

@Data
public class JobRequestDto {
    private int order_id;
    private String title;
    private String description;
    private String requirement;
    private double salary;
    private int address_id;

}
