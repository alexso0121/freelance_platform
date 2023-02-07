package com.example_service.User;

import com.example_service.User.Model.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

//integration test for crud template
//need to set the sql properties ->create-drop

@SpringBootTest
@AutoConfigureMockMvc
class UserApplicationTests {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private UserRespository userRespository;

	@Autowired
	private ObjectMapper objectMapper;


	//test post
	@Test
	void shouldCreateUser() throws Exception {
		//build user object
		User user=mockuser();
		String request=objectMapper.writeValueAsString(user);

		mockMvc.perform(MockMvcRequestBuilders.post("/User/adduser")
				.contentType(MediaType.APPLICATION_JSON)
				.content(request))
				.andExpect(status().isCreated());
		//Assertion
		Assertions.assertEquals(1, userRespository.findAll().size());


}

	//test get
	@Test
	void shouldGetUser() throws Exception {
		//save the record first
		userRespository.save(mockuser());
		mockMvc.perform(MockMvcRequestBuilders.get("/User/user/{id}",1)
				).andExpect(status().isFound())
				.andExpect(jsonPath("$.name")
						.value("alex"));
	}

	//test update
	@Test
	void shouldUpdateUser() throws Exception {
		userRespository.save(mockuser());
		User user=new User();
		user.setId(1);
		user.setName("Ben");

		String request=objectMapper.writeValueAsString(user);

		MvcResult result=mockMvc.perform(MockMvcRequestBuilders.put("/User/updateuser")
				.contentType(MediaType.APPLICATION_JSON)
				.content(request))
				.andExpect(status().is2xxSuccessful())
				.andReturn();

		Assertions.assertEquals("Successful update",result.getResponse().getContentAsString());

	}

	//test delete
	@Test
	void shouldDeleteUser() throws Exception {
		userRespository.save(mockuser());

		MvcResult result=mockMvc.perform(MockMvcRequestBuilders.delete("/User/deleteuser/{id}",1))
				.andExpect(status().is2xxSuccessful())
				.andReturn();

		Assertions.assertEquals("User with id 1is removed",result.getResponse().getContentAsString());
		Assertions.assertEquals(0,userRespository.findAll().size());

	}


	//build a user
	User mockuser(){
		User user=new User();
		user.setId(1);
		user.setName("alex");
		user.setEmail("sdf");
		return user;
	}



}



