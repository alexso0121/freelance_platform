package com.example.OrderService.Repository;

import com.example.OrderService.Entity.Location;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface LocationRepository extends JpaRepository<Location,Integer> {

    @Query(value = "SELECT region FROM location WHERE address_id = ?1 ;",nativeQuery = true)
    String getLocation(int address_id);
}
