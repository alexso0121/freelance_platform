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
import org.springframework.context.annotation.Lazy;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

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


    public Response postJob(JobOrder jobOrder) throws IllegalAccessException {
        JobOrder oldJob=jobRepository.findByTitle(jobOrder.getTitle());
        int user_id=jobOrder.getUser_id();
        if(oldJob!=null){
            return null;
        }
        //call user service check the score of user
        //change the localhost:8082 to name in eureke
        LOG.info("check the user can upload job post");
        Boolean result=userCoreService.VerifyCanOrder(user_id);
        if(!result){
          log.info("user score too low");
            kafkaTemplate.send("notificationTopic",new NoticeRespond(
                    user_id,"Job","You cannot post job since your credit is too low"
            ));
           return null;
        }

        LOG.info("User can upload ");
        System.out.println("user passed");
        jobOrder.setRegion(getLocation(jobOrder.getAddress_id()));
        JobOrder job=jobRepository.save(jobOrder);

        //build a row of relationship in dashBroad
        dashBroadRepository.save(new DashBroad(
                0,job.getOrder_id(),user_id, job.getApplication_number(), null,null));
        log.info("new dashboad is built");

        String notification="The Job with order id (" +job.getOrder_id()+") has successfully posted !";
        kafkaTemplate.send("notificationTopic",new NoticeRespond(
                user_id,"Job",notification
        ));
        return GetSingleJob(job.getOrder_id());
    }
    //similar using dto
    public Response GetSingleJob(int order_id){

        return jobRepository.singlejob(order_id);


    }
    public JobOrder findByOrderid(int id){
        return jobRepository.findById(id).orElse(null);
    }


    public Response editJob(JobRequestDto jobRequestDto) {
        JobOrder savejob=jobRepository.findById(jobRequestDto.getOrder_id()).orElse(null);

        savejob.setTitle(jobRequestDto.getTitle());
        savejob.setDescription(jobRequestDto.getDescription());
        savejob.setRequirement(jobRequestDto.getRequirement());
        savejob.setSalary(jobRequestDto.getSalary());
        savejob.setAddress_id(jobRequestDto.getAddress_id());
        savejob.setRegion(getLocation(savejob.getAddress_id()));
        jobRepository.save(savejob);

        return GetSingleJob(jobRequestDto.getOrder_id());
    }
    private String getLocation(int address_id){
        return locationRepository.getLocation(address_id);
    }

    public List<Response> showApplications(int id) {
        User user=userCoreService.findById(id);
        if(user==null){
            log.error("user not found");
            return null;
        }
        return user.getApplications().stream()
                .map(res->GetSingleJob(res.getOrder_id()))
                .collect(Collectors.toList());

    }
    //add application






}
