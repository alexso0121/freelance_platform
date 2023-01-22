package com.example.OrderService.Service;


import com.example.OrderService.Entity.JobOrder;
import com.example.OrderService.Repository.JobRepository;
import com.example.OrderService.Repository.LocationRepository;
import com.example.OrderService.dto.JobRequestDto;
import com.example.OrderService.dto.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Collection;

@Service
public class JobService {
    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private LocationRepository locationRepository;

    public Response postJob(JobOrder jobOrder) {
        JobOrder oldJob=jobRepository.findByTitle(jobOrder.getTitle());
        if(oldJob!=null){
            return null;
        }
        jobOrder.setRegion(getLocation(jobOrder.getAddress_id()));
        JobOrder job=jobRepository.save(jobOrder);
        return GetSingleJob(job.getOrder_id());
    }
    //similar using dto
    public Response GetSingleJob(int order_id){

        return jobRepository.singlejob(order_id);


    }
    public Collection<Response> convertLocation(Collection<Response> jobs){
        for (Response job: jobs){
            GetSingleJob(job.getOrder_id());
        }
        return jobs;
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
