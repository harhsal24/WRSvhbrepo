package com.hb.WRSvhb.config.authdtos;

import com.hb.WRSvhb.enums.Gender;
import com.hb.WRSvhb.enums.Role;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class SignUpDto {

    @NotEmpty
    private String firstName;

    @NotEmpty
    private String lastName;

    @NotEmpty
    private String login;

    @NotEmpty
    private char[] password;


    private Role role;

    private Gender gender;

    private Long managerId;

}
