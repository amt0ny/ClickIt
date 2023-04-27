package com.it.click.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import com.it.click.common.JwtResponse;
import com.it.click.common.LoginData;
import com.it.click.entites.MainProfile;
import com.it.click.service.IClickService;

@RestController
@RequestMapping
public class ClickController {
	
	@Autowired
	private IClickService clickService;
	
	@GetMapping("/test")
	public String test() {
		return "Yes working";
	}

	@PostMapping("/sendOtp")
	public String sendOtp(@RequestBody LoginData loginData) {
		
		return clickService.generateAndSendEmailOtp(loginData);
	}
	
	@PostMapping("/varifyOtp")
	public boolean varifyEmailByOtp(@RequestBody LoginData loginData) {
		
		return clickService.emailVarification(loginData);
	}
	
	@PostMapping("/signUp")
	public String addUser(@RequestBody MainProfile mainProfile) {
		
		return clickService.addUser(mainProfile);
	}
	
	@PostMapping("/login")
	public JwtResponse userLogin(@RequestBody LoginData loginRequest){
		
		return clickService.login(loginRequest);
	}
	
}
