package com.example.OrderService.Controller;

import com.example.OrderService.Entity.JobOrder;
import com.example.OrderService.Repository.JobRepository;
import com.example.OrderService.dto.JobRequestDto;
import com.example.OrderService.dto.Response;
import com.example.OrderService.Service.JobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/Job")
public class JobController {
    @Autowired
    private JobService jobService;

    @Autowired
    private JobRepository jobRepository;


    @PostMapping("/Joborder")
        public Response postJob(@RequestBody JobOrder jobOrder) throws IllegalAccessException {
        return jobService.postJob(jobOrder);
    }
    
    @GetMapping("/getJob/{order_id}")
        public Response getsinglejob(@PathVariable int order_id){
        return jobService.GetSingleJob(order_id);
    }

    /*
    can use traditional dto:
    build a converter method,
    map using findAll().stream().map(this::${converter_method}).collect(Collectors.toList())
     */
    @GetMapping("/getJobsUser/{user_id}")
        public Collection<Response> getAllJobs(@PathVariable int user_id){

        return jobRepository.getUserJobs(user_id);

        }

    @PutMapping("/editJob")
        public Response editjob(@RequestBody JobRequestDto jobRequestDto){
        return jobService.editJob(jobRequestDto);
    }
    @DeleteMapping("/deletejob/{order_id}")
    public String deleteJob(@PathVariable int order_id){
        if(jobRepository.findById(order_id).orElse(null)==null){
            return "no order id: "+order_id+" exist";
        }
         jobRepository.deleteById(order_id);

         return "successful delete with id: "+order_id;

    }

    @GetMapping("/getalljobs")
    public Collection<Response> getallJobs(){

        return jobRepository.getAllJobs();

    }
    @GetMapping("/getRegionjobs/{address_id}")
    public Collection<Response> getRegionJobs(@PathVariable int address_id){
        return  jobRepository.getRegionJobs(address_id);
    }
    }


