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
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.ArrayList;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
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
    @Container
    private static MySQLContainer container=new MySQLContainer("mysql:8.0.26")
            ;

    @DynamicPropertySource
    public static void setProperties(DynamicPropertyRegistry dynamicPropertyRegistry){
        dynamicPropertyRegistry.add("spring.datasource.url",container::getJdbcUrl);
        dynamicPropertyRegistry.add("spring.datasource.username",container::getUsername);
        dynamicPropertyRegistry.add("spring.datasource.password",container::getPassword);
    }

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

      User user=User.builder().id(1).username("admin")
              .password("admin").Address_id(1).build();
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
    void getProfile() throws Exception {
        User user =userRepository.save(mockUser());
         mockMvc.perform(get("/UserJob/getProfile/{id}", user.getId())
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                )

                .andExpect(status().isOk())
                 .andExpect(jsonPath("$.username")
                         .value("admin"))

                .andDo(print());


    }
    @Test
    void getUserByName() throws Exception {
        userRepository.deleteAll();
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

    }


    @Test
    void updateUser() throws Exception {
        User user=userRepository.save(mockUser());
        User updated=User.builder().id(user.getId()).username("alex").build();
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

        mockMvc.perform(get("/UserJob/getProfile/{id}", user.getId())
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                )

                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username")
                        .value("alex"))

                .andDo(print());

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
                .andExpect(jsonPath("$.username").value("tom"))
                .andExpect(jsonPath("$.address").value("Island"))
                .andExpect(jsonPath("$.score").value(3.0))
                .andDo(print());
        userRepository.deleteById(1);
    }



}