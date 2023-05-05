package com.it.click.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class BasicProfileResponse {
	
	private String name;
	private int distance;
	private int age;
	private String gender;
	private String photo;

}
