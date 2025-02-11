/* Formatted on 2009/01/21 10:52 (Formatter Plus v4.8.0) */
CREATE OR REPLACE PACKAGE BODY client_equipment
AS
------------------------------------------------------------------------------------
------------------------------------------------------------------------------------
   PROCEDURE getequipmentinfo (
      pi_serial_no                    IN       VARCHAR2,
      po_serial_no                    OUT      VARCHAR2,
      po_tech_type                    OUT      VARCHAR2,
      po_product_cd                   OUT      VARCHAR2,
      po_product_status_cd            OUT      VARCHAR2,
      po_vendor_name                  OUT      VARCHAR2,
      po_vendor_no                    OUT      VARCHAR2,
      po_equipment_status_type_id     OUT      NUMBER,
      po_equipment_status_id          OUT      NUMBER,
      po_stolen                       OUT      NUMBER,
      po_sublock1                     OUT      VARCHAR2,
      po_product_gp_type_id           OUT      NUMBER,
      po_product_gp_type_cd           OUT      VARCHAR2,
      po_product_gp_type_des          OUT      VARCHAR2,
      po_product_type_id              OUT      NUMBER,
      po_product_type_des             OUT      VARCHAR2,
      po_product_class_id             OUT      NUMBER,
      po_product_class_cd             OUT      VARCHAR2,
      po_product_class_des            OUT      VARCHAR2,
      po_provider_owner_id            OUT      NUMBER,
      po_lastmuleimei_for_sim         OUT      VARCHAR2,
      po_english_product_name         OUT      VARCHAR2,
      po_french_product_name          OUT      VARCHAR2,
      po_browser_version              OUT      VARCHAR2,
      po_firmware_version             OUT      VARCHAR2,
      po_prl_cd                       OUT      VARCHAR2,
      po_prl_des                      OUT      VARCHAR2,
      po_product_type_des_f           OUT      VARCHAR2,
      po_product_gp_type_des_f        OUT      VARCHAR2,
      po_min_cd                       OUT      VARCHAR2,
      po_customer_id                  OUT      NUMBER,
      po_product_type_list            OUT      VARCHAR2,
      po_initial_activation           OUT      VARCHAR2,
      po_mode_code                    OUT      NUMBER,
      po_mode_description             OUT      VARCHAR2,
      po_product_type                 OUT      VARCHAR2,
      po_equipment_type               OUT      VARCHAR2,
      po_equipment_type_class         OUT      VARCHAR2,
      po_cross_fleet                  OUT      VARCHAR2,
      po_cost                         OUT      NUMBER,
      po_cap_code                     OUT      VARCHAR2,
      po_coverage_region_code_list    OUT      VARCHAR2,
      po_encoding_format_code         OUT      VARCHAR2,
      po_ownership_code               OUT      VARCHAR2,
      po_prepaid                      OUT      VARCHAR2,
      po_frequency_cd                 OUT      VARCHAR2,
      po_firmware_feature_code_list   OUT      VARCHAR2,
      po_browser_protocol             OUT      VARCHAR2,
      po_unscanned                    OUT      NUMBER,
      po_equip_status_dt              OUT      DATE,
      po_puk                          OUT      VARCHAR2,
      po_product_category_id          OUT      NUMBER,
      po_virtual                      OUT      NUMBER,
      po_rim_pin                      OUT      VARCHAR2,
      po_brands                       OUT      brand_array,
	  po_assohandsetimei_for_usim	  OUT	   VARCHAR2,
	  po_local_imsi					  OUT	   VARCHAR2,
	  po_remote_imsi				  OUT	   VARCHAR2,
	  po_assignable					  OUT	   NUMBER,
	  po_previously_activated		  OUT      NUMBER,
      po_equipment_group              OUT      VARCHAR2,
      po_last_event_type              OUT      VARCHAR2,
      po_source_network_type_cd       OUT      VARCHAR2,
      po_pre_post_paid_flag           OUT      VARCHAR2
   )
   IS
      v_tech_type_class      VARCHAR2 (10);
      v_initial_activation   BOOLEAN       := TRUE;
   BEGIN
      po_serial_no := pi_serial_no;
      gettechnologytypeclass (pi_serial_no, v_tech_type_class);
      getequipmentinfo (pi_serial_no,
                        v_tech_type_class,
                        v_initial_activation,
                        po_tech_type,
                        po_product_cd,
                        po_product_status_cd,
                        po_vendor_name,
                        po_vendor_no,
                        po_equipment_status_type_id,
                        po_equipment_status_id,
                        po_stolen,
                        po_sublock1,
                        po_product_gp_type_id,
                        po_product_gp_type_cd,
                        po_product_gp_type_des,
                        po_product_type_id,
                        po_product_type_des,
                        po_product_class_id,
                        po_product_class_cd,
                        po_product_class_des,
                        po_provider_owner_id,
                        po_lastmuleimei_for_sim,
                        po_english_product_name,
                        po_french_product_name,
                        po_browser_version,
                        po_firmware_version,
                        po_prl_cd,
                        po_prl_des,
                        po_product_type_des_f,
                        po_product_gp_type_des_f,
                        po_min_cd,
                        po_customer_id,
                        po_product_type_list,
                        po_initial_activation,
                        po_mode_code,
                        po_mode_description,
                        po_product_type,
                        po_equipment_type,
                        po_equipment_type_class,
                        po_cross_fleet,
                        po_cost,
                        po_cap_code,
                        po_coverage_region_code_list,
                        po_encoding_format_code,
                        po_ownership_code,
                        po_prepaid,
                        po_frequency_cd,
                        po_firmware_feature_code_list,
                        po_browser_protocol,
                        po_unscanned,
                        po_equip_status_dt,
                        po_puk,
                        po_product_category_id,
                        po_virtual,
                        po_rim_pin,
                        po_brands,
						po_assohandsetimei_for_usim,
						po_local_imsi,
						po_remote_imsi,
						po_assignable,
						po_previously_activated,
						po_equipment_group,
						po_last_event_type,
						po_source_network_type_cd,
						po_pre_post_paid_flag
                       );
   EXCEPTION
      WHEN esnnotfound
      THEN
         raise_application_error
                         (-20310,
                          'Client_Equipment pkg: ESN or Product ID not found');
      WHEN OTHERS
      THEN
         RAISE;
--    RAISE_APPLICATION_ERROR(SQLCODE,SQLERRM);
   END getequipmentinfo;

------------------------------------------------------------------------------------
------------------------------------------------------------------------------------
   PROCEDURE getequipmentinfobyphoneno (
      pi_phone_number                 IN       VARCHAR2,
      po_serial_no                    OUT      VARCHAR2,
      po_tech_type                    OUT      VARCHAR2,
      po_product_cd                   OUT      VARCHAR2,
      po_product_status_cd            OUT      VARCHAR2,
      po_vendor_name                  OUT      VARCHAR2,
      po_vendor_no                    OUT      VARCHAR2,
      po_equipment_status_type_id     OUT      NUMBER,
      po_equipment_status_id          OUT      NUMBER,
      po_stolen                       OUT      NUMBER,
      po_sublock1                     OUT      VARCHAR2,
      po_product_gp_type_id           OUT      NUMBER,
      po_product_gp_type_cd           OUT      VARCHAR2,
      po_product_gp_type_des          OUT      VARCHAR2,
      po_product_type_id              OUT      NUMBER,
      po_product_type_des             OUT      VARCHAR2,
      po_product_class_id             OUT      NUMBER,
      po_product_class_cd             OUT      VARCHAR2,
      po_product_class_des            OUT      VARCHAR2,
      po_provider_owner_id            OUT      NUMBER,
      po_lastmuleimei_for_sim         OUT      VARCHAR2,
      po_english_product_name         OUT      VARCHAR2,
      po_french_product_name          OUT      VARCHAR2,
      po_browser_version              OUT      VARCHAR2,
      po_firmware_version             OUT      VARCHAR2,
      po_prl_cd                       OUT      VARCHAR2,
      po_prl_des                      OUT      VARCHAR2,
      po_product_type_des_f           OUT      VARCHAR2,
      po_product_gp_type_des_f        OUT      VARCHAR2,
      po_min_cd                       OUT      VARCHAR2,
      po_customer_id                  OUT      NUMBER,
      po_product_type_list            OUT      VARCHAR2,
      po_initial_activation           OUT      VARCHAR2,
      po_mode_code                    OUT      NUMBER,
      po_mode_description             OUT      VARCHAR2,
      po_product_type                 OUT      VARCHAR2,
      po_equipment_type               OUT      VARCHAR2,
      po_equipment_type_class         OUT      VARCHAR2,
      po_cross_fleet                  OUT      VARCHAR2,
      po_cost                         OUT      NUMBER,
      po_firmware_feature_code_list   OUT      VARCHAR2,
      po_browser_protocol             OUT      VARCHAR2,
      po_unscanned                    OUT      NUMBER,
      po_equip_status_dt              OUT      DATE,
      po_puk                          OUT      VARCHAR2,
      po_product_category_id          OUT      NUMBER,
      po_virtual                      OUT      NUMBER,
      po_rim_pin                      OUT      VARCHAR2,
      po_brands                       OUT      brand_array,
	  po_assohandsetimei_for_usim     OUT	   VARCHAR2,
	  po_local_imsi				      OUT	   VARCHAR2,
	  po_remote_imsi				  OUT	   VARCHAR2,
	  po_assignable					  OUT	   NUMBER,
      po_previously_activated         OUT      NUMBER,
      po_equipment_group              OUT      VARCHAR2,
      po_last_event_type              OUT      VARCHAR2,
      po_source_network_type_cd	      OUT      VARCHAR2,
      po_pre_post_paid_flag           OUT      VARCHAR2
   )
   IS
      CURSOR c_equipment_pcs
      IS
         SELECT   pe.serial_no
             FROM pcs_equipment pe, pcs_equip_status pes
            WHERE pe.mdn_cd = pi_phone_number
              AND pe.seq_no = 0
              AND pes.serial_no = pe.serial_no
              AND pes.seq_no = pe.seq_no
              AND pes.equipment_status_type_id = 2
              AND pes.equipment_status_id IN (8, 88)
              AND pes.equipment_status_dt =
                     (SELECT MAX (equipment_status_dt)
                        FROM pcs_equip_status pes1
                       WHERE pes1.serial_no = pes.serial_no
                         AND pes1.seq_no = pes.seq_no
                         AND pes1.equipment_status_type_id = 2)
         ORDER BY equipment_status_dt DESC;

      CURSOR c_equipment_mike
      IS
         SELECT   pe.serial_no
             FROM iden_equipment pe, iden_equip_status pes
            WHERE pe.min_cd = pi_phone_number
              AND pe.seq_no = 0
              AND pes.serial_no = pe.serial_no
              AND pes.seq_no = pe.seq_no
              AND pes.equipment_status_type_id = 2
              AND pes.equipment_status_id IN (8, 88)
              AND pes.equipment_status_dt =
                     (SELECT MAX (equipment_status_dt)
                        FROM iden_equip_status pes1
                       WHERE pes1.serial_no = pes.serial_no
                         AND pes1.seq_no = pes.seq_no
                         AND pes1.equipment_status_type_id = 2)
         ORDER BY equipment_status_dt DESC;

      CURSOR c_equip_analog
      IS
         SELECT   pe.serial_no
             FROM analog_equip pe
            WHERE pe.mdn_cd = pi_phone_number AND pe.seq_no = 0
         ORDER BY update_dt DESC;
--USIM
	  CURSOR c_equipment_usim
      IS
         SELECT   pe.usim_id
             FROM subscriber_network_num pe, usim_status pes
            WHERE pe.msisdn = pi_phone_number
              AND pe.usim_seq_no = 0
              AND pes.usim_id = pe.usim_id
              AND pes.seq_no = pe.usim_seq_no
              AND pes.equipment_status_type_id = 2
              AND pes.equipment_status_id IN (8, 88)
              AND pes.equipment_status_dt =
                     (SELECT MAX (equipment_status_dt)
                        FROM usim_status pes1
                       WHERE pes1.usim_id = pes.usim_id
                         AND pes1.seq_no = pes.seq_no
                         AND pes1.equipment_status_type_id = 2)
         ORDER BY equipment_status_dt DESC;
		 
      v_serial_no                   pcs_equipment.serial_no%TYPE;
      v_initial_activation          BOOLEAN                        := FALSE;
      v_tech_type_class             VARCHAR2 (10);
      v_cap_code                    VARCHAR2 (30);
      v_coverage_region_code_list   VARCHAR2 (255);
      v_encoding_format_code        VARCHAR2 (2);
      v_ownership_code              VARCHAR2 (20);
      v_prepaid                     VARCHAR2 (1);
      v_frequency_cd                VARCHAR2 (20);
	  
   BEGIN
      OPEN c_equipment_pcs;

      FETCH c_equipment_pcs
       INTO v_serial_no;

      CLOSE c_equipment_pcs;

      IF v_serial_no IS NOT NULL
      THEN
         v_tech_type_class := 'PCS';
      ELSE
         OPEN c_equipment_mike;

         FETCH c_equipment_mike
          INTO v_serial_no;

         CLOSE c_equipment_mike;

         IF v_serial_no IS NOT NULL
         THEN
            v_tech_type_class := 'MIKE';
         ELSE
            OPEN c_equipment_usim;

		    FETCH c_equipment_usim
		    INTO v_serial_no;

		    CLOSE c_equipment_usim;

		    IF v_serial_no IS NOT NULL
		    THEN
		        v_tech_type_class := 'USIM';
            ELSE
				OPEN c_equip_analog;

				FETCH c_equip_analog
				INTO v_serial_no;

				CLOSE c_equip_analog;

				IF v_serial_no IS NOT NULL
				THEN
					v_tech_type_class := 'ANALOG';
		         ELSE
					RAISE phonenotfound;
				END IF;	
            END IF;
         END IF;
      END IF;

      po_serial_no := v_serial_no;
      getequipmentinfo (v_serial_no,
                        v_tech_type_class,
                        v_initial_activation,
                        po_tech_type,
                        po_product_cd,
                        po_product_status_cd,
                        po_vendor_name,
                        po_vendor_no,
                        po_equipment_status_type_id,
                        po_equipment_status_id,
                        po_stolen,
                        po_sublock1,
                        po_product_gp_type_id,
                        po_product_gp_type_cd,
                        po_product_gp_type_des,
                        po_product_type_id,
                        po_product_type_des,
                        po_product_class_id,
                        po_product_class_cd,
                        po_product_class_des,
                        po_provider_owner_id,
                        po_lastmuleimei_for_sim,
                        po_english_product_name,
                        po_french_product_name,
                        po_browser_version,
                        po_firmware_version,
                        po_prl_cd,
                        po_prl_des,
                        po_product_type_des_f,
                        po_product_gp_type_des_f,
                        po_min_cd,
                        po_customer_id,
                        po_product_type_list,
                        po_initial_activation,
                        po_mode_code,
                        po_mode_description,
                        po_product_type,
                        po_equipment_type,
                        po_equipment_type_class,
                        po_cross_fleet,
                        po_cost,
                        v_cap_code,
                        v_coverage_region_code_list,
                        v_encoding_format_code,
                        v_ownership_code,
                        v_prepaid,
                        v_frequency_cd,
                        po_firmware_feature_code_list,
                        po_browser_protocol,
                        po_unscanned,
                        po_equip_status_dt,
                        po_puk,
                        po_product_category_id,
                        po_virtual,
                        po_rim_pin,
                        po_brands,
						po_assohandsetimei_for_usim,
						po_local_imsi,
						po_remote_imsi,
						po_assignable,
						po_previously_activated,
                        po_equipment_group,
                        po_last_event_type,
                        po_source_network_type_cd,
                        po_pre_post_paid_flag
                       );
      getbrands (po_product_cd, po_brands);
   EXCEPTION
      WHEN phonenotfound
      THEN
         raise_application_error
                (-20317,
                 'Client_Equipment pkg: Phone is not found as active in SEMS');
      WHEN OTHERS
      THEN
         RAISE;
--    RAISE_APPLICATION_ERROR(SQLCODE,SQLERRM);
   END getequipmentinfobyphoneno;

------------------------------------------------------------------------------------
------------------------------------------------------------------------------------
PROCEDURE getequipmentinfo (
      pi_serial_no                    IN       VARCHAR2,
      pi_tech_type_class              IN       VARCHAR2,
      pi_initial_activation           IN       BOOLEAN,
      po_tech_type                    OUT      VARCHAR2,
      po_product_cd                   OUT      VARCHAR2,
      po_product_status_cd            OUT      VARCHAR2,
      po_vendor_name                  OUT      VARCHAR2,
      po_vendor_no                    OUT      VARCHAR2,
      po_equipment_status_type_id     OUT      NUMBER,
      po_equipment_status_id          OUT      NUMBER,
      po_stolen                       OUT      NUMBER,
      po_sublock1                     OUT      VARCHAR2,
      po_product_gp_type_id           OUT      NUMBER,
      po_product_gp_type_cd           OUT      VARCHAR2,
      po_product_gp_type_des          OUT      VARCHAR2,
      po_product_type_id              OUT      NUMBER,
      po_product_type_des             OUT      VARCHAR2,
      po_product_class_id             OUT      NUMBER,
      po_product_class_cd             OUT      VARCHAR2,
      po_product_class_des            OUT      VARCHAR2,
      po_provider_owner_id            OUT      NUMBER,
      po_lastmuleimei_for_sim         OUT      VARCHAR2,
      po_english_product_name         OUT      VARCHAR2,
      po_french_product_name          OUT      VARCHAR2,
      po_browser_version              OUT      VARCHAR2,
      po_firmware_version             OUT      VARCHAR2,
      po_prl_cd                       OUT      VARCHAR2,
      po_prl_des                      OUT      VARCHAR2,
      po_product_type_des_f           OUT      VARCHAR2,
      po_product_gp_type_des_f        OUT      VARCHAR2,
      po_min_cd                       OUT      VARCHAR2,
      po_customer_id                  OUT      NUMBER,
      po_product_type_list            OUT      VARCHAR2,
      po_initial_activation           OUT      VARCHAR2,
      po_mode_code                    OUT      NUMBER,
      po_mode_description             OUT      VARCHAR2,
      po_product_type                 OUT      VARCHAR2,
      po_equipment_type               OUT      VARCHAR2,
      po_equipment_type_class         OUT      VARCHAR2,
      po_cross_fleet                  OUT      VARCHAR2,
      po_cost                         OUT      NUMBER,
      po_cap_code                     OUT      VARCHAR2,
      po_coverage_region_code_list    OUT      VARCHAR2,
      po_encoding_format_code         OUT      VARCHAR2,
      po_ownership_code               OUT      VARCHAR2,
      po_prepaid                      OUT      VARCHAR2,
      po_frequency_cd                 OUT      VARCHAR2,
      po_firmware_feature_code_list   OUT      VARCHAR2,
      po_browser_protocol             OUT      VARCHAR2,
      po_unscanned                    OUT      NUMBER,
      po_equip_status_dt              OUT      DATE,
      po_puk                          OUT      VARCHAR2,
      po_product_category_id          OUT      NUMBER,
      po_virtual                      OUT      NUMBER,
      po_rim_pin                      OUT      VARCHAR2,
      po_brands                       OUT      brand_array,
	  po_assohandsetimei_for_usim	  OUT	   VARCHAR2,
	  po_local_imsi					  OUT	   VARCHAR2,
	  po_remote_imsi				  OUT	   VARCHAR2,
	  po_assignable					  OUT	   NUMBER,	
	  po_previously_activated		  OUT      NUMBER,
      po_equipment_group			  OUT      VARCHAR2,
      po_last_event_type              OUT      VARCHAR2,
      po_source_network_type_cd       OUT      VARCHAR2,
      po_pre_post_paid_flag           OUT      VARCHAR2
   )
   IS
      CURSOR c_equipment_pcs
      IS
         SELECT   p.product_id, p.product_cd,
                  NVL (p.product_status_cd, ' ') product_status_cd,
                  p.technology_type, p.english_product_name,
                  p.french_product_name, pe.subsidy_lock_number,
                  pe.provider_owner_id, pe.master_lock_number,
                  pe.browser_version, pe.firmware_version, pe.mdn_cd,
                  pe.customer_id, pes.equipment_status_type_id,
                  pes.equipment_status_id, pes.equipment_status_dt,
                  pc.product_class_id, pc.product_class_cd,
                  pc.product_class_des, prc.prl_cd, prc.prl_des, bv.protocol, pc_xref_kb.source_network_type_cd
             FROM pcs_equipment pe,
                  product p,
                  pcs_equip_status pes,
                  product_classification pc,
                  esn_prl ep,
                  prl_code prc,
                  browser_version bv,
                  product_class_xref_kb pc_xref_kb
            WHERE pe.serial_no = pi_serial_no
              AND pe.seq_no = 0
              AND pe.product_id = p.product_id
              AND pe.serial_no = pes.serial_no
              AND pe.seq_no = pes.seq_no
              AND TRUNC (pes.equipment_status_dt) <= TRUNC (SYSDATE)
              AND pc.product_class_id = p.product_class_id
              AND pc_xref_kb.technology_type = p.technology_type
              AND pc_xref_kb.product_class_id = pc.product_class_id
              AND ep.serial_no(+) = pe.serial_no
              AND ep.seq_no(+) = pe.seq_no
              AND (ep.status = 'A' OR ep.status IS NULL)
              AND (   ep.effective_dt =
                         (SELECT MAX (effective_dt)
                            FROM esn_prl ep1
                           WHERE ep1.serial_no = ep.serial_no
                             AND ep1.seq_no = ep.seq_no
                             AND ep1.status = 'A')
                   OR ep.effective_dt IS NULL
                  )
              AND prc.prl_cd(+) = ep.prl_cd
              AND pe.browser_version = bv.browser_version(+)
         ORDER BY pes.equipment_status_dt DESC;

      pcs_rec                   c_equipment_pcs%ROWTYPE;
      v_product_status_cd       VARCHAR2 (2);
      v_vendor_no               VARCHAR2 (18)                           := '0';
      v_activated_status        VARCHAR2 (1)                            := '0';
      v_master_lock             VARCHAR2 (30);
	  v_imei_count                NUMBER (2)    := 0;
	  v_lost_ind_1	BOOLEAN:=FALSE;
	  v_lost_ind_2	BOOLEAN:=FALSE;
	  
      CURSOR c_pcs_equip_status
      IS
         SELECT   equipment_status_type_id, equipment_status_id
             FROM pcs_equip_status
            WHERE serial_no = pi_serial_no
              AND seq_no = 0
              AND TRUNC (equipment_status_dt) <= TRUNC (SYSDATE)
         ORDER BY equipment_status_dt DESC;

      v_initial_activation      CHAR (1)                                := 'Y';

      CURSOR c_mode (pi_product_id NUMBER)
      IS
         SELECT ci_gp.sub_type_id product_type_id,
                ci_gp.catalogue_item_des product_type_des
           FROM catalogue_item ci_p,
                catalogue_item ci_g,
                ci_relationship cr,
                ci_relationship crg,
                catalogue_item ci_gp
          WHERE ci_p.sub_type_id = pi_product_id
            AND ci_p.catalogue_item_type_cd = 'PRODUCT'
            AND ci_g.sub_type_id = 10002686
            AND ci_g.catalogue_item_type_cd = 'GROUP'
            AND cr.catalogue_parent_item_id = ci_g.catalogue_item_id
            AND cr.catalogue_child_item_id = ci_gp.catalogue_item_id
            AND cr.relationship_type_cd = 'GENERICGRP'
            AND ci_gp.catalogue_item_type_cd = 'GROUP'
            AND crg.catalogue_parent_item_id = ci_gp.catalogue_item_id
            AND crg.catalogue_child_item_id = ci_p.catalogue_item_id
            AND crg.relationship_type_cd = 'GENERICGRP';

--
--v_mode       number(22);
--v_mode_description varchar(100);
      CURSOR c_equipment_mike
      IS
         SELECT   p.product_id, p.product_cd,
                  NVL (p.product_status_cd, ' ') product_status_cd,
                  p.technology_type, p.english_product_name,
                  p.french_product_name, ie.provider_owner_id,
                  ie.browser_version, ie.firmware_version, ie.min_cd,
                  ie.customer_id, ies.equipment_status_type_id,
                  ies.equipment_status_id, ies.equipment_status_dt,
                  pc.product_class_id, pc.product_class_cd,
                  pc.product_class_des, bv.protocol ,pc_xref_kb.source_network_type_cd
             FROM iden_equipment ie,
                  product p,
                  iden_equip_status ies,
                  product_classification pc,
                  browser_version bv,
                  product_class_xref_kb pc_xref_kb
            WHERE ie.serial_no = pi_serial_no
              AND ie.seq_no = 0
              AND ie.product_id = p.product_id
              AND ie.serial_no = ies.serial_no
              AND ie.seq_no = ies.seq_no
              AND TRUNC (ies.equipment_status_dt) <= TRUNC (SYSDATE)
              AND pc.product_class_id = p.product_class_id
              AND pc_xref_kb.technology_type = p.technology_type
              AND pc_xref_kb.product_class_id = pc.product_class_id
              AND ie.browser_version = bv.browser_version(+)
         ORDER BY equipment_status_dt DESC;

      mike_rec                  c_equipment_mike%ROWTYPE;

      CURSOR c_iden_equip_status
      IS
         SELECT   equipment_status_type_id, equipment_status_id
             FROM iden_equip_status
            WHERE serial_no = pi_serial_no
              AND seq_no = 0
              AND TRUNC (equipment_status_dt) <= TRUNC (SYSDATE)
         ORDER BY equipment_status_dt DESC;

      CURSOR c_sim_imei
      IS
      	SELECT imei_serial_no
      	FROM sim_imei
      	WHERE imei_serial_no = pi_serial_no ;

      sim_imei_rec  c_sim_imei%ROWTYPE;

----------------------------
--UIM
----------------------------
      CURSOR c_equipment_uim
      IS
         SELECT   p.product_id, p.product_cd,
                  NVL (p.product_status_cd, ' ') product_status_cd,
                  p.technology_type, p.english_product_name,
                  p.french_product_name, ss.equipment_status_type_id,
                  ss.equipment_status_id, ss.equipment_status_dt, s.puk1,
                  pc.product_class_id, pc.product_class_cd,
                  pc.product_class_des,pc_xref_kb.source_network_type_cd
             FROM uim s, product p, uim_status ss, product_classification pc, product_class_xref_kb pc_xref_kb
            WHERE s.iccid = pi_serial_no
              AND s.seq_no = 0
              AND p.product_id = s.product_id
              AND ss.iccid = s.iccid
              AND ss.seq_no = s.seq_no
              AND TRUNC (ss.equipment_status_dt) <= TRUNC (SYSDATE)
              AND pc.product_class_id = p.product_class_id
              AND pc_xref_kb.technology_type = p.technology_type
              AND pc_xref_kb.product_class_id = pc.product_class_id
         ORDER BY ss.equipment_status_dt DESC;

      uim_rec                   c_equipment_uim%ROWTYPE;

      CURSOR c_uim_equip_status
      IS
         SELECT   equipment_status_type_id, equipment_status_id,
                  equipment_status_dt
             FROM uim_status
            WHERE iccid = pi_serial_no
              AND seq_no = 0
              AND TRUNC (equipment_status_dt) <= TRUNC (SYSDATE)
         ORDER BY equipment_status_dt DESC;

-----------------------------
--SIM
-----------------------------
      CURSOR c_equipment_sim
      IS
         SELECT   p.product_id, p.product_cd,
                  NVL (p.product_status_cd, ' ') product_status_cd,
                  p.technology_type, p.english_product_name,
                  p.french_product_name, ss.equipment_status_type_id,
                  ss.equipment_status_id, ss.equipment_status_dt, s.puk,
                  pc.product_class_id, pc.product_class_cd,
                  pc.product_class_des, pc_xref_kb.source_network_type_cd
             FROM sim s, product p, sim_status ss, product_classification pc,product_class_xref_kb pc_xref_kb
            WHERE s.sim_id = pi_serial_no
              AND s.seq_no = 0
              AND p.product_id = s.product_id
              AND ss.sim_id = s.sim_id
              AND ss.seq_no = s.seq_no
              AND TRUNC (ss.equipment_status_dt) <= TRUNC (SYSDATE)
              AND pc.product_class_id = p.product_class_id
              AND pc_xref_kb.technology_type = p.technology_type
              AND pc_xref_kb.product_class_id = pc.product_class_id
         ORDER BY ss.equipment_status_dt DESC;

      sim_rec                   c_equipment_sim%ROWTYPE;

      CURSOR c_sim_equip_status
      IS
         SELECT   equipment_status_type_id, equipment_status_id,
                  equipment_status_dt
             FROM sim_status
            WHERE sim_id = pi_serial_no
              AND seq_no = 0
              AND TRUNC (equipment_status_dt) <= TRUNC (SYSDATE)
         ORDER BY equipment_status_dt DESC;

  --VIRTUAL
----------------------------
      CURSOR c_equipment_virtual
      IS
         SELECT p.product_cd
           FROM virtual_equipment ve, product p 
          WHERE ve.serial_no = pi_serial_no AND p.product_id = ve.product_id;
          

      virtual_rec               c_equipment_virtual%ROWTYPE;

---------------------
--Analog equip
----------------
      CURSOR c_equip_analog
      IS
         SELECT   p.product_id, p.product_cd,
                  NVL (p.product_status_cd, ' ') product_status_cd,
                  p.technology_type, p.english_product_name,
                  p.french_product_name, ae.mdn_cd, ae.customer_id,
                  ae.provider_owner_id, aes.equipment_status_type_id,
                  aes.equipment_status_id, aes.equipment_status_dt,
                  pc.product_class_id, pc.product_class_cd,
                  pc.product_class_des,pc_xref_kb.source_network_type_cd
             FROM analog_equip ae,
                  product p,
                  analog_equip_status aes,
                  product_classification pc,
                  product_class_xref_kb pc_xref_kb
            WHERE ae.serial_no = pi_serial_no
              AND ae.seq_no = 0
              AND ae.product_id = p.product_id
              AND ae.serial_no = aes.serial_no(+)
              AND ae.seq_no = aes.seq_no(+)
              AND aes.equipment_status_dt(+) <= SYSDATE
              AND pc.product_class_id = p.product_class_id
              AND pc_xref_kb.technology_type = p.technology_type
          	  AND pc_xref_kb.product_class_id = pc.product_class_id
         ORDER BY aes.equipment_status_dt DESC;

      analog_rec                c_equip_analog%ROWTYPE;

      CURSOR c_analog_equip_status
      IS
         SELECT   equipment_status_type_id, equipment_status_id
             FROM analog_equip_status
            WHERE serial_no = pi_serial_no
              AND seq_no = 0
              AND TRUNC (equipment_status_dt) <= TRUNC (SYSDATE)
         ORDER BY equipment_status_dt DESC;

---------------
-- Pager for K2
---------------
      CURSOR c_equip_pager
      IS
         SELECT   p.product_id, p.product_cd,
                  NVL (p.product_status_cd, ' ') product_status_cd,
                  p.technology_type, p.english_product_name,
                  p.french_product_name, pe.min_cd, pe.customer_id,
                  pe.provider_owner_id, pes.equipment_status_type_id,
                  pes.equipment_status_id, pes.equipment_status_dt,
                  pc.product_class_id, pc.product_class_cd,
                  pc.product_class_des, pe.cap_code, pp.encoder_format_cd,
                  pp.paging_product_id, pe.frequency_id, pf.frequency_cd,pc_xref_kb.source_network_type_cd
             FROM paging_equip pe,
                  product p,
                  paging_equip_status pes,
                  product_classification pc,
                  paging_product pp,
                  paging_frequency pf,
                  product_class_xref_kb pc_xref_kb
            WHERE pe.serial_no = pi_serial_no
              AND pe.seq_no = 0
              AND pe.product_id = p.product_id
              AND pe.serial_no = pes.serial_no(+)
              AND pe.seq_no = pes.seq_no(+)
              AND pes.equipment_status_dt(+) <= SYSDATE
              AND pc.product_class_id = p.product_class_id
              AND pp.paging_product_id = pe.paging_product_id
              AND pe.frequency_id = pf.frequency_id
              AND pc_xref_kb.technology_type = p.technology_type
              AND pc_xref_kb.product_class_id = pc.product_class_id
         ORDER BY pes.equipment_status_dt DESC;

--
      pager_rec                 c_equip_pager%ROWTYPE;
      v_paging_product_id       NUMBER (22);

--
      CURSOR c_paging_equip_status
      IS
         SELECT   equipment_status_type_id, equipment_status_id
             FROM paging_equip_status
            WHERE serial_no = pi_serial_no
              AND seq_no = 0
              AND TRUNC (equipment_status_dt) <= TRUNC (SYSDATE)
         ORDER BY equipment_status_dt DESC;

--
      CURSOR c_paging_ownership
      IS
         SELECT   DECODE (equipment_status_id,
                          140, 'R',
                          'P'
                         )
             FROM paging_equip_status
            WHERE serial_no = pi_serial_no
              AND seq_no = 0
              AND TRUNC (equipment_status_dt) <= TRUNC (SYSDATE)
              AND equipment_status_type_id = 12
         ORDER BY equipment_status_dt DESC;

--
      CURSOR c_paging_prepaid (pi_product_id NUMBER)
      IS
         SELECT 'Y'
           FROM product_feature
          WHERE product_id = pi_product_id AND feature_id = 10002392;

--
      CURSOR c_paging_coverage_region_list (pi_frequency_id NUMBER)
      IS
         SELECT   coverage_region_id
             FROM coverage_region
            WHERE frequency_id = pi_frequency_id
              AND province_cd IN ('BC', 'AB')
              AND coverage_type IS NOT NULL
         ORDER BY coverage_region_id DESC;

--
      v_coverage_region_id      coverage_region.coverage_region_id%TYPE;
--
      v_product_rec             product_info_record;

---------------
-- USIM
---------------
	  CURSOR c_equipment_usim
      IS
	  
         SELECT   p.product_id, p.product_cd,
                  NVL (p.product_status_cd, ' ') product_status_cd,
                  p.technology_type, p.english_product_name,
                  p.french_product_name, pe.customer_id,
				  pes.equipment_status_type_id,
                  pes.equipment_status_id, pes.equipment_status_dt,
                  pc.product_class_id, pc.product_class_cd,
                  pc.product_class_des, c.puk, p.pre_post_paid_flag, pc_xref_kb.source_network_type_cd
             FROM usim pe,
			   product p,
                 usim_status pes,
                  product_classification pc,
				  usim_credential c,
				 product_class_xref_kb pc_xref_kb
            WHERE pe.usim_id = pi_serial_no
              AND pe.seq_no = 0
              AND pe.product_id = p.product_id
              AND pe.usim_id = pes.usim_id
              AND pe.seq_no = pes.seq_no
              AND TRUNC (pes.equipment_status_dt) <= TRUNC (SYSDATE)
              AND pc.product_class_id = p.product_class_id
			  AND c.usim_seq_no=0
			  AND c.usim_id=pe.usim_id
			  AND c.usim_credential_purpose_id=1
			  AND pc_xref_kb.technology_type = p.technology_type
              AND pc_xref_kb.product_class_id = pc.product_class_id
         ORDER BY pes.equipment_status_dt DESC;
		 
	  usim_rec                   c_equipment_usim%ROWTYPE;

      CURSOR c_usim_status
      IS
	   
         SELECT   equipment_status_type_id, equipment_status_id,
                  equipment_status_dt
             FROM usim_status
            WHERE usim_id = pi_serial_no
              AND seq_no = 0
              AND TRUNC (equipment_status_dt) <= TRUNC (SYSDATE)
         ORDER BY equipment_status_dt DESC;
		 

---------------------
--Cursor to select Product Service Initiatives for given product
      CURSOR c_promo (pi_product_id NUMBER)
      IS
         SELECT ci_gp.sub_type_id product_type_id
           FROM catalogue_item ci_p,
                catalogue_item ci_g,
                ci_relationship cr,
                ci_relationship crg,
                catalogue_item ci_gp
          WHERE ci_p.sub_type_id = pi_product_id           ---example 10001165
            AND ci_p.catalogue_item_type_cd = 'PRODUCT'
            AND ci_g.sub_type_id = 10005555
            AND ci_g.catalogue_item_type_cd = 'GROUP'
            AND cr.catalogue_parent_item_id = ci_g.catalogue_item_id
            AND cr.catalogue_child_item_id = ci_gp.catalogue_item_id
            AND cr.relationship_type_cd = 'GENERICGRP'
            AND ci_gp.catalogue_item_type_cd = 'GROUP'
            AND crg.catalogue_parent_item_id = ci_gp.catalogue_item_id
            AND crg.catalogue_child_item_id = ci_p.catalogue_item_id
            AND crg.relationship_type_cd = 'GENERICGRP';

      v_product_promo_type_id   NUMBER (22);
      v_product_id              NUMBER (22);


---------------------
   BEGIN
      po_virtual := 0;
      po_rim_pin := getrimpin (pi_serial_no);
	  po_assignable :=0;
	  po_previously_activated := 0;

      IF pi_initial_activation
      THEN
         v_initial_activation := 'Y';
      ELSE
         v_initial_activation := 'N';
      END IF;

      IF pi_tech_type_class = 'PCS'
      THEN
         OPEN c_equipment_pcs;

         FETCH c_equipment_pcs
          INTO pcs_rec;

         IF c_equipment_pcs%NOTFOUND
         THEN
            RAISE esnnotfound;
         END IF;

         v_product_id := pcs_rec.product_id;
         po_product_cd := pcs_rec.product_cd;
         po_product_status_cd := pcs_rec.product_status_cd;
         v_product_status_cd := pcs_rec.product_status_cd;
         po_tech_type := pcs_rec.technology_type;
         po_provider_owner_id := pcs_rec.provider_owner_id;
         po_equipment_status_type_id := pcs_rec.equipment_status_type_id;
         po_equipment_status_id := pcs_rec.equipment_status_id;
         po_equip_status_dt := pcs_rec.equipment_status_dt;
         po_sublock1 := pcs_rec.subsidy_lock_number;
         v_master_lock := pcs_rec.master_lock_number;
         po_product_class_id := pcs_rec.product_class_id;
         po_product_class_cd := pcs_rec.product_class_cd;
         po_product_class_des := pcs_rec.product_class_des;
         po_english_product_name := pcs_rec.english_product_name;
         po_french_product_name := pcs_rec.french_product_name;
         po_browser_version := pcs_rec.browser_version;
         po_firmware_version := pcs_rec.firmware_version;
         po_prl_cd := pcs_rec.prl_cd;
         po_prl_des := pcs_rec.prl_des;
         po_min_cd := pcs_rec.mdn_cd;
         po_customer_id := pcs_rec.customer_id;
         po_browser_protocol := pcs_rec.protocol;
         po_stolen := 0;
         po_source_network_type_cd := pcs_rec.source_network_type_cd;
         getbrands (po_product_cd, po_brands);

         IF    (    pcs_rec.equipment_status_type_id = 1
                AND pcs_rec.equipment_status_id = 6
               )
            OR (    pcs_rec.equipment_status_type_id = 3
                AND pcs_rec.equipment_status_id = 11
               )
            OR (    pcs_rec.equipment_status_type_id = 3
                AND pcs_rec.equipment_status_id = 56
               )
            OR (    pcs_rec.equipment_status_type_id = 3
                AND pcs_rec.equipment_status_id = 57
               )
         THEN
            po_stolen := 1;
         ELSE
            FOR equip_rec IN c_pcs_equip_status
            LOOP
               EXIT WHEN c_equipment_pcs%NOTFOUND;

               IF (   equip_rec.equipment_status_type_id = 1
                   OR equip_rec.equipment_status_type_id = 3
                  )
               THEN
                  IF    (    equip_rec.equipment_status_type_id = 1
                         AND equip_rec.equipment_status_id = 6
                        )
                     OR (    equip_rec.equipment_status_type_id = 3
                         AND equip_rec.equipment_status_id = 11
                        )
                     OR (    equip_rec.equipment_status_type_id = 3
                         AND equip_rec.equipment_status_id = 56
                        )
                     OR (    equip_rec.equipment_status_type_id = 3
                         AND equip_rec.equipment_status_id = 57
                        )
                  THEN
                     po_stolen := 1;
                  END IF;

                  EXIT;
               END IF;
            END LOOP;
         END IF;

         IF (    pcs_rec.equipment_status_type_id = 2
             AND pcs_rec.equipment_status_id = 8
            )
         THEN
            v_activated_status := 'A';
            v_initial_activation := 'N';
         END IF;

         IF v_initial_activation = 'Y'
         THEN
            FOR equip_rec IN c_pcs_equip_status
            LOOP
               EXIT WHEN c_equipment_pcs%NOTFOUND;

               IF (    equip_rec.equipment_status_type_id = 2
                   AND equip_rec.equipment_status_id = 8
                  )
               THEN
                  v_initial_activation := 'N';
                  EXIT;
               ELSIF (    equip_rec.equipment_status_type_id = 1
                      AND equip_rec.equipment_status_id = 3
                     )
               THEN
                  v_initial_activation := 'Y';
                  EXIT;
               END IF;
            END LOOP;

            po_initial_activation := v_initial_activation;
         END IF;

         -- Get Promo Product Types
         OPEN c_promo (pcs_rec.product_id);

         LOOP
            FETCH c_promo
             INTO v_product_promo_type_id;

            EXIT WHEN c_promo%NOTFOUND;
            po_product_type_list :=
                              po_product_type_list || v_product_promo_type_id;
         END LOOP;

         CLOSE c_promo;

         OPEN c_mode (pcs_rec.product_id);

         FETCH c_mode
          INTO po_mode_code, po_mode_description;

         CLOSE c_mode;

         --
         getfirmwareversionfeature (pcs_rec.firmware_version,
                                    po_firmware_feature_code_list);
      --
      ELSIF pi_tech_type_class = 'MIKE'
      THEN
         OPEN c_equipment_mike;

         FETCH c_equipment_mike
          INTO mike_rec;

         IF c_equipment_mike%NOTFOUND
         THEN
            RAISE esnnotfound;
         END IF;

         v_product_id := mike_rec.product_id;
         po_product_cd := mike_rec.product_cd;
         po_product_status_cd := mike_rec.product_status_cd;
         po_tech_type := mike_rec.technology_type;
         po_provider_owner_id := mike_rec.provider_owner_id;
         po_equipment_status_type_id := mike_rec.equipment_status_type_id;
         po_equipment_status_id := mike_rec.equipment_status_id;
         po_equip_status_dt := mike_rec.equipment_status_dt;
         po_product_class_id := mike_rec.product_class_id;
         po_product_class_cd := mike_rec.product_class_cd;
         po_product_class_des := mike_rec.product_class_des;
         po_english_product_name := mike_rec.english_product_name;
         po_french_product_name := mike_rec.french_product_name;
         po_browser_version := mike_rec.browser_version;
         po_firmware_version := mike_rec.firmware_version;
         po_min_cd := mike_rec.min_cd;
         po_customer_id := mike_rec.customer_id;
         po_browser_protocol := mike_rec.protocol;
         po_stolen := 0;
         po_source_network_type_cd := mike_rec.source_network_type_cd;
         getbrands (po_product_cd, po_brands);

         IF        mike_rec.equipment_status_type_id = 1
               AND mike_rec.equipment_status_id = 6
            OR (    mike_rec.equipment_status_type_id = 3
                AND mike_rec.equipment_status_id = 11
               )
            OR (    mike_rec.equipment_status_type_id = 3
                AND mike_rec.equipment_status_id = 56
               )
            OR (    mike_rec.equipment_status_type_id = 3
                AND mike_rec.equipment_status_id = 57
               )
         THEN
            po_stolen := 1;
         ELSE
            FOR equip_rec IN c_iden_equip_status
            LOOP
               EXIT WHEN c_iden_equip_status%NOTFOUND;

               IF (   equip_rec.equipment_status_type_id = 1
                   OR equip_rec.equipment_status_type_id = 3
                  )
               THEN
                  IF    (    equip_rec.equipment_status_type_id = 1
                         AND equip_rec.equipment_status_id = 6
                        )
                     OR (    equip_rec.equipment_status_type_id = 3
                         AND equip_rec.equipment_status_id = 11
                        )
                     OR (    equip_rec.equipment_status_type_id = 3
                         AND equip_rec.equipment_status_id = 56
                        )
                     OR (    equip_rec.equipment_status_type_id = 3
                         AND equip_rec.equipment_status_id = 57
                        )
                  THEN
                     po_stolen := 1;
                  END IF;

                  EXIT;
               END IF;
            END LOOP;
         END IF;

         IF (    mike_rec.equipment_status_type_id = 2
             AND mike_rec.equipment_status_id = 8
            )
         THEN
            po_initial_activation := 'N';
            v_initial_activation := 'N';
         END IF;

         IF v_initial_activation = 'Y'
         THEN
            FOR equip_rec IN c_iden_equip_status
            LOOP
               EXIT WHEN c_iden_equip_status%NOTFOUND;

               IF (    equip_rec.equipment_status_type_id = 2
                   AND equip_rec.equipment_status_id = 8
                  )
               THEN
                  v_initial_activation := 'N';
                  EXIT;
               ELSIF (    equip_rec.equipment_status_type_id = 1
                      AND equip_rec.equipment_status_id = 3
                     )
               THEN
                  v_initial_activation := 'Y';
                  EXIT;
               END IF;
            END LOOP;

						IF v_initial_activation = 'Y'
						THEN
							OPEN c_sim_imei;
							FETCH C_sim_imei
							INTO sim_imei_rec;

							v_imei_count := c_sim_imei%ROWCOUNT;
         			IF  v_imei_count = 0
         			THEN
            		v_initial_activation := 'Y';
            	ELSE
            		v_initial_activation := 'N';
         			END IF;
         			CLOSE c_sim_imei;
         		END IF;

            IF v_initial_activation = 'O'
            THEN
               po_initial_activation := 'Y';
            ELSE
               po_initial_activation := v_initial_activation;
            END IF;
         END IF;

              --
         -- Get Promo Product Types
              --
         OPEN c_promo (pcs_rec.product_id);

         LOOP
            FETCH c_promo
             INTO v_product_promo_type_id;

            EXIT WHEN c_promo%NOTFOUND;
            po_product_type_list :=
                              po_product_type_list || v_product_promo_type_id;
         END LOOP;

         CLOSE c_promo;

         --
         getfirmwareversionfeature (mike_rec.firmware_version,
                                    po_firmware_feature_code_list);
      --
      ELSIF pi_tech_type_class = 'VIRTUAL'
      THEN
         OPEN c_equipment_virtual;

         FETCH c_equipment_virtual
          INTO virtual_rec.product_cd;

         IF c_equipment_virtual%NOTFOUND
         THEN
            RAISE esnnotfound;
         END IF;

         CLOSE c_equipment_virtual;

         getequipmentinfobyproductcode (virtual_rec.product_cd,
                                        po_product_cd,
                                        v_product_id,
                                        po_product_category_id,
                                        po_vendor_name,
                                        po_vendor_no,
                                        po_product_gp_type_id,
                                        po_product_gp_type_cd,
                                        po_product_gp_type_des,
                                        po_product_gp_type_des_f,
                                        po_product_type_id,
                                        po_product_type_des,
                                        po_product_type_des_f,
                                        po_product_type,
                                        po_equipment_type,
                                        po_equipment_type_class,
                                        po_tech_type,
                                        po_english_product_name,
                                        po_french_product_name,
                                        po_product_class_id,
                                        po_product_status_cd,
                                        po_product_class_cd,
                                        po_product_class_des,
                                        po_mode_code,
                                        po_mode_description,
                                        po_product_type_list,
                                        po_cross_fleet,
                                        po_equipment_status_type_id,
                                        po_equipment_status_id,
                                        po_equip_status_dt,
                                        po_browser_version,
                                        po_firmware_version,
                                        po_prl_cd,
                                        po_prl_des,
                                        po_browser_protocol,
                                        po_firmware_feature_code_list,
                                        po_brands,
									    po_equipment_group,
									    po_source_network_type_cd
                                       );
         po_virtual := 1;
      --
      ELSIF pi_tech_type_class = 'ANALOG'
      THEN
         OPEN c_equip_analog;

         FETCH c_equip_analog
          INTO analog_rec;

         IF c_equip_analog%NOTFOUND
         THEN
            RAISE esnnotfound;
         END IF;

         v_product_id := analog_rec.product_id;
         po_product_cd := analog_rec.product_cd;
         po_product_status_cd := analog_rec.product_status_cd;
         po_tech_type := analog_rec.technology_type;
         po_provider_owner_id := analog_rec.provider_owner_id;
         po_equipment_status_type_id := analog_rec.equipment_status_type_id;
         po_equipment_status_id := analog_rec.equipment_status_id;
         po_equip_status_dt := analog_rec.equipment_status_dt;
         po_sublock1 := ' ';
         po_product_class_id := analog_rec.product_class_id;
         po_product_class_cd := analog_rec.product_class_cd;
         po_product_class_des := analog_rec.product_class_des;
         po_english_product_name := analog_rec.english_product_name;
         po_french_product_name := analog_rec.french_product_name;
         po_min_cd := analog_rec.mdn_cd;
         po_customer_id := analog_rec.customer_id;
         po_stolen := 0;
         po_source_network_type_cd := analog_rec.source_network_type_cd;
         getbrands (po_product_cd, po_brands);

         IF        analog_rec.equipment_status_type_id = 1
               AND analog_rec.equipment_status_id = 6
            OR (    analog_rec.equipment_status_type_id = 3
                AND analog_rec.equipment_status_id = 11
               )
            OR (    analog_rec.equipment_status_type_id = 3
                AND analog_rec.equipment_status_id = 56
               )
            OR (    analog_rec.equipment_status_type_id = 3
                AND analog_rec.equipment_status_id = 57
               )
         THEN
            po_stolen := 1;
         ELSE
            FOR equip_rec IN c_analog_equip_status
            LOOP
               EXIT WHEN c_analog_equip_status%NOTFOUND;

               IF (   equip_rec.equipment_status_type_id = 1
                   OR equip_rec.equipment_status_type_id = 3
                  )
               THEN
                  IF    (    equip_rec.equipment_status_type_id = 1
                         AND equip_rec.equipment_status_id = 6
                        )
                     OR (    equip_rec.equipment_status_type_id = 3
                         AND equip_rec.equipment_status_id = 11
                        )
                     OR (    equip_rec.equipment_status_type_id = 3
                         AND equip_rec.equipment_status_id = 56
                        )
                     OR (    equip_rec.equipment_status_type_id = 3
                         AND equip_rec.equipment_status_id = 57
                        )
                  THEN
                     po_stolen := 1;
                  END IF;

                  EXIT;
               END IF;
            END LOOP;

            IF (    analog_rec.equipment_status_type_id = 2
                AND analog_rec.equipment_status_id = 8
               )
            THEN
               po_initial_activation := 'N';
               v_initial_activation := 'N';
            END IF;

            IF v_initial_activation = 'Y'
            THEN
               FOR equip_rec IN c_analog_equip_status
               LOOP
                  EXIT WHEN c_analog_equip_status%NOTFOUND;

                  IF (    equip_rec.equipment_status_type_id = 2
                      AND equip_rec.equipment_status_id = 8
                     )
                  THEN
                     v_initial_activation := 'N';
                     EXIT;
                  ELSIF (    equip_rec.equipment_status_type_id = 1
                         AND equip_rec.equipment_status_id = 3
                        )
                  THEN
                     v_initial_activation := 'Y';
                     EXIT;
                  END IF;
               END LOOP;

               IF v_initial_activation = 'O'
               THEN
                  po_initial_activation := 'Y';
               ELSE
                  po_initial_activation := v_initial_activation;
               END IF;
            END IF;
         END IF;
----------------
-- Pager for K2
----------------
      ELSIF pi_tech_type_class = 'PAGER'
      THEN
         OPEN c_equip_pager;

         FETCH c_equip_pager
          INTO pager_rec;

         IF c_equip_pager%NOTFOUND
         THEN
            RAISE esnnotfound;
         END IF;

         v_product_id := pager_rec.product_id;
         v_paging_product_id := pager_rec.paging_product_id;
         po_product_cd := pager_rec.product_cd;
         po_product_status_cd := pager_rec.product_status_cd;
         po_tech_type := pager_rec.technology_type;
         po_provider_owner_id := pager_rec.provider_owner_id;
         po_equipment_status_type_id := pager_rec.equipment_status_type_id;
         po_equipment_status_id := pager_rec.equipment_status_id;
         po_equip_status_dt := pager_rec.equipment_status_dt;
         po_sublock1 := ' ';
         po_lastmuleimei_for_sim := ' ';
         po_browser_version := ' ';
         po_firmware_version := ' ';
         po_browser_protocol := ' ';
         po_prl_cd := ' ';
         po_prl_des := ' ';
         po_product_class_id := pager_rec.product_class_id;
         po_product_class_cd := pager_rec.product_class_cd;
         po_product_class_des := pager_rec.product_class_des;
         po_english_product_name := pager_rec.english_product_name;
         po_french_product_name := pager_rec.french_product_name;
         po_min_cd := pager_rec.min_cd;
         po_customer_id := pager_rec.customer_id;
         po_cap_code := pager_rec.cap_code;
         po_encoding_format_code := pager_rec.encoder_format_cd;
         po_frequency_cd := pager_rec.frequency_cd;
         po_stolen := 0;
         po_source_network_type_cd := pager_rec.source_network_type_cd;
         getbrands (po_product_cd, po_brands);

         IF        pager_rec.equipment_status_type_id = 1
               AND pager_rec.equipment_status_id = 6
            OR (    pager_rec.equipment_status_type_id = 11
                AND pager_rec.equipment_status_id = 127
               )
            OR (    pager_rec.equipment_status_type_id = 3
                AND pager_rec.equipment_status_id = 11
               )
            OR (    pager_rec.equipment_status_type_id = 3
                AND pager_rec.equipment_status_id = 56
               )
            OR (    pager_rec.equipment_status_type_id = 3
                AND pager_rec.equipment_status_id = 57
               )
         THEN
            po_stolen := 1;
         ELSE
            FOR equip_rec IN c_paging_equip_status
            LOOP
               EXIT WHEN c_paging_equip_status%NOTFOUND;

               IF (   equip_rec.equipment_status_type_id = 1
                   OR equip_rec.equipment_status_type_id = 3
                   OR equip_rec.equipment_status_type_id = 11
                  )
               THEN
                  IF    (    equip_rec.equipment_status_type_id = 1
                         AND equip_rec.equipment_status_id = 6
                        )
                     OR (    equip_rec.equipment_status_type_id = 11
                         AND equip_rec.equipment_status_id = 127
                        )
                     OR (    equip_rec.equipment_status_type_id = 3
                         AND equip_rec.equipment_status_id = 11
                        )
                     OR (    equip_rec.equipment_status_type_id = 3
                         AND equip_rec.equipment_status_id = 56
                        )
                     OR (    equip_rec.equipment_status_type_id = 3
                         AND equip_rec.equipment_status_id = 57
                        )
                  THEN
                     po_stolen := 1;
                  END IF;

                  EXIT;
               END IF;
            END LOOP;
         END IF;

         IF (    pager_rec.equipment_status_type_id = 2
             AND pager_rec.equipment_status_id = 8
            )
         THEN
            po_initial_activation := 'N';
            v_initial_activation := 'N';
         END IF;

         IF v_initial_activation = 'Y'
         THEN
            FOR equip_rec IN c_paging_equip_status
            LOOP
               EXIT WHEN c_paging_equip_status%NOTFOUND;

               IF (    equip_rec.equipment_status_type_id = 2
                   AND equip_rec.equipment_status_id = 8
                  )
               THEN
                  v_initial_activation := 'N';
                  EXIT;
               ELSIF (    equip_rec.equipment_status_type_id = 1
                      AND equip_rec.equipment_status_id = 3
                     )
               THEN
                  v_initial_activation := 'Y';
                  EXIT;
               END IF;
            END LOOP;

            IF v_initial_activation = 'O'
            THEN
               po_initial_activation := 'Y';
            ELSE
               po_initial_activation := v_initial_activation;
            END IF;
         END IF;

         -- get ownership (Rental R or Private P)
         OPEN c_paging_ownership;

         FETCH c_paging_ownership
          INTO po_ownership_code;

         IF c_paging_ownership%NOTFOUND
         THEN
            po_ownership_code := 'P';
         END IF;

         CLOSE c_paging_ownership;

         -- check prepaid indicator
         OPEN c_paging_prepaid (pager_rec.product_id);

         FETCH c_paging_prepaid
          INTO po_prepaid;

         IF c_paging_prepaid%NOTFOUND
         THEN
            po_prepaid := 'N';
         END IF;

         CLOSE c_paging_prepaid;

         -- get Coverage Region List
         v_coverage_region_id := '';

         OPEN c_paging_coverage_region_list (pager_rec.frequency_id);

         LOOP
            FETCH c_paging_coverage_region_list
             INTO v_coverage_region_id;

            EXIT WHEN c_paging_coverage_region_list%NOTFOUND;
            po_coverage_region_code_list :=
                  po_coverage_region_code_list || v_coverage_region_id || '|';
         END LOOP;

         po_coverage_region_code_list :=
            SUBSTR (po_coverage_region_code_list,
                    1,
                    INSTR (po_coverage_region_code_list,
                           '|',
                           -1
                          ) - 1
                   );

         CLOSE c_paging_coverage_region_list;
--
      ELSIF pi_tech_type_class = 'UIM'
      THEN
         OPEN c_equipment_uim;

         FETCH c_equipment_uim
          INTO uim_rec;

         IF c_equipment_uim%NOTFOUND
         THEN
            RAISE esnnotfound;
         END IF;

         v_product_id := uim_rec.product_id;
         po_product_cd := uim_rec.product_cd;
         po_product_status_cd := uim_rec.product_status_cd;
         po_tech_type := uim_rec.technology_type;
         po_equipment_status_type_id := uim_rec.equipment_status_type_id;
         po_equipment_status_id := uim_rec.equipment_status_id;
         po_equip_status_dt := uim_rec.equipment_status_dt;
         po_puk := uim_rec.puk1;
         po_product_class_id := uim_rec.product_class_id;
         po_product_class_cd := uim_rec.product_class_cd;
         po_product_class_des := uim_rec.product_class_des;
         po_english_product_name := uim_rec.english_product_name;
         po_french_product_name := uim_rec.french_product_name;
         po_source_network_type_cd := uim_rec.source_network_type_cd;
         po_stolen := 0;
         getbrands (po_product_cd, po_brands);

         IF        uim_rec.equipment_status_type_id = 1
               AND uim_rec.equipment_status_id = 6
            OR (    uim_rec.equipment_status_type_id = 3
                AND uim_rec.equipment_status_id = 11
               )
            OR (    uim_rec.equipment_status_type_id = 3
                AND uim_rec.equipment_status_id = 56
               )
            OR (    uim_rec.equipment_status_type_id = 3
                AND uim_rec.equipment_status_id = 57
               )
         THEN
            po_stolen := 1;
         ELSE
            FOR equip_rec IN c_uim_equip_status
            LOOP
               EXIT WHEN c_uim_equip_status%NOTFOUND;

               IF (   equip_rec.equipment_status_type_id = 1
                   OR equip_rec.equipment_status_type_id = 3
                  )
               THEN
                  IF    (    equip_rec.equipment_status_type_id = 1
                         AND equip_rec.equipment_status_id = 6
                        )
                     OR (    equip_rec.equipment_status_type_id = 3
                         AND equip_rec.equipment_status_id = 11
                        )
                     OR (    equip_rec.equipment_status_type_id = 3
                         AND equip_rec.equipment_status_id = 56
                        )
                     OR (    equip_rec.equipment_status_type_id = 3
                         AND equip_rec.equipment_status_id = 57
                        )
                  THEN
                     po_stolen := 1;
                  END IF;

                  EXIT;
               END IF;
            END LOOP;
         END IF;

         IF (    uim_rec.equipment_status_type_id = 2
             AND uim_rec.equipment_status_id = 8
            )
         THEN
            po_initial_activation := 'N';
            v_initial_activation := 'N';
         END IF;

         IF v_initial_activation = 'Y'
         THEN
            FOR equip_rec IN c_uim_equip_status
            LOOP
               EXIT WHEN c_uim_equip_status%NOTFOUND;

               IF (    equip_rec.equipment_status_type_id = 2
                   AND equip_rec.equipment_status_id = 8
                  )
               THEN
                  v_initial_activation := 'N';
                  EXIT;
               ELSIF (    equip_rec.equipment_status_type_id = 1
                      AND equip_rec.equipment_status_id = 3
                     )
               THEN
                  v_initial_activation := 'Y';
                  EXIT;
               END IF;
            END LOOP;

            IF v_initial_activation = 'O'
            THEN
               po_initial_activation := 'Y';
            ELSE
               po_initial_activation := v_initial_activation;
            END IF;
         END IF;
--
      ELSIF pi_tech_type_class = 'SIM'
      THEN
         OPEN c_equipment_sim;

         FETCH c_equipment_sim
          INTO sim_rec;

         IF c_equipment_sim%NOTFOUND
         THEN
            RAISE esnnotfound;
         END IF;

         v_product_id := sim_rec.product_id;
         po_product_cd := sim_rec.product_cd;
         po_product_status_cd := sim_rec.product_status_cd;
         po_tech_type := sim_rec.technology_type;
         po_equipment_status_type_id := sim_rec.equipment_status_type_id;
         po_equipment_status_id := sim_rec.equipment_status_id;
         po_equip_status_dt := sim_rec.equipment_status_dt;
         po_puk := sim_rec.puk;
         po_product_class_id := sim_rec.product_class_id;
         po_product_class_cd := sim_rec.product_class_cd;
         po_product_class_des := sim_rec.product_class_des;
         po_english_product_name := sim_rec.english_product_name;
         po_french_product_name := sim_rec.french_product_name;
         po_source_network_type_cd := sim_rec.source_network_type_cd;
         po_stolen := 0;
         getbrands (po_product_cd, po_brands);

         IF        sim_rec.equipment_status_type_id = 1
               AND sim_rec.equipment_status_id = 6
            OR (    sim_rec.equipment_status_type_id = 3
                AND sim_rec.equipment_status_id = 11
               )
            OR (    sim_rec.equipment_status_type_id = 3
                AND sim_rec.equipment_status_id = 56
               )
            OR (    sim_rec.equipment_status_type_id = 3
                AND sim_rec.equipment_status_id = 57
               )
         THEN
            po_stolen := 1;
         ELSE
            FOR equip_rec IN c_sim_equip_status
            LOOP
               EXIT WHEN c_sim_equip_status%NOTFOUND;

               IF (   equip_rec.equipment_status_type_id = 1
                   OR equip_rec.equipment_status_type_id = 3
                  )
               THEN
                  IF    (    equip_rec.equipment_status_type_id = 1
                         AND equip_rec.equipment_status_id = 6
                        )
                     OR (    equip_rec.equipment_status_type_id = 3
                         AND equip_rec.equipment_status_id = 11
                        )
                     OR (    equip_rec.equipment_status_type_id = 3
                         AND equip_rec.equipment_status_id = 56
                        )
                     OR (    equip_rec.equipment_status_type_id = 3
                         AND equip_rec.equipment_status_id = 57
                        )
                  THEN
                     po_stolen := 1;
                  END IF;

                  EXIT;
               END IF;
            END LOOP;
         END IF;

         IF (    sim_rec.equipment_status_type_id = 2
             AND sim_rec.equipment_status_id = 8
            )
         THEN
            po_initial_activation := 'N';
            v_initial_activation := 'N';
         END IF;

         IF v_initial_activation = 'Y'
         THEN
            FOR equip_rec IN c_sim_equip_status
            LOOP
               EXIT WHEN c_sim_equip_status%NOTFOUND;

               IF (    equip_rec.equipment_status_type_id = 2
                   AND equip_rec.equipment_status_id = 8
                  )
               THEN
                  v_initial_activation := 'N';
                  EXIT;
               ELSIF (    equip_rec.equipment_status_type_id = 1
                      AND equip_rec.equipment_status_id = 3
                     )
               THEN
                  v_initial_activation := 'Y';
                  EXIT;
               END IF;
            END LOOP;

            IF v_initial_activation = 'O'
            THEN
               po_initial_activation := 'Y';
            ELSE
               po_initial_activation := v_initial_activation;
            END IF;
         END IF;

         BEGIN
            getimeibysim (pi_serial_no, po_lastmuleimei_for_sim);
         EXCEPTION
            WHEN imeinotfound
            THEN
               po_lastmuleimei_for_sim := NULL;
            WHEN OTHERS
            THEN
               -- po_lastmuleimei_for_sim:=null;
               RAISE;
         -- RAISE_APPLICATION_ERROR(SQLCODE,SQLERRM);
         END;
--      END IF;
----------------------
--USIM
----------------------
	  ELSIF pi_tech_type_class = 'USIM'
      THEN
         OPEN c_equipment_usim;

         FETCH c_equipment_usim
          INTO usim_rec;

         IF c_equipment_usim%NOTFOUND
         THEN
            RAISE esnnotfound;
         END IF;

         v_product_id := usim_rec.product_id;
         po_product_cd := usim_rec.product_cd;
         po_product_status_cd := usim_rec.product_status_cd;
         po_tech_type := usim_rec.technology_type;
         po_equipment_status_type_id := usim_rec.equipment_status_type_id;
         po_equipment_status_id := usim_rec.equipment_status_id;
         po_equip_status_dt := usim_rec.equipment_status_dt;
         po_product_class_id := usim_rec.product_class_id;
         po_product_class_cd := usim_rec.product_class_cd;
         po_product_class_des := usim_rec.product_class_des;
         po_english_product_name := usim_rec.english_product_name;
         po_french_product_name := usim_rec.french_product_name;
         po_stolen := 0;
         po_source_network_type_cd := usim_rec.source_network_type_cd;
		 po_puk := usim_rec.puk;
		 po_pre_post_paid_flag := usim_rec.pre_post_paid_flag;
         getbrands (po_product_cd, po_brands);
		 getprofilebyusimid(pi_serial_no, po_local_imsi, po_remote_imsi);
		 
		IF (po_local_imsi IS NULL)
		THEN
			RAISE imsinotfound;
		END IF;
		IF (usim_rec.equipment_status_type_id = 2 AND usim_rec.equipment_status_id = 8)
		THEN
			po_previously_activated := 1;
		END IF;
        IF 	usim_rec.equipment_status_type_id = 1
               AND usim_rec.equipment_status_id = 6
            OR (    usim_rec.equipment_status_type_id = 3
                AND usim_rec.equipment_status_id = 11
               )
            OR (    usim_rec.equipment_status_type_id = 3
                AND usim_rec.equipment_status_id = 56
               )
            OR (    usim_rec.equipment_status_type_id = 3
                AND usim_rec.equipment_status_id = 57
               )
        THEN
            po_stolen := 1;
		END IF;
        IF(usim_rec.equipment_status_type_id = 16
                AND usim_rec.equipment_status_id = 187)
		THEN
				po_assignable :=1;
		END IF;
		
		FOR equip_rec IN c_usim_status
		LOOP
		   EXIT WHEN c_usim_status%NOTFOUND;
		   IF (equip_rec.equipment_status_type_id = 2 AND equip_rec.equipment_status_id = 8)
			THEN
				po_previously_activated := 1;
			END IF;
		   IF (equip_rec.equipment_status_type_id = 1
					 AND equip_rec.equipment_status_id = 6)
		   THEN			 
				v_lost_ind_1 := TRUE;
		   END IF;	
		   IF    (    equip_rec.equipment_status_type_id = 3
					 AND equip_rec.equipment_status_id = 11
					)
				 OR (    equip_rec.equipment_status_type_id = 3
					 AND equip_rec.equipment_status_id = 56
					)
				 OR (    equip_rec.equipment_status_type_id = 3
					 AND equip_rec.equipment_status_id = 57
					)
		   THEN
				 v_lost_ind_2 := TRUE;
		   END IF;
		   
			IF (equip_rec.equipment_status_type_id = 16
				 AND equip_rec.equipment_status_id = 187)
			THEN
				po_assignable :=1;
			END IF;
			
		END LOOP;
        
		IF(v_lost_ind_1 = TRUE AND v_lost_ind_2 = TRUE)
		THEN
			po_stolen := 1;
		END IF;

         IF (    usim_rec.equipment_status_type_id = 2
             AND usim_rec.equipment_status_id = 8
            )
         THEN
            po_initial_activation := 'N';
            v_initial_activation := 'N';
         END IF;

         IF v_initial_activation = 'Y'
         THEN
            FOR equip_rec IN c_usim_status
            LOOP
               EXIT WHEN c_usim_status%NOTFOUND;

               IF (    equip_rec.equipment_status_type_id = 2
                   AND equip_rec.equipment_status_id = 8
                  )
               THEN
                  v_initial_activation := 'N';
                  EXIT;
               ELSIF (    equip_rec.equipment_status_type_id = 1
                      AND equip_rec.equipment_status_id = 3
                     )
               THEN
                  v_initial_activation := 'Y';
                  EXIT;
               END IF;
            END LOOP;

            IF v_initial_activation = 'O'
            THEN
               po_initial_activation := 'Y';
            ELSE
               po_initial_activation := v_initial_activation;
            END IF;
         END IF;

         BEGIN
            getimeibyusimid (pi_serial_no, po_assohandsetimei_for_usim, po_last_event_type);
         EXCEPTION
            WHEN imeinotfound
            THEN
               po_assohandsetimei_for_usim := '';
			   po_last_event_type := '';
            WHEN OTHERS
            THEN
               -- po_lastmuleimei_for_sim:=null;
               RAISE;
         -- RAISE_APPLICATION_ERROR(SQLCODE,SQLERRM);
         END;
      END IF;

--- call GetProductInfo
      IF pi_tech_type_class = 'PAGER'
      THEN
         getproductinfo (v_paging_product_id,
                         pi_tech_type_class,
                         v_product_rec
                        );
      ELSE
         getproductinfo (v_product_id, v_product_rec);
      END IF;

      po_product_gp_type_id := v_product_rec.product_gp_type_id;
      po_product_gp_type_cd := v_product_rec.product_gp_type_cd;
      po_product_gp_type_des := v_product_rec.product_gp_type_des;
      po_product_gp_type_des_f := v_product_rec.product_gp_type_des_f;
      po_product_type_id := v_product_rec.product_type_id;
      po_product_type_des := v_product_rec.product_type_des;
      po_product_type_des_f := v_product_rec.product_type_des_f;
      po_vendor_no := v_product_rec.vendor_no;
      v_vendor_no := v_product_rec.vendor_no;
      po_vendor_name := v_product_rec.vendor_name;
      po_product_type := v_product_rec.product_type_kb;
      po_equipment_type := v_product_rec.equipment_type_kb;
      po_equipment_type_class := v_product_rec.equipment_type_class;
      po_cross_fleet := v_product_rec.cross_fleet;
      po_product_category_id := v_product_rec.product_category_id;
      po_equipment_group := v_product_rec.equipment_group;
	  getbrands (po_product_cd, po_brands);

			-- The following block overrides the po_brands returned by the getbrands procedure.
			-- It must be called after the last getbrands procedure call.  The excludekoodoprepaidbrand
			-- function will return an empty brand_array if the usim_rec.pre_post_paid_flag is 'PRE'
			-- and the po_brands contains the Koodo brand.  This is to prevent equipment validation
			-- for USIMs defined as Koodo Prepaid.  This is done as part of the Eagle project in order
			-- to prevent SIMs designated as Eagle SIMs from being activated in the TELUS managed
			-- interface applications.
			-- Commented out for the Koodo Pre2Post project, as we now need to (partially) support Koodo
			-- Prepaid USIMs.
			--IF pi_tech_type_class = 'USIM' THEN
			--	po_brands := excludekoodoprepaidbrand(usim_rec.pre_post_paid_flag, po_brands);
			--END IF;

-- Replace PCS sublock code with master code if equipment refurbished or previously activated
-- and not Sanyo
      IF     (v_product_status_cd = 'R' OR v_initial_activation = 'N')
         AND v_vendor_no != '10004245'
      THEN
         po_sublock1 := v_master_lock;
      END IF;

      -- check if unscanned - If for status type 4 latest  status is 63, then equipment is "un-scanned"
      v_equipment_status_type_id := equipment_state;
      v_equipment_status_id := walmart_lock;
      po_unscanned := numeric_false;
      checklateststatus (pi_serial_no,
                         pi_tech_type_class,
                         v_equipment_status_type_id,
                         v_equipment_status_id,
                         v_recordexist
                        );

      IF (v_recordexist)
      THEN
         po_unscanned := numeric_true;
      END IF;
   EXCEPTION
      WHEN esnnotfound 
      THEN
         IF pi_tech_type_class = 'PCS' AND c_equipment_pcs%ISOPEN
         THEN
            CLOSE c_equipment_pcs;
         ELSIF pi_tech_type_class = 'MIKE' AND c_equipment_mike%ISOPEN
         THEN
            CLOSE c_equipment_mike;
         ELSIF pi_tech_type_class = 'ANALOG' AND c_equip_analog%ISOPEN
         THEN
            CLOSE c_equip_analog;
         ELSIF pi_tech_type_class = 'SIM' AND c_equipment_sim%ISOPEN
         THEN
            CLOSE c_equipment_sim;
         ELSIF pi_tech_type_class = 'UIM' AND c_equipment_uim%ISOPEN
         THEN
            CLOSE c_equipment_uim;
         ELSIF pi_tech_type_class = 'PAGER' AND c_equip_pager%ISOPEN
         THEN
            CLOSE c_equip_pager;
         ELSIF pi_tech_type_class = 'VIRTUAL' AND c_equipment_virtual%ISOPEN
         THEN
            CLOSE c_equipment_virtual;
		ELSIF pi_tech_type_class = 'USIM' AND c_equipment_usim%ISOPEN
         THEN
            CLOSE c_equipment_usim;
         END IF;

         raise_application_error
                          (-20310,
                           'Client_Equipment pkg: ESN or Product ID not found');
	  WHEN imsinotfound
      THEN
		 IF pi_tech_type_class = 'USIM' AND c_equipment_usim%ISOPEN
         THEN
            CLOSE c_equipment_usim;
         END IF;

         raise_application_error
                          (-20326,
                           'Client_Equipment pkg: IMSI not found');						   
      WHEN OTHERS
      THEN
         IF pi_tech_type_class = 'PCS' AND c_equipment_pcs%ISOPEN
         THEN
            CLOSE c_equipment_pcs;
         ELSIF pi_tech_type_class = 'MIKE' AND c_equipment_mike%ISOPEN
         THEN
            CLOSE c_equipment_mike;
         ELSIF pi_tech_type_class = 'ANALOG' AND c_equip_analog%ISOPEN
         THEN
            CLOSE c_equip_analog;
         ELSIF pi_tech_type_class = 'SIM' AND c_equipment_sim%ISOPEN
         THEN
            CLOSE c_equipment_sim;
         ELSIF pi_tech_type_class = 'UIM' AND c_equipment_uim%ISOPEN
         THEN
            CLOSE c_equipment_uim;
         ELSIF pi_tech_type_class = 'PAGER' AND c_equip_pager%ISOPEN
         THEN
            CLOSE c_equip_pager;
         ELSIF pi_tech_type_class = 'VIRTUAL' AND c_equipment_virtual%ISOPEN
         THEN
            CLOSE c_equipment_virtual;
		ELSIF pi_tech_type_class = 'USIM' AND c_equipment_usim%ISOPEN
         THEN
            CLOSE c_equipment_usim;
         END IF;

         RAISE;
   --RAISE_APPLICATION_ERROR(SQLCODE,SQLERRM);
   END getequipmentinfo;

-------------------------------------------------------------------------------------------

   --------------------------------------------------------------------------------------------------------------------------
   PROCEDURE getmulebysim (
      pi_sim                          IN       VARCHAR2,
      po_imei                         OUT      VARCHAR2,
      po_tech_type                    OUT      VARCHAR2,
      po_product_cd                   OUT      VARCHAR2,
      po_product_status_cd            OUT      VARCHAR2,
      po_vendor_name                  OUT      VARCHAR2,
      po_vendor_no                    OUT      VARCHAR2,
      po_equipment_status_type_id     OUT      NUMBER,
      po_equipment_status_id          OUT      NUMBER,
      po_stolen                       OUT      NUMBER,
      po_sublock1                     OUT      VARCHAR2,
      po_product_gp_type_id           OUT      NUMBER,
      po_product_gp_type_cd           OUT      VARCHAR2,
      po_product_gp_type_des          OUT      VARCHAR2,
      po_product_type_id              OUT      NUMBER,
      po_product_type_des             OUT      VARCHAR2,
      po_product_class_id             OUT      NUMBER,
      po_product_class_cd             OUT      VARCHAR2,
      po_product_class_des            OUT      VARCHAR2,
      po_provider_owner_id            OUT      NUMBER,
      po_lastmuleimei_for_sim         OUT      VARCHAR2,
      po_english_product_name         OUT      VARCHAR2,
      po_french_product_name          OUT      VARCHAR2,
      po_browser_version              OUT      VARCHAR2,
      po_firmware_version             OUT      VARCHAR2,
      po_prl_cd                       OUT      VARCHAR2,
      po_prl_des                      OUT      VARCHAR2,
      po_product_type_des_f           OUT      VARCHAR2,
      po_product_gp_type_des_f        OUT      VARCHAR2,
      po_min_cd                       OUT      VARCHAR2,
      po_customer_id                  OUT      NUMBER,
      po_product_type_list            OUT      VARCHAR2,
      po_initial_activation           OUT      VARCHAR2,
      po_mode_code                    OUT      NUMBER,
      po_mode_description             OUT      VARCHAR2,
      po_product_type                 OUT      VARCHAR2,
      po_equipment_type               OUT      VARCHAR2,
      po_equipment_type_class         OUT      VARCHAR2,
      po_cross_fleet                  OUT      VARCHAR2,
      po_cost                         OUT      NUMBER,
      po_cap_code                     OUT      VARCHAR2,
      po_coverage_region_code_list    OUT      VARCHAR2,
      po_encoding_format_code         OUT      VARCHAR2,
      po_ownership_code               OUT      VARCHAR2,
      po_prepaid                      OUT      VARCHAR2,
      po_frequency_cd                 OUT      VARCHAR2,
      po_firmware_feature_code_list   OUT      VARCHAR2,
      po_browser_protocol             OUT      VARCHAR2,
      po_unscanned                    OUT      NUMBER,
      po_equip_status_dt              OUT      DATE,
      po_puk                          OUT      VARCHAR2,
      po_product_category_id          OUT      NUMBER,
      po_rim_pin                      OUT      VARCHAR2,
      po_brands                       OUT      brand_array,
	  po_equipment_group              OUT      VARCHAR2,
	  po_source_network_type_cd       OUT      VARCHAR2
   )
   IS
      v_imei      iden_equipment.serial_no%TYPE;
      v_virtual   VARCHAR2 (2);
	  v_local_imsi VARCHAR2 (2);
  	  v_remote_imsi VARCHAR2 (2);
  	  v_assohandsetimei_for_usim VARCHAR2 (2);
	  v_assignable NUMBER;
	  v_previously_activated NUMBER;
      v_last_event_type usim_pcs_device_assoc.event_type%TYPE;
      v_pre_post_paid_flag VARCHAR2 (2);
   BEGIN
      getimeibysim (pi_sim, v_imei);

      IF v_imei IS NOT NULL
      THEN
         po_imei := v_imei;
         getequipmentinfo (v_imei,
                           'MIKE',
                           FALSE,
                           po_tech_type,
                           po_product_cd,
                           po_product_status_cd,
                           po_vendor_name,
                           po_vendor_no,
                           po_equipment_status_type_id,
                           po_equipment_status_id,
                           po_stolen,
                           po_sublock1,
                           po_product_gp_type_id,
                           po_product_gp_type_cd,
                           po_product_gp_type_des,
                           po_product_type_id,
                           po_product_type_des,
                           po_product_class_id,
                           po_product_class_cd,
                           po_product_class_des,
                           po_provider_owner_id,
                           po_lastmuleimei_for_sim,
                           po_english_product_name,
                           po_french_product_name,
                           po_browser_version,
                           po_firmware_version,
                           po_prl_cd,
                           po_prl_des,
                           po_product_type_des_f,
                           po_product_gp_type_des_f,
                           po_min_cd,
                           po_customer_id,
                           po_product_type_list,
                           po_initial_activation,
                           po_mode_code,
                           po_mode_description,
                           po_product_type,
                           po_equipment_type,
                           po_equipment_type_class,
                           po_cross_fleet,
                           po_cost,
                           po_cap_code,
                           po_coverage_region_code_list,
                           po_encoding_format_code,
                           po_ownership_code,
                           po_prepaid,
                           po_frequency_cd,
                           po_firmware_feature_code_list,
                           po_browser_protocol,
                           po_unscanned,
                           po_equip_status_dt,
                           po_puk,
                           po_product_category_id,
                           v_virtual,
                           po_rim_pin,
                           po_brands,
						   v_assohandsetimei_for_usim,
						   v_local_imsi,
						   v_remote_imsi,
						   v_assignable,
                           v_previously_activated,
						   po_equipment_group,
                           v_last_event_type,
                           po_source_network_type_cd,
                           v_pre_post_paid_flag
                          );
         getbrands (po_product_cd, po_brands);
      END IF;
   EXCEPTION
      WHEN OTHERS
      THEN
         RAISE;
   END;

-------------------------------------------------------------------------------------------
-------------------------------------------------------------------------------------------
   PROCEDURE getproductinfo (
      pi_product_id         IN       NUMBER,
      ro_product_info_rec   OUT      product_info_record
   )
   IS
      pi_tech_type_class   VARCHAR2 (10) := 'NOT PAGER';
   BEGIN
      getproductinfo (pi_product_id, pi_tech_type_class, ro_product_info_rec);
   EXCEPTION
      WHEN OTHERS
      THEN
         RAISE;
   --RAISE_APPLICATION_ERROR(SQLCODE,SQLERRM);
   END getproductinfo;

----------------------------------------------------------------------------
   PROCEDURE getproductinfo (
      pi_product_id         IN       NUMBER,
      pi_tech_type_class    IN       VARCHAR2,
      ro_product_info_rec   OUT      product_info_record
   )
   IS
      CURSOR c_product_gr_type (p_product_id NUMBER)
      IS
         SELECT ci_g.sub_type_id product_gp_type_id,
                cg.ci_group_cd product_gp_type_cd,
                ci_g.catalogue_item_des product_gp_type_des,
                '' product_gp_type_des_f                 --Not existing feald
                                        ,
                ci_gp.sub_type_id product_type_id,
                ci_gp.catalogue_item_des product_type_des,
                '' product_type_des_f                    --Not existing feald
           FROM catalogue_item ci_p,
                catalogue_item ci_g,
                ci_group cg,
                ci_relationship cr,
                ci_relationship crg,
                catalogue_item ci_gp
          WHERE ci_p.sub_type_id = pi_product_id
            AND ci_p.catalogue_item_type_cd = 'PRODUCT'
            AND cg.ci_group_type_cd = 'PROD_EXCL_GRP'
            AND ci_g.sub_type_id = cg.ci_group_id
            AND ci_g.catalogue_item_type_cd = 'GROUP'
            AND cr.catalogue_parent_item_id = ci_g.catalogue_item_id
            AND cr.catalogue_child_item_id = ci_gp.catalogue_item_id
            AND cr.relationship_type_cd = 'SKU_COMP'
            -- changed from GENERICGRP
            AND ci_gp.catalogue_item_type_cd = 'GROUP'
            AND crg.catalogue_parent_item_id = ci_gp.catalogue_item_id
            AND crg.catalogue_child_item_id = ci_p.catalogue_item_id
            AND crg.relationship_type_cd = 'SKU_COMP';

      pr_type_rec            c_product_gr_type%ROWTYPE;

      CURSOR c_equip (p_product_id NUMBER)
      IS
         SELECT pcx.product_type_knowbility, pcx.equip_type_knowbility, pcx.sems_equipment_group,
                et.equip_type_class, p.product_category_id
           FROM product p, product_class_xref_kb pcx, kb_equip_type et
          WHERE p.product_id = p_product_id
            AND pcx.technology_type = p.technology_type
            AND pcx.product_class_id = p.product_class_id
            AND et.equip_type_knowbility(+) = pcx.equip_type_knowbility;

      equip_type_rec         c_equip%ROWTYPE;

      CURSOR c_pager_equip (p_paging_product_id NUMBER)
      IS
         SELECT pcx.product_type_knowbility, pmt.paging_model_type_cd,
                pmt.description
           FROM paging_model_type pmt,
                product_class_xref_kb pcx,
                product p,
                paging_product pp
          WHERE pmt.paging_model_type_cd = pp.paging_model_type_cd
            AND pcx.technology_type = p.technology_type
            AND p.product_id = pp.product_id
            AND pp.paging_product_id = p_paging_product_id;

      pager_equip_type_rec   c_pager_equip%ROWTYPE;

      CURSOR c_vendor (p_product_id NUMBER, p_exclude CHAR)
      IS
         SELECT m.manufacturer_id, m.manufacturer_name
           FROM product p, manufacturer m
          WHERE p.product_id = p_product_id
            AND m.manufacturer_id = p.manufacturer_id
            AND (       p_exclude = 'Y'
                    AND m.manufacturer_id != '10000119'    -- exclude Clearnet
                    AND m.manufacturer_id != '10007013' -- exclude Misc. Other
                    AND m.manufacturer_id != '10007011'
                 -- exclude Allen Telecom
                 OR p_exclude = 'N'
                );

      vendor_rec             c_vendor%ROWTYPE;

      CURSOR c_legacy (p_product_id NUMBER)
      IS
         SELECT feature_id
           FROM product_feature
          WHERE product_id = p_product_id AND feature_id = 10032685;

      v_feature              NUMBER (22);
   BEGIN
      IF pi_tech_type_class = 'PAGER'
      THEN
         OPEN c_pager_equip (pi_product_id);

         FETCH c_pager_equip
          INTO pager_equip_type_rec;

         IF c_pager_equip%FOUND
         THEN
            ro_product_info_rec.product_type_kb :=
                                 pager_equip_type_rec.product_type_knowbility;
            ro_product_info_rec.equipment_type_kb :=
                                    pager_equip_type_rec.paging_model_type_cd;
            ro_product_info_rec.equipment_type_class :=
                                             pager_equip_type_rec.description;
         END IF;

         CLOSE c_pager_equip;
      ELSE
         OPEN c_product_gr_type (pi_product_id);

         FETCH c_product_gr_type
          INTO pr_type_rec;

         IF c_product_gr_type%FOUND
         THEN
            ro_product_info_rec.product_gp_type_id :=
                                               pr_type_rec.product_gp_type_id;
            ro_product_info_rec.product_gp_type_cd :=
                                               pr_type_rec.product_gp_type_cd;
            ro_product_info_rec.product_gp_type_des :=
                                              pr_type_rec.product_gp_type_des;
            ro_product_info_rec.product_gp_type_des_f :=
                                            pr_type_rec.product_gp_type_des_f;
            ro_product_info_rec.product_type_id :=
                                                  pr_type_rec.product_type_id;
            ro_product_info_rec.product_type_des :=
                                                 pr_type_rec.product_type_des;
            ro_product_info_rec.product_type_des_f :=
                                               pr_type_rec.product_type_des_f;
         END IF;

         CLOSE c_product_gr_type;

         OPEN c_equip (pi_product_id);

         FETCH c_equip
          INTO equip_type_rec;

         IF c_equip%FOUND
         THEN
            ro_product_info_rec.product_type_kb :=
                                       equip_type_rec.product_type_knowbility;
            ro_product_info_rec.equipment_type_kb :=
                                         equip_type_rec.equip_type_knowbility;
            ro_product_info_rec.equipment_group :=
										 equip_type_rec.sems_equipment_group;
            ro_product_info_rec.equipment_type_class :=
                                              equip_type_rec.equip_type_class;
            ro_product_info_rec.product_category_id :=
                                           equip_type_rec.product_category_id;
         END IF;

         CLOSE c_equip;
      END IF;

      OPEN c_vendor (pi_product_id, 'Y');

      FETCH c_vendor
       INTO vendor_rec;

      IF c_vendor%FOUND
      THEN
         ro_product_info_rec.vendor_no := vendor_rec.manufacturer_id;
         ro_product_info_rec.vendor_name := vendor_rec.manufacturer_name;

         CLOSE c_vendor;
      ELSE
         CLOSE c_vendor;

         OPEN c_vendor (pi_product_id, 'N');

         FETCH c_vendor
          INTO vendor_rec;

         IF c_vendor%FOUND
         THEN
            ro_product_info_rec.vendor_no := vendor_rec.manufacturer_id;
            ro_product_info_rec.vendor_name := vendor_rec.manufacturer_name;
         ELSE
            ro_product_info_rec.vendor_no := '0';
            ro_product_info_rec.vendor_name := ' ';
         END IF;

         CLOSE c_vendor;
      END IF;

      OPEN c_legacy (pi_product_id);

      FETCH c_legacy
       INTO v_feature;

      IF c_legacy%FOUND
      THEN
         ro_product_info_rec.cross_fleet := 'Y';
      ELSE
         ro_product_info_rec.cross_fleet := 'N';
      END IF;

      CLOSE c_legacy;
   EXCEPTION
      WHEN OTHERS
      THEN
         RAISE;
   --RAISE_APPLICATION_ERROR(SQLCODE,SQLERRM);
   END getproductinfo;

-----------------------------------------------------------------------------
   PROCEDURE getimeibysim (pi_sim_id IN VARCHAR2, po_imei_id OUT VARCHAR2)
   IS
      CURSOR c_imei
      IS
         SELECT imei_serial_no
           FROM sim_imei
          WHERE sim_id = pi_sim_id
            AND sim_seq_no = 0
            AND imei_seq_no = 0
            AND seq_no = 0
            AND sim_imei_ass_dt =
                   (SELECT MAX (sim_imei_ass_dt)
                      FROM sim_imei
                     WHERE sim_id = pi_sim_id
                       AND sim_seq_no = 0
                       AND imei_seq_no = 0
                       AND seq_no = 0);

      v_imei_id   VARCHAR2 (50);
      v_counter   INTEGER       := 0;
   BEGIN
      OPEN c_imei;

      LOOP
         FETCH c_imei
          INTO v_imei_id;

         EXIT WHEN c_imei%NOTFOUND;
         v_counter := v_counter + 1;
         po_imei_id := v_imei_id;
      END LOOP;

      CLOSE c_imei;

      IF v_counter > 1
      THEN
         RAISE multipleimeiforsim;
      ELSIF v_counter = 0
      THEN
-- raise IMEINotFound;
         po_imei_id := NULL;
      END IF;
   EXCEPTION
--When IMEINotFound Then
-- raise IMEINotFound;
-- RAISE_APPLICATION_ERROR(-20313,'Client_Equipment pkg: IMEI  not found');
      WHEN multipleimeiforsim
      THEN
         RAISE multipleimeiforsim;
      --RAISE_APPLICATION_ERROR(-20315,'Client_Equipment pkg: Multiple Last Mule IMEI found ');
      WHEN OTHERS
      THEN
         IF c_imei%ISOPEN
         THEN
            CLOSE c_imei;
         END IF;

         RAISE;
-- RAISE_APPLICATION_ERROR(SQLCODE,SQLERRM);
   END getimeibysim;

----------------------------------------------------------------------------
-----------------------------------------------------------------------------
   PROCEDURE getsimbyimei (pi_imei_id IN VARCHAR2, po_sim_id OUT VARCHAR2)
   IS
      CURSOR c_sim
      IS
         SELECT sim_id
           FROM sim_imei
          WHERE imei_serial_no = pi_imei_id
            AND imei_seq_no = 0
            AND sim_seq_no = 0
            AND seq_no = 0
            AND sim_imei_ass_dt =
                   (SELECT MAX (sim_imei_ass_dt)
                      FROM sim_imei
                     WHERE imei_serial_no = pi_imei_id
                       AND imei_seq_no = 0
                       AND sim_seq_no = 0
                       AND seq_no = 0);

      v_sim_id    VARCHAR2 (50);
      v_counter   INTEGER       := 0;
   BEGIN
      OPEN c_sim;

      LOOP
         FETCH c_sim
          INTO v_sim_id;

         EXIT WHEN c_sim%NOTFOUND;
         v_counter := v_counter + 1;
         po_sim_id := v_sim_id;
      END LOOP;

      CLOSE c_sim;

      IF v_counter > 1
      THEN
         RAISE multiplesimforimei;
      ELSIF v_counter = 0
      THEN
         RAISE simnotfound;
      END IF;
   EXCEPTION
      WHEN simnotfound
      THEN
         raise_application_error (-20314,
                                  'Client_Equipment pkg: SIM not found'
                                 );
      WHEN multiplesimforimei
      THEN
         raise_application_error
            (-20316,
             'Client_Equipment pkg: Multiple SIM found for the Last Mule IMEI  '
            );
      WHEN OTHERS
      THEN
         IF c_sim%ISOPEN
         THEN
            CLOSE c_sim;
         END IF;

         RAISE;
--    RAISE_APPLICATION_ERROR(SQLCODE,SQLERRM);
   END getsimbyimei;

------------------------------------------------------------------------------------
------------------------------------------------------------------------------------
   PROCEDURE getmasterlocknumber (
      pi_serial_no        IN       VARCHAR2,
      pi_user_id          IN       VARCHAR2,
      pi_lock_reason_id   IN       NUMBER,
      po_master_lock      OUT      VARCHAR2
   )
   IS
      CURSOR c_user_lock
      IS
         SELECT max_no
           FROM user_lock_max
          WHERE username = UPPER (pi_user_id);

      v_max_no                   user_lock_max.max_no%TYPE;

      CURSOR c_lock_sum
      IS
         SELECT lock_access_attempt_no, ROWID
           FROM user_lock_summ
          WHERE user_request_lock = UPPER (pi_user_id)
            AND TRUNC (lock_access_attempt_dt) = TRUNC (SYSDATE)
                                                                --for update  nowait
      ;

      v_lock_access_attempt_no   user_lock_summ.lock_access_attempt_no%TYPE;
      v_rowid                    ROWID;

      CURSOR c_master_lock
      IS
         SELECT master_lock_number
           FROM pcs_equipment
          WHERE serial_no = pi_serial_no;

      v_master_lock_number       pcs_equipment.master_lock_number%TYPE;
   BEGIN
      OPEN c_user_lock;

      FETCH c_user_lock
       INTO v_max_no;

      IF c_user_lock%NOTFOUND
      THEN
         RAISE useridnotexist;
      END IF;

      CLOSE c_user_lock;

      OPEN c_lock_sum;

      FETCH c_lock_sum
       INTO v_lock_access_attempt_no, v_rowid;

      IF c_lock_sum%NOTFOUND
      THEN
         INSERT INTO user_lock_summ
                     (user_request_lock, lock_access_attempt_dt,
                      lock_access_attempt_no, load_dt, update_dt
                     )
              VALUES (UPPER (pi_user_id), SYSDATE,
                      1, SYSDATE, SYSDATE
                     );
      ELSIF v_lock_access_attempt_no < v_max_no
      THEN
         UPDATE user_lock_summ
            SET lock_access_attempt_dt = SYSDATE,
                lock_access_attempt_no = lock_access_attempt_no + 1,
                load_dt = SYSDATE,
                update_dt = SYSDATE
          WHERE ROWID = v_rowid;
      ELSE
         RAISE reachedmaxlock;
      END IF;

      CLOSE c_lock_sum;

      OPEN c_master_lock;

      FETCH c_master_lock
       INTO v_master_lock_number;

      IF c_master_lock%NOTFOUND
      THEN
         RAISE esnnotfound;
      END IF;

      po_master_lock := v_master_lock_number;

      CLOSE c_master_lock;

      INSERT INTO pcs_equip_lock
                  (serial_no, seq_no, load_dt, user_request_lock,
                   lock_reason_id, update_dt, user_last_modify
                  )
           VALUES (pi_serial_no, 0, SYSDATE, UPPER (pi_user_id),
                   pi_lock_reason_id, SYSDATE, USER
                  );

      COMMIT;
   EXCEPTION
      WHEN useridnotexist
      THEN
         ROLLBACK;
         raise_application_error
            (-20311,
             'Client_Equipment pkg: User is not in the list for Master Lock request'
            );
      WHEN esnnotfound
      THEN
         ROLLBACK;
         raise_application_error (-20310,
                                  'Client_Equipment pkg: ESN  not found'
                                 );
      WHEN reachedmaxlock
      THEN
         ROLLBACK;
         raise_application_error
                     (-20312,
                      'User have reached  Maximum in the Master Lock request'
                     );
      WHEN OTHERS
      THEN
         ROLLBACK;
         raise_application_error (-20000, SQLERRM);
   END getmasterlocknumber;

------------------------------------------------------------------------------
------------------------------------------------------------------------------
   PROCEDURE getmasterlocknumber (
      pi_serial_no        IN       VARCHAR2,
      pi_user_id          IN       VARCHAR2,
      pi_lock_reason_id   IN       NUMBER,
      pi_outlet_id        IN       NUMBER,
      pi_chnl_org_id      IN       NUMBER,
      po_master_lock      OUT      VARCHAR2
   )
   IS
      CURSOR c_user_lock
      IS
         SELECT max_no
           FROM user_lock_max
          WHERE username = UPPER (pi_user_id);

      v_max_no                   user_lock_max.max_no%TYPE;

      CURSOR c_lock_sum
      IS
         SELECT lock_access_attempt_no, ROWID
           FROM user_lock_summ
          WHERE user_request_lock = UPPER (pi_user_id)
            AND TRUNC (lock_access_attempt_dt) = TRUNC (SYSDATE)
                                                                --for update  nowait
      ;

      v_lock_access_attempt_no   user_lock_summ.lock_access_attempt_no%TYPE;
      v_rowid                    ROWID;

      CURSOR c_master_lock
      IS
         SELECT master_lock_number
           FROM pcs_equipment
          WHERE serial_no = pi_serial_no;

      v_master_lock_number       pcs_equipment.master_lock_number%TYPE;
   BEGIN
      OPEN c_user_lock;

      FETCH c_user_lock
       INTO v_max_no;

      IF c_user_lock%NOTFOUND
      THEN
         RAISE useridnotexist;
      END IF;

      CLOSE c_user_lock;

      OPEN c_lock_sum;

      FETCH c_lock_sum
       INTO v_lock_access_attempt_no, v_rowid;

      IF c_lock_sum%NOTFOUND
      THEN
         INSERT INTO user_lock_summ
                     (user_request_lock, lock_access_attempt_dt,
                      lock_access_attempt_no, load_dt, update_dt
                     )
              VALUES (UPPER (pi_user_id), SYSDATE,
                      1, SYSDATE, SYSDATE
                     );
      ELSIF v_lock_access_attempt_no < v_max_no
      THEN
         UPDATE user_lock_summ
            SET lock_access_attempt_dt = SYSDATE,
                lock_access_attempt_no = lock_access_attempt_no + 1,
                load_dt = SYSDATE,
                update_dt = SYSDATE
          WHERE ROWID = v_rowid;
      ELSE
         RAISE reachedmaxlock;
      END IF;

      CLOSE c_lock_sum;

      OPEN c_master_lock;

      FETCH c_master_lock
       INTO v_master_lock_number;

      IF c_master_lock%NOTFOUND
      THEN
         RAISE esnnotfound;
      END IF;

      po_master_lock := v_master_lock_number;

      CLOSE c_master_lock;

      INSERT INTO pcs_equip_lock
                  (serial_no, seq_no, load_dt, user_request_lock,
                   lock_reason_id, update_dt, user_last_modify,
                   outlet_id,
                   chnl_org_id
                  )
           VALUES (pi_serial_no, 0, SYSDATE, UPPER (pi_user_id),
                   pi_lock_reason_id, SYSDATE, USER,
                   DECODE (pi_outlet_id, 0, NULL, pi_outlet_id),
                   DECODE (pi_chnl_org_id, 0, NULL, pi_chnl_org_id)
                  );

      COMMIT;
   EXCEPTION
      WHEN useridnotexist
      THEN
         ROLLBACK;
         raise_application_error
            (-20311,
             'Client_Equipment pkg: User is not in the list for Master Lock request'
            );
      WHEN esnnotfound
      THEN
         ROLLBACK;
         raise_application_error (-20310,
                                  'Client_Equipment pkg: ESN  not found'
                                 );
      WHEN reachedmaxlock
      THEN
         ROLLBACK;
         raise_application_error
                     (-20312,
                      'User have reached  Maximum in the Master Lock request'
                     );
      WHEN OTHERS
      THEN
         ROLLBACK;
         raise_application_error (-20000, SQLERRM);
   END getmasterlocknumber;

------------------------------------------------------------------------------------
------------------------------------------------------------------------------------
   PROCEDURE gettechnologytypeclass (
      pi_serial_no         IN       VARCHAR2,
      po_tech_type_class   OUT      VARCHAR2
   )
   IS
      CURSOR c_equipment_pcs
      IS
         SELECT 'PCS'
           FROM pcs_equipment pe
          WHERE pe.serial_no = pi_serial_no AND pe.seq_no = 0;

      CURSOR c_equipment_mike
      IS
         SELECT 'MIKE'
           FROM iden_equipment ie
          WHERE ie.serial_no = pi_serial_no AND ie.seq_no = 0;

      CURSOR c_equipment_sim
      IS
         SELECT 'SIM'
           FROM sim s
          WHERE s.sim_id = pi_serial_no AND s.seq_no = 0;

      CURSOR c_equip_analog
      IS
         SELECT 'ANALOG'
           FROM analog_equip ae
          WHERE ae.serial_no = pi_serial_no AND ae.seq_no = 0;

      CURSOR c_equip_pager
      IS
         SELECT 'PAGER'
           FROM distadm.paging_equip pe
          WHERE pe.serial_no = pi_serial_no AND pe.seq_no = 0;

      CURSOR c_uim_card
      IS
         SELECT 'UIM'
           FROM uim
          WHERE uim.iccid = pi_serial_no AND uim.seq_no = 0;

      CURSOR c_equip_virtual
      IS
         SELECT 'VIRTUAL'
           FROM distadm.virtual_equipment ve
          WHERE ve.serial_no = pi_serial_no;

	  CURSOR c_equipment_usim
      IS
         SELECT 'USIM'
           FROM usim
          WHERE usim_id = pi_serial_no AND seq_no = 0;

	  v_tech_type_class   VARCHAR2 (10);
   BEGIN
      OPEN c_equip_virtual;

      FETCH c_equip_virtual
       INTO v_tech_type_class;

      IF c_equip_virtual%NOTFOUND
      THEN
         OPEN c_equipment_pcs;

         FETCH c_equipment_pcs
          INTO v_tech_type_class;

         IF c_equipment_pcs%NOTFOUND
         THEN
            OPEN c_equipment_mike;

            FETCH c_equipment_mike
             INTO v_tech_type_class;

            IF c_equipment_mike%NOTFOUND
            THEN
               OPEN c_equipment_sim;

               FETCH c_equipment_sim
                INTO v_tech_type_class;

				IF c_equipment_sim%NOTFOUND
				THEN
				   OPEN c_equipment_usim;
				   
                   FETCH c_equipment_usim
                   INTO v_tech_type_class;

				   IF c_equipment_usim%NOTFOUND
	               THEN
	                  OPEN c_uim_card;

	                  FETCH c_uim_card
	                   INTO v_tech_type_class;

	                  IF c_uim_card%NOTFOUND
	                  THEN
	                     OPEN c_equip_pager;

	                     FETCH c_equip_pager
	                      INTO v_tech_type_class;

	                     IF c_equip_pager%NOTFOUND
	                     THEN
	                        OPEN c_equip_analog;

	                        FETCH c_equip_analog
	                         INTO v_tech_type_class;

	                        IF c_equip_analog%NOTFOUND
	                        THEN
	                           RAISE esnnotfound;
	                        END IF;
							CLOSE c_equip_analog;
						END IF;
                        CLOSE c_equip_pager;
                     END IF;

                     CLOSE c_uim_card;
                  END IF;

                  CLOSE c_equipment_usim;
               END IF;

               CLOSE c_equipment_sim;
            END IF;

            CLOSE c_equipment_mike;
         END IF;

         CLOSE c_equipment_pcs;
      END IF;

      CLOSE c_equip_virtual;

      po_tech_type_class := v_tech_type_class;
   EXCEPTION
      WHEN OTHERS
      THEN
         IF c_equipment_pcs%ISOPEN
         THEN
            CLOSE c_equipment_pcs;
         ELSIF c_equipment_mike%ISOPEN
         THEN
            CLOSE c_equipment_mike;
		 ELSIF c_equipment_usim%ISOPEN
		 THEN
			CLOSE c_equipment_usim;	
         ELSIF c_equip_analog%ISOPEN
         THEN
            CLOSE c_equip_analog;
         ELSIF c_equipment_sim%ISOPEN
         THEN
            CLOSE c_equipment_sim;
         ELSIF c_uim_card%ISOPEN
         THEN
            CLOSE c_uim_card;
         ELSIF c_equip_virtual%ISOPEN
         THEN
            CLOSE c_equip_virtual;
         ELSIF c_equip_pager%ISOPEN
         THEN
            CLOSE c_equip_pager;
         END IF;

         RAISE;
   END;

----------------------------------------------------------------------------
----------------------------------------------------------------------------
   PROCEDURE getfirmwareversionfeature (
      pi_firmware_version         IN       VARCHAR2,
      po_firmware_feature_codes   OUT      VARCHAR2
   )
   IS
--cursor c_FirmwareFeature is
--       select b.feature_cd
--       from   feature b, firmware_version_feature a
--       where  a.feature_id = b.feature_id
--       and    b.feature_group_cd = 'FIRM'
--       and    a.firmware_version = pi_firmware_version
--       order by feature_cd;
      CURSOR c_firmwarefeature
      IS
         SELECT   feature_cd
             FROM v_firmware_feature_attribute
            WHERE feature_group_cd = 'FIRM'
              AND firmware_version = pi_firmware_version
              AND feature_cd <> 'PRIMMAG'
              AND VALUE = 'Y'
         ORDER BY feature_cd;

      v_firmware_feature_cd   VARCHAR (1000);
   BEGIN
      v_firmware_feature_cd := '';
      po_firmware_feature_codes := '';

      OPEN c_firmwarefeature;

      LOOP
         FETCH c_firmwarefeature
          INTO v_firmware_feature_cd;

         EXIT WHEN c_firmwarefeature%NOTFOUND;
         po_firmware_feature_codes :=
                    po_firmware_feature_codes || v_firmware_feature_cd || '|';
      END LOOP;

      po_firmware_feature_codes :=
         SUBSTR (po_firmware_feature_codes,
                 1,
                 INSTR (po_firmware_feature_codes, '|', -1) - 1
                );

      CLOSE c_firmwarefeature;
   END;

----------------------------------------------------------------------------
----------------------------------------------------------------------------
   PROCEDURE getwarranty (
      pi_serial_no                  IN       VARCHAR2,
      po_warranty_exp_date          OUT      DATE,
      po_initial_activation_date    OUT      DATE,
      po_initial_manufacture_date   OUT      DATE,
      po_latest_pending_date        OUT      DATE,
      po_latest_pending_model       OUT      VARCHAR2,
      po_message                    OUT      VARCHAR2,
      po_warranty_extension_date    OUT      DATE,
      po_doa_expiry_date            OUT      DATE
   )
   IS
      v_tech_type_class           VARCHAR2 (10);

      CURSOR c_warr_exp_date_pcs (ci_warr_status_cd VARCHAR2)
      IS
         SELECT   warranty_period_end_dt
             FROM pcs_equip_warranty
            WHERE serial_no = pi_serial_no
              AND seq_no = 0
              AND warranty_status_cd = ci_warr_status_cd
              AND warranty_period_end_dt IS NOT NULL
         ORDER BY warranty_period_end_dt DESC;

      CURSOR c_warr_exp_date_iden (ci_warr_status_cd VARCHAR2)
      IS
         SELECT   warranty_period_end_dt
             FROM iden_equip_warranty
            WHERE serial_no = pi_serial_no
              AND seq_no = 0
              AND warranty_status_cd = ci_warr_status_cd
              AND warranty_period_end_dt IS NOT NULL
         ORDER BY warranty_period_end_dt DESC;

      CURSOR c_warr_exp_date_sim (ci_warr_status_cd VARCHAR2)
      IS
         SELECT   warranty_period_end_dt
             FROM sim_warranty
            WHERE sim_id = pi_serial_no
              AND seq_no = 0
              AND warranty_status_cd = ci_warr_status_cd
              AND warranty_period_end_dt IS NOT NULL
         ORDER BY warranty_period_end_dt DESC;

      CURSOR c_warr_exp_date_uim (ci_warr_status_cd VARCHAR2)
      IS
         SELECT   warranty_period_end_dt
             FROM uim_warranty
            WHERE iccid = pi_serial_no
              AND seq_no = 0
              AND warranty_status_cd = ci_warr_status_cd
              AND warranty_period_end_dt IS NOT NULL
         ORDER BY warranty_period_end_dt DESC;

      CURSOR c_init_date_pcs (ci_warranty_type_cd VARCHAR2)
      IS
         SELECT   aa.warranty_period_start_dt
             FROM pcs_equip_warranty aa, warranty_type bb
            WHERE aa.serial_no = pi_serial_no
              AND aa.seq_no = 0
              AND aa.warranty_type_id = bb.warranty_type_id
              AND bb.warranty_type_cd = ci_warranty_type_cd
              AND aa.warranty_period_start_dt IS NOT NULL
         --     and aa.warranty_seq_no in (0, 1)
         ORDER BY aa.warranty_period_start_dt;

      CURSOR c_init_date_iden (ci_warranty_type_cd VARCHAR2)
      IS
         SELECT   aa.warranty_period_start_dt
             FROM iden_equip_warranty aa, warranty_type bb
            WHERE aa.serial_no = pi_serial_no
              AND aa.seq_no = 0
              AND aa.warranty_type_id = bb.warranty_type_id
              AND bb.warranty_type_cd = ci_warranty_type_cd
              AND aa.warranty_period_start_dt IS NOT NULL
         --   and aa.warranty_seq_no in (0, 1)
         ORDER BY aa.warranty_period_start_dt;

      CURSOR c_init_date_sim (ci_warranty_type_cd VARCHAR2)
      IS
         SELECT   aa.warranty_period_start_dt
             FROM sim_warranty aa, warranty_type bb
            WHERE aa.sim_id = pi_serial_no
              AND aa.seq_no = 0
              AND aa.warranty_type_id = bb.warranty_type_id
              AND bb.warranty_type_cd = ci_warranty_type_cd
              AND aa.warranty_period_start_dt IS NOT NULL
         --    and aa.warranty_seq_no in (0, 1)
         ORDER BY aa.warranty_period_start_dt;

      CURSOR c_init_date_uim (ci_warranty_type_cd VARCHAR2)
      IS
         SELECT   aa.warranty_period_start_dt
             FROM uim_warranty aa, warranty_type bb
            WHERE aa.iccid = pi_serial_no
              AND aa.seq_no = 0
              AND aa.warranty_type_id = bb.warranty_type_id
              AND bb.warranty_type_cd = ci_warranty_type_cd
              AND aa.warranty_period_start_dt IS NOT NULL
         ORDER BY aa.warranty_period_start_dt;

      CURSOR c_pend_model_pcs
      IS
         SELECT   original_serial_no
             FROM pcs_equip_warranty
            WHERE serial_no = pi_serial_no
              AND seq_no = 0
              AND warranty_status_cd = 'PEND'
              AND warranty_period_end_dt IS NOT NULL
              AND original_serial_no IS NOT NULL
         ORDER BY warranty_period_end_dt DESC;

      CURSOR c_pend_model_iden
      IS
         SELECT   original_serial_no
             FROM iden_equip_warranty
            WHERE serial_no = pi_serial_no
              AND seq_no = 0
              AND warranty_status_cd = 'PEND'
              AND warranty_period_end_dt IS NOT NULL
              AND original_serial_no IS NOT NULL
         ORDER BY warranty_period_end_dt DESC;

      CURSOR c_pend_model_sim
      IS
         SELECT   original_sim_id
             FROM sim_warranty
            WHERE sim_id = pi_serial_no
              AND seq_no = 0
              AND warranty_status_cd = 'PEND'
              AND warranty_period_end_dt IS NOT NULL
              AND original_sim_id IS NOT NULL
         ORDER BY warranty_period_end_dt DESC;

      CURSOR c_pend_model_uim
      IS
         SELECT   original_iccid
             FROM uim_warranty
            WHERE iccid = pi_serial_no
              AND seq_no = 0
              AND warranty_status_cd = 'PEND'
              AND warranty_period_end_dt IS NOT NULL
              AND original_iccid IS NOT NULL
         ORDER BY warranty_period_end_dt DESC;

      CURSOR c_refurb_pcs
      IS
         SELECT 'REFURB'
           FROM pcs_equip_warranty w, warranty_type wt
          WHERE w.serial_no = pi_serial_no
            AND w.seq_no = 0
            AND wt.warranty_type_id = w.warranty_type_id
            AND wt.warranty_type_cd = 'REFURB';

      CURSOR c_refurb_iden
      IS
         SELECT 'REFURB'
           FROM iden_equip_warranty w, warranty_type wt
          WHERE w.serial_no = pi_serial_no
            AND w.seq_no = 0
            AND wt.warranty_type_id = w.warranty_type_id
            AND wt.warranty_type_cd = 'REFURB';

      CURSOR c_refurb_sim
      IS
         SELECT 'REFURB'
           FROM sim_warranty w, warranty_type wt
          WHERE w.sim_id = pi_serial_no
            AND w.seq_no = 0
            AND wt.warranty_type_id = w.warranty_type_id
            AND wt.warranty_type_cd = 'REFURB';

      CURSOR c_refurb_uim
      IS
         SELECT 'REFURB'
           FROM uim_warranty w, warranty_type wt
          WHERE w.iccid = pi_serial_no
            AND w.seq_no = 0
            AND wt.warranty_type_id = w.warranty_type_id
            AND wt.warranty_type_cd = 'REFURB';

      v_refurb                    VARCHAR2 (10);

      CURSOR c_warr_extension
      IS
         SELECT TRUNC (SYSDATE) + 7 - 1 / 86400
           FROM DUAL;

      v_provider_owner_id         NUMBER (22);
      v_initial_activation_date   DATE;
      v_warr_count                NUMBER (2)    := 0;
   BEGIN
      gettechnologytypeclass (pi_serial_no, v_tech_type_class);

      IF v_tech_type_class = 'ANALOG'
      THEN
         po_message :=
                    'This handset requires a proof of purchase for warranty.';
      ELSIF v_tech_type_class = 'PCS'
      THEN
         OPEN c_warr_exp_date_pcs ('VALD');

         FETCH c_warr_exp_date_pcs
          INTO po_warranty_exp_date;

         v_warr_count := c_warr_exp_date_pcs%ROWCOUNT;

         CLOSE c_warr_exp_date_pcs;

         IF v_warr_count = 0
         THEN
            SELECT provider_owner_id
              INTO v_provider_owner_id
              FROM pcs_equipment
             WHERE serial_no = pi_serial_no AND seq_no = 0;

            IF v_provider_owner_id = 9
            THEN
               po_message :=
                    'This handset requires a proof of purchase for warranty.';
            ELSE
               po_message := 'No valid Warranty exists';
            END IF;
         ELSE
            OPEN c_init_date_pcs ('STANDARD');

            FETCH c_init_date_pcs
             INTO v_initial_activation_date;

            CLOSE c_init_date_pcs;

            po_initial_activation_date := v_initial_activation_date;

            OPEN c_refurb_pcs;

            FETCH c_refurb_pcs
             INTO v_refurb;

            IF c_refurb_pcs%NOTFOUND
            THEN
               po_doa_expiry_date := v_initial_activation_date + 30;
            END IF;

            CLOSE c_refurb_pcs;

            OPEN c_init_date_pcs ('MANUFACT');

            FETCH c_init_date_pcs
             INTO po_initial_manufacture_date;

            CLOSE c_init_date_pcs;

            OPEN c_warr_exp_date_pcs ('PEND');

            FETCH c_warr_exp_date_pcs
             INTO po_latest_pending_date;

            CLOSE c_warr_exp_date_pcs;

            OPEN c_pend_model_pcs;

            FETCH c_pend_model_pcs
             INTO po_latest_pending_model;

            CLOSE c_pend_model_pcs;

            OPEN c_warr_extension;

            FETCH c_warr_extension
             INTO po_warranty_extension_date;

            CLOSE c_warr_extension;
         END IF;
      ELSIF v_tech_type_class = 'MIKE'
      THEN
         OPEN c_warr_exp_date_iden ('VALD');

         FETCH c_warr_exp_date_iden
          INTO po_warranty_exp_date;

         v_warr_count := c_warr_exp_date_iden%ROWCOUNT;

         CLOSE c_warr_exp_date_iden;

         IF v_warr_count = 0
         THEN
            po_message := 'No valid Warranty exists';
         ELSE
            OPEN c_init_date_iden ('STANDARD');

            FETCH c_init_date_iden
             INTO v_initial_activation_date;

            CLOSE c_init_date_iden;

            po_initial_activation_date := v_initial_activation_date;

            OPEN c_refurb_iden;

            FETCH c_refurb_iden
             INTO v_refurb;

            IF c_refurb_iden%NOTFOUND
            THEN
               po_doa_expiry_date := v_initial_activation_date + 30;
            END IF;

            CLOSE c_refurb_iden;

            OPEN c_init_date_iden ('MANUFACT');

            FETCH c_init_date_iden
             INTO po_initial_manufacture_date;

            CLOSE c_init_date_iden;

            OPEN c_warr_exp_date_iden ('PEND');

            FETCH c_warr_exp_date_iden
             INTO po_latest_pending_date;

            CLOSE c_warr_exp_date_iden;

            OPEN c_pend_model_iden;

            FETCH c_pend_model_iden
             INTO po_latest_pending_model;

            CLOSE c_pend_model_iden;

            OPEN c_warr_extension;

            FETCH c_warr_extension
             INTO po_warranty_extension_date;

            CLOSE c_warr_extension;
         END IF;
      ELSIF v_tech_type_class = 'SIM'
      THEN
         OPEN c_warr_exp_date_sim ('VALD');

         FETCH c_warr_exp_date_sim
          INTO po_warranty_exp_date;

         v_warr_count := c_warr_exp_date_sim%ROWCOUNT;

         CLOSE c_warr_exp_date_sim;

         IF v_warr_count = 0
         THEN
            po_message := 'No valid Warranty exists';
         ELSE
            OPEN c_init_date_sim ('STANDARD');

            FETCH c_init_date_sim
             INTO v_initial_activation_date;

            CLOSE c_init_date_sim;

            po_initial_activation_date := v_initial_activation_date;

            OPEN c_refurb_sim;

            FETCH c_refurb_sim
             INTO v_refurb;

            IF c_refurb_sim%NOTFOUND
            THEN
               po_doa_expiry_date := v_initial_activation_date + 30;
            END IF;

            CLOSE c_refurb_sim;

            OPEN c_init_date_sim ('MANUFACT');

            FETCH c_init_date_sim
             INTO po_initial_manufacture_date;

            CLOSE c_init_date_sim;

            OPEN c_warr_exp_date_sim ('PEND');

            FETCH c_warr_exp_date_sim
             INTO po_latest_pending_date;

            CLOSE c_warr_exp_date_sim;

            OPEN c_pend_model_sim;

            FETCH c_pend_model_sim
             INTO po_latest_pending_model;

            CLOSE c_pend_model_sim;

            OPEN c_warr_extension;

            FETCH c_warr_extension
             INTO po_warranty_extension_date;

            CLOSE c_warr_extension;
         END IF;
      ELSIF v_tech_type_class = 'UIM'
      THEN
         OPEN c_warr_exp_date_uim ('VALD');

         FETCH c_warr_exp_date_uim
          INTO po_warranty_exp_date;

         v_warr_count := c_warr_exp_date_uim%ROWCOUNT;

         CLOSE c_warr_exp_date_uim;

         IF v_warr_count = 0
         THEN
            po_message := 'No valid Warranty exists';
         ELSE
            OPEN c_init_date_uim ('STANDARD');

            FETCH c_init_date_uim
             INTO v_initial_activation_date;

            CLOSE c_init_date_uim;

            po_initial_activation_date := v_initial_activation_date;

            OPEN c_refurb_uim;

            FETCH c_refurb_uim
             INTO v_refurb;

            IF c_refurb_uim%NOTFOUND
            THEN
               po_doa_expiry_date := v_initial_activation_date + 30;
            END IF;

            CLOSE c_refurb_uim;

            OPEN c_init_date_uim ('MANUFACT');

            FETCH c_init_date_uim
             INTO po_initial_manufacture_date;

            CLOSE c_init_date_uim;

            OPEN c_warr_exp_date_uim ('PEND');

            FETCH c_warr_exp_date_uim
             INTO po_latest_pending_date;

            CLOSE c_warr_exp_date_uim;

            OPEN c_pend_model_uim;

            FETCH c_pend_model_uim
             INTO po_latest_pending_model;

            CLOSE c_pend_model_uim;

            OPEN c_warr_extension;

            FETCH c_warr_extension
             INTO po_warranty_extension_date;

            CLOSE c_warr_extension;
         END IF;
      END IF;
   END;

------------------------------------------------------------------------------------------------------------
------------------------------------------------------------------------------------------------------------
   PROCEDURE getequipmentinfobyproductcode (
      pi_product_cd                   IN       VARCHAR2,
      po_product_cd                   OUT      VARCHAR2,
      po_product_id                   OUT      NUMBER,
      po_product_category_id          OUT      NUMBER,
      po_vendor_name                  OUT      VARCHAR2,
      po_vendor_no                    OUT      NUMBER,
      po_product_gp_type_id           OUT      NUMBER,
      po_product_gp_type_cd           OUT      VARCHAR2,
      po_product_gp_type_des          OUT      VARCHAR2,
      po_product_gp_type_des_f        OUT      VARCHAR2,
      po_product_type_id              OUT      NUMBER,
      po_product_type_des             OUT      VARCHAR2,
      po_product_type_des_f           OUT      VARCHAR2,
      po_product_type_kb              OUT      VARCHAR2,
      po_equipment_type_kb            OUT      VARCHAR2,
      po_equipment_type_class         OUT      VARCHAR2,
      po_technology_type              OUT      VARCHAR2,
      po_english_product_name         OUT      VARCHAR2,
      po_french_product_name          OUT      VARCHAR2,
      po_product_class_id             OUT      NUMBER,
      po_product_status_cd            OUT      VARCHAR2,
      po_product_class_cd             OUT      VARCHAR2,
      po_product_class_des            OUT      VARCHAR2,
      po_mode_code                    OUT      NUMBER,
      po_mode_description             OUT      VARCHAR2,
      po_product_type_list            OUT      VARCHAR2,
      po_cross_fleet                  OUT      VARCHAR2,
      po_equipment_status_type_id     OUT      NUMBER,
      po_equipment_status_id          OUT      NUMBER,
      po_equipment_status_dt          OUT      DATE,
      po_browser_version              OUT      VARCHAR2,
      po_firmware_version             OUT      VARCHAR2,
      po_prl_cd                       OUT      VARCHAR2,
      po_prl_des                      OUT      VARCHAR2,
      po_browser_protocol             OUT      VARCHAR2,
      po_firmware_feature_code_list   OUT      VARCHAR2,
      po_brands                       OUT      brand_array,
	  po_equipment_group		      OUT      VARCHAR2,
	  po_source_network_type_cd       OUT      VARCHAR2
   )
   IS
      v_product_record    product_info_full;
      v_tech_type_class   VARCHAR2 (10)     := 'NOT PAGER';
   BEGIN
      getproductinfobyproductcode (pi_product_cd,
                                   v_tech_type_class,
                                   v_product_record
                                  );
      getbrands (pi_product_cd, po_brands);
      po_product_cd := pi_product_cd;
      po_product_id := v_product_record.product_id;
      po_product_category_id := v_product_record.product_category_id;
      po_product_gp_type_id := v_product_record.product_gp_type_id;
      po_vendor_name := v_product_record.vendor_name;
      po_vendor_no := v_product_record.vendor_no;
      po_product_gp_type_id := v_product_record.product_gp_type_id;
      po_product_gp_type_cd := v_product_record.product_gp_type_cd;
      po_product_gp_type_des := v_product_record.product_gp_type_des;
      po_product_gp_type_des_f := v_product_record.product_gp_type_des_f;
      po_product_type_id := v_product_record.product_type_id;
      po_product_type_des := v_product_record.product_type_des;
      po_product_type_des_f := v_product_record.product_type_des_f;
      po_product_type_kb := v_product_record.product_type_kb;
      po_equipment_type_kb := v_product_record.equipment_type_kb;
      po_equipment_type_class := v_product_record.equipment_type_class;
      po_technology_type := v_product_record.technology_type;
      po_english_product_name := v_product_record.english_product_name;
      po_french_product_name := v_product_record.french_product_name;
      po_product_class_id := v_product_record.product_class_id;
      po_product_status_cd := v_product_record.product_status_cd;
      po_product_class_cd := v_product_record.product_class_cd;
      po_product_class_des := v_product_record.product_class_des;
      po_mode_code := v_product_record.mode_code;
      po_mode_description := v_product_record.mode_description;
      po_product_type_list := v_product_record.product_type_list;
      po_equipment_status_type_id := 2;
      po_equipment_status_id := 7;
      po_equipment_status_dt := SYSDATE;
      po_browser_version := v_product_record.browser_version;
      po_firmware_version := v_product_record.firmware_version;
      po_prl_cd := v_product_record.prl_cd;
      po_prl_des := v_product_record.prl_des;
      po_browser_protocol := v_product_record.browser_protocol;
      po_firmware_feature_code_list := v_product_record.firmware_feature_code_list;
      po_equipment_group := v_product_record.equipment_group;
      po_source_network_type_cd := v_product_record.source_network_type_cd;
   EXCEPTION
      WHEN OTHERS
      THEN
         raise_application_error
                 (-20161,
                     'Equipment Info By Product Code Query Failed. Oracle:(['
                  || SQLCODE
                  || '] ['
                  || SQLERRM
                  || '])'
                 );
   END getequipmentinfobyproductcode;

-------------------------------------------------------------------------------
----------------------------------------------------------------------------
   PROCEDURE getproductinfobyproductcode (
      pi_product_cd         IN       VARCHAR2,
      pi_tech_type_class    IN       VARCHAR2,
      ro_product_info_rec   OUT      product_info_full
   )
   IS
      v_product_id              NUMBER (22);

      CURSOR c_equip (p_product_cd VARCHAR2)
      IS
         SELECT pcx.product_type_knowbility, pcx.equip_type_knowbility, pcx.sems_equipment_group,
                et.equip_type_class, p.product_category_id,
                p.technology_type, p.english_product_name,
                p.french_product_name, p.product_id, p.product_class_id,
                NVL (p.product_status_cd, ' ') product_status_cd,
                pc.product_class_cd, pc.product_class_des,pcx.source_network_type_cd
           FROM product p,
                product_class_xref_kb pcx,
                kb_equip_type et,
                product_classification pc
          WHERE p.product_cd = p_product_cd
            AND pcx.technology_type = p.technology_type
            AND pcx.product_class_id = p.product_class_id
            AND pc.product_class_id = pcx.product_class_id
            AND et.equip_type_knowbility(+) = pcx.equip_type_knowbility;

      equip_type_rec            c_equip%ROWTYPE;

      CURSOR c_product_gr_type (p_product_id NUMBER)
      IS
         SELECT ci_g.sub_type_id product_gp_type_id,
                cg.ci_group_cd product_gp_type_cd,
                ci_g.catalogue_item_des product_gp_type_des,
                '' product_gp_type_des_f                  --Not existing feald
                                        ,
                ci_gp.sub_type_id product_type_id,
                ci_gp.catalogue_item_des product_type_des,
                '' product_type_des_f                     --Not existing feald
           FROM catalogue_item ci_p,
                catalogue_item ci_g,
                ci_group cg,
                ci_relationship cr,
                ci_relationship crg,
                catalogue_item ci_gp
          WHERE ci_p.sub_type_id = p_product_id
            AND ci_p.catalogue_item_type_cd = 'PRODUCT'
            AND cg.ci_group_type_cd = 'PROD_EXCL_GRP'
            AND ci_g.sub_type_id = cg.ci_group_id
            AND ci_g.catalogue_item_type_cd = 'GROUP'
            AND cr.catalogue_parent_item_id = ci_g.catalogue_item_id
            AND cr.catalogue_child_item_id = ci_gp.catalogue_item_id
            AND cr.relationship_type_cd = 'SKU_COMP'
            -- changed from GENERICGRP
            AND ci_gp.catalogue_item_type_cd = 'GROUP'
            AND crg.catalogue_parent_item_id = ci_gp.catalogue_item_id
            AND crg.catalogue_child_item_id = ci_p.catalogue_item_id
            AND crg.relationship_type_cd = 'SKU_COMP';

      pr_type_rec               c_product_gr_type%ROWTYPE;

      CURSOR c_pager_equip (p_paging_product_id NUMBER)
      IS
         SELECT pcx.product_type_knowbility, pmt.paging_model_type_cd,
                pmt.description
           FROM paging_model_type pmt,
                product_class_xref_kb pcx,
                product p,
                paging_product pp
          WHERE pmt.paging_model_type_cd = pp.paging_model_type_cd
            AND pcx.technology_type = p.technology_type
            AND p.product_id = pp.product_id
            AND pp.paging_product_id = p_paging_product_id;

      pager_equip_type_rec      c_pager_equip%ROWTYPE;

      CURSOR c_vendor (p_product_id NUMBER, p_exclude CHAR)
      IS
         SELECT m.manufacturer_id, m.manufacturer_name
           FROM product p, manufacturer m
          WHERE p.product_id = p_product_id
            AND m.manufacturer_id = p.manufacturer_id
            AND (       p_exclude = 'Y'
                    AND m.manufacturer_id != '10000119'    -- exclude Clearnet
                    AND m.manufacturer_id != '10007013' -- exclude Misc. Other
                    AND m.manufacturer_id != '10007011'
                 -- exclude Allen Telecom
                 OR p_exclude = 'N'
                );

      vendor_rec                c_vendor%ROWTYPE;

      CURSOR c_legacy (p_product_id NUMBER)
      IS
         SELECT feature_id
           FROM product_feature
          WHERE product_id = p_product_id AND feature_id = 10032685;

      v_feature                 NUMBER (22);

      CURSOR c_mode (pi_product_id NUMBER)
      IS
         SELECT ci_gp.sub_type_id product_type_id,
                ci_gp.catalogue_item_des product_type_des
           FROM catalogue_item ci_p,
                catalogue_item ci_g,
                ci_relationship cr,
                ci_relationship crg,
                catalogue_item ci_gp
          WHERE ci_p.sub_type_id = pi_product_id
            AND ci_p.catalogue_item_type_cd = 'PRODUCT'
            AND ci_g.sub_type_id = 10002686
            AND ci_g.catalogue_item_type_cd = 'GROUP'
            AND cr.catalogue_parent_item_id = ci_g.catalogue_item_id
            AND cr.catalogue_child_item_id = ci_gp.catalogue_item_id
            AND cr.relationship_type_cd = 'GENERICGRP'
            AND ci_gp.catalogue_item_type_cd = 'GROUP'
            AND crg.catalogue_parent_item_id = ci_gp.catalogue_item_id
            AND crg.catalogue_child_item_id = ci_p.catalogue_item_id
            AND crg.relationship_type_cd = 'GENERICGRP';

--Cursor to select Product Service Initiatives for given product
      CURSOR c_promo (pi_product_id NUMBER)
      IS
         SELECT ci_gp.sub_type_id product_type_id
           FROM catalogue_item ci_p,
                catalogue_item ci_g,
                ci_relationship cr,
                ci_relationship crg,
                catalogue_item ci_gp
          WHERE ci_p.sub_type_id = pi_product_id
            AND ci_p.catalogue_item_type_cd = 'PRODUCT'
            AND ci_g.sub_type_id = 10005555
            AND ci_g.catalogue_item_type_cd = 'GROUP'
            AND cr.catalogue_parent_item_id = ci_g.catalogue_item_id
            AND cr.catalogue_child_item_id = ci_gp.catalogue_item_id
            AND cr.relationship_type_cd = 'GENERICGRP'
            AND ci_gp.catalogue_item_type_cd = 'GROUP'
            AND crg.catalogue_parent_item_id = ci_gp.catalogue_item_id
            AND crg.catalogue_child_item_id = ci_p.catalogue_item_id
            AND crg.relationship_type_cd = 'GENERICGRP';

      CURSOR c_browser (p_product_id NUMBER)
      IS
         SELECT fb.product_id, fb.firmware_version, fb.browser_version,
                bv.protocol, fb.prl_cd
           --                pc.prl_des
         FROM   firmware_browser fb, browser_version bv, prl_code pc
          WHERE fb.product_id = p_product_id
            AND fb.default_ind = '1'
            AND fb.browser_version = bv.browser_version;

      --           AND fb.prl_cd = pc.prl_cd;
      browser_rec               c_browser%ROWTYPE;
      v_product_promo_type_id   NUMBER (22);
   BEGIN
      OPEN c_equip (pi_product_cd);

      FETCH c_equip
       INTO equip_type_rec;

      IF c_equip%FOUND
      THEN
         ro_product_info_rec.product_id := equip_type_rec.product_id;
         v_product_id := equip_type_rec.product_id;
         ro_product_info_rec.product_type_kb := equip_type_rec.product_type_knowbility;
         ro_product_info_rec.equipment_type_kb := equip_type_rec.equip_type_knowbility;
         ro_product_info_rec.equipment_type_class := equip_type_rec.equip_type_class;
         ro_product_info_rec.product_category_id := equip_type_rec.product_category_id;
         ro_product_info_rec.technology_type := equip_type_rec.technology_type;
         ro_product_info_rec.english_product_name := equip_type_rec.english_product_name;
         ro_product_info_rec.french_product_name := equip_type_rec.french_product_name;
         ro_product_info_rec.product_class_id := equip_type_rec.product_class_id;
         ro_product_info_rec.product_status_cd := equip_type_rec.product_status_cd;
         ro_product_info_rec.product_class_cd := equip_type_rec.product_class_cd;
         ro_product_info_rec.product_class_des := equip_type_rec.product_class_des;
		 ro_product_info_rec.equipment_group := equip_type_rec.sems_equipment_group;
		 ro_product_info_rec.source_network_type_cd := equip_type_rec.source_network_type_cd;
											 
      ELSE
         RAISE productnotfound;
      END IF;

      CLOSE c_equip;

      IF pi_tech_type_class = 'PAGER'
      THEN
         OPEN c_pager_equip (v_product_id);

         FETCH c_pager_equip
          INTO pager_equip_type_rec;

         IF c_pager_equip%FOUND
         THEN
            ro_product_info_rec.product_type_kb := pager_equip_type_rec.product_type_knowbility;
            ro_product_info_rec.equipment_type_kb := pager_equip_type_rec.paging_model_type_cd;
            ro_product_info_rec.equipment_type_class := pager_equip_type_rec.description;
         END IF;

         CLOSE c_pager_equip;
      ELSE
         OPEN c_product_gr_type (v_product_id);

         FETCH c_product_gr_type
          INTO pr_type_rec;

         IF c_product_gr_type%FOUND
         THEN
            ro_product_info_rec.product_gp_type_id := pr_type_rec.product_gp_type_id;
            ro_product_info_rec.product_gp_type_cd := pr_type_rec.product_gp_type_cd;
            ro_product_info_rec.product_gp_type_des := pr_type_rec.product_gp_type_des;
            ro_product_info_rec.product_gp_type_des_f := pr_type_rec.product_gp_type_des_f;
            ro_product_info_rec.product_type_id := pr_type_rec.product_type_id;
            ro_product_info_rec.product_type_des := pr_type_rec.product_type_des;
            ro_product_info_rec.product_type_des_f := pr_type_rec.product_type_des_f;
         END IF;

         CLOSE c_product_gr_type;
      END IF;

      OPEN c_vendor (v_product_id, 'Y');

      FETCH c_vendor
       INTO vendor_rec;

      IF c_vendor%FOUND
      THEN
         ro_product_info_rec.vendor_no := vendor_rec.manufacturer_id;
         ro_product_info_rec.vendor_name := vendor_rec.manufacturer_name;

         CLOSE c_vendor;
      ELSE
         CLOSE c_vendor;

         OPEN c_vendor (v_product_id, 'N');

         FETCH c_vendor
          INTO vendor_rec;

         IF c_vendor%FOUND
         THEN
            ro_product_info_rec.vendor_no := vendor_rec.manufacturer_id;
            ro_product_info_rec.vendor_name := vendor_rec.manufacturer_name;
         ELSE
            ro_product_info_rec.vendor_no := '0';
            ro_product_info_rec.vendor_name := ' ';
         END IF;

         CLOSE c_vendor;
      END IF;

      OPEN c_legacy (v_product_id);

      FETCH c_legacy
       INTO v_feature;

      IF c_legacy%FOUND
      THEN
         ro_product_info_rec.cross_fleet := 'Y';
      ELSE
         ro_product_info_rec.cross_fleet := 'N';
      END IF;

      CLOSE c_legacy;

      OPEN c_mode (v_product_id);

      FETCH c_mode
       INTO ro_product_info_rec.mode_code,
            ro_product_info_rec.mode_description;

      CLOSE c_mode;

-- Get Promo Product Types
      OPEN c_promo (v_product_id);

      LOOP
         FETCH c_promo
          INTO v_product_promo_type_id;

         EXIT WHEN c_promo%NOTFOUND;
         ro_product_info_rec.product_type_list :=
             ro_product_info_rec.product_type_list || v_product_promo_type_id;
      END LOOP;

      CLOSE c_promo;

      OPEN c_browser (v_product_id);

      LOOP
         FETCH c_browser
          INTO browser_rec;

         EXIT WHEN c_browser%NOTFOUND;
         ro_product_info_rec.browser_version := browser_rec.browser_version;
         ro_product_info_rec.firmware_version := browser_rec.firmware_version;
         ro_product_info_rec.browser_protocol := browser_rec.protocol;
         ro_product_info_rec.prl_cd := browser_rec.prl_cd;
--         ro_product_info_rec.prl_des := browser_rec.prl_des;
      END LOOP;

      CLOSE c_browser;

      getfirmwareversionfeature
                               (ro_product_info_rec.firmware_version,
                                ro_product_info_rec.firmware_feature_code_list
                               );
   EXCEPTION
      WHEN productnotfound
      THEN
         raise_application_error
                                (-20318,
                                 'Client_Equipment pkg: Product ID not found'
                                );
      WHEN OTHERS
      THEN
         raise_application_error
                   (-20161,
                       'Product Info By Product Code Query Failed. Oracle:(['
                    || SQLCODE
                    || '] ['
                    || SQLERRM
                    || '])'
                   );
   END getproductinfobyproductcode;

----------------------------------------------------------------------------------------------------------------
   PROCEDURE getproductidbyproductcode (
      pi_product_cd   IN       VARCHAR2,
      po_product_id   OUT      NUMBER
   )
   IS
      CURSOR c_product (p_product_cd VARCHAR2)
      IS
         SELECT p.product_id
           FROM product p
          WHERE p.product_cd = p_product_cd;
   BEGIN
      OPEN c_product (pi_product_cd);

      FETCH c_product
       INTO po_product_id;

      IF c_product%NOTFOUND
      THEN
         RAISE productnotfound;
      END IF;

      CLOSE c_product;
   EXCEPTION
      WHEN productnotfound
      THEN
         IF c_product%ISOPEN
         THEN
            CLOSE c_product;
         END IF;

         raise_application_error (-20318,
                                  'Client_Equipment pkg: Product ID not found'
                                 );
      WHEN OTHERS
      THEN
         IF c_product%ISOPEN
         THEN
            CLOSE c_product;
         END IF;

         raise_application_error
                      (-20161,
                          'Product Id By Product Code Query Failed. Oracle:(['
                       || SQLCODE
                       || '] ['
                       || SQLERRM
                       || '])'
                      );
   END getproductidbyproductcode;

---------------------------------------------------------------------------------------------------------------------------
   PROCEDURE changeequipmentstatus (
      pi_serial_no                  IN   VARCHAR2,
      pi_user_id                    IN   VARCHAR2,
      pi_equipment_status_type_id   IN   NUMBER,
      pi_equipment_status_id        IN   NUMBER,
      pi_tech_type                  IN   VARCHAR2,
      pi_product_class_id           IN   NUMBER
   )
   IS
      v_equipment_group      VARCHAR2 (20);

      CURSOR c_pcs_eq_status
      IS
         SELECT   equipment_status_type_id, equipment_status_id,
                  equipment_status_dt
             FROM pcs_equip_status
            WHERE serial_no = pi_serial_no
              AND seq_no = 0
              AND TRUNC (equipment_status_dt) <= TRUNC (SYSDATE)
         ORDER BY equipment_status_dt DESC;

      pcs_eq_status_rec      c_pcs_eq_status%ROWTYPE;

      CURSOR c_iden_eq_status
      IS
         SELECT   equipment_status_type_id, equipment_status_id,
                  equipment_status_dt
             FROM iden_equip_status
            WHERE serial_no = pi_serial_no
              AND seq_no = 0
              AND TRUNC (equipment_status_dt) <= TRUNC (SYSDATE)
         ORDER BY equipment_status_dt DESC;

      iden_eq_status_rec     c_iden_eq_status%ROWTYPE;

      CURSOR c_uim_eq_status
      IS
         SELECT   equipment_status_type_id, equipment_status_id,
                  equipment_status_dt
             FROM uim_status
            WHERE iccid = pi_serial_no
              AND seq_no = 0
              AND TRUNC (equipment_status_dt) <= TRUNC (SYSDATE)
         ORDER BY equipment_status_dt DESC;

      uim_eq_status_rec      c_uim_eq_status%ROWTYPE;

      CURSOR c_sim_eq_status
      IS
         SELECT   equipment_status_type_id, equipment_status_id,
                  equipment_status_dt
             FROM sim_status
            WHERE sim_id = pi_serial_no
              AND seq_no = 0
              AND TRUNC (equipment_status_dt) <= TRUNC (SYSDATE)
         ORDER BY equipment_status_dt DESC;

      sim_eq_status_rec      c_sim_eq_status%ROWTYPE;

      CURSOR c_analog_eq_status
      IS
         SELECT   equipment_status_type_id, equipment_status_id,
                  equipment_status_dt
             FROM analog_equip_status
            WHERE serial_no = pi_serial_no
              AND seq_no = 0
              AND TRUNC (equipment_status_dt) <= TRUNC (SYSDATE)
         ORDER BY equipment_status_dt DESC;

      analog_eq_status_rec   c_analog_eq_status%ROWTYPE;

      CURSOR c_paging_eq_status
      IS
         SELECT   equipment_status_type_id, equipment_status_id,
                  equipment_status_dt
             FROM paging_equip_status
            WHERE serial_no = pi_serial_no
              AND seq_no = 0
              AND TRUNC (equipment_status_dt) <= TRUNC (SYSDATE)
         ORDER BY equipment_status_dt DESC;

      paging_eq_status_rec   c_paging_eq_status%ROWTYPE;
   BEGIN
      getequipmentgroup (pi_tech_type, pi_product_class_id,
                         v_equipment_group);

      IF v_equipment_group = 'PCS'
      THEN
         INSERT INTO pcs_equip_status
                     (serial_no, seq_no, equipment_status_type_id,
                      equipment_status_id, equipment_status_dt, load_dt,
                      update_dt, user_last_modify
                     )
              VALUES (pi_serial_no, 0, pi_equipment_status_type_id,
                      pi_equipment_status_id, SYSDATE, SYSDATE,
                      SYSDATE, UPPER (pi_user_id)
                     );

         IF (    pi_equipment_status_type_id = 3
             AND (   pi_equipment_status_id = 11
                  OR pi_equipment_status_id = 56
                  OR pi_equipment_status_id = 57
                 )
            )
         -- insert an additional record
         THEN
            INSERT INTO pcs_equip_status
                        (serial_no, seq_no, equipment_status_type_id,
                         equipment_status_id, equipment_status_dt, load_dt,
                         update_dt, user_last_modify
                        )
                 VALUES (pi_serial_no, 0, 1,
                         6, SYSDATE, SYSDATE,
                         SYSDATE, UPPER (pi_user_id)
                        );
         ELSIF (pi_equipment_status_type_id = 3
                AND pi_equipment_status_id = 60
               )
         -- delete record with status_type_id = 1 and pi_equipment_status_id = 6
         THEN
            FOR pcs_eq_status_rec IN c_pcs_eq_status
            LOOP
               EXIT WHEN c_pcs_eq_status%NOTFOUND;

               IF (    pcs_eq_status_rec.equipment_status_type_id = 1
                   AND pcs_eq_status_rec.equipment_status_id = 6
                  )
               THEN
                  DELETE FROM pcs_equip_status pes
                        WHERE pes.serial_no = pi_serial_no
                          AND pes.seq_no = 0
                          AND pes.equipment_status_type_id =
                                    pcs_eq_status_rec.equipment_status_type_id
                          AND pes.equipment_status_id =
                                         pcs_eq_status_rec.equipment_status_id
                          AND pes.equipment_status_dt =
                                         pcs_eq_status_rec.equipment_status_dt;
               END IF;
            END LOOP;
         END IF;

         COMMIT;
      ELSIF v_equipment_group = 'IDEN'
      THEN
         INSERT INTO iden_equip_status
                     (serial_no, seq_no, equipment_status_type_id,
                      equipment_status_id, equipment_status_dt, load_dt,
                      update_dt, user_last_modify
                     )
              VALUES (pi_serial_no, 0, pi_equipment_status_type_id,
                      pi_equipment_status_id, SYSDATE, SYSDATE,
                      SYSDATE, UPPER (pi_user_id)
                     );

         IF (    pi_equipment_status_type_id = 3
             AND (   pi_equipment_status_id = 11
                  OR pi_equipment_status_id = 56
                  OR pi_equipment_status_id = 57
                 )
            )
         -- insert an additional record
         THEN
            INSERT INTO iden_equip_status
                        (serial_no, seq_no, equipment_status_type_id,
                         equipment_status_id, equipment_status_dt, load_dt,
                         update_dt, user_last_modify
                        )
                 VALUES (pi_serial_no, 0, 1,
                         6, SYSDATE, SYSDATE,
                         SYSDATE, UPPER (pi_user_id)
                        );
         ELSIF (pi_equipment_status_type_id = 3
                AND pi_equipment_status_id = 60
               )
         -- delete record with status_type_id = 1 and pi_equipment_status_id = 6
         THEN
            FOR iden_eq_status_rec IN c_iden_eq_status
            LOOP
               EXIT WHEN c_iden_eq_status%NOTFOUND;

               IF (    iden_eq_status_rec.equipment_status_type_id = 1
                   AND iden_eq_status_rec.equipment_status_id = 6
                  )
               THEN
                  DELETE FROM iden_equip_status ies
                        WHERE ies.serial_no = pi_serial_no
                          AND ies.seq_no = 0
                          AND ies.equipment_status_type_id =
                                   iden_eq_status_rec.equipment_status_type_id
                          AND ies.equipment_status_id =
                                        iden_eq_status_rec.equipment_status_id
                          AND ies.equipment_status_dt =
                                        iden_eq_status_rec.equipment_status_dt;
               END IF;
            END LOOP;
         END IF;

         COMMIT;
      ELSIF v_equipment_group = 'ANA'
      THEN
         INSERT INTO analog_equip_status
                     (serial_no, seq_no, equipment_status_type_id,
                      equipment_status_id, equipment_status_dt, load_dt,
                      update_dt, user_last_modify
                     )
              VALUES (pi_serial_no, 0, pi_equipment_status_type_id,
                      pi_equipment_status_id, SYSDATE, SYSDATE,
                      SYSDATE, UPPER (pi_user_id)
                     );

         IF (    pi_equipment_status_type_id = 3
             AND (   pi_equipment_status_id = 11
                  OR pi_equipment_status_id = 56
                  OR pi_equipment_status_id = 57
                 )
            )
         -- insert an additional record
         THEN
            INSERT INTO analog_equip_status
                        (serial_no, seq_no, equipment_status_type_id,
                         equipment_status_id, equipment_status_dt, load_dt,
                         update_dt, user_last_modify
                        )
                 VALUES (pi_serial_no, 0, 1,
                         6, SYSDATE, SYSDATE,
                         SYSDATE, UPPER (pi_user_id)
                        );
         ELSIF (pi_equipment_status_type_id = 3
                AND pi_equipment_status_id = 60
               )
         -- delete record with status_type_id = 1 and pi_equipment_status_id = 6
         THEN
            FOR analog_eq_status_rec IN c_analog_eq_status
            LOOP
               EXIT WHEN c_analog_eq_status%NOTFOUND;

               IF (    analog_eq_status_rec.equipment_status_type_id = 1
                   AND analog_eq_status_rec.equipment_status_id = 6
                  )
               THEN
                  DELETE FROM analog_equip_status aes
                        WHERE aes.serial_no = pi_serial_no
                          AND aes.seq_no = 0
                          AND aes.equipment_status_type_id =
                                 analog_eq_status_rec.equipment_status_type_id
                          AND aes.equipment_status_id =
                                      analog_eq_status_rec.equipment_status_id
                          AND aes.equipment_status_dt =
                                      analog_eq_status_rec.equipment_status_dt;
               END IF;
            END LOOP;
         END IF;

         COMMIT;
      ELSIF v_equipment_group = 'SIM'
      THEN
         INSERT INTO sim_status
                     (sim_id, seq_no, equipment_status_type_id,
                      equipment_status_id, equipment_status_dt, load_dt,
                      update_dt, user_last_modify
                     )
              VALUES (pi_serial_no, 0, pi_equipment_status_type_id,
                      pi_equipment_status_id, SYSDATE, SYSDATE,
                      SYSDATE, UPPER (pi_user_id)
                     );

         IF (    pi_equipment_status_type_id = 3
             AND (   pi_equipment_status_id = 11
                  OR pi_equipment_status_id = 56
                  OR pi_equipment_status_id = 57
                 )
            )
         -- insert an additional record
         THEN
            INSERT INTO sim_status
                        (sim_id, seq_no, equipment_status_type_id,
                         equipment_status_id, equipment_status_dt, load_dt,
                         update_dt, user_last_modify
                        )
                 VALUES (pi_serial_no, 0, 1,
                         6, SYSDATE, SYSDATE,
                         SYSDATE, UPPER (pi_user_id)
                        );
         ELSIF (pi_equipment_status_type_id = 3
                AND pi_equipment_status_id = 60
               )
         -- delete record with status_type_id = 1 and pi_equipment_status_id = 6
         THEN
            FOR sim_eq_status_rec IN c_sim_eq_status
            LOOP
               EXIT WHEN c_sim_eq_status%NOTFOUND;

               IF (    sim_eq_status_rec.equipment_status_type_id = 1
                   AND sim_eq_status_rec.equipment_status_id = 6
                  )
               THEN
                  DELETE FROM sim_status ss
                        WHERE ss.sim_id = pi_serial_no
                          AND ss.seq_no = 0
                          AND ss.equipment_status_type_id =
                                    sim_eq_status_rec.equipment_status_type_id
                          AND ss.equipment_status_id =
                                         sim_eq_status_rec.equipment_status_id
                          AND ss.equipment_status_dt =
                                         sim_eq_status_rec.equipment_status_dt;
               END IF;
            END LOOP;
         END IF;

         COMMIT;
      ELSIF v_equipment_group = 'UIM'
      THEN
         INSERT INTO uim_status
                     (iccid, seq_no, equipment_status_type_id,
                      equipment_status_id, equipment_status_dt, load_dt,
                      update_dt, user_last_modify
                     )
              VALUES (pi_serial_no, 0, pi_equipment_status_type_id,
                      pi_equipment_status_id, SYSDATE, SYSDATE,
                      SYSDATE, UPPER (pi_user_id)
                     );

         IF (    pi_equipment_status_type_id = 3
             AND (   pi_equipment_status_id = 11
                  OR pi_equipment_status_id = 56
                  OR pi_equipment_status_id = 57
                 )
            )
         -- insert an additional record
         THEN
            INSERT INTO uim_status
                        (iccid, seq_no, equipment_status_type_id,
                         equipment_status_id, equipment_status_dt, load_dt,
                         update_dt, user_last_modify
                        )
                 VALUES (pi_serial_no, 0, 1,
                         6, SYSDATE, SYSDATE,
                         SYSDATE, UPPER (pi_user_id)
                        );
         ELSIF (pi_equipment_status_type_id = 3
                AND pi_equipment_status_id = 60
               )
         -- delete record with status_type_id = 1 and pi_equipment_status_id = 6
         THEN
            FOR uim_eq_status_rec IN c_uim_eq_status
            LOOP
               EXIT WHEN c_uim_eq_status%NOTFOUND;

               IF (    uim_eq_status_rec.equipment_status_type_id = 1
                   AND uim_eq_status_rec.equipment_status_id = 6
                  )
               THEN
                  DELETE FROM uim_status us
                        WHERE us.iccid = pi_serial_no
                          AND us.seq_no = 0
                          AND us.equipment_status_type_id =
                                    uim_eq_status_rec.equipment_status_type_id
                          AND us.equipment_status_id =
                                         uim_eq_status_rec.equipment_status_id
                          AND us.equipment_status_dt =
                                         uim_eq_status_rec.equipment_status_dt;
               END IF;
            END LOOP;
         END IF;

         COMMIT;
      ELSIF v_equipment_group = 'PAGING'
      THEN
         INSERT INTO paging_equip_status
                     (serial_no, seq_no, equipment_status_type_id,
                      equipment_status_id, equipment_status_dt, load_dt,
                      update_dt, user_last_modify
                     )
              VALUES (pi_serial_no, 0, pi_equipment_status_type_id,
                      pi_equipment_status_id, SYSDATE, SYSDATE,
                      SYSDATE, UPPER (pi_user_id)
                     );

         IF (    pi_equipment_status_type_id = 3
             AND (   pi_equipment_status_id = 11
                  OR pi_equipment_status_id = 56
                  OR pi_equipment_status_id = 57
                 )
            )
         -- insert an additional record
         THEN
            INSERT INTO paging_equip_status
                        (serial_no, seq_no, equipment_status_type_id,
                         equipment_status_id, equipment_status_dt, load_dt,
                         update_dt, user_last_modify
                        )
                 VALUES (pi_serial_no, 0, 1,
                         6, SYSDATE, SYSDATE,
                         SYSDATE, UPPER (pi_user_id)
                        );
         ELSIF (pi_equipment_status_type_id = 3
                AND pi_equipment_status_id = 60
               )
         -- delete record with status_type_id = 1 and pi_equipment_status_id = 6
         THEN
            FOR paging_eq_status_rec IN c_paging_eq_status
            LOOP
               EXIT WHEN c_paging_eq_status%NOTFOUND;

               IF (    paging_eq_status_rec.equipment_status_type_id = 1
                   AND paging_eq_status_rec.equipment_status_id = 6
                  )
               THEN
                  DELETE FROM paging_equip_status ps
                        WHERE ps.serial_no = pi_serial_no
                          AND ps.seq_no = 0
                          AND ps.equipment_status_type_id =
                                 paging_eq_status_rec.equipment_status_type_id
                          AND ps.equipment_status_id =
                                      paging_eq_status_rec.equipment_status_id
                          AND ps.equipment_status_dt =
                                      paging_eq_status_rec.equipment_status_dt;
               END IF;
            END LOOP;
         END IF;

         COMMIT;
      END IF;
   EXCEPTION
      WHEN NO_DATA_FOUND
      THEN
         IF (c_pcs_eq_status%ISOPEN)
         THEN
            CLOSE c_pcs_eq_status;
         END IF;

         IF (c_iden_eq_status%ISOPEN)
         THEN
            CLOSE c_iden_eq_status;
         END IF;

         IF (c_uim_eq_status%ISOPEN)
         THEN
            CLOSE c_uim_eq_status;
         END IF;

         IF (c_sim_eq_status%ISOPEN)
         THEN
            CLOSE c_sim_eq_status;
         END IF;

         IF (c_analog_eq_status%ISOPEN)
         THEN
            CLOSE c_analog_eq_status;
         END IF;

         IF (c_paging_eq_status%ISOPEN)
         THEN
            CLOSE c_paging_eq_status;
         END IF;
      WHEN OTHERS
      THEN
         IF (c_pcs_eq_status%ISOPEN)
         THEN
            CLOSE c_pcs_eq_status;
         END IF;

         IF (c_iden_eq_status%ISOPEN)
         THEN
            CLOSE c_iden_eq_status;
         END IF;

         IF (c_uim_eq_status%ISOPEN)
         THEN
            CLOSE c_uim_eq_status;
         END IF;

         IF (c_sim_eq_status%ISOPEN)
         THEN
            CLOSE c_sim_eq_status;
         END IF;

         IF (c_analog_eq_status%ISOPEN)
         THEN
            CLOSE c_analog_eq_status;
         END IF;

         IF (c_paging_eq_status%ISOPEN)
         THEN
            CLOSE c_paging_eq_status;
         END IF;

         RAISE;
   END changeequipmentstatus;

-----------------------------------------------------------------------------------------------------------
----------------------------------------------------------------------------------------------------------------
   PROCEDURE getequipmentgroup (
      pi_tech_type          IN       VARCHAR2,
      pi_product_class_id   IN       NUMBER,
      po_equipment_group    OUT      VARCHAR2
   )
   IS
      CURSOR c_equip_group (p_tech_type VARCHAR2, p_product_class_id NUMBER)
      IS
         SELECT pcx.sems_equipment_group
           FROM product_class_xref_kb pcx
          WHERE pcx.technology_type = p_tech_type
            AND pcx.product_class_id = p_product_class_id;
   BEGIN
      OPEN c_equip_group (pi_tech_type, pi_product_class_id);

      FETCH c_equip_group
       INTO po_equipment_group;

      IF c_equip_group%NOTFOUND
      THEN
         RAISE equipmentgroupnotfound;
      END IF;

      CLOSE c_equip_group;
   EXCEPTION
      WHEN equipmentgroupnotfound
      THEN
         IF c_equip_group%ISOPEN
         THEN
            CLOSE c_equip_group;
         END IF;

         raise_application_error
                            (-20319,
                             'Client_Equipment pkg: Equipment Group not found'
                            );
      WHEN OTHERS
      THEN
         raise_application_error
                              (-20161,
                                  'GetEquipmentGroup Query Failed. Oracle:(['
                               || SQLCODE
                               || '] ['
                               || SQLERRM
                               || '])'
                              );
   END getequipmentgroup;

--------------------------------------------------------------------------------------------------------------

   ----------------------------------------------------------------------------------------------------------------
   PROCEDURE getvirtualesn (
      pi_serial_no    IN       VARCHAR2,
      pi_tech_type    IN       VARCHAR2,
      po_virtual_no   OUT      VARCHAR2
   )
   IS
      CURSOR c_virtual_pcs (p_serial_no VARCHAR2)
      IS
         SELECT ve.serial_no
           FROM virtual_equipment ve,
                product p1,
                pcs_equipment pe,
                product p2
          WHERE ve.product_id = p1.product_id
            AND pe.product_id = p2.product_id
            AND p1.product_class_id = p2.product_class_id
            AND p1.technology_type = p2.technology_type
            AND pe.serial_no = p_serial_no
            AND pe.seq_no = 0;

      CURSOR c_virtual_iden (p_serial_no VARCHAR2)
      IS
         SELECT ve.serial_no
           FROM virtual_equipment ve,
                product p1,
                iden_equipment ie,
                product p2
          WHERE ve.product_id = p1.product_id
            AND ie.product_id = p2.product_id
            AND p1.product_class_id = p2.product_class_id
            AND p1.technology_type = p2.technology_type
            AND ie.serial_no = p_serial_no
            AND ie.seq_no = 0;

      CURSOR c_virtual_sim (p_serial_no VARCHAR2)
      IS
         SELECT ve.serial_no
           FROM virtual_equipment ve, product p1, sim s, product p2
          WHERE ve.product_id = p1.product_id
            AND s.product_id = p2.product_id
            AND p1.product_class_id = p2.product_class_id
            AND p1.technology_type = p2.technology_type
            AND s.sim_id = p_serial_no
            AND s.seq_no = 0;

      CURSOR c_virtual_def (p_tech_type VARCHAR2)
      IS
         SELECT ve.serial_no
           FROM virtual_equipment ve, product p
          WHERE ve.default_ind = 'Y'
            AND ve.product_id = p.product_id
            AND p.technology_type = p_tech_type;
   BEGIN
      IF UPPER (pi_tech_type) = mike_technology_type
      THEN
         OPEN c_virtual_iden (pi_serial_no);

         FETCH c_virtual_iden
          INTO po_virtual_no;

         IF c_virtual_iden%NOTFOUND
         THEN
            OPEN c_virtual_sim (pi_serial_no);

            FETCH c_virtual_sim
             INTO po_virtual_no;

            IF c_virtual_sim%NOTFOUND
            THEN
               OPEN c_virtual_def (pi_tech_type);

               FETCH c_virtual_def
                INTO po_virtual_no;

               IF c_virtual_def%NOTFOUND
               THEN
                  RAISE virtualequipmentnotfound;
               END IF;

               CLOSE c_virtual_def;
            END IF;

            CLOSE c_virtual_sim;
         END IF;

         CLOSE c_virtual_iden;
      ELSE                                         -- non-mike technology_type
         OPEN c_virtual_pcs (pi_serial_no);

         FETCH c_virtual_pcs
          INTO po_virtual_no;

         IF c_virtual_pcs%NOTFOUND
         THEN
            OPEN c_virtual_def (pi_tech_type);

            FETCH c_virtual_def
             INTO po_virtual_no;

            IF c_virtual_def%NOTFOUND
            THEN
               RAISE virtualequipmentnotfound;
            END IF;

            CLOSE c_virtual_def;
         END IF;

         CLOSE c_virtual_pcs;
      END IF;
   EXCEPTION
      WHEN virtualequipmentnotfound
      THEN
         IF c_virtual_pcs%ISOPEN
         THEN
            CLOSE c_virtual_pcs;
         END IF;

         IF c_virtual_iden%ISOPEN
         THEN
            CLOSE c_virtual_iden;
         END IF;

         IF c_virtual_sim%ISOPEN
         THEN
            CLOSE c_virtual_sim;
         END IF;

         IF c_virtual_def%ISOPEN
         THEN
            CLOSE c_virtual_def;
         END IF;

         raise_application_error
                          (-20320,
                           'Client_Equipment pkg: Virtual Equipment not found'
                          );
      WHEN OTHERS
      THEN
         raise_application_error
                              (-20161,
                                  'GetEquipmentGroup Query Failed. Oracle:(['
                               || SQLCODE
                               || '] ['
                               || SQLERRM
                               || '])'
                              );
   END getvirtualesn;

--------------------------------------------------------------------------------------------------------------
      PROCEDURE getvirtualequipmentinfo (
      pi_serial_no                    IN       VARCHAR2,
      pi_tech_type                    IN       VARCHAR2,
      po_serial_no                    OUT      VARCHAR2,
      po_tech_type                    OUT      VARCHAR2,
      po_product_cd                   OUT      VARCHAR2,
      po_product_status_cd            OUT      VARCHAR2,
      po_vendor_name                  OUT      VARCHAR2,
      po_vendor_no                    OUT      VARCHAR2,
      po_equipment_status_type_id     OUT      NUMBER,
      po_equipment_status_id          OUT      NUMBER,
      po_stolen                       OUT      NUMBER,
      po_sublock1                     OUT      VARCHAR2,
      po_product_gp_type_id           OUT      NUMBER,
      po_product_gp_type_cd           OUT      VARCHAR2,
      po_product_gp_type_des          OUT      VARCHAR2,
      po_product_type_id              OUT      NUMBER,
      po_product_type_des             OUT      VARCHAR2,
      po_product_class_id             OUT      NUMBER,
      po_product_class_cd             OUT      VARCHAR2,
      po_product_class_des            OUT      VARCHAR2,
      po_provider_owner_id            OUT      NUMBER,
      po_lastmuleimei_for_sim         OUT      VARCHAR2,
      po_english_product_name         OUT      VARCHAR2,
      po_french_product_name          OUT      VARCHAR2,
      po_browser_version              OUT      VARCHAR2,
      po_firmware_version             OUT      VARCHAR2,
      po_prl_cd                       OUT      VARCHAR2,
      po_prl_des                      OUT      VARCHAR2,
      po_product_type_des_f           OUT      VARCHAR2,
      po_product_gp_type_des_f        OUT      VARCHAR2,
      po_min_cd                       OUT      VARCHAR2,
      po_customer_id                  OUT      NUMBER,
      po_product_type_list            OUT      VARCHAR2,
      po_initial_activation           OUT      VARCHAR2,
      po_mode_code                    OUT      NUMBER,
      po_mode_description             OUT      VARCHAR2,
      po_product_type                 OUT      VARCHAR2,
      po_equipment_type               OUT      VARCHAR2,
      po_equipment_type_class         OUT      VARCHAR2,
      po_cross_fleet                  OUT      VARCHAR2,
      po_cost                         OUT      NUMBER,
      po_cap_code                     OUT      VARCHAR2,
      po_coverage_region_code_list    OUT      VARCHAR2,
      po_encoding_format_code         OUT      VARCHAR2,
      po_ownership_code               OUT      VARCHAR2,
      po_prepaid                      OUT      VARCHAR2,
      po_frequency_cd                 OUT      VARCHAR2,
      po_firmware_feature_code_list   OUT      VARCHAR2,
      po_browser_protocol             OUT      VARCHAR2,
      po_unscanned                    OUT      NUMBER,
      po_equip_status_dt              OUT      DATE,
      po_puk                          OUT      VARCHAR2,
      po_product_category_id          OUT      NUMBER,
      po_virtual                      OUT      NUMBER,
      po_rim_pin                      OUT      VARCHAR2,
      po_brands                       OUT      brand_array,
	  po_equipment_group			  OUT	   VARCHAR2,
	  po_source_network_type_cd       OUT	   VARCHAR2
   )
   IS
      v_initial_activation   BOOLEAN := TRUE;
	  v_local_imsi VARCHAR2 (2);
  	  v_remote_imsi VARCHAR2 (2);
  	  v_assohandsetimei_for_usim VARCHAR2 (2);
	  v_assignable NUMBER;
	  v_previously_activated NUMBER;
      v_last_event_type usim_pcs_device_assoc.event_type%TYPE;
      v_pre_post_paid_flag VARCHAR2 (2);
   BEGIN
      getvirtualesn (pi_serial_no, po_serial_no);
      getequipmentinfo (po_serial_no,
                        'VIRTUAL',                       -- temporary for test
                        v_initial_activation,
                        po_tech_type,
                        po_product_cd,
                        po_product_status_cd,
                        po_vendor_name,
                        po_vendor_no,
                        po_equipment_status_type_id,
                        po_equipment_status_id,
                        po_stolen,
                        po_sublock1,
                        po_product_gp_type_id,
                        po_product_gp_type_cd,
                        po_product_gp_type_des,
                        po_product_type_id,
                        po_product_type_des,
                        po_product_class_id,
                        po_product_class_cd,
                        po_product_class_des,
                        po_provider_owner_id,
                        po_lastmuleimei_for_sim,
                        po_english_product_name,
                        po_french_product_name,
                        po_browser_version,
                        po_firmware_version,
                        po_prl_cd,
                        po_prl_des,
                        po_product_type_des_f,
                        po_product_gp_type_des_f,
                        po_min_cd,
                        po_customer_id,
                        po_product_type_list,
                        po_initial_activation,
                        po_mode_code,
                        po_mode_description,
                        po_product_type,
                        po_equipment_type,
                        po_equipment_type_class,
                        po_cross_fleet,
                        po_cost,
                        po_cap_code,
                        po_coverage_region_code_list,
                        po_encoding_format_code,
                        po_ownership_code,
                        po_prepaid,
                        po_frequency_cd,
                        po_firmware_feature_code_list,
                        po_browser_protocol,
                        po_unscanned,
                        po_equip_status_dt,
                        po_puk,
                        po_product_category_id,
                        po_virtual,
                        po_rim_pin,
                        po_brands,
						v_assohandsetimei_for_usim,
						v_local_imsi,
						v_remote_imsi,
						v_assignable,
                        v_previously_activated,
						po_equipment_group,
                        v_last_event_type,
                        po_source_network_type_cd,
                        v_pre_post_paid_flag
                       );
   EXCEPTION
      WHEN esnnotfound
      THEN
         raise_application_error
                 (-20310,
                  'Client_Equipment pkg: Virtual ESN or Product ID not found');
      WHEN OTHERS
      THEN
         RAISE;
--    RAISE_APPLICATION_ERROR(SQLCODE,SQLERRM);
   END getvirtualequipmentinfo;

     -----------------------------------------------------------------------------------
---------------------------------------------------------------------------------
   PROCEDURE getequipmentinfocheckpseudo (
      pi_serial_no                    IN       VARCHAR2,
      po_serial_no                    OUT      VARCHAR2,
      po_tech_type                    OUT      VARCHAR2,
      po_product_cd                   OUT      VARCHAR2,
      po_product_status_cd            OUT      VARCHAR2,
      po_vendor_name                  OUT      VARCHAR2,
      po_vendor_no                    OUT      VARCHAR2,
      po_equipment_status_type_id     OUT      NUMBER,
      po_equipment_status_id          OUT      NUMBER,
      po_stolen                       OUT      NUMBER,
      po_sublock1                     OUT      VARCHAR2,
      po_product_gp_type_id           OUT      NUMBER,
      po_product_gp_type_cd           OUT      VARCHAR2,
      po_product_gp_type_des          OUT      VARCHAR2,
      po_product_type_id              OUT      NUMBER,
      po_product_type_des             OUT      VARCHAR2,
      po_product_class_id             OUT      NUMBER,
      po_product_class_cd             OUT      VARCHAR2,
      po_product_class_des            OUT      VARCHAR2,
      po_provider_owner_id            OUT      NUMBER,
      po_lastmuleimei_for_sim         OUT      VARCHAR2,
      po_english_product_name         OUT      VARCHAR2,
      po_french_product_name          OUT      VARCHAR2,
      po_browser_version              OUT      VARCHAR2,
      po_firmware_version             OUT      VARCHAR2,
      po_prl_cd                       OUT      VARCHAR2,
      po_prl_des                      OUT      VARCHAR2,
      po_product_type_des_f           OUT      VARCHAR2,
      po_product_gp_type_des_f        OUT      VARCHAR2,
      po_min_cd                       OUT      VARCHAR2,
      po_customer_id                  OUT      NUMBER,
      po_product_type_list            OUT      VARCHAR2,
      po_initial_activation           OUT      VARCHAR2,
      po_mode_code                    OUT      NUMBER,
      po_mode_description             OUT      VARCHAR2,
      po_product_type                 OUT      VARCHAR2,
      po_equipment_type               OUT      VARCHAR2,
      po_equipment_type_class         OUT      VARCHAR2,
      po_cross_fleet                  OUT      VARCHAR2,
      po_cost                         OUT      NUMBER,
      po_cap_code                     OUT      VARCHAR2,
      po_coverage_region_code_list    OUT      VARCHAR2,
      po_encoding_format_code         OUT      VARCHAR2,
      po_ownership_code               OUT      VARCHAR2,
      po_prepaid                      OUT      VARCHAR2,
      po_frequency_cd                 OUT      VARCHAR2,
      po_firmware_feature_code_list   OUT      VARCHAR2,
      po_browser_protocol             OUT      VARCHAR2,
      po_unscanned                    OUT      NUMBER,
      po_equip_status_dt              OUT      DATE,
      po_puk                          OUT      VARCHAR2,
      po_product_category_id          OUT      NUMBER,
      po_virtual                      OUT      NUMBER,
      po_rim_pin                      OUT      VARCHAR2,
      po_brands                       OUT      brand_array,
      po_source_network_type_cd       OUT      VARCHAR2
   )
   IS
      v_tech_type_class      VARCHAR2 (10);
      v_initial_activation   BOOLEAN       := TRUE;
      v_serial_no            VARCHAR2 (20);
	  v_local_imsi VARCHAR2 (2);
  	  v_remote_imsi VARCHAR2 (2);
  	  v_assohandsetimei_for_usim VARCHAR2 (2);
	  v_assignable NUMBER;
	  v_previously_activated NUMBER;
	  v_equipment_group VARCHAR2 (20);
      v_last_event_type usim_pcs_device_assoc.event_type%TYPE;
      v_pre_post_paid_flag VARCHAR2 (2);
   --     v_check_if_pseudo      BOOLEAN       := FALSE;
   BEGIN
      po_serial_no := pi_serial_no;
      v_serial_no := pi_serial_no;

      BEGIN
         gettechnologytypeclass (pi_serial_no, v_tech_type_class);
      EXCEPTION
         WHEN esnnotfound
         THEN
            getesnbypseudoesn (pi_serial_no, po_serial_no);
            gettechnologytypeclass (po_serial_no, v_tech_type_class);
            v_serial_no := po_serial_no;
         WHEN OTHERS
         THEN
            RAISE;
      END;

      getequipmentinfo (v_serial_no,
                        v_tech_type_class,
                        v_initial_activation,
                        po_tech_type,
                        po_product_cd,
                        po_product_status_cd,
                        po_vendor_name,
                        po_vendor_no,
                        po_equipment_status_type_id,
                        po_equipment_status_id,
                        po_stolen,
                        po_sublock1,
                        po_product_gp_type_id,
                        po_product_gp_type_cd,
                        po_product_gp_type_des,
                        po_product_type_id,
                        po_product_type_des,
                        po_product_class_id,
                        po_product_class_cd,
                        po_product_class_des,
                        po_provider_owner_id,
                        po_lastmuleimei_for_sim,
                        po_english_product_name,
                        po_french_product_name,
                        po_browser_version,
                        po_firmware_version,
                        po_prl_cd,
                        po_prl_des,
                        po_product_type_des_f,
                        po_product_gp_type_des_f,
                        po_min_cd,
                        po_customer_id,
                        po_product_type_list,
                        po_initial_activation,
                        po_mode_code,
                        po_mode_description,
                        po_product_type,
                        po_equipment_type,
                        po_equipment_type_class,
                        po_cross_fleet,
                        po_cost,
                        po_cap_code,
                        po_coverage_region_code_list,
                        po_encoding_format_code,
                        po_ownership_code,
                        po_prepaid,
                        po_frequency_cd,
                        po_firmware_feature_code_list,
                        po_browser_protocol,
                        po_unscanned,
                        po_equip_status_dt,
                        po_puk,
                        po_product_category_id,
                        po_virtual,
                        po_rim_pin,
                        po_brands,
						v_assohandsetimei_for_usim,
						v_local_imsi,
						v_remote_imsi,
						v_assignable,
                        v_previously_activated,
						v_equipment_group,
                        v_last_event_type,
                        po_source_network_type_cd,
                        v_pre_post_paid_flag
                       );
      getbrands (po_product_cd, po_brands);
   EXCEPTION
      WHEN esnnotfound
      THEN
         raise_application_error
                         (-20310,
                          'Client_Equipment pkg: ESN or Product ID not found');
      WHEN OTHERS
      THEN
         RAISE;
   END getequipmentinfocheckpseudo;

--------------------------------------------------------------------------------
   FUNCTION getallesnbypseudoesn (pi_pseudo_serial_no IN VARCHAR2)
      RETURN esn_tbl_t
   IS
      serial_numbers   esn_tbl_t := esn_tbl_t ();
   BEGIN
      SELECT DISTINCT serial_no
      BULK COLLECT INTO serial_numbers
        FROM pcs_equipment
       WHERE alt_serial_no = pi_pseudo_serial_no
       	 AND serial_no <> pi_pseudo_serial_no;

      RETURN serial_numbers;
   END;

----------------------------------------------------------------------------------
   PROCEDURE getesnbypseudoesn (
      pi_pseudo_serial_no   IN       VARCHAR2,
      po_serial_no          OUT      VARCHAR2
   )
   IS
      CURSOR c_pseudo_esn (p_pseudo_serial_no VARCHAR2)
      IS
         SELECT pe.serial_no
           FROM pcs_equipment pe
          WHERE pe.alt_serial_no = p_pseudo_serial_no;
   BEGIN
      OPEN c_pseudo_esn (pi_pseudo_serial_no);

      FETCH c_pseudo_esn
       INTO po_serial_no;

      CLOSE c_pseudo_esn;
   EXCEPTION
      WHEN esnnotfound
      THEN
         CLOSE c_pseudo_esn;

         raise_application_error
                         (-20310,
                          'Client_Equipment pkg: ESN or Product ID not found'
                         );
      WHEN OTHERS
      THEN
         CLOSE c_pseudo_esn;

         raise_application_error
                              (-20310,
                                  'ESN By Pseudo ESN Query Failed. Oracle:(['
                               || SQLCODE
                               || '] ['
                               || SQLERRM
                               || '])'
                              );
   END;                                                            -- Function

   PROCEDURE checkifesnvirtual (
      pi_serial_no    IN       VARCHAR2,
      po_is_virtual   OUT      NUMBER
   )
   IS
      v_virtual_no   NUMBER (18);

      CURSOR c_virtual_no (p_serial_no VARCHAR2)
      IS
         SELECT ve.serial_no
           FROM virtual_equipment ve
          WHERE ve.serial_no = p_serial_no;
   BEGIN
      OPEN c_virtual_no (pi_serial_no);

      FETCH c_virtual_no
       INTO v_virtual_no;

      IF c_virtual_no%FOUND
      THEN
         po_is_virtual := 1;
      END IF;

      CLOSE c_virtual_no;
   EXCEPTION
      WHEN OTHERS
      THEN
         IF c_virtual_no%ISOPEN
         THEN
            CLOSE c_virtual_no;
         END IF;

         raise_application_error
                               (-20161,
                                   'checkifesnvirtual Query Failed. Oracle:(['
                                || SQLCODE
                                || '] ['
                                || SQLERRM
                                || '])'
                               );
   END checkifesnvirtual;

   PROCEDURE getequipmentinfobycapcode (
      pi_cap_cd                       IN       VARCHAR2,
      pi_encoding_format              IN       VARCHAR2,
      po_serial_no                    OUT      VARCHAR2,
      po_tech_type                    OUT      VARCHAR2,
      po_product_cd                   OUT      VARCHAR2,
      po_product_status_cd            OUT      VARCHAR2,
      po_vendor_name                  OUT      VARCHAR2,
      po_vendor_no                    OUT      VARCHAR2,
      po_equipment_status_type_id     OUT      NUMBER,
      po_equipment_status_id          OUT      NUMBER,
      po_stolen                       OUT      NUMBER,
      po_sublock1                     OUT      VARCHAR2,
      po_product_gp_type_id           OUT      NUMBER,
      po_product_gp_type_cd           OUT      VARCHAR2,
      po_product_gp_type_des          OUT      VARCHAR2,
      po_product_type_id              OUT      NUMBER,
      po_product_type_des             OUT      VARCHAR2,
      po_product_class_id             OUT      NUMBER,
      po_product_class_cd             OUT      VARCHAR2,
      po_product_class_des            OUT      VARCHAR2,
      po_provider_owner_id            OUT      NUMBER,
      po_lastmuleimei_for_sim         OUT      VARCHAR2,
      po_english_product_name         OUT      VARCHAR2,
      po_french_product_name          OUT      VARCHAR2,
      po_browser_version              OUT      VARCHAR2,
      po_firmware_version             OUT      VARCHAR2,
      po_prl_cd                       OUT      VARCHAR2,
      po_prl_des                      OUT      VARCHAR2,
      po_product_type_des_f           OUT      VARCHAR2,
      po_product_gp_type_des_f        OUT      VARCHAR2,
      po_min_cd                       OUT      VARCHAR2,
      po_customer_id                  OUT      NUMBER,
      po_product_type_list            OUT      VARCHAR2,
      po_initial_activation           OUT      VARCHAR2,
      po_mode_code                    OUT      NUMBER,
      po_mode_description             OUT      VARCHAR2,
      po_product_type                 OUT      VARCHAR2,
      po_equipment_type               OUT      VARCHAR2,
      po_equipment_type_class         OUT      VARCHAR2,
      po_cross_fleet                  OUT      VARCHAR2,
      po_cost                         OUT      NUMBER,
      po_cap_code                     OUT      VARCHAR2,
      po_coverage_region_code_list    OUT      VARCHAR2,
      po_encoding_format_code         OUT      VARCHAR2,
      po_ownership_code               OUT      VARCHAR2,
      po_prepaid                      OUT      VARCHAR2,
      po_frequency_cd                 OUT      VARCHAR2,
      po_firmware_feature_code_list   OUT      VARCHAR2,
      po_browser_protocol             OUT      VARCHAR2,
      po_unscanned                    OUT      NUMBER,
      po_equip_status_dt              OUT      DATE,
      po_puk                          OUT      VARCHAR2,
      po_product_category_id          OUT      NUMBER,
      po_virtual                      OUT      NUMBER,
      po_brands                       OUT      brand_array,
      po_source_network_type_cd       OUT      VARCHAR2
   )
   IS
      CURSOR c_equip_pager (p_cap_cd VARCHAR2, p_encoding_format VARCHAR2)
      IS
         SELECT   p.product_id, p.product_cd,
                  NVL (p.product_status_cd, ' ') product_status_cd,
                  p.technology_type, p.english_product_name,
                  p.french_product_name, pe.min_cd, pe.customer_id,
                  pe.provider_owner_id, pe.serial_no,
                  pes.equipment_status_type_id, pes.equipment_status_id,
                  pes.equipment_status_dt, pc.product_class_id,
                  pc.product_class_cd, pc.product_class_des, pe.cap_code,
                  pp.encoder_format_cd, pp.paging_product_id,
                  pe.frequency_id, pf.frequency_cd,pc_xref_kb.source_network_type_cd
             FROM paging_equip pe,
                  product p,
                  paging_equip_status pes,
                  product_classification pc,
                  paging_product pp,
                  paging_frequency pf,
                  product_class_xref_kb pc_xref_kb
            WHERE pe.cap_code = p_cap_cd
              AND pp.encoder_format_cd = p_encoding_format
              AND pe.seq_no = 0
              AND pe.product_id = p.product_id
              AND pe.serial_no = pes.serial_no(+)
              AND pe.seq_no = pes.seq_no(+)
              AND pes.equipment_status_dt(+) <= SYSDATE
              AND pc.product_class_id = p.product_class_id
              AND pp.paging_product_id = pe.paging_product_id
              AND pe.frequency_id = pf.frequency_id
              AND pc_xref_kb.technology_type = p.technology_type
              AND pc_xref_kb.product_class_id = pc.product_class_id
         ORDER BY pes.equipment_status_dt DESC;

--
      pager_rec                 c_equip_pager%ROWTYPE;
      v_paging_product_id       NUMBER (22);

--
      CURSOR c_paging_equip_status (p_serial_no VARCHAR2)
      IS
         SELECT   equipment_status_type_id, equipment_status_id
             FROM paging_equip_status
            WHERE serial_no = p_serial_no
              AND seq_no = 0
              AND TRUNC (equipment_status_dt) <= TRUNC (SYSDATE)
         ORDER BY equipment_status_dt DESC;

      v_initial_activation      CHAR (1)                                := 'Y';

--
      CURSOR c_mode (p_product_id NUMBER)
      IS
         SELECT ci_gp.sub_type_id product_type_id,
                ci_gp.catalogue_item_des product_type_des
           FROM catalogue_item ci_p,
                catalogue_item ci_g,
                ci_relationship cr,
                ci_relationship crg,
                catalogue_item ci_gp
          WHERE ci_p.sub_type_id = p_product_id
            AND ci_p.catalogue_item_type_cd = 'PRODUCT'
            AND ci_g.sub_type_id = 10002686
            AND ci_g.catalogue_item_type_cd = 'GROUP'
            AND cr.catalogue_parent_item_id = ci_g.catalogue_item_id
            AND cr.catalogue_child_item_id = ci_gp.catalogue_item_id
            AND cr.relationship_type_cd = 'GENERICGRP'
            AND ci_gp.catalogue_item_type_cd = 'GROUP'
            AND crg.catalogue_parent_item_id = ci_gp.catalogue_item_id
            AND crg.catalogue_child_item_id = ci_p.catalogue_item_id
            AND crg.relationship_type_cd = 'GENERICGRP';

--
      CURSOR c_paging_ownership (p_serial_no VARCHAR2)
      IS
         SELECT   DECODE (equipment_status_id, 140, 'R', 'P')
             FROM paging_equip_status
            WHERE serial_no = p_serial_no
              AND seq_no = 0
              AND TRUNC (equipment_status_dt) <= TRUNC (SYSDATE)
              AND equipment_status_type_id = 12
         ORDER BY equipment_status_dt DESC;

--
      CURSOR c_paging_prepaid (p_product_id NUMBER)
      IS
         SELECT 'Y'
           FROM product_feature
          WHERE product_id = p_product_id AND feature_id = 10002392;

--
      CURSOR c_paging_coverage_region_list (p_frequency_id NUMBER)
      IS
         SELECT   coverage_region_id
             FROM coverage_region
            WHERE frequency_id = p_frequency_id
              AND province_cd IN ('BC', 'AB')
              AND coverage_type IS NOT NULL
         ORDER BY coverage_region_id DESC;

--
      v_coverage_region_id      coverage_region.coverage_region_id%TYPE;
--
      v_product_rec             product_info_record;

--Cursor to select Product Service Initiatives for given product
      CURSOR c_promo (p_product_id NUMBER)
      IS
         SELECT ci_gp.sub_type_id product_type_id
           FROM catalogue_item ci_p,
                catalogue_item ci_g,
                ci_relationship cr,
                ci_relationship crg,
                catalogue_item ci_gp
          WHERE ci_p.sub_type_id = p_product_id
            AND ci_p.catalogue_item_type_cd = 'PRODUCT'
            AND ci_g.sub_type_id = 10005555
            AND ci_g.catalogue_item_type_cd = 'GROUP'
            AND cr.catalogue_parent_item_id = ci_g.catalogue_item_id
            AND cr.catalogue_child_item_id = ci_gp.catalogue_item_id
            AND cr.relationship_type_cd = 'GENERICGRP'
            AND ci_gp.catalogue_item_type_cd = 'GROUP'
            AND crg.catalogue_parent_item_id = ci_gp.catalogue_item_id
            AND crg.catalogue_child_item_id = ci_p.catalogue_item_id
            AND crg.relationship_type_cd = 'GENERICGRP';

      v_product_promo_type_id   NUMBER (22);
      v_product_id              NUMBER (22);
      v_tech_type_class         VARCHAR2 (10);
   BEGIN
      po_virtual := 0;

      OPEN c_equip_pager (pi_cap_cd, pi_encoding_format);

      FETCH c_equip_pager
       INTO pager_rec;

      IF c_equip_pager%NOTFOUND
      THEN
         RAISE esnnotfound;
      END IF;

      CLOSE c_equip_pager;

      po_serial_no := pager_rec.serial_no;
      v_product_id := pager_rec.product_id;
      v_paging_product_id := pager_rec.paging_product_id;
      po_product_cd := pager_rec.product_cd;
      po_product_status_cd := pager_rec.product_status_cd;
      po_tech_type := pager_rec.technology_type;
      po_provider_owner_id := pager_rec.provider_owner_id;
      po_equipment_status_type_id := pager_rec.equipment_status_type_id;
      po_equipment_status_id := pager_rec.equipment_status_id;
      po_equip_status_dt := pager_rec.equipment_status_dt;
      po_sublock1 := ' ';
      po_lastmuleimei_for_sim := ' ';
      po_browser_version := ' ';
      po_firmware_version := ' ';
      po_browser_protocol := ' ';
      po_prl_cd := ' ';
      po_prl_des := ' ';
      po_product_class_id := pager_rec.product_class_id;
      po_product_class_cd := pager_rec.product_class_cd;
      po_product_class_des := pager_rec.product_class_des;
      po_english_product_name := pager_rec.english_product_name;
      po_french_product_name := pager_rec.french_product_name;
      po_min_cd := pager_rec.min_cd;
      po_customer_id := pager_rec.customer_id;
      po_cap_code := pager_rec.cap_code;
      po_encoding_format_code := pager_rec.encoder_format_cd;
      po_frequency_cd := pager_rec.frequency_cd;
      po_stolen := 0;
      po_source_network_type_cd := pager_rec.source_network_type_cd;

      IF        pager_rec.equipment_status_type_id = 1
            AND pager_rec.equipment_status_id = 6
         OR (    pager_rec.equipment_status_type_id = 11
             AND pager_rec.equipment_status_id = 127
            )
         OR (    pager_rec.equipment_status_type_id = 3
             AND pager_rec.equipment_status_id = 11
            )
         OR (    pager_rec.equipment_status_type_id = 3
             AND pager_rec.equipment_status_id = 56
            )
         OR (    pager_rec.equipment_status_type_id = 3
             AND pager_rec.equipment_status_id = 57
            )
      THEN
         po_stolen := 1;
      ELSE
         FOR equip_rec IN c_paging_equip_status (pager_rec.serial_no)
         LOOP
            EXIT WHEN c_paging_equip_status%NOTFOUND;

            IF (   equip_rec.equipment_status_type_id = 1
                OR equip_rec.equipment_status_type_id = 3
                OR equip_rec.equipment_status_type_id = 11
               )
            THEN
               IF    (    equip_rec.equipment_status_type_id = 1
                      AND equip_rec.equipment_status_id = 6
                     )
                  OR (    equip_rec.equipment_status_type_id = 11
                      AND equip_rec.equipment_status_id = 127
                     )
                  OR (    equip_rec.equipment_status_type_id = 3
                      AND equip_rec.equipment_status_id = 11
                     )
                  OR (    equip_rec.equipment_status_type_id = 3
                      AND equip_rec.equipment_status_id = 56
                     )
                  OR (    equip_rec.equipment_status_type_id = 3
                      AND equip_rec.equipment_status_id = 57
                     )
               THEN
                  po_stolen := 1;
               END IF;

               EXIT;
            END IF;
         END LOOP;
      END IF;

      IF (    pager_rec.equipment_status_type_id = 2
          AND pager_rec.equipment_status_id = 8
         )
      THEN
         po_initial_activation := 'N';
      END IF;

      IF v_initial_activation = 'Y'
      THEN
         FOR equip_rec IN c_paging_equip_status (pager_rec.serial_no)
         LOOP
            EXIT WHEN c_paging_equip_status%NOTFOUND;

            IF (    equip_rec.equipment_status_type_id = 2
                AND equip_rec.equipment_status_id = 8
               )
            THEN
               v_initial_activation := 'N';
               EXIT;
            ELSIF (    equip_rec.equipment_status_type_id = 1
                   AND equip_rec.equipment_status_id = 3
                  )
            THEN
               v_initial_activation := 'Y';
               EXIT;
            END IF;
         END LOOP;

         IF v_initial_activation = 'O'
         THEN
            po_initial_activation := 'Y';
         ELSE
            po_initial_activation := v_initial_activation;
         END IF;
      END IF;

      getproductinfo (v_paging_product_id, 'PAGER', v_product_rec);
      po_product_gp_type_id := v_product_rec.product_gp_type_id;
      po_product_gp_type_cd := v_product_rec.product_gp_type_cd;
      po_product_gp_type_des := v_product_rec.product_gp_type_des;
      po_product_gp_type_des_f := v_product_rec.product_gp_type_des_f;
      po_product_type_id := v_product_rec.product_type_id;
      po_product_type_des := v_product_rec.product_type_des;
      po_product_type_des_f := v_product_rec.product_type_des_f;
      po_vendor_no := v_product_rec.vendor_no;
      po_vendor_name := v_product_rec.vendor_name;
      po_product_type := v_product_rec.product_type_kb;
      po_equipment_type := v_product_rec.equipment_type_kb;
      po_equipment_type_class := v_product_rec.equipment_type_class;
      po_cross_fleet := v_product_rec.cross_fleet;
      po_product_category_id := v_product_rec.product_category_id;
      getbrands (po_product_cd, po_brands);

      -- get ownership (Rental R or Private P)
      OPEN c_paging_ownership (pager_rec.serial_no);

      FETCH c_paging_ownership
       INTO po_ownership_code;

      IF c_paging_ownership%NOTFOUND
      THEN
         po_ownership_code := 'P';
      END IF;

      CLOSE c_paging_ownership;

      -- check prepaid indicator
      OPEN c_paging_prepaid (pager_rec.product_id);

      FETCH c_paging_prepaid
       INTO po_prepaid;

      IF c_paging_prepaid%NOTFOUND
      THEN
         po_prepaid := 'N';
      END IF;

      CLOSE c_paging_prepaid;

      -- get Coverage Region List
      v_coverage_region_id := '';

      OPEN c_paging_coverage_region_list (pager_rec.frequency_id);

      LOOP
         FETCH c_paging_coverage_region_list
          INTO v_coverage_region_id;

         EXIT WHEN c_paging_coverage_region_list%NOTFOUND;
         po_coverage_region_code_list :=
                  po_coverage_region_code_list || v_coverage_region_id || '|';
      END LOOP;

      po_coverage_region_code_list :=
         SUBSTR (po_coverage_region_code_list,
                 1,
                 INSTR (po_coverage_region_code_list, '|', -1) - 1
                );

      CLOSE c_paging_coverage_region_list;

      OPEN c_mode (pager_rec.product_id);

      FETCH c_mode
       INTO po_mode_code, po_mode_description;

      CLOSE c_mode;

      -- check if unscanned - If for status type 4 latest  status is 63, then equipment is "un-scanned"
      v_equipment_status_type_id := equipment_state;
      v_equipment_status_id := walmart_lock;
      po_unscanned := numeric_false;
      v_tech_type_class := 'PAGER';
      checklateststatus (po_serial_no,
                         v_tech_type_class,
                         v_equipment_status_type_id,
                         v_equipment_status_id,
                         v_recordexist
                        );

      IF (v_recordexist)
      THEN
         po_unscanned := numeric_true;
      END IF;
   EXCEPTION
      WHEN esnnotfound
      THEN
         raise_application_error
                         (-20310,
                          'Client_Equipment pkg: ESN or Product ID not found'
                         );
      WHEN OTHERS
      THEN
         RAISE;
   END getequipmentinfobycapcode;

   PROCEDURE getfeaturecodes (
      pi_product_cd      IN       VARCHAR2,
      po_feature_codes   OUT      refcursor
   )
   IS
   BEGIN
------------------------------------------------------------------
-- Create reference to a cursor  using SQL statement
------------------------------------------------------------------
      OPEN po_feature_codes
       FOR
          SELECT f.feature_cd
            FROM product p, feature f, product_feature pf
           WHERE p.product_cd = pi_product_cd
             AND p.product_id = pf.product_id
             AND pf.feature_id = f.feature_id
          UNION
          SELECT DISTINCT f.feature_cd
                     FROM product p,
                          firmware_browser fb,
                          firmware_version fv,
                          firmware_version_feature fvf,
                          feature f
                    WHERE p.product_id = fb.product_id
                      AND fb.firmware_version = fv.firmware_version
                      AND fv.firmware_version = fvf.firmware_version
                      AND fvf.feature_id = f.feature_id
                      AND p.product_cd = pi_product_cd;
   EXCEPTION
      WHEN NO_DATA_FOUND
      THEN
         IF (po_feature_codes%ISOPEN)
         THEN
            CLOSE po_feature_codes;
         END IF;

         -- return NULL cursor
         OPEN po_feature_codes
          FOR
             SELECT NULL
               FROM DUAL
              WHERE 1 = 0;
      WHEN OTHERS
      THEN
         IF (po_feature_codes%ISOPEN)
         THEN
            CLOSE po_feature_codes;
         END IF;

         raise_application_error
                               (-20160,
                                   'Get feature codes Query Failed. Oracle:(['
                                || SQLCODE
                                || '] ['
                                || SQLERRM
                                || '])'
                               );
   END getfeaturecodes;

-------------------------------------------------------------------------------------------------------------
   PROCEDURE getproductmodes (
      pi_product_cd      IN       VARCHAR2,
      po_product_modes   OUT      refcursor
   )
   IS
      v_product_id   NUMBER (22, 0);
   BEGIN
------------------------------------------------------------------
-- Create reference to a cursor  using SQL statement
------------------------------------------------------------------
      getproductidbyproductcode (pi_product_cd, v_product_id);

      OPEN po_product_modes
       FOR
          SELECT cg.ci_group_cd handset_mode,
                 ci_g.catalogue_item_des handset_mode_des,
                 ci_g.catalogue_item_des handset_mode_des_f
            FROM ci_group cg,
                 catalogue_item ci_p,
                 catalogue_item ci_g,
                 ci_relationship cr,
                 product p
           WHERE ci_p.sub_type_id = v_product_id
             AND ci_p.catalogue_item_type_cd = 'PRODUCT'
             AND ci_g.catalogue_item_type_cd = 'GROUP'
             AND cr.catalogue_parent_item_id = ci_g.catalogue_item_id
             AND cr.catalogue_child_item_id = ci_p.catalogue_item_id
             AND cr.relationship_type_cd = 'GENERICGRP'
             AND ci_g.sub_type_id = cg.ci_group_id
             AND cg.ci_group_type_cd = 'EQUIP_MODE_GRP'
             AND SYSDATE BETWEEN cr.effective_dt
                             AND NVL (cr.expiry_dt,
                                      TO_DATE ('1-Jan-4000', 'DD-MON-YYYY')
                                     )
             AND p.product_id = ci_p.sub_type_id
             AND p.equip_model_ind = 'N';
   EXCEPTION
      WHEN NO_DATA_FOUND
      THEN
         IF (po_product_modes%ISOPEN)
         THEN
            CLOSE po_product_modes;
         END IF;

         -- return NULL cursor
         OPEN po_product_modes
          FOR
             SELECT NULL
               FROM DUAL
              WHERE 1 = 0;
      --   RAISE;
      WHEN OTHERS
      THEN
         IF (po_product_modes%ISOPEN)
         THEN
            CLOSE po_product_modes;
         END IF;

         raise_application_error
                               (-20161,
                                   'Get product modes Query Failed. Oracle:(['
                                || SQLCODE
                                || '] ['
                                || SQLERRM
                                || '])'
                               );
   END getproductmodes;

------------------------------------------------------------------------------------

   ------------------------------------------------------------------------------------
   PROCEDURE isnewprepaidhandset (
      pi_serial_no     IN       VARCHAR2,
      pi_product_cd    IN       VARCHAR2,
      po_new_prepaid   OUT      NUMBER
   )
   IS
      prepaid              CONSTANT VARCHAR2 (10)                := 'PRE';
       both_pre_post        CONSTANT VARCHAR2 (10)                := 'BOTH';
      service_start_date   CONSTANT VARCHAR2 (20)           := '18-June-2006';
      v_prepaid_handset             BOOLEAN                      := FALSE;
      v_active                      BOOLEAN                      := FALSE;
      v_product_id                  product.product_id%TYPE;

      CURSOR c_product (p_product_cd VARCHAR2)
      IS
         SELECT product_id
           FROM product p
          WHERE p.product_cd = p_product_cd
           AND p.technology_type IN ('PCS', '1RTT', 'GSM', 'LTE')
            AND p.start_dt > TO_DATE (service_start_date, 'DD-MON-YYYY')
            AND p.pre_post_paid_flag = prepaid;

      CURSOR c_pcs_equip_status (p_serial_no VARCHAR2)
      IS
         SELECT   equipment_status_type_id, equipment_status_id
             FROM pcs_equip_status
            WHERE serial_no = p_serial_no
              AND seq_no = 0
              AND TRUNC (equipment_status_dt) <= TRUNC (SYSDATE)
         ORDER BY equipment_status_dt DESC;

      equip_rec                     c_pcs_equip_status%ROWTYPE;
   BEGIN
      OPEN c_product (pi_product_cd);

      FETCH c_product
       INTO v_product_id;

      IF c_product%FOUND
      THEN
         v_prepaid_handset := TRUE;
      END IF;

      CLOSE c_product;

      IF v_prepaid_handset = TRUE
      THEN
         FOR equip_rec IN c_pcs_equip_status (pi_serial_no)
         LOOP
            EXIT WHEN c_pcs_equip_status%NOTFOUND;

            IF (    equip_rec.equipment_status_type_id = 2
                AND equip_rec.equipment_status_id = 8
               )
            THEN
               v_active := TRUE;
               EXIT;
            END IF;
         END LOOP;

         IF v_active = FALSE                       -- handset never was active
         THEN
            po_new_prepaid := 1;
         END IF;
      END IF;
   EXCEPTION
      WHEN OTHERS
      THEN
         IF c_product%ISOPEN
         THEN
            CLOSE c_product;
         END IF;

         IF c_pcs_equip_status%ISOPEN
         THEN
            CLOSE c_pcs_equip_status;
         END IF;

         raise_application_error
                          (-20161,
                              'Is New Prepaid Handset Query Failed. Oracle:(['
                           || SQLCODE
                           || '] ['
                           || SQLERRM
                           || '])'
                          );
   END isnewprepaidhandset;

---------------------------------------------------------------------
   PROCEDURE checklateststatus (
      pi_serial_no                  IN       VARCHAR2,
      pi_po_tech_type_class         IN       VARCHAR2,
      pi_equipment_status_type_id   IN       NUMBER,
      pi_equipment_status_id        IN       NUMBER,
      po_recordexist                OUT      BOOLEAN
   )
   IS
      v_equip_status_type_id   pcs_equip_status.equipment_status_type_id%TYPE;
      v_equip_status_id        pcs_equip_status.equipment_status_id%TYPE;

      TYPE statustypelist IS TABLE OF NUMBER
         INDEX BY BINARY_INTEGER;

      TYPE statuslist IS TABLE OF NUMBER
         INDEX BY BINARY_INTEGER;

      status_type_ids          statustypelist;
      status_ids               statuslist;

      CURSOR c_pcs_equip_status
      IS
         SELECT   equipment_status_type_id, equipment_status_id
             FROM pcs_equip_status
            WHERE serial_no = pi_serial_no
              AND seq_no = 0
              AND TRUNC (equipment_status_dt) <= TRUNC (SYSDATE)
         ORDER BY equipment_status_dt DESC;

      CURSOR c_iden_equip_status
      IS
         SELECT   equipment_status_type_id, equipment_status_id
             FROM iden_equip_status
            WHERE serial_no = pi_serial_no
              AND seq_no = 0
              AND TRUNC (equipment_status_dt) <= TRUNC (SYSDATE)
         ORDER BY equipment_status_dt DESC;

      CURSOR c_uim_equip_status
      IS
         SELECT   equipment_status_type_id, equipment_status_id
             FROM uim_status
            WHERE iccid = pi_serial_no
              AND seq_no = 0
              AND TRUNC (equipment_status_dt) <= TRUNC (SYSDATE)
         ORDER BY equipment_status_dt DESC;

      CURSOR c_sim_equip_status
      IS
         SELECT   equipment_status_type_id, equipment_status_id
             FROM sim_status
            WHERE sim_id = pi_serial_no
              AND seq_no = 0
              AND TRUNC (equipment_status_dt) <= TRUNC (SYSDATE)
         ORDER BY equipment_status_dt DESC;

      CURSOR c_analog_equip_status
      IS
         SELECT   equipment_status_type_id, equipment_status_id
             FROM analog_equip_status
            WHERE serial_no = pi_serial_no
              AND seq_no = 0
              AND TRUNC (equipment_status_dt) <= TRUNC (SYSDATE)
         ORDER BY equipment_status_dt DESC;

      CURSOR c_paging_equip_status
      IS
         SELECT   equipment_status_type_id, equipment_status_id
             FROM paging_equip_status
            WHERE serial_no = pi_serial_no
              AND seq_no = 0
              AND TRUNC (equipment_status_dt) <= TRUNC (SYSDATE)
         ORDER BY equipment_status_dt DESC;
--USIM		 
	  CURSOR c_usim_equip_status
      IS
         SELECT   equipment_status_type_id, equipment_status_id
             FROM usim_status
            WHERE usim_id = pi_serial_no
              AND seq_no = 0
              AND TRUNC (equipment_status_dt) <= TRUNC (SYSDATE)
         ORDER BY equipment_status_dt DESC;	 
   BEGIN
------------------------------------------------------------------
-- Create reference to a cursor  using SQL statement
------------------------------------------------------------------
      po_recordexist := FALSE;

      CASE pi_po_tech_type_class
         WHEN 'PCS'
         THEN
            OPEN c_pcs_equip_status;

            FETCH c_pcs_equip_status
            BULK COLLECT INTO status_type_ids, status_ids;

            CLOSE c_pcs_equip_status;
         WHEN 'MIKE'
         THEN
            OPEN c_iden_equip_status;

            FETCH c_iden_equip_status
            BULK COLLECT INTO status_type_ids, status_ids;

            CLOSE c_iden_equip_status;
         WHEN 'SIM'
         THEN
            OPEN c_sim_equip_status;

            FETCH c_sim_equip_status
            BULK COLLECT INTO status_type_ids, status_ids;

            CLOSE c_sim_equip_status;
         WHEN 'ANALOG'
         THEN
            OPEN c_analog_equip_status;

            FETCH c_analog_equip_status
            BULK COLLECT INTO status_type_ids, status_ids;

            CLOSE c_analog_equip_status;
         WHEN 'PAGER'
         THEN
            OPEN c_paging_equip_status;

            FETCH c_paging_equip_status
            BULK COLLECT INTO status_type_ids, status_ids;

            CLOSE c_paging_equip_status;
         WHEN 'UIM'
         THEN
            OPEN c_uim_equip_status;

            FETCH c_uim_equip_status
            BULK COLLECT INTO status_type_ids, status_ids;

            CLOSE c_uim_equip_status;
		 WHEN 'USIM'
         THEN
            OPEN c_usim_equip_status;

            FETCH c_usim_equip_status
            BULK COLLECT INTO status_type_ids, status_ids;

            CLOSE c_usim_equip_status;			
         WHEN 'VIRTUAL'
         THEN
            --  Just IGNORE;
            NULL;
         ELSE
            RAISE invalidtechtype;
      END CASE;

      FOR i IN 1 .. status_type_ids.COUNT
      LOOP
         IF status_type_ids (i) = pi_equipment_status_type_id
         THEN
            IF status_ids (i) = pi_equipment_status_id
            THEN
               po_recordexist := TRUE;
            END IF;

            EXIT;
         END IF;
      END LOOP;
   EXCEPTION
      WHEN invalidtechtype
      THEN
         raise_application_error
                             (-20321,
                              'Client_Equipment pkg: Invalid technology Type');
      WHEN OTHERS
      THEN
         IF (c_pcs_equip_status%ISOPEN)
         THEN
            CLOSE c_pcs_equip_status;
         END IF;

         IF (c_iden_equip_status%ISOPEN)
         THEN
            CLOSE c_iden_equip_status;
         END IF;

         IF (c_uim_equip_status%ISOPEN)
         THEN
            CLOSE c_uim_equip_status;
         END IF;

         IF (c_analog_equip_status%ISOPEN)
         THEN
            CLOSE c_analog_equip_status;
         END IF;

         IF (c_sim_equip_status%ISOPEN)
         THEN
            CLOSE c_sim_equip_status;
         END IF;

         IF (c_paging_equip_status%ISOPEN)
         THEN
            CLOSE c_paging_equip_status;
         END IF;

		 IF (c_usim_equip_status%ISOPEN)
         THEN
            CLOSE c_usim_equip_status;
         END IF;
		 
         raise_application_error
                             (-20161,
                                 'Check Latest Status Query Failed. Oracle:(['
                              || SQLCODE
                              || '] ['
                              || SQLERRM
                              || '])');
   END checklateststatus;

---------------------------------------
---------------------------------------------------------------------------
   PROCEDURE getbaseproductprice (
      pi_product_cd   IN       VARCHAR2,
      pi_province     IN       VARCHAR2,
      pi_npa          IN       VARCHAR2,
      pi_act_date     IN       DATE,
      po_base_price   OUT      refcursor
   )
   IS
   BEGIN
------------------------------------------------------------------
-- Create reference to a cursor  using SQL statement
------------------------------------------------------------------
      OPEN po_base_price
       FOR
          SELECT   'ANPA' s_credit_reason, NVL (ppcr.price, 0.0) price,
                   ppcr.effective_dt eff_date
              FROM credit_region_area_code cra,
                   product_price_credit_region ppcr,
                   product prod
             WHERE prod.product_cd = pi_product_cd
               AND ppcr.product_id = prod.product_id
               AND ppcr.region_group_id = cra.region_group_id
               AND ppcr.effective_dt <= pi_act_date
               AND (ppcr.expiry_dt IS NULL OR ppcr.expiry_dt > pi_act_date)
               AND cra.npa_code = TO_NUMBER (pi_npa)
          UNION
          SELECT   'ZPRO' s_credit_reason, NVL (ppcr.price, 0.0) price,
                   ppcr.effective_dt eff_date
              FROM credit_region_province crp,
                   product_price_credit_region ppcr,
                   product prod
             WHERE prod.product_cd = pi_product_cd
               AND ppcr.product_id = prod.product_id
               AND ppcr.region_group_id = crp.region_group_id
               AND ppcr.effective_dt <= pi_act_date
               AND (ppcr.expiry_dt IS NULL OR ppcr.expiry_dt > pi_act_date)
               AND crp.province_cd = pi_province
          ORDER BY s_credit_reason ASC, eff_date DESC;
   EXCEPTION
      WHEN NO_DATA_FOUND
      THEN
         IF (po_base_price%ISOPEN)
         THEN
            CLOSE po_base_price;
         END IF;

         -- return NULL cursor
         OPEN po_base_price
          FOR
             SELECT NULL
               FROM DUAL
              WHERE 1 = 0;
      WHEN OTHERS
      THEN
         IF (po_base_price%ISOPEN)
         THEN
            CLOSE po_base_price;
         END IF;

         raise_application_error
                          (-20160,
                              'Get Base Product Price Query Failed. Oracle:(['
                           || SQLCODE
                           || '] ['
                           || SQLERRM
                           || '])'
                          );
   END getbaseproductprice;

   FUNCTION getrimpin (pi_serial_num IN VARCHAR2)
      RETURN VARCHAR2
   IS
      po_pin   VARCHAR2 (50);
   BEGIN
      SELECT pin
        INTO po_pin
        FROM pcs_rim_pin
       WHERE serial_no = pi_serial_num;

      RETURN po_pin;
   EXCEPTION
      WHEN NO_DATA_FOUND
      THEN
         BEGIN
            SELECT pin
              INTO po_pin
              FROM iden_rim_pin
             WHERE serial_no = pi_serial_num;

            RETURN po_pin;
         EXCEPTION
            WHEN NO_DATA_FOUND
            THEN
               po_pin := '';
               RETURN po_pin;
         END;
      WHEN OTHERS
      THEN
         po_pin := '';
         RETURN po_pin;
   END getrimpin;

   PROCEDURE getbrands (pi_product_cd IN VARCHAR2, po_brands OUT brand_array)
   IS
   BEGIN
      SELECT b.brand_id
      BULK COLLECT INTO po_brands
        FROM product p, catalogue_item ci, ci_modifier cm, brand b
       WHERE p.product_cd = pi_product_cd
         AND p.product_id = ci.sub_type_id
         AND ci.catalogue_item_type_cd = 'PRODUCT'
         AND ci.catalogue_item_id = cm.catalogue_item_id
         AND cm.modifier_type_cd = 'BRAND'
         AND cm.ci_modifier_id = b.brand_id;
   END getbrands;

----------------------------------------------------------------------------
   PROCEDURE getvirtualesn (
      pi_serial_no    IN       VARCHAR2,
      po_virtual_no   OUT      VARCHAR2
   )
   IS
      CURSOR c_virtual_no (p_serial_no VARCHAR2)
      IS
         SELECT ve.serial_no
           FROM virtual_equipment ve,
                product p1,
                pcs_equipment pe,
                product p2
          WHERE ve.product_id = p1.product_id
            AND pe.product_id = p2.product_id
            AND p1.product_class_id = p2.product_class_id
            AND p1.technology_type = p2.technology_type
            AND pe.serial_no = p_serial_no
            AND pe.seq_no = 0;

      CURSOR c_virtual_def
      IS
         SELECT ve.serial_no
           FROM virtual_equipment ve
          WHERE ve.default_ind = 'Y';
   BEGIN
      OPEN c_virtual_no (pi_serial_no);

      FETCH c_virtual_no
       INTO po_virtual_no;

      IF c_virtual_no%NOTFOUND
      THEN
         OPEN c_virtual_def;

         FETCH c_virtual_def
          INTO po_virtual_no;

         IF c_virtual_def%NOTFOUND
         THEN
            RAISE virtualequipmentnotfound;
         END IF;

         CLOSE c_virtual_def;
      END IF;

      CLOSE c_virtual_no;
   EXCEPTION
      WHEN virtualequipmentnotfound
      THEN
         IF c_virtual_no%ISOPEN
         THEN
            CLOSE c_virtual_no;
         END IF;

         IF c_virtual_def%ISOPEN
         THEN
            CLOSE c_virtual_def;
         END IF;

         raise_application_error
                          (-20320,
                           'Client_Equipment pkg: Virtual Equipment not found'
                          );
      WHEN OTHERS
      THEN
         raise_application_error
                              (-20161,
                                  'GetEquipmentGroup Query Failed. Oracle:(['
                               || SQLCODE
                               || '] ['
                               || SQLERRM
                               || '])'
                              );
   END getvirtualesn;

----------------------------------------------------------------------------
   PROCEDURE getpcslockreasons (po_lock_reasons OUT refcursor)
   IS
   BEGIN
      OPEN po_lock_reasons
       FOR
          SELECT   lock_reason_id, lock_reason_des
              FROM pcs_lock_reason
          ORDER BY lock_reason_id;
   EXCEPTION
      WHEN NO_DATA_FOUND
      THEN
         IF (po_lock_reasons%ISOPEN)
         THEN
            CLOSE po_lock_reasons;
         END IF;

         raise_application_error
                           (-20320,
                            'Client_Equipment_pkg: PCS Lock Reasons not found'
                           );
      WHEN OTHERS
      THEN
         IF (po_lock_reasons%ISOPEN)
         THEN
            CLOSE po_lock_reasons;
         END IF;

         raise_application_error
                            (-20160,
                                'Get PCS Lock Reasons Query Failed. Oracle:(['
                             || SQLCODE
                             || '] ['
                             || SQLERRM
                             || '])'
                            );
   END getpcslockreasons;

------------------------------------------------------------------------------
   PROCEDURE getimsisbyusim (pi_usim IN VARCHAR2, po_imsis OUT refcursor)
   IS
   BEGIN
      OPEN po_imsis
       FOR
          SELECT imsi
            FROM usim_subscriber_profile
           WHERE usim_id = pi_usim AND usim_seq_no = 0;
   EXCEPTION
      WHEN NO_DATA_FOUND
      THEN
         IF (po_imsis%ISOPEN)
         THEN
            CLOSE po_imsis;
         END IF;

         -- return NULL cursor
         OPEN po_imsis
          FOR
             SELECT NULL
               FROM DUAL
              WHERE 1 = 0;

         raise_application_error (-20323,
                                  'Client_Equipment_pkg: IMSI is not found'
                                 );
      WHEN OTHERS
      THEN
         IF (po_imsis%ISOPEN)
         THEN
            CLOSE po_imsis;
         END IF;

         -- return NULL cursor
         OPEN po_imsis
          FOR
             SELECT NULL
               FROM DUAL
              WHERE 1 = 0;

         raise_application_error
                                (-20325,
                                    'Get IMSI By USIM Query Failed. Oracle:(['
                                 || SQLCODE
                                 || '] ['
                                 || SQLERRM
                                 || '])'
                                );
   END getimsisbyusim;

-------------------------------------------------------------------------------
   PROCEDURE getusimbyimsi (pi_imsi IN VARCHAR2, po_usim OUT VARCHAR2)
   IS
   BEGIN
      SELECT usim_id
        INTO po_usim
        FROM usim_subscriber_profile
       WHERE imsi = pi_imsi AND usim_seq_no = 0;
   EXCEPTION
      WHEN NO_DATA_FOUND
      THEN
         raise_application_error (-20324,
                                  'Client_Equipment_pkg: USIM is not found'
                                 );
      WHEN OTHERS
      THEN
         raise_application_error
                               (-20325,
                                   'Get USIM By IMSI Query Failed. Oracle:(['
                                || SQLCODE
                                || '] ['
                                || SQLERRM
                                || '])'
                               );
   END getusimbyimsi;

-------------------------------------------------------------------------------
   PROCEDURE getusimlistbyimsis (
      pi_imsis       IN       t_imsi_array,
      po_usim_list   OUT      refcursor
   )
   IS
   BEGIN
      IF pi_imsis.COUNT > 0 AND pi_imsis.COUNT <= MAX_LIMIT
      THEN
         OPEN po_usim_list
          FOR
             SELECT /*+ FIRST_ROWS */ imsi, usim_id
               FROM usim_subscriber_profile
              WHERE imsi IN (SELECT *
                               FROM TABLE (CAST (pi_imsis AS t_imsi_array)))
                AND usim_seq_no = 0;
      ELSE
         RAISE invalidimsiarray;
      END IF;
   EXCEPTION
      WHEN invalidimsiarray
      THEN
         IF (po_usim_list%ISOPEN)
         THEN
            CLOSE po_usim_list;
         END IF;

         -- return NULL cursor
         OPEN po_usim_list
          FOR
             SELECT NULL
               FROM DUAL
              WHERE 1 = 0;

				 IF pi_imsis.COUNT > MAX_LIMIT THEN
         	raise_application_error
                             (-20322,
                              'Client_Equipment_pkg: Invalid input IMSI array (limit exceeded)'
                             );
				 ELSE
         	raise_application_error
                             (-20322,
                              'Client_Equipment_pkg: Invalid input IMSI array'
                             );
         END IF;
      WHEN NO_DATA_FOUND
      THEN
         IF (po_usim_list%ISOPEN)
         THEN
            CLOSE po_usim_list;
         END IF;

         -- return NULL cursor
         OPEN po_usim_list
          FOR
             SELECT NULL
               FROM DUAL
              WHERE 1 = 0;

         raise_application_error (-20324,
                                  'Client_Equipment_pkg: USIM is not found'
                                 );
      WHEN OTHERS
      THEN
         IF (po_usim_list%ISOPEN)
         THEN
            CLOSE po_usim_list;
         END IF;

         -- return NULL cursor
         OPEN po_usim_list
          FOR
             SELECT NULL
               FROM DUAL
              WHERE 1 = 0;

         raise_application_error
                          (-20325,
                              'Get USIM List By IMSIs Query Failed. Oracle:(['
                           || SQLCODE
                           || '] ['
                           || SQLERRM
                           || '])'
                          );
   END getusimlistbyimsis;
-------------------------------------------------------------------------------

	PROCEDURE getAssociatedHandsetbyUSIMID (
      pi_usim                          IN       VARCHAR2,
      po_imei                         OUT      VARCHAR2,
      po_tech_type                    OUT      VARCHAR2,
      po_product_cd                   OUT      VARCHAR2,
      po_product_status_cd            OUT      VARCHAR2,
      po_vendor_name                  OUT      VARCHAR2,
      po_vendor_no                    OUT      VARCHAR2,
      po_equipment_status_type_id     OUT      NUMBER,
      po_equipment_status_id          OUT      NUMBER,
      po_stolen                       OUT      NUMBER,
      po_sublock1                     OUT      VARCHAR2,
      po_product_gp_type_id           OUT      NUMBER,
      po_product_gp_type_cd           OUT      VARCHAR2,
      po_product_gp_type_des          OUT      VARCHAR2,
      po_product_type_id              OUT      NUMBER,
      po_product_type_des             OUT      VARCHAR2,
      po_product_class_id             OUT      NUMBER,
      po_product_class_cd             OUT      VARCHAR2,
      po_product_class_des            OUT      VARCHAR2,
      po_provider_owner_id            OUT      NUMBER,
      po_lastmuleimei_for_sim         OUT      VARCHAR2,
      po_english_product_name         OUT      VARCHAR2,
      po_french_product_name          OUT      VARCHAR2,
      po_browser_version              OUT      VARCHAR2,
      po_firmware_version             OUT      VARCHAR2,
      po_prl_cd                       OUT      VARCHAR2,
      po_prl_des                      OUT      VARCHAR2,
      po_product_type_des_f           OUT      VARCHAR2,
      po_product_gp_type_des_f        OUT      VARCHAR2,
      po_min_cd                       OUT      VARCHAR2,
      po_customer_id                  OUT      NUMBER,
      po_product_type_list            OUT      VARCHAR2,
      po_initial_activation           OUT      VARCHAR2,
      po_mode_code                    OUT      NUMBER,
      po_mode_description             OUT      VARCHAR2,
      po_product_type                 OUT      VARCHAR2,
      po_equipment_type               OUT      VARCHAR2,
      po_equipment_type_class         OUT      VARCHAR2,
      po_cross_fleet                  OUT      VARCHAR2,
      po_cost                         OUT      NUMBER,
      po_cap_code                     OUT      VARCHAR2,
      po_coverage_region_code_list    OUT      VARCHAR2,
      po_encoding_format_code         OUT      VARCHAR2,
      po_ownership_code               OUT      VARCHAR2,
      po_prepaid                      OUT      VARCHAR2,
      po_frequency_cd                 OUT      VARCHAR2,
      po_firmware_feature_code_list   OUT      VARCHAR2,
      po_browser_protocol             OUT      VARCHAR2,
      po_unscanned                    OUT      NUMBER,
      po_equip_status_dt              OUT      DATE,
      po_puk                          OUT      VARCHAR2,
      po_product_category_id          OUT      NUMBER,
      po_rim_pin                      OUT      VARCHAR2,
      po_brands                       OUT      brand_array,
	  po_equipment_group              OUT      VARCHAR2,
	  po_source_network_type_cd	      OUT      VARCHAR2
   )
   IS
      v_imei      pcs_equipment.serial_no%TYPE;
      v_virtual   VARCHAR2 (2);
	  v_local_imsi VARCHAR2 (2);
  	  v_remote_imsi VARCHAR2 (2);
  	  v_assohandsetimei_for_usim VARCHAR2 (2);
	  v_assignable NUMBER;
	  v_previously_activated NUMBER;
      v_last_event_type usim_pcs_device_assoc.event_type%TYPE;
      v_pre_post_paid_flag VARCHAR2 (2);
   BEGIN
 	  getimeibyusimid(pi_usim, v_imei, v_last_event_type);

      IF v_imei IS NOT NULL
      THEN
         po_imei := v_imei;
         getequipmentinfo (v_imei,
                           'PCS',
                           FALSE,
                           po_tech_type,
                           po_product_cd,
                           po_product_status_cd,
                           po_vendor_name,
                           po_vendor_no,
                           po_equipment_status_type_id,
                           po_equipment_status_id,
                           po_stolen,
                           po_sublock1,
                           po_product_gp_type_id,
                           po_product_gp_type_cd,
                           po_product_gp_type_des,
                           po_product_type_id,
                           po_product_type_des,
                           po_product_class_id,
                           po_product_class_cd,
                           po_product_class_des,
                           po_provider_owner_id,
                           po_lastmuleimei_for_sim,
                           po_english_product_name,
                           po_french_product_name,
                           po_browser_version,
                           po_firmware_version,
                           po_prl_cd,
                           po_prl_des,
                           po_product_type_des_f,
                           po_product_gp_type_des_f,
                           po_min_cd,
                           po_customer_id,
                           po_product_type_list,
                           po_initial_activation,
                           po_mode_code,
                           po_mode_description,
                           po_product_type,
                           po_equipment_type,
                           po_equipment_type_class,
                           po_cross_fleet,
                           po_cost,
                           po_cap_code,
                           po_coverage_region_code_list,
                           po_encoding_format_code,
                           po_ownership_code,
                           po_prepaid,
                           po_frequency_cd,
                           po_firmware_feature_code_list,
                           po_browser_protocol,
                           po_unscanned,
                           po_equip_status_dt,
                           po_puk,
                           po_product_category_id,
                           v_virtual,
                           po_rim_pin,
                           po_brands,
						   v_assohandsetimei_for_usim,
						   v_local_imsi,
						   v_remote_imsi,
						   v_assignable,
                           v_previously_activated,
						   po_equipment_group,
                           v_last_event_type,
                           po_source_network_type_cd,
                           v_pre_post_paid_flag
                          );
         getbrands (po_product_cd, po_brands);
      END IF;
   EXCEPTION
      WHEN OTHERS
      THEN
         RAISE;
   END getAssociatedHandsetbyUSIMID;

------------------------------------------------------------------------------

   PROCEDURE getimeibyusimid (pi_usim_id IN VARCHAR2, po_imei_id OUT VARCHAR2, po_last_event_type OUT VARCHAR2)
   IS
      CURSOR c_imei
      IS
         SELECT handset_serial_no, event_type
		 FROM (
			SELECT handset_serial_no, event_type
			FROM usim_pcs_device_assoc
			WHERE usim_id = pi_usim_id
			AND usim_pcs_device_assoc_seq_no = 0 
			ORDER BY usim_pcs_device_assoc_dt DESC)
		WHERE rownum <=1;

	  v_imei_id	c_imei%ROWTYPE;	
      v_counter   INTEGER       := 0;
   BEGIN
    OPEN c_imei;
    FETCH c_imei
    INTO v_imei_id;
	
	IF c_imei%NOTFOUND
      THEN
        po_imei_id := NULL;
        po_last_event_type := NULL;
      ELSE
		po_imei_id := v_imei_id.handset_serial_no;
        po_last_event_type := v_imei_id.event_type;
         
    END IF;    
	
	CLOSE c_imei;
	
   EXCEPTION
      WHEN OTHERS
      THEN
         IF c_imei%ISOPEN
         THEN
            CLOSE c_imei;
         END IF;

         RAISE;

   END getimeibyusimid;

------------------------------------------------------------------------------

   PROCEDURE getprofilebyusimid (pi_usim_id IN VARCHAR2, po_imsi_local OUT VARCHAR2, po_imsi_remote OUT VARCHAR2)
   IS
      CURSOR c_usim_profile
      IS
         SELECT   imsi, usim_id, subscriber_profile_type_id
             FROM usim_subscriber_profile
            WHERE usim_id = pi_usim_id
              AND usim_seq_no = 0;

   BEGIN
	
	FOR profile_rec IN c_usim_profile
		LOOP
			EXIT WHEN c_usim_profile%NOTFOUND;
			IF (profile_rec.subscriber_profile_type_id = 1)
			THEN
				po_imsi_local := profile_rec.imsi;
			ELSE
				po_imsi_remote := profile_rec.imsi;
			END IF;
		END LOOP;
   END getprofilebyusimid;
   
-------------------------------------------------------------------------------   
 PROCEDURE getusimbyimei (pi_imei_id IN VARCHAR2, po_usim_id OUT VARCHAR2)
      IS
        CURSOR c_usim
          IS
		SELECT usim_id 
		 FROM (
			SELECT usim_id
			FROM usim_pcs_device_assoc
			WHERE handset_serial_no = pi_imei_id
			AND usim_pcs_device_assoc_seq_no = 0 
			ORDER BY usim_pcs_device_assoc_dt DESC)
			WHERE rownum <=1;
   
   
         v_usim_id   VARCHAR2 (50);

      BEGIN
         OPEN c_usim;
   
          FETCH c_usim
          INTO v_usim_id;
              
          IF c_usim%NOTFOUND
      	  THEN
      	  	raise usimnotfound;
          END IF;
          
          po_usim_id := v_usim_id;
   
         CLOSE c_usim;
   
      EXCEPTION
      When usimnotfound 
        THEN
      	  IF c_usim%ISOPEN
	    THEN
	       CLOSE c_usim;
          END IF;
      	  RAISE_APPLICATION_ERROR(-20324,'Client_Equipment pkg: USIM  not found');
      
      WHEN OTHERS
         THEN
            IF c_usim%ISOPEN
            THEN
               CLOSE c_usim;
            END IF;
            RAISE;
   END getusimbyimei;
-------------------------------------------------------------------------------


----------------------------------------------------------------------------
-- Description: This function returns a brand_array with -1 if the input
-- 							pi-prepostflag is the value 'PRE' and the input pi_brands
--							contains the brand id = 3 representing Koodo Prepaid
-- Parameters : 
--		(IN) pi_prepostflag - value fetched from PRODUCT.pre_post_paid_flag
--         pi_brands - brand_array from an initial call to getbrands procedure
--
--    (OUT)
--         original values from pi_brands or if conditions in description is
--				 satisfied, brand_array with value of -1
----------------------------------------------------------------------------
FUNCTION excludekoodoprepaidbrand(pi_prepostflag IN PRODUCT.pre_post_paid_flag%TYPE, 
                                  pi_brands      IN BRAND_ARRAY) 
RETURN BRAND_ARRAY 
IS 
  po_brands BRAND_ARRAY; 
  vPrepaid CONSTANT PRODUCT.pre_post_paid_flag%TYPE := 'PRE'; 
  nKoodo_brandid CONSTANT brand.brand_id%TYPE := 3; 
BEGIN 
    po_brands := pi_brands; 

    IF ( pi_brands.COUNT > 0 ) THEN 
      FOR i IN pi_brands.FIRST..pi_brands.LAST LOOP 
          IF ( pi_prepostflag = vPrepaid 
               AND pi_brands(i) = nKoodo_brandid ) THEN 
            po_brands := BRAND_ARRAY();
            po_brands.EXTEND(1);
            po_brands(1) := -1; 
            EXIT; 
          END IF; 
      END LOOP; 
    END IF; 

    RETURN po_brands; 
EXCEPTION 
  WHEN OTHERS THEN 
             RETURN pi_brands; 
END excludekoodoprepaidbrand;
----------------------------------------------------------------------------

----------------------------------------------------------------------------
-- Description: This function returns version of the package used for
--							shakedown validation.
-- Parameters : 
--    (OUT)
--         version of the package
----------------------------------------------------------------------------
FUNCTION getVersion RETURN VARCHAR2
IS
   BEGIN
	   RETURN version_no;
END getVersion;
----------------------------------------------------------------------------


END;
/