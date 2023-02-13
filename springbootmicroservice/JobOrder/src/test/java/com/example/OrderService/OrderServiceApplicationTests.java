package com.example.OrderService;

import com.example.OrderService.Entity.JobOrder;
import com.example.OrderService.Repository.JobRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.aspectj.apache.bcel.util.ClassPath;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestExecutionListeners;

import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlScriptsTestExecutionListener;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest//(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Sql("/schema.sql")
class OrderServiceApplicationTests {
	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private JobRepository jobRepository;


	//@Test
	void expectHaveInit(){
		Assertions.assertEquals(1, jobRepository.findAll().size());
	}

	//@Sql("INSERT INTO user_info(name) VALUES \"alex\" ")
	@Test
	void shouldCreatePost() throws Exception {
		JobOrder job=jobRepository.save(createjob());
		String request=objectMapper.writeValueAsString(job);


		mockMvc.perform(MockMvcRequestBuilders.post("/Job/Joborder")
				.contentType(MediaType.APPLICATION_JSON)
				.content(request))
				.andExpect(status().is2xxSuccessful());


	}



	JobOrder createjob(){
		JobOrder job=new JobOrder();
		job.setTitle("Hiring software engineer");
		job.setDescription("responsible for developing and maintaining mobile app");
		job.setRequirement("Need to know basic sql springboot,2 years exp");
		job.setSalary(234);
		job.setOrder_id(1);
		return job;

	}
}
