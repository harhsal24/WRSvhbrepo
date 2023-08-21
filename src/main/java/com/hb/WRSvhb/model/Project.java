package com.hb.WRSvhb.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Project {


    @ManyToOne
    @JoinColumn(name = "team_leader_id")
    private Employee teamLeader;

    @ManyToMany
    @JoinTable(
            name = "project_employee",
            joinColumns = @JoinColumn(name = "project_id"),
            inverseJoinColumns = @JoinColumn(name = "employee_id")
    )
    private List<Employee> employees = new ArrayList<>();

    @OneToMany(mappedBy = "project")
    private List<WeeklyReport> weeklyReports = new ArrayList<>();



    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long projectId; 

    private String projectName;

    private LocalDate startDate;
    private LocalDate expectedEndDate;
}
