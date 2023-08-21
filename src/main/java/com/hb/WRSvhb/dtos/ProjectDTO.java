package com.hb.WRSvhb.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProjectDTO {
    private Long projectId;
    private String projectName;
    private EmployeeDTO teamLeader;
}
