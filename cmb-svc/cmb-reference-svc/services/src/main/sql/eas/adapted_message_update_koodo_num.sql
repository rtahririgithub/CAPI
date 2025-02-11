--Mar 4, 2015
--Update Koodo number from 1-866-99-KOODO to 1-844-232-7678

update adapted_message set message_text = 'This phone number isn''t eligible to be activated on this network. You''ll need to select a different area code and first 3 digits to complete your request.  If you need help, please call PAC at 844-232-7678.  (RC E1)' where APPLICATION_MESSAGE_ID = '92' and ADAPTED_MESSAGE_SEQ = '3';

update adapted_message set message_text = 'Ce numéro de téléphone n''est pas éligible pour être activé sur ce réseau.  Afin de compléter votre requête, vous devrez changer votre sélection de code régional et des trois (3) premiers chiffres du numéro.  Si vous avez besoin d''aide, veuillez appeler CAT au 844-232-7678.  (RC E1)' where APPLICATION_MESSAGE_ID = '92' and ADAPTED_MESSAGE_SEQ = '4';

update adapted_message set message_text = 'This phone number isn''t eligible to be activated on this network. You''ll need to select a different area code and first 3 digits to complete your request.  If you need help, please call PAC at 844-232-7678.  (RC E3)' where APPLICATION_MESSAGE_ID = '94' and ADAPTED_MESSAGE_SEQ = '3';

update adapted_message set message_text = 'Ce numéro de téléphone n''est pas éligible pour être activé sur ce réseau.  Afin de compléter votre requête, vous devrez changer votre sélection de code régional et des trois (3) premiers chiffres du numéro.  Si vous avez besoin d''aide, veuillez appeler CAT au 844-232-7678.  (RC E3)' where APPLICATION_MESSAGE_ID = '94' and ADAPTED_MESSAGE_SEQ = '4';

update adapted_message set message_text = 'We are experiencing difficulties in processing your transfer request. If you need help, please call PAC at 844-232-7678.  (RC E5)' where APPLICATION_MESSAGE_ID = '96' and ADAPTED_MESSAGE_SEQ = '3';

update adapted_message set message_text = 'Nous éprouvons des difficultés de traitement de votre demande de transfert. Si vous avez besoin d''aide, veuillez appeler CAT au 844-232-7678.   (RC E5)' where APPLICATION_MESSAGE_ID = '96' and ADAPTED_MESSAGE_SEQ = '4';

update adapted_message set message_text = 'This phone number isn''t eligible to be activated on this network because there isn''t a porting agreement with the current service provider.  Please select a new number to complete your activation, or call PAC at 844-232-7678 for help. (RC E7)' where APPLICATION_MESSAGE_ID = '97' and ADAPTED_MESSAGE_SEQ = '3';

update adapted_message set message_text = 'Ce numéro de téléphone n''est pas éligible pour être activé sur ce réseau car il n''y a pas d''entente de transfert avec le fournisseur de services actuel.  Veuillez sélectionner un nouveau numéro pour compléter votre activation, ou si vous avez besoin d''aide appelez CAT au 844-232-7678. (RC E7)' where APPLICATION_MESSAGE_ID = '97' and ADAPTED_MESSAGE_SEQ = '4';

delete from adapted_message where APPLICATION_MESSAGE_ID = '103' and ADAPTED_MESSAGE_SEQ = '5';

delete from adapted_message where APPLICATION_MESSAGE_ID = '103' and ADAPTED_MESSAGE_SEQ = '6';

update adapted_message set message_text = 'We are experiencing difficulties in processing your transfer request. Please call us at 844-232-7678.' where APPLICATION_MESSAGE_ID = '105' and ADAPTED_MESSAGE_SEQ = '5';

update adapted_message set message_text = 'Nous éprouvons des difficultés de traitement de votre demande de transfert. Veuillez nous appeler au 844-232-7678.' where APPLICATION_MESSAGE_ID = '105' and ADAPTED_MESSAGE_SEQ = '6';

update adapted_message set message_text = 'We could not locate an incoming transfer request for this number.' where APPLICATION_MESSAGE_ID = '106' and ADAPTED_MESSAGE_SEQ = '5';

update adapted_message set message_text = 'Nous ne trouvons pas de demande de transfert entrant pour ce numéro.' where APPLICATION_MESSAGE_ID = '106' and ADAPTED_MESSAGE_SEQ = '6';

update adapted_message set message_text = 'For the status of your transfer request, please call 844-232-7678.' where APPLICATION_MESSAGE_ID = '107' and ADAPTED_MESSAGE_SEQ = '5';

update adapted_message set message_text = 'Faites le 844-232-7678 pour connaître le statut de votre demande de transfert.' where APPLICATION_MESSAGE_ID = '107' and ADAPTED_MESSAGE_SEQ = '6';

update adapted_message set message_text = 'For the status of your transfer request, please call 844-232-7678.' where APPLICATION_MESSAGE_ID = '109' and ADAPTED_MESSAGE_SEQ = '5';

update adapted_message set message_text = 'Faites le 844-232-7678 pour connaître le statut de votre demande de transfert.' where APPLICATION_MESSAGE_ID = '109' and ADAPTED_MESSAGE_SEQ = '6';
