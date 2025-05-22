--liquibase formatted sql
--changeset junior:202505101541
--comment: insert account owner to send money

INSERT INTO account_owners
(id, "name", cpf, owner_type)
values
('dc6c4271-7088-4325-86fe-cce125648314', 'Sergio Tavares', '47683915003', 'PJ');

--rollback DELETE FROM account_owners WHERE id = 'dc6c4271-7088-4325-86fe-cce125648314';
