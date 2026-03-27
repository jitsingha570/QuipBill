package com.QuipBill_server.QuipBill.common.controller;

import com.QuipBill_server.QuipBill.common.dto.DatabaseHealthResponse;
import com.QuipBill_server.QuipBill.common.dto.HealthResponse;
import com.QuipBill_server.QuipBill.common.service.HealthCheckService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthController {

    private static final Logger log = LoggerFactory.getLogger(HealthController.class);

    private final HealthCheckService healthCheckService;

    public HealthController(HealthCheckService healthCheckService) {
        this.healthCheckService = healthCheckService;
    }

    @GetMapping({"/health", "/api/health"})
    public ResponseEntity<HealthResponse> health() {
        log.debug("Liveness check passed.");
        return ResponseEntity.ok(new HealthResponse("UP", "RUNNING"));
    }

    @GetMapping({"/health/db", "/api/health/db"})
    public ResponseEntity<DatabaseHealthResponse> databaseHealth() {
        try {
            boolean databaseHealthy = healthCheckService.isDatabaseHealthy();

            if (!databaseHealthy) {
                log.warn("Database health check failed.");
                return ResponseEntity.ok(new DatabaseHealthResponse("UP", "DOWN"));
            }

            log.debug("Database health check passed.");
            return ResponseEntity.ok(new DatabaseHealthResponse("UP", "UP"));
        } catch (Exception ex) {
            log.error("Database health endpoint encountered an unexpected error.", ex);
            return ResponseEntity.ok(new DatabaseHealthResponse("UP", "DOWN"));
        }
    }
}
