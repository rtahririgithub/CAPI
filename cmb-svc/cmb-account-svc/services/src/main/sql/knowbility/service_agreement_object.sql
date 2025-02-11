CREATE OR REPLACE TYPE service_agreement_o AS OBJECT (
   ban			                   NUMBER   (9),
   subscriber_no                   VARCHAR2 (20),
   soc							   CHAR(9),
   effective_date				   DATE,
   service_type					   CHAR(1),
   expiration_date				   DATE
 );
/

CREATE OR REPLACE TYPE service_agreement_t AS TABLE OF service_agreement_o
/

COMMIT ;

