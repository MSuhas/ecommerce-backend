CREATE TABLE product
(
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    price NUMERIC(10,2),
    stock INTEGER,
    category_id BIGINT NOT NULL,

    CONSTRAINT fk_product_category
        FOREIGN KEY(category_id)
            REFERENCES category(id)
);