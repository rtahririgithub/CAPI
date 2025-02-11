/************************************************************************************************
Rollback Script  for Velocity check Threshold setup for TFSFB_1
/*************************************************************************************************/

--delete Velocity check Threshold setup for TFSFB_1

DELETE from VELOCITY_CONFIG
WHERE APPID = 'TFSFB_1';

commit;

