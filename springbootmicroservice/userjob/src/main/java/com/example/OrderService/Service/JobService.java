package com.example.OrderService.Service;


import com.example.OrderService.Entity.DashBroad;
import com.example.OrderService.Entity.JobOrder;
import com.example.OrderService.Entity.User;
import com.example.OrderService.Repository.DashBroadRepository;
import com.example.OrderService.Repository.JobRepository;
import com.example.OrderService.Repository.LocationRepository;
import com.example.OrderService.dto.JobRequestDto;
import com.example.OrderService.dto.NoticeRespond;
import com.example.OrderService.dto.Response;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.security.saml2.Saml2RelyingPartyProperties;
import org.springframework.context.annotation.Lazy;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class JobService {
    private static final Logger LOG = LoggerFactory.getLogger(JobService.class);

    private final JobRepository jobRepository;



    private final DashBroadRepository dashBroadRepository;


    private final LocationRepository locationRepository;
    private final UserCoreService userCoreService;


    private final KafkaTemplate<String, NoticeRespond> kafkaTemplate;



    public JobService(JobRepository jobRepository, DashBroadRepository dashBroadRepository, LocationRepository locationRepository, @Lazy UserCoreService userCoreService, KafkaTemplate<String, NoticeRespond> kafkaTemplate) {
        this.jobRepository = jobRepository;
        this.dashBroadRepository = dashBroadRepository;
        this.locationRepository = locationRepository;
        this.userCoreService = userCoreService;
        this.kafkaTemplate = kafkaTemplate;

    }


    //post a job
    public Response postJob(JobOrder jobOrder) throws IllegalAccessException {
        //verify can post
        if(!VerifyCanPost(jobOrder)){
            return null;
        }
        LOG.info("User can upload ");
        int user_id=jobOrder.getUser_id();


        jobOrder.setRegion(getLocation(jobOrder.getAddress_id()));
        JobOrder job=jobRepository.save(jobOrder);

        //build a row of relationship in dashBroad
        dashBroadRepository.save(new DashBroad(
                0,job.getOrder_id(),user_id, job.getApplication_number(), null,null));
        log.info("new dashboad is built");

        String notification="The Job with order id (" +job.getOrder_id()+") has successfully posted !";
        sendNotice(notification,user_id);

        return GetSingleJob(job);
    }

    //check whether the user can post a job
    public Boolean VerifyCanPost(JobOrder jobOrder){
        //prevent repeat of job
        if(checkif_JobAlreadyExist(jobOrder.getTitle(),jobOrder.getOrganization())){
            log.info("job already exist");
            return false;
        }
        int user_id=jobOrder.getUser_id();
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
    public void sendNotice(String notification,int userid){
        LocalDateTime myDateObj = LocalDateTime.now();
        DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        String formattedDate = myDateObj.format(myFormatObj);
        kafkaTemplate.send("notificationTopic",new NoticeRespond(
                userid,formattedDate,notification
        ));
    }

    //main method for converting Joborder to user_id
    public Response GetSingleJob(JobOrder job){

        return Response.builder()
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
    public Boolean checkif_JobAlreadyExist(String title,String organization){
        JobOrder jobOrder= jobRepository.findByTitleAndOrganization(title,organization);
        return jobOrder != null;
    }

    public JobOrder findByOrderid(int id){
        return jobRepository.findById(id).orElse(null);
    }



    public Response editJob(JobRequestDto jobRequestDto) {
        JobOrder savejob=jobRepository.findById(jobRequestDto.getOrder_id()).orElse(null);

        assert savejob != null;
        savejob.setTitle(jobRequestDto.getTitle());
        savejob.setDescription(jobRequestDto.getDescription());
        savejob.setRequirement(jobRequestDto.getRequirement());
        savejob.setSalary(jobRequestDto.getSalary());
        savejob.setAddress_id(jobRequestDto.getAddress_id());
        savejob.setRegion(getLocation(savejob.getAddress_id()));
        JobOrder savedjob=jobRepository.save(savejob);

        return GetSingleJob(savedjob);
    }

    //get the location base on the address id
    private String getLocation(int address_id){
        return locationRepository.getLocation(address_id);
    }


    //show the user profile who have applied for the job
    public List<Response> showApplications(int id) {
        User user=userCoreService.findById(id);
        if(user==null){
            log.error("user not found");
            return null;
        }
        //map the Joborder to response
        return user.getApplications().stream()
                .map(this::GetSingleJob)
                .collect(Collectors.toList());

    }

    //get the jobs which is posted by the user
    public List<Response> getUserJobs(int userId) {
       return jobRepository.getUserJobs(userId).stream()
                .map(this::GetSingleJob)    //map the joborder class to response
                .collect(Collectors.toList());
    }

    //get all the jobs which are not being accepted
    public List<Response> getAllJobs() {
        return jobRepository.getAllJobs().stream()
                .filter(res->!res.isIsaccepted())
                .map(this::GetSingleJob)
                .collect(Collectors.toList());
    }

    //get jobs(not being accepted) base on the regions (address_id)
    public List<Response> getRegionJobs(int addressId) {
        return jobRepository.getRegionJobs(addressId).stream()
                .filter(res->!res.isIsaccepted())
                .map(this::GetSingleJob)
                .collect(Collectors.toList());
    }
    //add application






}
