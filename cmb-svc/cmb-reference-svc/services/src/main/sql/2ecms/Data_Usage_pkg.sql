/* Formatted on 2008/06/23 10:38 (Formatter Plus v4.8.5) */
CREATE OR REPLACE PACKAGE data_usage_pkg
AS
------------------------------------------------------------------------------------
-- IMPORTANT NOTE !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
-- Please increment the version_no variable value when you make changes to this package 
-- to reflect version changes.
 ------------------------------------------------------------------------
-- description: Package data_usage_pkg containing procedures
--    for data retrieval from 2ECMS database
--
-- Date           Developer           Modifications
-- 03-23-2006     Michael Qin       getdatausageunits
-- 04-12-2007     Marina Kuper     Changed getdatausagedetails
-- 08-13-2007     Tsz Chung Tong   Changed getdatausagedetails and added getMobileCountries
-- 10-03-2007     Tsz Chung Tong   Changed getdatausagedetails to retrieve RECEIVER_PHONE_NUMBER for Calling Circle Text Messaging
-- 06-19-2008     Marina Kuper     Changed getdatausagedetails and getdatausagesummary to reslolve data type issue
-- 07-14-3008	  Pavel Simonovsky Original package data_usage_pkg is splitted into two separate   
--                                 packages data_usage_pkg and data_usage_3ecms_pkg
--  May-11-2012     Naresh Annabathula     Added getVersion function for shakedown
-------------------------------------------------------------------------
   fleetnotfound            EXCEPTION;
   PRAGMA EXCEPTION_INIT (fleetnotfound, -20160);

-- Reference to a Cursor
   TYPE refcursor IS REF CURSOR;

-- result constants
   numeric_true    CONSTANT NUMBER (1) := 1;
   numeric_false   CONSTANT NUMBER (1) := 0;
   -- package version constant
   version_no          CONSTANT VARCHAR2(10)       := '3.19.4';

--------------------------------------------------------------------------
 FUNCTION getVersion RETURN VARCHAR2;
 
   PROCEDURE getdatausageservicetypes (
      po_data_usage_service_types   OUT   refcursor
   );

   PROCEDURE getdatausageunits (po_data_usage_units OUT refcursor);

   PROCEDURE getmobilecountries (po_mobile_countries OUT refcursor);
--------------------------------------------------------------------------
END;
/

SHO err

CREATE OR REPLACE PACKAGE BODY data_usage_pkg
AS
--################################################################
-- Procedures/Functions
--################################################################
--------------------------------------------------------------------------

  FUNCTION getVersion RETURN VARCHAR2
   IS
   BEGIN
	   RETURN version_no;
	END getVersion;
	
   PROCEDURE getdatausageservicetypes (
      po_data_usage_service_types   OUT   refcursor
   )
   IS
   BEGIN
      OPEN po_data_usage_service_types FOR
         SELECT service_type, description, french_des, service_type_group_cd
           FROM infranet_service_type;
   EXCEPTION
      WHEN NO_DATA_FOUND
      THEN
         IF (po_data_usage_service_types%ISOPEN)
         THEN
            CLOSE po_data_usage_service_types;
         END IF;

         -- return NULL cursor
         OPEN po_data_usage_service_types FOR
            SELECT NULL
              FROM DUAL
             WHERE 1 = 0;
      WHEN OTHERS
      THEN
         IF (po_data_usage_service_types%ISOPEN)
         THEN
            CLOSE po_data_usage_service_types;
         END IF;

         raise_application_error (-20160,
                                     'Get Data Usage Service Types. Oracle:(['
                                  || SQLCODE
                                  || '] ['
                                  || SQLERRM
                                  || '])'
                                 );
   END getdatausageservicetypes;

   PROCEDURE getdatausageunits (po_data_usage_units OUT refcursor)
   IS
   BEGIN
      OPEN po_data_usage_units FOR
         SELECT dr.data_record_uom_cd, event.unit_of_measure_en_des,
                event.unit_of_measure_fr_des
           FROM event_unit_of_measure event, data_record_event_uom dr
          WHERE event.unit_of_measure_cd = dr.unit_of_measure_cd;
   EXCEPTION
      WHEN NO_DATA_FOUND
      THEN
         IF (po_data_usage_units%ISOPEN)
         THEN
            CLOSE po_data_usage_units;
         END IF;

         -- return NULL cursor
         OPEN po_data_usage_units FOR
            SELECT NULL
              FROM DUAL
             WHERE 1 = 0;
      WHEN OTHERS
      THEN
         IF (po_data_usage_units%ISOPEN)
         THEN
            CLOSE po_data_usage_units;
         END IF;

         raise_application_error (-20160,
                                     'Get Data Usage Units. Oracle:(['
                                  || SQLCODE
                                  || '] ['
                                  || SQLERRM
                                  || '])'
                                 );
   END getdatausageunits;

   PROCEDURE getmobilecountries (po_mobile_countries OUT refcursor)
   IS
   BEGIN
      OPEN po_mobile_countries FOR
         SELECT DISTINCT mcc_country_cd, country.country_nm,
                         country.country_nm_french
                    FROM country, mobile_country_code
                   WHERE country.country_cd = mobile_country_code.country_cd
                     AND mcc_country_cd IS NOT NULL;
   EXCEPTION
      WHEN NO_DATA_FOUND
      THEN
         IF (po_mobile_countries%ISOPEN)
         THEN
            CLOSE po_mobile_countries;
         END IF;

         OPEN po_mobile_countries FOR
            SELECT NULL
              FROM DUAL
             WHERE 1 = 0;
      WHEN OTHERS
      THEN
         IF (po_mobile_countries%ISOPEN)
         THEN
            CLOSE po_mobile_countries;
         END IF;

         raise_application_error (-20160,
                                     'Get Mobile Countries. Oracle:(['
                                  || SQLCODE
                                  || '] ['
                                  || SQLERRM
                                  || '])'
                                 );
   END getmobilecountries;
--------------------------------------------------------------------------------------------------------------------------
END;
-- Data_Usage_pkg
/

SHO err

