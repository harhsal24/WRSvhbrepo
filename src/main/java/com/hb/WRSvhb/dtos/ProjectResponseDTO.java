package com.hb.WRSvhb.dtos;

import java.time.LocalDate;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProjectResponseDTO {

    private Long projectId;
    private String projectName;
    private LocalDate startDate;
    private LocalDate expectedEndDate;

    private EmployeeDTO teamLeader;
    private List<EmployeeDTO> employees;
    private List<WeeklyReportRequestResponseDTO> weeklyReports;


}
