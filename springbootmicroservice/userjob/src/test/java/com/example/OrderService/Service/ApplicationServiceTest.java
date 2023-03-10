package com.example.OrderService.Service;

import com.example.OrderService.Entity.Application;
import com.example.OrderService.Entity.JobOrder;
import com.example.OrderService.Entity.User;
import com.example.OrderService.Repository.ApplicationRepository;
import com.example.OrderService.Repository.JobRepository;
import com.example.OrderService.Repository.UserRepository;
import com.example.OrderService.dto.InfoResponse;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.autoconfigure.batch.BatchProperties;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.Mockito.*;



@Slf4j
@ExtendWith(MockitoExtension.class)
class ApplicationServiceTest {

    @InjectMocks
    private ApplicationService applicationService;
    @Mock
    private UserRepository userRepository;

    @Mock
    private JobRepository jobRepository;


   @Mock
   private ApplicationRepository applicationRepository;
    @Mock
    private UserCoreService userCoreService;

    @Mock
    private JobService jobService;

    private final UUID id1= UUID.fromString("6b6ca058-b904-11ed-afa1-0242ac120002");

    private final UUID id2=UUID.randomUUID();


    /*@Before
    public void init(){
        MockitoAnnotations.openMocks(this);
    }*/

    private User mockUser(String Username){
        return User.builder().username(Username).build();
    }
    private Application mockDashBroad(Boolean isaccepted){
        if (isaccepted){
            return Application.builder().order_id(1).apply_id(id1).isaccepted(true).build() ;}
        return Application.builder().order_id(1).apply_id(id1).isaccepted(true).build();
    }


    @Test
    void applyjob() {
        User mockuser=mockUser("admin");

        //Assertions.assertEquals("admin",userCoreService.findById(1).getUsername());
        ApplicationService Spy=spy(applicationService);
        doNothing().when(Spy).sendNotice("You have successfully applied for job id:1",id1);
        doNothing().when(Spy).sendNotice("You have received an application for job id:1",id1);

        doReturn(false).when(Spy).TooMuchApplication(id1);

        when(applicationRepository.save(any())).thenReturn(mockDashBroad(true));


        System.out.println(userRepository.findById(id1));
        ResponseEntity<String> res=Spy.Applyjob(1,id1,id1);
        System.out.println(res);
        Assertions.assertEquals("successfully added",res.getBody());


    }
    @Test
    void AlreadyApplyJob(){
        when(applicationRepository.findByApply_idAndOrder_id(id1,1))
                .thenReturn(mockDashBroad(false));
        ResponseEntity<String> res=applicationService.Applyjob(1,id1,id2);
        Assertions.assertEquals(null,res.getBody());
    }
    @Test
    void NottoomuchJobs(){
        Application singleApplication=Application.builder().order_id(1)
                        .apply_id(id1).build();
      when(applicationRepository.findByApply_id(id1)).thenReturn(List.of( singleApplication));
      Boolean res=applicationService.TooMuchApplication(id1);
      Assertions.assertEquals(false,res);
    }

    @Test
    void toomuchJobs(){
        ApplicationService Spy=spy(applicationService);
        Application singleApplication=Application.builder().order_id(1)
                .apply_id(id1).build();
        List<Application> applicationList=new ArrayList<>();
        for(int i=0;i<=20;i++){
            applicationList.add(singleApplication);
        }
        doNothing().when(Spy).sendNotice(any(),eq(id1));
        when(applicationRepository.findByApply_id(id1)).thenReturn(applicationList);
        Boolean res=Spy.TooMuchApplication(id1);
        Assertions.assertEquals(true,res);
    }



    @Test
    void acceptApplication() {



        Application application =Application.builder().order_id(1).apply_id(id1).isaccepted(false).build();

       when(applicationRepository.findByApply_idAndOrder_id(id1,1)).thenReturn(application);
       when(jobRepository.findById(1)).thenReturn(Optional.ofNullable(JobOrder.builder().order_id(1).Title("job1").build()));

        ApplicationService Spy=Mockito.spy(applicationService);
        doNothing().when(Spy).sendNotice("You have successfully accepted the application for job id:1",id1);


        doNothing().when(Spy).sendNotice("Your application for Job with title 'job1' has been accepted",id1) ;

        when(userCoreService.findById(id1)).thenReturn(mockUser("admin"));

        doNothing().when(Spy).generateChat(any());


        Assertions.assertEquals(userCoreService.getProfile(id1),Spy.acceptApplication(1,id1,id1).getBody());
    }


    //test when accepted, application_number reduced by 1
    @Test
    void HandleRemainNumber(){

        JobOrder job= JobOrder.builder().order_id(1).application_number(10).build();


        JobOrder res=applicationService.HandleRemain_number(job);

        Assertions.assertEquals(9,res.getApplication_number());

    }
    //test when application_number=0,job is closed
    @Test
    void HandleIfNoRemainNumber(){
        ApplicationService Spy=spy(applicationService);
        JobOrder job= JobOrder.builder().user_id(id1).order_id(1).application_number(1).build();
        User user= User.builder().id(id1).username("admin").build();

        when(userCoreService.findById(id1)).thenReturn(user);
        doNothing().when(Spy).sendNotice("You have recruited enough amount of employee!The job will be closed",id1);
        doNothing().when(Spy).generateChat(any());

        JobOrder res=Spy.HandleRemain_number(job);
        Assertions.assertEquals(true,res.isClosed());
    }}



    /*@Test
    void showAccepted() {
        Application application1 =Application.builder().order_id(1).apply_id(1).isaccepted(true).build();
        Application application2 =Application.builder().order_id(1).apply_id(2).isaccepted(true).build();
        Application application3 =Application.builder().order_id(1).apply_id(1).isaccepted(false).build();

        when(applicationRepository.findByOrder_id(1)).thenReturn(List.of(application1,application2,application3));
        List<InfoResponse> res=applicationService.showAccepted();
    }
}*/
