package com.hb.WRSvhb.dtos;

import com.hb.WRSvhb.enums.Gender;
import com.hb.WRSvhb.enums.Role;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmployeeRequestDTO {
    private String   firstName;
    private String lastName;
    private String  login;
    private char[]  password;
    private Role role;
    private Gender gender;
    private long managerId;

}
