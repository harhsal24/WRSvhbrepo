package com.hb.WRSvhb.config.authdtos.user;

import com.hb.WRSvhb.config.authdtos.CredentialsDto;
import com.hb.WRSvhb.config.authdtos.SignUpDto;
import com.hb.WRSvhb.config.authdtos.UserDto;
import com.hb.WRSvhb.config.authdtos.exceptions.AppException;
import com.hb.WRSvhb.model.Employee;
import com.hb.WRSvhb.repository.EmployeeRepository;


import com.hb.WRSvhb.utils.EmailUtil;
import com.hb.WRSvhb.utils.OtpUtil;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.nio.CharBuffer;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserService {

    private final EmployeeRepository employeeRepository;
    private final PasswordEncoder passwordEncoder;

    private final OtpUtil otpUtil;

    private final EmailUtil emailUtil;
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

        log.info("signup body coming {}",signUpDto);


        if (optionalEmployee.isPresent()) {
            throw new AppException("Login already exists", HttpStatus.BAD_REQUEST);
        }

        String otp = otpUtil.generateOtp();

        try {
            emailUtil.sendOtpEmail(signUpDto.getLogin(), otp);
        } catch (MessagingException e) {
            throw new RuntimeException("Unable to send OTP, please try again");
        }

        Employee employee = new Employee();
        employee.setName(signUpDto.getFirstName() + " " + signUpDto.getLastName());
        employee.setRole(signUpDto.getRole());
        employee.setEmail(signUpDto.getLogin());
        employee.setGender(signUpDto.getGender());
        employee.setOtp(otp);
        employee.setOtpGeneratedTime(LocalDateTime.now());

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
        userDto.setRole(employee.getRole());
        userDto.setActive(employee.isActive());

        return userDto;
    }

    public String verifyAccount(String email, String otp) {
        Employee user = employeeRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with this email: " + email));
        if (user.getOtp().equals(otp) && Duration.between(user.getOtpGeneratedTime(),
                LocalDateTime.now()).getSeconds() < (1 * 60)) {
            user.setActive(true);
            employeeRepository.save(user);
            return "OTP verified you can login";
        }
        return "Please regenerate otp and try again";
    }

    public String regenerateOtp(String email) {
        Employee user = employeeRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with this email: " + email));
        String otp = otpUtil.generateOtp();
        try {
            emailUtil.sendOtpEmail(email, otp);
        } catch (MessagingException e) {
            throw new RuntimeException("Unable to send otp please try again");
        }
        user.setOtp(otp);
        user.setOtpGeneratedTime(LocalDateTime.now());
        employeeRepository.save(user);
        return "Email sent... please verify account within 1 minute";
    }

    public String forgotPassword(String email) {
        log.info("Attempting forgotPassword with username: {}", email);
        Employee employee = employeeRepository.findByEmail(email)
                .orElseThrow(() -> new AppException("Unknown user", HttpStatus.NOT_FOUND));

        try {
            emailUtil.sendSetPasswordEmail(email);
        } catch (MessagingException e) {
            throw new RuntimeException("unable to sent set password email please try again");
        }
        return "please check your email to set password to your account";
    }

    public String setPassword(String email, String newPassword) {
        log.info("Attempting setPassword with username: {}", email);
        Employee employee = employeeRepository.findByEmail(email)
                .orElseThrow(() -> new AppException("Unknown user", HttpStatus.NOT_FOUND));

        employee.setPassword(passwordEncoder.encode(newPassword));
        employeeRepository.save(employee);

        return "new password set successfully login in with new password";
    }
}
