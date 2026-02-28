package com.QuipBill_server.QuipBill.modules.authentication.repository;

import com.QuipBill_server.QuipBill.modules.authentication.entity.Role;
import com.QuipBill_server.QuipBill.modules.authentication.entity.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {

    Optional<Role> findByName(RoleName name);
}