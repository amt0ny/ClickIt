package com.it.click.service;

import com.it.click.common.JwtResponse;
import com.it.click.common.LoginData;
import com.it.click.entites.EmailPass;
import com.it.click.entites.MainProfile;


public interface IClickService{

	String addUser(MainProfile mainProfile);

	JwtResponse login(LoginData loginRequest);

	JwtResponse otpVarification(LoginData loginData);

	String generateAndSendEmailOtp(EmailPass emailPass);

}
