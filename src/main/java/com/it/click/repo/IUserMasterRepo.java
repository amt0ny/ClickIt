package com.it.click.repo;

import com.it.click.entities.UserMaster;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IUserMasterRepo extends JpaRepository<UserMaster, String> {
    Optional<UserMaster> findByEmail(String email);
    List<UserMaster> findByManager(String email);
}
