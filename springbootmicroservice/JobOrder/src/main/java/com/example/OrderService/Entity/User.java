package com.example.OrderService.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Value;

import java.util.ArrayList;
import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="user_info")
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
    @OneToMany(targetEntity = JobOrder.class,cascade = CascadeType.ALL)
    @JoinColumn(name="UserJob_fk",referencedColumnName = "id")
    private List<JobOrder> applications=new ArrayList<>(20); //Application that User_applied


}
