package com.example.OrderService.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Table(name = "location")
public class Location {
    @Id
    @GeneratedValue
    private int id;
    private int address_id;
    private String region;

    public Location(int address_id, String region) {
        this.address_id=address_id;
        this.region=region;
    }
}
