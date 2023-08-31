package com.hb.WRSvhb.service;

import com.hb.WRSvhb.dtos.EmployeeDTO;
import com.hb.WRSvhb.dtos.ProjectDTO;
import com.hb.WRSvhb.dtos.ProjectRequestDTO;
import com.hb.WRSvhb.dtos.ProjectResponseDTO;
import com.hb.WRSvhb.dtos.WeeklyReportRequestResponseDTO;
import com.hb.WRSvhb.model.Employee;
import com.hb.WRSvhb.model.Project;
import com.hb.WRSvhb.model.WeeklyReport;
import com.hb.WRSvhb.repository.EmployeeRepository;
import com.hb.WRSvhb.repository.ProjectRepository;

import jakarta.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final EmployeeRepository employeeRepository;

    @Autowired
    public ProjectService(ProjectRepository projectRepository, EmployeeRepository employeeRepository) {
        this.projectRepository = projectRepository;
        this.employeeRepository = employeeRepository;
    }

    public List<ProjectResponseDTO> getAllProjects() {
        List<Project> projects = projectRepository.findAll();
        return projects.stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    public List<ProjectResponseDTO> getProjectsByEmployeeId(Long employeeId) {
        List<Project> projects = projectRepository.findByEmployees_EmpId(employeeId);
        return projects.stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    public List<ProjectResponseDTO> getProjectsByTeamLeaderId(Long teamLeaderId) {
        List<Project> projects = projectRepository.findByTeamLeader_EmpId(teamLeaderId);
        return projects.stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    public Optional<ProjectResponseDTO> getProjectById(Long projectId) {
        Optional<Project> project = projectRepository.findById(projectId);
        return project.map(this::convertToResponseDTO);
    }

    public ProjectResponseDTO createProject(ProjectRequestDTO projectRequestDTO) {
        Project project = convertToEntity(projectRequestDTO);
        Project savedProject = projectRepository.save(project);
        return convertToResponseDTO(savedProject);
    }


    public Optional<ProjectResponseDTO> updateProject(Long projectId, ProjectRequestDTO updatedProjectDTO) {
        Optional<Project> existingProject = projectRepository.findById(projectId);
        if (existingProject.isPresent()) {
            Project projectToUpdate = existingProject.get();
            // Update fields of projectToUpdate with data from updatedProjectDTO
            projectToUpdate.setProjectName(updatedProjectDTO.getProjectName());
            projectToUpdate.setStartDate(updatedProjectDTO.getStartDate());
            projectToUpdate.setExpectedEndDate(updatedProjectDTO.getExpectedEndDate());
            // Update other fields as needed

            // Update the list of employees
            List<EmployeeDTO> employeeDTOs = updatedProjectDTO.getEmployees();
            if (employeeDTOs != null) {
                List<Employee> employees = employeeDTOs.stream()
                        .map(employeeDTO -> findEmployeeById(employeeDTO.getEmployeeId()))
                        .collect(Collectors.toList());
                projectToUpdate.setEmployees(employees);
            }

            // Update the team leader
            EmployeeDTO teamLeaderDTO = updatedProjectDTO.getTeamLeader();
            if (teamLeaderDTO != null) {
                Employee teamLeader = findEmployeeById(teamLeaderDTO.getEmployeeId());
                projectToUpdate.setTeamLeader(teamLeader);
            }

            Project savedProject = projectRepository.save(projectToUpdate);
            return Optional.of(convertToResponseDTO(savedProject));
        }
        return Optional.empty();
    }

    private Employee findEmployeeById(Long employeeId) {
        Optional<Employee> employee = employeeRepository.findById(employeeId);
        return employee.orElseThrow(() -> new EntityNotFoundException("Employee not found with ID: " + employeeId));
    }



    // paginated version start here

    public Page<ProjectResponseDTO> getAllProjectsPaginated(Pageable pageable) {
        Page<Project> projectsPage = projectRepository.findAll(pageable);
        return projectsPage.map(this::convertToResponseDTO);
    }

    // Paginated version of getProjectsByEmployeeId
    public Page<ProjectResponseDTO> getProjectsByEmployeeIdPaginated(Long employeeId, Pageable pageable) {
        Page<Project> projectsPage = projectRepository.findByEmployees_EmpId(employeeId, pageable);
        return projectsPage.map(this::convertToResponseDTO);
    }

    // Paginated version of getProjectsByTeamLeaderId
    public Page<ProjectResponseDTO> getProjectsByTeamLeaderIdPaginated(Long teamLeaderId, Pageable pageable) {
        Page<Project> projectsPage = projectRepository.findByTeamLeader_EmpId(teamLeaderId, pageable);
        return projectsPage.map(this::convertToResponseDTO);
    }

//    paginated version end here


    // helper functions start
    private ProjectDTO convertToDTO(Project project) {
        ProjectDTO projectDTO = new ProjectDTO();
        projectDTO.setProjectId(project.getProjectId());
        projectDTO.setProjectName(project.getProjectName());
        // Set other fields
        return projectDTO;
    }
//updated
    private ProjectResponseDTO convertToResponseDTO(Project project) {
        ProjectResponseDTO responseDTO = new ProjectResponseDTO();

        if (project.getTeamLeader() != null) {
            responseDTO.setTeamLeader(convertToEmployeeDTO(project.getTeamLeader()));
        }

        if (project.getEmployees() != null) {
            responseDTO.setEmployees(project.getEmployees().stream()
                    .map(this::convertToEmployeeDTO)
                    .collect(Collectors.toList()));
        }

        if (project.getWeeklyReports() != null) {
            responseDTO.setWeeklyReports(project.getWeeklyReports().stream()
                    .map(this::convertToWeeklyReportRequestResponseDTO)
                    .collect(Collectors.toList()));
        }

        responseDTO.setProjectId(project.getProjectId());
        responseDTO.setProjectName(project.getProjectName());
        responseDTO.setStartDate(project.getStartDate());
        responseDTO.setExpectedEndDate(project.getExpectedEndDate());

        // Set other fields

        return responseDTO;
    }


    private EmployeeDTO convertToEmployeeDTO(Employee employee) {
        EmployeeDTO employeeDTO = new EmployeeDTO();
        employeeDTO.setEmployeeId(employee.getEmpId());
        employeeDTO.setEmployeeName(employee.getName());
        // Set other fields as needed
        return employeeDTO;
    }

    private WeeklyReportRequestResponseDTO convertToWeeklyReportRequestResponseDTO(WeeklyReport report) {
        WeeklyReportRequestResponseDTO reportResponseDTO = new WeeklyReportRequestResponseDTO();
        reportResponseDTO.setEmployee(convertToEmployeeDTO(report.getEmployee()));
        reportResponseDTO.setProject(convertToProjectDTO(report.getProject()));
        reportResponseDTO.setReportId(report.getReportId());
        reportResponseDTO.setReportCreatedDateTime(report.getReportCreatedDateTime());

        reportResponseDTO.setRemark(report.getRemark());
        reportResponseDTO.setPointsForDiscussion(report.getPointsForDiscussion());
        reportResponseDTO.setExpectedActivitiesOfUpcomingWeek(report.getExpectedActivitiesOfUpcomingWeek());
        reportResponseDTO.setReportStatus(report.getReportStatus());

        reportResponseDTO.setReportDetailsList(report.getReportDetailsList());

        return reportResponseDTO;
    }

    private ProjectDTO convertToProjectDTO(Project project) {
        ProjectDTO projectDTO = new ProjectDTO();
        projectDTO.setProjectId(project.getProjectId());
        projectDTO.setProjectName(project.getProjectName());
        if(project.getTeamLeader()!=null)
        {
            EmployeeDTO teamLeaderDTO = convertToEmployeeDTO(project.getTeamLeader());
            projectDTO.setTeamLeader(teamLeaderDTO);
        }
        // Set other fields as needed
        return projectDTO;
    }

    private Project convertToEntity(ProjectRequestDTO projectRequestDTO) {
        Project project = new Project();
        project.setProjectName(projectRequestDTO.getProjectName());
        project.setStartDate(projectRequestDTO.getStartDate());
        project.setExpectedEndDate(projectRequestDTO.getExpectedEndDate());
        // Set other fields as needed

        // Update the team leader
        EmployeeDTO teamLeaderDTO = projectRequestDTO.getTeamLeader();
        if (teamLeaderDTO != null) {
            Employee teamLeader = findEmployeeById(teamLeaderDTO.getEmployeeId());
            if (teamLeader == null) {
                // Handle the case where team leader is not found
                throw new IllegalArgumentException("Team leader with ID " + teamLeaderDTO.getEmployeeId() + " not found");
            }
            project.setTeamLeader(teamLeader);
        }

        // Update the list of employees
        List<EmployeeDTO> employeeDTOs = projectRequestDTO.getEmployees();
        if (employeeDTOs != null && !employeeDTOs.isEmpty()) {
            List<Employee> employees = employeeDTOs.stream()
                    .map(employeeDTO -> findEmployeeById(employeeDTO.getEmployeeId()))
                    .collect(Collectors.toList());

            // Check if all employees were found
            if (employees.size() != employeeDTOs.size()) {
                throw new IllegalArgumentException("One or more employees not found");
            }
            project.setEmployees(employees);
        }

        return project;
    }

    public boolean deleteProjectById(Long projectId) {
        Optional<Project> existingProject = projectRepository.findById(projectId);
        if (existingProject.isPresent()) {
            projectRepository.deleteById(projectId);
            return true;
        }
        return false;
    }

}
