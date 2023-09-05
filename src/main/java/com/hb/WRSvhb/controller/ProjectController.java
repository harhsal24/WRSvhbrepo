package com.hb.WRSvhb.controller;

import com.hb.WRSvhb.dtos.ProjectDTO;
import com.hb.WRSvhb.dtos.ProjectRequestDTO;
import com.hb.WRSvhb.dtos.ProjectResponseDTO;
import com.hb.WRSvhb.service.ProjectService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/projects")
public class ProjectController {

    private  static Logger log = LoggerFactory.getLogger(ProjectController.class);

    private final ProjectService projectService;

    @Autowired
    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }



    @GetMapping
    public ResponseEntity<List<ProjectResponseDTO>> getAllProjects() {
        List<ProjectResponseDTO> projectDTOs = projectService.getAllProjects();
        return ResponseEntity.ok(projectDTOs);
    }

     @GetMapping("/employee/{employeeId}")
    public ResponseEntity<List<ProjectResponseDTO>> getProjectsByEmployeeId(@PathVariable Long employeeId) {
        List<ProjectResponseDTO> projectResponseDTOs = projectService.getProjectsByEmployeeId(employeeId);
        return ResponseEntity.ok(projectResponseDTOs);
    }

    @GetMapping("/teamleader/{teamLeaderId}")
    public ResponseEntity<List<ProjectResponseDTO>> getProjectsByTeamLeaderId(@PathVariable Long teamLeaderId) {
        List<ProjectResponseDTO> projectResponseDTOs = projectService.getProjectsByTeamLeaderId(teamLeaderId);
        return ResponseEntity.ok(projectResponseDTOs);
    }

    @GetMapping("/{projectId}")
    public ResponseEntity<ProjectResponseDTO> getProjectById(@PathVariable Long projectId) {
        Optional<ProjectResponseDTO> projectDTO = projectService.getProjectById(projectId);
        return projectDTO.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<ProjectResponseDTO> createProject(@RequestBody ProjectRequestDTO projectRequestDTO) {
        ProjectResponseDTO createdProjectDTO = projectService.createProject(projectRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdProjectDTO);
    }



    @PutMapping("/{projectId}")
    public ResponseEntity<ProjectResponseDTO> updateProject(@PathVariable Long projectId, @RequestBody ProjectRequestDTO updatedProjectDTO) {

        log.info("in controller with ID {}: {}", projectId, updatedProjectDTO);


        Optional<ProjectResponseDTO> savedProjectDTO = projectService.updateProject(projectId, updatedProjectDTO);

        return savedProjectDTO.map(dto -> ResponseEntity.ok(dto)).orElse(ResponseEntity.notFound().build());
    }


    @DeleteMapping("/{projectId}")
    public ResponseEntity<Void> deleteProject(@PathVariable Long projectId) {
        boolean deleted = projectService.deleteProjectById(projectId);
        if (deleted) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // paginated version start

    @GetMapping("/pagination")
    public ResponseEntity<Page<ProjectResponseDTO>> getAllProjectsPaginated(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<ProjectResponseDTO> projectDTOs = projectService.getAllProjectsPaginated(pageable);
        return ResponseEntity.ok(projectDTOs);
    }

    @GetMapping("/employee/{employeeId}/pagination")
    public ResponseEntity<Page<ProjectResponseDTO>> getProjectsByEmployeeIdPaginated(
            @PathVariable Long employeeId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<ProjectResponseDTO> projectResponseDTOs = projectService.getProjectsByEmployeeIdPaginated(employeeId, pageable);
        return ResponseEntity.ok(projectResponseDTOs);
    }

    @GetMapping("/teamleader/{teamLeaderId}/pagination")
    public ResponseEntity<Page<ProjectResponseDTO>> getProjectsByTeamLeaderIdPaginated(
            @PathVariable Long teamLeaderId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<ProjectResponseDTO> projectResponseDTOs = projectService.getProjectsByTeamLeaderIdPaginated(teamLeaderId, pageable);
        return ResponseEntity.ok(projectResponseDTOs);
    }

    // end here

}
