package com.halil.twitter.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.halil.twitter.entity.User;

public interface UserRepo extends JpaRepository<User, Integer> {
	User findByUsername(String username);
	User findByToken(String token);
	
	User findByTokenOrId(String token, int id);
	
	List<User> findByRole(String role);
	
}
