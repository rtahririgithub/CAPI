CREATE OR REPLACE PACKAGE data_usage_3ecms_pkg
AS
------------------------------------------------------------------------------------
-- IMPORTANT NOTE !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
-- Please increment the version_no variable value when you make changes to this package 
-- to reflect version changes.
 -------------------------------------------------------------------------------------
-- description: Package data_usage_pkg containing procedures
--    for data retrieval from 3ECMS database
--
-- Date           Developer           Modifications
-- 03-23-2006     Michael Qin       getdatausageunits
-- 04-12-2007     Marina Kuper     Changed getdatausagedetails
-- 08-13-2007     Tsz Chung Tong   Changed getdatausagedetails and added getMobileCountries
-- 10-03-2007     Tsz Chung Tong   Changed getdatausagedetails to retrieve RECEIVER_PHONE_NUMBER for Calling Circle Text Messaging
-- 06-19-2008     Marina Kuper     Changed getdatausagedetails and getdatausagesummary to reslolve data type issue
-- 07-14-3008	  Pavel Simonovsky Original package data_usage_pkg is splitted into two separate   
--                                 packages data_usage_pkg and data_usage_3ecms_pkg
-- 12-05-2008     Marina Kuper     Added getUnpaidDataUsageTotal
--  May-09-2012     Naresh Annabathula     Added getVersion function for shakedown
-------------------------------------------------------------------------------
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
   
   PROCEDURE getdatausagedetails (
      pi_ban             IN       VARCHAR2,
      pi_phone_nbr       IN       VARCHAR2,
      pi_service_type    IN       VARCHAR2,
      pi_from_date       IN       VARCHAR2,
      pi_to_date         IN       VARCHAR2,
      pi_postpaid        IN       NUMBER,
      po_usage_details   OUT      refcursor
   );

   PROCEDURE getdatausagesummary (
      pi_ban             IN       VARCHAR2,
      pi_phone_nbr       IN       VARCHAR2,
      pi_from_date       IN       VARCHAR2,
      pi_to_date         IN       VARCHAR2,
      pi_postpaid        IN       NUMBER,
      po_usage_summary   OUT      refcursor
   );

   PROCEDURE getdatausagesummary (
      pi_service_types   IN       t_service_type_array,
      pi_ban             IN       VARCHAR2,
      pi_phone_nbr       IN       VARCHAR2,
      pi_from_date       IN       VARCHAR2,
      pi_to_date         IN       VARCHAR2,
      pi_postpaid        IN       NUMBER,
      po_usage_summary   OUT      refcursor
   );
   
    PROCEDURE getUnpaidDataUsageTotal (
      pi_ban             IN       VARCHAR2,
      pi_event_time      IN       VARCHAR2,
      po_usage_total   OUT      NUMBER
   );

--------------------------------------------------------------------------
END;
/

SHO err

CREATE OR REPLACE PACKAGE BODY data_usage_3ecms_pkg
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
	
   PROCEDURE getdatausagedetails (
      pi_ban             IN       VARCHAR2,
      pi_phone_nbr       IN       VARCHAR2,
      pi_service_type    IN       VARCHAR2,
      pi_from_date       IN       VARCHAR2,
      pi_to_date         IN       VARCHAR2,
      pi_postpaid        IN       NUMBER,
      po_usage_details   OUT      refcursor
   )
   IS
   BEGIN
------------------------------------------------------------------
-- Create reference to a cursor  using SQL statement
------------------------------------------------------------------
      IF pi_postpaid = numeric_true
      THEN
         OPEN po_usage_details FOR
            SELECT   /*+ INDEX (riep RATED_IP_POS_MIN ) */
                     event_time, event_type, event_name, units, amount,
                     NVL (title_nm, ''), NVL (artist_nm, ''),
                     NVL (album_nm, ''), content_size_no,
                     NVL (content_size_uom_cd, ''),
                     NVL (unit_of_measure, ''), event_source,
                     NVL (TO_CHAR (mcc_country_cd), ''),
                     NVL (receiver_phone_number, '')
                FROM rated_ipdr_events_pos riep
               WHERE ban = pi_ban
                 AND phone_number = pi_phone_nbr
                 AND event_type = pi_service_type
                 AND event_time >= TO_DATE (pi_from_date, 'MM/DD/YYYY')
                 AND event_time < TO_DATE (pi_to_date, 'MM/DD/YYYY')
            ORDER BY event_time DESC;
      ELSE
         OPEN po_usage_details FOR
            SELECT   /*+ INDEX (riep rated_ipdr_events_pre_1IX ) */
                     event_time, event_type, event_name, units, amount,
                     NVL (title_nm, ''), NVL (artist_nm, ''),
                     NVL (album_nm, ''), content_size_no,
                     NVL (content_size_uom_cd, ''),
                     NVL (unit_of_measure, ''), event_source,
                     NVL (TO_CHAR (mcc_country_cd), ''),
                     NVL (receiver_phone_number, '')
                FROM rated_ipdr_events_pre riep
               WHERE ban = pi_ban
                 AND phone_number = pi_phone_nbr
                 AND event_type = pi_service_type
                 AND event_time >= TO_DATE (pi_from_date, 'MM/DD/YYYY')
                 AND event_time < TO_DATE (pi_to_date, 'MM/DD/YYYY')
            ORDER BY event_time DESC;
      END IF;
   EXCEPTION
      WHEN NO_DATA_FOUND
      THEN
         IF (po_usage_details%ISOPEN)
         THEN
            CLOSE po_usage_details;
         END IF;

         -- return NULL cursor
         OPEN po_usage_details FOR
            SELECT NULL
              FROM DUAL
             WHERE 1 = 0;
      WHEN OTHERS
      THEN
         IF (po_usage_details%ISOPEN)
         THEN
            CLOSE po_usage_details;
         END IF;

         raise_application_error (-20160,
                                     'Get Data Usage Details. Oracle:(['
                                  || SQLCODE
                                  || '] ['
                                  || SQLERRM
                                  || '])'
                                 );
   END getdatausagedetails;

--------------------------------------------------------------------------
   PROCEDURE getdatausagesummary (
      pi_ban             IN       VARCHAR2,
      pi_phone_nbr       IN       VARCHAR2,
      pi_from_date       IN       VARCHAR2,
      pi_to_date         IN       VARCHAR2,
      pi_postpaid        IN       NUMBER,
      po_usage_summary   OUT      refcursor
   )
   IS
   BEGIN
------------------------------------------------------------------
-- Create reference to a cursor  using SQL statement
------------------------------------------------------------------
      IF pi_postpaid = numeric_true
      THEN
         OPEN po_usage_summary FOR
            SELECT   /*+ INDEX (riep RATED_IP_POS_MIN ) */
                     TRUNC (event_time), event_type, SUM (units),
                     SUM (amount), MAX (unit_of_measure)
                FROM rated_ipdr_events_pos riep
               WHERE event_time >= TO_DATE (pi_from_date, 'MM/DD/YYYY')
                 AND event_time < TO_DATE (pi_to_date, 'MM/DD/YYYY')
                 AND ban = pi_ban
                 AND phone_number = pi_phone_nbr
            GROUP BY TRUNC (event_time), event_type
            ORDER BY TRUNC (event_time) DESC, event_type;
      ELSE
         OPEN po_usage_summary FOR
            SELECT   /*+ INDEX (riep rated_ipdr_events_pre_1IX) */
                     TRUNC (event_time), event_type, SUM (units),
                     SUM (amount), MAX (unit_of_measure)
                FROM rated_ipdr_events_pre riep
               WHERE event_time >= TO_DATE (pi_from_date, 'MM/DD/YYYY')
                 AND event_time < TO_DATE (pi_to_date, 'MM/DD/YYYY')
                 AND ban = pi_ban
                 AND phone_number = pi_phone_nbr
            GROUP BY TRUNC (event_time), event_type
            ORDER BY TRUNC (event_time) DESC, event_type;
      END IF;
   EXCEPTION
      WHEN NO_DATA_FOUND
      THEN
         IF (po_usage_summary%ISOPEN)
         THEN
            CLOSE po_usage_summary;
         END IF;

         -- return NULL cursor
         OPEN po_usage_summary FOR
            SELECT NULL
              FROM DUAL
             WHERE 1 = 0;
      WHEN OTHERS
      THEN
         IF (po_usage_summary%ISOPEN)
         THEN
            CLOSE po_usage_summary;
         END IF;

         raise_application_error (-20160,
                                     'Get Data Usage Summary. Oracle:(['
                                  || SQLCODE
                                  || '] ['
                                  || SQLERRM
                                  || '])'
                                 );
   END getdatausagesummary;

--------------------------------------------------------------------------------------------------------------------------
   PROCEDURE getdatausagesummary (
      pi_service_types   IN       t_service_type_array,
      pi_ban             IN       VARCHAR2,
      pi_phone_nbr       IN       VARCHAR2,
      pi_from_date       IN       VARCHAR2,
      pi_to_date         IN       VARCHAR2,
      pi_postpaid        IN       NUMBER,
      po_usage_summary   OUT      refcursor
   )
   IS
      v_cursor_text    VARCHAR2 (32767);
      v_service_type   VARCHAR2 (256);                                --- ???
      v_table_name     VARCHAR2 (25);
      i_index          BINARY_INTEGER;
   BEGIN
      IF pi_postpaid = numeric_true
      THEN
         OPEN po_usage_summary FOR
            SELECT   /*+ INDEX (riep RATED_IP_POS_MIN ) */
                     TRUNC (event_time), event_type, SUM (units),
                     SUM (amount), MAX (unit_of_measure)
                FROM rated_ipdr_events_pos riep
               WHERE event_time >= TO_DATE (pi_from_date, 'MM/DD/YYYY')
                 AND event_time < TO_DATE (pi_to_date, 'MM/DD/YYYY')
                 AND ban = pi_ban
                 AND phone_number = pi_phone_nbr
                 AND event_type IN (
                        SELECT *
                          FROM TABLE
                                  (CAST
                                      (pi_service_types AS t_service_type_array
                                      )
                                  ))
            GROUP BY TRUNC (event_time), event_type
            ORDER BY TRUNC (event_time) DESC, event_type;
      ELSE
         OPEN po_usage_summary FOR
            SELECT   /*+ INDEX (riep rated_ipdr_events_pre_1IX) */
                     TRUNC (event_time), event_type, SUM (units),
                     SUM (amount), MAX (unit_of_measure)
                FROM rated_ipdr_events_pre riep
               WHERE event_time >= TO_DATE (pi_from_date, 'MM/DD/YYYY')
                 AND event_time < TO_DATE (pi_to_date, 'MM/DD/YYYY')
                 AND ban = pi_ban
                 AND phone_number = pi_phone_nbr
                 AND event_type IN (
                        SELECT *
                          FROM TABLE
                                  (CAST
                                      (pi_service_types AS t_service_type_array
                                      )
                                  ))
            GROUP BY TRUNC (event_time), event_type
            ORDER BY TRUNC (event_time) DESC, event_type;
      END IF;
   EXCEPTION
      WHEN NO_DATA_FOUND
      THEN
         IF (po_usage_summary%ISOPEN)
         THEN
            CLOSE po_usage_summary;
         END IF;

         -- return NULL cursor
         OPEN po_usage_summary FOR
            SELECT NULL
              FROM DUAL
             WHERE 1 = 0;
      WHEN OTHERS
      THEN
         IF (po_usage_summary%ISOPEN)
         THEN
            CLOSE po_usage_summary;
         END IF;

         raise_application_error (-20160,
                                     'Get Data Usage Summary. Oracle:(['
                                  || SQLCODE
                                  || '] ['
                                  || SQLERRM
                                  || '])'
                                 );
   END getdatausagesummary;
   
   PROCEDURE getUnpaidDataUsageTotal (
      pi_ban             IN       VARCHAR2,
      pi_event_time      IN       VARCHAR2,
      po_usage_total     OUT      NUMBER
   )
   IS
   BEGIN
      
	  SELECT /*+ INDEX (riep RATED_IPDR_EVENTS_POS_IDX3  ) */  sum(NVL(amount,0))
	   INTO po_usage_total
	   FROM rated_ipdr_events_pos riep 
	     WHERE ban = pi_ban 
		   AND event_time > TO_DATE (pi_event_time, 'MM/DD/YYYY');
	  
	  
   EXCEPTION
      WHEN OTHERS
        THEN
         raise_application_error (-20160,
                                     'Get Unpaid Data Usage Total. Oracle:(['
                                  || SQLCODE
                                  || '] ['
                                  || SQLERRM
                                  || '])'
                                 );
   END getUnpaidDataUsageTotal;

--------------------------------------------------------------------------------------------------------------------------
END;
-- Data_Usage_3ECMS_pkg
/

SHO err

