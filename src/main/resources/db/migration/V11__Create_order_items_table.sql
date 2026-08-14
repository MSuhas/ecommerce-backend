CREATE TABLE order_items
(
    id           BIGSERIAL PRIMARY KEY,
    order_id     BIGINT         NOT NULL,
    product_id   BIGINT         NOT NULL,
    product_name VARCHAR(255)   NOT NULL,
    unit_price   NUMERIC(19, 2) NOT NULL,
    quantity     INTEGER        NOT NULL,
    subtotal     NUMERIC(19, 2) NOT NULL,

    CONSTRAINT fk_order_items_order
        FOREIGN KEY (order_id)
            REFERENCES orders (id),

            CONSTRAINT chk_order_item_quantity
            CHECK (quantity > 0)
);

CREATE INDEX idx_order_items_order_id
    ON order_items (order_id);