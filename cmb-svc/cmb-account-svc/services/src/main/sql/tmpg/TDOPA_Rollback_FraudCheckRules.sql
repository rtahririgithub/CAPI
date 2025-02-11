
--step 1: delete fraud rules setup for online PrePaid Activations  Application (TDOPA_1)

DELETE from APP_TRANSTYP_FRAUD_XREF 
WHERE APP_ID = (select APP_ID from APPLICATION where APP_CD = 'TDOPA_1');

UPDATE FRAUD_CONFIG SET FRAUD_CONFIG_TXT=REPLACE(FRAUD_CONFIG_TXT, 'TDOPA,','');

DELETE from APPLICATION
WHERE APP_CD = 'TDOPA_1';

commit;


