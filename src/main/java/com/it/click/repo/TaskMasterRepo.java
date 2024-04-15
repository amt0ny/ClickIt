package com.it.click.repo;

import com.it.click.entities.TaskMaster;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskMasterRepo extends JpaRepository<TaskMaster, String> {
        List<TaskMaster> findAllByOwner(String email);
}
