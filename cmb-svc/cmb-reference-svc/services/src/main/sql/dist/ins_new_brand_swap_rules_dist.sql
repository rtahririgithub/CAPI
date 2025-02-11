-- TM_SWAP_APPL_XREF
INSERT INTO TM_SWAP_APPL_XREF
VALUES (2, 'CSOM', 'CCR', sysdate, sysdate, user);

-- BRAND_SWAP_RULE_TYP
INSERT INTO BRAND_SWAP_RULE_TYP
VALUES (3, 1, '*', 'CSOM', '1', sysdate, sysdate, user);

INSERT INTO BRAND_SWAP_RULE_TYP
VALUES (4, 2, '*', 'CSOM', '1', sysdate, sysdate, user);

COMMIT;
