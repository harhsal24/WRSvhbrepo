package com.hb.WRSvhb.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
public class RefreshToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String token;

    @Column(nullable = false)
    private LocalDateTime expirationDate;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;

    public boolean isValid() {
        return expirationDate.isAfter(LocalDateTime.now());
    }


    public boolean isExpired() {
        return expirationDate.isBefore(LocalDateTime.now());
    }

    // Constructors, getters, setters
}
