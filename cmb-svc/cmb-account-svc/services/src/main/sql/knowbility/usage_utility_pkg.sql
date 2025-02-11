
-- Start of DDL script for USAGE_UTILITY_PKG
-- Generated 4-Nov-04  4:33:16 pm
-- from DKB9-AMDOCS_EXTD3:1

-- Package USAGE_UTILITY_PKG

CREATE OR REPLACE
package Usage_Utility_pkg as
-------------------------------------------------------------------------
-- IMPORTANT NOTE !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
-- Please increment the version_no variable value when you make changes to this package 
-- to reflect version changes.
 ------------------------------------------------------------------------
-- description: Package Utility_pkg containing procedures
--		for USAGE data retrieval from Knowbility database
-- NOTE: Application (Oracle) Error Codes reserved for these package are:
--       -20150 to -20159   ( 10 codes )
--
--
-- Date	   		Developer     	  	  Modifications
-- 07-16-2003		Carlos Manjarres	  Initial Version
-- 07-28-2003		Carlos Manjarres	  QA Version
-- 08-19-2003		Carlos Manjarres	  Fixed bug in private function getAUCCMMTableName()
-- 08-20-2003       Carlos Manjarres      Return Home Province at the subscriber level.
-- 08-21-2003       Carlos Manjarres      Improved exception handling to avoid unnecessary system errors..
-- 11-05-2003		carlos Manjarres	  Added Filter to select only Inclusive Minutes Combined.
--   						   			  Added ACTION_DIRECTION_CD in inner query for future use.
--  01-16-2004		Carlos Manjarres	  Modified as per Q1-2004 design doc
--						   				  New parameter featureCode and new group by direction_cd
--										  Exposed function getAUCCMMTableName for testing and support
--  02-05-2004		Carlos Manjarres	  Add grouping by PRICE_PLAN_CODE
--  02-10-2004		Carlos Manjarres	  IM_ALLOWED and REMAINING are now consistent ==> Chargeable is correct
--  02-11-2004		Carlos Manjarres	  Renamed IM_ALLOWED_COMBD to IM_ALLOWED (bug introduced with version 1.0.4 Feb-10
--  03-26-2004		Carlos Manjarres	  Added sorting by ascending rate_plan effective date
--                                        and added SOC_EFFECTIVE_DATE for future use
--  09-03-2004		Carlos Manjarres	  Fixed bug retrieving multiple rows of province code for a subscriber
--  09-22-2004		Carlos Manjarres	  Fixed bug poiting to the previous month when bill is closing
--  11-04-2004		Carlos Manjarres	  Added indicator for shareable price plans. 'X' means isShareablePlan
--  11-04-2006      Marina Kuper          Added logic to pick up only records from the current bill cycle
--  Nov-08-2011     Tsz Chung Tong        Fixed GetUnbilledUsageSummary for missing single quote on subscriberId
--  May-09-2012     Naresh Annbathula     Added getVersion function for shakedown
-------------------------------------------------------------------------

UnbilledCycleInfoNotFound	    Exception;
PRAGMA							EXCEPTION_INIT(UnbilledCycleInfoNotFound, -20150);

UnbilledProvinceInfoNotFound	Exception;
PRAGMA							EXCEPTION_INIT(UnbilledProvinceInfoNotFound, -20151);

UnbilledUsageSummaryNotFound	Exception;
PRAGMA							EXCEPTION_INIT(UnbilledUsageSummaryNotFound, -20152);
version_no   CONSTANT VARCHAR2(10) := '3.19.4';

-- Reference to a Cursor
TYPE RefCursor Is Ref Cursor;

-------------------------------------------------------------------------
-- Get Unbilled Usage Summary  by Ban or by Subscriber
-- Records will be grouped by subscriber_no, price_plan_code, feature_code and direction
--------------------------------------------------------------------------
-- Filter shareable price plans is turned on
Procedure GetUnbilledUsageSummary(
		  		  pi_feature_code			IN	varchar2
		  		 ,pi_ban				    IN	number
				 ,pi_subscriber_no			IN	varchar2
				 ,po_unbilled_usage			OUT RefCursor) ;

------------------------------------------------------------------
-- Determine Home Province by Subscriber
------------------------------------------------------------------
Function getSubHomeProvince( pi_subscriber VARCHAR2 ) Return  VARCHAR2;
FUNCTION getVersion RETURN VARCHAR2; 


------------------------------------------------------------------
-- Determine Unbilled Accumulated Usage Table Name by BAN
------------------------------------------------------------------
Function getUnbilAUCCMMTableName( pi_ban NUMBER ) Return  VARCHAR2;

PROCEDURE println (v_line IN VARCHAR2);


End Usage_Utility_pkg;
/

-- End of DDL script for USAGE_UTILITY_PKG


-- Start of DDL script for USAGE_UTILITY_PKG
-- Generated 4-Nov-04  4:35:22 pm
-- from DKB9-AMDOCS_EXTD3:1

-- Package body USAGE_UTILITY_PKG

CREATE OR REPLACE
Package Body Usage_Utility_pkg As

--################################################################
-- Private Constanats
--################################################################

QUERY_LEVEL_SUB CONSTANT CHAR(3) := 'Sub';
QUERY_LEVEL_BAN CONSTANT CHAR(3) := 'Ban';



--################################################################
-- Private Procedures/Functions
--################################################################

------------------------------------------------------------------
-- getVersion function
------------------------------------------------------------------
   
   FUNCTION getVersion RETURN VARCHAR2
   IS
   BEGIN
	   RETURN version_no;
	END getVersion;
	
Function getBanHomeProvince( pi_ban NUMBER ) Return  VARCHAR2 Is
------------------------------------------------------------------
-- Determine Home Province by BAN
------------------------------------------------------------------

v_province			CHAR(2);

Begin
     v_province := null;

     select
       home_province
       into v_province
       from  billing_account
       where ban = pi_ban;

     If ( v_province is null ) Then
             raise UnbilledProvinceInfoNotFound;
     End If;

	 Return v_province;

     Exception
         when NO_DATA_FOUND then
             raise UnbilledProvinceInfoNotFound;

End; -- Function



--################################################################
-- Public Procedures/Functions
--################################################################


Function getUnbilAUCCMMTableName( pi_ban NUMBER ) Return  VARCHAR2 Is
------------------------------------------------------------------
-- Determine Unbilled Accumulated Usage Table Name by BAN
------------------------------------------------------------------
-- use system time and check bill cycle start/stop dates in CYCLE_CONTROL table

v_table_name			VARCHAR2(20);

Begin
     v_table_name := 'AUCCMM';

	 select AU_ISSUE_CODE
       into v_table_name
	   from cycle_control c,
	   		billing_account ba,
			logical_date ld
       where ban = pi_ban
	   		 and (ba.bill_cycle = c.cycle_code)
			 --find unbilled usage:
			 and (ld.logical_date_type = 'O' )
			 and (ld.logical_date between cycle_start_date and cycle_close_date );

	 Return v_table_name;

     Exception
         when NO_DATA_FOUND then
             raise UnbilledCycleInfoNotFound;

End; -- Function

Function getSubHomeProvince( pi_subscriber VARCHAR2 ) Return  VARCHAR2 Is
------------------------------------------------------------------
-- Determine Home Province by Subscriber
------------------------------------------------------------------

v_province			CHAR(2);

Begin
     v_province := null;

	 -- only the first recent province code will be returned.
	 select province
		 	into v_province
	 from (
			 select  m.province
		             from     npa_nxx_lr nnl,
		                      market m
		             where nnl.npa = substr(pi_subscriber,1,3)
		             and nnl.nxx = substr(pi_subscriber,4,3)
		             and nnl.begin_line_range  <= substr(pi_subscriber,7,4)
		             and nnl.end_line_range  >= substr(pi_subscriber,7,4)
		             and trunc(effective_date) <trunc(sysdate-1)
		             and m.market_code=nnl.sub_market_code
		             order by nnl.effective_date desc
	      )
	  where rownum <= 1	 ;

--     If ( v_province is null ) Then
--             raise UnbilledProvinceInfoNotFound;
--     End If;

	 Return v_province;

     Exception
         when NO_DATA_FOUND then
             Return v_province;

End; -- Function


--------------------------------------------------------------------------
-- Filter shareable price plans is turned on
Procedure GetUnbilledUsageSummary(
		  		  pi_feature_code			IN	varchar2
		  		 ,pi_ban				    IN	number
				 ,pi_subscriber_no			IN	varchar2
				 ,po_unbilled_usage			OUT RefCursor)
Is

	-- Local Variables
	 v_table_name			VARCHAR2(20);

	 v_sql_select			VARCHAR2(10000);
	 v_sql_from				VARCHAR2(50);
	 v_sql_where			VARCHAR2(750);
	 v_sql_criteria			VARCHAR2(250);
	 v_sql_group			VARCHAR2(250);
	 v_sql_tail				VARCHAR2(250);    -- to close inner/outter statements
	 v_sql_order2			VARCHAR2(250);    -- outter statement

	 v_query_level			CHAR(3);		  -- { Sub Ban }

	 v_record_type			number;			  -- in the future maybe a parameter

Begin

	 v_record_type := 1;	-- AIRTIME

	 -- determine type of call.   Could be Sub-level or BAN-level
	 If ( pi_subscriber_no is NOT NULL ) Then
	 	  v_query_level := QUERY_LEVEL_SUB;
	 Else
	 	  v_query_level := QUERY_LEVEL_BAN;
	 End If;

	 ------------------------------------------------------------------
	 -- find out usage table name
	 ------------------------------------------------------------------
	 v_table_name := getUnbilAUCCMMTableName( pi_ban );

	 ------------------------------------------------------------------
	 -- Prepare Dynamic SQL statement to retrieve usage summary
	 ------------------------------------------------------------------
	 -- The dynamic component is the accumulated usage table name


     -- Procedure to retrieve relevant information for the Usage Summary scenarios
     --
     -- 	Carlos Manjarres - Self-sercvice Phase III
     -- ==========================================================================
     --	Usage Summary
     --				Airtime ( STD )
	 --
     -- ********************************
     --		SUBSCRIBER LEVEL
     -- ********************************
     -- ==========================================================================
     --
     -- 	Last update:  Nov-04-2004

     /*
     -- Post-paid PCS and MIKE

        Summarize only telephony usage records for Post-Paid PCS

        1. Summarize grouping by
		   record_type , airtime_feature_cd , soc_effective_date, price_pln_code and direction

        2. Calculate: Remaining and Chargable

        Note:   ALL periods  are returned to caller.

     EXCEPTIONS:

     E1.-- Sometimes Feature_codes is not available in feature reference table:  (FUTURE USE)
     --
      Airtime_feature_cd
     	1 MCFTR	:dummy feature to accumulate account pooling contribution. only in record_type=7
      Secondary_feature_cd
     	2 MLTFTR	:default code for free minutes accumulation. only in record_type=6
     	3 NOFTR 	:default code for airtime or account poling. only if record_type  =1 or 7
     	4 Null

      Impact:
       		a. Since MCFTR is a dummy MIKE faeture to track account pooling contribution, it will not
     		   be displayed as another feature, but will be used to display the Account Pooling Remaining/Charges.
     		b. Since Secondary feature code is not displayed, the issue can be ignored.

     E2-- Sometimes Unit of measure is null
       Impact:
     		a. Default unit of measure is 'm' (minute) and only if the feature code is MMMAIL
     		   the default will be 's''
     		   note: I used lower case to internally distinguish real and defaulted values.
     		   At the time of retrieving the description of the unit of measure,
     		   ----> !! please use UPPER(unit_of_measure)
     */


     -- ======= BEGIN QUERY 2 =====================================
     -- Calculate: Remaining, Chargable
	 v_sql_select :=
'  select ' ||
'  BAN, ' ||
'  SUBSCRIBER_NO, ' ||
'  RECORD_TYPE, ' ||
'  AIRTIME_FEATURE_CD, ' ||
 --SECONDARY_FEATURE_CD,
'  PRICE_PLAN_CODE, ' ||
'  ACTION_DIRECTION_CD, ' ||
'  SOC_EFFECTIVE_DATE, ' ||
 ---------------
'  IM_ALLOCATION_IND, ' ||
 ---------------
'  TOTAL_CALLS , ' ||
'  NUM_OF_CALLS_1 , ' ||
'  NUM_OF_CALLS_2 , ' ||
'  NUM_OF_CALLS_3 , ' ||
'  NUM_OF_CALLS_4 , ' ||
'  NUM_OF_CALLS_5 , ' ||
'  NUM_OF_CALLS_6 , ' ||
'  NUM_FREE_AIR_CALLS , ' ||
'  NUM_SPECIAL_CALLS , ' ||
 ---------------
'  TOTAL_USED , ' ||
'  TOTAL_USED_1 , ' ||
'  TOTAL_USED_2 , ' ||
'  TOTAL_USED_3 , ' ||
'  TOTAL_USED_4 , ' ||
'  TOTAL_USED_5 , ' ||
'  TOTAL_USED_6 , ' ||
 ---------------
--'  IM_ALLOWED , ' ||
'  IM_ALLOWED_COMBD IM_ALLOWED , ' ||
'  IM_ALLOWED_1 , ' ||
'  IM_ALLOWED_2 , ' ||
'  IM_ALLOWED_3 , ' ||
'  IM_ALLOWED_4 , ' ||
'  IM_ALLOWED_5 , ' ||
'  IM_ALLOWED_6 , ' ||
 ---------------
'  IM_USED , ' ||
'  IM_USED_1 , ' ||
'  IM_USED_2 , ' ||
'  IM_USED_3 , ' ||
'  IM_USED_4 , ' ||
'  IM_USED_5 , ' ||
'  IM_USED_6 , ' ||
 ---------------
'  FREE , ' ||
'  FREE_1 , ' ||
'  FREE_2 , ' ||
'  FREE_3 , ' ||
'  FREE_4 , ' ||
'  FREE_5 , ' ||
'  FREE_6 , ' ||
 ---------------
'  UNIT_OF_MEASURE , ' ||
 ---------------
'  PRODUCT_TYPE, ' ||
	------------------ IM REMAINING ---------------------
	-- Calculate Remaining using inner query results.
	/*
       REMAINING_COMB =( IM_ALLOWED_COMBD - IM_USED_COMBD )
       if ( IM_ALLOWED_COMBD is NULL)  then  remaining by periods is applicable.
       	  	REMAINING_<period> =( IM_ALLOWED_<period> - IM_USED_<period>)
       else remaining by period is not applicable
       		REMAINING_<period> = 0
	--
		1. refine remaining by seting it to zero if result is negative
		Note: for record_type =7, im_remaining <=0 ,  since it is not applicable
	*/
--'  ( decode( record_type,7,0,(GREATEST(IM_ALLOWED - IM_USED,0)) ) ) im_remaining, ' ||
'  ( decode( record_type,7,0,(GREATEST(IM_ALLOWED_COMBD - IM_USED,0)) ) ) im_remaining, ' ||
'  decode( IM_ALLOWED_COMBD, 0, GREATEST(IM_ALLOWED_1 - IM_USED_1 ,0), 0 ) im_remaining_1, ' ||
'  decode( IM_ALLOWED_COMBD, 0, GREATEST(IM_ALLOWED_2 - IM_USED_2 ,0), 0 ) im_remaining_2, ' ||
'  decode( IM_ALLOWED_COMBD, 0, GREATEST(IM_ALLOWED_3 - IM_USED_3 ,0), 0 ) im_remaining_3, ' ||
'  decode( IM_ALLOWED_COMBD, 0, GREATEST(IM_ALLOWED_4 - IM_USED_4 ,0), 0 ) im_remaining_4, ' ||
'  decode( IM_ALLOWED_COMBD, 0, GREATEST(IM_ALLOWED_5 - IM_USED_5 ,0), 0 ) im_remaining_5, ' ||
'  decode( IM_ALLOWED_COMBD, 0, GREATEST(IM_ALLOWED_6 - IM_USED_6 ,0), 0 ) im_remaining_6, ' ||
	------------------ CHARGABLE USAGE ---------------------
	-- Calculate Chargable Minutes /Mesages using inner query results.
	/*
	 1. Chargable = ( total_used - im_used - free )
	 --
     2.if ( IM_ALLOWED_COMBD  = IM_USED_COMBD )  then  we have chargable minutes
       	  	CHARGABLE_<period> =( TOTAL_USED_<period> - IM_USED_<period>) - FREE_<period>
       else  chargable minuts is zero
       		CHARGABLE_<period> = 0
	--
	3. When usage is Messsages ( unit_of_measure <> 'M' ), the formula is different:
	   so we need to implement this rule:
	   --
	    if ( unit of measure is 'M' ) then
	 			Chargable = ( total_used - im_used - free )
		else
	 			Chargable = ( total_calls - im_used - free )
	*/
'  decode( upper(UNIT_OF_MEASURE),''M'', ' ||
'  		   decode( IM_ALLOWED_COMBD, IM_USED_COMBD, (TOTAL_USED - IM_USED - FREE), 0 ), ' ||
'  		   (TOTAL_CALLS - IM_USED ) ' ||
'  ) chargeable, ' ||
'  decode( upper(UNIT_OF_MEASURE),''M'', ' ||
'  		   decode( IM_ALLOWED_COMBD, IM_USED_COMBD, (TOTAL_USED_1 - IM_USED_1 - FREE_1), 0 ), ' ||
'  		   (NUM_OF_CALLS_1 - IM_USED_1 ) ' ||
'  ) chargeable_1, ' ||
'  decode( upper(UNIT_OF_MEASURE),''M'', ' ||
'  		   decode( IM_ALLOWED_COMBD, IM_USED_COMBD, (TOTAL_USED_2 - IM_USED_2 - FREE_2), 0 ), ' ||
'  		   (NUM_OF_CALLS_2 - IM_USED_2 ) ' ||
'  ) chargeable_2, ' ||
'  decode( upper(UNIT_OF_MEASURE),''M'', ' ||
'  		   decode( IM_ALLOWED_COMBD, IM_USED_COMBD, (TOTAL_USED_3 - IM_USED_3 - FREE_3), 0 ), ' ||
'  		   (NUM_OF_CALLS_3 - IM_USED_3 ) ' ||
'  ) chargeable_3, ' ||
'  decode( upper(UNIT_OF_MEASURE),''M'', ' ||
'  		   decode( IM_ALLOWED_COMBD, IM_USED_COMBD, (TOTAL_USED_4 - IM_USED_4 - FREE_4), 0 ), ' ||
'  (	   NUM_OF_CALLS_4 - IM_USED_4 ) ' ||
'  ) chargeable_4, ' ||
'  decode( upper(UNIT_OF_MEASURE),''M'', ' ||
'  		   decode( IM_ALLOWED_COMBD, IM_USED_COMBD, (TOTAL_USED_5 - IM_USED_5 - FREE_5), 0 ), ' ||
'  		   (NUM_OF_CALLS_5 - IM_USED_5 ) ' ||
'  ) chargeable_5, ' ||
'  decode( upper(UNIT_OF_MEASURE),''M'', ' ||
'  		   decode( IM_ALLOWED_COMBD, IM_USED_COMBD, (TOTAL_USED_6 - IM_USED_6 - FREE_6), 0 ), ' ||
'  		   (NUM_OF_CALLS_6 - IM_USED_6 ) ' ||
'  ) chargeable_6, ' ||
 ------------------ TOTAL CHARGES ---------------------
'  TOTAL_CHARGE , ' ||
'  CHARGE_1 , ' ||
'  CHARGE_2 , ' ||
'  CHARGE_3 , ' ||
'  CHARGE_4 , ' ||
'  CHARGE_5 , ' ||
'  CHARGE_6, ' ||
 ------------------ ACCOUNT_POOLING ---------------------
'  decode( record_type,7,GREATEST(0,IM_ALLOWED - TOTAL_CHARGE) ,0) AP_REMAINING, ' ||
'  decode( record_type,7,GREATEST(0,TOTAL_CHARGE - IM_ALLOWED) ,0) AP_CHARGE, ' ||
 ------------------ MISCELANEOUS ---------------------
'  to_char(LAST_CALL_DATE,''YYYY-MM-DD'') LAST_CALL_DATE , ' ||
'  PRORATION_IND, ' ||
'  usage_utility_pkg.getSubHomeProvince( subscriber_no) HOME_PROVINCE,' ||
-- isShareablePricePlan indicator
'  RATING_LEVEL_CODE ' ||
 --
'  from ' ||
'  ( ' ||
 -- ======= BEGIN INNER QUERY 1 =====================================
 -- Summarize by record_type and airtime_feature_cd
     '  select ' ||
      -- subscriber
     '  max ( au.BAN ) ban, ' ||
     '  max ( SUBSCRIBER_NO ) subscriber_no, ' ||
      -- Service
     '  RECORD_TYPE, ' ||
     '  AIRTIME_FEATURE_CD, ' ||
      -- max( SECONDARY_FEATURE_CD ) SECONDARY_FEATURE_CD,
     '  PRICE_PLAN_CODE, ' ||
     '  ACTION_DIRECTION_CD, ' ||
	 '  max(SOC_EFFECTIVE_DATE) SOC_EFFECTIVE_DATE, ' ||
	 '  max(RATING_LEVEL_CODE) RATING_LEVEL_CODE, ' ||
      -- indicators
     '  max( PRODUCT_TYPE ) PRODUCT_TYPE, ' ||
     '  max( IM_ALLOCATION_IND ) IM_ALLOCATION_IND, ' ||
      -- AIRTIME
      --------------------- TOTAL CALLS      -------------
     '  sum( ' ||
     '  NUM_OF_CALLS_PRD_1 + ' ||
     '  NUM_OF_CALLS_PRD_2 + ' ||
     '  NUM_OF_CALLS_PRD_3 + ' ||
     '  NUM_OF_CALLS_PRD_4 + ' ||
     '  NUM_OF_CALLS_PRD_5 + ' ||
     '  NUM_OF_CALLS_PRD_6 + ' ||
     '  NUM_OF_CALLS ' ||
     '  ) total_calls, ' ||
      -- broken down by periods
     '  sum( NUM_OF_CALLS_PRD_1 ) num_of_calls_1, ' ||
     '  sum( NUM_OF_CALLS_PRD_2 ) num_of_calls_2, ' ||
     '  sum( NUM_OF_CALLS_PRD_3 ) num_of_calls_3, ' ||
     '  sum( NUM_OF_CALLS_PRD_4 ) num_of_calls_4, ' ||
     '  sum( NUM_OF_CALLS_PRD_5 ) num_of_calls_5, ' ||
     '  sum( NUM_OF_CALLS_PRD_6 ) num_of_calls_6, ' ||
      -- extra counters
     '  sum( NUM_OF_FREE_AIR_CALLS ) num_free_air_calls, ' ||
     '  sum( NUM_OF_NO_AIR_CALLS ) num_special_calls, ' ||
      --
      --------------------- TOTAL MINUTES      -------------
     '  sum( ' ||
     '  CTN_MINS_PRD_1 + ' ||
     '  CTN_MINS_PRD_2 + ' ||
     '  CTN_MINS_PRD_3 + ' ||
     '  CTN_MINS_PRD_4 + ' ||
     '  CTN_MINS_PRD_5 + ' ||
     '  CTN_MINS_PRD_6 + ' ||
     '  CTN_MINS ' ||
     '  ) total_used, ' ||
      -- broken down by periods
     '  sum( CTN_MINS_PRD_1 ) total_used_1, ' ||
     '  sum( CTN_MINS_PRD_2 ) total_used_2, ' ||
     '  sum( CTN_MINS_PRD_3 ) total_used_3, ' ||
     '  sum( CTN_MINS_PRD_4 ) total_used_4, ' ||
     '  sum( CTN_MINS_PRD_5 ) total_used_5, ' ||
     '  sum( CTN_MINS_PRD_6 ) total_used_6, ' ||
      --
      --------------------- TOTAL CHARGES      -------------
     '  sum ( ' ||
     '  CHRG_AMT_PRD_1 + ' ||
     '  CHRG_AMT_PRD_2 + ' ||
     '  CHRG_AMT_PRD_3 + ' ||
     '  CHRG_AMT_PRD_4 + ' ||
     '  CHRG_AMT_PRD_5 + ' ||
     '  CHRG_AMT_PRD_6 + ' ||
     '  CHRG_AMT ' ||
     '  ) total_charge, ' ||
      -- broken down by periods
     '  sum (CHRG_AMT_PRD_1) charge_1, ' ||
     '  sum (CHRG_AMT_PRD_2) charge_2, ' ||
     '  sum (CHRG_AMT_PRD_3) charge_3, ' ||
     '  sum (CHRG_AMT_PRD_4) charge_4, ' ||
     '  sum (CHRG_AMT_PRD_5) charge_5, ' ||
     '  sum (CHRG_AMT_PRD_6) charge_6, ' ||
      --
      --------------------- IM ALLOWED      -------------
      -- Need to see original value later.
--     '  sum ( nvl(IM_ALLOWED_COMBD,0) ) im_allowed_combd, ' ||
	  -- BUG Jan-2004: forgot to apply proration and then round()
     '  sum ( round( nvl(IM_ALLOWED_COMBD,0) * ' ||
	 '              decode (nvl(PRORATION_FACTOR,0),0,1,PRORATION_FACTOR) ) ' ||
	 '  ) im_allowed_combd, ' ||
      --
     '  sum( ' ||
      -- 1. needs to be prorated using proration factor
      -- 2. if combined is NULL, then return agregation of periods
     '  decode(nvl(IM_ALLOWED_COMBD,0), 0 , ' ||
     '  	( (nvl(IM_ALLOWED_PRD_1,0) + ' ||
     '  	   nvl(IM_ALLOWED_PRD_2,0) + ' ||
     '  	   nvl(IM_ALLOWED_PRD_3,0) + ' ||
     '  	   nvl(IM_ALLOWED_PRD_4,0) + ' ||
     '  	   nvl(IM_ALLOWED_PRD_5,0) + ' ||
     '  	   nvl(IM_ALLOWED_PRD_6,0) ) * ' ||
      -- 3. if proration_factor is null, then set it to 1
     '   	   decode (nvl(PRORATION_FACTOR,0),0,1,PRORATION_FACTOR) ' ||
     '  	), ' ||
      -- 4. otherwise return only combined.
     '   	( ' ||
     '  	   nvl(IM_ALLOWED_COMBD,0) * ' ||
      -- 5. if proration_factor is null, then set it to 1
     '   	   decode (nvl(PRORATION_FACTOR,0),0,1,PRORATION_FACTOR) ' ||
     '  	) ' ||
     '  ) ' ||
     '  ) im_allowed, ' ||
      -- broken down by periods
     '  sum (nvl(IM_ALLOWED_PRD_1,0) * ' ||
     '  	decode (nvl(PRORATION_FACTOR,0),0,1,PRORATION_FACTOR) ' ||
     '  ) im_allowed_1, ' ||
     '  sum (nvl(IM_ALLOWED_PRD_2,0) * ' ||
     '  	decode (nvl(PRORATION_FACTOR,0),0,1,PRORATION_FACTOR) ' ||
     '  ) im_allowed_2, ' ||
     '  sum (nvl(IM_ALLOWED_PRD_3,0) * ' ||
     '  	decode (nvl(PRORATION_FACTOR,0),0,1,PRORATION_FACTOR) ' ||
     '  ) im_allowed_3, ' ||
     '  sum (nvl(IM_ALLOWED_PRD_4,0) * ' ||
     '  	decode (nvl(PRORATION_FACTOR,0),0,1,PRORATION_FACTOR) ' ||
     '  ) im_allowed_4, ' ||
     '  sum (nvl(IM_ALLOWED_PRD_5,0) * ' ||
     '  	decode (nvl(PRORATION_FACTOR,0),0,1,PRORATION_FACTOR) ' ||
     '  ) im_allowed_5, ' ||
     '  sum (nvl(IM_ALLOWED_PRD_6,0) * ' ||
     '  	decode (nvl(PRORATION_FACTOR,0),0,1,PRORATION_FACTOR) ' ||
     '  ) im_allowed_6, ' ||
      --
      --------------------- IM USED      -------------
      -- Need to see original value later.
     '  sum ( CTN_IM_USED_COMBD ) im_used_combd, ' ||
      --
     '  sum ( ' ||
      -- 1. if combined is 0, then return agregation of periods
     '  decode( CTN_IM_USED_COMBD, 0 , ' ||
     '  ( ' ||
     '  CTN_IM_USED_PRD_1 + ' ||
     '  CTN_IM_USED_PRD_2 + ' ||
     '  CTN_IM_USED_PRD_3 + ' ||
     '  CTN_IM_USED_PRD_4 + ' ||
     '  CTN_IM_USED_PRD_5 + ' ||
     '  CTN_IM_USED_PRD_6 ' ||
     '  ), ' ||
     '  CTN_IM_USED_COMBD ' ||
     '  ) ' ||
     '  ) im_used, ' ||
      -- broken down by periods
     '  sum( CTN_IM_USED_PRD_1 ) im_used_1, ' ||
     '  sum( CTN_IM_USED_PRD_2 ) im_used_2, ' ||
     '  sum( CTN_IM_USED_PRD_3 ) im_used_3, ' ||
     '  sum( CTN_IM_USED_PRD_4 ) im_used_4, ' ||
     '  sum( CTN_IM_USED_PRD_5 ) im_used_5, ' ||
     '  sum( CTN_IM_USED_PRD_6 ) im_used_6, ' ||
      --
      --------------------- FREE USAGE      -------------
     '  sum ( ' ||
     '  FREE_MIN_USED_PRD_1 + ' ||
     '  FREE_MIN_USED_PRD_2 + ' ||
     '  FREE_MIN_USED_PRD_3 + ' ||
     '  FREE_MIN_USED_PRD_4 + ' ||
     '  FREE_MIN_USED_PRD_5 + ' ||
     '  FREE_MIN_USED_PRD_6 ' ||
     '  ) free, ' ||
      -- broken down by periods
     '  sum (FREE_MIN_USED_PRD_1) free_1, ' ||
     '  sum (FREE_MIN_USED_PRD_2) free_2, ' ||
     '  sum (FREE_MIN_USED_PRD_3) free_3, ' ||
     '  sum (FREE_MIN_USED_PRD_4) free_4, ' ||
     '  sum (FREE_MIN_USED_PRD_5) free_5, ' ||
     '  sum (FREE_MIN_USED_PRD_6) free_6, ' ||
      --------------------- UNIT_OF_MEASURE      -------------
     '  min( ' ||
      -- ensure unit of measure is NOT NULL
      -- if unit of measure is NULL, return by default 'M'
      -- or 'S' when airtime_feature_cd is 'MMMMAIL'
     '  decode( trim(UNIT_MEASUR_CODE), null, ' ||
      -- when null
      -- 'S' only if airtime feature code is 'MMMAIL', otherwise 'M'
     '   	 decode(airtime_feature_cd,''MMMAIL'',''s'',''m''), ' ||
      -- normal scenario
     '   	 trim(UNIT_MEASUR_CODE) ' ||
     '  	 ) ' ||
     '  ) unit_of_measure, ' ||
      --------------------- MISCELANEOUS       -------------
     '  max( LAST_IM_CALL_DT ) last_call_date, ' ||
     '  max( decode(nvl(PRORATION_FACTOR,0),1,''N'',0,''N'',''Y'') ) proration_ind '
	 ;


	 v_sql_from :=
				 ' from ' || v_table_name || ' au , billing_account ba ' ;

-- initial criteria deprecated before QA
	 v_sql_criteria :=
	                               -- CRITERIA TO FILTER USAGE RECORDS
                             '  and ' ||
                             '  ( ( record_type = 1 and airtime_feature_cd in ( ''STD'',''CDMA'',''DATA'',''DISP'',''DGRP'',''MMMAIL'' ) ) ' ||
                             '  or ' ||
                             '  ( record_type = 5 ) ' ||
                             '  or ' ||
                             '  ( record_type = 7 ) ' ||
                             '  ) ';
-- July-2003  deprecated
	 v_sql_criteria :=
	                               -- CRITERIA TO FILTER USAGE RECORDS
                             '  and ' ||
                             '  ( record_type = 1 and airtime_feature_cd = ''STD'' )' ||
							 	  -- Nov-2003  IM Combined  Only
							 '  and ( im_allocation_ind = ''C'' )';


-- January-2004
	 v_sql_criteria :=
	                         -- CRITERIA TO FILTER USAGE RECORDS
							 	  -- Jan-2004 ignore Non-Combined since they are rated by other Price Plan SOCs
								  -- Includes Standards Extended Home Area Calls ( im_allocation_ind=null )
  							 '  and ( nvl(trim(im_allocation_ind),''?'')  not in (''N'') ) ';

	 If ( v_query_level = QUERY_LEVEL_SUB ) Then
    	 v_sql_where :=
    				 ' where ' ||
					 '		 record_type = ' 			|| v_record_type ||
    				 ' 		 and airtime_feature_cd = ''' || pi_feature_code || ''' ' ||
    				 ' 		 and au.ban = ' 			  	|| pi_ban ||
    				 ' 		 and au.subscriber_no = ''' 	   	|| pi_subscriber_no || ''' ' ||
    				 '       and au.ban = ba.ban '  ||
     				 '	     and  bl_cur_bill_seq_no = bill_seq_no '||
					 v_sql_criteria;

    	 v_sql_group :=
    				 ' group by ' ||
    				 ' 		 record_type, airtime_feature_cd, price_plan_code, action_direction_cd ' ;
     Else
	 	 -- BAN Level
    	 v_sql_where :=
    				 ' where ' ||
					 '		 record_type = ' 			|| v_record_type ||
    				 ' 		 and airtime_feature_cd = ''' || pi_feature_code || ''' ' ||
    				 ' 		 and au.ban = ' 			  	|| pi_ban ||
    				 '       and au.ban = ba.ban ' ||
     				 '	     and  bl_cur_bill_seq_no = bill_seq_no '||
					 v_sql_criteria;

    	 v_sql_group :=
    				 ' group by ' ||
    				 ' 		 subscriber_no, record_type, airtime_feature_cd, price_plan_code, action_direction_cd ' ;
     End If;

	 v_sql_tail :=
                      --
                      -- ======= END INNER QUERY 1 =====================================
                     '  ) ' ;
                      -- ======= END QUERY 2 =====================================

	 v_sql_order2 :=
	 	             ' order by  ban,subscriber_no, record_type, airtime_feature_cd, soc_effective_date, price_plan_code, action_direction_cd ';

	 ------------------------------------------------------------------
	 -- Create reference to a cursor  using a dynamic SQL statement.
	 ------------------------------------------------------------------
 
 --println(v_sql_select);
 
 	 open  po_unbilled_usage for
 	   	   v_sql_select || v_sql_from || v_sql_where || v_sql_group || v_sql_tail || v_sql_order2;

-- Exception Handling

Exception       -- method A: raise user defined oracle exception passing oracle error message to caller.

When UnbilledCycleInfoNotFound Then
/*
	 Raise_Application_Error(-20150, 'Unbilled Cycle Information Not Found'
	 							|| ' [Ban=' || pi_ban || ',Sub=' || pi_subscriber_no || ']' );
*/
     -- return NULL cursor
     open po_unbilled_usage for
	 	  select NULL from dual where 1=0;

When UnbilledProvinceInfoNotFound Then
/*	 Raise_Application_Error(-20151, 'Unbilled Home Province Information Not Found'
	 							|| ' [Ban=' || pi_ban || ',Sub=' || pi_subscriber_no || ']' );
*/
     -- return NULL cursor
     open po_unbilled_usage for
	 	  select NULL from dual where 1=0;

When NO_DATA_FOUND Then
	If ( po_unbilled_usage%ISOPEN ) Then
	   close po_unbilled_usage;
	End If;
/*
	Raise_Application_Error(-20152, 'Unbilled Usage Summary Not Found'
	 							|| ' [Ban=' || pi_ban || ',Sub=' || pi_subscriber_no || ']' );
*/
     -- return NULL cursor
     open po_unbilled_usage for
	 	  select NULL from dual where 1=0;

When Others Then
	If ( po_unbilled_usage%ISOPEN ) Then
	   close po_unbilled_usage;
	End If;
	Raise_Application_Error(-20152, 'Unbilled Usage Summary Query Failed. Oracle:(['
    							|| sqlcode || '] [' || sqlerrm  || '])'
	 							|| ' [Ban=' || pi_ban || ',Sub=' || pi_subscriber_no
    							|| ',UsageTable=' || v_table_name
								|| ']'
								);

/*
Exception       -- method B:  Capture every oracle exception and return NULL cursor to caller.
				-- experimental code
When Others Then
	If ( po_unbilled_usage%ISOPEN ) Then
	   close po_unbilled_usage;
	End If;
    -- return a NULL cursor to allow next level to throw NULL_CURSOR application exception.
    open  po_unbilled_usage for
        select NULL from dual where 1=0;
*/

End GetUnbilledUsageSummary;

--------------------------------------------------------------------------
PROCEDURE println (v_line IN VARCHAR2)
   IS
      i_length   NUMBER;
      i_index    NUMBER;
   BEGIN
      i_length := LENGTH (v_line);
      i_index := 0;

      WHILE i_length > 255
      LOOP
         DBMS_OUTPUT.put_line (SUBSTR (v_line, i_index, 255));
         i_index := i_index + 255;
         i_length := i_length - 255;
      END LOOP;

      DBMS_OUTPUT.put_line (SUBSTR (v_line,
                                    i_index,
                                    LENGTH (v_line) - i_index + 1
                                   )
                           );
   END println;



End Usage_Utility_pkg;
/

-- End of DDL script for USAGE_UTILITY_PKG
