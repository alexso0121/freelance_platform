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
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@Slf4j
@RunWith(SpringRunner.class)
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

    /*@Container
    private static MySQLContainer container=new MySQLContainer("mysql:8.0.26");

    @DynamicPropertySource
    public static void setProperties(DynamicPropertyRegistry dynamicPropertyRegistry){
        dynamicPropertyRegistry.add("spring.datasource.url",container::getJdbcUrl);
        dynamicPropertyRegistry.add("spring.datasource.username",container::getUsername);
        dynamicPropertyRegistry.add("spring.datasource.password",container::getPassword);
    }*/


    @BeforeEach
    public void setUp(){
        log.info("inti");
        jobRepository.save(Mockjob(1,"job1"));
        userRepository.save(mockUser(0,"admin"));
        applicationRepository.save(
                Application.builder().apply_id(1).order_id(1).build());


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
    void getsinglejob() throws Exception {


        mockMvc.perform(MockMvcRequestBuilders.get("/UserJob/Job/get/{order_id}",1)
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

    void getAllJobs() throws Exception {

        jobRepository.save(Mockjob(1,"job2"));
        userRepository.save(mockUser(0,"admin"));
        mockMvc.perform(MockMvcRequestBuilders.get("/UserJob/Jobs/User/{user_id}",1)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                )

                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title")
                        .value("job1"))
                .andExpect(jsonPath("$[1].title")
                        .value("job2"))
                .andDo(print());
            tear();
    }

    @Test

    void editJobCheckisedit() throws Exception {

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

    void showApplications() throws Exception {
        applicationRepository.save(Application.builder()
                .order_id(1).apply_id(1).isaccepted(true).build());

        ApplicationRequest requestDto=new ApplicationRequest(1,1,0);
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
    void removeApplication() throws Exception {
        Application BeforeRemove= applicationRepository.findById(1).orElse(null);
        assert BeforeRemove != null;
        Assertions.assertEquals(1,BeforeRemove.getOrder_id());

        ApplicationRequest requestDto=new ApplicationRequest(1,1,1);
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

       Application AfterRemove= applicationRepository.findById(1).orElse(null);
        Assertions.assertNull(AfterRemove);

        tear();
    }

    //check if only accepted application will be return
    @Test
    void showAccept() throws Exception {

        userRepository.save(mockUser(0,"Leo"));     //with id 2
        userRepository.save(mockUser(0,"alex"));    //with id 3

        //Admin's application with accepted
        applicationRepository.save(Application.builder().order_id(1).apply_id(1).isaccepted(true).build());
        //Leo's application with not accepted
        applicationRepository.save(Application.builder().order_id(1).apply_id(2).isaccepted(false).build());
        //alex's application with accepted
        applicationRepository.save(Application.builder().order_id(1).apply_id(3).isaccepted(true).build());

        //add accept
        ApplicationRequest res=new ApplicationRequest(1,1,1);
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
    void showApplicationshistory() throws Exception {
        jobRepository.save(Mockjob(1,"job2title"));
        applicationRepository.save(Application.builder().apply_id(1).order_id(2).build());

        mockMvc.perform(get("/UserJob/application/history/{id}", 1)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("job1"))
                .andExpect(jsonPath("$[1].title").value("job2title"))
                .andDo(print());

        userRepository.deleteById(1);
    }

}