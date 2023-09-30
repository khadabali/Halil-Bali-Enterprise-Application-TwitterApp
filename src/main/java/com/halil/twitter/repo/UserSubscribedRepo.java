package com.halil.twitter.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.halil.twitter.entity.User;
import com.halil.twitter.entity.UserSubscribed;

public interface UserSubscribedRepo extends JpaRepository<UserSubscribed, Integer> {

	UserSubscribed findByProducerAndSubscriber(User producer, User subscriber);
	
	List<UserSubscribed> findByProducer(User producer);
	
	List<UserSubscribed> findBySubscriber(User subscriber);
	
}
