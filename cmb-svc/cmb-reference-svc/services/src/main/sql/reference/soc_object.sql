/* Formatted on 2006/05/05 15:16 (Formatter Plus v4.8.0) */
--DROP TYPE socs_t
--/
CREATE OR REPLACE TYPE soc_o AS OBJECT (
   soc                   VARCHAR2 (9)
 );
/

CREATE OR REPLACE TYPE socs_t AS TABLE OF soc_o
/

COMMIT ;

