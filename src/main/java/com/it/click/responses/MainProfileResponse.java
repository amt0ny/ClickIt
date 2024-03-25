package com.it.click.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class MainProfileResponse {
	
	private String emailId;
	private String name;
	private int age;
	private String gender;
	private String profilePhoto;
	
}
