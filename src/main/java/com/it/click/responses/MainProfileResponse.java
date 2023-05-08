package com.it.click.responses;

import java.util.List;
import com.it.click.common.Photo;
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
	private String interestedGender;
	private List<Photo> photos;
	private String profilePhoto;
	private int maxAgeRange;
	private int maximumDistance;
	private List<String> interests;
	
}
