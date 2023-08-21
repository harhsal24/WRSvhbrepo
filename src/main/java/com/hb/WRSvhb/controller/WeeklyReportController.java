package com.hb.WRSvhb.controller;

import com.hb.WRSvhb.dtos.WeeklyReportResponseDTO;
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
    public ResponseEntity<List<WeeklyReportResponseDTO>> getAllReports() {
        List<WeeklyReportResponseDTO> reports = weeklyReportService.getAllReports();
        return ResponseEntity.ok(reports);
    }

    @GetMapping("/{reportId}")
    public ResponseEntity<WeeklyReportResponseDTO> getReportById(@PathVariable Long reportId) {
        return weeklyReportService.getReportById(reportId)
                .map(reportDTO -> new ResponseEntity<>(reportDTO, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }


    @GetMapping("/employee/{employeeId}")
    public ResponseEntity<List<WeeklyReportResponseDTO>> getReportsByEmployeeId(@PathVariable Long employeeId) {
        List<WeeklyReportResponseDTO> reports = weeklyReportService.getReportsByEmployeeId(employeeId);
        return ResponseEntity.ok(reports);
    }

    @GetMapping("/teamleader/{teamLeaderId}")
    public ResponseEntity<List<WeeklyReportResponseDTO>> getReportsByTeamLeaderId(@PathVariable Long teamLeaderId) {
        List<WeeklyReportResponseDTO> reports = weeklyReportService.getReportsByTeamLeaderId(teamLeaderId);
        return ResponseEntity.ok(reports);
    }

    @GetMapping("/employee/{employeeId}/project/{projectId}")
    public ResponseEntity<List<WeeklyReportResponseDTO>> getReportsByEmployeeAndProject(
            @PathVariable Long employeeId, @PathVariable Long projectId) {
        List<WeeklyReportResponseDTO> reports = weeklyReportService.getReportsByEmployeeAndProject(employeeId, projectId);
        return ResponseEntity.ok(reports);
    }

    @GetMapping("/teamleader/{teamLeaderId}/project/{projectId}")
    public ResponseEntity<List<WeeklyReportResponseDTO>> getReportsByTeamLeaderAndProject(
            @PathVariable Long teamLeaderId, @PathVariable Long projectId) {
        List<WeeklyReportResponseDTO> reports = weeklyReportService.getReportsByTeamLeaderAndProject(teamLeaderId, projectId);
        return ResponseEntity.ok(reports);
    }

    @GetMapping("/project/{projectId}")
    public ResponseEntity<List<WeeklyReportResponseDTO>> getReportsByProjectId(@PathVariable Long projectId) {
        List<WeeklyReportResponseDTO> reports = weeklyReportService.getReportsByProjectId(projectId);
        return ResponseEntity.ok(reports);
    }


    @PostMapping
    public ResponseEntity<WeeklyReportResponseDTO> createReport(@RequestBody WeeklyReportResponseDTO reportDTO) {
        WeeklyReportResponseDTO createdReportDTO = weeklyReportService.createReport(reportDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdReportDTO);
    }

    @PutMapping("/{reportId}")
    public ResponseEntity<WeeklyReportResponseDTO> updateReport(
            @PathVariable Long reportId, @RequestBody WeeklyReportResponseDTO updatedReportDTO) {
        Optional<WeeklyReportResponseDTO> result = weeklyReportService.updateReport(reportId, updatedReportDTO);
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
    public ResponseEntity<Page<WeeklyReportResponseDTO>> getAllReportsWithPagination(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<WeeklyReportResponseDTO> reports = weeklyReportService.getAllReportsWithPagination(pageable);
        return ResponseEntity.ok(reports);
    }

    @GetMapping("/employee/{employeeId}/pagination")
    public ResponseEntity<Page<WeeklyReportResponseDTO>> getReportsByEmployeeIdWithPagination(
            @PathVariable Long employeeId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<WeeklyReportResponseDTO> reports = weeklyReportService.getReportsByEmployeeIdWithPagination(employeeId, pageable);
        return ResponseEntity.ok(reports);
    }

    @GetMapping("/teamleader/{teamLeaderId}/pagination")
    public ResponseEntity<Page<WeeklyReportResponseDTO>> getReportsByTeamLeaderIdWithPagination(
            @PathVariable Long teamLeaderId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<WeeklyReportResponseDTO> reports = weeklyReportService.getReportsByTeamLeaderIdWithPagination(teamLeaderId, pageable);
        return ResponseEntity.ok(reports);
    }

    @GetMapping("/employee/{employeeId}/project/{projectId}/pagination")
    public ResponseEntity<Page<WeeklyReportResponseDTO>> getReportsByEmployeeAndProjectWithPagination(
            @PathVariable Long employeeId,
            @PathVariable Long projectId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<WeeklyReportResponseDTO> reports = weeklyReportService.getReportsByEmployeeAndProjectWithPagination(employeeId, projectId, pageable);
        return ResponseEntity.ok(reports);
    }

    @GetMapping("/teamleader/{teamLeaderId}/project/{projectId}/pagination")
    public ResponseEntity<Page<WeeklyReportResponseDTO>> getReportsByTeamLeaderAndProjectWithPagination(
            @PathVariable Long teamLeaderId,
            @PathVariable Long projectId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<WeeklyReportResponseDTO> reports = weeklyReportService.getReportsByTeamLeaderAndProjectWithPagination(teamLeaderId, projectId, pageable);
        return ResponseEntity.ok(reports);
    }

    @GetMapping("/project/{projectId}/pagination")
    public ResponseEntity<Page<WeeklyReportResponseDTO>> getReportsByProjectIdWithPagination(
            @PathVariable Long projectId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<WeeklyReportResponseDTO> reports = weeklyReportService.getReportsByProjectIdWithPagination(projectId, pageable);
        return ResponseEntity.ok(reports);
    }


}
