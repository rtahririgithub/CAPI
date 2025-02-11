--roll back script

delete from  application_error where application_message_id >= 204 and application_message_id<207;
delete from adapted_message where application_message_id >= 204 and application_message_id<207;
delete from  application_message where application_message_id >= 204 and application_message_id<207;


COMMIT;
