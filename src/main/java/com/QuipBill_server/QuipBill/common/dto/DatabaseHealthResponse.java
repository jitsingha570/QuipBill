package com.QuipBill_server.QuipBill.common.dto;

public record DatabaseHealthResponse(
        String status,
        String database
) {
}
