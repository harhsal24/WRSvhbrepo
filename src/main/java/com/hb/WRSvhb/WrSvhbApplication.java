package com.hb.WRSvhb;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.hb.WRSvhb.enums.Gender;

import com.hb.WRSvhb.enums.Role;
import com.hb.WRSvhb.model.Employee;

import com.hb.WRSvhb.service.EmployeeService;
import com.hb.WRSvhb.service.ProjectService;
import com.hb.WRSvhb.service.WeeklyReportService;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.crypto.password.PasswordEncoder;


@SpringBootApplication
@ComponentScan(basePackages = "com.hb.WRSvhb")
public class WrSvhbApplication {

	public static void main(String[] args) {
		SpringApplication.run(WrSvhbApplication.class, args);
	}

    @Autowired
    private PasswordEncoder passwordEncoder;

@Bean
public CommandLineRunner initData(
        EmployeeService employeeService,
        ProjectService projectService,
        WeeklyReportService weeklyReportService) {
    return args -> {
        // Create and save initial employees

    };
}


}
