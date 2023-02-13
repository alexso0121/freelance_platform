package com.example.OrderService.Controller;

import com.example.OrderService.Entity.DashBroad;
import com.example.OrderService.Entity.JobOrder;
import com.example.OrderService.Entity.User;
import com.example.OrderService.Repository.DashBroadRepository;
import com.example.OrderService.Repository.JobRepository;
import com.example.OrderService.Repository.UserRepository;
import com.example.OrderService.dto.DashRequestDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.junit.After;
import org.junit.Before;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.event.annotation.BeforeTestExecution;
import org.springframework.test.context.event.annotation.BeforeTestMethod;
import org.springframework.test.context.transaction.BeforeTransaction;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@Slf4j
class JobControllerTest {

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private DashBroadRepository dashBroadRepository;
    @Autowired
    private UserRepository userRepository;


    @BeforeEach
    public void setUp(){
        log.info("inti");
        jobRepository.save(Mockjob(1,"job1"));
        userRepository.save(mockUser(0,"admin"));
        List<User> list=new ArrayList<>();
        User user=userRepository.findById(1).orElse(null);
        list.add(user);
        List<User> list2=new ArrayList<>();
        list2.add(user);
        dashBroadRepository.save(new DashBroad(0,1,1,1,list,list2));


    }
    @AfterEach
    public void tear(){
        log.info("tear");

    }

    private JobOrder Mockjob(int user_id,String Title){
        JobOrder job=new JobOrder(0,user_id,Title,null,null,null,0,3,false,0,null);
        return job;
    }
    private User mockUser(int id,String username){

        User user= new User(id,username,"admin",null,null,"yl","sd"
                ,"434","dsf",null,4,2,new ArrayList<>());
       /* user.getApplications().add(mockJobOrder("job1title","b"));
        user.getApplications().add(mockJobOrder("job2title","d"));*/
        return user;
    }



    @Test
    @Transactional
    void getsinglejob() throws Exception {
      /*  jobRepository.save(Mockjob(1,"job1"));
        userRepository.save(mockUser(0,"admin"));*/

        log.info("test");
        mockMvc.perform(MockMvcRequestBuilders.get("/UserJob/Job/get/{order_id}",1)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                )

                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.title")
                        .value("job1"))
                .andExpect(jsonPath("$.username")
                        .value("admin"))
                .andDo(print());

        jobRepository.deleteAll();
        userRepository.deleteAll();

    }

    @Test
    @Transactional
    void getAllJobs() throws Exception {
        jobRepository.save(Mockjob(1,"job2"));
        userRepository.save(mockUser(0,"admin"));
        mockMvc.perform(MockMvcRequestBuilders.get("/UserJob/Jobs/User/{user_id}",1)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                )

                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title")
                        .value("job1"))
                .andExpect(jsonPath("$[0].username")
                        .value("admin"))
                .andExpect(jsonPath("$[1].title")
                        .value("job2"))
                .andExpect(jsonPath("$[1].username")
                        .value("admin"))
                .andDo(print());
        jobRepository.deleteAll();
        userRepository.deleteAll();
    }



    @Test
    @Transactional
    void showApplications() throws Exception {

        DashRequestDto requestDto=new DashRequestDto(1,1,0);
        String request=objectMapper.writeValueAsString(requestDto);

        mockMvc.perform(MockMvcRequestBuilders.post("/UserJob/Applications/show")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].username")
                        .value("admin"))
                .andDo(print());

    }



    @Test
    @Transactional
    void removeApplication() throws Exception {

        DashBroad BeforeRemove= dashBroadRepository.findById(1).orElse(null);
        Assertions.assertEquals(1,BeforeRemove.getApplier_id().size());

        DashRequestDto requestDto=new DashRequestDto(1,1,1);
        String request=objectMapper.writeValueAsString(requestDto);

          mockMvc.perform(MockMvcRequestBuilders.delete("/UserJob/Applications/delete")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                )
                .andExpect(status().isOk())
                .andExpect(content().string("you have successfully removed"))
                .andDo(print())
                .andReturn();

       DashBroad AfterRemove= dashBroadRepository.findById(1).orElse(null);
        Assertions.assertEquals(0,AfterRemove.getApplier_id().size());

    }

    @Test
    @Transactional
    void showAccept() throws Exception {
        //add accept
        DashRequestDto res=new DashRequestDto(1,1,1);
        String request=objectMapper.writeValueAsString(res);

        mockMvc.perform(MockMvcRequestBuilders.post("/UserJob/Accept/show")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].username")
                        .value("admin"))
                .andDo(print());


    }
}