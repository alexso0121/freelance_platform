package com.example.OrderService.Repository;

import com.example.OrderService.Entity.Application;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

//sql communicating with Application table
@Repository
public interface ApplicationRepository extends JpaRepository<Application,Integer> {

     @Query(value = "select * from application where order_id= ?1 ;",nativeQuery = true)
     List<Application> findByOrder_id(int order_id);

     @Query(value = "select * from application where apply_id= ?1 and order_id= ?2 ;",nativeQuery = true)
     Application findByApply_idAndOrder_id(UUID apply_id, int order_id);

     @Query(value = "select * from application where apply_id= ?1 ;",nativeQuery = true)
     List<Application> findByApply_id(UUID apply_id);



}
