--liquibase formatted sql
--changeset junior:202505011919
--comment: account table create

CREATE TABLE ACCOUNTS (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    account_number BPCHAR(5) NOT NULL,
    branch_number BPCHAR(4) NOT NULL,
    check_number BPCHAR(1) NOT NULL,
    amount NUMERIC NOT NULL,
    owner_id UUID NOT NULL,
    CONSTRAINT fk_account_owner FOREIGN KEY (owner_id) REFERENCES ACCOUNT_OWNERS(id) ON DELETE CASCADE,
    CONSTRAINT uk_account_branch_check_numbers  UNIQUE (account_number, branch_number, check_number)
);

--rollback DROP TABLE ACCOUNTS;
