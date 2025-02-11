-- rate_id=8

insert into property values (99, 0.15, 8);
commit;


/*--roll back script

delete from  property where configuration_id=99 and value=8;


COMMIT;

*/

