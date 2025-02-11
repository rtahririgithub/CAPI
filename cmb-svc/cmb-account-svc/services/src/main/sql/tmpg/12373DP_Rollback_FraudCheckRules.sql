
--step 1: delete fraud rules setup for Mobility Sales System (12373DP)

DELETE from APP_TRANSTYP_FRAUD_XREF 
WHERE APP_ID = (select APP_ID from APPLICATION where APP_CD = '12373DP_1');

DELETE from APPLICATION
WHERE APP_CD = '12373DP_1';

DELETE from APPLICATION_GROUP
WHERE APP_GROUP_CD = 'NGSP';

commit;