CREATE OR REPLACE PACKAGE memo_utility_pkg
AS
-------------------------------------------------------------------------
-- IMPORTANT NOTE !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
-- Please increment the version_no variable value when you make changes to this package 
-- to reflect version changes.
 ------------------------------------------------------------------------
-- description: Package Utility_pkg containing procedures
--    for MEMO's data retrieval from Knowbility database
-- NOTE: Application (Oracle) Error Codes reserved for these package are:
--       -20160 to -20169   ( 10 codes )
--
--
-- Date        Version			Developer           Modifications
-- 10-03-2005  					Marina Kuper           Changed GetMemosByCriteria retrieval
-- 30-03-2005   				Marina Kuper          Added memoId to the memo retrievals
-- 14-07-2005   				Marina Kuper          Changed GetLastMemo
-- 02-11-2005   				Marina Kuper          pi_search_limit added to  GetMemosByCriteria
-- 21-08-2006   				Marina Kuper          Changed GetMemos and GetLastMemo - fixed performance issue
-- 28-08-2006   				Marina Kuper          Changed getmemosbycriteria - fixed performance issue
-- 02-06-2008					R. Fong				  Updated getlastmemo, getmemos and getmemosbycriteria to sort by memo_date in order to
--									  support memo_id sequence reset
-- 18-06-2008					R. Fong				  Updated getlastmemo, getmemosbycriteria to correctly sort by memo_date in order to
--									  support memo_id sequence reset
-- Nov-25,2008  				M.Liao                Rollback to version 7, as KB finished reset memo_id sequence .
-- May-11-2012     				Naresh Annabathula     Added getVersion function for shakedown
-- March 27,2013  3.23.1		Mahan Razaghzadeh   Added new enhanced/tuned function : getmemosbycriteriaEnhanced 
-- April 26,2013  3.23.2		Mahan Razaghzadeh   Updated enhanced/tuned function : getmemosbycriteriaEnhanced 
-- April 26,2013  3.23.3		Mahan Razaghzadeh   Updated enhanced/tuned function : getmemosbycriteriaEnhanced 
-- 2017-12-05	2018.04			Emerson Cho			Removed unused GetMemosByCriteria and removed hint from getlastmemo per DBA (Anar) advice
-------------------------------------------------------------------------
   memonotfound   EXCEPTION;
   PRAGMA EXCEPTION_INIT (memonotfound, -20160);
   -- package version constant
   version_no          CONSTANT VARCHAR2(10)       := '2018.04';


-- Reference to a Cursor
   TYPE refcursor IS REF CURSOR;

--  Variables
   v_sql_select   VARCHAR2 (500);
   v_sql_from     VARCHAR2 (500);
   v_sql_where    VARCHAR2 (750);
   v_row_num      VARCHAR2 (20);

     FUNCTION getVersion RETURN VARCHAR2;
-------------------------------------------------------------------------
-- Get Last Memo  by Ban or by Ban and Subscriber
--------------------------------------------------------------------------
   PROCEDURE getlastmemo (
      pi_ban             IN       NUMBER,
      pi_subscriber_no   IN       VARCHAR2,
      pi_memo_type       IN       VARCHAR2,
      po_memo            OUT      refcursor
   );

--------------------------------------------------------------------------
-- Get Memo's  by Ban
--------------------------------------------------------------------------
   PROCEDURE getmemos (
      pi_ban       IN       NUMBER,
      pi_counter   IN       NUMBER,
      po_memo      OUT      refcursor
   );

-----------------------------------------------------------------------------------------------
-- Get quoted values
----------------------------------------------------------------------------------------------
   FUNCTION getquotedvalues (orig_values VARCHAR2)
      RETURN VARCHAR2;

---------------------------------------------------------------------------
-- Get Memo's  by Ban and search criteria Enhanced Version
--------------------------------------------------------------------------
   PROCEDURE getmemosbycriteriaEnhanced (
      pi_ban             IN       NUMBER,
      pi_subscriber_no   IN       VARCHAR2,
      pi_memo_types      IN       VARCHAR2,
      pi_manual_text     IN       VARCHAR2,
      pi_system_text     IN       VARCHAR2,
      pi_from_date       IN       VARCHAR2,
      pi_to_date         IN       VARCHAR2,
      pi_search_limit    IN       NUMBER,
      po_memo            OUT      refcursor
   );



END memo_utility_pkg;
/

SHO err

-- End of DDL script for MEMO_UTILITY_PKG



-- Start of DDL script for MEMO_UTILITY_PKG

-- Package body MEMO_UTILITY_PKG

CREATE OR REPLACE PACKAGE BODY memo_utility_pkg
AS
--################################################################
-- Procedures/Functions
--################################################################

   ------------------------------------------------------------------------------------------------------------------------------
   
  FUNCTION getVersion RETURN VARCHAR2
   IS
   BEGIN
	   RETURN version_no;
	END getVersion;
	
   FUNCTION getquotedvalues (orig_values VARCHAR2)
      RETURN VARCHAR2
   IS
      memo_values   VARCHAR2 (10000);
      pi_values     VARCHAR2 (10000);
      m_value       VARCHAR2 (30);
      sep_pos       NUMBER (30);
   BEGIN
      pi_values := orig_values;

      LOOP
         sep_pos := INSTR (pi_values, ',');
         EXIT WHEN (sep_pos = 0);
         m_value := SUBSTR (pi_values,
                            1,
                            sep_pos - 1
                           );
         m_value := '''' || m_value || ''' ';
         memo_values := memo_values || ',' || m_value;
         pi_values := SUBSTR (pi_values, sep_pos + 1);
      END LOOP;

      m_value := '''' || pi_values || ''' ';
      memo_values := memo_values || ',' || m_value;
      memo_values := SUBSTR (memo_values, 2);
      RETURN memo_values;
   END;                                                            -- Function

--------------------------------------------------------------------------
--------------------------------------------------------------------------
   PROCEDURE getlastmemo (
      pi_ban             IN       NUMBER,
      pi_subscriber_no   IN       VARCHAR2,
      pi_memo_type       IN       VARCHAR2,
      po_memo            OUT      refcursor
   )
   IS
      v_sql_statement   VARCHAR2 (10000);
   BEGIN
      IF (pi_subscriber_no IS NULL)
      THEN
         IF pi_memo_type = 'CreditCheck'
         THEN
            OPEN po_memo
             FOR
                SELECT /*+ INDEX_DESC ( MEMO MEMO_PK ) */
                       memo_date, memo_type, memo_subscriber,
                       memo_product_type, memo_manual_txt, memo_system_txt,
                       sys_update_date, operator_id, memo_id
                  FROM memo
                 WHERE memo_ban = pi_ban
                   AND memo_type IN ('DPCH', 'REFC')
                   AND ROWNUM <= 1;
         ELSE
            OPEN po_memo
             FOR
                SELECT /*+ INDEX_DESC ( MEMO MEMO_PK ) */ 
                       memo_date, memo_type, memo_subscriber,
                       memo_product_type, memo_manual_txt, memo_system_txt,
                       sys_update_date, operator_id, memo_id
                  FROM memo
                 WHERE memo_ban = pi_ban
                   AND memo_type = pi_memo_type
                   AND ROWNUM <= 1;
         END IF;
      ELSE
         OPEN po_memo
          FOR
             SELECT  /*+ INDEX_DESC ( MEMO MEMO_PK ) */
                    memo_date, memo_type, memo_subscriber, memo_product_type,
                    memo_manual_txt, memo_system_txt, sys_update_date,
                    operator_id, memo_id
               FROM memo
              WHERE memo_ban = pi_ban
                AND memo_subscriber = pi_subscriber_no
                AND memo_type = pi_memo_type
                AND ROWNUM <= 1;
      END IF;
   EXCEPTION
      WHEN NO_DATA_FOUND
      THEN
         IF (po_memo%ISOPEN)
         THEN
            CLOSE po_memo;
         END IF;

         -- return NULL cursor
         OPEN po_memo
          FOR
             SELECT NULL
               FROM DUAL
              WHERE 1 = 0;
      WHEN OTHERS
      THEN
         IF (po_memo%ISOPEN)
         THEN
            CLOSE po_memo;
         END IF;

         raise_application_error (-20160,
                                     'Memo Query Failed. Oracle:(['
                                  || SQLCODE
                                  || '] ['
                                  || SQLERRM
                                  || '])');
   END getlastmemo;

--------------------------------------------------------------------------

   --------------------------------------------------------------------------
   PROCEDURE getmemos (
      pi_ban       IN       NUMBER,
      pi_counter   IN       NUMBER,
      po_memo      OUT      refcursor
   )
   IS
      v_select_count    VARCHAR2 (100);
      v_sql_statement   VARCHAR2 (10000);
   BEGIN
      IF (pi_counter > 0)
      THEN
         OPEN po_memo
          FOR
             SELECT *
               FROM (SELECT /*+ INDEX_DESC ( MEMO MEMO_PK ) */
                            memo_date, memo_type, memo_subscriber,
                            memo_product_type, memo_manual_txt,
                            memo_system_txt, sys_update_date, operator_id,
                            memo_id
                       FROM memo
                      WHERE memo_ban = pi_ban)
              WHERE ROWNUM <= pi_counter;
      ELSE
         OPEN po_memo
          FOR
             SELECT *
               FROM (SELECT /*+ INDEX_DESC ( MEMO MEMO_PK ) */
                            memo_date, memo_type, memo_subscriber,
                            memo_product_type, memo_manual_txt,
                            memo_system_txt, sys_update_date, operator_id,
                            memo_id
                       FROM memo
                      WHERE memo_ban = pi_ban);
      END IF;
   EXCEPTION
      WHEN NO_DATA_FOUND
      THEN
         IF (po_memo%ISOPEN)
         THEN
            CLOSE po_memo;
         END IF;

         -- return NULL cursor
         OPEN po_memo
          FOR
             SELECT NULL
               FROM DUAL
              WHERE 1 = 0;
      WHEN OTHERS
      THEN
         IF (po_memo%ISOPEN)
         THEN
            CLOSE po_memo;
         END IF;

         raise_application_error (-20160,
                                     'Memo Query Failed. Oracle:(['
                                  || SQLCODE
                                  || '] ['
                                  || SQLERRM
                                  || '])');
   END getmemos;

------------------------------------------------------------------------------

PROCEDURE getMemosByCriteriaEnhanced (
      pi_ban                IN       NUMBER,
      pi_subscriber_no      IN       VARCHAR2,
      pi_memo_types         IN       VARCHAR2,
      pi_manual_text        IN       VARCHAR2,
      pi_system_text        IN       VARCHAR2,
      pi_from_date          IN       VARCHAR2,
      pi_to_date            IN       VARCHAR2,
      pi_search_limit       IN       NUMBER,
      po_memo               out      refcursor
   )
   IS
    v_search_limit                 NUMBER (5);
    search_limit                   CONSTANT NUMBER (5) := 1000;
    
   BEGIN

      v_search_limit := pi_search_limit;
      IF (v_search_limit = 0 OR v_search_limit IS NULL)
      THEN
         v_search_limit := search_limit;
      END IF;
 
      ------------------
      open po_memo
      FOR
          select /*+ INDEX_DESC ( MEMO MEMO_PK ) */
                memo_date , 
                memo_type, 
                memo_subscriber, 
                memo_product_type, 
                memo_manual_txt, 
                memo_system_txt, 
                sys_update_date, 
                operator_id, 
                memo_id 
          from memo 
          where memo_ban = pi_ban  
            and (pi_subscriber_no is null
                  OR
                 INSTR(upper(trim(pi_subscriber_no)),upper(trim(memo_subscriber)) ) > 0
                )
            and (pi_memo_types is null
                  OR
                 INSTR(upper(trim(pi_memo_types)),upper(trim(memo_type)) ) > 0
                 )
            and (pi_manual_text is null
                 or
                  (upper(memo_manual_txt) like  '%' || upper(trim(pi_manual_text)) || '%' 
                     or upper(memo_system_txt) like '%' ||upper(trim(pi_manual_text)) || '%'
                  ) 
                 ) 
            and (pi_from_date is null
                 or
                 (memo_date between to_date (pi_from_date,'mm/dd/yyyy hh24:mi:ss')
                     and to_date (pi_to_date,'mm/dd/yyyy hh24:mi:ss')
                 )  
                )
            and rownum <= v_search_limit;   
       
   EXCEPTION
      WHEN NO_DATA_FOUND
      THEN
         IF (po_memo%ISOPEN)
         THEN
            CLOSE po_memo;
         END IF;

         -- return NULL cursor
         OPEN po_memo
          FOR
             SELECT NULL
               FROM DUAL
              WHERE 1 = 0;
      WHEN OTHERS
      THEN
         IF (po_memo%ISOPEN)
         THEN
            CLOSE po_memo;
         END IF;

         raise_application_error (-20160,
                                     'Memo Query Failed. Oracle:(['
                                  || SQLCODE
                                  || '] ['
                                  || SQLERRM
                                  || '])');
   end getmemosbycriteriaenhanced;


-----------------------------------------------------------------------------------------------------------------------------------------
END;
-- MEMO_UTILITY_PKG
/

SHO err

-- End of DDL script for MEMO_UTILITY_PKG

