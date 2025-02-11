Create or replace package RA_Utility_pkg as

 ------------------------------------------------------------------------
-- description: Package Utility_pkg containing procedures
--		for data retrieval from Knowbility database
--
--Version KB_82_4
-- Date	   		Developer     	  	  Modifications
-- 07-04-2001		Ludmila Pomirche	  created
-- 11-13-2001           Ludmila Pomirche          BAN Status Activity Reason Code added
--						  to retrieval
-- 11-06-2002		Ludmila Pomirche	  Changes for 6.1 Mike
-- 06-04-2003           Ludmila Pomirche	  Changes for KB 7.0 release
-- 07-11-2003		Ludmila Pomirche	  Changes for Contract Renewal
-- 10-09-2003		Ludmila Pomirche	  Changes to Subscriber Count Logic
-- 10-30-2003		Ludmila Pomirche	  Bank Info retrieval added
-- 11-03-2003		Ludmila Pomirche	  Add Other Phone retrieval
-- 04-20-2004		Ludmila Poirche		  Collection buckets retrieval fix
-- 05-03-2004		Ludmila Pomirche	  Additional billing Info for Smart 2.0
-- 07-02-2004		Ludmila Pomirche	  Added write off indicator and Contact name and phone
-- 07-12-2004		Ludmila Pomirche	  Additional Collection Info
-- 08-03-2004		Ludmila Pomirche	  Additional billing Info for October release of  Smart
-- 09-17-2004		Ludmila Pomirche	  Added retrieval of conv_run_no
-- 10-04-2004		Marina Kuper     	  Added retrieval of consent_inds
-- 11-04-2004		Ludmila Pomirche	  Added retrieval of all subscriber counts
-- 12-03-2004		Ludmila Pomirche	  Collection activities retrieval modification
-- 12-29-2004		Ludmila Pomirche	  Reserved subscribers count modification
---02-15-2005       Marina  Kuper         Allowed the future expiration date for address_name_link
---o3-30-2005       Vladimir Tsitrin      Added 5 output fields to GetAccountInfoByBAN  
-------------------------------------------------------------------------

BANNotFound		Exception;
PRAGMA			EXCEPTION_INIT(BANNotFound, -20101);
-------------------------------------------------------------------------
-- description: Procedure GetAccountInfoByBAN to get Account Information
--------------------------------------------------------------------------


Procedure GetAccountInfoByBAN 	( pi_ban			IN	number
				 ,po_ban_status			OUT	char
				 ,po_account_type		OUT	char
				 ,po_account_sub_type		OUT	char
				 ,po_create_date		OUT	date
				 ,po_start_service_date		OUT	date
				 ,po_col_delinq_status		OUT	varchar2
				 ,po_ar_balance			OUT	number
				 ,po_dealer_code		OUT	varchar2
				 ,po_sales_rep_code		OUT	varchar2
				 ,po_bill_cycle			OUT	number
				 ,po_payment_method		OUT	varchar2
 				 ,po_work_telno			OUT	varchar2
 				 ,po_work_tn_extno		OUT	varchar2
 				 ,po_home_telno			OUT	varchar2
 				 ,po_birth_date			OUT	date
 				 ,po_contact_faxno		OUT	varchar2
 				 ,po_acc_password		OUT	varchar2
 				 ,po_customer_ssn		OUT	varchar2
 				 ,po_adr_primary_ln		OUT	varchar2
 				 ,po_adr_city			OUT	varchar2
 				 ,po_adr_province		OUT	varchar2
 				 ,po_adr_postal			OUT	varchar2
 				 ,po_first_name			OUT	varchar2
 				 ,po_last_business_name		OUT	varchar2
 				 ,po_special_instruction 	OUT	varchar2
 				 ,po_cu_age_bucket_0		OUT	number
 				 ,po_cu_age_bucket_1_30		OUT	number
 				 ,po_cu_age_bucket_31_60	OUT	number
 				 ,po_cu_age_bucket_61_90	OUT	number
 				 ,po_cu_age_bucket_91_plus	OUT	number
 				 ,po_cu_past_due_amt		OUT	number
 				 ,po_active_subs		OUT	number
 				 ,po_reserved_subs		OUT	number
 				 ,po_suspended_subs		OUT	number
 				 ,po_cancelled_subs		OUT	number
				 ,po_col_actv_JAN		OUT     char
				 ,po_col_actv_FEB		OUT     char
				 ,po_col_actv_MAR		OUT     char
				 ,po_col_actv_APR		OUT     char
				 ,po_col_actv_MAY		OUT     char
				 ,po_col_actv_JUN		OUT     char
				 ,po_col_actv_JUL		OUT     char
				 ,po_col_actv_AUG		OUT     char
				 ,po_col_actv_SEP		OUT     char
				 ,po_col_actv_OCT		OUT     char
				 ,po_col_actv_NOV		OUT     char
				 ,po_col_actv_DEC		OUT     char
				 ,po_DCK_JAN			OUT     number
				 ,po_DCK_FEB			OUT     number
				 ,po_DCK_MAR			OUT     number
				 ,po_DCK_APR			OUT     number
				 ,po_DCK_MAY			OUT     number
				 ,po_DCK_JUN			OUT     number
				 ,po_DCK_JUL			OUT     number
				 ,po_DCK_AUG			OUT     number
				 ,po_DCK_SEP			OUT     number
				 ,po_DCK_OCT			OUT     number
				 ,po_DCK_NOV			OUT     number
				 ,po_DCK_DEC			OUT     number
				 ,po_last_payment_amnt		OUT 	number
				 ,po_last_payment_date		OUT	date
				 ,po_hotline_ind		OUT     char
				 ,po_customer_id		OUT     number
				 ,po_language			OUT	varchar2
				 ,po_email			OUT	varchar2
				 ,po_adr_type 			OUT	varchar2
				 ,po_adr_secondary_ln 		OUT	varchar2
				 ,po_adr_country  		OUT	varchar2
				 ,po_adr_zip_geo_code 		OUT	varchar2
				 ,po_adr_state_code 		OUT	varchar2
				 ,po_civic_no 			OUT	varchar2
				 ,po_civic_no_suffix 		OUT	varchar2
				 ,po_adr_st_direction		OUT	varchar2
				 ,po_adr_street_name 		OUT	varchar2
				 ,po_adr_street_type 		OUT	varchar2
				 ,po_adr_designator             OUT	varchar2
 				 ,po_adr_identifier		OUT	varchar2
 				 ,po_adr_box 			OUT	varchar2
 				 ,po_unit_designator		OUT	varchar2
 				 ,po_unit_identifier		OUT	varchar2
 				 ,po_adr_area_nm  		OUT	varchar2
 				 ,po_adr_qualifier 		OUT	varchar2
 				 ,po_adr_site 			OUT	varchar2
 				 ,po_adr_compartment            OUT	varchar2
 				 ,po_middle_initial		OUT	varchar2
 				 ,po_name_title                 OUT	varchar2
 				 ,po_additional_title		OUT	varchar2
 				 ,po_contact_last_name    	OUT	varchar2
 				 ,po_drivr_licns_no             OUT	varchar2
 				 ,po_drivr_licns_state          OUT	varchar2
 				 ,po_drivr_licns_exp_dt    	OUT     date
 				 ,po_incorporation_no           OUT	varchar2
				 ,po_incorporation_date     	OUT	date
				 ,po_gur_cr_card_no             OUT	varchar2
				 ,po_gur_cr_card_exp_dt_mm	OUT	number
				 ,po_gur_cr_card_exp_dt_yyyy	OUT	number
				 ,po_credit_card_no         	OUT	varchar2
				 ,po_card_mem_hold_nm           OUT	varchar2
				 ,po_expiration_date_mm        	OUT	number
				 ,po_expiration_date_yyyy       OUT	number
				 ,po_status_actv_code           OUT	varchar2
				 ,po_status_actv_rsn_code       OUT	varchar2
				 ,po_bill_cycle_close_day	OUT	number
				 ,po_return_envelope_ind 	OUT	varchar2
				 ,po_bill_due_date		OUT	date
				 ,po_corp_hierarhy_ind	        OUT	varchar2
				 ,po_corp_csr_id		OUT	varchar2
				 ,po_inv_supression_ind		OUT     varchar2
				 ,po_bankCode 			OUT     varchar2
  				 ,po_bankAccountNumber 		OUT     varchar2
  				 ,po_bankBranchNumber 		OUT     varchar2
  				 ,po_bankAccountType 		OUT     varchar2
  				 ,po_directDebitStatus  	OUT     varchar2
  				 ,po_directDebitStatusRsn  	OUT     varchar2
  				 ,po_other_phone		OUT     varchar2
  				 ,po_other_phone_ext	 	OUT     varchar2
  				 ,po_other_phone_type	  	OUT     varchar2
  				 ,po_tax_gst_exmp_ind		OUT	varchar2
				 ,po_tax_pst_exmp_ind 		OUT	varchar2
				 ,po_tax_hst_exmp_ind		OUT	varchar2
				 ,po_tax_gst_exmp_exp_dt	OUT	date
				 ,po_tax_pst_exmp_exp_dt	OUT	date
				 ,po_tax_hst_exmp_exp_dt	OUT     date
				 ,po_home_province 		OUT     varchar2
  				 ,po_category  			OUT     varchar2
  				 ,po_next_bill_cycle		OUT     number
  				 ,po_next_bill_cycle_close_day	OUT     number
  				 ,po_verified_date  		OUT     date
  				 ,po_handle_by_subscriber_ind 	OUT     varchar2
  				 ,po_corporate_id		OUT	varchar2
  				 ,po_write_off_ind		OUT	varchar2
  				 ,po_contact_first_name		OUT	varchar2
  				 ,po_contact_name_title		OUT	varchar2
  				 ,po_contact_middle_initial	OUT	varchar2
  				 ,po_contact_additional_title	OUT	varchar2
  				 ,po_contact_name_suffix	OUT	varchar2
  				 ,po_contact_phone_number	OUT	varchar2
  				 ,po_contact_phone_number_ext	OUT	varchar2
  				 ,po_legal_business_name	OUT	varchar2
  				 ,po_col_path_code		OUT	varchar2
  				 ,po_col_step			OUT     number
  				 ,po_col_actv_code		OUT	varchar2
  				 ,po_col_actv_date		OUT	date
  				 ,po_col_next_step		OUT     number
  				 ,po_col_next_actv_date		OUT	date
  				 ,po_col_agency			OUT	varchar2
  				 ,po_adr_attention		OUT	varchar2
  				 ,po_adr_delivery_type		OUT	varchar2
  				 ,po_adr_group			OUT     varchar2
  				 ,po_tax_gst_exmp_rf_no		OUT	varchar2
				 ,po_tax_pst_exmp_rf_no 	OUT	varchar2
				 ,po_tax_hst_exmp_rf_no		OUT	varchar2
				 ,po_tax_gst_exmp_eff_dt	OUT	date
				 ,po_tax_pst_exmp_eff_dt	OUT	date
				 ,po_tax_hst_exmp_eff_dt	OUT date
				 ,po_status_last_date		OUT date
				 ,po_conv_run_no		    OUT number
				 ,po_client_cons_inds		OUT varchar2
				 ,po_all_active_subs		OUT	number
 				 ,po_all_reserved_subs		OUT	number
 				 ,po_all_suspended_subs		OUT	number
 				 ,po_all_cancelled_subs		OUT	number
                 ,po_bl_man_hndl_req_opid      OUT number
				 ,po_bl_man_hndl_eff_date      OUT date
				 ,po_bl_man_hndl_exp_date      OUT date
				 ,po_bl_man_hndl_rsn           OUT varchar2
				 ,po_bl_man_hndl_by_opid       OUT number);


End;
/

sho err

Create or replace package body RA_Utility_pkg as

Procedure GetAccountInfoByBAN 	( pi_ban			IN	number
				 ,po_ban_status			OUT	char
				 ,po_account_type		OUT	char
				 ,po_account_sub_type		OUT	char
				 ,po_create_date		OUT	date
				 ,po_start_service_date		OUT	date
				 ,po_col_delinq_status		OUT	varchar2
				 ,po_ar_balance			OUT	number
				 ,po_dealer_code		OUT	varchar2
				 ,po_sales_rep_code		OUT	varchar2
				 ,po_bill_cycle			OUT	number
				 ,po_payment_method		OUT	varchar2
 				 ,po_work_telno			OUT	varchar2
 				 ,po_work_tn_extno		OUT	varchar2
 				 ,po_home_telno			OUT	varchar2
 				 ,po_birth_date			OUT	date
 				 ,po_contact_faxno		OUT	varchar2
 				 ,po_acc_password		OUT	varchar2
 				 ,po_customer_ssn		OUT	varchar2
 				 ,po_adr_primary_ln		OUT	varchar2
 				 ,po_adr_city			OUT	varchar2
 				 ,po_adr_province		OUT	varchar2
 				 ,po_adr_postal			OUT	varchar2
 				 ,po_first_name			OUT	varchar2
 				 ,po_last_business_name		OUT	varchar2
 				 ,po_special_instruction 	OUT	varchar2
 				 ,po_cu_age_bucket_0		OUT	number
 				 ,po_cu_age_bucket_1_30		OUT	number
 				 ,po_cu_age_bucket_31_60	OUT	number
 				 ,po_cu_age_bucket_61_90	OUT	number
 				 ,po_cu_age_bucket_91_plus	OUT	number
 				 ,po_cu_past_due_amt		OUT	number
 				 ,po_active_subs		OUT	number
 				 ,po_reserved_subs		OUT	number
 				 ,po_suspended_subs		OUT	number
 				 ,po_cancelled_subs		OUT	number
				 ,po_col_actv_JAN		OUT     char
				 ,po_col_actv_FEB		OUT     char
				 ,po_col_actv_MAR		OUT     char
				 ,po_col_actv_APR		OUT     char
				 ,po_col_actv_MAY		OUT     char
				 ,po_col_actv_JUN		OUT     char
				 ,po_col_actv_JUL		OUT     char
				 ,po_col_actv_AUG		OUT     char
				 ,po_col_actv_SEP		OUT     char
				 ,po_col_actv_OCT		OUT     char
				 ,po_col_actv_NOV		OUT     char
				 ,po_col_actv_DEC		OUT     char
				 ,po_DCK_JAN			OUT     number
				 ,po_DCK_FEB			OUT     number
				 ,po_DCK_MAR			OUT     number
				 ,po_DCK_APR			OUT     number
				 ,po_DCK_MAY			OUT     number
				 ,po_DCK_JUN			OUT     number
				 ,po_DCK_JUL			OUT     number
				 ,po_DCK_AUG			OUT     number
				 ,po_DCK_SEP			OUT     number
				 ,po_DCK_OCT			OUT     number
				 ,po_DCK_NOV			OUT     number
				 ,po_DCK_DEC			OUT     number
				 ,po_last_payment_amnt		OUT 	number
				 ,po_last_payment_date		OUT	date
				 ,po_hotline_ind		OUT     char
				 ,po_customer_id		OUT     number
				 ,po_language			OUT	varchar2
				 ,po_email			OUT	varchar2
				 ,po_adr_type 			OUT	varchar2
				 ,po_adr_secondary_ln 		OUT	varchar2
				 ,po_adr_country  		OUT	varchar2
				 ,po_adr_zip_geo_code 		OUT	varchar2
				 ,po_adr_state_code 		OUT	varchar2
				 ,po_civic_no 			OUT	varchar2
				 ,po_civic_no_suffix 		OUT	varchar2
				 ,po_adr_st_direction		OUT	varchar2
				 ,po_adr_street_name 		OUT	varchar2
				 ,po_adr_street_type 		OUT	varchar2
				 ,po_adr_designator             OUT	varchar2
 				 ,po_adr_identifier		OUT	varchar2
 				 ,po_adr_box 			OUT	varchar2
 				 ,po_unit_designator		OUT	varchar2
 				 ,po_unit_identifier		OUT	varchar2
 				 ,po_adr_area_nm  		OUT	varchar2
 				 ,po_adr_qualifier 		OUT	varchar2
 				 ,po_adr_site 			OUT	varchar2
 				 ,po_adr_compartment            OUT	varchar2
 				 ,po_middle_initial		OUT	varchar2
 				 ,po_name_title                 OUT	varchar2
 				 ,po_additional_title		OUT	varchar2
 				 ,po_contact_last_name    	OUT	varchar2
 				 ,po_drivr_licns_no             OUT	varchar2
 				 ,po_drivr_licns_state          OUT	varchar2
 				 ,po_drivr_licns_exp_dt    	OUT     date
 				 ,po_incorporation_no           OUT	varchar2
				 ,po_incorporation_date     	OUT	date
				 ,po_gur_cr_card_no             OUT	varchar2
				 ,po_gur_cr_card_exp_dt_mm	OUT	number
				 ,po_gur_cr_card_exp_dt_yyyy	OUT	number
				 ,po_credit_card_no         	OUT	varchar2
				 ,po_card_mem_hold_nm           OUT	varchar2
				 ,po_expiration_date_mm        	OUT	number
				 ,po_expiration_date_yyyy       OUT	number
				 ,po_status_actv_code           OUT	varchar2
				 ,po_status_actv_rsn_code       OUT	varchar2
				 ,po_bill_cycle_close_day	OUT	number
				 ,po_return_envelope_ind 	OUT	varchar2
				 ,po_bill_due_date		OUT	date
				 ,po_corp_hierarhy_ind	        OUT	varchar2
				 ,po_corp_csr_id		OUT	varchar2
				 ,po_inv_supression_ind		OUT     varchar2
				 ,po_bankCode 			OUT     varchar2
  				 ,po_bankAccountNumber 		OUT     varchar2
  				 ,po_bankBranchNumber 		OUT     varchar2
  				 ,po_bankAccountType 		OUT     varchar2
  				 ,po_directDebitStatus  	OUT     varchar2
  				 ,po_directDebitStatusRsn  	OUT     varchar2
  				 ,po_other_phone		OUT     varchar2
  				 ,po_other_phone_ext	 	OUT     varchar2
  				 ,po_other_phone_type	  	OUT     varchar2
  				 ,po_tax_gst_exmp_ind		OUT	varchar2
				 ,po_tax_pst_exmp_ind 		OUT	varchar2
				 ,po_tax_hst_exmp_ind		OUT	varchar2
				 ,po_tax_gst_exmp_exp_dt	OUT	date
				 ,po_tax_pst_exmp_exp_dt	OUT	date
				 ,po_tax_hst_exmp_exp_dt	OUT     date
				 ,po_home_province 		OUT     varchar2
  				 ,po_category  			OUT     varchar2
  				 ,po_next_bill_cycle		OUT     number
  				 ,po_next_bill_cycle_close_day	OUT     number
  				 ,po_verified_date  		OUT     date
  				 ,po_handle_by_subscriber_ind 	OUT     varchar2
  				 ,po_corporate_id		OUT	varchar2
  				 ,po_write_off_ind		OUT	varchar2
  				 ,po_contact_first_name		OUT	varchar2
  				 ,po_contact_name_title		OUT	varchar2
  				 ,po_contact_middle_initial	OUT	varchar2
  				 ,po_contact_additional_title	OUT	varchar2
  				 ,po_contact_name_suffix	OUT	varchar2
  				 ,po_contact_phone_number	OUT	varchar2
  				 ,po_contact_phone_number_ext	OUT	varchar2
  				 ,po_legal_business_name	OUT	varchar2
  				 ,po_col_path_code		OUT	varchar2
  				 ,po_col_step			OUT     number
  				 ,po_col_actv_code		OUT	varchar2
  				 ,po_col_actv_date		OUT	date
  				 ,po_col_next_step		OUT     number
  				 ,po_col_next_actv_date		OUT	date
  				 ,po_col_agency			OUT	varchar2
  				 ,po_adr_attention		OUT	varchar2
  				 ,po_adr_delivery_type		OUT	varchar2
  				 ,po_adr_group			OUT     varchar2
  				 ,po_tax_gst_exmp_rf_no		OUT	varchar2
				 ,po_tax_pst_exmp_rf_no 	OUT	varchar2
				 ,po_tax_hst_exmp_rf_no		OUT	varchar2
				 ,po_tax_gst_exmp_eff_dt	OUT	date
				 ,po_tax_pst_exmp_eff_dt	OUT	date
				 ,po_tax_hst_exmp_eff_dt	OUT     date
				 ,po_status_last_date		OUT     date
				 ,po_conv_run_no		OUT     number
				 ,po_client_cons_inds		OUT     varchar2
				  ,po_all_active_subs		OUT	number
 				 ,po_all_reserved_subs		OUT	number
 				 ,po_all_suspended_subs		OUT	number
 				 ,po_all_cancelled_subs		OUT	number
                 ,po_bl_man_hndl_req_opid      OUT number
				 ,po_bl_man_hndl_eff_date      OUT date
				 ,po_bl_man_hndl_exp_date      OUT date
				 ,po_bl_man_hndl_rsn           OUT varchar2
				 ,po_bl_man_hndl_by_opid       OUT number) is

cursor c_account is
	select	distinct ba.ban, ba.ban_status,ba.account_type, ba.account_sub_type,ba.status_last_date
		,ba.start_service_date,ba.col_delinq_status,ba.ar_balance,ba.auto_gen_pym_type,ba.hot_line_ind
		,ba.status_actv_code, ba.status_actv_rsn_code , ba.cs_ret_envlp_ind
		,nvl(to_char(ba.hierarchy_id),'N') hierarchy_id, ba.cs_ca_rep_id
		, ba.inv_suppression_ind, ba.corporate_id , nvl(rtrim(ba.ar_wo_ind),'N')  wo_ind
		,ba.home_province, decode( ba.national_account,'R','R','N','N',null) national_account ,ba.cs_handle_by_ctn_ind
		,ba.tax_gst_exmp_ind, ba.tax_pst_exmp_ind, ba.tax_hst_exmp_ind
 		,ba.tax_gst_exmp_eff_dt, ba.tax_pst_exmp_eff_dt, ba.tax_hst_exmp_eff_dt
 		,ba.tax_gst_exmp_exp_dt, ba.tax_pst_exmp_exp_dt , ba.tax_hst_exmp_exp_dt
 		,ba.tax_gst_exmp_rf_no ,ba.tax_pst_exmp_rf_no ,ba.tax_hst_exmp_rf_no
 		,ba.col_path_code,ba.col_next_step_no,ba.col_next_step_date,ba.col_agncy_code
 		,ba.bl_man_hndl_req_opid, ba.bl_man_hndl_eff_date, ba.bl_man_hndl_exp_date
 		,ba.bl_man_hndl_rsn, ba.bl_man_hndl_by_opid
		,bc.cycle_code, bc.cycle_close_day,nbc.cycle_code next_bill_cycle, nbc.cycle_close_day  next_bill_cycle_close_day
		,substr(c.dealer_code, 1,10) dealer_code, substr(c.dealer_code, 11) sales_rep_code,c.customer_id
 		,c.work_telno,c.work_tn_extno, c.home_telno,c.birth_date
 		,c.contact_telno, c.contact_tn_extno, c.contact_faxno
 		,c.acc_password,c.customer_ssn
 		,c.lang_pref,c.email_address,c.drivr_licns_no , c.drivr_licns_state  ,c.drivr_licns_exp_dt
 		,c.incorporation_no ,c.incorporation_date
 		,c.gur_cr_card_no ,c.gur_cr_card_exp_dt
 		,c.other_telno, c.other_extno, c.other_tn_type
 		,c.verified_date, c.conv_run_no
 		,ad.adr_primary_ln, ad.adr_city, ad.adr_province, ad.adr_postal
 		,adr_type ,adr_secondary_ln ,adr_country  ,adr_zip_geo_code ,adr_state_code
 		,civic_no ,civic_no_suffix , adr_st_direction, adr_street_name ,adr_street_type ,adr_designator
 		,adr_identifier, adr_box ,unit_designator, unit_identifier, adr_area_nm
 		, adr_qualifier ,adr_site , adr_compartment ,adr_attention,adr_delivery_tp, adr_group
 		,nd.first_name, nd.last_business_name, middle_initial,name_title
 		, additional_title , name_format
 		,bdd.credit_card_no ,bdd.card_mem_hold_nm , bdd.expiration_date
 		,bdd.bnk_code, bdd.bnk_acct_number, bdd.bnk_acct_type, bdd.bnk_branch_number
 		,bdd.dd_status, bdd.status_reason
-- 		,m.memo_manual_txt	special_instruction
 		,cs.effective_date cs_effective_date, cs.cu_past_due_amt,cs.cu_age_bucket_0, cs.cu_age_bucket_1_30,cs.cu_age_bucket_31_60
 		,cs.cu_age_bucket_61_90,cs.cu_age_bucket_91_plus
 		,nx_past_due_amt
		,nx_age_bucket_0
		,nx_age_bucket_1_30
		,nx_age_bucket_31_60
		,nx_age_bucket_61_90
		,nx_age_bucket_91_plus
		,fu_past_due_amt
		,fu_age_bucket_0
		,fu_age_bucket_1_30
		,fu_age_bucket_31_60
		,fu_age_bucket_61_90
		,fu_age_bucket_91_plus
	 from 	billing_account ba
 	,customer c
 	,address_name_link anl
 	,address_data   ad
 	,name_data nd
 	,ban_direct_debit bdd
-- 	,memo m
 	,cycle bc
 	,collection_status cs
 	,cycle nbc
    ,logical_date ld
	where   ba.ban=pi_ban
	and 	c.customer_id=ba.customer_id
	and 	anl.customer_id=c.customer_id
	and     (trunc(anl.expiration_date )>trunc(ld.logical_date) or anl.expiration_date  is null)
    and 	anl.link_type = 'B'
    and     ld.logical_date_type='O'
	and     ad.address_id=anl.address_id
	and     nd.name_id=anl.name_id
	and     bdd.ban(+)=ba.ban
--	and     ba.ban=m.memo_ban(+)
--	and     m.memo_type(+)='3000'
	and     bc.cycle_code(+)=ba.bill_cycle
	and     nbc.cycle_code(+)=ba.bl_next_cycle
	and     cs.ban(+)=ba.ban;

acc_rec		c_account%ROWTYPE;

cursor c_sub_summary(p_account_type varchar2, p_account_sub_type varchar2) is
	 select	sum(decode(sub_status,'A',1,0)) A
	,sum(decode(sub_status,'S',1,0)) S
	--,sum(decode(sub_status,'R',1,0)) R
	,sum(decode(sub_status,'C',1,0)) C
	,count(*) total
	from subscriber
	where customer_id=pi_ban
	and ( product_type in ('C','I')
	or (p_account_type in ('I','B') and  p_account_sub_type ='M')
	or (p_account_type ='I' and p_account_sub_type ='J'))
	;

ss_rec	c_sub_summary%ROWTYPE;

cursor c_sub_summary_rsrv(p_account_type varchar2, p_account_sub_type varchar2) is
	select	count(*) total
	from subscriber s, physical_device p
	where s.customer_id=pi_ban
	and ( s.product_type in ('C','I')
	 and s.sub_status='R'
	or (p_account_type in ('I','B') and  p_account_sub_type ='M')
	or (p_account_type ='I' and p_account_sub_type ='J'))
	and p.customer_id=s.customer_id
    	and p.subscriber_no=s.subscriber_no
    	and p.product_type = s.product_type
    	and  p.esn_level = 1
    	and p.expiration_date is null  ;

v_rsrv	number(4);
cursor c_sub_summary_all is
	 select	sum(decode(sub_status,'A',1,0)) A
	,sum(decode(sub_status,'S',1,0)) S
	--,sum(decode(sub_status,'R',1,0)) R
	,sum(decode(sub_status,'C',1,0)) C
	,count(*) total
	from subscriber
	where customer_id=pi_ban;

ss_rec_all	c_sub_summary_all%ROWTYPE;

cursor c_sub_summary_rsrv_all is
	select	count(*) rsrv_total
	from subscriber s, physical_device p
	where s.customer_id=pi_ban
    and s.sub_status='R'
    and p.customer_id=s.customer_id
    and p.subscriber_no=s.subscriber_no
    and p.product_type = s.product_type
    and  p.esn_level = 1
    and p.expiration_date is null ;

v_rsrv_all	number(4);

 cursor c_consent_inds_cur is
    select cpui_cd
        from ban_cpui bc
        where bc.ban=pi_ban;

cursor c_collection is
	select c.col_actv_code, c.col_actv_date,to_char(c.col_actv_date,'mm'), ca.severity_level
	from	collection c
     		,collection_act ca
     		,logical_date ld
	where c.ban=pi_ban
and   c.col_actv_code=ca.col_activity_code
and   trunc(c.col_actv_date) > add_months(last_day(trunc(ld.logical_date)),-12)
and	ld.logical_date_type='O'
--and  ca.severity_level>=3
order by to_char(c.col_actv_date,'mm'),ca.severity_level;

cursor c_col_activity is
select c1.col_step_num, c1.col_actv_code, c1.col_actv_date
from collection c1
where c1.ban = pi_ban
and c1.ent_seq_no =
	(select max(ent_seq_no)
	from collection c2
	where c2.ban = c1.ban and c2.col_actv_type_ind = 'A');



cursor c_dck is
	select	to_char(pa.actv_date,'mm') mm_dck,count(*) nbr
	from	payment p, payment_activity pa
	,backout_rsn_code brc
	where	p.ban=pi_ban
	and	pa.ban=p.ban
	and 	pa.ent_seq_no=p.ent_seq_no
	and	pa.actv_code='BCK'
	and	pa.actv_reason_code=brc.bck_code
	and     brc.dck_history_ind='D'
	and     trunc(pa.actv_date) > add_months(last_day(trunc(sysdate)),-12)
	group	by to_char(pa.actv_date,'mm');

cursor c_finance_history is
	select to_char(fh.act_date,'mm') mm_dck, count(*) nbr
	from finance_history fh
	,backout_rsn_code brc
	where fh.ban=pi_ban
	and fh.act_code='R'
	and brc.bck_code=fh.act_reason_code
	and brc.dck_history_ind='D'
	and trunc(fh.act_date) > add_months(last_day(trunc(sysdate)),-12)
	group by to_char(fh.act_date,'mm');

cursor c_payment is
	select pa.actv_amt,pa.actv_date
	from	payment p, payment_activity pa
	where	p.ban=pi_ban
	and    (p.designation='B' or  p.designation is null)
	and	pa.ban=p.ban
	and 	pa.ent_seq_no=p.ent_seq_no
	and     pa.actv_code in ('PYM' ,'FNTT')
order by pa.actv_date desc, pa.ent_seq_no desc, pa.actv_seq_no desc ;



cursor c_contact_name is
	select nd.first_name, nd.last_business_name, nd.middle_initial,nd.name_title
 		, nd.additional_title ,nd.name_suffix
	from address_name_link anl
 	,name_data nd
    ,logical_date ld
 	where	anl.ban=pi_ban
	and     ( trunc(anl.expiration_date )>trunc(ld.logical_date) or anl.expiration_date  is null)
    and 	anl.link_type = 'C'
    and    ld.logical_date_type='O'
	and     nd.name_id=anl.name_id;

cursor c_bill is
	select bill_due_date
	from bill
 	where	ban=pi_ban
 	and bill_conf_status='C'
 	and  to_char(bill_due_date,'yyyy') !='4700'
	order by bill_due_date desc ;

cursor c_logical_date is
	select logical_date
	from logical_date
	where logical_date_type='O';

v_logical_date DATE;

cn_rec	 c_contact_name%ROWTYPE;

v_payment_amt	number(9,2);
v_payment_date	date;
v_account_type varchar2(1);
v_account_sub_type varchar2(1);

c_consent_inds_rec varchar2(1000);


Begin
open c_logical_date;
fetch c_logical_date into v_logical_date;
close c_logical_date;
open c_account;
fetch c_account into acc_rec;
If c_account%Found Then
	po_ban_status:= acc_rec.ban_status;
	po_account_type:= acc_rec.account_type;
	v_account_type := acc_rec.account_type;
	po_account_sub_type:= acc_rec.account_sub_type;
	v_account_sub_type :=	acc_rec.account_sub_type;
	po_create_date := nvl(acc_rec.start_service_date,acc_rec.status_last_date);
	po_start_service_date:=  acc_rec.start_service_date;
	po_col_delinq_status:= acc_rec.col_delinq_status;
	po_ar_balance	:= acc_rec.ar_balance;
	po_dealer_code	:= acc_rec.dealer_code;
	po_sales_rep_code := acc_rec.sales_rep_code;
	po_bill_cycle :=  acc_rec.cycle_code;
	po_payment_method := acc_rec.auto_gen_pym_type;
	po_work_telno := acc_rec.work_telno;
	po_work_tn_extno := acc_rec.work_tn_extno;
	po_home_telno:= acc_rec.home_telno;
	po_birth_date := acc_rec.birth_date;
	po_contact_faxno := acc_rec.contact_faxno;
	po_acc_password	:= acc_rec.acc_password;
	po_customer_ssn	:= acc_rec.customer_ssn;
	po_adr_primary_ln := acc_rec.adr_primary_ln;
	po_adr_province := acc_rec.adr_province;
	po_adr_city	:= acc_rec.adr_city;
	po_adr_postal	:= acc_rec.adr_postal;
	po_first_name	:= acc_rec.first_name;
	po_last_business_name	:= acc_rec.last_business_name;
--	po_special_instruction 	:= acc_rec.special_instruction;
	po_hotline_ind :=acc_rec.hot_line_ind;
	po_customer_id :=acc_rec.customer_id;
	po_language := acc_rec.lang_pref;
	po_email := acc_rec.email_address;
	po_adr_type := acc_rec.adr_type;
	po_adr_secondary_ln:= acc_rec.adr_secondary_ln ;
	po_adr_country := acc_rec.adr_country ;
	po_adr_zip_geo_code:= acc_rec.adr_zip_geo_code ;
	po_adr_state_code:= acc_rec.adr_state_code ;
	po_civic_no := acc_rec.civic_no;
	po_civic_no_suffix:= acc_rec.civic_no_suffix ;
	po_adr_st_direction:= acc_rec.adr_st_direction;
	po_adr_street_name:= acc_rec.adr_street_name ;
	po_adr_street_type:= acc_rec.adr_street_type ;
	po_adr_designator := acc_rec.adr_designator ;
	po_adr_identifier:= acc_rec.adr_identifier;
	po_adr_box := acc_rec.adr_box;
	po_unit_designator:= acc_rec.unit_designator;
	po_unit_identifier:= acc_rec.unit_identifier;
	po_adr_area_nm := acc_rec.adr_area_nm ;
	po_adr_qualifier:= acc_rec.adr_qualifier ;
	po_adr_site := acc_rec.adr_site;
	po_adr_compartment:= acc_rec.adr_compartment ;
	po_adr_attention := acc_rec.adr_attention;
	po_adr_delivery_type := acc_rec.adr_delivery_tp;
	po_adr_group := acc_rec.adr_group;
	po_middle_initial:= acc_rec.middle_initial;
	po_name_title := acc_rec.name_title  ;
	po_additional_title := acc_rec.additional_title;
	po_drivr_licns_no := acc_rec.drivr_licns_no;
 	po_drivr_licns_state  := acc_rec.drivr_licns_state;
 	po_drivr_licns_exp_dt  := acc_rec.drivr_licns_exp_dt;
 	po_incorporation_no  := acc_rec.incorporation_no;
	po_incorporation_date  := acc_rec.incorporation_date;
	po_gur_cr_card_no := acc_rec.gur_cr_card_no;
	po_gur_cr_card_exp_dt_mm := to_number(to_char(acc_rec.gur_cr_card_exp_dt,'mm'));
	po_gur_cr_card_exp_dt_yyyy := to_number(to_char(acc_rec.gur_cr_card_exp_dt,'yyyy'));
	po_credit_card_no   := acc_rec.credit_card_no ;
	po_card_mem_hold_nm   := acc_rec.card_mem_hold_nm;
	po_expiration_date_mm  := to_number(to_char(acc_rec.expiration_date ,'mm'));
	po_expiration_date_yyyy  := to_number(to_char(acc_rec.expiration_date ,'yyyy'));
	po_status_actv_code := acc_rec.status_actv_code;
        po_status_actv_rsn_code := acc_rec.status_actv_rsn_code;
        po_bill_cycle_close_day := acc_rec.cycle_close_day;
        po_return_envelope_ind := acc_rec.cs_ret_envlp_ind;
        po_corp_hierarhy_ind := acc_rec.hierarchy_id ;
	po_corp_csr_id	:= acc_rec.cs_ca_rep_id ;
	po_inv_supression_ind := acc_rec.inv_suppression_ind ;
	po_bankCode :=	acc_rec.bnk_code ;
  	po_bankAccountNumber := acc_rec.bnk_acct_number	;
  	po_bankBranchNumber  :=	acc_rec.bnk_branch_number;
  	po_bankAccountType  :=	acc_rec.bnk_acct_type;
  	po_directDebitStatus :=	acc_rec.dd_status;
  	po_directDebitStatusRsn	:= acc_rec.status_reason;
  	po_other_phone	:= acc_rec.other_telno;
 	po_other_phone_ext := acc_rec.other_extno;
 	po_other_phone_type := acc_rec.other_tn_type;
	po_home_province  := acc_rec.home_province;
	po_category  := acc_rec.national_account;
	po_next_bill_cycle := acc_rec.next_bill_cycle;
	po_next_bill_cycle_close_day := acc_rec.next_bill_cycle_close_day;
	po_verified_date  := acc_rec.verified_date;
	po_handle_by_subscriber_ind := acc_rec.cs_handle_by_ctn_ind;
	po_corporate_id :=acc_rec.corporate_id ;
	po_write_off_ind := acc_rec.wo_ind;
	po_contact_phone_number	:= acc_rec.contact_telno;
	po_contact_phone_number_ext := acc_rec.contact_tn_extno;
	po_col_path_code := acc_rec.col_path_code;
	po_col_next_step := acc_rec.col_next_step_no;
	po_col_next_actv_date := acc_rec.col_next_step_date;
	po_col_agency	:= acc_rec.col_agncy_code;
	po_status_last_date := acc_rec.status_last_date;
	po_conv_run_no:=acc_rec.conv_run_no;
	po_bl_man_hndl_req_opid := acc_rec.bl_man_hndl_req_opid;
	po_bl_man_hndl_eff_date := acc_rec.bl_man_hndl_eff_date;
	po_bl_man_hndl_exp_date := acc_rec.bl_man_hndl_exp_date;
	po_bl_man_hndl_rsn := acc_rec.bl_man_hndl_rsn;
	po_bl_man_hndl_by_opid := acc_rec.bl_man_hndl_by_opid;

	If acc_rec.tax_gst_exmp_eff_dt <= trunc(sysdate) Then
	po_tax_gst_exmp_ind := acc_rec.tax_gst_exmp_ind;
	po_tax_gst_exmp_exp_dt:=acc_rec.tax_gst_exmp_exp_dt;
	po_tax_gst_exmp_rf_no:=acc_rec.tax_gst_exmp_rf_no;
	po_tax_gst_exmp_eff_dt:=acc_rec.tax_gst_exmp_eff_dt;
	End If;

	If  acc_rec.tax_pst_exmp_eff_dt	 <= trunc(sysdate) Then
	po_tax_pst_exmp_ind :=	acc_rec.tax_pst_exmp_ind;
	po_tax_pst_exmp_exp_dt := acc_rec.tax_pst_exmp_exp_dt;
	po_tax_pst_exmp_rf_no :=acc_rec.tax_pst_exmp_rf_no;
	po_tax_pst_exmp_eff_dt :=acc_rec.tax_pst_exmp_eff_dt;
	End If;

	If acc_rec.tax_hst_exmp_eff_dt  <= trunc(sysdate) Then
	po_tax_hst_exmp_ind:=	acc_rec.tax_hst_exmp_ind;
	po_tax_hst_exmp_exp_dt:= acc_rec.tax_hst_exmp_exp_dt;
	po_tax_hst_exmp_rf_no := acc_rec.tax_hst_exmp_rf_no;
	po_tax_hst_exmp_eff_dt := acc_rec.tax_hst_exmp_eff_dt;
	End If;

 	If trunc(acc_rec.cs_effective_date)=trunc(v_logical_date) Then
 		po_cu_age_bucket_0	:= acc_rec.cu_age_bucket_0;
		po_cu_age_bucket_1_30	:= acc_rec.cu_age_bucket_1_30;
		po_cu_age_bucket_31_60	:=acc_rec.cu_age_bucket_31_60;
		po_cu_age_bucket_61_90	:=acc_rec.cu_age_bucket_61_90;
		po_cu_age_bucket_91_plus :=acc_rec.cu_age_bucket_91_plus;
		po_cu_past_due_amt :=acc_rec.cu_past_due_amt;
	elsif trunc(acc_rec.cs_effective_date +1)=trunc(v_logical_date) Then
 		po_cu_age_bucket_0	:= acc_rec.nx_age_bucket_0;
		po_cu_age_bucket_1_30	:= acc_rec.nx_age_bucket_1_30;
		po_cu_age_bucket_31_60	:=acc_rec.nx_age_bucket_31_60;
		po_cu_age_bucket_61_90	:=acc_rec.nx_age_bucket_61_90;
		po_cu_age_bucket_91_plus :=acc_rec.nx_age_bucket_91_plus;
		po_cu_past_due_amt :=acc_rec.nx_past_due_amt;
	else
		po_cu_age_bucket_0	:= acc_rec.fu_age_bucket_0;
		po_cu_age_bucket_1_30	:= acc_rec.fu_age_bucket_1_30;
		po_cu_age_bucket_31_60	:=acc_rec.fu_age_bucket_31_60;
		po_cu_age_bucket_61_90	:=acc_rec.fu_age_bucket_61_90;
		po_cu_age_bucket_91_plus :=acc_rec.fu_age_bucket_91_plus;
		po_cu_past_due_amt :=acc_rec.fu_past_due_amt;

	End If;


 		open c_contact_name;
 		fetch c_contact_name into cn_rec;
 			po_contact_first_name	:= cn_rec.first_name;
 			po_contact_last_name := cn_rec.last_business_name;
 			po_contact_name_title := cn_rec.name_title  ;
			po_contact_middle_initial := cn_rec.middle_initial;
			po_contact_additional_title := cn_rec.additional_title;
			po_contact_name_suffix	:= cn_rec.name_suffix;
 		close c_contact_name;


If acc_rec.account_type ='B' and  acc_rec.account_sub_type in ( 'P' ,'3') Then
 		po_legal_business_name:= acc_rec.additional_title;

        End If;
else
 raise 	BANNotFound;

End If;
close c_account;
--
open c_sub_summary(v_account_type,v_account_sub_type);
fetch c_sub_summary into ss_rec;
po_active_subs:= nvl(ss_rec.A,0);
po_suspended_subs:= nvl(ss_rec.S,0);
po_cancelled_subs:= nvl(ss_rec.C,0);
close c_sub_summary;
--
open c_sub_summary_rsrv(v_account_type,v_account_sub_type);
fetch c_sub_summary_rsrv into v_rsrv;
po_reserved_subs:= v_rsrv;
close c_sub_summary_rsrv;
--
open c_sub_summary_all();
fetch c_sub_summary_all into ss_rec_all;
po_all_active_subs:= nvl(ss_rec_all.A,0);
po_all_suspended_subs:= nvl(ss_rec_all.S,0);
po_all_cancelled_subs:= nvl(ss_rec_all.C,0);
close c_sub_summary_all;
--
open c_sub_summary_rsrv_all();
fetch c_sub_summary_rsrv_all into v_rsrv_all;
po_all_reserved_subs:= v_rsrv_all;
close c_sub_summary_rsrv_all;
--
for col_rec in c_collection loop
	If to_char(col_rec.col_actv_date,'mm')='01' Then
		po_col_actv_JAN:=col_rec.col_actv_code;
	Elsif to_char(col_rec.col_actv_date,'mm')='02' Then
	 	po_col_actv_FEB:=col_rec.col_actv_code;
	Elsif to_char(col_rec.col_actv_date,'mm')='03' Then
	 	po_col_actv_MAR:=col_rec.col_actv_code;
	Elsif to_char(col_rec.col_actv_date,'mm')='04' Then
	 	po_col_actv_APR:=col_rec.col_actv_code;
	Elsif to_char(col_rec.col_actv_date,'mm')='05' Then
	 	po_col_actv_MAY:=col_rec.col_actv_code;
	Elsif to_char(col_rec.col_actv_date,'mm')='06' Then
	 	po_col_actv_JUN:=col_rec.col_actv_code;
	Elsif to_char(col_rec.col_actv_date,'mm')='07' Then
	 	po_col_actv_JUL:=col_rec.col_actv_code;
	Elsif to_char(col_rec.col_actv_date,'mm')='08' Then
	 	po_col_actv_AUG:=col_rec.col_actv_code;
	Elsif to_char(col_rec.col_actv_date,'mm')='09' Then
	 	po_col_actv_SEP:=col_rec.col_actv_code;
	Elsif to_char(col_rec.col_actv_date,'mm')='10' Then
	 	po_col_actv_OCT:=col_rec.col_actv_code;
	Elsif to_char(col_rec.col_actv_date,'mm')='11' Then
	 	po_col_actv_NOV:=col_rec.col_actv_code;
	Elsif to_char(col_rec.col_actv_date,'mm')='12' Then
	 	po_col_actv_DEC:=col_rec.col_actv_code;
        End If;
end loop;

po_DCK_JAN:=0;
po_DCK_FEB:=0;
po_DCK_MAR:=0;
po_DCK_APR:=0;
po_DCK_MAY:=0;
po_DCK_JUN:=0;
po_DCK_JUL:=0;
po_DCK_AUG:=0;
po_DCK_SEP:=0;
po_DCK_OCT:=0;
po_DCK_NOV:=0;
po_DCK_DEC:=0;

for dck_rec in c_dck loop
	If  dck_rec.mm_dck='01' Then
		po_DCK_JAN:=dck_rec.nbr;
	Elsif dck_rec.mm_dck='02' Then
	 	po_DCK_FEB:=dck_rec.nbr;
	Elsif dck_rec.mm_dck='03' Then
	 	po_DCK_MAR:=dck_rec.nbr;
	Elsif dck_rec.mm_dck='04' Then
	 	po_DCK_APR:=dck_rec.nbr;
	Elsif dck_rec.mm_dck='05' Then
	 	po_DCK_MAY:=dck_rec.nbr;
	Elsif dck_rec.mm_dck='06' Then
	 	po_DCK_JUN:=dck_rec.nbr;
	Elsif dck_rec.mm_dck='07' Then
	 	po_DCK_JUL:=dck_rec.nbr;
	Elsif dck_rec.mm_dck='08' Then
	 	po_DCK_AUG:=dck_rec.nbr;
	Elsif dck_rec.mm_dck='09' Then
	 	po_DCK_SEP:=dck_rec.nbr;
	Elsif dck_rec.mm_dck='10' Then
	 	po_DCK_OCT:=dck_rec.nbr;
	Elsif dck_rec.mm_dck='11' Then
	 	po_DCK_NOV:=dck_rec.nbr;
	Elsif dck_rec.mm_dck='12' Then
	 	po_DCK_DEC:=dck_rec.nbr;
        End If;
end loop;

for fh_rec in c_finance_history loop
	If  fh_rec.mm_dck='01' Then
		po_DCK_JAN:=po_DCK_JAN + fh_rec.nbr;
	Elsif fh_rec.mm_dck='02' Then
	 	po_DCK_FEB:=po_DCK_FEB + fh_rec.nbr;
	Elsif fh_rec.mm_dck='03' Then
	 	po_DCK_MAR:=po_DCK_MAR + fh_rec.nbr;
	Elsif fh_rec.mm_dck='04' Then
	 	po_DCK_APR:=po_DCK_APR + fh_rec.nbr;
	Elsif fh_rec.mm_dck='05' Then
	 	po_DCK_MAY:=po_DCK_MAY + fh_rec.nbr;
	Elsif fh_rec.mm_dck='06' Then
	 	po_DCK_JUN:=po_DCK_JUN + fh_rec.nbr;
	Elsif fh_rec.mm_dck='07' Then
	 	po_DCK_JUL:=po_DCK_JUL + fh_rec.nbr;
	Elsif fh_rec.mm_dck='08' Then
	 	po_DCK_AUG:=po_DCK_AUG + fh_rec.nbr;
	Elsif fh_rec.mm_dck='09' Then
	 	po_DCK_SEP:=po_DCK_SEP + fh_rec.nbr;
	Elsif fh_rec.mm_dck='10' Then
	 	po_DCK_OCT:=po_DCK_OCT + fh_rec.nbr;
	Elsif fh_rec.mm_dck='11' Then
	 	po_DCK_NOV:=po_DCK_NOV + fh_rec.nbr;
	Elsif fh_rec.mm_dck='12' Then
	 	po_DCK_DEC:=po_DCK_DEC + fh_rec.nbr;
        End If;
end loop;
-- c_consent_ind_rec varchar(1000);

c_consent_inds_rec := '';
po_client_cons_inds := '';
   open c_consent_inds_cur;
    loop
         fetch c_consent_inds_cur into c_consent_inds_rec;
         exit when c_consent_inds_cur%NotFound;
         po_client_cons_inds := po_client_cons_inds||c_consent_inds_rec||'|';
    end loop;
    po_client_cons_inds := Substr(po_client_cons_inds,1,Instr(po_client_cons_inds,'|',-1)-1);
 close c_consent_inds_cur;


open   c_payment;
fetch c_payment into v_payment_amt
		     ,v_payment_date;
If c_payment%Found Then
po_last_payment_amnt:=v_payment_amt;
po_last_payment_date:=v_payment_date;
End if;
close  c_payment;
open c_bill;
fetch c_bill into po_bill_due_date;
close c_bill;
open c_col_activity;

fetch c_col_activity into po_col_step
			  ,po_col_actv_code
 			  ,po_col_actv_date;
close  	c_col_activity;

Exception
When BANNotFound Then
	If c_account%isOpen Then
		close c_account;
	End If;
	Raise_Application_Error(-20101, 'BAN  Not Found');
When Others Then
	If c_account%isOpen Then
		close c_account;
	End If;
	If c_sub_summary%isOpen Then
		close c_sub_summary;
	End If;
	If c_collection%isOpen Then
		close c_collection;
	End If;
	If c_dck%isOpen Then
		close c_dck;
	End If;
	If c_payment%isOpen Then
		close c_payment;
	End If;
	If c_contact_name%isOpen Then
		close c_contact_name;
	End If;
	raise;
End GetAccountInfoByBAN;








End;
/

sho err
