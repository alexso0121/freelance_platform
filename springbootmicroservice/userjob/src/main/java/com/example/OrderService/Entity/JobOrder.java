package com.example.OrderService.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

//main class for storing data in job
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name="JobOrder")
public class JobOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int order_id;
    private int user_id;
    private String Title;
    private String Description;
    private String Requirement;
    private String contact;
    private double salary;
    private String organization;
    private int address_id;
    private boolean isClosed =false;
    private String date;
    private int application_number=1;
    private String region;
    /*@OneToMany(targetEntity = Application.class,cascade = CascadeType.ALL)
    @JoinColumn(name="JobApply_fk",referencedColumnName = "order_id")
    private List<Application> applicationList=new ArrayList<>();

*/




}
