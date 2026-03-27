package com.QuipBill_server.QuipBill.common.service;

import org.junit.jupiter.api.Test;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.jdbc.core.JdbcTemplate;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class HealthCheckServiceTest {

    @Test
    void shouldReturnTrueWhenSelectOneSucceeds() {
        JdbcTemplate jdbcTemplate = mock(JdbcTemplate.class);
        when(jdbcTemplate.execute(any(org.springframework.jdbc.core.ConnectionCallback.class))).thenReturn(true);

        HealthCheckService service = new HealthCheckService(jdbcTemplate, 2);

        assertTrue(service.isDatabaseHealthy());
    }

    @Test
    void shouldReturnFalseWhenDatabaseCheckFails() {
        JdbcTemplate jdbcTemplate = mock(JdbcTemplate.class);
        when(jdbcTemplate.execute(any(org.springframework.jdbc.core.ConnectionCallback.class)))
                .thenThrow(new DataAccessResourceFailureException("Database unavailable"));

        HealthCheckService service = new HealthCheckService(jdbcTemplate, 2);

        assertFalse(service.isDatabaseHealthy());
    }
}
