CREATE OR REPLACE PACKAGE rule_utility_pkg 
AS
-------------------------------------------------------------------------
-- IMPORTANT NOTE !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
-- Please increment the version_no variable value when you make changes to this package 
-- to reflect version changes.
------------------------------------------------------------------------
-- description: PL/Sql utility package containing procedures and functions
--    for retrieving business rules configured in the EAS database.
--
-- Date           Developer           Modifications
-- 07-29-2009     Francis Lau         New to query the Rule tables
--  May-11-2012     Naresh Annabathula     Added getVersion function for shakedown
---------------------------------------------------------------------------

   TYPE refcursor IS REF CURSOR;

-- result constants
   numeric_true        CONSTANT NUMBER (1)     := 0.0;
   numeric_false       CONSTANT NUMBER (1)     := 1.0;
-- error messages
   err_invalid_input   CONSTANT VARCHAR2 (100)
                                   := 'Input parameters are invalid or NULL.';
   err_no_data_found   CONSTANT VARCHAR2 (100) := 'No data found.';
   err_other           CONSTANT VARCHAR2 (100) := 'Other PL/SQL error.';
-- package version constant
   version_no          CONSTANT VARCHAR2(10)       := '3.19.4';
   
 FUNCTION getVersion RETURN VARCHAR2;
-------------------------------------------------------------------------
-- Procedure to get a reference cursor to the rules configuration - 
-- All the rules that are effective, not expired and with their conditions and result.
--------------------------------------------------------------------------
   PROCEDURE getRules(
       po_result     OUT REFCURSOR
   );
END;
/

SHO err

CREATE OR REPLACE PACKAGE BODY rule_utility_pkg 
AS
FUNCTION getVersion RETURN VARCHAR2
   IS
   BEGIN
	   RETURN version_no;
	END getVersion;
--------------------------------------------------------------------------------------------------------------------
   PROCEDURE getRules(
       po_result     OUT REFCURSOR)
   IS
   BEGIN
       OPEN po_result
           FOR SELECT MR.MESSAGE_RULE_ID, MR.MESSAGE_RULE_TYPE_ID, MR.MESSAGE_RESULT_ID, MR.MESSAGE_ROLE_TYPE_ID, MR.MESSAGE_RULE_TXT,
           MR.RULE_RESULT_TYPE_ID, RRT.RULE_RESULT_TYPE_NM, RRT.RULE_RESULT_TYPE_TXT,
           MRT.MESSAGE_RULE_CLASS_ID,
           RCV.RULE_CONDITION_ID, RCV.RULE_COND_VALUE_STR, RCV.RULE_COND_VALUE_FROM_QTY, RCV.RULE_COND_VALUE_TO_QTY, 
           RCV.RULE_COND_VALUE_FROM_DT, RCV.RULE_COND_VALUE_TO_DT,
           RC.RULE_CONDITION_TYPE_ID, RC.RULE_CONDITION_TXT
           FROM MESSAGE_RULE MR,
           MESSAGE_RULE_TYPE MRT,
           MESSAGE_RULE_CONDITION_ASSN MRCA,
           RULE_CONDITION_VALUE RCV,
           RULE_CONDITION RC,
           RULE_RESULT_TYPE RRT
           WHERE MR.EFFECTIVE_START_TS <= SYSDATE
           AND MR.EFFECTIVE_END_TS > SYSDATE
           AND MRT.MESSAGE_RULE_TYPE_ID = MR.MESSAGE_RULE_TYPE_ID
           AND MRCA.MESSAGE_RULE_ID = MR.MESSAGE_RULE_ID
           AND MRCA.EFFECTIVE_START_TS <= SYSDATE
           AND MRCA.EFFECTIVE_END_TS > SYSDATE
           AND RCV.RULE_CONDITION_VALUE_ID = MRCA.RULE_CONDITION_VALUE_ID
           AND RCV.EFFECTIVE_START_TS <= SYSDATE
           AND RCV.EFFECTIVE_END_TS > SYSDATE
           AND RC.RULE_CONDITION_ID = RCV.RULE_CONDITION_ID
           AND MR.RULE_RESULT_TYPE_ID = RRT.RULE_RESULT_TYPE_ID
           ORDER BY MR.MESSAGE_RULE_ID, RCV.RULE_CONDITION_ID;

   EXCEPTION
   WHEN NO_DATA_FOUND
   THEN
       IF po_result%ISOPEN
       THEN
           CLOSE po_result;
       END IF;

   WHEN OTHERS
   THEN
       IF po_result%ISOPEN
       THEN
           CLOSE po_result;
       END IF;
       RAISE;
   END getRules;

END;
/

SHO err

grant execute on rule_utility_pkg to actv_app;

