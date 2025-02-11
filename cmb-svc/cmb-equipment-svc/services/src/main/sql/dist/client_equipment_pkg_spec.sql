/* Formatted on 2006/11/23 12:08 (Formatter Plus v4.8.0) */
CREATE OR REPLACE PACKAGE client_equipment
AS
------------------------------------------------------------------
 ------------------------------------------------------------------------
-- description: Package Client_Equipment  containing procedures
--    to interface to the DIST database
--
-- Database: DIST | Schema: IVRADM
-- Date           Developer           Modifications
-- 11-SEP-2000    Ludmila Pomirche    created
-- 24-Oct-2000    Ludmila Pomirche          added GetMasterLockNumber procedure
-- 30-Jan-2001    Ludmila Pomirche    added second GetEquipmentInfo procedure
-- 30-Jan-2001    Ludmila Pomirche    added GetIMEIBySIM  procedure
-- 30-Jan-2001    Ludmila Pomirche    added GetSIMByIMEI procedure
-- 22-Feb-2001    Ludmila Pomirche    added logic for analog phones
-- 27-Feb-2001    Ludmila Pomirche    po_provider_owner_id  OUT parameter added
-- 02-Mar-2001    Ludmila Pomirche          po_equipment_status_type_id, po_equipment_status_id added
-- 22-May-2001    Ludmila Pomirche    added cursor for vendor query
-- 31-May-2001    Ludmila Pomirche    added second GetMasterLockNumber procedure for dealers
-- 30-Jul-2001    Ludmila Pomirche    Misc changes for Fusion East Project\
-- 18-Feb-2002    Ludmila Pomirche    PRL ,webbrowser and firmware version added to retrieval
--                  Added methods getEquipment with phone_no as parameter
-- 19-Mar-2002    Ludmila Pomirche    Added po_Mode parameter to getEquipment
-- 30-Apr-2002    Ludmila Pomirche    Lost/Stolen validation addition
-- 18-Jun-2002    Ludmila Pomirche    added GetWarranty procedure
-- 26-Feb-2002    Ludmila Pomirche    added Equipment Type and Product Type as out parameters
-- 29-Jul-2003    Ludmila Pomirche    Pager attributes added
-- 10-SEP-2003    Daphne Chan         add second GetProductInfo procedure
-- 27-Oct-2003    Daphne Chan         change the po_new_param_1 to po_firmware_feature_code_list
--                                                for Chameleon.
--                                                Add GetFirmwareVersionFeature procedure
-- 11-Mar-2004    Ludmila Pomirche    new procedure getMuleBySIM
-- 15-Mar-2005      Marina Kuper          Changed queries for GetProductInfo (P3MS Re-archtecture )
-- 11-Apr-2005      Marina Kuper          Added po_equip_status_dt parameter to getEquipment
-- 26-Apr-2005      Marina Kuper          Added PUK Code
-- 30-May-2005      Marina Kuper          Added fix for P3MS (cursor c_product_gr_type)
-- 31-May-2005      Marina Kuper          Added po_product_category_id parameter to getEquipment
-- 11-July-2005     Marina Kuper          Added Procedure GetEquipmentInfoByProductCode
-- 03-August-2005   Marina Kuper          Added Procedure ChangeEquipmentStatus
-- 03-August-2005   Marina Kuper          Changed logic for stolen equipment
-- 16-August-2005   Ludmila Pomirche      Added logic for UIM in getEquipment
-- 24-August-2005   Marina Kuper          Added logic for UIM in GetWarranty
-- 28-September-2005 Marina Kuper         Added procedure getvirtualESN
-- 14- October-2005  Marina Kuper         Added logic for updating UIM Equipment
-- 24 - October-2005 Marina Kuper         Added new Procedure getEquipmentInfo (with check_pseudo)
-- 11- November-2005 Marina Kuper         Added procedure checkifesnvirtual to fix Staging issue
-- 14- November-2005 Marina Kuper         Added procedure getEquipmentInfoByCapCode
-- 22- November-2005 Marina Kuper         Changed gettechnologytypeclass to check for analog at the end
-- 17 - January-2006 Marina Kuper         Added procedure getfeaturecodes
-- 23- January -2006 Marina Kuper         Changed changeequipmentstatus - production fix
-- 30 - March - 2006 Marina Kuper         Restored getequipmentinfobyphoneno
-- 13 - April - 2006 Marina Kuper         Added procedure getproductModes
-- 11 - May-2006     Marina Kuper         Added new Procedure isNewPrepaidHandset
-- 30 - May-2006     Marina Kuper         Added logic for un-scanned equipment
-- 21 - July-2006    Dimitry Siganevich   Added new Procedure getBaseProductPrice
-- 8-September-2006  Michael Qin   Added new function getrimpin
-- 12- October - 2006 Marina Kuper        Changed getequipmentinfobyphoneno and getequipmentinfo to use mdn_cd instead of min_cd for PCS and analog
-- 8-Nov-2006        Tsz Chung Tong       Added new Procedure getBrands (for Zebra)
-- 03- January- 2007 Marina Kuper         Changed getbrands
-- 010- January- 2007 Marina Kuper         Changed getvirtualequipmentinfo 
-- 31-Jan-2007       Tsz Chung Tong       Changed isnewprepaidhandset
-- 30-March-2007     R. Fong       		Added getpcslockreasons
-- 24-Apr-2007       Tsz Chung Tong       Changed GetEquipmentInfo (PROD00081737 fix. po_initial_activation was returned incorrectly.)
-- 31-May-2007       Tsz Chung Tong       Changed query in getfeaturecodes (EVDO REVA)
-- 28-May-2008		 Pavel Simonovsky	  Added getallesnbypseudoesn 
-- 15-December-2008  Marina Kuper         Added get IMSIs by USIM.
-- 15-December-2008  Marina Kuper         Added get USIM by IMSI.
-- 15-December-2008  Marina Kuper         Added get USIM List by IMSIs.   
-- 21-Jan-2009	Belinda Liang	Changed procedure gettechnologytypeclass to read USIM table base on serial number, if find record, return technology type classs as ‘USIM’
-- 21-Jan-2009	Belinda Liang	Added procedure getAssociatedHandsetbyUSIMID
-- 21-Jan-2009	Belinda Liang 	Added procedure getimeibyusimid
-- 21-Jan-2009	Belinda Liang 	Changed procedures getequipmentinfo to retrieve USIM related information
-- 21-Jan-2009	Belinda Liang	Added procedure getprofilebyusimid to retrieve local and remote IMSIs
-- 21-Jan-2009	Belinda Liang	Changed procedure GetEquipmentInfobyPhoneNo to return USIM equipment as well
-- 21-Jan-2009	Belinda Liang	Changed procedure CheckLatestStatus to check USIM status as well 
-- 21-Jan-2009	Belinda Liang	Changed procedure getmulebysim, getvirtualequipmentinfo, getequipmentinfocheckpseudo to accept more parameters for associatedhandsetimei, localimsi and remoteimsi when call getEquipmentInfo 
-- 26-Jan-2009	Belinda Liang	Changed procedure GetEquipmentInfobyPhoneNo, getequipmentinfo to return USIM PUK and assiganble flag
-- 26-Feb-2009	Belinda Liang	Changed procedure isnewprepaidhandset to allow HSPA Handset
-- 04-Mar-2009	Belinda Liang	Changed procedure isnewprepaidhandset to look at "PRE" only as prepaid bundle indicatior
-- 25-Mar-2009  Tsz Chung Tong Changed getallesnbypseudoesn to select distinct serial_no only
-- Apr-01-2009  Mujeeb Waraich  Added new procedure getusimbyimei
-- 02-Apr-2009  Tsz Chung Tong Changed getallesnbypseudoesn to not return data if serial_no equals to alt_serial_no
-- 02-Apr-2009  Roni Chaia      Changed procedures GetEquipmentInfobyPhoneNo, getequipmentinfo to return previously_activated flag
-- 03-Apr-2009  Roni Chaia      Added logic for equipment_group
-- 07-Apr-2009  Roni Chaia      Added logic for last_event_type
--13-Apr-2009	Belinda Liang	Changed getequipmentinfo to increase variable v_master_lock length from 10 to 30
--20-Apr-2009	Belinda Liang	Changed getimeibyusimid to avoid using MAX clause and look at usim_pcs_device_assoc_seq_no instead of usim_seq_no and handset_seq_no
--20-Apr-2009   Mujeeb Waraich  Chnaged getusimbyimei similar to changes done in getimeibyusimid
--28-Apr-2009	Belinda Liang	Changed  Getequipmentinfobyphoneno and  getTechnologyTypeClass to move USIM cursor open logic right after PCS and MIKE
--05-May-2009	Belinda Liang	fix PROD00139243, change variable length from 2 to 20 for v_equipment_group in procedure getequipmentinfocheckpseudo(); return equipment_group in procedure getmulebysim(), getVirtualEquipmentinfo() and getAssociatedHandsetbyUSIMID
-- 12-May-2009  Tsz Chung Tong   Added MAX_LIMIT constant (with value of 1000) and changed getusimlistbyimsis to raise exception if input array is over the limit.
-- 14-May-2009	Belinda Liang	fix 139818, modify lost/stolen logic for USIM in getequipmentinfo() to loop all status history to look for 1/6 and (3/11 or 3/56 or 3/57)
-- 21-May-2009	Belinda Liang	fix defect 140633, modify method getEquipmentInfo(), current only go through all status history when no stolen and assignable status found,  change it to anyway loop all status history to look for assignable/stolen and previously activated
-- 06-Nov-2009	Belinda Liang	fix production PDIST performance issue by adding hint to getusimlistbyimsis() 
-- 27-Oct-2010  Anitha Duraisamy Production fix- PROD00181013 Isnewprepaidhandset() The fix is to change the query to allow HSPA PDA devices to allow SKU credits. 
-- 29-Nov-2010  Michael Liao    CR414456, remove product_class_id filter in isnewprepaidhandset procedure's query 
-- 27-Jul-2011  Brandon Wen     V3SIM, Release null check for remote IMSI. Only if local IMSI is null, raise exception.
-- 01-March-2012  Naresh Annabathula   Commented - Covent-July changes
-- 04-May-2012	Canh Tran	Add function excludekoodoprepaidbrand, modified getequipmentinfo procedure to call excludekoodoprepaidbrand for USIMs
-- 07-May-2012	Canh Tran	Add function getVersion
-- 24-May-2012  Naresh Annabathula   Un-Commented - Covent changes
-- 10-August-2016  R. Fong   Modified getequipmentinfo and getequipmentinfobyphoneno procedures to return pre_post_paid_flag attribute; modified getequipmentinfo to remove call to excludekoodoprepaidbrand;
--								modified getmulebysim, getvirtualequipmentinfo, getequipmentinfocheckpseudo, getAssociatedHandsetbyUSIMID procedures (which call getequipmentinfo) to replace the 
--								po_pre_post_paid_flag output parameter with a local variable.
------------------------------------------------------------------

   esnnotfound                  EXCEPTION;
   PRAGMA EXCEPTION_INIT (esnnotfound, -20310);
   useridnotexist               EXCEPTION;
   PRAGMA EXCEPTION_INIT (useridnotexist, -20311);
   reachedmaxlock               EXCEPTION;
   PRAGMA EXCEPTION_INIT (reachedmaxlock, -20312);
   imeinotfound                 EXCEPTION;
   PRAGMA EXCEPTION_INIT (imeinotfound, -20313);
   simnotfound                  EXCEPTION;
   PRAGMA EXCEPTION_INIT (simnotfound, -20314);
   multipleimeiforsim           EXCEPTION;
   PRAGMA EXCEPTION_INIT (multipleimeiforsim, -20315);
   multiplesimforimei           EXCEPTION;
   PRAGMA EXCEPTION_INIT (multiplesimforimei, -20316);
   phonenotfound                EXCEPTION;
   PRAGMA EXCEPTION_INIT (phonenotfound, -20317);
   productnotfound              EXCEPTION;
   PRAGMA EXCEPTION_INIT (productnotfound, -20318);
   equipmentgroupnotfound       EXCEPTION;
   PRAGMA EXCEPTION_INIT (equipmentgroupnotfound, -20319);
   virtualequipmentnotfound     EXCEPTION;
   PRAGMA EXCEPTION_INIT (virtualequipmentnotfound, -20320);
   invalidtechtype              EXCEPTION;
   PRAGMA EXCEPTION_INIT (invalidtechtype, -20321);
   invalidimsiarray             EXCEPTION;
   PRAGMA EXCEPTION_INIT (invalidimsiarray, -20322);
   imsinotfound                  EXCEPTION;
   PRAGMA EXCEPTION_INIT (imsinotfound, -20323);
   usimnotfound                  EXCEPTION;
   PRAGMA EXCEPTION_INIT (usimnotfound, -20324);
   genericsqlexception           EXCEPTION;
   PRAGMA EXCEPTION_INIT (genericsqlexception , -20325);   
------------------------------------------------------------------------------
-- Reference to a Cursor
   TYPE refcursor IS REF CURSOR;

   numeric_true        CONSTANT NUMBER (1)     := 1;
   numeric_false       CONSTANT NUMBER (1)     := 0;
   -- Equipment status types:
   equipment_state     CONSTANT NUMBER (22, 0) := 4;
   -- Equipment statuses :
   walmart_lock        CONSTANT NUMBER (22, 0) := 63;
   v_equipment_status_type_id   NUMBER (22, 0);
   v_equipment_status_id        NUMBER (22, 0);
   v_recordexist                BOOLEAN        := FALSE;
   mike_technology_type         VARCHAR2(6) := 'MIKE' ;
   MAX_LIMIT           CONSTANT NUMBER (4) := 1000;
   version_no          	   CONSTANT VARCHAR2(10)       := '3.20.2';   

------------------------------------------------------------------------------
-- description: Procedure GetEquipmentInfo to get Equipment Information
-- Paramaters : (IN) pi_Serial_No - Serial Number of the equipment
--         pi_tech_type - identifies type of the equipment
--         pi_initial_activation - identifies if equipment ever been activated
--
--    (OUT)po_product_cd - product code
--         po_product_status_cd - product status(New, Refurbished, etc.)
--         po_vendor_name - Manufacturer name of the equipment
--         po_vendor_no - Manufacturer number of the equipment
--         po_stolen - identifies if equipment stolen
--         po_sublock1 - Subsidy lock number
--         po_product_gp_type_id
--         po_product_gp_type_cd
--         po_product_gp_type_des
--         po_product_type_id
--         po_product_type_des
--         po_product_class_id
--         po_product_class_cd
--         po_product_class_des
--         po_provider_owner_id
--         po_lastmuleimei_for_sim
--         po_english_product_name
--         po_french_product_name
--         po_browser_version
--         po_firmware_version
--         po_prl_cd
--         po_prl_des
--         po_product_type_des_f
--         po_product_gp_type_des_f
--         po_min_cd
--         po_customer_id
--         po_product_type_list
--         po_initial_activation - identifies if equipment ever been activated
--         po_mode_code - mode of the PCS equipment
--         po_mode_description - Description of the PCS equipment mode
--                   po_product_type  - Knowbility Product Type
--         po_equipment_type - Knowbility Equipment Type
--         po_equipment_type_class
--         po_cross_fleet
--         po_cost
--         po_cap_code
--         po_coverage_region_code_list
--         po_encoding_format_code
--         po_ownership_code
--         po_prepaid
--         po_frequency_cd
--         po_firmware_feature_code_list
--         po_browser_protocol
--         po_unscanned
--         po_equip_status_dt
--         po_puk
--         po_product_category_id
--         po_virtual
-----------------------------------------------------------------------------
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
      po_brands                       OUT      BRAND_ARRAY,
	  po_assohandsetimei_for_usim	  OUT	   VARCHAR2,
	  po_local_imsi					  OUT	   VARCHAR2,
	  po_remote_imsi				  OUT	   VARCHAR2,
	  po_assignable					  OUT	   NUMBER,
	  po_previously_activated         OUT      NUMBER,
      po_equipment_group              OUT      VARCHAR2,
	  po_last_event_type              OUT      VARCHAR2,
	  po_source_network_type_cd       OUT      VARCHAR2,
	  po_pre_post_paid_flag           OUT      VARCHAR2
   );

---------------------------------------------------------------------------------------
---------------------------------------------------------------------------------------
-- description: Procedure GetEquipmentInfoCheckPSseudo to get Equipment Information
-- Paramaters : (IN) pi_Serial_No - Serial Number of the equipment
--          (OUT)po_tech_type - identifies type of the equipment
--         po_product_cd - product code
--         po_product_status_cd - product status(New, Refurbished, etc.)
--         po_vendor_name - Manufacturer name of the equipment
--         po_vendor_no - Manufacturer number of the equipment
--         po_stolen - identifies if equipment stolen
--         po_sublock1 - Subsidy lock number
--         po_product_gp_type_id
--         po_product_gp_type_cd
--         po_product_gp_type_des
--         po_product_type_id
--         po_product_type_des
--         po_product_class_id
--         po_product_class_cd
--         po_product_class_des
--         po_provider_owner_id
--         po_lastmuleimei_for_sim
--         po_english_product_name
--         po_french_product_name
--         po_browser_version
--         po_firmware_version
--         po_prl_cd
--         po_prl_des
--         po_product_type_des_f
--         po_product_gp_type_des_f
--         po_product_type_list
--         po_initial_activation - identifies if equipment ever been activated
--         po_mode_code - mode of the PCS equipment
--         po_mode_description - Description of the PCS equipment mode
--                   po_product_type  - Knowbility Product Type
--         po_equipment_type - Knowbility Equipment Type
--         po_equipment_type_class
--         po_cross_fleet
--         po_cost
--         po_firmware_feature_code_list
--         po_browser_protocol
--         po_unscanned
--         po_equip_status_dt
--         po_puk
--           po_product_category_id
--         po_virtual
-----------------------------------------------------------------------------
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
   );

-----------------------------------------------------------------------------------------
---------------------------------------------------------------------------------------
-- description: Procedure GetEquipmentInfo to get Equipment Information( Modifyed for pager)
-- Paramaters : (IN) pi_Serial_No - Serial Number of the equipment
--
--    (OUT)po_tech_type - identifies type of the equipment
--         po_product_cd - product code
--         po_product_status_cd - product status(New, Refurbished, etc.)
--         po_vendor_name - Manufacturer name of the equipment
--         po_vendor_no - Manufacturer number of the equipment
--         po_stolen - identifies if equipment stolen
--         po_sublock1 - Subsidy lock number
--         po_product_gp_type_id
--         po_product_gp_type_cd
--         po_product_gp_type_des
--         po_product_type_id
--         po_product_type_des
--         po_product_class_id
--         po_product_class_cd
--         po_product_class_des
--         po_provider_owner_id
--         po_lastmuleimei_for_sim
--         po_english_product_name
--         po_french_product_name
--         po_browser_version
--         po_firmware_version
--         po_prl_cd
--         po_prl_des
--         po_product_type_des_f
--         po_product_gp_type_des_f
--         po_product_type_list
--         po_initial_activation - identifies if equipment ever been activated
--         po_mode_code - mode of the PCS equipment
--         po_mode_description - Description of the PCS equipment mode
--                   po_product_type  - Knowbility Product Type
--         po_equipment_type - Knowbility Equipment Type
--         po_equipment_type_class
--         po_cross_fleet
--         po_cost
--         po_cap_code
--         po_coverage_region_code_list
--         po_encoding_format_code
--         po_ownership_code
--         po_prepaid
--         po_frequency_cd
--         po_firmware_feature_code_list
--         po_browser_protocol
--         po_unscanned
--         po_equip_status_dt
--         po_puk
--         po_product_category_id
--         po_virtual
-----------------------------------------------------------------------------
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
   );

-----------------------------------------------------------------------------
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
   );

-----------------------------------------------------------------------------------------

   -----------------------------------------------------------------------------------------
-- description: Procedure GetIMEIBySIM to get IMEI ID by SIM ID
-- Paramaters : (IN) pi_SIM_ID - SIM ID
--
--    (OUT)po_IMEI_ID - IMEI ID
-----------------------------------------------------------------------------
   PROCEDURE getimeibysim (pi_sim_id IN VARCHAR2, po_imei_id OUT VARCHAR2);

----------------------------------------------------------------------------
-- description: Procedure GetSIMByIMEI to get  SIM ID by IMEI ID
-- Paramaters : (IN)po_IMEI_ID - IMEI ID
--
--    (OUT)pi_SIM_ID - SIM ID
-----------------------------------------------------------------------------
   PROCEDURE getsimbyimei (pi_imei_id IN VARCHAR2, po_sim_id OUT VARCHAR2);

----------------------------------------------------------------------------
-- description: Procedure GetMasterLockNumber to get Equipment Information
-- Paramaters : (IN) pi_Serial_No - Serial Number of the equipment
--         pi_user_id - ID  of the user, performing request
--         pi_lock_reason_id  - request reason
--
--    (OUT)
--         po_master_lock - Master lock number
-----------------------------------------------------------------------------
   PROCEDURE getmasterlocknumber (
      pi_serial_no        IN       VARCHAR2,
      pi_user_id          IN       VARCHAR2,
      pi_lock_reason_id   IN       NUMBER,
      po_master_lock      OUT      VARCHAR2
   );

----------------------------------------------------------------------------
-- description: Procedure GetMasterLockNumber to get Equipment Information
-- Paramaters : (IN) pi_Serial_No - Serial Number of the equipment
--         pi_user_id - ID  of the user, performing request
--         pi_lock_reason_id  - request reason
--         pi_outlet_id
--         pi_chnl_org_id
--
--    (OUT)
--         po_master_lock - Master lock number
-------------------------------------------------------------------------------------------------
   PROCEDURE getmasterlocknumber (
      pi_serial_no        IN       VARCHAR2,
      pi_user_id          IN       VARCHAR2,
      pi_lock_reason_id   IN       NUMBER,
      pi_outlet_id        IN       NUMBER,
      pi_chnl_org_id      IN       NUMBER,
      po_master_lock      OUT      VARCHAR2
   );

   TYPE product_info_record IS RECORD (
      product_id              product.product_id%TYPE,
      product_category_id     product.product_category_id%TYPE,
      vendor_name             manufacturer.manufacturer_name%TYPE,
      vendor_no               manufacturer.manufacturer_id%TYPE,
      product_gp_type_id      catalogue_item.sub_type_id%TYPE,
      product_gp_type_cd      ci_group.ci_group_cd%TYPE,
      product_gp_type_des     catalogue_item.catalogue_item_des%TYPE,
      product_gp_type_des_f   catalogue_item.catalogue_item_des%TYPE,
      product_type_id         catalogue_item.sub_type_id%TYPE,
      product_type_des        catalogue_item.catalogue_item_des%TYPE,
      product_type_des_f      catalogue_item.catalogue_item_des%TYPE,
      product_type_kb         product_class_xref_kb.product_type_knowbility%TYPE,
      equipment_type_kb       product_class_xref_kb.equip_type_knowbility%TYPE,
      equipment_type_class    kb_equip_type.equip_type_class%TYPE,
      cross_fleet             CHAR (1),
      product_cost            NUMBER (10, 2),
	  equipment_group         product_class_xref_kb.sems_equipment_group%TYPE
   );

--------------------------------------------------------------------------------------------------------------
   TYPE product_info_full IS RECORD (
      product_cd                   product.product_cd%TYPE,
      product_id                   product.product_id%TYPE,
      product_category_id          product.product_category_id%TYPE,
      vendor_name                  manufacturer.manufacturer_name%TYPE,
      vendor_no                    manufacturer.manufacturer_id%TYPE,
      product_gp_type_id           catalogue_item.sub_type_id%TYPE,
      product_gp_type_cd           ci_group.ci_group_cd%TYPE,
      product_gp_type_des          catalogue_item.catalogue_item_des%TYPE,
      product_gp_type_des_f        catalogue_item.catalogue_item_des%TYPE,
      product_type_id              catalogue_item.sub_type_id%TYPE,
      product_type_des             catalogue_item.catalogue_item_des%TYPE,
      product_type_des_f           catalogue_item.catalogue_item_des%TYPE,
      product_type_kb              product_class_xref_kb.product_type_knowbility%TYPE,
      equipment_type_kb            product_class_xref_kb.equip_type_knowbility%TYPE,
      equipment_type_class         kb_equip_type.equip_type_class%TYPE,
      technology_type              product.technology_type%TYPE,
      english_product_name         product.english_product_name%TYPE,
      french_product_name          product.french_product_name%TYPE,
      product_class_id             product.product_class_id%TYPE,
      product_status_cd            product.product_status_cd%TYPE,
      product_class_cd             product_classification.product_class_cd%TYPE,
      product_class_des            product_classification.product_class_des%TYPE,
      mode_code                    catalogue_item.sub_type_id%TYPE,
      mode_description             catalogue_item.catalogue_item_des%TYPE,
      product_type_list            VARCHAR2 (200),
      cross_fleet                  CHAR (1),
      equipment_status_type_id     NUMBER (22),
      equipment_status_id          NUMBER (22),
      equipment_status_dt          DATE,
      browser_version              firmware_browser.browser_version%TYPE,
      firmware_version             firmware_browser.firmware_version%TYPE,
      prl_cd                       firmware_browser.prl_cd%TYPE,
      prl_des                      prl_code.prl_des%TYPE,
      browser_protocol             VARCHAR2 (50),
      firmware_feature_code_list   VARCHAR2 (200),
      equipment_group         	   product_class_xref_kb.sems_equipment_group%TYPE,
      source_network_type_cd       product_class_xref_kb.source_network_type_cd%TYPE
   );

-------------------------------------------------------------------------------------------------------
   PROCEDURE getproductinfo (
      pi_product_id         IN       NUMBER,
      ro_product_info_rec   OUT      product_info_record
   );

   PROCEDURE getproductinfo (
      pi_product_id         IN       NUMBER,
      pi_tech_type_class    IN       VARCHAR2,
      ro_product_info_rec   OUT      product_info_record
   );

   PROCEDURE gettechnologytypeclass (
      pi_serial_no         IN       VARCHAR2,
      po_tech_type_class   OUT      VARCHAR2
   );

   PROCEDURE getfirmwareversionfeature (
      pi_firmware_version         IN       VARCHAR2,
      po_firmware_feature_codes   OUT      VARCHAR2
   );

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
   );

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
      po_equipment_group			  OUT      VARCHAR2,
      po_source_network_type_cd       OUT      VARCHAR2
   );

   PROCEDURE getproductinfobyproductcode (
      pi_product_cd         IN       VARCHAR2,
      pi_tech_type_class    IN       VARCHAR2,
      ro_product_info_rec   OUT      product_info_full
   );

   PROCEDURE getproductidbyproductcode (
      pi_product_cd   IN       VARCHAR2,
      po_product_id   OUT      NUMBER
   );

   PROCEDURE changeequipmentstatus (
      pi_serial_no                  IN   VARCHAR2,
      pi_user_id                    IN   VARCHAR2,
      pi_equipment_status_type_id   IN   NUMBER,
      pi_equipment_status_id        IN   NUMBER,
      pi_tech_type                  IN   VARCHAR2,
      pi_product_class_id           IN   NUMBER
   );

   PROCEDURE getequipmentgroup (
      pi_tech_type          IN       VARCHAR2,
      pi_product_class_id   IN       NUMBER,
      po_equipment_group    OUT      VARCHAR2
   );

   PROCEDURE getvirtualesn (
      pi_serial_no    IN       VARCHAR2,
      pi_tech_type    IN       VARCHAR2,
      po_virtual_no   OUT      VARCHAR2
   );
   
    PROCEDURE getvirtualesn (
      pi_serial_no    IN       VARCHAR2,
      po_virtual_no   OUT      VARCHAR2
   );

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
      po_brands                       OUT      BRAND_ARRAY,
	  po_equipment_group              OUT      VARCHAR2,
	  po_source_network_type_cd       OUT      VARCHAR2
   );

   PROCEDURE getesnbypseudoesn (
      pi_pseudo_serial_no   IN       VARCHAR2,
      po_serial_no          OUT      VARCHAR2
   );

   FUNCTION getallesnbypseudoesn (
      pi_pseudo_serial_no   IN       VARCHAR2
   ) RETURN ESN_TBL_T;
	
   PROCEDURE checkifesnvirtual (
      pi_serial_no    IN       VARCHAR2,
      po_is_virtual   OUT      NUMBER
   );

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
   );

   PROCEDURE getfeaturecodes (
      pi_product_cd      IN       VARCHAR2,
      po_feature_codes   OUT      refcursor
   );

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
	  po_assohandsetimei_for_usim	  OUT	   VARCHAR2,
	  po_local_imsi					  OUT	   VARCHAR2,
	  po_remote_imsi				  OUT	   VARCHAR2,
	  po_assignable					  OUT	   NUMBER,
      po_previously_activated         OUT      NUMBER,
      po_equipment_group              OUT      VARCHAR2,
	  po_last_event_type              OUT      VARCHAR2,
	  po_source_network_type_cd       OUT      VARCHAR2,
	  po_pre_post_paid_flag           OUT      VARCHAR2
   );

   PROCEDURE getproductmodes (
      pi_product_cd      IN       VARCHAR2,
      po_product_modes   OUT      refcursor
   );

   PROCEDURE isnewprepaidhandset (
      pi_serial_no     IN       VARCHAR2,
      pi_product_cd    IN       VARCHAR2,
      po_new_prepaid   OUT      NUMBER
   );

   PROCEDURE checklateststatus (
      pi_serial_no                  IN       VARCHAR2,
      pi_po_tech_type_class         IN       VARCHAR2,
      pi_equipment_status_type_id   IN       NUMBER,
      pi_equipment_status_id        IN       NUMBER,
      po_recordexist                OUT      BOOLEAN
   );

   PROCEDURE getbaseproductprice (
      pi_product_cd   IN       VARCHAR2,
      pi_province     IN       VARCHAR2,
      pi_npa          IN       VARCHAR2,
      pi_act_date     IN       DATE,
      po_base_price   OUT      refcursor
   );

   FUNCTION getrimpin (pi_serial_num IN VARCHAR2)
      RETURN VARCHAR2;

   PROCEDURE getbrands (pi_product_cd IN VARCHAR2, po_brands OUT brand_array);
   
   PROCEDURE getpcslockreasons (
   	  po_lock_reasons OUT refcursor
   );
   
   PROCEDURE getIMSIsByUSIM (
      pi_USIM          IN       VARCHAR2,
      po_IMSIs         OUT     refcursor
   ); 
   
   PROCEDURE getUSIMByIMSI(
      pi_IMSI          IN       VARCHAR2,
      po_USIM          OUT      VARCHAR2
   );
   
   PROCEDURE getUSIMListByIMSIs(
      pi_IMSIs          IN     t_IMSI_array,
      po_USIM_list     OUT     refcursor
   );
   
   PROCEDURE getassociatedhandsetbyusimid (
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
	  po_source_network_type_cd       OUT      VARCHAR2
   );
---------------------------------------------------------------------------------------
	PROCEDURE getimeibyusimid (pi_usim_id IN VARCHAR2, po_imei_id OUT VARCHAR2,po_last_event_type OUT VARCHAR2);

---------------------------------------------------------------------------------------
	PROCEDURE getprofilebyusimid (pi_usim_id IN VARCHAR2, po_imsi_local OUT VARCHAR2, po_imsi_remote OUT VARCHAR2);
---------------------------------------------------------------------------------------
	PROCEDURE getusimbyimei (pi_imei_id IN VARCHAR2, po_usim_id OUT VARCHAR2);

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
   FUNCTION excludekoodoprepaidbrand (pi_prepostflag IN PRODUCT.pre_post_paid_flag%TYPE, pi_brands IN BRAND_ARRAY)
      RETURN BRAND_ARRAY;
----------------------------------------------------------------------------

----------------------------------------------------------------------------
-- Description: This function returns version of the package used for
--							shakedown validation.
-- Parameters : 
--    (OUT)
--         version of the package
----------------------------------------------------------------------------
FUNCTION getVersion RETURN VARCHAR2;
----------------------------------------------------------------------------

END;



/
