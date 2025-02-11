-- The following queries are designed to be executed as a script against the EAS datasource 

UPDATE adapted_message
SET MESSAGE_TEXT = 'You are adding the International Roaming Card as secondary equipment. In order for the Card to work properly, the client needs to have International Dialing and the International Roaming Feature.'
WHERE APPLICATION_MESSAGE_ID = 82 AND LANGUAGE_CD = 'EN';
 

COMMIT;

