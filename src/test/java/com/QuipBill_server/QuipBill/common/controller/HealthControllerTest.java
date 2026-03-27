package com.QuipBill_server.QuipBill.common.controller;

import com.QuipBill_server.QuipBill.common.dto.DatabaseHealthResponse;
import com.QuipBill_server.QuipBill.common.dto.HealthResponse;
import com.QuipBill_server.QuipBill.common.service.HealthCheckService;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class HealthControllerTest {

    @Test
    void shouldReturnOkForLivenessWithoutCheckingDatabase() {
        HealthCheckService healthCheckService = mock(HealthCheckService.class);

        HealthController controller = new HealthController(healthCheckService);
        ResponseEntity<HealthResponse> response = controller.health();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("UP", response.getBody().status());
        assertEquals("RUNNING", response.getBody().service());
    }

    @Test
    void shouldReturnUpWhenDatabaseIsHealthy() {
        HealthCheckService healthCheckService = mock(HealthCheckService.class);
        when(healthCheckService.isDatabaseHealthy()).thenReturn(true);

        HealthController controller = new HealthController(healthCheckService);
        ResponseEntity<DatabaseHealthResponse> response = controller.databaseHealth();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("UP", response.getBody().status());
        assertEquals("UP", response.getBody().database());
    }

    @Test
    void shouldReturnUpWithDatabaseDownWhenDatabaseIsUnavailable() {
        HealthCheckService healthCheckService = mock(HealthCheckService.class);
        when(healthCheckService.isDatabaseHealthy()).thenReturn(false);

        HealthController controller = new HealthController(healthCheckService);
        ResponseEntity<DatabaseHealthResponse> response = controller.databaseHealth();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("UP", response.getBody().status());
        assertEquals("DOWN", response.getBody().database());
    }

    @Test
    void shouldReturnUpWithDatabaseDownWhenDatabaseCheckThrowsException() {
        HealthCheckService healthCheckService = mock(HealthCheckService.class);
        when(healthCheckService.isDatabaseHealthy()).thenThrow(new RuntimeException("DB not ready"));

        HealthController controller = new HealthController(healthCheckService);
        ResponseEntity<DatabaseHealthResponse> response = controller.databaseHealth();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("UP", response.getBody().status());
        assertEquals("DOWN", response.getBody().database());
    }
}
