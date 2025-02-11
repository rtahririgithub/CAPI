------------------------------------------------------------------------
-- description: Object pd_entries_o and Object pd_entries_t as a collection of pd_entries_o
--    for managing Phone Directory Entries in CODS database
--
-- Date           Developer           Modifications
-- Feb-18-2010    Daniel Canelea  Created new OBJECT pd_entries_o and TYPE pd_entries_t
-- Mar-08-2010    Daniel Canelea  Removed EASADM reference for types creation
-- Apr-07-2010    Tsz Chung Tong  Changed length of pd_entry_phone_number to 256
--                                        length of pd_entry_nickname to 60
-- Apr-08-2001    Tsz Chung Tong  Renamed pd_entry_creation_date to pd_entry_effective_date
--------------------------------------------------------------------------------------


CREATE OR REPLACE TYPE pd_entries_o AS OBJECT (
   pd_entry_phone_number    VARCHAR  (256),
   pd_entry_nickname        VARCHAR2 (60),
   pd_entry_effective_date  TIMESTAMP,
   pd_entry_exists          VARCHAR  (1)
); 
/

--------------------------------------------------------------------------------------
CREATE OR REPLACE
TYPE pd_entries_t AS TABLE OF pd_entries_o ;
/

