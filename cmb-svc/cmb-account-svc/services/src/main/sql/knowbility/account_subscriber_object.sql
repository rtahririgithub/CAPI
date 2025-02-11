/* Formatted on 2006/05/05 15:16 (Formatter Plus v4.8.0) */
--DROP TYPE account_subscriber_t
--/
CREATE OR REPLACE TYPE account_subscriber_o AS OBJECT (
   account_id                      NUMBER   (9),
   subscriber_id                   VARCHAR2 (20)
 );
/

CREATE OR REPLACE TYPE account_subscriber_t AS TABLE OF account_subscriber_o
/

COMMIT ;

