INSERT INTO application_message
VALUES (90, 'TELUS -> Amp’d', sysdate, sysdate, user);
INSERT INTO adapted_message
VALUES (90, 1, 'Non-Amp’d handset to Amp’d handset swap is invalid for some applications.', null, null, 'EN', 4, sysdate, sysdate, user);
INSERT INTO adapted_message
VALUES (90, 2, 'Les échanges d’appareils non offerts par Amp’d pour des appareils Amp’d ne sont pas permis dans certaines applications.', null, null, 'FR', 4, sysdate, sysdate, user);

INSERT INTO application_message
VALUES (91, 'Amp’d -> TELUS', sysdate, sysdate, user);
INSERT INTO adapted_message
VALUES (91, 1, 'Amp’d handset to non-Amp’d handset swap is invalid for some applications.', null, null, 'EN', 4, sysdate, sysdate, user);
INSERT INTO adapted_message
VALUES (91, 2, 'Les échanges d’appareils Amp’d pour des appareils non offerts par Amp’d ne sont pas permis dans certaines applications.', null, null, 'FR', 4, sysdate, sysdate, user);

COMMIT;

