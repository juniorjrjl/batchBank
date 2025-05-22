--liquibase formatted sql
--changeset junior:202505011922
--comment: account_history table create

CREATE TABLE ACCOUNT_HISTORIES (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    amount NUMERIC NOT NULL,
    transaction_type VARCHAR NOT NULL,
    created_at TIMESTAMPTZ NOT NULL,
    description VARCHAR NOT NULL,
    account_id UUID NOT NULL,
    CONSTRAINT fk_account FOREIGN KEY (account_id) REFERENCES ACCOUNTS(id) ON DELETE CASCADE
);

--rollback DROP TABLE ACCOUNT_HISTORIES;
