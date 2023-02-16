package com.example.OrderService.Service;

import com.example.OrderService.Entity.DashBroad;
import com.example.OrderService.Entity.JobOrder;
import com.example.OrderService.Entity.User;
import com.example.OrderService.Repository.DashBroadRepository;
import com.example.OrderService.Repository.JobRepository;
import com.example.OrderService.dto.ChatMessage;
import com.example.OrderService.dto.InfoResponse;
import com.example.OrderService.dto.NoticeRespond;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;


import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class DashbroadService {
    private final DashBroadRepository dashBroadRepository;

    private final JobRepository jobRepository;

    private final UserCoreService userCoreService;

    private final KafkaTemplate<String, NoticeRespond> kafkaTemplate;

    private final JobService jobService;
    private final WebClient.Builder webclient;




    public DashbroadService(DashBroadRepository dashBroadRepository, JobRepository jobRepository, UserCoreService userCoreService, KafkaTemplate<String, NoticeRespond> kafkaTemplate, JobService jobService, WebClient.Builder webclient) {
        this.dashBroadRepository = dashBroadRepository;

        this.jobRepository = jobRepository;
        this.userCoreService = userCoreService;
        this.kafkaTemplate = kafkaTemplate;
        this.jobService = jobService;
        this.webclient = webclient;

    }


    //user_id apply for the job
    public String Applyjob(int order_id,int apply_id){
        //check order_id

        DashBroad dashBroad=dashBroadRepository.findByOrder_id(order_id);

        User appliedUser=userCoreService.findById(apply_id);

            if(dashBroad.getApplier_id().contains(appliedUser)){
                return "you have already applied the job";
            }
        dashBroad.getApplier_id().add(userCoreService.getUser(apply_id));    //update the dashbroad
        dashBroad.setApplier_id(dashBroad.getApplier_id());
        dashBroadRepository.save(dashBroad);

        log.info("verify success");
            //add in applications records in user entity
            postApplication(appliedUser,order_id);

            //send notification
            String notification="You have successfully applied for job id:"+order_id;
        System.out.println(apply_id+order_id);
            sendNotice(notification,apply_id);

        return "successfully added";

    }

    public void sendNotice(String notification,int userid){
        LocalDateTime myDateObj = LocalDateTime.now();
        DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        String formattedDate = myDateObj.format(myFormatObj);
        kafkaTemplate.send("notificationTopic",new NoticeRespond(
                userid,formattedDate,notification
        ));
    }

    //add the application to the user entity
    public Boolean postApplication(User user,int order_id){

            if(user.getApplications().size()==20){
                String notification="You have already send to much of applications.Please delete some and try again";
            sendNotice(notification, user.getId());
            return false;
            }
            JobOrder job=jobService.findByOrderid(order_id);
            user.getApplications().add(job);
            System.out.println(job);
            userCoreService.saveAndReturn(user);
            log.info("add application");
            return true;


        }


  //display a list of application profile to poster
    public List<InfoResponse> showApplications(int order_id,int user_id){
        DashBroad dashBroad= dashBroadRepository.findByOrder_id(order_id);
        if(dashBroad==null){
            log.error("no requested id found");
            return null;
        }
       /* if(dashBroad.getPoster_id()!=user_id){
            log.error("only poster can read the application detail");
            return null;
        }*/
        log.info("user id "+user_id+" request for accessing applications in job id: "+order_id);
        //find the list of application
        //map user_id to user profile=>collect a list of profile
        return dashBroad.getApplier_id().stream()
                        .map(res->userCoreService.getProfile(res.getId()))
                .collect(Collectors.toList());
    };

    public void VerifyCanAcceptApplication(@NonNull DashBroad dashBroad,@NonNull User user,NonNull acceptedProfile){
         if(!dashBroad.getApplier_id().contains(user)){
             new IllegalAccessException("no application found");
        }


    }

    public InfoResponse acceptApplication(int orderId, int posterId,int applyId) {
        //check userId ,
        DashBroad dashBroad= dashBroadRepository.findByOrder_id(orderId);
        assert dashBroad!=null;
        InfoResponse acceptedProfile=userCoreService.getProfile(applyId);
        assert acceptedProfile!=null;
        User user=userCoreService.getUser(applyId);
        assert user!=null;
        VerifyCanAcceptApplication(dashBroad,user,acceptedProfile);


        //update the accepted list
        log.info("valid request");
        dashBroad.getAccepted_id().add(userCoreService.getUser(applyId));
        dashBroad.setAccepted_id(dashBroad.getAccepted_id());
        dashBroad.setApplication_remain(dashBroad.getApplication_remain()-1);
        dashBroadRepository.save(dashBroad);

        JobOrder jobOrder=jobRepository.findById(orderId).orElse(null);
        //update the joborder
        if(dashBroad.getApplication_remain()==0){
            assert jobOrder != null;
            jobOrder.setIsaccepted(true);
            jobRepository.save(jobOrder);
        }

        //send notice
        String posterNotice="You have successfully accepted "+acceptedProfile.getUsername()+"'s application for job id:"+orderId;
        sendNotice(posterNotice,applyId);

        String ApplyNotice="Your application for Job with title '"+jobOrder.getTitle()+"' has been accepted" +
                " at";
        sendNotice(ApplyNotice,applyId);

        //build a chatroom=>send chat

        log.info("Start sending chat");
        int sizeOfRoom=dashBroad.getAccepted_id().size();
        System.out.println(sizeOfRoom);
        String username=userCoreService.findById(posterId).getUsername();
        System.out.println("sizeOfRoom: "+sizeOfRoom);
        if(sizeOfRoom==1){ //no room found=> build room
            ChatMessage buildmessage=new ChatMessage(null,orderId, ChatMessage.MessageType.BUILD,
                    username+" has built the room",username,null);
            generateChat(buildmessage);

        }
        ChatMessage Joinmessage=new ChatMessage(null,orderId, ChatMessage.MessageType.JOIN,
                acceptedProfile.getUsername()+" has join the room",
                acceptedProfile.getUsername(),null);
        generateChat(Joinmessage);

        return acceptedProfile;

    }


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




    public String deleteApplication(int orderId, int applyId) {
        //verify
        DashBroad job= dashBroadRepository.findByOrder_id(orderId);
        if(job==null){
            log.error("no joborder found");
            return "no joborder found";
        }
        User user=userCoreService.getUser(applyId);
        System.out.println("Hi"+user.getApplications());
        if(!job.getApplier_id().contains(user)){
            log.error("user havent apply the job");
            return "you have not apply job";
        }

        //remove user in job
        job.getApplier_id().remove(user);
        dashBroadRepository.save(job);

        //remove application in user
        if(jobRepository.findById(orderId)==null){
            return "no application found";
        }
        JobOrder jobOrder=jobRepository.findById(orderId).orElse(null);
        user.getApplications().remove(jobOrder);
        System.out.println(jobRepository.findById(orderId));
        userCoreService.saveAndReturn(user);
        System.out.println(user.getApplications());

        return "you have successfully removed";


}
    public List<InfoResponse> showAccepted(int order_id,int user_id){
        DashBroad dashBroad= dashBroadRepository.findByOrder_id(order_id);
        if(dashBroad==null){
            log.error("no requested id found");
            return null;
        }
       /* if(dashBroad.getPoster_id()!=user_id){
            log.error("only poster can read the application detail");
            return null;
        }*/
        log.info("user id "+user_id+" request for accessing applications in job id: "+order_id);
        //find the list of application
        //map user_id to user profile=>collect a list of profile
        return dashBroad.getAccepted_id().stream()
                .map(res->userCoreService.getProfile(res.getId()))
                .collect(Collectors.toList());
    };


}
