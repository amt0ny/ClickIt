package com.it.click.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import com.it.click.common.JwtResponse;
import com.it.click.common.LoginData;
import com.it.click.entites.EmailPass;
import com.it.click.entites.MainProfile;
import com.it.click.service.IClickService;

import io.swagger.annotations.Authorization;

@CrossOrigin(origins = "*", methods = { RequestMethod.POST }, allowedHeaders = "*")
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
	public String sendOtp(@RequestBody EmailPass emailPass) {
		
		return clickService.generateAndSendEmailOtp(emailPass);
	}
	
	@PostMapping("/varifyOtp")
	public JwtResponse varifyEmailByOtp(@RequestBody LoginData loginData) {
		
		return clickService.otpVarification(loginData);
	}
	
	@PostMapping("/signUp")
	public String addUser(@RequestBody MainProfile mainProfile, @RequestHeader String token) {
		
		return clickService.signUp(mainProfile,token);
	}
	
	@PostMapping("/login")
	public JwtResponse userLogin(@RequestBody EmailPass emailPass){
		
		return clickService.login(emailPass);
	}
	
}
