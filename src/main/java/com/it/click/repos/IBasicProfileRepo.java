package com.it.click.repos;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import com.it.click.entites.BasicProfile;

@Repository
public interface IBasicProfileRepo extends MongoRepository<BasicProfile, Integer>{

}
