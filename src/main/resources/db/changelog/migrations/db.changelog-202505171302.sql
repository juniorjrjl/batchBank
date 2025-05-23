--liquibase formatted sql
--changeset junior:202505171302
--comment: spring batch BATCH_JOB_EXECUTION_CONTEXT

CREATE TABLE BATCH_JOB_EXECUTION_CONTEXT  (
	JOB_EXECUTION_ID BIGINT NOT NULL PRIMARY KEY,
	SHORT_CONTEXT VARCHAR(2500) NOT NULL,
	SERIALIZED_CONTEXT TEXT ,
	constraint JOB_EXEC_CTX_FK foreign key (JOB_EXECUTION_ID)
	references BATCH_JOB_EXECUTION(JOB_EXECUTION_ID)
) ;

--rollback DROP TABLE BATCH_JOB_EXECUTION_CONTEXT;
