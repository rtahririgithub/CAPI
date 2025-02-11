/* Formatted on 2006/05/05 15:16 (Formatter Plus v4.8.0) */
--DROP TYPE share_pp_subscriber_t
--/
CREATE OR REPLACE TYPE share_pp_subscriber_o AS OBJECT (
   soc                      CHAR     (9),
   subscriber_no            VARCHAR2 (20),
   sub_status               CHAR     (1),
   max_subscribers_number   NUMBER   (3,0),
   soc_seq_no               NUMBER   (9,0), 
   sub_status_date          DATE,
   effective_date           DATE
 );
/

CREATE OR REPLACE TYPE share_pp_subscriber_t AS TABLE OF share_pp_subscriber_o
/

COMMIT ;

