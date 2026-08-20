CREATE TABLE payment_webhook_events
(
    id          BIGSERIAL PRIMARY KEY,
    event_id    VARCHAR(255) NOT NULL,
    provider    VARCHAR(50)  NOT NULL,
    received_at TIMESTAMP    NOT NULL,

    CONSTRAINT uq_payment_webhook_event_id
        UNIQUE (event_id)
);