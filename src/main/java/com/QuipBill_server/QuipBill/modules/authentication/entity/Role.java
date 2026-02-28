package com.QuipBill_server.QuipBill.modules.authentication.entity;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "roles")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)  // Stores enum as text in DB
    @Column(length = 50, unique = true, nullable = false)
    private RoleName name;

    @JsonIgnore  // 🔥 Prevent infinite recursion in JSON
    @ManyToMany(mappedBy = "roles", fetch = FetchType.LAZY)
    private Set<Shop> users = new HashSet<>();

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

    public void setUsers(Set<Shop> users) {
        this.users = users;
    }
}