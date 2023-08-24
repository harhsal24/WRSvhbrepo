package com.hb.WRSvhb.service;

import com.hb.WRSvhb.dtos.WeeklyReportRequestForUpdateByRole;
import com.hb.WRSvhb.dtos.WeeklyReportRequestResponseDTO;
import com.hb.WRSvhb.enums.Role;
import com.hb.WRSvhb.model.Employee;
import com.hb.WRSvhb.model.Project;
import com.hb.WRSvhb.model.WeeklyReport;
import com.hb.WRSvhb.repository.EmployeeRepository;
import com.hb.WRSvhb.repository.WeeklyReportRepository;
import com.hb.WRSvhb.dtos.EmployeeDTO;
import com.hb.WRSvhb.dtos.ProjectDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class WeeklyReportService {

    private final WeeklyReportRepository weeklyReportRepository;
    private final EmployeeRepository employeeRepository;

    @Autowired
    public WeeklyReportService(WeeklyReportRepository weeklyReportRepository, EmployeeRepository employeeRepository) {
        this.weeklyReportRepository = weeklyReportRepository;
        this.employeeRepository = employeeRepository;
    }

    public List<WeeklyReportRequestResponseDTO> getAllReports() {
        List<WeeklyReport> reports = weeklyReportRepository.findAll();
        return reports.stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }


    public Optional<WeeklyReportRequestResponseDTO> getReportById(Long reportId) {
        Optional<WeeklyReport> report = weeklyReportRepository.findById(reportId);
        return report.map(this::convertToResponseDTO);
    }

    public List<WeeklyReportRequestResponseDTO> getReportsByEmployeeId(Long employeeId) {
        List<WeeklyReport> reports = weeklyReportRepository
                .findByEmployeeEmpIdOrderByReportCreatedDateTimeDesc(employeeId);
        return reports.stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    public List<WeeklyReportRequestResponseDTO> getReportsByTeamLeaderId(Long teamLeaderId) {
        List<WeeklyReport> reports = weeklyReportRepository
                .findByProjectTeamLeaderEmpIdOrderByReportCreatedDateTimeDesc(teamLeaderId);
        return reports.stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    public List<WeeklyReportRequestResponseDTO> getReportsByEmployeeAndProject(Long employeeId, Long projectId) {
        List<WeeklyReport> reports = weeklyReportRepository
                .findByEmployeeEmpIdAndProjectProjectIdOrderByReportCreatedDateTimeDesc(employeeId, projectId);
        return reports.stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    public List<WeeklyReportRequestResponseDTO> getReportsByTeamLeaderAndProject(Long teamLeaderId, Long projectId) {
        List<WeeklyReport> reports = weeklyReportRepository
                .findByProjectTeamLeaderEmpIdAndProjectProjectIdOrderByReportCreatedDateTimeDesc(teamLeaderId,
                        projectId);
        return reports.stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    public List<WeeklyReportRequestResponseDTO> getReportsByProjectId(Long projectId) {
        List<WeeklyReport> reports = weeklyReportRepository
                .findByProjectProjectIdOrderByReportCreatedDateTimeDesc(projectId);
        return reports.stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    public WeeklyReportRequestResponseDTO createReport(WeeklyReportRequestResponseDTO reportDTO) {
        WeeklyReport report = convertToEntity(reportDTO);
        report = weeklyReportRepository.save(report);
        return convertToResponseDTO(report);
    }

//    public Optional<WeeklyReportRequestResponseDTO> updateReport(Long reportId, WeeklyReportRequestResponseDTO updatedReportDTO,Long employeeId ) {
//        Optional<WeeklyReport> existingReport = weeklyReportRepository.findById(reportId);
//
//           Optional<Employee> employee =employeeRepository.findByEmpId(employeeId);
//
//           if (employee.isPresent())
//             {
//            Employee emp = employee.get();
//
//        if (existingReport.isPresent()  ) {
//            WeeklyReport reportToUpdate = existingReport.get();
//
//            if ((updatedReportDTO.getReportDetailsList() != null && emp.getRole()== Role.REGULAR_EMPLOYEE)) {
//                reportToUpdate.setReportDetailsList(updatedReportDTO.getReportDetailsList());
//            }
//
//            if (emp.getRole()==Role.TEAM_LEADER) {
//                reportToUpdate.setRemark(updatedReportDTO.getRemark());
//                reportToUpdate.setPointsForDiscussion(updatedReportDTO.getPointsForDiscussion());
//                reportToUpdate.setExpectedActivitiesOfUpcomingWeek(updatedReportDTO.getExpectedActivitiesOfUpcomingWeek());
//                reportToUpdate.setReportStatus(updatedReportDTO.getReportStatus());
//            }
//            reportToUpdate = weeklyReportRepository.save(reportToUpdate);
//            return Optional.of(convertToResponseDTO(reportToUpdate));
//        }
//        }
//        return Optional.empty(); // Report not found
//    }
    public Optional<WeeklyReportRequestResponseDTO> updateReport(Long reportId, WeeklyReportRequestForUpdateByRole updatedReport) {
        Optional<WeeklyReport> existingReport = weeklyReportRepository.findById(reportId);
    {
        if (existingReport.isPresent()  ) {
            WeeklyReport reportToUpdate = existingReport.get();

            if ((updatedReport.getReportDetailsList() != null && updatedReport.getRole()== Role.REGULAR_EMPLOYEE)) {
                reportToUpdate.setReportDetailsList(updatedReport.getReportDetailsList());
            }

            if (updatedReport.getRole()==Role.TEAM_LEADER) {
                reportToUpdate.setRemark(updatedReport.getRemark());
                reportToUpdate.setPointsForDiscussion(updatedReport.getPointsForDiscussion());
                reportToUpdate.setExpectedActivitiesOfUpcomingWeek(updatedReport.getExpectedActivitiesOfUpcomingWeek());
                reportToUpdate.setReportStatus(updatedReport.getReportStatus());
            }

            reportToUpdate = weeklyReportRepository.save(reportToUpdate);
            return Optional.of(convertToResponseDTO(reportToUpdate));
        }
        }

        return Optional.empty(); // Report not found
    }



    public boolean deleteReport(Long reportId) {
        Optional<WeeklyReport> reportToDelete = weeklyReportRepository.findById(reportId);
        if (reportToDelete.isPresent()) {
            weeklyReportRepository.delete(reportToDelete.get());
            return true;
        }
        return false;
    }

//    pagination logic start

    public Page<WeeklyReportRequestResponseDTO> getAllReportsWithPagination(Pageable pageable) {
        Page<WeeklyReport> reports = weeklyReportRepository.findAll(pageable);
        return reports.map(this::convertToResponseDTO);
    }

    public Page<WeeklyReportRequestResponseDTO> getReportsByProjectIdWithPagination(Long projectId, Pageable pageable) {
        Page<WeeklyReport> reports = weeklyReportRepository.findByProjectProjectIdOrderByReportCreatedDateTimeDesc(projectId, pageable);
        return reports.map(this::convertToResponseDTO);

    }

    public Page<WeeklyReportRequestResponseDTO> getReportsByEmployeeIdWithPagination(Long employeeId, Pageable pageable) {
        Page<WeeklyReport> reports = weeklyReportRepository.findByEmployeeEmpIdOrderByReportCreatedDateTimeDesc(employeeId, pageable);
        return reports.map(this::convertToResponseDTO);
    }

    public Page<WeeklyReportRequestResponseDTO> getReportsByTeamLeaderIdWithPagination(Long teamLeaderId, Pageable pageable) {
        Page<WeeklyReport> reports = weeklyReportRepository.findByProjectTeamLeaderEmpIdOrderByReportCreatedDateTimeDesc(teamLeaderId, pageable);
        return reports.map(this::convertToResponseDTO);
    }

    public Page<WeeklyReportRequestResponseDTO> getReportsByEmployeeAndProjectWithPagination(Long employeeId, Long projectId, Pageable pageable) {
        Page<WeeklyReport> reports = weeklyReportRepository.findByEmployeeEmpIdAndProjectProjectIdOrderByReportCreatedDateTimeDesc(employeeId, projectId, pageable);
        return reports.map(this::convertToResponseDTO);
    }

    public Page<WeeklyReportRequestResponseDTO> getReportsByTeamLeaderAndProjectWithPagination(Long teamLeaderId, Long projectId, Pageable pageable) {
        Page<WeeklyReport> reports = weeklyReportRepository.findByProjectTeamLeaderEmpIdAndProjectProjectIdOrderByReportCreatedDateTimeDesc(teamLeaderId, projectId, pageable);
        return reports.map(this::convertToResponseDTO);
    }

//    paginated methods end here

    // Helper methods to convert between entities and DTOs
        private WeeklyReportRequestResponseDTO convertToResponseDTO (WeeklyReport report){
            WeeklyReportRequestResponseDTO reportResponseDTO = new WeeklyReportRequestResponseDTO();
            reportResponseDTO.setEmployee(convertToEmployeeDTO(report.getEmployee()));
            reportResponseDTO.setProject(convertToProjectDTO(report.getProject()));
            reportResponseDTO.setReportId(report.getReportId());
            reportResponseDTO.setReportCreatedDateTime(report.getReportCreatedDateTime());
            reportResponseDTO.setReportDetailsList(report.getReportDetailsList());
            reportResponseDTO.setRemark(report.getRemark());
            reportResponseDTO.setPointsForDiscussion(report.getPointsForDiscussion());
            reportResponseDTO.setExpectedActivitiesOfUpcomingWeek(report.getExpectedActivitiesOfUpcomingWeek());
            reportResponseDTO.setReportStatus(report.getReportStatus());
            return reportResponseDTO;
        }

        private WeeklyReport convertToEntity (WeeklyReportRequestResponseDTO reportDTO){
            WeeklyReport report = new WeeklyReport();
            report.setEmployee(convertToEmployeeEntity(reportDTO.getEmployee()));
            report.setProject(convertToProjectEntity(reportDTO.getProject()));
            report.setReportId(reportDTO.getReportId());
            report.setReportCreatedDateTime(reportDTO.getReportCreatedDateTime());
            report.setReportDetailsList(reportDTO.getReportDetailsList());
            report.setRemark(reportDTO.getRemark());
            report.setPointsForDiscussion(reportDTO.getPointsForDiscussion());
            report.setExpectedActivitiesOfUpcomingWeek(reportDTO.getExpectedActivitiesOfUpcomingWeek());
            report.setReportStatus(reportDTO.getReportStatus());
            return report;
        }

        private EmployeeDTO convertToEmployeeDTO (Employee employee){
            EmployeeDTO employeeDTO = new EmployeeDTO();
            employeeDTO.setEmployeeId(employee.getEmpId());
            employeeDTO.setEmployeeName(employee.getName());
            // Set other fields
            return employeeDTO;
        }

        private ProjectDTO convertToProjectDTO (Project project){
            ProjectDTO projectDTO = new ProjectDTO();
            projectDTO.setProjectId(project.getProjectId());
            projectDTO.setProjectName(project.getProjectName());

            EmployeeDTO teamLeaderDTO = new EmployeeDTO();
            teamLeaderDTO.setEmployeeId(project.getTeamLeader().getEmpId());
            teamLeaderDTO.setEmployeeName(project.getTeamLeader().getName());

            projectDTO.setTeamLeader(teamLeaderDTO);
            // Set other fields of the teamLeaderDTO if needed

            return projectDTO;

        }

        private Employee convertToEmployeeEntity (EmployeeDTO employeeDTO){
            Employee employee = new Employee();
            employee.setEmpId(employeeDTO.getEmployeeId());
            employee.setName(employeeDTO.getEmployeeName());
            // Set other fields
            return employee;
        }
        private Project convertToProjectEntity (ProjectDTO projectDTO){
            Project project = new Project();
            project.setProjectId(projectDTO.getProjectId());
            project.setProjectName(projectDTO.getProjectName());
            if (projectDTO.getTeamLeader() != null) {
                Employee teamLeaderEntity = convertToEmployeeEntity(projectDTO.getTeamLeader());
                project.setTeamLeader(teamLeaderEntity);
            }
            return project;
        }


    }
