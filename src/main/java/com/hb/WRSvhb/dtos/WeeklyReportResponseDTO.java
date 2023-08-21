package com.hb.WRSvhb.dtos;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import com.hb.WRSvhb.enums.ReportStatus;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WeeklyReportResponseDTO {
    private EmployeeDTO employee;
    private ProjectDTO project;
    private Long reportId;
    private LocalDateTime reportCreatedDateTime;
    private List<WeeklyReportDetails> reportDetailsList;
    private String remark;
    private String pointsForDiscussion;
    private String expectedActivitiesOfUpcomingWeek;
    private ReportStatus reportStatus;

}
