package com.example.OrderService.Service;

import com.example.OrderService.Entity.DashBroad;
import com.example.OrderService.Entity.JobOrder;
import com.example.OrderService.Entity.User;
import com.example.OrderService.Repository.DashBroadRepository;
import com.example.OrderService.Repository.JobRepository;
import com.example.OrderService.Repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.internal.runners.JUnit4ClassRunner;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;



@Slf4j
@ExtendWith(MockitoExtension.class)
class DashbroadServiceTest {

    @InjectMocks
    private DashbroadService dashbroadService;
    @Mock
    private UserRepository userRepository;

    @Mock
    private JobRepository jobRepository;


   @Mock
   private DashBroadRepository dashBroadRepository;
    @Mock
    private UserCoreService userCoreService;

    @Mock
    private JobService jobService;


    /*@Before
    public void init(){
        MockitoAnnotations.openMocks(this);
    }*/


    @Test
    void applyjob() {
        User mockuser=mockUser("admin");

        //Assertions.assertEquals("admin",userCoreService.findById(1).getUsername());
        DashbroadService Spy=spy(dashbroadService);
        doReturn(true).when(Spy).sendNotice("You have successfully applied for job id:1",1,1);
        doReturn(true).when(Spy).postApplication(mockuser,1);

        when(userCoreService.findById(1)).thenReturn(mockuser);
        when(dashBroadRepository.findByOrder_id(1)).thenReturn(mockDashBroad(false));
        when(dashBroadRepository.save(any())).thenReturn(mockDashBroad(true));


        System.out.println(userRepository.findById(1));
        String res=Spy.Applyjob(1,1);
        System.out.println(res);
        Assertions.assertEquals("successfully added",res);


    }


    private User mockUser(String Username){
       return new User(1,Username,"admin",null,null,"yl","sd"
                ,"434","dsf",null,4,2,new ArrayList<>());
    }
    private DashBroad mockDashBroad(Boolean haveapplicaiton){
        List<User> list=new ArrayList<>();
        list.add(mockUser("admin"));
        if (haveapplicaiton){
        return new DashBroad(1,1,1,1,list,new ArrayList<>());}
        return new DashBroad(1,1,1,1,new ArrayList<>(),new ArrayList<>());
    }
    @Test
    void acceptApplication() {
        User user1=new User(0,"admin","admin",null,null,"yl","sd"
                ,"434","dsf",null,4,2,new ArrayList<>());

        List<User> list=new ArrayList<>();
        list.add(user1);
        DashBroad dashBroad=new DashBroad(1,1,1,1,list,new ArrayList<>());
        JobOrder job=new JobOrder(0,1,"sda",null,null,null,0,3,false,0,null);

        when(dashBroadRepository.findByOrder_id(1)).thenReturn(dashBroad);
        Assertions.assertEquals(true,dashBroad.getApplier_id().contains(user1));

        DashbroadService Spy=Mockito.spy(dashbroadService);



        Assertions.assertEquals(userCoreService.getProfile(3),Spy.acceptApplication(1,1,3));
    }
    @Test
    void postApplication(){
        User user=mockUser("admin");
        JobOrder job=JobOrder.builder().order_id(1).Title("test").build();

        when(jobService.findByOrderid(1)).thenReturn(job);
        when(userCoreService.saveAndReturn(user)).thenReturn(user);


        Assertions.assertEquals(true,dashbroadService.postApplication(user,1));

    }
    @Test
    void postApplicationWithError(){

        User user=mockUser("admin");
        JobOrder job=JobOrder.builder().order_id(1).Title("test").build();
        DashbroadService Spy=spy(dashbroadService);

        doReturn(true).when(Spy)
                .sendNotice("You have already send to much of applications.Please delete some and try again"
                ,1,1);

       /* when(jobService.findByOrderid(1)).thenReturn(job);
        when(userCoreService.saveAndReturn(user)).thenReturn(user);*/

        for(int i=0;i<20;i++){
            user.getApplications().add(job);
        }
        System.out.println(user.getApplications().size());

        Assertions.assertEquals(false,Spy.postApplication(user,1));


    }

    @Test
    void deleteApplication() {

    }

    @Test
    void showAccepted() {
    }
}
