DELETE FROM application_error WHERE application_message_id >= 266 and application_message_id <= 325;
DELETE FROM ADAPTED_MESSAGE WHERE application_message_id >= 266 and application_message_id <= 325;
DELETE FROM application_message WHERE application_message_id >= 266 and application_message_id <= 325;
COMMIT;