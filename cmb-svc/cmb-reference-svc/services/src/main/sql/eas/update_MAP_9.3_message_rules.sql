/* Expire rules for Mobile Apps 9.3 Project                   */
/* All rules with category code = 'NET' need to be surpressed */

UPDATE message_rule
SET effective_end_ts = SYSTIMESTAMP, last_updt_ts = SYSTIMESTAMP
WHERE message_rule_id IN (628, 630, 631);

COMMIT;
/