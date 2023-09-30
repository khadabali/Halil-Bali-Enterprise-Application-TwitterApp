package com.halil.twitter.controller;

import java.sql.Timestamp;
import java.util.Collections;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.halil.twitter.entity.User;
import com.halil.twitter.payload.LoginBody;
import com.halil.twitter.payload.LoginResponse;
import com.halil.twitter.repo.UserRepo;
import com.halil.twitter.services.UserService;

@RestController
public class HomeController {

	@Autowired
	UserRepo userRepo;
	
	@Autowired
	UserService userService;
	
	@PostMapping("/register")
	public ResponseEntity<?> register(@RequestBody User user){	
		
		User findUserByUserName = userService.findUserByUserName(user.getUsername());
		if (findUserByUserName != null) {
	        // User already exists, return an error message as JSON
	        String errorMessage = "User already exists";
	        return ResponseEntity.badRequest().body("{\"message\": \"" + errorMessage + "\"}");
	    }
		
		
        UUID uuid = UUID.randomUUID();
        String uuidString = uuid.toString();
        user.setToken(uuidString);
		
		User savedUser = userRepo.save(user);	
		return ResponseEntity.ok(savedUser);
	}
	
	
	
	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody LoginBody body){
		
		User findUserByUserName = userService.findUserByUserName(body.getUsername());
		if (findUserByUserName == null) {
	        String errorMessage = "User Not found";
	        return ResponseEntity.badRequest().body("{\"message\": \"" + errorMessage + "\"}");
	    }
		
		if(!findUserByUserName.getPassword().equals(body.getPassword()) ) {
			String errorMessage = "Password not matching";
	        return ResponseEntity.badRequest().body("{\"message\": \"" + errorMessage + "\"}");
		}
		
		
		findUserByUserName.setLastLoggedin(new Timestamp(new Date().getTime()));
		
		User save = userRepo.save(findUserByUserName);
		
		LoginResponse loginResponse = new LoginResponse(); 
				loginResponse.setToken(save.getToken());
				loginResponse.setUsername(save.getUsername());
				loginResponse.setRole(save.getRole());
		return ResponseEntity.ok(loginResponse);
	}
	
	
	
	
	
	
	@GetMapping("/user")
    public Map<String, Object> user(@AuthenticationPrincipal OAuth2User principal) {
        return Collections.singletonMap("name", principal.getAttribute("name"));
    }
	
			
	@GetMapping("/test")
	public ResponseEntity<?> test(){
		return ResponseEntity.ok("All is okay");
	}	

}
