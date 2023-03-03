package com.example.OrderService.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

//main class for storing data in application
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Table(name = "application")
public class Application {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int application_id;
    private int order_id;
    private boolean isaccepted=false;
    private UUID apply_id;

    /*private int poster_id;  //user_id who post the job

    private int application_remain;

    @OneToMany(targetEntity = User.class,cascade = CascadeType.ALL)
    @JoinColumn(name="UserApply_fk",referencedColumnName = "dashbroad_id")
    private List<User> applier_id=new ArrayList<>(20); //user_id who want to apply the job

    @OneToMany(targetEntity = User.class,cascade = CascadeType.ALL)
    @JoinColumn(name="UserAccept",referencedColumnName = "dashbroad_id")
    private List<User> accepted_id=new ArrayList<>(20);  //user_id who are accepted by the poster
*/
}
