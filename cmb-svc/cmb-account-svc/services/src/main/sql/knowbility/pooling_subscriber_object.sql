CREATE OR REPLACE TYPE pooling_subscriber_o AS OBJECT (
   subscriber_no                   VARCHAR2 (20),
   pool_group_id		   NUMBER (3)
 );
/

CREATE OR REPLACE TYPE pooling_subscribers_t AS TABLE OF pooling_subscriber_o;
/

COMMIT;