DELETE FROM APP_MESSAGES;

INSERT INTO APP_MESSAGES ( APP_ROLE_ID, RESPONSE_CODE, RESPONSE_MESSAGE_EN, RESPONSE_MESSAGE_FR, RESPONSE_KB ) 
						VALUES ( 'HOTSPOT_DEFAULT', '100', 
										'Our payment gateway is unable to process the credit card provided.  Please check with your financial institution or try a different credit card.', 
										'Notre passerelle de paiement ne peut pas traiter le paiement effectué avec la carte de crédit fournie. Veuillez vérifier avec votre institution financière ou essayer avec une autre carte de crédit.', 
										NULL); 

INSERT INTO APP_MESSAGES ( APP_ROLE_ID, RESPONSE_CODE, RESPONSE_MESSAGE_EN, RESPONSE_MESSAGE_FR, RESPONSE_KB ) 
						VALUES ( 'HOTSPOT_DEFAULT', '101', 
											'Our payment gateway is unable to process the credit card provided.  Please try a different credit card.', 
											'Notre passerelle de paiement ne peut pas traiter le paiement effectué avec la carte de crédit fournie. Veuillez essayer avec une autre carte de crédit.', 
											'101 - SUSPECTED FRAUD'); 

INSERT INTO APP_MESSAGES ( APP_ROLE_ID, RESPONSE_CODE, RESPONSE_MESSAGE_EN, RESPONSE_MESSAGE_FR, RESPONSE_KB ) 
						VALUES ( 'HOTSPOT_DEFAULT', '102', 
											'Our payment gateway is currently unable to process international (non-Canadian) Visa cards.  International MasterCard and American Express cards are accepted.  Please try again using one of these cards.', 
											'Notre passerelle de paiement ne peut pas traiter présentement les paiements effectués avec les cartes VISA internationales (non émises au Canada). Les cartes de crédit internationales Mastercard et American Express sont acceptées. Veuillez essayer de nouveau avec l une de ces cartes.', 
											NULL); 

INSERT INTO APP_MESSAGES (APP_ROLE_ID, RESPONSE_CODE, RESPONSE_MESSAGE_EN, RESPONSE_MESSAGE_FR, RESPONSE_KB ) 
						VALUES ( 'HOTSPOT_DEFAULT', '103', 
											'Our payment gateway is unable to process the credit card provided.  Please try again later or try a different credit card.',
										 'Notre passerelle de paiement ne peut pas traiter le paiement effectué avec la carte de crédit fournie. Veuillez essayer de nouveau plus tard ou essayer avec une autre carte de crédit.', 
										 NULL);
										 
INSERT INTO APP_MESSAGES ( APP_ROLE_ID, RESPONSE_CODE, RESPONSE_MESSAGE_EN, RESPONSE_MESSAGE_FR, RESPONSE_KB ) 
						VALUES ( 'AMDOCS_CLIENT', '100', 
											'Sorry, we cannot process your credit card payment.  Please verify the credit card details you provided and try again.  If the problem persists, please contact your financial institution.', 
											'Sorry, we cannot process your credit card payment.  Please verify the credit card details you provided and try again.  If the problem persists, please contact your financial institution.', 
											NULL); 

INSERT INTO APP_MESSAGES ( APP_ROLE_ID, RESPONSE_CODE, RESPONSE_MESSAGE_EN, RESPONSE_MESSAGE_FR, RESPONSE_KB ) 
						VALUES ( 'AMDOCS_CLIENT', '101', 
											'Sorry, we can not complete your credit card payment. Please check your credit card number and expiration date. If you continue to experience difficulties, please contact TELUS Mobility Client Care by dialing 611 from your handset, or by calling 1-866-558-2273.', 
											'Sorry, we can not complete your credit card payment. Please check your credit card number and expiration date. If you continue to experience difficulties, please contact TELUS Mobility Client Care by dialing 611 from your handset, or by calling 1-866-558-2273.', 
											'101 - SUSPECTED FRAUD'); 

INSERT INTO APP_MESSAGES ( APP_ROLE_ID, RESPONSE_CODE, RESPONSE_MESSAGE_EN, RESPONSE_MESSAGE_FR, RESPONSE_KB ) 
						VALUES ( 'AMDOCS_CLIENT', '102', 
											'Sorry, we can not complete your credit card payment. Please check your credit card number and expiration date. If you have an international credit card, you may need to pre-register it. If you continue to experience difficulties, please contact TELUS Mobility Client Care by dialing 611 from your handset, or by calling 1-866-558-2273.', 
											'Sorry, we can not complete your credit card payment. Please check your credit card number and expiration date. If you have an international credit card, you may need to pre-register it. If you continue to experience difficulties, please contact TELUS Mobility Client Care by dialing 611 from your handset, or by calling 1-866-558-2273.', 
											NULL); 

INSERT INTO APP_MESSAGES ( APP_ROLE_ID, RESPONSE_CODE, RESPONSE_MESSAGE_EN, RESPONSE_MESSAGE_FR, RESPONSE_KB ) 
						VALUES ( 'AMDOCS_CLIENT', '103', 
										'Sorry, we can not complete your credit card payment. Please check your credit card number and expiration date. If you continue to experience difficulties, please contact TELUS Mobility Client Care by dialing 611 from your handset, or by calling 1-866-558-2273.', 
										'Sorry, we can not complete your credit card payment. Please check your credit card number and expiration date. If you continue to experience difficulties, please contact TELUS Mobility Client Care by dialing 611 from your handset, or by calling 1-866-558-2273.', 
										NULL); 

INSERT INTO APP_MESSAGES ( APP_ROLE_ID, RESPONSE_CODE, RESPONSE_MESSAGE_EN, RESPONSE_MESSAGE_FR, RESPONSE_KB ) 
						VALUES ( 'AMDOCS_CLIENT', '300', 
											'We cannot process your transaction as entered.  Please verify the details you provided and try again.  If you continue to receive this message, please contact TELUS Mobility Client Care by dailing 611 from your handset, or by calling 1-866-558-2273.', 
											'We cannot process your transaction as entered.  Please verify the details you provided and try again.  If you continue to receive this message, please contact TELUS Mobility Client Care by dailing 611 from your handset, or by calling 1-866-558-2273.', 
											NULL); 
											
INSERT INTO APP_MESSAGES ( APP_ROLE_ID, RESPONSE_CODE, RESPONSE_MESSAGE_EN, RESPONSE_MESSAGE_FR, RESPONSE_KB ) 
						VALUES ( 'AMDOCS_DEALER', '100', 
											'Sorry, we cannot process your credit card payment.  Please verify the credit card details you provided and try again.  If the problem persists, please contact your financial institution.', 
											'Sorry, we cannot process your credit card payment.  Please verify the credit card details you provided and try again.  If the problem persists, please contact your financial institution.', 
											NULL); 

INSERT INTO APP_MESSAGES ( APP_ROLE_ID, RESPONSE_CODE, RESPONSE_MESSAGE_EN, RESPONSE_MESSAGE_FR, RESPONSE_KB ) 
						VALUES ( 'AMDOCS_DEALER', '101', 
											'Sorry, we cannot process your credit card payment. Please verify the credit card details you provided. If the information supplied is correct, please contact TELUS Mobility.', 
											'Sorry, we cannot process your credit card payment. Please verify the credit card details you provided. If the information supplied is correct, please contact TELUS Mobility.', 
											'101 - SUSPECTED FRAUD'); 

INSERT INTO APP_MESSAGES ( APP_ROLE_ID, RESPONSE_CODE, RESPONSE_MESSAGE_EN, RESPONSE_MESSAGE_FR, RESPONSE_KB ) 
						VALUES ( 'AMDOCS_DEALER', '102', 
											'Sorry, we cannot complete your credit card payment. Please check the credit card number and expiration date. If this is an international credit card, you may need to pre-register it. If the problem persists, please contact TELUS Mobility.', 
											'Sorry, we cannot complete your credit card payment. Please check the credit card number and expiration date. If this is an international credit card, you may need to pre-register it. If the problem persists, please contact TELUS Mobility.', 
											NULL); 

INSERT INTO APP_MESSAGES ( APP_ROLE_ID, RESPONSE_CODE, RESPONSE_MESSAGE_EN, RESPONSE_MESSAGE_FR, RESPONSE_KB ) 
						VALUES ( 'AMDOCS_DEALER', '103', 
											'Sorry, we cannot process your credit card payment. Please verify the credit card details you provided. If the information supplied is correct, please contact TELUS Mobility.', 
											'Sorry, we cannot process your credit card payment. Please verify the credit card details you provided. If the information supplied is correct, please contact TELUS Mobility.', 
											NULL); 
											
INSERT INTO APP_MESSAGES ( APP_ROLE_ID, RESPONSE_CODE, RESPONSE_MESSAGE_EN, RESPONSE_MESSAGE_FR, RESPONSE_KB ) 
						VALUES ( 'AMDOCS_DEALER', '300', 
											'Sorry, we cannot process your credit card payment. Please verify the credit card details you provided and try again.  If you continue to receive this message, please contact TELUS Mobility.', 
											'Sorry, we cannot process your credit card payment. Please verify the credit card details you provided and try again.  If you continue to receive this message, please contact TELUS Mobility.', 
											NULL); 

INSERT INTO APP_MESSAGES ( APP_ROLE_ID, RESPONSE_CODE, RESPONSE_MESSAGE_EN, RESPONSE_MESSAGE_FR, RESPONSE_KB ) 
						VALUES ( 'AMDOCS_AGENT', '100', 
											'Sorry, we cannot process your credit card payment.  Please verify the credit card details you provided and try again.  If the problem persists, please contact your financial institution.', 
											'Sorry, we cannot process your credit card payment.  Please verify the credit card details you provided and try again.  If the problem persists, please contact your financial institution.', 
											NULL); 

INSERT INTO APP_MESSAGES ( APP_ROLE_ID, RESPONSE_CODE, RESPONSE_MESSAGE_EN, RESPONSE_MESSAGE_FR, RESPONSE_KB ) 
						VALUES ( 'AMDOCS_AGENT', '101', 
											'The credit card payment cannot be processed.  Please verify the details you provided. If the information supplied is correct, please forward details to FWSM.', 
											'The credit card payment cannot be processed.  Please verify the details you provided. If the information supplied is correct, please forward details to Loss Prevention.', 
											'101 - SUSPECTED FRAUD'); 

INSERT INTO APP_MESSAGES ( APP_ROLE_ID, RESPONSE_CODE, RESPONSE_MESSAGE_EN, RESPONSE_MESSAGE_FR, RESPONSE_KB ) 
						VALUES ( 'AMDOCS_AGENT', '102', 
											'We cannot process the credit card payment at this time because the credit card being used is International.  International credit cards must be pre-registered prior to use at TELUS Mobility.', 
											'We cannot process the credit card payment at this time because the credit card being used is International.  International credit cards must be pre-registered prior to use at TELUS Mobility.', 
											NULL); 

INSERT INTO APP_MESSAGES ( APP_ROLE_ID, RESPONSE_CODE, RESPONSE_MESSAGE_EN, RESPONSE_MESSAGE_FR, RESPONSE_KB ) 
						VALUES ( 'AMDOCS_AGENT', '103', 
											'The credit card payment cannot be processed.  Please verify the details you provided. If the information supplied is correct, please forward details to FWSM.', 
											'The credit card payment cannot be processed.  Please verify the details you provided. If the information supplied is correct, please forward details to Loss Prevention.', 
											NULL);
											
INSERT INTO APP_MESSAGES ( APP_ROLE_ID, RESPONSE_CODE, RESPONSE_MESSAGE_EN, RESPONSE_MESSAGE_FR, RESPONSE_KB ) 
						VALUES ( 'AMDOCS_AGENT', '300', 
											'We cannot process the credit card payment.  One or more of the client details do not match that of the credit card holder.  Please verify the client details and try again.  If you continue to receive this message, please view the KB memo for additional information.', 
											'We cannot process the credit card payment.  One or more of the client details do not match that of the credit card holder.  Please verify the client details and try again.  If you continue to receive this message, please view the KB memo for additional information.', 
											NULL);  

INSERT INTO APP_MESSAGES ( APP_ROLE_ID, RESPONSE_CODE, RESPONSE_MESSAGE_EN, RESPONSE_MESSAGE_FR, RESPONSE_KB ) 
						VALUES ( 'AMDOCS_DEFAULT', '100', 
											'Sorry, we cannot process your credit card payment.  Please verify the credit card details you provided and try again.  If the problem persists, please contact your financial institution.', 
											'Sorry, we cannot process your credit card payment.  Please verify the credit card details you provided and try again.  If the problem persists, please contact your financial institution.', 
											NULL); 

INSERT INTO APP_MESSAGES ( APP_ROLE_ID, RESPONSE_CODE, RESPONSE_MESSAGE_EN, RESPONSE_MESSAGE_FR, RESPONSE_KB ) 
						VALUES ( 'AMDOCS_DEFAULT', '101', 
											'Sorry, we cannot complete your credit card payment. Please check your credit card number and expiration date. If you continue to experience difficulties, please contact TELUS Mobility Client Care by dialing 611 from your handset, or by calling 1-866-558-2273.', 
											'Sorry, we cannot complete your credit card payment. Please check your credit card number and expiration date. If you continue to experience difficulties, please contact TELUS Mobility Client Care by dialing 611 from your handset, or by calling 1-866-558-2273.', 
											'101 - SUSPECTED FRAUD'); 

INSERT INTO APP_MESSAGES ( APP_ROLE_ID, RESPONSE_CODE, RESPONSE_MESSAGE_EN, RESPONSE_MESSAGE_FR, RESPONSE_KB ) 
						VALUES ( 'AMDOCS_DEFAULT', '102', 
										'Sorry, we cannot complete your credit card payment. Please check your credit card number and expiration date. If you have an international credit card, you may need to pre-register it. If you continue to experience difficulties, please contact TELUS Mobility Client Care by dialing 611 from your handset, or by calling 1-866-558-2273.', 
										'Sorry, we cannot complete your credit card payment. Please check your credit card number and expiration date. If you have an international credit card, you may need to pre-register it. If you continue to experience difficulties, please contact TELUS Mobility Client Care by dialing 611 from your handset, or by calling 1-866-558-2273.', 
										NULL); 

INSERT INTO APP_MESSAGES ( APP_ROLE_ID, RESPONSE_CODE, RESPONSE_MESSAGE_EN, RESPONSE_MESSAGE_FR, RESPONSE_KB ) 
						VALUES ( 'AMDOCS_DEFAULT', '103', 
											'Sorry, we cannot complete your credit card payment. Please check your credit card number and expiration date. If you continue to experience difficulties, please contact TELUS Mobility Client Care by dialing 611 from your handset, or by calling 1-866-558-2273.', 
											'Sorry, we cannot complete your credit card payment. Please check your credit card number and expiration date. If you continue to experience difficulties, please contact TELUS Mobility Client Care by dialing 611 from your handset, or by calling 1-866-558-2273.', 
											NULL); 

INSERT INTO APP_MESSAGES ( APP_ROLE_ID, RESPONSE_CODE, RESPONSE_MESSAGE_EN, RESPONSE_MESSAGE_FR, RESPONSE_KB ) 
						VALUES ( 'AMDOCS_DEFAULT', '300', 
											'We cannot process your transaction as entered.  Please verify the details you provided and try again.  If you continue to receive this message, please contact TELUS Mobility Client Care by dailing 611 from your handset, or by calling 1-866-558-2273.', 
											'We cannot process your transaction as entered.  Please verify the details you provided and try again.  If you continue to receive this message, please contact TELUS Mobility Client Care by dailing 611 from your handset, or by calling 1-866-558-2273.', 
											NULL); 

COMMIT;
