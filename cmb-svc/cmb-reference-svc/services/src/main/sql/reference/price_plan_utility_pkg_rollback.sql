/* Formatted on 2006/11/06 14:00 (Formatter Plus v4.8.0) */
CREATE OR REPLACE PACKAGE price_plan_utility_pkg
AS
-------------------------------------------------------------------------
-- IMPORTANT NOTE !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
-- Please increment the version_no variable value when you make changes to this package 
-- to reflect version changes.
------------------------------------------------------------------------
-- description: Package Price_Plan_pkg containing procedures
--    for price plan related data retrieval from Knowbility database
--
-- Date           Developer           Modifications
-- 31-03-2006     Marina Kuper        Fixed c_soc_rel query
-- 06-04-2006     Marina Kuper        Added bl_zero_chrg_suppress_ind
-- 17-04-2006     Marina Kuper        Added procedure getIncludedPromotions
-- 25-04-2006     Marina Kuper        Added procedure getPricePlan
-- 15-05-2006     Marina Kuper        Changed procedure getPricePlan
-- 18-05-2006     Marina Kuper        Added logic for LBS
-- 30-06-2004     Marina Kuper        Fixed UC rate in getfeaturesforpriceplan
-- 05-07-2006     Dimitry Siganevich  Add new fields mp_ind and mpc_ind in GetPricePlan, GetIncludedServices,GetIncludedPromotions, GetOptionalServices
-- 17-07-2006     Marina Kuper        Fixed  procedure getIncludedPromotions and procedure getIncludedServices - duration
-- 11-08-2006     Marina Kuper        Fixed LBS issue
-- 26-11-2006     Marina Kuper        Fixed GetOptionalservices
-- Oct 27,2006    Tsz Chung Tong      Modified initservicefeatureobject
-- Oct 30,2006    Tsz Chung Tong      Added brand indicator to getpriceplan
-- 30-10-2006     Marina Kuper        Fixed performance issue in GetOptionalservices
-- 06-11-2006     Marina Kuper        Fixed performance issue in GetIncludedPromotions
-- 08-11-2006     Marina Kuper        Fixed performance issue in GetPricePlan
-- 14-11-2006     Marina Kuper        Removed unused logical date
-- 29-11-2006     Roman Tov           new function getpriceplanlist
-- Jun 22,2007    Michael Liao        Added new field calling_circle_size in getfeaturesforservice and getfeaturesforpriceplan
-- Sep 12,2007    Marina Kuper        Fixed soc_relation_retrievals
-- Nov 19,2007    Marina Kuper        Added aom_avail_ind to PP queries
-- Jan 10,2008    Tsz Chung Tong			Include family type "A" in GetOptionalservices
-- Jan 24,2008    Tsz Chung Tong			Rolled back changes done on Jan 10, 2008 due to project postpone
-- Feb 21,2008    Tsz Chung Tong			Include changes made on Jan 10, 2008 again for Apr release
-- Mar 17,2008    Tsz Chung Tong      Minor optimization
-- Mar 25,2008    Tsz Chung Tong      Retrieve equipment type from soc_equip_relation in getOptionalServices
-- Mar 26,2008    Tsz Chung Tong      Retrieve equipment type from soc_equip_relation in GetIncludedPromotions
-- Apr 08,2008    Richard Fong  	  Updated getoptionalservices and getincludedpromotions to support new 'Z' family type
-- Apr 14,2008    Richard Fong  	  Added getregularservices procedure
-- Aug 18, 2008	  Richard Fong		  Added support for FEATURE.feature_type and FEATURE.pool_group_id to all queries
-- Aug 27, 2008	  Richard Fong		  Added support for SOC.mci_ind to getpriceplan and getpriceplanlist queries
-- Sep 19, 2008	  Richard Fong		  Added support for SOC.coverage_type to all service and priceplan queries
-- Jan 16, 2008   Tsz Chung Tong      Updated getoptionalservices and getincludedpromotions to include network_type in query
-- Jan 26, 2009	  Andrew Pereira	  Updated getpriceplanlist to incorporate network_type into query (Holborn R1)
-- Jan 27, 2009   Tsz Chung Tong     Fixed getoptionalsrevices and getincludedpromotions
-- Feb 06, 2009   Tsz Chung Tong     Fixed getpriceplan, getIncludedServices, getRegularServices to retrieve network_type
-- Feb 18, 2009   Andrew Pereira	 removed unnecessary duplicate conditions in getincludedpromotions function
-- Mar 24, 2009	  Andrew Pereira	  Applied changes to support the launch of the new TBS VTT SOC by obtaining the default 
--									  parameter value from the def_sw_params column of the feature table (for VTT features only).
-- Apr 01, 2009   Tsz Chung TOng     - Added procedure checkMandatorySOC and modified GetIncludedServices,GetIncludedPromotions, GetOptionalServices to utitlize the new procedure
-- Apr 03, 2009   Tsz Chung TOng     - Added procedure checkMandatorySOCs.
-- May 25, 2009   Tsz Chung TOng     - Added procedure getSocEquipRelations and removed calls to getNetworkTypeBySOC
-- Sept 16, 2010  Michael Liao       - Modify Query in following procedures to return new column BILL_CYCLE_TREATMENT_CD on SOC table 
--                                     initservicefeatureobject, GetIncludedPromotions, GetIncludedServices, GetOptionalServices,
--                                     GetPricePlan,GetPricePlanList
-- Oct 14, 2011   Michael Liao       - Modified query in following procdures to, include brand_id, exclude soc_group_family  
--                                      GetIncludedPromotions, GetIncludedServices, GetOptionalServices, GetPricePlan, GetPricePlanList
--                                   - Added two new procedures: getSocFamilyTypes  , getSocAllowSharingGroups
--  May-11-2012     Naresh Annabathula     Added getVersion function for shakedown
-- 3.20.1 July 12, 2012 Michael Liao   -Modify query in getfeaturesforservice  , getfeaturesforpriceplan: 
--                                    -add criteria: PP_UC_RATE.RATE_VERSION_NUM=0 
-- 3.20.2 July 18, 2012 Michael Liao   -bug fix: query in getfeaturesforservice  , getfeaturesforpriceplan shall use outter join for PP_UC_RATE.RATE_VERSION_NUM
-- October 25, 2013	Kris Viceral	  -bug fix: getpriceplanlist: Province availability issue: Plans not available for any Province when db entry is updated with "ALL"
-- May 24, 2014	3.28.1 Richard Fong	  - Modified the folowing functions/procedures for BF 2.0:
-- 										- getpriceplan: added seat_type and soc_service_type
-- 										- getpriceplanlist: added seat_type and soc_service_type
--										- getincludedpromotions: added soc_service_type
--										- getincludedservices: added soc_service_type
--										- getoptionalservices: added soc_service_type
-- 3.29.1  July 14, 2014   Maxim Strigachov - Added support for duration service addition to a contract.
-- 3.29    July 16, 2014   Maxim Strigachov - Modified the folowing functions/procedures for RLH:
--										- getpriceplan: added soc_duration_hours
-- 										- getpriceplanlist: added soc_duration_hours
--										- getincludedpromotions: soc_duration_hours
--										- getincludedservices: soc_duration_hours
--										- getoptionalservices: soc_duration_hours
-- Feb 17, 2015 3.40 Richard Fong 	  - Modified the folowing functions/procedures for RLH phase 2:
-- 										- getpriceplan: added rlh_ind
-- 										- getpriceplanlist: added rlh_ind
--										- getincludedpromotions: added rlh_ind
--										- getincludedservices: added rlh_ind
--										- getoptionalservices: added rlh_ind
-------------------------------------------------------------------------------------------------------------------------------------
   
   priceplannotfound            EXCEPTION;
   PRAGMA EXCEPTION_INIT (priceplannotfound, -20101);
   servicesnotfound             EXCEPTION;
   PRAGMA EXCEPTION_INIT (servicesnotfound, -20102);
   optionalservicenotfound      EXCEPTION;
   PRAGMA EXCEPTION_INIT (optionalservicenotfound, -20103);
   includedpromotionsnotfound   EXCEPTION;
   PRAGMA EXCEPTION_INIT (includedpromotionsnotfound, -20104);

   TYPE refcursor IS REF CURSOR;

   v_service_feature_tab        service_feature_t;
   brand_telus         CONSTANT NUMBER (1)        := 1.0;
   brand_ampd          CONSTANT NUMBER (1)        := 2.0;

-- result constants
   numeric_true        CONSTANT NUMBER (1)     := 0.0;
   numeric_false       CONSTANT NUMBER (1)     := 1.0;
-- error messages
   err_invalid_input   CONSTANT VARCHAR2 (100)
                                   := 'Input parameters are invalid or NULL.';
   err_no_data_found   CONSTANT VARCHAR2 (100) := 'No data found.';
   err_other           CONSTANT VARCHAR2 (100) := 'Other PL/SQL error.';
   -- package version constant
   version_no          CONSTANT VARCHAR2(10)       := '3.40';

  FUNCTION getVersion RETURN VARCHAR2;

-------------------------------------------------------------------------
-- Get Optional Services For The Given Price Plan
--------------------------------------------------------------------------
   PROCEDURE getoptionalservices (
      pi_price_plan_code    IN       VARCHAR2,
      pi_equipment_type     IN       VARCHAR2,
      pi_network_type       IN       VARCHAR2,
      pi_province_code      IN       VARCHAR2,
      pi_account_type       IN       VARCHAR2,
      pi_account_sub_type   IN       VARCHAR2,
      po_services           OUT      refcursor
   );

-------------------------------------------------------------------------
-- Get Include Promotions For The Given Price Plan
--------------------------------------------------------------------------
   PROCEDURE getincludedpromotions (
      pi_price_plan_code   IN       VARCHAR2,
      pi_equipment_type    IN       VARCHAR2,
      pi_network_type      IN       VARCHAR2,
      pi_province_code     IN       VARCHAR2,
      pi_term              IN       NUMBER,
      po_promotions        OUT      refcursor
   );

----------------------------------------------------------------------------------
-- Get Features for SOC
--------------------------------------------------------------------------
   PROCEDURE getfeaturesforservice (pi_service_feature_ob IN service_feature_o);

----------------------------------------------------------------------------------
-- Get Features for PricePlan
--------------------------------------------------------------------------
   PROCEDURE getfeaturesforpriceplan (
      pi_service_feature_ob   IN   service_feature_o
   );

----------------------------------------------------------------------------------------
   PROCEDURE initservicefeatureobject (
      pi_service_feature_ob   IN OUT   service_feature_o
   );

-------------------------------------------------------------------------
-- Get Include Services For The Given Price Plan
--------------------------------------------------------------------------
   PROCEDURE getincludedservices (
      pi_price_plan_code   IN       VARCHAR2,
      po_incl_services     OUT      refcursor
   );

-------------------------------------------------------------------------
-- Get PricePlan
--------------------------------------------------------------------------
   PROCEDURE getpriceplan (
      pi_price_plan_code   IN       VARCHAR2,
      po_price_plan        OUT      refcursor
   );

  PROCEDURE getlogicaldate (po_logical_date OUT DATE);
  
  FUNCTION getpriceplanlist(
      pi_productType       	IN		VARCHAR2, 
	  pi_provinceCode		IN		VARCHAR2, 
	  pi_account_type       IN      VARCHAR2,
      pi_equipment_type     IN      VARCHAR2,
	  pi_brand				IN		NUMBER,
	  pi_currentPlansOnly	IN		VARCHAR2, 
	  pi_availableForActivation IN	VARCHAR2,
	  pi_networkType		IN		VARCHAR2,
	  po_priceplans			OUT	refcursor,
	  po_error_message      OUT     VARCHAR2
	  )
	  return NUMBER;  
	  
	FUNCTION getNetworkTypeBySOC (pi_soc VARCHAR2)
	  RETURN VARCHAR2;

   PROCEDURE checkMandatorySOC(
   			pi_soc        IN  VARCHAR2,
   			po_pda_count  OUT NUMBER,
   			po_rim_count  OUT NUMBER
   );
   
   PROCEDURE checkMandatorySOCs(
   			pi_socs        IN  t_socs,
   			po_result     OUT REFCURSOR
   );
   
	PROCEDURE getSocEquipRelations(
   			pi_socs				IN	t_socs,
   			po_result			OUT	REFCURSOR);
	
   	PROCEDURE getSocFamilyTypes(
   			pi_socs				IN	t_socs,
   			po_result			OUT	REFCURSOR);

   	PROCEDURE getSocAllowSharingGroups(
   			pi_socs				IN	t_socs,
   			po_result			OUT	REFCURSOR);
END;
/

SHO err



CREATE OR REPLACE PACKAGE BODY price_plan_utility_pkg
AS
 FUNCTION getVersion RETURN VARCHAR2
   IS
   BEGIN
	   RETURN version_no;
	END getVersion;
--------------------------------------------------------------------------------------------------------------------
  PROCEDURE getoptionalservices (
      pi_price_plan_code    IN       VARCHAR2,
      pi_equipment_type     IN       VARCHAR2,
      pi_network_type       IN       VARCHAR2,
	    pi_province_code      IN       VARCHAR2,
      pi_account_type       IN       VARCHAR2,
      pi_account_sub_type   IN       VARCHAR2,
      po_services           OUT      refcursor
   )
   IS
      v_service_feature_ob   service_feature_o;
      -- v_soc_ob               soc_o;
      v_socs_tab             t_socs;
      v_logical_date         DATE;
      pda_mandatory_count   NUMBER;
      rim_mandatory_count   NUMBER;

      CURSOR c_related_socs (p_price_plan_code VARCHAR2, v_logical_date DATE)
      IS
         SELECT s.soc
           FROM soc_relation sr, soc_group sg_s, soc_group sg_p, soc s
          WHERE sg_s.soc = s.soc
            AND sg_p.soc = pi_price_plan_code
            AND sr.soc_src = sg_s.gp_soc
            AND sr.soc_dest = sg_p.gp_soc
            AND TRUNC (sr.src_effective_date) <= TRUNC (v_logical_date)
            AND TRUNC (sr.dest_effective_date) <= TRUNC (v_logical_date)
            AND (   sr.expiration_date IS NULL OR
            				TRUNC (sr.expiration_date) > TRUNC (v_logical_date)
                )
            AND sr.relation_type = 'G'
            AND s.soc_status = 'A'
            AND s.for_sale_ind = 'Y'
            AND s.current_ind = 'Y';

      related_socs_rec       c_related_socs%ROWTYPE;

      CURSOR c_services (
         pi_price_plan_code    VARCHAR2,
         pi_equipment_type     VARCHAR2,
         pi_province_code      VARCHAR2,
         pi_account_type       VARCHAR2,
         pi_account_sub_type   VARCHAR2,
         v_logical_date        DATE
      )
      IS
         SELECT DISTINCT s.service_type, s.soc, s.soc_description,
                         s.soc_description_f, 
                         NVL (s.minimum_no_months, 0) AS minimum_no_months,
                         NVL (s.eserve_ind, 'N') AS eserve_ind,
                         NVL (s.rda_ind, 'N') AS rda_ind,
                         s.bl_zero_chrg_suppress_ind,
                         DECODE (uspt.soc,
                                 NULL, 'N',
                                 'Y'
                                ) AS promo, s.soc_status, s.for_sale_ind,
                         s.sale_eff_date, s.sale_exp_date, s.current_ind,
                         DECODE (s.product_type,
                                 'C', 'Y',
                                 s.inc_cel_ftr_ind
                                ) AS telephony_features_inc,
                         s.inc_dc_ftr_ind, s.inc_pds_ftr_ind, s.product_type,
                         s.period_set_code, s.soc_level_code, ser.equipment_type, ser.network_type, 
						 s.coverage_type, s.bill_cycle_treatment_cd, 
						 NVL (s.brand_id, 1) brand_id, --TBS change
						 s.soc_service_type,
						 s.soc_duration_hours,
						 s.rlh_ind
                    FROM soc_group s_gp_p,
                         acct_type_priceplan_soc_groups atpsg,
                         priceplan_soc_groups psg,
                         soc_group s_gp_s,
                         soc s,
                         soc ps,
                         soc_submkt_relation ssr,
                         market m,
                         soc_equip_relation ser,
                         uc_soc_promo_terms uspt
                   WHERE s_gp_p.soc = pi_price_plan_code
                     AND psg.price_plan_group = s_gp_p.gp_soc
                     AND atpsg.acct_type = pi_account_type
                     AND atpsg.acct_sub_type = pi_account_sub_type
                     AND atpsg.priceplan_soc_group_id =
                                                    psg.priceplan_soc_group_id
                     AND s_gp_s.gp_soc = psg.soc_group
                     AND s.soc = s_gp_s.soc
                     AND s.soc_status = 'A'
                     AND s.for_sale_ind = 'Y'
                     AND s.current_ind = 'Y'
                     AND s.product_type = ps.product_type
                     AND TRUNC (s.sale_eff_date) <= TRUNC (v_logical_date)
                     AND (   s.sale_exp_date IS NULL OR
                     				TRUNC (s.sale_exp_date) > TRUNC (v_logical_date)
                         )
                     AND ps.soc = pi_price_plan_code
                     AND m.province = pi_province_code
                     AND (   ssr.sub_market = 'ALL' OR
                     					ssr.sub_market = m.market_code
                         )
                     AND ssr.soc = s.soc
                     AND ser.soc = s.soc
                     AND ser.effective_date = s.effective_date
                     AND (   ser.equipment_type = pi_equipment_type
                          OR ser.equipment_type = '9'
                         )
										 AND (pi_network_type = '9' OR
												  ser.network_type = pi_network_type OR 
													ser.network_type='9'
													)
                     AND uspt.soc(+) = s.soc
                     AND uspt.soc IS NULL
                     AND s.soc IN (SELECT *
                                     FROM TABLE (CAST (v_socs_tab AS t_socs)))
                ORDER BY 2;

      services_rec           c_services%ROWTYPE;

      CURSOR c_soc_rel (p_soc VARCHAR2, v_logical_date DATE)
      IS
         SELECT soc_src, soc_dest,
                DECODE (relation_type,
                        'M', 'W',
                        'S', 'X',
                        NULL
                       ) AS relation_type
           FROM soc_relation_ext sr
          WHERE soc_dest = p_soc AND relation_type IN ('M', 'S')
          AND (   sr.expiration_date IS NULL OR
          				TRUNC (sr.expiration_date) > TRUNC (v_logical_date)
                         )
         UNION
         SELECT soc_src, soc_dest, relation_type
           FROM soc_relation sr
          WHERE soc_dest = p_soc AND relation_type = 'F'
         UNION
         SELECT soc_dest, soc_src, relation_type
           FROM soc_relation_ext sr
          WHERE soc_src = p_soc AND relation_type IN ('M', 'S')
           AND (   sr.expiration_date IS NULL OR
           				TRUNC (sr.expiration_date) > TRUNC (v_logical_date)
                         );

      soc_rel_rec            c_soc_rel%ROWTYPE;

/* Oct 04,2011 M.Liao: remove this becaues we populate familyType info outside of procedure         
      CURSOR c_soc_fam (p_soc VARCHAR2)
      IS
         SELECT sfg.family_type
           FROM soc s, soc_group sg, soc_family_group sfg
          WHERE s.soc = p_soc
            AND sg.soc = s.soc
            AND sfg.soc_group = sg.gp_soc
            AND sfg.family_type IN ('E', 'L', 'C', 'G', 'A', 'Z');

      soc_fam_rec            c_soc_fam%ROWTYPE;
*/
      CURSOR c_coverage_type (p_soc VARCHAR2)
      IS
         SELECT sct.coverage_type
           FROM soc_coverage_type sct
          WHERE sct.soc = p_soc;

      v_coverage_type        VARCHAR2 (10);
      v_rel_found            BOOLEAN;

   BEGIN
      v_rel_found := FALSE;
      v_service_feature_tab := service_feature_t ();
      v_socs_tab := NULL;
      getlogicaldate (v_logical_date);

      OPEN c_related_socs (pi_price_plan_code, v_logical_date);

      FETCH c_related_socs
      BULK COLLECT INTO v_socs_tab;

      IF v_socs_tab IS NOT NULL
      THEN
         v_rel_found := TRUE;
      END IF;

      CLOSE c_related_socs;

      IF (v_rel_found = FALSE)
      THEN
         RAISE optionalservicenotfound;
      END IF;

      FOR services_rec IN c_services (pi_price_plan_code,
                                      pi_equipment_type,
                                      pi_province_code,
                                      pi_account_type,
                                      pi_account_sub_type,
                                      v_logical_date
                                     )
      LOOP
         EXIT WHEN c_services%NOTFOUND;
         initservicefeatureobject (v_service_feature_ob);
         checkMandatorySOC(services_rec.soc, pda_mandatory_count, rim_mandatory_count);
         IF (pda_mandatory_count > 0)
         THEN
         	   v_service_feature_ob.pda_mandatory_ind := 'Y';
         END IF;
         IF (rim_mandatory_count > 0)
         THEN
         	   v_service_feature_ob.rim_mandatory_ind := 'Y';
         END IF;
         v_service_feature_ob.service_type := services_rec.service_type;
         v_service_feature_ob.soc := services_rec.soc;
         v_service_feature_ob.soc_description := services_rec.soc_description;
         v_service_feature_ob.soc_description_f :=
                                               services_rec.soc_description_f;
         v_service_feature_ob.minimum_no_months :=
                                               services_rec.minimum_no_months;
         v_service_feature_ob.eserve_ind := services_rec.eserve_ind;
         v_service_feature_ob.rda_ind := services_rec.rda_ind;
         v_service_feature_ob.bl_zero_chrg_suppress_ind :=
                                       services_rec.bl_zero_chrg_suppress_ind;
         v_service_feature_ob.promo := services_rec.promo;
         v_service_feature_ob.soc_status := services_rec.soc_status;
         v_service_feature_ob.for_sale_ind := services_rec.for_sale_ind;
         v_service_feature_ob.sale_eff_date := services_rec.sale_eff_date;
         v_service_feature_ob.sale_exp_date := services_rec.sale_exp_date;
         v_service_feature_ob.current_ind := services_rec.current_ind;
         v_service_feature_ob.telephony_features_inc :=
                                          services_rec.telephony_features_inc;
         v_service_feature_ob.dispatch_features_inc :=
                                                  services_rec.inc_dc_ftr_ind;
         v_service_feature_ob.wireless_web_features_inc :=
                                                 services_rec.inc_pds_ftr_ind;
         v_service_feature_ob.s_product_type := services_rec.product_type;
         v_service_feature_ob.period_set_code := services_rec.period_set_code;
         v_service_feature_ob.soc_level_code := services_rec.soc_level_code;
         v_service_feature_ob.equipment_type := services_rec.equipment_type;
		 v_service_feature_ob.coverage_type := services_rec.coverage_type;
		 v_service_feature_ob.network_type := services_rec.network_type;
 		 v_service_feature_ob.bill_cycle_treatment_cd := services_rec.bill_cycle_treatment_cd;
 		 v_service_feature_ob.brand_ind := services_rec.brand_id; --TBS
 		 v_service_feature_ob.soc_service_type := services_rec.soc_service_type;
 		 v_service_feature_ob.soc_duration_hours := services_rec.soc_duration_hours;
 		 v_service_feature_ob.rlh_ind := services_rec.rlh_ind;
 		 
         /* OPEN c_soc_rel (services_rec.soc, v_logical_date);  

         FETCH c_soc_rel
          INTO soc_rel_rec;

         IF c_soc_rel%FOUND
         THEN
            v_service_feature_ob.relation_type := soc_rel_rec.relation_type;
         END IF;

         CLOSE c_soc_rel; */
         
         FOR soc_rel_rec IN c_soc_rel (services_rec.soc, v_logical_date)
         LOOP
            EXIT WHEN c_soc_rel%NOTFOUND;

          CASE
            WHEN soc_rel_rec.relation_type = 'F'
              THEN  v_service_feature_ob.has_promotion := 'Y';
          
           WHEN soc_rel_rec.relation_type = 'M'
             THEN  v_service_feature_ob.has_bound := 'Y';
          
           WHEN soc_rel_rec.relation_type = 'W'
             THEN  v_service_feature_ob.is_bound := 'Y';
          
           WHEN soc_rel_rec.relation_type = 'S'
             THEN  v_service_feature_ob.has_seq_bound := 'Y';
          
           WHEN soc_rel_rec.relation_type = 'X'
             THEN  v_service_feature_ob.is_seq_bound := 'Y';
           END CASE;
         END LOOP;
         
/* Oct 04,2011 M.Liao: remove this becaues we populate familyType info outside of procedure         
         FOR soc_fam_rec IN c_soc_fam (services_rec.soc)
         LOOP
            EXIT WHEN c_soc_fam%NOTFOUND;

            IF soc_fam_rec.family_type = 'L'
            THEN
               v_service_feature_ob.loyalty_and_retention_service := 'Y';
            END IF;

            IF soc_fam_rec.family_type = 'E'
            THEN
               v_service_feature_ob.equiv_service_exists := 'Y';
            END IF;

            IF soc_fam_rec.family_type = 'G'
            THEN
               v_service_feature_ob.lbs_trackee := 'Y';
            END IF;

            IF soc_fam_rec.family_type = 'C'
            THEN
               v_service_feature_ob.lbs_tracker := 'Y';
            END IF;

            IF soc_fam_rec.family_type = 'A'
            THEN
               v_service_feature_ob.mandatory := 'Y';
            END IF;
                     
            IF soc_fam_rec.family_type = 'Z'
            THEN
               v_service_feature_ob.promo_validation_eligible := 'Y';
            END IF;      
         END LOOP;
*/
         IF services_rec.product_type = 'P'
         THEN
            OPEN c_coverage_type (services_rec.soc);

            FETCH c_coverage_type
             INTO v_coverage_type;

            IF c_coverage_type%FOUND
            THEN
               v_service_feature_ob.coverage_type := v_coverage_type;
            END IF;

            CLOSE c_coverage_type;
         END IF;

         getfeaturesforservice (v_service_feature_ob);
      END LOOP;

      OPEN po_services
       FOR
          SELECT *
            FROM TABLE (CAST (v_service_feature_tab AS service_feature_t));

      v_service_feature_tab.DELETE;
      v_service_feature_ob := NULL;
      v_socs_tab.DELETE;
   -- v_soc_ob  := NULL;
   EXCEPTION
      WHEN NO_DATA_FOUND
      THEN
         IF (po_services%ISOPEN)
         THEN
            CLOSE po_services;
         END IF;

         IF (c_services%ISOPEN)
         THEN
            CLOSE c_services;
         END IF;

         IF (c_soc_rel%ISOPEN)
         THEN
            CLOSE c_soc_rel;
         END IF;

/* Oct 04,2011 M.Liao: remove this becaues we populate familyType info outside of procedure         
         IF (c_soc_fam%ISOPEN)
         THEN
            CLOSE c_soc_fam;
         END IF;
*/
         IF (c_coverage_type%ISOPEN)
         THEN
            CLOSE c_coverage_type;
         END IF;

         IF (c_related_socs%ISOPEN)
         THEN
            CLOSE c_related_socs;
         END IF;

         OPEN po_services
          FOR
             SELECT NULL
               FROM DUAL
              WHERE 1 = 0;
      WHEN optionalservicenotfound
      THEN
         IF (po_services%ISOPEN)
         THEN
            CLOSE po_services;
         END IF;

         IF (c_services%ISOPEN)
         THEN
            CLOSE c_services;
         END IF;

         IF (c_soc_rel%ISOPEN)
         THEN
            CLOSE c_soc_rel;
         END IF;

/* Oct 04,2011 M.Liao: remove this becaues we populate familyType info outside of procedure         
         IF (c_soc_fam%ISOPEN)
         THEN
            CLOSE c_soc_fam;
         END IF;
*/
         IF (c_coverage_type%ISOPEN)
         THEN
            CLOSE c_coverage_type;
         END IF;

         IF (c_related_socs%ISOPEN)
         THEN
            CLOSE c_related_socs;
         END IF;

         OPEN po_services
          FOR
             SELECT NULL
               FROM DUAL
              WHERE 1 = 0;
      WHEN OTHERS
      THEN
         IF (po_services%ISOPEN)
         THEN
            CLOSE po_services;
         END IF;

         IF (c_services%ISOPEN)
         THEN
            CLOSE c_services;
         END IF;

         IF (c_soc_rel%ISOPEN)
         THEN
            CLOSE c_soc_rel;
         END IF;

/* Oct 04,2011 M.Liao: remove this becaues we populate familyType info outside procedure         
         IF (c_soc_fam%ISOPEN)
         THEN
            CLOSE c_soc_fam;
         END IF;
*/
         IF (c_coverage_type%ISOPEN)
         THEN
            CLOSE c_coverage_type;
         END IF;

         IF (c_related_socs%ISOPEN)
         THEN
            CLOSE c_related_socs;
         END IF;

         raise_application_error
                          (-20102,
                              'Get Optional Services  Query Failed. Oracle:(['
                           || SQLCODE
                           || '] ['
                           || SQLERRM
                           || '])');
   END;

-------------------------------------------------------------------
   PROCEDURE getfeaturesforservice (pi_service_feature_ob IN service_feature_o)
   IS
      CURSOR c_features (service_code VARCHAR2)
      IS
         SELECT DISTINCT f.feature_code, f.feature_desc, f.feature_desc_f,
                         f.dup_ftr_allow_ind, f.csm_param_req_ind,
                         rf.rc_freq_of_pym, NVL (rc.rate, 0)
                                                            recurring_charge,
                         NVL (uc.rate, 0) usage_charge, fcr.category_code,
                         f.msisdn_ind,
                         NVL (f.ftr_service_type, ' ') AS ftr_service_type,
                         f.switch_code, brc.soc AS brc_soc, f.product_type,
                         urf.mpc_ind,
                         urf.calling_circle_size,
						 f.feature_type,
						 f.pool_group_id,
						 f.def_sw_params
                    FROM rated_feature rf,
                         feature f,
                         pp_rc_rate rc,
                         batch_pp_rc_rate brc,
                         pp_uc_rate uc,
                         uc_rated_feature urf,
                         feature_category_relation fcr
                   WHERE rf.soc = service_code
                     AND f.feature_code = rf.feature_code
                     AND f.feature_group = 'SF'
                     AND rc.soc(+) = rf.soc
                     AND rc.effective_date(+) = rf.effective_date
                     AND rc.feature_code(+) = rf.feature_code
                     AND uc.soc(+) = rf.soc
                     AND uc.effective_date(+) = rf.effective_date
                     AND uc.feature_code(+) = rf.feature_code
                     AND uc.rate_version_num(+) = 0
                     AND fcr.feature_code(+) = rf.feature_code
                     AND rc.soc = brc.soc(+)
                     AND rc.feature_code = brc.feature_code(+)
                     AND urf.soc(+) = rf.soc
                     AND urf.effective_date(+) = rf.effective_date
                     AND urf.feature_code(+) = rf.feature_code
                     AND (   urf.action IS NULL OR
                     					urf.action =
                                (SELECT MIN (action)
                                   FROM uc_rated_feature urf1
                                  WHERE urf1.soc(+) = urf.soc
                                    AND urf1.effective_date(+) =
                                                            urf.effective_date
                                    AND urf1.feature_code(+) =
                                                              urf.feature_code)
                         )
                ORDER BY 1;

      features_rec           c_features%ROWTYPE;
      v_service_feature_ob   service_feature_o;
   BEGIN
      v_service_feature_ob := pi_service_feature_ob;

      FOR features_rec IN c_features (pi_service_feature_ob.soc)
      LOOP
         IF c_features%NOTFOUND
         THEN
            v_service_feature_tab.EXTEND;
            v_service_feature_tab (v_service_feature_tab.LAST) :=
                                                         v_service_feature_ob;
            EXIT;
         END IF;

         v_service_feature_ob.feature_code := features_rec.feature_code;
         v_service_feature_ob.feature_desc := features_rec.feature_desc;
         v_service_feature_ob.feature_desc_f := features_rec.feature_desc_f;
         v_service_feature_ob.dup_ftr_allow_ind :=
                                                features_rec.dup_ftr_allow_ind;
         v_service_feature_ob.csm_param_req_ind :=
                                                features_rec.csm_param_req_ind;
         v_service_feature_ob.rc_freq_of_pym := features_rec.rc_freq_of_pym;
         v_service_feature_ob.rc_rate := features_rec.recurring_charge;
         v_service_feature_ob.uc_rate := features_rec.usage_charge;
         v_service_feature_ob.category_code := features_rec.category_code;
         v_service_feature_ob.msisdn_ind := features_rec.msisdn_ind;
         v_service_feature_ob.ftr_service_type :=
                                                 features_rec.ftr_service_type;
         v_service_feature_ob.switch_code := features_rec.switch_code;
         v_service_feature_ob.f_product_type := features_rec.product_type;
         v_service_feature_ob.brc_soc := features_rec.brc_soc;
         v_service_feature_ob.mpc_ind := features_rec.mpc_ind;
         v_service_feature_ob.calling_circle_size := features_rec.calling_circle_size;
		 v_service_feature_ob.feature_type := features_rec.feature_type;
		 v_service_feature_ob.pool_group_id := features_rec.pool_group_id;
	     v_service_feature_ob.def_sw_params := features_rec.def_sw_params;
         v_service_feature_tab.EXTEND;
         v_service_feature_tab (v_service_feature_tab.LAST) :=
                                                          v_service_feature_ob;
      END LOOP;
   EXCEPTION
      WHEN NO_DATA_FOUND
      THEN
         IF (c_features%ISOPEN)
         THEN
            CLOSE c_features;
         END IF;
      WHEN OTHERS
      THEN
         IF (c_features%ISOPEN)
         THEN
            CLOSE c_features;
         END IF;

         RAISE;
         raise_application_error
                       (-20102,
                           'Get Features for Service  Query Failed. Oracle:(['
                        || SQLCODE
                        || '] ['
                        || SQLERRM
                        || '])');
   END getfeaturesforservice;

------------------------------------------------------------------------------------------------------------
   PROCEDURE getincludedpromotions (
      pi_price_plan_code   IN       VARCHAR2,
      pi_equipment_type    IN       VARCHAR2,
      pi_network_type      IN       VARCHAR2,
      pi_province_code     IN       VARCHAR2,
      pi_term              IN       NUMBER,
      po_promotions        OUT      refcursor
   )
   IS
      v_service_feature_ob   service_feature_o;
      v_logical_date         DATE;
      v_rel_found            BOOLEAN;
      v_socs_tab             t_socs;
      rim_mandatory_count    NUMBER;
      pda_mandatory_count    NUMBER;

      CURSOR c_pp_promo (p_price_plan_code VARCHAR2, v_logical_date DATE)
      IS
         SELECT s.soc
           FROM soc_relation sr, soc_group sg_s, soc_group sg_p, soc s
          WHERE sg_s.soc = s.soc
            AND sg_p.soc = p_price_plan_code
            AND sr.soc_src = sg_s.gp_soc
            AND sr.soc_dest = sg_p.gp_soc
            AND TRUNC (sr.src_effective_date) <= TRUNC (v_logical_date)
            AND TRUNC (sr.dest_effective_date) <= TRUNC (v_logical_date)
            AND (   sr.expiration_date IS NULL OR
            				TRUNC (sr.expiration_date) > TRUNC (v_logical_date)
                )
            AND sr.relation_type = 'G'
            AND s.soc_status = 'A'
            AND s.for_sale_ind = 'Y'
            AND s.current_ind = 'Y';

      CURSOR c_services (
         pi_price_plan_code   VARCHAR2,
         pi_equipment_type    VARCHAR2,
         pi_province_code     VARCHAR2,
         pi_term              NUMBER,
         v_logical_date       DATE
      )
      IS
         SELECT DISTINCT s.service_type, s.soc, s.soc_description,
                         s.soc_description_f,
                         NVL (s.minimum_no_months, 0) AS minimum_no_months,
                         s.bl_zero_chrg_suppress_ind, ser.equipment_type, ser.network_type, 
                         NVL (s.eserve_ind, 'N') AS eserve_ind,
                         NVL (s.rda_ind, 'N') rda_ind,
                         DECODE (uspt.soc,
                                 NULL, 'N',
                                 'Y'
                                ) promo, uspt.DURATION,
                         uspt.client_available_ind, uspt.dealer_available_ind,
                         uspt.act_avail_ind, uspt.chng_avail_ind,
                         uspt.included_promo_ind, s.soc_status,
                         s.for_sale_ind, s.sale_eff_date, s.sale_exp_date,
                         s.current_ind,
                         DECODE (s.product_type,
                                 'C', 'Y',
                                 s.inc_cel_ftr_ind
                                ) AS telephony_features_inc,
                         s.inc_dc_ftr_ind, s.inc_pds_ftr_ind, s.product_type,
                         s.period_set_code, s.soc_level_code, s.coverage_type,
                         s.bill_cycle_treatment_cd,
                         NVL (s.brand_id, 1) brand_id, --TBS change
                         s.soc_service_type,
                         s.soc_duration_hours,
                         s.rlh_ind
                    FROM soc_group s_gp_p,
                         priceplan_soc_groups psg,
                         soc_group s_gp_s,
                         soc s,
                         soc ps,
                         soc_submkt_relation ssr,
                         market m,
                         soc_equip_relation ser,
                         uc_soc_promo_terms uspt
                   WHERE s_gp_p.soc = pi_price_plan_code
                     AND psg.price_plan_group = s_gp_p.gp_soc
                     AND s_gp_s.gp_soc = psg.soc_group
                     AND s.soc = s_gp_s.soc
                     AND s.soc_status = 'A'
                     AND s.for_sale_ind = 'Y'
                     AND s.current_ind = 'Y'
                     AND s.product_type = ps.product_type
                     AND TRUNC (s.sale_eff_date) <= TRUNC (v_logical_date)
                     AND (   s.sale_exp_date IS NULL OR
                     					TRUNC (s.sale_exp_date) > TRUNC (v_logical_date)
                         )
                     AND ps.soc = pi_price_plan_code
                     AND m.province = pi_province_code
                     AND (   ssr.sub_market = m.market_code
                          OR ssr.sub_market = 'ALL'
                         )
                     AND ssr.soc = s.soc
                     AND ser.soc = s.soc
                     AND ser.effective_date = s.effective_date
                     AND (   ser.equipment_type = pi_equipment_type
                          OR ser.equipment_type = '9'
                         )
										 AND (pi_network_type = '9' OR
                     		  ser.network_type = pi_network_type OR 
                     		  ser.network_type='9'
                         )
                     AND uspt.soc = s.soc
                     AND uspt.minimum_no_months = pi_term
                     AND s.soc IN (SELECT *
                                     FROM TABLE (CAST (v_socs_tab AS t_socs)))
         UNION
         SELECT DISTINCT s.service_type, s.soc, s.soc_description,
                         s.soc_description_f,
                         NVL (s.minimum_no_months, 0) AS minimum_no_months,
                         s.bl_zero_chrg_suppress_ind, ser.equipment_type, ser.network_type, 
                         NVL (s.eserve_ind, 'N') eserve_ind,
                         NVL (s.rda_ind, 'N') rda_ind,
                         DECODE (uspt.soc,
                                 NULL, 'N',
                                 'Y'
                                ) promo, uspt.DURATION,
                         uspt.client_available_ind, uspt.dealer_available_ind,
                         uspt.act_avail_ind, uspt.chng_avail_ind,
                         uspt.included_promo_ind, s.soc_status,
                         s.for_sale_ind, s.sale_eff_date, s.sale_exp_date,
                         s.current_ind,
                         DECODE (s.product_type,
                                 'C', 'Y',
                                 s.inc_cel_ftr_ind
                                ) AS telephony_features_inc,
                         s.inc_dc_ftr_ind, s.inc_pds_ftr_ind, s.product_type,
                         s.period_set_code, s.soc_level_code, s.coverage_type,
                         s.bill_cycle_treatment_cd,
                         s.brand_id, --TBS change
                         s.soc_service_type,
                         s.soc_duration_hours,
                         s.rlh_ind
                    FROM soc ps,
                         soc s,
                         soc_submkt_relation ssr,
                         market m,
                         soc_equip_relation ser,
                         uc_soc_promo_terms uspt
                   WHERE ps.soc = pi_price_plan_code
                     AND uspt.for_all_price_plans_ind = 'Y'
                     AND (   uspt.minimum_no_months = pi_term
                          OR uspt.minimum_no_months = 99
                         )
                     AND s.soc = uspt.soc
                     AND s.soc_status = 'A'
                     AND s.for_sale_ind = 'Y'
                     AND s.current_ind = 'Y'
                     AND s.product_type = ps.product_type
                     AND TRUNC (s.sale_eff_date) <= TRUNC (v_logical_date)
                     AND (   s.sale_exp_date IS NULL OR
                     					TRUNC (s.sale_exp_date) > TRUNC (v_logical_date)
                         )
                     AND m.province = pi_province_code
                     AND (   ssr.sub_market = m.market_code
                          OR ssr.sub_market = 'ALL'
                         )
                     AND ssr.soc = s.soc
                     AND ser.soc = s.soc
                     AND ser.effective_date = s.effective_date
                     AND (   ser.equipment_type = pi_equipment_type
                          OR ser.equipment_type = '9'
                         )
										 AND (pi_network_type = '9' OR
                     		  ser.network_type = pi_network_type OR 
                     		  ser.network_type='9'
                     	    )
                     AND s.soc IN (SELECT *
                                     FROM TABLE (CAST (v_socs_tab AS t_socs)))
                ORDER BY 2;

      services_rec           c_services%ROWTYPE;

      CURSOR c_soc_rel (p_soc VARCHAR2, v_logical_date DATE)
      IS
         SELECT soc_src, soc_dest,
                DECODE (relation_type,
                        'M', 'W',
                        'S', 'X',
                        NULL
                       ) AS relation_type
           FROM soc_relation_ext sr
          WHERE soc_dest = p_soc AND relation_type IN ('M', 'S')
          AND (   sr.expiration_date IS NULL OR
          				TRUNC (sr.expiration_date) > TRUNC (v_logical_date)
                         )
         UNION
         SELECT soc_src, soc_dest, relation_type
           FROM soc_relation sr
          WHERE soc_dest = p_soc AND relation_type = 'F'
         UNION
         SELECT soc_dest, soc_src, relation_type
           FROM soc_relation_ext sr
          WHERE soc_src = p_soc AND relation_type IN ('M', 'S')
          AND (   sr.expiration_date IS NULL OR
          				TRUNC (sr.expiration_date) > TRUNC (v_logical_date)
                         );

      soc_rel_rec            c_soc_rel%ROWTYPE;

/* Oct 04,2011 M.Liao: remove this becaues we populate familyType info outside of procedure         
      CURSOR c_soc_fam (p_soc VARCHAR2)
      IS
         SELECT sfg.family_type
           FROM soc s, soc_group sg, soc_family_group sfg
          WHERE s.soc = p_soc
            AND sg.soc = s.soc
            AND sfg.soc_group = sg.gp_soc
            AND sfg.family_type IN ('E', 'C', 'G', 'Z');

      soc_fam_rec            c_soc_fam%ROWTYPE;
*/
      CURSOR c_coverage_type (p_soc VARCHAR2)
      IS
         SELECT sct.coverage_type
           FROM soc_coverage_type sct
          WHERE sct.soc = p_soc;

      v_coverage_type        VARCHAR2 (10);
   BEGIN
      v_rel_found := FALSE;
      v_service_feature_tab := service_feature_t ();
      v_socs_tab := NULL;
      getlogicaldate (v_logical_date);

      OPEN c_pp_promo (pi_price_plan_code, v_logical_date);

      FETCH c_pp_promo
      BULK COLLECT INTO v_socs_tab;

      IF v_socs_tab IS NOT NULL
      THEN
         v_rel_found := TRUE;
      END IF;

      CLOSE c_pp_promo;

      IF (v_rel_found = FALSE)
      THEN
         RAISE includedpromotionsnotfound;
      END IF;

      FOR services_rec IN c_services (pi_price_plan_code,
                                      pi_equipment_type,
                                      pi_province_code,
                                      pi_term,
                                      v_logical_date
                                     )
      LOOP
         EXIT WHEN c_services%NOTFOUND;
         initservicefeatureobject (v_service_feature_ob);
         
         checkMandatorySOC(services_rec.soc, pda_mandatory_count, rim_mandatory_count);
         IF (pda_mandatory_count > 0)
         THEN
         	   v_service_feature_ob.pda_mandatory_ind := 'Y';
         END IF;
         IF (rim_mandatory_count > 0)
         THEN
         	   v_service_feature_ob.rim_mandatory_ind := 'Y';
         END IF;
         
         v_service_feature_ob.service_type := services_rec.service_type;
         v_service_feature_ob.soc := services_rec.soc;
         v_service_feature_ob.soc_description := services_rec.soc_description;
         v_service_feature_ob.soc_description_f :=
                                               services_rec.soc_description_f;
         v_service_feature_ob.minimum_no_months :=
                                               services_rec.minimum_no_months;
         v_service_feature_ob.eserve_ind := services_rec.eserve_ind;
         v_service_feature_ob.rda_ind := services_rec.rda_ind;
         v_service_feature_ob.bl_zero_chrg_suppress_ind :=
                                       services_rec.bl_zero_chrg_suppress_ind;
         v_service_feature_ob.promo := services_rec.promo;
         v_service_feature_ob.included_promo_ind :=
                                              services_rec.included_promo_ind;
         v_service_feature_ob.soc_status := services_rec.soc_status;
         v_service_feature_ob.for_sale_ind := services_rec.for_sale_ind;
         v_service_feature_ob.sale_eff_date := services_rec.sale_eff_date;
         v_service_feature_ob.sale_exp_date := services_rec.sale_exp_date;
         v_service_feature_ob.current_ind := services_rec.current_ind;
         v_service_feature_ob.telephony_features_inc :=
                                          services_rec.telephony_features_inc;
         v_service_feature_ob.dispatch_features_inc :=
                                                  services_rec.inc_dc_ftr_ind;
         v_service_feature_ob.wireless_web_features_inc :=
                                                 services_rec.inc_pds_ftr_ind;
         v_service_feature_ob.s_product_type := services_rec.product_type;
         v_service_feature_ob.period_set_code := services_rec.period_set_code;
         v_service_feature_ob.soc_level_code := services_rec.soc_level_code;
         v_service_feature_ob.loyalty_and_retention_service := 'N';
         v_service_feature_ob.client_available_ind :=
                                            services_rec.client_available_ind;
         v_service_feature_ob.dealer_available_ind :=
                                            services_rec.dealer_available_ind;
         v_service_feature_ob.DURATION := services_rec.DURATION;
         v_service_feature_ob.equipment_type := services_rec.equipment_type;
 		 v_service_feature_ob.coverage_type := services_rec.coverage_type;
 		     v_service_feature_ob.network_type := services_rec.network_type;
 		 v_service_feature_ob.bill_cycle_treatment_cd := services_rec.bill_cycle_treatment_cd;    
 		 v_service_feature_ob.brand_ind := services_rec.brand_id; --TBS
 		 v_service_feature_ob.soc_service_type := services_rec.soc_service_type;
 		 v_service_feature_ob.soc_duration_hours := services_rec.soc_duration_hours;
 		 v_service_feature_ob.rlh_ind := services_rec.rlh_ind;

        /* OPEN c_soc_rel (services_rec.soc, v_logical_date);

         FETCH c_soc_rel
          INTO soc_rel_rec;

         IF c_soc_rel%FOUND
         THEN
            v_service_feature_ob.relation_type := soc_rel_rec.relation_type;
         END IF;

         CLOSE c_soc_rel;*/
         
         FOR soc_rel_rec IN c_soc_rel (services_rec.soc, v_logical_date)
         LOOP
            EXIT WHEN c_soc_rel%NOTFOUND;

          CASE
            WHEN soc_rel_rec.relation_type = 'F'
              THEN  v_service_feature_ob.has_promotion := 'Y';
          
           WHEN soc_rel_rec.relation_type = 'M'
             THEN  v_service_feature_ob.has_bound := 'Y';
          
           WHEN soc_rel_rec.relation_type = 'W'
             THEN  v_service_feature_ob.is_bound := 'Y';
          
           WHEN soc_rel_rec.relation_type = 'S'
             THEN  v_service_feature_ob.has_seq_bound := 'Y';
          
           WHEN soc_rel_rec.relation_type = 'X'
             THEN  v_service_feature_ob.is_seq_bound := 'Y';
           END CASE;
         END LOOP;

/* Oct 04,2011 M.Liao: remove this becaues we populate familyType info outside of procedure         
         FOR soc_fam_rec IN c_soc_fam (services_rec.soc)
         LOOP
            EXIT WHEN c_soc_fam%NOTFOUND;

            IF soc_fam_rec.family_type = 'L'
            THEN
               v_service_feature_ob.loyalty_and_retention_service := 'Y';
            END IF;

            IF soc_fam_rec.family_type = 'E'
            THEN
               v_service_feature_ob.equiv_service_exists := 'Y';
            END IF;

            IF soc_fam_rec.family_type = 'G'
            THEN
               v_service_feature_ob.lbs_trackee := 'Y';
            END IF;

            IF soc_fam_rec.family_type = 'C'
            THEN
               v_service_feature_ob.lbs_tracker := 'Y';
            END IF;
            
            IF soc_fam_rec.family_type = 'Z'
            THEN
               v_service_feature_ob.promo_validation_eligible := 'Y';
            END IF;
         END LOOP;
*/
         IF services_rec.product_type = 'P'
         THEN
            OPEN c_coverage_type (services_rec.soc);

            FETCH c_coverage_type
             INTO v_coverage_type;

            IF c_coverage_type%FOUND
            THEN
               v_service_feature_ob.coverage_type := v_coverage_type;
            END IF;

            CLOSE c_coverage_type;
         END IF;

         getfeaturesforservice (v_service_feature_ob);
      END LOOP;

      OPEN po_promotions
       FOR
          SELECT *
            FROM TABLE (CAST (v_service_feature_tab AS service_feature_t));

      v_service_feature_tab.DELETE;
      v_service_feature_ob := NULL;
      v_socs_tab.DELETE;
   EXCEPTION
      WHEN NO_DATA_FOUND OR includedpromotionsnotfound
      THEN
         IF (po_promotions%ISOPEN)
         THEN
            CLOSE po_promotions;
         END IF;

         IF (c_services%ISOPEN)
         THEN
            CLOSE c_services;
         END IF;

         IF (c_soc_rel%ISOPEN)
         THEN
            CLOSE c_soc_rel;
         END IF;

/* Oct 04,2011 M.Liao: remove this becaues we populate familyType info outside of procedure         
         IF (c_soc_fam%ISOPEN)
         THEN
            CLOSE c_soc_fam;
         END IF;
*/
         IF (c_coverage_type%ISOPEN)
         THEN
            CLOSE c_coverage_type;
         END IF;

         IF (c_pp_promo%ISOPEN)
         THEN
            CLOSE c_pp_promo;
         END IF;

         OPEN po_promotions
          FOR
             SELECT NULL
               FROM DUAL
              WHERE 1 = 0;
      -- raise_application_error (-20102, 'No included promotions were found');
      WHEN OTHERS
      THEN
         IF (po_promotions%ISOPEN)
         THEN
            CLOSE po_promotions;
         END IF;

         IF (c_services%ISOPEN)
         THEN
            CLOSE c_services;
         END IF;

         IF (c_soc_rel%ISOPEN)
         THEN
            CLOSE c_soc_rel;
         END IF;

/* Oct 04,2011 M.Liao: remove this becaues we populate familyType info outside of procedure         
         IF (c_soc_fam%ISOPEN)
         THEN
            CLOSE c_soc_fam;
         END IF;
*/
         IF (c_coverage_type%ISOPEN)
         THEN
            CLOSE c_coverage_type;
         END IF;

         IF (c_pp_promo%ISOPEN)
         THEN
            CLOSE c_pp_promo;
         END IF;

         raise_application_error
                        (-20102,
                            'Get Included promotions  Query Failed. Oracle:(['
                         || SQLCODE
                         || '] ['
                         || SQLERRM
                         || '])');
   END getincludedpromotions;

-----------------------------------------------------------------------------------------------
   PROCEDURE initservicefeatureobject (
      pi_service_feature_ob   IN OUT   service_feature_o
   )
   IS
   BEGIN
      pi_service_feature_ob := service_feature_o();
   EXCEPTION
      WHEN OTHERS
      THEN
         raise_application_error (-20102,
                                     'Init Service Feature Object. Oracle:(['
                                  || SQLCODE
                                  || '] ['
                                  || SQLERRM
                                  || '])');
   END initservicefeatureobject;

-----------------------------------------------------------------------------------------
   PROCEDURE getincludedservices (
      pi_price_plan_code   IN       VARCHAR2,
      po_incl_services     OUT      refcursor
   )
   IS
      v_service_feature_ob   service_feature_o;
      v_logical_date         DATE;
      pda_mandatory_count    NUMBER;
      rim_mandatory_count    NUMBER;

      CURSOR c_services (pi_price_plan_code VARCHAR2, v_logical_date DATE)
      IS
         SELECT DISTINCT s.service_type, s.soc, s.soc_description,
                         s.soc_description_f,
                         NVL (s.minimum_no_months, 0) minimum_no_months,
                         s.rda_ind, s.bl_zero_chrg_suppress_ind,
                         DECODE (uspt.soc,
                                 NULL, 'N',
                                 'Y'
                                ) promo, uspt.DURATION, s.soc_status,
                         s.for_sale_ind, s.sale_eff_date, s.sale_exp_date,
                         s.current_ind,
                         DECODE (s.product_type,
                                 'C', 'Y',
                                 s.inc_cel_ftr_ind
                                ) telephony_features_inc,
                         s.inc_dc_ftr_ind, s.inc_pds_ftr_ind, s.product_type,
                         NVL (s.min_commit_amt, 0) min_commit_amt,
                         s.soc_level_code, 'I' service_type_ind,
                         s.period_set_code,
                         NVL (s.soc_def_user_seg,
                              'PERSONAL  ') soc_def_user_seg,
                         s.min_req_usg_chrg, s.coverage_type, s.bill_cycle_treatment_cd,
                         NVL (s.brand_id, 1) brand_id, --TBS change
                         s.soc_service_type,
                         s.soc_duration_hours,
                         s.rlh_ind
                    FROM soc p,
                         soc s,
                         soc_relation sr,
                         --logical_date ld,
                         uc_soc_promo_terms uspt
                   WHERE p.soc = pi_price_plan_code
                     AND sr.soc_src = p.soc
                     AND s.soc = sr.soc_dest
                     AND sr.relation_type = 'O'
                     AND s.soc_status = 'A'
                     AND s.for_sale_ind = 'Y'
                     AND TRUNC (s.sale_eff_date) <= TRUNC (v_logical_date)
                     AND (   s.sale_exp_date IS NULL OR 
                     				TRUNC (s.sale_exp_date) > TRUNC (v_logical_date)
                         )
                    -- AND ld.logical_date_type = 'O'
                     AND uspt.soc(+) = s.soc
                     AND (   uspt.minimum_no_months IS NULL OR
                     				uspt.minimum_no_months = p.minimum_no_months
                         )
                ORDER BY 2;

      services_rec           c_services%ROWTYPE;

      CURSOR c_soc_rel (p_soc VARCHAR2, v_logical_date DATE)
      IS
         SELECT soc_src, soc_dest,
                DECODE (relation_type,
                        'M', 'W',
                        'S', 'X',
                        NULL
                       ) AS relation_type
           FROM soc_relation_ext sr
          WHERE soc_dest = p_soc AND relation_type IN ('M', 'S')
         UNION
         SELECT soc_src, soc_dest, relation_type
           FROM soc_relation sr
          WHERE soc_dest = p_soc AND relation_type = 'F'
           AND (   sr.expiration_date IS NULL OR
           				TRUNC (sr.expiration_date) > TRUNC (v_logical_date)
                         )
         UNION
         SELECT soc_dest, soc_src, relation_type
           FROM soc_relation_ext sr
          WHERE soc_src = p_soc AND relation_type IN ('M', 'S')
           AND (   sr.expiration_date IS NULL OR 
           					TRUNC (sr.expiration_date) > TRUNC (v_logical_date)
                         );

      soc_rel_rec            c_soc_rel%ROWTYPE;
      
   BEGIN
      v_service_feature_tab := service_feature_t ();
      getlogicaldate (v_logical_date);

      FOR services_rec IN c_services (pi_price_plan_code, v_logical_date)
      LOOP
         EXIT WHEN c_services%NOTFOUND;
         initservicefeatureobject (v_service_feature_ob);
         checkMandatorySOC(services_rec.soc, pda_mandatory_count, rim_mandatory_count);
         IF (pda_mandatory_count > 0)
         THEN
         	   v_service_feature_ob.pda_mandatory_ind := 'Y';
         END IF;
         IF (rim_mandatory_count > 0)
         THEN
         	   v_service_feature_ob.rim_mandatory_ind := 'Y';
         END IF;
         v_service_feature_ob.service_type := services_rec.service_type;
         v_service_feature_ob.soc := services_rec.soc;
         v_service_feature_ob.soc_description := services_rec.soc_description;
         v_service_feature_ob.soc_description_f := services_rec.soc_description_f;
         v_service_feature_ob.minimum_no_months := services_rec.minimum_no_months;
         v_service_feature_ob.rda_ind := services_rec.rda_ind;
         v_service_feature_ob.bl_zero_chrg_suppress_ind := services_rec.bl_zero_chrg_suppress_ind;
         v_service_feature_ob.promo := services_rec.promo;
         v_service_feature_ob.soc_status := services_rec.soc_status;
         v_service_feature_ob.for_sale_ind := services_rec.for_sale_ind;
         v_service_feature_ob.sale_eff_date := services_rec.sale_eff_date;
         v_service_feature_ob.sale_exp_date := services_rec.sale_exp_date;
         v_service_feature_ob.current_ind := services_rec.current_ind;
         v_service_feature_ob.telephony_features_inc := services_rec.telephony_features_inc;
         v_service_feature_ob.dispatch_features_inc := services_rec.inc_dc_ftr_ind;
         v_service_feature_ob.wireless_web_features_inc := services_rec.inc_pds_ftr_ind;
         v_service_feature_ob.s_product_type := services_rec.product_type;
         v_service_feature_ob.period_set_code := services_rec.period_set_code;
         v_service_feature_ob.soc_level_code := services_rec.soc_level_code;
         v_service_feature_ob.loyalty_and_retention_service := 'N';
         v_service_feature_ob.service_type_ind := services_rec.service_type_ind;
         v_service_feature_ob.DURATION := services_rec.DURATION;
		 v_service_feature_ob.coverage_type := services_rec.coverage_type;
		 v_service_feature_ob.bill_cycle_treatment_cd := services_rec.bill_cycle_treatment_cd; 		 
 		 v_service_feature_ob.brand_ind := services_rec.brand_id; --TBS
			   --v_service_feature_ob.network_type := getNetworkTypeBySOC(pi_price_plan_code);	   
 		 v_service_feature_ob.soc_service_type := services_rec.soc_service_type;
 		 v_service_feature_ob.soc_duration_hours := services_rec.soc_duration_hours;
 		 v_service_feature_ob.rlh_ind := services_rec.rlh_ind;

        /* OPEN c_soc_rel (services_rec.soc, v_logical_date);

         FETCH c_soc_rel
          INTO soc_rel_rec;

         IF c_soc_rel%FOUND
         THEN
            v_service_feature_ob.relation_type := soc_rel_rec.relation_type;
         END IF;

         CLOSE c_soc_rel; */
         
         FOR soc_rel_rec IN c_soc_rel (services_rec.soc, v_logical_date)
         LOOP
            EXIT WHEN c_soc_rel%NOTFOUND;

          CASE
            WHEN soc_rel_rec.relation_type = 'F'
              THEN  v_service_feature_ob.has_promotion := 'Y';
          
           WHEN soc_rel_rec.relation_type = 'M'
             THEN  v_service_feature_ob.has_bound := 'Y';
          
           WHEN soc_rel_rec.relation_type = 'W'
             THEN  v_service_feature_ob.is_bound := 'Y';
          
           WHEN soc_rel_rec.relation_type = 'S'
             THEN  v_service_feature_ob.has_seq_bound := 'Y';
          
           WHEN soc_rel_rec.relation_type = 'X'
             THEN  v_service_feature_ob.is_seq_bound := 'Y';
           END CASE;
         END LOOP;

         getfeaturesforservice (v_service_feature_ob);
      END LOOP;

      OPEN po_incl_services
       FOR
          SELECT *
            FROM TABLE (CAST (v_service_feature_tab AS service_feature_t));

      v_service_feature_tab.DELETE;
      v_service_feature_ob := NULL;
   EXCEPTION
      WHEN NO_DATA_FOUND
      THEN
         IF (po_incl_services%ISOPEN)
         THEN
            CLOSE po_incl_services;
         END IF;

         IF (c_services%ISOPEN)
         THEN
            CLOSE c_services;
         END IF;

         IF (c_soc_rel%ISOPEN)
         THEN
            CLOSE c_soc_rel;
         END IF;

         OPEN po_incl_services
          FOR
             SELECT NULL
               FROM DUAL
              WHERE 1 = 0;
      -- raise_application_error (-20102, 'No included services were found');
      WHEN OTHERS
      THEN
         IF (po_incl_services%ISOPEN)
         THEN
            CLOSE po_incl_services;
         END IF;

         IF (c_services%ISOPEN)
         THEN
            CLOSE c_services;
         END IF;

         IF (c_soc_rel%ISOPEN)
         THEN
            CLOSE c_soc_rel;
         END IF;

         raise_application_error
                          (-20102,
                              'Get Included services  Query Failed. Oracle:(['
                           || SQLCODE
                           || '] ['
                           || SQLERRM
                           || '])');
   END getincludedservices;

-------------------------------------------------------------------
   PROCEDURE getfeaturesforpriceplan (
      pi_service_feature_ob   IN   service_feature_o
   )
   IS
      CURSOR c_features (service_code VARCHAR2)
      IS
         SELECT DISTINCT f.feature_code, f.feature_desc, f.feature_desc_f,
                         f.dup_ftr_allow_ind, f.csm_param_req_ind,
                         rf.rc_freq_of_pym, NVL (rc.rate, 0)
                                                            recurring_charge,
                         DECODE (uc.rate,
                                 uc.rate, 0
                                ) usage_charge, fcr.category_code,
                         f.msisdn_ind,
                         NVL (f.ftr_service_type, ' ') AS ftr_service_type,
                         f.switch_code, f.product_type,
                         urf.uc_rounding_factor, urf.mpc_ind,
                         urf.calling_circle_size,
						 f.feature_type,
						 f.pool_group_id, 
						 f.def_sw_params
                    FROM rated_feature rf,
                         feature f,
                         pp_rc_rate rc,
                         uc_rated_feature urf,
                         pp_uc_rate uc,
                         feature_category_relation fcr
                   WHERE rf.soc = service_code
                     AND f.feature_code = rf.feature_code
                     AND f.feature_group = 'SF'
                     AND fcr.feature_code(+) = rf.feature_code
                     AND rc.soc(+) = rf.soc
                     AND rc.effective_date(+) = rf.effective_date
                     AND rc.feature_code(+) = rf.feature_code
                     AND uc.soc(+) = rf.soc
                     AND uc.effective_date(+) = rf.effective_date
                     AND uc.feature_code(+) = rf.feature_code
                     AND uc.rate_version_num(+) = 0
                     AND urf.soc(+) = rf.soc
                     AND urf.effective_date(+) = rf.effective_date
                     AND urf.feature_code(+) = rf.feature_code
                     AND (   urf.action IS NULL OR
                     				 urf.action =
                                (SELECT MIN (action)
                                   FROM uc_rated_feature urf1
                                  WHERE urf1.soc(+) = urf.soc
                                    AND urf1.effective_date(+) =
                                                            urf.effective_date
                                    AND urf1.feature_code(+) =
                                                              urf.feature_code)
                         )
                ORDER BY 1;

      features_rec           c_features%ROWTYPE;
      v_service_feature_ob   service_feature_o;
   BEGIN
      v_service_feature_ob := pi_service_feature_ob;

      FOR features_rec IN c_features (pi_service_feature_ob.soc)
      LOOP
         IF c_features%NOTFOUND
         THEN
            v_service_feature_tab.EXTEND;
            v_service_feature_tab (v_service_feature_tab.LAST) :=
                                                         v_service_feature_ob;
            EXIT;
         END IF;

         v_service_feature_ob.feature_code := features_rec.feature_code;
         v_service_feature_ob.feature_desc := features_rec.feature_desc;
         v_service_feature_ob.feature_desc_f := features_rec.feature_desc_f;
         v_service_feature_ob.dup_ftr_allow_ind :=
                                                features_rec.dup_ftr_allow_ind;
         v_service_feature_ob.csm_param_req_ind :=
                                                features_rec.csm_param_req_ind;
         v_service_feature_ob.rc_freq_of_pym := features_rec.rc_freq_of_pym;
         v_service_feature_ob.rc_rate := features_rec.recurring_charge;
         v_service_feature_ob.uc_rate := features_rec.usage_charge;
         v_service_feature_ob.category_code := features_rec.category_code;
         v_service_feature_ob.msisdn_ind := features_rec.msisdn_ind;
         v_service_feature_ob.ftr_service_type :=
                                                 features_rec.ftr_service_type;
         v_service_feature_ob.switch_code := features_rec.switch_code;
         v_service_feature_ob.f_product_type := features_rec.product_type;
         v_service_feature_ob.uc_rounding_factor :=
                                               features_rec.uc_rounding_factor;
         v_service_feature_ob.mpc_ind := features_rec.mpc_ind;
         v_service_feature_ob.calling_circle_size := features_rec.calling_circle_size;
		 v_service_feature_ob.feature_type := features_rec.feature_type;
		 v_service_feature_ob.pool_group_id := features_rec.pool_group_id;
		 v_service_feature_ob.def_sw_params := features_rec.def_sw_params;
         v_service_feature_tab.EXTEND;
         v_service_feature_tab (v_service_feature_tab.LAST) :=
                                                          v_service_feature_ob;
      END LOOP;
   EXCEPTION
      WHEN NO_DATA_FOUND
      THEN
         IF (c_features%ISOPEN)
         THEN
            CLOSE c_features;
         END IF;
      WHEN OTHERS
      THEN
         IF (c_features%ISOPEN)
         THEN
            CLOSE c_features;
         END IF;

         RAISE;
         raise_application_error
                    (-20102,
                        'Get Features for Price Plan  Query Failed. Oracle:(['
                     || SQLCODE
                     || '] ['
                     || SQLERRM
                     || '])');
   END getfeaturesforpriceplan;
-------------------------------------------------------------------------------------------------
   PROCEDURE getlogicaldate (po_logical_date OUT DATE)
   IS
   BEGIN
      SELECT logical_date
        INTO po_logical_date
        FROM logical_date ld
       WHERE ld.logical_date_type = 'O';
   END getlogicaldate;
   -------------------------------------------------------------------------------------------------------
   
   PROCEDURE getpriceplan (
      pi_price_plan_code   IN       VARCHAR2,
      po_price_plan        OUT      refcursor
   )
   IS
      v_service_feature_ob   service_feature_o;

      CURSOR c_interface_price_plan (pi_price_plan_code VARCHAR2)
      IS
         SELECT DISTINCT s.service_type, s.soc, s.soc_description,
                         s.soc_description_f,
                         NVL (s.minimum_no_months, 0) AS minimum_no_months,
                         s.rda_ind, s.bl_zero_chrg_suppress_ind,
                         psg.client_available_ind, psg.dealer_available_ind,
                         psg.act_avail_ind, psg.chng_avail_ind, s.soc_status,
                         s.for_sale_ind, s.sale_eff_date, s.sale_exp_date,
                         s.current_ind,
                         DECODE (s.product_type,
                                 'C', 'Y',
                                 s.inc_cel_ftr_ind) AS telephony_features_inc,
                         s.inc_dc_ftr_ind, s.inc_pds_ftr_ind, s.product_type,
                         NVL (s.min_commit_amt, 0) min_commit_amt,
                         s.soc_level_code, 'P' service_type_ind,
                         psg.client_chng_avail_ind,
                         psg.dealer_chng_avail_ind,
                         psg.client_modify_avail_ind,
                         psg.dealer_modify_avail_ind,
                         psg.corporate_renewal_ind,
                         psg.non_corporate_renewal_ind, s.period_set_code,
                         NVL (s.soc_def_user_seg,
                              'PERSONAL  ') soc_def_user_seg,
                         psg.ares_avail_ind, psg.csa_avail_ind,
                         s.min_req_usg_chrg, s.mp_ind, s.brand_id, psg.aom_avail_ind,
						 s.mci_ind, s.coverage_type, s.bill_cycle_treatment_cd,
						 s.seat_type, s.soc_service_type,
						 s.soc_duration_hours, s.rlh_ind 
                    FROM soc s,
                         soc_group sg,
                         priceplan_soc_groups psg
                    WHERE s.soc = pi_price_plan_code
                     AND sg.soc = s.soc
                     AND psg.price_plan_group = sg.gp_soc
                     AND EXISTS (
                            SELECT 1
                              FROM soc_group sg1, priceplan_soc_groups psg1
                             WHERE sg1.soc = pi_price_plan_code
                               AND psg1.price_plan_group = sg1.gp_soc);

      services_rec           c_interface_price_plan%ROWTYPE;

-------------------------------------------------------------
      CURSOR c_non_interface_price_plan (pi_price_plan_code VARCHAR2)
      IS
         SELECT DISTINCT s.service_type, s.soc, s.soc_description,
                         s.soc_description_f,
                         NVL (s.minimum_no_months, 0) minimum_no_months,
                         s.rda_ind, s.bl_zero_chrg_suppress_ind, 'N', 'N',
                         'N', 'N', s.soc_status, s.for_sale_ind,
                         s.sale_eff_date, s.sale_exp_date, s.current_ind,
                         DECODE (s.product_type,
                                 'C', 'Y',
                                 s.inc_cel_ftr_ind
                                ) telephony_features_in,
                         s.inc_dc_ftr_ind, s.inc_pds_ftr_ind, s.product_type,
                         NVL (s.min_commit_amt, 0) min_commit_amt,
                         s.soc_level_code, 'P' service_type_ind,
                         '' client_chng_avail_ind, '' dealer_chng_avail_ind,
                         '' client_modify_avail_ind,
                         '' dealer_modify_avail_ind, '' corporate_renewal_ind,
                         '' non_corporate_renewal_ind, s.period_set_code,
                         NVL (s.soc_def_user_seg,
                              'PERSONAL  ') soc_def_user_seg,
                         '' ares_avail_ind, '' csa_avail_ind,
                         s.min_req_usg_chrg, s.mp_ind, s.brand_id, '' aom_avail_ind,
						 s.mci_ind, s.coverage_type, s.bill_cycle_treatment_cd,
						 s.seat_type, s.soc_service_type,
						 s.soc_duration_hours, s.rlh_ind 
                    FROM soc s
                   WHERE s.soc = pi_price_plan_code
                     AND NOT EXISTS (
                            SELECT 1
                              FROM soc_group sg1, priceplan_soc_groups psg1
                             WHERE sg1.soc = pi_price_plan_code
                               AND psg1.price_plan_group = sg1.gp_soc);

------------------------------------------------------------------
      CURSOR c_discount_available (p_soc VARCHAR2)
      IS
         SELECT dps.soc
           FROM soc_discount_plan dps
          WHERE dps.soc = p_soc;

      v_disc_soc             soc_discount_plan.soc%TYPE;

      CURSOR c_suspension_price_plan (p_soc VARCHAR2)
      IS
         SELECT sarc.soc
           FROM soc_activity_reason_code sarc
          WHERE sarc.soc = p_soc;

      v_susp_price_soc       soc_activity_reason_code.soc%TYPE;

      CURSOR c_sharable_plan (p_soc VARCHAR2)
      IS
         SELECT spp.max_subscribers_number, spp.secondary_soc
           FROM shareable_price_plan spp, soc_group sg
          WHERE sg.soc = p_soc AND spp.price_plan_group = sg.gp_soc;

      sharable_plan_rec      c_sharable_plan%ROWTYPE;

/*
      CURSOR c_family_type (p_soc VARCHAR2)
      IS
         SELECT sfg.family_type
           FROM soc_group sg, soc_family_group sfg
          WHERE sg.soc = p_soc
            AND sfg.soc_group = sg.gp_soc
            AND sfg.family_type IN ('R', 'T', 'F', 'N', 'O', 'D', 'M');
            
      v_family_type          soc_family_group.family_type%TYPE;
*/      
      
   BEGIN
      v_service_feature_tab := service_feature_t ();
      initservicefeatureobject (v_service_feature_ob);

      OPEN c_interface_price_plan (pi_price_plan_code);

      FETCH c_interface_price_plan
       INTO services_rec;

      IF c_interface_price_plan%NOTFOUND
      THEN
         OPEN c_non_interface_price_plan (pi_price_plan_code);

         FETCH c_non_interface_price_plan
          INTO services_rec;

         IF c_non_interface_price_plan%NOTFOUND
         THEN
            RAISE priceplannotfound;
         END IF;

         CLOSE c_non_interface_price_plan;
      END IF;

      CLOSE c_interface_price_plan;

      v_service_feature_ob.service_type := services_rec.service_type;
      v_service_feature_ob.soc := services_rec.soc;
      v_service_feature_ob.soc_description := services_rec.soc_description;
      v_service_feature_ob.soc_description_f := services_rec.soc_description_f;
      v_service_feature_ob.minimum_no_months := services_rec.minimum_no_months;
      v_service_feature_ob.bl_zero_chrg_suppress_ind :=
                                        services_rec.bl_zero_chrg_suppress_ind;
      v_service_feature_ob.soc_status := services_rec.soc_status;
      v_service_feature_ob.for_sale_ind := services_rec.for_sale_ind;
      v_service_feature_ob.sale_eff_date := services_rec.sale_eff_date;
      v_service_feature_ob.sale_exp_date := services_rec.sale_exp_date;
      v_service_feature_ob.current_ind := services_rec.current_ind;
      v_service_feature_ob.telephony_features_inc :=
                                           services_rec.telephony_features_inc;
      v_service_feature_ob.dispatch_features_inc :=
                                                   services_rec.inc_dc_ftr_ind;
      v_service_feature_ob.wireless_web_features_inc :=
                                                  services_rec.inc_pds_ftr_ind;
      v_service_feature_ob.s_product_type := services_rec.product_type;
      v_service_feature_ob.soc_level_code := services_rec.soc_level_code;
      v_service_feature_ob.client_available_ind :=
                                             services_rec.client_available_ind;
      v_service_feature_ob.dealer_available_ind :=
                                             services_rec.dealer_available_ind;
      v_service_feature_ob.chng_avail_ind := services_rec.chng_avail_ind;
      v_service_feature_ob.act_avail_ind := services_rec.act_avail_ind;
      v_service_feature_ob.client_chng_avail_ind :=
                                            services_rec.client_chng_avail_ind;
      v_service_feature_ob.dealer_chng_avail_ind :=
                                            services_rec.dealer_chng_avail_ind;
      v_service_feature_ob.client_modify_avail_ind :=
                                          services_rec.client_modify_avail_ind;
      v_service_feature_ob.dealer_modify_avail_ind :=
                                          services_rec.dealer_modify_avail_ind;
      v_service_feature_ob.corporate_renewal_ind :=
                                            services_rec.corporate_renewal_ind;
      v_service_feature_ob.non_corporate_renewal_ind :=
                                        services_rec.non_corporate_renewal_ind;
      v_service_feature_ob.ares_avail_ind := services_rec.ares_avail_ind;
      v_service_feature_ob.csa_avail_ind := services_rec.csa_avail_ind;
      v_service_feature_ob.soc_def_user_seg := services_rec.soc_def_user_seg;
      v_service_feature_ob.min_req_usg_chrg := services_rec.min_req_usg_chrg;
      v_service_feature_ob.min_commit_amt := services_rec.min_commit_amt;
      v_service_feature_ob.service_type_ind := services_rec.service_type_ind;
      v_service_feature_ob.period_set_code := services_rec.period_set_code;
      v_service_feature_ob.mp_ind := services_rec.mp_ind;
      v_service_feature_ob.brand_ind := services_rec.brand_id;
      v_service_feature_ob.aom_avail_ind := services_rec.aom_avail_ind;
	  v_service_feature_ob.mci_ind := services_rec.mci_ind;
	  v_service_feature_ob.coverage_type := services_rec.coverage_type;
	  v_service_feature_ob.bill_cycle_treatment_cd := services_rec.bill_cycle_treatment_cd;
	  v_service_feature_ob.seat_type := services_rec.seat_type;
	  v_service_feature_ob.soc_service_type := services_rec.soc_service_type;
	  v_service_feature_ob.soc_duration_hours := services_rec.soc_duration_hours;
	  v_service_feature_ob.rlh_ind := services_rec.rlh_ind;

      OPEN c_discount_available (pi_price_plan_code);

      FETCH c_discount_available
       INTO v_disc_soc;

      IF c_discount_available%FOUND
      THEN
         v_service_feature_ob.discount_available := 'Y';
      ELSE
         v_service_feature_ob.discount_available := 'N';
      END IF;

      CLOSE c_discount_available;

      OPEN c_suspension_price_plan (pi_price_plan_code);

      FETCH c_suspension_price_plan
       INTO v_susp_price_soc;

      IF c_suspension_price_plan%FOUND
      THEN
         v_service_feature_ob.suspension_price_plan := 'Y';
      ELSE
         v_service_feature_ob.suspension_price_plan := 'N';
      END IF;

      CLOSE c_suspension_price_plan;
      
      OPEN c_sharable_plan (pi_price_plan_code);

      FETCH c_sharable_plan
       INTO sharable_plan_rec;

      v_service_feature_ob.max_subscribers_number :=
                                      sharable_plan_rec.max_subscribers_number;
      v_service_feature_ob.secondary_soc := sharable_plan_rec.secondary_soc;

      CLOSE c_sharable_plan;
/*
      OPEN c_family_type (pi_price_plan_code);

      FETCH c_family_type
       INTO v_family_type;

      v_service_feature_ob.family_type := v_family_type;

      CLOSE c_family_type;
 */     
      --v_service_feature_ob.network_type := getNetworkTypeBySOC(pi_price_plan_code);

      getfeaturesforpriceplan (v_service_feature_ob);

      OPEN po_price_plan
       FOR
          SELECT *
            FROM TABLE (CAST (v_service_feature_tab AS service_feature_t));

      v_service_feature_tab.DELETE;
      v_service_feature_ob := NULL;
   EXCEPTION
      WHEN priceplannotfound
      THEN
         raise_application_error (-20201,
                                  'Price Plan pkg: Price Plan not found');
      WHEN NO_DATA_FOUND
      THEN
         IF (po_price_plan%ISOPEN)
         THEN
            CLOSE po_price_plan;
         END IF;

         IF (c_non_interface_price_plan%ISOPEN)
         THEN
            CLOSE c_non_interface_price_plan;
         END IF;

         IF (c_interface_price_plan%ISOPEN)
         THEN
            CLOSE c_interface_price_plan;
         END IF;

         IF (c_discount_available%ISOPEN)
         THEN
            CLOSE c_discount_available;
         END IF;

         IF (c_suspension_price_plan%ISOPEN)
         THEN
            CLOSE c_suspension_price_plan;
         END IF;

         IF (c_sharable_plan%ISOPEN)
         THEN
            CLOSE c_sharable_plan;
         END IF;
/*
         IF (c_family_type%ISOPEN)
         THEN
            CLOSE c_family_type;
         END IF;
*/
         OPEN po_price_plan
          FOR
             SELECT NULL
               FROM DUAL
              WHERE 1 = 0;
      WHEN OTHERS
      THEN
         IF (po_price_plan%ISOPEN)
         THEN
            CLOSE po_price_plan;
         END IF;

         IF (c_non_interface_price_plan%ISOPEN)
         THEN
            CLOSE c_non_interface_price_plan;
         END IF;

         IF (c_interface_price_plan%ISOPEN)
         THEN
            CLOSE c_interface_price_plan;
         END IF;

         IF (c_discount_available%ISOPEN)
         THEN
            CLOSE c_discount_available;
         END IF;

         IF (c_suspension_price_plan%ISOPEN)
         THEN
            CLOSE c_suspension_price_plan;
         END IF;

         raise_application_error (-20102,
                                     'Get Price Plan Query Failed. Oracle:(['
                                  || SQLCODE
                                  || '] ['
                                  || SQLERRM
                                  || '])');
   END getpriceplan;
   
  FUNCTION getpriceplanlist(
      pi_productType       	IN		VARCHAR2, 
	  pi_provinceCode		IN		VARCHAR2, 
	  pi_account_type       IN      VARCHAR2,
      pi_equipment_type     IN      VARCHAR2,
	  pi_brand				IN		NUMBER,
	  pi_currentPlansOnly	IN		VARCHAR2, 
	  pi_availableForActivation IN	VARCHAR2,
	  pi_networkType		IN		VARCHAR2,
	  po_priceplans			OUT	refcursor,
	  po_error_message      OUT     VARCHAR2
	  )
	  return NUMBER
	  IS
      i_result        NUMBER (1);
	  i_NETWORK_TYPE_ALL VARCHAR2(1) := 9;

    BEGIN
      BEGIN
         OPEN po_priceplans
          FOR
			SELECT DISTINCT
				s.soc, s.soc_description, s.soc_description_f, s.service_type, nvl(s.minimum_no_months, 0), 
				s.rda_ind, s.bl_zero_chrg_suppress_ind, psg.client_available_ind, psg.dealer_available_ind,
				psg.act_avail_ind, psg.chng_avail_ind, '', s.soc_status, s.for_sale_ind, s.sale_eff_date,
				s.sale_exp_date, s.current_ind, decode(s.product_type, 'C', 'Y', s.inc_cel_ftr_ind), 
				s.inc_dc_ftr_ind, s.inc_pds_ftr_ind, decode(dps.soc,null, 'N', 'Y'), nvl(rc.rate,0), 
				urf.uc_rounding_factor, 0, nvl(s.min_commit_amt, 0), s.soc_level_code, 
				psg.client_chng_avail_ind, psg.dealer_chng_avail_ind, psg.client_modify_avail_ind,
				psg.dealer_modify_avail_ind, psg.corporate_renewal_ind, psg.non_corporate_renewal_ind, 
				s.period_set_code, nvl(rf.rc_freq_of_pym,0), nvl(s.soc_def_user_seg ,'PERSONAL  '),
				psg.ares_avail_ind, psg.csa_avail_ind, 
				--decode(sg_r.family_type, null, '@', sg_r.family_type), 
				s.min_req_usg_chrg, decode(sarc.soc,null, 'N', 'Y') sarc_soc, s.mp_ind, s.brand_id brand_id, 
				psg.aom_avail_ind, s.mci_ind, ser.network_type, s.bill_cycle_treatment_cd,
				s.seat_type, s.soc_service_type, s.soc_duration_hours, s.rlh_ind
			FROM acct_type_priceplan_soc_groups atpsg, priceplan_soc_groups psg, soc_group sg, soc s, 
				soc_submkt_relation ssr, soc_equip_relation ser, market m, logical_date ld, 
				soc_discount_plan dps, pp_rc_rate rc, rated_feature rf, uc_rated_feature urf,
				--(select sg.soc, sfg.family_type  from soc_group sg, soc_family_group sfg
			    --where family_type in ('R', 'T', 'F', 'N', 'O', 'D', 'M') and sg.gp_soc = sfg.soc_group) sg_r, 
			    soc_activity_reason_code sarc
			WHERE	atpsg.acct_type = pi_account_type
			and    	psg.priceplan_soc_group_id = atpsg.priceplan_soc_group_id 
			and	psg.act_avail_ind = pi_availableForActivation and  sg.gp_soc = psg.price_plan_group 
			and    	s.soc = sg.soc and	s.effective_date = ssr.effective_date 
			and    	s.service_type = 'P' and 	s.product_type = pi_producttype
			and    	s.soc_status = 'A' and    	s.for_sale_ind = 'Y' 
			and    	trunc(s.sale_eff_date) <= trunc(ld.logical_date)
			and	(s.sale_exp_date is null OR trunc(s.sale_exp_date) > trunc(ld.logical_date)) 
			and    	ld.logical_date_type = 'O' and	s.current_ind = pi_currentPlansOnly
			and    	m.province = pi_provincecode and (ssr.sub_market = 'ALL' or (ssr.sub_market = m.market_code))  
			and   	ssr.soc = s.soc and   	ser.soc = ssr.soc 
			and	ser.effective_date = ssr.effective_date  and	ser.equipment_type = pi_equipment_type
			and	dps.soc(+) = s.soc and	rc.soc(+) = s.soc  and	rc.effective_date(+) = s.effective_date 
			and	(rc.feature_code = 'STD' or rc.feature_code is null) and	rf.soc(+) = s.soc 
			and	rf.effective_date(+)=s.effective_date and	(rf.feature_code = 'STD'  or rf.feature_code is null) 
			and	urf.soc(+) = s.soc  and	urf.effective_date(+) = s.effective_date and	(urf.feature_code = 'STD' 
			or urf.feature_code is null) 
			--and sg_r.soc(+) = s.soc
			and sarc.soc(+) = s.soc
			and	(urf.action = (select min(action) from uc_rated_feature urf1 where urf1.soc(+) = urf.soc 
            and urf1.effective_date(+) = urf.effective_date and urf1.feature_code(+) = urf.feature_code) 
         	or urf.action is null) and not exists (select 1 from soc_group sg1, priceplan_soc_groups psg1, 
			priceplan_soc_gp_product_type psgpt where sg1.soc = s.soc and psg1.price_plan_group = sg1.gp_soc 
			and psgpt.priceplan_soc_group_id = psg1.priceplan_soc_group_id and psgpt.include_ind = 'Y')						
			and (i_NETWORK_TYPE_ALL = pi_networkType or (ser.network_type = pi_networkType or ser.network_type = i_NETWORK_TYPE_ALL))
			order by s.soc_description;

            i_result := numeric_true;
      EXCEPTION
         WHEN NO_DATA_FOUND
         THEN
            po_error_message := err_no_data_found;
            i_result := numeric_false;

            IF (po_priceplans%ISOPEN)
            THEN
               CLOSE po_priceplans;
            END IF;
         WHEN OTHERS
         THEN
            po_error_message := err_other;
            i_result := numeric_false;
            IF (po_priceplans%ISOPEN)
            THEN
               CLOSE po_priceplans;
            END IF;

            raise_application_error
                     (-20102,
                         'getpriceplanlist Query Failed. Oracle:(['
                      || SQLCODE
                      || '] ['
                      || SQLERRM
                      || '])');
      END;  
      RETURN i_result;
   END getpriceplanlist;
   
   ------------------------------------------------------------------------------------
/* Not In use - This procedure is not in spec, and not referenced by other procedure in this package
 * So comment out M.Liao Sept 16, 2010
    
   PROCEDURE getregularservices (
      po_services           OUT      refcursor
   )
   IS
      v_service_feature_ob   service_feature_o;

      CURSOR c_services
      IS
         SELECT DISTINCT s.service_type, s.soc, s.soc_description,
                         s.soc_description_f,
                         NVL (s.minimum_no_months, 0) AS minimum_no_months,
                         NVL (s.eserve_ind, 'N') AS eserve_ind,
                         NVL (s.rda_ind, 'N') AS rda_ind,
                         s.bl_zero_chrg_suppress_ind,
                         DECODE (uspt.soc, NULL, 'N', 'Y') AS promo,
                         s.soc_status, s.for_sale_ind,
                         s.sale_eff_date, s.sale_exp_date, s.current_ind,
                         DECODE (s.product_type, 'C', 'Y', s.inc_cel_ftr_ind)
                         	AS telephony_features_inc,
                         s.inc_dc_ftr_ind, s.inc_pds_ftr_ind, s.product_type,
                         s.period_set_code, s.soc_level_code,
                         uspt.act_avail_ind, uspt.chng_avail_ind, 
                         uspt.included_promo_ind, s.coverage_type
                    FROM soc s, uc_soc_promo_terms uspt
                   WHERE s.service_type != 'P'
                     AND uspt.soc(+) = s.soc
                ORDER BY 2;

      services_rec           c_services%ROWTYPE;

      CURSOR c_soc_fam (p_soc VARCHAR2)
      IS
         SELECT sfg.family_type
           FROM soc s, soc_group sg, soc_family_group sfg
          WHERE s.soc = p_soc
            AND sg.soc = s.soc
            AND sfg.soc_group = sg.gp_soc
            AND sfg.family_type IN ('E', 'L', 'C', 'G', 'A', 'Z');

      soc_fam_rec            c_soc_fam%ROWTYPE;      

      CURSOR c_soc_rel (p_soc VARCHAR2)
      IS
         SELECT soc_src, soc_dest, relation_type
           FROM soc_relation
          WHERE soc_dest(+) = p_soc 
            AND relation_type = 'F'
         UNION
         SELECT soc_src, soc_dest, 'W'
           FROM soc_relation_ext
          WHERE soc_dest(+) = p_soc 
            AND relation_type = 'M'
         UNION
         SELECT soc_src, soc_dest, 'X'
           FROM soc_relation_ext
          WHERE soc_dest(+) = p_soc 
            AND relation_type = 'S'
         UNION
         SELECT soc_dest, soc_src, relation_type
           FROM soc_relation_ext
          WHERE soc_src(+) = p_soc 
            AND relation_type IN ('M', 'S');

      soc_rel_rec            c_soc_rel%ROWTYPE;

   BEGIN
      v_service_feature_tab := service_feature_t ();

      FOR services_rec IN c_services ()
      LOOP
         EXIT WHEN c_services%NOTFOUND;
         initservicefeatureobject (v_service_feature_ob);
         v_service_feature_ob.service_type := services_rec.service_type;
         v_service_feature_ob.soc := services_rec.soc;
         v_service_feature_ob.soc_description := services_rec.soc_description;
         v_service_feature_ob.soc_description_f := services_rec.soc_description_f;
         v_service_feature_ob.minimum_no_months := services_rec.minimum_no_months;
         v_service_feature_ob.eserve_ind := services_rec.eserve_ind;
         v_service_feature_ob.rda_ind := services_rec.rda_ind;
         v_service_feature_ob.bl_zero_chrg_suppress_ind := services_rec.bl_zero_chrg_suppress_ind;
         v_service_feature_ob.promo := services_rec.promo;
         v_service_feature_ob.soc_status := services_rec.soc_status;
         v_service_feature_ob.for_sale_ind := services_rec.for_sale_ind;
         v_service_feature_ob.sale_eff_date := services_rec.sale_eff_date;
         v_service_feature_ob.sale_exp_date := services_rec.sale_exp_date;
         v_service_feature_ob.current_ind := services_rec.current_ind;
         v_service_feature_ob.telephony_features_inc := services_rec.telephony_features_inc;
         v_service_feature_ob.dispatch_features_inc := services_rec.inc_dc_ftr_ind;
         v_service_feature_ob.wireless_web_features_inc := services_rec.inc_pds_ftr_ind;
         v_service_feature_ob.s_product_type := services_rec.product_type;
         v_service_feature_ob.period_set_code := services_rec.period_set_code;
         v_service_feature_ob.soc_level_code := services_rec.soc_level_code;
         --v_service_feature_ob.equipment_type := services_rec.equipment_type;
         v_service_feature_ob.coverage_type := services_rec.coverage_type;
				 --v_service_feature_ob.network_type := getNetworkTypeBySOC(services_rec.soc);
       
         FOR soc_rel_rec IN c_soc_rel (services_rec.soc)
         LOOP
            EXIT WHEN c_soc_rel%NOTFOUND;
            
          	CASE
            WHEN soc_rel_rec.relation_type = 'F'
            	THEN  v_service_feature_ob.has_promotion := 'Y';          
            	
           	WHEN soc_rel_rec.relation_type = 'M'
            	THEN  v_service_feature_ob.has_bound := 'Y';          
            	
           	WHEN soc_rel_rec.relation_type = 'W'
         	   THEN  v_service_feature_ob.is_bound := 'Y';          
         	   
           	WHEN soc_rel_rec.relation_type = 'S'
            	THEN  v_service_feature_ob.has_seq_bound := 'Y';          
            	
           	WHEN soc_rel_rec.relation_type = 'X'
            	THEN  v_service_feature_ob.is_seq_bound := 'Y';
           	END CASE;
         END LOOP;
         
         FOR soc_fam_rec IN c_soc_fam (services_rec.soc)
         LOOP
            EXIT WHEN c_soc_fam%NOTFOUND;

            IF soc_fam_rec.family_type = 'L'
            THEN
	            v_service_feature_ob.loyalty_and_retention_service := 'Y';
            END IF;

            IF soc_fam_rec.family_type = 'E'
            THEN
	            v_service_feature_ob.equiv_service_exists := 'Y';
            END IF;

            IF soc_fam_rec.family_type = 'G'
            THEN
	            v_service_feature_ob.lbs_trackee := 'Y';
            END IF;

            IF soc_fam_rec.family_type = 'C'
            THEN
    	        v_service_feature_ob.lbs_tracker := 'Y';
            END IF;

            IF soc_fam_rec.family_type = 'A'
            THEN
        	    v_service_feature_ob.mandatory := 'Y';
            END IF;
                     
            IF soc_fam_rec.family_type = 'Z'
            THEN
            	v_service_feature_ob.promo_validation_eligible := 'Y';
            END IF;      
         END LOOP;

         getfeaturesforservice (v_service_feature_ob);
      END LOOP;

      OPEN po_services
       FOR
          SELECT *
            FROM TABLE (CAST (v_service_feature_tab AS service_feature_t));

      v_service_feature_tab.DELETE;
      v_service_feature_ob := NULL;

   EXCEPTION
      WHEN NO_DATA_FOUND
      THEN
         IF (po_services%ISOPEN)
         THEN
            CLOSE po_services;
         END IF;

         IF (c_services%ISOPEN)
         THEN
            CLOSE c_services;
         END IF;

         IF (c_soc_rel%ISOPEN)
         THEN
            CLOSE c_soc_rel;
         END IF;

         IF (c_soc_fam%ISOPEN)
         THEN
            CLOSE c_soc_fam;
         END IF;

         OPEN po_services
          FOR
             SELECT NULL
               FROM DUAL
              WHERE 1 = 0;
              
      WHEN OTHERS
      THEN
         IF (po_services%ISOPEN)
         THEN
            CLOSE po_services;
         END IF;

         IF (c_services%ISOPEN)
         THEN
            CLOSE c_services;
         END IF;

         IF (c_soc_rel%ISOPEN)
         THEN
            CLOSE c_soc_rel;
         END IF;

         IF (c_soc_fam%ISOPEN)
         THEN
            CLOSE c_soc_fam;
         END IF;

         raise_application_error
                          (-20102,
                              'Get Regular Services query failed. Oracle:(['
                           || SQLCODE
                           || '] ['
                           || SQLERRM
                           || '])');
   
   END getregularservices;

*/

   /* NOT IN USE after introduction of getSocEquipRelations */
	 FUNCTION getNetworkTypeBySOC (pi_soc VARCHAR2)
	 RETURN VARCHAR2
	 IS
	 networkType   VARCHAR2(1);
	 CURSOR c_network_type 
      IS
      	SELECT network_type 
      	FROM   soc_equip_relation ser 
      	WHERE  soc = pi_soc
      	GROUP BY soc, network_type;
      	
   v_network_type  soc_equip_relation.network_type%TYPE;
      

	 BEGIN
	 	  OPEN c_network_type;
      FETCH c_network_type
       INTO v_network_type;

      networkType := v_network_type;

      CLOSE c_network_type;
      
      RETURN networkType;
      
   EXCEPTION
   WHEN NO_DATA_FOUND
   THEN
         raise_application_error (-20101, 'SOC Network Type Cannot be Found');
   WHEN OTHERS
   THEN
         RAISE;
   END getNetworkTypeBySOC;
   
   /*
   This procedure is to check if a SOC belongs to a PDA / RIM mandatory soc group. Prior to Holborn, a RIM-only / PDA-only SOC has equipment
   type configured to RIM or PDA only. Sinec Holborn, a RIM-only / PDA-only SOC is opened up for other equipment types and is therefore unable to
   recognize a RIM/PDA mandatory SOC.
   */
   PROCEDURE checkMandatorySOC(
   			pi_soc        IN  VARCHAR2,
   			po_pda_count  OUT NUMBER,
   			po_rim_count  OUT NUMBER)
   IS
   CURSOR c_mandatory_socgroup
   		IS
   			SELECT SUM (DECODE (TRIM(gp_soc), 
   			                    'MRIMP', 1, 
   			                    'MRIMM', 1, 
   			                    0)) rim,
       				 SUM (DECODE (TRIM(gp_soc), 'MPDA', 1, 0)) pda
  			FROM soc_group WHERE SOC =pi_soc;

	 mandatory_socgroup_rec       c_mandatory_socgroup%ROWTYPE;
   BEGIN
   		OPEN c_mandatory_socgroup;

   		FETCH c_mandatory_socgroup
   		 INTO mandatory_socgroup_rec;
   		
   		po_pda_count := mandatory_socgroup_rec.pda;
   		po_rim_count := mandatory_socgroup_rec.rim;
   		
   		CLOSE c_mandatory_socgroup;

   EXCEPTION
   WHEN NO_DATA_FOUND
   THEN
   		po_pda_count := 0;
   		po_rim_count := 0;

   WHEN OTHERS
   THEN
   		RAISE;
   END checkMandatorySOC;
   
   
   PROCEDURE checkMandatorySOCs(
   			pi_socs        IN  t_socs,
   			po_result     OUT REFCURSOR)
   IS
   BEGIN
   		OPEN po_result
   		FOR SELECT SOC, SUM (DECODE (TRIM(gp_soc), 
   			                    'MRIMP', 1, 
   			                    'MRIMM', 1, 
   			                    0)) rim,
       				 SUM (DECODE (TRIM(gp_soc), 'MPDA', 1, 0)) pda
  			FROM soc_group WHERE SOC IN (SELECT *
                                     FROM TABLE (CAST (pi_socs AS t_socs)))
        GROUP BY SOC;

   EXCEPTION
   WHEN NO_DATA_FOUND
   THEN
   		IF po_result%ISOPEN
   		THEN
   			CLOSE po_result;
   		END IF;

   WHEN OTHERS
   THEN
   		RAISE;
   END checkMandatorySOCs;        



   PROCEDURE getSocEquipRelations(
   			pi_socs				IN	t_socs,
   			po_result			OUT	REFCURSOR)
   IS
   BEGIN
   		OPEN po_result
   		FOR SELECT soc, equipment_type, network_type 
   				FROM soc_equip_relation 
   				WHERE soc in (SELECT *
   				              FROM TABLE (CAST (pi_socs AS t_socs)));
   EXCEPTION
   WHEN NO_DATA_FOUND
   THEN
   		IF po_result%ISOPEN
   		THEN
   			CLOSE po_result;
   		END IF;

   WHEN OTHERS
   THEN
   		RAISE;
   END getSocEquipRelations;

--------------------------------------------------------------------------
-- Get FamilyType for SOC
--------------------------------------------------------------------------
   PROCEDURE getSocFamilyTypes(
   			pi_socs				IN	t_socs,
   			po_result			OUT	REFCURSOR)
   IS
   BEGIN
   		OPEN po_result
   		FOR select distinct sg.soc, sfg.family_type  
			from  soc_group sg
			join soc_family_group sfg on sfg.soc_group = sg.gp_soc
   			WHERE soc in (SELECT * FROM TABLE (CAST (pi_socs AS t_socs)));
   EXCEPTION
   WHEN NO_DATA_FOUND
   THEN
   		IF po_result%ISOPEN
   		THEN
   			CLOSE po_result;
   		END IF;

   WHEN OTHERS
   THEN
   		RAISE;
   END getSocFamilyTypes;

--------------------------------------------------------------------------
-- Get Allow sharing group for SOC
--------------------------------------------------------------------------
   PROCEDURE getSocAllowSharingGroups(
   			pi_socs				IN	t_socs,
   			po_result			OUT	REFCURSOR)
   IS
   BEGIN
   		OPEN po_result for select sysdate from dual;
   		OPEN po_result
   		FOR select distinct sg.soc, sgasg.ALLOW_SHARING_GROUP_CD , sgasg.ALLOW_SHARING_ACCESS_TYPE_CD 
			from  soc_group sg
			join  soc_grp_allow_sharing_grp sgasg on sgasg.gp_soc = sg.gp_soc
   			WHERE soc in (SELECT * FROM TABLE (CAST (pi_socs AS t_socs)));
   EXCEPTION
   WHEN NO_DATA_FOUND
   THEN
   		IF po_result%ISOPEN
   		THEN
   			CLOSE po_result;
   		END IF;

   WHEN OTHERS
   THEN
   		RAISE;
   END getSocAllowSharingGroups;
END;
/

SHO err

