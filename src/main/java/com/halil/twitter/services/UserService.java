package com.halil.twitter.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.halil.twitter.entity.User;
import com.halil.twitter.repo.UserRepo;

@Component
public class UserService {

	@Autowired
	private UserRepo userRepo;
	
	
	public User findUserByUserName(String username) {
		User findByUsername = userRepo.findByUsername(username);
		if(findByUsername == null) {
			return null;			
		}
		return findByUsername;
	}
	
}
