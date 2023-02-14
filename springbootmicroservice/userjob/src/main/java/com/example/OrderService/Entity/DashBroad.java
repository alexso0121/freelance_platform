package com.example.OrderService.Entity;

import com.example.OrderService.dto.InfoResponse;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "dash_board")
public class DashBroad {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int dashbroad_id;
    private int order_id;

    private int poster_id;  //user_id who post the job

    private int application_remain;

    @OneToMany(targetEntity = User.class,cascade = CascadeType.ALL)
    @JoinColumn(name="UserApply_fk",referencedColumnName = "dashbroad_id")
    private List<User> applier_id=new ArrayList<>(20); //user_id who want to apply the job

    @OneToMany(targetEntity = User.class,cascade = CascadeType.ALL)
    @JoinColumn(name="UserAccept",referencedColumnName = "dashbroad_id")
    private List<User> accepted_id=new ArrayList<>(20);  //user_id who are accepted by the poster

}
