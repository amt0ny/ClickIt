package com.it.click.repos;

import java.lang.StackWalker.Option;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import com.it.click.entites.MainProfile;

@Repository
public interface IMainProfileRepo extends MongoRepository<MainProfile, Integer>{

	boolean existsByEmail(String email);

	Optional<MainProfile> findByEmail(String email);

}
