package com.it.click.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
public class TaskMaster {

    @Id
    @GeneratedValue
    private String id;
    private String email;
    private String taskType;
    private String task;
    private String taskDescription;
    private String taskStatus;
    private String priority;
    private String owner;
    private String assignedBy;
}
