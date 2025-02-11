-- delete fraud rules setup for Application TELUS Financial Service (TFS)-Device Balance (TFSFB_1)
 
DELETE from APP_TRANSTYP_FRAUD_XREF 
WHERE APP_ID = (select APP_ID from APPLICATION where APP_CD = 'TFSFB_1');

DELETE from APPLICATION
WHERE APP_CD = 'TFSFB_1';

DELETE from APPLICATION_GROUP
WHERE APP_GROUP_CD = 'TFSFB';


commit;

