package com.example.OrderService.Service;


import com.example.OrderService.Entity.JobOrder;
import com.example.OrderService.Repository.ApplicationRepository;
import com.example.OrderService.Repository.JobRepository;
import com.example.OrderService.Repository.LocationRepository;
import com.example.OrderService.dto.JobRequestDto;
import com.example.OrderService.dto.NoticeRespond;
import com.example.OrderService.dto.JobResponse;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

//business logic for handling Jobs
@Slf4j
@Service
public class JobService {
    private static final Logger LOG = LoggerFactory.getLogger(JobService.class);

    private final JobRepository jobRepository;



    private final ApplicationRepository applicationRepository;


    private final LocationRepository locationRepository;
    private final UserCoreService userCoreService;


    private final KafkaTemplate<String, NoticeRespond> kafkaTemplate;

    private final LocationService locationService;



    public JobService(JobRepository jobRepository, ApplicationRepository applicationRepository, LocationRepository locationRepository, @Lazy UserCoreService userCoreService, KafkaTemplate<String, NoticeRespond> kafkaTemplate, LocationService locationService) {
        this.jobRepository = jobRepository;
        this.applicationRepository = applicationRepository;
        this.locationRepository = locationRepository;
        this.userCoreService = userCoreService;
        this.kafkaTemplate = kafkaTemplate;
        this.locationService = locationService;
    }


    //post a job
    public ResponseEntity<JobResponse> postJob(JobOrder jobOrder) throws IllegalAccessException {
        //verify can post
        if(!VerifyCanPost(jobOrder)){
            return ResponseEntity.status(
            HttpStatus.FORBIDDEN)
                    .body(null);
        }
        LOG.info("User can upload ");
        UUID user_id=jobOrder.getUser_id();

        //convert the address_id to region base on the 18 district in hk
        jobOrder.setRegion(locationService.getLocation(jobOrder.getAddress_id()));
        JobOrder job=jobRepository.save(jobOrder);

        //send notification to the poster
        String notification="The Job with order id (" +job.getOrder_id()+") has successfully posted !";
        sendNotice(notification,user_id);

        return ResponseEntity.status(HttpStatus.CREATED)
                        .body(
                GetSingleJob(job));
    }

    //check whether the user can post a job
    public Boolean VerifyCanPost(JobOrder jobOrder){
        //prevent repeat of job
        if(checkif_JobAlreadyExist(jobOrder.getTitle(),jobOrder.getUser_id())){
            log.info("job already exist");
            return false;
        }
        UUID user_id=jobOrder.getUser_id();
        //call user service check the score of user

        LOG.info("check the user can upload job post");
        //check user credit
        Boolean result=userCoreService.VerifyCanOrder(user_id);
        if(!result){
            log.info("user score too low");
            sendNotice("You cannot post job since your credit is too low",user_id);
            return false;
        }
        return true;
    }

    //send notification to the notification microservice
    public void sendNotice(String notification,UUID userid){
        LocalDateTime myDateObj = LocalDateTime.now();
        DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        String formattedDate = myDateObj.format(myFormatObj);
        kafkaTemplate.send("notificationTopic",new NoticeRespond(
                userid,formattedDate,notification
        ));
    }

    //main method for converting Joborder to user_id
    public JobResponse GetSingleJob(JobOrder job){

        return JobResponse.builder()
                .order_id(job.getOrder_id())
                .title(job.getTitle())
                .description(job.getDescription())
                .requirement(job.getRequirement())
                .salary(job.getSalary())
                .date(job.getDate())
                .organization(job.getOrganization())
                .application_number(job.getApplication_number())
                .region(job.getRegion()).build();

    }

    //check if the job's with same title and organisation already exist
    public Boolean checkif_JobAlreadyExist(String title,UUID user_id){
        JobOrder jobOrder= jobRepository.findByTitleAnduser(title,user_id);
        return jobOrder != null;
    }

    public JobOrder findByOrderid(int id){
        return jobRepository.findById(id).orElse(null);
    }



    //edit the job
    public JobResponse editJob(JobRequestDto jobRequestDto) {
        JobOrder savejob=jobRepository.findById(jobRequestDto.getOrder_id()).orElse(null);

        assert savejob != null;
        savejob.setTitle(jobRequestDto.getTitle());
        savejob.setDescription(jobRequestDto.getDescription());
        savejob.setRequirement(jobRequestDto.getRequirement());
        savejob.setSalary(jobRequestDto.getSalary());
        savejob.setAddress_id(jobRequestDto.getAddress_id());
        savejob.setRegion(locationService.getLocation(jobRequestDto.getAddress_id()));
        savejob.setApplication_number(jobRequestDto.getApplication_number());
        jobRepository.save(savejob);

        return GetSingleJob(savejob);
    }

    //get the location base on the address id




    //get the jobs which is posted by the user
    public List<JobResponse> getUserJobs(UUID userId) {
       return jobRepository.getUserJobs(userId).stream()
                .map(this::GetSingleJob)    //map the joborder class to response
                .collect(Collectors.toList());
    }

    //get all the jobs which are not being accepted
    public List<JobResponse> getAllJobs() {
        return jobRepository.getAllJobs().stream()
                .filter(res->!res.isClosed())
                .map(this::GetSingleJob)
                .collect(Collectors.toList());
    }

    //get jobs(not being accepted) base on the regions (address_id)
    public List<JobResponse> getRegionJobs(int addressId) {
        return jobRepository.getRegionJobs(addressId).stream()
                .filter(res->!res.isClosed())
                .map(this::GetSingleJob)
                .collect(Collectors.toList());
    }





}
