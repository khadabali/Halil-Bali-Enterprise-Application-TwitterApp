package com.halil.twitter.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.halil.twitter.entity.Message;
import com.halil.twitter.entity.User;

public interface MessageRepo extends JpaRepository<Message, Integer> {
	List<Message> findByProducer(User producer);
}
