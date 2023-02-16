package com.example.OrderService.Controller;

import com.example.OrderService.Entity.JobOrder;
import com.example.OrderService.Repository.JobRepository;
import com.example.OrderService.Service.DashbroadService;

import com.example.OrderService.dto.DashRequestDto;
import com.example.OrderService.dto.InfoResponse;
import com.example.OrderService.dto.JobRequestDto;
import com.example.OrderService.dto.Response;
import com.example.OrderService.Service.JobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;

//api for job handling
@RestController
@RequestMapping("/UserJob")
public class JobController {
    @Autowired
    private JobService jobService;

    @Autowired
    private DashbroadService dashbroadService;

    @Autowired
    private JobRepository jobRepository;


    //posting jobs
    @PostMapping("/Job/post")
    public Response postJob(@RequestBody JobOrder jobOrder) throws IllegalAccessException {
        return jobService.postJob(jobOrder);
    }

    //get an single job
    @GetMapping("/Job/get/{order_id}")
    public Response getsinglejob(@PathVariable int order_id) {
        JobOrder job=jobService.findByOrderid(order_id);
        return jobService.GetSingleJob(job);
    }


    //get the number of posted jobs of the specific user
    @GetMapping("/Jobs/User/{user_id}")
    public List<Response> getAllJobs(@PathVariable int user_id) {

        return jobService.getUserJobs(user_id);

    }

    //update job
    @PutMapping("/job/edit")
    public Response editjob(@RequestBody JobRequestDto jobRequestDto) {
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

    //get all jobs
    @GetMapping("/jobs/all")
    public List<Response> getallJobs() {

        return jobService.getAllJobs();

    }

    @GetMapping("jobs/region/{address_id}")
    public List<Response> getRegionJobs(@PathVariable int address_id){
        return jobService.getRegionJobs(address_id);
    }

    //user apply a job
    @PostMapping("/job/apply")
    //user_id apply for the job
    public String Applyjob(@RequestBody DashRequestDto applyjobdto) {
        return dashbroadService.Applyjob(applyjobdto.getOrder_id(),applyjobdto.getApply_id());
    }


    //access the user profile of the applications
    //only the poster are permitted use this api
    @PostMapping("/Applications/show")
    public List<InfoResponse> showApplications(@RequestBody DashRequestDto dto) {
        return dashbroadService.showApplications(dto.getOrder_id(), dto.getPoster_id());
    }
    //the poster accept application
    //only the poster are permitted use this api
    @PutMapping("/Applications/accept")
    public InfoResponse acceptApplication (@RequestBody DashRequestDto dto){
        return dashbroadService.acceptApplication(dto.getOrder_id(), dto.getPoster_id(), dto.getApply_id());
    }

    //delete the application
    @DeleteMapping("/Applications/delete")
    public String RemoveApplication(@RequestBody DashRequestDto dto){
        return dashbroadService.deleteApplication(dto.getOrder_id(),dto.getApply_id());
    }

    //the poster accept the application
    @PostMapping("/Accept/show")        //need check
    public List<InfoResponse> showAccept(@RequestBody DashRequestDto dto) {
        return dashbroadService.showAccepted(dto.getOrder_id(), dto.getPoster_id());
    }
}
