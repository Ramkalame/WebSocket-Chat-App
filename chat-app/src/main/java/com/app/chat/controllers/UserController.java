package com.app.chat.controllers;

import java.io.Console;

import org.apache.logging.log4j.message.Message;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {
	
	
	public void sendMessage() {
		
	}
	
	@MessageMapping("/message")
	public void sendMessage(Message message) {
		
		System.out.println(message);
		
		
	}

}
