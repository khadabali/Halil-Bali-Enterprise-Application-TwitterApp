package com.halil.twitter.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.halil.twitter.payload.LoginResponse;

@Controller
public class ViewController {

	
	@GetMapping("/my-token")
	public String tokenInfo(
			@RequestParam("token") String token,
			@RequestParam("username") String username,
			@RequestParam("role") String role
			){
		LoginResponse loginResponse = new LoginResponse();
		loginResponse.setToken(token);
		loginResponse.setUsername(username);
		loginResponse.setRole(role);
		return "token";
	}
		
	
}
