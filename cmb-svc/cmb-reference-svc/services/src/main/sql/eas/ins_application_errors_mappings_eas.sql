-- PPV-0100
-- INSERT INTO application_message
-- VALUES (92, 'Neither ACCT, PIN NOR ESN populated', sysdate, sysdate, user);
-- INSERT INTO adapted_message
-- VALUES (92, 1, 'Please provide at least one piece of information from your Old Service Provider (Account #, Password/PIN or Equpment Serial Number).', null, null, 'EN', 3, sysdate, sysdate, user);
-- INSERT INTO adapted_message
-- VALUES (92, 2, 'Veuillez fournir au moins un ancien numéro de compte d''un fournisseur de service, le mot de passe ou le NIP ou encore le numéro de série de l''appareil.', null, null, 'FR', 3, sysdate, sysdate, user);
-- INSERT INTO application_error 
-- VALUES (15, 'PPV-0100', 92, 'Neither ACCT, PIN NOR ESN populated.', sysdate, sysdate, user);

-- PPV-0110
INSERT INTO application_message
VALUES (93, 'Neither ACCT NOR ESN populated.', sysdate, sysdate, user);
INSERT INTO adapted_message
VALUES (93, 1, 'Please provide at least one piece of information from your Old Service Provider: Account # or Equpment Serial Number.', null, null, 'EN', 3, sysdate, sysdate, user);
INSERT INTO adapted_message
VALUES (93, 2, 'Veuillez fournir au moins un ancien numéro de compte d''un fournisseur de service, le mot de passe ou le NIP ou encore le numéro de série de l''appareil.', null, null, 'FR', 3, sysdate, sysdate, user);
INSERT INTO application_error 
VALUES (15, 'PPV-0110', 93, 'Neither ACCT NOR ESN populated.', sysdate, sysdate, user);

-- PPV-0120
INSERT INTO application_message
VALUES (94, 'MDN NOT POPULATED WHERE REQUIRED.', sysdate, sysdate, user);
INSERT INTO adapted_message
VALUES (94, 1, 'Please verfify Mobile Directory Number field is filled in correctly.', null, null, 'EN', 3, sysdate, sysdate, user);
INSERT INTO adapted_message
VALUES (94, 2, 'Please verfify Mobile Directory Number field is filled in correctly.', null, null, 'FR', 3, sysdate, sysdate, user);
INSERT INTO application_error 
VALUES (15, 'PPV-0120', 94, 'MDN NOT POPULATED WHERE REQUIRED.', sysdate, sysdate, user);

-- PPV-0130
INSERT INTO application_message
VALUES (95, 'MDN POPULATED WHERE PROHIBITED.', sysdate, sysdate, user);
INSERT INTO adapted_message
VALUES (95, 1, 'Please verfify Mobile Directory Number field is filled in correctly.', null, null, 'EN', 3, sysdate, sysdate, user);
INSERT INTO adapted_message
VALUES (95, 2, 'Please verfify Mobile Directory Number field is filled in correctly.', null, null, 'FR', 3, sysdate, sysdate, user);
INSERT INTO application_error 
VALUES (15, 'PPV-0130', 95, 'MDN POPULATED WHERE PROHIBITED.', sysdate, sysdate, user);

-- PPV-0140
INSERT INTO application_message
VALUES (96, 'REMARKS NOT POPULATED WHERE REQUIRED.', sysdate, sysdate, user);
INSERT INTO adapted_message
VALUES (96, 1, 'Please verfify Remarks field is filled in correctly.', null, null, 'EN', 3, sysdate, sysdate, user);
INSERT INTO adapted_message
VALUES (96, 2, 'Please verfify Remarks field is filled in correctly.', null, null, 'FR', 3, sysdate, sysdate, user);
INSERT INTO application_error 
VALUES (15, 'PPV-0140', 96, 'REMARKS NOT POPULATED WHERE REQUIRED.', sysdate, sysdate, user);

-- PPV-0150
INSERT INTO application_message
VALUES (97, 'CAN NOT FIND EVEN A SINGLE LNUM.', sysdate, sysdate, user);
INSERT INTO adapted_message
VALUES (97, 1, 'Please verify your phone number.', null, null, 'EN', 3, sysdate, sysdate, user);
INSERT INTO adapted_message
VALUES (97, 2, 'Please verify your phone number.', null, null, 'FR', 3, sysdate, sysdate, user);
INSERT INTO application_error 
VALUES (15, 'PPV-0150', 97, 'CAN NOT FIND EVEN A SINGLE LNUM.', sysdate, sysdate, user);

-- PPV-0160
INSERT INTO application_message
VALUES (98, 'NPQTY does not reflect the count of lnum.', sysdate, sysdate, user);
INSERT INTO adapted_message
VALUES (98, 1, 'Please confirm how many lines involve in porting request.', null, null, 'EN', 3, sysdate, sysdate, user);
INSERT INTO adapted_message
VALUES (98, 2, 'Please confirm how many lines involve in porting request.', null, null, 'FR', 3, sysdate, sysdate, user);
INSERT INTO application_error 
VALUES (15, 'PPV-0160', 98, 'NPQTY does not reflect the count of lnum.', sysdate, sysdate, user);

-- PPV-0170
INSERT INTO application_message
VALUES (99, 'VER_ID_RESP NOT POPULATED on WPRR.', sysdate, sysdate, user);
INSERT INTO adapted_message
VALUES (99, 1, 'Please verfify Version Identification for the Response field is filled in correctly.', null, null, 'EN', 3, sysdate, sysdate, user);
INSERT INTO adapted_message
VALUES (99, 2, 'Please verfify Version Identification for the Response field is filled in correctly.', null, null, 'FR', 3, sysdate, sysdate, user);
INSERT INTO application_error 
VALUES (15, 'PPV-0170', 99, 'VER_ID_RESP NOT POPULATED on WPRR.', sysdate, sysdate, user);

-- PPV-0180
INSERT INTO application_message
VALUES (100, 'DUPLICATE LNUM FOUND.', sysdate, sysdate, user);
INSERT INTO adapted_message
VALUES (100, 1, 'Please verify your phone number.', null, null, 'EN', 3, sysdate, sysdate, user);
INSERT INTO adapted_message
VALUES (100, 2, 'Please verify your phone number.', null, null, 'FR', 3, sysdate, sysdate, user);
INSERT INTO application_error 
VALUES (15, 'PPV-0180', 100, 'DUPLICATE LNUM FOUND.', sysdate, sysdate, user);

-- PPV-0190
INSERT INTO application_message
VALUES (101, 'NEW RESELLER NAME NOT POPULATED.', sysdate, sysdate, user);
INSERT INTO adapted_message
VALUES (101, 1, 'Please verfify new service provider field is filled in correctly.', null, null, 'EN', 3, sysdate, sysdate, user);
INSERT INTO adapted_message
VALUES (101, 2, 'Please verfify new service provider field is filled in correctly.', null, null, 'FR', 3, sysdate, sysdate, user);
INSERT INTO application_error 
VALUES (15, 'PPV-0190', 101, 'NEW RESELLER NAME NOT POPULATED.', sysdate, sysdate, user);

-- PPV-0200
INSERT INTO application_message
VALUES (102, 'OLD RESELLER NAME NOT POPULATED.', sysdate, sysdate, user);
INSERT INTO adapted_message
VALUES (102, 1, 'Please verfify old service provider field is filled in correctly.', null, null, 'EN', 3, sysdate, sysdate, user);
INSERT INTO adapted_message
VALUES (102, 2, 'Please verfify old service provider field is filled in correctly.', null, null, 'FR', 3, sysdate, sysdate, user);
INSERT INTO application_error 
VALUES (15, 'PPV-0200', 102, 'OLD RESELLER NAME NOT POPULATED.', sysdate, sysdate, user);

-- PPV-0210
INSERT INTO application_message
VALUES (103, 'AGENCY AUTH FIELDS NOT POPULATED.', sysdate, sysdate, user);
INSERT INTO adapted_message
VALUES (103, 1, 'Please verfify agency authorization field is filled in correctly.', null, null, 'EN', 3, sysdate, sysdate, user);
INSERT INTO adapted_message
VALUES (103, 2, 'Please verfify agency authorization field is filled in correctly.', null, null, 'FR', 3, sysdate, sysdate, user);
INSERT INTO application_error 
VALUES (15, 'PPV-0210', 103, 'AGENCY AUTH FIELDS NOT POPULATED.', sysdate, sysdate, user);

-- PPV-0220
INSERT INTO application_message
VALUES (104, 'BILLNAMEs NOT POPULATED.', sysdate, sysdate, user);
INSERT INTO adapted_message
VALUES (104, 1, 'Please verify that all Name fields are filled in correctly.', null, null, 'EN', 3, sysdate, sysdate, user);
INSERT INTO adapted_message
VALUES (104, 2, 'Veuillez vérifier que tous les champs des noms sont remplis correctement.', null, null, 'FR', 3, sysdate, sysdate, user);
INSERT INTO application_error 
VALUES (15, 'PPV-0220', 104, 'BILLNAMEs NOT POPULATED.', sysdate, sysdate, user);

-- PPV-0230
INSERT INTO application_message
VALUES (105, 'INVALID CHC FOUND.', sysdate, sysdate, user);
INSERT INTO adapted_message
VALUES (105, 1, 'Please verify valid values are used for Co-ordinates Hot Cut that is Y, N or Not Populated.', null, null, 'EN', 3, sysdate, sysdate, user);
INSERT INTO adapted_message
VALUES (105, 2, 'Please verify valid values are used for Co-ordinates Hot Cut that is Y, N or Not Populated.', null, null, 'FR', 3, sysdate, sysdate, user);
INSERT INTO application_error 
VALUES (15, 'PPV-0230', 105, 'INVALID CHC FOUND.', sysdate, sysdate, user);

-- PPV-0240
INSERT INTO application_message
VALUES (106, 'INVALID ZIP or POSTAL CODE.', sysdate, sysdate, user);
INSERT INTO adapted_message
VALUES (106, 1, 'Please verify your Postal Code.', null, null, 'EN', 3, sysdate, sysdate, user);
INSERT INTO adapted_message
VALUES (106, 2, 'Veuillez vérifier votre code postal.', null, null, 'FR', 3, sysdate, sysdate, user);
INSERT INTO application_error 
VALUES (15, 'PPV-0240', 106, 'INVALID ZIP or POSTAL CODE.', sysdate, sysdate, user);

-- PPV-0250
INSERT INTO application_message
VALUES (107, 'INVALID STATE or province.', sysdate, sysdate, user);
INSERT INTO adapted_message
VALUES (107, 1, 'Please verify your Province.', null, null, 'EN', 3, sysdate, sysdate, user);
INSERT INTO adapted_message
VALUES (107, 2, 'Veuillez vérifier le nom de votre province.', null, null, 'FR', 3, sysdate, sysdate, user);
INSERT INTO application_error 
VALUES (15, 'PPV-0250', 107, 'INVALID STATE or province.', sysdate, sysdate, user);

-- PPV-0260
INSERT INTO application_message
VALUES (108, 'INVALID SUB VALUE.', sysdate, sysdate, user);
INSERT INTO adapted_message
VALUES (108, 1, 'INVALID SUB VALUE.', null, null, 'EN', 3, sysdate, sysdate, user);
INSERT INTO adapted_message
VALUES (108, 2, 'INVALID SUB VALUE.', null, null, 'FR', 3, sysdate, sysdate, user);
INSERT INTO application_error 
VALUES (15, 'PPV-0260', 108, 'INVALID SUB VALUE.', sysdate, sysdate, user);

-- PPV-0270
INSERT INTO application_message
VALUES (109, 'RCODE, RDET NOT POPULATED WHERE REQUIRED.', sysdate, sysdate, user);
INSERT INTO adapted_message
VALUES (109, 1, 'Please verfify Reason Code, Reason Code Detail fields are filled in correctly.', null, null, 'EN', 3, sysdate, sysdate, user);
INSERT INTO adapted_message
VALUES (109, 2, 'Please verfify Reason Code, Reason Code Detail fields are filled in correctly.', null, null, 'FR', 3, sysdate, sysdate, user);
INSERT INTO application_error 
VALUES (15, 'PPV-0270', 109, 'RCODE, RDET NOT POPULATED WHERE REQUIRED.', sysdate, sysdate, user);

-- PPV-0280
INSERT INTO application_message
VALUES (110, 'RCODE, RDET POPULATED WHERE prohibited.', sysdate, sysdate, user);
INSERT INTO adapted_message
VALUES (110, 1, 'Please verfify Reason Code, Reason Code Detail fields are filled in correctly.', null, null, 'EN', 3, sysdate, sysdate, user);
INSERT INTO adapted_message
VALUES (110, 2, 'Please verfify Reason Code, Reason Code Detail fields are filled in correctly.', null, null, 'FR', 3, sysdate, sysdate, user);
INSERT INTO application_error 
VALUES (15, 'PPV-0280', 110, 'RCODE, RDET POPULATED WHERE prohibited.', sysdate, sysdate, user);

-- PPV-0290
INSERT INTO application_message
VALUES (111, 'invalid DCode.', sysdate, sysdate, user);
INSERT INTO adapted_message
VALUES (111, 1, 'Please verify proper Delay Code is used.', null, null, 'EN', 3, sysdate, sysdate, user);
INSERT INTO adapted_message
VALUES (111, 2, 'Please verify proper Delay Code is used.', null, null, 'FR', 3, sysdate, sysdate, user);
INSERT INTO application_error 
VALUES (15, 'PPV-0290', 111, 'invalid DCode.', sysdate, sysdate, user);

-- PPV-0300
INSERT INTO application_message
VALUES (112, 'MISSING MANDATORY FIELDS.', sysdate, sysdate, user);
INSERT INTO adapted_message
VALUES (112, 1, 'Please verify all mandatory fields are filled.', null, null, 'EN', 3, sysdate, sysdate, user);
INSERT INTO adapted_message
VALUES (112, 2, 'Please verify all mandatory fields are filled.', null, null, 'FR', 3, sysdate, sysdate, user);
INSERT INTO application_error 
VALUES (15, 'PPV-0300', 112, 'MISSING MANDATORY FIELDS.', sysdate, sysdate, user);

-- PPV-0310
INSERT INTO application_message
VALUES (113, 'VER_ID_REQ NOT POPULATED.', sysdate, sysdate, user);
INSERT INTO adapted_message
VALUES (113, 1, 'Please verfify Version Identification for the Request field is filled in correctly.', null, null, 'EN', 3, sysdate, sysdate, user);
INSERT INTO adapted_message
VALUES (113, 2, 'Please verfify Version Identification for the Request field is filled in correctly.', null, null, 'FR', 3, sysdate, sysdate, user);
INSERT INTO application_error 
VALUES (15, 'PPV-0310', 113, 'VER_ID_REQ NOT POPULATED.', sysdate, sysdate, user);

-- PPV-0320
INSERT INTO application_message
VALUES (114, 'Due DATE does not match.', sysdate, sysdate, user);
INSERT INTO adapted_message
VALUES (114, 1, 'The due date does not match.', null, null, 'EN', 3, sysdate, sysdate, user);
INSERT INTO adapted_message
VALUES (114, 2, 'The due date does not match.', null, null, 'FR', 3, sysdate, sysdate, user);
INSERT INTO application_error 
VALUES (15, 'PPV-0320', 114, 'Due DATE does not match.', sysdate, sysdate, user);

-- E1
INSERT INTO application_message
VALUES (115, 'PORT_IN_ELIGIBILITY.', sysdate, sysdate, user);
INSERT INTO adapted_message
VALUES (115, 1, 'No, this number is not eligible to be transferred to TELUS because it is currently not transferrable. Client can call their current service provider for details or take a new number to activate with TELUS.', null, null, 'EN', 3, sysdate, sysdate, user);
INSERT INTO adapted_message
VALUES (115, 2, 'Non, ce numéro ne peut pas être transféré à TELUS parce que Il ne peut être transféré présentement. Le client peut appeler son fournisseur de services actuel pour obtenir plus de détails ou choisir un nouveau numéro de téléphone  pour mettre son appareil en service à TELUS.', null, null, 'FR', 3, sysdate, sysdate, user);
INSERT INTO application_error 
VALUES (15, 'E1', 115, 'PORT_IN_ELIGIBILITY.', sysdate, sysdate, user);

-- E2
INSERT INTO application_message
VALUES (116, 'PORT_IN_ELIGIBILITY.', sysdate, sysdate, user);
INSERT INTO adapted_message
VALUES (116, 1, 'No, this number is not eligible to be transferred to TELUS because Wireless Number Portability (WNP) is not currently supported in the client''s local calling area.', null, null, 'EN', 3, sysdate, sysdate, user);
INSERT INTO adapted_message
VALUES (116, 2, ' Non, ce numéro ne peut pas être transféré à TELUS parce que la transférabilité des numéros sans fil n''est actuellement pas offerte dans la zone d''appel local du client.', null, null, 'FR', 3, sysdate, sysdate, user);
INSERT INTO application_error 
VALUES (15, 'E2', 116, 'PORT_IN_ELIGIBILITY.', sysdate, sysdate, user);

-- E3
INSERT INTO application_message
VALUES (117, 'PORT_IN_ELIGIBILITY.', sysdate, sysdate, user);
INSERT INTO adapted_message
VALUES (117, 1, 'No, this number is not eligible to be transferred to TELUS because we do not currently provide service in the client''s local calling area.', null, null, 'EN', 3, sysdate, sysdate, user);
INSERT INTO adapted_message
VALUES (117, 2, ' Non, ce numéro ne peut pas être transféré à TELUS parce que le service n''est pas offert dans la zone d''appel local du client.', null, null, 'FR', 3, sysdate, sysdate, user);
INSERT INTO application_error 
VALUES (15, 'E3', 117, 'PORT_IN_ELIGIBILITY.', sysdate, sysdate, user);

-- E4
INSERT INTO application_message
VALUES (118, 'PORT_IN_ELIGIBILITY.', sysdate, sysdate, user);
INSERT INTO adapted_message
VALUES (118, 1, 'Wireless or intermodal port.', null, null, 'EN', 3, sysdate, sysdate, user);
INSERT INTO adapted_message
VALUES (118, 2, 'Wireless or intermodal port.', null, null, 'FR', 3, sysdate, sysdate, user);
INSERT INTO application_error 
VALUES (15, 'E4', 118, 'PORT_IN_ELIGIBILITY.', sysdate, sysdate, user);

-- E5
INSERT INTO application_message
VALUES (119, 'PORT_IN_ELIGIBILITY.', sysdate, sysdate, user);
INSERT INTO adapted_message
VALUES (119, 1, 'Provide name of OSP; use to populate WPR.', null, null, 'EN', 3, sysdate, sysdate, user);
INSERT INTO adapted_message
VALUES (119, 2, 'Provide name of OSP; use to populate WPR.', null, null, 'FR', 3, sysdate, sysdate, user);
INSERT INTO application_error 
VALUES (15, 'E5', 119, 'PORT_IN_ELIGIBILITY.', sysdate, sysdate, user);

-- E7
INSERT INTO application_message
VALUES (120, 'PORT_IN_ELIGIBILITY.', sysdate, sysdate, user);
INSERT INTO adapted_message
VALUES (120, 1, 'No, this number is not eligible to be transferred to TELUS because there is no agreement in place to allow for number transfers between TELUS and the client''s current service provider.', null, null, 'EN', 3, sysdate, sysdate, user);
INSERT INTO adapted_message
VALUES (120, 2, ' Non, ce numéro ne peut pas être transféré à TELUS parce que il n''existe aucune entente permettant le transfert des numéros entre TELUS et le fournisseur de service actuel du client.', null, null, 'FR', 3, sysdate, sysdate, user);
INSERT INTO application_error 
VALUES (15, 'E7', 120, 'PORT_IN_ELIGIBILITY.', sysdate, sysdate, user);

-- E8
INSERT INTO application_message
VALUES (121, 'PORT_IN_ELIGIBILITY.', sysdate, sysdate, user);
INSERT INTO adapted_message
VALUES (121, 1, 'No, this number is not eligible to be transferred to TELUS because it is already active on our network.', null, null, 'EN', 3, sysdate, sysdate, user);
INSERT INTO adapted_message
VALUES (121, 2, ' Non, ce numéro ne peut pas être transféré à TELUS parce que il est déjà en service sur notre réseau.', null, null, 'FR', 3, sysdate, sysdate, user);
INSERT INTO application_error 
VALUES (15, 'E8', 121, 'PORT_IN_ELIGIBILITY.', sysdate, sysdate, user);

-- E0
INSERT INTO application_message
VALUES (122, 'PORT_IN_ELIGIBILITY.', sysdate, sysdate, user);
INSERT INTO adapted_message
VALUES (122, 1, 'Yes, this number is eligible to be transferred to [PCS Postpaid/Mike/Pay & Talk].  [note: insert appropriate product type(s) with punctuation, as necessary].', null, null, 'EN', 3, sysdate, sysdate, user);
INSERT INTO adapted_message
VALUES (122, 2, 'Oui, ce numéro peut être transféré à un appareil SCP avec service postpayé/Mike/ SCP avec service Payez & Parlez  [remarque : inscrivez le ou les types de produit appropriés et les signes de ponctuation requis].', null, null, 'FR', 3, sysdate, sysdate, user);
INSERT INTO application_error 
VALUES (15, 'E0', 122, 'PORT_IN_ELIGIBILITY.', sysdate, sysdate, user);

-- E9
INSERT INTO application_message
VALUES (123, 'TN is being ported already.', sysdate, sysdate, user);
INSERT INTO adapted_message
VALUES (123, 1, 'A transfer request for this number is already in progress.  Use the transfer status tool to check the transfer progress of this number.', null, null, 'EN', 3, sysdate, sysdate, user);
INSERT INTO adapted_message
VALUES (123, 2, 'Une demande de transfert de ce numéro de téléphone est déjà en cours de traitement. Utilisez l''outil à cet effet pour vérifier la situation liée au transfert de ce numéro.', null, null, 'FR', 3, sysdate, sysdate, user);
INSERT INTO application_error 
VALUES (15, 'E9', 123, 'TN is being ported already.', sysdate, sysdate, user);

-- E999
INSERT INTO application_message
VALUES (124, 'Catch All.', sysdate, sysdate, user);
INSERT INTO adapted_message
VALUES (124, 1, 'Your request could not be completed.  Please try again later.', null, null, 'EN', 3, sysdate, sysdate, user);
INSERT INTO adapted_message
VALUES (124, 2, 'Impossible de traiter votre demande.   S''il vous plait essai encore plus tard.', null, null, 'FR', 3, sysdate, sysdate, user);
INSERT INTO application_error 
VALUES (15, 'E999', 124, 'Catch All.', sysdate, sysdate, user);

-- ST1
INSERT INTO application_message
VALUES (125, 'STATUS_CODE.', sysdate, sysdate, user);
INSERT INTO adapted_message
VALUES (125, 1, 'Your transfer request has been completed successfully. Welcome to TELUS and thank you for joining us!', null, null, 'EN', 3, sysdate, sysdate, user);
INSERT INTO adapted_message
VALUES (125, 2, 'Votre demande de transfert a été complétée avec succès. Bienvenue chez TELUS et merci de vous joindre à nous!', null, null, 'FR', 3, sysdate, sysdate, user);
INSERT INTO application_error 
VALUES (15, 'ST1', 125, 'STATUS_CODE.', sysdate, sysdate, user);

-- ST2
INSERT INTO application_message
VALUES (126, 'STATUS_CODE.', sysdate, sysdate, user);
INSERT INTO adapted_message
VALUES (126, 1, 'We are experiencing difficulties in processing your transfer request. Please call us at 1-877-TO-TELUS and select the ''resolve an issue'' option. Thank you.', null, null, 'EN', 3, sysdate, sysdate, user);
INSERT INTO adapted_message
VALUES (126, 2, 'Nous éprouvons des difficultés dans le traitement de votre demande de transfert. Veuillez nous appeler au 1-877- 868-3587   et sélectionner l''option " résoudre un problème ". Merci.', null, null, 'FR', 3, sysdate, sysdate, user);
INSERT INTO application_error 
VALUES (15, 'ST2', 126, 'STATUS_CODE.', sysdate, sysdate, user);

-- ST3
INSERT INTO application_message
VALUES (127, 'STATUS_CODE.', sysdate, sysdate, user);
INSERT INTO adapted_message
VALUES (127, 1, 'Your transfer request is in progress. We will notify you of any changes to the status of your request. Thank you for your patience.', null, null, 'EN', 3, sysdate, sysdate, user);
INSERT INTO adapted_message
VALUES (127, 2, ' Votre demande de transfert est en cours de traitement. Nous vous aviserons de tout changement de statut de votre demande. Merci de votre patience.', null, null, 'FR', 3, sysdate, sysdate, user);
INSERT INTO application_error 
VALUES (15, 'ST3', 127, 'STATUS_CODE.', sysdate, sysdate, user);

-- ST4
INSERT INTO application_message
VALUES (128, 'STATUS_CODE.', sysdate, sysdate, user);
INSERT INTO adapted_message
VALUES (128, 1, 'We could not locate a transfer request for this number. Please call us at 1-877-253-2763 if you wish to transfer this number to TELUS.', null, null, 'EN', 3, sysdate, sysdate, user);
INSERT INTO adapted_message
VALUES (128, 2, 'Nous ne pouvons pas localiser la demande de transfert pour ce numéro. Veuillez appeler au 1-877-253-2763 si vous désirez transférer ce numéro chez TELUS.', null, null, 'FR', 3, sysdate, sysdate, user);
INSERT INTO application_error 
VALUES (15, 'ST4', 128, 'STATUS_CODE.', sysdate, sysdate, user);

COMMIT;
