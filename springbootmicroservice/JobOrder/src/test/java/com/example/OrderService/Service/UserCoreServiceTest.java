package com.example.OrderService.Service;

import com.example.OrderService.Entity.User;
import com.example.OrderService.Repository.UserRepository;
import com.example.OrderService.dto.InfoResponse;
import org.junit.Before;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MockMvcBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class UserCoreServiceTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserCoreService userCoreService;

    @Autowired
    private MockMvc mockMvc;





    private User mockUser(){
        return new User(0,"admin","admin",null,null,"yl","sd"
                ,"434","dsf",null,0,2,null);
    }

    @Test
    void getProfile()  {
        InfoResponse response=userCoreService.getProfile(1);
        Assertions.assertEquals("admin",response.getUsername());
    }



    @Test //check case if already have the username
    void updateUserRepeatedUsername() {
        userRepository.save(mockUser());
        userRepository.save(new User(0,"alex","admin",null,null,"yl","sd"
                ,"434","dsf",null,0,2,null));
        User update=new User(1,"alex","admin",null,null,"yl","sd"
                ,"434","dsf",null,0,2,null);

        String res=userCoreService.updateUser(update);
        Assertions.assertEquals("the username has already been used",res);
    }


    @Test
    void verifyCanOrder() {
        User user=userRepository.save(mockUser());
        Boolean ScoreLowerThan2=userCoreService.VerifyCanOrder(1);
        Assertions.assertEquals(false,ScoreLowerThan2);

        user.setScore(3);
        userRepository.save(user);
        Boolean ScoreHigherThan2 =userCoreService.VerifyCanOrder(1);
        Assertions.assertEquals(true,ScoreHigherThan2);



    }
}