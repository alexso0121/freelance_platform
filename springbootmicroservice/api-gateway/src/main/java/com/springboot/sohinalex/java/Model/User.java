package com.springboot.sohinalex.java.Model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {

    private int id;
    private String username;
    private String password;
    private String fullName;
    private String email;

    private String skill_set;

    private String contact;
    private String cv;
    private int Address_id;

    private String address;


    private String role;

    private double score=3.0;



}