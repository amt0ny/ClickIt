package com.it.click.controller;

import java.time.LocalDate;
import java.util.List;

import com.it.click.common.LoginData;
import com.it.click.common.UserProfileResponse;
import com.it.click.entities.RegisterData;
import com.it.click.entities.TaskMaster;
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
	
	@GetMapping("/getUserProfileByEmail")
	public UserProfileResponse getUserProfile(@RequestHeader String token, @RequestHeader String email) {
		return clickService.getUserProfile(token, email);
	}

	@PostMapping("/addSelfTask")
	public String addSelfTask(@RequestHeader String token, @RequestBody TaskMaster taskData) {
		return clickService.addSelfTask(token, taskData);
	}

	@PostMapping("/addTaskToKJunior")
	public String addTaskToJunior(@RequestHeader String token, @RequestBody TaskMaster taskData) {
		return clickService.assignTaskToJunior(token, taskData);
	}

	@GetMapping("/getTeamByToken")
	public List<UserProfileResponse> getTeamByToken(@RequestHeader String token) {
		return clickService.getTeamListByToken(token);
	}

	@GetMapping("/getDeveloperProfile")
	public UserMaster getDeveloperProfile(@RequestHeader String token, @RequestHeader String developerEmail) {
		return clickService.getDeveloperProfile(token, developerEmail);
	}

	@GetMapping("/getSelfTasksByToken")
	public List<TaskMaster> getSelfTasksByToken(@RequestHeader String token) {
		return clickService.getSelfTasksByToken(token);
	}
}
