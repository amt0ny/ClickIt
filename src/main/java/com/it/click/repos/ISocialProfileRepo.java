package com.it.click.repos;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import com.it.click.entites.SocialProfile;

@Repository
public interface ISocialProfileRepo extends MongoRepository<SocialProfile, Integer>{

}
