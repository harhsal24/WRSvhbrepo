package com.hb.WRSvhb.controller;


import com.hb.WRSvhb.config.authdtos.exceptions.NotFoundException;
import com.hb.WRSvhb.dtos.EmployeeDTO;
import com.hb.WRSvhb.dtos.EmployeeRequestDTO;
import com.hb.WRSvhb.dtos.EmployeeResponseDTO;
import com.hb.WRSvhb.enums.Role;
import com.hb.WRSvhb.model.Employee;
import com.hb.WRSvhb.service.EmployeeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/employees")
public class EmployeeController {

    private final  EmployeeService employeeService;
    private final Logger logger = LoggerFactory.getLogger(EmployeeController.class);

    @Autowired
    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }


    @GetMapping("/allEmployees")
    public ResponseEntity<List<EmployeeDTO>> getAllEmployees() {
        List<EmployeeDTO> allEmployees = employeeService.findAllEmployees()
                .stream()
                .map(this::convertToEmployeeDTO)
                .collect(Collectors.toList());

        return ResponseEntity.ok(allEmployees);
    }

    @GetMapping("/{employeeId}")
    public ResponseEntity<EmployeeResponseDTO> getEmployeeById(@PathVariable Long employeeId) {
        try {
            EmployeeResponseDTO employeeDTO = employeeService.getEmployeeById(employeeId)
                    .orElseThrow(() -> new NotFoundException("Employee not found with ID: " + employeeId));
            return ResponseEntity.ok(employeeDTO);
        } catch (NotFoundException ex) {
            logger.error(ex.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/byRole/{role}")
    public ResponseEntity<List<EmployeeDTO>> getEmployeesByRole(@PathVariable Role role) {
        List<Employee> employees = employeeService.getEmployeesByRole(role);
        List<EmployeeDTO> employeeDTOs = employees.stream()
                .map(employeeService::convertToDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(employeeDTOs);
    }

    @PostMapping
    public ResponseEntity<Employee> createEmployee(@RequestBody EmployeeRequestDTO employee) {
        Employee createdEmployee = employeeService.createEmployee(employee);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdEmployee);
    }

    @PutMapping("/{empId}")
    public ResponseEntity<Employee> updateEmployee(@PathVariable Long empId, @RequestBody EmployeeRequestDTO employee) {
        Employee updatedEmployee = employeeService.updateEmployee(empId, employee);
        if (updatedEmployee != null) {
            return ResponseEntity.ok(updatedEmployee);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{empId}")
    public ResponseEntity<Void> deleteEmployee(@PathVariable Long empId) {
        boolean deleted = employeeService.deleteEmployeeById(empId);
        if (deleted) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

      @GetMapping("/regularEmployees")
    public ResponseEntity<List<EmployeeDTO>> getRegularEmployees() {
        List<EmployeeDTO> regularEmployees = employeeService.findAllEmployeesByRole(Role.REGULAR_EMPLOYEE)
                .stream()
                .map(this::convertToEmployeeDTO)
                .collect(Collectors.toList());
        
        return ResponseEntity.ok(regularEmployees);
    }

    @GetMapping("/teamLeaders")
    public ResponseEntity<List<EmployeeDTO>> getTeamLeaders() {
        List<EmployeeDTO> teamLeaders = employeeService.findAllEmployeesByRole(Role.TEAM_LEADER)
                .stream()
                .map(this::convertToEmployeeDTO)
                .collect(Collectors.toList());
        
        return ResponseEntity.ok(teamLeaders);
    }
     private EmployeeDTO convertToEmployeeDTO(Employee employee) {
        EmployeeDTO employeeDTO = new EmployeeDTO();
        employeeDTO.setEmployeeId(employee.getEmpId());
        employeeDTO.setEmployeeName(employee.getName());
        return employeeDTO;
    }
}
