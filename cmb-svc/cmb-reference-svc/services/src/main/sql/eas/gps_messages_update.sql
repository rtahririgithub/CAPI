UPDATE adapted_message SET
MESSAGE_TEXT='Advise client that new handset is GPS capable. For information on how to take advantage of the GPS services proceed to mytelusmobility.com'
WHERE APPLICATION_MESSAGE_ID=87 AND ADAPTED_MESSAGE_SEQ=1;

UPDATE adapted_message SET
MESSAGE_TEXT='Informez le client que le nouvel appareil est compatible avec les fonctionnalités GPS. Pour savoir comment bénéficier des services GPS, accédez au site montelusmobilite.com'
WHERE APPLICATION_MESSAGE_ID=87 AND ADAPTED_MESSAGE_SEQ=2;

UPDATE adapted_message SET
MESSAGE_TEXT='Advise client that the handset they are swapping to does not support GPS capabilities. All GPS based services will automatically be removed as part of the swap'
WHERE APPLICATION_MESSAGE_ID=88 AND ADAPTED_MESSAGE_SEQ=1;

UPDATE adapted_message SET
MESSAGE_TEXT='Veuillez informer le client que l’appareil échangé ne supporte pas les fonctions GPS.  Tout les services relatifs au GPS vont être enlevés/perdus après l’échange'
WHERE APPLICATION_MESSAGE_ID=88 AND ADAPTED_MESSAGE_SEQ=2;

COMMIT;