-- Corporate Reseller Accounts
Insert into ACCOUNT_SUB_TYPE_DEFAULTS (ACCT_TYPE,ACCT_SUB_TYPE,NL_ID,DEFAULT_DEALER,DEFAULT_SALES_CODE) 
values ('C','Z','TLS','A001000001','0000');

INSERT INTO ACCT_TYPE_NUMBER_LOCATIONS (acct_type, acct_sub_type, number_location_id, dealer, sales_code)
VALUES('C', 'Z', 'TLS','A001000001','0000');

-- Business Regular Medium Accounts
Insert into ACCOUNT_SUB_TYPE_DEFAULTS (ACCT_TYPE,ACCT_SUB_TYPE,NL_ID,DEFAULT_DEALER,DEFAULT_SALES_CODE,PRICE_PLAN_GROUP,SOC_GROUP) 
values ('B','X','TLS','A001000001','0000','PBR','SBR');

INSERT INTO ACCT_TYPE_NUMBER_LOCATIONS (acct_type, acct_sub_type, number_location_id, dealer, sales_code)
VALUES('B', 'X', 'TLS','A001000001','0000');
