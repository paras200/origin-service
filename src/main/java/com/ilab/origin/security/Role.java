package com.ilab.origin.security;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "MASTER_ROLE_DATA")
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;
}
