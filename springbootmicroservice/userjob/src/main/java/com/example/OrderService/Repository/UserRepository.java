package com.example.OrderService.Repository;

import com.example.OrderService.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.UUID;

//sql communicating with User table
@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
    @Query(value="SELECT * FROM user_info WHERE username=?1",nativeQuery = true)
    User findUserbyName(String username);

    @Query(value = "SELECT * FROM user_info WHERE email=?1",nativeQuery = true)
    User findUserbyEmail(String email);



}
