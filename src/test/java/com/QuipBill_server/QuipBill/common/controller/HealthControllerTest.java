package com.QuipBill_server.QuipBill.common.controller;

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
    void shouldReturnOkWhenApplicationAndDatabaseAreHealthy() {
        HealthCheckService healthCheckService = mock(HealthCheckService.class);
        when(healthCheckService.isDatabaseHealthy()).thenReturn(true);

        HealthController controller = new HealthController(healthCheckService);
        ResponseEntity<HealthResponse> response = controller.health();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("UP", response.getBody().status());
        assertEquals("UP", response.getBody().database());
        assertEquals("QuipBill", response.getBody().application());
    }

    @Test
    void shouldReturnInternalServerErrorWhenDatabaseIsDown() {
        HealthCheckService healthCheckService = mock(HealthCheckService.class);
        when(healthCheckService.isDatabaseHealthy()).thenReturn(false);

        HealthController controller = new HealthController(healthCheckService);
        ResponseEntity<HealthResponse> response = controller.health();

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("DOWN", response.getBody().status());
        assertEquals("DOWN", response.getBody().database());
        assertEquals("QuipBill", response.getBody().application());
    }
}
