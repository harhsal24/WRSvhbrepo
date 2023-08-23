package com.hb.WRSvhb.dtos;

import java.util.List;

import com.hb.WRSvhb.enums.Gender;
import com.hb.WRSvhb.enums.Role;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmployeeResponseDTO {

    private EmployeeDTO manager;
    private List<ProjectDTO> ledProjects;
    private List<ProjectDTO> projects;
    private List<WeeklyReportRequestResponseDTO> weeklyReports;
    private Long empId;
    private String name;
    private Role role;
    private String email;
    private Gender gender;


}
