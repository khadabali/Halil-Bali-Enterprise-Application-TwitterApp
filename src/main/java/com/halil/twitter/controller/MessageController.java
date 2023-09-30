package com.halil.twitter.controller;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.halil.twitter.entity.Message;
import com.halil.twitter.entity.User;
import com.halil.twitter.repo.MessageRepo;
import com.halil.twitter.repo.UserRepo;
import com.halil.twitter.services.Helper;
import com.halil.twitter.services.MessageService;

@RestController
public class MessageController {

	@Autowired
	private Helper helper;
	
	@Autowired
	private UserRepo userRepo;
	
	@Autowired
	private MessageRepo messageRepo;
	
	@Autowired
	private MessageService messageService;

	@PostMapping("/token/{token}/create-message")
	public ResponseEntity<?> getMySubscriptions(@PathVariable String token, @RequestBody Message messageBody){
		// Token validation
		String tokenValidate = helper.tokenValidate(token);
		if (tokenValidate != null) {
			if (tokenValidate == "INVALID") {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body("{\"message\": \" User not found \"}");
			} else if (tokenValidate == "EXPIRED") {
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
						.body("{\"message\": \" Session expired, please Login again\"}");
			}
		}
				
		//user role validation
		User producer = userRepo.findByToken(token);
		if(!producer.getRole().equals("PRODUCER")) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN)
					.body("{\"message\": \" You are not a Producer!! Only producer can post message\"}");
		}
		
		
		Message message = new Message();
		
		message.setMessage(messageBody.getMessage());
		message.setProducer(producer);
		message.setTimestamp(new Timestamp(new Date().getTime()));
		
		Message save = messageRepo.save(message);
		
		return ResponseEntity.ok(save);
	}
	

	
	
//	v.	Get messages produces by specific producer	
	@GetMapping("/token/{token}/message/producer/{tokenOrId}")
	public ResponseEntity<?> getMessageOfProducer(@PathVariable String token, @PathVariable String tokenOrId) {
		// Token validation
		String tokenValidate = helper.tokenValidate(token);
		if (tokenValidate != null) {
			if (tokenValidate == "INVALID") {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body("{\"message\": \" User not found \"}");
			} else if (tokenValidate == "EXPIRED") {
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
						.body("{\"message\": \" Session expired, please Login again\"}");
			}
		}

		if (tokenOrId == null || tokenOrId.equals("")) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"message\": \" Missing Producer token\"}");
		}

		// producer validation
		int parseInt = 0;
		try {
			parseInt = Integer.parseInt(tokenOrId);
		} catch (Exception e) {

		}

		User producer = userRepo.findByTokenOrId(tokenOrId, parseInt);
		if (producer == null || !producer.getRole().equals("PRODUCER")) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND)
					.body("{\"message\": \"No match found for Producer token or Id!!\"}");
		}

		List<Message> findByProducer = messageRepo.findByProducer(producer);

		return ResponseEntity.ok(findByProducer);
	}
	
	
	
	
	//vi.	Get messages for specific subscriber
	@GetMapping("/token/{token}/my-messages")
	public ResponseEntity<?> myMessages(@PathVariable String token) {
		// Token validation
		String tokenValidate = helper.tokenValidate(token);
		if (tokenValidate != null) {
			if (tokenValidate == "INVALID") {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body("{\"message\": \" User not found \"}");
			} else if (tokenValidate == "EXPIRED") {
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
						.body("{\"message\": \" Session expired, please Login again\"}");
			}
		}

		//user role validation
		User subscriber = userRepo.findByToken(token);
		if(!subscriber.getRole().equals("SUBSCRIBER")) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN)
					.body("{\"message\": \" You are not a Subscriber!!\"}");
		}
		
		List<Message> allMessagesOfSubscriber = messageService.getAllMessagesOfSubscriber(subscriber);		
		
		return ResponseEntity.ok(allMessagesOfSubscriber);
	}
	
	
}
