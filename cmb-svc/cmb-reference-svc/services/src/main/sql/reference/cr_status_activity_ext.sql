CREATE TABLE STATUS_ACTIVITY_EXT
(
  STATUS_CODE           CHAR(1)            NOT NULL,
  ACTIVITY_CODE         CHAR(3)            NOT NULL,
  ACTIVITY_REASON_CODE  CHAR(4)            NOT NULL,
  STATUS_LEVEL          CHAR(1),
  PORT_OUT_ALLOWED_IND  CHAR(1)
)
TABLESPACE DATA4
PCTUSED    40
PCTFREE    10
INITRANS   1
MAXTRANS   255
STORAGE    (
            INITIAL          40K
            NEXT             40K
            MINEXTENTS       1
            MAXEXTENTS       505
            PCTINCREASE      50
            FREELISTS        1
            FREELIST GROUPS  1
            BUFFER_POOL      DEFAULT
           )
LOGGING 
NOCACHE
NOPARALLEL;


ALTER TABLE STATUS_ACTIVITY_EXT ADD (
  PRIMARY KEY (STATUS_CODE, ACTIVITY_CODE, ACTIVITY_REASON_CODE)
    USING INDEX 
    TABLESPACE DATA4
    PCTFREE    10
    INITRANS   2
    MAXTRANS   255
    STORAGE    (
                INITIAL          40K
                NEXT             40K
                MINEXTENTS       1
                MAXEXTENTS       505
                PCTINCREASE      50
                FREELISTS        1
                FREELIST GROUPS  1
               ));


