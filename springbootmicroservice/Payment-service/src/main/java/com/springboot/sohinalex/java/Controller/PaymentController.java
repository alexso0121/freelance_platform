package com.springboot.sohinalex.java.Controller;

import com.springboot.sohinalex.java.Entity.Payment;
import com.springboot.sohinalex.java.Repository.PaymentRepository;
import com.springboot.sohinalex.java.Service.PaymentService;
import com.springboot.sohinalex.java.dto.CustomerRequest;
import com.springboot.sohinalex.java.dto.PaymentRequest;
import com.stripe.model.Customer;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;


/*
** 0.02 % administrative fee will be charged

Freelance_platform Payment workflow
1.job poster paid for the job to the platform after it finds enough customers (administrative fee will be charged )
2.freelancer finish the jobs
3.platform paid the salaries back to the freelancer

Stripe Payment workflow:
1.frontend send credit card credential to Stripe and token is returned
2.the token ,email ,user_id are sent to backend
3.the token and email is used to create the customer class is saved in Stripe
4.The detail of the payment transaction will be saved to the db
 */

@RestController
@RequestMapping("/Payment")
public class PaymentController {
    private final PaymentService paymentService;
    private final PaymentRepository paymentRepository;


    public PaymentController(PaymentService paymentService, PaymentRepository paymentRepository) {
        this.paymentService = paymentService;


        this.paymentRepository = paymentRepository;
    }

    //for user to save the customer in the Stripe Side
    //better save the customer_id in the front_end side
    @PostMapping("/customer/build")
    public Mono<ResponseEntity<Customer>> CreateClientForStripe(@RequestBody CustomerRequest request) throws Exception {
        return paymentService.createCustomer(request.getToken(),request.getEmail(),request.getUser_id());
    }

    //api for job poster paid to the platform
    @PostMapping("/transfer/Poster")
    public Mono<ResponseEntity<Payment>> PosterPaymentToPlatform(@RequestBody PaymentRequest request) throws Exception {
        return paymentService.chargeCustomerCard(request.getCustomerId(), request.getAmount(), request.getUser_id());
    }

    //platform paid to freelancer after jobs
    @PostMapping("/transfer/payout")
    public Mono<ResponseEntity<Payment>> PlatformPaymentToFreelacncer(@RequestBody PaymentRequest request) throws Exception{
        return paymentService.payoutToFreelancer(request.getCustomerId(), request.getAmount(), request.getUser_id());
    }

    //api for refund from platform to job poster
    @PostMapping("/refund/{charge_id}")
    public  Mono<ResponseEntity<Payment>> Refund(@PathVariable String charge_id){
        return paymentService.refundPayment(charge_id);
    }

    //display all payment for the user base on user_id
    @GetMapping("/transaction/display/")
    public Flux<Payment> showTransactionByUserId(@RequestParam UUID user_id){
        return paymentRepository.findByUser_id(user_id);
    }

    //display all data in db
    @GetMapping("/transaction/all")
    public Flux<Payment> showTransactionAll(){
        return paymentRepository.findAll();
    }

}