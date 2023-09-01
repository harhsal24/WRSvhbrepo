package com.hb.WRSvhb.service;

import com.hb.WRSvhb.dtos.EmployeeDTO;
import com.hb.WRSvhb.dtos.EmployeeResponseDTO;
import com.hb.WRSvhb.dtos.ProjectDTO;
import com.hb.WRSvhb.dtos.WeeklyReportRequestResponseDTO;
import com.hb.WRSvhb.enums.Role;
import com.hb.WRSvhb.model.Employee;
import com.hb.WRSvhb.model.Project;
import com.hb.WRSvhb.model.WeeklyReport;
import com.hb.WRSvhb.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EmployeeService {

    private final EmployeeRepository employeeRepository;

    @Autowired
    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    public List<Employee> findAllEmployees() {
        return employeeRepository.findAll();
    }

    public Employee createEmployee(Employee employee) {
        return employeeRepository.save(employee);
    }

    public Employee updateEmployee(Long empId, Employee updatedEmployee) {
        Employee existingEmployee = employeeRepository.findById(empId).orElse(null);
        if (existingEmployee != null) {
            // Update the fields of the existing employee with the new data
            existingEmployee.setName(updatedEmployee.getName());
            existingEmployee.setRole(updatedEmployee.getRole());
            // Update other fields as needed

            return employeeRepository.save(existingEmployee);
        }
        return null; // Employee not found
    }

    public List<Employee> findAllEmployeesByRole(Role role) {
        return employeeRepository.findByRole(role);
    }

    public boolean deleteEmployeeById(Long employeeId) {
        if (employeeRepository.existsById(employeeId)) {
            employeeRepository.deleteById(employeeId);
            return true;
        }
        return false;
    }

    public Optional<EmployeeResponseDTO> getEmployeeById(Long employeeId) {
        return employeeRepository.findById(employeeId)
                .map(this::convertToResponseDTO);
    }

    public List<Employee> getEmployeesByRole(Role role) {
        return employeeRepository.findByRole(role);
    }

    private EmployeeResponseDTO convertToResponseDTO(Employee employee) {
        EmployeeResponseDTO responseDTO = new EmployeeResponseDTO();
        responseDTO.setEmpId(employee.getEmpId());
        responseDTO.setName(employee.getName());
        responseDTO.setRole(employee.getRole());
        responseDTO.setGender(employee.getGender());
        responseDTO.setEmail(employee.getEmail());

        if (employee.getManager() != null) {
            responseDTO.setManager(convertToDTO(employee.getManager()));
        }

        // Convert projects
        List<ProjectDTO> projectDTOs = convertToProjectDTOList(employee.getProjects());
        responseDTO.setProjects(projectDTOs);

        // Convert led projects
        List<ProjectDTO> ledProjectDTOs = convertToProjectDTOList(employee.getLedProjects());
        responseDTO.setLedProjects(ledProjectDTOs);

        // Convert weekly reports
        List<WeeklyReportRequestResponseDTO> reportResponseDTOs = convertToWeeklyReportDTOList(employee.getWeeklyReports());
        responseDTO.setWeeklyReports(reportResponseDTOs);

        return responseDTO;
    }
    private List<ProjectDTO> convertToProjectDTOList(List<Project> projects) {
        return projects.stream()
                .map(this::convertToProjectDTO)
                .collect(Collectors.toList());
    }

    private List<WeeklyReportRequestResponseDTO> convertToWeeklyReportDTOList(List<WeeklyReport> weeklyReports) {
        return weeklyReports.stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }
    private ProjectDTO convertToProjectDTO(Project project) {
        ProjectDTO projectDTO = new ProjectDTO();
        projectDTO.setProjectId(project.getProjectId());
        projectDTO.setProjectName(project.getProjectName());
        if(project.getTeamLeader()!=null)
        {
            EmployeeDTO teamLeaderDTO = convertToDTO(project.getTeamLeader());
            projectDTO.setTeamLeader(teamLeaderDTO);
        }
        // Set other fields as needed
        return projectDTO;
    }
    private WeeklyReportRequestResponseDTO convertToResponseDTO (WeeklyReport report){
        WeeklyReportRequestResponseDTO reportResponseDTO = new WeeklyReportRequestResponseDTO();
        reportResponseDTO.setEmployee(convertToDTO(report.getEmployee()));
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

    private Employee convertToEntity(EmployeeDTO dto) {
        Employee employee = new Employee();
        employee.setEmpId(dto.getEmployeeId());
        employee.setName(dto.getEmployeeName());
        // Set other fields
        return employee;
    }

    public EmployeeDTO convertToDTO(Employee employee) {
        EmployeeDTO dto = new EmployeeDTO();
        dto.setEmployeeId(employee.getEmpId());
        dto.setEmployeeName(employee.getName());
        // Set other relevant fields
        return dto;
    }
}
