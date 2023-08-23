package com.hb.WRSvhb.repository;

import com.hb.WRSvhb.enums.Role;
import com.hb.WRSvhb.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    Optional<Employee> findByEmail(String email);
    Optional<Employee> findByEmpId(Long empId);
    List<Employee> findByRole(Role role);

}