-- Fix for: "duplicate key value violates unique constraint ... Key (barcode)=(...) already exists"
--
-- Goal: allow the same barcode to exist in multiple shops by enforcing uniqueness on (shop_id, barcode).
-- NOTE: Run this against your PostgreSQL database (Neon / local) with appropriate privileges.

BEGIN;

-- 1) Drop the old unique constraint (name taken from the Postgres error message)
ALTER TABLE products
    DROP CONSTRAINT IF EXISTS ukqfr8vf85k3q1xinifvsl1eynf;

-- 2) Ensure the intended composite unique constraint exists
DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1
        FROM pg_constraint c
                 JOIN pg_class t ON t.oid = c.conrelid
        WHERE t.relname = 'products'
          AND c.contype = 'u'
          AND c.conname = 'uk_products_shop_id_barcode'
    ) THEN
        ALTER TABLE products
            ADD CONSTRAINT uk_products_shop_id_barcode UNIQUE (shop_id, barcode);
    END IF;
END $$;

COMMIT;
