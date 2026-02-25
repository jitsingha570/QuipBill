package com.QuipBill_server.QuipBill.modules.authentication.entity;

import jakarta.persistence.*;
import java.util.Set;

@Entity
@Table(name = "roles")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(length = 50, unique = true, nullable = false)
    private RoleName name;

    @ManyToMany(mappedBy = "roles")
    private Set<Shop> users;

    // Constructors
    public Role() {}

    public Role(RoleName name) {
        this.name = name;
    }

    // Getters & Setters
    public Long getId() {
        return id;
    }

    public RoleName getName() {
        return name;
    }

    public void setName(RoleName name) {
        this.name = name;
    }

    public Set<Shop> getUsers() {
        return users;
    }
}