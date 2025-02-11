-- CLEAN-UP
DELETE FROM application_error;

DELETE FROM adapted_message where application_message_id >= 92 and application_message_id <= 153 AND adapted_message_seq in (1, 2);
									
DELETE FROM application_message
 WHERE application_message_id >= 92 and application_message_id <= 153 ;


-- E1
INSERT INTO application_message
VALUES (92, 'PORT_IN_ELIGIBILITY.', sysdate, sysdate, user);
INSERT INTO adapted_message
VALUES (92, 1, 'No, this number is not eligible to be transferred to TELUS because it is currently not transferrable. Client can call their current service provider for details or take a new number to activate with TELUS.', null, null, 'EN', 3, sysdate, sysdate, user);
INSERT INTO adapted_message
VALUES (92, 2, 'Non, ce numéro ne peut pas être transféré à TELUS parce que Il ne peut être transféré présentement. Le client peut appeler son fournisseur de services actuel pour obtenir plus de détails ou choisir un nouveau numéro de téléphone  pour mettre son appareil en service à TELUS.', null, null, 'FR', 3, sysdate, sysdate, user);
INSERT INTO application_error 
VALUES (15, 'E1', 92, 'PORT_IN_ELIGIBILITY.', sysdate, sysdate, user);

-- E2
INSERT INTO application_message
VALUES (93, 'PORT_IN_ELIGIBILITY.', sysdate, sysdate, user);
INSERT INTO adapted_message
VALUES (93, 1, 'No, this number is not eligible to be transferred to TELUS because Wireless Number Portability (WNP) is not currently supported in the client''s local calling area.', null, null, 'EN', 3, sysdate, sysdate, user);
INSERT INTO adapted_message
VALUES (93, 2, ' Non, ce numéro ne peut pas être transféré à TELUS parce que la transférabilité des numéros sans fil n''est actuellement pas offerte dans la zone d''appel local du client.', null, null, 'FR', 3, sysdate, sysdate, user);
INSERT INTO application_error 
VALUES (15, 'E2', 93, 'PORT_IN_ELIGIBILITY.', sysdate, sysdate, user);

-- E3
INSERT INTO application_message
VALUES (94, 'PORT_IN_ELIGIBILITY.', sysdate, sysdate, user);
INSERT INTO adapted_message
VALUES (94, 1, 'No, this number is not eligible to be transferred to TELUS because we do not currently provide service in the client''s local calling area.', null, null, 'EN', 3, sysdate, sysdate, user);
INSERT INTO adapted_message
VALUES (94, 2, ' Non, ce numéro ne peut pas être transféré à TELUS parce que le service n''est pas offert dans la zone d''appel local du client.', null, null, 'FR', 3, sysdate, sysdate, user);
INSERT INTO application_error 
VALUES (15, 'E3', 94, 'PORT_IN_ELIGIBILITY.', sysdate, sysdate, user);

-- E4
INSERT INTO application_message
VALUES (95, 'PORT_IN_ELIGIBILITY.', sysdate, sysdate, user);
INSERT INTO adapted_message
VALUES (95, 1, 'Wireless or intermodal port.', null, null, 'EN', 3, sysdate, sysdate, user);
INSERT INTO adapted_message
VALUES (95, 2, 'Wireless or intermodal port.', null, null, 'FR', 3, sysdate, sysdate, user);
INSERT INTO application_error 
VALUES (15, 'E4', 95, 'PORT_IN_ELIGIBILITY.', sysdate, sysdate, user);

-- E5
INSERT INTO application_message
VALUES (96, 'PORT_IN_ELIGIBILITY.', sysdate, sysdate, user);
INSERT INTO adapted_message
VALUES (96, 1, 'No, this number is not eligible to be transferred to TELUS because it is currently not transferrable. Client can call their current service provider for details or take a new number to activate with TELUS.', null, null, 'EN', 3, sysdate, sysdate, user);
INSERT INTO adapted_message
VALUES (96, 2, 'Non, ce numéro ne peut pas être transféré à TELUS parce que Il ne peut être transféré présentement. Le client peut appeler son fournisseur de services actuel pour obtenir plus de détails ou choisir un nouveau numéro de téléphone  pour mettre son appareil en service à TELUS.', null, null, 'FR', 3, sysdate, sysdate, user);
INSERT INTO application_error 
VALUES (15, 'E5', 96, 'PORT_IN_ELIGIBILITY.', sysdate, sysdate, user);

-- E7
INSERT INTO application_message
VALUES (97, 'PORT_IN_ELIGIBILITY.', sysdate, sysdate, user);
INSERT INTO adapted_message
VALUES (97, 1, 'No, this number is not eligible to be transferred to TELUS because there is no agreement in place to allow for number transfers between TELUS and the client''s current service provider.', null, null, 'EN', 3, sysdate, sysdate, user);
INSERT INTO adapted_message
VALUES (97, 2, ' Non, ce numéro ne peut pas être transféré à TELUS parce que il n''existe aucune entente permettant le transfert des numéros entre TELUS et le fournisseur de service actuel du client.', null, null, 'FR', 3, sysdate, sysdate, user);
INSERT INTO application_error 
VALUES (15, 'E7', 97, 'PORT_IN_ELIGIBILITY.', sysdate, sysdate, user);

-- E8
INSERT INTO application_message
VALUES (98, 'PORT_IN_ELIGIBILITY.', sysdate, sysdate, user);
INSERT INTO adapted_message
VALUES (98, 1, 'No, this number is not eligible to be transferred to TELUS because it is already active on our network.', null, null, 'EN', 3, sysdate, sysdate, user);
INSERT INTO adapted_message
VALUES (98, 2, ' Non, ce numéro ne peut pas être transféré à TELUS parce que il est déjà en service sur notre réseau.', null, null, 'FR', 3, sysdate, sysdate, user);
INSERT INTO application_error 
VALUES (15, 'E8', 98, 'PORT_IN_ELIGIBILITY.', sysdate, sysdate, user);

-- E0
INSERT INTO application_message
VALUES (99, 'PORT_IN_ELIGIBILITY.', sysdate, sysdate, user);
INSERT INTO adapted_message
VALUES (99, 1, 'Yes, this number is eligible to be transferred to [PCS Postpaid/Mike/Pay & Talk].  [note: insert appropriate product type(s) with punctuation, as necessary].', null, null, 'EN', 3, sysdate, sysdate, user);
INSERT INTO adapted_message
VALUES (99, 2, 'Oui, ce numéro peut être transféré à un appareil SCP avec service postpayé/Mike/ SCP avec service Payez & Parlez  [remarque : inscrivez le ou les types de produit appropriés et les signes de ponctuation requis].', null, null, 'FR', 3, sysdate, sysdate, user);
INSERT INTO application_error 
VALUES (15, 'E0', 99, 'PORT_IN_ELIGIBILITY.', sysdate, sysdate, user);

-- E9
INSERT INTO application_message
VALUES (100, 'TN is being ported already.', sysdate, sysdate, user);
INSERT INTO adapted_message
VALUES (100, 1, 'A transfer request for this number is already in progress.  Use the transfer status tool to check the transfer progress of this number.', null, null, 'EN', 3, sysdate, sysdate, user);
INSERT INTO adapted_message
VALUES (100, 2, 'Une demande de transfert de ce numéro de téléphone est déjà en cours de traitement. Utilisez l''outil à cet effet pour vérifier la situation liée au transfert de ce numéro.', null, null, 'FR', 3, sysdate, sysdate, user);
INSERT INTO application_error 
VALUES (15, 'E9', 100, 'TN is being ported already.', sysdate, sysdate, user);

-- E999
INSERT INTO application_message
VALUES (101, 'Catch All.', sysdate, sysdate, user);
INSERT INTO adapted_message
VALUES (101, 1, 'Your request could not be completed.  Please try again later.', null, null, 'EN', 3, sysdate, sysdate, user);
INSERT INTO adapted_message
VALUES (101, 2, 'Impossible de traiter votre demande.   S''il vous plait essai encore plus tard.', null, null, 'FR', 3, sysdate, sysdate, user);
INSERT INTO application_error 
VALUES (15, 'E999', 101, 'Catch All.', sysdate, sysdate, user);

-- ST1
INSERT INTO application_message
VALUES (102, 'STATUS_CODE.', sysdate, sysdate, user);
INSERT INTO adapted_message
VALUES (102, 1, 'Your transfer request has been completed successfully. Welcome to TELUS and thank you for joining us!', null, null, 'EN', 3, sysdate, sysdate, user);
INSERT INTO adapted_message
VALUES (102, 2, 'Votre demande de transfert a été complétée avec succès. Bienvenue chez TELUS et merci de vous joindre à nous!', null, null, 'FR', 3, sysdate, sysdate, user);
INSERT INTO application_error 
VALUES (15, 'ST1', 102, 'STATUS_CODE.', sysdate, sysdate, user);

-- ST2
INSERT INTO application_message
VALUES (103, 'STATUS_CODE.', sysdate, sysdate, user);
INSERT INTO adapted_message
VALUES (103, 1, 'Your transfer request is active.  Please call us at 1-877-TO-TELUS and select the ''finish my transfer'' option.', null, null, 'EN', 3, sysdate, sysdate, user);
INSERT INTO adapted_message
VALUES (103, 2, 'Votre demande de transfert est en cours de traitement. Veuillez appeler au 1-877- 868-3587 et sélectionner l''option "terminer mon transfert".', null, null, 'FR', 3, sysdate, sysdate, user);
INSERT INTO application_error 
VALUES (15, 'ST2', 103, 'STATUS_CODE.', sysdate, sysdate, user);

-- ST3
INSERT INTO application_message
VALUES (104, 'STATUS_CODE.', sysdate, sysdate, user);
INSERT INTO adapted_message
VALUES (104, 1, 'Your transfer request is in progress. We will notify you of any changes to the status of your request. Thank you for your patience.', null, null, 'EN', 3, sysdate, sysdate, user);
INSERT INTO adapted_message
VALUES (104, 2, ' Votre demande de transfert est en cours de traitement. Nous vous aviserons de tout changement de statut de votre demande. Merci de votre patience.', null, null, 'FR', 3, sysdate, sysdate, user);
INSERT INTO application_error 
VALUES (15, 'ST3', 104, 'STATUS_CODE.', sysdate, sysdate, user);

-- ST4
INSERT INTO application_message
VALUES (105, 'STATUS_CODE.', sysdate, sysdate, user);
INSERT INTO adapted_message
VALUES (105, 1, 'We are experiencing difficulties in processing your transfer request. Please call us at 1-877-TO-TELUS and select the ''resolve an issue'' option. Thank you.', null, null, 'EN', 3, sysdate, sysdate, user);
INSERT INTO adapted_message
VALUES (105, 2, 'Nous éprouvons des difficultés dans le traitement de votre demande de transfert. Veuillez nous appeler au 1-877- 868-3587   et sélectionner l''option " résoudre un problème ". Merci.', null, null, 'FR', 3, sysdate, sysdate, user);
INSERT INTO application_error 
VALUES (15, 'ST4', 105, 'STATUS_CODE.', sysdate, sysdate, user);

-- ST5
INSERT INTO application_message
VALUES (106, 'STATUS_CODE.', sysdate, sysdate, user);
INSERT INTO adapted_message
VALUES (106, 1, 'We could not locate a transfer request for this number. Please call us at 1-877-253-2763 if you wish to transfer this number to TELUS.', null, null, 'EN', 3, sysdate, sysdate, user);
INSERT INTO adapted_message
VALUES (106, 2, 'Nous ne pouvons pas localiser la demande de transfert pour ce numéro. Veuillez appeler au 1-877-253-2763 si vous désirez transférer ce numéro chez TELUS.', null, null, 'FR', 3, sysdate, sysdate, user);
INSERT INTO application_error 
VALUES (15, 'ST5', 106, 'STATUS_CODE.', sysdate, sysdate, user);

-- ST6
INSERT INTO application_message
VALUES (107, 'STATUS_CODE.', sysdate, sysdate, user);
INSERT INTO adapted_message
VALUES (107, 1, 'Please call us at 1-877-253-2763 for the status of your request. Thank you.', null, null, 'EN', 3, sysdate, sysdate, user);
INSERT INTO adapted_message
VALUES (107, 2, 'Veuillez appeler au 1-877-253-2763 pour connaître le statut de votre demande. Merci.', null, null, 'FR', 3, sysdate, sysdate, user);
INSERT INTO application_error 
VALUES (15, 'ST6', 107, 'STATUS_CODE.', sysdate, sysdate, user);

-- ST7
INSERT INTO application_message
VALUES (108, 'STATUS_CODE.', sysdate, sysdate, user);
INSERT INTO adapted_message
VALUES (108, 1, 'This transfer request was cancelled.', null, null, 'EN', 3, sysdate, sysdate, user);
INSERT INTO adapted_message
VALUES (108, 2, 'La demande de transfert a été annulée.', null, null, 'FR', 3, sysdate, sysdate, user);
INSERT INTO application_error 
VALUES (15, 'ST7', 108, 'STATUS_CODE.', sysdate, sysdate, user);

-- ST8
INSERT INTO application_message
VALUES (109, 'STATUS_CODE.', sysdate, sysdate, user);
INSERT INTO adapted_message
VALUES (109, 1, 'Please call us at 1-877-253-2763 for the status of your request. Thank you.', null, null, 'EN', 3, sysdate, sysdate, user);
INSERT INTO adapted_message
VALUES (109, 2, 'Veuillez appeler au 1-877-253-2763 pour connaître le statut de votre demande. Merci.', null, null, 'FR', 3, sysdate, sysdate, user);
INSERT INTO application_error 
VALUES (15, 'ST8', 109, 'STATUS_CODE.', sysdate, sysdate, user);

-- SYS-000
INSERT INTO application_message
VALUES (110, 'General System Error', sysdate, sysdate, user);
INSERT INTO adapted_message
VALUES (110, 1, 'General System Error.', null, null, 'EN', 3, sysdate, sysdate, user);
INSERT INTO adapted_message
VALUES (110, 2, 'Erreur système générale.', null, null, 'FR', 3, sysdate, sysdate, user);
INSERT INTO application_error 
VALUES (15, 'SYS-000', 110, 'General System Error.', sysdate, sysdate, user);

-- PV-010
INSERT INTO application_message
VALUES (111, 'Missing Required Field', sysdate, sysdate, user);
INSERT INTO adapted_message
VALUES (111, 1, 'Missing Required Field', null, null, 'EN', 3, sysdate, sysdate, user);
INSERT INTO adapted_message
VALUES (111, 2, 'Ce champ obligatoire n''est pas rempli.', null, null, 'FR', 3, sysdate, sysdate, user);
INSERT INTO application_error 
VALUES (15, 'PV-010', 111, 'Missing Required Field', sysdate, sysdate, user);

-- PV-011
INSERT INTO application_message
VALUES (112, 'Failed Data Format Validation', sysdate, sysdate, user);
INSERT INTO adapted_message
VALUES (112, 1, 'Failed Data Format Validation.', null, null, 'EN', 3, sysdate, sysdate, user);
INSERT INTO adapted_message
VALUES (112, 2, 'La validation du format de données a échoué.', null, null, 'FR', 3, sysdate, sysdate, user);
INSERT INTO application_error 
VALUES (15, 'PV-011', 112, 'Failed Data Format Validation', sysdate, sysdate, user);

-- PV-030
INSERT INTO application_message
VALUES (113, 'WICIS Release Number must be 3.0.0', sysdate, sysdate, user);
INSERT INTO adapted_message
VALUES (113, 1, 'WICIS Release Number must be 3.0.0 [PV-030].', null, null, 'EN', 3, sysdate, sysdate, user);
INSERT INTO adapted_message
VALUES (113, 2, 'WICIS Release Number must be 3.0.0 [PV-030].', null, null, 'FR', 3, sysdate, sysdate, user);
INSERT INTO application_error 
VALUES (15, 'PV-030', 113, 'WICIS Release Number must be 3.0.0', sysdate, sysdate, user);

-- PV-031
INSERT INTO application_message
VALUES (114, 'SMG Request Number and Response Number cannot be null', sysdate, sysdate, user);
INSERT INTO adapted_message
VALUES (114, 1, 'SMG Request Number and Response Number cannot be null [PV-031].', null, null, 'EN', 3, sysdate, sysdate, user);
INSERT INTO adapted_message
VALUES (114, 2, 'SMG Request Number and Response Number cannot be null [PV-031].', null, null, 'FR', 3, sysdate, sysdate, user);
INSERT INTO application_error 
VALUES (15, 'PV-031', 114, 'SMG Request Number and Response Number cannot be null', sysdate, sysdate, user);

-- PV-032
INSERT INTO application_message
VALUES (115, 'SMG Request Number, Request Version Number, and SMG Response Number cannot be null', sysdate, sysdate, user);
INSERT INTO adapted_message
VALUES (115, 1, 'SMG Request Number, Request Version Number, and SMG Response Number cannot be null [PV-032].', null, null, 'EN', 3, sysdate, sysdate, user);
INSERT INTO adapted_message
VALUES (115, 2, 'SMG Request Number, Request Version Number, and SMG Response Number cannot be null [PV-032].', null, null, 'FR', 3, sysdate, sysdate, user);
INSERT INTO application_error
VALUES (15, 'PV-032', 115, 'SMG Request Number, Request Version Number, and SMG Response Number cannot be null', sysdate, sysdate, user);

-- PV-040
INSERT INTO application_message
VALUES (116, 'Old Network SP is invalid or missing; Required Format={0}; Actual Value={1}', sysdate, sysdate, user);
INSERT INTO adapted_message
VALUES (116, 1, 'Old Network SP is invalid or missing; Required Format={0}; Actual Value={1} [PV-040].', null, null, 'EN', 3, sysdate, sysdate, user);
INSERT INTO adapted_message
VALUES (116, 2, 'Old Network SP is invalid or missing; Required Format={0}; Actual Value={1} [PV-040].', null, null, 'FR', 3, sysdate, sysdate, user);
INSERT INTO application_error
VALUES (15, 'PV-040', 116, 'Old Network SP is invalid or missing; Required Format={0}; Actual Value={1}', sysdate, sysdate, user);

-- PV-041
INSERT INTO application_message
VALUES (117, 'New Network SP is invalid or missing; Required Format={0}; Actual Value={1}', sysdate, sysdate, user);
INSERT INTO adapted_message
VALUES (117, 1, 'New Network SP is invalid or missing; Required Format={0}; Actual Value={1} [PV-041].', null, null, 'EN', 3, sysdate, sysdate, user);
INSERT INTO adapted_message
VALUES (117, 2, 'New Network SP is invalid or missing; Required Format={0}; Actual Value={1} [PV-041].', null, null, 'FR', 3, sysdate, sysdate, user);
INSERT INTO application_error
VALUES (15, 'PV-041', 117, 'New Network SP is invalid or missing; Required Format={0}; Actual Value={1}', sysdate, sysdate, user);

-- PV-401
INSERT INTO application_message
VALUES (118, 'Delay Code is invalid or missing for Response Type=D', sysdate, sysdate, user);
INSERT INTO adapted_message
VALUES (118, 1, 'Delay Code is invalid or missing for Response Type=D [PV-401].', null, null, 'EN', 3, sysdate, sysdate, user);
INSERT INTO adapted_message
VALUES (118, 2, 'Delay Code is invalid or missing for Response Type=D [PV-401].', null, null, 'FR', 3, sysdate, sysdate, user);
INSERT INTO application_error
VALUES (15, 'PV-401', 118, 'Delay Code is invalid or missing for Response Type=D', sysdate, sysdate, user);

-- PV-042
INSERT INTO application_message
VALUES (119, 'New Local SP is invalid or missing; Required Format={0}; Actual Value={1}', sysdate, sysdate, user);
INSERT INTO adapted_message
VALUES (119, 1, 'New Local SP is invalid or missing; Required Format={0}; Actual Value={1} [PV-042].', null, null, 'EN', 3, sysdate, sysdate, user);
INSERT INTO adapted_message
VALUES (119, 2, 'New Local SP is invalid or missing; Required Format={0}; Actual Value={1} [PV-042].', null, null, 'FR', 3, sysdate, sysdate, user);
INSERT INTO application_error
VALUES (15, 'PV-042', 119, 'New Local SP is invalid or missing; Required Format={0}; Actual Value={1}', sysdate, sysdate, user);

-- PV-043
INSERT INTO application_message
VALUES (120, 'Old Network SP, New Network SP and New Local SP cannot be changed in SPR', sysdate, sysdate, user);
INSERT INTO adapted_message
VALUES (120, 1, 'Old Network SP, New Network SP and New Local SP cannot be changed in SPR [PV-043].', null, null, 'EN', 3, sysdate, sysdate, user);
INSERT INTO adapted_message
VALUES (120, 2, 'Old Network SP, New Network SP and New Local SP cannot be changed in SPR [PV-043].', null, null, 'FR', 3, sysdate, sysdate, user);
INSERT INTO application_error
VALUES (15, 'PV-043', 120, 'Old Network SP, New Network SP and New Local SP cannot be changed in SPR', sysdate, sysdate, user);

-- PV-044
INSERT INTO application_message
VALUES (121, 'Old Local SP is invalid or missing; Required Format={0}; Actual Value={1}', sysdate, sysdate, user);
INSERT INTO adapted_message
VALUES (121, 1, 'Old Local SP is invalid or missing; Required Format={0}; Actual Value={1} [PV-044].', null, null, 'EN', 3, sysdate, sysdate, user);
INSERT INTO adapted_message
VALUES (121, 2, 'Old Local SP is invalid or missing; Required Format={0}; Actual Value={1} [PV-044].', null, null, 'FR', 3, sysdate, sysdate, user);
INSERT INTO application_error
VALUES (15, 'PV-044', 121, 'Old Local SP is invalid or missing; Required Format={0}; Actual Value={1}', sysdate, sysdate, user);

-- PV-050
INSERT INTO application_message
VALUES (122, 'Invalid WPR/SPR Type value; Required={0}; Actual={1}', sysdate, sysdate, user);
INSERT INTO adapted_message
VALUES (122, 1, 'Invalid WPR/SPR Type value; Required={0}; Actual={1} [PV-050].', null, null, 'EN', 3, sysdate, sysdate, user);
INSERT INTO adapted_message
VALUES (122, 2, 'Invalid WPR/SPR Type value; Required={0}; Actual={1} [PV-050].', null, null, 'FR', 3, sysdate, sysdate, user);
INSERT INTO application_error
VALUES (15, 'PV-050', 122, 'Invalid WPR/SPR Type value; Required={0}; Actual={1}', sysdate, sysdate, user);

-- PV-060
INSERT INTO application_message
VALUES (123, 'Invalid Number Port Direction Indicator value; Required Format={0}; Actual={1}', sysdate, sysdate, user);
INSERT INTO adapted_message
VALUES (123, 1, 'Invalid Number Port Direction Indicator value; Required Format={0}; Actual={1} [PV-060].', null, null, 'EN', 3, sysdate, sysdate, user);
INSERT INTO adapted_message
VALUES (123, 2, 'Invalid Number Port Direction Indicator value; Required Format={0}; Actual={1} [PV-060].', null, null, 'FR', 3, sysdate, sysdate, user);
INSERT INTO application_error
VALUES (15, 'PV-060', 123, 'Invalid Number Port Direction Indicator value; Required Format={0}; Actual={1}', sysdate, sysdate, user);

-- PV-061
INSERT INTO application_message
VALUES (124, 'Number Port Direction cannot be changed in SPR', sysdate, sysdate, user);
INSERT INTO adapted_message
VALUES (124, 1, 'Number Port Direction cannot be changed in SPR [PV-061].', null, null, 'EN', 3, sysdate, sysdate, user);
INSERT INTO adapted_message
VALUES (124, 2, 'Number Port Direction cannot be changed in SPR [PV-061].', null, null, 'FR', 3, sysdate, sysdate, user);
INSERT INTO application_error
VALUES (15, 'PV-061', 124, 'Number Port Direction cannot be changed in SPR', sysdate, sysdate, user);

-- PV-070
INSERT INTO application_message
VALUES (125, 'Invalid Desired/Date Time value. Must be at least 2.5 hours', sysdate, sysdate, user);
INSERT INTO adapted_message
VALUES (125, 1, 'Invalid Desired/Date Time value. Must be at least 2.5 hours [PV-070].', null, null, 'EN', 3, sysdate, sysdate, user);
INSERT INTO adapted_message
VALUES (125, 2, 'Invalid Desired/Date Time value. Must be at least 2.5 hours [PV-070].', null, null, 'FR', 3, sysdate, sysdate, user);
INSERT INTO application_error
VALUES (15, 'PV-070', 125, 'Invalid Desired/Date Time value. Must be at least 2.5 hours', sysdate, sysdate, user);

-- PV-071
INSERT INTO application_message
VALUES (126, 'Invalid Desired/Date Time value. Must be at least 48 hours', sysdate, sysdate, user);
INSERT INTO adapted_message
VALUES (126, 1, 'Invalid Desired/Date Time value. Must be at least 48 hours [PV-071].', null, null, 'EN', 3, sysdate, sysdate, user);
INSERT INTO adapted_message
VALUES (126, 2, 'Invalid Desired/Date Time value. Must be at least 48 hours [PV-071].', null, null, 'FR', 3, sysdate, sysdate, user);
INSERT INTO application_error
VALUES (15, 'PV-071', 126, 'Invalid Desired/Date Time value. Must be at least 48 hours', sysdate, sysdate, user);

-- PV-080
INSERT INTO application_message
VALUES (127, 'Please verify valid values are used for Co-ordinates Hot Cut that is Y, N or Not Populated.', sysdate, sysdate, user);
INSERT INTO adapted_message
VALUES (127, 1, 'Please verify valid values are used for Co-ordinates Hot Cut that is Y, N or Not Populated.', null, null, 'EN', 3, sysdate, sysdate, user);
INSERT INTO adapted_message
VALUES (127, 2, 'La valeur du transfert simultané coordonné n''est pas valide.', null, null, 'FR', 3, sysdate, sysdate, user);
INSERT INTO application_error
VALUES (15, 'PV-080', 127, 'Please verify valid values are used for Co-ordinates Hot Cut that is Y, N or Not Populated.', sysdate, sysdate, user);

-- PV-090
INSERT INTO application_message
VALUES (128, 'Please verfify agency authorization field is filled incorrectly.', sysdate, sysdate, user);
INSERT INTO adapted_message
VALUES (128, 1, 'Please verfify agency authorization field is filled incorrectly.', null, null, 'EN', 3, sysdate, sysdate, user);
INSERT INTO adapted_message
VALUES (128, 2, 'L''autorisation de l''agence n''est pas indiqué.', null, null, 'FR', 3, sysdate, sysdate, user);
INSERT INTO application_error
VALUES (15, 'PV-090', 128, 'Please verfify agency authorization field is filled incorrectly.', sysdate, sysdate, user);

-- PV-091
INSERT INTO application_message
VALUES (129, 'Please verfify agency authorization field is filled incorrectly.', sysdate, sysdate, user);
INSERT INTO adapted_message
VALUES (129, 1, 'Please verfify agency authorization field is filled incorrectly.', null, null, 'EN', 3, sysdate, sysdate, user);
INSERT INTO adapted_message
VALUES (129, 2, 'Le nom d''autorisation de l''agence n''est pas valide ou n''est pas indiqué.', null, null, 'FR', 3, sysdate, sysdate, user);
INSERT INTO application_error
VALUES (15, 'PV-091', 129, 'Please verfify agency authorization field is filled incorrectly.', sysdate, sysdate, user);

-- PV-092
INSERT INTO application_message
VALUES (130, 'Please verfify agency authorization date field is filled incorrectly.', sysdate, sysdate, user);
INSERT INTO adapted_message
VALUES (130, 1, 'Please verfify agency authorization date field is filled incorrectly.', null, null, 'EN', 3, sysdate, sysdate, user);
INSERT INTO adapted_message
VALUES (130, 2, 'La date d''autorisation de l''agence n''est pas valide ou n''est pas indiquée.', null, null, 'FR', 3, sysdate, sysdate, user);
INSERT INTO application_error
VALUES (15, 'PV-092', 130, 'Please verfify agency authorization date field is filled incorrectly.', sysdate, sysdate, user);

-- PV-110
INSERT INTO application_message
VALUES (131, 'Initiator Representative is invalid or missing.', sysdate, sysdate, user);
INSERT INTO adapted_message
VALUES (131, 1, 'Initiator Representative is invalid or missing.', null, null, 'EN', 3, sysdate, sysdate, user);
INSERT INTO adapted_message
VALUES (131, 2, 'Le nom du représentant ayant fait la demande initiale n''est pas valide ou n''est pas indiqué.', null, null, 'FR', 3, sysdate, sysdate, user);
INSERT INTO application_error
VALUES (15, 'PV-110', 131, 'Initiator Representative is invalid or missing.', sysdate, sysdate, user);

-- PV-111
INSERT INTO application_message
VALUES (132, 'Implementation Contact is invalid or missing; Required Format={0}; Actual Value={1}', sysdate, sysdate, user);
INSERT INTO adapted_message
VALUES (132, 1, 'Implementation Contact is invalid or missing; Required Format={0}; Actual Value={1} [PV-111].', null, null, 'EN', 3, sysdate, sysdate, user);
INSERT INTO adapted_message
VALUES (132, 2, 'Implementation Contact is invalid or missing; Required Format={0}; Actual Value={1} [PV-111].', null, null, 'FR', 3, sysdate, sysdate, user);
INSERT INTO application_error
VALUES (15, 'PV-111', 132, 'Implementation Contact is invalid or missing; Required Format={0}; Actual Value={1}', sysdate, sysdate, user);

-- PV-112
INSERT INTO application_message
VALUES (133, 'Implementation Contact Telephone Number is invalid or missing; Required Format={0}; Actual Value={1}', sysdate, sysdate, user);
INSERT INTO adapted_message
VALUES (133, 1, 'Implementation Contact Telephone Number is invalid or missing; Required Format={0}; Actual Value={1} [PV-112].', null, null, 'EN', 3, sysdate, sysdate, user);
INSERT INTO adapted_message
VALUES (133, 2, 'Implementation Contact Telephone Number is invalid or missing; Required Format={0}; Actual Value={1} [PV-112].', null, null, 'FR', 3, sysdate, sysdate, user);
INSERT INTO application_error
VALUES (15, 'PV-112', 133, 'Implementation Contact Telephone Number is invalid or missing; Required Format={0}; Actual Value={1}', sysdate, sysdate, user);

-- PV-115
INSERT INTO application_message
VALUES (134, 'Representative Name and Contact Telephone Number is missing or invalid.', sysdate, sysdate, user);
INSERT INTO adapted_message
VALUES (134, 1, 'Representative Name and Contact Telephone Number is missing or invalid. [PV-115].', null, null, 'EN', 3, sysdate, sysdate, user);
INSERT INTO adapted_message
VALUES (134, 2, 'Representative Name and Contact Telephone Number is missing or invalid. [PV-115].', null, null, 'FR', 3, sysdate, sysdate, user);
INSERT INTO application_error
VALUES (15, 'PV-115', 134, 'Representative Name and Contact Telephone Number is missing or invalid.', sysdate, sysdate, user);

-- PV-150
INSERT INTO application_message
VALUES (135, 'Either Billing First Name/Last Name or Business Name must be provided.', sysdate, sysdate, user);
INSERT INTO adapted_message
VALUES (135, 1, 'Either Billing First Name/Last Name or Business Name must be provided.', null, null, 'EN', 3, sysdate, sysdate, user);
INSERT INTO adapted_message
VALUES (135, 2, 'Vous devez indiquer soit le prénom et le nom de famille, soit la dénomination sociale pour l''envoi du relevé.', null, null, 'FR', 3, sysdate, sysdate, user);
INSERT INTO application_error
VALUES (15, 'PV-150', 135, 'Either Billing First Name/Last Name or Business Name must be provided.', sysdate, sysdate, user);

-- PV-151
INSERT INTO application_message
VALUES (136, 'Billing Street Name is invalid or missing.', sysdate, sysdate, user);
INSERT INTO adapted_message
VALUES (136, 1, 'Billing Street Name is invalid or missing.', null, null, 'EN', 3, sysdate, sysdate, user);
INSERT INTO adapted_message
VALUES (136, 2, 'Le nom de la rue indiqué pour la facturation n''est pas valide ou n''est pas indiqué.', null, null, 'FR', 3, sysdate, sysdate, user);
INSERT INTO application_error
VALUES (15, 'PV-151', 136, 'Billing Street Name is invalid or missing.', sysdate, sysdate, user);

-- PV-152
INSERT INTO application_message
VALUES (137, 'Please verify your Province.', sysdate, sysdate, user);
INSERT INTO adapted_message
VALUES (137, 1, 'Please verify your Province.', null, null, 'EN', 3, sysdate, sysdate, user);
INSERT INTO adapted_message
VALUES (137, 2, 'Veuillez vérifier le nom de votre province.', null, null, 'FR', 3, sysdate, sysdate, user);
INSERT INTO application_error
VALUES (15, 'PV-152', 137, 'Please verify your Province.', sysdate, sysdate, user);

-- PV-153
INSERT INTO application_message
VALUES (138, 'Please verify your Postal Code.', sysdate, sysdate, user);
INSERT INTO adapted_message
VALUES (138, 1, 'Please verify your Postal Code.', null, null, 'EN', 3, sysdate, sysdate, user);
INSERT INTO adapted_message
VALUES (138, 2, 'Veuillez vérifier votre code postal.', null, null, 'FR', 3, sysdate, sysdate, user);
INSERT INTO application_error
VALUES (15, 'PV-153', 138, 'Please verify your Postal Code.', sysdate, sysdate, user);

-- PV-200
INSERT INTO application_message
VALUES (139, 'Account Number, PIN or ESN must be provided for Single-line WPR', sysdate, sysdate, user);
INSERT INTO adapted_message
VALUES (139, 1, 'Please provide at least one piece of information from your Old Service Provider (Account #, Password/PIN or Equpment Serial Number).', null, null, 'EN', 3, sysdate, sysdate, user);
INSERT INTO adapted_message
VALUES (139, 2, 'Veuillez fournir au moins un ancien numéro de compte d''un fournisseur de service, le mot de passe ou le NIP ou encore le numéro de série de l''appareil.', null, null, 'FR', 3, sysdate, sysdate, user);
INSERT INTO application_error
VALUES (15, 'PV-200', 139, 'Account Number, PIN or ESN must be provided for Single-line WPR', sysdate, sysdate, user);

-- PV-201
INSERT INTO application_message
VALUES (140, 'ESN cannot be used for Multi-line WPR.', sysdate, sysdate, user);
INSERT INTO adapted_message
VALUES (140, 1, 'ESN cannot be used for Multi-line WPR.', null, null, 'EN', 3, sysdate, sysdate, user);
INSERT INTO adapted_message
VALUES (140, 2, 'Le numéro ESN ne peut être utilisé pour les demandes de transfert de numéro sans fil sur des lignes multiples.', null, null, 'FR', 3, sysdate, sysdate, user);
INSERT INTO application_error
VALUES (15, 'PV-201', 140, 'ESN cannot be used for Multi-line WPR.', sysdate, sysdate, user);

-- PV-220
INSERT INTO application_message
VALUES (141, 'Number Port Quantity is invalid or missing.', sysdate, sysdate, user);
INSERT INTO adapted_message
VALUES (141, 1, 'Number Port Quantity is invalid or missing.', null, null, 'EN', 3, sysdate, sysdate, user);
INSERT INTO adapted_message
VALUES (141, 2, 'La quantité de transferts de numéro n''est pas valide ou n''est pas indiquée.', null, null, 'FR', 3, sysdate, sysdate, user);
INSERT INTO application_error
VALUES (15, 'PV-220', 141, 'Number Port Quantity is invalid or missing.', sysdate, sysdate, user);

-- PV-221
INSERT INTO application_message
VALUES (142, 'Please confirm how many lines are involved in porting request.', sysdate, sysdate, user);
INSERT INTO adapted_message
VALUES (142, 1, 'Please confirm how many lines are involved in porting request.', null, null, 'EN', 3, sysdate, sysdate, user);
INSERT INTO adapted_message
VALUES (142, 2, 'La quantité de transferts de numéro ne correspond pas aux numéros de lignes.', null, null, 'FR', 3, sysdate, sysdate, user);
INSERT INTO application_error
VALUES (15, 'PV-221', 142, 'Please confirm how many lines are involved in porting request.', sysdate, sysdate, user);

-- PV-222
INSERT INTO application_message
VALUES (143, 'Please verfify Mobile Directory Number field is filled in correctly. ', sysdate, sysdate, user);
INSERT INTO adapted_message
VALUES (143, 1, 'Please verfify Mobile Directory Number field is filled in correctly. ', null, null, 'EN', 3, sysdate, sysdate, user);
INSERT INTO adapted_message
VALUES (143, 2, 'La valeur Numéro de téléphone n''est pas valide.', null, null, 'FR', 3, sysdate, sysdate, user);
INSERT INTO application_error
VALUES (15, 'PV-222', 143, 'Please verfify Mobile Directory Number field is filled in correctly. ', sysdate, sysdate, user);

-- PV-223
INSERT INTO application_message
VALUES (144, 'Line Number or Phone Number cannot be changed in SPR', sysdate, sysdate, user);
INSERT INTO adapted_message
VALUES (144, 1, 'Line Number or Phone Number cannot be changed in SPR [PV-223].', null, null, 'EN', 3, sysdate, sysdate, user);
INSERT INTO adapted_message
VALUES (144, 2, 'Line Number or Phone Number cannot be changed in SPR [PV-223].', null, null, 'FR', 3, sysdate, sysdate, user);
INSERT INTO application_error
VALUES (15, 'PV-223', 144, 'Line Number or Phone Number cannot be changed in SPR', sysdate, sysdate, user);

-- PV-230
INSERT INTO application_message
VALUES (145, 'New Reseller Name must be provided if New Network SP != Old Network SP', sysdate, sysdate, user);
INSERT INTO adapted_message
VALUES (145, 1, 'New Reseller Name must be provided if New Network SP != Old Network SP [PV-230].', null, null, 'EN', 3, sysdate, sysdate, user);
INSERT INTO adapted_message
VALUES (145, 2, 'New Reseller Name must be provided if New Network SP != Old Network SP [PV-230].', null, null, 'FR', 3, sysdate, sysdate, user);
INSERT INTO application_error
VALUES (15, 'PV-230', 145, 'New Reseller Name must be provided if New Network SP != Old Network SP', sysdate, sysdate, user);

-- PV-240
INSERT INTO application_message
VALUES (146, 'LRN is invalid or missing; Required Format={0}; Actual Value={1}', sysdate, sysdate, user);
INSERT INTO adapted_message
VALUES (146, 1, 'LRN is invalid or missing; Required Format={0}; Actual Value={1} [PV-240].', null, null, 'EN', 3, sysdate, sysdate, user);
INSERT INTO adapted_message
VALUES (146, 2, 'LRN is invalid or missing; Required Format={0}; Actual Value={1} [PV-240].', null, null, 'FR', 3, sysdate, sysdate, user);
INSERT INTO application_error
VALUES (15, 'PV-240', 146, 'LRN is invalid or missing; Required Format={0}; Actual Value={1}', sysdate, sysdate, user);

-- PV-250
INSERT INTO application_message
VALUES (147, 'Return to Old SP is invalid or missing; Required Format={0}; Actual Value={1}', sysdate, sysdate, user);
INSERT INTO adapted_message
VALUES (147, 1, 'Return to Old SP is invalid or missing; Required Format={0}; Actual Value={1} [PV-250].', null, null, 'EN', 3, sysdate, sysdate, user);
INSERT INTO adapted_message
VALUES (147, 2, 'Return to Old SP is invalid or missing; Required Format={0}; Actual Value={1} [PV-250].', null, null, 'FR', 3, sysdate, sysdate, user);
INSERT INTO application_error
VALUES (15, 'PV-250', 147, 'Return to Old SP is invalid or missing; Required Format={0}; Actual Value={1}', sysdate, sysdate, user);

-- PV-251
INSERT INTO application_message
VALUES (148, 'Auto Activate Flag cannot be changed in SPR', sysdate, sysdate, user);
INSERT INTO adapted_message
VALUES (148, 1, 'Auto Activate Flag cannot be changed in SPR [PV-251].', null, null, 'EN', 3, sysdate, sysdate, user);
INSERT INTO adapted_message
VALUES (148, 2, 'Auto Activate Flag cannot be changed in SPR [PV-251].', null, null, 'FR', 3, sysdate, sysdate, user);
INSERT INTO application_error
VALUES (15, 'PV-251', 148, 'Auto Activate Flag cannot be changed in SPR', sysdate, sysdate, user);

-- PV-260
INSERT INTO application_message
VALUES (149, 'TELUS BAN Information is invalid or missing', sysdate, sysdate, user);
INSERT INTO adapted_message
VALUES (149, 1, 'TELUS BAN Information is invalid or missing [PV-260].', null, null, 'EN', 3, sysdate, sysdate, user);
INSERT INTO adapted_message
VALUES (149, 2, 'TELUS BAN Information is invalid or missing [PV-260].', null, null, 'FR', 3, sysdate, sysdate, user);
INSERT INTO application_error
VALUES (15, 'PV-260', 149, 'TELUS BAN Information is invalid or missing', sysdate, sysdate, user);

-- PV-300
INSERT INTO application_message
VALUES (150, 'Port Type is invalid or missing; Required={0}; Actual Value={1}', sysdate, sysdate, user);
INSERT INTO adapted_message
VALUES (150, 1, 'Port Type is invalid or missing; Required={0}; Actual Value={1} [PV-300].', null, null, 'EN', 3, sysdate, sysdate, user);
INSERT INTO adapted_message
VALUES (150, 2, 'Port Type is invalid or missing; Required={0}; Actual Value={1} [PV-300].', null, null, 'FR', 3, sysdate, sysdate, user);
INSERT INTO application_error
VALUES (15, 'PV-300', 150, 'Port Type is invalid or missing; Required={0}; Actual Value={1}', sysdate, sysdate, user);

-- PV-301
INSERT INTO application_message
VALUES (151, 'Port Type cannot be changed in SPR', sysdate, sysdate, user);
INSERT INTO adapted_message
VALUES (151, 1, 'Port Type cannot be changed in SPR [PV-301].', null, null, 'EN', 3, sysdate, sysdate, user);
INSERT INTO adapted_message
VALUES (151, 2, 'Port Type cannot be changed in SPR [PV-301].', null, null, 'FR', 3, sysdate, sysdate, user);
INSERT INTO application_error
VALUES (15, 'PV-301', 151, 'Port Type cannot be changed in SPR', sysdate, sysdate, user);

-- PV-304
INSERT INTO application_message
VALUES (152, 'Invalid Message Type; Actual Value={0}', sysdate, sysdate, user);
INSERT INTO adapted_message
VALUES (152, 1, 'Invalid Message Type; Actual Value={0} [PV-304].', null, null, 'EN', 3, sysdate, sysdate, user);
INSERT INTO adapted_message
VALUES (152, 2, 'Invalid Message Type; Actual Value={0} [PV-304].', null, null, 'FR', 3, sysdate, sysdate, user);
INSERT INTO application_error
VALUES (15, 'PV-304', 152, 'Invalid Message Type; Actual Value={0}', sysdate, sysdate, user);

-- PV-400
INSERT INTO application_message
VALUES (153, 'Response Type is invalid or missing; Required Format={0}; Actual={1}', sysdate, sysdate, user);
INSERT INTO adapted_message
VALUES (153, 1, 'Response Type is invalid or missing; Required Format={0}; Actual={1} [PV-400].', null, null, 'EN', 3, sysdate, sysdate, user);
INSERT INTO adapted_message
VALUES (153, 2, 'Response Type is invalid or missing; Required Format={0}; Actual={1} [PV-400].', null, null, 'FR', 3, sysdate, sysdate, user);
INSERT INTO application_error
VALUES (15, 'PV-400', 153, 'Response Type is invalid or missing; Required Format={0}; Actual={1}', sysdate, sysdate, user);


COMMIT;
