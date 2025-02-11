
--step 1: delete fraud rules setup for Application Mobility Sales System / Next Gen Sales Platform  (NGSP_FB) Telus Brand

DELETE from APP_TRANSTYP_FRAUD_XREF  WHERE APP_ID = (select APP_ID from APPLICATION where APP_CD = 'QAZ_1');

DELETE from APPLICATION WHERE APP_CD = 'QAZ_1';

DELETE from APPLICATION_GROUP WHERE APP_GROUP_CD = 'NGSP_FB';

commit;
