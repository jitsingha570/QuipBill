package com.QuipBill_server.QuipBill.common.controller;

import com.QuipBill_server.QuipBill.common.dto.HealthResponse;
import com.QuipBill_server.QuipBill.common.service.HealthCheckService;
import java.time.Instant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
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
        long startTime = System.nanoTime();
        boolean databaseHealthy = healthCheckService.isDatabaseHealthy();
        long responseTimeMs = (System.nanoTime() - startTime) / 1_000_000;

        if (!databaseHealthy) {
            log.warn("Health check failed. Database unavailable. responseTimeMs={}", responseTimeMs);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new HealthResponse(
                            "DOWN",
                            "QuipBill",
                            "DOWN",
                            Instant.now(),
                            responseTimeMs,
                            "Application is running but database is unavailable"
                    ));
        }

        log.debug("Health check passed. responseTimeMs={}", responseTimeMs);
        return ResponseEntity.ok(new HealthResponse(
                "UP",
                "QuipBill",
                "UP",
                Instant.now(),
                responseTimeMs,
                "Application and database are healthy"
        ));
    }
}
