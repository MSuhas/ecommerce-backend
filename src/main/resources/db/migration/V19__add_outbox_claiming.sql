ALTER TABLE outbox_event
    ADD COLUMN status VARCHAR(20) NOT NULL DEFAULT 'PENDING';
ALTER TABLE outbox_event
    ADD COLUMN claimed_at TIMESTAMP WITH TIME ZONE;
CREATE INDEX idx_outbox_pending
    ON outbox_event (status, claimed_at, id);