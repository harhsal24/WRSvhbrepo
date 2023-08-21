package com.hb.WRSvhb.model;

import com.hb.WRSvhb.enums.ReportStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class WeeklyReport {

    @ManyToOne
    @JoinColumn(name = "employee_id")
    private Employee employee;

    @ManyToOne
    @JoinColumn(name = "project_id")
    private Project project;



    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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

    @Enumerated(EnumType.STRING)
    private ReportStatus reportStatus;

    @PrePersist
    protected void onCreate() {
        reportCreatedDateTime = LocalDateTime.now();
    }
}
