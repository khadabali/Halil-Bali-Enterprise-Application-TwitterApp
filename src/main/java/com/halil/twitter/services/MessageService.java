package com.halil.twitter.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.halil.twitter.entity.Message;
import com.halil.twitter.entity.User;
import com.halil.twitter.entity.UserSubscribed;
import com.halil.twitter.repo.MessageRepo;
import com.halil.twitter.repo.UserSubscribedRepo;

@Component
public class MessageService {

	@Autowired
	UserSubscribedRepo userSubscribedRepo;
	
	@Autowired
	private MessageRepo messageRepo;
	
	public List<Message> getAllMessagesOfSubscriber(User subscriber){
		
		List<UserSubscribed> producersList = userSubscribedRepo.findBySubscriber(subscriber);
		
		List<Message> allMessages = new ArrayList<>();
		
		for(UserSubscribed userSubscribed : producersList) {
			User producer = userSubscribed.getProducer();			
			List<Message> producersMessage = messageRepo.findByProducer(producer);
			
			for(Message message : producersMessage){
				allMessages.add(message);
			}
			
		}		
		
		return allMessages;
	}
	
}

