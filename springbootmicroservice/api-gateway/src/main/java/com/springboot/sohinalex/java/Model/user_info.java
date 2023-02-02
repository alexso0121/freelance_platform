package com.springboot.sohinalex.java.Model;



import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class user_info {

    @Id
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