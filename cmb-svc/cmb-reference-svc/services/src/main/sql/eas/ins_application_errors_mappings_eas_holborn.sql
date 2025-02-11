--E0
UPDATE adapted_message
  SET message_text = 'This number is eligible for transfer to [PCS Postpaid/Mike/Pay & Talk].', update_dt=sysdate, user_last_modify=user
  WHERE application_message_id=99  AND adapted_message_seq = 1;
UPDATE adapted_message
  SET message_text = 'Ce numéro peut être transféré au service [SCP postpayé/Mike/Payez & Parlez].', update_dt=sysdate, user_last_modify=user
  WHERE application_message_id=99  AND adapted_message_seq = 2;
UPDATE adapted_message
  SET message_text = 'Yes, this mobile phone number is eligible to be moved to Koodo Mobile.', update_dt=sysdate, user_last_modify=user
  WHERE application_message_id=99  AND adapted_message_seq = 3;
UPDATE adapted_message
  SET message_text = 'Oui, ce numéro de téléphone mobile peut être transféré au réseau de Koodo Mobile. ', update_dt=sysdate, user_last_modify=user
  WHERE application_message_id=99  AND adapted_message_seq = 4;
--E1  
UPDATE adapted_message
  SET message_text = 'Unfortunately, the client''s request can not be completed because this device''s telephone number is not eligible to move to the selected TELUS network.  Please advise the client they can select a new number from a different NPA-NXX to activate this device on the TELUS network or contact your TELUS Support Team for additional assistance.  <RC E1>', update_dt=sysdate, user_last_modify=user
  WHERE application_message_id=92  AND adapted_message_seq = 1;
UPDATE adapted_message
  SET message_text = 'Malheureusement, la demande du client ne peut être traitée parce que le numéro de téléphone de l''appareil en question ne peut être transféré au réseau TELUS sélectionné. Veuillez indiquer au client qu''il peut sélectionner un nouveau numéro avec indicatif régional différent pour mettre en service l''appareil sur le réseau TELUS ou communiquer avec votre représentant(e) de TELUS pour obtenir de l''aide. <RC E1>', update_dt=sysdate, user_last_modify=user
  WHERE application_message_id=92  AND adapted_message_seq = 2;
UPDATE adapted_message
  SET message_text = 'Unfortunately, this number isn''t eligible to move to the selected network. Please advise the customer that to switch to Koodo Mobile they can select a new number from a different NPA-NXX, or contact your Koodo Support Team for assistance <RC E1>', update_dt=sysdate, user_last_modify=user
  WHERE application_message_id=92  AND adapted_message_seq = 3;
UPDATE adapted_message
  SET message_text = 'Malheureusement, ce numéro ne peut pas être transféré au réseau de Koodo Mobile sélectionné. Veuillez indiquer au client qu’il peut sélectionner un nouveau numéro avec un indicatif régional différent pour mettre en service son appareil sur le réseau Koodo. <RC E1>', update_dt=sysdate, user_last_modify=user
  WHERE application_message_id=92  AND adapted_message_seq = 4;
--E2
UPDATE adapted_message
  SET message_text = 'Unfortunately, the client''s request can not be completed because this device''s telephone number is not eligible to move to the selected TELUS network.  Please advise the client they can select a new number from a different NPA-NXX to activate this device on the TELUS network or contact your TELUS Support Team for additional assistance. <RC E2>', update_dt=sysdate, user_last_modify=user
  WHERE application_message_id=93  AND adapted_message_seq = 1;
UPDATE adapted_message
  SET message_text = 'Malheureusement, la demande du client ne peut être traitée parce que le numéro de téléphone de l''appareil en question ne peut être transféré au réseau TELUS sélectionné. Veuillez indiquer au client qu''il peut sélectionner un nouveau numéro avec indicatif régional différent pour mettre en service l''appareil sur le réseau TELUS ou communiquer avec votre représentant(e) de TELUS pour obtenir de l''aide. <RC E2>', update_dt=sysdate, user_last_modify=user
  WHERE application_message_id=93  AND adapted_message_seq = 2;
UPDATE adapted_message
  SET message_text = 'Unfortunately, this number isn''t eligible to move to the selected network. Please advise the customer that to switch to Koodo Mobile they can select a new number from a different NPA-NXX, or contact your Koodo Support Team for assistance <RC E2>', update_dt=sysdate, user_last_modify=user
  WHERE application_message_id=93  AND adapted_message_seq = 3;
UPDATE adapted_message
  SET message_text = 'Malheureusement, ce numéro ne peut pas être transféré au réseau de Koodo Mobile sélectionné. Veuillez indiquer au client qu''il peut sélectionner un nouveau numéro avec un indicatif régional différent pour mettre en service son appareil sur le réseau Koodo. <RC E2>', update_dt=sysdate, user_last_modify=user
  WHERE application_message_id=93  AND adapted_message_seq = 4;
--E3
UPDATE adapted_message
  SET message_text = 'Unfortunately, the client''s request can not be completed because this device''s telephone number is not eligible to move to selected TELUS network. Please advise the client they can select a new number to activate this device on the TELUS network or contact your TELUS Support Team for additional assistance.  <RC E3>', update_dt=sysdate, user_last_modify=user
  WHERE application_message_id=94  AND adapted_message_seq = 1;
UPDATE adapted_message
  SET message_text = 'Malheureusement, la demande du client ne peut être traitée parce que le numéro de téléphone de l''appareil en question ne peut être transféré au réseau TELUS sélectionné. Veuillez indiquer au client qu''il peut sélectionner un nouveau numéro pour mettre en service l''appareil sur le réseau TELUS ou communiquer avec  votre représentant(e) de TELUS pour obtenir de l''aide. <RC E3>', update_dt=sysdate, user_last_modify=user
  WHERE application_message_id=94  AND adapted_message_seq = 2;
UPDATE adapted_message
  SET message_text = 'Unfortunately, this number isn''t eligible to move to the selected network. Please advise the customer that to switch to Koodo Mobile they can select a new number, or contact your Koodo Support Team for assistance. <RC E3>', update_dt=sysdate, user_last_modify=user
  WHERE application_message_id=94  AND adapted_message_seq = 3;
UPDATE adapted_message
  SET message_text = 'Malheureusement, ce numéro ne peut pas être transféré au réseau de Koodo Mobile sélectionné. Veuillez indiquer au client qu''il peut sélectionner un nouveau numéro pour mettre en service son appareil sur le réseau de Koodo Mobile ou communiquer avec l''équi.  <RC E3>', update_dt=sysdate, user_last_modify=user
  WHERE application_message_id=94  AND adapted_message_seq = 4;
--E5
UPDATE adapted_message
  SET message_text = 'Unfortunately,  we are experiencing difficulties in processing the client''s request. Please contact your TELUS Support Team for additional assistance.  <RC E5>', update_dt=sysdate, user_last_modify=user
  WHERE application_message_id=96  AND adapted_message_seq = 1;
UPDATE adapted_message
  SET message_text = 'Malheureusement, nous éprouvons des difficultés avec le traitement de la demande du client. Veuillez communiquer avec votre représentant(e) de TELUS pour obtenir de l''aide.  <RC E5>', update_dt=sysdate, user_last_modify=user
  WHERE application_message_id=96  AND adapted_message_seq = 2;
UPDATE adapted_message
  SET message_text = 'Unfortunately,  we are experiencing difficulties in processing the customer''s transfer request. Please contact your Koodo Support Team for assistance.  <RC E5>', update_dt=sysdate, user_last_modify=user
  WHERE application_message_id=96  AND adapted_message_seq = 3;
UPDATE adapted_message
  SET message_text = 'Nous éprouvons actuellement des difficultés et nous n''arrivons malheureusement pas à traiter la demande de transfert du client. Veuillez communiquer avec l''équipe du service à la clientèle de Koodo pour obtenir de l''aide. <RC E5>', update_dt=sysdate, user_last_modify=user
  WHERE application_message_id=96  AND adapted_message_seq = 4;
--E7
UPDATE adapted_message
  SET message_text = 'Unfortunately, this request can not be completed because there is no porting agreement between TELUS and the client''s current service provider. Please advise the client they can select a new number to activate this device on the TELUS network or  contact your TELUS Support Team for additional assistance.  <RC E7>', update_dt=sysdate, user_last_modify=user
  WHERE application_message_id=97  AND adapted_message_seq = 1;
UPDATE adapted_message
  SET message_text = 'Malheureusement, cette demande ne peut être traitée parce que TELUS n''a aucune entente de transfert avec le fournisseur de service actuel du client. Veuillez indiquer au client qu''il peut choisir un nouveau numéro pour mettre l''appareil en service sur le réseau TELUS ou communiquer avec  votre représentant(e) de TELUS pour obtenir de l''aide. <RC E7>', update_dt=sysdate, user_last_modify=user
  WHERE application_message_id=97  AND adapted_message_seq = 2;
UPDATE adapted_message
  SET message_text = 'Unfortunately, this number isn''t eligible to move to the selected network because there is no porting agreement with the cutomers current service provider. Please advise the customer that to switch to Koodo Mobile they can select a new number, or contact your Koodo Support Team for assistance. <RC E7>', update_dt=sysdate, user_last_modify=user
  WHERE application_message_id=97  AND adapted_message_seq = 3;
UPDATE adapted_message
  SET message_text = 'Malheureusement, il n''est pas possible de transférer ce numéro au réseau sélectionné, car il n''existe pas d''entente de transfert avec le fournisseur d''accès du client. Veuillez indiquer au client qu''il peut sélectionner un nouveau numéro pour mettre en se.  <RC E7>', update_dt=sysdate, user_last_modify=user
  WHERE application_message_id=97  AND adapted_message_seq = 4;
--E8
UPDATE adapted_message
  SET message_text = 'Unfortunately, the client''s request can not be processed because this telephone number is already an active [PCS Post-paid/Mike/Pay & Talk] subscriber.  Please advise the client they can select a new number to activate this device on the TELUS network or  contact your TELUS Support Team for additional assistance.  <RC E8>', update_dt=sysdate, user_last_modify=user
  WHERE application_message_id=98  AND adapted_message_seq = 1;
UPDATE adapted_message
  SET message_text = 'Malheureusement, la demande du client ne peut être traitée parce que le numéro de téléphone appartient déjà à un client du service [SCP postpayé/Mike/Payez & Parlez]. Veuillez indiquer au client qu''il peut sélectionner un nouveau numéro pour procéder à la mise en service de cet appareil sur le réseau TELUS ou communiquer avec votre représentant(e) de TELUS pour obtenir de l''aide. <RC E8>', update_dt=sysdate, user_last_modify=user
  WHERE application_message_id=98  AND adapted_message_seq = 2;
UPDATE adapted_message
  SET message_text = 'Unfortunately, this number isn''t eligible to move to the selected network. Please advise the customer that to switch to Koodo Mobile they can select a new number, or contact your Koodo Support Team for assistance. <RC E8>', update_dt=sysdate, user_last_modify=user
  WHERE application_message_id=98  AND adapted_message_seq = 3;
UPDATE adapted_message
  SET message_text = 'Malheureusement, il n''est pas possible de transférer ce numéro au réseau sélectionné, car il n''existe pas d''entente de transfert avec le fournisseur d''accès du client. Veuillez indiquer au client qu''il peut sélectionner un nouveau numéro pour mettre en se.  <RC E8>', update_dt=sysdate, user_last_modify=user
  WHERE application_message_id=98  AND adapted_message_seq = 4; 
--E9
UPDATE adapted_message
  SET message_text = 'A transfer request for this telephone number is already in progress.  Please use the ''check transfer status'' tool to view the progress of this port request.  <RC E9>', update_dt=sysdate, user_last_modify=user
  WHERE application_message_id=100  AND adapted_message_seq = 1;
UPDATE adapted_message
  SET message_text = 'Une demande de transfert est déjà en traitement pour ce numéro. Veuillez utiliser l''outil de vérification de l''état du transfert  pour connaître l''état de la demande.  <RC E9>', update_dt=sysdate, user_last_modify=user
  WHERE application_message_id=100  AND adapted_message_seq = 2;
DELETE FROM adapted_message where application_message_id=100 and adapted_message_seq in (3, 4) and brand_id=3;
INSERT INTO adapted_message
	VALUES (100, 3, 'The customer''s request to move to Koodo Mobile is in progress. Advise the customer that we will notify them of any changes to the status of the request.  <RC E9>', null, null, 'EN', 3, sysdate, sysdate, user, 3);
INSERT INTO adapted_message
	VALUES (100, 4, 'La demande de transfert du client au réseau de Koodo Mobile est en cours de traitement. Veuillez informer le client que nous l''aviserons de tout changement à l''état de sa demande.  <RC E9>', null, null, 'FR', 3, sysdate, sysdate, user, 3);
--E9A  
UPDATE adapted_message
  SET message_text = 'You''ve already submitted a request to move your number. Unfortunately, your new request can''t be processed. <RC E9A>', update_dt=sysdate, user_last_modify=user
  WHERE application_message_id=156  AND adapted_message_seq = 1;
UPDATE adapted_message
  SET message_text = 'Vous avez déjà soumis une demande de transfert de votre numéro. Malheureusement, votre nouvelle demande ne peut pas être traitée. <RC E9A>', update_dt=sysdate, user_last_modify=user
  WHERE application_message_id=156  AND adapted_message_seq = 2;
DELETE FROM adapted_message where application_message_id=156 and adapted_message_seq in (3, 4) and brand_id=3;
INSERT INTO adapted_message
	VALUES (156, 3, 'You''ve already submitted a request to move your number. Unfortunately, your new request can''t be processed. <RC E9A>', null, null, 'EN', 3, sysdate, sysdate, user, 3);
INSERT INTO adapted_message
	VALUES (156, 4, 'Vous avez déjà soumis une demande de transfert de votre numéro. Malheureusement, votre nouvelle demande ne peut pas être traitée. <RC E9A>', null, null, 'FR', 3, sysdate, sysdate, user, 3);
--E10
UPDATE adapted_message
  SET message_text = 'Porting not supported. <RC E10>', update_dt=sysdate, user_last_modify=user
  WHERE application_message_id=157  AND adapted_message_seq = 1;
UPDATE adapted_message
  SET message_text = 'Demande de transfert n''est pas possible. <RC E10>', update_dt=sysdate, user_last_modify=user
  WHERE application_message_id=157  AND adapted_message_seq = 2;
DELETE FROM adapted_message where application_message_id=157 and adapted_message_seq in (3, 4) and brand_id=3;
INSERT INTO adapted_message
	VALUES (157, 3, 'Porting not supported. <RC E10>', null, null, 'EN', 3, sysdate, sysdate, user, 3);
INSERT INTO adapted_message
	VALUES (157, 4, 'Demande de transfert n''est pas possible. <RC E10>', null, null, 'FR', 3, sysdate, sysdate, user, 3);

--E11
UPDATE adapted_message
  SET message_text = 'We are not able to satisfy this request at this time. Please contact a Koodo rep and reference error message MTK. <RC E11>', update_dt=sysdate, user_last_modify=user
  WHERE application_message_id=207  AND adapted_message_seq = 1;
UPDATE adapted_message
  SET message_text = 'Il nous est impossible de satisfaire à votre demande pour le moment. Veuillez communiquer avec un représentant Koodo et lui indiquer le message d''erreur MTK. <RC E11>', update_dt=sysdate, user_last_modify=user
  WHERE application_message_id=207  AND adapted_message_seq = 2;
COMMIT;