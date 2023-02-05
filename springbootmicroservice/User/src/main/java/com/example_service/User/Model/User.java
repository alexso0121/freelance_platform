package com.example_service.User.Model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Value;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "user_info")
public class User {

    @Id
    @GeneratedValue
    private int id;
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
