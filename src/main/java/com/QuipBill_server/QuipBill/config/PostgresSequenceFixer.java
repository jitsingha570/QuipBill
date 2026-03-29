package com.QuipBill_server.QuipBill.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;

@Slf4j
@Component
@RequiredArgsConstructor
public class PostgresSequenceFixer implements CommandLineRunner {

    private final JdbcTemplate jdbcTemplate;
    private final DataSource dataSource;

    @Override
    public void run(String... args) {
        if (!isPostgres()) {
            return;
        }

        try {
            syncIdentitySequence("users", "id");
            syncIdentitySequence("roles", "id");
            syncIdentitySequence("products", "product_id");
            syncIdentitySequence("bills", "id");
            syncIdentitySequence("bill_items", "id");
            syncIdentitySequence("printers", "id");
            syncIdentitySequence("print_templates", "id");
            syncIdentitySequence("daily_sales_summary", "id");
        } catch (Exception ex) {
            log.warn("Failed to synchronize PostgreSQL sequences", ex);
        }
    }

    private void syncIdentitySequence(String tableName, String columnName) {
        String sequenceName = jdbcTemplate.queryForObject(
                "SELECT pg_get_serial_sequence(?, ?)",
                String.class,
                tableName,
                columnName
        );

        if (sequenceName == null || sequenceName.isBlank()) {
            return;
        }

        Long nextValue = jdbcTemplate.queryForObject(
                "SELECT COALESCE(MAX(" + quoteIdent(columnName) + "), 0) + 1 FROM " + quoteIdent(tableName),
                Long.class
        );

        jdbcTemplate.execute(
                "SELECT setval('" + escapeLiteral(sequenceName) + "'::regclass, " + nextValue + ", false)"
        );

        log.info("Synchronized sequence {} for {}.{} to next value {}", sequenceName, tableName, columnName, nextValue);
    }

    private boolean isPostgres() {
        try (Connection connection = dataSource.getConnection()) {
            String db = connection.getMetaData().getDatabaseProductName();
            return db != null && db.toLowerCase().contains("postgres");
        } catch (Exception ex) {
            log.warn("Unable to detect database type", ex);
            return false;
        }
    }

    private String quoteIdent(String identifier) {
        return "\"" + identifier.replace("\"", "\"\"") + "\"";
    }

    private String escapeLiteral(String value) {
        return value.replace("'", "''");
    }
}
