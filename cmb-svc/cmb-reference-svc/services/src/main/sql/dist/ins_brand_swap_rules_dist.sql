-- BRAND_SWAP_RULE
INSERT INTO BRAND_SWAP_RULE
VALUES (1, 1, 2, 'TELUS -> Amp''d', 90, sysdate, sysdate, user);

INSERT INTO BRAND_SWAP_RULE
VALUES (2, 2, 1, 'Amp''d -> TELUS', 91, sysdate, sysdate, user);

-- BRAND_SWAP_RULE_TYP
INSERT INTO BRAND_SWAP_RULE_TYP
VALUES (1, 1, '*', 'OOM', '1', sysdate, sysdate, user);

INSERT INTO BRAND_SWAP_RULE_TYP
VALUES (2, 2, '*', 'OOM', '1', sysdate, sysdate, user);

COMMIT;
