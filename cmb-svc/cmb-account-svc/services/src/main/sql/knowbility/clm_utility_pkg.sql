/* Formatted on 2006/11/06 11:28 (Formatter Plus v4.8.5) */
------------------------------------------------------------------------------------------------
-- description: Package clm_utility_pkg contains procedures
--    for usage
--
------------------------------------------------------------------------------------------------
-- Date        Developer           		Modifications
------------------------------------------------------------------------------------------------
-- 11-06-2006  Roman Tov			added procedures
-- 11-06-2006  Kuhan Paramsothy			formatted code
------------------------------------------------------------------------------------------------
-- IMPORTANT NOTE !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
-- Please increment the version_no variable value when you make changes to this package 
-- to reflect version changes.
------------------------------------------------------------------------------------------------
CREATE OR REPLACE PACKAGE clm_utility_pkg
AS
   unbilledcycleinfonotfound   EXCEPTION;
   PRAGMA EXCEPTION_INIT (unbilledcycleinfonotfound, -20150);
   version_no   CONSTANT VARCHAR2(10) := '3.19.4';

   TYPE refcursor IS REF CURSOR;

   FUNCTION getunbilauccmmtablename (pi_ban NUMBER)
      RETURN VARCHAR2;
 	FUNCTION getVersion RETURN VARCHAR2;
   PROCEDURE getunbilledusage (
      pi_ban              IN       NUMBER,
      po_unbilled_usage   OUT      refcursor
   );
END;
/

CREATE OR REPLACE PACKAGE BODY clm_utility_pkg
AS
   FUNCTION getunbilauccmmtablename (pi_ban NUMBER)
      RETURN VARCHAR2
   IS
      v_table_name   VARCHAR2 (20);
   BEGIN
      v_table_name := 'AUCCMM';

      SELECT au_issue_code
        INTO v_table_name
        FROM cycle_control c, billing_account ba, bill b
       WHERE ba.ban = pi_ban
         AND (ba.bill_cycle = c.cycle_code)
         AND (b.ban = ba.ban)
         AND (b.bill_seq_no = ba.bl_cur_bill_seq_no)
         AND (b.prd_cvrg_strt_date BETWEEN c.cycle_start_date
                                       AND c.cycle_close_date
             );

      RETURN v_table_name;
   EXCEPTION
      WHEN NO_DATA_FOUND
      THEN
         RAISE unbilledcycleinfonotfound;
   END;
   
 	FUNCTION getVersion RETURN VARCHAR2
   IS
   BEGIN
	   RETURN version_no;
	END getVersion;
	
   PROCEDURE getunbilledusage (
      pi_ban              IN       NUMBER,
      po_unbilled_usage   OUT      refcursor
   )
   IS
      v_table_name   VARCHAR2 (20);
      v_sql_select   VARCHAR2 (10000);
   BEGIN
      v_table_name := getunbilauccmmtablename (pi_ban);
      v_sql_select :=
            'SELECT sum(NVL(chrg_amt_prd_1,0)+NVL(chrg_amt_prd_2,0)'
         || '+NVL(chrg_amt_prd_3,0)+NVL(chrg_amt_prd_4,0)+NVL(chrg_amt_prd_5,0)'
         || '+NVL(chrg_amt_prd_6,0)+NVL(CHRG_AMT,0)) FROM '
         || v_table_name
         || ' WHERE ban='
         || pi_ban
         || ' and record_type in (1,2,4,5)';

      OPEN po_unbilled_usage FOR v_sql_select;
   EXCEPTION
      WHEN NO_DATA_FOUND
      THEN
         IF (po_unbilled_usage%ISOPEN)
         THEN
            CLOSE po_unbilled_usage;
         END IF;

         OPEN po_unbilled_usage FOR
            SELECT NULL
              FROM DUAL
             WHERE 1 = 0;
      WHEN OTHERS
      THEN
         IF (po_unbilled_usage%ISOPEN)
         THEN
            CLOSE po_unbilled_usage;
         END IF;

         raise_application_error (-20152,
                                     'Unbilled Usage Query Failed. Oracle:(['
                                  || SQLCODE
                                  || '] ['
                                  || SQLERRM
                                  || '])'
                                  || ' [Ban='
                                  || pi_ban
                                  || ',UsageTable='
                                  || v_table_name
                                  || ']'
                                 );
   END getunbilledusage;
END;
/