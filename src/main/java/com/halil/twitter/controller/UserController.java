package com.halil.twitter.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.halil.twitter.entity.User;
import com.halil.twitter.repo.UserRepo;
import com.halil.twitter.services.Helper;

@RestController
public class UserController {

	@Autowired
	Helper helper;
	
	@Autowired
	UserRepo userRepo;
	
	@GetMapping("/token/{token}/my-info")
	public ResponseEntity<?> myinfo(@PathVariable String token){
		
		//Token validation
		String tokenValidate = helper.tokenValidate(token);
		if(tokenValidate != null) {
			if(tokenValidate == "INVALID" ) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body("{\"message\": \" User not found \"}");
			}else if(tokenValidate == "EXPIRED") {
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("{\"message\": \" Session expired, please Login again\"}");
			}
		}		
		
		User findByRole = userRepo.findByToken(token);		
		
		return ResponseEntity.ok(findByRole);
	}
	
	
	@GetMapping("/token/{token}/get-producers")
	public ResponseEntity<?> getAllProducer(@PathVariable String token){
		
		//Token validation
		String tokenValidate = helper.tokenValidate(token);
		if(tokenValidate != null) {
			if(tokenValidate == "INVALID" ) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body("{\"message\": \" User not found \"}");
			}else if(tokenValidate == "EXPIRED") {
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("{\"message\": \" Session expired, please Login again\"}");
			}
		}		
		
		List<User> findByRole = userRepo.findByRole("PRODUCER");		
		
		return ResponseEntity.ok(findByRole);
	}
	
	
	
	
	
	@GetMapping("/token/{token}/get-subscribers")
	public ResponseEntity<?> getAllSubscribers(@PathVariable String token){
		
		//Token validation
		String tokenValidate = helper.tokenValidate(token);
		if(tokenValidate != null) {
			if(tokenValidate == "INVALID" ) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body("{\"message\": \" User not found \"}");
			}else if(tokenValidate == "EXPIRED") {
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("{\"message\": \" Session expired, please Login again\"}");
			}
		}
		
		List<User> findByRole = userRepo.findByRole("SUBSCRIBER");		
		
		return ResponseEntity.ok(findByRole);
	}
	
	
	
	
	@PostMapping("/token/{token}/change-role")
	public ResponseEntity<?> cahngeRole(@PathVariable String token, @RequestBody User user) {

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

		User findByToken = userRepo.findByToken(token);
		findByToken.setRole(user.getRole());
		User save = userRepo.save(findByToken);
		
		return ResponseEntity.ok(save);
	}
	
	
	
	
	
	
}
