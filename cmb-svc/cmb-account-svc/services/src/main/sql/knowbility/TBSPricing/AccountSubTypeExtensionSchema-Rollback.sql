DELETE FROM ACCOUNT_SUB_TYPE_DEFAULTS
WHERE acct_type = 'B' and acct_sub_type in ('A', 'N');

DELETE FROM ACCOUNT_SUB_TYPE_DEFAULTS
WHERE acct_type = 'C' and acct_sub_type = 'Y';

DELETE FROM ACCT_TYPE_NUMBER_LOCATIONS
WHERE acct_type = 'B' and acct_sub_type in ('A', 'N');

DELETE FROM ACCT_TYPE_NUMBER_LOCATIONS
WHERE acct_type = 'C' and acct_sub_type = 'Y';

COMMIT;