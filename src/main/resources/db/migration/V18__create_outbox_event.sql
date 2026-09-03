CREATE TABLE outbox_event
(
    id             BIGSERIAL PRIMARY KEY,

    event_type     VARCHAR(100)             NOT NULL,
    aggregate_type VARCHAR(100)             NOT NULL,
    aggregate_id   BIGINT                   NOT NULL,

    payload        TEXT                     NOT NULL,

    created_at     TIMESTAMP WITH TIME ZONE NOT NULL,

    published_at   TIMESTAMP WITH TIME ZONE
);