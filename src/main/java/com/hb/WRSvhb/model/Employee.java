package com.hb.WRSvhb.model;

import com.hb.WRSvhb.enums.Gender;
import com.hb.WRSvhb.enums.Role;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long empId;

    @Column(nullable = false)
    private String name;


    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Role role;

    @Column(unique = true)
    private String email;

    @Column(nullable = false)
    private String password;


    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Gender gender;


    @ManyToOne
    @JoinColumn(name = "manager_id")
    private Employee manager;

    @OneToMany(mappedBy = "teamLeader")
    private List<Project> ledProjects = new ArrayList<>();


    @ManyToMany(mappedBy = "employees")
    private List<Project> projects = new ArrayList<>();


    @OneToMany(mappedBy = "employee")
    private List<WeeklyReport> weeklyReports = new ArrayList<>();

    @OneToOne(mappedBy = "employee", cascade = CascadeType.ALL, orphanRemoval = true)
    private RefreshToken refreshToken;

    private boolean active;

    private String otp;

    private LocalDateTime otpGeneratedTime;

}
