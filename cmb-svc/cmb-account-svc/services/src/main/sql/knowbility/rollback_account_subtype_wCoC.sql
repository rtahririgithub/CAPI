-- Corporate Reseller Accounts
delete from ACCOUNT_SUB_TYPE_DEFAULTS
where
ACCT_TYPE = 'C' and ACCT_SUB_TYPE = 'Z';

delete from ACCT_TYPE_NUMBER_LOCATIONS 
where 
acct_type = 'C' and acct_sub_type = 'Z';

-- Business Regular Medium Accounts
delete from ACCOUNT_SUB_TYPE_DEFAULTS
where
ACCT_TYPE = 'B' and ACCT_SUB_TYPE = 'X';

delete from ACCT_TYPE_NUMBER_LOCATIONS 
where 
acct_type = 'B' and acct_sub_type = 'X';
