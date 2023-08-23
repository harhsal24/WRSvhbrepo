package com.hb.WRSvhb.config.authdtos.user;

import com.hb.WRSvhb.config.authdtos.CredentialsDto;
import com.hb.WRSvhb.config.authdtos.SignUpDto;
import com.hb.WRSvhb.config.authdtos.UserDto;
import com.hb.WRSvhb.config.authdtos.exceptions.AppException;
import com.hb.WRSvhb.model.Employee;
import com.hb.WRSvhb.repository.EmployeeRepository;


import lombok.RequiredArgsConstructor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.nio.CharBuffer;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserService {

    private final EmployeeRepository employeeRepository;
    private final PasswordEncoder passwordEncoder;

    private static final Logger log = LoggerFactory.getLogger(UserService.class);



    public UserDto login(CredentialsDto credentialsDto) {
        log.info("Attempting login with username: {}", credentialsDto.getLogin());
        Employee employee = employeeRepository.findByEmail(credentialsDto.getLogin())
                .orElseThrow(() -> new AppException("Unknown user", HttpStatus.NOT_FOUND));

        if (passwordEncoder.matches(CharBuffer.wrap(credentialsDto.getPassword()), employee.getPassword())) {
            log.info("Login successful for user: {}", credentialsDto.getLogin());
            return convertEmployeeToUserDto(employee);
        }
        log.warn("Invalid password for user: {}", credentialsDto.getLogin());
        throw new AppException("Invalid password", HttpStatus.BAD_REQUEST);
    }

    public UserDto register(SignUpDto signUpDto) {
        Optional<Employee> optionalEmployee = employeeRepository.findByEmail(signUpDto.getLogin());

        if (optionalEmployee.isPresent()) {
            throw new AppException("Login already exists", HttpStatus.BAD_REQUEST);
        }


        Employee employee = new Employee();
        employee.setName(signUpDto.getFirstName() + " " + signUpDto.getLastName());
        employee.setRole(signUpDto.getRole());
        employee.setEmail(signUpDto.getLogin());
        employee.setGender(signUpDto.getGender());

        if (signUpDto.getManagerId() != null) {
            Employee manager = employeeRepository.findByEmpId(signUpDto.getManagerId())
                    .orElseThrow(() -> new AppException("Manager not found", HttpStatus.NOT_FOUND));
            employee.setManager(manager);
        }
        // Encrypt and set password
        employee.setPassword(passwordEncoder.encode(CharBuffer.wrap(signUpDto.getPassword())));

        Employee savedEmployee = employeeRepository.save(employee);

        return convertEmployeeToUserDto(employeeRepository.save(employee));
    }

    public UserDto findByLogin(String login) {
        Employee employee = employeeRepository.findByEmail(login)
                .orElseThrow(() -> new AppException("Unknown user", HttpStatus.NOT_FOUND));
        return convertEmployeeToUserDto(employee);
    }


    public UserDto convertEmployeeToUserDto(Employee employee) {
        UserDto userDto = new UserDto();
        userDto.setEmpId(employee.getEmpId());
        userDto.setName(employee.getName());
        userDto.setLogin(employee.getEmail());
        // Set other properties as needed
        return userDto;
    }


}
