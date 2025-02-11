CREATE OR REPLACE TYPE service_feature_sml_o AS OBJECT (
   ban							   NUMBER (9),
   subscriber_no                   VARCHAR2 (20),
   feature_code					   CHAR(6),
   soc							   CHAR(9),
   service_type                    CHAR(1),
   ftr_expiration_date             DATE
 );
/

CREATE OR REPLACE TYPE service_feature_sml_t AS TABLE OF service_feature_sml_o
/

COMMIT ;

