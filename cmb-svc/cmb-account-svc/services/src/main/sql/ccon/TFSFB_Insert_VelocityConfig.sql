/************************************************************************************************
Velocity check Threshold setup for TELUS Financial Services (TFS) – Finance Device Balance (TFSFB_1)
/*************************************************************************************************/

--Velocity check Threshold setup for TFSFB_1
INSERT INTO VELOCITY_CONFIG
(APPID,TRANSACTIONS_PER_DAY,TRANSACTIONS_PER_MONTH,DAY_TOTAL,MONTH_TOTAL)
VALUES ('TFSFB_1',2,10,500,500);

commit;