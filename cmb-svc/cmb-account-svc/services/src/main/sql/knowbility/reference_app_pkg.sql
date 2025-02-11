CREATE OR REPLACE PACKAGE reference_app_pkg
AS
-------------------------------------------------------------------------
-- IMPORTANT NOTE !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
-- Please increment the version_no variable value when you make changes to this package 
-- to reflect version changes.
------------------------------------------------------------------------
-- description: Package REFERENCE_APP_PKG contains procedures and functions
--          for reference data retrieval from Knowbility database.
--
-- Date           Developer            Modifications
-- 06-14-2006   M Kuper                Copied from reference_pkg
-- 11-07-2006   Marina Kuper           Moved BillCycles query from  REFERENCE_PKG
-- 11-08-2006   Marina Kuper           Changed to static SQL
-- 04-04-2007	R. Fong				   Added getnpanxxfortelusmsisdn and
--										 getnpanxxforportedinmsisdn procedures
--  May-11-2012     Naresh Annabathula     Added getVersion function for shakedown
-------------------------------------------------------------------------
   TYPE refcursor IS REF CURSOR;

   invalid_ref_type             EXCEPTION;
   -- search constants
   search_all          CONSTANT VARCHAR2 (1)   := '*';
   -- result constants
   numeric_true        CONSTANT NUMBER (1)     := 0;
   numeric_false       CONSTANT NUMBER (1)     := 1;
   -- error messages
   err_invalid_input   CONSTANT VARCHAR2 (100)
                                   := 'Input parameters are invalid or NULL.';
   err_no_data_found   CONSTANT VARCHAR2 (100) := 'No data found.';
   err_other           CONSTANT VARCHAR2 (100) := 'Other PL/SQL error.';
   -- absolute maximum for the number of accounts to be retrieved
   max_maximorum       CONSTANT NUMBER (4)     := 1000;
-- package version constant
   version_no          CONSTANT VARCHAR2(10)       := '3.19.4';
   
  FUNCTION getVersion RETURN VARCHAR2;

   FUNCTION getreferencedata (
      v_reftype         IN       VARCHAR,
      v_param           IN       VARCHAR,
      c_cursor          OUT      refcursor,
      v_error_message   OUT      VARCHAR2
   )
      RETURN NUMBER;
      
   PROCEDURE getnpanxxfortelusmsisdn (
      pi_npa          IN       VARCHAR2,
      pi_nxx	      IN       VARCHAR2,
      pi_line_range   IN       VARCHAR2,
      po_npa_nxx      OUT      refcursor
   );
   
   PROCEDURE getnpanxxforportedinmsisdn (
      pi_npa          IN       VARCHAR2,
      pi_nxx	      IN       VARCHAR2,
      pi_line_range   IN       VARCHAR2,
      po_npa_nxx      OUT      refcursor
   );
   
END;
/

CREATE OR REPLACE PACKAGE BODY reference_app_pkg
AS
 FUNCTION getVersion RETURN VARCHAR2
   IS
   BEGIN
	   RETURN version_no;
	END getVersion;
	
   FUNCTION getreferencedata (
      v_reftype         IN       VARCHAR,
      v_param           IN       VARCHAR,
      c_cursor          OUT      refcursor,
      v_error_message   OUT      VARCHAR2
   )
      RETURN NUMBER
   IS
      i_result        NUMBER (1);
      v_cursor_text   VARCHAR2 (32767);
   BEGIN
      BEGIN
         IF v_reftype IS NOT NULL AND LENGTH (RTRIM (LTRIM (v_reftype))) > 0
         THEN
            CASE v_reftype
               WHEN 'BillCycles'
               THEN
                  OPEN c_cursor
                   FOR
                      SELECT cycle_code, cycle_desc, cycle_close_day,
                             cycle_population_cd, no_of_bans, cycle_bill_day,
                             cycle_due_day, no_of_subs, allocation_ind,
                             cycle_weight
                        FROM CYCLE
                       WHERE allocation_ind <> 'N';
               WHEN 'KnowbilityOperators'
               THEN
                  OPEN c_cursor
                   FOR
                      SELECT u.user_id, u.user_full_name,
                             wpa.wpasn_work_position_code, wp.wp_supervisor,
                             wal.charge_threshold, wal.adjustment_threshold,
                             func.func_department_code
                        FROM users_ltd u,
                             work_position_assignment wpa,
                             work_position wp,
                             wp_approval_level wal,
                             FUNCTION func
                       WHERE u.user_id = wpa.wpasn_user_id(+)
                         AND wpa.wpasn_work_position_code = wp.wp_work_position_code(+)
                         AND wp.approval_level = wal.approval_level(+)
                         AND (   wpa.wpasn_expiration_date IS NULL
                              OR wpa.wpasn_expiration_date > SYSDATE
                             )
                         AND (   wp.wp_expiration_date IS NULL
                              OR wp.wp_expiration_date > SYSDATE
                             )
                         AND wp.wp_function_code = func.func_function_code(+);
               WHEN 'LogicalDate'
               THEN
                  OPEN c_cursor
                   FOR
                      SELECT logical_date
                        FROM logical_date
                       WHERE logical_date_type = 'O';
               WHEN 'SystemDate'
               THEN
                  OPEN c_cursor
                   FOR
                      SELECT SYSDATE
                        FROM DUAL;
               ELSE
                  RAISE invalid_ref_type;
            END CASE;

            i_result := numeric_true;
         ELSE
            v_error_message := err_invalid_input;
            i_result := numeric_false;
         END IF;
      EXCEPTION
         WHEN invalid_ref_type
         THEN
            v_error_message := 'Invalid reference type: ' || v_reftype;
            i_result := numeric_false;

            IF (c_cursor%ISOPEN)
            THEN
               CLOSE c_cursor;
            END IF;

            OPEN c_cursor
             FOR
                SELECT NULL
                  FROM DUAL
                 WHERE 1 = 0;
         WHEN OTHERS
         THEN
            v_error_message := SQLERRM;
            i_result := numeric_false;

            IF (c_cursor%ISOPEN)
            THEN
               CLOSE c_cursor;
            END IF;

            OPEN c_cursor
             FOR
                SELECT NULL
                  FROM DUAL
                 WHERE 1 = 0;
      END;

      RETURN i_result;
   END getreferencedata;
   
   PROCEDURE getnpanxxfortelusmsisdn (
      pi_npa          IN       VARCHAR2,
      pi_nxx	      IN       VARCHAR2,
      pi_line_range   IN       VARCHAR2,
      po_npa_nxx      OUT      refcursor
   )
   IS
   BEGIN
      OPEN po_npa_nxx
         FOR
            SELECT 	nnn.npanxx
            FROM 	market_npa_nxx_lr mnnl, market_npa_nxx_lr mnnl2, npanxx_nl_ngp nnn
            WHERE 	mnnl.npa = pi_npa
            AND 	mnnl.nxx = pi_nxx
            AND 	mnnl.begin_line_range <= pi_line_range
            AND		mnnl.end_line_range >= pi_line_range
            AND		mnnl.product_type = 'N'
            AND		mnnl2.ngp = mnnl.ngp
	        AND		mnnl2.npa = mnnl.npa
			AND		nnn.npanxx = mnnl2.npa || mnnl2.nxx
			AND		nnn.product_type = 'I'
			AND		nnn.available_ind = 'Y'
			ORDER BY decode(mnnl2.logical_vm_machine, mnnl.logical_vm_machine, 1, 2); 
      EXCEPTION
         WHEN NO_DATA_FOUND
      		THEN
               IF (po_npa_nxx%ISOPEN)
         	   THEN
                  CLOSE po_npa_nxx;
         	   END IF;
               OPEN po_npa_nxx
                  FOR
                     SELECT NULL
                     FROM DUAL
                     WHERE 1 = 0;
         WHEN OTHERS
            THEN
               IF (po_npa_nxx%ISOPEN)
               THEN
                  CLOSE po_npa_nxx;
               END IF;
               raise_application_error
                               (-20160,
                                   'Get NPA-NXX for TELUS Number Range Query Failed. Oracle:(['
                                || SQLCODE
                                || '] ['
                                || SQLERRM
                                || '])');
   END getnpanxxfortelusmsisdn;
   
   -- For now, this procedure is identical to getnpanxxfortelusmsisdn.  This will change when
   -- Amdocs fixes their defects for retrieving fax numbers for Mike port-ins.
   PROCEDURE getnpanxxforportedinmsisdn (
      pi_npa          IN       VARCHAR2,
      pi_nxx	      IN       VARCHAR2,
      pi_line_range   IN       VARCHAR2,
      po_npa_nxx      OUT      refcursor
   )   IS
   BEGIN
      OPEN po_npa_nxx
         FOR
            SELECT 	nnn.npanxx
            FROM 	market_npa_nxx_lr mnnl, market_npa_nxx_lr mnnl2, npanxx_nl_ngp nnn
            WHERE 	mnnl.npa = pi_npa
            AND 	mnnl.nxx = pi_nxx
            AND 	mnnl.begin_line_range <= pi_line_range
            AND		mnnl.end_line_range >= pi_line_range
            AND		mnnl.product_type = 'N'
            AND		mnnl2.ngp = mnnl.ngp
	        AND		mnnl2.npa = mnnl.npa
			AND		nnn.npanxx = mnnl2.npa || mnnl2.nxx
			AND		nnn.product_type = 'I'
			AND		nnn.available_ind = 'Y'
			ORDER BY decode(mnnl2.logical_vm_machine, mnnl.logical_vm_machine, 1, 2); 
      EXCEPTION
         WHEN NO_DATA_FOUND
      		THEN
               IF (po_npa_nxx%ISOPEN)
         	   THEN
                  CLOSE po_npa_nxx;
         	   END IF;
               OPEN po_npa_nxx
                  FOR
                     SELECT NULL
                     FROM DUAL
                     WHERE 1 = 0;
         WHEN OTHERS
            THEN
               IF (po_npa_nxx%ISOPEN)
               THEN
                  CLOSE po_npa_nxx;
               END IF;
               raise_application_error
                               (-20160,
                                   'Get NPA-NXX for Ported Number Range Query Failed. Oracle:(['
                                || SQLCODE
                                || '] ['
                                || SQLERRM
                                || '])');
   END getnpanxxforportedinmsisdn;
   
END;
/

