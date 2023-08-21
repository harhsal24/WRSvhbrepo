package com.hb.WRSvhb.repository;

import com.hb.WRSvhb.model.WeeklyReport;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WeeklyReportRepository extends JpaRepository<WeeklyReport,Long> {

    List<WeeklyReport> findByEmployeeEmpIdOrderByReportCreatedDateTimeDesc(Long employeeId);

    List<WeeklyReport> findByProjectTeamLeaderEmpIdOrderByReportCreatedDateTimeDesc(Long teamLeaderId);

    List<WeeklyReport> findByEmployeeEmpIdAndProjectProjectIdOrderByReportCreatedDateTimeDesc(Long employeeId, Long projectId);

    List<WeeklyReport> findByProjectTeamLeaderEmpIdAndProjectProjectIdOrderByReportCreatedDateTimeDesc(Long teamLeaderId, Long projectId);

    List<WeeklyReport> findByProjectProjectIdOrderByReportCreatedDateTimeDesc(Long projectId);

    //pagination

    Page<WeeklyReport> findAll(Pageable pageable);


    Page<WeeklyReport> findByProjectProjectIdOrderByReportCreatedDateTimeDesc(Long projectId, Pageable pageable);


    Page<WeeklyReport> findByEmployeeEmpIdOrderByReportCreatedDateTimeDesc(Long employeeId, Pageable pageable);


    Page<WeeklyReport> findByProjectTeamLeaderEmpIdOrderByReportCreatedDateTimeDesc(Long teamLeaderId, Pageable pageable);

    Page<WeeklyReport> findByEmployeeEmpIdAndProjectProjectIdOrderByReportCreatedDateTimeDesc(Long employeeId, Long projectId, Pageable pageable);

    Page<WeeklyReport> findByProjectTeamLeaderEmpIdAndProjectProjectIdOrderByReportCreatedDateTimeDesc(Long teamLeaderId, Long projectId, Pageable pageable);


}
