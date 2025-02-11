-- Script to insert reference data in the Contact Event Database
-- Carlos Manjarres - May-07-2004  Push Communication Project.

---------------------------------------------------------------------
-- Event Types
---------------------------------------------------------------------
INSERT INTO coneadm.contact_event_type
            (contact_event_type_id, description, description_french,
             outbound_indicator, load_dt, update_dt, user_last_modify
            )
     VALUES (2, 'Creation of Client Portal Account', 'Creation of Client Portal Account','N', sysdate, sysdate, 'manjarca' );

INSERT INTO coneadm.contact_event_type
            (contact_event_type_id, description, description_french,
             outbound_indicator, load_dt, update_dt, user_last_modify
            )
     VALUES (3, 'Client login into Portal Account', 'Client login into Portal Account','N', sysdate, sysdate, 'manjarca' );

INSERT INTO coneadm.contact_event_type
            (contact_event_type_id, description, description_french,
             outbound_indicator, load_dt, update_dt, user_last_modify
            )
     VALUES (4, 'Client requested repair of equipment', 'Client requested repair of equipment','N', sysdate, sysdate, 'manjarca' );

INSERT INTO coneadm.contact_event_type
            (contact_event_type_id, description, description_french,
             outbound_indicator, load_dt, update_dt, user_last_modify
            )
     VALUES (5, 'Client picked up  repair of equipment', 'Client picked up repair of equipment','N', sysdate, sysdate, 'manjarca' );
	 
INSERT INTO coneadm.contact_event_type
            (contact_event_type_id, description, description_french,
             outbound_indicator, load_dt, update_dt, user_last_modify
            )
     VALUES (6, 'Client responded to repair quotation', 'Client responded to repair quotation','N', sysdate, sysdate, 'manjarca' );

INSERT INTO coneadm.contact_event_type
            (contact_event_type_id, description, description_french,
             outbound_indicator, load_dt, update_dt, user_last_modify
            )
     VALUES (7, 'Subscriber authentication attempt', 'Subscriber authentication attempt','N', sysdate, sysdate, 'manjarca' );

INSERT INTO coneadm.contact_event_type
            (contact_event_type_id, description, description_french,
             outbound_indicator, load_dt, update_dt, user_last_modify
            )
     VALUES (8, 'Account owner authentication attempt', 'Account owner authentication attempt','N', sysdate, sysdate, 'manjarca' );

---------------------------------------------------------------------
-- Mechanism Types
---------------------------------------------------------------------
INSERT INTO coneadm.contact_mechanism_type
            (contact_mechanism_id, description, description_french,
             load_dt, update_dt, user_last_modify
            )
     VALUES (2, 'Web portal', 'Web portal',sysdate, sysdate, user ); 
	 
INSERT INTO coneadm.contact_mechanism_type
            (contact_mechanism_id, description, description_french,
             load_dt, update_dt, user_last_modify
            )
     VALUES (3, 'Face-to-face meeting', 'Face-to-face meeting',sysdate, sysdate, user ); 

INSERT INTO coneadm.contact_mechanism_type
            (contact_mechanism_id, description, description_french,
             load_dt, update_dt, user_last_modify
            )
     VALUES (4, 'Telephone', 'Telephone',sysdate, sysdate, user ); 
	 
INSERT INTO coneadm.contact_mechanism_type
            (contact_mechanism_id, description, description_french,
             load_dt, update_dt, user_last_modify
            )
     VALUES (5, 'E-mail', 'E-mail',sysdate, sysdate, user ); 

---------------------------------------------------------------------
-- Allowed Mechanism 
---------------------------------------------------------------------

INSERT INTO coneadm.allowed_cont_event_mech
            (contact_event_type_id, contact_mechanism_id, database_name,
             table_name, load_dt, update_dt, user_last_modify
            )
     VALUES (2, 2, null,
             null, sysdate, sysdate, user );

INSERT INTO coneadm.allowed_cont_event_mech
            (contact_event_type_id, contact_mechanism_id, load_dt, update_dt, user_last_modify
            )
     VALUES (4, 3, sysdate, sysdate, user );

INSERT INTO coneadm.allowed_cont_event_mech
            (contact_event_type_id, contact_mechanism_id, load_dt, update_dt, user_last_modify
            )
     VALUES (5, 3, sysdate, sysdate, user );

INSERT INTO coneadm.allowed_cont_event_mech
            (contact_event_type_id, contact_mechanism_id, load_dt, update_dt, user_last_modify
            )
     VALUES (6, 3, sysdate, sysdate, user );

INSERT INTO coneadm.allowed_cont_event_mech
            (contact_event_type_id, contact_mechanism_id, load_dt, update_dt, user_last_modify
            )
     VALUES (6, 4, sysdate, sysdate, user );

INSERT INTO coneadm.allowed_cont_event_mech
            (contact_event_type_id, contact_mechanism_id, load_dt, update_dt, user_last_modify
            )
     VALUES (6, 5, sysdate, sysdate, user );

INSERT INTO coneadm.allowed_cont_event_mech
            (contact_event_type_id, contact_mechanism_id, database_name,
             table_name, load_dt, update_dt, user_last_modify
            )
     VALUES (7, 2, null,
             null, sysdate, sysdate, user );

INSERT INTO coneadm.allowed_cont_event_mech
            (contact_event_type_id, contact_mechanism_id, database_name,
             table_name, load_dt, update_dt, user_last_modify
            )
     VALUES (8, 2, null,
             null, sysdate, sysdate, user );


---------------------------------------------------------------------
-- SOFTWARE APPLICATION
--        Add all online applications depending on TELUS-API 
---------------------------------------------------------------------
			
INSERT INTO software_application
            (software_application_id, NAME, description, load_dt,
             update_dt, user_last_modify
            )
values(2,	'MERCRY',	'Mercury',	sysdate,	sysdate,	'manjarca');

INSERT INTO software_application
            (software_application_id, NAME, description, load_dt,
             update_dt, user_last_modify
            )
values(3,	'APOLLO',	'Apollo',	sysdate,	sysdate,	'manjarca');

INSERT INTO software_application
            (software_application_id, NAME, description, load_dt,
             update_dt, user_last_modify
            )
values(4,	'SMARTD',	'Smart Desktop',	sysdate,	sysdate,	'manjarca');

INSERT INTO software_application
            (software_application_id, NAME, description, load_dt,
             update_dt, user_last_modify
            )
values(5,	'ARES',	'Retail Activation',	sysdate,	sysdate,	'manjarca');

INSERT INTO software_application
            (software_application_id, NAME, description, load_dt,
             update_dt, user_last_modify
            )
values(6,	'CSA',	'Corporate Store Activation',	sysdate,	sysdate,	'manjarca');

INSERT INTO software_application
            (software_application_id, NAME, description, load_dt,
             update_dt, user_last_modify
            )
values(7,	'CTIADM',	'CTI',	sysdate,	sysdate,	'manjarca');

INSERT INTO software_application
            (software_application_id, NAME, description, load_dt,
             update_dt, user_last_modify
            )
values(8,	'EBILL',	'E-Bill',	sysdate,	sysdate,	'manjarca');

INSERT INTO software_application
            (software_application_id, NAME, description, load_dt,
             update_dt, user_last_modify
            )
values(9,	'ECARE',	'E-Care',	sysdate,	sysdate,	'manjarca');

INSERT INTO software_application
            (software_application_id, NAME, description, load_dt,
             update_dt, user_last_modify
            )
values(10,	'GEMINI',	'Gemini',	sysdate,	sysdate,	'manjarca');

INSERT INTO software_application
            (software_application_id, NAME, description, load_dt,
             update_dt, user_last_modify
            )
values(11,	'IVR_IM',	'IVR',	sysdate,	sysdate,	'manjarca');

INSERT INTO software_application
            (software_application_id, NAME, description, load_dt,
             update_dt, user_last_modify
            )
values(12,	'OFFERM',	'Offer Management',	sysdate,	sysdate,	'manjarca');

INSERT INTO software_application
            (software_application_id, NAME, description, load_dt,
             update_dt, user_last_modify
            )
values(13,	'SSERVE',	'Self Serve',	sysdate,	sysdate,	'manjarca');

INSERT INTO software_application
            (software_application_id, NAME, description, load_dt,
             update_dt, user_last_modify
            )
values(14,	'SWPTRK',	'Swap Track',	sysdate,	sysdate,	'manjarca');

INSERT INTO software_application
            (software_application_id, NAME, description, load_dt,
             update_dt, user_last_modify
            )
values(15,	'WEBRA',	'Web RA',	sysdate,	sysdate,	'manjarca');


COMMIT;
	 
