CREATE TABLE users
(
    id                BIGSERIAL PRIMARY KEY,

    first_name        VARCHAR(255) NOT NULL,

    last_name         VARCHAR(255) NOT NULL,

    email             VARCHAR(255) NOT NULL UNIQUE,

    password          VARCHAR(255) NOT NULL,

    phone             VARCHAR(20)  NOT NULL,

    profile_image_url VARCHAR(500),

    active            BOOLEAN      NOT NULL DEFAULT TRUE,

    role_id           BIGINT       NOT NULL,

    CONSTRAINT fk_users_roles
        FOREIGN KEY (role_id)
            REFERENCES roles (id)
);