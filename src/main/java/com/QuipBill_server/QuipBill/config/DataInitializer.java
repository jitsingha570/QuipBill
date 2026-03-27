package com.QuipBill_server.QuipBill.config;

import com.QuipBill_server.QuipBill.modules.authentication.entity.Role;
import com.QuipBill_server.QuipBill.modules.authentication.entity.RoleName;
import com.QuipBill_server.QuipBill.modules.authentication.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final RoleRepository roleRepository;

    @Override
    public void run(String... args) {
        try {
            if (roleRepository.findByName(RoleName.ROLE_SHOP).isEmpty()) {
                roleRepository.save(new Role(RoleName.ROLE_SHOP));
            }

            if (roleRepository.findByName(RoleName.ROLE_ADMIN).isEmpty()) {
                roleRepository.save(new Role(RoleName.ROLE_ADMIN));
            }
        } catch (Exception ex) {
            log.warn("Skipping role initialization because the database is not available yet.", ex);
        }
    }
}
