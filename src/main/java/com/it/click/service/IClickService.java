package com.it.click.service;

import com.it.click.common.JwtResponse;
import com.it.click.common.LoginData;
import com.it.click.common.UserProfileResponse;
import com.it.click.entities.RegisterData;
import com.it.click.entities.TaskMaster;
import com.it.click.entities.UserMaster;
import com.it.click.responses.MainProfileResponse;

import java.util.List;


public interface IClickService{

	String signUp(RegisterData mainProfile);

	JwtResponse login(LoginData loginData);

	UserMaster getUserDashBoardByRole(String bToken);

	UserProfileResponse getUserProfile(String token, String email);

	String updateProfile(UserMaster userMasterData, String token);

	String addSelfTask(String token, TaskMaster taskData);

	String assignTaskToJunior(String token, TaskMaster taskData);

	List<UserProfileResponse> getTeamListByToken(String token);

	UserMaster getDeveloperProfile(String token, String developerEmail);

	List<TaskMaster> getSelfTasksByToken(String token);
}
