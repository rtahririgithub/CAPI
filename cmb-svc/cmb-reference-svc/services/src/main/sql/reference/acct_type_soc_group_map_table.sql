/*==============================================================*/
/* Table: ACCT_TYPE_SOC_GROUP_MAP                               */
/*==============================================================*/
CREATE TABLE ACCT_TYPE_SOC_GROUP_MAP 
(
   SOC_GROUP            VARCHAR2(9 CHAR)     NOT NULL,
   ACCT_TYPE            VARCHAR2(1 CHAR)     NOT NULL,
   ACCT_SUB_TYPE        VARCHAR2(1 CHAR)     NOT NULL,
   APPLICATION_ID       VARCHAR2(50 CHAR)    NOT NULL,
   CREATE_USER_ID       VARCHAR2(40 CHAR)    NOT NULL,
   CREATE_TS            TIMESTAMP(0)         NOT NULL,
   LAST_UPDT_USER_ID    VARCHAR2(40 CHAR)    NOT NULL,
   LAST_UPDT_TS         TIMESTAMP(0)         NOT NULL
)
TABLESPACE "TABLESPACE DATA_01";
