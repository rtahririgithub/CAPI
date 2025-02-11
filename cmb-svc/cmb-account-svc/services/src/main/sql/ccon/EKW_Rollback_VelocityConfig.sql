--step 1: rollback velocity config for EKW

DELETE from VELOCITY_CONFIG 
WHERE APPID = 'EKW_3';

commit;
