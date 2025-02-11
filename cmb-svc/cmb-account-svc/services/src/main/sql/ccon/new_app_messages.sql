DELETE FROM APP_MESSAGES;

INSERT INTO APP_MESSAGES ( APP_ROLE_ID, RESPONSE_CODE, RESPONSE_MESSAGE_EN, RESPONSE_MESSAGE_FR, RESPONSE_KB ) 
						VALUES ( 'HOTSPOT_DEFAULT', '100', 
								'Our payment gateway is unable to process the credit card provided.  Please check with your financial institution or try a different credit card.', 
								'Notre passerelle de paiement ne peut pas traiter le paiement effectu� avec la carte de cr�dit fournie. Veuillez v�rifier avec votre institution financi�re ou essayer avec une autre carte de cr�dit.', 
								'Transaction failure. Credit card declined by financial institution. Client should contact financial'); 

INSERT INTO APP_MESSAGES ( APP_ROLE_ID, RESPONSE_CODE, RESPONSE_MESSAGE_EN, RESPONSE_MESSAGE_FR, RESPONSE_KB ) 
						VALUES ( 'HOTSPOT_DEFAULT', '101', 
								'Our payment gateway is unable to process the credit card provided.  Please try a different credit card.', 
								'Notre passerelle de paiement ne peut pas traiter le paiement effectu� avec la carte de cr�dit fournie. Veuillez essayer avec une autre carte de cr�dit.', 
								'Transaction failure. Credit card listed in negative database. Use Source form to send details to Loss Prevention.'); 

INSERT INTO APP_MESSAGES ( APP_ROLE_ID, RESPONSE_CODE, RESPONSE_MESSAGE_EN, RESPONSE_MESSAGE_FR, RESPONSE_KB ) 
						VALUES ( 'HOTSPOT_DEFAULT', '102', 
								'Our payment gateway is currently unable to process international (non-Canadian) Visa cards. International MasterCard and American Express cards are accepted.  Please try again using one of these cards.', 
								'Notre passerelle de paiement ne peut pas traiter pr�sentement les paiements effectu�s avec les cartes VISA internationales (non �mises au Canada). Les cartes de cr�dit internationales Mastercard  et American Express sont accept�es. Veuillez essayer de nouveau avec l une de ces cartes.', 
								'Transaction failure. International credit card. Client must pre-register credit card w/Loss Prevention or use Canadian credit card.'); 

INSERT INTO APP_MESSAGES (APP_ROLE_ID, RESPONSE_CODE, RESPONSE_MESSAGE_EN, RESPONSE_MESSAGE_FR, RESPONSE_KB ) 
						VALUES ( 'HOTSPOT_DEFAULT', '103', 
								'Our payment gateway is unable to process the credit card provided.  Please try again later or try a  different credit card.',
								'Notre passerelle de paiement ne peut pas traiter le paiement effectu� avec la carte de cr�dit fournie. Veuillez essayer de nouveau plus tard ou essayer avec une autre carte de cr�dit.', 
								'Transaction failure. Credit card has exceeded maximum number of  transactions allowed by TELUS Mobility during a prescribed period.');
										 
INSERT INTO APP_MESSAGES ( APP_ROLE_ID, RESPONSE_CODE, RESPONSE_MESSAGE_EN, RESPONSE_MESSAGE_FR, RESPONSE_KB ) 
						VALUES ( 'AMDOCS_CLIENT', '100', 
								'Sorry, we cannot process your credit card payment.  Please verify the credit card details you provided and try again.  If the problem persists, please contact your financial institution.', 
								'D�sol�s, votre paiement par carte de cr�dit ne peut pas �tre effectu�. Veuillez v�rifier les renseignements sur la carte de cr�dit et essayez de nouveau. Si le probl�me persiste, veuillez communiquer avec votre institution financi�re.', 
								'Transaction failure. Credit card declined by financial institution. Client should contact financial institution.'); 

INSERT INTO APP_MESSAGES ( APP_ROLE_ID, RESPONSE_CODE, RESPONSE_MESSAGE_EN, RESPONSE_MESSAGE_FR, RESPONSE_KB ) 
						VALUES ( 'AMDOCS_CLIENT', '101', 
								'Sorry, we can not complete your credit card payment. Please check your credit card number and expiration date. If you continue to experience difficulties, please contact Client Care by dialing 611 from your handset.', 
								'D�sol�s, votre paiement par carte de cr�dit ne peut pas �tre effectu�. Veuillez v�rifier les renseignements sur la carte de cr�dit. Si le probl�me persiste, veuillez communiquer avec le service � la client�le en composant le 611.', 
								'Transaction failure. Credit card listed in negative database. Use Source form to send details to Loss Prevention.'); 

INSERT INTO APP_MESSAGES ( APP_ROLE_ID, RESPONSE_CODE, RESPONSE_MESSAGE_EN, RESPONSE_MESSAGE_FR, RESPONSE_KB ) 
						VALUES ( 'AMDOCS_CLIENT', '102', 
								'Sorry, we can not complete your credit card payment. Please check your credit card number and expiration date. If you have an international credit card, you may need to pre-register it. If you continue to experience difficulties, please contact Client Care by dialing 611 from your handset.', 
								'D�sol�s, votre paiement par carte de cr�dit ne peut pas �tre effectu�. Veuillez v�rifier les renseignements sur la carte de cr�dit. S''il s''agit d''une carte de cr�dit internationale, vous aurez peut-�tre � la pr�enregistrer. Si le probl�me persiste, veuillez communiquer avec le service � la client�le en composant le 611.', 
								'Transaction failure. International credit card. Client must pre-register credit card w/Loss Prevention or use Canadian credit card.'); 

INSERT INTO APP_MESSAGES ( APP_ROLE_ID, RESPONSE_CODE, RESPONSE_MESSAGE_EN, RESPONSE_MESSAGE_FR, RESPONSE_KB ) 
						VALUES ( 'AMDOCS_CLIENT', '103', 
								'Sorry, we cannot complete your credit card transaction. Please verify the credit card details provided. If the issue continues, please contact Client Care by dialing 611 from your handset.', 
								'D�sol�s, votre paiement par carte de cr�dit ne peut pas �tre effectu�. Veuillez v�rifier les renseignements sur la carte de cr�dit. Si le probl�me persiste, veuillez communiquer avec le service � la client�le en composant le 611.', 
								'Transaction failure. Credit card has exceeded maximum number of  transactions allowed by TELUS Mobility during a prescribed period.'); 

INSERT INTO APP_MESSAGES ( APP_ROLE_ID, RESPONSE_CODE, RESPONSE_MESSAGE_EN, RESPONSE_MESSAGE_FR, RESPONSE_KB ) 
						VALUES ( 'AMDOCS_CLIENT', '300', 
								'We cannot process your transaction as entered.  Please verify the details you provided and try again.  If you continue to receive this message, please contact Client Care by dailing 611 from your handset.', 
								'Votre paiement par carte de cr�dit ne peut pas �tre effectu� tel que demand�. Veuillez v�rifier les renseignements sur la carte de cr�dit et essayez de nouveau. Si le probl�me persiste, veuillez communiquer avec le service � la client�le en composant le 611.', 
								'Transaction failure.  AVS Mismatch. Billing address provided by client must match address on file with credit bureau.'); 
											
INSERT INTO APP_MESSAGES ( APP_ROLE_ID, RESPONSE_CODE, RESPONSE_MESSAGE_EN, RESPONSE_MESSAGE_FR, RESPONSE_KB ) 
						VALUES ( 'AMDOCS_DEALER', '100', 
								'Sorry, we cannot process your credit card payment.  Please verify the credit card details you provided and try again.  If the problem persists, please contact your financial institution.', 
								'D�sol�s, le paiement par carte de cr�dit ne peut pas �tre effectu�. Veuillez v�rifier les renseignements sur la carte de cr�dit et essayez de nouveau. Si le probl�me persiste, veuillez communiquer avec votre institution financi�re. ', 
								'Transaction failure. Credit card declined by financial institution. Client should contact financial institution.'); 

INSERT INTO APP_MESSAGES ( APP_ROLE_ID, RESPONSE_CODE, RESPONSE_MESSAGE_EN, RESPONSE_MESSAGE_FR, RESPONSE_KB ) 
						VALUES ( 'AMDOCS_DEALER', '101', 
								'Sorry, we cannot process your credit card payment. Please verify the credit card details you provided. If the information supplied is correct, please contact Client Care.', 
								'D�sol�s, votre paiement par carte de cr�dit ne peut pas �tre effectu�. Veuillez v�rifier les renseignements sur la carte de cr�dit. Si ces renseignements sont exacts, veuillez communiquer avec le service � la client�le.', 
								'Transaction failure. Credit card listed in negative database. Use Source form to send details to Loss Prevention.'); 

INSERT INTO APP_MESSAGES ( APP_ROLE_ID, RESPONSE_CODE, RESPONSE_MESSAGE_EN, RESPONSE_MESSAGE_FR, RESPONSE_KB ) 
						VALUES ( 'AMDOCS_DEALER', '102', 
								'Sorry, we cannot complete your credit card payment. Please check the credit card number and expiration date. If this is an international credit card, you may need to pre-register it. If the problem persists, please contact Client Care.', 
								'D�sol�s, le paiement par carte de cr�dit ne peut pas �tre effectu�. Veuillez v�rifier le num�ro et la date d''expiration de la carte de cr�dit. S''il s''agit d''une carte de cr�dit internationale, vous aurez peut-�tre � la pr�enregistrer. Si le probl�me persiste, veuillez communiquer avec le service � la client�le en composant le 611.', 
								'Transaction failure. International credit card. Client must pre-register credit card w/Loss Prevention or use Canadian credit card.'); 

INSERT INTO APP_MESSAGES ( APP_ROLE_ID, RESPONSE_CODE, RESPONSE_MESSAGE_EN, RESPONSE_MESSAGE_FR, RESPONSE_KB ) 
						VALUES ( 'AMDOCS_DEALER', '103', 
								'Sorry, we cannot process your credit card transaction. Please verify the credit card details provided. If the information supplied is correct, please contact Client Care.', 
								'D�sol�s, votre paiement par carte de cr�dit ne peut pas �tre effectu�. Veuillez v�rifier les renseignements sur la carte de cr�dit. Si ces renseignements sont exacts, veuillez communiquer avec le service � la client�le.', 
								'Transaction failure. Credit card has exceeded maximum number of  transactions allowed by TELUS Mobility during a prescribed period.'); 
											
INSERT INTO APP_MESSAGES ( APP_ROLE_ID, RESPONSE_CODE, RESPONSE_MESSAGE_EN, RESPONSE_MESSAGE_FR, RESPONSE_KB ) 
						VALUES ( 'AMDOCS_DEALER', '300', 
								'Sorry, we cannot process your credit card payment. Please verify the credit card details you provided and try again.  If you continue to receive this message, please contact Client Care.', 
								'D�sol�s, le paiement par carte de cr�dit ne peut pas �tre effectu�. Veuillez v�rifier les renseignements sur la carte de cr�dit et essayez de nouveau. Si vous obtenez encore ce message, veuillez communiquer avec le service � la client�le.', 
								'Transaction failure.  AVS Mismatch. Billing address provided by client must match address on file with credit bureau.'); 

INSERT INTO APP_MESSAGES ( APP_ROLE_ID, RESPONSE_CODE, RESPONSE_MESSAGE_EN, RESPONSE_MESSAGE_FR, RESPONSE_KB ) 
						VALUES ( 'AMDOCS_AGENT', '100', 
								'Sorry, we cannot process your credit card payment.  Please verify the credit card details you provided and try again.  If the problem persists, please contact your financial institution.', 
								'Le paiement par carte de cr�dit ne peut pas �tre effectu�. Veuillez v�rifier les renseignements sur la carte de cr�dit et essayez de nouveau. Si le probl�me persiste, veuillez communiquer avec votre institution financi�re.', 
								'Transaction failure. Credit card declined by financial institution. Client should contact financial institution.'); 

INSERT INTO APP_MESSAGES ( APP_ROLE_ID, RESPONSE_CODE, RESPONSE_MESSAGE_EN, RESPONSE_MESSAGE_FR, RESPONSE_KB ) 
						VALUES ( 'AMDOCS_AGENT', '101', 
								'The credit card payment cannot be processed.  Please verify the details you provided. If the information supplied is correct, please forward details to Loss Prevention.', 
								'Le paiement par carte de cr�dit ne peut pas �tre effectu�. Veuillez v�rifier les renseignements sur la carte de cr�dit. Si ces renseignements sont exacts, veuillez les faire suivre � l �quipe de pr�vention des sinistres.', 
								'Transaction failure. Credit card listed in negative database. Use Source form to send details to Loss Prevention.'); 

INSERT INTO APP_MESSAGES ( APP_ROLE_ID, RESPONSE_CODE, RESPONSE_MESSAGE_EN, RESPONSE_MESSAGE_FR, RESPONSE_KB ) 
						VALUES ( 'AMDOCS_AGENT', '102', 
								'We cannot process the credit card payment at this time because the credit card being used is International.  International credit cards must be pre-registered prior to use.', 
								'Le paiement par carte de cr�dit ne peut pas �tre effectu� car il s agit d une carte de cr�dit internationale. Les cartes de cr�dit internationales doivent �tre pr�enregistr�es pour pouvoir �tre utilis�es.', 
								'Transaction failure. International credit card. Client must pre-register credit card w/Loss Prevention or use Canadian credit card.'); 

INSERT INTO APP_MESSAGES ( APP_ROLE_ID, RESPONSE_CODE, RESPONSE_MESSAGE_EN, RESPONSE_MESSAGE_FR, RESPONSE_KB ) 
						VALUES ( 'AMDOCS_AGENT', '103', 
								'The credit card transaction cannot be processed.  Please verify the credit card details provided. If the information supplied is correct, please view the memo for additional information.', 
								'Le paiement par carte de cr�dit ne peut pas �tre effectu�. Veuillez v�rifier les renseignements sur la carte de cr�dit. Si ces renseignements sont exacts, veuillez consulter les m�mos dans Knowbility.', 
								'Transaction failure. Credit card has exceeded maximum number of  transactions allowed by TELUS Mobility during a prescribed period.');
											
INSERT INTO APP_MESSAGES ( APP_ROLE_ID, RESPONSE_CODE, RESPONSE_MESSAGE_EN, RESPONSE_MESSAGE_FR, RESPONSE_KB ) 
   						VALUES ( 'AMDOCS_AGENT', '300', 
								'We cannot process the credit card payment.  One or more of the client details do not match that of the credit card holder.  Please verify the client details and try again.  If you continue to receive this message, please view the memo for additional information.', 
								'Le paiement par carte de cr�dit ne peut pas �tre effectu� car au moins un renseignement sur le client ne correspond pas au titulaire de la carte. Veuillez v�rifier les d�tails sur le client et essayez de nouveau. Si vous obtenez encore ce message, veuillez consulter les m�mos dans Knowbility.', 
								'Transaction failure.  AVS Mismatch. Billing address provided by client must match address on file with credit bureau.');  

INSERT INTO APP_MESSAGES ( APP_ROLE_ID, RESPONSE_CODE, RESPONSE_MESSAGE_EN, RESPONSE_MESSAGE_FR, RESPONSE_KB ) 
						VALUES ( 'AMDOCS_DEFAULT', '100', 
								'Sorry, we cannot process your credit card payment.  Please verify the credit card details you provided and try again.  If the problem persists, please contact your financial institution.', 
								'D�sol�s, votre paiement par carte de cr�dit ne peut pas �tre effectu�. Veuillez v�rifier less renseignements sur la carte de cr�dit et essayez de nouveau. Si le probl�me persiste, veuillez communiquer avec votre institution financi�re.', 
								'Transaction failure. Credit card declined by financial institution. Client should contact financial institution.'); 

INSERT INTO APP_MESSAGES ( APP_ROLE_ID, RESPONSE_CODE, RESPONSE_MESSAGE_EN, RESPONSE_MESSAGE_FR, RESPONSE_KB ) 
						VALUES ( 'AMDOCS_DEFAULT', '101', 
								'Sorry, we cannot complete your credit card payment. Please check your credit card number and expiration date. If you continue to experience difficulties, please contact Client Care by dialing 611 from your handset.', 
								'D�sol�s, votre paiement par carte de cr�dit ne peut pas �tre effectu�. Veuillez v�rifier les renseignements sur la carte de cr�dit. Si le probl�me persiste, veuillez communiquer avec le service � la client�le en composant le 611.', 
								'Transaction failure. Credit card listed in negative database. Use Source form to send details to Loss Prevention.'); 

INSERT INTO APP_MESSAGES ( APP_ROLE_ID, RESPONSE_CODE, RESPONSE_MESSAGE_EN, RESPONSE_MESSAGE_FR, RESPONSE_KB ) 
						VALUES ( 'AMDOCS_DEFAULT', '102', 
								'Sorry, we cannot complete your credit card payment. Please check your credit card number and expiration date. If you have an international credit card, you may need to pre-register it. If you continue to experience difficulties, please contact Client Care by dialing 611 from your handset.', 
								'D�sol�s, votre paiement par carte de cr�dit ne peut pas �tre effectu�. Veuillez v�rifier les renseignements sur la carte de cr�dit. S''il s''agit d''une carte de cr�dit internationale, vous aurez peut-�tre � la pr�enregistrer. Si le probl�me persiste, veuillez communiquer avec le service � la client�le en composant le 611.', 
								'Transaction failure. International credit card. Client must pre-register credit card w/Loss Prevention or use Canadian credit card.'); 

INSERT INTO APP_MESSAGES ( APP_ROLE_ID, RESPONSE_CODE, RESPONSE_MESSAGE_EN, RESPONSE_MESSAGE_FR, RESPONSE_KB ) 
						VALUES ( 'AMDOCS_DEFAULT', '103', 
								'Sorry, we cannot complete your credit card transaction. Please verify the credit card details provided. If the issue continues, please contact Client Care by dialing 611 from your handset.', 
								'D�sol�s, votre paiement par carte de cr�dit ne peut pas �tre effectu�. Veuillez v�rifier les renseignements sur la carte de cr�dit. Si le probl�me persiste, veuillez communiquer avec le service � la client�le en composant le 611.', 
								'Transaction failure. Credit card has exceeded maximum number of  transactions allowed by TELUS Mobility during a prescribed period.'); 

INSERT INTO APP_MESSAGES ( APP_ROLE_ID, RESPONSE_CODE, RESPONSE_MESSAGE_EN, RESPONSE_MESSAGE_FR, RESPONSE_KB ) 
						VALUES ( 'AMDOCS_DEFAULT', '300', 
								'We cannot process your transaction as entered.  Please verify the details you provided and try again.  If you continue to receive this message, please contact Client Care by dailing 611 from your handset.', 
								'Votre paiement par carte de cr�dit ne peut pas �tre effectu� tel que demand�. Veuillez v�rifier les renseignements sur la carte de cr�dit et essayez de nouveau. Si le probl�me persiste, veuillez communiquer avec le service � la client�le en composant le 611.', 
								'Transaction failure.  AVS Mismatch. Billing address provided by client must match address on file with credit bureau.'); 

COMMIT;
