package com.it.click.service.impl;

import java.time.LocalDate;
import java.util.*;
import com.it.click.entities.RegisterData;
import com.it.click.entities.UserMaster;
import com.it.click.repo.IRegisterDataRepo;
import com.it.click.repo.IUserMasterRepo;
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
import com.it.click.common.JwtResponse;
import com.it.click.common.LoginData;
import com.it.click.exception.NoValueException;
import com.it.click.responses.MainProfileResponse;
import com.it.click.service.IClickService;
import com.it.click.service.helper.JwtService;

@Service
public class ClickServiceImpl implements IClickService, UserDetailsService {

	@Autowired
	private IRegisterDataRepo registerDataRepo;
	@Autowired
	private IUserMasterRepo userMasterRepo;
	@Autowired
	private AuthenticationManager authenticationManager;
	@Autowired
	private JwtService jwtService;

	@Override
	public String signUp(RegisterData registerData) {
		if(registerDataRepo.existsByEmail(registerData.getEmail())){
			throw new NoValueException("signUp", "Bad Request",
					"Email already registered with us '" + registerData.getEmail() + "'");
		}
		String id = UUID.randomUUID().toString().substring(0,8);
		registerData.setId(id);
		registerData.setPassword(passwordEncoder().encode(registerData.getPassword()));
		registerDataRepo.save(registerData);

		UserMaster userMaster = new UserMaster();
		userMaster.setEmail(registerData.getEmail());
		userMaster.setId(id);
		userMaster.setStatus("ACTIVE");
		userMaster.setJoinedOn(LocalDate.now());
		userMasterRepo.save(userMaster);

		return "User added";
	}

	@Bean
	public PasswordEncoder passwordEncoder() {

		return new BCryptPasswordEncoder();
	}

	@Override
	public JwtResponse login(LoginData loginData) {

		if (!registerDataRepo.existsByEmail(loginData.getEmail())) {
			throw new NoValueException("login", "Bad Request",
					"Email not registered with us, please create an account");
		}

		String pass = registerDataRepo.findByEmail(loginData.getEmail()).get().getPassword();
		String rowPass = loginData.getPassword();

		if (passwordEncoder().matches(rowPass, pass)) {
			JwtResponse token;
			token = generateTokenByEmailAndPassword(loginData.getEmail(), loginData.getPassword());
			return token;
		} else {
			throw new NoValueException("login", "Bad Request", "Password or Username is incorrect");
		}
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		Optional<RegisterData> optional = registerDataRepo.findByEmail(username);

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

	private String verifyToken(String bearer) {

		if (bearer.isEmpty() || bearer == null)
			throw new NoValueException("verifyToken", "Bad Request", "Empty token");

		String[] token = bearer.split(" ");
		Date expirationDate;
		try {
			expirationDate = jwtService.extractExpiration(token[1]);
		} catch (Exception e) {
			throw new NoValueException("verifyToken", "Bad Request", "Invalid token");
		}

		if (expirationDate.before(new Date()))
			throw new NoValueException("verifyToken", "Bad Request", "token expired");

		return token[1];
	}

	@Override
	public UserMaster getUserDashBoardByRole(String bToken) {

		String token = verifyToken(bToken);
		String email = jwtService.extractUsername(token);

		if (userMasterRepo.findByEmail(email).isEmpty()){
			throw new NoValueException("getUserDashBoardByRole", "Bad Request",
					"Unable to fetch user data");
		}
		return  userMasterRepo.findByEmail(email).get();
	}

	@Override
	public MainProfileResponse getUserProfile(String btoken) {
		String token = verifyToken(btoken);
		String email = jwtService.extractUsername(token);

		Optional<RegisterData> mainProfileOpt = registerDataRepo.findByEmail(email);
		if (mainProfileOpt.isEmpty()) {
			throw new NoValueException("getUserProfile", "Bad Request",
					"Seems like you've log out or token is expired");
		}

		RegisterData registerData = mainProfileOpt.get();
		return MainProfileResponse.builder()
				.emailId(registerData.getEmail())
				.name(registerData.getName())
				.build();
	}

	@Override
	public String updateProfile(UserMaster userMasterData, String btoken) {
		String token = verifyToken(btoken);
		String email = jwtService.extractUsername(token);
		UserMaster dbuser;

		if (userMasterRepo.findByEmail(email).isEmpty()){
			throw new NoValueException("updateProfile", "Bad Request",
					"User email does not exists in table 'UserMaster'");
		}else {
			 dbuser = userMasterRepo.findByEmail(email).get();
		}
		UserMaster userMaster = new UserMaster();
		userMaster.setId(dbuser.getId());
		userMaster.setEmail(dbuser.getEmail());
		userMaster.setJoinedOn(dbuser.getJoinedOn());
		userMaster.setName(dbuser.getName());
		userMaster.setStatus(dbuser.getStatus());

		userMaster.setDepartment(userMasterData.getDepartment());
		userMaster.setDob(userMasterData.getDob());
		userMaster.setDesignation(userMasterData.getDesignation());
		userMaster.setFatherName(userMasterData.getFatherName());
		userMaster.setManager(userMasterData.getManager());
		userMaster.setPhoto(userMasterData.getPhoto());
		userMaster.setMobileNumber(userMasterData.getMobileNumber());

		userMasterRepo.save(userMaster);
		return "Details updated";
	}
}
