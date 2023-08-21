package com.hb.WRSvhb.service;

import com.hb.WRSvhb.model.Employee;
import com.hb.WRSvhb.model.Project;
import com.hb.WRSvhb.model.WeeklyReport;
import com.hb.WRSvhb.repository.EmployeeRepository;
import com.hb.WRSvhb.repository.WeeklyReportRepository;
import com.hb.WRSvhb.dtos.EmployeeDTO;
import com.hb.WRSvhb.dtos.ProjectDTO;
import com.hb.WRSvhb.dtos.WeeklyReportResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class WeeklyReportService {

    private final WeeklyReportRepository weeklyReportRepository;
    private final EmployeeRepository employeeRepository;
    @Autowired
    public WeeklyReportService(WeeklyReportRepository weeklyReportRepository,EmployeeRepository employeeRepository) {
        this.weeklyReportRepository = weeklyReportRepository;
        this.employeeRepository=employeeRepository;
    }

    public List<WeeklyReportResponseDTO> getAllReports() {
        List<WeeklyReport> reports = weeklyReportRepository.findAll();
        return reports.stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }


    public Optional<WeeklyReportResponseDTO> getReportById(Long reportId) {
        Optional<WeeklyReport> report = weeklyReportRepository.findById(reportId);
        return report.map(this::convertToResponseDTO);
    }

    public List<WeeklyReportResponseDTO> getReportsByEmployeeId(Long employeeId) {
        List<WeeklyReport> reports = weeklyReportRepository
                .findByEmployeeEmpIdOrderByReportCreatedDateTimeDesc(employeeId);
        return reports.stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    public List<WeeklyReportResponseDTO> getReportsByTeamLeaderId(Long teamLeaderId) {
        List<WeeklyReport> reports = weeklyReportRepository
                .findByProjectTeamLeaderEmpIdOrderByReportCreatedDateTimeDesc(teamLeaderId);
        return reports.stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    public List<WeeklyReportResponseDTO> getReportsByEmployeeAndProject(Long employeeId, Long projectId) {
        List<WeeklyReport> reports = weeklyReportRepository
                .findByEmployeeEmpIdAndProjectProjectIdOrderByReportCreatedDateTimeDesc(employeeId, projectId);
        return reports.stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    public List<WeeklyReportResponseDTO> getReportsByTeamLeaderAndProject(Long teamLeaderId, Long projectId) {
        List<WeeklyReport> reports = weeklyReportRepository
                .findByProjectTeamLeaderEmpIdAndProjectProjectIdOrderByReportCreatedDateTimeDesc(teamLeaderId,
                        projectId);
        return reports.stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    public List<WeeklyReportResponseDTO> getReportsByProjectId(Long projectId) {
        List<WeeklyReport> reports = weeklyReportRepository
                .findByProjectProjectIdOrderByReportCreatedDateTimeDesc(projectId);
        return reports.stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    public WeeklyReportResponseDTO createReport(WeeklyReportResponseDTO reportDTO) {
        WeeklyReport report = convertToEntity(reportDTO);
        report = weeklyReportRepository.save(report);
        return convertToResponseDTO(report);
    }

    public Optional<WeeklyReportResponseDTO> updateReport(Long reportId, WeeklyReportResponseDTO updatedReportDTO) {
        Optional<WeeklyReport> existingReport = weeklyReportRepository.findById(reportId);
        if (existingReport.isPresent()) {
            WeeklyReport reportToUpdate = existingReport.get();

            reportToUpdate.setPlannedCompletionDate(updatedReportDTO.getPlannedCompletionDate());
            reportToUpdate.setActualCompletionDate(updatedReportDTO.getActualCompletionDate());
            reportToUpdate.setDeliverables(updatedReportDTO.getDeliverables());
            reportToUpdate.setNoOfHours(updatedReportDTO.getNoOfHours());
            reportToUpdate.setActivity(updatedReportDTO.getActivity());
            reportToUpdate.setRemark(updatedReportDTO.getRemark());
            reportToUpdate.setPointsForDiscussion(updatedReportDTO.getPointsForDiscussion());
            reportToUpdate.setExpectedActivitiesOfUpcomingWeek(updatedReportDTO.getExpectedActivitiesOfUpcomingWeek());
            reportToUpdate.setReportStatus(updatedReportDTO.getReportStatus());

            reportToUpdate = weeklyReportRepository.save(reportToUpdate);
            return Optional.of(convertToResponseDTO(reportToUpdate));
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

    // Helper methods to convert between entities and DTOs
    private WeeklyReportResponseDTO convertToResponseDTO(WeeklyReport report) {
        WeeklyReportResponseDTO reportResponseDTO = new WeeklyReportResponseDTO();
        reportResponseDTO.setEmployee(convertToEmployeeDTO(report.getEmployee()));
        reportResponseDTO.setProject(convertToProjectDTO(report.getProject()));
        reportResponseDTO.setReportId(report.getReportId());
        reportResponseDTO.setReportCreatedDateTime(report.getReportCreatedDateTime());
        reportResponseDTO.setPlannedCompletionDate(report.getPlannedCompletionDate());
        reportResponseDTO.setActualCompletionDate(report.getActualCompletionDate());
        reportResponseDTO.setDeliverables(report.getDeliverables());
        reportResponseDTO.setNoOfHours(report.getNoOfHours());
        reportResponseDTO.setActivity(report.getActivity());
        reportResponseDTO.setRemark(report.getRemark());
        reportResponseDTO.setPointsForDiscussion(report.getPointsForDiscussion());
        reportResponseDTO.setExpectedActivitiesOfUpcomingWeek(report.getExpectedActivitiesOfUpcomingWeek());
        reportResponseDTO.setReportStatus(report.getReportStatus());
        return reportResponseDTO;
    }

    private WeeklyReport convertToEntity(WeeklyReportResponseDTO reportDTO) {
        WeeklyReport report = new WeeklyReport();
        report.setEmployee(convertToEmployeeEntity(reportDTO.getEmployee()));
        report.setProject(convertToProjectEntity(reportDTO.getProject()));
        report.setReportId(reportDTO.getReportId());
        report.setReportCreatedDateTime(reportDTO.getReportCreatedDateTime());
        report.setPlannedCompletionDate(reportDTO.getPlannedCompletionDate());
        report.setActualCompletionDate(reportDTO.getActualCompletionDate());
        report.setDeliverables(reportDTO.getDeliverables());
        report.setNoOfHours(reportDTO.getNoOfHours());
        report.setActivity(reportDTO.getActivity());
        report.setRemark(reportDTO.getRemark());
        report.setPointsForDiscussion(reportDTO.getPointsForDiscussion());
        report.setExpectedActivitiesOfUpcomingWeek(reportDTO.getExpectedActivitiesOfUpcomingWeek());
        report.setReportStatus(reportDTO.getReportStatus());
        return report;
    }

    private EmployeeDTO convertToEmployeeDTO(Employee employee) {
        EmployeeDTO employeeDTO = new EmployeeDTO();
        employeeDTO.setEmployeeId(employee.getEmpId());
        employeeDTO.setEmployeeName(employee.getName());
        // Set other fields
        return employeeDTO;
    }

 private ProjectDTO convertToProjectDTO(Project project) {
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

private Employee convertToEmployeeEntity(EmployeeDTO employeeDTO) {
        Employee employee = new Employee();
        employee.setEmpId(employeeDTO.getEmployeeId());
        employee.setName(employeeDTO.getEmployeeName());
        // Set other fields
        return employee;
}
private Project convertToProjectEntity(ProjectDTO projectDTO) {
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
