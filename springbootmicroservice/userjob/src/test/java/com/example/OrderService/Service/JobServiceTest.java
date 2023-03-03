package com.example.OrderService.Service;

import com.example.OrderService.Entity.JobOrder;
import com.example.OrderService.Repository.ApplicationRepository;
import com.example.OrderService.Repository.JobRepository;
import com.example.OrderService.Repository.LocationRepository;
import com.example.OrderService.dto.JobRequestDto;
import com.example.OrderService.dto.JobResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

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
    private ApplicationRepository applicationRepository;

    @Mock
    private LocationService locationService;

    private final UUID uuid1=UUID.randomUUID();


    @Test
    void postValidJob() throws IllegalAccessException {
        JobOrder jobOrder=JobOrder.builder().order_id(1).user_id(uuid1).Title("demo").address_id(1).build();
        JobResponse ans= JobResponse.builder().order_id(1)
                .title("demo").region("Yuen Long").build();
        JobService Spy=spy(jobService);

        when(jobRepository.findByTitleAnduser(any(),eq(uuid1))).thenReturn(null);
        when(userCoreService.VerifyCanOrder(uuid1)).thenReturn(true);
        when(locationService.getLocation(1)).thenReturn("Yuen Long");
        when(jobRepository.save(any())).thenReturn(jobOrder);
        doNothing().when(Spy).sendNotice("The Job with order id (1) has successfully posted !",uuid1);

        Assertions.assertEquals(ans,Spy.postJob(jobOrder).getBody());
    }
    @Test
    void postRepeatTitleJob() throws IllegalAccessException {
        JobOrder jobOrder=JobOrder.builder().order_id(1).user_id(uuid1).Title("demo").address_id(1).build();

        JobService Spy=spy(jobService);

        when(jobRepository.findByTitleAnduser(any(),eq(uuid1))).thenReturn(jobOrder);


        Assertions.assertEquals(null,Spy.postJob(jobOrder).getBody());
    }
    @Test
    void postIfUserScoreTooLow() throws IllegalAccessException {
        JobOrder jobOrder=JobOrder.builder().order_id(1).user_id(uuid1).Title("demo").address_id(1).build();

        JobService Spy=spy(jobService);

        when(userCoreService.VerifyCanOrder(uuid1)).thenReturn(false);
        doNothing().when(Spy).sendNotice("You cannot post job since your credit is too low",uuid1);


        Assertions.assertEquals(null,Spy.postJob(jobOrder).getBody());
    }

    @Test
    void GetJobsNotAccept(){
        JobOrder job1=JobOrder.builder().Title("job1").organization("google").address_id(1).isClosed(false).build();
        JobOrder job2=JobOrder.builder().Title("job2").organization("facebook").address_id(1).isClosed(false).build();

        when(jobRepository.getRegionJobs(1)).thenReturn(List.of(job1,job2));

        List<JobResponse> res=jobService.getRegionJobs(1);
        Assertions.assertEquals(jobService.GetSingleJob(job1),res.get(0));
        Assertions.assertEquals(2,res.size());

    }

    @Test
    void editJob(){
        JobOrder jobB4Edit=JobOrder.builder()
                .Title("job1")
                .date("today")
                .organization("org")
                .order_id(1).build();
        JobRequestDto requestDto=JobRequestDto.builder().order_id(1)
                .user_id(uuid1).title("updateJob").requirement("req")
                .application_number(2)
                .contact("1").salary(123).address_id(2)
                .description("des").build();
        JobResponse expectedRes=JobResponse.builder().order_id(1)
                .title("updateJob").requirement("req")
                .application_number(2)
                .organization("org")
                .date("today")
                .salary(123).region("Island")
                .description("des").build();

        JobService Spy=spy(jobService);
        when(jobRepository.findById(1)).thenReturn(Optional.ofNullable(jobB4Edit));
        when(jobRepository.save(any())).thenReturn(null);
        when(locationService.getLocation(2)).thenReturn("Island");

        JobResponse res=Spy.editJob(requestDto);
        Assertions.assertEquals(expectedRes,res);

    }

    @Test
    //test can api filter out the jobs who is accepted
    void getJobsAccepted() {

        JobOrder job1=JobOrder.builder().Title("job1").organization("google").address_id(1).isClosed(true).build();
        JobOrder job2=JobOrder.builder().Title("job2").organization("facebook").address_id(1).isClosed(false).build();

        when(jobRepository.getRegionJobs(1)).thenReturn(List.of(job1,job2));

        List<JobResponse> res=jobService.getRegionJobs(1);
        Assertions.assertEquals(jobService.GetSingleJob(job2),res.get(0));
        Assertions.assertEquals(1,res.size());
    }




}