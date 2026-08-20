ALTER TABLE payments
    ADD COLUMN provider_payment_id VARCHAR(255);

ALTER TABLE payments
    ADD CONSTRAINT uq_payments_provider_payment_id
        UNIQUE (provider_payment_id);