package com.example.OrderService.Repository;

import com.example.OrderService.Entity.DashBroad;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface DashBroadRepository extends JpaRepository<DashBroad,Integer> {

     @Query(value = "select * from dash_board where order_id= ?1 ;",nativeQuery = true)
     DashBroad findByOrder_id(int order_id);

     //DashBroad findByPoster_id(int poster_id);
}
