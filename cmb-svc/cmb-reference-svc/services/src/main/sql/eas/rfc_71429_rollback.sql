DELETE FROM adapted_message where application_message_id  >= 158 and application_message_id <= 170 AND adapted_message_seq = 1;
DELETE FROM adapted_message where application_message_id  >= 167 and application_message_id <= 170 AND adapted_message_seq = 2;
DELETE FROM application_message where application_message_id  >= 158 and application_message_id <= 170;


DELETE FROM adapted_message where application_message_id  in (92, 93, 94, 96, 97, 98, 99, 104) AND adapted_message_seq in (3, 4);
DELETE FROM adapted_message where application_message_id  in (102, 103, 105, 106, 107, 109) AND adapted_message_seq in (5, 6);
COMMIT;
