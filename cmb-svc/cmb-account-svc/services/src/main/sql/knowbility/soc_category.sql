DROP TYPE soc_category_t
/

CREATE OR REPLACE 
TYPE soc_category_o AS OBJECT (
   soc                      CHAR     (9),
   category_code            VARCHAR2 (3),
   restr_start_date         DATE
   --restr_end_date           DATE
 );
/
CREATE OR REPLACE TYPE soc_category_t AS TABLE OF soc_category_o
/
COMMIT ;


