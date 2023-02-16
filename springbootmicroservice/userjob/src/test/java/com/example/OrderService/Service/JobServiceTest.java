package com.example.OrderService.Service;

import com.example.OrderService.Entity.JobOrder;
import com.example.OrderService.Repository.DashBroadRepository;
import com.example.OrderService.Repository.JobRepository;
import com.example.OrderService.Repository.LocationRepository;
import com.example.OrderService.dto.Response;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.mockito.Mockito.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class JobServiceTest {
    @InjectMocks
    private JobService jobService;

    @Mock
    private UserCoreService userCoreService;

    @Mock
    private JobRepository jobRepository;

    @Mock
    private LocationRepository locationRepository;

    @Mock
    private DashBroadRepository dashBroadRepository;


    @Test
    void postValidJob() throws IllegalAccessException {
        JobOrder jobOrder=JobOrder.builder().order_id(1).user_id(1).Title("demo").address_id(1).build();
        Response ans= Response.builder().order_id(1)
                .title("demo").region("Yuen Long").build();
        JobService Spy=spy(jobService);

        when(jobRepository.findByTitleAndOrganization(any(),any())).thenReturn(null);
        when(userCoreService.VerifyCanOrder(1)).thenReturn(true);
        when(locationRepository.getLocation(1)).thenReturn("Yuen Long");
        when(jobRepository.save(any())).thenReturn(jobOrder);
        doNothing().when(Spy).sendNotice("The Job with order id (1) has successfully posted !",1);

        Assertions.assertEquals(ans,Spy.postJob(jobOrder));
    }

    @Test
    void GetJobsNotAccept(){
        JobOrder job1=JobOrder.builder().Title("job1").organization("google").address_id(1).isaccepted(false).build();
        JobOrder job2=JobOrder.builder().Title("job2").organization("facebook").address_id(1).isaccepted(false).build();

        when(jobRepository.getRegionJobs(1)).thenReturn(List.of(job1,job2));

        List<Response> res=jobService.getRegionJobs(1);
        Assertions.assertEquals(jobService.GetSingleJob(job1),res.get(0));
        Assertions.assertEquals(2,res.size());

    }

    @Test
    //test can api filter out the jobs who is accepted
    void getJobsAccepted() {

        JobOrder job1=JobOrder.builder().Title("job1").organization("google").address_id(1).isaccepted(true).build();
        JobOrder job2=JobOrder.builder().Title("job2").organization("facebook").address_id(1).isaccepted(false).build();

        when(jobRepository.getRegionJobs(1)).thenReturn(List.of(job1,job2));

        List<Response> res=jobService.getRegionJobs(1);
        Assertions.assertEquals(jobService.GetSingleJob(job2),res.get(0));
        Assertions.assertEquals(1,res.size());
    }

    @Test
    void findByOrderid() {
    }

    @Test
    void editJob() {
    }

    @Test
    void showApplications() {
    }

    @Test
    void postApplication() {
    }
}