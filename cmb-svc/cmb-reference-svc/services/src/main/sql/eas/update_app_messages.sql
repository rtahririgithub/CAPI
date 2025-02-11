-- defect fixes: message type id changes

-- defect # prod00116352
UPDATE ADAPTED_MESSAGE SET MESSAGE_TEXT = 'Important notice: Mobile VPN service can only be added to business and corporate subscribers and a minimum of 20 subscribers is required to subscribe for the first time to the Mobile VPN service. All subscribers must have either Premium Business Support 5 (for business accounts) or Premium Corporate Support 3 (for corporate accounts) in order to activate the Mobile VPN service.  Please add the appropriate Premium Business Support feature with the Mobile VPN service if necessary.' WHERE application_message_id = 204 AND adapted_message_seq = 1 AND language_cd = 'EN';

-- defect fixes: updated messages

UPDATE ADAPTED_MESSAGE SET MESSAGE_TEXT = 'Appels locaux - Interconnexion: Le forfait de fusion de minutes a �t� ajout� au compte de cet abonn�.' WHERE application_message_id = 208 AND adapted_message_seq IN (2, 4) AND language_cd = 'FR';
UPDATE ADAPTED_MESSAGE SET MESSAGE_TEXT = 'Appels locaux - Interconnexion: Le forfait de fusion de minutes a �t� ajout� � votre compte.' WHERE application_message_id = 208 AND adapted_message_seq = 6 AND language_cd = 'FR';

UPDATE ADAPTED_MESSAGE SET MESSAGE_TEXT = 'Appels locaux - Interconnexion: Pour b�n�ficier du forfait de fusion de minutes, vous devez ajouter d�autres abonn�s au compte qui seront autoris�s � partager le lot de minutes.' WHERE application_message_id = 209 AND adapted_message_seq IN (2, 4, 6, 8) AND language_cd = 'FR';

UPDATE ADAPTED_MESSAGE SET MESSAGE_TEXT = 'Appels locaux - Interconnexion: Pour b�n�ficier du forfait de fusion de minutes, vous devez ajouter d�autres abonn�s qui seront autoris�s � partager le lot de minutes.' WHERE application_message_id = 210 AND adapted_message_seq = 2 AND language_cd = 'FR';
  
UPDATE ADAPTED_MESSAGE SET MESSAGE_TEXT = 'Appels locaux - Interconnexion: Cet abonn� b�n�ficie maintenant du forfait de fusion de minutes avec les autres abonn�s inscrits � ce compte.' WHERE application_message_id = 211 AND adapted_message_seq IN (2, 4) AND language_cd = 'FR';
UPDATE ADAPTED_MESSAGE SET MESSAGE_TEXT = 'Appels locaux - Interconnexion: Vous b�n�ficiez maintenant du forfait de fusion de minutes avec les autres abonn�s inscrits � ce compte.' WHERE application_message_id = 211 AND adapted_message_seq IN (6, 8) AND language_cd = 'FR';
  
UPDATE ADAPTED_MESSAGE SET MESSAGE_TEXT = 'Appels locaux - Interconnexion: Le partage de minutes sera d�sactiv�. Cet abonn� ne sera plus en mesure de partager le lot de minutes avec d�autres abonn�s inscrits au compte.' WHERE application_message_id = 212 AND adapted_message_seq IN (2, 4, 6) AND language_cd = 'FR';
  
UPDATE ADAPTED_MESSAGE SET MESSAGE_TEXT = 'Appels locaux - Interconnexion: En raison de ce changement, il ne reste qu�un seul abonn� inscrit au compte. Il ne sera pas en mesure de partager le lot de minutes avec d�autres abonn�s.' WHERE application_message_id = 213 AND adapted_message_seq = 2 AND language_cd = 'FR';
  
UPDATE ADAPTED_MESSAGE SET MESSAGE_TEXT = 'Appels locaux - Interconnexion: L�activation ne peut �tre effectu�e. Pour qu�un forfait de 0 minute soit ajout� au compte, il est n�cessaire de changer au moins l�un des forfaits inscrits pour un forfait contribuant au lot de minutes.' WHERE application_message_id = 214 AND adapted_message_seq = 2 AND language_cd = 'FR';

UPDATE ADAPTED_MESSAGE SET MESSAGE_TEXT = 'Interurbains - Interconnexion: Le forfait de fusion de minutes a �t� ajout� au compte de cet abonn�.' WHERE application_message_id = 215 AND adapted_message_seq IN (2, 4) AND language_cd = 'FR';
UPDATE ADAPTED_MESSAGE SET MESSAGE_TEXT = 'Interurbains - Interconnexion: Le forfait de fusion de minutes a �t� ajout� � votre compte.' WHERE application_message_id = 215 AND adapted_message_seq = 6 AND language_cd = 'FR';
  
UPDATE ADAPTED_MESSAGE SET MESSAGE_TEXT = 'Interurbains - Interconnexion: Pour b�n�ficier du forfait de fusion de minutes, vous devez ajouter d�autres abonn�s au compte qui seront autoris�s � partager le lot de minutes.' WHERE application_message_id = 216 AND adapted_message_seq IN (2, 4, 6, 8) AND language_cd = 'FR';

UPDATE ADAPTED_MESSAGE SET MESSAGE_TEXT = 'Interurbains - Interconnexion: Pour b�n�ficier du forfait de fusion de minutes, vous devez ajouter d�autres abonn�s qui seront autoris�s � partager le lot de minutes.' WHERE application_message_id = 217 AND adapted_message_seq = 2 AND language_cd = 'FR';

UPDATE ADAPTED_MESSAGE SET MESSAGE_TEXT = 'Interurbains - Interconnexion: Cet abonn� b�n�ficie maintenant du forfait de fusion de minutes avec les autres abonn�s inscrits � ce compte.' WHERE application_message_id = 218 AND adapted_message_seq IN (2, 4) AND language_cd = 'FR';
UPDATE ADAPTED_MESSAGE SET MESSAGE_TEXT = 'Interurbains - Interconnexion: Vous b�n�ficiez maintenant du forfait de fusion de minutes avec les autres abonn�s inscrits � ce compte.' WHERE application_message_id = 218 AND adapted_message_seq IN (6, 8) AND language_cd = 'FR';
  
UPDATE ADAPTED_MESSAGE SET MESSAGE_TEXT = 'Interurbains - Interconnexion: Le partage de minutes sera d�sactiv�. Cet abonn� ne sera plus en mesure de partager le lot de minutes avec d�autres abonn�s inscrits au compte.' WHERE application_message_id = 219 AND adapted_message_seq IN (2, 4) AND language_cd = 'FR';
UPDATE ADAPTED_MESSAGE SET MESSAGE_TEXT = 'Interurbains - Interconnexion: Le partage de minutes sera d�sactiv�. Vous ne serez plus en mesure de partager le lot de minutes avec d�autres abonn�s inscrits au compte.' WHERE application_message_id = 219 AND adapted_message_seq = 6 AND language_cd = 'FR';

UPDATE ADAPTED_MESSAGE SET MESSAGE_TEXT = 'Interurbains - Interconnexion: En raison de ce changement, il ne reste qu�un seul abonn� inscrit au compte. Il ne sera pas en mesure de partager le lot de minutes avec d�autres abonn�s.' WHERE application_message_id = 220 AND adapted_message_seq = 2 AND language_cd = 'FR';
  
UPDATE ADAPTED_MESSAGE SET MESSAGE_TEXT = 'Appels locaux - Contact Direct: Le forfait de fusion de minutes a �t� ajout� au compte de cet abonn�.' WHERE application_message_id = 221 AND adapted_message_seq IN (2, 4) AND language_cd = 'FR';
UPDATE ADAPTED_MESSAGE SET MESSAGE_TEXT = 'Appels locaux - Contact Direct: Le forfait de fusion de minutes a �t� ajout� � votre compte.' WHERE application_message_id = 221 AND adapted_message_seq = 6 AND language_cd = 'FR';
  
UPDATE ADAPTED_MESSAGE SET MESSAGE_TEXT = 'Appels locaux - Contact Direct: Pour b�n�ficier du forfait de fusion de minutes, vous devez ajouter d�autres abonn�s au compte qui seront autoris�s � partager le lot de minutes.' WHERE application_message_id = 222 AND adapted_message_seq IN (2, 4, 6, 8) AND language_cd = 'FR';
  
UPDATE ADAPTED_MESSAGE SET MESSAGE_TEXT = 'Appels locaux - Contact Direct: Pour b�n�ficier du forfait de fusion de minutes, vous devez ajouter d�autres abonn�s qui seront autoris�s � partager le lot de minutes.' WHERE application_message_id = 223 AND adapted_message_seq = 2 AND language_cd = 'FR';
  
UPDATE ADAPTED_MESSAGE SET MESSAGE_TEXT = 'Appels locaux - Contact Direct: Cet abonn� b�n�ficie maintenant du forfait de fusion de minutes avec les autres abonn�s inscrits � ce compte.' WHERE application_message_id = 224 AND adapted_message_seq IN (2, 4) AND language_cd = 'FR';
UPDATE ADAPTED_MESSAGE SET MESSAGE_TEXT = 'Appels locaux - Contact Direct: Vous b�n�ficiez maintenant du forfait de fusion de minutes avec les autres abonn�s inscrits � ce compte.' WHERE application_message_id = 224 AND adapted_message_seq IN (6, 8) AND language_cd = 'FR';

UPDATE ADAPTED_MESSAGE SET MESSAGE_TEXT = 'Appels locaux - Contact Direct: Le partage de minutes sera d�sactiv�. Cet abonn� ne sera plus en mesure de partager le lot de minutes avec d�autres abonn�s inscrits au compte.' WHERE application_message_id = 225 AND adapted_message_seq IN (2, 4) AND language_cd = 'FR';
UPDATE ADAPTED_MESSAGE SET MESSAGE_TEXT = 'Appels locaux - Contact Direct: Vous b�n�ficiez maintenant du forfait de fusion de minutes avec les autres abonn�s inscrits � ce compte.' WHERE application_message_id = 225 AND adapted_message_seq = 6 AND language_cd = 'FR';

UPDATE ADAPTED_MESSAGE SET MESSAGE_TEXT = 'Appels locaux - Contact Direct: En raison de ce changement, il ne reste qu�un seul abonn� inscrit au compte. Il ne sera pas en mesure de partager le lot de minutes avec d�autres abonn�s.' WHERE application_message_id = 226 AND adapted_message_seq = 2 AND language_cd = 'FR';
  
UPDATE ADAPTED_MESSAGE SET MESSAGE_TEXT = 'Appels en itin�rance - Interconnexion: Le forfait de fusion de minutes a �t� ajout� au compte de cet abonn�.' WHERE application_message_id = 227 AND adapted_message_seq IN (2, 4) AND language_cd = 'FR';
UPDATE ADAPTED_MESSAGE SET MESSAGE_TEXT = 'Appels en itin�rance - Interconnexion: Le forfait de fusion de minutes a �t� ajout� � votre compte.' WHERE application_message_id = 227 AND adapted_message_seq = 6 AND language_cd = 'FR';
  
UPDATE ADAPTED_MESSAGE SET MESSAGE_TEXT = 'Appels en itin�rance - Interconnexion: Pour b�n�ficier du forfait de fusion de minutes, vous devez ajouter d�autres abonn�s au compte qui seront autoris�s � partager le lot de minutes.' WHERE application_message_id = 228 AND adapted_message_seq IN (2, 4, 6, 8) AND language_cd = 'FR';

UPDATE ADAPTED_MESSAGE SET MESSAGE_TEXT = 'Appels en itin�rance - Interconnexion: Pour b�n�ficier du forfait de fusion de minutes, vous devez ajouter d�autres abonn�s qui seront autoris�s � partager le lot de minutes.' WHERE application_message_id = 229 AND adapted_message_seq = 2 AND language_cd = 'FR';
  
UPDATE ADAPTED_MESSAGE SET MESSAGE_TEXT = 'Appels en itin�rance - Interconnexion: Cet abonn� b�n�ficie maintenant du forfait de fusion de minutes avec les autres abonn�s inscrits � ce compte.' WHERE application_message_id = 230 AND adapted_message_seq IN (2, 4) AND language_cd = 'FR';
UPDATE ADAPTED_MESSAGE SET MESSAGE_TEXT = 'Appels en itin�rance - Interconnexion: Vous b�n�ficiez maintenant du forfait de fusion de minutes avec les autres abonn�s inscrits � ce compte.' WHERE application_message_id = 230 AND adapted_message_seq IN (6, 8) AND language_cd = 'FR';
  
UPDATE ADAPTED_MESSAGE SET MESSAGE_TEXT = 'Appels en itin�rance - Interconnexion: Le partage de minutes sera d�sactiv�. Cet abonn� ne sera plus en mesure de partager le lot de minutes avec d�autres abonn�s inscrits au compte.' WHERE application_message_id = 231 AND adapted_message_seq IN (2, 4) AND language_cd = 'FR';
UPDATE ADAPTED_MESSAGE SET MESSAGE_TEXT = 'Appels en itin�rance - Interconnexion: Le partage de minutes sera d�sactiv�. Vous ne serez plus en mesure de partager le lot de minutes avec d�autres abonn�s inscrits au compte.' WHERE application_message_id = 231 AND adapted_message_seq = 6 AND language_cd = 'FR';
  
UPDATE ADAPTED_MESSAGE SET MESSAGE_TEXT = 'Appels en itin�rance - Interconnexion: En raison de ce changement, il ne reste qu�un seul abonn� inscrit au compte. Il ne sera pas en mesure de partager le lot de minutes avec d�autres abonn�s.' WHERE application_message_id = 232 AND adapted_message_seq = 2 AND language_cd = 'FR';
  
UPDATE ADAPTED_MESSAGE SET MESSAGE_TEXT = 'Appels interurbains en itin�rance - Interconnexion: Le forfait de fusion de minutes a �t� ajout� au compte de cet abonn�.' WHERE application_message_id = 233 AND adapted_message_seq IN (2, 4) AND language_cd = 'FR';
UPDATE ADAPTED_MESSAGE SET MESSAGE_TEXT = 'Appels interurbains en itin�rance - Interconnexion: Le forfait de fusion de minutes a �t� ajout� � votre compte.' WHERE application_message_id = 233 AND adapted_message_seq = 6 AND language_cd = 'FR';

UPDATE ADAPTED_MESSAGE SET MESSAGE_TEXT = 'Appels interurbains en itin�rance - Interconnexion: Pour b�n�ficier du forfait de fusion de minutes, vous devez ajouter d�autres abonn�s au compte qui seront autoris�s � partager le lot de minutes.' WHERE application_message_id = 234 AND adapted_message_seq IN (2, 4, 6, 8) AND language_cd = 'FR';

UPDATE ADAPTED_MESSAGE SET MESSAGE_TEXT = 'Appels interurbains en itin�rance - Interconnexion: Pour b�n�ficier du forfait de fusion de minutes, vous devez ajouter d�autres abonn�s qui seront autoris�s � partager le lot de minutes.' WHERE application_message_id = 235 AND adapted_message_seq = 2 AND language_cd = 'FR';
 
UPDATE ADAPTED_MESSAGE SET MESSAGE_TEXT = 'Appels interurbains en itin�rance - Interconnexion: Cet abonn� b�n�ficie maintenant du forfait de fusion de minutes avec les autres abonn�s inscrits � ce compte.' WHERE application_message_id = 236 AND adapted_message_seq IN (2, 4) AND language_cd = 'FR';
UPDATE ADAPTED_MESSAGE SET MESSAGE_TEXT = 'Appels interurbains en itin�rance - Interconnexion: Vous b�n�ficiez maintenant du forfait de fusion de minutes avec les autres abonn�s inscrits � ce compte.' WHERE application_message_id = 236 AND adapted_message_seq IN (6, 8) AND language_cd = 'FR';
  
UPDATE ADAPTED_MESSAGE SET MESSAGE_TEXT = 'Appels interurbains en itin�rance - Interconnexion: Le partage de minutes sera d�sactiv�. Cet abonn� ne sera plus en mesure de partager le lot de minutes avec d�autres abonn�s inscrits au compte.' WHERE application_message_id = 237 AND adapted_message_seq IN (2, 4) AND language_cd = 'FR';
UPDATE ADAPTED_MESSAGE SET MESSAGE_TEXT = 'Appels interurbains en itin�rance - Interconnexion: Le partage de minutes sera d�sactiv�. Vous ne serez plus en mesure de partager le lot de minutes avec d�autres abonn�s inscrits au compte.' WHERE application_message_id = 237 AND adapted_message_seq = 6 AND language_cd = 'FR';
  
UPDATE ADAPTED_MESSAGE SET MESSAGE_TEXT = 'Appels interurbains en itin�rance - Interconnexion: En raison de ce changement, il ne reste qu�un seul abonn� inscrit au compte. Il ne sera pas en mesure de partager le lot de minutes avec d�autres abonn�s.' WHERE application_message_id = 238 AND adapted_message_seq = 2 AND language_cd = 'FR';
  
UPDATE ADAPTED_MESSAGE SET MESSAGE_TEXT = 'Interurbains - Contact Direct: Le forfait de fusion de minutes a �t� ajout� au compte de cet abonn�.' WHERE application_message_id = 239 AND adapted_message_seq IN (2, 4) AND language_cd = 'FR';
UPDATE ADAPTED_MESSAGE SET MESSAGE_TEXT = 'Interurbains - Contact Direct: Le forfait de fusion de minutes a �t� ajout� � votre compte.' WHERE application_message_id = 239 AND adapted_message_seq = 6 AND language_cd = 'FR';
  
UPDATE ADAPTED_MESSAGE SET MESSAGE_TEXT = 'Interurbains - Contact Direct: Pour b�n�ficier du forfait de fusion de minutes, vous devez ajouter d�autres abonn�s au compte qui seront autoris�s � partager le lot de minutes.' WHERE application_message_id = 240 AND adapted_message_seq IN (2, 4, 6, 8) AND language_cd = 'FR';
  
UPDATE ADAPTED_MESSAGE SET MESSAGE_TEXT = 'Interurbains - Contact Direct: Pour b�n�ficier du forfait de fusion de minutes, vous devez ajouter d�autres abonn�s qui seront autoris�s � partager le lot de minutes.' WHERE application_message_id = 241 AND adapted_message_seq = 2 AND language_cd = 'FR';
  
UPDATE ADAPTED_MESSAGE SET MESSAGE_TEXT = 'Interurbains - Contact Direct: Cet abonn� b�n�ficie maintenant du forfait de fusion de minutes avec les autres abonn�s inscrits � ce compte.' WHERE application_message_id = 242 AND adapted_message_seq IN (2, 4) AND language_cd = 'FR';
UPDATE ADAPTED_MESSAGE SET MESSAGE_TEXT = 'Interurbains - Contact Direct: Vous b�n�ficiez maintenant du forfait de fusion de minutes avec les autres abonn�s inscrits � ce compte.' WHERE application_message_id = 242 AND adapted_message_seq IN (6, 8) AND language_cd = 'FR';
 
UPDATE ADAPTED_MESSAGE SET MESSAGE_TEXT = 'Interurbains - Contact Direct: Le partage de minutes sera d�sactiv�. Cet abonn� ne sera plus en mesure de partager le lot de minutes avec d�autres abonn�s inscrits au compte.' WHERE application_message_id = 243 AND adapted_message_seq IN (2, 4) AND language_cd = 'FR';
UPDATE ADAPTED_MESSAGE SET MESSAGE_TEXT = 'Interurbains - Contact Direct: Le partage de minutes sera d�sactiv�. Vous ne serez plus en mesure de partager le lot de minutes avec d�autres abonn�s inscrits au compte.' WHERE application_message_id = 243 AND adapted_message_seq = 6 AND language_cd = 'FR';
  
UPDATE ADAPTED_MESSAGE SET MESSAGE_TEXT = 'Interurbains - Contact Direct: En raison de ce changement, il ne reste qu�un seul abonn� inscrit au compte. Il ne sera pas en mesure de partager le lot de minutes avec d�autres abonn�s.' WHERE application_message_id = 244 AND adapted_message_seq = 2 AND language_cd = 'FR';

UPDATE ADAPTED_MESSAGE SET MESSAGE_TEXT = 'Appels locaux - Interconnexion: Le forfait de fusion de minutes a �t� ajout� au compte de cet abonn�.  Appels locaux - Contact Direct: Le forfait de fusion de minutes a �t� ajout� au compte de cet abonn�.' WHERE application_message_id = 245 AND adapted_message_seq IN (2, 4) AND language_cd = 'FR';
UPDATE ADAPTED_MESSAGE SET MESSAGE_TEXT = 'Appels locaux - Interconnexion: Le forfait de fusion de minutes a �t� ajout� � votre compte.  Appels locaux - Contact Direct: Le forfait de fusion de minutes a �t� ajout� � votre compte.' WHERE application_message_id = 245 AND adapted_message_seq = 6 AND language_cd = 'FR';

UPDATE ADAPTED_MESSAGE SET MESSAGE_TEXT = 'Appels locaux - Interconnexion: Pour b�n�ficier du forfait de fusion de minutes, vous devez ajouter d�autres abonn�s au compte qui seront autoris�s � partager le lot de minutes.  Appels locaux - Contact Direct: Pour b�n�ficier du forfait de fusion de minutes, vous devez ajouter d�autres abonn�s au compte qui seront autoris�s � partager le lot de minutes.' WHERE application_message_id = 246 AND adapted_message_seq IN (2, 4, 6, 8) AND language_cd = 'FR';

UPDATE ADAPTED_MESSAGE SET MESSAGE_TEXT = 'Appels locaux - Interconnexion: Pour b�n�ficier du forfait de fusion de minutes, vous devez ajouter d�autres abonn�s qui seront autoris�s � partager le lot de minutes.  Appels locaux - Contact Direct: Pour b�n�ficier du forfait de fusion de minutes, vous devez ajouter d�autres abonn�s qui seront autoris�s � partager le lot de minutes.' WHERE application_message_id = 247 AND adapted_message_seq = 2 AND language_cd = 'FR';

UPDATE ADAPTED_MESSAGE SET MESSAGE_TEXT = 'Appels locaux - Interconnexion: Cet abonn� b�n�ficie maintenant du forfait de fusion de minutes avec les autres abonn�s inscrits � ce compte.  Appels locaux - Contact Direct: Cet abonn� b�n�ficie maintenant du forfait de fusion de minutes avec les autres abonn�s inscrits � ce compte.' WHERE application_message_id = 248 AND adapted_message_seq IN (2, 4) AND language_cd = 'FR';
UPDATE ADAPTED_MESSAGE SET MESSAGE_TEXT = 'Appels locaux - Interconnexion: Vous b�n�ficiez maintenant du forfait de fusion de minutes avec les autres abonn�s inscrits � ce compte.  Appels locaux - Contact Direct: Vous b�n�ficiez maintenant du forfait de fusion de minutes avec les autres abonn�s inscrits � ce compte.' WHERE application_message_id = 248 AND adapted_message_seq IN (6, 8) AND language_cd = 'FR';

UPDATE ADAPTED_MESSAGE SET MESSAGE_TEXT = 'Appels locaux - Interconnexion: Le partage de minutes sera d�sactiv�. Cet abonn� ne sera plus en mesure de partager le lot de minutes avec d�autres abonn�s inscrits au compte.  Appels locaux - Contact Direct: Le partage de minutes sera d�sactiv�. Cet abonn� ne sera plus en mesure de partager le lot de minutes avec d�autres abonn�s inscrits au compte.' WHERE application_message_id = 249 AND adapted_message_seq IN (2, 4) AND language_cd = 'FR';
UPDATE ADAPTED_MESSAGE SET MESSAGE_TEXT = 'Appels locaux - Interconnexion: Le partage de minutes sera d�sactiv�. Vous ne serez plus en mesure de partager le lot de minutes avec d�autres abonn�s inscrits au compte.  Appels locaux - Contact Direct: Le partage de minutes sera d�sactiv�. Vous ne serez plus en mesure de partager le lot de minutes avec d�autres abonn�s inscrits au compte.' WHERE application_message_id = 249 AND adapted_message_seq = 6 AND language_cd = 'FR';

UPDATE ADAPTED_MESSAGE SET MESSAGE_TEXT = 'Appels locaux - Interconnexion: En raison de ce changement, il ne reste qu�un seul abonn� inscrit au compte. Il ne sera pas en mesure de partager le lot de minutes avec d�autres abonn�s.  Appels locaux - Contact Direct: En raison de ce changement, il ne reste qu�un seul abonn� inscrit au compte. Il ne sera pas en mesure de partager le lot de minutes avec d�autres abonn�s.' WHERE application_message_id = 250 AND adapted_message_seq = 2 AND language_cd = 'FR';

UPDATE ADAPTED_MESSAGE SET MESSAGE_TEXT = 'Interurbains - Interconnexion: Le forfait de fusion de minutes a �t� ajout� au compte de cet abonn�.  Interurbains - Contact Direct: Le forfait de fusion de minutes a �t� ajout� au compte de cet abonn�.' WHERE application_message_id = 251 AND adapted_message_seq IN (2, 4) AND language_cd = 'FR';
UPDATE ADAPTED_MESSAGE SET MESSAGE_TEXT = 'Interurbains - Interconnexion: Le forfait de fusion de minutes a �t� ajout� � votre compte.  Interurbains - Contact Direct: Le forfait de fusion de minutes a �t� ajout� � votre compte.' WHERE application_message_id = 251 AND adapted_message_seq = 6 AND language_cd = 'FR';

UPDATE ADAPTED_MESSAGE SET MESSAGE_TEXT = 'Interurbains - Interconnexion: Pour b�n�ficier du forfait de fusion de minutes, vous devez ajouter d�autres abonn�s au compte qui seront autoris�s � partager le lot de minutes.  Interurbains - Contact Direct: Pour b�n�ficier du forfait de fusion de minutes, vous devez ajouter d�autres abonn�s au compte qui seront autoris�s � partager le lot de minutes.' WHERE application_message_id = 252 AND adapted_message_seq IN (2, 4, 6, 8) AND language_cd = 'FR';

UPDATE ADAPTED_MESSAGE SET MESSAGE_TEXT = 'Interurbains - Interconnexion: Pour b�n�ficier du forfait de fusion de minutes, vous devez ajouter d�autres abonn�s qui seront autoris�s � partager le lot de minutes.  Interurbains - Contact Direct: Pour b�n�ficier du forfait de fusion de minutes, vous devez ajouter d�autres abonn�s qui seront autoris�s � partager le lot de minutes.' WHERE application_message_id = 253 AND adapted_message_seq = 2 AND language_cd = 'FR';

UPDATE ADAPTED_MESSAGE SET MESSAGE_TEXT = 'Interurbains - Interconnexion: Cet abonn� b�n�ficie maintenant du forfait de fusion de minutes avec les autres abonn�s inscrits � ce compte.  Interurbains - Contact Direct: Cet abonn� b�n�ficie maintenant du forfait de fusion de minutes avec les autres abonn�s inscrits � ce compte.' WHERE application_message_id = 254 AND adapted_message_seq IN (2, 4) AND language_cd = 'FR';
UPDATE ADAPTED_MESSAGE SET MESSAGE_TEXT = 'Interurbains - Interconnexion: Vous b�n�ficiez maintenant du forfait de fusion de minutes avec les autres abonn�s inscrits � ce compte.  Interurbains - Contact Direct: Vous b�n�ficiez maintenant du forfait de fusion de minutes avec les autres abonn�s inscrits � ce compte.' WHERE application_message_id = 254 AND adapted_message_seq IN (6, 8) AND language_cd = 'FR';

UPDATE ADAPTED_MESSAGE SET MESSAGE_TEXT = 'Interurbains - Interconnexion: Le partage de minutes sera d�sactiv�. Cet abonn� ne sera plus en mesure de partager le lot de minutes avec d�autres abonn�s inscrits au compte.  Interurbains - Contact Direct: Le partage de minutes sera d�sactiv�. Cet abonn� ne sera plus en mesure de partager le lot de minutes avec d�autres abonn�s inscrits au compte.' WHERE application_message_id = 255 AND adapted_message_seq IN (2, 4) AND language_cd = 'FR';
UPDATE ADAPTED_MESSAGE SET MESSAGE_TEXT = 'Interurbains - Interconnexion: Le partage de minutes sera d�sactiv�. Vous ne serez plus en mesure de partager le lot de minutes avec d�autres abonn�s inscrits au compte.  Interurbains - Contact Direct: Le partage de minutes sera d�sactiv�. Vous ne serez plus en mesure de partager le lot de minutes avec d�autres abonn�s inscrits au compte.' WHERE application_message_id = 255 AND adapted_message_seq = 6 AND language_cd = 'FR';

UPDATE ADAPTED_MESSAGE SET MESSAGE_TEXT = 'Interurbains - Interconnexion: En raison de ce changement, il ne reste qu�un seul abonn� inscrit au compte. Il ne sera pas en mesure de partager le lot de minutes avec d�autres abonn�s.  Interurbains - Contact Direct: En raison de ce changement, il ne reste qu�un seul abonn� inscrit au compte. Il ne sera pas en mesure de partager le lot de minutes avec d�autres abonn�s.' WHERE application_message_id = 256 AND adapted_message_seq = 2 AND language_cd = 'FR';


-- defect fixes: new messages

INSERT INTO ADAPTED_MESSAGE(APPLICATION_MESSAGE_ID, ADAPTED_MESSAGE_SEQ, MESSAGE_TEXT, APPLICATION_ID, AUDIENCE_TYPE_ID, LANGUAGE_CD, MESSAGE_TYPE_ID, LOAD_DT, UPDATE_DT, USER_LAST_MODIFY, BRAND_ID)  VALUES ( 262, 7, 'A free SMS notification may be sent to all orphaned 0-minute pool plan subscribers. Select the �send sms notification� checkbox to send a system-generated message to these subscribers.', 2, null, 'EN', 5, sysdate, sysdate, user, null);
INSERT INTO ADAPTED_MESSAGE(APPLICATION_MESSAGE_ID, ADAPTED_MESSAGE_SEQ, MESSAGE_TEXT, APPLICATION_ID, AUDIENCE_TYPE_ID, LANGUAGE_CD, MESSAGE_TYPE_ID, LOAD_DT, UPDATE_DT, USER_LAST_MODIFY, BRAND_ID)  VALUES ( 262, 8, 'Il est possible d�envoyer un avis gratuit par message texte � tous les abonn�s restants d�un forfait avec fusion de 0 minute. Cochez la case ��envoyer le message texte d�avis�� pour que le syst�me envoie un message � ces abonn�s.', 2, null, 'FR', 5, sysdate, sysdate, user, null);

COMMIT;