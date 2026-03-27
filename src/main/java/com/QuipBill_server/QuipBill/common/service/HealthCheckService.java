package com.QuipBill_server.QuipBill.common.service;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.ConnectionCallback;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class HealthCheckService {

    private static final Logger log = LoggerFactory.getLogger(HealthCheckService.class);
    private static final String HEALTH_QUERY = "SELECT 1";

    private final JdbcTemplate jdbcTemplate;
    private final int dbQueryTimeoutSeconds;

    public HealthCheckService(
            JdbcTemplate jdbcTemplate,
            @Value("${app.health.db-query-timeout-seconds:2}") int dbQueryTimeoutSeconds
    ) {
        this.jdbcTemplate = jdbcTemplate;
        this.dbQueryTimeoutSeconds = dbQueryTimeoutSeconds;
    }

    public boolean isDatabaseHealthy() {
        long startTime = System.nanoTime();

        try {
            boolean healthy = Boolean.TRUE.equals(jdbcTemplate.execute((ConnectionCallback<Boolean>) connection -> {
                try (PreparedStatement statement = connection.prepareStatement(HEALTH_QUERY)) {
                    statement.setQueryTimeout(dbQueryTimeoutSeconds);

                    try (ResultSet resultSet = statement.executeQuery()) {
                        return resultSet.next() && resultSet.getInt(1) == 1;
                    }
                }
            }));

            long durationMs = (System.nanoTime() - startTime) / 1_000_000;
            log.debug("Database health check completed in {} ms. Healthy={}", durationMs, healthy);
            return healthy;
        } catch (Exception ex) {
            long durationMs = (System.nanoTime() - startTime) / 1_000_000;
            log.error("Database health check failed after {} ms", durationMs, ex);
            return false;
        }
    }
}
