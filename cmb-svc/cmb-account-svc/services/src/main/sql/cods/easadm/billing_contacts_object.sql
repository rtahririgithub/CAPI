CREATE OR REPLACE TYPE contacts_o AS OBJECT (
   contact_mechanism_id         NUMBER (5),
   contact_address              VARCHAR2 (60)
   
); 
/
CREATE OR REPLACE 
TYPE contacts_t AS TABLE OF contacts_o ;
/



