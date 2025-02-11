CREATE OR REPLACE PACKAGE prepaid_transaction_pkg
AS
-------------------------------------------------------------------------
-- IMPORTANT NOTE !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
-- Please increment the version_no variable value when you make changes to this package 
-- to reflect version changes.
 ------------------------------------------------------------------------
-- description: Package prepaid_transaction_pkg containing procedures
--    for data retrieval from ECPCS database
--
-- Date            Developer           Modifications
-- Aug  7, 2007    Tsz Chung Tong	     creation of this package
-- Sep 21, 2007    Tsz Chung Tong      updated getPrepaidEventHistory method
-- Sep 25, 2007    Tsz Chung Tong      modified getPrepaidEventHistory to retrieve gmt_offset
-- Jan 09, 2008    Michael Liao        modified getPrepaidEventHistory to retrieve pre_event_status, post_event_status, pre_event_amount, post_event_amount
--                                       modified getPrepaidCallHistory to retrieve reason_type_id, reason_id, discount_id
-- Jan 15, 2008    Tsz Chung Tong      modified getPrepaidEventHistory to retrieve unit_type_id
-- Jan 15, 2008    Michael Liao        adjust column name according to Table pre_event_status->pre_event_status_ind, post_event_status->post_event_status_ind
--                                     , pre_event_amount->pre_event_amt, post_event_amount->post_event_amt
-- Jun 3, 2008	   Belinda Liang       updated getPrepaidCallHistory method to retrieve ld_call_cost,local_call_cost
-- Sep 16, 2008    Mujeeb Waraich      updated getPrepaidCallHistory method to retrieve roaming_call_cost, network_serving_area_switch_id, network_serving_area_route_id
-- Jun 24, 2009    Mujeeb Waraich      updated getPrepaidCallHistory method to retrieve called_market_cd and calling_market_cd
--  May-30-2012     Naresh Annbathula     Added getVersion function for shakedown
-------------------------------------------------------------------------

-- Reference to a Cursor
   TYPE refcursor IS REF CURSOR;

-- result constants
   numeric_true    CONSTANT NUMBER (1) := 1;
   numeric_false   CONSTANT NUMBER (1) := 0;
   -- package version constant
   version_no   CONSTANT VARCHAR2(10) := '3.19.4';

--------------------------------------------------------------------------
   PROCEDURE getPrepaidEventHistory (
      pi_min_msisdn      IN       VARCHAR2,
      pi_from_date       IN       VARCHAR2,
      pi_to_date         IN       VARCHAR2,
      po_event_history   OUT      refcursor
   );
   
   PROCEDURE getPrepaidEventHistory (
      pi_min_msisdn      IN       VARCHAR2,
      pi_from_date       IN       VARCHAR2,
      pi_to_date         IN       VARCHAR2,
      pi_event_types     IN       T_EVENT_TYPE_ARRAY,
      po_event_history   OUT      refcursor
   );
   
   PROCEDURE getPrepaidCallHistory (
      pi_min_msisdn      IN       VARCHAR2,
      pi_from_date       IN       VARCHAR2,
      pi_to_date         IN       VARCHAR2,
      po_call_history   OUT      refcursor
   );
   
	FUNCTION getVersion RETURN VARCHAR2; 
--------------------------------------------------------------------------
END;
/

SHO err

CREATE OR REPLACE PACKAGE BODY prepaid_transaction_pkg
AS
--################################################################
-- Procedures/Functions
--################################################################
--------------------------------------------------------------------------
------------------------------------------------------------------
-- getVersion function
------------------------------------------------------------------
   
   FUNCTION getVersion RETURN VARCHAR2
   IS
   BEGIN
	   RETURN version_no;
	END getVersion;
	
   PROCEDURE getPrepaidEventHistory (
      pi_min_msisdn      IN       VARCHAR2,
      pi_from_date       IN       VARCHAR2,
      pi_to_date         IN       VARCHAR2,
      po_event_history   OUT      refcursor
   )
   IS
   BEGIN
------------------------------------------------------------------
-- Create reference to a cursor  using SQL statement
------------------------------------------------------------------
		getPrepaidEventHistory (pi_min_msisdn, pi_from_date, pi_to_date, NULL, po_event_history);
   END getPrepaidEventHistory;
   
   
   PROCEDURE getPrepaidEventHistory (
      pi_min_msisdn      IN       VARCHAR2,
      pi_from_date       IN       VARCHAR2,
      pi_to_date         IN       VARCHAR2,
      pi_event_types     IN       T_EVENT_TYPE_ARRAY,
      po_event_history   OUT      refcursor
   )
   IS
   BEGIN
------------------------------------------------------------------
-- Create reference to a cursor  using SQL statement
------------------------------------------------------------------
		IF pi_event_types IS NOT NULL AND pi_event_types.COUNT > 0 THEN
			OPEN po_event_history
			FOR
				SELECT 	  start_dt, event_type_id, debit_credit_cd, 
									applied_amt, card_id, credit_card_no, 
									bscs_reference_cd, wps_start_bal, wps_end_bal, 
									userid, source_cd, transaction_reference_cd, 
									related_trans_reference_cd, reason_id, accum_outstanding_charge_amt,
									IPDR_event_ts, confiscated_amt, gmt_offset, 
									pre_event_status_ind, post_event_status_ind, pre_event_amt, post_event_amt, unit_type_id 
									FROM	  event_detail
									WHERE	  min_msisdn =  pi_min_msisdn
									AND 	  trunc(start_dt) >= to_date(pi_from_date,'mm/dd/yyyy')
									AND     trunc(start_dt) <= to_date(pi_to_date,'mm/dd/yyyy')
									AND     event_type_id IN (SELECT * FROM TABLE (CAST (pi_event_types AS t_event_type_array)))
									order by 1 desc;
    ELSE
    	OPEN 	po_event_history
    	FOR
    		SELECT 	  start_dt, event_type_id, debit_credit_cd, 
									applied_amt, card_id, credit_card_no, 
									bscs_reference_cd, wps_start_bal, wps_end_bal, 
									userid, source_cd, transaction_reference_cd, 
									related_trans_reference_cd, reason_id, accum_outstanding_charge_amt,
									IPDR_event_ts, confiscated_amt, gmt_offset, 
									pre_event_status_ind, post_event_status_ind, pre_event_amt, post_event_amt, unit_type_id 
									FROM	  event_detail
									WHERE	  min_msisdn =  pi_min_msisdn
									AND 	  trunc(start_dt) >= to_date(pi_from_date,'mm/dd/yyyy')
									AND     trunc(start_dt) <= to_date(pi_to_date,'mm/dd/yyyy')
									order by 1 desc;
    END IF;
   EXCEPTION
      WHEN NO_DATA_FOUND
      THEN
         IF (po_event_history%ISOPEN)
         THEN
            CLOSE po_event_history;
         END IF;

         -- return NULL cursor
         OPEN po_event_history
          FOR
             SELECT NULL
               FROM DUAL
              WHERE 1 = 0;
      WHEN OTHERS
      THEN
         IF (po_event_history%ISOPEN)
         THEN
            CLOSE po_event_history;
         END IF;

         raise_application_error (-20160,
                                     'Get Prepaid Event History. Oracle:(['
                                  || SQLCODE
                                  || '] ['
                                  || SQLERRM
                                  || '])'
                                 );
   END getPrepaidEventHistory;

   PROCEDURE getPrepaidCallHistory (
      pi_min_msisdn      IN       VARCHAR2,
      pi_from_date       IN       VARCHAR2,
      pi_to_date         IN       VARCHAR2,
      po_call_history   OUT      refcursor
   )
   IS
   BEGIN
------------------------------------------------------------------
-- Create reference to a cursor  using SQL statement
------------------------------------------------------------------
		OPEN po_call_history
			FOR
				SELECT 	  start_dt, end_dt, local_ld_call_type, 
									intl_domestic_call_ind, origin_direction_cd, charge_duration, 
									calling_msisdn, called_msisdn, charged_amt, 
									wps_start_balance, wps_end_balance, call_vm_ind, wps_rate_id, 
                    				serving_sid,
                    				reason_type_id,reason_id,discount_id,ld_call_cost,local_call_cost,roaming_call_cost, 
                    				network_serving_area_switch_id, network_serving_area_route_id,called_market_cd,calling_market_cd 
                    		FROM	  call_detail 
                    		WHERE	  ((calling_msisdn =  pi_min_msisdn and origin_direction_cd = 'O') 
                    		OR          (called_msisdn = pi_min_msisdn and origin_direction_cd = 'I')) 
                    		AND 	  ( trunc(start_dt) >= to_date(pi_from_date,'mm/dd/yyyy') 
                    		AND          trunc(start_dt) <= to_date(pi_to_date,'mm/dd/yyyy') 
                    		OR 	   trunc(end_dt) >= to_date(pi_from_date,'mm/dd/yyyy') 
                    		AND          trunc(end_dt) <= to_date(pi_to_date,'mm/dd/yyyy'));
   EXCEPTION
      WHEN NO_DATA_FOUND
      THEN
         IF (po_call_history%ISOPEN)
         THEN
            CLOSE po_call_history;
         END IF;

         -- return NULL cursor
         OPEN po_call_history
          FOR
             SELECT NULL
               FROM DUAL
              WHERE 1 = 0;
      WHEN OTHERS
      THEN
         IF (po_call_history%ISOPEN)
         THEN
            CLOSE po_call_history;
         END IF;

         raise_application_error (-20160,
                                     'Get Prepaid Event History. Oracle:(['
                                  || SQLCODE
                                  || '] ['
                                  || SQLERRM
                                  || '])'
                                 );
   END getPrepaidCallHistory;
--------------------------------------------------------------------------------------------------------------------------
END;
-- prepaid_transaction_pkg
/

SHO err

