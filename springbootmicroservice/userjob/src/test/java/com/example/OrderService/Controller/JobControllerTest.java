package com.example.OrderService.Controller;

import com.example.OrderService.Entity.Application;
import com.example.OrderService.Entity.JobOrder;
import com.example.OrderService.Entity.User;
import com.example.OrderService.Repository.ApplicationRepository;
import com.example.OrderService.Repository.JobRepository;
import com.example.OrderService.Repository.UserRepository;
import com.example.OrderService.dto.ApplicationRequest;
import com.example.OrderService.dto.JobRequestDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.ArrayList;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc

@Slf4j
//@RunWith(SpringRunner.class)
@Testcontainers
class JobControllerTest   {

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ApplicationRepository applicationRepository;
    @Autowired
    private UserRepository userRepository;

    @Container
    private static MySQLContainer container=new MySQLContainer("mysql:8.0.26")
            ;

    @DynamicPropertySource
    public static void setProperties(DynamicPropertyRegistry dynamicPropertyRegistry){
        dynamicPropertyRegistry.add("spring.datasource.url",container::getJdbcUrl);
        dynamicPropertyRegistry.add("spring.datasource.username",container::getUsername);
        dynamicPropertyRegistry.add("spring.datasource.password",container::getPassword);
    }


    @BeforeEach
    public void setUp(){
        log.info("inti");



    }


    public void tear(){
        log.info("tear");
        userRepository.deleteAll();
        applicationRepository.deleteAll();
        jobRepository.deleteAll();
    }

    private JobOrder Mockjob(int user_id,String Title){
        JobOrder job=JobOrder.builder().user_id(user_id).Title(Title).build();
        return job;
    }
    private User mockUser(int id,String username){

        User user= User.builder().id(id).username(username).build();
       /* user.getApplications().add(mockJobOrder("job1title","b"));
        user.getApplications().add(mockJobOrder("job2title","d"));*/
        return user;
    }




    @Test
    @Transactional
    void getsinglejob() throws Exception {

        JobOrder job=jobRepository.save(Mockjob(1,"job1"));
        User user=userRepository.save(mockUser(0,"admin"));
        applicationRepository.save(
                Application.builder().apply_id(user.getId()).order_id(job.getOrder_id()).build());

        mockMvc.perform(MockMvcRequestBuilders.get("/UserJob/Job/get/{order_id}",job.getOrder_id())
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                )
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.title")
                        .value("job1"))
                .andDo(print());
            tear();

    }

    @Test
    @Transactional
    void getAllJobs() throws Exception {
        JobOrder job1=jobRepository.save(Mockjob(1,"job1"));
        User user=userRepository.save(mockUser(0,"admin"));
        applicationRepository.save(
                Application.builder().apply_id(user.getId()).order_id(job1.getOrder_id()).build());

        JobOrder job2=jobRepository.save(Mockjob(1,"job2"));
        User user2=userRepository.save(mockUser(0,"admin"));
        applicationRepository.save(
                Application.builder().apply_id(user2.getId()).order_id(job2.getOrder_id()).build());
        /*jobRepository.save(Mockjob(1,"job2"));
        userRepository.save(mockUser(0,"admin"));*/
        mockMvc.perform(MockMvcRequestBuilders.get("/UserJob/jobs/all")
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                )

                .andExpect(status().isOk())
                .andExpect(jsonPath("$[1].title")
                        .value("job1"))
                .andExpect(jsonPath("$[4].title")
                        .value("job2"))
                .andDo(print());
            tear();
    }

    @Test
    @Transactional
    void editJobCheckisedit() throws Exception {
        JobOrder job=jobRepository.save(Mockjob(1,"job1"));
        User user=userRepository.save(mockUser(0,"admin"));
        applicationRepository.save(
                Application.builder().apply_id(user.getId()).order_id(job.getOrder_id()).build());


        JobRequestDto requestDto=JobRequestDto.builder().order_id(1).title("updatejob").build();
        String request= objectMapper.writeValueAsString(requestDto);


        mockMvc.perform(MockMvcRequestBuilders.put("/UserJob/job/edit")
                .contentType(MediaType.APPLICATION_JSON)
                .content(request).accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("updatejob"))
                .andDo(print());
        tear();
        System.out.println(userRepository.findById(1));
    }



    @Test
    @Transactional
    void showApplications() throws Exception {
        JobOrder job=jobRepository.save(Mockjob(1,"job1"));
        User user=userRepository.save(mockUser(0,"admin"));
        /*applicationRepository.save(
                Application.builder().apply_id(user.getId()).order_id(job.getOrder_id()).build());
*/
        applicationRepository.save(Application.builder()
                .order_id(job.getOrder_id()).apply_id(user.getId()).isaccepted(true).build());

        ApplicationRequest requestDto=new ApplicationRequest(job.getOrder_id(), user.getId(), 0);
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

        tear();
    }



    @Test
    @Transactional
    void removeApplication() throws Exception {
        JobOrder job=jobRepository.save(Mockjob(1,"job1"));
        User user=userRepository.save(mockUser(0,"admin"));
        applicationRepository.save(
                Application.builder().apply_id(user.getId()).order_id(job.getOrder_id()).build());

        Application BeforeRemove= applicationRepository.findById(user.getId()).orElse(null);
        assert BeforeRemove != null;


        ApplicationRequest requestDto=new ApplicationRequest(job.getOrder_id(), user.getId(), user.getId());
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

       Application AfterRemove= applicationRepository.findById(user.getId()).orElse(null);
        Assertions.assertNull(AfterRemove);

        tear();
    }

    //check if only accepted application will be return
    @Test
    @Transactional
    void showAccept() throws Exception {
        JobOrder job=jobRepository.save(Mockjob(1,"job1"));
        User user=userRepository.save(mockUser(0,"admin"));
        applicationRepository.save(
                Application.builder().apply_id(user.getId()).order_id(job.getOrder_id()).build());


        User user2=userRepository.save(mockUser(0,"Leo"));     //with id 2
        User user3=userRepository.save(mockUser(0,"alex"));    //with id 3

        //Admin's application with accepted
        applicationRepository.save(Application.builder().order_id(job.getOrder_id()).apply_id(user.getId()).isaccepted(true).build());
        //Leo's application with not accepted
        applicationRepository.save(Application.builder().order_id(job.getOrder_id()).apply_id(user2.getId()).isaccepted(false).build());
        //alex's application with accepted
        applicationRepository.save(Application.builder().order_id(job.getOrder_id()).apply_id(user3.getId()).isaccepted(true).build());

        //add accept
        ApplicationRequest res=new ApplicationRequest(job.getOrder_id(), 1,1);
        String request=objectMapper.writeValueAsString(res);




        mockMvc.perform(MockMvcRequestBuilders.post("/UserJob/Accept/show")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].username")
                        .value("admin"))
                .andExpect(jsonPath("$[1].username")        //Leo's application is excluded
                        .value("alex"))
                .andDo(print())
               ;


        tear();
    }
    @Test
    @Transactional
    void showApplicationshistory() throws Exception {
        JobOrder job=jobRepository.save(Mockjob(1,"job1"));
        User user=userRepository.save(mockUser(0,"admin"));
        applicationRepository.save(
                Application.builder().apply_id(user.getId()).order_id(job.getOrder_id()).build());

        JobOrder job2=jobRepository.save(Mockjob(1,"job2title"));
        applicationRepository.save(Application.builder().apply_id(user.getId()).order_id(job2.getOrder_id()).build());

        mockMvc.perform(get("/UserJob/application/history/{id}", user.getId())
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("job1"))
                .andExpect(jsonPath("$[1].title").value("job2title"))
                .andDo(print());


        tear();
    }

}