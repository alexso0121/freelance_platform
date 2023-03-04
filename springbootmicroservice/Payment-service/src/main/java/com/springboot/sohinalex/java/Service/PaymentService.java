package com.springboot.sohinalex.java.Service;

import com.springboot.sohinalex.java.Entity.Payment;
import com.springboot.sohinalex.java.Repository.PaymentRepository;
import com.springboot.sohinalex.java.dto.NoticeRespond;
import com.stripe.Stripe;
import com.stripe.exception.*;
import com.stripe.model.Charge;
import com.stripe.model.Customer;
import com.stripe.model.Refund;
import com.stripe.model.Transfer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


import javax.crypto.SecretKey;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class PaymentService {
    private final PaymentRepository paymentRepository;

    @Autowired
    private KafkaTemplate<String, NoticeRespond> kafkaTemplate;



    public PaymentService(@Value("${Stripe.secretKey}") String secretKey, PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
        Stripe.apiKey = secretKey;
    }


        //for user to save the customer in the Stripe Side
        //better save the customer_id in the front_end side
        public Mono<ResponseEntity<Customer>> createCustomer(String token, String email,UUID user_id) throws Exception {
            Map<String, Object> customerParams = new HashMap<String, Object>();
            customerParams.put("email", email);
            customerParams.put("source", token);

            //send notice
            String notification= "Your credit card is successfully verified.You are now ready for online payment !";
            sendNotice(notification,user_id);
            return Mono.just(ResponseEntity.status(HttpStatus.CREATED)
                            .body(
                    Customer.create(customerParams)));

        }

        public Charge buildCharge(String customerId, double amount) throws Exception {

            String sourceCard =     Customer.retrieve(customerId).getDefaultSource();
            Map<String, Object> chargeParams = new HashMap<String, Object>();
            chargeParams.put("amount", amount);
            chargeParams.put("currency", "USD");
            chargeParams.put("customer", customerId);
            chargeParams.put("source", sourceCard);
            return Charge.create(chargeParams);

        }

    public void sendNotice(String notification,UUID userid){
        LocalDateTime myDateObj = LocalDateTime.now();
        DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        String formattedDate = myDateObj.format(myFormatObj);
        kafkaTemplate.send("notificationTopic",new NoticeRespond(
                userid,formattedDate,notification
        ));
    }

    public Mono<ResponseEntity<Payment>> chargeCustomerCard(String customerId, double amount, UUID user_id) throws Exception {
        amount*=1.02; //fee for administration
        Charge charge=buildCharge(customerId,amount);

        //send notice
        String notification= "The payment is successful.Pls notice us if your freelancer finish the job or if you want to cancel the order";
        sendNotice(notification,user_id);


        return savepayment(charge.getId(), Payment.PaymentType.CHARGE,user_id,amount);


    }
    //main saving method
    public Mono<ResponseEntity<Payment>> savepayment(String charge_id,Payment.PaymentType type,UUID user_id,Double amount){
        return paymentRepository.save(Payment.builder()
                        .charge_id(charge_id)
                        .paymentType(type)
                        .user_id(user_id)
                        .amount(amount)
                        .dateTime(Instant.now())
                        .build())
                .switchIfEmpty(Mono.error(new Error("cannot save the job")))
                .map(res->{
                    System.out.println(res);
                    return ResponseEntity.status(HttpStatus.CREATED)
                            .body(res);
                });
    }

    public Mono<ResponseEntity<Payment>> payoutToFreelancer(String customerId, Double amount, UUID user_id) throws APIConnectionException, APIException, AuthenticationException, InvalidRequestException, CardException {
        Map<String, Object> transferParams = new HashMap<String, Object>();
        transferParams.put("amount", amount);
        transferParams.put("currency", "usd");
        transferParams.put("destination", customerId);
        //send notice
        String notification= "The job salaries is already transferred to your account.Pls contact us if you cannot received the payment";
        sendNotice(notification,user_id);

        Transfer transfer = Transfer.create(transferParams);
        return savepayment(transfer.getId(), Payment.PaymentType.TRANSFER,user_id,amount);

    }

    public Mono<ResponseEntity<Payment>> refundPayment(String chargeId)  {
        return paymentRepository.findById(chargeId)
                .switchIfEmpty(Mono.error(new Error("new chargeId found")))
                .flatMap(payment -> {

                        Map<String, Object> params = new HashMap<String, Object>();
                        params.put("charge", chargeId);
                        params.put("amount", payment.getAmount());

                    Refund refund = null;
                    try {
                        refund = Refund.create(params);
                    } catch (Exception exception){
                        throw new Error("stripe error");
                    }
                    String notification = "The job salaries is already transferred to your account.Pls contact us if you cannot received the payment";
                        sendNotice(notification, payment.getUser_id());

                        return savepayment(refund.getId(), Payment.PaymentType.REFUND,payment.getUser_id(),payment.getAmount());

                });


    }


}

