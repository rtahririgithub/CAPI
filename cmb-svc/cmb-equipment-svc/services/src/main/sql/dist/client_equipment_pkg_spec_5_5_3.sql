CREATE OR REPLACE package Client_Equipment as
 ------------------------------------------------------------------------
-- description: Package Client_Equipment  containing procedures
--		to interface to the DIST database
--
-- Database: DIST	| Schema: IVRADM
-- Date	   		Developer     	  	  Modifications
-- 11-SEP-2000		Ludmila Pomirche	  created
-- 24-Oct-2000		Ludmila Pomirche          added GetMasterLockNumber procedure
-- 30-Jan-2001		Ludmila Pomirche	  added second GetEquipmentInfo procedure
-- 30-Jan-2001		Ludmila Pomirche	  added GetIMEIBySIM  procedure
-- 30-Jan-2001		Ludmila Pomirche	  added GetSIMByIMEI procedure
-- 22-Feb-2001		Ludmila Pomirche	  added logic for analog phones
-- 27-Feb-2001		Ludmila Pomirche	  po_provider_owner_id	OUT parameter added
-- 02-Mar-2001		Ludmila Pomirche          po_equipment_status_type_id, po_equipment_status_id added
-- 22-May-2001		Ludmila Pomirche	  added cursor for vendor query
-- 31-May-2001		Ludmila Pomirche	  added second GetMasterLockNumber procedure for dealers
-- 30-Jul-2001		Ludmila Pomirche	  Misc changes for Fusion East Project\
-- 18-Feb-2002		Ludmila Pomirche	  PRL ,webbrowser and firmware version added to retrieval
--						  Added methods getEquipment with phone_no as parameter
-- 19-Mar-2002		Ludmila Pomirche	  Added po_Mode parameter to getEquipment
-- 30-Apr-2002		Ludmila Pomirche	  Lost/Stolen validation addition
-- 18-Jun-2002		Ludmila Pomirche	  added GetWarranty procedure
-- 26-Feb-2002		Ludmila Pomirche	  added Equipment Type and Product Type as out parameters
-- 29-Jul-2003		Ludmila Pomirche	  Pager attributes added
-- 10-SEP-2003		Daphne Chan     	  add second GetProductInfo procedure
-- 27-Oct-2003		Daphne Chan     	  change the po_new_param_1 to po_firmware_feature_code_list
--                                                for Chameleon.
--                                                Add GetFirmwareVersionFeature procedure
-- 11-Mar-2004		Ludmila Pomirche	  new procedure getMuleBySIM
-- 15-Mar-2005          Marina Kuper              Changed queries for GetProductInfo (P3MS Re-archtecture )
-- 31-June-2005         Marina Kuper              Changed cursor c_product_gr_type (RELATIONSHIP_TYPE_CD = 'SKU_COMP')
-------------------------------------------------------------------------------
ESNNotFound		Exception;
PRAGMA			EXCEPTION_INIT(ESNNotFound, -20310);
UserIDNotExist		Exception;
PRAGMA			EXCEPTION_INIT(UserIDNotExist, -20311);
ReachedMaxLock		Exception;
PRAGMA			EXCEPTION_INIT(ReachedMaxLock, -20312);
IMEINotFound		Exception;
PRAGMA			EXCEPTION_INIT(IMEINotFound, -20313);
SIMNotFound		Exception;
PRAGMA			EXCEPTION_INIT(SIMNotFound, -20314);
multipleIMEIforSIM	Exception;
PRAGMA			EXCEPTION_INIT(multipleIMEIforSIM, -20315);
multipleSIMforIMEI	Exception;
PRAGMA			EXCEPTION_INIT(multipleSIMforIMEI, -20316);
PhoneNotFound		Exception;
PRAGMA			EXCEPTION_INIT(PhoneNotFound, -20317);
------------------------------------------------------------------------------

------------------------------------------------------------------------------
-- description: Procedure GetEquipmentInfo to get Equipment Information
-- Paramaters : (IN) pi_Serial_No - Serial Number of the equipment
--		     pi_tech_type - identifies type of the equipment
--		     pi_initial_activation - identifies if equipment ever been activated
--
--		(OUT)po_product_cd - product code
--		     po_product_status_cd - product status(New, Refurbished, etc.)
--		     po_vendor_name - Manufacturer name of the equipment
--		     po_vendor_no - Manufacturer number of the equipment
--		     po_stolen - identifies if equipment stolen
--		     po_sublock1 - Subsidy lock number
--		     po_product_gp_type_id
--		     po_product_gp_type_cd
--		     po_product_gp_type_des
--	 	     po_product_type_id
--		     po_product_type_des
--		     po_product_class_id
--		     po_product_class_cd
--		     po_product_class_des
--		     po_provider_owner_id
--		     po_lastmuleimei_for_sim
--		     po_english_product_name
--		     po_french_product_name
--		     po_browser_version
--		     po_firmware_version
--		     po_prl_cd
--		     po_prl_des
--		     po_product_type_des_f
--		     po_product_gp_type_des_f
--		     po_min_cd
--		     po_customer_id
--		     po_product_type_list
--		     po_initial_activation - identifies if equipment ever been activated
--		     po_mode_code - mode of the PCS equipment
--		     po_mode_description - Description of the PCS equipment mode
--                   po_product_type  - Knowbility Product Type
--		     po_equipment_type - Knowbility Equipment Type
--		     po_equipment_type_class
--		     po_cross_fleet
--		     po_cost
--		     po_cap_code
--		     po_coverage_region_code_list
--		     po_encoding_format_code
--		     po_ownership_code
--		     po_prepaid
--		     po_frequency_cd
--		     po_firmware_feature_code_list
--		     po_browser_protocol
--		     po_new_param_3
-----------------------------------------------------------------------------
Procedure GetEquipmentInfo (pi_serial_no			IN	varchar2
			    ,pi_tech_type_class			IN	varchar2
			    ,pi_initial_activation		IN      boolean
			    ,po_tech_type			OUT	varchar2
			    ,po_product_cd			OUT     varchar2
			    ,po_product_status_cd		OUT     varchar2
			    ,po_vendor_name			OUT	varchar2
			    ,po_vendor_no			OUT	varchar2
			    ,po_equipment_status_type_id 	OUT	number
			    ,po_equipment_status_id		OUT	number
			    ,po_stolen				OUT     number
			    ,po_sublock1			OUT	varchar2
			    ,po_product_gp_type_id		OUT	number
			    ,po_product_gp_type_cd		OUT	varchar2
			    ,po_product_gp_type_des		OUT	varchar2
			    ,po_product_type_id			OUT	number
			    ,po_product_type_des		OUT	varchar2
			    ,po_product_class_id		OUT	number
			    ,po_product_class_cd		OUT	varchar2
			    ,po_product_class_des		OUT	varchar2
			    ,po_provider_owner_id		OUT	number
			    ,po_lastmuleimei_for_sim    	OUT	varchar2
			    ,po_english_product_name		OUT	varchar2
			    ,po_french_product_name		OUT	varchar2
			    ,po_browser_version			OUT	varchar2
			    ,po_firmware_version		OUT     varchar2
			    ,po_prl_cd				OUT     varchar2
			    ,po_prl_des				OUT     varchar2
			    ,po_product_type_des_f		OUT     varchar2
			    ,po_product_gp_type_des_f           OUT	varchar2
			    ,po_min_cd				OUT	varchar2
			    ,po_customer_id		        OUT	number
			    ,po_product_type_list	        OUT     varchar2
			    ,po_initial_activation		OUT     varchar2
			    ,po_mode_code	        	OUT     number
			    ,po_mode_description		OUT     varchar2
			    ,po_product_type  			OUT     varchar2
		            ,po_equipment_type 			OUT     varchar2
		            ,po_equipment_type_class		OUT     varchar2
		            ,po_cross_fleet			OUT     varchar2
		            ,po_cost				OUT     number
			    ,po_cap_code 			OUT     varchar2
			    ,po_coverage_region_code_list	OUT     varchar2
		            ,po_encoding_format_code		OUT     varchar2
		            ,po_ownership_code			OUT     varchar2
		            ,po_prepaid				OUT     varchar2
		            ,po_frequency_cd			OUT     varchar2
		            ,po_firmware_feature_code_list	OUT     varchar2
			    ,po_browser_protocol		OUT     varchar2
		            ,po_new_param_3			OUT     number
		            );
----------------------------------------------------------------------------

-- description: Procedure GetEquipmentInfoByPhoneNo to get Equipment Information
-- Paramaters : (IN) pi_phone_number  - Phone Number of the equipment
--
--		(OUT)po_Serial_No - Serial Number of the equipment
--	             po_tech_type - identifies type of the equipment
--		     po_product_cd - product code
--		     po_product_status_cd - product status(New, Refurbished, etc.)
--		     po_vendor_name - Manufacturer name of the equipment
--		     po_vendor_no - Manufacturer number of the equipment
--		     po_stolen - identifies if equipment stolen
--		     po_sublock1 - Subsidy lock number
--		     po_product_gp_type_id
--		     po_product_gp_type_cd
--		     po_product_gp_type_des
--		     po_product_type_id
--		     po_product_type_des
--		     po_product_class_id
--		     po_product_class_cd
--		     po_product_class_des
--		     po_provider_owner_id
--		     po_lastmuleimei_for_sim
--		     po_english_product_name
--		     po_french_product_name
--		     po_browser_version
--		     po_firmware_version
--		     po_prl_cd
--		     po_prl_des
--		     po_product_type_des_f
--		     po_product_gp_type_des_f
--		     po_product_type_list
--		     po_initial_activation - identifies if equipment ever been activated
--		     po_mode_code - mode of the PCS equipment
--		     po_mode_description - Description of the PCS equipment mode
--                   po_product_type  - Knowbility Product Type
--		     po_equipment_type - Knowbility Equipment Type
--		     po_equipment_type_class
--		     po_cross_fleet
--		     po_cost
--		     po_firmware_feature_code_list
--		     po_browser_protocol
--		     po_new_param_3
-----------------------------------------------------------------------------
Procedure GetEquipmentInfoByPhoneNo (pi_phone_number IN varchar2
		           ,po_serial_no			OUT	varchar2
			    ,po_tech_type			OUT	varchar2
			    ,po_product_cd			OUT     varchar2
			    ,po_product_status_cd		OUT     varchar2
			    ,po_vendor_name			OUT	varchar2
			    ,po_vendor_no			OUT	varchar2
			    ,po_equipment_status_type_id 	OUT	number
			    ,po_equipment_status_id		OUT	number
			    ,po_stolen				OUT     number
			    ,po_sublock1			OUT	varchar2
			    ,po_product_gp_type_id		OUT	number
			    ,po_product_gp_type_cd		OUT	varchar2
			    ,po_product_gp_type_des		OUT	varchar2
			    ,po_product_type_id			OUT	number
			    ,po_product_type_des		OUT	varchar2
			    ,po_product_class_id		OUT	number
			    ,po_product_class_cd		OUT	varchar2
			    ,po_product_class_des		OUT	varchar2
			    ,po_provider_owner_id		OUT	number
			    ,po_lastmuleimei_for_sim    	OUT	varchar2
			    ,po_english_product_name		OUT	varchar2
		     	    ,po_french_product_name		OUT	varchar2
		      	    ,po_browser_version			OUT	varchar2
			    ,po_firmware_version		OUT	varchar2
		     	    ,po_prl_cd				OUT	varchar2
		     	    ,po_prl_des				OUT	varchar2
			    ,po_product_type_des_f		OUT	varchar2
			    ,po_product_gp_type_des_f           OUT	varchar2
			    ,po_min_cd				OUT	varchar2
			    ,po_customer_id		        OUT	number
			    ,po_product_type_list	        OUT     varchar2
			    ,po_initial_activation		OUT      varchar2
			    ,po_mode_code	        	OUT     number
			    ,po_mode_description		OUT     varchar2
			    ,po_product_type  			OUT     varchar2
		            ,po_equipment_type 			OUT     varchar2
		            ,po_equipment_type_class		OUT     varchar2
		            ,po_cross_fleet			OUT     varchar2
		            ,po_cost				OUT     number
			    ,po_firmware_feature_code_list      OUT     varchar2
			    ,po_browser_protocol		OUT     varchar2
		            ,po_new_param_3			OUT     number);
---------------------------------------------------------------------------------------
-- description: Procedure GetEquipmentInfo to get Equipment Information
-- Paramaters : (IN) pi_Serial_No - Serial Number of the equipment
--
--		(OUT)po_tech_type - identifies type of the equipment
--		     po_product_cd - product code
--		     po_product_status_cd - product status(New, Refurbished, etc.)
--		     po_vendor_name - Manufacturer name of the equipment
--		     po_vendor_no - Manufacturer number of the equipment
--		     po_stolen - identifies if equipment stolen
--		     po_sublock1 - Subsidy lock number
--		     po_product_gp_type_id
--		     po_product_gp_type_cd
--		     po_product_gp_type_des
--		     po_product_type_id
--		     po_product_type_des
--		     po_product_class_id
--		     po_product_class_cd
--		     po_product_class_des
--		     po_provider_owner_id
--		     po_lastmuleimei_for_sim
--		     po_english_product_name
--		     po_french_product_name
--		     po_browser_version
--		     po_firmware_version
--		     po_prl_cd
--		     po_prl_des
--		     po_product_type_des_f
--		     po_product_gp_type_des_f
--		     po_product_type_list
--		     po_initial_activation - identifies if equipment ever been activated
--		     po_mode_code - mode of the PCS equipment
--		     po_mode_description - Description of the PCS equipment mode
--                   po_product_type  - Knowbility Product Type
--		     po_equipment_type - Knowbility Equipment Type
--		     po_equipment_type_class
--		     po_cross_fleet
--		     po_cost
--		     po_firmware_feature_code_list
--		     po_browser_protocol
--		     po_new_param_3
-----------------------------------------------------------------------------
Procedure GetEquipmentInfo (pi_serial_no			IN	varchar2
		            ,po_serial_no			OUT	varchar2
			    ,po_tech_type			OUT	varchar2
			    ,po_product_cd			OUT     varchar2
			    ,po_product_status_cd		OUT     varchar2
			    ,po_vendor_name			OUT	varchar2
			    ,po_vendor_no			OUT	varchar2
			    ,po_equipment_status_type_id 	OUT	number
			    ,po_equipment_status_id		OUT	number
			    ,po_stolen				OUT     number
			    ,po_sublock1			OUT	varchar2
			    ,po_product_gp_type_id		OUT	number
			    ,po_product_gp_type_cd		OUT	varchar2
			    ,po_product_gp_type_des		OUT	varchar2
			    ,po_product_type_id			OUT	number
			    ,po_product_type_des		OUT	varchar2
			    ,po_product_class_id		OUT	number
			    ,po_product_class_cd		OUT	varchar2
			    ,po_product_class_des		OUT	varchar2
			    ,po_provider_owner_id		OUT	number
			    ,po_lastmuleimei_for_sim    	OUT	varchar2
			    ,po_english_product_name		OUT	varchar2
		     	    ,po_french_product_name		OUT	varchar2
		      	    ,po_browser_version			OUT	varchar2
			    ,po_firmware_version		OUT	varchar2
		     	    ,po_prl_cd				OUT	varchar2
		     	    ,po_prl_des				OUT	varchar2
			    ,po_product_type_des_f		OUT	varchar2
			    ,po_product_gp_type_des_f           OUT	varchar2
			    ,po_min_cd				OUT	varchar2
			    ,po_customer_id		        OUT	number
			    ,po_product_type_list	        OUT     varchar2
			    ,po_initial_activation		OUT      varchar2
			    ,po_mode_code	        	OUT     number
			    ,po_mode_description		OUT     varchar2
			    ,po_product_type  			OUT     varchar2
		            ,po_equipment_type 			OUT     varchar2
		            ,po_equipment_type_class		OUT     varchar2
		            ,po_cross_fleet			OUT     varchar2
		            ,po_cost				OUT     number
			    ,po_firmware_feature_code_list	OUT     varchar2
			    ,po_browser_protocol		OUT     varchar2
		            ,po_new_param_3			OUT     number);
-----------------------------------------------------------------------------------------
---------------------------------------------------------------------------------------
-- description: Procedure GetEquipmentInfo to get Equipment Information( Modifyed for pager)
-- Paramaters : (IN) pi_Serial_No - Serial Number of the equipment
--
--		(OUT)po_tech_type - identifies type of the equipment
--		     po_product_cd - product code
--		     po_product_status_cd - product status(New, Refurbished, etc.)
--		     po_vendor_name - Manufacturer name of the equipment
--		     po_vendor_no - Manufacturer number of the equipment
--		     po_stolen - identifies if equipment stolen
--		     po_sublock1 - Subsidy lock number
--		     po_product_gp_type_id
--		     po_product_gp_type_cd
--		     po_product_gp_type_des
--		     po_product_type_id
--		     po_product_type_des
--		     po_product_class_id
--		     po_product_class_cd
--		     po_product_class_des
--		     po_provider_owner_id
--		     po_lastmuleimei_for_sim
--		     po_english_product_name
--		     po_french_product_name
--		     po_browser_version
--		     po_firmware_version
--		     po_prl_cd
--		     po_prl_des
--		     po_product_type_des_f
--		     po_product_gp_type_des_f
--		     po_product_type_list
--		     po_initial_activation - identifies if equipment ever been activated
--		     po_mode_code - mode of the PCS equipment
--		     po_mode_description - Description of the PCS equipment mode
--                   po_product_type  - Knowbility Product Type
--		     po_equipment_type - Knowbility Equipment Type
--		     po_equipment_type_class
--		     po_cross_fleet
--		     po_cost
--		     po_cap_code
--		     po_coverage_region_code_list
--		     po_encoding_format_code
--		     po_ownership_code
--		     po_prepaid
--		     po_frequency_cd
--		     po_firmware_feature_code_list
--		     po_browser_protocol
--		     po_new_param_3
-----------------------------------------------------------------------------
Procedure GetEquipmentInfo (pi_serial_no			IN	varchar2
		            ,po_serial_no			OUT	varchar2
			    ,po_tech_type			OUT	varchar2
			    ,po_product_cd			OUT     varchar2
			    ,po_product_status_cd		OUT     varchar2
			    ,po_vendor_name			OUT	varchar2
			    ,po_vendor_no			OUT	varchar2
			    ,po_equipment_status_type_id 	OUT	number
			    ,po_equipment_status_id		OUT	number
			    ,po_stolen				OUT     number
			    ,po_sublock1			OUT	varchar2
			    ,po_product_gp_type_id		OUT	number
			    ,po_product_gp_type_cd		OUT	varchar2
			    ,po_product_gp_type_des		OUT	varchar2
			    ,po_product_type_id			OUT	number
			    ,po_product_type_des		OUT	varchar2
			    ,po_product_class_id		OUT	number
			    ,po_product_class_cd		OUT	varchar2
			    ,po_product_class_des		OUT	varchar2
			    ,po_provider_owner_id		OUT	number
			    ,po_lastmuleimei_for_sim    	OUT	varchar2
			    ,po_english_product_name		OUT	varchar2
		     	    ,po_french_product_name		OUT	varchar2
		      	    ,po_browser_version			OUT	varchar2
			    ,po_firmware_version		OUT	varchar2
		     	    ,po_prl_cd				OUT	varchar2
		     	    ,po_prl_des				OUT	varchar2
			    ,po_product_type_des_f		OUT	varchar2
			    ,po_product_gp_type_des_f           OUT	varchar2
			    ,po_min_cd				OUT	varchar2
			    ,po_customer_id		        OUT	number
			    ,po_product_type_list	        OUT     varchar2
			    ,po_initial_activation		OUT      varchar2
			    ,po_mode_code	        	OUT     number
			    ,po_mode_description		OUT     varchar2
			    ,po_product_type  			OUT     varchar2
		            ,po_equipment_type 			OUT     varchar2
		            ,po_equipment_type_class		OUT     varchar2
		            ,po_cross_fleet			OUT     varchar2
		            ,po_cost				OUT     number
		            ,po_cap_code 			OUT     varchar2
			    ,po_coverage_region_code_list	OUT     varchar2
		            ,po_encoding_format_code		OUT     varchar2
		            ,po_ownership_code			OUT     varchar2
		            ,po_prepaid				OUT     varchar2
		            ,po_frequency_cd			OUT     varchar2
			    ,po_firmware_feature_code_list	OUT     varchar2
			    ,po_browser_protocol		OUT     varchar2
		            ,po_new_param_3			OUT     number);
-----------------------------------------------------------------------------
Procedure GetMuleBySIM	    (pi_sim				IN	varchar2
		            ,po_imei				OUT	varchar2
			    ,po_tech_type			OUT	varchar2
			    ,po_product_cd			OUT     varchar2
			    ,po_product_status_cd		OUT     varchar2
			    ,po_vendor_name			OUT	varchar2
			    ,po_vendor_no			OUT	varchar2
			    ,po_equipment_status_type_id 	OUT	number
			    ,po_equipment_status_id		OUT	number
			    ,po_stolen				OUT     number
			    ,po_sublock1			OUT	varchar2
			    ,po_product_gp_type_id		OUT	number
			    ,po_product_gp_type_cd		OUT	varchar2
			    ,po_product_gp_type_des		OUT	varchar2
			    ,po_product_type_id			OUT	number
			    ,po_product_type_des		OUT	varchar2
			    ,po_product_class_id		OUT	number
			    ,po_product_class_cd		OUT	varchar2
			    ,po_product_class_des		OUT	varchar2
			    ,po_provider_owner_id		OUT	number
			    ,po_lastmuleimei_for_sim    	OUT	varchar2
			    ,po_english_product_name		OUT	varchar2
		     	    ,po_french_product_name		OUT	varchar2
		      	    ,po_browser_version			OUT	varchar2
			    ,po_firmware_version		OUT	varchar2
		     	    ,po_prl_cd				OUT	varchar2
		     	    ,po_prl_des				OUT	varchar2
			    ,po_product_type_des_f		OUT	varchar2
			    ,po_product_gp_type_des_f           OUT	varchar2
			    ,po_min_cd				OUT	varchar2
			    ,po_customer_id		        OUT	number
			    ,po_product_type_list	        OUT     varchar2
			    ,po_initial_activation		OUT      varchar2
			    ,po_mode_code	        	OUT     number
			    ,po_mode_description		OUT     varchar2
			    ,po_product_type  			OUT     varchar2
		            ,po_equipment_type 			OUT     varchar2
		            ,po_equipment_type_class		OUT     varchar2
		            ,po_cross_fleet			OUT     varchar2
		            ,po_cost				OUT     number
		            ,po_cap_code 			OUT     varchar2
			    ,po_coverage_region_code_list	OUT     varchar2
		            ,po_encoding_format_code		OUT     varchar2
		            ,po_ownership_code			OUT     varchar2
		            ,po_prepaid				OUT     varchar2
		            ,po_frequency_cd			OUT     varchar2
			    ,po_firmware_feature_code_list	OUT     varchar2
			    ,po_browser_protocol		OUT     varchar2
		            ,po_new_param_3			OUT     number);
-----------------------------------------------------------------------------------------
-- description: Procedure GetIMEIBySIM to get IMEI ID by SIM ID
-- Paramaters : (IN) pi_SIM_ID - SIM ID
--
--		(OUT)po_IMEI_ID - IMEI ID
-----------------------------------------------------------------------------
Procedure GetIMEIBySIM  (pi_SIM_ID		IN	varchar2
			 ,po_IMEI_ID		OUT	varchar2
			  );
----------------------------------------------------------------------------
-- description: Procedure GetSIMByIMEI to get  SIM ID by IMEI ID
-- Paramaters : (IN)po_IMEI_ID - IMEI ID
--
--		(OUT)pi_SIM_ID - SIM ID
-----------------------------------------------------------------------------
Procedure GetSIMByIMEI (pi_IMEI_ID		IN	varchar2
			,po_SIM_ID		OUT	varchar2
			 );
----------------------------------------------------------------------------
-- description: Procedure GetMasterLockNumber to get Equipment Information
-- Paramaters : (IN) pi_Serial_No - Serial Number of the equipment
--		     pi_user_id - ID  of the user, performing request
--		     pi_lock_reason_id	- request reason
--
--		(OUT)
--		     po_master_lock - Master lock number
-----------------------------------------------------------------------------
Procedure GetMasterLockNumber (pi_serial_no		IN	varchar2
			        ,pi_user_id		IN	varchar2
			    	,pi_lock_reason_id	IN      number
			    	,po_master_lock		OUT	varchar2) ;
----------------------------------------------------------------------------
-- description: Procedure GetMasterLockNumber to get Equipment Information
-- Paramaters : (IN) pi_Serial_No - Serial Number of the equipment
--		     pi_user_id - ID  of the user, performing request
--		     pi_lock_reason_id	- request reason
--		     pi_outlet_id
--		     pi_chnl_org_id
--
--		(OUT)
--		     po_master_lock - Master lock number
-------------------------------------------------------------------------------------------------
Procedure GetMasterLockNumber (pi_serial_no		IN	varchar2
			        ,pi_user_id		IN	varchar2
			    	,pi_lock_reason_id	IN      number
			    	,pi_outlet_id		IN	number
			    	,pi_chnl_org_id		IN	number
			    	,po_master_lock		OUT	varchar2) ;


Type Product_Info_Record is record
	 			(product_id		        	product.PRODUCT_ID%Type
			    ,vendor_name			    manufacturer.MANUFACTURER_NAME%Type
			    ,vendor_no			        manufacturer.MANUFACTURER_ID%Type
			    ,product_gp_type_id			catalogue_item .SUB_TYPE_ID%Type
			    ,product_gp_type_cd			ci_group .CI_GROUP_CD%Type
			    ,product_gp_type_des		catalogue_item .CATALOGUE_ITEM_DES%Type
			    ,product_gp_type_des_f		catalogue_item .CATALOGUE_ITEM_DES%Type
			    ,product_type_id			catalogue_item .SUB_TYPE_ID%Type
			    ,product_type_des			catalogue_item .CATALOGUE_ITEM_DES%Type
			    ,product_type_des_f			catalogue_item .CATALOGUE_ITEM_DES%Type
			    ,product_type_kb			product_class_xref_kb.PRODUCT_TYPE_KNOWBILITY%Type
			    ,equipment_type_kb			product_class_xref_kb.EQUIP_TYPE_KNOWBILITY%Type
			    ,equipment_type_class	        kb_equip_type.EQUIP_TYPE_CLASS%Type
			    ,cross_fleet			char(1)
			    ,product_cost			number(10,2));
-------------------------------------------------------------------------------------------------------
Procedure GetProductInfo (pi_product_id				IN	number
			  ,ro_product_info_rec			OUT	Product_Info_Record );

Procedure GetProductInfo (pi_product_id				IN	number
                         ,pi_tech_type_class                    IN      varchar2
			  ,ro_product_info_rec			OUT	Product_Info_Record );

Procedure GetTechnologyTypeClass(pi_serial_no		 IN varchar2
				, po_tech_type_class	 OUT varchar2);

Procedure GetFirmwareVersionFeature (pi_firmware_version	IN varchar2
                                    ,po_firmware_feature_codes	OUT varchar2);

Procedure GetWarranty(pi_serial_no			IN      varchar2
		      ,po_warranty_exp_date		OUT	date
  		      ,po_initial_activation_date 	OUT	date
  		      ,po_initial_manufacture_date 	OUT	date
  		      ,po_latest_pending_date		OUT	date
  		      ,po_latest_pending_model		OUT	varchar2
  		      ,po_message			OUT	varchar2
  		      ,po_warranty_extension_date	OUT	date
  		      ,po_DOA_expiry_date		OUT	date);
---------------------------------------------------------------------------------------------------------
End;
/
