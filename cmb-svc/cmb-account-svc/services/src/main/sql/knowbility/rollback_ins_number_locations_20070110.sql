DELETE FROM ACCT_TYPE_NUMBER_LOCATIONS
WHERE acct_type = 'C'
and acct_sub_type in ('O', 'C', 'U', 'V');

DELETE FROM ACCOUNT_SUB_TYPE_DEFAULTS
WHERE acct_type = 'C'
and acct_sub_type in ('O', 'C', 'U', 'V');
