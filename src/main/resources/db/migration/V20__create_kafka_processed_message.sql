CREATE TABLE kafka_processed_message
(
    id           BIGSERIAL PRIMARY KEY,
    consumer_group VARCHAR(100) NOT NULL,
    event_id     VARCHAR(100)             NOT NULL,
    processed_at TIMESTAMP WITH TIME ZONE NOT NULL,

    CONSTRAINT uk_kafka_processed_message_event_id
        UNIQUE (consumer_group, event_id)
);