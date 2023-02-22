package com.example.OrderService.Repository;

import com.example.OrderService.Entity.JobOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

//sql communicating with Job table
@Repository
public interface JobRepository extends JpaRepository<JobOrder, Integer> {

     @Query(
             value = "SELECT * "+
                     " FROM job_order " +
                     " WHERE title= ?1 AND organization=?2 ;",nativeQuery = true
     )
     JobOrder findByTitleAndOrganization(String title,String organization);

     @Query(
             value = "SELECT *"+
                     " FROM job_order " +
                     " WHERE user_id= ?1 ORDER BY date DESC;",nativeQuery = true
     )
     List<JobOrder> getUserJobs(int user_id);
     @Query(
             value = "SELECT *"+
                     " FROM job_order " +
                     " WHERE address_id = ?1 ORDER BY date DESC;",nativeQuery = true
     )
     List<JobOrder> getRegionJobs(int address_id);


     @Query(
             value = "SELECT *"+
                     " FROM job_order " +
                     " ORDER BY date DESC;",nativeQuery = true
     )
     List<JobOrder> getAllJobs();
}
