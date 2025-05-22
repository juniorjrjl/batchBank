--liquibase formatted sql
--changeset junior:202505101541
--comment: insert accounts to receive money

INSERT INTO accounts
(branch_number, account_number, check_number, amount, owner_id)
VALUES
('8989', '00254', '9', 245441.31, 'dc6c4271-7088-4325-86fe-cce125648314');

--rollback DELETE FROM account WHERE owner_id = 'dc6c4271-7088-4325-86fe-cce125648314';
