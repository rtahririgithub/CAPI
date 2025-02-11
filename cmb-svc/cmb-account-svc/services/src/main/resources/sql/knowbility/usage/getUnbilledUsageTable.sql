SELECT au_issue_code
FROM cycle_control c
JOIN billing_account ba
ON ba.bill_cycle = c.cycle_code
JOIN logical_date ld
ON ld.logical_date BETWEEN cycle_start_date AND c.cycle_close_date
WHERE ban                 = :ban
AND (ld.logical_date_type = 'O' )