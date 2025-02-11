CREATE OR REPLACE TYPE phone_numbers_o AS OBJECT (
   subscriber_no                    VARCHAR2 (20),
   resource_number                  VARCHAR2 (32)
   
);
/
CREATE OR REPLACE TYPE phone_numbers_t AS TABLE OF phone_numbers_o ;
/
COMMIT ;

