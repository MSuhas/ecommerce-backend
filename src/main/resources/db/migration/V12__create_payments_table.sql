CREATE TABLE payments
(
    id         BIGSERIAL PRIMARY KEY,
    order_id   BIGINT         NOT NULL,
    status     VARCHAR(30)    NOT NULL,
    amount     NUMERIC(19, 2) NOT NULL,
    created_at TIMESTAMP      NOT NULL,
    updated_at TIMESTAMP      NOT NULL,

    CONSTRAINT fk_payments_order
        FOREIGN KEY (order_id)
            REFERENCES orders (id),

    CONSTRAINT uq_payments_order
        UNIQUE (order_id)
);