-- ST5
INSERT INTO application_message
VALUES (129, 'STATUS_CODE.', sysdate, sysdate, user);
INSERT INTO adapted_message
VALUES (129, 1, 'We could not locate a transfer request for this number. Please call us at 1-877-253-2763 if you wish to transfer this number to TELUS.', null, null, 'EN', 3, sysdate, sysdate, user);
INSERT INTO adapted_message
VALUES (129, 2, 'Nous ne pouvons pas localiser la demande de transfert pour ce numéro. Veuillez appeler au 1-877-253-2763 si vous désirez transférer ce numéro chez TELUS.', null, null, 'FR', 3, sysdate, sysdate, user);
INSERT INTO application_error 
VALUES (15, 'ST5', 129, 'STATUS_CODE.', sysdate, sysdate, user);

-- ST6
INSERT INTO application_message
VALUES (130, 'STATUS_CODE.', sysdate, sysdate, user);
INSERT INTO adapted_message
VALUES (130, 1, 'Please call us at 1-877-253-2763 for the status of your request. Thank you.', null, null, 'EN', 3, sysdate, sysdate, user);
INSERT INTO adapted_message
VALUES (130, 2, 'Veuillez appeler au 1-877-253-2763 pour connaître le statut de votre demande. Merci.', null, null, 'FR', 3, sysdate, sysdate, user);
INSERT INTO application_error 
VALUES (15, 'ST6', 130, 'STATUS_CODE.', sysdate, sysdate, user);

-- ST7
INSERT INTO application_message
VALUES (131, 'STATUS_CODE.', sysdate, sysdate, user);
INSERT INTO adapted_message
VALUES (131, 1, 'This transfer request was cancelled.', null, null, 'EN', 3, sysdate, sysdate, user);
INSERT INTO adapted_message
VALUES (131, 2, 'La demande de transfert a été annulée.', null, null, 'FR', 3, sysdate, sysdate, user);
INSERT INTO application_error 
VALUES (15, 'ST7', 131, 'STATUS_CODE.', sysdate, sysdate, user);

-- ST8
INSERT INTO application_message
VALUES (132, 'STATUS_CODE.', sysdate, sysdate, user);
INSERT INTO adapted_message
VALUES (132, 1, 'Please call us at 1-877-253-2763 for the status of your request. Thank you.', null, null, 'EN', 3, sysdate, sysdate, user);
INSERT INTO adapted_message
VALUES (132, 2, 'Veuillez appeler au 1-877-253-2763 pour connaître le statut de votre demande. Merci.', null, null, 'FR', 3, sysdate, sysdate, user);
INSERT INTO application_error 
VALUES (15, 'ST8', 132, 'STATUS_CODE.', sysdate, sysdate, user);

COMMIT;
