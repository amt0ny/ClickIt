package com.it.click.service;

import com.it.click.common.EmailVarificationData;
import com.it.click.common.LoginRequest;
import com.it.click.entites.MainProfile;

public interface IClickService {

	String addUser(MainProfile mainProfile);

	boolean login(LoginRequest loginRequest);

	boolean emailVarification(EmailVarificationData emailVarificationData);

	String generateAndSendEmailOtp(EmailVarificationData emailVarificationData);

}
