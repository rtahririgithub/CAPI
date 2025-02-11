-- The following queries are designed to be executed as a script against the EAS datasource 

UPDATE adapted_message
SET MESSAGE_TEXT = 'TELUS mobile email service will be lost.'
WHERE APPLICATION_MESSAGE_ID = 83 AND LANGUAGE_CD = 'EN';
 

UPDATE adapted_message
SET MESSAGE_TEXT = 'Le service Courriel sans fil TELUS sera perdu.'
WHERE APPLICATION_MESSAGE_ID = 83 AND LANGUAGE_CD = 'FR';

 
UPDATE adapted_message
SET MESSAGE_TEXT = 'TELUS mobile email service may now be provisioned. Please direct client to telusmobility.com/telus-mobile-email for more information.'
WHERE APPLICATION_MESSAGE_ID = 84 AND LANGUAGE_CD = 'EN';

 
UPDATE adapted_message
SET MESSAGE_TEXT = 'Le service Courriel sans fil TELUS peut être maintenant fourni. Veuillez diriger le client vers le site telusmobilite.com/courrielsansfiltelus pour qu’il obtienne de plus amples renseignements.'
WHERE APPLICATION_MESSAGE_ID = 84 AND LANGUAGE_CD = 'FR';

COMMIT;

