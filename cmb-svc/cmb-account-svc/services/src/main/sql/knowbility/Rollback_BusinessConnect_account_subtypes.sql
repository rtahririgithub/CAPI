-- delete Business Connect Regular Accounts and personal accounts defaults

DELETE FROM ACCOUNT_SUB_TYPE_DEFAULTS
WHERE acct_type = 'B' and acct_sub_type in ('F', 'G');

DELETE FROM ACCT_TYPE_NUMBER_LOCATIONS
WHERE acct_type = 'B' and acct_sub_type in ('F', 'G');

COMMIT;