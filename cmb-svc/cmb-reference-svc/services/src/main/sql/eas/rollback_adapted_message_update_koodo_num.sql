--Mar 4, 2015
--Rollback Koodo number change from 1-844-232-7678 to 1-866-99-KOODO

update adapted_message set message_text = 'This phone number isn''t eligible to be activated on this network. You''ll need to select a different area code and first 3 digits to complete your request.  If you need help, please call us at 1-866-99-Koodo.  (RC E1)' where APPLICATION_MESSAGE_ID = '92' and ADAPTED_MESSAGE_SEQ = '3';

update adapted_message set message_text = 'Ce numéro de téléphone n''est pas éligible pour être activé sur ce réseau.  Afin de compléter votre requête, vous devrez changer votre sélection de code régional et des trois (3)  premiers chiffres du numéro.  Si vous avez besoin d''aide, veuillez nous appeler au 1-866-99-KOODO.  (RC E1)' where APPLICATION_MESSAGE_ID = '92' and ADAPTED_MESSAGE_SEQ = '4';

update adapted_message set message_text = 'This phone number isn''t eligible to be activated on this network. You''ll need to select a different area code and first 3 digits to complete your request.  If you need help, please call us at 1-866-99-Koodo. (RC E3)' where APPLICATION_MESSAGE_ID = '94' and ADAPTED_MESSAGE_SEQ = '3';

update adapted_message set message_text = 'Ce numéro de téléphone n''est pas éligible pour être activé sur ce réseau.  Afin de compléter votre requête, vous devrez changer votre sélection de code régional et des trois (3)  premiers chiffres du numéro.  Si vous avez besoin d''aide, veuillez nous appeler au 1-866-99-KOODO. (RC E3)' where APPLICATION_MESSAGE_ID = '94' and ADAPTED_MESSAGE_SEQ = '4';

update adapted_message set message_text = 'We are experiencing difficulties in processing your transfer request. Please call us at 1-866-99-Koodo or visit a Koodo retailer for assistance. (RC E5)' where APPLICATION_MESSAGE_ID = '96' and ADAPTED_MESSAGE_SEQ = '3';

update adapted_message set message_text = 'Nous éprouvons des difficultés de traitement de votre demande de transfert. Veuillez nous appeler au 1-866-99-Koodo ou rendez-vous chez l''un de nos détaillants pour obtenir de l''aide. (RC E5)' where APPLICATION_MESSAGE_ID = '96' and ADAPTED_MESSAGE_SEQ = '4';

update adapted_message set message_text = 'This phone number isn''t eligible to be activated on this network because there isn''t a porting agreement with the current service provider.  Please select a new number to complete your activation, or call us at 1-866-99-Koodo for help. (RC E7)' where APPLICATION_MESSAGE_ID = '97' and ADAPTED_MESSAGE_SEQ = '3';

update adapted_message set message_text = 'Ce numéro de téléphone n''est pas éligible pour être activé sur ce réseau car il n''y a pas d''entente de transfert avec le fournisseur de services actuel.  Veuillez sélectionner un nouveau numéro pour compléter votre activation, ou si vous avez besoin d''aide appellez-nous au 1-866-99-KOODO. (RC E7)' where APPLICATION_MESSAGE_ID = '97' and ADAPTED_MESSAGE_SEQ = '4';

insert into adapted_message (APPLICATION_MESSAGE_ID, ADAPTED_MESSAGE_SEQ, MESSAGE_TEXT, LANGUAGE_CD, MESSAGE_TYPE_ID, LOAD_DT, UPDATE_DT, USER_LAST_MODIFY, BRAND_ID) 
values ('103', '5', 'Your request to move  to Koodo Mobile is active.  Please call 1-866-99-Koodo and press 4.', 'EN', '3', to_date('07/10/2012','mm/dd/yyyy'), to_date('07/10/2012','mm/dd/yyyy'), 'EASADM', '3');

insert into adapted_message (APPLICATION_MESSAGE_ID, ADAPTED_MESSAGE_SEQ, MESSAGE_TEXT, LANGUAGE_CD, MESSAGE_TYPE_ID, LOAD_DT, UPDATE_DT, USER_LAST_MODIFY, BRAND_ID) 
values ('103', '6', 'Votre demande de transfert chez Koodo Mobile est active. Appelez au numéro 1-866-99-Koodo et faites le 4.', 'FR', '3', to_date('07/10/2012','mm/dd/yyyy'), to_date('07/10/2012','mm/dd/yyyy'), 'EASADM', '3');

update adapted_message set message_text = 'Sorry, but we''re experiencing difficulties in processing your request to move to Koodo Mobile. Please call 1-866-99-Koodo and press 4.' where APPLICATION_MESSAGE_ID = '105' and ADAPTED_MESSAGE_SEQ = '5';

update adapted_message set message_text = 'Générique : Nous éprouvons des difficultés dans le traitement de votre demande de transfert chez Koodo Mobile. Appelez au numéro 1-866-99-Koodo et faites le 4.' where APPLICATION_MESSAGE_ID = '105' and ADAPTED_MESSAGE_SEQ = '6';

update adapted_message set message_text = 'We could not locate a request for this number. Please call 1-866-99-KOODO if you wish to move your number to Koodo Mobile.' where APPLICATION_MESSAGE_ID = '106' and ADAPTED_MESSAGE_SEQ = '5';

update adapted_message set message_text = 'Nous ne trouvons pas de demande pour ce numéro. Appelez au 1-866-99-KOODO si vous voulez transférer votre numéro chez Koodo Mobile.' where APPLICATION_MESSAGE_ID = '106' and ADAPTED_MESSAGE_SEQ = '6';

update adapted_message set message_text = 'For the status of your move, please call  at 1-866-99-KOODO.' where APPLICATION_MESSAGE_ID = '107' and ADAPTED_MESSAGE_SEQ = '5';

update adapted_message set message_text = 'Faites le 1-866-99-KOODO pour connaître le statut de votre demande de tranfert.' where APPLICATION_MESSAGE_ID = '107' and ADAPTED_MESSAGE_SEQ = '6';

update adapted_message set message_text = 'For the status of your move, please call  at 1-866-99-KOODO.' where APPLICATION_MESSAGE_ID = '109' and ADAPTED_MESSAGE_SEQ = '5';

update adapted_message set message_text = 'Faites le 1-866-99-KOODO pour connaître le statut de votre demande de tranfert.' where APPLICATION_MESSAGE_ID = '109' and ADAPTED_MESSAGE_SEQ = '6';
