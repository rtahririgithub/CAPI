create or replace package PU_BA_Integration as

 ------------------------------------------------------------------------
-- description: Package PU_BA_Integration  containing procedures 
--		for Portal User Info and Billing Account Data Integration 
--
-- Database: CODS 	Schema: EASADM	
--	
-- Date	   		Developer     	  	  Modifications
-- 14-Dec-2000		Ludmila Pomirche	  created
-- 28-Aug-2001		Ludmila Pomirche          modification related to database structure changes
-----------------------------------------------------------------------

----------------------------------------------------------------------------
-- description: Portal_admin_Account_insert for inserting record into
--		portal_administered_account 
-- Paramaters : (IN)  pi_portal_user_id - Portal User ID
--		(IN) pi_Client_account_Id  - CRDB Client account ID
--		(IN)pi_Role_CD		  - CRDB Role Code
--		(IN)pi_User	- User , source of data
--		    
--		(OUT)po_Insert_succeed - Indicator that insert succeed
--
-----------------------------------------------------------------------------
Procedure Portal_admin_Account_insert (pi_Portal_User_Id	IN number
					,pi_Client_account_Id   IN number
					,pi_Role_CD		IN varchar2
					,pi_User		IN varchar2 default 'WEBRA'
					,po_Insert_succeed	OUT  boolean);
					
----------------------------------------------------------------------------
-- description: Procedure GetClientAccountId to get CRDB Client Account ID
--		by Account ID  (Ban)
-- Paramaters : 
--		(IN)  pi_Account_ID -  Billing Account Number
--		    
--		(OUT)po_Client_Account_ID - CRDB Client account ID
--
-----------------------------------------------------------------------------
Procedure GetClientAccountId (pi_Account_ID	IN	number
			     ,po_Client_Account_ID	OUT 	number);

----------------------------------------------------------------------------
-- description: Procedure PU_BA_staging_Batch for processing records 
--		from the activated_account_staging table
----------------------------------------------------------------------------			     

Procedure PU_BA_Staging_Batch;

------------------------------------------------------------------------------
-- description: Procedure Write_Error_Log for writing error log into
--		the batch_error_log table
------------------------------------------------------------------------------

Procedure Write_Error_Log (pi_process		varchar2
			   ,pi_error_code	number
			   ,pi_error_msg	varchar2);
End;
/

create or replace package body PU_BA_Integration as

Procedure Portal_admin_Account_insert (pi_Portal_User_Id	IN number
					,pi_Client_account_Id   IN number
					,pi_Role_CD		IN varchar2
					,pi_User		IN varchar2 default 'WEBRA'
					,po_Insert_succeed	OUT boolean) is
v_counter	integer:=0;
					
Begin
select count(*) into v_counter
from   portal_administered_account
where	client_account_id =pi_Client_account_Id 
and 	portal_user_id = pi_Portal_User_Id
and 	role_cd =pi_Role_CD;

If v_counter=0 Then
insert into portal_administered_account
				(client_account_id
		 		,portal_user_id
		 		,role_cd
		 		,effective_dt
		 		,expiration_dt
		 		,load_dt
		 		,update_dt
		 		,user_last_modify)
		 		values
		 		(pi_Client_account_Id 
		 		,pi_Portal_User_Id
		 		,pi_Role_CD
		 		,sysdate
		 		,null
		 		,sysdate
		 		,sysdate
		 		,pi_User);
commit;
End If;
po_Insert_succeed:=true;
Exception
When Others Then
	po_Insert_succeed:=false;
	Write_Error_Log('Insert into Portal Administered Account ',SQLCODE,SQLERRM);
End Portal_admin_Account_insert;

---------------------------------------------------------------------------
---------------------------------------------------------------------------
Procedure GetClientAccountId (pi_Account_ID		IN	number
			     ,po_Client_Account_ID	OUT 	number) is
			     
cursor c_client	is
		select	client_account_id 
		from	alias_type  a
			,client_account_alias caa
		where	caa.alias_value = to_char(pi_Account_ID)
		and	caa.alias_type_id = a.alias_type_id
		and     caa.effective_dt <= sysdate
		and     (caa.expiry_dt is null 
			 or trunc(caa.expiry_dt)  > trunc(sysdate))
		and     a.xternal_system_cd ='KNOW'
		and     a.alias_name ='BAN';
		
		
v_client_account_Id	number(22);
v_client_counter	integer:=0;		   
Begin
open c_client;
fetch c_client into v_client_account_Id;
v_client_counter:=c_client%RowCount;
close	c_client;
If v_client_counter= 0  Then
	po_Client_Account_ID:=0;
Else
po_Client_Account_ID:=v_client_account_Id;
End If;
Exception
When Others Then
	po_Client_Account_ID:=0;
	Write_Error_Log('Getting Client Account ID',SQLCODE,SQLERRM);
End GetClientAccountId;			     

---------------------------------------------------------------------------
---------------------------------------------------------------------------
Procedure PU_BA_Staging_Batch is
cursor c_staging is
	select 	rowid
		,portal_user_id
		,bscs_account_id
		,role_cd
		,user_last_modify
	from activated_account_staging
	where load_dt < sysdate -1/288;
	
v_rowid			rowid;
v_portal_user_id	number(22);
v_bscs_account_id	number(22);
v_role_cd		varchar2(5);
v_user_last_modify	varchar2(10);
	
v_client_account_id	number(22):=0;
v_insert_succeed	boolean;

v_time_expiry		date;
Begin
select sysdate + 1/1440 into v_time_expiry
from dual;
open c_staging; 
loop
If sysdate>v_time_expiry Then
exit;
End If;
fetch c_staging into v_rowid
		    ,v_portal_user_id
		    ,v_bscs_account_id
		    ,v_role_cd
		    ,v_user_last_modify;
exit when c_staging%notFound;
GetClientAccountId(v_bscs_account_id, v_client_account_id);
If v_client_account_id>0 Then
	Portal_admin_Account_insert (v_portal_User_Id	
				     ,v_client_account_id   
				      ,v_Role_CD	
				      ,v_user_last_modify	
				      ,v_insert_succeed);
	If v_insert_succeed Then
		delete from activated_account_staging
		where rowid=v_rowid;
		commit;
	End If;
End If;
end loop;
Exception
When Others Then
 	rollback;
 	Write_Error_Log('Portal User, BSCS Account Batch',SQLCODE,SQLERRM);
End PU_BA_Staging_Batch;

---------------------------------------------------------------------------
---------------------------------------------------------------------------
Procedure Write_Error_Log (pi_process		varchar2
			  ,pi_error_code	number
			  ,pi_error_msg		varchar2) is
			   
Begin
insert	into batch_error_log
 		(error_id
  		,package_name 
  		,process
  		,error_code
  		,error_message	
  		,create_date	
  		,create_user)
  	 values
  	 	(batch_error_seq.nextval
  	 	,'EAS_PORTAL'
  	 	,pi_process
  	 	 ,pi_error_code	
		 ,pi_error_msg
		 ,sysdate
		 ,user);
Exception
When Others Then
null; 			
End Write_Error_Log;					
End;
/