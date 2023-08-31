package com.hb.WRSvhb.service;

import com.hb.WRSvhb.dtos.EmployeeDTO;
import com.hb.WRSvhb.dtos.EmployeeRequestDTO;
import com.hb.WRSvhb.dtos.EmployeeResponseDTO;
import com.hb.WRSvhb.enums.Role;
import com.hb.WRSvhb.model.Employee;
import com.hb.WRSvhb.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    private  final PasswordEncoder passwordEncoder;

    @Autowired
    public EmployeeService(EmployeeRepository employeeRepository, PasswordEncoder passwordEncoder) {
        this.employeeRepository = employeeRepository;
        this.passwordEncoder=passwordEncoder;
    }

    public List<Employee> findAllEmployees() {
        return employeeRepository.findAll();
    }

    public Employee createEmployee(EmployeeRequestDTO employee) {
         Employee newEmployee = new Employee();
           Optional<Employee> manager=  employeeRepository.findById(employee.getManagerId());
            if (manager.isPresent()){
                newEmployee.setManager(manager.get());
            }else{
                newEmployee.setManager(null);
            }
            newEmployee.setName(employee.getFirstName()+" "+ employee.getLastName());
            newEmployee.setRole(employee.getRole());
            newEmployee.setGender(employee.getGender());
            newEmployee.setEmail(employee.getLogin());
        // Convert char[] password to a String
        String password = new String(employee.getPassword());
            newEmployee.setPassword(passwordEncoder.encode(password));
        return employeeRepository.save(newEmployee);
    }

    public Employee updateEmployee(Long empId, EmployeeRequestDTO updatedEmployee) {
        Employee existingEmployee = employeeRepository.findById(empId).orElse(null);
        Employee manager=  employeeRepository.findById(updatedEmployee.getManagerId()).orElse(null);;
        if (manager!=null){
            existingEmployee.setManager(manager);
        }
            if (existingEmployee != null) {
            // Update the fields of the existing employee with the new data
            existingEmployee.setName(updatedEmployee.getFirstName() + " " + updatedEmployee.getLastName());
            existingEmployee.setRole(updatedEmployee.getRole());
            existingEmployee.setGender(updatedEmployee.getGender());
            existingEmployee.setEmail(updatedEmployee.getLogin());

            // Convert char[] password to a String
            String password = new String(updatedEmployee.getPassword());
            existingEmployee.setPassword(passwordEncoder.encode(password));

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
        // Set other fields as needed
        return responseDTO;
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
