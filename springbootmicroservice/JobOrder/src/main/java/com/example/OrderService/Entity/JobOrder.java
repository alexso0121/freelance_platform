package com.example.OrderService.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name="JobOrder")
public class JobOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int order_id;
    private int user_id;
    private String Title;
    private String Description;
    private String Requirement;
    private Date date;
    private double salary;
    private int address_id;
    private boolean isaccepted=false;
    private int application_number=1;
    private String region;



}
