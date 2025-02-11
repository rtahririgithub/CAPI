create or replace package EAS_Portal as

 ------------------------------------------------------------------------
-- description: Package EAS_Portal  containing procedures 
--		for Portal User ID and BSCS Account ID Link Transfer to CDRB
--
--  Database: EAS 	Schema: EASADM		
--	
-- Date	   		Developer     	  	  Modifications
-- 14-Dec-2000		Ludmila Pomirche	  created
--
-----------------------------------------------------------------------

----------------------------------------------------------------------------
-- description: Procedure PU_BA_Link_Transfer for transferring Portal User ID
--		and BSCS Account ID link to the CRDBADM on CODS
-- Paramaters : (IN)  pi_portal_user_id - Portal User ID
--		(IN)  pi_customer_ID - Customer ID in BSCS
--		    
--		(OUT)po_transfer_succeed - Indicator that transfer succeed
--
-----------------------------------------------------------------------------
Procedure PU_BA_Link_Transfer (pi_portal_user_id	IN 	number
			       ,pi_customer_id		IN 	number
			       ,po_transfer_succeed	OUT	boolean);
			       
----------------------------------------------------------------------------
-- description: Procedure PU_BA_Link_Batch for selecting all records that should be 
--		transferred to the CRDB
----------------------------------------------------------------------------
			       
Procedure PU_BA_Link_Batch ;

------------------------------------------------------------------------------
-- description: Procedure Write_Error_Log for writing error log into
--		the batch_error_log table
------------------------------------------------------------------------------

Procedure Write_Error_Log (pi_process		varchar2
			   ,pi_error_code	number
			   ,pi_error_msg	varchar2);
			      
End;
/

create or replace package body EAS_Portal as

Procedure PU_BA_Link_Transfer (pi_portal_user_id	IN	number
			       ,pi_customer_id		IN 	number
			       ,po_transfer_succeed	OUT	boolean) is
Begin
insert into activated_account_staging 
            			(activated_account_staging_id
            			, portal_user_id 
            			, bscs_account_id 
            			, role_cd
                 		, loading_crdb_ind 
                 		, load_dt
                        	, update_dt 
                        	, user_last_modify 
                        	, source_system)
                        	values
                        	(activated_account_staging_seq.nextval
                        	, pi_portal_user_id	
                         	, pi_customer_id		
                          	, 'PT' 
                          	, 'P' 
                        	, sysdate
                        	, sysdate
                        	, 'WEBRA'
                         	, 'WEBRA' ) ;               	
po_transfer_succeed:=true;

Exception
When Others Then
 po_transfer_succeed:=false;
 Write_Error_Log('Insert into activated_account_staging for PU: '||pi_portal_user_id||',BA: '||pi_customer_id,SQLCODE,SQLERRM);
End PU_BA_Link_Transfer;

Procedure PU_BA_Link_Batch is

cursor  c_not_transferred is 
	select	rowid
		,client_id
		,customer_id
	from   activation_log
	where  transferred_to_CRDB='N'
	and	client_id not in (0,27,910,2497,1071,14625)
	and     dealer_id=0
	and     customer_id!=0
	;
	
rec_transferred	boolean:=false;

Begin
for rec in c_not_transferred loop
rec_transferred:=false;
PU_BA_Link_Transfer(rec.client_id,rec.customer_id,rec_transferred);
If rec_transferred Then
   update	activation_log
   set		transferred_to_CRDB='Y'
   where 	rowid=rec.rowid;
   commit;
End If;	
end loop;
Exception
When Others Then
	Write_Error_Log('Portal User , BSCS Account Link ',SQLCODE,SQLERRM);
End PU_BA_Link_Batch;

Procedure Write_Error_Log (pi_process		varchar2
			   ,pi_error_code	number
			   ,pi_error_msg	varchar2) is
			   
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