package com.it.click.service.impl;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Properties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.it.click.common.EmailRequest;
import com.it.click.common.EmailVarificationData;
import com.it.click.common.LoginRequest;
import com.it.click.entites.BasicProfile;
import com.it.click.entites.MainProfile;
import com.it.click.entites.SocialProfile;
import com.it.click.exception.NoValueException;
import com.it.click.repos.IBasicProfileRepo;
import com.it.click.repos.IMainProfileRepo;
import com.it.click.repos.ISocialProfileRepo;
import com.it.click.service.IClickService;
import jakarta.mail.Authenticator;
import jakarta.mail.Message;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

@Service
public class ClickServiceImpl implements IClickService{
	
	@Autowired
	private IMainProfileRepo mainProfileRepo;
	
	@Autowired
	private IBasicProfileRepo basicProfileRepo;
	
	@Autowired
	private ISocialProfileRepo socialProfileRepo;

	@Override
	public String addUser(MainProfile mainProfile) {
		
		mainProfileRepo.save(mainProfile);
		
		BasicProfile basicProfile = BasicProfile.builder()
				.id(mainProfile.getId())
				.name(mainProfile.getName())
				.lattitude(mainProfile.getLattitude())
				.longitude(mainProfile.getLongitude())
				.age(mainProfile.getAge())
				.gender(mainProfile.getGender())
				.photo(mainProfile.getProfilePicture())
				.build();
		
		SocialProfile socialProfile = SocialProfile.builder()
				.id(mainProfile.getId())
				.name(mainProfile.getName())
				.lattitude(mainProfile.getLattitude())
				.longitude(mainProfile.getLongitude())
				.gender(mainProfile.getGender())
				.hobbies(mainProfile.getHobbies())
				.interest(mainProfile.getInterest())
				.photos(mainProfile.getPhotos())
				.build();
		
		socialProfileRepo.save(socialProfile);
		
		basicProfileRepo.save(basicProfile);
		
		return "User added";
	}

	@Override
	public boolean login(LoginRequest loginRequest) {
		
		if (!mainProfileRepo.existsByEmail(loginRequest.getEmail())) {
			throw new NoValueException("login", "Bad Request", "Email not registered with us, please create an account");
		}
		
		if (mainProfileRepo.existsByEmail(loginRequest.getEmail())) {
			if (mainProfileRepo.findByEmail(loginRequest.getEmail()).get().getPassword().equals(loginRequest.getPassowrd())) {
				
				return true;
			}else {
				throw new NoValueException("login", "Bad Request", "Password or Username is incorrect");
			}
			
		}else {
			throw new NoValueException("login", "Bad Request", "Email already registerd with us");
		}
		
	}
	
	public void sendEmail(EmailRequest emailRequest) {
		
		Properties properties = new Properties();
		
		properties.put("mail.smtp.auth", true);
		properties.put("mail.smtp.starttls.enable", true);
		properties.put("mail.smtp.port", "587");
		properties.put("mail.smtp.host", "smtp.gmail.com");
		
		
		String username = "pritamshank";
		String password = "nkajeayubbcdmshd";
		
		Session session = Session.getInstance(properties, new Authenticator() {
			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
				
				return new PasswordAuthentication (username, password);
			}});
		try {
			Message message = new MimeMessage(session);
			
			message.setRecipient(Message.RecipientType.TO, new InternetAddress(emailRequest.getTo()));
			message.setFrom(new InternetAddress("pritamshank@gmail.com"));
			message.setSubject(emailRequest.getSubject());
			message.setText(emailRequest.getMessage());
			
			Transport.send(message);
			
		} catch (Exception e) {
			throw new NoValueException("sendEmail", "Bad Request", "Otp varification faild");
		}
	}
	
	public String generateOtp(String name, String email) throws NoSuchAlgorithmException {

		 String combination = name + email;
        MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
        messageDigest.update(combination.getBytes());
        byte[] digest = messageDigest.digest();

        StringBuilder sb = new StringBuilder();
        for (byte b : digest) {
            sb.append(String.format("%02x", b));
        }

        String hexString = sb.toString();
        long decimalNumber = Long.parseLong(hexString.substring(0, 6), 16);
        long sixDigitOtp = decimalNumber % 1000000;
		return String.valueOf(sixDigitOtp);
	}

	@Override
	public boolean emailVarification(EmailVarificationData emailVarificationData) {
		String otp = null;
		try {
			 otp = generateOtp(emailVarificationData.getName(), emailVarificationData.getTo());
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if (otp != null) {
			if (emailVarificationData.getOtp().equals(otp)) {
				return true;
			}else {
				throw new NoValueException("emailVarification", "Bad Request", "Otp varification faild");
			}
		}else {
			return false;
		}
	}

	@Override
	public String generateAndSendEmailOtp(EmailVarificationData emailVarificationData) {
		
		if (mainProfileRepo.existsByEmail(emailVarificationData.getTo())) {
			throw new NoValueException("generateAndSendEmailOtp", "Bad Request", "Email already exists with us");
		}
		
		String otp = null; 
		try {
			otp = generateOtp(emailVarificationData.getName(), emailVarificationData.getTo());
		} catch (NoSuchAlgorithmException e) {
			
			e.printStackTrace();
		}
		
		if (otp != null) {
			EmailRequest emailRequest= EmailRequest.builder()
					.message("Your one time password is "+otp)
					.subject("OTP varification from ClickIt")
					.to(emailVarificationData.getTo())
					.build();
			
			sendEmail(emailRequest);
			return "Otp sent successfully";
		}else {
			throw new NoValueException("generateAndSendEmailOtp", "Bad Request", "facing trouble while sending otp");
		}
	}
}
