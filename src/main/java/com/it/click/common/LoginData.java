package com.it.click.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class LoginData {

	private String email;
	private String password;
	private String otp;
	
}
