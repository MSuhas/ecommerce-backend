CREATE TABLE processed_message (
                                   id BIGSERIAL PRIMARY KEY,
                                   event_id VARCHAR(255) NOT NULL,
                                   processed_at TIMESTAMP WITH TIME ZONE NOT NULL,

                                   CONSTRAINT uk_processed_message_event_id
                                       UNIQUE (event_id)
);