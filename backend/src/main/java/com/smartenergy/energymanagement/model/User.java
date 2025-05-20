package com.smartenergy.energymanagement.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "users")  // Maps to the "users" table in PostgreSQL
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    private String companyName;
    private String state;
    private String phoneNumber;
    
    @Column(nullable = false)
    private String dashboardLink;

}
