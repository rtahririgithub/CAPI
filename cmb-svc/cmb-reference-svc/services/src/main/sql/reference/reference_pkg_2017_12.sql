/* Formatted on 2006/09/27 11:51 (Formatter Plus v4.8.0) */
CREATE OR REPLACE PACKAGE reference_pkg
AS
-------------------------------------------------------------------------
-- IMPORTANT NOTE !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
-- Please increment the version_no variable value when you make changes to this package 
-- to reflect version changes.
------------------------------------------------------------------------
-- description: Package REFERENCE_PKG contains procedures and functions
--          for reference data retrieval from Knowbility database.
--
-- Date       version    Developer            Modifications
-- 10-26-2005  		Vladimir Tsitrin     created
-- 06-16-2006   	Marina   Kuper    Moved KnowbilityOperators query to  REFERENCE_APP_PKG
-- 06-30-2006   	Michael Qin     CollectionPathDetails
-- 10-26-2005  		Vladimir Tsitrin     added PaymentTransferReasons
-- 11-07-2006   	Marina Kuper      Moved BillCycles query to  REFERENCE_APP_PKG
-- 19-08-2006   	Marina Kuper      Fixed cursor closing issue
-- 18-09-2006   	Michael Liao      Added new function IsPortOutAllowed
-- Nov 14,2006  	Michael Liao      Modified function getreferencedata regarding retrieving "MigrationTypes"
-- 29-11-2006   	Roman Tov         Modified AccountTypes query
-- 17-01-2007   	Tsz Chung Tong    Fixed "LettersByTitleKeyword" under getreferencedata
-- Feb 7,2007   	Tsz Chung Tong    Pareto Project - Added "BanSegments" under getreferencedata
-- 02-04-2007   	Marina Kuper      Fixed CountryProvinces
-- Jun 11,2007  	Michael Liao      Added retrieving "SpecialNumbers" and "SpecialNumberRanges" in side getrferencedata
-- Oct 03,2007  	Marina Kuper      Changed retrieving "SpecialNumbers" and "SpecialNumberRanges" 
-- Nov 22,2007  	Dimitry Siganevich      added DiscountPlans, RetrievePromoDiscountPlans
-- Jan 18,2008  	Sutha      Modified function getreferencedata regarding retrieving "PoolingGroups"
-- Apr 9, 2008 		Andrew Pereira		Modified function getreferencedata to retrieve "VendorServices"
-- Dec 16, 2008 	Andrew Pereira		Modified function getreferencedata to retrieve "NetworkTypes"
-- Jun 25,2009  	Mujeeb Waraich    Modified function getreferencedate to retrieve "Routes" and added coulumn adr_county to retrieve from SID table
-- Feb 23, 2010 	Andrew Pereira		Modified function getreferencedata to retrieve "TaxationPolicies"
-- May 30, 2012 	Naresh		       Added getVersion for shakedown testing
-- May 02, 2013 3.23.1 	Mahan Razagh           Added new enhanced function : RetrievePromoDiscountPlansEnha
-- Jul 14, 2013 3.23.2  Tsz Chung Tong   Corrected parameter name in RetrievePromoDiscountPlansEnh (v_sub_market should be v_ignore_province)
-- May 24, 2014 3.28.3  Richard Fong   Modified function getreferencedata to retrieve "SeatTypes"
-----------------------------------------------------------------------
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
   err_no_data_found   CONSTANT VARCHAR2 (50) := 'No data found.';
   err_other           CONSTANT VARCHAR2 (50) := 'Other PL/SQL error.';
   -- absolute maximum for the number of accounts to be retrieved
   max_maximorum       CONSTANT NUMBER (4)     := 1000;
   -- package version constant
   version_no          CONSTANT VARCHAR2(10)       := '3.28.3';

   FUNCTION getVersion RETURN VARCHAR2;
     
   FUNCTION getreferencedata (
      v_reftype         IN       VARCHAR,
      v_param           IN       VARCHAR,
      c_cursor          OUT      refcursor,
      v_error_message   OUT      VARCHAR2
   )
      RETURN NUMBER;

   FUNCTION isportoutallowed (
      v_status     IN   VARCHAR,
      v_activity   IN   VARCHAR,
      v_reason     IN   VARCHAR
   )
      RETURN NUMBER;
      
   FUNCTION RetrievePromoDiscountPlans (
      v_param1          IN       VARCHAR,
      v_param2          IN       VARCHAR,
      v_param3          IN       VARCHAR,
      v_param4          IN       VARCHAR,
      v_param5          IN       t_product_promo_type,
      v_param6          IN       VARCHAR,
      v_param7          IN       VARCHAR,
      v_param8          IN       VARCHAR,
      po_discounts      OUT      refcursor,
      v_error_message   OUT      VARCHAR2
   )
      RETURN NUMBER;       


FUNCTION RetrievePromoDiscountPlansEnha(
      v_soc                   IN       VARCHAR,
      v_province            IN       VARCHAR,
      v_ignore_province       IN       VARCHAR,
      v_term_months     IN       VARCHAR,
      v_product_type     IN       t_product_promo_type,
      v_initial_activation IN       VARCHAR,
      v_prod_init_act     IN       VARCHAR,
      v_expiry_date       IN       VARCHAR,
      po_discounts        OUT     refcursor,
      v_error_message  OUT   VARCHAR2
   )
      RETURN NUMBER;
END;
/

CREATE OR REPLACE PACKAGE BODY reference_pkg
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
               WHEN 'Generations'
               THEN
                  OPEN c_cursor
                   FOR
                      SELECT nm_generation_code, nm_generation_desc,
                             nm_generation_desc_f
                        FROM name_generation;
               WHEN 'AccountTypes'
               THEN
                  OPEN c_cursor
                   FOR
                      SELECT AT.acc_type, AT.acc_sub_type, AT.description,
                             AT.description_f, AT.duplicate_ban_check,
                             atd.nl_id, atd.default_dealer,
                             atd.default_sales_code,
                             NVL (atd.min_ufmi_in_range, 0), AT.name_format, NVL(AT.cas_credit_req_ind,'N')
                        FROM account_type AT, account_sub_type_defaults atd
                       WHERE AT.acc_type = atd.acct_type(+)
                             AND AT.acc_sub_type = atd.acct_sub_type(+);
               WHEN 'ActivityTypes'
               THEN
                  OPEN c_cursor
                   FOR
                      SELECT   'CSM', ca_activity_code code,
                               ca_activity_rsn_code rsn,
                               ca_activity_desc act_desc,
                               ca_activity_desc_f act_desc_f,
                               ca_activity_rsn_desc rsn_desc, NULL, NULL,
                               NULL, NULL, 'N'
                          FROM csm_activity
                      UNION
                      SELECT DISTINCT 'CSM', csa_activity_code code,
                                      csa_activity_rsn_code rsn,
                                      csa_activity_desc act_desc,
                                      csa_activity_desc_f act_desc_f,
                                      csa_activity_rsn_desc rsn_desc,
                                      csa_activity_rsn_desc_f rsn_desc_f,
                                      csa_charge_code, NULL,
                                      csa_process_code,
                                      DECODE (sarc.activity_reason_code,
                                              NULL, 'N',
                                              'Y'
                                             )
                                 FROM csm_status_act csa,
                                      soc_activity_reason_code sarc
                                WHERE sarc.activity_code(+) =
                                                         csa.csa_activity_code
                                  AND sarc.activity_reason_code(+) =
                                                     csa.csa_activity_rsn_code
                      UNION
                      SELECT   'OTHER', art.actv_code, art.actv_reason_code,
                               aa.ar_activity_desc, aa.ar_activity_desc_f,
                               art.bill_fixed_text, art.bill_fixed_text_f,
                               art.feature_code, art.direction, NULL, 'N'
                          FROM activity_rsn_text art, ar_activities aa
                         WHERE aa.ar_activity(+) = art.actv_code
                      UNION
                      SELECT   'CANCEL', 'ADJR', RTRIM (cancel_reason_code),
                               'Reversal', 'Annulé', cancel_desc,
                               cancel_desc_f, NULL, NULL, process_code, 'N'
                          FROM cancel_reason
                      ORDER BY 1, 2;
               WHEN 'AdjustmentReasons'
               THEN
                  OPEN c_cursor
                   FOR
                      SELECT reason_code, adj_dsc, adj_dsc_f, man_adj_ind,
                             adj_def_amt, adj_def_amt_over_ind,
                             adj_level_code, adj_actv_code, adj_tax_rep_ind,
                             adj_category, recur_frequency, max_no_times,
                             promotion_expiration_date, adj_type_code
                        FROM adjustment_reason;
               WHEN 'AllProvinces'
               THEN
                  OPEN c_cursor
                   FOR
                      SELECT country, state_code, state_name, state_name_f
                        FROM state
                       WHERE NOT state_name LIKE '%DO NOT USE%';
               WHEN 'AllTitles'
               THEN
                  OPEN c_cursor
                   FOR
                      SELECT   nm_title_code, nm_title_name, nm_title_name_f
                          FROM name_title
                      ORDER BY nm_title_name;
               WHEN 'BillHoldRedirectDestinations'
               THEN
                  OPEN c_cursor
                   FOR
                      SELECT destination_code, destination_desc,
                             destination_desc_f
                        FROM hold_bill_dest;
               WHEN 'BusinessRoles'
               THEN
                  OPEN c_cursor
                   FOR
                      SELECT business_role_code, description
                        FROM business_role;
               WHEN 'CollectionActivities'
               THEN
                  OPEN c_cursor
                   FOR
                      SELECT col_activity_code, col_activity_desc,
                             col_activity_desc_f
                        FROM collection_act;
               WHEN 'CollectionAgencies'
               THEN
                  OPEN c_cursor
                   FOR
                      SELECT col_agency_code, col_agency_full_name,
                             col_agency_full_name_f
                        FROM collection_agency;
               WHEN 'CollectionStates'
               THEN
                  OPEN c_cursor
                   FOR
                      SELECT   col_path_code, col_step_num,
                               col_activity_code
                          FROM collection_pol_stp
                      ORDER BY col_path_code, col_step_num;
               WHEN 'Countries'
               THEN
                  OPEN c_cursor
                   FOR
                      SELECT country_code, country_name, country_name_f
                        FROM foreign_country;
               WHEN 'CountryProvinces'
               THEN
                  OPEN c_cursor
                   FOR
                      SELECT country, state_code, state_name, state_name_f
                        FROM state
                       WHERE country = v_Param
                         AND NOT state_name LIKE '%DO NOT USE%';
               WHEN 'CreditCardPaymentTypes'
               THEN
                  OPEN c_cursor
                   FOR
                      SELECT DISTINCT pym_sub_method, psm_desc, psm_desc_f
                                 FROM payment_sub_method
                                WHERE pym_method = 'CC';
               WHEN 'CreditMessages'
               THEN
                  OPEN c_cursor
                   FOR
                      SELECT credit_message_cd, credit_referral_flg,
                             english_message, french_message
                        FROM credit_class_decision;
               WHEN 'Departments'
               THEN
                  OPEN c_cursor
                   FOR
                      SELECT   dprt_department_code, dprt_dprt_desc,
                               dprt_dprt_desc_f, dprt_deflt_work_pos
                          FROM department
                         WHERE dprt_dprt_desc NOT LIKE '0-%'
                           AND dprt_dprt_desc NOT LIKE 'B0%'
                           AND dprt_dprt_desc NOT LIKE 'B-%'
                           AND dprt_dprt_desc NOT LIKE '0%'
                           AND dprt_dprt_desc NOT LIKE 'A-%'
                           AND dprt_dprt_desc NOT LIKE 'C0%'
                           AND dprt_dprt_desc NOT LIKE '%Obsolete%'
                      ORDER BY dprt_department_code;
               WHEN 'EquipmentPossessions'
               THEN
                  OPEN c_cursor
                   FOR
                      SELECT gen_code, gen_desc, gen_desc_f
                        FROM generic_codes
                       WHERE gen_type = 'LINE_TYPE';
               WHEN 'LettersByCategory'
               THEN
                  OPEN c_cursor
                   FOR
                      SELECT letter_code, letter_desc, letter_desc_f,
                             letter_cat, effective_date, expiration_date,
                             letter_title, letter_title_f, letter_ver,
                             letter_subcat, letter_level
                        FROM lms_letters l1
                       WHERE letter_cat = v_param
                         AND manual_request_ind = 'Y'
                         AND effective_date <= SYSDATE
                         AND (   expiration_date IS NULL
                              OR expiration_date >= SYSDATE
                             )
                         AND letter_ver =
                                (SELECT MAX (letter_ver)
                                   FROM lms_letters l2
                                  WHERE l2.letter_code = l1.letter_code
                                    AND l2.letter_cat = l1.letter_cat
                                    AND (   l2.letter_subcat IS NULL
                                         OR l2.letter_subcat =
                                                              l1.letter_subcat
                                        )
                                    AND l2.letter_level = l1.letter_level);
               WHEN 'LettersByTitleKeyword'
               THEN
                  OPEN c_cursor
                   FOR
                      SELECT letter_code, letter_desc, letter_desc_f,
                             letter_cat, effective_date, expiration_date,
                             letter_title, letter_title_f, letter_ver,
                             letter_subcat, letter_level
                        FROM lms_letters l1
                        WHERE (   UPPER (letter_title) LIKE '' || v_param || '%'
                              OR UPPER (letter_title_f) LIKE '' || v_param || ' %'
                             )
                         AND manual_request_ind = 'Y'
                         AND effective_date <= SYSDATE
                         AND (   expiration_date IS NULL
                              OR expiration_date >= SYSDATE
                             )
                         AND letter_ver =
                                (SELECT MAX (letter_ver)
                                   FROM lms_letters l2
                                  WHERE l2.letter_code = l1.letter_code
                                    AND l2.letter_cat = l1.letter_cat
                                    AND (   l2.letter_subcat IS NULL
                                         OR l2.letter_subcat =
                                                              l1.letter_subcat
                                        )
                                    AND l2.letter_level = l1.letter_level);
               WHEN 'LogicalDate'
               THEN
                  OPEN c_cursor
                   FOR
                      SELECT logical_date
                        FROM logical_date
                       WHERE logical_date_type = 'O';
               WHEN 'ManualChargeTypes'
               THEN
                  OPEN c_cursor
                   FOR
                      SELECT DISTINCT charge_info.feature_code,
                                      feature.feature_desc,
                                      feature.feature_desc_f,
                                      feature.product_type,
                                      charge_info.mnl_oc_chrg_level,
                                      charge_info.mnl_oc_amt_ovrd_ind,
                                      charge_info.def_bal_impact_cd,
                                      pp_oc_rate.rate
                                 FROM charge_info, feature, pp_oc_rate
                                WHERE charge_info.feature_code =
                                                         feature.feature_code
                                  AND charge_info.feature_code =
                                                       pp_oc_rate.feature_code
                                  AND charge_info.ftr_revenue_code = 'O'
                                  AND charge_info.manual_oc_create_ind = 'Y';
               WHEN 'MarketProvinces'
               THEN
                  OPEN c_cursor
                   FOR
                      SELECT ssr.sub_market, m.province
                        FROM market m, soc_submkt_relation ssr
                       WHERE RTRIM (ssr.soc) = v_param
                             AND m.market_code(+) = ssr.sub_market;
               WHEN 'Networks'
               THEN
                  OPEN c_cursor
                   FOR
                      SELECT n_dap_id, n_dap_alias, n_dap_desc
                        FROM n_dap;
               WHEN 'NetworkTypes'
               THEN
                  OPEN c_cursor
                   FOR
                      SELECT GEN_CODE, GEN_DESC, GEN_DESC_F 
					  FROM GENERIC_CODES 
					  WHERE GEN_TYPE = 'NETWORK_TYPE';
               WHEN 'PaymentMethodTypes'
               THEN
                  OPEN c_cursor
                   FOR
                      SELECT DISTINCT pym_method, method_desc, method_desc_f
                                 FROM payment_method;
               WHEN 'PaymentSourceTypes'
               THEN
                  OPEN c_cursor
                   FOR
                      SELECT source_id, src_name, src_name_f, source_type
                        FROM payment_source;
               WHEN 'PaymentTransferReasons'
               THEN
                  OPEN c_cursor
                   FOR
                      SELECT bck_code, bck_dsc, bck_dsc_f
                        FROM backout_rsn_code
                       WHERE bck_rsn_type IN ('F', 'T');
               WHEN 'ProductTypes'
               THEN
                  OPEN c_cursor
                   FOR
                      SELECT product_type, product_desc, product_desc_f
                        FROM product;
               WHEN 'Provinces'
               THEN
                  OPEN c_cursor
                   FOR
                      SELECT   p.province_code, p.province_name,
                               p.province_name_f, pe.canada_post_code
                          FROM province p, province_ext pe
                         WHERE p.province_code <> 'CL'
                           AND p.province_code = pe.province_code
                      ORDER BY p.province_name;
               WHEN 'ProvisioningTransactionStatuses'
               THEN
                  OPEN c_cursor
                   FOR
                      SELECT   srv_sts_cd, srv_sts_dsc
                          FROM srv_statuses
                      ORDER BY srv_sts_dsc;
               WHEN 'ProvisioningTransactionTypes'
               THEN
                  OPEN c_cursor
                   FOR
                      SELECT gen_code, gen_desc, gen_desc_f
                        FROM generic_codes
                       WHERE gen_type = 'SC_ACTIVITY';
               WHEN 'SIDs'
               THEN
                  OPEN c_cursor
                   FOR
                      SELECT SID, sid_commercial_name, sid_state_prov,
                             sid_city,adr_county 
                        FROM SID
                       WHERE expiration_date IS NULL
                          OR expiration_date > SYSDATE;
               WHEN 'States'
               THEN
                  OPEN c_cursor
                   FOR
                      SELECT   state_code, state_name, state_name_f
                          FROM state
                         WHERE country = 'USA'
                      ORDER BY state_name;
               WHEN 'StreetTypes'
               THEN
                  OPEN c_cursor
                   FOR
                      SELECT gen_code, gen_desc, gen_desc_f
                        FROM generic_codes
                       WHERE gen_type = 'STREET_TYPE';
               WHEN 'SystemDate'
               THEN
                  OPEN c_cursor
                   FOR
                      SELECT SYSDATE
                        FROM DUAL;
               WHEN 'TaxationPolicy'
               THEN
                  OPEN c_cursor
                   FOR
					  SELECT PROVINCE_CODE, TAX_TYPE, PST_RATE,
	  			 			 nvl(PST_CALC_MTHD,'U'), PST_MIN_TAXBL_AMT, 
	  			 			 HST_RATE, gst.GST_RATE 
					  FROM PROVNC_TAX_POLICY ptp,
					  	   (
	        			   	SELECT GST_RATE, EFFECTIVE_DATE, EXPIRATION_DATE 
    	                   	FROM GEN_TAX_POLICY
                	       )gst,
                           (SELECT LOGICAL_DATE
                            FROM LOGICAL_DATE
                            WHERE LOGICAL_DATE_TYPE='O') ld
					  WHERE ptp.effective_date <= ld.LOGICAL_DATE 
			 			AND (ptp.expiration_date is null or ptp.expiration_date >= ld.LOGICAL_DATE)
                        AND (gst.effective_date <= ld.LOGICAL_DATE AND (gst.expiration_date is null or gst.expiration_date >= ld.LOGICAL_DATE));                       
               WHEN 'Titles'
               THEN
                  OPEN c_cursor
                   FOR
                      SELECT   nt.nm_title_code, nt.nm_title_name,
                               nt.nm_title_name_f, ntf.nm_title_code_f
                          FROM name_title nt, name_title_filter ntf
                         WHERE nt.nm_title_code = ntf.nm_title_code
                      ORDER BY nt.nm_title_name;
               WHEN 'UnitTypes'
               THEN
                  OPEN c_cursor
                   FOR
                      SELECT   gen_code, gen_desc, gen_desc_f
                          FROM generic_codes
                         WHERE gen_type = 'UNIT_DESIG'
                      ORDER BY gen_desc;
               WHEN 'MigrationTypes'
               THEN
                  OPEN c_cursor
                   FOR
                      SELECT   gen_code, gen_desc, gen_desc_f
                          FROM generic_codes
                         WHERE gen_type = 'MIGRATION_TYPE'
                      UNION
                      SELECT generic_code, description, description_f
                        FROM generic_codes_ext
                       WHERE generic_type='MIGRATION_TYPE'
                      ORDER BY 2;
               WHEN 'CreditCheckDepositChangeReasons'
               THEN
                  OPEN c_cursor
                   FOR
                      SELECT   gen_code, gen_desc, gen_desc_f
                          FROM generic_codes
                         WHERE gen_type = 'DEP_CHG_RSN'
                      ORDER BY gen_desc;
               WHEN 'ExceptionReasons'
               THEN
                  OPEN c_cursor
                   FOR
                      SELECT exc_rsn_code, exc_desc, exc_desc_f
                        FROM batch_trx_rej_rsn;
               WHEN 'CreditClasses'
               THEN
                  OPEN c_cursor
                   FOR
                      SELECT credit_class, acc_elig_for_lt_pym,
                             crd_class_shrt_desc, crd_class_shrt_desc_f,
                             crd_class_long_desc, crd_class_long_desc_f,
                             deposit_required_ind, online_select_ind
                        FROM credit_class;
               WHEN 'MemoTypeCategories'
               THEN
                  OPEN c_cursor
                   FOR
                      SELECT DISTINCT memtp_memo_category
                                 FROM memo_type
                                WHERE memtp_manual_ind = 'Y';
               WHEN 'MemoTypes'
               THEN
                  OPEN c_cursor
                   FOR
                      SELECT   memtp_memo_type, memtp_memo_description,
                               memtp_memo_description_f, memtp_memo_category,
                               memtp_system_txt, memtp_system_txt_f,
                               memtp_num_of_prm, memtp_manual_ind
                          FROM memo_type
                      ORDER BY memtp_memo_type;
               WHEN 'CollectionPathDetails'
               THEN
                  OPEN c_cursor
                   FOR
                      SELECT   col_path_code, col_step_num,
                               col_activity_code, days_from_prev_step,
                               point_of_days_count, follow_up_type,
                               bill_msg_actv_code, bill_msg_actv_rsn_cd,
                               approval_fu_type, letter_title,
                               letter_title_f
                          FROM collection_pol_stp LEFT JOIN letters ON collection_pol_stp.letter_code =
                                                                         letters.letter_code
                      ORDER BY col_path_code, col_step_num;
               WHEN 'CollectionStepApproval'
               THEN
                  OPEN c_cursor
                   FOR
                      SELECT generic_code, description, description_f
                        FROM generic_codes_ext
                       WHERE generic_type = 'COLL_STEP_APPR';
               WHEN 'BanSegments'
               THEN
                  OPEN c_cursor
                   FOR
                      SELECT   brand_id, account_type, seg_area, seg_segment, seg_subsegment
                          FROM segmentation;
               WHEN 'SpecialNumbers'
               THEN
                  OPEN c_cursor
                   FOR
                      SELECT npa||nxx||line_no phone_number
                             , special_num_desc,special_num_desc_f
                             , range_last_npa||range_last_nxx||range_last_line_no range_last_num
                        FROM special_numbers sn, logical_date ld
                       WHERE TRUNC (sn.effective_date) <= TRUNC (ld.logical_date)
                         AND ( TRUNC (sn.expiration_date) > TRUNC (ld.logical_date)
                              OR sn.expiration_date IS NULL
                             )
                       AND ld.logical_date_type = 'O'      
                       AND range_last_npa is null;
               WHEN 'SpecialNumberRanges'
               THEN
                  OPEN c_cursor
                   FOR
                      SELECT npa||nxx||line_no phone_number
                             , special_num_desc,special_num_desc_f
                             , range_last_npa||range_last_nxx||range_last_line_no range_last_num
                        FROM special_numbers sn, logical_date ld
                        WHERE TRUNC (sn.effective_date) <= TRUNC (ld.logical_date)
                         AND ( TRUNC (sn.expiration_date) > TRUNC (ld.logical_date)
                              OR sn.expiration_date IS NULL
                             )
                       AND ld.logical_date_type = 'O'    
                       AND range_last_npa is not null;
               WHEN 'DiscountPlans'
               THEN
                  OPEN c_cursor
                   FOR
                      select distinct  dp.discount_plan_cd,discount_desc,
                             discount_desc_f,avail_eff_date,avail_exp_date,
                             flat_amt,disc_qty_type ,nvl(default_num_month,0),
                             group_code,product_type,discount_plan_level,dpb.brand_id 
					  from   discount_plan dp, 
					         discount_category dc, 
						     logical_date ld, 
						     discount_plan_brands dpb
					  where  dp.discount_plan_cd = dc.disc_plan_cd 
					  and    dp.discount_plan_cd = dpb.discount_plan_cd(+)
					  and ( trunc(dp.avail_eff_date)<=trunc(ld.logical_date) 
					  and  		(trunc(dp.avail_exp_date)>trunc(ld.logical_date) 
					  			or   dp.avail_exp_date is null) 
					  		and v_param = 'Y' or  v_param = 'N')
					  and  ld.logical_date_type='O'
					  order by discount_plan_cd;
				WHEN 'PoolingGroups' 
	       		THEN
	               OPEN c_cursor
                    FOR
					  SELECT pool_group_id, pool_group_desc, pool_group_desc_f, coverage_type
		         	  FROM POOLING_GROUPS
			 		  ORDER BY pool_group_desc;	
	       		WHEN 'VendorServices'
               	THEN
                   OPEN c_cursor
                    FOR
                      SELECT DISTINCT VC.CATEGORY_CODE, GC.DESCRIPTION, GC.DESCRIPTION_F, 
							 VC.UNIT_OF_MEASURE_CD, VC.PERIOD_CNT, SV.VENDOR_NM, 
							 VC.RESTRICT_IND, VC.NOTIFY_IND
					  FROM VENDOR_CATEGORY VC, SERVICE_VENDOR SV, GENERIC_CODES_EXT GC
					  WHERE VC.SERVICE_VENDOR_ID = SV.SERVICE_VENDOR_ID AND
	  						VC.CATEGORY_CODE = GC.GENERIC_CODE
					  ORDER BY VC.CATEGORY_CODE;
           		WHEN 'Routes'
	       		THEN
	       	 	   OPEN c_cursor
	           	    FOR
	           		  select SWITCH_ID, ROUTE_ID, SERVE_SID, ADR_CITY, ADR_COUNTY, ADR_PROV_STATE_CD 
	        		  from route;
            	WHEN 'SeatTypes'
               	THEN
                   OPEN c_cursor
                    FOR
                      select seat_type, seat_desc, seat_desc_f
                      from seat_types;
                      
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
            IF (c_cursor%ISOPEN)
            THEN
               CLOSE c_cursor;
            END IF;

            v_error_message := 'Invalid reference type: ' || v_reftype;
            i_result := numeric_false;
         WHEN OTHERS
         THEN
            IF (c_cursor%ISOPEN)
            THEN
               CLOSE c_cursor;
            END IF;

            v_error_message := SQLERRM;
            i_result := numeric_false;
      END;

      RETURN i_result;
   END getreferencedata;

   FUNCTION isportoutallowed (
      v_status     IN   VARCHAR,
      v_activity   IN   VARCHAR,
      v_reason     IN   VARCHAR
   )
      RETURN NUMBER
   IS
      RESULT   NUMBER   := 0;
      ind      CHAR (1);
   BEGIN
      SELECT port_out_allowed_ind
        INTO ind
        FROM status_activity_ext
       WHERE status_code = v_status
         AND activity_code = v_activity
         AND activity_reason_code = v_reason;

      IF ((ind IS NOT NULL) AND (ind = 'Y'))
      THEN
         RESULT := 1;
      END IF;

      RETURN RESULT;
   EXCEPTION
      WHEN NO_DATA_FOUND
      THEN
         RETURN 0;
      WHEN OTHERS
      THEN
         RAISE;
   END isportoutallowed;
   
   FUNCTION RetrievePromoDiscountPlans (
      v_param1          IN       VARCHAR,
      v_param2          IN       VARCHAR,
      v_param3          IN       VARCHAR,
      v_param4          IN       VARCHAR,
      v_param5          IN       t_product_promo_type,
      v_param6          IN       VARCHAR,
      v_param7          IN       VARCHAR,
      v_param8          IN       VARCHAR,
      po_discounts      OUT      refcursor,
      v_error_message   OUT      VARCHAR2
   )
      RETURN NUMBER 
   IS
      i_result        		NUMBER (1);
      v_cursor_text   		VARCHAR2 (32767);
 
   BEGIN 
      BEGIN
         IF v_param4 IS NOT NULL AND LENGTH (RTRIM (LTRIM (v_param4))) > 0
         THEN
         OPEN po_discounts
          FOR SELECT DISTINCT sdp.discount_plan_cd 
                           ,dp.discount_desc   
                           ,dp.discount_desc_f  
                           ,dp.avail_eff_date 
                           ,dp.avail_exp_date 
                           ,dp.flat_amt  
                           ,dp.disc_qty_type 
                           ,nvl(dp.default_num_month,0)
                           ,sdp.offer_end_date 
                           ,nvl(pp_disc.disc_plan_cd,'N') 
                           ,product_type 
                           ,discount_plan_level 
                           ,dpb.brand_id
             from soc_submkt_relation ssr    
                  ,soc_discount_plan sdp
                  ,soc_discount_market sdm 
                  ,market m 
                  ,discount_plan dp 
                  ,logical_date  ld 
                  ,(select disc_plan_cd from discount_category 
                    where group_code='PP') pp_disc 
                  ,discount_plan_brands dpb 
             where rtrim(ssr.soc) = v_param1 
             and (( ssr.sub_market=m.market_code 
                   and  m.province= v_param2 
                   or ssr.sub_market='ALL') 
                   and v_param3 ='N' 
                   or v_param3 ='Y')
             and   sdp.soc =  ssr.soc
			 and sdp.term_months = v_param4   
             and  ( sdp.product_type_id IN (SELECT * FROM TABLE (CAST (v_param5 AS t_product_promo_type)))
             	and  ( sdp.initial_activation_ind = v_param6 or sdp.initial_activation_ind ='A')
             	and   v_param7 ='N' 
                or    v_param7 ='Y' ) 
             and ( trunc(sdp.effective_date)<=trunc(ld.logical_date) 
                  and  (trunc(sdp.expiry_date)>trunc(ld.logical_date) 
                  or   sdp.expiry_date is null) 
                  and v_param8 ='Y' 
                  or  v_param8 ='N' ) 
             and  sdp.discount_plan_cd=dp.discount_plan_cd 
             and ( trunc(dp.avail_eff_date)<=trunc(ld.logical_date) 
                  and  (trunc(dp.avail_exp_date)>trunc(ld.logical_date) 
                  or   dp.avail_exp_date is null) 
                  and v_param8 ='Y' 
                  or  v_param8 ='N' ) 
             and  sdm.soc_discount_plan_id=sdp.soc_discount_plan_id 
             and (( sdm.sub_market_code=m.market_code 
                  and  m.province= v_param2 
                  or sdm.sub_market_code='ALL') 
                  and (  trunc(sdm.effective_date)<=trunc(ld.logical_date) 
                  and  (trunc(sdm.expiry_date)>trunc(ld.logical_date) 
                  or   sdm.expiry_date is null) 
                  and v_param8 ='Y' 
                  or  v_param8 ='N' ) 
                  and v_param3 ='N' 
                  or v_param3 ='Y') 
             and  ld.logical_date_type='O' 
             and pp_disc.disc_plan_cd(+) =sdp.discount_plan_cd 
             and dp.discount_plan_cd = dpb.discount_plan_cd(+)  ;
         ELSE
         OPEN po_discounts
          FOR SELECT DISTINCT sdp.discount_plan_cd 
                           ,dp.discount_desc   
                           ,dp.discount_desc_f  
                           ,dp.avail_eff_date 
                           ,dp.avail_exp_date 
                           ,dp.flat_amt  
                           ,dp.disc_qty_type 
                           ,nvl(dp.default_num_month,0)
                           ,sdp.offer_end_date 
                           ,nvl(pp_disc.disc_plan_cd,'N') 
                           ,product_type 
                           ,discount_plan_level 
                           ,dpb.brand_id
             from soc_submkt_relation ssr    
                  ,soc_discount_plan sdp
                  ,soc_discount_market sdm 
                  ,market m 
                  ,discount_plan dp 
                  ,logical_date  ld 
                  ,(select disc_plan_cd from discount_category 
                    where group_code='PP') pp_disc 
                  ,discount_plan_brands dpb 
             where rtrim(ssr.soc) = v_param1 
             and (( ssr.sub_market=m.market_code 
                   and  m.province= v_param2 
                   or ssr.sub_market='ALL') 
                   and v_param3 ='N' 
                   or v_param3 ='Y')
             and   sdp.soc =  ssr.soc
             and  ( sdp.product_type_id IN (SELECT * FROM TABLE (CAST (v_param5 AS t_product_promo_type)))
             	and  ( sdp.initial_activation_ind = v_param6 or sdp.initial_activation_ind ='A')
             	and   v_param7 ='N' 
                or    v_param7 ='Y' ) 
             and ( trunc(sdp.effective_date)<=trunc(ld.logical_date) 
                  and  (trunc(sdp.expiry_date)>trunc(ld.logical_date) 
                  or   sdp.expiry_date is null) 
                  and v_param8 ='Y' 
                  or  v_param8 ='N' ) 
             and  sdp.discount_plan_cd=dp.discount_plan_cd 
             and ( trunc(dp.avail_eff_date)<=trunc(ld.logical_date) 
                  and  (trunc(dp.avail_exp_date)>trunc(ld.logical_date) 
                  or   dp.avail_exp_date is null) 
                  and v_param8 ='Y' 
                  or  v_param8 ='N' ) 
             and  sdm.soc_discount_plan_id=sdp.soc_discount_plan_id 
             and (( sdm.sub_market_code=m.market_code 
                  and  m.province= v_param2 
                  or sdm.sub_market_code='ALL') 
                  and (  trunc(sdm.effective_date)<=trunc(ld.logical_date) 
                  and  (trunc(sdm.expiry_date)>trunc(ld.logical_date) 
                  or   sdm.expiry_date is null) 
                  and v_param8 ='Y' 
                  or  v_param8 ='N' ) 
                  and v_param3 ='N' 
                  or v_param3 ='Y') 
             and  ld.logical_date_type='O' 
             and pp_disc.disc_plan_cd(+) =sdp.discount_plan_cd 
             and dp.discount_plan_cd = dpb.discount_plan_cd(+)  ;        
         END IF;
 
			
         i_result := numeric_true;
      EXCEPTION
         WHEN NO_DATA_FOUND
         THEN
            v_error_message := err_no_data_found;
            i_result := numeric_false;

            IF (po_discounts%ISOPEN)
            THEN
               CLOSE po_discounts;
            END IF;
         WHEN OTHERS
         THEN
            v_error_message := err_other;
            i_result := numeric_false;
            IF (po_discounts%ISOPEN)
            THEN
               CLOSE po_discounts;
            END IF;

            raise_application_error
                     (-20102,
                         'RetrievePromoDiscountPlans Query Failed. Oracle:(['
                      || SQLCODE
                      || '] ['
                      || SQLERRM
                      || '])');
      END;  
      RETURN i_result;			  
   END RetrievePromoDiscountPlans;

 FUNCTION RetrievePromoDiscountPlansEnha(
      v_soc                   IN       VARCHAR,
      v_province            IN       VARCHAR,
      v_ignore_province       IN       VARCHAR,
      v_term_months     IN       VARCHAR,
      v_product_type     IN       t_product_promo_type,
      v_initial_activation IN       VARCHAR,
      v_prod_init_act     IN       VARCHAR,
      v_expiry_date       IN       VARCHAR,
      po_discounts        OUT     refcursor,
      v_error_message  OUT   VARCHAR2
   )
      RETURN NUMBER
   IS
      i_result                NUMBER (1);

   BEGIN
      BEGIN
         IF v_term_months IS NOT NULL AND LENGTH (RTRIM (LTRIM (v_term_months))) > 0
         THEN
         OPEN po_discounts
          FOR SELECT DISTINCT sdp.discount_plan_cd
                           ,dp.discount_desc
                           ,dp.discount_desc_f
                           ,dp.avail_eff_date
                           ,dp.avail_exp_date
                           ,dp.flat_amt
                           ,dp.disc_qty_type
                           ,nvl(dp.default_num_month,0)
                           ,sdp.offer_end_date
                           ,nvl(pp_disc.disc_plan_cd,'N')
                           ,product_type
                           ,discount_plan_level
                           ,dpb.brand_id
             from soc_submkt_relation ssr
                  ,soc_discount_plan sdp
                  ,soc_discount_market sdm
                  ,market m
                  ,discount_plan dp
                  ,logical_date  ld
                  ,(select disc_plan_cd from discount_category
                    where group_code='PP') pp_disc
                  ,discount_plan_brands dpb
             where rtrim(ssr.soc) = v_soc
             and (( ssr.sub_market=m.market_code
                   and  m.province= v_province
                   or ssr.sub_market='ALL')
                   and v_ignore_province ='N'
                   or v_ignore_province ='Y')
             and   sdp.soc =  ssr.soc
             and sdp.term_months = v_term_months
             and  ( sdp.product_type_id IN (SELECT * FROM TABLE (CAST (v_product_type AS t_product_promo_type)))
                 and  ( sdp.initial_activation_ind = v_initial_activation or sdp.initial_activation_ind ='A')
                 and   v_prod_init_act ='N'
                or    v_prod_init_act ='Y' )
             and ( trunc(sdp.effective_date)<=trunc(ld.logical_date)
                  and  (trunc(sdp.expiry_date)>trunc(ld.logical_date)
                  or   sdp.expiry_date is null)
                  and v_expiry_date ='Y'
                  or  v_expiry_date ='N' )
             and  sdp.discount_plan_cd=dp.discount_plan_cd
             and ( trunc(dp.avail_eff_date)<=trunc(ld.logical_date)
                  and  (trunc(dp.avail_exp_date)>trunc(ld.logical_date)
                  or   dp.avail_exp_date is null)
                  and v_expiry_date ='Y'
                  or v_expiry_date ='N' )
             and  sdm.soc_discount_plan_id=sdp.soc_discount_plan_id
             and (( sdm.sub_market_code=m.market_code
                  and  m.province= v_province
                  or sdm.sub_market_code='ALL')
                  and (  trunc(sdm.effective_date)<=trunc(ld.logical_date)
                  and  (trunc(sdm.expiry_date)>trunc(ld.logical_date)
                  or   sdm.expiry_date is null)
                  and v_expiry_date ='Y'
                  or  v_expiry_date ='N' )
                  and v_ignore_province ='N'
                  or v_ignore_province ='Y')
             and  ld.logical_date_type='O'
             and pp_disc.disc_plan_cd(+) =sdp.discount_plan_cd
             and dp.discount_plan_cd = dpb.discount_plan_cd(+)  ;
         ELSE
         OPEN po_discounts
          FOR SELECT DISTINCT sdp.discount_plan_cd
                           ,dp.discount_desc
                           ,dp.discount_desc_f
                           ,dp.avail_eff_date
                           ,dp.avail_exp_date
                           ,dp.flat_amt
                           ,dp.disc_qty_type
                           ,nvl(dp.default_num_month,0)
                           ,sdp.offer_end_date
                           ,nvl(pp_disc.disc_plan_cd,'N')
                           ,product_type
                           ,discount_plan_level
                           ,dpb.brand_id
             from soc_submkt_relation ssr
                  ,soc_discount_plan sdp
                  ,soc_discount_market sdm
                  ,market m
                  ,discount_plan dp
                  ,logical_date  ld
                  ,(select disc_plan_cd from discount_category
                    where group_code='PP') pp_disc
                  ,discount_plan_brands dpb
             where rtrim(ssr.soc) = v_soc
             and (( ssr.sub_market=m.market_code
                   and  m.province= v_province
                   or ssr.sub_market='ALL')
                   and v_ignore_province ='N'
                   or v_ignore_province ='Y')
             and   sdp.soc =  ssr.soc
             and  ( sdp.product_type_id IN (SELECT * FROM TABLE (CAST (v_product_type AS t_product_promo_type)))
                 and  ( sdp.initial_activation_ind = v_initial_activation or sdp.initial_activation_ind ='A')
                 and   v_prod_init_act ='N'
                or   v_prod_init_act ='Y' )
             and ( trunc(sdp.effective_date)<=trunc(ld.logical_date)
                  and  (trunc(sdp.expiry_date)>trunc(ld.logical_date)
                  or   sdp.expiry_date is null)
                  and v_expiry_date ='Y'
                  or v_expiry_date ='N' )
             and  sdp.discount_plan_cd=dp.discount_plan_cd
             and ( trunc(dp.avail_eff_date)<=trunc(ld.logical_date)
                  and  (trunc(dp.avail_exp_date)>trunc(ld.logical_date)
                  or   dp.avail_exp_date is null)
                  and v_expiry_date ='Y'
                  or  v_expiry_date ='N' )
             and  sdm.soc_discount_plan_id=sdp.soc_discount_plan_id
             and (( sdm.sub_market_code=m.market_code
                  and  m.province= v_province
                  or sdm.sub_market_code='ALL')
                  and (  trunc(sdm.effective_date)<=trunc(ld.logical_date)
                  and  (trunc(sdm.expiry_date)>trunc(ld.logical_date)
                  or   sdm.expiry_date is null)
                  and v_expiry_date ='Y'
                  or  v_expiry_date ='N' )
                  and v_ignore_province ='N'
                  or v_ignore_province ='Y')
             and  ld.logical_date_type='O'
             and pp_disc.disc_plan_cd(+) =sdp.discount_plan_cd
             and dp.discount_plan_cd = dpb.discount_plan_cd(+)  ;
         END IF;


         i_result := numeric_true;
      EXCEPTION
         WHEN NO_DATA_FOUND
         THEN
            v_error_message := err_no_data_found;
            i_result := numeric_false;

            IF (po_discounts%ISOPEN)
            THEN
               CLOSE po_discounts;
            END IF;
         WHEN OTHERS
         THEN
            v_error_message := err_other;
            i_result := numeric_false;
            IF (po_discounts%ISOPEN)
            THEN
               CLOSE po_discounts;
            END IF;

            raise_application_error
                     (-20102,
                         'RetrievePromoDiscountPlans Query Failed. Oracle:(['
                      || SQLCODE
                      || '] ['
                      || SQLERRM
                      || '])');
      END;
      RETURN I_RESULT;
   END RetrievePromoDiscountPlansEnha;
      
END;
/

