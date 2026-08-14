CREATE TABLE orders
(
    id           BIGSERIAL PRIMARY KEY,
    user_id      BIGINT         NOT NULL,
    status       VARCHAR(30)    NOT NULL,
    total_amount NUMERIC(19, 2) NOT NULL,
    created_at   TIMESTAMP      NOT NULL,
    updated_at   TIMESTAMP      NOT NULL,

    CONSTRAINT fk_orders_user
        FOREIGN KEY (user_id)
            REFERENCES users (id)
);

CREATE INDEX idx_orders_user_id
    ON orders(user_id);