------------------------------------------------------------------------
-- description: Package Client_Info  containing procedures to get Client info
--		for CTI application from CODS database
-- 		
-- Database: CODS	| Schema: CTIADM
-- Date	   		Developer     	  	  Modifications
-- 04-SEP-2001		Ludmila Pomirche	  created
-- 08-Feb-2002          Ludmila Pomirche	  Added Priority_accounts logic

------------------------------------------------------------------------
------------------------------------------------------------------------------------
-- IMPORTANT NOTE !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
-- Please increment the version_no variable value when you make changes to this package 
-- to reflect version changes.
--------------------------------------------------------------------------------------


Create or replace package Client_Info as

------------------------------------------------------------------------

ClientNotFound		Exception;
PRAGMA			EXCEPTION_INIT(ClientNotFound, -20501);

InvoicesNotFound	Exception;
PRAGMA			EXCEPTION_INIT(InvoicesNotFound	, -20502);

InvalidMobileNo		Exception;
PRAGMA			EXCEPTION_INIT(InvalidMobileNo	, -20503);
------------------------------------------------------------------------

version_no   CONSTANT VARCHAR2(10) := '3.19.4';

FUNCTION getVersion RETURN VARCHAR2; 
   
Procedure GetClientInfo (pi_mobile_number	IN 	varchar2
			,po_client_name		OUT 	varchar2
			,po_3mths_average	OUT	number
			,po_rate_plan		OUT	varchar2);
			
Procedure GetPRClient_Account_ID (pi_mobile_number		IN 	varchar2
				 ,po_pr_client_account_id	OUT	varchar2);
			
			
End;
/
Grant execute on client_info to ods_app;

Create or replace package body Client_Info as	

FUNCTION getVersion RETURN VARCHAR2
IS
 BEGIN
	   RETURN version_no;
	END getVersion;


Procedure GetClientInfo (pi_mobile_number	IN 	varchar2
			,po_client_name		OUT 	varchar2
			,po_3mths_average	OUT	number
			,po_rate_plan		OUT	varchar2) is


	
v_client_account_id	number;
	
cursor c_name(pi_client_account_id number) is
	select  ltrim(title ||' '||first_name||' '||last_name)
	from	individual i
		,client_account ca
	where 	ca.client_account_id = pi_client_account_id 
	and	i.individual_id=ca.billing_contact_id;

v_name		varchar2(100);

v_nbr		number(10);	
				
-- Get SUM() of billed amounts for 3 calendar months billed prior to current month
	
cursor c_3m_inv_sum(pi_client_account_id number)  is
	select	sum(bill_amt) 
	from	bill_summary
	where	client_account_id  = pi_client_account_id 
	and	trunc(bill_date) <= add_months(last_day(trunc(sysdate)),- 1)	
	and 	trunc(bill_date) > add_months(last_day(trunc(sysdate)),- 4)	 
	group by client_account_id ;


v_3m_inv_sum		number(22,2);

cursor c_no_of_child_subs(pi_client_account_id number)  is
	select	count(*) from subscription
	where	client_account_id=any
		(select  client_account_id 
		from	client_account
		where ( payment_responsible_ind is null or payment_responsible_ind!='Y')
		start with parent_client_account_id = pi_client_account_id 
		connect by  client_account_id = PRIOR parent_client_account_id
		)
	and  current_status_cd='A';
			
v_no_of_child_subs 	number(5);

v_no_of_own_subs 	number(3);

	
cursor c_rp is
	select rp.english_short_des
	from   subscription s
	,subscription_alias sa
	,rate_plan rp
	where   sa.alias_value=pi_mobile_number	
	and  (sa.expiry_dt is null or trunc(sa.expiry_dt) > trunc(sysdate))
		and	sa.effective_dt =
				(select max(effective_dt)
				 from subscription_alias 
				 where alias_value=pi_mobile_number	
				 and  (sa.expiry_dt is null or trunc(sa.expiry_dt) > trunc(sysdate))) 
	and     s.subscription_id=sa.subscription_id
	and    rp.rate_plan_id=s.current_rate_plan_id;

cursor c_prestige(pi_client_account_id number) is
       select 1 
       from prestige_account
       where  client_account_id =pi_client_account_id;
       
v_prestige_ind	 number(1):=0;	


Begin


If pi_mobile_number is null  Then
	raise InvalidMobileNo;
End If;

GetPRClient_Account_ID(pi_mobile_number,v_client_account_id);

open c_name(v_client_account_id); 
	fetch c_name into v_name;
close c_name;
po_client_name := nvl(v_name,' '); 


open c_rp;
fetch c_rp into po_rate_plan;
close c_rp;

open  c_prestige(v_client_account_id);
fetch c_prestige into v_prestige_ind;
close  c_prestige;

If v_prestige_ind=1 Then
	po_3mths_average := -99999 ;
else
	open c_3m_inv_sum(v_client_account_id);
		fetch c_3m_inv_sum into v_3m_inv_sum;
	close c_3m_inv_sum;
	If v_3m_inv_sum is null Then
		po_3mths_average := -2 ;
	else
		open c_no_of_child_subs(v_client_account_id);
			fetch c_no_of_child_subs into v_no_of_child_subs;
		close 	c_no_of_child_subs;

	select count(*) into v_no_of_own_subs
	from subscription 
	where client_account_id = v_client_account_id
	and  current_status_cd='A';
	
	po_3mths_average := v_3m_inv_sum/(3*(v_no_of_child_subs + v_no_of_own_subs ));

	End If;
End If;
	
Exception
When InvalidMobileNo Then
	Raise_Application_Error(-20503, 'Invalid mobile number');
	po_client_name := ' ';
When  ClientNotFound Then
	po_3mths_average := - 1;
	po_client_name := ' ';
--When InvoicesNotFound Then
--	Raise_Application_Error(-20502, 'Invoices  Not Found ');
When Others Then
	raise; 
End GetClientInfo;

Procedure GetPRClient_Account_ID (pi_mobile_number		IN 	varchar2
				 ,po_pr_client_account_id	OUT	varchar2) is
				 
cursor c_client_pr is
	 select client_account_id
	    from (select client_account_id, client_level_cd
	          from client_account
		 	  where payment_responsible_ind = 'Y'
			  connect by prior parent_client_account_id = client_account_id
				 	 start with client_account_id =
					  		(select client_account_id
							 from	  subscription
							 where  cell_phone_no = pi_mobile_number
							 and	  current_status_cd = 'A')
	 		  )
		where client_level_cd in
			  (select max(client_level_cd)
	  		   from (select client_account_id, client_level_cd
					 from client_account
					 where payment_responsible_ind = 'Y'
					 connect by prior parent_client_account_id = client_account_id
							 start with client_account_id =
						 		  (select client_account_id
								   from	  subscription
								   where  cell_phone_no = pi_mobile_number
								   and	  current_status_cd = 'A')
					 )
			  );
			 
Begin
open c_client_pr;
fetch c_client_pr  into po_pr_client_account_id;
 If c_client_pr%NotFound  Then
	raise ClientNotFound;
 End If;
close c_client_pr; 
Exception
When Others Then
	raise;
End GetPRClient_Account_ID;				 
			
End;


/		