package com.example.OrderService.Repository;

import com.example.OrderService.Entity.JobOrder;
import com.example.OrderService.dto.Response;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface JobRepository extends JpaRepository<JobOrder, Integer> {

     @Query(
             value = "SELECT * "+
                     " FROM job_order " +
                     " WHERE title= ?1 ;",nativeQuery = true
     )
     JobOrder findByTitle(String title);
     @Query(
             value = "SELECT region,order_id,title,description,requirement,salary,user_info.username,user_info.contact,date"+
                     " FROM job_order " +
                     " FULL JOIN user_info " +
                     " ON user_id=user_info.id" +
                     " WHERE order_id= ?1 ;",nativeQuery = true
     )
     Response singlejob(int order_id);

     @Query(
             value = "SELECT region,order_id,title,description,requirement,salary,user_info.username,user_info.contact,date"+
                     " FROM job_order " +
                     " FULL JOIN user_info " +
                     " ON user_id=user_info.id" +
                     " WHERE user_info.id= ?1 ORDER BY date DESC;",nativeQuery = true
     )
     Collection<Response> getUserJobs(int userId);
     @Query(
             value = "SELECT region,order_id,title,description,requirement,salary,user_info.username,user_info.contact,date"+
                     " FROM job_order " +
                     " FULL JOIN user_info " +
                     " ON user_id=user_info.id" +
                     " WHERE region_id = ?1 ORDER BY date DESC;",nativeQuery = true
     )
     Collection<Response> getRegionJobs(int region_id);


     @Query(
             value = "SELECT region,order_id,title,description,requirement,salary,user_info.username,user_info.contact,date"+
                     " FROM job_order " +
                     " FULL JOIN user_info " +
                     " ON user_id=user_info.id" +
                     " ORDER BY date DESC;",nativeQuery = true
     )
     Collection<Response> getAllJobs();
}
