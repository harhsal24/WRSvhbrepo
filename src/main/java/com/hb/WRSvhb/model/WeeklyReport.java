package com.hb.WRSvhb.model;

import com.hb.WRSvhb.dtos.WeeklyReportDetails;
import com.hb.WRSvhb.enums.ReportStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class WeeklyReport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reportId;


    @ManyToOne
    @JoinColumn(name = "employee_id")
    private Employee employee;


    @ManyToOne
    @JoinColumn(name = "project_id")
    private Project project;

    private LocalDateTime reportCreatedDateTime;

    @Column(nullable = false)
    @ElementCollection
    private List<WeeklyReportDetails> reportDetailsList;

    private String remark;

    private String pointsForDiscussion;

    private String expectedActivitiesOfUpcomingWeek;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ReportStatus reportStatus;

    @PrePersist
    protected void onCreate() {
        reportCreatedDateTime = LocalDateTime.now();
    }
}
