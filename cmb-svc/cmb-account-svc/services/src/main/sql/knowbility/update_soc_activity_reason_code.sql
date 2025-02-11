delete from SOC_ACTIVITY_REASON_CODE where activity_code = 'SUS' and activity_reason_code in ('LST', 'STL');

insert into soc_activity_reason_code (soc, activity_code, activity_reason_code)
values ('MKVAD150 ', 'SUS', 'STL ');

insert into soc_activity_reason_code (soc, activity_code, activity_reason_code)
values ('MKVAD150 ', 'SUS', 'LST ');

