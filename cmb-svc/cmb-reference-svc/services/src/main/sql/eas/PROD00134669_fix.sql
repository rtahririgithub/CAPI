update adapted_message
   set brand_id = 2, update_dt=sysdate, user_last_modify=user, audience_type_id = null
 where application_message_id in (102, 103, 105, 106, 107, 109) and adapted_message_seq in (3, 4);
 
commit;                                      

