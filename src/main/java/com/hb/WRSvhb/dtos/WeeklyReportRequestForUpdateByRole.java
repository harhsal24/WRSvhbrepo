package com.hb.WRSvhb.dtos;

import com.hb.WRSvhb.enums.ReportStatus;
import com.hb.WRSvhb.enums.Role;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class WeeklyReportRequestForUpdateByRole extends WeeklyReportRequestResponseDTO {

    private Role role;

}
