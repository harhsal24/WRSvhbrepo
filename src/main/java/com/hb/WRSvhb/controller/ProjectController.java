package com.hb.WRSvhb.controller;

import com.hb.WRSvhb.dtos.ProjectDTO;
import com.hb.WRSvhb.dtos.ProjectRequestDTO;
import com.hb.WRSvhb.dtos.ProjectResponseDTO;
import com.hb.WRSvhb.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/projects")
public class ProjectController {

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
}
