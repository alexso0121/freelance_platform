package com.example_service.User.Model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;




@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "user_info")
public class User {

    @Id
    @GeneratedValue
    private int id;
    private String name;
    private String fullName;
    private String email;
    private String address;
    private String skillSet;

    private String contact;
    private String cv;

    private int Address_id;


}
