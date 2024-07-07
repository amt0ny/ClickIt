package com.it.click.service.impl;

import java.time.LocalDate;
import java.util.*;
import com.it.click.common.UserProfileResponse;
import com.it.click.entities.RegisterData;
import com.it.click.entities.TaskMaster;
import com.it.click.entities.UserMaster;
import com.it.click.repo.IRegisterDataRepo;
import com.it.click.repo.IUserMasterRepo;
import com.it.click.repo.TaskMasterRepo;
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
	@Autowired
	private TaskMasterRepo taskMasterRepo;

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
		userMaster.setName(registerData.getName());
		userMaster.setDesignation(registerData.getUserType());
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
	public UserProfileResponse getUserProfile(String btoken, String employeeEmail) {
		String token = verifyToken(btoken);
		String email = jwtService.extractUsername(token);
		Optional<UserMaster> userMaster = userMasterRepo.findByEmail(email);
		if (userMaster.isEmpty()) {
			throw new NoValueException("getUserProfile", "Bad Request",
					"Seems like you've logged out or token is expired");
		}
		UserProfileResponse userProfileResponse = new UserProfileResponse();
		if (!email.equals(employeeEmail)){
			UserMaster employeeData =  userMasterRepo.findByEmail(employeeEmail).get();
			userProfileResponse.setRole(employeeData.getRole());
			userProfileResponse.setName(employeeData.getName());
			userProfileResponse.setContactNo(employeeData.getMobileNumber());
			userProfileResponse.setDesignation(employeeData.getDesignation());
			userProfileResponse.setEmail(employeeData.getEmail());

		}
		return userProfileResponse;
	}

	@Override
	public UserMaster updateOwnProfile(UserMaster userMasterData, String btoken) {
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
		userMaster.setEmail(userMasterData.getEmail());
		userMaster.setName(userMasterData.getName());
		userMaster.setDob(userMasterData.getDob());
		userMaster.setFatherName(userMasterData.getFatherName());
		userMaster.setPhoto(userMasterData.getPhoto());
		userMaster.setMobileNumber(userMasterData.getMobileNumber());
		userMasterRepo.save(userMaster);
		return userMasterRepo.findByEmail(email).get();
	}

	@Override
	public String addSelfTask(String token, TaskMaster taskMasterData) {
		String tokenString = verifyToken(token);
		String email = jwtService.extractUsername(tokenString);
		TaskMaster taskMaster =  new TaskMaster();
		taskMaster.setTaskStatus("ACTIVE");
		taskMaster.setOwner(email);
		taskMaster.setTaskDescription(taskMasterData.getTaskDescription());
		taskMaster.setTaskType(taskMasterData.getTaskType());
		taskMaster.setTask(taskMasterData.getTask());
		taskMaster.setPriority(taskMasterData.getPriority());
		taskMaster.setAssignedBy("Self");
		taskMasterRepo.save(taskMaster);
		return "Self Task Added";
	}

	@Override
	public String assignTaskToJunior(String token, TaskMaster taskMasterData) {
		String tokenString = verifyToken(token);
		String email = jwtService.extractUsername(tokenString);
		TaskMaster taskMaster =  new TaskMaster();
		taskMaster.setTaskStatus("ACTIVE");
		taskMaster.setOwner(taskMasterData.getOwner());
		taskMaster.setTaskDescription(taskMasterData.getTaskDescription());
		taskMaster.setTaskType(taskMasterData.getTaskType());
		taskMaster.setTask(taskMasterData.getTask());
		taskMaster.setPriority(taskMasterData.getPriority());
		taskMaster.setAssignedBy(email);

		return "Task Assigned to User : "+ taskMasterData.getOwner();
	}

	@Override
	public List<UserProfileResponse> getTeamListByToken(String token) {
		String tokenString = verifyToken(token);
		String email = jwtService.extractUsername(tokenString);
		List<UserMaster> userMasterList = userMasterRepo.findByManager(email);
		List<UserProfileResponse> userProfileResponseList = new ArrayList<>();
		for (UserMaster userMasterDate: userMasterList) {
			UserProfileResponse userProfileResponse = new UserProfileResponse();
			userProfileResponse.setEmail(userMasterDate.getEmail());
			userProfileResponse.setRole(userMasterDate.getRole());
			userProfileResponse.setName(userMasterDate.getName());
			userProfileResponse.setContactNo(userMasterDate.getMobileNumber());
			userProfileResponse.setDesignation(userMasterDate.getDesignation());
			userProfileResponseList.add(userProfileResponse);
		}
		return  userProfileResponseList;
	}

	@Override
	public UserMaster getDeveloperProfile(String token, String developerEmail) {
		verifyToken(token);
		if (userMasterRepo.findByEmail(developerEmail).isPresent()){
			return userMasterRepo.findByEmail(developerEmail).get();
		}else {
			throw new NoValueException("getDeveloperProfile", "Bad Request",
					"No user with email '" + developerEmail + "'");
		}
	}

	@Override
	public List<TaskMaster> getSelfTasksByToken(String token) {
		String tokenString = verifyToken(token);
		String email = jwtService.extractUsername(tokenString);
        return taskMasterRepo.findAllByOwner(email);
	}

	@Override
	public String updateTask(String token, TaskMaster taskData) {
		if (taskMasterRepo.findById(taskData.getId()).isEmpty()){
			throw new NoValueException("updateTask", "Bad Request",
					"There is no such task for id : '"+taskData.getId()+"'");
		}
		TaskMaster taskMaster = taskMasterRepo.findById(taskData.getId()).get();
		taskMaster.setId(taskData.getId());
		taskMaster.setTaskDescription(taskData.getTaskDescription());
		taskMaster.setTaskType(taskData.getTaskType());
		taskMaster.setTaskStatus(taskData.getTaskStatus());
		taskMaster.setOwner(taskData.getOwner());
		taskMaster.setAssignedBy(taskData.getAssignedBy());
		taskMaster.setPriority(taskData.getPriority());

		return "Task Updated";
	}

	@Override
	public String updateDeveloperTask(String token, TaskMaster taskData, String developerEmail) {
		if (taskMasterRepo.findById(taskData.getId()).isEmpty()){
			throw new NoValueException("updateDeveloperTask", "Bad Request",
					"There is no such user with with taskId : '"+taskData.getId()+"'");
		}

		TaskMaster taskMaster = taskMasterRepo.findByIdAndEmail(taskData.getId(), developerEmail);
		taskMaster.setId(taskMaster.getId());
		taskMaster.setTaskDescription(taskData.getTaskDescription());
		taskMaster.setTaskType(taskData.getTaskType());
		taskMaster.setTaskStatus(taskData.getTaskStatus());
		taskMaster.setOwner(taskData.getOwner());
		taskMaster.setAssignedBy(taskData.getAssignedBy());
		taskMaster.setPriority(taskData.getPriority());

		return "Task Updated";
	}
}
