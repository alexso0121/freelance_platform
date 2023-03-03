package com.springboot.sohinalex.java.Model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class user_info {

   
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