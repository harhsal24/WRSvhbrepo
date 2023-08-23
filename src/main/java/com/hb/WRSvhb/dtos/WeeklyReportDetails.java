package com.hb.WRSvhb.dtos;

import jakarta.persistence.Embeddable;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Embeddable
public class WeeklyReportDetails {

    @Builder.Default
    private LocalDate plannedCompletionDate = LocalDate.now(); // Default value for LocalDate

    @Builder.Default
    private LocalDate actualCompletionDate = LocalDate.now(); // Default value for LocalDate

    @Builder.Default
    private String deliverables = "Default Deliverables"; // Default value for String

    @Builder.Default
    private int noOfHours = 0; // Default value for int

    @Builder.Default
    private String activity = "Default Activity"; // Default value for String
}
