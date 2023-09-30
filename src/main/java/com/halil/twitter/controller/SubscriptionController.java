package com.halil.twitter.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.halil.twitter.entity.User;
import com.halil.twitter.entity.UserSubscribed;
import com.halil.twitter.repo.UserRepo;
import com.halil.twitter.repo.UserSubscribedRepo;
import com.halil.twitter.services.Helper;

@RestController
public class SubscriptionController {

	
	@Autowired
	private Helper helper;
	
	@Autowired
	private UserRepo userRepo;
	
	@Autowired
	private UserSubscribedRepo userSubscribedRepo;
	
	@PostMapping("/token/{token}/subscribe-to")
	public ResponseEntity<?> subscribeToProducer(
			@PathVariable String token,
			@RequestParam("producer") String producerToken
			){
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
		
		
		if(producerToken == null || producerToken.equals("")) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body("{\"message\": \" Missing Producer token\"}");
		}
		
		
		//producer validation
		int parseInt = 0;
		try {
			parseInt = Integer.parseInt(producerToken);
		}catch(Exception e){
			
		}
		User producer = userRepo.findByTokenOrId(producerToken, parseInt);
		
		if(producer == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND)
					.body("{\"message\": \" Producer not found\"}");
		}
		
				
		UserSubscribed findByProducerAndSubscriber = userSubscribedRepo.findByProducerAndSubscriber(producer, subscriber);
		if(findByProducerAndSubscriber != null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body("{\"message\": \"Already subscribed to producer "+producer.getUsername()+"\"}");
		}
		
		
		UserSubscribed userSubscribed = new UserSubscribed();
		userSubscribed.setProducer(producer);
		userSubscribed.setSubscriber(subscriber);
		
		userSubscribedRepo.save(userSubscribed);		
		
		return ResponseEntity.ok("Successfully subscribed to Producer : " + producer.getUsername());
	}
	
	
	
	
	
	
	@GetMapping("/token/{token}/my-subscriptions")
	public ResponseEntity<?> getMySubscriptions(@PathVariable String token){
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
		
		List<UserSubscribed> findBySubscriber = userSubscribedRepo.findBySubscriber(subscriber);		
		
		return ResponseEntity.ok(findBySubscriber);
	}
	
	
	
	
	
	@GetMapping("/token/{token}/my-subscribers")
	public ResponseEntity<?> getMySubscribers(@PathVariable String token){
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
		if(!subscriber.getRole().equals("PRODUCER")) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN)
					.body("{\"message\": \" You are not a PRODUCER!!\"}");
		}
		
		List<UserSubscribed> findbyProducer = userSubscribedRepo.findByProducer(subscriber);		
		
		return ResponseEntity.ok(findbyProducer);
	}
	
	
	
	
	
	
	
	
	
	
	
}
