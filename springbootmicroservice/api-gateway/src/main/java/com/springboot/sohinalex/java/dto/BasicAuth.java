package com.springboot.sohinalex.java.dto;

import jakarta.validation.constraints.Null;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
public class BasicAuth {
    @Null
    private int id;
    private String username;
    private String password;


}
