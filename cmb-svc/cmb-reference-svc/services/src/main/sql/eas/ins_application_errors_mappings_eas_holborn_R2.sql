--E0
UPDATE adapted_message
  SET message_text = 'This number is eligible for transfer to [PCS Postpaid/Mike/Pay & Talk]. [note: insert appropriate product type(s) with punctuation, as necessary].  (RC E0)', update_dt=sysdate, user_last_modify=user
  WHERE application_message_id=99  AND adapted_message_seq = 1;
UPDATE adapted_message
  SET message_text = 'Ce numéro est admissible à un transfert au service [SCP postpayé, Mike, Payez & Parlez].  (RC E0)', update_dt=sysdate, user_last_modify=user
  WHERE application_message_id=99  AND adapted_message_seq = 2;
UPDATE adapted_message
  SET message_text = 'Yes, this mobile phone number is eligible to be moved to Koodo Mobile.  Visit our <a href="http://koodomobile.com">Web Store</a> or a Koodo retailer to activate this number with a Koodo Mobile phone.  (RC E0)', update_dt=sysdate, user_last_modify=user
  WHERE application_message_id=99  AND adapted_message_seq = 3;
UPDATE adapted_message
  SET message_text = 'Oui, ce numéro de téléphone mobile peut être transféré chez Koodo Mobile. Visitez notre <a href="http://koodomobile.com">Boutique Web</a> ou un détaillant Koodo pour activer ce numéro avec un téléphone Koodo Mobile.  (RC E0)', update_dt=sysdate, user_last_modify=user
  WHERE application_message_id=99  AND adapted_message_seq = 4;
--E1  
UPDATE adapted_message
  SET message_text = 'This request can not be completed because this device''s phone number cannot be activated on the selected network. Please select a phone number with a different area code and first three digits to activate this device or contact TELUS for additional assistance.  (RC E1)', update_dt=sysdate, user_last_modify=user
  WHERE application_message_id=92  AND adapted_message_seq = 1;
UPDATE adapted_message
  SET message_text = 'Cette demande ne peut être traitée car le numéro de téléphone ne peut être activé sur ce réseau. Veuillez modifier le code régional et les trois premiers chiffres du numéro pour activer ce cellulaire ou communiquer avec TELUS pour obtenir de l’aide. (RC E1)', update_dt=sysdate, user_last_modify=user
  WHERE application_message_id=92  AND adapted_message_seq = 2;
UPDATE adapted_message
  SET message_text = 'This phone number isn''t eligible to be activated on this network. You''ll need to select a different area code and first 3 digits to complete your request.  If you need help, please call us at 1-866-99-Koodo.  (RC E1)', update_dt=sysdate, user_last_modify=user
  WHERE application_message_id=92  AND adapted_message_seq = 3;
UPDATE adapted_message
  SET message_text = 'Ce numéro de téléphone n’est pas éligible pour être activé sur ce réseau.  Afin de compléter votre requête, vous devrez changer votre sélection de code régional et des trois (3)  premiers chiffres du numéro.  Si vous avez besoin d’aide, veuillez nous appeler au 1-866-99-KOODO.  (RC E1)', update_dt=sysdate, user_last_modify=user
  WHERE application_message_id=92  AND adapted_message_seq = 4;
--E2
UPDATE adapted_message
  SET message_text = 'This request cannot be completed because the device''s phone number cannot be activated on this network.  Please select a new number to activate this device or contact TELUS for additional assistance. (RC E2)', update_dt=sysdate, user_last_modify=user
  WHERE application_message_id=93  AND adapted_message_seq = 1;
UPDATE adapted_message
  SET message_text = 'Cette demande ne peut être traitée car le numéro de téléphone ne peut être activé sur ce réseau.  Veuillez choisir un nouveau numéro pour activer ce cellulaire ou communiquer avec TELUS pour obtenir de l’aide. (RC E2)', update_dt=sysdate, user_last_modify=user
  WHERE application_message_id=93  AND adapted_message_seq = 2;
UPDATE adapted_message
  SET message_text = 'This phone number cannot be activated on the selected network. Please select a phone number from a different NPA-NXX to continue or contact your Koodo Support Team for assistance.   (RC E2)', update_dt=sysdate, user_last_modify=user
  WHERE application_message_id=93  AND adapted_message_seq = 3;
UPDATE adapted_message
  SET message_text = 'Ce numéro de téléphone ne peut pas être activé sur le réseau sélectionné. Veuillez choisir un numéro de téléphone avec un NPA-NXX différent pour continuer ou communiquez avec votre équipe du service à la clientèle de Koodo pour obtenir de l’aide.  (RC E2)', update_dt=sysdate, user_last_modify=user
  WHERE application_message_id=93  AND adapted_message_seq = 4;
--E3
UPDATE adapted_message
  SET message_text = 'This request cannot be completed because the device''s phone number cannot be activated on this network. Please select a phone number with a different area code and first three digits to activate this device or contact TELUS for additional assistance. (RC E3)', update_dt=sysdate, user_last_modify=user
  WHERE application_message_id=94  AND adapted_message_seq = 1;
UPDATE adapted_message
  SET message_text = 'Cette demande ne peut être traitée car le numéro de téléphone ne peut être activé sur ce réseau. Veuillez modifier le code régional et les trois premiers chiffres du numéro pour activer ce cellulaire ou communiquer avec TELUS pour obtenir de l’aide.  (RC E3)', update_dt=sysdate, user_last_modify=user
  WHERE application_message_id=94  AND adapted_message_seq = 2;
UPDATE adapted_message
  SET message_text = 'This phone number isn''t eligible to be activated on this network. You''ll need to select a different area code and first 3 digits to complete your request.  If you need help, please call us at 1-866-99-Koodo. (RC E3)', update_dt=sysdate, user_last_modify=user
  WHERE application_message_id=94  AND adapted_message_seq = 3;
UPDATE adapted_message
  SET message_text = 'Ce numéro de téléphone n’est pas éligible pour être activé sur ce réseau.  Afin de compléter votre requête, vous devrez changer votre sélection de code régional et des trois (3)  premiers chiffres du numéro.  Si vous avez besoin d’aide, veuillez nous appeler au 1-866-99-KOODO. (RC E3)', update_dt=sysdate, user_last_modify=user
  WHERE application_message_id=94  AND adapted_message_seq = 4;
--E5
UPDATE adapted_message
  SET message_text = 'We are experiencing difficulties in processing this request. Please contact TELUS for additional assistance. (RC E5)', update_dt=sysdate, user_last_modify=user
  WHERE application_message_id=96  AND adapted_message_seq = 1;
UPDATE adapted_message
  SET message_text = 'Nous éprouvons des difficultés à traiter cette demande. Veuillez communiquer avec TELUS pour obtenir de l’aide. (RC E5)', update_dt=sysdate, user_last_modify=user
  WHERE application_message_id=96  AND adapted_message_seq = 2;
UPDATE adapted_message
  SET message_text = 'We are experiencing difficulties in processing your transfer request. Please call us at 1-866-99-Koodo or visit a Koodo retailer for assistance. (RC E5)', update_dt=sysdate, user_last_modify=user
  WHERE application_message_id=96  AND adapted_message_seq = 3;
UPDATE adapted_message
  SET message_text = 'Nous éprouvons des difficultés de traitement de votre demande de transfert. Veuillez nous appeler au 1-866-99-Koodo ou rendez-vous chez l’un de nos détaillants pour obtenir de l’aide. (RC E5)', update_dt=sysdate, user_last_modify=user
  WHERE application_message_id=96  AND adapted_message_seq = 4;
--E7
UPDATE adapted_message
  SET message_text = 'This request cannot be completed because there is no porting agreement between TELUS and the current service provider. Please select a new number to activate this device or contact TELUS for additional assistance.  (RC E7)', update_dt=sysdate, user_last_modify=user
  WHERE application_message_id=97  AND adapted_message_seq = 1;
UPDATE adapted_message
  SET message_text = 'Cette demande ne peut être traitée car il n’existe pas d’entente de transfert entre TELUS et le fournisseur de service actuel. Veuillez choisir un nouveau numéro pour activer ce cellulaire ou communiquer avec TELUS pour obtenir de l’aide.  (RC E7)', update_dt=sysdate, user_last_modify=user
  WHERE application_message_id=97  AND adapted_message_seq = 2;
UPDATE adapted_message
  SET message_text = 'This phone number isn''t eligible to be activated on this network because there isn''t a porting agreement with the current service provider.  Please select a new number to complete your activation, or call us at 1-866-99-Koodo for help. (RC E7)', update_dt=sysdate, user_last_modify=user
  WHERE application_message_id=97  AND adapted_message_seq = 3;
UPDATE adapted_message
  SET message_text = 'Ce numéro de téléphone n’est pas éligible pour être activé sur ce réseau car il n’y a pas d’entente de transfert avec le fournisseur de services actuel.  Veuillez sélectionner un nouveau numéro pour compléter votre activation, ou si vous avez besoin d’aide appellez-nous au 1-866-99-KOODO. (RC E7)', update_dt=sysdate, user_last_modify=user
  WHERE application_message_id=97  AND adapted_message_seq = 4;
--E8
UPDATE adapted_message
  SET message_text = 'This request cannot be completed because this phone number is already an active [PCS Post-paid/Mike/Pay & Talk] subscriber.  Please select a new number to activate this device or contact TELUS for additional assistance.  (RC E8)', update_dt=sysdate, user_last_modify=user
  WHERE application_message_id=98  AND adapted_message_seq = 1;
UPDATE adapted_message
  SET message_text = 'Cette demande ne peut être traitée car le numéro de téléphone appartient déjà à un abonné actif au service [SCP postpayé, Mike, Payez & Parlez]. Veuillez choisir un nouveau numéro pour activer ce cellulaire ou communiquer avec TELUS pour obtenir de l’aide.  (RC E8)', update_dt=sysdate, user_last_modify=user
  WHERE application_message_id=98  AND adapted_message_seq = 2;
UPDATE adapted_message
  SET message_text = 'No, this number isn''t eligible to move to Koodo Mobile because it is already active on our network. To switch to Koodo Mobile with a new number, please visit our Web Store (link to Webstore) or a Koodo retailer to activate with Koodo Mobile.  (RC E8)', update_dt=sysdate, user_last_modify=user
  WHERE application_message_id=98  AND adapted_message_seq = 3;
UPDATE adapted_message
  SET message_text = 'Non, ce numéro ne peut pas être transféré chez Koodo Mobile parce que Il est déjà en service sur notre réseau. Pour passer chez Koodo Mobile et avoir un nouveau numéro, appelez visitez notre Boutique Web ou passez chez un détaillant Koodo. (RC E8)', update_dt=sysdate, user_last_modify=user
  WHERE application_message_id=98  AND adapted_message_seq = 4; 
--E9
UPDATE adapted_message
  SET message_text = 'A transfer request for this phone number is already in progress.  Please use the ''check transfer status'' tool to view the progress of this port request.  (RC E9)', update_dt=sysdate, user_last_modify=user
  WHERE application_message_id=100  AND adapted_message_seq = 1;
UPDATE adapted_message
  SET message_text = 'Une demande de transfert est en cours de traitement pour ce numéro de téléphone. Pour savoir à quelle étape elle se trouve, veuillez utiliser l’outil de vérification de l’état du transfert. (RC E9)', update_dt=sysdate, user_last_modify=user
  WHERE application_message_id=100  AND adapted_message_seq = 2;
UPDATE adapted_message
  SET message_text = 'Your request to move to Koodo Mobile is in progress. We''ll notify you of any changes to the status of your request.  (RC E9)', update_dt=sysdate, user_last_modify=user
  WHERE application_message_id=100  AND adapted_message_seq = 3;
UPDATE adapted_message
  SET message_text = 'Votre demande de transfert chez Koodo Mobile est en cours. Nous vous aviserons de tout changement de statut de votre demande. (RC E9)', update_dt=sysdate, user_last_modify=user
  WHERE application_message_id=100  AND adapted_message_seq = 4;
--E9A  
UPDATE adapted_message
  SET message_text = 'You''ve already submitted a request to move your number. Unfortunately, your new request can''t be processed. (RC E9A)', update_dt=sysdate, user_last_modify=user
  WHERE application_message_id=156  AND adapted_message_seq = 1;
UPDATE adapted_message
  SET message_text = 'Vous avez déjà soumis une demande de transfert de votre numéro. Malheureusement, votre nouvelle demande ne peut pas être traitée. (RC E9A)', update_dt=sysdate, user_last_modify=user
  WHERE application_message_id=156  AND adapted_message_seq = 2;
UPDATE adapted_message
  SET message_text = 'You''ve already submitted a request to move your number. Unfortunately, your new request can''t be processed. (RC E9A)', update_dt=sysdate, user_last_modify=user
  WHERE application_message_id=156  AND adapted_message_seq = 3;
UPDATE adapted_message
  SET message_text = 'Vous avez déjà soumis une demande de transfert de votre numéro. Malheureusement, votre nouvelle demande ne peut pas être traitée. (RC E9A)', update_dt=sysdate, user_last_modify=user
  WHERE application_message_id=156  AND adapted_message_seq = 4;
--E10
UPDATE adapted_message
  SET message_text = 'Porting not supported. (RC E10)', update_dt=sysdate, user_last_modify=user
  WHERE application_message_id=157  AND adapted_message_seq = 1;
UPDATE adapted_message
  SET message_text = 'Demande de transfert n''est pas possible. (RC E10)', update_dt=sysdate, user_last_modify=user
  WHERE application_message_id=157  AND adapted_message_seq = 2;
UPDATE adapted_message
  SET message_text = 'Porting not supported. (RC E10)', update_dt=sysdate, user_last_modify=user
  WHERE application_message_id=157  AND adapted_message_seq = 3;
UPDATE adapted_message
  SET message_text = 'Demande de transfert n''est pas possible. (RC E10)', update_dt=sysdate, user_last_modify=user
  WHERE application_message_id=157  AND adapted_message_seq = 4;
--E11
UPDATE adapted_message
  SET message_text = 'We are not able to satisfy this request at this time. Please contact a Koodo rep and reference error message MTK. (RC E11)', update_dt=sysdate, user_last_modify=user
  WHERE application_message_id=207  AND adapted_message_seq = 1;
UPDATE adapted_message
  SET message_text = 'Il nous est impossible de satisfaire à votre demande pour le moment. Veuillez communiquer avec un représentant Koodo et lui indiquer le message d''erreur MTK. (RC E11)', update_dt=sysdate, user_last_modify=user
  WHERE application_message_id=207  AND adapted_message_seq = 2;

COMMIT;