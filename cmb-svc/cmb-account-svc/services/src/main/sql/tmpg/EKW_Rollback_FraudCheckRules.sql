
--step 1: delete fraud rules setup for Application Koodo Webstore Checkout

DELETE from APP_TRANSTYP_FRAUD_XREF 
WHERE APP_ID = (select APP_ID from APPLICATION where APP_CD = 'EKW_3');

DELETE from APPLICATION
WHERE APP_CD = 'EKW_3';

DELETE from APPLICATION_GROUP
WHERE APP_GROUP_CD = 'EKW';

commit;