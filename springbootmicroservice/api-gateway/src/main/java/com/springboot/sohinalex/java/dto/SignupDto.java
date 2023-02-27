package com.springboot.sohinalex.java.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SignupDto {
    private String username;
    private String password;
    private String fullName;
    private String email;

    private String skill_set;

    private String contact;
    private String cv;
    private int Address_id;  //Jsonvalue ==address_id


}
