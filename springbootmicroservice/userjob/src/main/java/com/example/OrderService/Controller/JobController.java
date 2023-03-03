package com.example.OrderService.Controller;

import com.example.OrderService.Entity.JobOrder;
import com.example.OrderService.Repository.JobRepository;
import com.example.OrderService.Service.ApplicationService;

import com.example.OrderService.dto.ApplicationRequest;
import com.example.OrderService.dto.InfoResponse;
import com.example.OrderService.dto.JobRequestDto;
import com.example.OrderService.dto.JobResponse;
import com.example.OrderService.Service.JobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

//api for job handling
@RestController
@RequestMapping("/UserJob")
public class JobController {
    @Autowired
    private JobService jobService;

    @Autowired
    private ApplicationService applicationService;

    @Autowired
    private JobRepository jobRepository;


    //posting jobs
    @PostMapping("/Job/post")
    public ResponseEntity<JobResponse> postJob(@RequestBody JobOrder jobOrder) throws IllegalAccessException {
        return jobService.postJob(jobOrder);
    }

    //show details of a single job
    @GetMapping("/Job/get/{order_id}")
    public JobResponse getsinglejob(@PathVariable int order_id) {
        JobOrder job=jobService.findByOrderid(order_id);
        return jobService.GetSingleJob(job);
    }


    //get the number of posted jobs of the specific user
    @GetMapping("/Jobs/User/{user_id}")
    public List<JobResponse> getAllJobs(@PathVariable UUID user_id) {

        return jobService.getUserJobs(user_id);

    }

    //editing job
    @PutMapping("/job/edit")
    public JobResponse editjob(@RequestBody JobRequestDto jobRequestDto) {
        return jobService.editJob(jobRequestDto);
    }

    //delete job
    @DeleteMapping("/jobs/delete/{order_id}")
    public String deleteJob(@PathVariable int order_id) {
        if (jobRepository.findById(order_id).orElse(null) == null) {
            return "no order id: " + order_id + " exist";
        }
        jobRepository.deleteById(order_id);

        return "successful delete with id: " + order_id;

    }

    //show all available jobs
    @GetMapping("/jobs/all")
    public List<JobResponse> getallJobs() {

        return jobService.getAllJobs();

    }

   /*
    -showing available jobs to the clients
    -filter base on region
    */
    @GetMapping("jobs/region/{address_id}")
    public List<JobResponse> getRegionJobs(@PathVariable int address_id){
        return jobService.getRegionJobs(address_id);
    }



    //get all data in the job table
    @GetMapping("/admin/jobs/all")
    public List<JobOrder> DisplayallJob(){
        return jobRepository.findAll();
    }


}
