INSERT INTO message_type
  VALUES (5, 'Void', sysdate, sysdate, user);
  
INSERT INTO application
  VALUES (0, 'Default','DEFAULT', sysdate, sysdate, user);  


-- MAP-ADD-NM   Adding NetMotion Soc

INSERT INTO application_message
  VALUES (204, 'Adding Mobile VPN SOC', sysdate, sysdate, user);
INSERT INTO adapted_message
  VALUES (204, 1, 'default english message',0, null, 'EN', 3, sysdate, sysdate, user, null);
INSERT INTO adapted_message
  VALUES (204, 2, 'default french message',0, null, 'FR', 3, sysdate, sysdate, user, null); 
INSERT INTO adapted_message
  VALUES (204, 3, 'default english message',8, null, 'EN', 5, sysdate, sysdate, user, null);
INSERT INTO adapted_message
  VALUES (204, 4, 'default french message',8, null, 'FR', 5, sysdate, sysdate, user, null);
INSERT INTO application_error
  VALUES ( 0,'MAP-ADD-NM', 204, 'Adding Mobile VPN SOC', sysdate, sysdate, user);
  
-- real messages to replace template values  
UPDATE  adapted_message SET 
message_text='Important notice: Mobile VPN service can only be added to business and corporate subscribers and a minimum of 20 subscribers is required to subscribe for the first time to the Mobile VPN service. All subscribers must have either Premium Business Support 5 (for business accounts) or Premium Corporate Support 3 (for corporate accounts) in order to activate the Mobile VPN service.  Please add the appropriate Premium Business Support feature with the Mobile VPN service if necessary.'
  WHERE application_message_id=204 AND application_id=0 AND language_cd='EN';  
UPDATE  adapted_message SET 
message_text='Avis important : Le service RPV (réseau privé virtuel) sans fil peut être ajouté au compte des clients d''affaires et corporatifs seulement et l''abonnement initial à ce service nécessite un nombre minimal de 20 abonnés. Tous les clients doivent être abonnés aux services Soutien opérationnel supérieur 5 (comptes d''affaires) ou Soutien supérieur aux clients corporatifs 3 (comptes corporatifs) afin de profiter du service RPV sans fil. Veuillez ajouter au besoin une option du service Soutien opérationnel supérieur au service RPV sans fil.'
  WHERE application_message_id=204 AND application_id=0 AND language_cd='FR';  

-- real messages to replace template values for Apollo
UPDATE  adapted_message SET message_text='Only eligible to business accounts. A minimum of 20 subscribers on this account must subscribe to both Mobile VPN service and Premium Business Support 5 during the initial activation.'
  WHERE application_message_id=204 AND application_id=8 AND language_cd='EN';  
UPDATE  adapted_message SET message_text='Offre réservée aux clients d''affaires. Au moins 20 personnes inscrites à ce compte doivent choisir à la fois le service RPV sans fil et Soutien opérationnel supérieur 5 au moment de l''abonnement initial.'
  WHERE application_message_id=204 AND application_id=8 AND language_cd='FR';  


-- MAP-REM-NM   Removing NetMotion Soc

INSERT INTO application_message
  VALUES (205, 'Removing Mobile VPN SOC', sysdate, sysdate, user);
INSERT INTO adapted_message
  VALUES (205, 1, 'default english message', 0, null, 'EN', 3, sysdate, sysdate, user, null);
INSERT INTO adapted_message
  VALUES (205, 2, 'default french message', 0, null, 'FR', 3, sysdate, sysdate, user, null);
  INSERT INTO adapted_message
  VALUES (205, 3, 'default english message', 8, null, 'EN', 5, sysdate, sysdate, user, null);
INSERT INTO adapted_message
  VALUES (205, 4, 'default french message', 8, null, 'FR', 5, sysdate, sysdate, user, null);
INSERT INTO application_error
  VALUES ( 0,'MAP-REM-NM', 205, 'Removing Mobile VPN SOC', sysdate, sysdate, user);
  
-- real messages to replace template values  
UPDATE  adapted_message SET message_text='Do not remove Premium Business Support 5 (for business accounts) and Premium Corporate Support 3 (for corporate accounts) when removing the Mobile VPN service from an account, unless otherwise instructed by the subscriber.'
  WHERE application_message_id=205 AND language_cd='EN';  
UPDATE  adapted_message SET message_text='N''annulez pas les services Soutien opérationnel supérieur 5 (comptes d''affaires) et Soutien supérieur aux clients corporatifs 3 (comptes corporatifs) auxquels le client est abonné lorsque vous retirez le service RPV sans fil de son compte, à moins que ce dernier ne vous ait demandé de le faire.'
  WHERE application_message_id=205 AND language_cd='FR';  
 
  
-- MAP-ADD-WA   Adding WorkAlone Soc

INSERT INTO application_message
  VALUES (206, 'Adding WorkAlone SOC', sysdate, sysdate, user);
INSERT INTO adapted_message
  VALUES (206, 1, 'default english message', 0, null, 'EN', 3, sysdate, sysdate, user, null);
INSERT INTO adapted_message
  VALUES (206, 2, 'default french message', 0, null, 'FR', 3, sysdate, sysdate, user, null);
  INSERT INTO adapted_message
  VALUES (206, 3, 'default english message', 8, null, 'EN', 5, sysdate, sysdate, user, null);
INSERT INTO adapted_message
  VALUES (206, 4, 'default french message', 8, null, 'FR', 5, sysdate, sysdate, user, null);
INSERT INTO application_error
  VALUES ( 0,'MAP-ADD-WA', 206, 'Adding WorkAlone SOC', sysdate, sysdate, user);
  
-- real messages to replace template values  
UPDATE  adapted_message SET 
message_text='TELUS Integrated Alert and Assist features are only available to Business "B" BANS and Corporate "C" BANS. Consumer "I" BAN regular customers do not qualify for this product for legal, liability and publicity reasons.  Please remove this feature if it has been added to a consumer or corporate individual account.'
  WHERE application_message_id=206 AND language_cd='EN';  
UPDATE  adapted_message SET
message_text='Les services d''avis et d''aide intégrés de TELUS sont offerts aux clients d''affaires (comptes de type "B")  et corporatifs (comptes de type "C") seulement. Les clients qui ont un compte consommateur régulier (de type "I") ne peuvent pas bénéficier de ce produit pour des raisons juridiques, de responsabilité et de publicité. Si ce service a été inscrit au compte consommateur ou corporatif individuel d''un client, veuillez le retirer.'
  WHERE application_message_id=206 AND language_cd='FR';


-- WLNP E11 Code Addition 

INSERT INTO application_message
  VALUES (207, 'PORT_IN_ELIGIBILITY.', sysdate, sysdate, user);
INSERT INTO adapted_message
  VALUES (207, 1, 'We are not able to satisfy this request at this time. Please contact a Koodo rep and reference error message MTK.', 0, null, 'EN', 1, sysdate, sysdate, user, 3);
INSERT INTO adapted_message
  VALUES (207, 2, 'Il nous est impossible de satisfaire à votre demande pour le moment. Veuillez communiquer avec un représentant Koodo et lui indiquer le message d''erreur MTK.',0, null, 'FR', 1, sysdate, sysdate, user, 3); 
INSERT INTO application_error VALUES (15, 'E11', 207, 'PORT_IN_ELIGIBILITY.', sysdate, sysdate, user);

COMMIT;