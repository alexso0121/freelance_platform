package com.example_service.User.Repository;

import com.example_service.User.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRespository extends JpaRepository<User,Integer> {
    @Query(value="SELECT * FROM user_info WHERE username=?1",nativeQuery = true)
    User findUserbyName(String username);

    @Query(value = "SELECT * FROM user_info WHERE email=?1",nativeQuery = true)
    User findUserbyEmail(String email);



}
