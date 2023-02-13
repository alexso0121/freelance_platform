package com.example.OrderService.Service;

import com.example.OrderService.Entity.DashBroad;
import com.example.OrderService.Entity.JobOrder;
import com.example.OrderService.Entity.User;
import com.example.OrderService.Repository.DashBroadRepository;
import com.example.OrderService.Repository.JobRepository;
import com.example.OrderService.Repository.UserRepository;
import com.example.OrderService.dto.ChatMessage;
import com.example.OrderService.dto.InfoResponse;
import com.example.OrderService.dto.NoticeRespond;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.MockConsumer;
import org.apache.kafka.clients.consumer.OffsetResetStrategy;
import org.apache.kafka.common.protocol.types.Field;
import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@SpringBootTest
@AutoConfigureMockMvc
@Slf4j
class DashbroadServiceTest {

    @Autowired
    private DashbroadService dashbroadService;

    @Autowired
    private DashBroadRepository dashBroadRepositoryReal;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JobRepository jobRepositoryReal;
    @MockBean
    private JobRepository jobRepository;


    @MockBean
    private kafkaProducer kafkaProducer;

    @MockBean
    private DashBroadRepository dashBroadRepository;

    @Autowired
    private UserCoreService userCoreService;


    @BeforeEach
    void setUp(){

        User user1=new User(0,"admin","admin",null,null,"yl","sd"
                ,"434","dsf",null,4,2,new ArrayList<>());
        User user2=new User(0,"alex","admin",null,null,"yl","sd"
                ,"434","dsf",null,4,2,new ArrayList<>());
        userRepository.save(user1);
        userRepository.save(user2);
        jobRepositoryReal.save(new JobOrder(0,1,"sda",null,null,null,0,3,false,0,null));
        log.info("init");
        //dashBroadRepository.save(new DashBroad(0,1,1,2,list,list));

    }



    @Test
    @Transactional
    void applyjob() {

        //Assertions.assertEquals("admin",userCoreService.findById(1).getUsername());

        when(kafkaProducer.sendNotice("You have successfully applied for job id:1",2,1)).thenReturn(true);

        when(dashBroadRepository.findByOrder_id(1)).thenReturn(mockDashBroad());
        userRepository.save(mockUser("admin"));
        String res=dashbroadService.Applyjob(1,1);
        Assertions.assertEquals("successfully added",res);

        DashBroad dashBroad=dashBroadRepository.findByOrder_id(1);


        assert dashBroad != null;
        Assertions.assertEquals("admin"
                ,dashBroad.getApplier_id().get(0).getUsername());


    }

   /* @Test
    @Transactional
    void showApplications() {
        User user2=new User(0,"alex","admin",null,null,"yl","sd"
                ,"434","dsf",null,4,2,new ArrayList<>());
        List<User> list=new ArrayList<>();
        list.add(user2);
        DashBroad dashBroad=new DashBroad(1,1,1,1,list,new ArrayList<>());
        when(dashBroadRepository.findByOrder_id(1)).thenReturn(dashBroad);
        when(userCoreService.getProfile(1)).thenReturn(RealUserCoreService.getProfile(1));
        Assertions.assertEquals("admin", dashbroadService.showApplications(1,1).get(0).getUsername());
    }*/

    private User mockUser(String Username){
       return new User(0,Username,"admin",null,null,"yl","sd"
                ,"434","dsf",null,4,2,new ArrayList<>());
    }
    private DashBroad mockDashBroad(){
        List<User> list=new ArrayList<>();
        list.add(mockUser("admin"));
        return new DashBroad(1,1,1,1,list,new ArrayList<>());
    }
    @Test
    @Transactional
    void acceptApplication() {
        User user1=new User(0,"admin","admin",null,null,"yl","sd"
                ,"434","dsf",null,4,2,new ArrayList<>());
        userRepository.save(user1);
        System.out.println(userCoreService.getProfile(1));
        Assertions.assertNotEquals(null,userCoreService.getProfile(1));
        List<User> list=new ArrayList<>();
        list.add(user1);
        DashBroad dashBroad=new DashBroad(1,1,1,1,list,new ArrayList<>());
        JobOrder job=new JobOrder(0,1,"sda",null,null,null,0,3,false,0,null);

        when(jobRepository.findById(1)).thenReturn(Optional.of(job));
        when(dashBroadRepository.findByOrder_id(1)).thenReturn(dashBroad);
        Assertions.assertEquals(true,dashBroad.getApplier_id().contains(user1));

        DashbroadService Spy=Mockito.spy(dashbroadService);

        doNothing().when(Spy).generateChat(any());

        Assertions.assertEquals(userCoreService.getProfile(3),Spy.acceptApplication(1,1,3));
    }
    @Test
    void deleteApplication() {

    }

    @Test
    void showAccepted() {
    }
}
