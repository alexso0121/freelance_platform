package com.springboot.sohinalex.java.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class Chatroom {
   private int order_id;
   private int host_id;
   private List<Integer> userList=new ArrayList<>();

}
