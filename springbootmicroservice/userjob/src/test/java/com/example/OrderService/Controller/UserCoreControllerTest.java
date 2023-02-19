package com.example.OrderService.Controller;

import com.example.OrderService.Entity.JobOrder;
import com.example.OrderService.Entity.User;
import com.example.OrderService.Repository.UserRepository;
import com.example.OrderService.Service.UserCoreService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@AutoConfigureMockMvc
class UserCoreControllerTest  {


    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserCoreService userCoreService;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;
/*
   *//* @BeforeTestMethod
    public void setUp(){
        System.out.println("begin");
        userRepository.save(mockUser());
    }
    @AfterTestMethod
    public void tearOff(){
        System.out.println("end");
        userRepository.deleteById(1);
    }
*/
    private User mockUser(){

      User user=User.builder().id(1).username("admin").password("admin").build();
        return user;
    }

    private JobOrder mockJobOrder(String title,String des){
        //return new JobOrder(0,1,title,des,null,null,0,2,false,0,null);
        return JobOrder.builder()
                .order_id(1)
                .Title(title)
                .Description(des)
                .build();
    }





    @Test
    @Transactional
    void getProfile() throws Exception {
        userRepository.save(mockUser());
         mockMvc.perform(get("/UserJob/getProfile/{id}", 1)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                )

                .andExpect(status().isOk())
                 .andExpect(jsonPath("$.username")
                         .value("admin"))

                .andDo(print());

        userRepository.deleteById(1);
    }
    @Test
    @Transactional
    void getUserByName() throws Exception {
        userRepository.save(mockUser());
        mockMvc.perform(get("/UserJob/get/Byusername/{username}", "admin")
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("admin"));
        //if cant found user
        MvcResult whatever = mockMvc.perform(get("/UserJob/get/Byusername/{username}", "whatever")
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                )
                .andExpect(status().isOk())
                .andReturn();

        Assertions.assertEquals("",whatever.getResponse().getContentAsString());
        userRepository.deleteById(1);
    }


    @Test
    @Transactional
    void updateUser() throws Exception {
        userRepository.save(mockUser());
        User updated=User.builder().id(1).username("alex").build();
        String request=objectMapper.writeValueAsString(updated);

        MvcResult mvcResult=mockMvc.perform(MockMvcRequestBuilders.put("/UserJob/updateuser")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .content(request)
                )
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn();

        Assertions.assertEquals("Successful update",mvcResult.getResponse().getContentAsString());

        mockMvc.perform(get("/UserJob/getProfile/{id}", 1)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                )

                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username")
                        .value("alex"))

                .andDo(print());
        userRepository.deleteById(1);
    }






    @Test
    void addUser() throws Exception {
        User user=mockUser();
                user.setUsername("tom");


        String request=objectMapper.writeValueAsString(user);
        mockMvc.perform(MockMvcRequestBuilders.post("/UserJob/add/user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(request)
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("tom"));
        userRepository.deleteById(1);
    }



}