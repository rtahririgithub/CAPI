
UPDATE adapted_message 
SET MESSAGE_TEXT='ESN is locked. Please return to the point of purchase.'
WHERE APPLICATION_MESSAGE_ID=89 AND ADAPTED_MESSAGE_SEQ=1;

UPDATE adapted_message 
SET MESSAGE_TEXT='Le numéro ESN est verrouillé. Veuillez retourner à l’endroit où vous avez acheté l’appareil.' 
WHERE APPLICATION_MESSAGE_ID=89 AND ADAPTED_MESSAGE_SEQ=2;

COMMIT;
