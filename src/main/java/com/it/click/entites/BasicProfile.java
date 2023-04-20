package com.it.click.entites;

import org.springframework.data.mongodb.core.mapping.Document;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document(collection = "BasicProfile")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class BasicProfile {
	
	@Id
	private int id;
	private String name;
	private Double longitude;
	private Double lattitude;
	private int age;
	private String gender;
	private String photo;

}
