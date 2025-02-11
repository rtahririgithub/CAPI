INSERT INTO APP_TMPG_MESSAGES ( APP_ROLE_ID, TMPG_CODE, RESPONSE_CODE )
						VALUES ( 'ECA_WPS', 'AV100051', '104' );

INSERT INTO APP_MESSAGES ( APP_ROLE_ID, RESPONSE_CODE, RESPONSE_MESSAGE_EN, RESPONSE_MESSAGE_FR, RESPONSE_KB ) 
						VALUES ( 'AMDOCS_DEFAULT', '104', 
								'Sorry, we cannot complete your credit card payment. Please check your credit card number and expiration date.   If you continue to experience difficulties, please contact Client Care by dialing 611 from your handset.', 
								'Nous sommes désolés de ne pouvoir traiter votre paiement par carte de crédit. Veuillez vérifier votre numéro de carte de crédit et la date d''expiration. Si vous éprouvez toujours des difficultés, veuillez communiquer avec le Service à la clientèle en composant le 611 à partir de votre téléphone.', 
								'Transaction failure AV100051. Credit card expiry date is in the past.  A valid expiry date must be entered.'); 

INSERT INTO APP_MESSAGES ( APP_ROLE_ID, RESPONSE_CODE, RESPONSE_MESSAGE_EN, RESPONSE_MESSAGE_FR, RESPONSE_KB ) 
						VALUES ( 'AMDOCS_CLIENT', '104', 
								'Sorry, we cannot complete your credit card payment. Please check your credit card number and expiration date.   If you continue to experience difficulties, please contact Client Care by dialing 611 from your handset.', 
								'Nous sommes désolés de ne pouvoir traiter votre paiement par carte de crédit. Veuillez vérifier votre numéro de carte de crédit et la date d''expiration. Si vous éprouvez toujours des difficultés, veuillez communiquer avec le Service à la clientèle en composant le 611 à partir de votre téléphone.', 
								'Transaction failure AV100051. Credit card expiry date is in the past.  A valid expiry date must be entered.'); 
								
INSERT INTO APP_MESSAGES ( APP_ROLE_ID, RESPONSE_CODE, RESPONSE_MESSAGE_EN, RESPONSE_MESSAGE_FR, RESPONSE_KB ) 
						VALUES ( 'AMDOCS_AGENT', '104', 
								'Sorry, we cannot complete the credit card payment. Please check the credit card number and expiration date.',
								'Nous sommes désolés de ne pouvoir traiter votre paiement par carte de crédit. Veuillez vérifier votre numéro de carte de crédit et la date d''expiration.', 
								'Transaction failure AV100051. Credit card expiry date is in the past.  A valid expiry date must be entered.'); 

COMMIT;