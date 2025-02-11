/************************************************************************************************
Velocity check Threshold setup for Online PrePaid Activations  Application (TDOPA_1)
/*************************************************************************************************/

--Velocity check Threshold setup for TDOPA_1
INSERT INTO VELOCITY_CONFIG
(APPID,TRANSACTIONS_PER_DAY,TRANSACTIONS_PER_MONTH,DAY_TOTAL,MONTH_TOTAL)
VALUES ('TDOPA_1',99,999,400,12400);

commit;