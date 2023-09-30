package com.halil.twitter.services;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.halil.twitter.entity.User;
import com.halil.twitter.repo.UserRepo;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class CustomSuccessHandler implements AuthenticationSuccessHandler {

	@Autowired
	UserRepo userRepo;
	
	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {
		
		System.out.println("SUCCESSFULL GITHUB OAuth LOGIN");
		
		
//		String redirectUrl = null;
		String username = null;
		String token = null;
		String role = null;
		
		if (authentication.getPrincipal() instanceof DefaultOAuth2User) {			
			DefaultOAuth2User userDetails = (DefaultOAuth2User) authentication.getPrincipal();
			
			username = userDetails.getAttribute("email") != null ? userDetails.getAttribute("email")
					: userDetails.getAttribute("login") + "@gmail.com";
			
			User findByUsername = userRepo.findByUsername(username);
			
			User userSend = null;
			
			UUID uuid = UUID.randomUUID();
	        String uuidString = uuid.toString();
	        
			if ( findByUsername == null) {			
				User user = new User();			
				user.setFullName(userDetails.getAttribute("name"));
				user.setUsername(username);
				user.setRole("SUBSCRIBER");
				user.setPassword(null);					
		        user.setToken(uuidString);
		        token = uuidString;	
		        role = "SUBSCRIBER";
				User save = userRepo.save(user);
				userSend = save;
			}else {
				userSend = findByUsername;
				token = findByUsername.getToken();
				role = findByUsername.getRole();
			}			
			
			userSend.setLastLoggedin(new Timestamp(new Date().getTime()));
			
			userRepo.save(userSend);
		}


		String redirectUrl = "/my-token?token="+token+"&username="+username+"&role="+role;
		new DefaultRedirectStrategy().sendRedirect(request, response, redirectUrl);		
	}

}
