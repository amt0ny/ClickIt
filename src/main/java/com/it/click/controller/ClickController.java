package com.it.click.controller;

import java.time.LocalDate;
import java.util.List;

import com.it.click.common.LoginData;
import com.it.click.entities.RegisterData;
import com.it.click.entities.UserMaster;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import com.it.click.common.JwtResponse;
import com.it.click.responses.BasicProfileResponse;
import com.it.click.service.IClickService;

@CrossOrigin(origins = "*", methods = { RequestMethod.POST, RequestMethod.GET }, allowedHeaders = "*")
@RestController
@RequestMapping
public class ClickController {

	@Autowired
	private IClickService clickService;

	@PostMapping("/registerUser")
	public String addUser(@RequestBody RegisterData registerData) {
		return clickService.signUp(registerData);
	}
	
	@PostMapping("/login")
	public JwtResponse userLogin(@RequestBody LoginData loginData){
		return clickService.login(loginData);
	}

	@PostMapping("/updateProfile")
	public String completeProfile(@RequestBody UserMaster userMasterData, @RequestHeader String token){
		return clickService.updateProfile(userMasterData, token);
	}
	
	@GetMapping("/getUserDashBoardByToken")
	public UserMaster getUsersList(@RequestHeader String token){
		return clickService.getUserDashBoardByRole(token);
	}
	
//	@GetMapping("/getUserProfileByToken")
//	public MainProfileResponse getUserProfile(@RequestHeader String token) {
//
//		return clickService.getUserProfile(token);
//	}
	
}
