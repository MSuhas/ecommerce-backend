CREATE TABLE order_saga
(
    id         BIGSERIAL PRIMARY KEY,
    saga_id    VARCHAR(100)             NOT NULL UNIQUE,
    order_id   BIGINT,
    product_id BIGINT                   NOT NULL,
    quantity   INTEGER                  NOT NULL,
    status     VARCHAR(50)              NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL
);