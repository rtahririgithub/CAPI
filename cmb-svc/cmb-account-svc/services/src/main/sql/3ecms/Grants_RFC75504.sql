
grant execute on t_service_type_array to BASREAD;

grant select on rated_ipdr_events_pos to BASREAD;
grant select on rated_ipdr_events_pre to BASREAD;

grant execute on data_usage_3ecms_pkg to BASREAD;

create or replace public synonym t_service_type_array for ipadm.t_service_type_array;
create or replace public synonym data_usage_3ecms_pkg for ipadm.data_usage_3ecms_pkg;
create or replace public synonym rated_ipdr_events_pos for ipadm.rated_ipdr_events_pos;
create or replace public synonym rated_ipdr_events_pre for ipadm.rated_ipdr_events_pre;


