
package com.hb.WRSvhb.dtos;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class ProjectRequestDTO {
    private EmployeeDTO teamLeader;
    private List<EmployeeDTO> employees;
    private Long projectId;
    private String projectName;
    private LocalDate startDate;
    private LocalDate expectedEndDate;
}
