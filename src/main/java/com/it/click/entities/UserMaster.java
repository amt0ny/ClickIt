package com.it.click.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.LocalDate;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
public class UserMaster {

    @Id
    private String id;
    private String name;
    private String email;
    private String mobileNumber;
    private LocalDate dob;
    private String fatherName;
    private String photo;
    private String status;
    private LocalDate joinedOn;
    private String designation;
    private String manager;
    private String department;

}
