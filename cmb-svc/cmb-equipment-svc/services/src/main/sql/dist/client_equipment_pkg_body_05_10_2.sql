
*** SCRIPT START :  Session:IVRADM3@DDIST(1)   30-Aug-2005 9:27:46 *** 
Processing ...
CREATE OR REPLACE package body Client_Equipment as


------------------------------------------------------------------------------------

-----------------------------------------------------------------------------------
---------------------------------------------------------------------------------
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
		            ,po_new_param_3			OUT     number
                            ,po_equip_status_dt           OUT     date
                            ,po_puk           OUT     varchar2
                            ,po_product_category_id  OUT number
                    ) is

 v_tech_type_class		varchar2(10);
 v_initial_activation   	boolean:=true;
 v_cap_code 			varchar2(30);
 v_coverage_region_code_list	varchar2(255);
 v_encoding_format_code		varchar2(2);
 v_ownership_code		varchar2(20);
 v_prepaid			varchar2(1);
 v_frequency_cd			varchar2(20);
Begin
po_serial_no:= pi_serial_no;
GetTechnologyTypeClass(pi_serial_no,v_tech_type_class);
	GetEquipmentInfo(pi_serial_no
			 ,v_tech_type_class
			 ,v_initial_activation
			 ,po_tech_type
			 ,po_product_cd
			 ,po_product_status_cd
			 ,po_vendor_name
			 ,po_vendor_no
			 ,po_equipment_status_type_id
			 ,po_equipment_status_id
			 ,po_stolen
			 ,po_sublock1
			 ,po_product_gp_type_id
			 ,po_product_gp_type_cd
			 ,po_product_gp_type_des
			 ,po_product_type_id
			 ,po_product_type_des
			 ,po_product_class_id
			 ,po_product_class_cd
			 ,po_product_class_des
			 ,po_provider_owner_id
			 ,po_lastmuleimei_for_sim
			 ,po_english_product_name
		     	 ,po_french_product_name
		     	 ,po_browser_version
			 ,po_firmware_version
		     	 ,po_prl_cd
		     	 ,po_prl_des
			 ,po_product_type_des_f
			  ,po_product_gp_type_des_f
			  ,po_min_cd
			  ,po_customer_id
			  ,po_product_type_list
			  ,po_initial_activation
			  ,po_mode_code
			  ,po_mode_description
			  ,po_product_type
		          ,po_equipment_type
		          ,po_equipment_type_class
		          ,po_cross_fleet
		          ,po_cost
		          ,v_cap_code
 			  ,v_coverage_region_code_list
 			  ,v_encoding_format_code
 			  ,v_ownership_code
 			  ,v_prepaid
 			  ,v_frequency_cd
			  ,po_firmware_feature_code_list
			  ,po_browser_protocol
		          ,po_new_param_3
                          ,po_equip_status_dt
                          ,po_puk
                          ,po_product_category_id
                  );

Exception
When ESNNotFound Then
	RAISE_APPLICATION_ERROR(-20310,'Client_Equipment pkg: ESN or Product ID not found');
When Others Then
	raise;
-- 	RAISE_APPLICATION_ERROR(SQLCODE,SQLERRM);
End GetEquipmentInfo;

------------------------------------------------------------------------------------
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
		            ,po_new_param_3			OUT     number
                            ,po_equip_status_dt                 OUT     date
                            ,po_puk           OUT     varchar2
                            ,po_product_category_id  OUT number
                    ) is

 v_tech_type_class	varchar2(10);
 v_initial_activation   boolean:=true;
Begin
po_serial_no:= pi_serial_no;
GetTechnologyTypeClass(pi_serial_no,v_tech_type_class);
	GetEquipmentInfo(pi_serial_no
			 ,v_tech_type_class
			 ,v_initial_activation
			 ,po_tech_type
			 ,po_product_cd
			 ,po_product_status_cd
			 ,po_vendor_name
			 ,po_vendor_no
			 ,po_equipment_status_type_id
			 ,po_equipment_status_id
			 ,po_stolen
			 ,po_sublock1
			 ,po_product_gp_type_id
			 ,po_product_gp_type_cd
			 ,po_product_gp_type_des
			 ,po_product_type_id
			 ,po_product_type_des
			 ,po_product_class_id
			 ,po_product_class_cd
			 ,po_product_class_des
			 ,po_provider_owner_id
			 ,po_lastmuleimei_for_sim
			 ,po_english_product_name
		     	 ,po_french_product_name
		     	 ,po_browser_version
			 ,po_firmware_version
		     	 ,po_prl_cd
		     	 ,po_prl_des
			 ,po_product_type_des_f
			  ,po_product_gp_type_des_f
			  ,po_min_cd
			  ,po_customer_id
			  ,po_product_type_list
			  ,po_initial_activation
			  ,po_mode_code
			  ,po_mode_description
			  ,po_product_type
		          ,po_equipment_type
		          ,po_equipment_type_class
		          ,po_cross_fleet
		          ,po_cost
		          ,po_cap_code
			  ,po_coverage_region_code_list
		          ,po_encoding_format_code
		          ,po_ownership_code
		          ,po_prepaid
		          ,po_frequency_cd
			  ,po_firmware_feature_code_list
			  ,po_browser_protocol
		          ,po_new_param_3
                          ,po_equip_status_dt
                          ,po_puk
                          ,po_product_category_id
                  );

Exception
When ESNNotFound Then
	RAISE_APPLICATION_ERROR(-20310,'Client_Equipment pkg: ESN or Product ID not found');
When Others Then
	raise;
-- 	RAISE_APPLICATION_ERROR(SQLCODE,SQLERRM);
End GetEquipmentInfo;

------------------------------------------------------------------------------------
------------------------------------------------------------------------------------
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
			    ,po_firmware_feature_code_list	OUT     varchar2
			    ,po_browser_protocol		OUT     varchar2
		            ,po_new_param_3			OUT     number
                            ,po_equip_status_dt                 OUT     date
                            ,po_puk           OUT     varchar2
                            ,po_product_category_id OUT     number
                    ) is
cursor c_equipment_PCS is
 	select pe.serial_no from pcs_equipment pe, pcs_equip_status pes
	where pe.min_cd=pi_phone_number
	and   pe.seq_no=0
	and   pes.serial_no=pe.serial_no
	and   pes.seq_no=pe.seq_no
	and   pes.equipment_status_type_id=2 and pes.equipment_status_id in (8,88)
	and   pes.equipment_status_dt =
       		(select max(equipment_status_dt)
       		from pcs_equip_status pes1
       		where pes1.serial_no=pes.serial_no
       		and   pes1.seq_no=pes.seq_no
       		and   pes1.equipment_status_type_id=2 )
	order by  equipment_status_dt desc;


cursor c_equipment_MIKE is
 	select pe.serial_no from iden_equipment pe, iden_equip_status pes
	where pe.min_cd=pi_phone_number
	and   pe.seq_no=0
	and   pes.serial_no=pe.serial_no
	and   pes.seq_no=pe.seq_no
	and   pes.equipment_status_type_id=2 and pes.equipment_status_id in (8,88)
	and   pes.equipment_status_dt =
       		(select max(equipment_status_dt)
       		from iden_equip_status pes1
       		where pes1.serial_no=pes.serial_no
       		and   pes1.seq_no=pes.seq_no
       		and   pes1.equipment_status_type_id=2 )
	order by  equipment_status_dt desc;

cursor c_equip_analog is
 	select pe.serial_no from analog_equip pe
	where pe.min_cd=pi_phone_number
	and   pe.seq_no=0
	order by update_dt desc;
 v_serial_no		pcs_equipment.SERIAL_NO%Type;

 v_initial_activation  boolean:=false;
 v_tech_type_class     varchar2(10);
 v_cap_code 			varchar2(30);
 v_coverage_region_code_list	varchar2(255);
 v_encoding_format_code		varchar2(2);
 v_ownership_code		 varchar2(20);
 v_prepaid			varchar2(1);
 v_frequency_cd			varchar2(20);

Begin
open c_equipment_PCS;
fetch c_equipment_PCS into  v_serial_no;
close c_equipment_PCS;
If 	v_serial_no  is not null Then
	v_tech_type_class:='PCS';
Else
	open c_equipment_MIKE;
	fetch c_equipment_MIKE into  v_serial_no;
	close c_equipment_MIKE;
	If 	v_serial_no  is not null Then
		v_tech_type_class:='MIKE';
	Else
		open c_equip_analog;
		fetch c_equip_analog into v_serial_no;
		close c_equip_analog;
		If 	v_serial_no  is not null Then
			v_tech_type_class:='ANALOG';
			Else
			raise PhoneNotFound;
		End If;
	End If;
End If;
po_serial_no:=v_serial_no;
GetEquipmentInfo(v_serial_no
,v_tech_type_class
,v_initial_activation
,po_tech_type
,po_product_cd
,po_product_status_cd
,po_vendor_name
,po_vendor_no
,po_equipment_status_type_id
,po_equipment_status_id
,po_stolen
,po_sublock1
,po_product_gp_type_id
,po_product_gp_type_cd
,po_product_gp_type_des
,po_product_type_id
,po_product_type_des
,po_product_class_id
,po_product_class_cd
,po_product_class_des
,po_provider_owner_id
,po_lastmuleimei_for_sim
,po_english_product_name
,po_french_product_name
,po_browser_version
,po_firmware_version
,po_prl_cd
,po_prl_des
,po_product_type_des_f
,po_product_gp_type_des_f
,po_min_cd
,po_customer_id
,po_product_type_list
,po_initial_activation
,po_mode_code
,po_mode_description
,po_product_type
,po_equipment_type
,po_equipment_type_class
,po_cross_fleet
,po_cost
,v_cap_code
,v_coverage_region_code_list
,v_encoding_format_code
,v_ownership_code
,v_prepaid
,v_frequency_cd
,po_firmware_feature_code_list
,po_browser_protocol
,po_new_param_3
,po_equip_status_dt
,po_puk
,po_product_category_id
);

Exception
When PhoneNotFound Then
	RAISE_APPLICATION_ERROR(-20317,'Client_Equipment pkg: Phone is not found as active in SEMS');
When Others Then
	raise;
-- 	RAISE_APPLICATION_ERROR(SQLCODE,SQLERRM);
End GetEquipmentInfoByPhoneNo;

------------------------------------------------------------------------------------
------------------------------------------------------------------------------------
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
 			    ,po_new_param_3			OUT     number
                            ,po_equip_status_dt                 OUT     date
                            ,po_puk           OUT     varchar2
                            ,po_product_category_id  OUT number
                  ) is

 	cursor c_equipment_PCS is
 	select	p.product_id
 		,p.product_cd
 		,nvl(p.product_status_cd,' ')  product_status_cd
 		,p.technology_type
		,p.english_product_name
		,p.french_product_name
 		,pe.subsidy_lock_number
 		,pe.provider_owner_id
 		,pe.master_lock_number
		,pe.browser_version
		,pe.firmware_version
		,pe.min_cd
		,pe.customer_id
 		,pes.equipment_status_type_id
 		,pes.equipment_status_id
        ,pes.equipment_status_dt
 		,pc.product_class_id
 		,pc.product_class_cd
 		,pc.product_class_des
		,prc.prl_cd
		,prc.prl_des
                ,bv.protocol
 	from	pcs_equipment pe
 		,product p
 		,pcs_equip_status pes
 		,product_classification pc
		,esn_prl ep
		,prl_code prc
                ,browser_version bv
 	where	pe.serial_no=pi_serial_no
 	and		pe.seq_no=0
 	and		pe.product_id=p.product_id
 	and 	pe.serial_no=pes.serial_no
 	and		pe.seq_no=pes.seq_no
 	and     trunc(pes.equipment_status_dt) <= trunc(sysdate)
 	and     pc.product_class_id = p.product_class_id
	and   	ep.serial_no(+)=pe.serial_no
	and     ep.seq_no(+)=pe.seq_no
	and 	(ep.status='A' or ep.status is null)
	and     (ep.effective_dt =
						(select max(effective_dt)
			 			from  esn_prl ep1
			 			where ep1.serial_no=ep.serial_no
			 			and   ep1.seq_no=ep.seq_no
			 			and   ep1.status='A')
			or ep.effective_dt is null)
	and     prc.prl_cd(+)=ep.prl_cd
        and     pe.browser_version = bv.browser_version (+)
 	order   by pes.equipment_status_dt desc;
pcs_rec	c_equipment_PCS%ROWTYPE;
v_product_status_cd	varchar2(2);
v_vendor_no 	        varchar2(18):='0';
v_activated_status	varchar2(1):='0';
v_master_lock           varchar2(10);
cursor c_pcs_equip_status is
     select     equipment_status_type_id
 		,equipment_status_id
     from       pcs_equip_status
     where      serial_no=pi_serial_no
     and        seq_no=0
     and        trunc(equipment_status_dt) <= trunc(sysdate)
     order     by equipment_status_dt desc;

v_initial_activation 	char(1):='Y';

cursor c_mode(pi_product_id number) is
	select ci_gp.sub_type_id product_type_id,
    ci_gp.catalogue_item_des product_type_des
from catalogue_item ci_p ,catalogue_item ci_g,ci_relationship cr, ci_relationship crg, catalogue_item ci_gp
where ci_p.sub_type_id= pi_product_id
and ci_p.catalogue_item_type_cd = 'PRODUCT'
and ci_g.sub_type_id= 10002686
and ci_g.catalogue_item_type_cd = 'GROUP'
and cr.catalogue_parent_item_id=ci_g.catalogue_item_id
and cr.catalogue_child_item_id=ci_gp.catalogue_item_id
AND cr.RELATIONSHIP_TYPE_CD = 'GENERICGRP'
and ci_gp.catalogue_item_type_cd = 'GROUP'
and crg.catalogue_parent_item_id=ci_gp.catalogue_item_id
and crg.catalogue_child_item_id=ci_p.catalogue_item_id
AND crg.RELATIONSHIP_TYPE_CD = 'GENERICGRP';

--
--v_mode			number(22);
--v_mode_description	varchar(100);

cursor c_equipment_MIKE is
 	select	 p.product_id
 		,p.product_cd
 		,nvl(p.product_status_cd,' ')  product_status_cd
 		,p.technology_type
		,p.english_product_name
		,p.french_product_name
 		,ie.provider_owner_id
		,ie.browser_version
		,ie.firmware_version
		,ie.min_cd
		,ie.customer_id
 		,ies.equipment_status_type_id
 		,ies.equipment_status_id
        ,ies.equipment_status_dt
 		,pc.product_class_id
 		,pc.product_class_cd
 		,pc.product_class_des
		,bv.protocol
 	from	iden_equipment ie
 		,product p
 		,iden_equip_status ies
 		,product_classification pc
 		,browser_version bv
 	where	ie.serial_no=pi_serial_no
 	and	ie.seq_no=0
 	and	ie.product_id=p.product_id
 	and 	ie.serial_no=ies.serial_no
 	and	ie.seq_no=ies.seq_no
 	and     trunc(ies.equipment_status_dt) <= trunc(sysdate)
 	and     pc.product_class_id = p.product_class_id
 	and     ie.browser_version = bv.browser_version (+)
 	order   by equipment_status_dt desc;
mike_rec	c_equipment_MIKE%ROWTYPE;
cursor c_iden_equip_status is
     select     equipment_status_type_id
 		,equipment_status_id
     from       iden_equip_status
     where      serial_no=pi_serial_no
     and        seq_no=0
     and        trunc(equipment_status_dt) <= trunc(sysdate)
     order     by equipment_status_dt desc;
     
 ----------------------------
 --UIM
 ----------------------------
 cursor c_equipment_UIM is
 	select	p.product_id
 		,p.product_cd
 		,nvl(p.product_status_cd,' ')  product_status_cd
 		,p.technology_type
 		,p.english_product_name
		,p.french_product_name
 		,ss.equipment_status_type_id
 		,ss.equipment_status_id
        	,ss.equipment_status_dt
        	,s.puk1
 		,pc.product_class_id
 		,pc.product_class_cd
 		,pc.product_class_des
 	from	uim s
 		,product p
 		,uim_status ss
 		,product_classification pc
 	where	s.iccid=pi_serial_no
 	and	s.seq_no=0
 	and	p.product_id=s.product_id
 	and 	ss.iccid=s.iccid
 	and	ss.seq_no=s.seq_no
 	and     trunc(ss.equipment_status_dt) <= trunc(sysdate)
 	and     pc.product_class_id = p.product_class_id
 	order   by ss.equipment_status_dt desc;
uim_rec	c_equipment_UIM%ROWTYPE;
cursor c_uim_equip_status is
     select     equipment_status_type_id
 		,equipment_status_id
        , equipment_status_dt
     from       uim_status
     where      iccid=pi_serial_no
      and        seq_no=0
     and        equipment_status_dt <= trunc(sysdate)
     order      by equipment_status_dt desc;
 -----------------------------
 --SIM
 -----------------------------
cursor c_equipment_SIM is
 	select	p.product_id
 		,p.product_cd
 		,nvl(p.product_status_cd,' ')  product_status_cd
 		,p.technology_type
 		,p.english_product_name
		,p.french_product_name
 		,ss.equipment_status_type_id
 		,ss.equipment_status_id
        	,ss.equipment_status_dt
        	,s.puk
 		,pc.product_class_id
 		,pc.product_class_cd
 		,pc.product_class_des
 	from	sim s
 		,product p
 		,sim_status ss
 		,product_classification pc
 	where	s.sim_id=pi_serial_no
 	and	s.seq_no=0
 	and	p.product_id=s.product_id
 	and 	ss.sim_id=s.sim_id
 	and	ss.seq_no=s.seq_no
 	and     trunc(ss.equipment_status_dt) <= trunc(sysdate)
 	and     pc.product_class_id = p.product_class_id
 	order   by ss.equipment_status_dt desc;
sim_rec	c_equipment_SIM%ROWTYPE;
cursor c_sim_equip_status is
     select     equipment_status_type_id
 		,equipment_status_id
        , equipment_status_dt
     from       sim_status
     where      sim_id=pi_serial_no
      and        seq_no=0
     and        equipment_status_dt <= trunc(sysdate)
     order      by equipment_status_dt desc;
     
 ----------------
 --Analog equip
 ----------------
cursor c_equip_analog is
 	select	p.product_id
 		,p.product_cd
 		,nvl(p.product_status_cd,' ')  product_status_cd
 		,p.technology_type
		,p.english_product_name
		,p.french_product_name
		,ae.min_cd
		,ae.customer_id
 		,ae.provider_owner_id
 		,aes.equipment_status_type_id
 		,aes.equipment_status_id
        ,aes.equipment_status_dt
 		,pc.product_class_id
 		,pc.product_class_cd
 		,pc.product_class_des
 	from	analog_equip ae
 		,product p
 		,analog_equip_status aes
 		,product_classification pc
 	where	ae.serial_no=pi_serial_no
 	and	ae.seq_no=0
 	and	ae.product_id=p.product_id
 	and 	ae.serial_no=aes.serial_no(+)
 	and	ae.seq_no=aes.seq_no(+)
 	and     aes.equipment_status_dt(+) <= sysdate
 	and     pc.product_class_id = p.product_class_id
 	order   by aes.equipment_status_dt desc;
analog_rec	c_equip_analog%ROWTYPE;
cursor c_analog_equip_status is
     select     equipment_status_type_id
 		,equipment_status_id
     from       analog_equip_status
     where      serial_no=pi_serial_no
      and        seq_no=0
     and        trunc(equipment_status_dt) <= trunc(sysdate)
     order     by  equipment_status_dt desc;
---------------
-- Pager for K2
---------------
cursor c_equip_Pager is
 	select	p.product_id
 		,p.product_cd
 		,nvl(p.product_status_cd,' ')  product_status_cd
 		,p.technology_type
		,p.english_product_name
		,p.french_product_name
		,pe.min_cd
		,pe.customer_id
 		,pe.provider_owner_id
 		,pes.equipment_status_type_id
 		,pes.equipment_status_id
        ,pes.equipment_status_dt
 		,pc.product_class_id
 		,pc.product_class_cd
 		,pc.product_class_des
 		,pe.cap_code
		,pp.encoder_format_cd
                ,pp.paging_product_id
		,pe.frequency_id
		,pf.frequency_cd
 	from	paging_equip pe
 		,product p
 		,paging_equip_status pes
 		,product_classification pc
		,paging_product pp
		,paging_frequency pf
 	where	pe.serial_no=pi_serial_no
 	and	pe.seq_no=0
 	and	pe.product_id=p.product_id
 	and 	pe.serial_no=pes.serial_no(+)
 	and	pe.seq_no=pes.seq_no(+)
 	and     pes.equipment_status_dt(+) <= sysdate
 	and     pc.product_class_id = p.product_class_id
	and     pp.paging_product_id = pe.paging_product_id
	and     pe.frequency_id = pf.frequency_id
 	order   by pes.equipment_status_dt desc;
--
pager_rec	c_equip_Pager%ROWTYPE;
v_paging_product_id    number(22);
--
cursor c_paging_equip_status is
	select  equipment_status_type_id
		,equipment_status_id
	from    paging_equip_status
	where   serial_no=pi_serial_no
	and     seq_no=0
	and     trunc(equipment_status_dt) <= trunc(sysdate)
	order   by  equipment_status_dt desc;
--
cursor c_paging_ownership is
	select  DECODE(equipment_status_id, 140,'R','P')
	from    paging_equip_status
	where   serial_no=pi_serial_no
	and     seq_no=0
	and     trunc(equipment_status_dt) <= trunc(sysdate)
	and     equipment_status_type_id = 12
	order   by  equipment_status_dt desc;
--
cursor c_paging_prepaid (pi_product_id number) is
	select  'Y'
	from    product_feature
	where   product_id = pi_product_id
	and     feature_id = 10002392
	;
--
cursor c_paging_coverage_region_list (pi_frequency_id number) is
	select  coverage_region_id
	from    coverage_region
	where   frequency_id = pi_frequency_id
	and     province_cd in ('BC','AB')
	and     coverage_type is not null
	order   by  coverage_region_id desc;
--
v_coverage_region_id	coverage_region.coverage_region_id%TYPE;
--
v_product_rec		Product_Info_Record;

--Cursor to select Product Service Initiatives for given product
cursor c_promo(pi_product_id number) is
	select ci_gp.sub_type_id product_type_id
from catalogue_item ci_p ,catalogue_item ci_g,ci_relationship cr, ci_relationship crg, catalogue_item ci_gp
where ci_p.sub_type_id= pi_product_id  ---example 10001165
and ci_p.catalogue_item_type_cd = 'PRODUCT'
and ci_g.sub_type_id= 10005555
and ci_g.catalogue_item_type_cd = 'GROUP'
and cr.catalogue_parent_item_id=ci_g.catalogue_item_id
and cr.catalogue_child_item_id=ci_gp.catalogue_item_id
AND cr.RELATIONSHIP_TYPE_CD = 'GENERICGRP'
and ci_gp.catalogue_item_type_cd = 'GROUP'
and crg.catalogue_parent_item_id=ci_gp.catalogue_item_id
and crg.catalogue_child_item_id=ci_p.catalogue_item_id
AND crg.RELATIONSHIP_TYPE_CD = 'GENERICGRP';

v_product_promo_type_id	 number(22);

v_product_id    number(22);

Begin
If pi_initial_activation Then
v_initial_activation:= 'Y';
else
v_initial_activation:= 'N';
End If;
If pi_tech_type_class='PCS' Then
	open c_equipment_PCS;
	fetch c_equipment_PCS into pcs_rec;
	If c_equipment_PCS%notFound Then
		raise ESNNotFound;
	End If;
	        v_product_id := pcs_rec.product_id;
		po_product_cd:= pcs_rec.product_cd;
		po_product_status_cd := pcs_rec.product_status_cd;
		v_product_status_cd := pcs_rec.product_status_cd;
		po_tech_type:= pcs_rec.technology_type;
		po_provider_owner_id:= pcs_rec.provider_owner_id;
		po_equipment_status_type_id:= pcs_rec.equipment_status_type_id;
		po_equipment_status_id:= pcs_rec.equipment_status_id;
        po_equip_status_dt:=pcs_rec.equipment_status_dt;
		po_sublock1 := pcs_rec.subsidy_lock_number;
		v_master_lock := pcs_rec.master_lock_number;
		po_product_class_id:= pcs_rec.product_class_id;
		po_product_class_cd:=pcs_rec.product_class_cd;
		po_product_class_des:=pcs_rec.product_class_des;
		po_english_product_name:=pcs_rec.english_product_name;
		po_french_product_name:=pcs_rec.french_product_name;
		po_browser_version:=pcs_rec.browser_version;
	        po_firmware_version:=pcs_rec.firmware_version;
		po_prl_cd:=pcs_rec.prl_cd;
		po_prl_des:=pcs_rec.prl_des;
		po_min_cd:= pcs_rec.min_cd;
		po_customer_id:=pcs_rec.customer_id;
		po_browser_protocol:=pcs_rec.protocol;
		po_stolen:=0;
	If (pcs_rec.equipment_status_type_id=1 and pcs_rec.equipment_status_id=6) or
	    (pcs_rec.equipment_status_type_id=3 and pcs_rec.equipment_status_id=11)or
	    (pcs_rec.equipment_status_type_id=3 and pcs_rec.equipment_status_id=56) or
	    (pcs_rec.equipment_status_type_id=3 and pcs_rec.equipment_status_id=57)  Then
		po_stolen:=1;
	Else
	  for equip_rec in c_pcs_equip_status loop
		exit when  c_equipment_PCS%NotFound;
		If (equip_rec.equipment_status_type_id=1 or equip_rec.equipment_status_type_id=3) Then
	     If (equip_rec.equipment_status_type_id=1 and equip_rec.equipment_status_id=6) or
	   	  (equip_rec.equipment_status_type_id=3 and equip_rec.equipment_status_id=11)or
	   	  (equip_rec.equipment_status_type_id=3 and equip_rec.equipment_status_id=56) or
	          (equip_rec.equipment_status_type_id=3 and equip_rec.equipment_status_id=57)  Then
		   po_stolen:=1;
		  End If; 
		  exit;
		End If;
		end loop;	
	End If;
	If (pcs_rec.equipment_status_type_id=2 and pcs_rec.equipment_status_id=8)   Then
		v_activated_status :='A';
		v_initial_activation:='N';
	End If;
	If v_initial_activation='Y' Then
		for equip_rec in c_pcs_equip_status loop
		exit when  c_equipment_PCS%NotFound;
		If (equip_rec.equipment_status_type_id=2 and equip_rec.equipment_status_id=8) Then
			v_initial_activation:='N';
			exit;
		Elsif (equip_rec.equipment_status_type_id=1 and equip_rec.equipment_status_id=3) Then
			v_initial_activation:='Y';
			exit;
		End If;
		end loop;

	      po_initial_activation:= v_initial_activation;
	End If;

      -- Get Promo Product Types
      open c_promo(pcs_rec.product_id);
      loop
      fetch c_promo into v_product_promo_type_id;
      exit when c_promo%NotFound;
      po_product_type_list:= po_product_type_list||v_product_promo_type_id;
      end loop;
      close c_promo;
      open c_mode(pcs_rec.product_id);
      fetch c_mode into  po_mode_code
      			,po_mode_description;
      close c_mode;
      --
      GetFirmwareVersionFeature (pcs_rec.firmware_version, po_firmware_feature_code_list);
      --
Elsif pi_tech_type_class='MIKE' Then
	open c_equipment_MIKE;
	fetch c_equipment_MIKE into mike_rec;
	If c_equipment_MIKE%notFound Then
		raise ESNNotFound;
	End If;
	        v_product_id := mike_rec.product_id;
		po_product_cd:= mike_rec.product_cd;
		po_product_status_cd := mike_rec.product_status_cd;
		po_tech_type:= mike_rec.technology_type;
		po_provider_owner_id:= mike_rec.provider_owner_id;
		po_equipment_status_type_id:= mike_rec.equipment_status_type_id;
		po_equipment_status_id:= mike_rec.equipment_status_id;
        po_equip_status_dt:= mike_rec.equipment_status_dt;
		po_product_class_id:=mike_rec.product_class_id;
		po_product_class_cd:=mike_rec.product_class_cd;
		po_product_class_des:=mike_rec.product_class_des;
		po_english_product_name:=mike_rec.english_product_name;
		po_french_product_name:=mike_rec.french_product_name;
		po_browser_version:=mike_rec.browser_version;
	        po_firmware_version:=mike_rec.firmware_version;
	        po_min_cd:= mike_rec.min_cd;
		po_customer_id:=mike_rec.customer_id;
		po_browser_protocol:=mike_rec.protocol;
		po_stolen:=0;
	If mike_rec.equipment_status_type_id=1 and mike_rec.equipment_status_id=6 or
	    (mike_rec.equipment_status_type_id=3 and mike_rec.equipment_status_id=11)or
	    (mike_rec.equipment_status_type_id=3 and mike_rec.equipment_status_id=56) or
	    (mike_rec.equipment_status_type_id=3 and mike_rec.equipment_status_id=57) Then
		po_stolen:=1;
	Else
		for equip_rec in c_iden_equip_status  loop
		exit when  c_iden_equip_status%NotFound;
		
		If (equip_rec.equipment_status_type_id=1 or equip_rec.equipment_status_type_id=3) Then
		 If (equip_rec.equipment_status_type_id=1 and equip_rec.equipment_status_id=6) or
	   	  (equip_rec.equipment_status_type_id=3 and equip_rec.equipment_status_id=11)or
	   	  (equip_rec.equipment_status_type_id=3 and equip_rec.equipment_status_id=56) or
	          (equip_rec.equipment_status_type_id=3 and equip_rec.equipment_status_id=57)  Then
		   po_stolen:=1;
		  End If; 
		  exit;
		End If;
		end loop;
	End If;
	If (mike_rec.equipment_status_type_id=2 and mike_rec.equipment_status_id=8)   Then
		po_initial_activation:='N';
	End If;
	If po_initial_activation='Y' Then
		for equip_rec in c_iden_equip_status loop
		 exit when c_iden_equip_status%NotFound;
		If (equip_rec.equipment_status_type_id=2 and equip_rec.equipment_status_id=8) Then
			v_initial_activation:='N';
			exit;
		Elsif (equip_rec.equipment_status_type_id=1 and equip_rec.equipment_status_id=3) Then
			v_initial_activation:='Y';
			exit;
		End If;
		end loop;
		If v_initial_activation='O' Then
		po_initial_activation:='Y';
		else
		po_initial_activation:= v_initial_activation;
		End If;
	End If;
        --
	-- Get Promo Product Types
        --
	open c_promo(pcs_rec.product_id);
	loop
      		fetch c_promo into v_product_promo_type_id;
     		exit when c_promo%NotFound;
      		po_product_type_list:= po_product_type_list||v_product_promo_type_id;
      	end loop;
      	close c_promo;
        --
        GetFirmwareVersionFeature (mike_rec.firmware_version, po_firmware_feature_code_list);
        --
Elsif pi_tech_type_class='ANALOG' Then
	open c_equip_analog;
	fetch c_equip_analog into analog_rec;
	If c_equip_analog%notFound Then
		raise ESNNotFound;
	End If;
	        v_product_id := analog_rec.product_id;
		po_product_cd:= analog_rec.product_cd;
		po_product_status_cd := analog_rec.product_status_cd;
		po_tech_type:= analog_rec.technology_type;
		po_provider_owner_id:= analog_rec.provider_owner_id;
		po_equipment_status_type_id:=analog_rec.equipment_status_type_id;
		po_equipment_status_id:=analog_rec.equipment_status_id;
        po_equip_status_dt:=analog_rec.equipment_status_dt;
		po_sublock1 :=' ';
		po_product_class_id:=analog_rec.product_class_id;
		po_product_class_cd:=analog_rec.product_class_cd;
		po_product_class_des:=analog_rec.product_class_des;
		po_english_product_name:=analog_rec.english_product_name;
		po_french_product_name:=analog_rec.french_product_name;
		po_min_cd:= analog_rec.min_cd;
		po_customer_id:=analog_rec.customer_id;
		po_stolen:=0;
		
	If  analog_rec.equipment_status_type_id=1 and analog_rec.equipment_status_id=6 or
	    (analog_rec.equipment_status_type_id=3 and analog_rec.equipment_status_id=11)or
	    (analog_rec.equipment_status_type_id=3 and analog_rec.equipment_status_id=56) or
	    (analog_rec.equipment_status_type_id=3 and analog_rec.equipment_status_id=57)Then
		po_stolen:=1;
	Else
	  for equip_rec in c_analog_equip_status loop
		exit when  c_analog_equip_status%NotFound;
		If (equip_rec.equipment_status_type_id=1 or equip_rec.equipment_status_type_id=3) Then
		 If (equip_rec.equipment_status_type_id=1 and equip_rec.equipment_status_id=6) or
	   	  (equip_rec.equipment_status_type_id=3 and equip_rec.equipment_status_id=11)or
	   	  (equip_rec.equipment_status_type_id=3 and equip_rec.equipment_status_id=56) or
	          (equip_rec.equipment_status_type_id=3 and equip_rec.equipment_status_id=57)  Then
		   po_stolen:=1;
		  End If;  
		  exit;
		End If;
	   end loop;	
	If (analog_rec.equipment_status_type_id=2 and analog_rec.equipment_status_id=8)   Then
		po_initial_activation:='N';
	End If;
	If po_initial_activation='Y' Then
		for equip_rec in c_analog_equip_status loop
		 exit when c_analog_equip_status%NotFound;
		If (equip_rec.equipment_status_type_id=2 and equip_rec.equipment_status_id=8) Then
			v_initial_activation:='N';
			exit;
		Elsif (equip_rec.equipment_status_type_id=1 and equip_rec.equipment_status_id=3) Then
			v_initial_activation:='Y';
			exit;
		End If;
		end loop;
		If v_initial_activation='O' Then
		po_initial_activation:='Y';
		else
		po_initial_activation:= v_initial_activation;
		End If;
	End If;
	End If;
----------------
-- Pager for K2
----------------
Elsif pi_tech_type_class='PAGER' Then
	Open c_equip_Pager;
	Fetch c_equip_Pager Into pager_rec;
	If c_equip_Pager%NotFound Then
		Raise ESNNotFound;
	End If;
	        v_product_id := pager_rec.product_id;
	        v_paging_product_id := pager_rec.paging_product_id;
		po_product_cd:= pager_rec.product_cd;
		po_product_status_cd := pager_rec.product_status_cd;
		po_tech_type:=pager_rec.technology_type;
		po_provider_owner_id:= pager_rec.provider_owner_id;
		po_equipment_status_type_id:=pager_rec.equipment_status_type_id;
		po_equipment_status_id:=pager_rec.equipment_status_id;
        po_equip_status_dt:=pager_rec.equipment_status_dt;
		po_sublock1 :=' ';
		po_lastmuleimei_for_sim :=' ';
		po_browser_version:=' ';
	        po_firmware_version:=' ';
	        po_browser_protocol:=' ';
		po_prl_cd:=' ';
		po_prl_des:=' ';
		po_product_class_id:=pager_rec.product_class_id;
		po_product_class_cd:=pager_rec.product_class_cd;
		po_product_class_des:=pager_rec.product_class_des;
		po_english_product_name:=pager_rec.english_product_name;
		po_french_product_name:=pager_rec.french_product_name;
		po_min_cd:= pager_rec.min_cd;
		po_customer_id:=pager_rec.customer_id;
		po_cap_code:=pager_rec.cap_code;
		po_encoding_format_code:=pager_rec.encoder_format_cd;
		po_frequency_cd:=pager_rec.frequency_cd;
                po_stolen:=0; 
	If   pager_rec.equipment_status_type_id=1 And Pager_rec.equipment_status_id=6 Or
	    (pager_rec.equipment_status_type_id=11 and pager_rec.equipment_status_id=127)Or
	    (pager_rec.equipment_status_type_id=3 and pager_rec.equipment_status_id=11)Or
	    (pager_rec.equipment_status_type_id=3 and pager_rec.equipment_status_id=56)Or
	    (pager_rec.equipment_status_type_id=3 and pager_rec.equipment_status_id=57)Then
		po_stolen:=1;
	Else
	 for equip_rec in c_paging_equip_status loop
		exit when  c_paging_equip_status%NotFound;
		If (equip_rec.equipment_status_type_id=1 or equip_rec.equipment_status_type_id=3 or
		    equip_rec.equipment_status_type_id=11) Then
	     If (equip_rec.equipment_status_type_id=1 and equip_rec.equipment_status_id=6) or
		  (equip_rec.equipment_status_type_id=11 and equip_rec.equipment_status_id=127) or
	   	  (equip_rec.equipment_status_type_id=3 and equip_rec.equipment_status_id=11)or
	   	  (equip_rec.equipment_status_type_id=3 and equip_rec.equipment_status_id=56) or
	          (equip_rec.equipment_status_type_id=3 and equip_rec.equipment_status_id=57)  Then
		   po_stolen:=1;
		  End If; 
		  exit;
		End If;
	  end loop;	
	End If;
	If (pager_rec.equipment_status_type_id=2 and pager_rec.equipment_status_id=8) Then
		po_initial_activation:='N';
	End If;
	If v_initial_activation='Y' Then
		For equip_rec In c_paging_equip_status Loop
			Exit When c_paging_equip_status%NotFound;
			If (equip_rec.equipment_status_type_id=2 and equip_rec.equipment_status_id=8) Then
				v_initial_activation:='N';
				exit;
			Elsif (equip_rec.equipment_status_type_id=1 and equip_rec.equipment_status_id=3) Then
				v_initial_activation:='Y';
				exit;
			End If;
		End Loop;
		If v_initial_activation='O' Then
			po_initial_activation:='Y';
		Else
			po_initial_activation:= v_initial_activation;
		End If;
	End If;
	-- get ownership (Rental R or Private P)
	open  c_paging_ownership;
	fetch c_paging_ownership into po_ownership_code;
	If c_paging_ownership%NotFound Then po_ownership_code := 'P'; End If;
	close c_paging_ownership;
	-- check prepaid indicator
	open  c_paging_prepaid (pager_rec.product_id);
	fetch c_paging_prepaid into po_prepaid;
	If c_paging_prepaid%NotFound Then po_prepaid := 'N'; End If;
	close c_paging_prepaid;
	-- get Coverage Region List
	v_coverage_region_id := '';
	open  c_paging_coverage_region_list (pager_rec.frequency_id);
	loop
		fetch c_paging_coverage_region_list into v_coverage_region_id;
		exit when c_paging_coverage_region_list%NotFound;
		po_coverage_region_code_list := po_coverage_region_code_list||v_coverage_region_id||'|';
	end loop;
	po_coverage_region_code_list := Substr(po_coverage_region_code_list,1,Instr(po_coverage_region_code_list,'|',-1)-1);
	close c_paging_coverage_region_list;
--
Elsif pi_tech_type_class='UIM' Then
	open c_equipment_UIM;
	fetch c_equipment_UIM into uim_rec;
	If c_equipment_UIM%notFound Then
		raise ESNNotFound;
	End If;
	                v_product_id := uim_rec.product_id;
			po_product_cd:= uim_rec.product_cd;
			po_product_status_cd := uim_rec.product_status_cd;
			po_tech_type:= uim_rec.technology_type;
			po_equipment_status_type_id:= uim_rec.equipment_status_type_id;
			po_equipment_status_id:= uim_rec.equipment_status_id;
            		po_equip_status_dt:= uim_rec.equipment_status_dt;
            		po_puk:= uim_rec.puk1;
			po_product_class_id:=uim_rec.product_class_id;
			po_product_class_cd:=uim_rec.product_class_cd;
			po_product_class_des:=uim_rec.product_class_des;
			po_english_product_name:=uim_rec.english_product_name;
			po_french_product_name:=uim_rec.french_product_name;
			po_stolen:=0;
			If uim_rec.equipment_status_type_id=1 and uim_rec.equipment_status_id=6 or
	    		(uim_rec.equipment_status_type_id=3 and uim_rec.equipment_status_id=11) or
	    		(uim_rec.equipment_status_type_id=3 and uim_rec.equipment_status_id=56) or
	    		(uim_rec.equipment_status_type_id=3 and uim_rec.equipment_status_id=57)Then
				po_stolen:=1;
			Else
			  for equip_rec in c_uim_equip_status loop
				exit when  c_uim_equip_status%NotFound;
				If (equip_rec.equipment_status_type_id=1 or equip_rec.equipment_status_type_id=3) Then
				 If (equip_rec.equipment_status_type_id=1 and equip_rec.equipment_status_id=6) or
	   	 		 (equip_rec.equipment_status_type_id=3 and equip_rec.equipment_status_id=11)or
	   	  		 (equip_rec.equipment_status_type_id=3 and equip_rec.equipment_status_id=56) or
	          		(equip_rec.equipment_status_type_id=3 and equip_rec.equipment_status_id=57)  Then
		  		  po_stolen:=1;
		  		 End If;
		   		 exit;
				End If;
			end loop;
			End If;
			If (uim_rec.equipment_status_type_id=2 and uim_rec.equipment_status_id=8)   Then
			po_initial_activation:='N';
			End If;
			If po_initial_activation='Y' Then
			for equip_rec in c_uim_equip_status loop
		 		exit when c_uim_equip_status%NotFound;
				If (equip_rec.equipment_status_type_id=2 and equip_rec.equipment_status_id=8) Then
					v_initial_activation:='N';
					exit;
				Elsif (equip_rec.equipment_status_type_id=1 and equip_rec.equipment_status_id=3) Then
				v_initial_activation:='Y';
				exit;
				End If;
			end loop;
		If v_initial_activation='O' Then
		po_initial_activation:='Y';
		else
		po_initial_activation:= v_initial_activation;
		End If;
	End If;
--
Elsif pi_tech_type_class='SIM' Then
	open c_equipment_SIM;
	fetch c_equipment_SIM into sim_rec;
	If c_equipment_SIM%notFound Then
		raise ESNNotFound;
	End If;
	                v_product_id := sim_rec.product_id;
			po_product_cd:= sim_rec.product_cd;
			po_product_status_cd := sim_rec.product_status_cd;
			po_tech_type:= sim_rec.technology_type;
			po_equipment_status_type_id:= sim_rec.equipment_status_type_id;
			po_equipment_status_id:= sim_rec.equipment_status_id;
            po_equip_status_dt:= sim_rec.equipment_status_dt;
            po_puk:= sim_rec.puk;
			po_product_class_id:=sim_rec.product_class_id;
			po_product_class_cd:=sim_rec.product_class_cd;
			po_product_class_des:=sim_rec.product_class_des;
			po_english_product_name:=sim_rec.english_product_name;
			po_french_product_name:=sim_rec.french_product_name;
			po_stolen:=0;
			If sim_rec.equipment_status_type_id=1 and sim_rec.equipment_status_id=6 or
	    		(sim_rec.equipment_status_type_id=3 and sim_rec.equipment_status_id=11) or
	    		(sim_rec.equipment_status_type_id=3 and sim_rec.equipment_status_id=56) or
	    		(sim_rec.equipment_status_type_id=3 and sim_rec.equipment_status_id=57)Then
				po_stolen:=1;
			Else
			  for equip_rec in c_sim_equip_status loop
				exit when  c_sim_equip_status%NotFound;
				If (equip_rec.equipment_status_type_id=1 or equip_rec.equipment_status_type_id=3) Then
				 If (equip_rec.equipment_status_type_id=1 and equip_rec.equipment_status_id=6) or
	   	 		 (equip_rec.equipment_status_type_id=3 and equip_rec.equipment_status_id=11)or
	   	  		 (equip_rec.equipment_status_type_id=3 and equip_rec.equipment_status_id=56) or
	          		(equip_rec.equipment_status_type_id=3 and equip_rec.equipment_status_id=57)  Then
		  		 po_stolen:=1;
		  		 End If;
		   		 exit;
				End If;
			end loop;
			End If;
			If (sim_rec.equipment_status_type_id=2 and sim_rec.equipment_status_id=8)   Then
			po_initial_activation:='N';
			End If;
			If po_initial_activation='Y' Then
			for equip_rec in c_sim_equip_status loop
		 		exit when c_sim_equip_status%NotFound;
				If (equip_rec.equipment_status_type_id=2 and equip_rec.equipment_status_id=8) Then
					v_initial_activation:='N';
					exit;
				Elsif (equip_rec.equipment_status_type_id=1 and equip_rec.equipment_status_id=3) Then
				v_initial_activation:='Y';
				exit;
				End If;
			end loop;
		If v_initial_activation='O' Then
		po_initial_activation:='Y';
		else
		po_initial_activation:= v_initial_activation;
		End If;
	End If;
			Begin
			GetIMEIBySIM(pi_serial_no, po_lastmuleimei_for_sim);
			Exception
				When IMEINotFound Then
					po_lastmuleimei_for_sim:=null;
				When Others Then
			--	po_lastmuleimei_for_sim:=null;
				raise;
 			--	RAISE_APPLICATION_ERROR(SQLCODE,SQLERRM);
			End;
End If;
--- call GetProductInfo
         IF pi_tech_type_class='PAGER' THEN
		GetProductInfo(v_paging_product_id,pi_tech_type_class,v_product_rec);
         ELSE
		GetProductInfo(v_product_id,v_product_rec);
         END IF;
		po_product_gp_type_id:=v_product_rec.product_gp_type_id;
		po_product_gp_type_cd:=v_product_rec.product_gp_type_cd;
		po_product_gp_type_des:=v_product_rec.product_gp_type_des;
		po_product_gp_type_des_f:=v_product_rec.product_gp_type_des_f;
		po_product_type_id:= v_product_rec.product_type_id;
		po_product_type_des:=v_product_rec.product_type_des;
		po_product_type_des_f:=v_product_rec.product_type_des_f;
		po_vendor_no :=v_product_rec.vendor_no;
		v_vendor_no:= v_product_rec.vendor_no;
		po_vendor_name := v_product_rec.vendor_name;
		po_product_type := v_product_rec.product_type_kb;
		po_equipment_type := v_product_rec.equipment_type_kb;
		po_equipment_type_class := v_product_rec.equipment_type_class;
		po_cross_fleet := v_product_rec.cross_fleet;
        po_product_category_id := v_product_rec.product_category_id;
        

-- Replace PCS sublock code with master code if equipment refurbished or previously activated
-- and not Sanyo
If (v_product_status_cd ='R' or v_initial_activation='N') and v_vendor_no!='10004245' Then
	po_sublock1:= v_master_lock;
End If;
Exception
When ESNNotFound Then
  If pi_tech_type_class='PCS' and c_equipment_PCS%isOpen Then
  	close c_equipment_PCS;
  Elsif pi_tech_type_class='MIKE' and c_equipment_MIKE%isOpen Then
  	close c_equipment_MIKE;
  Elsif pi_tech_type_class='ANALOG' and c_equip_analog%isOpen Then
  	close c_equip_analog;
   Elsif pi_tech_type_class='SIM' and c_equipment_SIM%isOpen Then
  	close c_equipment_SIM;
  Elsif pi_tech_type_class='PAGER' and c_equip_Pager%isOpen Then
  	close c_equip_Pager;
  End If;
  RAISE_APPLICATION_ERROR(-20310,'Client_Equipment pkg: ESN or Product ID not found');
When Others Then
  If pi_tech_type_class='PCS' and c_equipment_PCS%isOpen Then
  	close c_equipment_PCS;
  Elsif pi_tech_type_class='MIKE' and c_equipment_MIKE%isOpen Then
  	close c_equipment_MIKE;
  Elsif pi_tech_type_class='ANALOG' and c_equip_analog%isOpen Then
  	close c_equip_analog;
   Elsif pi_tech_type_class='SIM' and c_equipment_SIM%isOpen Then
  	close c_equipment_SIM;
  Elsif pi_tech_type_class='PAGER' and c_equip_Pager%isOpen Then
  	close c_equip_Pager;
  End If;
  raise;
 --RAISE_APPLICATION_ERROR(SQLCODE,SQLERRM);
End GetEquipmentInfo;
-------------------------------------------------------------------------------------------




	--------------------------------------------------------------------------------------------------------------------------
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
		            ,po_new_param_3	  		OUT     number
                            ,po_equip_status_dt                 OUT     date
                            ,po_puk           OUT     varchar2
                            ,po_product_category_id OUT     number
                    ) is

v_imei 		iden_equipment.SERIAL_NO%Type;
Begin
GetIMEIBySIM(pi_sim, v_imei) ;
If v_imei is not NULL Then
po_imei := v_imei;
	 GetEquipmentInfo(v_imei
		 	 ,'MIKE'
		  	 ,false
			 ,po_tech_type
			 ,po_product_cd
			 ,po_product_status_cd
			 ,po_vendor_name
			 ,po_vendor_no
			 ,po_equipment_status_type_id
			 ,po_equipment_status_id
			 ,po_stolen
			 ,po_sublock1
			 ,po_product_gp_type_id
			 ,po_product_gp_type_cd
			 ,po_product_gp_type_des
			 ,po_product_type_id
			 ,po_product_type_des
			 ,po_product_class_id
			 ,po_product_class_cd
			 ,po_product_class_des
			 ,po_provider_owner_id
			 ,po_lastmuleimei_for_sim
			 ,po_english_product_name
		     	 ,po_french_product_name
		     	 ,po_browser_version
			 ,po_firmware_version
		     	 ,po_prl_cd
		     	 ,po_prl_des
			 ,po_product_type_des_f
			  ,po_product_gp_type_des_f
			  ,po_min_cd
			  ,po_customer_id
			  ,po_product_type_list
			  ,po_initial_activation
			  ,po_mode_code
			  ,po_mode_description
			  ,po_product_type
		          ,po_equipment_type
		          ,po_equipment_type_class
		          ,po_cross_fleet
		          ,po_cost
		          ,po_cap_code
			  ,po_coverage_region_code_list
		          ,po_encoding_format_code
		          ,po_ownership_code
		          ,po_prepaid
		          ,po_frequency_cd
			  ,po_firmware_feature_code_list
			  ,po_browser_protocol
		          ,po_new_param_3
                          ,po_equip_status_dt
                          ,po_puk
                          ,po_product_category_id
                  );

End If;
Exception
When Others Then
raise;
End;


-------------------------------------------------------------------------------------------
-------------------------------------------------------------------------------------------
Procedure GetProductInfo (pi_product_id					IN	number
			  ,ro_product_info_rec			OUT	Product_Info_Record )
			     is
pi_tech_type_class     varchar2(10) := 'NOT PAGER';

BEGIN
    GetProductInfo (pi_product_id, pi_tech_type_class, ro_product_info_rec);
Exception
When Others Then
  raise;
 --RAISE_APPLICATION_ERROR(SQLCODE,SQLERRM);
End GetProductInfo;
----------------------------------------------------------------------------
Procedure GetProductInfo (pi_product_id                         IN      number
                         ,pi_tech_type_class                    IN      VARCHAR2
			 ,ro_product_info_rec			OUT	Product_Info_Record )
          is
cursor c_product_gr_type(p_product_id number) is
	select ci_g.sub_type_id product_gp_type_id
	 , cg.ci_group_cd  product_gp_type_cd
       	, ci_g.catalogue_item_des product_gp_type_des
       , '' product_gp_type_des_f  --Not existing feald
	,ci_gp.sub_type_id  product_type_id
    	,ci_gp.catalogue_item_des product_type_des
        , '' product_type_des_f     --Not existing feald
from catalogue_item ci_p ,catalogue_item ci_g, ci_group cg, ci_relationship cr, ci_relationship crg, catalogue_item ci_gp
where ci_p.sub_type_id= pi_product_id
and ci_p.catalogue_item_type_cd = 'PRODUCT'
and cg.ci_group_type_cd='PROD_EXCL_GRP'
and ci_g.sub_type_id= cg.ci_group_id
and ci_g.catalogue_item_type_cd = 'GROUP'
and cr.catalogue_parent_item_id=ci_g.catalogue_item_id
and cr.catalogue_child_item_id=ci_gp.catalogue_item_id
AND cr.RELATIONSHIP_TYPE_CD = 'GENERICGRP' -- to do change to  SKU_COMP
and ci_gp.catalogue_item_type_cd = 'GROUP'
and crg.catalogue_parent_item_id=ci_gp.catalogue_item_id
and crg.catalogue_child_item_id=ci_p.catalogue_item_id
AND crg.RELATIONSHIP_TYPE_CD = 'SKU_COMP';


pr_type_rec	c_product_gr_type%ROWTYPE;

cursor c_equip(p_product_id number) is
	select	pcx.product_type_knowbility
		,pcx.equip_type_knowbility
		,et.equip_type_class, p.product_category_id
	from 	 product p
 		,product_class_xref_kb pcx
 		,kb_equip_type et
 	where 	p.product_id = p_product_id
 	and     pcx.technology_type=p.technology_type
 	and     pcx.product_class_id=p.product_class_id
 	and     et.equip_type_knowbility(+) = pcx.equip_type_knowbility;
equip_type_rec	c_equip%ROWTYPE;

cursor c_pager_equip(p_paging_product_id number) is
        select  pcx.product_type_knowbility
               ,pmt.paging_model_type_cd
               ,pmt.description
        from
                paging_model_type pmt
               ,product_class_xref_kb pcx
               ,product p
               ,paging_product pp
        where   pmt.paging_model_type_cd = pp.paging_model_type_cd
        and     pcx.technology_type = p.technology_type
        and     p.product_id = pp.product_id
        and     pp.paging_product_id = p_paging_product_id;

pager_equip_type_rec	c_pager_equip%ROWTYPE;

cursor c_vendor(p_product_id number
		   ,p_exclude	char) is
	select	m.manufacturer_id,  m.manufacturer_name
	from  product p, manufacturer m
	where	p.product_id = p_product_id
	and 	m.manufacturer_id = p.manufacturer_id
	and  	(p_exclude='Y'
		 and 	m.manufacturer_id != '10000119'	-- exclude Clearnet
 		 and  	m.manufacturer_id != '10007013'	-- exclude Misc. Other
 		 and	m.manufacturer_id != '10007011' -- exclude Allen Telecom
 		 or p_exclude='N');

vendor_rec	c_vendor%ROWTYPE;

cursor c_legacy(p_product_id number) is
	select feature_id
	from product_feature
	where   product_id = p_product_id
 	and    feature_id=10032685;
v_feature	number(22);

Begin
IF pi_tech_type_class = 'PAGER' THEN
open c_pager_equip(pi_product_id) ;
	fetch c_pager_equip into pager_equip_type_rec;
	If c_pager_equip%Found Then
	ro_product_info_rec.product_type_kb := pager_equip_type_rec.product_type_knowbility;
	ro_product_info_rec.equipment_type_kb := pager_equip_type_rec.paging_model_type_cd;
	ro_product_info_rec.equipment_type_class := pager_equip_type_rec.description;
	End If;
close  c_pager_equip;
ELSE
open c_product_gr_type(pi_product_id) ;
	fetch  c_product_gr_type into pr_type_rec;
	If c_product_gr_type%Found Then
		ro_product_info_rec.product_gp_type_id:=pr_type_rec.product_gp_type_id;
		ro_product_info_rec.product_gp_type_cd:=pr_type_rec.product_gp_type_cd;
		ro_product_info_rec.product_gp_type_des:=pr_type_rec.product_gp_type_des;
		ro_product_info_rec.product_gp_type_des_f:=pr_type_rec.product_gp_type_des_f;
		ro_product_info_rec.product_type_id:= pr_type_rec.product_type_id;
		ro_product_info_rec.product_type_des:=pr_type_rec.product_type_des;
		ro_product_info_rec.product_type_des_f:=pr_type_rec.product_type_des_f;
	End If;
close  c_product_gr_type;
open c_equip(pi_product_id) ;
	fetch c_equip into equip_type_rec;
	If c_equip%Found Then
	ro_product_info_rec.product_type_kb := equip_type_rec.product_type_knowbility;
	ro_product_info_rec.equipment_type_kb := equip_type_rec.equip_type_knowbility;
	ro_product_info_rec.equipment_type_class := equip_type_rec.equip_type_class;
    ro_product_info_rec.product_category_id := equip_type_rec.product_category_id;
	End If;
close  c_equip;
END IF;
open c_vendor(pi_product_id,'Y');
	fetch c_vendor into vendor_rec;
	If c_vendor%Found Then
		ro_product_info_rec.vendor_no := vendor_rec.manufacturer_id;
		ro_product_info_rec.vendor_name := vendor_rec.manufacturer_name;
        close c_vendor;
	Else
		close c_vendor;
		open c_vendor(pi_product_id,'N');
		fetch c_vendor into vendor_rec;
		If c_vendor%Found Then
			ro_product_info_rec.vendor_no := vendor_rec.manufacturer_id;
			ro_product_info_rec.vendor_name := vendor_rec.manufacturer_name;
		Else
			ro_product_info_rec.vendor_no :='0';
			ro_product_info_rec.vendor_name :=' ';
		End If;
		close c_vendor;
	End If;
open c_legacy(pi_product_id) ;
	fetch c_legacy into v_feature;
	If c_legacy%Found Then
	ro_product_info_rec.cross_fleet:='Y';
	else
	ro_product_info_rec.cross_fleet:='N';
	End If;
close c_legacy;
Exception
When Others Then
  raise;
 --RAISE_APPLICATION_ERROR(SQLCODE,SQLERRM);
End GetProductInfo;
-----------------------------------------------------------------------------
Procedure GetIMEIBySIM  (pi_SIM_ID		IN	varchar2
			,po_IMEI_ID		OUT	varchar2
			     ) is
cursor c_imei is
	select 	imei_serial_no
	from	sim_imei
	where	sim_id=pi_SIM_ID
	and	sim_seq_no=0
	and	imei_seq_no=0
	and 	seq_no=0
	and 	sim_imei_ass_dt=
		(select max(sim_imei_ass_dt)
		from	sim_imei
		where	sim_id=pi_SIM_ID
		and	sim_seq_no=0
		and	imei_seq_no=0
		and 	seq_no=0);
v_IMEI_ID	varchar2(50);
v_counter	integer:=0;
Begin
open c_imei;
loop
fetch c_imei into v_IMEI_ID;
exit when c_imei%notFound;
v_counter:=v_counter + 1;
po_IMEI_ID:= v_IMEI_ID;
end loop;
close c_imei;
If v_counter>1 Then
	raise multipleIMEIforSIM;
Elsif v_counter=0 Then
--	raise IMEINotFound;
po_IMEI_ID:= null;
End If;
Exception
--When IMEINotFound Then
--	raise IMEINotFound;
--	RAISE_APPLICATION_ERROR(-20313,'Client_Equipment pkg: IMEI  not found');
When multipleIMEIforSIM Then
	  raise multipleIMEIforSIM;
	--RAISE_APPLICATION_ERROR(-20315,'Client_Equipment pkg: Multiple Last Mule IMEI found ');
When Others Then
	If c_imei%isOpen Then
		close c_imei;
	End If;
	raise;
--	RAISE_APPLICATION_ERROR(SQLCODE,SQLERRM);
End GetIMEIBySIM ;
----------------------------------------------------------------------------
-----------------------------------------------------------------------------
Procedure GetSIMByIMEI  (pi_IMEI_ID	IN	varchar2
			 ,po_SIM_ID	OUT	varchar2
			     ) is
cursor c_sim is
	select 	sim_id
	from	sim_imei
	where	imei_serial_no=pi_IMEI_ID
	and	imei_seq_no=0
	and	sim_seq_no=0
	and 	seq_no=0
	and 	sim_imei_ass_dt=
		(select max(sim_imei_ass_dt)
		from	sim_imei
		where	imei_serial_no=pi_IMEI_ID
		and	imei_seq_no=0
		and	sim_seq_no=0
		and 	seq_no=0
		);
v_SIM_ID	varchar2(50);
v_counter	integer:=0;
Begin
open c_sim;
loop
fetch c_sim into v_SIM_ID;
exit when c_sim%notFound;
v_counter:=v_counter+1;
po_SIM_ID:= v_SIM_ID;
end loop;
close c_sim;
If v_counter>1 Then
	raise multipleSIMforIMEI;
Elsif	v_counter=0 Then
 	raise SIMNotFound;
End If;
Exception
When SIMNotFound Then
	RAISE_APPLICATION_ERROR(-20314,'Client_Equipment pkg: SIM not found');
When multipleSIMforIMEI Then
	RAISE_APPLICATION_ERROR(-20316,'Client_Equipment pkg: Multiple SIM found for the Last Mule IMEI  ');
When Others Then
	If c_sim%isOpen Then
		close c_sim;
	End If;
	raise;
-- 	RAISE_APPLICATION_ERROR(SQLCODE,SQLERRM);
End GetSIMByIMEI ;
------------------------------------------------------------------------------------
------------------------------------------------------------------------------------
Procedure GetMasterLockNumber (pi_serial_no			IN	varchar2
			    	,pi_user_id			IN	varchar2
			    	,pi_lock_reason_id		IN      number
			    	,po_master_lock			OUT	varchar2) is
cursor c_user_lock is
		select max_no
		from user_lock_max
		where username=upper(pi_user_id);
v_max_no		user_lock_max.max_no%TYPE;
cursor c_lock_sum is
		select lock_access_attempt_no , rowid
		from user_lock_summ
		where user_request_lock=upper(pi_user_id)
		and  trunc(lock_access_attempt_dt)=trunc(sysdate)
		--for update  nowait
		;
v_lock_access_attempt_no		user_lock_summ.lock_access_attempt_no%TYPE;
v_rowid					rowid;
cursor c_master_lock is
	select master_lock_number
	from pcs_equipment
	where serial_no=pi_serial_no;
v_master_lock_number	pcs_equipment.master_lock_number%TYPE;
Begin
open c_user_lock;
fetch c_user_lock into v_max_no;
If c_user_lock%NotFound Then
	raise	UserIDNotExist;
End If;
close c_user_lock;
open c_lock_sum;
fetch c_lock_sum into v_lock_access_attempt_no
		      ,v_rowid;
If c_lock_sum%NotFound Then
	insert into user_lock_summ
		(user_request_lock
		 ,lock_access_attempt_dt
		 ,lock_access_attempt_no
		 ,load_dt
		 ,update_dt)
	values
		(upper(pi_user_id)
		,sysdate
		,1
		,sysdate
		,sysdate);
Elsif v_lock_access_attempt_no < v_max_no Then
	update 	user_lock_summ
	set	lock_access_attempt_dt=sysdate
		,lock_access_attempt_no=lock_access_attempt_no + 1
		,load_dt=sysdate
		,update_dt=sysdate
	where 	rowid=v_rowid;
Else
 raise ReachedMaxLock;
End If;
close c_lock_sum;
open c_master_lock;
	fetch c_master_lock into v_master_lock_number;
	If c_master_lock%notFound Then
		raise ESNNotFound;
	End If;
	po_master_lock:=v_master_lock_number;
close c_master_lock;
	insert into pcs_equip_lock
		(serial_no
		,seq_no
		,load_dt
		,user_request_lock
		,lock_reason_id
		,update_dt
		,user_last_modify)
	values
		(pi_serial_no
		,0
		,sysdate
		,upper(pi_user_id)
		,pi_lock_reason_id
		,sysdate
		,user);
commit;
Exception
When UserIDNotExist Then
	rollback;
	RAISE_APPLICATION_ERROR(-20311,'Client_Equipment pkg: User is not in the list for Master Lock request');
When ESNNotFound Then
	rollback;
	RAISE_APPLICATION_ERROR(-20310,'Client_Equipment pkg: ESN  not found');
When ReachedMaxLock Then
	rollback;
	RAISE_APPLICATION_ERROR(-20312,'User have reached  Maximum in the Master Lock request');
When Others Then
 	RAISE_APPLICATION_ERROR(-20000,SQLERRM);
End GetMasterLockNumber;

------------------------------------------------------------------------------
------------------------------------------------------------------------------
Procedure GetMasterLockNumber (pi_serial_no		IN	varchar2
			        ,pi_user_id		IN	varchar2
			    	,pi_lock_reason_id	IN      number
			    	,pi_outlet_id		IN	number
			    	,pi_chnl_org_id		IN	number
			    	,po_master_lock		OUT	varchar2) is
cursor c_user_lock is
		select max_no
		from user_lock_max
		where username=upper(pi_user_id);
v_max_no		user_lock_max.max_no%TYPE;
cursor c_lock_sum is
		select lock_access_attempt_no , rowid
		from user_lock_summ
		where user_request_lock=upper(pi_user_id)
		and  trunc(lock_access_attempt_dt)=trunc(sysdate)
		--for update  nowait
		;
v_lock_access_attempt_no		user_lock_summ.lock_access_attempt_no%TYPE;
v_rowid					rowid;
cursor c_master_lock is
	select master_lock_number
	from pcs_equipment
	where serial_no=pi_serial_no;
v_master_lock_number	pcs_equipment.master_lock_number%TYPE;
Begin
open c_user_lock;
fetch c_user_lock into v_max_no;
If c_user_lock%NotFound Then
	raise	UserIDNotExist;
End If;
close c_user_lock;
open c_lock_sum;
fetch c_lock_sum into v_lock_access_attempt_no
		      ,v_rowid;
If c_lock_sum%NotFound Then
	insert into user_lock_summ
		(user_request_lock
		 ,lock_access_attempt_dt
		 ,lock_access_attempt_no
		 ,load_dt
		 ,update_dt)
	values
		(upper(pi_user_id)
		,sysdate
		,1
		,sysdate
		,sysdate);
Elsif v_lock_access_attempt_no < v_max_no Then
	update 	user_lock_summ
	set	lock_access_attempt_dt=sysdate
		,lock_access_attempt_no=lock_access_attempt_no + 1
		,load_dt=sysdate
		,update_dt=sysdate
	where 	rowid=v_rowid;
Else
 raise ReachedMaxLock;
End If;
close c_lock_sum;
open c_master_lock;
	fetch c_master_lock into v_master_lock_number;
	If c_master_lock%notFound Then
		raise ESNNotFound;
	End If;
	po_master_lock:=v_master_lock_number;
	close c_master_lock;
	insert into pcs_equip_lock
		(serial_no
		,seq_no
		,load_dt
		,user_request_lock
		,lock_reason_id
		,update_dt
		,user_last_modify
		,outlet_id
		,chnl_org_id	)
	values
		(pi_serial_no
		,0
		,sysdate
		,upper(pi_user_id)
		,pi_lock_reason_id
		,sysdate
		,user
		,decode(pi_outlet_id,0,null,pi_outlet_id)
		,decode(pi_chnl_org_id,0,null,pi_chnl_org_id));
commit;
Exception
When UserIDNotExist Then
	rollback;
	RAISE_APPLICATION_ERROR(-20311,'Client_Equipment pkg: User is not in the list for Master Lock request');
When ESNNotFound Then
	rollback;
	RAISE_APPLICATION_ERROR(-20310,'Client_Equipment pkg: ESN  not found');
When ReachedMaxLock Then
	rollback;
	RAISE_APPLICATION_ERROR(-20312,'User have reached  Maximum in the Master Lock request');
When Others Then
 	RAISE_APPLICATION_ERROR(-20000,SQLERRM);
End GetMasterLockNumber;

------------------------------------------------------------------------------------
------------------------------------------------------------------------------------
Procedure GetTechnologyTypeClass(pi_serial_no		IN  varchar2
				, po_tech_type_class	OUT varchar2) is
cursor c_equipment_PCS is
 	select	'PCS'
 	from	pcs_equipment pe
 	where	pe.serial_no=pi_serial_no
 	and	pe.seq_no=0;
cursor c_equipment_MIKE is
 	select	'MIKE'
 	from	iden_equipment ie
 	where	ie.serial_no=pi_serial_no
 	and	ie.seq_no=0;
cursor c_equipment_SIM is
 	select	'SIM'
 	from	sim s
 	where	s.sim_id=pi_serial_no
 	and	s.seq_no=0;
cursor c_equip_analog is
 	select	 'ANALOG'
 	from	analog_equip ae
 	where	ae.serial_no=pi_serial_no
 	and	ae.seq_no=0;
cursor c_equip_pager is
 	select	'PAGER'
 	from	distadm.paging_equip pe
 	where	pe.serial_no=pi_serial_no
 	and	pe.seq_no=0;
 cursor c_uim_card is
 	select	'UIM'
 	from	uim
 	where	uim.iccid=pi_serial_no
 	and	uim.seq_no=0;

v_tech_type_class	varchar2(10);

Begin
open  c_equipment_PCS;
fetch c_equipment_PCS into  v_tech_type_class;
If  c_equipment_PCS%NotFound Then
	open c_equipment_MIKE;
	fetch c_equipment_MIKE into v_tech_type_class;
	If c_equipment_MIKE%NotFound Then
		open c_equipment_SIM;
		fetch c_equipment_SIM into v_tech_type_class;
		If c_equipment_SIM%NotFound Then
			open c_equip_analog;
			fetch c_equip_analog into v_tech_type_class;
			If c_equip_analog%NotFound Then
				open c_uim_card;
				fetch c_uim_card into v_tech_type_class;
				If c_uim_card%NotFound Then
                                	open c_equip_Pager;
                                	fetch c_equip_Pager into v_tech_type_class;
                                	If c_equip_Pager%NotFound Then
        			    	raise ESNNotFound;
			       		End If;
                                	close c_equip_Pager;
                                End If;
				close c_uim_card;
                                
                        End If;
			close c_equip_analog;
		End if;
		close c_equipment_SIM;
	End if;
	close c_equipment_MIKE;
End If;
close c_equipment_PCS;
po_tech_type_class := v_tech_type_class;
Exception
When Others Then
If  c_equipment_PCS%isOpen Then
  	close c_equipment_PCS;
  Elsif  c_equipment_MIKE%isOpen Then
  	close c_equipment_MIKE;
  Elsif  c_equip_analog%isOpen Then
  	close c_equip_analog;
   Elsif  c_equipment_SIM%isOpen Then
  	close c_equipment_SIM;
  Elsif  c_equip_Pager%isOpen Then
  	close c_equip_Pager;
End if;
raise;

End;
----------------------------------------------------------------------------
----------------------------------------------------------------------------
Procedure GetFirmwareVersionFeature(pi_firmware_version	IN  varchar2
                           , po_firmware_feature_codes	OUT varchar2) is

--cursor c_FirmwareFeature is
--       select b.feature_cd
--       from   feature b, firmware_version_feature a
--       where  a.feature_id = b.feature_id
--       and    b.feature_group_cd = 'FIRM'
--       and    a.firmware_version = pi_firmware_version
--       order by feature_cd;
cursor c_FirmwareFeature is
       select feature_cd
       from   v_firmware_feature_attribute
       where  feature_group_cd = 'FIRM'
       and    firmware_version = pi_firmware_version
       and    feature_cd <> 'PRIMMAG'
       and    value = 'Y'
       order by feature_cd;

v_firmware_feature_cd   varchar(1000);

Begin
   v_firmware_feature_cd := '';
   po_firmware_feature_codes := '';
   open  c_FirmwareFeature;
   loop
         fetch c_FirmwareFeature into v_firmware_feature_cd;
         exit when c_FirmwareFeature%NotFound;
         po_firmware_feature_codes := po_firmware_feature_codes||v_firmware_feature_cd||'|';
   end loop;
   po_firmware_feature_codes := Substr(po_firmware_feature_codes,1,Instr(po_firmware_feature_codes,'|',-1)-1);
   close c_FirmwareFeature;
End;
----------------------------------------------------------------------------
----------------------------------------------------------------------------
Procedure GetWarranty(pi_serial_no			IN      varchar2
		      ,po_warranty_exp_date		OUT	date
  		      ,po_initial_activation_date 	OUT	date
  		      ,po_initial_manufacture_date 	OUT	date
  		      ,po_latest_pending_date		OUT	date
  		      ,po_latest_pending_model		OUT	varchar2
  		      ,po_message			OUT	varchar2
  		      ,po_warranty_extension_date	OUT	date
  		      ,po_DOA_expiry_date		OUT	date) is

v_tech_type_class	varchar2(10);
cursor c_warr_exp_date_pcs(ci_warr_status_cd varchar2) is
   select warranty_period_end_dt
   from   pcs_equip_warranty
   where   serial_no=pi_serial_no
   and seq_no=0
   and warranty_status_cd = ci_warr_status_cd
   and warranty_period_end_dt is not null
   order by warranty_period_end_dt desc  ;

cursor c_warr_exp_date_iden(ci_warr_status_cd varchar2) is
   select warranty_period_end_dt
   from   iden_equip_warranty
   where   serial_no=pi_serial_no
   and seq_no=0
   and warranty_status_cd = ci_warr_status_cd
   and warranty_period_end_dt is not null
   order by warranty_period_end_dt desc  ;

cursor c_warr_exp_date_sim(ci_warr_status_cd varchar2) is
   select warranty_period_end_dt
   from   sim_warranty
   where   sim_id=pi_serial_no
   and seq_no=0
   and warranty_status_cd = ci_warr_status_cd
   and warranty_period_end_dt is not null
   order by warranty_period_end_dt desc  ;
   
 cursor c_warr_exp_date_uim(ci_warr_status_cd varchar2) is
   select warranty_period_end_dt
   from   uim_warranty
   where   iccid=pi_serial_no
   and seq_no=0
   and warranty_status_cd = ci_warr_status_cd
   and warranty_period_end_dt is not null
   order by warranty_period_end_dt desc  ;  
   

cursor c_init_date_pcs(ci_warranty_type_cd varchar2) is
	 select aa.warranty_period_start_dt
         from   pcs_equip_warranty aa
                ,warranty_type bb
         where aa.serial_no=pi_serial_no
         and aa.seq_no=0
         and aa.warranty_type_id = bb.warranty_type_id
         and bb.warranty_type_cd = ci_warranty_type_cd
         and aa.warranty_period_start_dt is not null
  --     and aa.warranty_seq_no in (0, 1)
         order by aa.warranty_period_start_dt  ;

cursor c_init_date_iden(ci_warranty_type_cd varchar2) is
	 select aa.warranty_period_start_dt
         from  iden_equip_warranty aa
                ,warranty_type bb
         where aa.serial_no=pi_serial_no
         and aa.seq_no=0
         and aa.warranty_type_id = bb.warranty_type_id
         and bb.warranty_type_cd = ci_warranty_type_cd
         and aa.warranty_period_start_dt is not null
      --   and aa.warranty_seq_no in (0, 1)
         order by aa.warranty_period_start_dt  ;

cursor c_init_date_sim(ci_warranty_type_cd varchar2) is
	 select aa.warranty_period_start_dt
         from   sim_warranty aa
                ,warranty_type bb
         where aa.sim_id=pi_serial_no
         and aa.seq_no=0
         and aa.warranty_type_id = bb.warranty_type_id
         and bb.warranty_type_cd = ci_warranty_type_cd
         and aa.warranty_period_start_dt is not null
     --    and aa.warranty_seq_no in (0, 1)
         order by aa.warranty_period_start_dt ;
         
         cursor c_init_date_uim(ci_warranty_type_cd varchar2) is
	 select aa.warranty_period_start_dt
         from   uim_warranty aa
                ,warranty_type bb
         where aa.iccid=pi_serial_no
         and aa.seq_no=0
         and aa.warranty_type_id = bb.warranty_type_id
         and bb.warranty_type_cd = ci_warranty_type_cd
         and aa.warranty_period_start_dt is not null
            order by aa.warranty_period_start_dt ;

cursor c_pend_model_pcs is
	 select original_serial_no
         from   pcs_equip_warranty
         where serial_no =  pi_serial_no
         and seq_no=0
         and warranty_status_cd = 'PEND'
         and warranty_period_end_dt is not null
         and original_serial_no is not null
         order by warranty_period_end_dt desc ;

cursor c_pend_model_iden is
	 select original_serial_no
         from   iden_equip_warranty
         where serial_no =  pi_serial_no
         and seq_no=0
         and warranty_status_cd = 'PEND'
         and warranty_period_end_dt is not null
         and original_serial_no is not null
         order by warranty_period_end_dt desc ;

cursor c_pend_model_sim is
 	 select original_sim_id
         from sim_warranty
         where sim_id  =  pi_serial_no
         and seq_no=0
         and warranty_status_cd = 'PEND' and warranty_period_end_dt is not null
         and original_sim_id is not null
         order by warranty_period_end_dt desc ;
         
         cursor c_pend_model_uim is
 	 select original_iccid
         from uim_warranty
         where iccid  =  pi_serial_no
         and seq_no=0
         and warranty_status_cd = 'PEND' and warranty_period_end_dt is not null
         and original_iccid is not null
         order by warranty_period_end_dt desc ;

cursor c_refurb_pcs is
 	select 'REFURB'
        from   pcs_equip_warranty w,
        warranty_type wt
        where w.serial_no = pi_serial_no and
        w.seq_no = 0 and
        wt.warranty_type_id =  w.warranty_type_id and
        wt.warranty_type_cd = 'REFURB'  ;

cursor c_refurb_iden is
 	select 'REFURB'
        from   iden_equip_warranty w,
        warranty_type wt
        where w.serial_no = pi_serial_no and
        w.seq_no = 0 and
        wt.warranty_type_id =  w.warranty_type_id and
        wt.warranty_type_cd = 'REFURB'  ;


cursor c_refurb_sim is
 	select 'REFURB'
        from   sim_warranty w,
        warranty_type wt
        where w.sim_id = pi_serial_no and
        w.seq_no = 0 and
        wt.warranty_type_id =  w.warranty_type_id and
        wt.warranty_type_cd = 'REFURB'  ;
        
        cursor c_refurb_uim is
 	select 'REFURB'
        from   uim_warranty w,
        warranty_type wt
        where w.iccid = pi_serial_no and
        w.seq_no = 0 and
        wt.warranty_type_id =  w.warranty_type_id and
        wt.warranty_type_cd = 'REFURB'  ;

v_refurb	varchar2(10);
cursor c_warr_extension is
	select trunc(sysdate) + 7 - 1/86400
	from dual;

v_provider_owner_id   		number(22);
v_initial_activation_date	date;
v_warr_count			number(2):=0;
Begin
GetTechnologyTypeClass(pi_serial_no,v_tech_type_class);
If v_tech_type_class='ANALOG' Then
po_message := 'This handset requires a proof of purchase for warranty.';
Elsif v_tech_type_class='PCS' Then
	open c_warr_exp_date_pcs('VALD');
	fetch c_warr_exp_date_pcs into po_warranty_exp_date;
	v_warr_count :=   c_warr_exp_date_pcs%ROWCOUNT;
	close c_warr_exp_date_pcs;
	If v_warr_count=0 Then
		select provider_owner_id into v_provider_owner_id
		from pcs_equipment
		where serial_no=pi_serial_no
		and seq_no=0;
		If v_provider_owner_id =9 Then
		po_message := 'This handset requires a proof of purchase for warranty.';
		Else
		po_message := 'No valid Warranty exists';
		End If;
	Else
	open c_init_date_pcs('STANDARD');
		fetch c_init_date_pcs into v_initial_activation_date;
	close c_init_date_pcs;
	po_initial_activation_date :=v_initial_activation_date;
	open c_refurb_pcs;
	fetch c_refurb_pcs into v_refurb;
	If c_refurb_pcs%NotFound Then
		po_DOA_expiry_date :=v_initial_activation_date + 30;
	End If;
	close c_refurb_pcs;
	open c_init_date_pcs('MANUFACT');
		fetch c_init_date_pcs into po_initial_manufacture_date;
	close c_init_date_pcs;
	open c_warr_exp_date_pcs('PEND');
	fetch c_warr_exp_date_pcs into po_latest_pending_date;
	close c_warr_exp_date_pcs;
	open c_pend_model_pcs;
	fetch c_pend_model_pcs into po_latest_pending_model;
	close c_pend_model_pcs;
	open c_warr_extension;
	fetch c_warr_extension into po_warranty_extension_date;
	close c_warr_extension;
	End If;
Elsif v_tech_type_class='MIKE'  Then
	open c_warr_exp_date_iden('VALD');
	fetch c_warr_exp_date_iden into po_warranty_exp_date;
	v_warr_count :=   c_warr_exp_date_iden%ROWCOUNT;
	close c_warr_exp_date_iden;
	If v_warr_count=0 Then
	po_message := 'No valid Warranty exists';
	Else
	open c_init_date_iden('STANDARD');
		fetch c_init_date_iden into v_initial_activation_date;
	close c_init_date_iden;
	po_initial_activation_date :=v_initial_activation_date;
	open c_refurb_iden;
	fetch c_refurb_iden into v_refurb;
	If c_refurb_iden%NotFound Then
		po_DOA_expiry_date :=v_initial_activation_date + 30;
	End If;
	close c_refurb_iden;
	open c_init_date_iden('MANUFACT');
		fetch c_init_date_iden into po_initial_manufacture_date;
	close c_init_date_iden;
	open c_warr_exp_date_iden('PEND');
		fetch c_warr_exp_date_iden into po_latest_pending_date;
	close c_warr_exp_date_iden;
	open c_pend_model_iden;
		fetch c_pend_model_iden into po_latest_pending_model;
	close c_pend_model_iden;
	open c_warr_extension;
		fetch c_warr_extension into po_warranty_extension_date;
	close c_warr_extension;
	End If;
Elsif v_tech_type_class='SIM'  Then
	open c_warr_exp_date_sim('VALD');
	fetch c_warr_exp_date_sim into po_warranty_exp_date;
	v_warr_count :=   c_warr_exp_date_sim%ROWCOUNT;
	close c_warr_exp_date_sim;
	If v_warr_count=0 Then
	po_message := 'No valid Warranty exists';
	Else
	open c_init_date_sim('STANDARD');
		fetch c_init_date_sim into v_initial_activation_date;
	close c_init_date_sim;
	po_initial_activation_date :=v_initial_activation_date;
	open c_refurb_sim;
	fetch c_refurb_sim into v_refurb;
	If c_refurb_sim%NotFound Then
		po_DOA_expiry_date :=v_initial_activation_date + 30;
	End If;
	close c_refurb_sim;
	open c_init_date_sim('MANUFACT');
		fetch c_init_date_sim into po_initial_manufacture_date;
	close c_init_date_sim;
	open c_warr_exp_date_sim('PEND');
		fetch c_warr_exp_date_sim into po_latest_pending_date;
	close c_warr_exp_date_sim;
	open c_pend_model_sim;
		fetch c_pend_model_sim into po_latest_pending_model;
	close c_pend_model_sim;
	open c_warr_extension;
		fetch c_warr_extension into po_warranty_extension_date;
	close c_warr_extension;
	End If;
	Elsif v_tech_type_class='UIM'  Then
	open c_warr_exp_date_uim('VALD');
	fetch c_warr_exp_date_uim into po_warranty_exp_date;
	v_warr_count :=   c_warr_exp_date_uim%ROWCOUNT;
	close c_warr_exp_date_uim;
	If v_warr_count=0 Then
	po_message := 'No valid Warranty exists';
	Else
	open c_init_date_uim('STANDARD');
		fetch c_init_date_uim into v_initial_activation_date;
	close c_init_date_uim;
	po_initial_activation_date :=v_initial_activation_date;
	open c_refurb_uim;
	fetch c_refurb_uim into v_refurb;
	If c_refurb_uim%NotFound Then
		po_DOA_expiry_date :=v_initial_activation_date + 30;
	End If;
	close c_refurb_uim;
	open c_init_date_uim('MANUFACT');
		fetch c_init_date_uim into po_initial_manufacture_date;
	close c_init_date_uim;
	open c_warr_exp_date_uim('PEND');
		fetch c_warr_exp_date_uim into po_latest_pending_date;
	close c_warr_exp_date_uim;
	open c_pend_model_uim;
		fetch c_pend_model_uim into po_latest_pending_model;
	close c_pend_model_uim;
	open c_warr_extension;
		fetch c_warr_extension into po_warranty_extension_date;
	close c_warr_extension;
	End If;
End If;
End;

------------------------------------------------------------------------------------------------------------
------------------------------------------------------------------------------------------------------------
Procedure GetEquipmentInfoByProductCode( pi_product_cd		        IN	varchar2
                                        ,po_product_cd                  OUT     varchar2
				        ,po_product_id                     OUT     number
   					,po_product_category_id         OUT     number
   					,po_vendor_name                 OUT     varchar2
  					,po_vendor_no                   OUT     number
  					,po_product_gp_type_id          OUT     number
   					,po_product_gp_type_cd          OUT     varchar2
  					,po_product_gp_type_des         OUT     varchar2
   					,po_product_gp_type_des_f       OUT     varchar2
   					,po_product_type_id             OUT     number
   					,po_product_type_des            OUT     varchar2
  					,po_product_type_des_f          OUT     varchar2
  					,po_product_type_kb             OUT     varchar2
   					,po_equipment_type_kb           OUT     varchar2
   					,po_equipment_type_class        OUT     varchar2
   					,po_technology_type             OUT     varchar2
   					,po_english_product_name        OUT     varchar2
   					,po_french_product_name         OUT     varchar2
   					,po_product_class_id            OUT     number
  					,po_product_status_cd           OUT     varchar2
   					,po_product_class_cd            OUT     varchar2
  					,po_product_class_des           OUT     varchar2
   					,po_mode_code                   OUT     number
   					,po_mode_description            OUT     varchar2
   					,po_product_type_list           OUT     varchar2
   					,po_cross_fleet			OUT 	varchar2 
  					,po_equipment_status_type_id    OUT 	number
  					,po_equipment_status_id         OUT 	number
  					,po_equipment_status_dt         OUT 	date
  					,po_browser_version		OUT 	varchar2
			   		,po_firmware_version               OUT 	varchar2 
			    		,po_prl_cd			        OUT 	varchar2 
			    		,po_prl_des			OUT 	varchar2
			   		,po_browser_protocol               OUT 	varchar2
   			    		,po_firmware_feature_code_list     OUT 	varchar2
   			    		) 
Is

  v_product_record        Product_Info_Full;
  v_tech_type_class       varchar2(10) := 'NOT PAGER';
  

 				       
Begin

  GetProductInfoByProductCode(pi_product_cd,v_tech_type_class,v_product_record);
 
  
   po_product_cd := pi_product_cd;
   po_product_id := v_product_record.product_id;
   po_product_category_id := v_product_record.product_category_id;
   po_product_gp_type_id := v_product_record.product_gp_type_id;
   po_vendor_name:= v_product_record.vendor_name;
   po_vendor_no := v_product_record.vendor_no;
   po_product_gp_type_id := v_product_record.product_gp_type_id;
   po_product_gp_type_cd := v_product_record.product_gp_type_cd;
   po_product_gp_type_des := v_product_record.product_gp_type_des;
   po_product_gp_type_des_f := v_product_record.product_gp_type_des_f;
   po_product_type_id := v_product_record.product_type_id;
   po_product_type_des	 := v_product_record.product_type_des;
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
   po_equipment_status_dt := sysdate;
   po_browser_version := v_product_record.browser_version;
   po_firmware_version := v_product_record.firmware_version;
   po_prl_cd := v_product_record.prl_cd;
   po_prl_des := v_product_record.prl_des;
   po_browser_protocol := v_product_record.browser_protocol;
   po_firmware_feature_code_list  := v_product_record.firmware_feature_code_list;                     
 
 

 Exception

When Others Then
	
	Raise_Application_Error(-20161, 'Equipment Info By Product Code Query Failed. Oracle:(['
    							|| sqlcode || '] [' || sqlerrm  || '])'  );

End GetEquipmentInfoByProductCode;
				       
-------------------------------------------------------------------------------
----------------------------------------------------------------------------
Procedure GetProductInfoByProductCode (pi_product_cd              IN   varchar2
                                     ,pi_tech_type_class        IN   varchar2
                         			 ,ro_product_info_rec			OUT	Product_Info_Full )
          is
          
v_product_id	  	  number(22);          
          
          
cursor c_equip(p_product_cd varchar2) is
	select	pcx.product_type_knowbility
		,pcx.equip_type_knowbility
		,et.equip_type_class, p.product_category_id
		,p.technology_type, p.english_product_name,p.french_product_name,p.product_id
		,p.product_class_id ,nvl(p.product_status_cd,' ')  product_status_cd
		,pc.product_class_cd ,pc.product_class_des
	from 	 product p
 		,product_class_xref_kb pcx
 		,kb_equip_type et
 		,product_classification pc
 	where 	p.product_cd = p_product_cd
 	and     pcx.technology_type=p.technology_type
 	and     pcx.product_class_id=p.product_class_id
 	and     pc.product_class_id = pcx.product_class_id
 	and     et.equip_type_knowbility(+) = pcx.equip_type_knowbility;
 	
equip_type_rec	c_equip%ROWTYPE;
          
cursor c_product_gr_type(p_product_id number) is
	select ci_g.sub_type_id product_gp_type_id
	 , cg.ci_group_cd  product_gp_type_cd
       	, ci_g.catalogue_item_des product_gp_type_des
       , '' product_gp_type_des_f  --Not existing feald
	,ci_gp.sub_type_id  product_type_id
    	,ci_gp.catalogue_item_des product_type_des
        , '' product_type_des_f     --Not existing feald
from catalogue_item ci_p ,catalogue_item ci_g, ci_group cg, ci_relationship cr, ci_relationship crg, catalogue_item ci_gp
where ci_p.sub_type_id= p_product_id
and ci_p.catalogue_item_type_cd = 'PRODUCT'
and cg.ci_group_type_cd='PROD_EXCL_GRP'
and ci_g.sub_type_id= cg.ci_group_id
and ci_g.catalogue_item_type_cd = 'GROUP'
and cr.catalogue_parent_item_id=ci_g.catalogue_item_id
and cr.catalogue_child_item_id=ci_gp.catalogue_item_id
AND cr.RELATIONSHIP_TYPE_CD = 'GENERICGRP' -- to do change to  SKU_COMP
and ci_gp.catalogue_item_type_cd = 'GROUP'
and crg.catalogue_parent_item_id=ci_gp.catalogue_item_id
and crg.catalogue_child_item_id=ci_p.catalogue_item_id
AND crg.RELATIONSHIP_TYPE_CD = 'SKU_COMP';


pr_type_rec	c_product_gr_type%ROWTYPE;


cursor c_pager_equip(p_paging_product_id number) is
        select  pcx.product_type_knowbility
               ,pmt.paging_model_type_cd
               ,pmt.description
        from
                paging_model_type pmt
               ,product_class_xref_kb pcx
               ,product p
               ,paging_product pp
        where   pmt.paging_model_type_cd = pp.paging_model_type_cd
        and     pcx.technology_type = p.technology_type
        and     p.product_id = pp.product_id
        and     pp.paging_product_id = p_paging_product_id;

pager_equip_type_rec	c_pager_equip%ROWTYPE;

cursor c_vendor(p_product_id number
		   ,p_exclude	char) is
	select	m.manufacturer_id,  m.manufacturer_name
	from  product p, manufacturer m
	where	p.product_id = p_product_id
	and 	m.manufacturer_id = p.manufacturer_id
	and  	(p_exclude='Y'
		 and 	m.manufacturer_id != '10000119'	-- exclude Clearnet
 		 and  	m.manufacturer_id != '10007013'	-- exclude Misc. Other
 		 and	m.manufacturer_id != '10007011' -- exclude Allen Telecom
 		 or p_exclude='N');

vendor_rec	c_vendor%ROWTYPE;

cursor c_legacy(p_product_id number) is
	select feature_id
	from product_feature
	where   product_id = p_product_id
 	and    feature_id=10032685;
v_feature	number(22);

cursor c_mode(pi_product_id number) is
	select ci_gp.sub_type_id product_type_id,
    ci_gp.catalogue_item_des product_type_des
from catalogue_item ci_p ,catalogue_item ci_g,ci_relationship cr, ci_relationship crg, catalogue_item ci_gp
where ci_p.sub_type_id= pi_product_id
and ci_p.catalogue_item_type_cd = 'PRODUCT'
and ci_g.sub_type_id= 10002686
and ci_g.catalogue_item_type_cd = 'GROUP'
and cr.catalogue_parent_item_id=ci_g.catalogue_item_id
and cr.catalogue_child_item_id=ci_gp.catalogue_item_id
AND cr.RELATIONSHIP_TYPE_CD = 'GENERICGRP'
and ci_gp.catalogue_item_type_cd = 'GROUP'
and crg.catalogue_parent_item_id=ci_gp.catalogue_item_id
and crg.catalogue_child_item_id=ci_p.catalogue_item_id
AND crg.RELATIONSHIP_TYPE_CD = 'GENERICGRP';


--Cursor to select Product Service Initiatives for given product
cursor c_promo(pi_product_id number) is
	select ci_gp.sub_type_id product_type_id
from catalogue_item ci_p ,catalogue_item ci_g,ci_relationship cr, ci_relationship crg, catalogue_item ci_gp
where ci_p.sub_type_id= pi_product_id  
and ci_p.catalogue_item_type_cd = 'PRODUCT'
and ci_g.sub_type_id= 10005555
and ci_g.catalogue_item_type_cd = 'GROUP'
and cr.catalogue_parent_item_id=ci_g.catalogue_item_id
and cr.catalogue_child_item_id=ci_gp.catalogue_item_id
AND cr.RELATIONSHIP_TYPE_CD = 'GENERICGRP'
and ci_gp.catalogue_item_type_cd = 'GROUP'
and crg.catalogue_parent_item_id=ci_gp.catalogue_item_id
and crg.catalogue_child_item_id=ci_p.catalogue_item_id
AND crg.RELATIONSHIP_TYPE_CD = 'GENERICGRP';

cursor c_browser is
	select fb.product_id, fb.firmware_version, fb.browser_version,fb.prl_cd,
	bv.protocol, pc.prl_des
	from firmware_browser fb, browser_version bv, prl_code pc
	where fb.product_id = 10001716
	and fb.default_ind = '1'
	and fb.browser_version = bv.browser_version
	and fb.prl_cd = pc.prl_cd;

browser_rec c_browser%ROWTYPE;	

v_product_promo_type_id	 number(22);



Begin

open c_equip(pi_product_cd) ;
	fetch c_equip into equip_type_rec;
	If c_equip%Found Then
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
        Else
        raise ProductNotFound;
 	End If;
close  c_equip;

IF pi_tech_type_class = 'PAGER' THEN
open c_pager_equip(v_product_id) ;
	fetch c_pager_equip into pager_equip_type_rec;
	If c_pager_equip%Found Then
	ro_product_info_rec.product_type_kb := pager_equip_type_rec.product_type_knowbility;
	ro_product_info_rec.equipment_type_kb := pager_equip_type_rec.paging_model_type_cd;
	ro_product_info_rec.equipment_type_class := pager_equip_type_rec.description;
	End If;
close  c_pager_equip;
ELSE
open c_product_gr_type(v_product_id) ;
	fetch  c_product_gr_type into pr_type_rec;
	If c_product_gr_type%Found Then
		ro_product_info_rec.product_gp_type_id:=pr_type_rec.product_gp_type_id;
		ro_product_info_rec.product_gp_type_cd:=pr_type_rec.product_gp_type_cd;
		ro_product_info_rec.product_gp_type_des:=pr_type_rec.product_gp_type_des;
		ro_product_info_rec.product_gp_type_des_f:=pr_type_rec.product_gp_type_des_f;
		ro_product_info_rec.product_type_id:= pr_type_rec.product_type_id;
		ro_product_info_rec.product_type_des:=pr_type_rec.product_type_des;
		ro_product_info_rec.product_type_des_f:=pr_type_rec.product_type_des_f;
	End If;
close  c_product_gr_type;

END IF;
open c_vendor(v_product_id,'Y');
	fetch c_vendor into vendor_rec;
	If c_vendor%Found Then
		ro_product_info_rec.vendor_no := vendor_rec.manufacturer_id;
		ro_product_info_rec.vendor_name := vendor_rec.manufacturer_name;
        close c_vendor;
	Else
		close c_vendor;
		open c_vendor(v_product_id,'N');
		fetch c_vendor into vendor_rec;
		If c_vendor%Found Then
			ro_product_info_rec.vendor_no := vendor_rec.manufacturer_id;
			ro_product_info_rec.vendor_name := vendor_rec.manufacturer_name;
		Else
			ro_product_info_rec.vendor_no :='0';
			ro_product_info_rec.vendor_name :=' ';
		End If;
		close c_vendor;
	End If;
open c_legacy(v_product_id) ;
	fetch c_legacy into v_feature;
	If c_legacy%Found Then
	ro_product_info_rec.cross_fleet:='Y';
	else
	ro_product_info_rec.cross_fleet:='N';
	End If;
close c_legacy;
open c_mode(v_product_id);
      fetch c_mode into  ro_product_info_rec.mode_code
      			,ro_product_info_rec.mode_description;
close c_mode;
      
-- Get Promo Product Types
open c_promo(v_product_id);
     loop
      fetch c_promo into v_product_promo_type_id;
      exit when c_promo%NotFound;
      ro_product_info_rec.product_type_list:= ro_product_info_rec.product_type_list||v_product_promo_type_id;
      end loop;
close c_promo;

open c_browser;
     loop
      fetch c_browser into browser_rec;
      exit when c_browser%NotFound;
      ro_product_info_rec.browser_version := browser_rec.browser_version;
      ro_product_info_rec.firmware_version := browser_rec.firmware_version;
      ro_product_info_rec.browser_protocol := browser_rec.protocol;
      ro_product_info_rec.prl_cd := browser_rec.prl_cd;
      ro_product_info_rec.prl_des := browser_rec.prl_des;
     end loop;
close c_browser;

GetFirmwareVersionFeature (ro_product_info_rec.firmware_version,ro_product_info_rec.firmware_feature_code_list);

     
Exception

When ProductNotFound Then
  
  RAISE_APPLICATION_ERROR(-20318,'Client_Equipment pkg: Product ID not found');

When Others Then
	Raise_Application_Error(-20161, 'Product Info By Product Code Query Failed. Oracle:(['
    							|| sqlcode || '] [' || sqlerrm  || '])'  );

End GetProductInfoByProductCode;
----------------------------------------------------------------------------------------------------------------
Procedure GetProductIdByProductCode(pi_product_cd		IN	varchar2
				       ,po_product_id             OUT  number) 
Is
  
  cursor c_product(p_product_cd varchar2) is
	select	p.product_id 
	from  product p
        where p.product_cd = p_product_cd;
        
 Begin       
        
 open c_product(pi_product_cd) ;
	fetch c_product into po_product_id;
    If c_product%NotFound Then
	raise ProductNotFound ;
   End If; 
 close c_product;       
   
 Exception

 
 When ProductNotFound Then
  
  RAISE_APPLICATION_ERROR(-20318,'Client_Equipment pkg: Product ID not found');

When Others Then
	
	Raise_Application_Error(-20161, 'Product Id By Product Code Query Failed. Oracle:(['
				|| sqlcode || '] [' || sqlerrm  || '])'  );

End GetProductIdByProductCode;
---------------------------------------------------------------------------------------------------------------------------

Procedure ChangeEquipmentStatus (pi_serial_no		        IN	varchar2
			        ,pi_user_id		        IN	varchar2
			    	,pi_equipment_status_type_id 	IN	number
			        ,pi_equipment_status_id		IN	number
			        ,pi_tech_type                   IN      varchar2 
			        ,pi_product_class_id            IN	number
			        ) is
			        
v_equipment_group	varchar2(20);			        

Begin

GetEquipmentGroup(pi_tech_type,pi_product_class_id,v_equipment_group);

If v_equipment_group = 'PCS' Then
  insert into pcs_equip_status
		(serial_no
		,seq_no
		,equipment_status_type_id
		,equipment_status_id
		,equipment_status_dt
		,load_dt
		,update_dt
		,user_last_modify) 
	values
		(pi_serial_no
		,0
		,pi_equipment_status_type_id
		,pi_equipment_status_id
		,sysdate
		,sysdate
		,sysdate
		,upper(pi_user_id));
commit;		
  Elsif v_equipment_group = 'IDEN' Then
    insert into iden_equip_status
		(serial_no
		,seq_no
		,equipment_status_type_id
		,equipment_status_id
		,equipment_status_dt
		,load_dt
		,update_dt
		,user_last_modify) 
	values
		(pi_serial_no
		,0
		,pi_equipment_status_type_id
		,pi_equipment_status_id
		,sysdate
		,sysdate
		,sysdate
		,upper(pi_user_id));
commit;		
Elsif v_equipment_group = 'ANA' Then
   insert into analog_equip_status
		(serial_no
		,seq_no
		,equipment_status_type_id
		,equipment_status_id
		,equipment_status_dt
		,load_dt
		,update_dt
		,user_last_modify) 
	values
		(pi_serial_no
		,0
		,pi_equipment_status_type_id
		,pi_equipment_status_id
		,sysdate
		,sysdate
		,sysdate
		,upper(pi_user_id));
commit;		
Elsif v_equipment_group = 'SIM' Then
   insert into sim_status
		(sim_id
		,seq_no
		,equipment_status_type_id
		,equipment_status_id
		,equipment_status_dt
		,load_dt
		,update_dt
		,user_last_modify) 
	values
		(pi_serial_no
		,0
		,pi_equipment_status_type_id
		,pi_equipment_status_id
		,sysdate
		,sysdate
		,sysdate
		,upper(pi_user_id));
commit;		
Elsif v_equipment_group = 'PAGING' Then
   insert into paging_equip_status
		(serial_no
		,seq_no
		,equipment_status_type_id
		,equipment_status_id
		,equipment_status_dt
		,load_dt
		,update_dt
		,user_last_modify) 
	values
		(pi_serial_no
		,0
		,pi_equipment_status_type_id
		,pi_equipment_status_id
		,sysdate
		,sysdate
		,sysdate
		,upper(pi_user_id));
	
commit;
End If; 
Exception

When ESNNotFound Then
	rollback;
	RAISE_APPLICATION_ERROR(-20310,'Client_Equipment pkg: ESN  not found');

When Others Then
 	RAISE_APPLICATION_ERROR(-20000,SQLERRM);
End ChangeEquipmentStatus;
-----------------------------------------------------------------------------------------------------------
----------------------------------------------------------------------------------------------------------------
Procedure GetEquipmentGroup            (pi_tech_type                   IN      varchar2 
			                ,pi_product_class_id            IN	number
				        ,po_equipment_group             OUT      varchar2
				       ) 
Is
  
  cursor c_equip_group(p_tech_type varchar2 ,p_product_class_id number) is
	select	pcx.sems_equipment_group
	  from  product_class_xref_kb pcx
          where pcx.technology_type = p_tech_type
          and pcx.product_class_id  = p_product_class_id;
        
 Begin       
        
 open c_equip_group(pi_tech_type  ,pi_product_class_id ) ;
	fetch c_equip_group into po_equipment_group;
    If c_equip_group%NotFound Then
	raise EquipmentGroupNotFound ;
   End If; 
 close c_equip_group;       
   
 Exception

 
 When EquipmentGroupNotFound Then
  
  RAISE_APPLICATION_ERROR(-20318,'Client_Equipment pkg: Equipment Group not found');

When Others Then
	
  RAISE_APPLICATION_ERROR(-20161, 'GetEquipmentGroup Query Failed. Oracle:(['
				|| sqlcode || '] [' || sqlerrm  || '])'  );

End GetEquipmentGroup;				    
	
--------------------------------------------------------------------------------------------------------------
End;
PACKAGE BODY CLIENT_EQUIPMENT compiled successfully
*** SCRIPT END :  Session:IVRADM3@DDIST(1)   30-Aug-2005 9:27:50 *** 
