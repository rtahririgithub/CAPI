--roll back script

delete from  application_error where application_message_id >= 208 and application_message_id <=265;
delete from adapted_message where application_message_id >= 208 and application_message_id <= 265;
delete from  application_message where application_message_id >= 208 and application_message_id <= 265;


COMMIT;
