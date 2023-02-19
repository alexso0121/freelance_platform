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
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public JobResponse postJob(@RequestBody JobOrder jobOrder) throws IllegalAccessException {
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
    public List<JobResponse> getAllJobs(@PathVariable int user_id) {

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

    //sending application to a specific job
    @PostMapping("/job/apply")
    //user_id apply for the job
    public String Applyjob(@RequestBody ApplicationRequest applyjobdto) {
        return applicationService.Applyjob(applyjobdto.getOrder_id()
                ,applyjobdto.getPoster_id(),applyjobdto.getApply_id());
    }


    //-access the user profile of the applications
    //-only the poster are permitted use this api
    @PostMapping("/Applications/show")
    public List<InfoResponse> showApplications(@RequestBody ApplicationRequest dto) {
        return applicationService.showApplications(dto.getOrder_id(), dto.getPoster_id());
    }

    //the poster accept application
    //only the poster are permitted use this api
    @PutMapping("/Applications/accept")
    public InfoResponse acceptApplication (@RequestBody ApplicationRequest dto){
        return applicationService.acceptApplication(dto.getOrder_id(), dto.getPoster_id(), dto.getApply_id());
    }

    //delete the application
    @DeleteMapping("/Applications/delete")
    public String RemoveApplication(@RequestBody ApplicationRequest dto){
        return applicationService.deleteApplication(dto.getOrder_id(),dto.getApply_id());
    }

    //the job poster accept the application
    @PostMapping("/Accept/show")        //need check
    public List<InfoResponse> showAccept(@RequestBody ApplicationRequest dto) {
        return applicationService.showAccepted(dto.getOrder_id(), dto.getPoster_id());
    }
    //show the number of jobs the user has applied
    @GetMapping("/application/history/{id}")
    public List<JobResponse> showApplicationshistory(@PathVariable int id){
        return applicationService.showApplicationsToUser(id);
    }
}
