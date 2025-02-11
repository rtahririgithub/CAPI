create or replace 
PACKAGE ACC_RETRIEVAL_PKG
AS
-------------------------------------------------------------------------
-- IMPORTANT NOTE !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
-- Please increment the version_no variable value when you make changes to this package 
-- to reflect version changes.
--------------------------------------------------------------------------
-- description: Package GetAccountInfoPkg containing procedures retrieving account details in KB
--
-- Date           Version    Developer           Modifications
-- March 4, 2013  3.23.1    Tsz Chung Tong        created with getAccountInfoByBAN and getAccountExtPropertiesByBan
-- March 28,2013  3.23.2    Tsz Chung Tong        Moved getLWaccountInfoByBAN from RA_UTILITY_PKG
-- Apr   22,2013  3.23.3    Michael Liao        Modified getlwaccountinfobyban, return one more field: customer.email_address
-- Apr   24,2013  3.23.4    Michael Liao        Modified getlwaccountinfobyban, return one more field: customer.lang_pref
-- May   13, 2013 3.23.5    	Mahan Razagh  	Added new enhanced function getAccountsByPostalCode
-- May   16, 2013 3.23.5    	Mahan Razagh  	Added new enhanced function getAccountsByName
-- June  6,  2013 3.23.6    	Mahan Razagh  	Modified enhanced function getAccountsByName Defect ID # 13555
-- July  22, 2013 3.23.7    	Mahan Razagh  	Modified enhanced function getAccountsByName Defect ID # 15433
-- July  25, 2013 3.24.1   Tsz Chung Tong       Version # update only to align with Oct'13 release. No functional change
-- Jan 23, 2018 2018.01.1  Tsz Chung Tong      Fixed getLWaccountInfoByBAN to return data even if bill cycle day is invalid
-- Jan 25, 2018 2018.03.1  Tsz Chung Tong      Changed version # to 2018.03.1 
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
   version_no          CONSTANT VARCHAR2(10)       := '2018.03.1';
   

-------------------------------------------------------------------------
-- description: Procedure GetAccountInfoByBAN to get Account Information
--------------------------------------------------------------------------
   PROCEDURE getAccountInfoByBAN (
      pi_ban                         IN       NUMBER,
     c_account                 OUT REFCURSOR,
     po_logical_date           OUT DATE
   );
   
   PROCEDURE getAccountExtPropertiesByBan(
    pi_ban IN NUMBER,
     c_consent_inds_cur    OUT REFCURSOR,
      c_collection             OUT REFCURSOR,
      c_dck                     OUT REFCURSOR,
      c_finance_history     OUT REFCURSOR,
      c_contact_name    OUT REFCURSOR,
      po_bill_due_date  OUT DATE,
      po_col_step                    OUT      NUMBER,
    po_col_actv_code               OUT      VARCHAR2,
    po_col_actv_date               OUT      DATE,
    po_col_written_off_date OUT DATE,
    po_last_payment_amnt           OUT      NUMBER,
       po_last_payment_date           OUT      DATE,
         po_last_payment_actual_amt       OUT      NUMBER,
         po_last_payment_actv_code      OUT      VARCHAR2
      );

    PROCEDURE getLWaccountInfoByBAN (
        pi_ban                         IN       NUMBER,
        c_account                 OUT REFCURSOR,
         po_logical_date           OUT DATE
    );

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
      v_error_message        OUT      VARCHAR2
    ) RETURN NUMBER;

  PROCEDURE getAccountsByPostalCode (
         pi_lastName     IN        VARCHAR2,
         pi_postalCode    IN        VARCHAR2,
         pi_maximum        IN        NUMBER,
         po_curAccounts    OUT        refcursor
     );

     
END ACC_RETRIEVAL_PKG;
/

SHO err

CREATE OR REPLACE PACKAGE BODY ACC_RETRIEVAL_PKG
AS
	PROCEDURE getlogicaldate (po_logical_date OUT DATE)
   IS
   BEGIN
      SELECT logical_date
        INTO po_logical_date
        FROM logical_date ld
       WHERE ld.logical_date_type = 'O';
   END getlogicaldate;
   
   PROCEDURE getAccountInfoByBAN (
      pi_ban                         IN       NUMBER,
      c_account                 OUT REFCURSOR,
	  po_logical_date           OUT DATE
     )
   IS
   

   BEGIN
          
      getlogicaldate (po_logical_date);

      OPEN c_account FOR 
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
                         name_suffix, NVL(ba.brand_id, brand_telus) brand_ind,
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
                                                       TRUNC ((po_logical_date ))
                          OR anl.expiration_date IS NULL
                         )
                     AND anl.link_type = 'B'
                     AND ad.address_id = anl.address_id
                     AND nd.name_id = anl.name_id
                     AND bdd.ban(+) = ba.ban
                     AND bc.cycle_code(+) = ba.bill_cycle
                     AND nbc.cycle_code(+) = ba.bl_next_cycle
                     AND cs.ban(+) = ba.ban;
   
   EXCEPTION
      WHEN OTHERS
      THEN
         IF c_account%ISOPEN
         THEN
            CLOSE c_account;
         END IF;

         RAISE;
   END getAccountInfoByBAN;
   

   PROCEDURE getAccountExtPropertiesByBan(
    pi_ban IN NUMBER,
     c_consent_inds_cur    OUT REFCURSOR,
      c_collection             OUT REFCURSOR,
      c_dck                     OUT REFCURSOR,
      c_finance_history     OUT REFCURSOR,
      c_contact_name    OUT REFCURSOR,
      po_bill_due_date  OUT DATE,
	  po_col_step                    OUT      NUMBER,
    po_col_actv_code               OUT      VARCHAR2,
    po_col_actv_date               OUT      DATE,
	po_col_written_off_date OUT DATE,
	po_last_payment_amnt           OUT      NUMBER,
       po_last_payment_date           OUT      DATE,
     	po_last_payment_actual_amt       OUT      NUMBER,
     	po_last_payment_actv_code      OUT      VARCHAR2
	  )
	IS
	v_logical_date         DATE;
   BEGIN
          
      getlogicaldate (v_logical_date);
	
	  ACC_ATTRIB_RETRIEVAL_PKG.getConsentIndicator(pi_ban, c_consent_inds_cur);
	  HISTORY_UTILITY_PKG.getLast12MthsCollection (pi_ban, v_logical_date, c_collection);
	  HISTORY_UTILITY_PKG.getCollectionStep (pi_ban, po_col_step, po_col_actv_code, po_col_actv_date);
	  HISTORY_UTILITY_PKG.getWrittenOffDate (pi_ban, po_col_written_off_date);
	  HISTORY_UTILITY_PKG.getDishonouredPaymentCounts (pi_ban, c_dck, c_finance_history);
	  HISTORY_UTILITY_PKG.getLastPayment (pi_ban, po_last_payment_amnt, po_last_payment_date, po_last_payment_actual_amt, po_last_payment_actv_code); 		  
	  ACC_ATTRIB_RETRIEVAL_PKG.getContactName(pi_ban, v_logical_date, c_contact_name);
	  INVOICE_PKG.getLatestBillDueDate (pi_ban, po_bill_due_date);

	END getAccountExtPropertiesByBan;
	
	PROCEDURE getLWaccountInfoByBAN (
		pi_ban                         IN       NUMBER,
		c_account                 OUT REFCURSOR,
	 	po_logical_date           OUT DATE
	)
	IS
	BEGIN
		getlogicaldate (po_logical_date);
		OPEN c_account
		FOR
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
					 c.email_address,
					 c.lang_pref
	  		FROM billing_account ba, collection_status cs, customer c, cycle bc
	 			WHERE ba.ban = pi_ban 
	 			  AND cs.ban(+) = ba.ban 
	 			  AND ba.customer_id=c.customer_id 
	 			  AND ba.bill_cycle=bc.cycle_code(+) ;
   EXCEPTION
      WHEN OTHERS
      THEN
         IF c_account%ISOPEN
         THEN
            CLOSE c_account;
         END IF;

         RAISE;
	END getLWaccountInfoByBAN;

 ---------------------------- Enhanced Postal Code function

  PROCEDURE getAccountsByPostalCode (
         pi_lastName     IN        VARCHAR2,
         pi_postalCode    IN        VARCHAR2,
         pi_maximum        IN        NUMBER,
         po_curAccounts    OUT        refcursor
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
                      AND     ad.adr_postal= pi_postalCode
                              --or ad.adr_zip_geo_code = pi_postalCode) -- Removed for performance issues as it was useless condition
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
     
  --------------------------- getaccountsbyname Enhanced function
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
      v_error_message        OUT      VARCHAR2
   )
   RETURN NUMBER
 IS
      i_result                NUMBER (1);
      i_max                   NUMBER (4);
      v_control_name_perc     VARCHAR2(1):='%'; 
      v_first_name_perc       VARCHAR2(1):='%'; 

 BEGIN
   BEGIN
   -- Set the maximum number of returned records
   IF i_maximum > 0 AND i_maximum < max_maximorum  THEN
      i_max := i_maximum;
   ELSE
      i_max := max_maximorum;
   END IF;
      
   -- Set Control name wild card
   IF i_control_name_exact = numeric_true THEN
      v_control_name_perc := '';
   END IF;

   -- Set first name wild card
   IF LENGTH (rtrim (ltrim (v_first_name))) IS NOT NULL THEN
     IF i_first_name_exact = numeric_true  THEN   
        v_first_name_perc := '';
      END IF;
   END IF;
      
   IF to_char(c_name_type)='B' THEN
     OPEN o_accounts
     FOR
     SELECT *
      FROM 
        (SELECT a.*,
           (CASE WHEN (COUNT (*) OVER ()) > i_max THEN numeric_true ELSE numeric_false END)
                      AS has_more
         FROM
        (SELECT DISTINCT   
                    ba.ban, ba.ban_status, ba.account_type,
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
              WHERE 
                    ND.CONTROL_NAME like UPPER(TRIM(V_CONTROL_NAME)) ||  V_CONTROL_NAME_PERC
                AND (nd.first_name LIKE upper(trim(v_first_name)) || v_first_name_perc OR LENGTH (rtrim (ltrim(v_first_name))) IS NULL)    
                AND (ba.account_type = c_account_type OR c_account_type = search_all)
                AND (ba.ban_status = c_account_status OR c_account_status = search_all)
                AND (ba.home_province = v_province OR v_province = search_all)
                AND (i_brand_ind = brand_all
                       OR
                     (i_brand_ind != brand_all AND i_brand_ind != brand_telus AND ba.brand_id = to_char(i_brand_ind))
                       OR
                     (i_brand_ind != brand_all AND i_brand_ind = brand_telus AND (ba.brand_id = to_char(i_brand_ind) OR ba.brand_id IS NULL))
                     )
                AND anl.name_id = nd.name_id
                AND (   anl.expiration_date IS NULL
                     OR anl.expiration_date > SYSDATE
                    )
                AND anl.link_type = 'B'
                AND ad.address_id = anl.address_id
                AND ba.ban = anl.customer_id
                AND c.customer_id = anl.customer_id
                AND ROWNUM <= i_max+1) A
             )  
      WHERE ROWNUM <= i_max;
   ELSE  -- Other types of name type
     OPEN o_accounts
     FOR
     SELECT *
      FROM 
        (SELECT a.*,
           (CASE WHEN (COUNT (*) OVER ()) > i_max THEN numeric_true ELSE numeric_false END)
                      AS has_more
         FROM
        (SELECT DISTINCT  
                  ba.ban, ba.ban_status, ba.account_type,
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
                  nd.additional_title, ba.status_last_date, nvl(ba.brand_id,'1') brand_ind,
                  ba.gl_segment, ba.gl_subsegment 
             FROM billing_account ba,
                  customer C,
                  address_name_link anl,
                  address_data ad,
                  name_data nd
            where 
                  (ba.account_type = c_account_type OR c_account_type = search_all)
              AND (ba.ban_status = c_account_status OR c_account_status = search_all)
              AND (ba.home_province = v_province OR v_province = search_all)
              AND (i_brand_ind = brand_all
                     OR
                   (i_brand_ind != brand_all AND i_brand_ind != brand_telus AND ba.brand_id = to_char(i_brand_ind))
                     OR
                   (i_brand_ind != brand_all AND i_brand_ind = brand_telus AND (ba.brand_id = to_char(i_brand_ind) OR ba.brand_id IS NULL))
                   )
              AND   anl.name_id = nd.name_id
              AND ( anl.expiration_date IS NULL
                  OR anl.expiration_date > SYSDATE
                  )
              AND anl.link_type = 'B'
              AND ad.address_id = anl.address_id
              AND ba.ban = anl.customer_id
              AND c.customer_id = anl.customer_id
              AND ba.ban IN
                  (SELECT DISTINCT  anl.customer_id  
                     FROM 
                        address_name_link anl,
                        name_data nd
                  WHERE 
                        nd.control_name like upper(trim(v_control_name)) ||  v_control_name_perc
                    AND (nd.first_name LIKE upper(trim(v_first_name)) || v_first_name_perc OR LENGTH (rtrim (ltrim(v_first_name)))IS NULL)    
                    AND (anl.link_type = to_char(c_name_type) OR c_name_type = search_all)
                    AND anl.name_id = nd.name_id
                    AND (   anl.expiration_date IS NULL
                         OR anl.expiration_date > SYSDATE
                        )          
                  )
                AND ROWNUM <= i_max+1) A
             )  
      WHERE ROWNUM <= i_max;
   
   END IF;
     
   i_result := numeric_true;
                            
   EXCEPTION
         
      WHEN NO_DATA_FOUND
      THEN
      IF (o_accounts%ISOPEN) THEN
             CLOSE o_accounts;
      END IF;
      i_result := numeric_false;
                
      WHEN OTHERS
      THEN
         
      IF (o_accounts%ISOPEN) THEN
          CLOSE o_accounts;
      END IF;
         
      v_error_message := SQLERRM;
      i_result := numeric_false;
      RAISE;
   END;

   RETURN i_result;
   
 END getaccountsbyname;
 
	
END ACC_RETRIEVAL_PKG;
/


SHO err
