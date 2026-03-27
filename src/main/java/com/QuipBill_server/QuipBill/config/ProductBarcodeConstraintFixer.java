package com.QuipBill_server.QuipBill.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.util.List;

/**
 * Ensures barcodes are unique per shop (shop_id, barcode), not globally.
 *
 * This is needed because Hibernate `ddl-auto=update` will NOT reliably remove
 * previously-created unique constraints (e.g., UNIQUE(barcode)).
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class ProductBarcodeConstraintFixer implements CommandLineRunner {

    private static final String LEGACY_BARCODE_UK = "ukqfr8vf85k3q1xinifvsl1eynf";
    private static final String TARGET_COMPOSITE_UK = "uk_products_shop_id_barcode";

    private final JdbcTemplate jdbcTemplate;
    private final DataSource dataSource;

    @Override
    public void run(String... args) {
        if (!isPostgres()) {
            return;
        }

        try {
            // Drop a known legacy unique constraint name (seen in production errors).
            jdbcTemplate.execute(
                    "ALTER TABLE products DROP CONSTRAINT IF EXISTS " + quoteIdent(LEGACY_BARCODE_UK)
            );

            // Drop ANY remaining UNIQUE(barcode) constraints on products.
            List<String> barcodeOnlyConstraints = jdbcTemplate.queryForList("""
                    SELECT c.conname
                    FROM pg_constraint c
                             JOIN pg_class t ON t.oid = c.conrelid
                    WHERE t.relname = 'products'
                      AND c.contype = 'u'
                      AND (
                        SELECT array_agg(att.attname ORDER BY u.ordinality)
                        FROM unnest(c.conkey) WITH ORDINALITY AS u(attnum, ordinality)
                                 JOIN pg_attribute att ON att.attrelid = t.oid AND att.attnum = u.attnum
                      ) = ARRAY['barcode']
                    """, String.class);

            for (String constraintName : barcodeOnlyConstraints) {
                jdbcTemplate.execute("ALTER TABLE products DROP CONSTRAINT IF EXISTS " + quoteIdent(constraintName));
            }

            Integer compositeExists = jdbcTemplate.queryForObject("""
                    SELECT COUNT(*)
                    FROM pg_constraint c
                             JOIN pg_class t ON t.oid = c.conrelid
                    WHERE t.relname = 'products'
                      AND c.contype = 'u'
                      AND c.conname = ?
                    """, Integer.class, TARGET_COMPOSITE_UK);

            if (compositeExists != null && compositeExists == 0) {
                jdbcTemplate.execute(
                        "ALTER TABLE products ADD CONSTRAINT " + quoteIdent(TARGET_COMPOSITE_UK) + " UNIQUE (shop_id, barcode)"
                );
                log.info("Added composite unique constraint {} on products(shop_id, barcode)", TARGET_COMPOSITE_UK);
            }

        } catch (Exception e) {
            // Don't prevent the app from starting; log and continue.
            log.warn("Failed to fix products barcode unique constraints", e);
        }
    }

    private boolean isPostgres() {
        try (Connection connection = dataSource.getConnection()) {
            String db = connection.getMetaData().getDatabaseProductName();
            return db != null && db.toLowerCase().contains("postgres");
        } catch (Exception e) {
            log.warn("Unable to detect database type", e);
            return false;
        }
    }

    private String quoteIdent(String identifier) {
        if (identifier == null) {
            return "\"\"";
        }
        return "\"" + identifier.replace("\"", "\"\"") + "\"";
    }
}
