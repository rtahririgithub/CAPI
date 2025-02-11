CREATE OR REPLACE PACKAGE ACC_ATTRIB_RETRIEVAL_PKG
AS
-------------------------------------------------------------------------
-- IMPORTANT NOTE !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
-- Please increment the version_no variable value when you make changes to this package 
-- to reflect version changes.
--------------------------------------------------------------------------
-- Date           Version	Developer           Modifications
-- Mar 26, 2013   3.23.1	Tsz Chung Tong      Refactored getAccountInfoByBAN from RA_UTILITY_PKG to this package

-------------------------------------------------------------------------------------------------------
   TYPE refcursor IS REF CURSOR;
	version_no          CONSTANT VARCHAR2(10)       := '3.23.1';
	
	FUNCTION getVersion RETURN VARCHAR2; 
	
	PROCEDURE getConsentIndicator(
		pi_ban	NUMBER,
		c_consent_inds_cur OUT REFCURSOR
	);
	
	PROCEDURE getContactName(
	pi_ban NUMBER,
	pi_logical_date DATE,
	c_contact_name OUT REFCURSOR
	);

END ACC_ATTRIB_RETRIEVAL_PKG;
/

SHO err

CREATE OR REPLACE PACKAGE BODY ACC_ATTRIB_RETRIEVAL_PKG
AS
FUNCTION getVersion RETURN VARCHAR2
   IS
   BEGIN
	   RETURN version_no;
	END getVersion;

PROCEDURE getConsentIndicator(
		pi_ban	NUMBER,
		c_consent_inds_cur OUT REFCURSOR
	)
	IS
	BEGIN
	OPEN c_consent_inds_cur
      FOR
         SELECT cpui_cd
           FROM ban_cpui bc
          WHERE bc.ban = pi_ban;
	END getConsentIndicator;
	
PROCEDURE getContactName(
	pi_ban NUMBER,
	pi_logical_date DATE,
	c_contact_name OUT REFCURSOR
	)
	IS
	BEGIN
	OPEN c_contact_name
    FOR
         SELECT nd.first_name, nd.last_business_name, nd.middle_initial,
                nd.name_title, nd.additional_title, nd.name_suffix
           FROM address_name_link anl, name_data nd 
          WHERE anl.ban = pi_ban
            AND (   TRUNC (anl.expiration_date) > TRUNC (pi_logical_date)
                 OR anl.expiration_date IS NULL
                )
            AND anl.link_type = 'C'
            AND nd.name_id = anl.name_id;
	END getContactName;
END ACC_ATTRIB_RETRIEVAL_PKG;
/

SHO err