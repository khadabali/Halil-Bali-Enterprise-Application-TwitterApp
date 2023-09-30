package com.halil.twitter.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

@Entity
public class UserSubscribed {


	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@ManyToOne
	private User subscriber;
	
	@ManyToOne
	private User producer;
	
	public UserSubscribed() {
		// TODO Auto-generated constructor stub
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public User getSubscriber() {
		return subscriber;
	}
	public void setSubscriber(User subscriber) {
		this.subscriber = subscriber;
	}
	public User getProducer() {
		return producer;
	}
	public void setProducer(User producer) {
		this.producer = producer;
	}
	
	
	
}
