DELETE FROM adapted_message where application_message_id  >= 92 and application_message_id <= 94;
DELETE FROM adapted_message where application_message_id  >= 96 and application_message_id <= 100;
DELETE FROM adapted_message where application_message_id in (156, 157, 207);

Insert into ADAPTED_MESSAGE
   (APPLICATION_MESSAGE_ID, ADAPTED_MESSAGE_SEQ, MESSAGE_TEXT, LANGUAGE_CD, MESSAGE_TYPE_ID, LOAD_DT, UPDATE_DT, USER_LAST_MODIFY)
 Values
   (92, 1, 'No, this number is not eligible to be transferred to TELUS because it is currently not transferrable. Client can call their current service provider for details or take a new number to activate with TELUS.', 'EN', 3, TO_DATE('11/14/2007 12:30:51', 'MM/DD/YYYY HH24:MI:SS'), TO_DATE('11/14/2007 12:30:51', 'MM/DD/YYYY HH24:MI:SS'), 'EASADM');
Insert into ADAPTED_MESSAGE
   (APPLICATION_MESSAGE_ID, ADAPTED_MESSAGE_SEQ, MESSAGE_TEXT, LANGUAGE_CD, MESSAGE_TYPE_ID, LOAD_DT, UPDATE_DT, USER_LAST_MODIFY)
 Values
   (92, 2, 'Non, ce numéro ne peut pas être transféré à TELUS parce que Il ne peut être transféré présentement. Le client peut appeler son fournisseur de services actuel pour obtenir plus de détails ou choisir un nouveau numéro de téléphone  pour mettre son appareil en service à TELUS.', 'FR', 3, TO_DATE('11/14/2007 12:30:51', 'MM/DD/YYYY HH24:MI:SS'), TO_DATE('11/14/2007 12:30:51', 'MM/DD/YYYY HH24:MI:SS'), 'EASADM');
Insert into ADAPTED_MESSAGE
   (APPLICATION_MESSAGE_ID, ADAPTED_MESSAGE_SEQ, MESSAGE_TEXT, LANGUAGE_CD, MESSAGE_TYPE_ID, LOAD_DT, UPDATE_DT, USER_LAST_MODIFY, BRAND_ID)
 Values
   (92, 3, 'No, this number isn''t eligible to move to Koodo Mobile because it is currently not transferrable. Customer can call their current service provider for details or take a new number to activate with Koodo Mobile.', 'EN', 3, TO_DATE('10/12/2007 13:07:51', 'MM/DD/YYYY HH24:MI:SS'), TO_DATE('10/12/2007 13:07:51', 'MM/DD/YYYY HH24:MI:SS'), 'EASADM', 3);
Insert into ADAPTED_MESSAGE
   (APPLICATION_MESSAGE_ID, ADAPTED_MESSAGE_SEQ, MESSAGE_TEXT, LANGUAGE_CD, MESSAGE_TYPE_ID, LOAD_DT, UPDATE_DT, USER_LAST_MODIFY, BRAND_ID)
 Values
   (92, 4, 'Non, ce numéro ne peut pas être transféré chez Koodo Mobile parce que Le client peut appeler son fournisseur de services actuel pour obtenir plus de détails ou choisir un nouveau numéro de téléphone pour activer un téléphone chez Koodo Mobile.', 'FR', 3, TO_DATE('10/12/2007 13:07:51', 'MM/DD/YYYY HH24:MI:SS'), TO_DATE('10/12/2007 13:07:51', 'MM/DD/YYYY HH24:MI:SS'), 'EASADM', 3);
Insert into ADAPTED_MESSAGE
   (APPLICATION_MESSAGE_ID, ADAPTED_MESSAGE_SEQ, MESSAGE_TEXT, LANGUAGE_CD, MESSAGE_TYPE_ID, LOAD_DT, UPDATE_DT, USER_LAST_MODIFY)
 Values
   (93, 1, 'No, this number is not eligible to be transferred to TELUS because Wireless Number Portability (WNP) is not currently supported in the client''s local calling area.', 'EN', 3, TO_DATE('11/14/2007 12:30:51', 'MM/DD/YYYY HH24:MI:SS'), TO_DATE('11/14/2007 12:30:51', 'MM/DD/YYYY HH24:MI:SS'), 'EASADM');
Insert into ADAPTED_MESSAGE
   (APPLICATION_MESSAGE_ID, ADAPTED_MESSAGE_SEQ, MESSAGE_TEXT, LANGUAGE_CD, MESSAGE_TYPE_ID, LOAD_DT, UPDATE_DT, USER_LAST_MODIFY)
 Values
   (93, 2, ' Non, ce numéro ne peut pas être transféré à TELUS parce que la transférabilité des numéros sans fil n''est actuellement pas offerte dans la zone d''appel local du client.', 'FR', 3, TO_DATE('11/14/2007 12:30:51', 'MM/DD/YYYY HH24:MI:SS'), TO_DATE('11/14/2007 12:30:51', 'MM/DD/YYYY HH24:MI:SS'), 'EASADM');
Insert into ADAPTED_MESSAGE
   (APPLICATION_MESSAGE_ID, ADAPTED_MESSAGE_SEQ, MESSAGE_TEXT, LANGUAGE_CD, MESSAGE_TYPE_ID, LOAD_DT, UPDATE_DT, USER_LAST_MODIFY, BRAND_ID)
 Values
   (93, 3, 'No, this number isn''t eligible to move to Koodo Mobile because Wireless Number Portability (WNP) is not currently supported in the customer''s local calling area.', 'EN', 3, TO_DATE('10/12/2007 13:07:51', 'MM/DD/YYYY HH24:MI:SS'), TO_DATE('10/12/2007 13:07:51', 'MM/DD/YYYY HH24:MI:SS'), 'EASADM', 3);
Insert into ADAPTED_MESSAGE
   (APPLICATION_MESSAGE_ID, ADAPTED_MESSAGE_SEQ, MESSAGE_TEXT, LANGUAGE_CD, MESSAGE_TYPE_ID, LOAD_DT, UPDATE_DT, USER_LAST_MODIFY, BRAND_ID)
 Values
   (93, 4, 'Non, ce numéro ne peut pas être transféré chez Koodo Mobile parce que La transférabilité des numéros sans fil n''est actuellement pas offerte dans la zone d''appel local du client.', 'FR', 3, TO_DATE('10/12/2007 13:07:51', 'MM/DD/YYYY HH24:MI:SS'), TO_DATE('10/12/2007 13:07:51', 'MM/DD/YYYY HH24:MI:SS'), 'EASADM', 3);
Insert into ADAPTED_MESSAGE
   (APPLICATION_MESSAGE_ID, ADAPTED_MESSAGE_SEQ, MESSAGE_TEXT, LANGUAGE_CD, MESSAGE_TYPE_ID, LOAD_DT, UPDATE_DT, USER_LAST_MODIFY)
 Values
   (94, 1, 'No, this number is not eligible to be transferred to TELUS because we do not currently provide service in the client''s local calling area.', 'EN', 3, TO_DATE('11/14/2007 12:30:51', 'MM/DD/YYYY HH24:MI:SS'), TO_DATE('11/14/2007 12:30:51', 'MM/DD/YYYY HH24:MI:SS'), 'EASADM');
Insert into ADAPTED_MESSAGE
   (APPLICATION_MESSAGE_ID, ADAPTED_MESSAGE_SEQ, MESSAGE_TEXT, LANGUAGE_CD, MESSAGE_TYPE_ID, LOAD_DT, UPDATE_DT, USER_LAST_MODIFY)
 Values
   (94, 2, ' Non, ce numéro ne peut pas être transféré à TELUS parce que le service n''est pas offert dans la zone d''appel local du client.', 'FR', 3, TO_DATE('11/14/2007 12:30:51', 'MM/DD/YYYY HH24:MI:SS'), TO_DATE('11/14/2007 12:30:51', 'MM/DD/YYYY HH24:MI:SS'), 'EASADM');
Insert into ADAPTED_MESSAGE
   (APPLICATION_MESSAGE_ID, ADAPTED_MESSAGE_SEQ, MESSAGE_TEXT, LANGUAGE_CD, MESSAGE_TYPE_ID, LOAD_DT, UPDATE_DT, USER_LAST_MODIFY, BRAND_ID)
 Values
   (94, 3, 'No, this number isn''t eligible to move to Koodo Mobile because we do not currently provide service in the customer''s local calling area.', 'EN', 3, TO_DATE('10/12/2007 13:07:51', 'MM/DD/YYYY HH24:MI:SS'), TO_DATE('10/12/2007 13:07:51', 'MM/DD/YYYY HH24:MI:SS'), 'EASADM', 3);
Insert into ADAPTED_MESSAGE
   (APPLICATION_MESSAGE_ID, ADAPTED_MESSAGE_SEQ, MESSAGE_TEXT, LANGUAGE_CD, MESSAGE_TYPE_ID, LOAD_DT, UPDATE_DT, USER_LAST_MODIFY, BRAND_ID)
 Values
   (94, 4, 'Non, ce numéro ne peut pas être transféré chez Koodo Mobile parce que le service n''est pas offert dans la zone d''appel local du client.', 'FR', 3, TO_DATE('10/12/2007 13:07:51', 'MM/DD/YYYY HH24:MI:SS'), TO_DATE('10/12/2007 13:07:51', 'MM/DD/YYYY HH24:MI:SS'), 'EASADM', 3);
Insert into ADAPTED_MESSAGE
   (APPLICATION_MESSAGE_ID, ADAPTED_MESSAGE_SEQ, MESSAGE_TEXT, LANGUAGE_CD, MESSAGE_TYPE_ID, LOAD_DT, UPDATE_DT, USER_LAST_MODIFY)
 Values
   (96, 1, 'No, this number is not eligible to be transferred to TELUS because it is currently not transferrable. Client can call their current service provider for details or take a new number to activate with TELUS.', 'EN', 3, TO_DATE('11/14/2007 12:30:52', 'MM/DD/YYYY HH24:MI:SS'), TO_DATE('11/14/2007 12:30:52', 'MM/DD/YYYY HH24:MI:SS'), 'EASADM');
Insert into ADAPTED_MESSAGE
   (APPLICATION_MESSAGE_ID, ADAPTED_MESSAGE_SEQ, MESSAGE_TEXT, LANGUAGE_CD, MESSAGE_TYPE_ID, LOAD_DT, UPDATE_DT, USER_LAST_MODIFY)
 Values
   (96, 2, 'Non, ce numéro ne peut pas être transféré à TELUS parce que Il ne peut être transféré présentement. Le client peut appeler son fournisseur de services actuel pour obtenir plus de détails ou choisir un nouveau numéro de téléphone  pour mettre son appareil en service à TELUS.', 'FR', 3, TO_DATE('11/14/2007 12:30:52', 'MM/DD/YYYY HH24:MI:SS'), TO_DATE('11/14/2007 12:30:52', 'MM/DD/YYYY HH24:MI:SS'), 'EASADM');
Insert into ADAPTED_MESSAGE
   (APPLICATION_MESSAGE_ID, ADAPTED_MESSAGE_SEQ, MESSAGE_TEXT, LANGUAGE_CD, MESSAGE_TYPE_ID, LOAD_DT, UPDATE_DT, USER_LAST_MODIFY, BRAND_ID)
 Values
   (96, 3, 'No, this number isn''t eligible to move to Koodo Mobile because it is currently not transferrable. Customer can call their current service provider for details or take a new number to activate with Koodo Mobile.', 'EN', 3, TO_DATE('10/12/2007 13:07:51', 'MM/DD/YYYY HH24:MI:SS'), TO_DATE('10/12/2007 13:07:51', 'MM/DD/YYYY HH24:MI:SS'), 'EASADM', 3);
Insert into ADAPTED_MESSAGE
   (APPLICATION_MESSAGE_ID, ADAPTED_MESSAGE_SEQ, MESSAGE_TEXT, LANGUAGE_CD, MESSAGE_TYPE_ID, LOAD_DT, UPDATE_DT, USER_LAST_MODIFY, BRAND_ID)
 Values
   (96, 4, 'Non, ce numéro ne peut pas être transféré chez Koodo Mobile parce que Le client peut appeler son fournisseur de services actuel pour obtenir plus de détails ou choisir un nouveau numéro de téléphone pour activer un téléphone chez Koodo Mobile.', 'FR', 3, TO_DATE('10/12/2007 13:07:51', 'MM/DD/YYYY HH24:MI:SS'), TO_DATE('10/12/2007 13:07:51', 'MM/DD/YYYY HH24:MI:SS'), 'EASADM', 3);
Insert into ADAPTED_MESSAGE
   (APPLICATION_MESSAGE_ID, ADAPTED_MESSAGE_SEQ, MESSAGE_TEXT, LANGUAGE_CD, MESSAGE_TYPE_ID, LOAD_DT, UPDATE_DT, USER_LAST_MODIFY)
 Values
   (97, 1, 'No, this number is not eligible to be transferred to TELUS because there is no agreement in place to allow for number transfers between TELUS and the client''s current service provider.', 'EN', 3, TO_DATE('11/14/2007 12:30:52', 'MM/DD/YYYY HH24:MI:SS'), TO_DATE('11/14/2007 12:30:52', 'MM/DD/YYYY HH24:MI:SS'), 'EASADM');
Insert into ADAPTED_MESSAGE
   (APPLICATION_MESSAGE_ID, ADAPTED_MESSAGE_SEQ, MESSAGE_TEXT, LANGUAGE_CD, MESSAGE_TYPE_ID, LOAD_DT, UPDATE_DT, USER_LAST_MODIFY)
 Values
   (97, 2, ' Non, ce numéro ne peut pas être transféré à TELUS parce que il n''existe aucune entente permettant le transfert des numéros entre TELUS et le fournisseur de service actuel du client.', 'FR', 3, TO_DATE('11/14/2007 12:30:52', 'MM/DD/YYYY HH24:MI:SS'), TO_DATE('11/14/2007 12:30:52', 'MM/DD/YYYY HH24:MI:SS'), 'EASADM');
Insert into ADAPTED_MESSAGE
   (APPLICATION_MESSAGE_ID, ADAPTED_MESSAGE_SEQ, MESSAGE_TEXT, LANGUAGE_CD, MESSAGE_TYPE_ID, LOAD_DT, UPDATE_DT, USER_LAST_MODIFY, BRAND_ID)
 Values
   (97, 3, 'Unfortunately, this number isn''t eligible to move to Koodo Mobile. If you''d like to move to Koodo Mobile with a new number, please visit a Koodo retailer or go to koodomobile.com.', 'EN', 3, TO_DATE('10/12/2007 13:07:51', 'MM/DD/YYYY HH24:MI:SS'), TO_DATE('10/12/2007 13:07:51', 'MM/DD/YYYY HH24:MI:SS'), 'EASADM', 3);
Insert into ADAPTED_MESSAGE
   (APPLICATION_MESSAGE_ID, ADAPTED_MESSAGE_SEQ, MESSAGE_TEXT, LANGUAGE_CD, MESSAGE_TYPE_ID, LOAD_DT, UPDATE_DT, USER_LAST_MODIFY, BRAND_ID)
 Values
   (97, 4, 'Malheureusement, ce numéro ne peut pas être transféré chez Koodo Mobile. Pour passer chez Koodo Mobile et choisir un nouveau numéro, visitez un détaillant Koodo ou allez à koodomobile.com.', 'FR', 3, TO_DATE('10/12/2007 13:07:51', 'MM/DD/YYYY HH24:MI:SS'), TO_DATE('10/12/2007 13:07:51', 'MM/DD/YYYY HH24:MI:SS'), 'EASADM', 3);
Insert into ADAPTED_MESSAGE
   (APPLICATION_MESSAGE_ID, ADAPTED_MESSAGE_SEQ, MESSAGE_TEXT, LANGUAGE_CD, MESSAGE_TYPE_ID, LOAD_DT, UPDATE_DT, USER_LAST_MODIFY)
 Values
   (98, 1, 'No, this number is not eligible to be transferred to TELUS because it is already active on our network.', 'EN', 3, TO_DATE('11/14/2007 12:30:53', 'MM/DD/YYYY HH24:MI:SS'), TO_DATE('11/14/2007 12:30:53', 'MM/DD/YYYY HH24:MI:SS'), 'EASADM');
Insert into ADAPTED_MESSAGE
   (APPLICATION_MESSAGE_ID, ADAPTED_MESSAGE_SEQ, MESSAGE_TEXT, LANGUAGE_CD, MESSAGE_TYPE_ID, LOAD_DT, UPDATE_DT, USER_LAST_MODIFY)
 Values
   (98, 2, ' Non, ce numéro ne peut pas être transféré à TELUS parce que il est déjà en service sur notre réseau.', 'FR', 3, TO_DATE('11/14/2007 12:30:53', 'MM/DD/YYYY HH24:MI:SS'), TO_DATE('11/14/2007 12:30:53', 'MM/DD/YYYY HH24:MI:SS'), 'EASADM');
Insert into ADAPTED_MESSAGE
   (APPLICATION_MESSAGE_ID, ADAPTED_MESSAGE_SEQ, MESSAGE_TEXT, LANGUAGE_CD, MESSAGE_TYPE_ID, LOAD_DT, UPDATE_DT, USER_LAST_MODIFY, BRAND_ID)
 Values
   (98, 3, 'Unfortunately, this number isn''t eligible to move to Koodo Mobile. If you''d like to move to Koodo Mobile with a new number, please visit a Koodo retailer or go to koodomobile.com.', 'EN', 3, TO_DATE('10/12/2007 13:07:51', 'MM/DD/YYYY HH24:MI:SS'), TO_DATE('10/12/2007 13:07:51', 'MM/DD/YYYY HH24:MI:SS'), 'EASADM', 3);
Insert into ADAPTED_MESSAGE
   (APPLICATION_MESSAGE_ID, ADAPTED_MESSAGE_SEQ, MESSAGE_TEXT, LANGUAGE_CD, MESSAGE_TYPE_ID, LOAD_DT, UPDATE_DT, USER_LAST_MODIFY, BRAND_ID)
 Values
   (98, 4, 'Malheureusement, ce numéro ne peut pas être transféré chez Koodo Mobile. Pour passer chez Koodo Mobile et choisir un nouveau numéro, visitez un détaillant Koodo ou allez à koodomobile.com.', 'FR', 3, TO_DATE('10/12/2007 13:07:52', 'MM/DD/YYYY HH24:MI:SS'), TO_DATE('10/12/2007 13:07:52', 'MM/DD/YYYY HH24:MI:SS'), 'EASADM', 3);
Insert into ADAPTED_MESSAGE
   (APPLICATION_MESSAGE_ID, ADAPTED_MESSAGE_SEQ, MESSAGE_TEXT, LANGUAGE_CD, MESSAGE_TYPE_ID, LOAD_DT, UPDATE_DT, USER_LAST_MODIFY)
 Values
   (99, 1, 'Yes, this number is eligible to be transferred to [PCS Postpaid/Mike/Pay ].  [note: insert appropriate product type(s) with punctuation, as necessary].', 'EN', 3, TO_DATE('11/14/2007 12:30:59', 'MM/DD/YYYY HH24:MI:SS'), TO_DATE('11/14/2007 12:30:59', 'MM/DD/YYYY HH24:MI:SS'), 'EASADM');
Insert into ADAPTED_MESSAGE
   (APPLICATION_MESSAGE_ID, ADAPTED_MESSAGE_SEQ, MESSAGE_TEXT, LANGUAGE_CD, MESSAGE_TYPE_ID, LOAD_DT, UPDATE_DT, USER_LAST_MODIFY)
 Values
   (99, 2, 'Oui, ce numéro peut être transféré à un appareil SCP avec service postpayé/Mike/ SCP avec service Payez   [remarque : inscrivez le ou les types de produit appropriés et les signes de ponctuation requis].', 'FR', 3, TO_DATE('11/14/2007 12:31:02', 'MM/DD/YYYY HH24:MI:SS'), TO_DATE('11/14/2007 12:31:02', 'MM/DD/YYYY HH24:MI:SS'), 'EASADM');
Insert into ADAPTED_MESSAGE
   (APPLICATION_MESSAGE_ID, ADAPTED_MESSAGE_SEQ, MESSAGE_TEXT, LANGUAGE_CD, MESSAGE_TYPE_ID, LOAD_DT, UPDATE_DT, USER_LAST_MODIFY, BRAND_ID)
 Values
   (99, 3, 'Yes, this number can be moved to Koodo Mobile. Please call us at 1-866-99-Koodo or visit a Koodo retailer to submit a request.', 'EN', 3, TO_DATE('10/12/2007 13:07:50', 'MM/DD/YYYY HH24:MI:SS'), TO_DATE('10/12/2007 13:07:50', 'MM/DD/YYYY HH24:MI:SS'), 'EASADM', 3);
Insert into ADAPTED_MESSAGE
   (APPLICATION_MESSAGE_ID, ADAPTED_MESSAGE_SEQ, MESSAGE_TEXT, LANGUAGE_CD, MESSAGE_TYPE_ID, LOAD_DT, UPDATE_DT, USER_LAST_MODIFY, BRAND_ID)
 Values
   (99, 4, 'Oui, ce numéro peut être transféré chez Koodo Mobile. Appelez nous au 1-866-99-Koodo ou passez chez un détaillant Koodo pour faire une demande de transfert.', 'FR', 3, TO_DATE('10/12/2007 13:07:51', 'MM/DD/YYYY HH24:MI:SS'), TO_DATE('10/12/2007 13:07:51', 'MM/DD/YYYY HH24:MI:SS'), 'EASADM', 3);
Insert into ADAPTED_MESSAGE
   (APPLICATION_MESSAGE_ID, ADAPTED_MESSAGE_SEQ, MESSAGE_TEXT, LANGUAGE_CD, MESSAGE_TYPE_ID, LOAD_DT, UPDATE_DT, USER_LAST_MODIFY)
 Values
   (100, 1, 'A transfer request for this number is already in progress.  Use the transfer status tool to check the transfer progress of this number.', 'EN', 3, TO_DATE('11/14/2007 12:31:02', 'MM/DD/YYYY HH24:MI:SS'), TO_DATE('11/14/2007 12:31:02', 'MM/DD/YYYY HH24:MI:SS'), 'EASADM');
Insert into ADAPTED_MESSAGE
   (APPLICATION_MESSAGE_ID, ADAPTED_MESSAGE_SEQ, MESSAGE_TEXT, LANGUAGE_CD, MESSAGE_TYPE_ID, LOAD_DT, UPDATE_DT, USER_LAST_MODIFY)
 Values
   (100, 2, 'Une demande de transfert de ce numéro de téléphone est déjà en cours de traitement. Utilisez l''outil à cet effet pour vérifier la situation liée au transfert de ce numéro.', 'FR', 3, TO_DATE('11/14/2007 12:31:02', 'MM/DD/YYYY HH24:MI:SS'), TO_DATE('11/14/2007 12:31:02', 'MM/DD/YYYY HH24:MI:SS'), 'EASADM');
Insert into ADAPTED_MESSAGE
   (APPLICATION_MESSAGE_ID, ADAPTED_MESSAGE_SEQ, MESSAGE_TEXT, LANGUAGE_CD, MESSAGE_TYPE_ID, LOAD_DT, UPDATE_DT, USER_LAST_MODIFY)
 Values
   (156, 1, 'You''ve already submitted a request to move your number. Unfortunately, your new request can''t be processed.', 'EN', 3, TO_DATE('09/04/2007 13:00:14', 'MM/DD/YYYY HH24:MI:SS'), TO_DATE('09/04/2007 13:00:14', 'MM/DD/YYYY HH24:MI:SS'), 'EASADM');
Insert into ADAPTED_MESSAGE
   (APPLICATION_MESSAGE_ID, ADAPTED_MESSAGE_SEQ, MESSAGE_TEXT, LANGUAGE_CD, MESSAGE_TYPE_ID, LOAD_DT, UPDATE_DT, USER_LAST_MODIFY)
 Values
   (156, 2, 'Vous avez déjà soumis une demande de transfert de votre numéro. Malheureusement, votre nouvelle demande ne peut pas être traitée.', 'FR', 3, TO_DATE('09/04/2007 13:00:14', 'MM/DD/YYYY HH24:MI:SS'), TO_DATE('09/04/2007 13:00:14', 'MM/DD/YYYY HH24:MI:SS'), 'EASADM');
Insert into ADAPTED_MESSAGE
   (APPLICATION_MESSAGE_ID, ADAPTED_MESSAGE_SEQ, MESSAGE_TEXT, LANGUAGE_CD, MESSAGE_TYPE_ID, LOAD_DT, UPDATE_DT, USER_LAST_MODIFY)
 Values
   (157, 1, 'Porting not supported.', 'EN', 3, TO_DATE('09/04/2007 13:00:14', 'MM/DD/YYYY HH24:MI:SS'), TO_DATE('09/04/2007 13:00:14', 'MM/DD/YYYY HH24:MI:SS'), 'EASADM');
Insert into ADAPTED_MESSAGE
   (APPLICATION_MESSAGE_ID, ADAPTED_MESSAGE_SEQ, MESSAGE_TEXT, LANGUAGE_CD, MESSAGE_TYPE_ID, LOAD_DT, UPDATE_DT, USER_LAST_MODIFY)
 Values
   (157, 2, 'Demande de transfert n''est pas possible.', 'FR', 3, TO_DATE('09/04/2007 13:00:14', 'MM/DD/YYYY HH24:MI:SS'), TO_DATE('09/04/2007 13:00:14', 'MM/DD/YYYY HH24:MI:SS'), 'EASADM');
Insert into ADAPTED_MESSAGE
   (APPLICATION_MESSAGE_ID, ADAPTED_MESSAGE_SEQ, MESSAGE_TEXT, APPLICATION_ID, LANGUAGE_CD, MESSAGE_TYPE_ID, LOAD_DT, UPDATE_DT, USER_LAST_MODIFY, BRAND_ID)
 Values
   (207, 1, 'We are not able to satisfy this request at this time. Please contact a Koodo rep and reference error message MTK.', 0, 'EN', 1, TO_DATE('11/28/2008 15:02:13', 'MM/DD/YYYY HH24:MI:SS'), TO_DATE('11/28/2008 15:02:13', 'MM/DD/YYYY HH24:MI:SS'), 'ACTV_APP', 3);
Insert into ADAPTED_MESSAGE
   (APPLICATION_MESSAGE_ID, ADAPTED_MESSAGE_SEQ, MESSAGE_TEXT, APPLICATION_ID, LANGUAGE_CD, MESSAGE_TYPE_ID, LOAD_DT, UPDATE_DT, USER_LAST_MODIFY, BRAND_ID)
 Values
   (207, 2, 'Il nous est impossible de satisfaire à votre demande pour le moment. Veuillez communiquer avec un représentant Koodo et lui indiquer le message d''erreur MTK.', 0, 'FR', 1, TO_DATE('11/28/2008 15:02:13', 'MM/DD/YYYY HH24:MI:SS'), TO_DATE('11/28/2008 15:02:13', 'MM/DD/YYYY HH24:MI:SS'), 'ACTV_APP', 3);

COMMIT;

