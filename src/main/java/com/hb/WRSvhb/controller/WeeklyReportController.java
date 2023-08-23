package com.hb.WRSvhb.controller;

import com.hb.WRSvhb.dtos.WeeklyReportRequestResponseDTO;
import com.hb.WRSvhb.model.WeeklyReport;
import com.hb.WRSvhb.service.WeeklyReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/reports")
public class WeeklyReportController {

    private final WeeklyReportService weeklyReportService;


    @Autowired
    public WeeklyReportController(WeeklyReportService weeklyReportService) {
        this.weeklyReportService = weeklyReportService;
    }

    @GetMapping
    public ResponseEntity<List<WeeklyReportRequestResponseDTO>> getAllReports() {
        List<WeeklyReportRequestResponseDTO> reports = weeklyReportService.getAllReports();
        return ResponseEntity.ok(reports);
    }

    @GetMapping("/{reportId}")
    public ResponseEntity<WeeklyReportRequestResponseDTO> getReportById(@PathVariable Long reportId) {
        return weeklyReportService.getReportById(reportId)
                .map(reportDTO -> new ResponseEntity<>(reportDTO, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }


    @GetMapping("/employee/{employeeId}")
    public ResponseEntity<List<WeeklyReportRequestResponseDTO>> getReportsByEmployeeId(@PathVariable Long employeeId) {
        List<WeeklyReportRequestResponseDTO> reports = weeklyReportService.getReportsByEmployeeId(employeeId);
        return ResponseEntity.ok(reports);
    }

    @GetMapping("/teamleader/{teamLeaderId}")
    public ResponseEntity<List<WeeklyReportRequestResponseDTO>> getReportsByTeamLeaderId(@PathVariable Long teamLeaderId) {
        List<WeeklyReportRequestResponseDTO> reports = weeklyReportService.getReportsByTeamLeaderId(teamLeaderId);
        return ResponseEntity.ok(reports);
    }

    @GetMapping("/employee/{employeeId}/project/{projectId}")
    public ResponseEntity<List<WeeklyReportRequestResponseDTO>> getReportsByEmployeeAndProject(
            @PathVariable Long employeeId, @PathVariable Long projectId) {
        List<WeeklyReportRequestResponseDTO> reports = weeklyReportService.getReportsByEmployeeAndProject(employeeId, projectId);
        return ResponseEntity.ok(reports);
    }

    @GetMapping("/teamleader/{teamLeaderId}/project/{projectId}")
    public ResponseEntity<List<WeeklyReportRequestResponseDTO>> getReportsByTeamLeaderAndProject(
            @PathVariable Long teamLeaderId, @PathVariable Long projectId) {
        List<WeeklyReportRequestResponseDTO> reports = weeklyReportService.getReportsByTeamLeaderAndProject(teamLeaderId, projectId);
        return ResponseEntity.ok(reports);
    }

    @GetMapping("/project/{projectId}")
    public ResponseEntity<List<WeeklyReportRequestResponseDTO>> getReportsByProjectId(@PathVariable Long projectId) {
        List<WeeklyReportRequestResponseDTO> reports = weeklyReportService.getReportsByProjectId(projectId);
        return ResponseEntity.ok(reports);
    }


    @PostMapping
    public ResponseEntity<WeeklyReportRequestResponseDTO> createReport(@RequestBody WeeklyReportRequestResponseDTO reportDTO) {
        WeeklyReportRequestResponseDTO createdReportDTO = weeklyReportService.createReport(reportDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdReportDTO);
    }

    @PutMapping("/{reportId}")
    public ResponseEntity<WeeklyReportRequestResponseDTO> updateReport(
            @PathVariable Long reportId, @RequestBody WeeklyReportRequestResponseDTO updatedReportDTO) {
        Optional<WeeklyReportRequestResponseDTO> result = weeklyReportService.updateReport(reportId, updatedReportDTO);
        return result.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{reportId}")
    public ResponseEntity<Void> deleteReport(@PathVariable Long reportId) {
        boolean deleted = weeklyReportService.deleteReport(reportId);
        if (deleted) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    //paginated version

    @GetMapping("/pagination")
    public ResponseEntity<Page<WeeklyReportRequestResponseDTO>> getAllReportsWithPagination(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<WeeklyReportRequestResponseDTO> reports = weeklyReportService.getAllReportsWithPagination(pageable);
        return ResponseEntity.ok(reports);
    }

    @GetMapping("/employee/{employeeId}/pagination")
    public ResponseEntity<Page<WeeklyReportRequestResponseDTO>> getReportsByEmployeeIdWithPagination(
            @PathVariable Long employeeId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<WeeklyReportRequestResponseDTO> reports = weeklyReportService.getReportsByEmployeeIdWithPagination(employeeId, pageable);
        return ResponseEntity.ok(reports);
    }

    @GetMapping("/teamleader/{teamLeaderId}/pagination")
    public ResponseEntity<Page<WeeklyReportRequestResponseDTO>> getReportsByTeamLeaderIdWithPagination(
            @PathVariable Long teamLeaderId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<WeeklyReportRequestResponseDTO> reports = weeklyReportService.getReportsByTeamLeaderIdWithPagination(teamLeaderId, pageable);
        return ResponseEntity.ok(reports);
    }

    @GetMapping("/employee/{employeeId}/project/{projectId}/pagination")
    public ResponseEntity<Page<WeeklyReportRequestResponseDTO>> getReportsByEmployeeAndProjectWithPagination(
            @PathVariable Long employeeId,
            @PathVariable Long projectId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<WeeklyReportRequestResponseDTO> reports = weeklyReportService.getReportsByEmployeeAndProjectWithPagination(employeeId, projectId, pageable);
        return ResponseEntity.ok(reports);
    }

    @GetMapping("/teamleader/{teamLeaderId}/project/{projectId}/pagination")
    public ResponseEntity<Page<WeeklyReportRequestResponseDTO>> getReportsByTeamLeaderAndProjectWithPagination(
            @PathVariable Long teamLeaderId,
            @PathVariable Long projectId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<WeeklyReportRequestResponseDTO> reports = weeklyReportService.getReportsByTeamLeaderAndProjectWithPagination(teamLeaderId, projectId, pageable);
        return ResponseEntity.ok(reports);
    }

    @GetMapping("/project/{projectId}/pagination")
    public ResponseEntity<Page<WeeklyReportRequestResponseDTO>> getReportsByProjectIdWithPagination(
            @PathVariable Long projectId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<WeeklyReportRequestResponseDTO> reports = weeklyReportService.getReportsByProjectIdWithPagination(projectId, pageable);
        return ResponseEntity.ok(reports);
    }


}
