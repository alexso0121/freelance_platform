package com.example_service.User;

import com.example_service.User.Model.User;
import com.example_service.User.Repository.UserRespository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Primary;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.testcontainers.junit.jupiter.Testcontainers;


import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
class UserApplicationTests {


	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private UserRespository userRespository;
	@Autowired
	private Bean bean;


	@Autowired
	private ObjectMapper objectMapper;


	@Test
	void shouldCreateUser() throws Exception {
		User user=mockuser();
		String request=objectMapper.writeValueAsString(user);

		mockMvc.perform(MockMvcRequestBuilders.post("/adduser")
				.contentType(MediaType.APPLICATION_JSON)
				.content(request))
				.andExpect(status().isCreated());
		Assertions.assertEquals(1, userRespository.findAll().size());


}

	@Test
	void shouldGetUser() throws Exception {
		userRespository.save(mockuser());
		mockMvc.perform(MockMvcRequestBuilders.get("/user/{id}",1)
				).andExpect(status().isFound())
				.andExpect(jsonPath("$.name").value("alex"));
	}

	@Test
	void shouldUpdateUser() throws Exception {
		userRespository.save(mockuser());
		User user=new User();
		user.setId(1);
		user.setName("Ben");

		String request=objectMapper.writeValueAsString(user);

		MvcResult result=mockMvc.perform(MockMvcRequestBuilders.put("/updateuser")
				.contentType(MediaType.APPLICATION_JSON)
				.content(request))
				.andExpect(status().is2xxSuccessful())
				.andReturn();

		Assertions.assertEquals("Successful update",result.getResponse().getContentAsString());

	}

	@Test
	void shouldDeleteUser() throws Exception {
		userRespository.save(mockuser());

		MvcResult result=mockMvc.perform(MockMvcRequestBuilders.delete("/deleteuser/{id}",1))
				.andExpect(status().is2xxSuccessful())
				.andReturn();

		Assertions.assertEquals("User with id 1is removed",result.getResponse().getContentAsString());
		Assertions.assertEquals(0,userRespository.findAll().size());

	}


	User mockuser(){
		User user=new User();
		user.setId(1);
		user.setName("alex");
		user.setEmail("sdf");
		return user;
	}



}



