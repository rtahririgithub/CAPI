/************************************************************************************************
Rollback Script  for Online PrePaid Activations  Application (TDOPA_1)
/*************************************************************************************************/

--delete Velocity check Threshold setup for TDOPA_1

DELETE from VELOCITY_CONFIG
WHERE APPID = 'TDOPA_1';

commit;

