package com.springboot.sohinalex.java.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SignupDto {
    private String username;
    private String password;
    private String fullName;
    private String email;

    private String skill_set;

    private String contact;
    private String cv;
    private int Address_id;

    private String address;
}
