INSERT INTO application_message
VALUES (154, 'Non-Koodo -> Koodo', sysdate, sysdate, user);
INSERT INTO adapted_message
VALUES (154, 1, 'Non-Koodo handset to Koodo handset swap is invalid for some applications.', null, null, 'EN', 4, sysdate, sysdate, user, null);
INSERT INTO adapted_message
VALUES (154, 2, 'Les échanges d''appareils non offerts par Koodo pour des appareils Koodo ne sont pas permis dans certaines applications.', null, null, 'FR', 4, sysdate, sysdate, user, null);

INSERT INTO application_message
VALUES (155, 'Koodo -> Non-Koodo', sysdate, sysdate, user);
INSERT INTO adapted_message
VALUES (155, 1, 'Koodo handset to non-Koodo handset swap is invalid for some applications.', null, null, 'EN', 4, sysdate, sysdate, user, null);
INSERT INTO adapted_message
VALUES (155, 2, 'Les échanges d''appareils Koodo pour des appareils non offerts par Koodo ne sont pas permis dans certaines applications.', null, null, 'FR', 4, sysdate, sysdate, user, null);

INSERT INTO application_message
VALUES (156, 'PORT_IN_ELIGIBILITY.', sysdate, sysdate, user);
INSERT INTO adapted_message
VALUES (156, 1, 'You''ve already submitted a request to move your number. Unfortunately, your new request can''t be processed.', null, null, 'EN', 3, sysdate, sysdate, user, null);
INSERT INTO adapted_message
VALUES (156, 2, 'Vous avez déjà soumis une demande de transfert de votre numéro. Malheureusement, votre nouvelle demande ne peut pas être traitée.', null, null, 'FR', 3, sysdate, sysdate, user, null);
INSERT INTO application_error
VALUES (15, 'E9A', 156, 'PORT_IN_ELIGIBILITY.', sysdate, sysdate, user);

INSERT INTO application_message
VALUES (157, 'PORT_IN_ELIGIBILITY.', sysdate, sysdate, user);
INSERT INTO adapted_message
VALUES (157, 1, 'Porting not supported.', null, null, 'EN', 3, sysdate, sysdate, user, null);
INSERT INTO adapted_message
VALUES (157, 2, 'Demande de transfert n''est pas possible.', null, null, 'FR', 3, sysdate, sysdate, user, null);
INSERT INTO application_error
VALUES (15, 'E10', 157, 'PORT_IN_ELIGIBILITY.', sysdate, sysdate, user);

COMMIT;