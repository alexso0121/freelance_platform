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

@RestController
@RequestMapping("/UserJob")
public class JobController {
    @Autowired
    private JobService jobService;

    @Autowired
    private DashbroadService dashbroadService;

    @Autowired
    private JobRepository jobRepository;


    @PostMapping("/Job/post")
    public Response postJob(@RequestBody JobOrder jobOrder) throws IllegalAccessException {
        return jobService.postJob(jobOrder);
    }

    @GetMapping("/Job/get/{order_id}")
    public Response getsinglejob(@PathVariable int order_id) {
        return jobService.GetSingleJob(order_id);
    }

    /*
    can use traditional dto:
    build a converter method,
    map using findAll().stream().map(this::${converter_method}).collect(Collectors.toList())
     */
    @GetMapping("/Jobs/User/{user_id}")
    public Collection<Response> getAllJobs(@PathVariable int user_id) {

        return jobRepository.getUserJobs(user_id);

    }

    @PutMapping("/job/edit")
    public Response editjob(@RequestBody JobRequestDto jobRequestDto) {
        return jobService.editJob(jobRequestDto);
    }

    @DeleteMapping("/jobs/delete/{order_id}")
    public String deleteJob(@PathVariable int order_id) {
        if (jobRepository.findById(order_id).orElse(null) == null) {
            return "no order id: " + order_id + " exist";
        }
        jobRepository.deleteById(order_id);

        return "successful delete with id: " + order_id;

    }

    @GetMapping("/jobs/all")
    public Collection<Response> getallJobs() {

        return jobRepository.getAllJobs();

    }

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

    @DeleteMapping("/Applications/delete")
    public String RemoveApplication(@RequestBody DashRequestDto dto){
        return dashbroadService.deleteApplication(dto.getOrder_id(),dto.getApply_id());
    }
    @PostMapping("/Accept/show")        //need check
    public List<InfoResponse> showAccept(@RequestBody DashRequestDto dto) {
        return dashbroadService.showAccepted(dto.getOrder_id(), dto.getPoster_id());
    }
}
