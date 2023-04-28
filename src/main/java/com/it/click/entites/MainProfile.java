package com.it.click.entites;

import java.util.List;
import javax.persistence.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import com.it.click.common.Photo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document(collection = "MainProfile")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class MainProfile {

	@Id
	private int id;
	private String name;
	private String email;
	private String password;
	private Double longitude;
	private Double lattitude;
	private int age;
	private String gender;
	private List<Photo> photos;
	private String profilePicture;
	private List<String> hobbies;
	private List<String> interest;

}
