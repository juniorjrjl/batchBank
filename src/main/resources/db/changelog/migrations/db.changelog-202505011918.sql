--liquibase formatted sql
--changeset junior:202505011918
--comment: account_owner table create

CREATE TABLE ACCOUNT_OWNERS (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name VARCHAR NOT NULL,
    owner_type VARCHAR NOT NULL,
    cpf VARCHAR NOT NULL UNIQUE
);

--rollback DROP TABLE ACCOUNT_OWNERS;
