package com.hb.WRSvhb.dtos;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Embeddable
public class WeeklyReportDetails {

    private LocalDate plannedCompletionDate;
    private LocalDate actualCompletionDate;
    private String deliverables;
    private int noOfHours;
    private String activity;

}
