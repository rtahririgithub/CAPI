-- INSERT STATUS MESSAGES FOR AMP'D

-- ST1
INSERT INTO adapted_message
VALUES (102, 3, 'Your transfer request has been completed successfully. Welcome to Amp''d Mobile and thank you for joining us!', null, 3, 'EN', 3, sysdate, sysdate, user);
INSERT INTO adapted_message
VALUES (102, 4, 'Votre demande de transfert a �t� compl�t�e avec succ�s. Bienvenue chez Amp''d Mobile et merci de vous joindre � nous!', null, 3, 'FR', 3, sysdate, sysdate, user);

-- ST2
INSERT INTO adapted_message
VALUES (103, 3, 'Your transfer request is active.  Please call us at 1-877-611-AMPD (2673) and select the ''finish my transfer'' option.', null, 3, 'EN', 3, sysdate, sysdate, user);
INSERT INTO adapted_message
VALUES (103, 4, 'Votre demande de transfert est en cours de traitement. Veuillez appeler au 1-877-611-2673   et s�lectionner l''option ��terminer mon transfert��.', null, 3, 'FR', 3, sysdate, sysdate, user);

-- ST4
INSERT INTO adapted_message
VALUES (105, 3, 'We are experiencing difficulties in processing your transfer request. Please call us at 1-877-611-AMPD (2673) and select the ''resolve an issue'' option. Thank you.', null, 3, 'EN', 3, sysdate, sysdate, user);
INSERT INTO adapted_message
VALUES (105, 4, 'Nous �prouvons des difficult�s dans le traitement de votre demande de transfert. Veuillez nous appeler au 1-877-611-2673 et s�lectionner l''option � r�soudre un probl�me �. Merci.', null, 3, 'FR', 3, sysdate, sysdate, user);

-- ST5
INSERT INTO adapted_message
VALUES (106, 3, 'We could not locate a transfer request for this number. Please call us at 1-877-611-AMPD (2673) if you wish to transfer this number to Amp''d Mobile.', null, 3, 'EN', 3, sysdate, sysdate, user);
INSERT INTO adapted_message
VALUES (106, 4, 'Nous ne pouvons pas localiser la demande de transfert pour ce num�ro. Veuillez appeler au 1-877-611-2673 si vous d�sirez transf�rer ce num�ro chez Amp''d Mobile.', null, 3, 'FR', 3, sysdate, sysdate, user);

-- ST6
INSERT INTO adapted_message
VALUES (107, 3, 'Please call us at 1-877-611-AMPD (2673) for the status of your request. Thank you.', null, 3, 'EN', 3, sysdate, sysdate, user);
INSERT INTO adapted_message
VALUES (107, 4, 'Veuillez appeler au 1-877-611-2673 pour conna�tre le statut de votre demande. Merci.', null, 3, 'FR', 3, sysdate, sysdate, user);

-- ST8
INSERT INTO adapted_message
VALUES (109, 3, 'Please call us at 1-877-611-AMPD (2673) for the status of your request. Thank you.', null, 3, 'EN', 3, sysdate, sysdate, user);
INSERT INTO adapted_message
VALUES (109, 4, 'Veuillez appeler au 1-877-611-2673 pour conna�tre le statut de votre demande. Merci.', null, 3, 'FR', 3, sysdate, sysdate, user);


COMMIT;
