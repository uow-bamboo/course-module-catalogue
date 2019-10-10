CREATE TABLE objectcache
(
    KEY         varchar(100) NOT NULL,
    OBJECTDATA  bytea,
    CREATEDDATE TIMESTAMP(6),
    CONSTRAINT OBJECTCACHE_PK PRIMARY KEY (KEY)
);

CREATE TABLE AUDIT_EVENT
(
    ID             uuid      NOT NULL,
    EVENT_DATE_UTC timestamp NOT NULL,
    OPERATION      text      NOT NULL,
    USERCODE       text,
    DATA           text,
    TARGET_ID      uuid      NOT NULL,
    TARGET_TYPE    text      NOT NULL,
    CONSTRAINT AUDIT_EVENT_PK PRIMARY KEY (ID)
);

CREATE INDEX IDX_AUDIT_EVENT_FK ON AUDIT_EVENT (TARGET_ID, TARGET_TYPE);
CREATE INDEX IDX_AUDIT_EVENT_USEROP ON AUDIT_EVENT (OPERATION, USERCODE);
