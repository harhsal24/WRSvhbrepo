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
//        Employee employee1 = new Employee();
//        employee1.setName("John Doe");
//        employee1.setRole(Role.TEAM_LEADER);
//        employee1.setEmail("john@example.com");
//        employee1.setGender(Gender.MALE);
//        employeeService.createEmployee(employee1);
//
//        Employee employee2 = new Employee();
//        employee2.setName("Jane Smith");
//        employee2.setRole(Role.TEAM_LEADER);
//        employee2.setEmail("jane@example.com");
//        employee2.setGender(Gender.FEMALE);
//        employeeService.createEmployee(employee2);






        // Create and save initial projects
        // Project project1 = new Project();
        // project1.setProjectName("Project A");
        // project1.setStartDate(LocalDate.of(2023, 8, 1));
        // project1.setExpectedEndDate(LocalDate.of(2023, 12, 31));
        // project1.setTeamLeader(employee1);
        // employee1.getLedProjects().add(project1);
        // project1.getEmployees().add(employee1);
        // projectService.createProject(project1);

        // Project project2 = new Project();
        // project2.setProjectName("Project B");
        // project2.setStartDate(LocalDate.of(2023, 9, 1));
        // project2.setExpectedEndDate(LocalDate.of(2023, 11, 30));
        // project2.setTeamLeader(employee2);
        // employee2.getLedProjects().add(project2);
        // project2.getEmployees().add(employee2);
        // projectService.createProject(project2);

        // // Create and save initial weekly reports
        // WeeklyReport report1 = new WeeklyReport();
        // report1.setEmployee(employee1);
        // report1.setProject(project1);
        // report1.setPlannedCompletionDate(LocalDate.of(2023, 8, 15));
        // report1.setActualCompletionDate(LocalDate.of(2023, 8, 16));
        // report1.setDeliverables("Implemented feature X");
        // report1.setNoOfHours(40);
        // report1.setActivity("Coding");
        // report1.setRemark("No issues");
        // report1.setPointsForDiscussion("None");
        // report1.setExpectedActivitiesOfUpcomingWeek("Test feature X");
        // report1.setReportStatus(ReportStatus.APPROVED);
        // weeklyReportService.createReport(report1);

        // WeeklyReport report2 = new WeeklyReport();
        // report2.setEmployee(employee2);
        // report2.setProject(project2);
        // report2.setPlannedCompletionDate(LocalDate.of(2023, 9, 15));
        // report2.setActualCompletionDate(LocalDate.of(2023, 9, 16));
        // report2.setDeliverables("Fixed bug Y");
        // report2.setNoOfHours(30);
        // report2.setActivity("Debugging");
        // report2.setRemark("Found root cause");
        // report2.setPointsForDiscussion("Discuss testing strategy");
        // report2.setExpectedActivitiesOfUpcomingWeek("Optimize code");
        // report2.setReportStatus(ReportStatus.IN_PROGRESS);
        // weeklyReportService.createReport(report2);

        // You can continue adding more dummy data for employees, projects, and reports
    };
}


}
