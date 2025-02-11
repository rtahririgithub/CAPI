-- ROLLBACK Script

-- ************** GROUP_ROLE rows being deleted
-- 3 Rows should get deleted
DELETE FROM GROUP_ROLE WHERE GROUP_ID IN (select GROUP_ID from TGROUP where GROUP_CD = 'CSRDESKTOP');

-- ************** ROLE table rows to be deleted now
-- 3 Rows should get deleted
DELETE FROM ROLE WHERE ROLE_CD in ('EEAGENT_SD','EEAGENT_OOM', 'EEAGENT_MERCURY');


-- ************* TGROUP table rows to be deleted now
-- 1 row will be deleted
DELETE FROM TGROUP where group_cd = 'CSRDESKTOP';
and application_nm = 'TBS Pricing';


COMMIT;
