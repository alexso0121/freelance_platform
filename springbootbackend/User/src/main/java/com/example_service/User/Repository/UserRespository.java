package com.example_service.User.Repository;

import com.example_service.User.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRespository extends JpaRepository<User,Integer> {
    @Query("SELECT * FROM user_info WHERE Name=?1")
    Optional<User> findUserbyName(String Name);

    @Query("SELECT * FROM user_info WHERE email=?1")
    Optional<User> findUserbyEmail(String email);

}
