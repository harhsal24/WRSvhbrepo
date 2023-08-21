package com.hb.WRSvhb.repository;

import com.hb.WRSvhb.model.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {

    List<Project> findByEmployees_EmpId(Long employeeId);

    List<Project> findByTeamLeader_EmpId(Long teamLeaderId);

}