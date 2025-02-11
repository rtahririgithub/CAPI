INSERT INTO application_message
VALUES (158, 'INTER-BRAND_PORT', sysdate, sysdate, user);
INSERT INTO adapted_message
VALUES (158, 1, 'Unable to cancel source subscriber.', null, null, 'EN', 4, sysdate, sysdate, user, null);

INSERT INTO application_message
VALUES (159, 'INTER-BRAND_PORT', sysdate, sysdate, user);
INSERT INTO adapted_message
VALUES (159, 1, 'Unable to reserve phone number on target subscriber.', null, null, 'EN', 4, sysdate, sysdate, user, null);

INSERT INTO application_message
VALUES (160, 'INTER-BRAND_PORT', sysdate, sysdate, user);
INSERT INTO adapted_message
VALUES (160, 1, 'Unable to activate phone number on target subscriber.', null, null, 'EN', 4, sysdate, sysdate, user, null);

INSERT INTO application_message
VALUES (161, 'INTER-BRAND_PORT', sysdate, sysdate, user);
INSERT INTO adapted_message
VALUES (161, 1, 'Unable to resume subscriber on source account (rollback step).', null, null, 'EN', 4, sysdate, sysdate, user, null);

INSERT INTO application_message
VALUES (162, 'INTER-BRAND_PORT', sysdate, sysdate, user);
INSERT INTO adapted_message
VALUES (162, 1, 'Unable to release phone number on target account (rollback step).', null, null, 'EN', 4, sysdate, sysdate, user, null);

INSERT INTO application_message
VALUES (163, 'INTER-BRAND_PORT', sysdate, sysdate, user);
INSERT INTO adapted_message
VALUES (163, 1, 'Unable to retrieve source subscriber.', null, null, 'EN', 4, sysdate, sysdate, user, null);

INSERT INTO application_message
VALUES (164, 'INTER-BRAND_PORT', sysdate, sysdate, user);
INSERT INTO adapted_message
VALUES (164, 1, 'Unable to resume target subscriber.', null, null, 'EN', 4, sysdate, sysdate, user, null);

INSERT INTO application_message
VALUES (165, 'INTER-BRAND_PORT', sysdate, sysdate, user);
INSERT INTO adapted_message
VALUES (165, 1, 'Unable to change phone number to target subscriber.', null, null, 'EN', 4, sysdate, sysdate, user, null);

INSERT INTO application_message
VALUES (166, 'INTER-BRAND_PORT', sysdate, sysdate, user);
INSERT INTO adapted_message
VALUES (166, 1, 'Invalid inter-brand port activity reason code.', null, null, 'EN', 4, sysdate, sysdate, user, null);

INSERT INTO application_message
VALUES (167, 'PORT_OUT_ELIGIBILITY', sysdate, sysdate, user);
INSERT INTO adapted_message
VALUES (167, 1, 'Phone number has a transfer block set to blocked and is not eligible to port out.', null, null, 'EN', 4, sysdate, sysdate, user, null);
INSERT INTO adapted_message
VALUES (167, 2, 'Le numéro de téléphone fait l''objet d''un blocage de transfert et il ne peut pas être transféré.', null, null, 'FR', 4, sysdate, sysdate, user, null);

INSERT INTO application_message
VALUES (168, 'PORT_OUT_ELIGIBILITY', sysdate, sysdate, user);
INSERT INTO adapted_message
VALUES (168, 1, 'Your carrier rejected your port request for the following reason: Incorrect Account number provided.', null, null, 'EN', 4, sysdate, sysdate, user, null);
INSERT INTO adapted_message
VALUES (168, 2, 'Votre fournisseur a rejeté votre demande de transfert pour la raison suivante: No de compte requis ou incorrect.', null, null, 'FR', 4, sysdate, sysdate, user, null);

INSERT INTO application_message
VALUES (169, 'PORT_OUT_ELIGIBILITY', sysdate, sysdate, user);
INSERT INTO adapted_message
VALUES (169, 1, 'Your carrier rejected your port request for the following reason: Incorrect ESN provided.', null, null, 'EN', 4, sysdate, sysdate, user, null);
INSERT INTO adapted_message
VALUES (169, 2, 'Votre fournisseur a rejeté votre demande de transfert pour la raison suivante: NSE/MEID incorrect.', null, null, 'FR', 4, sysdate, sysdate, user, null);

INSERT INTO application_message
VALUES (170, 'PORT_OUT_ELIGIBILITY', sysdate, sysdate, user);
INSERT INTO adapted_message
VALUES (170, 1, 'Your carrier rejected your port request for the following reason: Incorrect PIN provided.', null, null, 'EN', 4, sysdate, sysdate, user, null);
INSERT INTO adapted_message
VALUES (170, 2, 'Votre fournisseur a rejeté votre demande de transfert pour la raison suivante: mot de passe/NIP incorrect.', null, null, 'FR', 4, sysdate, sysdate, user, null);

UPDATE adapted_message
SET message_text = 'Please provide at least one piece of information from your Old Service Provider (Account #, Password/PIN or Equipment Serial Number).'
WHERE application_message_id = 139
AND language_cd = 'EN';

COMMIT;