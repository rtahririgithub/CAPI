
UPDATE adapted_message 
SET MESSAGE_TEXT='ESN is locked. Please return to the point of purchase.'
WHERE APPLICATION_MESSAGE_ID=89 AND ADAPTED_MESSAGE_SEQ=1;

UPDATE adapted_message 
SET MESSAGE_TEXT='Le num�ro ESN est verrouill�. Veuillez retourner � l�endroit o� vous avez achet� l�appareil.' 
WHERE APPLICATION_MESSAGE_ID=89 AND ADAPTED_MESSAGE_SEQ=2;

COMMIT;
