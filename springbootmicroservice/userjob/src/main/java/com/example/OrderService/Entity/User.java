package com.example.OrderService.Entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

//main class for data in user
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Table(name="user_info")
public class User {

    @Id
    @GeneratedValue
    private UUID id;
    private String username;
    private String password;
    private String fullName;
    private String email;
    private String address;
    private String skill_set;

    private String contact;
    private String cv;

    private String role;

    private double score=3.0;

    private int Address_id;

}
