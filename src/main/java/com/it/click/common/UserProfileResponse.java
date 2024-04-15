package com.it.click.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class UserProfileResponse {

    private String name;
    private String email;
    private String contactNo;
    private String designation;
    private String role;
}
