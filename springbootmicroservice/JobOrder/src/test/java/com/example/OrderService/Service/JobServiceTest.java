package com.example.OrderService.Service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class JobServiceTest {
    @Autowired
    private JobService jobService;




    @Test
    void postJob() {
    }

    @Test
    void getSingleJob() {

    }

    @Test
    void findByOrderid() {
    }

    @Test
    void editJob() {
    }

    @Test
    void showApplications() {
    }

    @Test
    void postApplication() {
    }
}