DELETE FROM adapted_message where application_message_id  >= 171 and application_message_id <= 190 AND adapted_message_seq = 1;
DELETE FROM adapted_message where application_message_id  >= 171 and application_message_id <= 190 AND adapted_message_seq = 2;
DELETE FROM application_message where application_message_id  >= 171 and application_message_id <= 190;

COMMIT;
