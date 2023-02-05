package com.example.OrderService.Service;


import com.example.OrderService.Entity.JobOrder;
import com.example.OrderService.Repository.JobRepository;
import com.example.OrderService.Repository.LocationRepository;
import com.example.OrderService.dto.JobRequestDto;
import com.example.OrderService.dto.NoticeRespond;
import com.example.OrderService.dto.Response;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Slf4j
@Service
public class JobService {
    private static final Logger LOG = LoggerFactory.getLogger(JobService.class);
    @Autowired
    private JobRepository jobRepository;


    @Autowired
    private LocationRepository locationRepository;

    @Autowired
    private  KafkaTemplate<String, NoticeRespond> kafkaTemplate;

    @Autowired
    private WebClient.Builder webClientBuilder;



    public Response postJob(JobOrder jobOrder) throws IllegalAccessException {
        JobOrder oldJob=jobRepository.findByTitle(jobOrder.getTitle());
        int user_id=jobOrder.getSender_id();
        if(oldJob!=null){
            return null;
        }
        //call user service check the score of user
        //change the localhost:8082 to name in eureke
        LOG.info("check the user can upload job post");
        Boolean result=webClientBuilder.baseUrl("http://USER").build().get()
                .uri(uriBuilder -> uriBuilder
                        .path("/User/Checkuser/{id}")//"http://localhost:8082/Checkuser/{id}")
                        .build(user_id))
                        .retrieve()
                                .bodyToMono(Boolean.class)
                                        .block(); //make syn request

        if(result!=true){
          log.info("user score too low");
            kafkaTemplate.send("notificationTopic",new NoticeRespond(
                    user_id,"You cannot post job since your credit is too low"
            ));
           return null;
        }
        LOG.info("User can upload ");
        System.out.println("user passed");
        jobOrder.setRegion(getLocation(jobOrder.getAddress_id()));
        JobOrder job=jobRepository.save(jobOrder);

        String notification="The Job with order id (" +job.getOrder_id()+") has successfully posted !";
        kafkaTemplate.send("notificationTopic",new NoticeRespond(
                user_id,notification
        ));
        return GetSingleJob(job.getOrder_id());
    }
    //similar using dto
    public Response GetSingleJob(int order_id){

        return jobRepository.singlejob(order_id);


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



}
