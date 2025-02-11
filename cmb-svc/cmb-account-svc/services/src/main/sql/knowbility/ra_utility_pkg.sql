/* Formatted on 2006/11/13 10:49 (Formatter Plus v4.8.0) */
CREATE OR REPLACE PACKAGE ra_utility_pkg
AS
-------------------------------------------------------------------------
-- IMPORTANT NOTE !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
-- Please increment the version_no variable value when you make changes to this package 
-- to reflect version changes.
--------------------------------------------------------------------------
-- description: Package Utility_pkg containing procedures
--    for data retrieval from Knowbility database
--
-- Date           Developer           Modifications
-- 07-04-2001     Ludmila Pomirche    created
-- 11-13-2001           Ludmila Pomirche          BAN Status Activity Reason Code added
--                  to retrieval
-- 11-06-2002     Ludmila Pomirche    Changes for 6.1 Mike
-- 06-04-2003           Ludmila Pomirche    Changes for KB 7.0 release
-- 07-11-2003     Ludmila Pomirche    Changes for Contract Renewal
-- 10-09-2003     Ludmila Pomirche    Changes to Subscriber Count Logic
-- 10-30-2003     Ludmila Pomirche    Bank Info retrieval added
-- 11-03-2003     Ludmila Pomirche    Add Other Phone retrieval
-- 04-20-2004     Ludmila Poirche        Collection buckets retrieval fix
-- 05-03-2004     Ludmila Pomirche    Additional billing Info for Smart 2.0
-- 07-02-2004     Ludmila Pomirche    Added write off indicator and Contact name and phone
-- 07-12-2004     Ludmila Pomirche    Additional Collection Info
-- 08-03-2004     Ludmila Pomirche    Additional billing Info for October release of  Smart
-- 09-17-2004     Ludmila Pomirche    Added retrieval of conv_run_no
-- 10-04-2004     Marina Kuper        Added retrieval of consent_inds
-- 11-04-2004     Ludmila Pomirche    Added retrieval of all subscriber counts
-- 12-03-2004     Ludmila Pomirche    Collection activities retrieval modification
-- 12-29-2004     Ludmila Pomirche    Reserved subscribers count modification
-- 02-15-2005  Marina  Kuper         Allowed the future expiration date for address_name_link
-- 03-30-2005  Vladimir Tsitrin      Added 5 output fields to GetAccountInfoByBAN
-- 05-31-2005  Vladimir Tsitrin      Added method GetAccountsByBANs
-- 02-15-2005   Marina  Kuper         Changed method GetAccountsByBANs - errors handling
-- 07-28-2005   Vladimir Tsitrin    Added method GetAccountsByName and updated GetAccountsByBANs
-- 09-13-2005  Marina  Kuper       Added method GetServiceSubscriberCounts
-- 09-27-2005  Marina  Kuper       Added query for payment refund
-- 11-15-2005  Richard Fong      Modified the GetAccountInfoByBan
-- 12-02-2005  Marina  Kuper       Added fix for payment refund
-- 28-03-2006  Marina  Kuper      Added procedure  GetPhoneNumbersByBan
-- 22-06-2006  Michael Qin   Added procudure  getCollectionHistory
-- 28-06-2006  Dimitry Siganevich      Added method GetMinutePoolingSubscribers
-- 18-08-2006  Marina Kuper  Fixed performance issue in GetAccountsByBANs
-- 19-08-2006  Marina Kuper  Fixed cursor issue
-- 28-09-2006  Marina Kuper  Fixed performance issue in  GetMinutePoolingSubscribers
-- 02-10-2006  Marina  Kuper Added procedure GetSharePricePlanSubscribers
-- 03-10-2006  Marina  Kuper Added procedure GetShareSOCSubscribers
-- 01-11-2006  Marina  Kuper Changed procedure GetSharePricePlanSubscribers
-- 03-11-2006  Michael Qin   Added function get_corporate_name
-- 09-11-2006  Marina  Kuper Changed procedure getservicesubscribercounts
-- 09-11-2006  Marina  Kuper Changed function getaccountsbyname
-- 13-11-2006  Marina  Kuper Changed procedure GetAccountInfoByBan - logical date
-- 13-11-2006  Marina  Kuper Changed function getminutepoolingsubscribers - logical date
-- Nov 21,2006 Tsz Chung Tong   Added brand indicator to GetAccountsByName, GetAccountsByBANs, GetAccountInfoByBAN
-- 11-12-2006  Marina  Kuper Fixed procedure GetSharePricePlanSubscribers
-- 22-03-2007  Marina  Kuper Added procedure isFeatureCategoryExistOnSubscribers
-- 26-06-2007  Marina  Kuper Changed procedure isFeatureCategoryExistOnSubscribers
-- 18-01-2008  R. Fong		 Changed function getminutepoolingsubscribers and added function getminutepoolingsubsbycoverage
-- 26-02-2008  A. Pereira Changed getaccountinfobyban function by removing conditional checks for tax exemption.
-- 28-04-2008  A. Pereira Re-added missing get_corporate_name function originally created by Michael Qin.
-- 22-07-2008  Tsz Chung Tong       Modified getaccountinfobyban and getaccountsbybans to retrieve GL_SEGMENT and GL_SUBSEGMENT columns
-- 30-07-2008  A. Pereira	Added function getpoolingsubscribercounts for CMP2 project (October 2008 release)
-- 08-08-2008  A. Pereira   Modified getpoolingsubscribercounts to properly retrieve counts for accounts with dollar-pooling subscribers
-- 19-08-2008  Tsz Chung Tong  Added getLastCreditCheckResultByBan to retrieve last credit check result of the account
-- 26-08-2008  A. Pereira   Modified strSQL variable in getpoolingsubscribercounts to be 32767 in size instead of 1000 (fix Db error reported) 
-- 28-08-2008  A. Pereira   Modified getpoolingsubscribercounts to use static SQL statements instead of a dynamic statement.
-- 03-08-2008  R. Fong  Added getzeropoolingsubscribercounts function.
-- 03-08-2008  A. Pereira split getpoolingsubscribercounts function into two new functions (getminutepoolingsubcounts and getdollarpoolingsubcounts). Also addressed missing optional services check within these functions.
-- 08-09-2008  A. Pereira modified getzeropoolingsubscribercounts function to address defect # 116811
-- 13-09-2008  R. Fong Modified getzeropoolingsubscribercounts function to fix defect PROD00117040
-- 03-10-2008  R. Fong Modified getzeropoolingsubscribercounts function to fix defect PROD00118143
-- Nov 7,2008  C. Tong Modified getLastCreditCheckResultByBan function to fix performance
-- Nov 12,2008 A. Pereira modified getzeropoolingsubscribercounts function to address performance problem
-- Dec 09, 2008 M.Liao  Modified getcollectionhistory to fix defect PROD00117959 (base on Amdocs' query): 
--                        1) remove user's expiration_date filter, 
--                        2) adjust the filter for WORK_POSITION_ASSIGNMENT.WPASN_EXPIRATION_DATE
--                        3) add filter COLLECTION_ACT.ACTUAL_ACT_IND = 'Y', 
-- Mar 11, 2009 Tsz Chung Tong   Modified getaccountinfobyban, removed following queries because they are unused in AccountJdbcDAO:
--                          1) c_sub_summary 
--                          2) c_sub_summary_rsrv 
--                          3) c_sub_summary_all
--                          4) c_sub_summary_rsrv_all
-- Mar 30, 2009, Michael Liao  Added function getBillingParamNoOfInvoice
-- Apr 01, 2009, Mujeeb Waraich Added new procedure getaccountbyimsi
-- Apr 29, 2009 Tsz Chung Tong  Added procedure getPCSNetworkCountByBan
-- May 01, 2009  Tsz Chung Tong  Changed getPCSNetworkCountByBAN implementation due to requirement change
-- May 05, 2009  Tsz Chung Tong Changed getBillingParamNoOfInvoice to retrieve BP_MEDIA_CATEGORY and BP_BILL_FORMAT
-- May 19, 2009  Tsz Chung Tong Changed getaccountsbybans to put a limit on the number of BANs to search for. 
-- Jun 17, 2009  Tsz Chung Tong Added getlwaccountinfobyban
-- Sep 24, 2009  Tsz Chung Tong Added getSubscriberCounts. Changed getlwaccountinfobyban and getaccountinfobyban 
--                              to call getSubscriberCounts and return the subscriber counts
-- April 30,2010 Michael Liao   PCI changes:
--                           1) Modified getAccountInfoByBan 
--                              1.1) return credit card first 6 and last 4 digits from table CUSTOMER and BAN_DIRECT_DEBIT
--                              1.2) refine the query for retrieving last payment informations 
--                           2) Modified getLastCreditCheckResultByBan to return credit card first 6 and last 4 digits from table CREDIT_HISTORY
-- June 24, 2010 Anitha Duraisamy Changed isFeatureCategoryExist for defect no PROD00172492
-- August 12, 2010 Anitha Durasiamy Changed for IVR CR- added two fields to getlwaccountinfobyban procedure
-- Aug 26, 2010 Michael Liao  Modified getAccountInfoByBan to return correct last payment information:
--                            Last payment is a payment with the most recent activity date and with first activity being PYM or FNTT
--                            The returning information for the payment, however, are orginal amount, deposit date,actual amount, and last activity code
-- Sep 29, 2011 Rad Andric    Adding new function to retrieve Subscribers for given account and Service/Plan Family Type
-- Nov 09, 2011 Brandon Wen	  Changed getlwaccountinfobyban to add out parameter po_home_province, retrieved from ba.home_province
-- Dec 05, 2011 Brandon Wen   Added additional clause in getSubscribersByServiceFamily and getSubscribersBySharingGroups for TBS Pricing as DBA feedback
-- Apr 18, 2012 Tsz Chung Tong Added getVersion function to return the version number of the package.
-- Apr 19, 2012 Tsz Chung Tong Added getAccountsByPhoneNumber and getOnlyLastAccountsByPhoneNumber procedures.
-- Apr 24, 2012 Tsz Chung Tong Added getBanByPhoneNumber function
-- Apr 26, 2012 Tsz Chung Tong Added getAccountsByPostalCode procedure
-- 3.19.4 -  Apr 26, 2012 Michael Liao  Modified getlwaccountinfobyban, return follwoign information:
--                            bill_cycle, cycle_close_day, dealer_code, sales_rep_code
-- 3.23.1 -  Apr 22, 2013 Michael Liao  Modified getlwaccountinfobyban, return follwoign information:
--                            customer.email_address
-- 3.23.2 -  Apr 24, 2013 Michael Liao  Modified getlwaccountinfobyban, return follwoign information:
--                            customer.lang_pref
-- 3.28.1 - March 1, 2014 Naresh Annabathula Added getBanBySeatNumber function
-- 3.28.2 - April 10, 2014 Tsz Chung Tong Updated getBillingParamNoOfInvoice function (Defect 13000)
-- 3.28.3 -	May 24, 2014   added getLastAccountsBySeatNumber function 
-- 3.41.1 - August 11, 2015 Tsz Chung Tong added getSubscriberDataSharingInfo
--          August 13, 2015 Wilson Cheong updated getSubscriberDataSharingInfo (removed hints)
--			August 14, 2015 R. Fong updated getSubscriberDataSharingInfo
-- 3.41.2   August17, 2015 Updated version #
-------------------------------------------------------------------------------------------------------
   bannotfound                  EXCEPTION;
   PRAGMA EXCEPTION_INIT (bannotfound, -20101);
   
   multipleaccountfound           EXCEPTION;
   PRAGMA EXCEPTION_INIT (multipleaccountfound, -20102);

   TYPE refcursor IS REF CURSOR;

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
   brand_telus         CONSTANT NUMBER (1)     := 1;
   brand_ampd          CONSTANT NUMBER (1)     := 2;
   brand_all           CONSTANT NUMBER (3)     := 255;
   version_no          CONSTANT VARCHAR2(10)       := '3.41.2';
   

-------------------------------------------------------------------------
-- description: Procedure GetAccountInfoByBAN to get Account Information
--------------------------------------------------------------------------
   PROCEDURE getaccountinfobyban (
      pi_ban                         IN       NUMBER,
      po_ban_status                  OUT      CHAR,
      po_account_type                OUT      CHAR,
      po_account_sub_type            OUT      CHAR,
      po_create_date                 OUT      DATE,
      po_start_service_date          OUT      DATE,
      po_col_delinq_status           OUT      VARCHAR2,
      po_ar_balance                  OUT      NUMBER,
      po_dealer_code                 OUT      VARCHAR2,
      po_sales_rep_code              OUT      VARCHAR2,
      po_bill_cycle                  OUT      NUMBER,
      po_payment_method              OUT      VARCHAR2,
      po_work_telno                  OUT      VARCHAR2,
      po_work_tn_extno               OUT      VARCHAR2,
      po_home_telno                  OUT      VARCHAR2,
      po_birth_date                  OUT      DATE,
      po_contact_faxno               OUT      VARCHAR2,
      po_acc_password                OUT      VARCHAR2,
      po_customer_ssn                OUT      VARCHAR2,
      po_adr_primary_ln              OUT      VARCHAR2,
      po_adr_city                    OUT      VARCHAR2,
      po_adr_province                OUT      VARCHAR2,
      po_adr_postal                  OUT      VARCHAR2,
      po_first_name                  OUT      VARCHAR2,
      po_last_business_name          OUT      VARCHAR2,
      po_special_instruction         OUT      VARCHAR2,
      po_cu_age_bucket_0             OUT      NUMBER,
      po_cu_age_bucket_1_30          OUT      NUMBER,
      po_cu_age_bucket_31_60         OUT      NUMBER,
      po_cu_age_bucket_61_90         OUT      NUMBER,
      po_cu_age_bucket_91_plus       OUT      NUMBER,
      po_cu_past_due_amt             OUT      NUMBER,
      po_active_subs                 OUT      NUMBER,
      po_reserved_subs               OUT      NUMBER,
      po_suspended_subs              OUT      NUMBER,
      po_cancelled_subs              OUT      NUMBER,
      po_col_actv_jan                OUT      CHAR,
      po_col_actv_feb                OUT      CHAR,
      po_col_actv_mar                OUT      CHAR,
      po_col_actv_apr                OUT      CHAR,
      po_col_actv_may                OUT      CHAR,
      po_col_actv_jun                OUT      CHAR,
      po_col_actv_jul                OUT      CHAR,
      po_col_actv_aug                OUT      CHAR,
      po_col_actv_sep                OUT      CHAR,
      po_col_actv_oct                OUT      CHAR,
      po_col_actv_nov                OUT      CHAR,
      po_col_actv_dec                OUT      CHAR,
      po_dck_jan                     OUT      NUMBER,
      po_dck_feb                     OUT      NUMBER,
      po_dck_mar                     OUT      NUMBER,
      po_dck_apr                     OUT      NUMBER,
      po_dck_may                     OUT      NUMBER,
      po_dck_jun                     OUT      NUMBER,
      po_dck_jul                     OUT      NUMBER,
      po_dck_aug                     OUT      NUMBER,
      po_dck_sep                     OUT      NUMBER,
      po_dck_oct                     OUT      NUMBER,
      po_dck_nov                     OUT      NUMBER,
      po_dck_dec                     OUT      NUMBER,
      po_last_payment_amnt           OUT      NUMBER,
      po_last_payment_date           OUT      DATE,
      po_hotline_ind                 OUT      CHAR,
      po_customer_id                 OUT      NUMBER,
      po_language                    OUT      VARCHAR2,
      po_email                       OUT      VARCHAR2,
      po_adr_type                    OUT      VARCHAR2,
      po_adr_secondary_ln            OUT      VARCHAR2,
      po_adr_country                 OUT      VARCHAR2,
      po_adr_zip_geo_code            OUT      VARCHAR2,
      po_adr_state_code              OUT      VARCHAR2,
      po_civic_no                    OUT      VARCHAR2,
      po_civic_no_suffix             OUT      VARCHAR2,
      po_adr_st_direction            OUT      VARCHAR2,
      po_adr_street_name             OUT      VARCHAR2,
      po_adr_street_type             OUT      VARCHAR2,
      po_adr_designator              OUT      VARCHAR2,
      po_adr_identifier              OUT      VARCHAR2,
      po_adr_box                     OUT      VARCHAR2,
      po_unit_designator             OUT      VARCHAR2,
      po_unit_identifier             OUT      VARCHAR2,
      po_adr_area_nm                 OUT      VARCHAR2,
      po_adr_qualifier               OUT      VARCHAR2,
      po_adr_site                    OUT      VARCHAR2,
      po_adr_compartment             OUT      VARCHAR2,
      po_middle_initial              OUT      VARCHAR2,
      po_name_title                  OUT      VARCHAR2,
      po_additional_title            OUT      VARCHAR2,
      po_contact_last_name           OUT      VARCHAR2,
      po_drivr_licns_no              OUT      VARCHAR2,
      po_drivr_licns_state           OUT      VARCHAR2,
      po_drivr_licns_exp_dt          OUT      DATE,
      po_incorporation_no            OUT      VARCHAR2,
      po_incorporation_date          OUT      DATE,
      po_gur_cr_card_no              OUT      VARCHAR2,
      po_gur_cr_card_exp_dt_mm       OUT      NUMBER,
      po_gur_cr_card_exp_dt_yyyy     OUT      NUMBER,
      po_credit_card_no              OUT      VARCHAR2,
      po_card_mem_hold_nm            OUT      VARCHAR2,
      po_expiration_date_mm          OUT      NUMBER,
      po_expiration_date_yyyy        OUT      NUMBER,
      po_status_actv_code            OUT      VARCHAR2,
      po_status_actv_rsn_code        OUT      VARCHAR2,
      po_bill_cycle_close_day        OUT      NUMBER,
      po_return_envelope_ind         OUT      VARCHAR2,
      po_bill_due_date               OUT      DATE,
      po_corp_hierarhy_ind           OUT      VARCHAR2,
      po_corp_csr_id                 OUT      VARCHAR2,
      po_inv_supression_ind          OUT      VARCHAR2,
      po_bankcode                    OUT      VARCHAR2,
      po_bankaccountnumber           OUT      VARCHAR2,
      po_bankbranchnumber            OUT      VARCHAR2,
      po_bankaccounttype             OUT      VARCHAR2,
      po_directdebitstatus           OUT      VARCHAR2,
      po_directdebitstatusrsn        OUT      VARCHAR2,
      po_other_phone                 OUT      VARCHAR2,
      po_other_phone_ext             OUT      VARCHAR2,
      po_other_phone_type            OUT      VARCHAR2,
      po_tax_gst_exmp_ind            OUT      VARCHAR2,
      po_tax_pst_exmp_ind            OUT      VARCHAR2,
      po_tax_hst_exmp_ind            OUT      VARCHAR2,
      po_tax_gst_exmp_exp_dt         OUT      DATE,
      po_tax_pst_exmp_exp_dt         OUT      DATE,
      po_tax_hst_exmp_exp_dt         OUT      DATE,
      po_home_province               OUT      VARCHAR2,
      po_category                    OUT      VARCHAR2,
      po_next_bill_cycle             OUT      NUMBER,
      po_next_bill_cycle_close_day   OUT      NUMBER,
      po_verified_date               OUT      DATE,
      po_handle_by_subscriber_ind    OUT      VARCHAR2,
      po_corporate_id                OUT      VARCHAR2,
      po_write_off_ind               OUT      VARCHAR2,
      po_contact_first_name          OUT      VARCHAR2,
      po_contact_name_title          OUT      VARCHAR2,
      po_contact_middle_initial      OUT      VARCHAR2,
      po_contact_additional_title    OUT      VARCHAR2,
      po_contact_name_suffix         OUT      VARCHAR2,
      po_contact_phone_number        OUT      VARCHAR2,
      po_contact_phone_number_ext    OUT      VARCHAR2,
      po_legal_business_name         OUT      VARCHAR2,
      po_col_path_code               OUT      VARCHAR2,
      po_col_step                    OUT      NUMBER,
      po_col_actv_code               OUT      VARCHAR2,
      po_col_actv_date               OUT      DATE,
      po_col_next_step               OUT      NUMBER,
      po_col_next_actv_date          OUT      DATE,
      po_col_agency                  OUT      VARCHAR2,
      po_adr_attention               OUT      VARCHAR2,
      po_adr_delivery_type           OUT      VARCHAR2,
      po_adr_group                   OUT      VARCHAR2,
      po_tax_gst_exmp_rf_no          OUT      VARCHAR2,
      po_tax_pst_exmp_rf_no          OUT      VARCHAR2,
      po_tax_hst_exmp_rf_no          OUT      VARCHAR2,
      po_tax_gst_exmp_eff_dt         OUT      DATE,
      po_tax_pst_exmp_eff_dt         OUT      DATE,
      po_tax_hst_exmp_eff_dt         OUT      DATE,
      po_status_last_date            OUT      DATE,
      po_conv_run_no                 OUT      NUMBER,
      po_client_cons_inds            OUT      VARCHAR2,
      po_all_active_subs             OUT      NUMBER,
      po_all_reserved_subs           OUT      NUMBER,
      po_all_suspended_subs          OUT      NUMBER,
      po_all_cancelled_subs          OUT      NUMBER,
      po_bl_man_hndl_req_opid        OUT      NUMBER,
      po_bl_man_hndl_eff_date        OUT      DATE,
      po_bl_man_hndl_exp_date        OUT      DATE,
      po_bl_man_hndl_rsn             OUT      VARCHAR2,
      po_bl_man_hndl_by_opid         OUT      NUMBER,
      po_last_payment_actual_amt     OUT      NUMBER,
      po_name_suffix                 OUT      VARCHAR2,
      po_last_act_hotline            OUT      DATE,
      po_col_delinq_sts_date         OUT      DATE,
      po_col_written_off_date        OUT      DATE,
 			po_brand_ind                   OUT      NUMBER,
 			po_gl_segment                  OUT      VARCHAR2,
 			po_gl_subsegment               OUT      VARCHAR2,
  	  po_gur_cr_card_first6          OUT      VARCHAR2,
  	  po_gur_cr_card_last4           OUT      VARCHAR2,
  	  po_payment_card_first6         OUT      VARCHAR2,
  	  po_payment_card_last4          OUT      VARCHAR2,
  	  po_last_payment_actv_code      OUT      VARCHAR2
   );

-----------------------------------------------------------------------------------------
-- description: Procedure GetAccountsByBANs returns a cursor of AccountSummary
--              for an array of BANs.
-----------------------------------------------------------------------------------------
   FUNCTION getaccountsbybans (
      i_ban_numbers     IN       t_ban_array,
      o_accounts        OUT      refcursor,
      v_error_message   OUT      VARCHAR2
   )
      RETURN NUMBER;

-----------------------------------------------------------------------------------------
-- description: Procedure GetAccountsByName returns a cursor of AccountSummary
--              for given search parameters. Set v_First_Name = '' to search
--              by Business Name
-----------------------------------------------------------------------------------------
   FUNCTION getaccountsbyname (
      c_name_type            IN       VARCHAR2,
      v_control_name         IN       VARCHAR2,
      i_control_name_exact   IN       NUMBER,
      v_first_name           IN       VARCHAR2,
      i_first_name_exact     IN       NUMBER,
      c_account_status       IN       VARCHAR2,
      c_account_type         IN       VARCHAR2,
      v_province             IN       VARCHAR2,
      i_maximum              IN       NUMBER,
      i_brand_ind            IN       NUMBER,
      o_accounts             OUT      refcursor,
      i_has_more             OUT      NUMBER,
      v_error_message        OUT      VARCHAR2
   ) RETURN NUMBER;

-----------------------------------------------------------------------------------------
-- description: Procedure GetServiceSubscriberCount returns a cursor of Subscribers
--              for an array of ServiceCodes.
-----------------------------------------------------------------------------------------
   FUNCTION getservicesubscribercounts (
      pi_service_codes     IN       t_service_codes,
      pi_include_expired   IN       NUMBER,
      pi_ban               IN       NUMBER,
      po_subscribers       OUT      refcursor,
      v_error_message      OUT      VARCHAR2
   ) RETURN NUMBER;

-----------------------------------------------------------------------------------------
-- description: Procedure GetPhoneNumbersByBan returns a cursor of r subscribers
-- for the selected Mike Account
-----------------------------------------------------------------------------------------
   PROCEDURE getphonenumbersbyban (
      pi_ban             IN       NUMBER,
      po_phone_numbers   OUT      refcursor
   );

   PROCEDURE getcollectionhistory (
      banid      IN       NUMBER,
      fromdate   IN       DATE,
      todate     IN       DATE,
      results    OUT      refcursor
   );

-----------------------------------------------------------------------------------------
-- description: Function getminutepoolingsubscribers returns a cursor of subscribers
--              with price plans where SOC.MP_IND = 'Y' (minute pooling capable 
--				indicatior), FEATURE.FEATURE_TYPE = 'P' (minute pooling
--				feature type).
-----------------------------------------------------------------------------------------
   FUNCTION getminutepoolingsubscribers (
      pi_ban            IN       NUMBER,
      po_subscribers    OUT      refcursor,
      v_error_message   OUT      VARCHAR2
   ) RETURN NUMBER;
      
-----------------------------------------------------------------------------------------
-- description: Function getminutepoolingsubsbycoverage returns a cursor of 
--				subscribers with price plans where SOC.MP_IND = 'Y' (minute pooling
--				capable indicatior), FEATURE.FEATURE_TYPE = 'P' (minute pooling
--				feature type) and POOLING_GROUPS.COVERAGE_TYPE (minute pooling coverage
--				type) in the supplied array of coverage types.
-----------------------------------------------------------------------------------------
   FUNCTION getminutepoolingsubsbycoverage (
      pi_ban             IN       NUMBER,
      pi_coverage_types  IN       t_parameter_name_array,
      po_subscribers     OUT      refcursor,
      v_error_message    OUT      VARCHAR2
   ) RETURN NUMBER;
      
   PROCEDURE getsharepriceplansubscribers (
      pi_ban           IN       NUMBER,
      po_subscribers   OUT      refcursor
   );

   PROCEDURE getshareablesocsubscribers (
      pi_ban               IN       NUMBER,
      pi_price_plan_code   IN       VARCHAR2,
      po_subscribers       OUT      refcursor
   );

  PROCEDURE isFeatureCategoryExist (
      pi_ban               IN       NUMBER,
      pi_category_code     IN       VARCHAR2,
      po_category_exist    OUT      NUMBER
   );
      
   PROCEDURE getlogicaldate (po_logical_date OUT DATE);    
      
   FUNCTION get_corporate_name (corp_id in number) return varchar2;

   FUNCTION getminutepoolingsubcounts (
	  pi_ban             	IN       NUMBER,
	  pi_pool_group_id		IN		 NUMBER,
      po_pooling_counts 	OUT      refcursor,
      v_error_message       OUT      VARCHAR2
   ) RETURN NUMBER;

   FUNCTION getdollarpoolingsubcounts (
	  pi_ban             	IN       NUMBER,
	  pi_product_type 		IN		 VARCHAR2,
      po_pooling_counts 	OUT      refcursor,
      v_error_message       OUT      VARCHAR2
   ) RETURN NUMBER;

		FUNCTION getLastCreditCheckResultByBan (
		pi_ban         							IN  	NUMBER,
		pi_product_type 						IN 		CHAR,
		po_credit_class 						OUT		VARCHAR2,
		po_credit_limit 						OUT		NUMBER,
		po_deposit_amt  						OUT		NUMBER,
		po_credit_result2 					OUT		VARCHAR2,
		po_beacon_score 						OUT		NUMBER,
		po_credit_referral_flag 		OUT		VARCHAR2,
		po_french_message 					OUT		VARCHAR2,
		po_credit_req_sts 					OUT		CHAR,
		po_sin  										OUT		VARCHAR2,
		po_drivr_licns_no 					OUT		VARCHAR2,
		po_date_of_birth  					OUT		DATE,
		po_credit_card_no  					OUT		VARCHAR2,
		po_crd_card_exp_date 				OUT		DATE,
		po_incorporation_no  				OUT		VARCHAR2,
		po_incorporation_date 			OUT		DATE,
		po_credit_date  						OUT		DATE,
		po_credit_param_type 				OUT		CHAR,
		po_dep_chg_rsn_cd  					OUT		VARCHAR2,
		po_selected_market_account 	OUT		NUMBER,
		po_selected_company_name 		OUT		VARCHAR2,
		po_credit_card_first6               OUT VARCHAR2,
		po_credit_card_last4                OUT VARCHAR2
		) RETURN NUMBER;

   FUNCTION getzeropoolingsubscribercounts (
      pi_ban             	IN       NUMBER,
	  pi_pool_group_id		IN		 NUMBER,
      po_pooling_counts 	OUT      refcursor,
      v_error_message       OUT      VARCHAR2
   ) RETURN NUMBER;

	PROCEDURE getBillingParamNoOfInvoice (
		banId in number,
		po_noOfInvoice OUT NUMBER,
		po_media_category OUT VARCHAR2,
		po_bill_format    OUT VARCHAR2
		);
  
  PROCEDURE getaccountbyimsi(pi_imsi_id IN VARCHAR2, po_account_id OUT NUMBER);
  
  PROCEDURE getPCSNetworkCountByBAN (
   	pi_ban IN  NUMBER,
   	po_counts OUT REFCURSOR);
   	
	PROCEDURE getlwaccountinfobyban (
		pi_ban                         IN       NUMBER,
		po_account_type                OUT      CHAR,
		po_account_sub_type            OUT      CHAR,
		po_gl_segment                  OUT      VARCHAR2,
		po_gl_subsegment               OUT      VARCHAR2,
		po_brand_ind                   OUT      NUMBER,
		po_col_path_code               OUT      VARCHAR2,
		po_ar_balance                  OUT      NUMBER,
		po_cu_age_bucket_0             OUT      NUMBER,
		po_cu_past_due_amt             OUT      NUMBER,
		po_active_subs                 OUT      NUMBER,
		po_reserved_subs               OUT      NUMBER,
		po_suspended_subs              OUT      NUMBER,
		po_cancelled_subs              OUT      NUMBER,
		po_active_subs_all             OUT      NUMBER,
		po_reserved_subs_all           OUT      NUMBER,
		po_suspended_subs_all          OUT      NUMBER,
		po_cancelled_subs_all          OUT      NUMBER,
		po_status_actv_rsn_code		   OUT	    VARCHAR2,
		po_ban_status				   OUT	    CHAR,
		po_home_province			   OUT	    VARCHAR2,
		po_bill_cycle                  OUT      NUMBER,
		po_cycle_close_day             OUT      NUMBER,
		po_dealer_code                 OUT      VARCHAR2,
		po_sales_rep_code              OUT      VARCHAR2,
		po_email_address               OUT      VARCHAR2,
		po_lang_pref                   OUT      VARCHAR2
	);
	
	PROCEDURE getSubscriberCounts(
		pi_ban							 					 IN				NUMBER,
		p_account_type  		 					 IN				VARCHAR2,
    p_account_sub_type   					 IN				VARCHAR2,
		po_active_subs                 OUT      NUMBER,
		po_reserved_subs               OUT      NUMBER,
		po_suspended_subs              OUT      NUMBER,
		po_cancelled_subs              OUT      NUMBER,
		po_active_subs_all             OUT      NUMBER,
		po_reserved_subs_all           OUT      NUMBER,
		po_suspended_subs_all          OUT      NUMBER,
		po_cancelled_subs_all          OUT      NUMBER
	 );
	 
-----------------------------------------------------------------------------------------
-- description: Function getSubscribersByServiceFamily returns a cursor of 
--				subscriber IDs which have service or price plan that is configured with 
--				given family type in SOC_FAMILY_GROUP table
-----------------------------------------------------------------------------------------	 
	FUNCTION getSubscribersByServiceFamily(
		pi_ban		  IN			NUMBER,
		pi_family_type    IN			VARCHAR2,
		pi_effective_date IN			DATE,
		po_subscribers    OUT      		refcursor,
		v_error_message   OUT      		VARCHAR2
	 ) RETURN NUMBER;	
	 	 
-----------------------------------------------------------------------------------------
-- description: Function getSubscribersBySharingGroups returns a cursor of 
--				subscriber data sharing information for given data sharing group list
-----------------------------------------------------------------------------------------	 
	FUNCTION getSubscribersBySharingGroups(
		pi_ban		  		IN		NUMBER,
		pi_data_sharing_group_codes 	IN       	t_parameter_name_array,
		pi_effective_date 		IN		DATE,
		po_subscribers    		OUT      	refcursor,
		v_error_message   		OUT    		VARCHAR2
	 ) RETURN NUMBER;
	 
	 FUNCTION getSubscriberDataSharingInfo(
		pi_ban		  					IN		NUMBER,
		po_sub_data_sharing_info	  	OUT     refcursor,
		v_error_message   				OUT    	VARCHAR2
	 ) RETURN NUMBER;
	 
	 FUNCTION getVersion RETURN VARCHAR2; 
	 
	 PROCEDURE getAccountsByPhoneNumber (
	 	pi_phoneNumber	IN 		VARCHAR2,
	 	po_accounts		OUT		refcursor
	 );
	 
	  PROCEDURE getLastAccountsBySeatNumber (
	 	pi_seatNumber	IN 		VARCHAR2,
	 	po_accounts		OUT		refcursor
	 );
	 
	 PROCEDURE getLastAccountsByPhoneNumber (
	 	pi_phoneNumber	IN 		VARCHAR2,
	 	po_accounts	OUT		refcursor
	 );
	 
	 FUNCTION getBanByPhoneNumber (
	 	pi_phoneNumber	IN 		VARCHAR2
	 ) RETURN NUMBER;

	 PROCEDURE getAccountsByPostalCode (
	 	pi_lastName 	IN		VARCHAR2,
	 	pi_postalCode	IN		VARCHAR2,
	 	pi_maximum		IN		NUMBER,
	 	po_curAccounts	OUT		refcursor
	 );


 FUNCTION getBanBySeatNumber (
	 	pi_seatNumber	IN 	VARCHAR2
	 ) RETURN NUMBER;
	 
END;
/

SHO err

CREATE OR REPLACE PACKAGE BODY ra_utility_pkg
AS
   PROCEDURE getaccountinfobyban (
      pi_ban                         IN       NUMBER,
      po_ban_status                  OUT      CHAR,
      po_account_type                OUT      CHAR,
      po_account_sub_type            OUT      CHAR,
      po_create_date                 OUT      DATE,
      po_start_service_date          OUT      DATE,
      po_col_delinq_status           OUT      VARCHAR2,
      po_ar_balance                  OUT      NUMBER,
      po_dealer_code                 OUT      VARCHAR2,
      po_sales_rep_code              OUT      VARCHAR2,
      po_bill_cycle                  OUT      NUMBER,
      po_payment_method              OUT      VARCHAR2,
      po_work_telno                  OUT      VARCHAR2,
      po_work_tn_extno               OUT      VARCHAR2,
      po_home_telno                  OUT      VARCHAR2,
      po_birth_date                  OUT      DATE,
      po_contact_faxno               OUT      VARCHAR2,
      po_acc_password                OUT      VARCHAR2,
      po_customer_ssn                OUT      VARCHAR2,
      po_adr_primary_ln              OUT      VARCHAR2,
      po_adr_city                    OUT      VARCHAR2,
      po_adr_province                OUT      VARCHAR2,
      po_adr_postal                  OUT      VARCHAR2,
      po_first_name                  OUT      VARCHAR2,
      po_last_business_name          OUT      VARCHAR2,
      po_special_instruction         OUT      VARCHAR2,
      po_cu_age_bucket_0             OUT      NUMBER,
      po_cu_age_bucket_1_30          OUT      NUMBER,
      po_cu_age_bucket_31_60         OUT      NUMBER,
      po_cu_age_bucket_61_90         OUT      NUMBER,
      po_cu_age_bucket_91_plus       OUT      NUMBER,
      po_cu_past_due_amt             OUT      NUMBER,
      po_active_subs                 OUT      NUMBER,
      po_reserved_subs               OUT      NUMBER,
      po_suspended_subs              OUT      NUMBER,
      po_cancelled_subs              OUT      NUMBER,
      po_col_actv_jan                OUT      CHAR,
      po_col_actv_feb                OUT      CHAR,
      po_col_actv_mar                OUT      CHAR,
      po_col_actv_apr                OUT      CHAR,
      po_col_actv_may                OUT      CHAR,
      po_col_actv_jun                OUT      CHAR,
      po_col_actv_jul                OUT      CHAR,
      po_col_actv_aug                OUT      CHAR,
      po_col_actv_sep                OUT      CHAR,
      po_col_actv_oct                OUT      CHAR,
      po_col_actv_nov                OUT      CHAR,
      po_col_actv_dec                OUT      CHAR,
      po_dck_jan                     OUT      NUMBER,
      po_dck_feb                     OUT      NUMBER,
      po_dck_mar                     OUT      NUMBER,
      po_dck_apr                     OUT      NUMBER,
      po_dck_may                     OUT      NUMBER,
      po_dck_jun                     OUT      NUMBER,
      po_dck_jul                     OUT      NUMBER,
      po_dck_aug                     OUT      NUMBER,
      po_dck_sep                     OUT      NUMBER,
      po_dck_oct                     OUT      NUMBER,
      po_dck_nov                     OUT      NUMBER,
      po_dck_dec                     OUT      NUMBER,
      po_last_payment_amnt           OUT      NUMBER,
      po_last_payment_date           OUT      DATE,
      po_hotline_ind                 OUT      CHAR,
      po_customer_id                 OUT      NUMBER,
      po_language                    OUT      VARCHAR2,
      po_email                       OUT      VARCHAR2,
      po_adr_type                    OUT      VARCHAR2,
      po_adr_secondary_ln            OUT      VARCHAR2,
      po_adr_country                 OUT      VARCHAR2,
      po_adr_zip_geo_code            OUT      VARCHAR2,
      po_adr_state_code              OUT      VARCHAR2,
      po_civic_no                    OUT      VARCHAR2,
      po_civic_no_suffix             OUT      VARCHAR2,
      po_adr_st_direction            OUT      VARCHAR2,
      po_adr_street_name             OUT      VARCHAR2,
      po_adr_street_type             OUT      VARCHAR2,
      po_adr_designator              OUT      VARCHAR2,
      po_adr_identifier              OUT      VARCHAR2,
      po_adr_box                     OUT      VARCHAR2,
      po_unit_designator             OUT      VARCHAR2,
      po_unit_identifier             OUT      VARCHAR2,
      po_adr_area_nm                 OUT      VARCHAR2,
      po_adr_qualifier               OUT      VARCHAR2,
      po_adr_site                    OUT      VARCHAR2,
      po_adr_compartment             OUT      VARCHAR2,
      po_middle_initial              OUT      VARCHAR2,
      po_name_title                  OUT      VARCHAR2,
      po_additional_title            OUT      VARCHAR2,
      po_contact_last_name           OUT      VARCHAR2,
      po_drivr_licns_no              OUT      VARCHAR2,
      po_drivr_licns_state           OUT      VARCHAR2,
      po_drivr_licns_exp_dt          OUT      DATE,
      po_incorporation_no            OUT      VARCHAR2,
      po_incorporation_date          OUT      DATE,
      po_gur_cr_card_no              OUT      VARCHAR2,
      po_gur_cr_card_exp_dt_mm       OUT      NUMBER,
      po_gur_cr_card_exp_dt_yyyy     OUT      NUMBER,
      po_credit_card_no              OUT      VARCHAR2,
      po_card_mem_hold_nm            OUT      VARCHAR2,
      po_expiration_date_mm          OUT      NUMBER,
      po_expiration_date_yyyy        OUT      NUMBER,
      po_status_actv_code            OUT      VARCHAR2,
      po_status_actv_rsn_code        OUT      VARCHAR2,
      po_bill_cycle_close_day        OUT      NUMBER,
      po_return_envelope_ind         OUT      VARCHAR2,
      po_bill_due_date               OUT      DATE,
      po_corp_hierarhy_ind           OUT      VARCHAR2,
      po_corp_csr_id                 OUT      VARCHAR2,
      po_inv_supression_ind          OUT      VARCHAR2,
      po_bankcode                    OUT      VARCHAR2,
      po_bankaccountnumber           OUT      VARCHAR2,
      po_bankbranchnumber            OUT      VARCHAR2,
      po_bankaccounttype             OUT      VARCHAR2,
      po_directdebitstatus           OUT      VARCHAR2,
      po_directdebitstatusrsn        OUT      VARCHAR2,
      po_other_phone                 OUT      VARCHAR2,
      po_other_phone_ext             OUT      VARCHAR2,
      po_other_phone_type            OUT      VARCHAR2,
      po_tax_gst_exmp_ind            OUT      VARCHAR2,
      po_tax_pst_exmp_ind            OUT      VARCHAR2,
      po_tax_hst_exmp_ind            OUT      VARCHAR2,
      po_tax_gst_exmp_exp_dt         OUT      DATE,
      po_tax_pst_exmp_exp_dt         OUT      DATE,
      po_tax_hst_exmp_exp_dt         OUT      DATE,
      po_home_province               OUT      VARCHAR2,
      po_category                    OUT      VARCHAR2,
      po_next_bill_cycle             OUT      NUMBER,
      po_next_bill_cycle_close_day   OUT      NUMBER,
      po_verified_date               OUT      DATE,
      po_handle_by_subscriber_ind    OUT      VARCHAR2,
      po_corporate_id                OUT      VARCHAR2,
      po_write_off_ind               OUT      VARCHAR2,
      po_contact_first_name          OUT      VARCHAR2,
      po_contact_name_title          OUT      VARCHAR2,
      po_contact_middle_initial      OUT      VARCHAR2,
      po_contact_additional_title    OUT      VARCHAR2,
      po_contact_name_suffix         OUT      VARCHAR2,
      po_contact_phone_number        OUT      VARCHAR2,
      po_contact_phone_number_ext    OUT      VARCHAR2,
      po_legal_business_name         OUT      VARCHAR2,
      po_col_path_code               OUT      VARCHAR2,
      po_col_step                    OUT      NUMBER,
      po_col_actv_code               OUT      VARCHAR2,
      po_col_actv_date               OUT      DATE,
      po_col_next_step               OUT      NUMBER,
      po_col_next_actv_date          OUT      DATE,
      po_col_agency                  OUT      VARCHAR2,
      po_adr_attention               OUT      VARCHAR2,
      po_adr_delivery_type           OUT      VARCHAR2,
      po_adr_group                   OUT      VARCHAR2,
      po_tax_gst_exmp_rf_no          OUT      VARCHAR2,
      po_tax_pst_exmp_rf_no          OUT      VARCHAR2,
      po_tax_hst_exmp_rf_no          OUT      VARCHAR2,
      po_tax_gst_exmp_eff_dt         OUT      DATE,
      po_tax_pst_exmp_eff_dt         OUT      DATE,
      po_tax_hst_exmp_eff_dt         OUT      DATE,
      po_status_last_date            OUT      DATE,
      po_conv_run_no                 OUT      NUMBER,
      po_client_cons_inds            OUT      VARCHAR2,
      po_all_active_subs             OUT      NUMBER,
      po_all_reserved_subs           OUT      NUMBER,
      po_all_suspended_subs          OUT      NUMBER,
      po_all_cancelled_subs          OUT      NUMBER,
      po_bl_man_hndl_req_opid        OUT      NUMBER,
      po_bl_man_hndl_eff_date        OUT      DATE,
      po_bl_man_hndl_exp_date        OUT      DATE,
      po_bl_man_hndl_rsn             OUT      VARCHAR2,
      po_bl_man_hndl_by_opid         OUT      NUMBER,
      po_last_payment_actual_amt       OUT      NUMBER,
      po_name_suffix                 OUT      VARCHAR2,
      po_last_act_hotline            OUT      DATE,
      po_col_delinq_sts_date         OUT      DATE,
      po_col_written_off_date        OUT      DATE,
  		po_brand_ind                   OUT      NUMBER,
  		po_gl_segment                  OUT      VARCHAR2,
  		po_gl_subsegment               OUT      VARCHAR2,
  	  po_gur_cr_card_first6          OUT      VARCHAR2,
  	  po_gur_cr_card_last4           OUT      VARCHAR2,
  	  po_payment_card_first6         OUT      VARCHAR2,
  	  po_payment_card_last4          OUT      VARCHAR2,
  	  po_last_payment_actv_code      OUT      VARCHAR2
   )
   IS
      CURSOR c_account (v_logical_date DATE)
      IS
         SELECT DISTINCT ba.ban, ba.ban_status, ba.account_type,
                         ba.account_sub_type, ba.status_last_date,
                         ba.start_service_date, ba.col_delinq_status,
                         ba.ar_balance, ba.auto_gen_pym_type,
                         ba.hot_line_ind, ba.status_actv_code,
                         ba.status_actv_rsn_code, ba.cs_ret_envlp_ind,
                         NVL (TO_CHAR (ba.hierarchy_id), 'N') hierarchy_id,
                         ba.cs_ca_rep_id, ba.inv_suppression_ind,
                         ba.corporate_id,
                         NVL (RTRIM (ba.ar_wo_ind), 'N') wo_ind,
                         ba.home_province,
                         DECODE (ba.national_account,
                                 'R', 'R',
                                 'N', 'N',
                                 NULL
                                ) national_account,
                         ba.cs_handle_by_ctn_ind, ba.tax_gst_exmp_ind,
                         ba.tax_pst_exmp_ind, ba.tax_hst_exmp_ind,
                         ba.tax_gst_exmp_eff_dt, ba.tax_pst_exmp_eff_dt,
                         ba.tax_hst_exmp_eff_dt, ba.tax_gst_exmp_exp_dt,
                         ba.tax_pst_exmp_exp_dt, ba.tax_hst_exmp_exp_dt,
                         ba.tax_gst_exmp_rf_no, ba.tax_pst_exmp_rf_no,
                         ba.tax_hst_exmp_rf_no, ba.col_path_code,
                         ba.col_next_step_no, ba.col_next_step_date,
                         ba.col_agncy_code, ba.bl_man_hndl_req_opid,
                         ba.bl_man_hndl_eff_date, ba.bl_man_hndl_exp_date,
                         ba.bl_man_hndl_rsn, ba.bl_man_hndl_by_opid,
                         ba.last_act_hotline, ba.col_delinq_sts_date,
                         bc.cycle_code, bc.cycle_close_day,
                         nbc.cycle_code next_bill_cycle,
                         nbc.cycle_close_day next_bill_cycle_close_day,
                         SUBSTR (c.dealer_code,
                                 1,
                                 10
                                ) dealer_code,
                         SUBSTR (c.dealer_code, 11) sales_rep_code,
                         c.customer_id, c.work_telno, c.work_tn_extno,
                         c.home_telno, c.birth_date, c.contact_telno,
                         c.contact_tn_extno, c.contact_faxno, c.acc_password,
                         c.customer_ssn, c.lang_pref, c.email_address,
                         c.drivr_licns_no, c.drivr_licns_state,
                         c.drivr_licns_exp_dt, c.incorporation_no,
                         c.incorporation_date, c.gur_cr_card_no,
                         c.gur_pymt_card_first_six_str,c.gur_pymt_card_last_four_str,
                         c.gur_cr_card_exp_dt, c.other_telno, c.other_extno,
                         c.other_tn_type, c.verified_date, c.conv_run_no,
                         ad.adr_primary_ln, ad.adr_city, ad.adr_province,
                         ad.adr_postal, adr_type, adr_secondary_ln,
                         adr_country, adr_zip_geo_code, adr_state_code,
                         civic_no, civic_no_suffix, adr_st_direction,
                         adr_street_name, adr_street_type, adr_designator,
                         adr_identifier, adr_box, unit_designator,
                         unit_identifier, adr_area_nm, adr_qualifier,
                         adr_site, adr_compartment, adr_attention,
                         adr_delivery_tp, adr_group, nd.first_name,
                         nd.last_business_name, middle_initial, name_title,
                         additional_title, name_format, bdd.credit_card_no,
                         bdd.pymt_card_first_six_str, bdd.pymt_card_last_four_str,
                         bdd.card_mem_hold_nm, bdd.expiration_date,
                         bdd.bnk_code, bdd.bnk_acct_number,
                         bdd.bnk_acct_type, bdd.bnk_branch_number,
                         bdd.dd_status, bdd.status_reason
--       ,m.memo_manual_txt   special_instruction
                         ,
                         cs.effective_date cs_effective_date,
                         cs.cu_past_due_amt, cs.cu_age_bucket_0,
                         cs.cu_age_bucket_1_30, cs.cu_age_bucket_31_60,
                         cs.cu_age_bucket_61_90, cs.cu_age_bucket_91_plus,
                         nx_past_due_amt, nx_age_bucket_0,
                         nx_age_bucket_1_30, nx_age_bucket_31_60,
                         nx_age_bucket_61_90, nx_age_bucket_91_plus,
                         fu_past_due_amt, fu_age_bucket_0,
                         fu_age_bucket_1_30, fu_age_bucket_31_60,
                         fu_age_bucket_61_90, fu_age_bucket_91_plus,
                         name_suffix, NVL(ba.brand_id, '1') brand_ind,
                         ba.gl_segment, ba.gl_subsegment
                    FROM billing_account ba,
                         customer c,
                         address_name_link anl,
                         address_data ad,
                         name_data nd,
                         ban_direct_debit bdd,
                         CYCLE bc,
                         collection_status cs,
                         CYCLE nbc 
                       
                   WHERE ba.ban = pi_ban
                     AND c.customer_id = ba.customer_id
                     AND anl.customer_id = c.customer_id
                     AND (   TRUNC (anl.expiration_date) >
                                                       TRUNC ((v_logical_date ))
                          OR anl.expiration_date IS NULL
                         )
                     AND anl.link_type = 'B'
                     AND ad.address_id = anl.address_id
                     AND nd.name_id = anl.name_id
                     AND bdd.ban(+) = ba.ban
-- and     ba.ban=m.memo_ban(+)
-- and     m.memo_type(+)='3000'
                     AND bc.cycle_code(+) = ba.bill_cycle
                     AND nbc.cycle_code(+) = ba.bl_next_cycle
                     AND cs.ban(+) = ba.ban;

      acc_rec                c_account%ROWTYPE;

      CURSOR c_consent_inds_cur
      IS
         SELECT cpui_cd
           FROM ban_cpui bc
          WHERE bc.ban = pi_ban;

      CURSOR c_collection (v_logical_date DATE)
      IS
         SELECT   c.col_actv_code, c.col_actv_date,
                  TO_CHAR (c.col_actv_date, 'mm'), ca.severity_level
             FROM collection c, collection_act ca 
            WHERE c.ban = pi_ban
              AND c.col_actv_code = ca.col_activity_code
              AND TRUNC (c.col_actv_date) >
                           ADD_MONTHS (LAST_DAY (TRUNC (v_logical_date)),
                                       -12)
            
         ORDER BY TO_CHAR (c.col_actv_date, 'mm'), ca.severity_level;

      CURSOR c_col_activity
      IS
         SELECT c1.col_step_num, c1.col_actv_code, c1.col_actv_date
           FROM collection c1
          WHERE c1.ban = pi_ban
            AND c1.ent_seq_no =
                       (SELECT MAX (ent_seq_no)
                          FROM collection c2
                         WHERE c2.ban = c1.ban AND c2.col_actv_type_ind = 'A');

      CURSOR c_written_off_date
      IS
         SELECT c1.col_actv_date
           FROM collection c1
          WHERE c1.ban = pi_ban
            AND c1.ent_seq_no =
                           (SELECT MAX (ent_seq_no)
                              FROM collection c2
                             WHERE c2.ban = c1.ban AND c2.col_actv_code = 'W');

      CURSOR c_dck
      IS
         SELECT   TO_CHAR (pa.actv_date, 'mm') mm_dck, COUNT (*) nbr
             FROM payment p, payment_activity pa, backout_rsn_code brc
            WHERE p.ban = pi_ban
              AND pa.ban = p.ban
              AND pa.ent_seq_no = p.ent_seq_no
              AND pa.actv_code = 'BCK'
              AND pa.actv_reason_code = brc.bck_code
              AND brc.dck_history_ind = 'D'
              AND TRUNC (pa.actv_date) >
                                   ADD_MONTHS (LAST_DAY (TRUNC (SYSDATE)),
                                               -12)
         GROUP BY TO_CHAR (pa.actv_date, 'mm');

      CURSOR c_finance_history
      IS
         SELECT   TO_CHAR (fh.act_date, 'mm') mm_dck, COUNT (*) nbr
             FROM finance_history fh, backout_rsn_code brc
            WHERE fh.ban = pi_ban
              AND fh.act_code = 'R'
              AND brc.bck_code = fh.act_reason_code
              AND brc.dck_history_ind = 'D'
              AND TRUNC (fh.act_date) >
                                   ADD_MONTHS (LAST_DAY (TRUNC (SYSDATE)),
                                               -12)
         GROUP BY TO_CHAR (fh.act_date, 'mm');

      CURSOR c_payment
      IS
         SELECT   p.original_amt, p.deposit_date, pa.ent_seq_no
             FROM payment p, payment_activity pa
            WHERE p.ban = pi_ban
              --AND (p.designation = 'B' OR p.designation IS NULL)
              AND pa.ban = p.ban
              AND pa.ent_seq_no = p.ent_seq_no
              AND pa.actv_code IN ('PYM', 'FNTT')
         ORDER BY pa.actv_date DESC, pa.ent_seq_no DESC;

      CURSOR c_pymt_actual_amt ( p_ban number, p_ent_seq_no number)
      IS
         select sum( actv_amt) 
           from payment_activity
          where ban= p_ban and ent_seq_no=p_ent_seq_no
       group by ban,ent_seq_no;

     CURSOR c_pymt_last_activity ( p_ban number, p_ent_seq_no number)
      IS
         select actv_code
           from payment_activity
          where ban= p_ban and ent_seq_no=p_ent_seq_no
         ORDER BY actv_seq_no DESC;
         
         /*
      CURSOR c_payment_refund (p_ban NUMBER, p_ent_seq_no NUMBER)
      IS
         SELECT   pa.actv_amt
             FROM payment p, payment_activity pa
            WHERE p.ban = pi_ban
              AND p.ent_seq_no = p_ent_seq_no
              AND (p.designation = 'B' OR p.designation IS NULL)
              AND pa.ban = p.ban
              AND pa.ent_seq_no = p.ent_seq_no
              AND pa.actv_code = 'RFN'
         ORDER BY pa.actv_date DESC, pa.ent_seq_no DESC, pa.actv_seq_no DESC;
         */

      CURSOR c_contact_name (v_logical_date DATE)
      IS
         SELECT nd.first_name, nd.last_business_name, nd.middle_initial,
                nd.name_title, nd.additional_title, nd.name_suffix
           FROM address_name_link anl, name_data nd 
          WHERE anl.ban = pi_ban
            AND (   TRUNC (anl.expiration_date) > TRUNC (v_logical_date)
                 OR anl.expiration_date IS NULL
                )
            AND anl.link_type = 'C'
            AND nd.name_id = anl.name_id;

      CURSOR c_bill
      IS
         SELECT   bill_due_date
             FROM bill
            WHERE ban = pi_ban
              AND bill_conf_status = 'C'
              AND TO_CHAR (bill_due_date, 'yyyy') != '4700'
         ORDER BY bill_due_date DESC;


      v_logical_date         DATE;
      cn_rec                 c_contact_name%ROWTYPE;
      v_payment_amt          NUMBER (9, 2);
      --v_payment_refund_amt   NUMBER (9, 2);
      v_ent_seq_no           NUMBER (9, 0);
      v_payment_date         DATE;
      v_account_type         VARCHAR2 (1);
      v_account_sub_type     VARCHAR2 (1);
      c_consent_inds_rec     VARCHAR2 (1000);
      v_last_payment_actual_amt NUMBER (9,2);
      v_last_payment_actv_code  VARCHAR2(4);

   BEGIN
          
      getlogicaldate (v_logical_date);

      OPEN c_account (v_logical_date);

      FETCH c_account
       INTO acc_rec;

      IF c_account%FOUND
      THEN
         po_ban_status := acc_rec.ban_status;
         po_account_type := acc_rec.account_type;
         v_account_type := acc_rec.account_type;
         po_account_sub_type := acc_rec.account_sub_type;
         v_account_sub_type := acc_rec.account_sub_type;
         po_create_date :=
                   NVL (acc_rec.start_service_date, acc_rec.status_last_date);
				 po_brand_ind := to_number(acc_rec.brand_ind);      -- brand indicator
         po_start_service_date := acc_rec.start_service_date;
         po_col_delinq_status := acc_rec.col_delinq_status;
         po_ar_balance := acc_rec.ar_balance;
         po_dealer_code := acc_rec.dealer_code;
         po_sales_rep_code := acc_rec.sales_rep_code;
         po_bill_cycle := acc_rec.cycle_code;
         po_payment_method := acc_rec.auto_gen_pym_type;
         po_work_telno := acc_rec.work_telno;
         po_work_tn_extno := acc_rec.work_tn_extno;
         po_home_telno := acc_rec.home_telno;
         po_birth_date := acc_rec.birth_date;
         po_contact_faxno := acc_rec.contact_faxno;
         po_acc_password := acc_rec.acc_password;
         po_customer_ssn := acc_rec.customer_ssn;
         po_adr_primary_ln := acc_rec.adr_primary_ln;
         po_adr_province := acc_rec.adr_province;
         po_adr_city := acc_rec.adr_city;
         po_adr_postal := acc_rec.adr_postal;
         po_first_name := acc_rec.first_name;
         po_last_business_name := acc_rec.last_business_name;
-- po_special_instruction  := acc_rec.special_instruction;
         po_hotline_ind := acc_rec.hot_line_ind;
         po_customer_id := acc_rec.customer_id;
         po_language := acc_rec.lang_pref;
         po_email := acc_rec.email_address;
         po_adr_type := acc_rec.adr_type;
         po_adr_secondary_ln := acc_rec.adr_secondary_ln;
         po_adr_country := acc_rec.adr_country;
         po_adr_zip_geo_code := acc_rec.adr_zip_geo_code;
         po_adr_state_code := acc_rec.adr_state_code;
         po_civic_no := acc_rec.civic_no;
         po_civic_no_suffix := acc_rec.civic_no_suffix;
         po_adr_st_direction := acc_rec.adr_st_direction;
         po_adr_street_name := acc_rec.adr_street_name;
         po_adr_street_type := acc_rec.adr_street_type;
         po_adr_designator := acc_rec.adr_designator;
         po_adr_identifier := acc_rec.adr_identifier;
         po_adr_box := acc_rec.adr_box;
         po_unit_designator := acc_rec.unit_designator;
         po_unit_identifier := acc_rec.unit_identifier;
         po_adr_area_nm := acc_rec.adr_area_nm;
         po_adr_qualifier := acc_rec.adr_qualifier;
         po_adr_site := acc_rec.adr_site;
         po_adr_compartment := acc_rec.adr_compartment;
         po_adr_attention := acc_rec.adr_attention;
         po_adr_delivery_type := acc_rec.adr_delivery_tp;
         po_adr_group := acc_rec.adr_group;
         po_middle_initial := acc_rec.middle_initial;
         po_name_title := acc_rec.name_title;
         po_additional_title := acc_rec.additional_title;
         po_drivr_licns_no := acc_rec.drivr_licns_no;
         po_drivr_licns_state := acc_rec.drivr_licns_state;
         po_drivr_licns_exp_dt := acc_rec.drivr_licns_exp_dt;
         po_incorporation_no := acc_rec.incorporation_no;
         po_incorporation_date := acc_rec.incorporation_date;
         po_gur_cr_card_no := acc_rec.gur_cr_card_no;
         po_gur_cr_card_exp_dt_mm :=
                       TO_NUMBER (TO_CHAR (acc_rec.gur_cr_card_exp_dt, 'mm'));
         po_gur_cr_card_exp_dt_yyyy :=
                     TO_NUMBER (TO_CHAR (acc_rec.gur_cr_card_exp_dt, 'yyyy'));
         po_credit_card_no := acc_rec.credit_card_no;
         po_card_mem_hold_nm := acc_rec.card_mem_hold_nm;
         po_expiration_date_mm :=
                          TO_NUMBER (TO_CHAR (acc_rec.expiration_date, 'mm'));
         po_expiration_date_yyyy :=
                        TO_NUMBER (TO_CHAR (acc_rec.expiration_date, 'yyyy'));
         po_status_actv_code := acc_rec.status_actv_code;
         po_status_actv_rsn_code := acc_rec.status_actv_rsn_code;
         po_bill_cycle_close_day := acc_rec.cycle_close_day;
         po_return_envelope_ind := acc_rec.cs_ret_envlp_ind;
         po_corp_hierarhy_ind := acc_rec.hierarchy_id;
         po_corp_csr_id := acc_rec.cs_ca_rep_id;
         po_inv_supression_ind := acc_rec.inv_suppression_ind;
         po_bankcode := acc_rec.bnk_code;
         po_bankaccountnumber := acc_rec.bnk_acct_number;
         po_bankbranchnumber := acc_rec.bnk_branch_number;
         po_bankaccounttype := acc_rec.bnk_acct_type;
         po_directdebitstatus := acc_rec.dd_status;
         po_directdebitstatusrsn := acc_rec.status_reason;
         po_other_phone := acc_rec.other_telno;
         po_other_phone_ext := acc_rec.other_extno;
         po_other_phone_type := acc_rec.other_tn_type;
         po_home_province := acc_rec.home_province;
         po_category := acc_rec.national_account;
         po_next_bill_cycle := acc_rec.next_bill_cycle;
         po_next_bill_cycle_close_day := acc_rec.next_bill_cycle_close_day;
         po_verified_date := acc_rec.verified_date;
         po_handle_by_subscriber_ind := acc_rec.cs_handle_by_ctn_ind;
         po_corporate_id := acc_rec.corporate_id;
         po_write_off_ind := acc_rec.wo_ind;
         po_contact_phone_number := acc_rec.contact_telno;
         po_contact_phone_number_ext := acc_rec.contact_tn_extno;
         po_col_path_code := acc_rec.col_path_code;
         po_col_next_step := acc_rec.col_next_step_no;
         po_col_next_actv_date := acc_rec.col_next_step_date;
         po_col_agency := acc_rec.col_agncy_code;
         po_status_last_date := acc_rec.status_last_date;
         po_conv_run_no := acc_rec.conv_run_no;
         po_bl_man_hndl_req_opid := acc_rec.bl_man_hndl_req_opid;
         po_bl_man_hndl_eff_date := acc_rec.bl_man_hndl_eff_date;
         po_bl_man_hndl_exp_date := acc_rec.bl_man_hndl_exp_date;
         po_bl_man_hndl_rsn := acc_rec.bl_man_hndl_rsn;
         po_bl_man_hndl_by_opid := acc_rec.bl_man_hndl_by_opid;
         po_name_suffix := acc_rec.name_suffix;
         po_last_act_hotline := acc_rec.last_act_hotline;
         po_col_delinq_sts_date := acc_rec.col_delinq_sts_date;
         po_tax_gst_exmp_ind := acc_rec.tax_gst_exmp_ind;
         po_tax_gst_exmp_exp_dt := acc_rec.tax_gst_exmp_exp_dt;
         po_tax_gst_exmp_rf_no := acc_rec.tax_gst_exmp_rf_no;
         po_tax_gst_exmp_eff_dt := acc_rec.tax_gst_exmp_eff_dt;
         po_tax_pst_exmp_ind := acc_rec.tax_pst_exmp_ind;
         po_tax_pst_exmp_exp_dt := acc_rec.tax_pst_exmp_exp_dt;
         po_tax_pst_exmp_rf_no := acc_rec.tax_pst_exmp_rf_no;
         po_tax_pst_exmp_eff_dt := acc_rec.tax_pst_exmp_eff_dt;
         po_tax_hst_exmp_ind := acc_rec.tax_hst_exmp_ind;
         po_tax_hst_exmp_exp_dt := acc_rec.tax_hst_exmp_exp_dt;
         po_tax_hst_exmp_rf_no := acc_rec.tax_hst_exmp_rf_no;
         po_tax_hst_exmp_eff_dt := acc_rec.tax_hst_exmp_eff_dt;
         po_gl_segment := acc_rec.gl_segment;
         po_gl_subsegment := acc_rec.gl_subsegment;
         po_gur_cr_card_first6 := acc_rec.gur_pymt_card_first_six_str;
         po_gur_cr_card_last4 := acc_rec.gur_pymt_card_last_four_str;
         po_payment_card_first6 := acc_rec.pymt_card_first_six_str;
         po_payment_card_last4 := acc_rec.pymt_card_last_four_str;

         IF TRUNC (acc_rec.cs_effective_date) = TRUNC (v_logical_date)
         THEN
            po_cu_age_bucket_0 := acc_rec.cu_age_bucket_0;
            po_cu_age_bucket_1_30 := acc_rec.cu_age_bucket_1_30;
            po_cu_age_bucket_31_60 := acc_rec.cu_age_bucket_31_60;
            po_cu_age_bucket_61_90 := acc_rec.cu_age_bucket_61_90;
            po_cu_age_bucket_91_plus := acc_rec.cu_age_bucket_91_plus;
            po_cu_past_due_amt := acc_rec.cu_past_due_amt;
         ELSIF TRUNC (acc_rec.cs_effective_date + 1) = TRUNC (v_logical_date)
         THEN
            po_cu_age_bucket_0 := acc_rec.nx_age_bucket_0;
            po_cu_age_bucket_1_30 := acc_rec.nx_age_bucket_1_30;
            po_cu_age_bucket_31_60 := acc_rec.nx_age_bucket_31_60;
            po_cu_age_bucket_61_90 := acc_rec.nx_age_bucket_61_90;
            po_cu_age_bucket_91_plus := acc_rec.nx_age_bucket_91_plus;
            po_cu_past_due_amt := acc_rec.nx_past_due_amt;
         ELSE
            po_cu_age_bucket_0 := acc_rec.fu_age_bucket_0;
            po_cu_age_bucket_1_30 := acc_rec.fu_age_bucket_1_30;
            po_cu_age_bucket_31_60 := acc_rec.fu_age_bucket_31_60;
            po_cu_age_bucket_61_90 := acc_rec.fu_age_bucket_61_90;
            po_cu_age_bucket_91_plus := acc_rec.fu_age_bucket_91_plus;
            po_cu_past_due_amt := acc_rec.fu_past_due_amt;
         END IF;

         OPEN c_contact_name (v_logical_date);

         FETCH c_contact_name
          INTO cn_rec;

         po_contact_first_name := cn_rec.first_name;
         po_contact_last_name := cn_rec.last_business_name;
         po_contact_name_title := cn_rec.name_title;
         po_contact_middle_initial := cn_rec.middle_initial;
         po_contact_additional_title := cn_rec.additional_title;
         po_contact_name_suffix := cn_rec.name_suffix;

         CLOSE c_contact_name;

         IF     acc_rec.account_type = 'B'
            AND acc_rec.account_sub_type IN ('P', '3')
         THEN
            po_legal_business_name := acc_rec.additional_title;
         END IF;
      ELSE
         RAISE bannotfound;
      END IF;

      CLOSE c_account;

			getSubscriberCounts (pi_ban, 
													 v_account_type, 
													 v_account_sub_type,
													 po_active_subs,
													 po_reserved_subs,
													 po_suspended_subs,
													 po_cancelled_subs,
													 po_all_active_subs,
													 po_all_reserved_subs,
													 po_all_suspended_subs,
													 po_all_cancelled_subs);
      
--
      FOR col_rec IN c_collection (v_logical_date)
      LOOP
         IF TO_CHAR (col_rec.col_actv_date, 'mm') = '01'
         THEN
            po_col_actv_jan := col_rec.col_actv_code;
         ELSIF TO_CHAR (col_rec.col_actv_date, 'mm') = '02'
         THEN
            po_col_actv_feb := col_rec.col_actv_code;
         ELSIF TO_CHAR (col_rec.col_actv_date, 'mm') = '03'
         THEN
            po_col_actv_mar := col_rec.col_actv_code;
         ELSIF TO_CHAR (col_rec.col_actv_date, 'mm') = '04'
         THEN
            po_col_actv_apr := col_rec.col_actv_code;
         ELSIF TO_CHAR (col_rec.col_actv_date, 'mm') = '05'
         THEN
            po_col_actv_may := col_rec.col_actv_code;
         ELSIF TO_CHAR (col_rec.col_actv_date, 'mm') = '06'
         THEN
            po_col_actv_jun := col_rec.col_actv_code;
         ELSIF TO_CHAR (col_rec.col_actv_date, 'mm') = '07'
         THEN
            po_col_actv_jul := col_rec.col_actv_code;
         ELSIF TO_CHAR (col_rec.col_actv_date, 'mm') = '08'
         THEN
            po_col_actv_aug := col_rec.col_actv_code;
         ELSIF TO_CHAR (col_rec.col_actv_date, 'mm') = '09'
         THEN
            po_col_actv_sep := col_rec.col_actv_code;
         ELSIF TO_CHAR (col_rec.col_actv_date, 'mm') = '10'
         THEN
            po_col_actv_oct := col_rec.col_actv_code;
         ELSIF TO_CHAR (col_rec.col_actv_date, 'mm') = '11'
         THEN
            po_col_actv_nov := col_rec.col_actv_code;
         ELSIF TO_CHAR (col_rec.col_actv_date, 'mm') = '12'
         THEN
            po_col_actv_dec := col_rec.col_actv_code;
         END IF;
      END LOOP;

      po_dck_jan := 0;
      po_dck_feb := 0;
      po_dck_mar := 0;
      po_dck_apr := 0;
      po_dck_may := 0;
      po_dck_jun := 0;
      po_dck_jul := 0;
      po_dck_aug := 0;
      po_dck_sep := 0;
      po_dck_oct := 0;
      po_dck_nov := 0;
      po_dck_dec := 0;

      FOR dck_rec IN c_dck
      LOOP
         IF dck_rec.mm_dck = '01'
         THEN
            po_dck_jan := dck_rec.nbr;
         ELSIF dck_rec.mm_dck = '02'
         THEN
            po_dck_feb := dck_rec.nbr;
         ELSIF dck_rec.mm_dck = '03'
         THEN
            po_dck_mar := dck_rec.nbr;
         ELSIF dck_rec.mm_dck = '04'
         THEN
            po_dck_apr := dck_rec.nbr;
         ELSIF dck_rec.mm_dck = '05'
         THEN
            po_dck_may := dck_rec.nbr;
         ELSIF dck_rec.mm_dck = '06'
         THEN
            po_dck_jun := dck_rec.nbr;
         ELSIF dck_rec.mm_dck = '07'
         THEN
            po_dck_jul := dck_rec.nbr;
         ELSIF dck_rec.mm_dck = '08'
         THEN
            po_dck_aug := dck_rec.nbr;
         ELSIF dck_rec.mm_dck = '09'
         THEN
            po_dck_sep := dck_rec.nbr;
         ELSIF dck_rec.mm_dck = '10'
         THEN
            po_dck_oct := dck_rec.nbr;
         ELSIF dck_rec.mm_dck = '11'
         THEN
            po_dck_nov := dck_rec.nbr;
         ELSIF dck_rec.mm_dck = '12'
         THEN
            po_dck_dec := dck_rec.nbr;
         END IF;
      END LOOP;

      FOR fh_rec IN c_finance_history
      LOOP
         IF fh_rec.mm_dck = '01'
         THEN
            po_dck_jan := po_dck_jan + fh_rec.nbr;
         ELSIF fh_rec.mm_dck = '02'
         THEN
            po_dck_feb := po_dck_feb + fh_rec.nbr;
         ELSIF fh_rec.mm_dck = '03'
         THEN
            po_dck_mar := po_dck_mar + fh_rec.nbr;
         ELSIF fh_rec.mm_dck = '04'
         THEN
            po_dck_apr := po_dck_apr + fh_rec.nbr;
         ELSIF fh_rec.mm_dck = '05'
         THEN
            po_dck_may := po_dck_may + fh_rec.nbr;
         ELSIF fh_rec.mm_dck = '06'
         THEN
            po_dck_jun := po_dck_jun + fh_rec.nbr;
         ELSIF fh_rec.mm_dck = '07'
         THEN
            po_dck_jul := po_dck_jul + fh_rec.nbr;
         ELSIF fh_rec.mm_dck = '08'
         THEN
            po_dck_aug := po_dck_aug + fh_rec.nbr;
         ELSIF fh_rec.mm_dck = '09'
         THEN
            po_dck_sep := po_dck_sep + fh_rec.nbr;
         ELSIF fh_rec.mm_dck = '10'
         THEN
            po_dck_oct := po_dck_oct + fh_rec.nbr;
         ELSIF fh_rec.mm_dck = '11'
         THEN
            po_dck_nov := po_dck_nov + fh_rec.nbr;
         ELSIF fh_rec.mm_dck = '12'
         THEN
            po_dck_dec := po_dck_dec + fh_rec.nbr;
         END IF;
      END LOOP;

-- c_consent_ind_rec varchar(1000);
      c_consent_inds_rec := '';
      po_client_cons_inds := '';

      OPEN c_consent_inds_cur;

      LOOP
         FETCH c_consent_inds_cur
          INTO c_consent_inds_rec;

         EXIT WHEN c_consent_inds_cur%NOTFOUND;
         po_client_cons_inds :=
                             po_client_cons_inds || c_consent_inds_rec || '|';
      END LOOP;

      po_client_cons_inds :=
         SUBSTR (po_client_cons_inds,
                 1,
                 INSTR (po_client_cons_inds,
                        '|',
                        -1
                       ) - 1
                );

      CLOSE c_consent_inds_cur;

      OPEN c_payment;

      FETCH c_payment
       INTO v_payment_amt, v_payment_date, v_ent_seq_no;

      IF c_payment%FOUND
      THEN
         po_last_payment_amnt := v_payment_amt; --this is payment original amount
         po_last_payment_date :=  v_payment_date;  --this is payment deposit date
         
         OPEN c_pymt_actual_amt ( pi_ban,v_ent_seq_no);
         FETCH c_pymt_actual_amt INTO v_last_payment_actual_amt;
         
         po_last_payment_actual_amt := v_last_payment_actual_amt;

         CLOSE c_pymt_actual_amt;
         
         OPEN c_pymt_last_activity ( pi_ban,v_ent_seq_no);
         FETCH c_pymt_last_activity INTO v_last_payment_actv_code;
         
         po_last_payment_actv_code := v_last_payment_actv_code; -- activity code
         CLOSE c_pymt_last_activity;
         
/*
         OPEN c_payment_refund (pi_ban, v_ent_seq_no);

         FETCH c_payment_refund
          INTO v_payment_refund_amt;

         IF c_payment_refund%FOUND
         THEN
            po_last_payment_refunded := 1;
         END IF;

         CLOSE c_payment_refund;
*/         
      END IF;

      CLOSE c_payment;

      OPEN c_bill;

      FETCH c_bill
       INTO po_bill_due_date;

      CLOSE c_bill;

      OPEN c_col_activity;

      FETCH c_col_activity
       INTO po_col_step, po_col_actv_code, po_col_actv_date;

      CLOSE c_col_activity;

      OPEN c_written_off_date;

      FETCH c_written_off_date
       INTO po_col_written_off_date;

      CLOSE c_written_off_date;
   EXCEPTION
      WHEN bannotfound
      THEN
         IF c_account%ISOPEN
         THEN
            CLOSE c_account;
         END IF;

         raise_application_error (-20101, 'BAN  Not Found');
      WHEN OTHERS
      THEN
         IF c_account%ISOPEN
         THEN
            CLOSE c_account;
         END IF;

/*
         IF c_sub_summary%ISOPEN
         THEN
            CLOSE c_sub_summary;
         END IF;
*/
         IF c_collection%ISOPEN
         THEN
            CLOSE c_collection;
         END IF;

         IF c_dck%ISOPEN
         THEN
            CLOSE c_dck;
         END IF;

         IF c_payment%ISOPEN
         THEN
            CLOSE c_payment;
         END IF;

         IF c_pymt_actual_amt%ISOPEN
         THEN
            CLOSE c_pymt_actual_amt;
         END IF;
/*
         IF c_payment_refund%ISOPEN
         THEN
            CLOSE c_payment_refund;
         END IF;
*/
         IF c_contact_name%ISOPEN
         THEN
            CLOSE c_contact_name;
         END IF;

         IF c_written_off_date%ISOPEN
         THEN
            CLOSE c_written_off_date;
         END IF;

         RAISE;
   END getaccountinfobyban;

   PROCEDURE println (v_line IN VARCHAR2)
   IS
      i_length   NUMBER;
      i_index    NUMBER;
   BEGIN
      i_length := LENGTH (v_line);
      i_index := 0;

      WHILE i_length > 255
      LOOP
         DBMS_OUTPUT.put_line (SUBSTR (v_line,
                                       i_index,
                                       255
                                      ));
         i_index := i_index + 255;
         i_length := i_length - 255;
      END LOOP;

      DBMS_OUTPUT.put_line (SUBSTR (v_line,
                                    i_index,
                                    LENGTH (v_line) - i_index + 1
                                   ));
   END println;

   FUNCTION getaccountsbybans (
      i_ban_numbers     IN       t_ban_array,
      o_accounts        OUT      refcursor,
      v_error_message   OUT      VARCHAR2
   )
      RETURN NUMBER
   IS
      v_cursor_text   VARCHAR2 (32767);
      i_index         BINARY_INTEGER;
      i_result        NUMBER (1);
   BEGIN
      BEGIN
/*
         IF i_ban_numbers.COUNT > max_maximorum THEN
            i_ban_numbers := i_ban_numbers.TRIM(max_maximorum-i_ban_numbers.COUNT);
         END IF;
*/         
         IF i_ban_numbers.COUNT > 0
         THEN
            OPEN o_accounts
             FOR
                SELECT DISTINCT ba.ban, ba.ban_status, ba.account_type,
                                ba.account_sub_type,
                                NVL (ba.start_service_date,
                                     ba.status_last_date),
                                ba.start_service_date,
                                SUBSTR (c.dealer_code,
                                        1,
                                        10
                                       ) dealer_code,
                                SUBSTR (c.dealer_code, 11) sales_rep_code,
                                c.customer_id, nd.first_name,
                                nd.last_business_name, middle_initial,
                                name_title, ad.adr_primary_ln, ad.adr_city,
                                ad.adr_province, ad.adr_postal, adr_type,
                                adr_secondary_ln, adr_country,
                                adr_zip_geo_code, adr_state_code, civic_no,
                                civic_no_suffix, adr_st_direction,
                                adr_street_name, adr_street_type,
                                adr_designator, adr_identifier, adr_box,
                                unit_designator, unit_identifier,
                                adr_area_nm, adr_qualifier, adr_site,
                                adr_compartment, c.acc_password,
                                ba.status_actv_code, ba.status_actv_rsn_code,
                                nd.additional_title, ba.status_last_date, NVL(ba.brand_id,'1') brand_ind,
                                ba.gl_segment, ba.gl_subsegment
                           FROM billing_account ba,
                                customer c,
                                address_name_link anl,
                                address_data ad,
                                name_data nd
                          WHERE ba.ban IN (
                                   SELECT *
                                     FROM TABLE
                                             (CAST
                                                 (i_ban_numbers AS t_ban_array)) WHERE ROWNUM <= max_maximorum)
                            AND anl.name_id = nd.name_id
                            AND (   anl.expiration_date IS NULL
                                 OR anl.expiration_date > SYSDATE
                                )
                            AND anl.link_type = 'B'
                            AND ad.address_id = anl.address_id
                            AND ba.ban = anl.customer_id
                            AND c.customer_id = anl.customer_id;

            i_result := numeric_true;
         ELSE
            v_error_message := err_invalid_input;
            i_result := numeric_false;
         END IF;
      EXCEPTION
         WHEN NO_DATA_FOUND
         THEN
            IF (o_accounts%ISOPEN)
            THEN
               CLOSE o_accounts;
            END IF;

            v_error_message := err_no_data_found;
         WHEN OTHERS
         THEN
            IF (o_accounts%ISOPEN)
            THEN
               CLOSE o_accounts;
            END IF;

            v_error_message := SQLERRM;
            i_result := numeric_false;
      END;

      RETURN i_result;
   END getaccountsbybans;

   FUNCTION getaccountsbyname (
      c_name_type            IN       VARCHAR2,
      v_control_name         IN       VARCHAR2,
      i_control_name_exact   IN       NUMBER,
      v_first_name           IN       VARCHAR2,
      i_first_name_exact     IN       NUMBER,
      c_account_status       IN       VARCHAR2,
      c_account_type         IN       VARCHAR2,
      v_province             IN       VARCHAR2,
      i_maximum              IN       NUMBER,
      i_brand_ind            IN       NUMBER,
      o_accounts             OUT      refcursor,
      i_has_more             OUT      NUMBER,
      v_error_message        OUT      VARCHAR2
   )
      RETURN NUMBER
   IS
      a_ban_array        t_ban_array;
      c_account_cursor   refcursor;
      v_cursor_text      VARCHAR2 (32767);
      i_max              NUMBER (4);
      i_count            BINARY_INTEGER;
      b_full_join        BOOLEAN;
      i_result           NUMBER (1);
   BEGIN
      BEGIN
         b_full_join :=
               (TO_CHAR (c_account_status) != search_all)
            OR (TO_CHAR (c_account_type) != search_all)
            OR (v_province != search_all) OR (i_brand_ind != brand_all);
         v_cursor_text := 'SELECT DISTINCT anl.customer_id ';

         IF b_full_join
         THEN
            v_cursor_text :=
                  v_cursor_text
               || '  FROM name_data nd, address_name_link anl, billing_account ba ';
         ELSE
            v_cursor_text :=
               v_cursor_text || '  FROM name_data nd, address_name_link anl ';
         END IF;

         IF i_control_name_exact = numeric_true
         THEN
            v_cursor_text :=
                  v_cursor_text
               || ' WHERE nd.control_name = '''
               || UPPER (v_control_name)
               || ''' ';
         ELSE
            v_cursor_text :=
                  v_cursor_text
               || ' WHERE nd.control_name LIKE '''
               || UPPER (v_control_name)
               || '%'' ';
         END IF;

         IF LENGTH (RTRIM (LTRIM (v_first_name))) > 0
         THEN
            IF i_first_name_exact = numeric_true
            THEN
               v_cursor_text :=
                     v_cursor_text
                  || '   AND nd.first_name = '''
                  || UPPER (v_first_name)
                  || ''' ';
            ELSE
               v_cursor_text :=
                     v_cursor_text
                  || '   AND nd.first_name LIKE '''
                  || UPPER (v_first_name)
                  || '%'' ';
            END IF;
         END IF;

         v_cursor_text :=
               v_cursor_text
            || '   AND anl.name_id = nd.name_id '
            || '   AND (anl.expiration_date IS NULL OR anl.expiration_date > sysdate) ';

         IF TO_CHAR (c_name_type) != search_all
         THEN
            v_cursor_text :=
                  v_cursor_text
               || '   AND anl.link_type = '''
               || TO_CHAR (c_name_type)
               || ''' ';
         END IF;

         IF b_full_join
         THEN
            v_cursor_text :=
                          v_cursor_text || '   AND ba.ban = anl.customer_id ';

            IF TO_CHAR (c_account_type) != search_all
            THEN
               v_cursor_text :=
                     v_cursor_text
                  || '   AND ba.account_type = '''
                  || c_account_type
                  || ''' ';
            END IF;

            IF TO_CHAR (c_account_status) != search_all
            THEN
               v_cursor_text :=
                     v_cursor_text
                  || '   AND ba.ban_status = '''
                  || c_account_status
                  || ''' ';
            END IF;

            IF TO_CHAR (v_province) != search_all
            THEN
               v_cursor_text :=
                     v_cursor_text
                  || '   AND ba.home_province = '''
                  || v_province
                  || ''' ';
            END IF;
            
						IF i_brand_ind != brand_all
         		THEN
         	 		IF i_brand_ind != brand_telus THEN
           			v_cursor_text :=  v_cursor_text || ' AND ba.brand_id = ''' || to_char(i_brand_ind) || '''';
           		ELSE
	           		v_cursor_text :=  v_cursor_text || ' AND (ba.brand_id = ''' || to_char(i_brand_ind) || ''' OR ba.brand_id IS NULL)';
           		END IF;
         		END IF;
         END IF;

         IF i_maximum > 0 AND i_maximum < max_maximorum
         THEN
            i_max := i_maximum;
         ELSE
            i_max := max_maximorum;
         END IF;

         OPEN c_account_cursor
          FOR v_cursor_text;

         a_ban_array := t_ban_array (NULL);

         FETCH c_account_cursor
         BULK COLLECT INTO a_ban_array LIMIT (i_max + 1);

         i_count := a_ban_array.COUNT;

         IF i_count > i_max AND NOT c_account_cursor%NOTFOUND
         THEN
            i_has_more := numeric_true;
         ELSE
            i_has_more := numeric_false;
         END IF;

         CLOSE c_account_cursor;

         IF i_count > 0
         THEN
            WHILE a_ban_array (a_ban_array.COUNT) IS NULL
            LOOP
               a_ban_array.TRIM;
            END LOOP;

            IF i_has_more = numeric_true
            THEN
               a_ban_array.TRIM;
            END IF;

            i_result :=
                  getaccountsbybans (a_ban_array,
                                     o_accounts,
                                     v_error_message
                                    );
         ELSE
            v_error_message := err_no_data_found;
            i_result := numeric_false;
         END IF;
      EXCEPTION
         WHEN NO_DATA_FOUND
         THEN
            v_error_message := SQLERRM;
            i_result := numeric_false;
         WHEN OTHERS
         THEN
            RAISE;
      END;

      RETURN i_result;
   END getaccountsbyname;

--------------------------------------------------------------------------------------------------------------------
   FUNCTION getservicesubscribercounts (
      pi_service_codes     IN       t_service_codes,
      pi_include_expired   IN       NUMBER,
      pi_ban               IN       NUMBER,
      po_subscribers       OUT      refcursor,
      v_error_message      OUT      VARCHAR2
   )
      RETURN NUMBER
   IS
      v_cursor_text    VARCHAR2 (32767);
      v_service_code   VARCHAR2 (20);
      i_index          BINARY_INTEGER;
      i_result         NUMBER (1);
   BEGIN
      BEGIN
         IF pi_service_codes.COUNT > 0
         THEN
            IF (pi_include_expired = 1)
            THEN
               OPEN po_subscribers
                FOR                                        -- exclude expired
                   SELECT DISTINCT sa.soc, sa.subscriber_no, s.sub_status
                              FROM service_agreement sa,
                                   subscriber s,
                                   logical_date ld
                             WHERE sa.ban = pi_ban
                               AND s.customer_id = sa.ban
                               AND s.subscriber_no = sa.subscriber_no
                               AND sa.soc IN (
                                      SELECT *
                                        FROM TABLE
                                                (CAST
                                                    (pi_service_codes AS t_service_codes)))
                               AND (   TRUNC (sa.expiration_date) >
                                                       TRUNC (ld.logical_date)
                                    OR sa.expiration_date IS NULL
                                   )
                               AND ld.logical_date_type = 'O';
            ELSE
               OPEN po_subscribers
                FOR
                   SELECT DISTINCT sa.soc, sa.subscriber_no, s.sub_status
                              FROM service_agreement sa, subscriber s
                             WHERE sa.ban = pi_ban
                               AND s.customer_id = sa.ban
                               AND s.subscriber_no = sa.subscriber_no
                               AND sa.soc IN (
                                      SELECT *
                                        FROM TABLE
                                                (CAST
                                                    (pi_service_codes AS t_service_codes)));
            END IF;

            i_result := numeric_true;
         ELSE
            v_error_message := err_invalid_input;
            i_result := numeric_false;
         END IF;
      EXCEPTION
         WHEN NO_DATA_FOUND
         THEN
            v_error_message := err_no_data_found;
            i_result := numeric_false;

            IF (po_subscribers%ISOPEN)
            THEN
               CLOSE po_subscribers;
            END IF;
         WHEN OTHERS
         THEN
            IF (po_subscribers%ISOPEN)
            THEN
               CLOSE po_subscribers;
            END IF;

            raise_application_error
                      (-20102,
                          'GetServiceSubscriberCounts Query Failed. Oracle:(['
                       || SQLCODE
                       || '] ['
                       || SQLERRM
                       || '])');
      END;

      RETURN i_result;
   END getservicesubscribercounts;

   PROCEDURE getphonenumbersbyban (
      pi_ban             IN       NUMBER,
      po_phone_numbers   OUT      refcursor
   )
   IS
      v_subscriber_no   VARCHAR2 (20)           := 'no value';
      v_phones_ob       phone_numbers_o;
      v_phones_tab      phone_numbers_t;

      CURSOR c_subscribers (pi_ban VARCHAR2)
      IS
         SELECT   *
             FROM (SELECT   sr.subscriber_no, sr.resource_number,
                            MAX (sr.resource_seq) sn
                       FROM subscriber_rsource sr
                      WHERE sr.ban = pi_ban AND sr.resource_type = 'N'
                   GROUP BY sr.subscriber_no, sr.resource_number)
         ORDER BY 1, 3 DESC, 2;

      rec_subscribers   c_subscribers%ROWTYPE;
   BEGIN
      v_phones_tab := phone_numbers_t ();

      FOR rec_subscribers IN c_subscribers (pi_ban)
      LOOP
         EXIT WHEN c_subscribers%NOTFOUND;

         IF rec_subscribers.subscriber_no != v_subscriber_no
         THEN
            v_phones_ob := phone_numbers_o (NULL, NULL);
            v_subscriber_no := rec_subscribers.subscriber_no;
            v_phones_ob.subscriber_no := rec_subscribers.subscriber_no;
            v_phones_ob.resource_number := rec_subscribers.resource_number;
            v_phones_tab.EXTEND;
            v_phones_tab (v_phones_tab.LAST) := v_phones_ob;
         END IF;
      END LOOP;

      OPEN po_phone_numbers
       FOR
          SELECT   *
              FROM TABLE (CAST (v_phones_tab AS phone_numbers_t))
          ORDER BY 1, 2;

      v_phones_tab.DELETE;
      v_phones_ob := NULL;
   EXCEPTION
      WHEN NO_DATA_FOUND
      THEN
         IF (po_phone_numbers%ISOPEN)
         THEN
            CLOSE po_phone_numbers;
         END IF;

         IF (c_subscribers%ISOPEN)
         THEN
            CLOSE c_subscribers;
         END IF;

         OPEN po_phone_numbers
          FOR
             SELECT NULL
               FROM DUAL
              WHERE 1 = 0;
      -- raise_application_error (-20102, 'No phone numbers were found');
      WHEN OTHERS
      THEN
         IF (po_phone_numbers%ISOPEN)
         THEN
            CLOSE po_phone_numbers;
         END IF;

         IF (c_subscribers%ISOPEN)
         THEN
            CLOSE c_subscribers;
         END IF;

         OPEN po_phone_numbers
          FOR
             SELECT NULL
               FROM DUAL
              WHERE 1 = 0;

         raise_application_error
                    (-20102,
                        'Get get phonenumbers by ban  Query Failed. Oracle:(['
                     || SQLCODE
                     || '] ['
                     || SQLERRM
                     || '])');
   END getphonenumbersbyban;

   PROCEDURE getcollectionhistory (
      banid      IN       NUMBER,
      fromdate   IN       DATE,
      todate     IN       DATE,
      results    OUT      refcursor
   )
   IS
   BEGIN
      IF (fromdate IS NULL OR todate IS NULL)
      THEN
         OPEN results
          FOR
             SELECT   c.col_actv_date, c.col_actv_type_ind, c.col_path_code,
                      c.col_step_num, c.col_actv_code, c.asgn_collector,
                      u.user_short_name, c.asgn_agency
                FROM  COLLECTION_ACT            CA,
                      WORK_POSITION_ASSIGNMENT  WPA,
                      COLLECTION                C,
                      USERS_LTD                 U
                WHERE
                      C.BAN = banid
                      AND C.ASGN_COLLECTOR = WPA.WPASN_WORK_POSITION_CODE (+)
                      AND C.COL_ACTV_DATE >= WPA.WPASN_EFFECTIVE_DATE (+)
                      AND C.COL_ACTV_DATE < NVL(WPA.WPASN_EXPIRATION_DATE (+), TO_DATE('3000-12-31','yyyy-mm-dd'))
                      AND C.COL_ACTV_CODE = CA.COL_ACTIVITY_CODE(+)
                      AND CA.ACTUAL_ACT_IND = 'Y'
                      AND WPA.wpasn_user_id = u.user_id (+)
             ORDER BY c.ent_seq_no DESC;
      ELSE
         OPEN results
          FOR
             SELECT   c.col_actv_date, c.col_actv_type_ind, c.col_path_code,
                      c.col_step_num, c.col_actv_code, c.asgn_collector,
                      u.user_short_name, c.asgn_agency
                FROM  COLLECTION_ACT            CA,
                      WORK_POSITION_ASSIGNMENT  WPA,
                      COLLECTION                C,
                      USERS_LTD                 U
                WHERE
                      C.BAN = banid
                      AND C.ASGN_COLLECTOR = WPA.WPASN_WORK_POSITION_CODE (+)
                      AND C.COL_ACTV_DATE >= WPA.WPASN_EFFECTIVE_DATE (+)
                      AND C.COL_ACTV_DATE < NVL(WPA.WPASN_EXPIRATION_DATE (+), TO_DATE('3000-12-31','yyyy-mm-dd'))
                      AND C.COL_ACTV_CODE = CA.COL_ACTIVITY_CODE(+)
                      AND CA.ACTUAL_ACT_IND = 'Y'
                      AND WPA.wpasn_user_id = u.user_id (+)
                      AND c.col_actv_date >= fromdate
                      AND c.col_actv_date <= todate
             ORDER BY c.ent_seq_no DESC;
      END IF;
   EXCEPTION
      WHEN NO_DATA_FOUND
      THEN
         OPEN results
          FOR
             SELECT NULL
               FROM DUAL;

         IF (results%ISOPEN)
         THEN
            CLOSE results;
         END IF;
      WHEN OTHERS
      THEN
         IF (results%ISOPEN)
         THEN
            CLOSE results;
         END IF;

         raise_application_error
                    (-20163,
                        'ERROR: ra_utility_pkg.getCollectionHistory(). SQL ['
                     || SQLCODE
                     || '] Error ['
                     || SQLERRM
                     || ']',
                     TRUE
                    );
   END getcollectionhistory;

--------------------------------------------------------------------------------------------------------------------
   
   FUNCTION getminutepoolingsubscribers (
      pi_ban            IN       NUMBER,
      po_subscribers    OUT      refcursor,
      v_error_message   OUT      VARCHAR2
   ) RETURN NUMBER
   
   IS
      i_result        NUMBER (1);
      v_logical_date  DATE;
   
   BEGIN   
      BEGIN    
         getlogicaldate(v_logical_date);
         
         OPEN po_subscribers
         	FOR
	          	SELECT DISTINCT sa.soc, sa.subscriber_no, s.sub_status,
	                             s.sub_status_date, sa.effective_date
	            FROM service_agreement sa,
	                             subscriber s,
	                             soc soc
	            WHERE sa.ban = pi_ban
	            AND s.customer_id = sa.ban
	            AND s.subscriber_no = sa.subscriber_no
	            AND (TRUNC(sa.expiration_date) > TRUNC(v_logical_date)
	                 OR sa.expiration_date IS NULL)
	            AND sa.service_type = 'P'
	            AND sa.soc = soc.soc
	            AND soc.mp_ind = 'Y'
	            AND EXISTS (
	            	SELECT 1
	                FROM service_feature sf, feature f
	                WHERE sf.ban = sa.ban
	                AND sf.subscriber_no = sa.subscriber_no
	                AND (TRUNC(sf.ftr_expiration_date) > TRUNC(v_logical_date)
	                     OR sf.ftr_expiration_date IS NULL)
	                AND sf.feature_code = f.feature_code
	                AND f.feature_type = 'P'
	            );

         i_result := numeric_true;
      
      EXCEPTION
         WHEN NO_DATA_FOUND
         THEN
            v_error_message := err_no_data_found;
            i_result := numeric_false;
            IF (po_subscribers%ISOPEN)
            THEN
               CLOSE po_subscribers;
            END IF;
         WHEN OTHERS
         THEN
            IF (po_subscribers%ISOPEN)
            THEN
               CLOSE po_subscribers;
            END IF;
            raise_application_error
                     (-20102,
                         'GetMinutePoolingSubscribers Query Failed. Oracle:(['
                      || SQLCODE
                      || '] ['
                      || SQLERRM
                      || '])');
      
      END;
      RETURN i_result;
      
   END getminutepoolingsubscribers;

-------------------------------------------------------------------------------------------------

   FUNCTION getminutepoolingsubsbycoverage (
      pi_ban             IN       NUMBER,
      pi_coverage_types  IN       t_parameter_name_array,
      po_subscribers     OUT      refcursor,
      v_error_message    OUT      VARCHAR2
   ) RETURN NUMBER
      
   IS
      i_result         NUMBER (1);
      v_logical_date   DATE;
      v_subs_tab	   t_subscriber_array;
   
      CURSOR c_subscribers (pi_ban VARCHAR2, pi_coverage_types t_parameter_name_array, v_logical_date DATE)
      IS
      	 SELECT sf.subscriber_no
         FROM service_feature sf, feature f, pooling_groups pg
         WHERE sf.ban = pi_ban
         AND (TRUNC(sf.ftr_expiration_date) > TRUNC(v_logical_date)
         	OR sf.ftr_expiration_date IS NULL)
         AND sf.feature_code = f.feature_code
         AND f.feature_type = 'P'
         AND f.pool_group_id = pg.pool_group_id
         AND pg.coverage_type IN (
         	SELECT * 
         	FROM TABLE
         		(CAST(pi_coverage_types AS t_parameter_name_array))
         );

   BEGIN
      BEGIN
         v_subs_tab := NULL;
         getlogicaldate(v_logical_date);
             
         IF pi_coverage_types.COUNT > 0
         THEN
          	 OPEN c_subscribers (pi_ban, pi_coverage_types, v_logical_date);
			 FETCH c_subscribers
		 	 BULK COLLECT INTO v_subs_tab;
		 	 
	         OPEN po_subscribers
	         FOR
	            SELECT DISTINCT sa.soc, sa.subscriber_no, s.sub_status,
	            	s.sub_status_date, sa.effective_date
	            FROM service_agreement sa, subscriber s, soc soc
	            WHERE sa.ban = pi_ban
	            AND s.customer_id = sa.ban
	            AND s.subscriber_no = sa.subscriber_no
	            AND (TRUNC(sa.expiration_date) > TRUNC(v_logical_date)
	                 OR sa.expiration_date IS NULL)
	            AND sa.service_type = 'P'
	            AND sa.soc = soc.soc
	            AND soc.mp_ind = 'Y'
	            AND (s.subscriber_no) IN (
	            	SELECT *
	            	FROM TABLE (CAST (v_subs_tab AS t_subscriber_array)));
	
	         i_result := numeric_true;
         
         ELSE
             v_error_message := err_invalid_input;
             i_result := numeric_false;             
         END IF;
         
         v_subs_tab.DELETE;
      	 
      EXCEPTION
         WHEN NO_DATA_FOUND
         THEN
            v_error_message := err_no_data_found;
            i_result := numeric_false;
            IF (po_subscribers%ISOPEN)
            THEN
               CLOSE po_subscribers;
            END IF;
            
         WHEN OTHERS
         THEN
            IF (po_subscribers%ISOPEN)
            THEN
               CLOSE po_subscribers;
            END IF;
            raise_application_error
                     (-20102,
                         'GetMinutePoolingSubsByCoverage Query Failed. Oracle:(['
                      || SQLCODE
                      || '] ['
                      || SQLERRM
                      || '])');
      
      END;
      RETURN i_result;
      
   END getminutepoolingsubsbycoverage;
   
-------------------------------------------------------------------------------------------------

   PROCEDURE getsharepriceplansubscribers (
      pi_ban           IN       NUMBER,
      po_subscribers   OUT      refcursor
   )
   IS
   BEGIN
      OPEN po_subscribers
       FOR
          SELECT   *
              FROM (SELECT sa.soc, sa.subscriber_no, s.sub_status,
                           spp.max_subscribers_number, s.sub_status_date,
                           sa.effective_date, sa.soc_seq_no,
                           spp.price_plan_group spp_gr,
                           DECODE (s.sub_status,
                                   'C', 1,
                                   2
                                  ) virt_1,
                           RANK () OVER (PARTITION BY sa.ban, sa.subscriber_no, sa.product_type, sa.service_type ORDER BY sa.soc_seq_no DESC)
                                                                       virt_2
                      FROM service_agreement sa,
                           soc_group sg,
                           shareable_price_plan spp,
                           subscriber s,
                           logical_date ld
                     WHERE sa.ban = pi_ban
                       AND sa.soc = sg.soc
                       AND sa.service_type = 'P'
                       AND sg.gp_soc = spp.price_plan_group(+)
                       AND s.customer_id = sa.ban
                       AND s.subscriber_no = sa.subscriber_no
                       AND ld.logical_date_type = 'O'
                       AND (   s.sub_status = 'C'
                            OR (    s.sub_status != 'C'
                                AND (   TRUNC (sa.expiration_date) >
                                                       TRUNC (ld.logical_date)
                                     OR sa.expiration_date IS NULL
                                    )
                               )
                           ))
             WHERE spp_gr IS NOT NULL AND (virt_1 = 2 OR virt_2 = 1)
          ORDER BY 1, 5 DESC;
   EXCEPTION
      WHEN NO_DATA_FOUND
      THEN
         IF (po_subscribers%ISOPEN)
         THEN
            CLOSE po_subscribers;
         END IF;

         OPEN po_subscribers
          FOR
             SELECT NULL
               FROM DUAL
              WHERE 1 = 0;
      WHEN OTHERS
      THEN
         IF (po_subscribers%ISOPEN)
         THEN
            CLOSE po_subscribers;
         END IF;

         OPEN po_subscribers
          FOR
             SELECT NULL
               FROM DUAL
              WHERE 1 = 0;

         raise_application_error
             (-20102,
                 'Get GetShare Price Plan Subscribers Query Failed. Oracle:(['
              || SQLCODE
              || '] ['
              || SQLERRM
              || '])');
   END getsharepriceplansubscribers;

-----------------------------------------------------------------------------------------------------
   PROCEDURE getshareablesocsubscribers (
      pi_ban               IN       NUMBER,
      pi_price_plan_code   IN       VARCHAR2,
      po_subscribers       OUT      refcursor
   )
   IS
   BEGIN
      OPEN po_subscribers
       FOR
          SELECT sa1.soc, sa1.subscriber_no, s1.sub_status,
                 sa1.effective_date
            FROM service_agreement sa1,
                 subscriber s1,
                 soc,
                 (SELECT sa.ban, sa.subscriber_no, sa.expiration_date,
                         sa.effective_date
                    FROM service_agreement sa, subscriber s, logical_date ld
                   WHERE sa.ban = pi_ban
                     AND sa.soc = pi_price_plan_code
                     AND (   TRUNC (sa.expiration_date) >
                                                       TRUNC (ld.logical_date)
                          OR sa.expiration_date IS NULL
                         )
                     AND ld.logical_date_type = 'O'
                     AND s.customer_id = sa.ban
                     AND s.subscriber_no = sa.subscriber_no
                     AND s.sub_status != 'C') sub_p
           WHERE s1.customer_id = sub_p.ban
             AND s1.subscriber_no = sub_p.subscriber_no
             AND sa1.ban = s1.customer_id
             AND sa1.subscriber_no = s1.subscriber_no
             AND (   TRUNC (sa1.effective_date) <
                                                 TRUNC (sub_p.expiration_date)
                  OR sub_p.expiration_date IS NULL
                 )
             AND (   TRUNC (sub_p.effective_date) <
                                                   TRUNC (sa1.expiration_date)
                  OR sa1.expiration_date IS NULL
                 )
             AND (   TRUNC (sa1.expiration_date) > TRUNC (SYSDATE)
                  OR TRUNC (sa1.expiration_date) IS NULL
                 )
             AND soc.soc = sa1.soc
             AND soc.soc_level_code = 'P'
             AND soc.service_type NOT IN ('P', 'O', 'T');
   EXCEPTION
      WHEN NO_DATA_FOUND
      THEN
         IF (po_subscribers%ISOPEN)
         THEN
            CLOSE po_subscribers;
         END IF;

         OPEN po_subscribers
          FOR
             SELECT NULL
               FROM DUAL
              WHERE 1 = 0;
      WHEN OTHERS
      THEN
         IF (po_subscribers%ISOPEN)
         THEN
            CLOSE po_subscribers;
         END IF;

         OPEN po_subscribers
          FOR
             SELECT NULL
               FROM DUAL
              WHERE 1 = 0;

         raise_application_error
                      (-20102,
                          'Get Price Plan Subscribers Query Failed. Oracle:(['
                       || SQLCODE
                       || '] ['
                       || SQLERRM
                       || '])');
   END getshareablesocsubscribers;

----------------------------------------------------------------------------------------
  PROCEDURE isFeatureCategoryExist (
      pi_ban               IN       NUMBER,
      pi_category_code     IN       VARCHAR2,
      po_category_exist    OUT      NUMBER )
IS

v_logical_date Date;

 
   BEGIN
   
   po_category_exist := 0;
   getlogicaldate (v_logical_date);
   
   SELECT count (*) into po_category_exist FROM service_feature sf,
       feature_category_relation fcr
       WHERE  sf.ban = pi_ban
       AND soc_effective_date <= v_logical_date
       AND  sf.feature_code = fcr.feature_code 
       AND fcr.category_code = pi_category_code
       AND ftr_effective_date <= v_logical_date
       AND (   TRUNC (ftr_expiration_date) > v_logical_date
       OR ftr_expiration_date IS NULL );
       
EXCEPTION
   
   WHEN OTHERS
   THEN
       raise_application_error
                      (-20102,
                          'Is Feature Category Exist Query Failed. Oracle:(['
                       || SQLCODE
                       || '] ['
                       || SQLERRM
                       || '])');
END isFeatureCategoryExist;
-------------------------------------------------------------------------
  PROCEDURE getlogicaldate (po_logical_date OUT DATE)
   IS
   BEGIN
      SELECT logical_date
        INTO po_logical_date
        FROM logical_date ld
       WHERE ld.logical_date_type = 'O';
   END getlogicaldate;
 -------------------------------------------------------------------------  

FUNCTION get_corporate_name (corp_id in number) return varchar2 is
    corp_name varchar2(500);

  BEGIN
    select obj_name into corp_name from ch_objects where obj_id = corp_id;    
    RETURN corp_name;
    exception
       when NO_DATA_FOUND then
         return null;
       when OTHERS then
         raise;

  END get_corporate_name;

-------------------------------------------------------------------------------------------------

   FUNCTION getminutepoolingsubcounts (
      pi_ban             	IN       NUMBER,
	  pi_pool_group_id		IN		 NUMBER,
      po_pooling_counts 	OUT      refcursor,
      v_error_message       OUT      VARCHAR2
   ) RETURN NUMBER
      
   IS
      i_result         NUMBER (1);
      v_logical_date   DATE;
      v_agreements_tab service_agreement_t;
	  v_pooling_tab   pooling_subscribers_t;
     
   BEGIN	  

	  SELECT service_agreement_o(ban,subscriber_no,soc,effective_date,service_type,expiration_date) bulk collect INTO v_agreements_tab
	  FROM (SELECT BAN, SUBSCRIBER_NO, SOC, EFFECTIVE_DATE, SERVICE_TYPE, EXPIRATION_DATE
	  		FROM service_agreement
	  		WHERE BAN = pi_ban );

      BEGIN
          getlogicaldate(v_logical_date);

		  IF (pi_pool_group_id != -1) THEN
		  	-- by Pool Group Id
	  		SELECT pooling_subscriber_o(subscriber_no,pool_group_id) bulk collect INTO v_pooling_tab
	  		FROM (SELECT sf.subscriber_no, f.pool_group_id
			 	  FROM service_feature sf,
			 	  	   feature f			
			 	  WHERE sf.BAN = pi_ban
			 	  		AND sf.FEATURE_CODE = f.FEATURE_CODE 
				   		AND f.feature_type = 'P'						
				   		AND (TRUNC(sf.ftr_expiration_date) > TRUNC(v_logical_date) OR sf.ftr_expiration_date IS NULL)
						AND f.pool_group_id = pi_pool_group_id );
		  ELSE
		      -- pooling all (identical to statement in true clause above but missing last filter condition)
	  		SELECT pooling_subscriber_o(subscriber_no,pool_group_id) bulk collect INTO v_pooling_tab
	  		FROM (SELECT sf.SUBSCRIBER_NO, f.POOL_GROUP_ID
			 	  FROM service_feature sf,
			 	  	   feature f			
			 	  WHERE sf.BAN = pi_ban
			 	  		AND sf.FEATURE_CODE = f.FEATURE_CODE 
				   		AND f.feature_type = 'P'
				   		AND (TRUNC(sf.ftr_expiration_date) > TRUNC(v_logical_date) OR sf.ftr_expiration_date IS NULL));
		  END IF;

		 OPEN po_pooling_counts 
			FOR
			  SELECT DISTINCT v.SOC, v.SUBSCRIBER_NO, v.SUB_STATUS, v.SUB_STATUS_DATE, v.EFFECTIVE_DATE, v.BAN, f.POOL_GROUP_ID 
			  FROM (SELECT sa.BAN, sa.SOC, sa.SUBSCRIBER_NO, s.SUB_STATUS, s.SUB_STATUS_DATE, sa.EFFECTIVE_DATE 
	  				FROM subscriber s, 
	       				(SELECT * FROM TABLE (CAST (v_agreements_tab AS service_agreement_t))) sa 
	  				WHERE sa.SERVICE_TYPE='P' 
	  					  AND (TRUNC(sa.expiration_date) > TRUNC(v_logical_date) OR sa.expiration_date IS NULL) 
						  AND sa.BAN = s.CUSTOMER_ID 
						  AND	sa.SUBSCRIBER_NO = s.SUBSCRIBER_NO ) v, 
		    		soc,
					(SELECT * FROM TABLE (CAST (v_pooling_tab AS pooling_subscribers_t))) f 
			  WHERE v.SOC = soc.SOC  
	  				AND soc.mp_ind='Y' 
	  				AND v.SUBSCRIBER_NO = f.SUBSCRIBER_NO
			  ORDER BY f.pool_group_id, v.SOC;

	     i_result := numeric_true;
         
         v_agreements_tab.DELETE;
      	       	 
      EXCEPTION
         WHEN NO_DATA_FOUND
         THEN
            v_error_message := err_no_data_found;
            i_result := numeric_false;
            IF (po_pooling_counts%ISOPEN)
            THEN
               CLOSE po_pooling_counts;
            END IF;
            
         WHEN OTHERS
         THEN
            IF (po_pooling_counts%ISOPEN)
            THEN
               CLOSE po_pooling_counts;
            END IF;
            raise_application_error
                     (-20102,
                         'getpoolingsubscribercounts Query Failed. Oracle:(['
                      || SQLCODE
                      || '] ['
                      || SQLERRM
                      || '])');
      
      END;
      RETURN i_result;
      
   END getminutepoolingsubcounts;

-------------------------------------------------------------------------------------------------

   FUNCTION getdollarpoolingsubcounts (
      pi_ban             	IN       NUMBER,
	  pi_product_type 		IN		 VARCHAR2,
      po_pooling_counts 	OUT      refcursor,
      v_error_message       OUT      VARCHAR2
   ) RETURN NUMBER
      
   IS
      i_result         NUMBER (1);
      v_logical_date   DATE;
      v_agreements_tab service_agreement_t;
	  v_pooling_tab   pooling_subscribers_t;
     
   BEGIN	  

	  SELECT service_agreement_o(ban,subscriber_no,soc,effective_date,service_type,expiration_date) bulk collect INTO v_agreements_tab
	  FROM (SELECT BAN, SUBSCRIBER_NO, SOC, EFFECTIVE_DATE, SERVICE_TYPE, EXPIRATION_DATE
	  		FROM service_agreement
	  		WHERE BAN = pi_ban );

      BEGIN
          getlogicaldate(v_logical_date);

		  IF (pi_product_type = 'I') THEN
	  		SELECT pooling_subscriber_o(subscriber_no,pool_group_id) bulk collect INTO v_pooling_tab
	  		FROM (SELECT sf.subscriber_no, f.pool_group_id
		  		  FROM service_feature sf,
				 	   feature f			
				  WHERE sf.BAN = pi_ban
			 	  	    AND sf.FEATURE_CODE = f.FEATURE_CODE 
				  		AND f.feature_type = 'A'						
				  		AND (TRUNC(sf.ftr_expiration_date) > TRUNC(v_logical_date) OR sf.ftr_expiration_date IS NULL));

		  	-- dollar pooling IDEN
		 	OPEN po_pooling_counts 
			  FOR
			  	SELECT DISTINCT v.SOC, v.SUBSCRIBER_NO, v.SUB_STATUS, v.SUB_STATUS_DATE, v.EFFECTIVE_DATE, v.BAN, f.POOL_GROUP_ID 
			  	FROM (SELECT sa.BAN, sa.SOC, sa.SUBSCRIBER_NO, s.SUB_STATUS, s.SUB_STATUS_DATE, sa.EFFECTIVE_DATE 
	  				  FROM subscriber s, 
	       				   (SELECT * FROM TABLE (CAST (v_agreements_tab AS service_agreement_t))) sa 
	  				  WHERE sa.SERVICE_TYPE='P' 
	  					    AND (TRUNC(sa.expiration_date) > TRUNC(v_logical_date) OR sa.expiration_date IS NULL) 
						    AND sa.BAN = s.CUSTOMER_ID 
						    AND	sa.SUBSCRIBER_NO = s.SUBSCRIBER_NO ) v, 
		    		  soc,
					  (SELECT * FROM TABLE (CAST (v_pooling_tab AS pooling_subscribers_t))) f 
 			    WHERE v.SOC = soc.SOC  
	  				  AND v.SUBSCRIBER_NO = f.SUBSCRIBER_NO
					  AND soc.min_commit_amt > 0 
					  AND soc.mci_ind IN ('I', 'C') 
			    ORDER BY f.pool_group_id, v.SOC;

		  ELSE
			-- dollar pooling PCS
		 	OPEN po_pooling_counts 
			  FOR
			  	SELECT DISTINCT v.SOC, v.SUBSCRIBER_NO, v.SUB_STATUS, v.SUB_STATUS_DATE, v.EFFECTIVE_DATE, v.BAN, null POOL_GROUP_ID 
			  	FROM (SELECT sa.BAN, sa.SOC, sa.SUBSCRIBER_NO, s.SUB_STATUS, s.SUB_STATUS_DATE, sa.EFFECTIVE_DATE 
	  				  FROM subscriber s, 
	       				   (SELECT * FROM TABLE (CAST (v_agreements_tab AS service_agreement_t))) sa 
	  				  WHERE sa.SERVICE_TYPE='P' 
	  					    AND (TRUNC(sa.expiration_date) > TRUNC(v_logical_date) OR sa.expiration_date IS NULL) 
						    AND sa.BAN = s.CUSTOMER_ID 
						    AND	sa.SUBSCRIBER_NO = s.SUBSCRIBER_NO ) v, 
		    		  soc
 			    WHERE v.SOC = soc.SOC   
					  AND soc.min_commit_amt > 0 
					  AND soc.mci_ind = 'I' 
			    ORDER BY pool_group_id, v.SOC;
		  
		  END IF;

	     i_result := numeric_true;
         
         v_agreements_tab.DELETE;
      	       	 
      EXCEPTION
         WHEN NO_DATA_FOUND
         THEN
            v_error_message := err_no_data_found;
            i_result := numeric_false;
            IF (po_pooling_counts%ISOPEN)
            THEN
               CLOSE po_pooling_counts;
            END IF;
            
         WHEN OTHERS
         THEN
            IF (po_pooling_counts%ISOPEN)
            THEN
               CLOSE po_pooling_counts;
            END IF;
            raise_application_error
                     (-20102,
                         'getdollarpoolingsubcounts Query Failed. Oracle:(['
                      || SQLCODE
                      || '] ['
                      || SQLERRM
                      || '])');
      
      END;
      RETURN i_result;
      
   END getdollarpoolingsubcounts;
-------------------------------------------------------------------------------------------------

   FUNCTION getzeropoolingsubscribercounts (
	pi_ban             IN   NUMBER,
	pi_pool_group_id   IN   NUMBER,
	po_pooling_counts  OUT  refcursor,
	v_error_message    OUT  VARCHAR2
   ) RETURN NUMBER
     
   IS
      i_result         NUMBER (1);
      v_logical_date   DATE;
      v_agreements_tab service_agreement_t;
      v_pooling_tab    pooling_subscribers_t;


   BEGIN
      BEGIN
	 	 v_agreements_tab := NULL;
         v_pooling_tab := NULL;
         getlogicaldate(v_logical_date);
         
	 	 SELECT service_agreement_o(ban, subscriber_no, soc, effective_date, service_type, expiration_date) 
		    BULK COLLECT INTO v_agreements_tab

	  	 FROM (SELECT ban, subscriber_no, soc, effective_date, service_type, expiration_date
			   FROM service_agreement
			   WHERE BAN = pi_ban);






		 IF (pi_pool_group_id = -1) THEN
			SELECT pooling_subscriber_o(subscriber_no, pool_group_id) BULK COLLECT INTO v_pooling_tab
			FROM (
  			  SELECT sf.subscriber_no, f.pool_group_id
			  FROM service_feature sf, feature f, inclus_by_period ibp, soc s
			  WHERE sf.feature_code = f.feature_code
      			    AND sf.soc = ibp.soc
      			    AND sf.soc = s.soc
	                AND (TRUNC(sf.ftr_expiration_date) > TRUNC(v_logical_date) OR sf.ftr_expiration_date IS NULL)
	                AND ibp.inclusive_mou = 0
            	    AND ibp.feature_code = 'STD'
            	    AND sf.service_type = 'P'
            	    AND s.mp_ind = 'Y'
            	    AND (f.pool_group_id IS NOT NULL OR f.pool_group_id != '')
            	    AND sf.ban = pi_ban
            	    AND (
            	  	     (f.feature_type = 'P') 
            	  	     OR 
            		     (f.pool_group_id IN (
            		    				    SELECT f1.pool_group_id
            			  				    FROM service_feature sf1, feature f1
            			  				    WHERE sf.ban = sf1.ban
            			        		  		  AND sf.subscriber_no = sf1.subscriber_no
            			  						  AND sf1.feature_code = f1.feature_code
            			        				  AND f1.feature_type = 'P'
            			  						  AND (TRUNC(sf1.ftr_expiration_date) > TRUNC(v_logical_date) OR sf1.ftr_expiration_date IS NULL)))
            		    )		   		  
              UNION
              SELECT sf.subscriber_no, f.pool_group_id
              FROM service_feature sf, feature f, soc s, pp_uc_rate pur
              WHERE sf.soc = pur.soc
	                AND sf.feature_code = f.feature_code
	                AND sf.soc = s.soc
	                AND (TRUNC(sf.ftr_expiration_date) > TRUNC(v_logical_date) OR sf.ftr_expiration_date IS NULL)
	                AND (f.pool_group_id IS NOT NULL OR f.pool_group_id != '')
	                AND sf.service_type = 'P'
	                AND s.mp_ind = 'Y'
	                AND NOT EXISTS (
                                SELECT 1 
            				    FROM inclus_by_period ibp 
            				    WHERE pur.soc = ibp.soc 
            				  	      AND pur.period_value_code = ibp.period_value_code
            				  		  AND ibp.feature_code = 'STD')
            	    AND sf.ban = pi_ban						
              	    AND (
            	  	     (f.feature_type = 'P')
            	  	     OR
            		     (f.pool_group_id IN (
            					            SELECT f1.pool_group_id
            			  				    FROM service_feature sf1, feature f1
            							    WHERE sf.ban = sf1.ban
            							  		  AND sf.subscriber_no = sf1.subscriber_no
            									  AND sf1.feature_code = f1.feature_code
            									  AND f1.feature_type = 'P'
            									  AND (TRUNC(sf1.ftr_expiration_date) > TRUNC(v_logical_date) OR sf1.ftr_expiration_date IS NULL)))
            		     )
		    );

		 ELSE

	        SELECT pooling_subscriber_o(subscriber_no, pool_group_id) BULK COLLECT INTO v_pooling_tab
			FROM (
			  SELECT sf.subscriber_no, f.pool_group_id
			  FROM service_feature sf, feature f, inclus_by_period ibp, soc s
			  WHERE sf.feature_code = f.feature_code 
			     	AND sf.soc = ibp.soc
			  	    AND sf.soc = s.soc
			        AND (TRUNC(sf.ftr_expiration_date) > TRUNC(v_logical_date) OR sf.ftr_expiration_date IS NULL)
			  	    AND sf.service_type = 'P'
			  	    AND s.mp_ind = 'Y'
			        AND ibp.inclusive_mou = 0
	  				AND ibp.feature_code = 'STD'
      				AND f.pool_group_id = pi_pool_group_id
	  				AND sf.ban = pi_ban
				    AND (
	  	   				 (f.feature_type = 'P')
		   				 OR
					     (f.pool_group_id IN (
			      						SELECT f1.pool_group_id
				  						FROM service_feature sf1, feature f1
				  						WHERE sf.ban = sf1.ban
				  							  AND sf.subscriber_no = sf1.subscriber_no
				  							  AND sf1.feature_code = f1.feature_code
			      		                      AND f1.feature_type = 'P'
				  		                      AND (TRUNC(sf1.ftr_expiration_date) > TRUNC(v_logical_date) OR sf1.ftr_expiration_date IS NULL))
						 )
		                )
  			  UNION
  			  SELECT sf.subscriber_no, f.pool_group_id
              FROM service_feature sf, feature f, soc s, pp_uc_rate pur
              WHERE sf.feature_code = f.feature_code
  	                AND sf.soc = s.soc
  	                AND sf.soc = pur.soc	  
                    AND (TRUNC(sf.ftr_expiration_date) > TRUNC(v_logical_date) OR sf.ftr_expiration_date IS NULL)
  	                AND sf.service_type = 'P'
     	            AND s.mp_ind = 'Y'
  	                AND NOT EXISTS (
  	                                SELECT 1 
  				                    FROM inclus_by_period ibp 
  			                        WHERE pur.soc = ibp.soc 
  			      		                  AND pur.period_value_code = ibp.period_value_code
  			      		                  AND ibp.feature_code = 'STD')	  
  	                AND f.pool_group_id = pi_pool_group_id
  	                AND sf.ban = pi_ban
    	            AND (
  	   	                 (f.feature_type = 'P') 
  		                 OR 
  		                 (f.pool_group_id IN (
  		                                      SELECT f1.pool_group_id
  				  							  FROM service_feature sf1, feature f1
  				                              WHERE sf.ban = sf1.ban
  				  		                            AND sf.subscriber_no = sf1.subscriber_no
  				  		                            AND sf1.feature_code = f1.feature_code
  						                            AND f1.feature_type = 'P'
  				  		                            AND (TRUNC(sf1.ftr_expiration_date) > TRUNC(v_logical_date) OR sf1.ftr_expiration_date IS NULL))
  		                 )
		                )
			);

		 END IF;

	     OPEN po_pooling_counts
	     FOR
	        SELECT DISTINCT sa.soc, sa.subscriber_no, s.sub_status,
	     	  s.sub_status_date, sa.effective_date, sa.ban, ps.pool_group_id
	        FROM subscriber s, soc soc,
			   (SELECT * FROM TABLE (CAST(v_agreements_tab AS service_agreement_t))) sa,
			   (SELECT * FROM TABLE (CAST(v_pooling_tab AS pooling_subscribers_t))) ps
	        WHERE sa.ban = pi_ban
	        AND s.customer_id = sa.ban
	        AND s.subscriber_no = sa.subscriber_no
	        AND (TRUNC(sa.expiration_date) > TRUNC(v_logical_date)
	           OR sa.expiration_date IS NULL)
	        AND sa.service_type = 'P'
	        AND sa.soc = soc.soc
	        AND soc.mp_ind = 'Y'
	        AND s.subscriber_no = ps.subscriber_no
			AND NOT EXISTS (
		       SELECT 1
		       FROM service_feature sf, soc soc, uc_rated_feature ucf, inclus_by_period ibp, pooling_groups pg
		       WHERE sf.ban = pi_ban
		       AND (TRUNC(sf.ftr_expiration_date) > TRUNC(v_logical_date)
			      OR sf.ftr_expiration_date IS NULL)
		       AND sf.service_type != 'P'
		       AND sf.soc = ucf.soc
		       AND ucf.mpc_ind = 'Y'
		       AND sf.soc = ibp.soc
		       AND ibp.inclusive_mou > 0
		       AND sf.soc = soc.soc
		       AND ps.pool_group_id = pg.pool_group_id
		       AND pg.coverage_type = decode(soc.coverage_type, 'G', 'O', soc.coverage_type)
		       AND s.subscriber_no = sf.subscriber_no);
	
         i_result := numeric_true;
         

         v_pooling_tab.DELETE;
	 	 v_agreements_tab.DELETE;
      	 
      EXCEPTION
         WHEN NO_DATA_FOUND
         THEN
            v_error_message := err_no_data_found;
            i_result := numeric_false;
            IF (po_pooling_counts%ISOPEN)
            THEN
               CLOSE po_pooling_counts;
            END IF;
            
         WHEN OTHERS
         THEN
            IF (po_pooling_counts%ISOPEN)
            THEN
               CLOSE po_pooling_counts;
            END IF;
            raise_application_error
                     (-20102,
                         'getzeropoolingsubscribercounts Query Failed. Oracle:(['
                      || SQLCODE
                      || '] ['
                      || SQLERRM
                      || '])');
      
      END;
      RETURN i_result;

   END getzeropoolingsubscribercounts;

-------------------------------------------------------------------------------------------------

	FUNCTION getLastCreditCheckResultByBan (
		pi_ban         							IN  	NUMBER,
		pi_product_type 						IN 		CHAR,
		po_credit_class 						OUT		VARCHAR2,
		po_credit_limit 						OUT		NUMBER,
		po_deposit_amt  						OUT		NUMBER,
		po_credit_result2 					OUT		VARCHAR2,
		po_beacon_score 						OUT		NUMBER,
		po_credit_referral_flag 		OUT		VARCHAR2,
		po_french_message 					OUT		VARCHAR2,
		po_credit_req_sts 					OUT		CHAR,
		po_sin  										OUT		VARCHAR2,
		po_drivr_licns_no 					OUT		VARCHAR2,
		po_date_of_birth  					OUT		DATE,
		po_credit_card_no  					OUT		VARCHAR2,
		po_crd_card_exp_date 				OUT		DATE,
		po_incorporation_no  				OUT		VARCHAR2,
		po_incorporation_date 			OUT		DATE,
		po_credit_date  						OUT		DATE,
		po_credit_param_type 				OUT		CHAR,
		po_dep_chg_rsn_cd  					OUT		VARCHAR2,
		po_selected_market_account 	OUT		NUMBER,
		po_selected_company_name 		OUT		VARCHAR2,
		po_credit_card_first6               OUT VARCHAR2,
		po_credit_card_last4                OUT VARCHAR2
		)
		
		RETURN NUMBER
		
		IS
		business_seq_no  NUMBER(9);
		business_line    NUMBER(9);
		CURSOR c_credit_result
		IS
			SELECT ch.credit_class, ch.credit_limit, cd.deposit_amt, ch.credit_result_2,
       				beacon_score, ccd.credit_referral_flg, ccd.french_message,
       				ch.credit_req_sts, ch.SIN, ch.drivr_licns_no, ch.date_of_birth,
       				ch.credit_card_no, ch.crd_card_exp_date, ch.incorporation_no,
       				ch.incorporation_date, ch.credit_date, ch.credit_param_type,
       				ch.dep_chg_rsn_cd, cbl.market_account, cbl.company_name, ch.business_l_seq_no, ch.business_l_line,
       				ch.pymt_card_first_six_str,ch.pymt_card_last_four_str
  		FROM credit_history ch, crd_deposit cd, credit_class_decision ccd, crd_business_list cbl
 			WHERE ch.ban = pi_ban
   					AND ch.crd_seq_no =
          			(SELECT MAX (crd_seq_no)
             			FROM credit_history ch2
            			WHERE ch2.ban = ch.ban
              					AND (ch2.credit_req_sts = 'D' OR ch2.credit_req_sts IS NULL))
   					AND (ch.credit_req_sts = 'D' OR ch.credit_req_sts IS NULL)
   					AND cd.deposit_seq_no(+) = ch.deposit_seq_no
   					AND cd.product_type(+) = pi_product_type
   					AND ccd.credit_message_cd(+) = SUBSTR (ch.credit_result_2, 1, 3) 
   					AND cbl.business_l_seq_no(+) = ch.business_l_seq_no 
   					AND cbl.crd_seq_no(+) = ch.crd_seq_no;

     credit_rec  c_credit_result%ROWTYPE;
     
     
     CURSOR c_selected_crd_business
     IS
     	 SELECT market_account, company_name
     	 FROM crd_business_list
     	 WHERE business_l_seq_no = business_seq_no AND business_l_line = business_line;
     	 
     credit_business_rec c_selected_crd_business%ROWTYPE;

		BEGIN
      OPEN c_credit_result;
      FETCH c_credit_result
       INTO credit_rec;

      IF c_credit_result%FOUND
      THEN
      	po_credit_class := credit_rec.credit_class;
      	po_credit_limit := credit_rec.credit_limit;
      	po_deposit_amt := credit_rec.deposit_amt;
				po_credit_result2  := credit_rec.credit_result_2;
				po_beacon_score := credit_rec.beacon_score;
				po_credit_referral_flag := credit_rec.credit_referral_flg;
				po_french_message := credit_rec.french_message;
				po_credit_req_sts := credit_rec.credit_req_sts;
				po_sin := credit_rec.sin;
				po_drivr_licns_no := credit_rec.drivr_licns_no;
				po_date_of_birth  := credit_rec.date_of_birth;
				po_credit_card_no := credit_rec.credit_card_no;
				po_crd_card_exp_date  := credit_rec.crd_card_exp_date;
				po_incorporation_no := credit_rec.incorporation_no;
				po_incorporation_date := credit_rec.incorporation_date;
				po_credit_date := credit_rec.credit_date;
				po_credit_param_type := credit_rec.credit_param_type;
				po_dep_chg_rsn_cd := credit_rec.dep_chg_rsn_cd;
				po_selected_market_account := credit_rec.market_account;
				po_selected_company_name := credit_rec.company_name;
				business_seq_no := credit_rec.business_l_seq_no;
				business_line := credit_rec.business_l_line;
				po_credit_card_first6 := credit_rec.pymt_card_first_six_str;
				po_credit_card_last4 := credit_rec.pymt_card_last_four_str;
				
		IF business_seq_no IS NOT NULL AND po_selected_market_account IS NULL
				THEN
					IF business_line IS NULL
					THEN
						business_line := 0;
					END IF;
					
					OPEN 	c_selected_crd_business;
					FETCH c_selected_crd_business
						INTO credit_business_rec;
						
					IF c_selected_crd_business%FOUND
					THEN
						po_selected_market_account := credit_business_rec.market_account;
						po_selected_company_name := credit_business_rec.company_name;
					END IF;     
      	END IF;
      		RETURN numeric_true;
      END IF;

			RETURN numeric_false;   		
		EXCEPTION
   WHEN OTHERS
   THEN
       raise_application_error
                      (-20102,
                          'getLastCreditCheckResultByBan Query Failed. Oracle:(['
                       || SQLCODE
                       || '] ['
                       || SQLERRM
                       || '])');

	END getLastCreditCheckResultByBan;
		
	
	PROCEDURE getBillingParamNoOfInvoice (
		banId in number,
		po_noOfInvoice OUT NUMBER,
		po_media_category OUT VARCHAR2,
		po_bill_format    OUT VARCHAR2)
	is
	  BEGIN
	    select bp_no_of_copies, bp_media_category, bp_bill_format 
	    into po_noOfInvoice, po_media_category, po_bill_format 
	    from billing_parameters bp, ADDRESS_NAME_LINK anl, logical_date ld 
	    where bp.bp_ban= banId
		    AND LD.LOGICAL_DATE_TYPE='O'
	        AND  NVL(BP.BP_EFFECTIVE_DATE, TO_DATE('19600101','YYYYMMDD')) <= TRUNC(ld.LOGICAL_DATE)
	        AND  NVL(BP.BP_EXPIRATION_DATE, TO_DATE('47001231','YYYYMMDD')) > TRUNC(ld.LOGICAL_DATE)
	        AND  ANL.FOREIGN_SEQ_NO = BP.BP_SEQ_NO
	        AND  ANL.BAN = BP.BP_BAN
	        AND  ANL.LINK_TYPE IN ( 'B','M' )      
	        AND  ANL.EFFECTIVE_DATE <= TRUNC(ld.LOGICAL_DATE)
	        AND  NVL(ANL.EXPIRATION_DATE, TO_DATE('47001231','YYYYMMDD')) > TRUNC(ld.LOGICAL_DATE);
	    exception
	       when NO_DATA_FOUND then
	         po_noOfInvoice := 0;
	       when OTHERS then
	         raise;
	
	  END getBillingParamNoOfInvoice;
	  
------------------------------------------------------
    PROCEDURE getaccountbyimsi(pi_imsi_id IN VARCHAR2, po_account_id OUT NUMBER)
	 IS
	    CURSOR c_account
	      IS
		SELECT distinct sr1.ban
	        FROM subscriber_rsource sr1
	          WHERE sr1.imsi_number = pi_imsi_id
	            AND sr1.resource_type = 'Q'
	            AND sr1.record_exp_date = TO_DATE ('12/31/4700', 'mm/dd/yyyy');
	
	
	
	      v_account_id   NUMBER (9);
	      v_counter   INTEGER       := 0;
	   BEGIN
	      OPEN c_account;
	
	      LOOP
	         FETCH c_account
	          INTO v_account_id;
	
	         EXIT WHEN c_account%NOTFOUND;
	         v_counter := v_counter + 1;
	         po_account_id := v_account_id;
	      END LOOP;
	
	      CLOSE c_account;
	
	      IF v_counter > 1
	      THEN
	         RAISE multipleaccountfound;
	      ELSIF v_counter = 0
	      THEN
	    	raise bannotfound;
	      END IF;
	   EXCEPTION
	   When bannotfound Then
	   	RAISE_APPLICATION_ERROR(-20101,'rat_Utility pkg: Account not found');
	   WHEN multipleaccountfound
	      THEN
		 RAISE_APPLICATION_ERROR(-20102,'rat_Utility pkg: Multiple Account found ');
	      WHEN OTHERS
	      THEN
	         IF c_account%ISOPEN
	         THEN
	            CLOSE c_account;
	         END IF;
	         RAISE;
   END getaccountbyimsi;
   
   
   PROCEDURE getPCSNetworkCountByBAN (
   	pi_ban IN  NUMBER,
   	po_counts OUT REFCURSOR)
   IS
   BEGIN   		
   		OPEN po_counts
   		FOR
				SELECT S.SUB_STATUS,
   		       SUM(DECODE(pd.unit_esn, 
             						'100000000000000000', 1,
			  								0)) hspa,
			  		 SUM(DECODE(pd.unit_esn, 
              					'100000000000000000', 0,
			  								1)) cdma 
  		FROM physical_device pd, subscriber s
 			WHERE pd.customer_id = pi_ban
   		AND pd.customer_id = s.customer_id
   		AND pd.subscriber_no = s.subscriber_no
   		AND pd.product_type = 'C' 
   		AND pd.esn_seq_no = (SELECT MAX (ESN_SEQ_NO) 
   		                     FROM physical_device pd1 
   		                     WHERE pd1.customer_id=pd.customer_id AND 
   		                           pd1.subscriber_no = pd.subscriber_no)
   		GROUP BY S.SUB_STATUS;
		
		EXCEPTION
      WHEN NO_DATA_FOUND
      THEN
         IF (po_counts%ISOPEN)
         THEN
            CLOSE po_counts;
         END IF;

         OPEN po_counts
          FOR
             SELECT NULL
               FROM DUAL
              WHERE 1 = 0;
      WHEN OTHERS
      THEN
         IF (po_counts%ISOPEN)
         THEN
            CLOSE po_counts;
         END IF;

         OPEN po_counts
          FOR
             SELECT NULL
               FROM DUAL
              WHERE 1 = 0;

         raise_application_error
             (-20102,
                 'getPCSNetworkCountByBAN Query Failed. Oracle:(['
              || SQLCODE
              || '] ['
              || SQLERRM
              || '])');
   END getPCSNetworkCountByBAN;	
------------------------------------------------------

	PROCEDURE getlwaccountinfobyban (
		pi_ban                         IN       NUMBER,
		po_account_type                OUT      CHAR,
		po_account_sub_type            OUT      CHAR,
		po_gl_segment                  OUT      VARCHAR2,
		po_gl_subsegment               OUT      VARCHAR2,
		po_brand_ind                   OUT      NUMBER,
		po_col_path_code               OUT      VARCHAR2,
		po_ar_balance                  OUT      NUMBER,
		po_cu_age_bucket_0             OUT      NUMBER,
		po_cu_past_due_amt             OUT      NUMBER,
		po_active_subs                 OUT      NUMBER,
		po_reserved_subs               OUT      NUMBER,
		po_suspended_subs              OUT      NUMBER,
		po_cancelled_subs              OUT      NUMBER,
		po_active_subs_all             OUT      NUMBER,
		po_reserved_subs_all           OUT      NUMBER,
		po_suspended_subs_all          OUT      NUMBER,
		po_cancelled_subs_all          OUT      NUMBER,
		po_status_actv_rsn_code		   OUT	    VARCHAR2,
		po_ban_status				   OUT	    CHAR,
		po_home_province			   OUT	    VARCHAR2,
		po_bill_cycle                   OUT      NUMBER,
		po_cycle_close_day             OUT      NUMBER,
		po_dealer_code                 OUT      VARCHAR2,
		po_sales_rep_code              OUT      VARCHAR2,
		po_email_address               OUT      VARCHAR2,
		po_lang_pref                   OUT      VARCHAR2
	)
	IS
	CURSOR c_lwa
	IS
			SELECT ba.account_type,
						 ba.account_sub_type,
       			 ba.gl_segment,
       			 ba.gl_subsegment,
       			 ba.brand_id,
       			 ba.col_path_code,
       			 ba.ar_balance,
       			 cs.effective_date cs_effective_date,
       			 cs.cu_age_bucket_0,
       			 cs.nx_age_bucket_0,
       			 cs.fu_age_bucket_0,
       			 cs.cu_past_due_amt,
       			 cs.nx_past_due_amt,
       			 cs.fu_past_due_amt,
       			 ba.status_actv_rsn_code,
	   			 ba.ban_status,
				 ba.home_province,
				 ba.bill_cycle,
				 bc.cycle_close_day,
				 substr(c.dealer_code, 1,10) dealer_code, 
				 substr(c.dealer_code, 11) sales_rep_code,
				 c.email_address,c.lang_pref
  		FROM billing_account ba, collection_status cs, customer c, cycle bc
 			WHERE ba.ban = pi_ban 
 			  AND cs.ban(+) = ba.ban 
 			  AND ba.customer_id=c.customer_id 
 			  AND ba.bill_cycle=bc.cycle_code ;

   rec_lwa       		c_lwa%ROWTYPE;
   


   		v_logical_date  	DATE;
      v_account_type         VARCHAR2 (1);
      v_account_sub_type     VARCHAR2 (1);
	BEGIN
		getlogicaldate (v_logical_date);
		OPEN c_lwa;

    FETCH c_lwa
    	INTO rec_lwa;

    IF c_lwa%FOUND
    THEN	
    	po_account_type := rec_lwa.account_type;
    	po_account_sub_type := rec_lwa.account_sub_type;
    	v_account_type := rec_lwa.account_type;
    	v_account_sub_type := rec_lwa.account_sub_type;
    	po_gl_segment := rec_lwa.gl_segment;
			po_gl_subsegment := rec_lwa.gl_subsegment;
			po_brand_ind := rec_lwa.brand_id;
			po_col_path_code := rec_lwa.col_path_code;
			po_ar_balance := rec_lwa.ar_balance;
			po_ban_status := rec_lwa.ban_status;
			po_home_province := rec_lwa.home_province;
			po_status_actv_rsn_code := rec_lwa.status_actv_rsn_code;
			po_bill_cycle := rec_lwa.bill_cycle;
			po_cycle_close_day := rec_lwa.cycle_close_day;
			po_dealer_code := rec_lwa.dealer_code;
			po_sales_rep_code := rec_lwa.sales_rep_code;
			po_email_address :=rec_lwa.email_address;
			po_lang_pref  :=rec_lwa.lang_pref;

			IF TRUNC (rec_lwa.cs_effective_date) = TRUNC (v_logical_date)
			THEN
  			po_cu_age_bucket_0 := rec_lwa.cu_age_bucket_0;
  			po_cu_past_due_amt := rec_lwa.cu_past_due_amt;
			ELSIF TRUNC (rec_lwa.cs_effective_date + 1) = TRUNC (v_logical_date)
			THEN
  			po_cu_age_bucket_0 := rec_lwa.nx_age_bucket_0;
  			po_cu_past_due_amt := rec_lwa.nx_past_due_amt;
			ELSE
  			po_cu_age_bucket_0 := rec_lwa.fu_age_bucket_0;
  			po_cu_past_due_amt := rec_lwa.fu_past_due_amt;
			END IF;
			
			getSubscriberCounts (pi_ban, 
													 v_account_type, 
													 v_account_sub_type,
													 po_active_subs,
													 po_reserved_subs,
													 po_suspended_subs,
													 po_cancelled_subs,
													 po_active_subs_all,
													 po_reserved_subs_all,
													 po_suspended_subs_all,
													 po_cancelled_subs_all);
    ELSE
         RAISE bannotfound;
    END IF;
   EXCEPTION
      WHEN bannotfound
      THEN
         IF c_lwa%ISOPEN
         THEN
            CLOSE c_lwa;
         END IF;

         raise_application_error (-20101, 'BAN  Not Found');
      WHEN OTHERS THEN RAISE;
	END getlwaccountinfobyban;

	PROCEDURE getSubscriberCounts(
		pi_ban							 					 IN				NUMBER,
		p_account_type  		 					 IN				VARCHAR2,
    p_account_sub_type   					 IN				VARCHAR2,
		po_active_subs                 OUT      NUMBER,
		po_reserved_subs               OUT      NUMBER,
		po_suspended_subs              OUT      NUMBER,
		po_cancelled_subs              OUT      NUMBER,
		po_active_subs_all             OUT      NUMBER,
		po_reserved_subs_all           OUT      NUMBER,
		po_suspended_subs_all          OUT      NUMBER,
		po_cancelled_subs_all          OUT      NUMBER
	 )
	 IS
   CURSOR c_sub_summary_all  /* query for getAllActiveSubscriberCount(), getAllSuspendedSubscriberCount() and getAllCancelledSubscriberCount()*/
   IS
    SELECT SUM (DECODE (sub_status,
                        'A', 1,
                             0
                        )) a, 
           SUM (DECODE (sub_status,
                        'S', 1,
                             0
                        )) s,
           SUM (DECODE (sub_status,
                        'C', 1,
                             0
                         )) c, 
           COUNT (*) total
    FROM subscriber
    WHERE customer_id = pi_ban;
               
   CURSOR c_sub_summary /* query for getActiveSubscriberCount(), getSuspendedSubscriberCount() and getCancelledSubscriberCount()*/
   IS
    SELECT SUM (DECODE (sub_status,
                        'A', 1,
                             0
                        )) a, 
           SUM (DECODE (sub_status,
                        'S', 1,
                             0
                        )) s,
           SUM (DECODE (sub_status,
                        'C', 1,
                             0
                         )) c, 
           COUNT (*) total
    FROM subscriber
    WHERE customer_id = pi_ban
          AND (   product_type IN ('C', 'I')
                 OR (p_account_type IN ('I', 'B') AND p_account_sub_type = 'M'
                    )
                 OR (p_account_type = 'I' AND p_account_sub_type = 'J')
               );

    CURSOR c_sub_summary_rsrv_all 
    IS         
			SELECT   COUNT (*) total
      FROM subscriber s, physical_device p
      WHERE s.customer_id = pi_ban
            AND s.sub_status = 'R'
            AND p.customer_id = s.customer_id
            AND p.subscriber_no = s.subscriber_no
            AND p.product_type = s.product_type
            AND p.esn_level = 1
            AND p.expiration_date IS NULL;
                           
    CURSOR c_sub_summary_rsrv 
      IS
         SELECT COUNT (*) total
           FROM subscriber s, physical_device p
          WHERE s.customer_id = pi_ban
            AND (   s.product_type IN ('C', 'I') AND s.sub_status = 'R'
                 OR (p_account_type IN ('I', 'B') AND p_account_sub_type = 'M'
                    )
                 OR (p_account_type = 'I' AND p_account_sub_type = 'J')
                )
            AND p.customer_id = s.customer_id
            AND p.subscriber_no = s.subscriber_no
            AND p.product_type = s.product_type
            AND p.esn_level = 1
            AND p.expiration_date IS NULL;	
            
    ss_rec                 c_sub_summary%ROWTYPE;
    ss_all_rec             c_sub_summary_all%ROWTYPE;

    v_rsrv                 NUMBER (4);
    v_rsrv_all             NUMBER (4);
 
	BEGIN
			OPEN c_sub_summary_all;
      FETCH c_sub_summary_all
       INTO ss_all_rec;
      po_active_subs_all := NVL (ss_all_rec.a, 0);
      po_suspended_subs_all := NVL (ss_all_rec.s, 0);
      po_cancelled_subs_all := NVL (ss_all_rec.c, 0);
      CLOSE c_sub_summary_all;

      
      OPEN c_sub_summary;
      FETCH c_sub_summary
       INTO ss_rec;
      po_active_subs := NVL (ss_rec.a, 0);
      po_suspended_subs := NVL (ss_rec.s, 0);
      po_cancelled_subs := NVL (ss_rec.c, 0);
      CLOSE c_sub_summary;

      
      OPEN c_sub_summary_rsrv_all;
      FETCH c_sub_summary_rsrv_all
       INTO v_rsrv_all;
      po_reserved_subs_all := v_rsrv_all;
      CLOSE c_sub_summary_rsrv_all;


      OPEN c_sub_summary_rsrv;
      FETCH c_sub_summary_rsrv
       INTO v_rsrv;
      po_reserved_subs := v_rsrv;

      CLOSE c_sub_summary_rsrv;	
   EXCEPTION
      WHEN OTHERS
      THEN
      IF c_sub_summary_all%ISOPEN
      THEN
      		CLOSE c_sub_summary_all;
      END IF;
      
      IF c_sub_summary%ISOPEN
      THEN
      		CLOSE c_sub_summary;
      END IF;

      IF c_sub_summary_rsrv_all%ISOPEN
      THEN
      		CLOSE c_sub_summary_rsrv_all;
      END IF;      
      IF c_sub_summary_rsrv%ISOPEN
      THEN
      		CLOSE c_sub_summary_rsrv;
      END IF;
      
      RAISE;

	END getSubscriberCounts;

-------------------------------------------------------------------------------------------------
	
	
   FUNCTION getSubscribersByServiceFamily(
	pi_ban		  IN		NUMBER,
	pi_family_type    IN		VARCHAR2,
	pi_effective_date IN		DATE,
	po_subscribers    OUT      	refcursor,
	v_error_message   OUT      	VARCHAR2
   ) RETURN NUMBER
	 
   IS
      i_result         NUMBER (1);
      v_agreements_tab service_agreement_t;
	    

   BEGIN
      BEGIN
 	 v_agreements_tab := NULL;

         
	 SELECT service_agreement_o(ban, subscriber_no, soc, effective_date, service_type, expiration_date) 
	   BULK COLLECT INTO v_agreements_tab
	 FROM (SELECT ban, subscriber_no, soc, effective_date, service_type, expiration_date
		   FROM service_agreement
		   WHERE BAN = pi_ban 
		   AND (TRUNC(expiration_date) > TRUNC(pi_effective_date) OR expiration_date IS NULL));

	OPEN po_subscribers 
	 FOR
	 	
	 	SELECT DISTINCT sa.SUBSCRIBER_NO 
	 	 FROM (SELECT * FROM TABLE (CAST (v_agreements_tab AS service_agreement_t))) sa,
	 	       soc_group sg, soc_family_group sfg
   	  	 WHERE sa.soc = sg.soc AND sg.gp_soc = sfg.soc_group 
	 	  	AND sfg.family_type = pi_family_type 
	 	  	AND TRUNC(sa.effective_date) <= TRUNC(pi_effective_date);
		    
	   i_result := numeric_true;
         
           v_agreements_tab.DELETE;
      	       	 
      EXCEPTION
         WHEN NO_DATA_FOUND
         THEN
            v_error_message := err_no_data_found;
            i_result := numeric_false;
            IF (po_subscribers%ISOPEN)
            THEN
               CLOSE po_subscribers;
            END IF;
            
         WHEN OTHERS
         THEN
            IF (po_subscribers%ISOPEN)
            THEN
               CLOSE po_subscribers;
            END IF;
            raise_application_error
                     (-20102,
                         'getSubscribersByServiceFamily Query Failed. Oracle:(['
                      || SQLCODE
                      || '] ['
                      || SQLERRM
                      || '])');
      
      END;
      RETURN i_result;
      
   END getSubscribersByServiceFamily;		    

-------------------------------------------------------------------------------------------------
		    		    

   FUNCTION getSubscribersBySharingGroups(
		pi_ban		  		IN		NUMBER,
		pi_data_sharing_group_codes 	IN       	t_parameter_name_array,
		pi_effective_date 		IN		DATE,
		po_subscribers    		OUT      	refcursor,
		v_error_message   		OUT    		VARCHAR2
	 ) RETURN NUMBER
	 
  IS
      i_result         NUMBER (1);
      v_agreements_tab service_agreement_t;
   

   BEGIN
      BEGIN
 	 v_agreements_tab := NULL;

         
	 SELECT service_agreement_o(ban, subscriber_no, soc, effective_date, service_type, expiration_date) 
	   BULK COLLECT INTO v_agreements_tab
	 FROM (SELECT ban, subscriber_no, soc, effective_date, service_type, expiration_date
		   FROM service_agreement
		   WHERE BAN = pi_ban 
		   AND (TRUNC(expiration_date) > TRUNC(pi_effective_date) OR expiration_date IS NULL));

	OPEN po_subscribers 
	 FOR
	 	
	 	SELECT DISTINCT sa.SUBSCRIBER_NO, ssg.ALLOW_SHARING_GROUP_CD, ssg.ALLOW_SHARING_ACCESS_TYPE_CD 
	 	 FROM (SELECT * FROM TABLE (CAST (v_agreements_tab AS service_agreement_t))) sa,
	 	       SOC_GROUP sg, SOC_GRP_ALLOW_SHARING_GRP ssg
   	  	 WHERE  sa.soc = sg.soc AND sg.gp_soc = ssg.gp_soc
   	  	 AND TRUNC(sa.effective_date) <= TRUNC(pi_effective_date)
   	  	 AND ssg.ALLOW_SHARING_GROUP_CD IN (
		          	SELECT * 
		          	FROM TABLE
		          		(CAST(pi_data_sharing_group_codes AS t_parameter_name_array)))
   	  	 ORDER BY 1,2,3;
		    
	   i_result := numeric_true;
         
           v_agreements_tab.DELETE;
      	       	 
      EXCEPTION
         WHEN NO_DATA_FOUND
         THEN
            v_error_message := err_no_data_found;
            i_result := numeric_false;
            IF (po_subscribers%ISOPEN)
            THEN
               CLOSE po_subscribers;
            END IF;
            
         WHEN OTHERS
         THEN
            IF (po_subscribers%ISOPEN)
            THEN
               CLOSE po_subscribers;
            END IF;
            raise_application_error
                     (-20102,
                         'getSubscribersBySharingGroups Query Failed. Oracle:(['
                      || SQLCODE
                      || '] ['
                      || SQLERRM
                      || '])');
      
      END;
      RETURN i_result;
      
   END getSubscribersBySharingGroups;
   
   FUNCTION getSubscriberDataSharingInfo(
		pi_ban		  						IN		NUMBER,
		po_sub_data_sharing_info		  	OUT    	refcursor,
		v_error_message   					OUT		VARCHAR2
   ) RETURN NUMBER
	 
   IS
      i_result         NUMBER (1);
      v_agreements_tab service_agreement_t;

   BEGIN
      BEGIN
 	     v_agreements_tab := NULL;
         
	     SELECT service_agreement_o(ban, subscriber_no, soc, effective_date, service_type, expiration_date) BULK COLLECT INTO v_agreements_tab
	     FROM (
	        SELECT ban, subscriber_no, soc, effective_date, service_type, expiration_date
		    FROM service_agreement
		    WHERE ban = pi_ban 
		    AND service_type <> 'O'
		    AND TRUNC (effective_date) <= TRUNC(SYSDATE)
		    AND (TRUNC(expiration_date) > TRUNC(SYSDATE) OR expiration_date IS NULL)
		 );

         OPEN po_sub_data_sharing_info 
	        FOR
	        
		    SELECT sa.subscriber_no, sa.soc, s.soc_description, s.soc_description_f, sa.service_type, prr.rate, sgasg.allow_sharing_group_cd, sgasg.allow_sharing_access_type_cd
		    FROM (SELECT * FROM TABLE (CAST (v_agreements_tab AS service_agreement_t))) sa, pp_rc_rate prr, soc_group sg, soc_grp_allow_sharing_grp sgasg, soc s
		    WHERE sa.ban = pi_ban
		    AND ((sa.service_type = 'P' AND prr.feature_code = 'STD') OR sa.service_type <> 'P')
		    AND prr.soc = sa.soc
		    AND sa.soc = sg.soc
		    AND sg.gp_soc = sgasg.gp_soc
		    AND s.soc = sa.soc
		    
		    UNION
		
			SELECT DISTINCT sa.subscriber_no, sa.soc, s.soc_description, s.soc_description_f, sa.service_type, prr.rate, NULL as allow_sharing_group_cd, NULL as allow_sharing_access_type_cd
			FROM (SELECT * FROM TABLE (CAST (v_agreements_tab AS service_agreement_t))) sa, pp_rc_rate prr, soc s
			WHERE sa.subscriber_no IN (
				SELECT subscriber_no FROM (SELECT * FROM TABLE (CAST (v_agreements_tab AS service_agreement_t))) sa, soc_group sg, soc_grp_allow_sharing_grp sgasg
				WHERE sa.soc = sg.soc
				AND sg.gp_soc = sgasg.gp_soc
				AND sa.ban = pi_ban
			)
			AND sa.ban = pi_ban
			AND ((sa.service_type = 'P' AND prr.feature_code = 'STD') OR sa.service_type <> 'P')
			AND sa.soc = prr.soc
			AND sa.soc = s.soc
			AND NOT EXISTS (SELECT 1 FROM soc_group sg, soc_grp_allow_sharing_grp sgasg WHERE sg.gp_soc = sgasg.gp_soc AND sg.soc = sa.soc);
		    
		    i_result := numeric_true;
            v_agreements_tab.DELETE;
      	       	 
         EXCEPTION
         WHEN NO_DATA_FOUND
         THEN
            v_error_message := err_no_data_found;
            i_result := numeric_false;
            IF (po_sub_data_sharing_info%ISOPEN)
            THEN
               CLOSE po_sub_data_sharing_info;
            END IF;
            
         WHEN OTHERS
         THEN
            IF (po_sub_data_sharing_info%ISOPEN)
            THEN
               CLOSE po_sub_data_sharing_info;
            END IF;
            raise_application_error(-20102, 'getSubscriberDataSharingInfo Query Failed. Oracle:([' || SQLCODE || '] [' || SQLERRM || '])');
      END;
      RETURN i_result;
      
   END getSubscriberDataSharingInfo;
   
   FUNCTION getVersion RETURN VARCHAR2
   IS
   BEGIN
	   RETURN version_no;
	END getVersion;


	 PROCEDURE getAccountsByPhoneNumber (
	 	pi_phoneNumber	IN 		VARCHAR2,
	 	po_accounts	OUT		refcursor
	 )IS
	 BEGIN
		OPEN po_accounts
             FOR
                select distinct ba.ban, ba.ban_status, ba.account_type, ba.account_sub_type, ba.sys_creation_date,
				ba.start_service_date, substr(c.dealer_code, 1,10) dealer_code, substr(c.dealer_code, 11) sales_rep_code,
				c.customer_id, nd.first_name, nd.last_business_name, middle_initial, name_title, ad.adr_primary_ln,
				ad.adr_city, ad.adr_province, ad.adr_postal, adr_type, adr_secondary_ln, adr_country, adr_zip_geo_code,
				adr_state_code, civic_no, civic_no_suffix, adr_st_direction, adr_street_name, adr_street_type, adr_designator,
				adr_identifier, adr_box, unit_designator, unit_identifier, adr_area_nm, adr_qualifier, adr_site,
				adr_compartment, c.acc_password, ba.status_actv_code, ba.status_actv_rsn_code, nd.additional_title,
				ba.status_last_date,  to_number(NVL(ba.brand_id,'1')) as brandId 
				from   subscriber s, billing_account ba, customer c, address_name_link anl, address_data ad, name_data nd
				where   s.subscriber_no = pi_phoneNumber and 
						ba.ban = s.customer_ban  and
						c.customer_id = ba.customer_id and
						anl.customer_id= c.customer_id and
						anl.expiration_date  is null and
						anl.link_type = 'B' and
						nd.name_id = anl.name_id and
						ad.address_id = anl.address_id
				union
				select distinct ba.ban, ba.ban_status, ba.account_type, ba.account_sub_type, ba.sys_creation_date,
				ba.start_service_date, substr(c.dealer_code, 1,10) dealer_code, substr(c.dealer_code, 11) sales_rep_code,
				c.customer_id, nd.first_name, nd.last_business_name, middle_initial, name_title, ad.adr_primary_ln,
				ad.adr_city, ad.adr_province, ad.adr_postal, adr_type, adr_secondary_ln, adr_country, adr_zip_geo_code,
				adr_state_code, civic_no, civic_no_suffix, adr_st_direction, adr_street_name, adr_street_type, adr_designator ,
				adr_identifier, adr_box, unit_designator, unit_identifier, adr_area_nm, adr_qualifier, adr_site, 
				adr_compartment, c.acc_password, ba.status_actv_code, ba.status_actv_rsn_code, nd.additional_title,
				ba.status_last_date, to_number(NVL(ba.brand_id,'1')) as brandId 
				from   subscriber_rsource sr, subscriber s, billing_account ba, customer c, address_name_link anl, 
						address_data ad, name_data nd
				where  sr.resource_number = pi_phoneNumber and
						sr.resource_type='N' and
						s.subscriber_no=  sr.subscriber_no and
						ba.ban=s.customer_ban and
						c.customer_id = ba.customer_id and
						anl.customer_id= c.customer_id and
						anl.expiration_date  is null and
						anl.link_type = 'B' and
						nd.name_id = anl.name_id and
						ad.address_id=anl.address_id;

      EXCEPTION
         WHEN NO_DATA_FOUND
         THEN
            IF (po_accounts%ISOPEN)
            THEN
               CLOSE po_accounts;
            END IF;

         WHEN OTHERS
         THEN
            IF (po_accounts%ISOPEN)
            THEN
               CLOSE po_accounts;
            END IF;	 
	 END getAccountsByPhoneNumber;
	 
	 PROCEDURE getLastAccountsBySeatNumber (
	 	pi_seatNumber	IN 		VARCHAR2,
	 	po_accounts	OUT		refcursor
	 )IS
	 BEGIN
		OPEN po_accounts
             FOR
                select distinct ba.ban, ba.ban_status, ba.account_type, ba.account_sub_type, ba.sys_creation_date,
				ba.start_service_date, substr(c.dealer_code, 1,10) dealer_code, substr(c.dealer_code, 11) sales_rep_code,
				c.customer_id, nd.first_name, nd.last_business_name, middle_initial, name_title, ad.adr_primary_ln,
				ad.adr_city, ad.adr_province, ad.adr_postal, adr_type, adr_secondary_ln, adr_country, adr_zip_geo_code,
				adr_state_code, civic_no, civic_no_suffix, adr_st_direction, adr_street_name, adr_street_type, adr_designator ,
				adr_identifier, adr_box, unit_designator, unit_identifier, adr_area_nm, adr_qualifier, adr_site, 
				adr_compartment, c.acc_password, ba.status_actv_code, ba.status_actv_rsn_code, nd.additional_title,
				ba.status_last_date, to_number(NVL(ba.brand_id,'1')) as brandId 
				from   subscriber_rsource sr, subscriber s, billing_account ba, customer c, address_name_link anl, 
						address_data ad, name_data nd
				where  sr.resource_number = pi_seatNumber and
						sr.resource_type in ('V','L','I','O') and
						s.subscriber_no=  sr.subscriber_no and
						ba.ban=s.customer_ban and
						s.customer_ban=sr.ban and
						c.customer_id = ba.customer_id and
						anl.customer_id= c.customer_id and
						anl.expiration_date  is null and
						anl.link_type = 'B' and
						nd.name_id = anl.name_id and
						ad.address_id=anl.address_id and s.effective_date = (select max(s1.effective_date )
											from subscriber s1, subscriber_rsource sr1 
											where sr1.resource_number=sr.resource_number
											and sr1.resource_type in ('V','L','I','O')
											and s1.subscriber_no=sr1.subscriber_no );

      EXCEPTION
         WHEN NO_DATA_FOUND
         THEN
            IF (po_accounts%ISOPEN)
            THEN
               CLOSE po_accounts;
            END IF;

         WHEN OTHERS
         THEN
            IF (po_accounts%ISOPEN)
            THEN
               CLOSE po_accounts;
            END IF;	 
	 END getLastAccountsBySeatNumber;
	 
	 PROCEDURE getLastAccountsByPhoneNumber (
	 	pi_phoneNumber	IN 		VARCHAR2,
	 	po_accounts	OUT		refcursor
	 )IS
	 BEGIN
		OPEN po_accounts
             FOR
                select distinct ba.ban, ba.ban_status, ba.account_type, ba.account_sub_type, ba.sys_creation_date,
				ba.start_service_date, substr(c.dealer_code, 1,10) dealer_code, substr(c.dealer_code, 11) sales_rep_code,
				c.customer_id, nd.first_name, nd.last_business_name, middle_initial, name_title, ad.adr_primary_ln,
				ad.adr_city, ad.adr_province, ad.adr_postal, adr_type, adr_secondary_ln, adr_country, adr_zip_geo_code,
				adr_state_code, civic_no, civic_no_suffix, adr_st_direction, adr_street_name, adr_street_type, adr_designator,
				adr_identifier, adr_box, unit_designator, unit_identifier, adr_area_nm, adr_qualifier, adr_site,
				adr_compartment, c.acc_password, ba.status_actv_code, ba.status_actv_rsn_code, nd.additional_title,
				ba.status_last_date,  to_number(NVL(ba.brand_id,'1')) as brandId 
				from   subscriber s, billing_account ba, customer c, address_name_link anl, address_data ad, name_data nd
				where   s.subscriber_no = pi_phoneNumber and 
						ba.ban = s.customer_ban  and
						c.customer_id = ba.customer_id and
						anl.customer_id= c.customer_id and
						anl.expiration_date  is null and
						anl.link_type = 'B' and
						nd.name_id = anl.name_id and
						ad.address_id = anl.address_id and 
						s.sub_status_date = (select max(sub_status_date) 
											from subscriber s1 
											where s1.subscriber_no=s.subscriber_no ) 
				union
				select distinct ba.ban, ba.ban_status, ba.account_type, ba.account_sub_type, ba.sys_creation_date,
				ba.start_service_date, substr(c.dealer_code, 1,10) dealer_code, substr(c.dealer_code, 11) sales_rep_code,
				c.customer_id, nd.first_name, nd.last_business_name, middle_initial, name_title, ad.adr_primary_ln,
				ad.adr_city, ad.adr_province, ad.adr_postal, adr_type, adr_secondary_ln, adr_country, adr_zip_geo_code,
				adr_state_code, civic_no, civic_no_suffix, adr_st_direction, adr_street_name, adr_street_type, adr_designator ,
				adr_identifier, adr_box, unit_designator, unit_identifier, adr_area_nm, adr_qualifier, adr_site, 
				adr_compartment, c.acc_password, ba.status_actv_code, ba.status_actv_rsn_code, nd.additional_title,
				ba.status_last_date, to_number(NVL(ba.brand_id,'1')) as brandId 
				from   subscriber_rsource sr, subscriber s, billing_account ba, customer c, address_name_link anl, 
						address_data ad, name_data nd
				where  sr.resource_number = pi_phoneNumber and
						sr.resource_type='N' and
						s.subscriber_no=  sr.subscriber_no and
						ba.ban=s.customer_ban and
						c.customer_id = ba.customer_id and
						anl.customer_id= c.customer_id and
						anl.expiration_date  is null and
						anl.link_type = 'B' and
						nd.name_id = anl.name_id and
						ad.address_id=anl.address_id and 
						s.effective_date = (select max(s1.effective_date )
											from subscriber s1, subscriber_rsource sr1 
											where sr1.resource_number=sr.resource_number
											and sr1.resource_type='N' 
											and s1.subscriber_no=sr1.subscriber_no );

      EXCEPTION
         WHEN NO_DATA_FOUND THEN
            IF (po_accounts%ISOPEN) THEN
               CLOSE po_accounts;
            END IF;

         WHEN OTHERS THEN
            IF (po_accounts%ISOPEN) THEN
               CLOSE po_accounts;
            END IF;	 
	END getLastAccountsByPhoneNumber;

	FUNCTION getBanByPhoneNumber (
	 	pi_phoneNumber	IN 		VARCHAR2
	 ) RETURN NUMBER
	 IS
	 	po_ban NUMBER(9); 
	 BEGIN	 
		 SELECT customer_ban INTO po_ban FROM 
		 (SELECT customer_ban 
		FROM subscriber s1
		WHERE subscriber_no = pi_phoneNumber
		and sub_status!='C'
		UNION
		SELECT s2.customer_ban
		FROM   subscriber s2, subscriber_rsource sr
		WHERE  sr.resource_number= pi_phoneNumber
		and   sr.resource_status!='C' 
		and   sr.resource_type='N' 
		and sr.resource_seq = 
		 ( SELECT  max(sr2.resource_seq) 
		  FROM   subscriber_rsource   sr2 
		   WHERE   sr2.subscriber_no= sr.subscriber_no 
		   and  sr2.resource_type='N') 
		and s2.subscriber_no = sr.subscriber_no 
		and s2.sub_status!='C'
		);
		
		RETURN po_ban;
	EXCEPTION
         WHEN NO_DATA_FOUND
         THEN
            RETURN 0;

         WHEN OTHERS
         THEN
            RETURN 0;
	 END getBanByPhoneNumber;
-------------------------------------------------------------------------------------------------

	 PROCEDURE getAccountsByPostalCode (
	 	pi_lastName 	IN		VARCHAR2,
	 	pi_postalCode	IN		VARCHAR2,
	 	pi_maximum		IN		NUMBER,
	 	po_curAccounts	OUT		refcursor
	 )
	 IS
	 BEGIN
		 OPEN po_curAccounts
		 FOR
			 SELECT  DISTINCT ba.ban, ba.ban_status, ba.account_type,  ba.account_sub_type,
					  nvl(ba.start_service_date,ba.status_last_date), ba.start_service_date,
					  substr(c.dealer_code, 1,10) dealer_code, substr(c.dealer_code, 11) sales_rep_code, c.customer_id, 
					  nd.first_name, nd.last_business_name, middle_initial, name_title,
					  ad.adr_primary_ln, ad.adr_city, ad.adr_province, ad.adr_postal,
					  adr_type, adr_secondary_ln, adr_country, adr_zip_geo_code, adr_state_code,
					  civic_no, civic_no_suffix, adr_st_direction, adr_street_name, adr_street_type,
					  adr_designator, adr_identifier, adr_box, unit_designator, unit_identifier,
					  adr_area_nm, adr_qualifier, adr_site, adr_compartment,
					  c.acc_password, ba.status_actv_code, ba.status_actv_rsn_code,
					  nd.additional_title, ba.status_last_date, 
					  to_number(NVL(ba.brand_id,brand_telus)) as brandId
				FROM    billing_account ba,
					  customer c,
					  address_name_link anl,
					  address_data ad,
					  name_data nd
				WHERE   nd.control_name LIKE pi_lastName
					  and     anl.name_id = nd.name_id
					  and     anl.expiration_date  IS NULL
			          and     anl.link_type = 'B'
			          and     ad.address_id=anl.address_id
			          and     (ad.adr_postal= pi_postalCode
					  or ad.adr_zip_geo_code = pi_postalCode)
					  and     ba.ban = anl.customer_id
			          and     c.customer_id = anl.customer_id
			          and rownum <= pi_maximum;
			          
		EXCEPTION
	         WHEN NO_DATA_FOUND
	         THEN
	            IF po_curAccounts%ISOPEN THEN
	            	CLOSE po_curAccounts;
	            END IF;
	         WHEN OTHERS
	         THEN
	           	IF po_curAccounts%ISOPEN THEN
	            	CLOSE po_curAccounts;
	            END IF;
     END getAccountsByPostalCode;
     
	FUNCTION getBanBySeatNumber (
	 	pi_seatNumber	IN 	VARCHAR2
	 ) RETURN NUMBER
	 IS
	 	po_ban NUMBER(9); 
	 BEGIN	 
		SELECT s2.customer_ban INTO po_ban
        FROM   subscriber s2, subscriber_rsource sr
        WHERE  sr.resource_number = pi_seatNumber
        and   sr.resource_status!='C' 
        and   sr.resource_type  in ( 'V','L','I','O')
        and sr.resource_seq = 
         ( SELECT  max(sr2.resource_seq) 
          FROM   subscriber_rsource   sr2 
           WHERE   sr2.subscriber_no= sr.subscriber_no 
           and 	sr2.resource_number = sr.resource_number 
           and  sr2.resource_type  in ( 'V','L','I','O')) 
        and s2.subscriber_no = sr.subscriber_no 
        and s2.sub_status!='C' ;
		RETURN po_ban;
	EXCEPTION
         WHEN NO_DATA_FOUND
         THEN
            RETURN 0;
         WHEN OTHERS
         THEN
            RETURN 0;
END getBanBySeatNumber;

END;
/


SHO err
