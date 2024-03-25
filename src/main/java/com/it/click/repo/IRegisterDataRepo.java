package com.it.click.repo;

import com.it.click.entities.RegisterData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface IRegisterDataRepo extends JpaRepository<RegisterData, String> {
    Optional<RegisterData> findByEmail(String email);
    boolean existsByEmail(String email);
}
