/************************************************************************************************
Velocity check Threshold setup for TERM2.
/*************************************************************************************************/

--Velocity check Threshold setup for TERM2
INSERT INTO VELOCITY_CONFIG
(APPID,TRANSACTIONS_PER_DAY,TRANSACTIONS_PER_MONTH,DAY_TOTAL,MONTH_TOTAL)
VALUES ('TERM2',0,0,400,0);

commit;