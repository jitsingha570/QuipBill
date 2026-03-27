package com.QuipBill_server.QuipBill.common.dto;

import java.time.Instant;

public record HealthResponse(
        String status,
        String application,
        String database,
        Instant timestamp,
        long responseTimeMs,
        String message
) {
}
