
package com.hb.WRSvhb.dtos;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class ProjectRequestDTO {

    private Long projectId;

    @NotEmpty
    private String projectName;

    @NotEmpty
    private LocalDate startDate;

    @NotEmpty
    private LocalDate expectedEndDate;

    private EmployeeDTO teamLeader;

    private List<EmployeeDTO> employees;


}
