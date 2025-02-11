/************************************************************************************************
Rollback Script  for Velocity check Threshold setup for TERM2
/*************************************************************************************************/

--delete Velocity check Threshold setup for TERM2
DELETE from VELOCITY_CONFIG
WHERE APPID = 'TERM2';


commit;

