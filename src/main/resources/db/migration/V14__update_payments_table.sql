ALTER TABLE payments
    ADD COLUMN provider_order_id VARCHAR(255);

ALTER TABLE payments
    ADD CONSTRAINT uq_payments_provider_order_id
        UNIQUE (provider_order_id);