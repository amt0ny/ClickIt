package com.it.click.entites;

import java.util.List;
import org.springframework.data.mongodb.core.mapping.Document;
import com.it.click.common.Photo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document(collection = "SocialProfile")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class SocialProfile {

	private String userId;
	private String name;
	private Double longitude;
	private Double lattitude;
	private int age;
	private String gender;
	private String description;
	private List<Photo> photos;
	private List<String> hobbies;
	private List<String> interest;
	
}
