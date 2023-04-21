package com.it.click.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.it.click.common.EmailVarificationData;
import com.it.click.common.LoginRequest;
import com.it.click.entites.MainProfile;
import com.it.click.service.IClickService;

@RestController
@RequestMapping
public class ClickController {
	
	@Autowired
	private IClickService clickService;
	
	@PostMapping("/sendOtp")
	public String sendOtp(@RequestBody EmailVarificationData emailVarificationData) {
		
		return clickService.generateAndSendEmailOtp(emailVarificationData);
	}
	
	@GetMapping("/varifyEmail")
	public boolean varifyEmailByOtp(@RequestBody EmailVarificationData emailVarificationData) {
		
		return clickService.emailVarification(emailVarificationData);
	}
	
	@PostMapping("/signUp")
	public String addUser(@RequestBody MainProfile mainProfile) {
		
		return clickService.addUser(mainProfile);
	}
	
	@PostMapping("/login")
	public boolean userLogin(@RequestBody LoginRequest loginRequest){
		
		return clickService.login(loginRequest);
	}
	

}
