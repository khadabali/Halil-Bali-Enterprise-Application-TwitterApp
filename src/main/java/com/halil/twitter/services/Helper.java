package com.halil.twitter.services;

import java.sql.Timestamp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.halil.twitter.entity.User;
import com.halil.twitter.repo.UserRepo;

@Component
public class Helper {

	@Autowired
	private UserRepo userRepo;
	
	public String tokenValidate(String token) {
		User findByToken = userRepo.findByToken(token);
		if(findByToken == null) {
			return "INVALID"; 
		}
		
        Timestamp givenTimestamp = Timestamp.valueOf(findByToken.getLastLoggedin().toString());

        Timestamp currentTimestamp = new Timestamp(System.currentTimeMillis());

        long timeDifferenceMillis = currentTimestamp.getTime() - givenTimestamp.getTime();

        //15 minute = 9000000 milliseconds
        if(timeDifferenceMillis > 900000) {
//        	System.out.println("Expired " + timeDifferenceMillis);
            return "EXPIRED";	
        }else { 
//        	System.out.println("All is ok " + timeDifferenceMillis);
    		return null;
        }     

	}
	
}
