drop table property;
drop table configuration;

CREATE TABLE configuration (
       configuration_id     NUMBER(9) NOT NULL,
       name                 VARCHAR2(1024) NOT NULL,
       parent_id            NUMBER(9) NOT NULL,
       CONSTRAINT PK_Configuration
       PRIMARY KEY ( configuration_id   ) 
);

CREATE UNIQUE INDEX IU1_configuration ON configuration
(
       name,
       parent_id
);



                             
CREATE SEQUENCE configuration_SEQ;                             

--------------------------------------------------------------------------------------------


CREATE TABLE property (
       configuration_id     NUMBER(9) NOT NULL,
       name                 VARCHAR2(1024) NOT NULL,
       value                VARCHAR2(1024) NULL,
       CONSTRAINT PKProperty
       PRIMARY KEY (configuration_id, name) 
);




                             
                             
--------------------------------------------------------------------------
grant select, delete, insert, update on CONFIGURATION   to actv_app;     
                                                                         
                                                                         
                                                                         
 grant select, delete, insert, update on PROPERTY  to actv_app;          
                                                                         
                                                                         
 grant select on CONFIGURATION_SEQ     to actv_app; 
 
 grant select, delete, insert, update on log   to actv_app; 
 
 grant select, delete, insert, update on activation_log  to actv_app;    
            
 grant execute on Log_Utility  to actv_app;    
 
 grant select on act_log_seq     to actv_app;    
 
 grant select on log_seq     to actv_app;               