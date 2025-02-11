 
 
--'VAD','CRC','LST', 'CRQ','PRTO','STL','SUPO','RPR'
 
insert into status_activity_ext (status_code, activity_code, activity_reason_code, status_level,port_out_allowed_ind)
  values ('S','SUS','VAD ','S','Y');

insert into status_activity_ext (status_code, activity_code, activity_reason_code, status_level,port_out_allowed_ind)
  values ('S','SUS','CRC ','S','Y');

insert into status_activity_ext (status_code, activity_code, activity_reason_code, status_level,port_out_allowed_ind)
  values ('S','SUS','LST ','S','Y');

insert into status_activity_ext (status_code, activity_code, activity_reason_code, status_level,port_out_allowed_ind)
  values ('S','SUS','CRQ ','S','Y');

insert into status_activity_ext (status_code, activity_code, activity_reason_code, status_level,port_out_allowed_ind)
  values ('S','SUS','PRTO','S','Y');

insert into status_activity_ext (status_code, activity_code, activity_reason_code, status_level,port_out_allowed_ind)
  values ('S','SUS','STL ','S','Y');

insert into status_activity_ext (status_code, activity_code, activity_reason_code, status_level,port_out_allowed_ind)
  values ('S','SUS','SUPO','S','Y');

insert into status_activity_ext (status_code, activity_code, activity_reason_code, status_level,port_out_allowed_ind)
  values ('S','SUS','RPR ','S','Y');

commit;
