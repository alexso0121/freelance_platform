package com.example.OrderService.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

//class for handling request for api involving application
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApplicationRequest {
    private int order_id;
    private int poster_id;
    private int apply_id;
}
