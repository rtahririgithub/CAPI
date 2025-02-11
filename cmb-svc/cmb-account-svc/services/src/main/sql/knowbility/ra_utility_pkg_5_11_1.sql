/* Formatted on 2005/09/19 16:34 (Formatter Plus v4.8.0) */
CREATE OR REPLACE PACKAGE ra_utility_pkg
AS
 ------------------------------------------------------------------------
-- description: Package Utility_pkg containing procedures
--    for data retrieval from Knowbility database
--
--Version KB_82_4
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
-- 02-15-2005       Marina  Kuper         Allowed the future expiration date for address_name_link
-- 03-30-2005       Vladimir Tsitrin      Added 5 output fields to GetAccountInfoByBAN
-- 05-31-2005       Vladimir Tsitrin      Added method GetAccountsByBANs
-- 02-15-2005       Marina  Kuper         Changed method GetAccountsByBANs - errors handling
-- 07-28-2005     Vladimir Tsitrin    Added method GetAccountsByName and updated GetAccountsByBANs
-- 09-13-2005       Marina  Kuper       Added method GetServiceSubscriberCounts
-------------------------------------------------------------------------
   bannotfound                  EXCEPTION;
   PRAGMA EXCEPTION_INIT (bannotfound, -20101);

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
      po_bl_man_hndl_by_opid         OUT      NUMBER
   );

------------------------------------------------------------------------------
-- description: Procedure GetAccountsByBANs returns a cursor of AccountSummary
--              for an array of BANs.
------------------------------------------------------------------------------
   FUNCTION getaccountsbybans (
      i_ban_numbers     IN       t_ban_array,
      o_accounts        OUT      refcursor,
      v_error_message   OUT      VARCHAR2
   )
      RETURN NUMBER;

------------------------------------------------------------------------------
-- description: Procedure GetAccountsByName returns a cursor of AccountSummary
--              for given search parameters. Set v_First_Name = '' to search
--              by Business Name
------------------------------------------------------------------------------
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
      o_accounts             OUT      refcursor,
      i_has_more             OUT      NUMBER,
      v_error_message        OUT      VARCHAR2
   )
      RETURN NUMBER;

     ------------------------------------------------------------------------------
-- description: Procedure GetServiceSubscriberCount returns a cursor of Subscribers
--              for an array of ServiceCodes.
------------------------------------------------------------------------------
   FUNCTION getservicesubscribercounts (
      pi_service_codes     IN       t_service_codes,
      pi_include_expired   IN       NUMBER,
      pi_ban               IN       NUMBER,
      po_subscribers       OUT      refcursor,
      v_error_message      OUT      VARCHAR2
   )
      RETURN NUMBER;
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
      po_bl_man_hndl_by_opid         OUT      NUMBER
   )
   IS
      CURSOR c_account
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
                         bc.cycle_code, bc.cycle_close_day,
                         nbc.cycle_code next_bill_cycle,
                         nbc.cycle_close_day next_bill_cycle_close_day,
                         SUBSTR (c.dealer_code, 1, 10) dealer_code,
                         SUBSTR (c.dealer_code, 11) sales_rep_code,
                         c.customer_id, c.work_telno, c.work_tn_extno,
                         c.home_telno, c.birth_date, c.contact_telno,
                         c.contact_tn_extno, c.contact_faxno, c.acc_password,
                         c.customer_ssn, c.lang_pref, c.email_address,
                         c.drivr_licns_no, c.drivr_licns_state,
                         c.drivr_licns_exp_dt, c.incorporation_no,
                         c.incorporation_date, c.gur_cr_card_no,
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
                         fu_age_bucket_61_90, fu_age_bucket_91_plus
                    FROM billing_account ba,
                         customer c,
                         address_name_link anl,
                         address_data ad,
                         name_data nd,
                         ban_direct_debit bdd
--    ,memo m
         ,
                         CYCLE bc,
                         collection_status cs,
                         CYCLE nbc,
                         logical_date ld
                   WHERE ba.ban = pi_ban
                     AND c.customer_id = ba.customer_id
                     AND anl.customer_id = c.customer_id
                     AND (   TRUNC (anl.expiration_date) >
                                                       TRUNC (ld.logical_date)
                          OR anl.expiration_date IS NULL
                         )
                     AND anl.link_type = 'B'
                     AND ld.logical_date_type = 'O'
                     AND ad.address_id = anl.address_id
                     AND nd.name_id = anl.name_id
                     AND bdd.ban(+) = ba.ban
-- and     ba.ban=m.memo_ban(+)
-- and     m.memo_type(+)='3000'
                     AND bc.cycle_code(+) = ba.bill_cycle
                     AND nbc.cycle_code(+) = ba.bl_next_cycle
                     AND cs.ban(+) = ba.ban;

      acc_rec              c_account%ROWTYPE;

      CURSOR c_sub_summary (
         p_account_type       VARCHAR2,
         p_account_sub_type   VARCHAR2
      )
      IS
         SELECT SUM (DECODE (sub_status, 'A', 1, 0)) a,
                SUM (DECODE (sub_status, 'S', 1, 0)) s
                                                      --,sum(decode(sub_status,'R',1,0)) R
                ,
                SUM (DECODE (sub_status, 'C', 1, 0)) c, COUNT (*) total
           FROM subscriber
          WHERE customer_id = pi_ban
            AND (   product_type IN ('C', 'I')
                 OR (p_account_type IN ('I', 'B') AND p_account_sub_type = 'M'
                    )
                 OR (p_account_type = 'I' AND p_account_sub_type = 'J')
                );

      ss_rec               c_sub_summary%ROWTYPE;

      CURSOR c_sub_summary_rsrv (
         p_account_type       VARCHAR2,
         p_account_sub_type   VARCHAR2
      )
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

      v_rsrv               NUMBER (4);

      CURSOR c_sub_summary_all
      IS
         SELECT SUM (DECODE (sub_status, 'A', 1, 0)) a,
                SUM (DECODE (sub_status, 'S', 1, 0)) s
                                                      --,sum(decode(sub_status,'R',1,0)) R
                ,
                SUM (DECODE (sub_status, 'C', 1, 0)) c, COUNT (*) total
           FROM subscriber
          WHERE customer_id = pi_ban;

      ss_rec_all           c_sub_summary_all%ROWTYPE;

      CURSOR c_sub_summary_rsrv_all
      IS
         SELECT COUNT (*) rsrv_total
           FROM subscriber s, physical_device p
          WHERE s.customer_id = pi_ban
            AND s.sub_status = 'R'
            AND p.customer_id = s.customer_id
            AND p.subscriber_no = s.subscriber_no
            AND p.product_type = s.product_type
            AND p.esn_level = 1
            AND p.expiration_date IS NULL;

      v_rsrv_all           NUMBER (4);

      CURSOR c_consent_inds_cur
      IS
         SELECT cpui_cd
           FROM ban_cpui bc
          WHERE bc.ban = pi_ban;

      CURSOR c_collection
      IS
         SELECT   c.col_actv_code, c.col_actv_date,
                  TO_CHAR (c.col_actv_date, 'mm'), ca.severity_level
             FROM collection c, collection_act ca, logical_date ld
            WHERE c.ban = pi_ban
              AND c.col_actv_code = ca.col_activity_code
              AND TRUNC (c.col_actv_date) >
                           ADD_MONTHS (LAST_DAY (TRUNC (ld.logical_date)),
                                       -12)
              AND ld.logical_date_type = 'O'
--and  ca.severity_level>=3
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
         SELECT   pa.actv_amt, pa.actv_date
             FROM payment p, payment_activity pa
            WHERE p.ban = pi_ban
              AND (p.designation = 'B' OR p.designation IS NULL)
              AND pa.ban = p.ban
              AND pa.ent_seq_no = p.ent_seq_no
              AND pa.actv_code IN ('PYM', 'FNTT')
         ORDER BY pa.actv_date DESC, pa.ent_seq_no DESC, pa.actv_seq_no DESC;

      CURSOR c_contact_name
      IS
         SELECT nd.first_name, nd.last_business_name, nd.middle_initial,
                nd.name_title, nd.additional_title, nd.name_suffix
           FROM address_name_link anl, name_data nd, logical_date ld
          WHERE anl.ban = pi_ban
            AND (   TRUNC (anl.expiration_date) > TRUNC (ld.logical_date)
                 OR anl.expiration_date IS NULL
                )
            AND anl.link_type = 'C'
            AND ld.logical_date_type = 'O'
            AND nd.name_id = anl.name_id;

      CURSOR c_bill
      IS
         SELECT   bill_due_date
             FROM bill
            WHERE ban = pi_ban
              AND bill_conf_status = 'C'
              AND TO_CHAR (bill_due_date, 'yyyy') != '4700'
         ORDER BY bill_due_date DESC;

      CURSOR c_logical_date
      IS
         SELECT logical_date
           FROM logical_date
          WHERE logical_date_type = 'O';

      v_logical_date       DATE;
      cn_rec               c_contact_name%ROWTYPE;
      v_payment_amt        NUMBER (9, 2);
      v_payment_date       DATE;
      v_account_type       VARCHAR2 (1);
      v_account_sub_type   VARCHAR2 (1);
      c_consent_inds_rec   VARCHAR2 (1000);
   BEGIN
      OPEN c_logical_date;

      FETCH c_logical_date
       INTO v_logical_date;

      CLOSE c_logical_date;

      OPEN c_account;

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

         IF acc_rec.tax_gst_exmp_eff_dt <= TRUNC (SYSDATE)
         THEN
            po_tax_gst_exmp_ind := acc_rec.tax_gst_exmp_ind;
            po_tax_gst_exmp_exp_dt := acc_rec.tax_gst_exmp_exp_dt;
            po_tax_gst_exmp_rf_no := acc_rec.tax_gst_exmp_rf_no;
            po_tax_gst_exmp_eff_dt := acc_rec.tax_gst_exmp_eff_dt;
         END IF;

         IF acc_rec.tax_pst_exmp_eff_dt <= TRUNC (SYSDATE)
         THEN
            po_tax_pst_exmp_ind := acc_rec.tax_pst_exmp_ind;
            po_tax_pst_exmp_exp_dt := acc_rec.tax_pst_exmp_exp_dt;
            po_tax_pst_exmp_rf_no := acc_rec.tax_pst_exmp_rf_no;
            po_tax_pst_exmp_eff_dt := acc_rec.tax_pst_exmp_eff_dt;
         END IF;

         IF acc_rec.tax_hst_exmp_eff_dt <= TRUNC (SYSDATE)
         THEN
            po_tax_hst_exmp_ind := acc_rec.tax_hst_exmp_ind;
            po_tax_hst_exmp_exp_dt := acc_rec.tax_hst_exmp_exp_dt;
            po_tax_hst_exmp_rf_no := acc_rec.tax_hst_exmp_rf_no;
            po_tax_hst_exmp_eff_dt := acc_rec.tax_hst_exmp_eff_dt;
         END IF;

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

         OPEN c_contact_name;

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

--
      OPEN c_sub_summary (v_account_type, v_account_sub_type);

      FETCH c_sub_summary
       INTO ss_rec;

      po_active_subs := NVL (ss_rec.a, 0);
      po_suspended_subs := NVL (ss_rec.s, 0);
      po_cancelled_subs := NVL (ss_rec.c, 0);

      CLOSE c_sub_summary;

--
      OPEN c_sub_summary_rsrv (v_account_type, v_account_sub_type);

      FETCH c_sub_summary_rsrv
       INTO v_rsrv;

      po_reserved_subs := v_rsrv;

      CLOSE c_sub_summary_rsrv;

--
      OPEN c_sub_summary_all ();

      FETCH c_sub_summary_all
       INTO ss_rec_all;

      po_all_active_subs := NVL (ss_rec_all.a, 0);
      po_all_suspended_subs := NVL (ss_rec_all.s, 0);
      po_all_cancelled_subs := NVL (ss_rec_all.c, 0);

      CLOSE c_sub_summary_all;

--
      OPEN c_sub_summary_rsrv_all ();

      FETCH c_sub_summary_rsrv_all
       INTO v_rsrv_all;

      po_all_reserved_subs := v_rsrv_all;

      CLOSE c_sub_summary_rsrv_all;

--
      FOR col_rec IN c_collection
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
                 INSTR (po_client_cons_inds, '|', -1) - 1
                );

      CLOSE c_consent_inds_cur;

      OPEN c_payment;

      FETCH c_payment
       INTO v_payment_amt, v_payment_date;

      IF c_payment%FOUND
      THEN
         po_last_payment_amnt := v_payment_amt;
         po_last_payment_date := v_payment_date;
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

         IF c_sub_summary%ISOPEN
         THEN
            CLOSE c_sub_summary;
         END IF;

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

         IF c_contact_name%ISOPEN
         THEN
            CLOSE c_contact_name;
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
         IF i_ban_numbers.COUNT > 0
         THEN
            v_cursor_text :=
                  'SELECT DISTINCT ba.ban, '
               || '       ba.ban_status, '
               || '       ba.account_type, '
               || '       ba.account_sub_type, '
               || '       NVL(ba.start_service_date, ba.status_last_date), '
               || '       ba.start_service_date, '
               || '       SUBSTR(c.dealer_code, 1, 10) dealer_code, '
               || '       SUBSTR(c.dealer_code, 11) sales_rep_code, '
               || '       c.customer_id, '
               || '       nd.first_name, '
               || '       nd.last_business_name, '
               || '       middle_initial, '
               || '       name_title, '
               || '       ad.adr_primary_ln, '
               || '       ad.adr_city, '
               || '       ad.adr_province, '
               || '       ad.adr_postal, '
               || '       adr_type, '
               || '       adr_secondary_ln, '
               || '       adr_country, '
               || '       adr_zip_geo_code, '
               || '       adr_state_code, '
               || '       civic_no, '
               || '       civic_no_suffix, '
               || '       adr_st_direction, '
               || '       adr_street_name, '
               || '       adr_street_type, '
               || '       adr_designator, '
               || '       adr_identifier, '
               || '       adr_box, '
               || '       unit_designator, '
               || '       unit_identifier, '
               || '       adr_area_nm, '
               || '       adr_qualifier, '
               || '       adr_site, '
               || '       adr_compartment, '
               || '       c.acc_password, '
               || '       ba.status_actv_code, '
               || '       ba.status_actv_rsn_code, '
               || '       nd.additional_title, '
               || '       ba.status_last_date '
               || '  FROM billing_account ba, '
               || '       customer c, '
               || '       address_name_link anl, '
               || '       address_data ad, '
               || '       name_data nd '
               || ' WHERE ba.ban IN (';
            i_index := 1;

            WHILE i_index < i_ban_numbers.COUNT
            LOOP
               v_cursor_text :=
                              v_cursor_text || i_ban_numbers (i_index)
                              || ', ';
               i_index := i_index + 1;
            END LOOP;

            v_cursor_text := v_cursor_text || i_ban_numbers (i_index);
            v_cursor_text :=
                  v_cursor_text
               || ') '
               || '   AND anl.name_id = nd.name_id '
               || '   AND (anl.expiration_date IS NULL OR anl.expiration_date > sysdate)'
               || '   AND anl.link_type = ''B'' '
               || '   AND ad.address_id = anl.address_id '
               || '   AND ba.ban = anl.customer_id '
               || '   AND c.customer_id = anl.customer_id';

            OPEN o_accounts
             FOR v_cursor_text;

            i_result := numeric_true;
         ELSE
            v_error_message := err_invalid_input;
            i_result := numeric_false;
         END IF;
      EXCEPTION
         WHEN NO_DATA_FOUND
         THEN
            v_error_message := err_no_data_found;
         WHEN OTHERS
         THEN
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
      o_accounts             OUT      refcursor,
      i_has_more             OUT      NUMBER,
      v_error_message        OUT      VARCHAR2
   )
      RETURN NUMBER
   IS
      a_ban_array        t_ban_array;
      c_account_cursor   refcursor;
      v_cursor_text      VARCHAR2 (32767);
      i_count            BINARY_INTEGER;
      i_max              NUMBER (4);
      b_full_join        BOOLEAN;
      i_result           NUMBER (1);
   BEGIN
      BEGIN
         b_full_join :=
               (TO_CHAR (c_account_status) != search_all)
            OR (TO_CHAR (c_account_type) != search_all)
            OR (v_province != search_all);
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
         i_count := 1;

         LOOP
            a_ban_array.EXTEND;

            FETCH c_account_cursor
             INTO a_ban_array (i_count);

            EXIT WHEN (c_account_cursor%NOTFOUND OR i_count = i_max + 1);
            i_count := i_count + 1;
         END LOOP;

         IF i_count > i_max AND NOT c_account_cursor%NOTFOUND
         THEN
            i_has_more := numeric_true;
         ELSE
            i_has_more := numeric_false;
         END IF;

         CLOSE c_account_cursor;

         IF i_count > 1
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
                  getaccountsbybans (a_ban_array, o_accounts, v_error_message);
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
            THEN                                           -- exclude expired
               v_cursor_text :=
                     'select distinct  sa.soc, sa.subscriber_no, s.sub_status '
                  || '  from service_agreement sa, subscriber s , logical_date ld  '
                  || ' where sa.ban = '
                  || pi_ban
                  || ' and s.customer_id = sa.ban '
                  || ' and s.subscriber_no=sa.subscriber_no '
                  || ' and sa.soc IN (';
               i_index := 1;

               WHILE i_index < pi_service_codes.COUNT
               LOOP
                  v_service_code :=
                                   '''' || pi_service_codes (i_index)
                                   || ''' ';
                  v_cursor_text := v_cursor_text || v_service_code || ', ';
                  i_index := i_index + 1;
               END LOOP;

               v_service_code := '''' || pi_service_codes (i_index) || ''' ';
               v_cursor_text := v_cursor_text || v_service_code;
               v_cursor_text :=
                     v_cursor_text
                  || ') '
                  || ' and (trunc(sa.expiration_date) > trunc(ld.logical_date) or sa.expiration_date is null)'
                  || ' and ld.logical_date_type=''O'' ';
            ELSE
               v_cursor_text :=
                     'select distinct  sa.soc, sa.subscriber_no, s.sub_status '
                  || '  from service_agreement sa, subscriber s '
                  || ' where sa.ban = '
                  || pi_ban
                  || ' and s.customer_id = sa.ban '
                  || ' and s.subscriber_no=sa.subscriber_no '
                  || ' and sa.soc IN (';
               i_index := 1;

               WHILE i_index < pi_service_codes.COUNT
               LOOP
                  v_service_code :=
                                   '''' || pi_service_codes (i_index)
                                   || ''' ';
                  v_cursor_text := v_cursor_text || v_service_code || ', ';
                  i_index := i_index + 1;
               END LOOP;

               v_service_code := '''' || pi_service_codes (i_index) || ''' ';
               v_cursor_text := v_cursor_text || v_service_code;
               v_cursor_text := v_cursor_text || ') ';
            END IF;

            OPEN po_subscribers
             FOR v_cursor_text;

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
                       || '])'
                      );
      END;

      RETURN i_result;
   END getservicesubscribercounts;
END;
/

SHO err

