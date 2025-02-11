/*
* Notes : Change the application Id for Mobility Sales System ( NGSP) from 12373DP_1 to WSX and disable the velocity check
**/


-- step 1 Change the Application id from "12373DP_1 to WSX" for Mobility Sales System application

update application set app_cd = 'WSX_1' where app_cd = '12373DP_1';


-- step 2 disable the velocity check for WSX_1


DELETE FROM app_transtyp_fraud_xref WHERE  app_id = (SELECT app_id FROM application WHERE app_cd = 'WSX_1')
AND fraud_check_type_id = (SELECT fraud_check_type_id FROM fraud_check_type WHERE fraud_check_type_cd = 'VEL');
                                         
                                        
UPDATE FRAUD_CONFIG SET FRAUD_CONFIG_TXT=REPLACE(FRAUD_CONFIG_TXT, '12373DP,','') where  fraud_config_txt like '%12373DP%' and
fraud_check_type_id = (select fraud_check_type_id from fraud_check_type where fraud_check_type_cd = 'VEL');


-- step 3 update FRAUD_CONFIG to replace 12373DP application Id.

UPDATE FRAUD_CONFIG SET FRAUD_CONFIG_TXT=REPLACE(FRAUD_CONFIG_TXT, '12373DP','WSX') where fraud_config_txt like  '%12373DP%' ;


commit;




