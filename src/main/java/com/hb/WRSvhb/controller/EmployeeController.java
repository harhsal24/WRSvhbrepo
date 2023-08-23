package com.hb.WRSvhb.controller;


import com.hb.WRSvhb.dtos.EmployeeDTO;
import com.hb.WRSvhb.dtos.EmployeeResponseDTO;
import com.hb.WRSvhb.enums.Role;
import com.hb.WRSvhb.model.Employee;
import com.hb.WRSvhb.service.EmployeeService;
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
        Optional<EmployeeResponseDTO> employeeDTO = employeeService.getEmployeeById(employeeId);
        return employeeDTO.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
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
    public ResponseEntity<Employee> createEmployee(@RequestBody Employee employee) {
        Employee createdEmployee = employeeService.createEmployee(employee);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdEmployee);
    }

    @PutMapping("/{empId}")
    public ResponseEntity<Employee> updateEmployee(@PathVariable Long empId, @RequestBody Employee employee) {
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
