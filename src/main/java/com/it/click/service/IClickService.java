package com.it.click.service;

import java.util.List;
import com.it.click.common.JwtResponse;
import com.it.click.common.LoginData;
import com.it.click.entites.BasicProfile;
import com.it.click.entites.EmailPass;
import com.it.click.entites.MainProfile;
import com.it.click.responses.BasicProfileResponse;


public interface IClickService{

	String signUp(MainProfile mainProfile, String token);

	JwtResponse login(EmailPass emailPass);

	JwtResponse otpVarification(LoginData loginData);

	String generateAndSendEmailOtp(EmailPass emailPass);

	List<BasicProfileResponse> getUserDashBoardByIntereset(String token);

}
