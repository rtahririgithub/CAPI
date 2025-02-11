--service_feature_o in AMDOCS_EXTO_REF in PR as of Jan 2015 release

DROP TYPE service_feature_t
/

CREATE OR REPLACE TYPE service_feature_o AS OBJECT (
   service_type                    VARCHAR2 (1),
   soc                             VARCHAR2 (9),
   soc_description                 VARCHAR2 (30),
   soc_description_f               VARCHAR2 (30),
   minimum_no_months               NUMBER (3, 0),
   eserve_ind                      VARCHAR2 (1),
   rda_ind                         VARCHAR2 (1),
   bl_zero_chrg_suppress_ind       VARCHAR2 (1),
   coverage_type                   VARCHAR2 (10),
   promo                           VARCHAR2 (1),
--------------------------------------------------
   soc_status                      VARCHAR2 (1),
   for_sale_ind                    VARCHAR2 (1),
   sale_eff_date                   DATE,
   sale_exp_date                   DATE,
   current_ind                     VARCHAR2 (1),
   telephony_features_inc          VARCHAR2 (1),
   dispatch_features_inc           VARCHAR2 (1),
   wireless_web_features_inc       VARCHAR2 (1),
   s_product_type                  VARCHAR2 (1),
   equiv_service_exists            VARCHAR2 (1), --this attribute no longer be populated as of Nov 2011 release
--------------------------------------------------
   period_set_code                 VARCHAR2 (4),
   loyalty_and_retention_service   VARCHAR2 (1), --this attribute no longer be populated as of Nov 2011 release
   soc_level_code                  VARCHAR2 (1),
   included_promo_ind              VARCHAR2 (1),
   client_available_ind            VARCHAR2 (1),
   dealer_available_ind            VARCHAR2 (1),
   discount_available              VARCHAR2 (1),
   suspension_price_plan           VARCHAR2 (1),
   max_subscribers_number          NUMBER (3, 0),
   secondary_soc                   VARCHAR2 (9),
--------------------------------------------------
   family_type                     VARCHAR2 (1),
   act_avail_ind                   VARCHAR2 (1),
   chng_avail_ind                  VARCHAR2 (1),
   client_chng_avail_ind           VARCHAR2 (1),
   dealer_chng_avail_ind           VARCHAR2 (1),
   client_modify_avail_ind         VARCHAR2 (1),
   dealer_modify_avail_ind         VARCHAR2 (1),
   corporate_renewal_ind           VARCHAR2 (1),
   non_corporate_renewal_ind       VARCHAR2 (1),
   ares_avail_ind                  VARCHAR2 (1),
--------------------------------------------------
   csa_avail_ind                   VARCHAR2 (1),
   soc_def_user_seg                VARCHAR2 (10),
   min_req_usg_chrg                NUMBER (11, 0),
   min_commit_amt                  NUMBER (9, 2),
   service_type_ind                VARCHAR2 (1),
   mp_ind                          VARCHAR2 (1),
   duration                        NUMBER (3, 0),
   lbs_tracker                      VARCHAR2 (1),--this attribute no longer be populated as of Nov 2011 release
   lbs_trackee                      VARCHAR2 (1),--this attribute no longer be populated as of Nov 2011 release
   feature_code                    VARCHAR2 (6),
--------------------------------------------------
   feature_desc                    VARCHAR2 (100),
   feature_desc_f                  VARCHAR2 (100),
   dup_ftr_allow_ind               VARCHAR2 (1),
   csm_param_req_ind               VARCHAR2 (1),
   rc_freq_of_pym                  NUMBER (2, 0),
   rc_rate                         NUMBER (9, 2),
   uc_rate                         NUMBER (9, 2),
   category_code                   VARCHAR2 (6),
   msisdn_ind                      VARCHAR2 (1),
   ftr_service_type                VARCHAR2 (1),
--------------------------------------------------
   switch_code                     VARCHAR2 (6),
   f_product_type                  VARCHAR2 (1),
   brc_soc                         VARCHAR2 (9),
   uc_rounding_factor              NUMBER (7, 2),
   mpc_ind						   VARCHAR2 (1),
   brand_ind                       NUMBER (3, 0),
   calling_circle_size             NUMBER(3,0),
   has_promotion                   VARCHAR2 (1),
   has_bound                       VARCHAR2 (1),
   is_bound                        VARCHAR2 (1),
--------------------------------------------------
   has_seq_bound                   VARCHAR2 (1),
   is_seq_bound                    VARCHAR2 (1),
   aom_avail_ind                   VARCHAR2 (1),
   mandatory                       VARCHAR2 (1),
   equipment_type                  VARCHAR2 (1),
   promo_validation_eligible	   VARCHAR2 (1),--this attribute no longer be populated as of Nov 2011 release
   feature_type					   VARCHAR2 (1),
   pool_group_id				   NUMBER (3, 0),
   mci_ind 						   VARCHAR2 (1),
   def_sw_params				   VARCHAR2 (2000),
   network_type                    VARCHAR2 (1),
   pda_mandatory_ind               VARCHAR2 (1),
   rim_mandatory_ind               VARCHAR2 (1),
   bill_cycle_treatment_cd         VARCHAR2 (4),
   --------------------------------------------------
   seat_type                	   VARCHAR2 (4),
   soc_service_type                VARCHAR2 (4),
   soc_duration_hours              NUMBER(9),

   --define a constructor: take no parameter
   CONSTRUCTOR FUNCTION service_feature_o
    RETURN SELF AS RESULT

);
/

CREATE OR REPLACE TYPE BODY service_feature_o AS

   --This constructor only initialize the attributes that need to have non null values.
   --Procedure shall switch to this constructor, reason for this change:
   -- 1. each time we add new attribute, there is one less spot need to be changed.
   -- 2  allow existing pakcage continue working when we develop new pacakge working with new structure of this object
   -- (of cause only net addition )
  CONSTRUCTOR FUNCTION service_feature_o
    RETURN SELF AS RESULT
  AS
  BEGIN
    SELF.brand_ind :=1;
    SELF.pda_mandatory_ind :='N';
    SELF.rim_mandatory_ind :='N';

    RETURN;
  END; --CONSTRUCTOR FUNCTION

END;  --TYPE BODY
/

CREATE OR REPLACE TYPE service_feature_t AS TABLE OF service_feature_o
/

COMMIT;