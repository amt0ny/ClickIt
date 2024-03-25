package com.it.click.service;

import com.it.click.common.JwtResponse;
import com.it.click.common.LoginData;
import com.it.click.entities.RegisterData;
import com.it.click.entities.UserMaster;
import com.it.click.responses.MainProfileResponse;


public interface IClickService{

	String signUp(RegisterData mainProfile);

	JwtResponse login(LoginData loginData);

	UserMaster getUserDashBoardByRole(String bToken);

	MainProfileResponse getUserProfile(String token);

	String updateProfile(UserMaster userMasterData, String token);
}
