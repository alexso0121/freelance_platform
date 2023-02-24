package com.example.OrderService.Controller;

import com.example.OrderService.Entity.Application;
import com.example.OrderService.Repository.ApplicationRepository;
import com.example.OrderService.Service.ApplicationService;
import com.example.OrderService.dto.ApplicationRequest;
import com.example.OrderService.dto.InfoResponse;
import com.example.OrderService.dto.JobResponse;
import org.springframework.web.bind.annotation.*;

import java.util.List;

//the Integration test of apis in applicationController are also in JobControllerTest file
@RestController
@RequestMapping("/UserJob")
public class ApplicationController {
    private final ApplicationService applicationService;
    private final ApplicationRepository applicationRepository;

    public ApplicationController(ApplicationService applicationService, ApplicationRepository applicationRepository) {
        this.applicationService = applicationService;
        this.applicationRepository = applicationRepository;
    }

    @PostMapping("/job/apply")
    //user_id apply for the job
    public String Applyjob(@RequestBody ApplicationRequest applyjobdto) {
        return applicationService.Applyjob(applyjobdto.getOrder_id()
                ,applyjobdto.getApply_id(),applyjobdto.getPoster_id());
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

    //show all data in application table
    @GetMapping("/admin/application/all")
    public List<Application> DisplayallApplication(){
        return applicationRepository.findAll();
    }
}
