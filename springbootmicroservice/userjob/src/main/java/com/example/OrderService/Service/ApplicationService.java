package com.example.OrderService.Service;

import com.example.OrderService.Entity.Application;
import com.example.OrderService.Entity.JobOrder;
import com.example.OrderService.Entity.User;
import com.example.OrderService.Repository.ApplicationRepository;
import com.example.OrderService.Repository.JobRepository;
import com.example.OrderService.dto.ChatMessage;
import com.example.OrderService.dto.InfoResponse;
import com.example.OrderService.dto.JobResponse;
import com.example.OrderService.dto.NoticeRespond;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;


import javax.swing.*;
import javax.swing.border.Border;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

//business logic involving application
@Service
@Slf4j
public class ApplicationService {
    private final ApplicationRepository applicationRepository;

    private final JobRepository jobRepository;

    private final UserCoreService userCoreService;

    private final KafkaTemplate<String, NoticeRespond> kafkaTemplate;

    private final JobService jobService;
    private final WebClient.Builder webclient;




    public ApplicationService(ApplicationRepository applicationRepository, JobRepository jobRepository, UserCoreService userCoreService, KafkaTemplate<String, NoticeRespond> kafkaTemplate, JobService jobService, WebClient.Builder webclient) {
        this.applicationRepository = applicationRepository;

        this.jobRepository = jobRepository;
        this.userCoreService = userCoreService;
        this.kafkaTemplate = kafkaTemplate;
        this.jobService = jobService;
        this.webclient = webclient;

    }


    //applying job
    /*
    1.verify can order
    2.save the order
    3.send notice
    */
    public String Applyjob(int order_id,int apply_id,int poster_id){

        //check if the user sending too much application already
        if(TooMuchApplication(order_id)){
            return "you have too much application, please delete some";
        };

        //check if the user have already applied the job
        Application saved=applicationRepository.findByApply_idAndOrder_id(apply_id,order_id);
        assert saved==null;

        log.info("verify success");

        //save the application
        applicationRepository.save(Application.builder()
                .apply_id(apply_id)
                .order_id(order_id)
                .build());

        //sending notification to both applier and the poster
        String applier_notification="You have successfully applied for job id:"+order_id;
        System.out.println(apply_id+order_id);
        sendNotice(applier_notification,apply_id);

        String poster_notification="You have received an application for job id:"+order_id;
        sendNotice(poster_notification,poster_id);
        return "successfully added";

    }

    //method for sending notice to the notification microservice
    public void sendNotice(String notification,int userid){
        LocalDateTime myDateObj = LocalDateTime.now();
        DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        String formattedDate = myDateObj.format(myFormatObj);
        kafkaTemplate.send("notificationTopic",new NoticeRespond(
                userid,formattedDate,notification
        ));
    }

    //check if there are too application for the user
    //if yes send notice to the client
    public Boolean TooMuchApplication(int apply_id){
        List<Application> ApplicationList = applicationRepository.findByApply_id(apply_id);
        if(ApplicationList.size()<=20){
            return false;
        }
        String notification="You have already send to much of applications.Please delete some and try again";
        sendNotice(notification, apply_id);
        return true;

        }


    //display the list of user who have applied for the job
    public List<InfoResponse> showApplications(int order_id,int user_id){
        List<Application> application = applicationRepository.findByOrder_id(order_id);

        log.info("user id "+user_id+" request for accessing applications in job id: "+order_id);
        //find the list of application
        //map user_id to user profile=>collect a list of profile
        return application.stream()
                        .map(res->userCoreService.getProfile(res.getApply_id()))
                .collect(Collectors.toList());
    };

    //check the number of quota for job to be close
    public JobOrder HandleRemain_number(JobOrder jobOrder){

        //update the number  remain
        int applicationNumber = jobOrder.getApplication_number();
        jobOrder.setApplication_number(applicationNumber-1);


       if(applicationNumber-1==0){
           jobOrder.setClosed(true);
           sendNotice("You have recruited enough amount of employee!The job will be closed",jobOrder.getUser_id());
           String username=userCoreService.findById(jobOrder.getUser_id()).getUsername();
           ChatMessage Joinmessage=new ChatMessage(null,jobOrder.getOrder_id(), ChatMessage.MessageType.BUILD,
                   username+" has built the room",
                   username,null);
           generateChat(Joinmessage);
       }
         jobRepository.save(jobOrder);
       return jobOrder;

    }

    //poster accept one of the application
    /*
    1.verify
    2.set the application to isaccepted
    3.send notice
    4.add the user to the chatroom (build base on the order_ud)
    * */
    public InfoResponse acceptApplication(int orderId, int posterId,int applyId) {
        //check userId ,
        Application application = applicationRepository.findByApply_idAndOrder_id(applyId, orderId);
        assert application != null;

        //update the application
        log.info("valid request");
        application.setIsaccepted(true);
        applicationRepository.save(application);

        JobOrder jobOrder=jobRepository.findById(orderId).orElse(null);
        assert jobOrder != null;
        //send notice
        String posterNotice="You have successfully accepted the application for job id:"+orderId;
        sendNotice(posterNotice,posterId);


        String ApplyNotice="Your application for Job with title '"+jobOrder.getTitle()+"' has been accepted" ;

        sendNotice(ApplyNotice,applyId);

        //handle the quota of applications or set job is closed
        HandleRemain_number(jobOrder);

        //handle chat message
        log.info("Start sending chat");
        String username=userCoreService.findById(posterId).getUsername();
        ChatMessage Joinmessage=new ChatMessage(null,orderId, ChatMessage.MessageType.JOIN,
                username+" has join the room",
                username,null);
        generateChat(Joinmessage);

        return userCoreService.getProfile(applyId);

    }


    //sending post chat request to the chatroom microservice
    public void generateChat(ChatMessage buildmessage) {
        webclient.baseUrl("http://CHATROOM").build()
                .post().uri("/Chat/chat/post")
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .body(Mono.just(buildmessage), ChatMessage.class)
                .retrieve()
                .bodyToMono(ChatMessage.class)
                .doOnNext(System.out::println)
                .block();


    }
    //show the Job profile which the user has applied
    public List<JobResponse> showApplicationsToUser(int id) {
        List<Application> byApplyId = applicationRepository.findByApply_id(id);

        //map the Joborder to response
        return byApplyId.stream()
                .map(res->jobService.GetSingleJob(jobService.findByOrderid(res.getOrder_id())))
                .collect(Collectors.toList());

    }

    // user delete the application
    public String deleteApplication(int orderId, int applyId) {
        //verify
        Application application= applicationRepository.findByApply_idAndOrder_id(applyId,orderId);
        if(application==null){
            log.error("no joborder found");
            return "no joborder found";
        }
        applicationRepository.deleteById(application.getApplication_id());

        return "you have successfully removed";


}
    //show the application that are being accepted base on the order_id;
    public List<InfoResponse> showAccepted(int order_id,int user_id){
        //get all the application
        List<Application> applications = applicationRepository.findByOrder_id(order_id);
        if(applications ==null){
            log.error("no requested id found");
            return null;
        }
        log.info("user id "+user_id+" request for accessing applications in job id: "+order_id);

        //filter only return the application who is accepted
        //map them to the profile
        return applications.stream()
                .filter(Application::isIsaccepted)
                .map(res->userCoreService.getProfile(res.getApply_id()))
                .collect(Collectors.toList());
    };


}
