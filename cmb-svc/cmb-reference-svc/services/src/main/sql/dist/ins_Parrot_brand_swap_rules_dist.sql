-- TM_SWAP_APPL_XREF
INSERT INTO TM_SWAP_APPL_XREF
VALUES (3, 'SWAPTRACK', '*', sysdate, sysdate, user);


-- BRAND_SWAP_RULE
INSERT INTO BRAND_SWAP_RULE
VALUES (3, 1, 3, 'TELUS -> Koodo', 154, sysdate, sysdate, user);

INSERT INTO BRAND_SWAP_RULE
VALUES (4, 3, 1, 'Koodo -> TELUS', 155, sysdate, sysdate, user);

INSERT INTO BRAND_SWAP_RULE
VALUES (5, 2, 3, 'Amp''d -> Koodo', 154, sysdate, sysdate, user);

INSERT INTO BRAND_SWAP_RULE
VALUES (6, 3, 2, 'Koodo -> Amp''d', 155, sysdate, sysdate, user);


-- BRAND_SWAP_RULE_TYP
INSERT INTO BRAND_SWAP_RULE_TYP
VALUES (5, 3, '*', 'SWAPTRACK', '0', sysdate, sysdate, user);

INSERT INTO BRAND_SWAP_RULE_TYP
VALUES (6, 4, '*', 'SWAPTRACK', '0', sysdate, sysdate, user);

INSERT INTO BRAND_SWAP_RULE_TYP
VALUES (7, 5, '*', 'SWAPTRACK', '0', sysdate, sysdate, user);

INSERT INTO BRAND_SWAP_RULE_TYP
VALUES (8, 6, '*', 'SWAPTRACK', '0', sysdate, sysdate, user);

COMMIT;

