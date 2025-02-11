-- PV-410	 EXP MUST BE Y OR BLANK
INSERT INTO application_message
  VALUES (198, 'EXP MUST BE Y OR BLANK', sysdate, sysdate, user);
INSERT INTO adapted_message
  VALUES (198, 1, 'EXP MUST BE Y OR BLANK',null, null, 'EN', 3, sysdate, sysdate, user, null);
INSERT INTO adapted_message
  VALUES (198, 2, 'EXP DOIT ETRE Y OU CHAMP VIDE',null, null, 'FR', 3, sysdate, sysdate, user, null);
INSERT INTO application_error
  VALUES ( 15,'PV-410', 198, 'EXP MUST BE Y OR BLANK', sysdate, sysdate, user);

-- PV-411	CANNOT EXPEDITE WHEN WIRELESS TO WIRELESS
INSERT INTO application_message
  VALUES (199, 'CANNOT EXPEDITE WHEN WIRELESS TO WIRELESS', sysdate, sysdate, user);
INSERT INTO adapted_message
  VALUES (199, 1, 'CANNOT EXPEDITE WHEN WIRELESS TO WIRELESS',null, null, 'EN', 3, sysdate, sysdate, user, null);
INSERT INTO adapted_message
  VALUES (199, 2, 'ON NE PEUT EXPEDIER SI C''EST SANS FILE À SANS FILE',null, null, 'FR', 3, sysdate, sysdate, user, null);
INSERT INTO application_error
  VALUES ( 15,'PV-411', 199, 'CANNOT EXPEDITE WHEN WIRELESS TO WIRELESS', sysdate, sysdate, user);

-- PV-412	DSL IS PROHIBITED	
INSERT INTO application_message
  VALUES (200, 'DSL IS PROHIBITED', sysdate, sysdate, user);
INSERT INTO adapted_message
  VALUES (200, 1, 'DSL IS PROHIBITED',null, null, 'EN', 3, sysdate, sysdate, user, null);
INSERT INTO adapted_message
  VALUES (200, 2, 'DSL N''EST PAS PERMIS',null, null, 'FR', 3, sysdate, sysdate, user, null);
INSERT INTO application_error
  VALUES ( 15,'PV-412', 200, 'DSL IS PROHIBITED', sysdate, sysdate, user);

--  PV-413	DSL IS REQUIRED
INSERT INTO application_message
  VALUES (201, 'DSL IS REQUIRED', sysdate, sysdate, user);
INSERT INTO adapted_message
  VALUES (201, 1, 'DSL IS REQUIRED',null, null, 'EN', 3, sysdate, sysdate, user, null);
INSERT INTO adapted_message
  VALUES (201, 2, 'DSL EST REQUIS',null, null, 'FR', 3, sysdate, sysdate, user, null);
INSERT INTO application_error
  VALUES ( 15,'PV-413', 201, 'DSL IS REQUIRED', sysdate, sysdate, user);

-- PV-414	DSL CONTAINS INVALID VALUE	 	 
INSERT INTO application_message
  VALUES (202, 'DSL CONTAINS INVALID VALUE', sysdate, sysdate, user);
INSERT INTO adapted_message
  VALUES (202, 1, 'DSL CONTAINS INVALID VALUE',null, null, 'EN', 3, sysdate, sysdate, user, null);
INSERT INTO adapted_message
  VALUES (202, 2, 'DSL CONTIENT UNE VALEUR INVALIDE',null, null, 'FR', 3, sysdate, sysdate, user, null);
INSERT INTO application_error
  VALUES ( 15,'PV-414', 202, 'DSL CONTAINS INVALID VALUE', sysdate, sysdate, user);

--  PV-415	DSL_LNUM IS REQUIRED
INSERT INTO application_message
  VALUES (203, 'DSL_LNUM IS REQUIRED', sysdate, sysdate, user);
INSERT INTO adapted_message
  VALUES (203, 1, 'DSL_LNUM IS REQUIRED',null, null, 'EN', 3, sysdate, sysdate, user, null);
INSERT INTO adapted_message
  VALUES (203, 2, 'DSL_LNUM EST REQUIS',null, null, 'FR', 3, sysdate, sysdate, user, null);
INSERT INTO application_error
  VALUES ( 15,'PV-415', 203, 'DSL_LNUM IS REQUIRED', sysdate, sysdate, user);


UPDATE adapted_message SET message_text = 'DSL_LNUM N''EST PAS PERMIS', UPDATE_DT=SYSDATE WHERE application_message_id=191 AND adapted_message_seq=2 AND LANGUAGE_CD='FR';
UPDATE adapted_message SET message_text = 'DSL_LNUM CONTIENT UNE REFERENCE INVALIDE', UPDATE_DT=SYSDATE WHERE application_message_id=192 AND adapted_message_seq=2 AND LANGUAGE_CD='FR';
UPDATE adapted_message SET message_text = 'DSL_LNUM REFERENCE NE CONTIENT AUCUN TN', UPDATE_DT=SYSDATE WHERE application_message_id=193 AND adapted_message_seq=2 AND LANGUAGE_CD='FR';
UPDATE adapted_message SET message_text = 'EUMI N''EST PAS PERMIS', UPDATE_DT=SYSDATE WHERE application_message_id=194 AND adapted_message_seq=2 AND LANGUAGE_CD='FR';
UPDATE adapted_message SET message_text = 'EUMI EST REQUIS', UPDATE_DT=SYSDATE WHERE application_message_id=195 AND adapted_message_seq=2 AND LANGUAGE_CD='FR';
UPDATE adapted_message SET message_text = 'EUMI CONTIENT UNE VALEUR INVALIDE', UPDATE_DT=SYSDATE WHERE application_message_id=196 AND adapted_message_seq=2 AND LANGUAGE_CD='FR';
UPDATE adapted_message SET message_text = 'ORSELLNM N''EST PAS PERMIS', UPDATE_DT=SYSDATE WHERE application_message_id=197 AND adapted_message_seq=2 AND LANGUAGE_CD='FR';

COMMIT;

/*--roll back script

delete from  application_error where application_message_id >= 197 and application_message_id<203;
delete from  adapted_message where application_message_id >= 197 and application_message_id<203;
delete from  application_message where application_message_id >= 197 and application_message_id<203;


COMMIT;

*/
