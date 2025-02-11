/* Rollback expired rules for Mobile Apps 9.3 Project         */
/* All rules with category code = 'NET' need to be re-enabled */

UPDATE message_rule
SET effective_end_ts = TO_TIMESTAMP ('9999-12-31 00:00:00', 'YYYY-MM-DD HH24:MI:SS'), last_updt_ts = SYSTIMESTAMP
WHERE message_rule_id IN (628, 630, 631);

COMMIT;
/