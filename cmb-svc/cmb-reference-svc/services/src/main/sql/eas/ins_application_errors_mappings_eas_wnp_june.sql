-- PV-416   DSL_LNUM IS PROHIBITED
INSERT INTO application_message
  VALUES (191, 'DSL_LNUM IS PROHIBITED', sysdate, sysdate, user);
INSERT INTO adapted_message
  VALUES (191, 1, 'DSL_LNUM IS PROHIBITED',null, null, 'EN', 3, sysdate, sysdate, user, null);
INSERT INTO adapted_message
  VALUES (191, 2, 'DSL_LNUM N''EST PAS PERMIS',null, null, 'FR', 3, sysdate, sysdate, user, null);
INSERT INTO application_error
  VALUES ( 15,'PV-416', 191, 'DSL_LNUM IS PROHIBITED', sysdate, sysdate, user);

-- PV-417   DSL_LNUM CONTAINS INVALID REFERENCE
INSERT INTO application_message
  VALUES (192, 'DSL_LNUM CONTAINS INVALID REFERENCE', sysdate, sysdate, user);
INSERT INTO adapted_message
  VALUES (192, 1, 'DSL_LNUM CONTAINS INVALID REFERENCE',null, null, 'EN', 3, sysdate, sysdate, user, null);
INSERT INTO adapted_message
  VALUES (192, 2, 'DSL_LNUM CONTIENT UNE REFERENCE INVALIDE',null, null, 'FR', 3, sysdate, sysdate, user, null);
INSERT INTO application_error
  VALUES ( 15,'PV-417', 192, 'DSL_LNUM CONTAINS INVALID REFERENCE', sysdate, sysdate, user);

-- PV-418   DSL_LNUM REFERENCE DOS NOT CANTAINS A SINGLE TN
INSERT INTO application_message
  VALUES (193, 'DSL_LNUM REFERENCE DOS NOT CANTAINS A SINGLE TN', sysdate, sysdate, user);
INSERT INTO adapted_message
  VALUES (193, 1, 'DSL_LNUM REFERENCE DOS NOT CANTAINS A SINGLE TN',null, null, 'EN', 3, sysdate, sysdate, user, null);
INSERT INTO adapted_message
  VALUES (193, 2, 'DSL_LNUM REFERENCE NE CONTIENT AUCUN TN',null, null, 'FR', 3, sysdate, sysdate, user, null);
INSERT INTO application_error
  VALUES ( 15,'PV-418', 193, 'DSL_LNUM REFERENCE DOS NOT CANTAINS A SINGLE TN', sysdate, sysdate, user);

-- PV-419   EUMI IS PROHIBITED
INSERT INTO application_message
  VALUES (194, 'EUMI IS PROHIBITED', sysdate, sysdate, user);
INSERT INTO adapted_message
  VALUES (194, 1, 'EUMI IS PROHIBITED',null, null, 'EN', 3, sysdate, sysdate, user, null);
INSERT INTO adapted_message
  VALUES (194, 2, 'EUMI N''EST PAS PERMIS',null, null, 'FR', 3, sysdate, sysdate, user, null);
INSERT INTO application_error
  VALUES ( 15,'PV-419', 194, 'EUMI IS PROHIBITED', sysdate, sysdate, user);

-- PV-420   EUMI IS REQUIRED
INSERT INTO application_message
  VALUES (195, 'EUMI IS REQUIRED', sysdate, sysdate, user);
INSERT INTO adapted_message
  VALUES (195, 1, 'EUMI IS REQUIRED',null, null, 'EN', 3, sysdate, sysdate, user, null);
INSERT INTO adapted_message
  VALUES (195, 2, 'EUMI EST REQUIS',null, null, 'FR', 3, sysdate, sysdate, user, null);
INSERT INTO application_error
  VALUES ( 15,'PV-420', 195, 'EUMI IS REQUIRED', sysdate, sysdate, user);

-- PV-421   EUMI CONTAINS INVALID VALUE
INSERT INTO application_message
  VALUES (196, 'EUMI CONTAINS INVALID VALUE', sysdate, sysdate, user);
INSERT INTO adapted_message
  VALUES (196, 1, 'EUMI CONTAINS INVALID VALUE',null, null, 'EN', 3, sysdate, sysdate, user, null);
INSERT INTO adapted_message
  VALUES (196, 2, 'EUMI CONTIENT UNE VALEUR INVALIDE',null, null, 'FR', 3, sysdate, sysdate, user, null);
INSERT INTO application_error
  VALUES ( 15,'PV-421', 196, 'EUMI CONTAINS INVALID VALUE', sysdate, sysdate, user);

-- PV-422   ORSELLNM IS PROHIBITED
INSERT INTO application_message
  VALUES (197, 'ORSELLNM IS PROHIBITED', sysdate, sysdate, user);
INSERT INTO adapted_message
  VALUES (197, 1, 'ORSELLNM IS PROHIBITED',null, null, 'EN', 3, sysdate, sysdate, user, null);
INSERT INTO adapted_message
  VALUES (197, 2, 'ORSELLNM N''EST PAS PERMIS',null, null, 'FR', 3, sysdate, sysdate, user, null);
INSERT INTO application_error
  VALUES ( 15,'PV-422', 197, 'ORSELLNM IS PROHIBITED', sysdate, sysdate, user);


COMMIT;

/*--roll back script

delete from  application_error where application_message_id >= 191 and application_message_id<198;
delete from adapted_message where application_message_id >= 191 and application_message_id<198;
delete from  application_message where application_message_id >= 191 and application_message_id<198;


COMMIT;

*/
