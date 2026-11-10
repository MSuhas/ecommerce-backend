ALTER TABLE orders
    ADD COLUMN saga_id VARCHAR(100);

ALTER TABLE orders
    ADD CONSTRAINT uk_orders_saga_id UNIQUE (saga_id);