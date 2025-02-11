UPDATE ADAPTED_MESSAGE SET MESSAGE_TEXT='Le compte ciblé contient un abonné sur un forfait de fusion de minutes. Veuillez enlever les forfaits de fusion de minutes avant d''ajouter un forfait famille, un forfait partagez affaires ou un forfait de fusion de dollars.'
WHERE application_message_id=260 and adapted_message_seq=2 and language_cd='FR';
COMMIT;