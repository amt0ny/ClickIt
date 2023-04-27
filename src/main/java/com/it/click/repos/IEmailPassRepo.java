package com.it.click.repos;

import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import com.it.click.entites.EmailPass;

@Repository
public interface IEmailPassRepo extends MongoRepository<EmailPass, String>{

	boolean existsByEmail(String email);
	
	Optional<EmailPass> findByEmail(String email);

}
