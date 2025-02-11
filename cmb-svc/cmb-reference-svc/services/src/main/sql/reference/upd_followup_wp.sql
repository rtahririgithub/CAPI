delete from follow_up_type_assigned_wp where work_position_code = '12169' and follow_up_type in ('BILR', 'BIRD','BRCH', 'CSFD', 'CANV','RPOM');
insert into follow_up_type_assigned_wp values ('BILR',SYSDATE,NULL,SYSDATE,NULL,'12169');
insert into follow_up_type_assigned_wp values ('BIRD',SYSDATE,NULL,SYSDATE,NULL,'12169');
insert into follow_up_type_assigned_wp values ('RPOM',SYSDATE,NULL,SYSDATE,NULL,'12169');
insert into follow_up_type_assigned_wp values ('BRCH',SYSDATE,NULL,SYSDATE,NULL,'12169');
insert into follow_up_type_assigned_wp values ('CSFD',SYSDATE,NULL,SYSDATE,NULL,'12169');
insert into follow_up_type_assigned_wp values ('CANV',SYSDATE,NULL,SYSDATE,NULL,'12169');
commit;
