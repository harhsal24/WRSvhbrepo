package com.hb.WRSvhb.config.authdtos.user;

import com.hb.WRSvhb.model.Employee;
import com.hb.WRSvhb.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private EmployeeRepository employeeRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Employee> employee= employeeRepository.findByEmail(username);
        if (!employee.isPresent()){
            throw new UsernameNotFoundException("could not find username");
        }
            CustomUserDetails customUserDetails=new CustomUserDetails(employee.get());

        return customUserDetails;
    }
}
