package com.it.click.repos;

import java.util.List;
import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import com.it.click.entites.BasicProfile;

@Repository
public interface IBasicProfileRepo extends MongoRepository<BasicProfile, String>{

	List<Optional<BasicProfile>> findByAgeAndGender(int age, String Gender);

	List<Optional<BasicProfile>> findByAgeAndGender();

}
