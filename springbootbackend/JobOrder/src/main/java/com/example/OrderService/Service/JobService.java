package com.example.OrderService.Service;


import com.example.OrderService.Entity.JobOrder;
import com.example.OrderService.Repository.JobRepository;
import com.example.OrderService.Repository.LocationRepository;
import com.example.OrderService.dto.JobRequestDto;
import com.example.OrderService.dto.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Collection;

@Service
public class JobService {
    private static final Logger LOG = LoggerFactory.getLogger(JobService.class);
    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private  WebClient webClient;
    @Autowired
    private LocationRepository locationRepository;

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
                        .path("/Checkuser/{id}")//"http://localhost:8082/Checkuser/{id}")
                        .build(252))
                        .retrieve()
                                .bodyToMono(Boolean.class)
                                        .block(); //make syn request

        if(Boolean.FALSE.equals(result)){
           throw new IllegalAccessException("User score to low");
        }
        LOG.info("User can upload ");
        System.out.println("user passed");
        jobOrder.setRegion(getLocation(jobOrder.getAddress_id()));
        JobOrder job=jobRepository.save(jobOrder);
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
