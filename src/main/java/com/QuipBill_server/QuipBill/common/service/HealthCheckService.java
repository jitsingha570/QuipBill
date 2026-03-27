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
    private final int dbCheckRetries;
    private final long retryDelayMs;

    public HealthCheckService(
            JdbcTemplate jdbcTemplate,
            @Value("${app.health.db-query-timeout-seconds:2}") int dbQueryTimeoutSeconds,
            @Value("${app.health.db-check-retries:2}") int dbCheckRetries,
            @Value("${app.health.db-retry-delay-ms:250}") long retryDelayMs
    ) {
        this.jdbcTemplate = jdbcTemplate;
        this.dbQueryTimeoutSeconds = dbQueryTimeoutSeconds;
        this.dbCheckRetries = Math.max(1, dbCheckRetries);
        this.retryDelayMs = Math.max(0L, retryDelayMs);
    }

    public boolean isDatabaseHealthy() {
        long startTime = System.nanoTime();

        for (int attempt = 1; attempt <= dbCheckRetries; attempt++) {
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
                log.debug(
                        "Database health check completed in {} ms on attempt {}. Healthy={}",
                        durationMs,
                        attempt,
                        healthy
                );
                return healthy;
            } catch (Exception ex) {
                long durationMs = (System.nanoTime() - startTime) / 1_000_000;
                log.warn(
                        "Database health check attempt {} of {} failed after {} ms",
                        attempt,
                        dbCheckRetries,
                        durationMs,
                        ex
                );

                if (attempt < dbCheckRetries) {
                    sleepBeforeRetry();
                }
            }
        }

        long durationMs = (System.nanoTime() - startTime) / 1_000_000;
        log.error("Database health check failed after {} ms and {} attempts", durationMs, dbCheckRetries);
        return false;
    }

    private void sleepBeforeRetry() {
        if (retryDelayMs == 0) {
            return;
        }

        try {
            Thread.sleep(retryDelayMs);
        } catch (InterruptedException interruptedException) {
            Thread.currentThread().interrupt();
            log.warn("Database health check retry sleep was interrupted.");
        }
    }
}
