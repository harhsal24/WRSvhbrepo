package com.hb.WRSvhb.dtos;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.hb.WRSvhb.enums.ReportStatus;

import jakarta.persistence.PrePersist;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WeeklyReportResponseDTO {
    private EmployeeDTO employee;
    private ProjectDTO project;
    private Long reportId;
    private LocalDateTime reportCreatedDateTime;
    private LocalDate plannedCompletionDate;
    private LocalDate actualCompletionDate;
    private String deliverables;
    private int noOfHours;
    private String activity;
    private String remark;
    private String pointsForDiscussion;
    private String expectedActivitiesOfUpcomingWeek;
    private ReportStatus reportStatus;

}
