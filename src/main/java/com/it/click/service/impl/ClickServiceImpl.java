package com.it.click.service.impl;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Optional;
import java.util.Properties;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.it.click.common.EmailRequest;
import com.it.click.common.JwtResponse;
import com.it.click.common.LoginData;
import com.it.click.entites.BasicProfile;
import com.it.click.entites.EmailPass;
import com.it.click.entites.MainProfile;
import com.it.click.entites.SocialProfile;
import com.it.click.exception.NoValueException;
import com.it.click.repos.IBasicProfileRepo;
import com.it.click.repos.IEmailPassRepo;
import com.it.click.repos.IMainProfileRepo;
import com.it.click.repos.ISocialProfileRepo;
import com.it.click.service.IClickService;
import com.it.click.service.helper.JwtService;

@Service
public class ClickServiceImpl implements IClickService, UserDetailsService {

	@Autowired
	private IMainProfileRepo mainProfileRepo;

	@Autowired
	private IBasicProfileRepo basicProfileRepo;

	@Autowired
	private ISocialProfileRepo socialProfileRepo;

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private JwtService jwtService;

	@Autowired
	private IEmailPassRepo emailPassRepo;

	@Override
	public String addUser(MainProfile mainProfile) {

		mainProfile.setPassword(passwordEncoder().encode(mainProfile.getPassword()));

		BasicProfile basicProfile = BasicProfile.builder().id(mainProfile.getId()).name(mainProfile.getName())
				.lattitude(mainProfile.getLattitude()).longitude(mainProfile.getLongitude()).age(mainProfile.getAge())
				.gender(mainProfile.getGender()).photo(mainProfile.getProfilePicture()).build();

		SocialProfile socialProfile = SocialProfile.builder().id(mainProfile.getId()).name(mainProfile.getName())
				.lattitude(mainProfile.getLattitude()).longitude(mainProfile.getLongitude())
				.gender(mainProfile.getGender()).hobbies(mainProfile.getHobbies()).interest(mainProfile.getInterest())
				.photos(mainProfile.getPhotos()).build();

		mainProfileRepo.save(mainProfile);

		socialProfileRepo.save(socialProfile);

		basicProfileRepo.save(basicProfile);

		return "User added";
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Override
	public JwtResponse login(LoginData loginData) {

		if (!emailPassRepo.existsByEmail(loginData.getEmail())) {
			throw new NoValueException("login", "Bad Request",
					"Email not registered with us, please create an account");
		}

		String pass = emailPassRepo.findByEmail(loginData.getEmail()).get().getPassword();
		String rowPass = loginData.getPassword();

		if (passwordEncoder().matches(rowPass, pass)) {

			EmailRequest emailRequest = EmailRequest.builder().message("Logged in successfully")
					.subject("Logged in ClickIt").to(loginData.getEmail()).build();
			JwtResponse token = new JwtResponse();

			token = generateTokenByEmailAndPassword(loginData.getEmail(), loginData.getPassword());

			if (token != null) {
				sendEmail(emailRequest);
			}

			return token;

		} else {
			throw new NoValueException("login", "Bad Request", "Password or Username is incorrect");
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

				return new PasswordAuthentication(username, password);
			}
		});
		try {
			Message message = new MimeMessage(session);

			message.setRecipient(Message.RecipientType.TO, new InternetAddress(emailRequest.getTo()));
			message.setFrom(new InternetAddress("pritamshank@gmail.com"));
			message.setSubject(emailRequest.getSubject());
			message.setText(emailRequest.getMessage());
			Transport.send(message);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String generateOtp(String name, String email) throws NoSuchAlgorithmException {

		if (emailPassRepo.existsByEmail(email)) {
			throw new NoValueException("generateOtp", "Bad Request", "Email already registered with us '"+email+"'");
		}

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
	public JwtResponse otpVarification(LoginData loginData) {
		String otp = null;
		try {
			otp = generateOtp(loginData.getPassword(), loginData.getEmail());
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}

		if (otp != null) {

			if (loginData.getOtp().equals(otp)) {

				EmailRequest emailRequest = EmailRequest.builder().message("Your have Signed-up successfully")
						.subject("Sign up - ClickIt").to(loginData.getEmail()).build();

				EmailPass emailPass = new EmailPass();
				emailPass.setEmail(loginData.getEmail());
				emailPass.setPassword(passwordEncoder().encode(loginData.getPassword()));

				emailPassRepo.save(emailPass);

				JwtResponse token = generateTokenByEmailAndPassword(loginData.getEmail(), loginData.getPassword());

				if (token != null && !token.equals("")) {

					sendEmail(emailRequest);

				} else {
					throw new NoValueException("otpVarification", "Bad Request", "token was null or empty");
				}

				return token;

			} else {
				throw new NoValueException("otpVarification", "Bad Request", "Otp didn't match");
			}

		} else {
			throw new NoValueException("otpVarification", "Bad Request", "Otp cannot be null or empty");
		}
	}

	@Override
	public String generateAndSendEmailOtp(EmailPass emailPass) {

		if (mainProfileRepo.existsByEmail(emailPass.getEmail())) {
			throw new NoValueException("generateAndSendEmailOtp", "Bad Request", "Email already exists with us");
		}

		String otp = null;
		try {

			otp = generateOtp(emailPass.getPassword(), emailPass.getEmail());

		} catch (NoSuchAlgorithmException e) {

			e.printStackTrace();
		}

		if (otp != null) {

			EmailRequest emailRequest = EmailRequest.builder().message("Your one time password is " + otp)
					.subject("OTP varification from ClickIt").to(emailPass.getEmail()).build();

			sendEmail(emailRequest);

			return "Otp sent successfully";

		} else {
			throw new NoValueException("generateAndSendEmailOtp", "Bad Request", "facing trouble while sending otp");
		}
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		Optional<EmailPass> optional = emailPassRepo.findByEmail(username);
		if (optional.isPresent()) {
			return optional.get();
		} else {
			throw new NoValueException("loadUserByUsername", "Bad Request", "User does not exists");
		}
	}

	public JwtResponse generateTokenByEmailAndPassword(String email, String password) {

		try {
			
			this.authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));

		} catch (Exception e) {
			e.printStackTrace();
			throw new NoValueException("loadUserByUsername", "Bad Request", "User does not exists");
		}

		UserDetails userDetails = loadUserByUsername(email);

		String token = jwtService.generateToken(userDetails);

		return new JwtResponse(token);
	}
}
