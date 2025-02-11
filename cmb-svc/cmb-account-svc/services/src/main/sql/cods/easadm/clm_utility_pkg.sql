CREATE OR REPLACE PACKAGE CLM_UTILITY_PKG
AS
------------------------------------------------------------------------------------------------
-- description: Package clm_utility_pkg containing procedures
--    for clm data retrieval from CRDB database
-- NOTE: Application (Oracle) Error Codes reserved for these package are:
--       -20160 to -20169   ( 10 codes )
--
------------------------------------------------------------------------------------------------
-- Date        Developer           		Modifications
------------------------------------------------------------------------------------------------
-- 02-08-2005  Roman Tov				created and added function GetCLMSummary--  May-09-2012     Naresh Annbathula     Added getVersion function for shakedown
------------------------------------------------------------------------------------------------
   bannotfound                  EXCEPTION;
   PRAGMA EXCEPTION_INIT (bannotfound, -20101);

   TYPE refcursor IS REF CURSOR;

-- search constants
   search_all          CONSTANT VARCHAR2 (1)   := '*';   -- version constants   version_no          CONSTANT VARCHAR2(10)       := '3.19.4';
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

   FUNCTION GetCLMSummary (
      i_ban             IN       NUMBER,
      c_result          OUT      refcursor,
      v_err_msg         OUT      VARCHAR2
   )
	RETURN NUMBER;	  FUNCTION getVersion RETURN VARCHAR2;

END;
/

CREATE OR REPLACE PACKAGE BODY CLM_UTILITY_PKG
AS	FUNCTION getVersion RETURN VARCHAR2   IS   BEGIN	   RETURN version_no;	END getVersion;	
   FUNCTION GetCLMSummary (
      i_ban             IN       NUMBER,
      c_result          OUT      refcursor,
      v_err_msg         OUT      VARCHAR2
   )
   RETURN NUMBER IS
   		i_result            NUMBER (1);
		v_Cursor_Text       VARCHAR2(32767);
   BEGIN
      BEGIN
        v_Cursor_Text := 'SELECT  UNPAID_BILL_CHRG_AMT,UNBILLD_AIRTM_CHRG_AMT,UNBILLD_DATA_CHRG_AMT,
						  PEND_CHRG_AMT,PEND_ADJMT_AMT,REQ_MIN_PYMT_AMT
                          from CREDIT_LIMIT_BALANCE
				          where ban = ' || i_ban;

		open c_result for v_Cursor_Text;
		i_result := numeric_true;	

		exception
			when no_data_found then
				open c_result for select null from dual;
			
			when others then
				raise_application_error(-20161, 
							'ERROR: clm_utility_pkg.getclmsummary(). SQL [' ||
															SQLCODE ||
															'] Error [' ||
															SQLERRM ||
															']',
															true);
			i_result := numeric_false;
        END;
	   RETURN i_result;
    END GetCLMSummary;
END;
/

