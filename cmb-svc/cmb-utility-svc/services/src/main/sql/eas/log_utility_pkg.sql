Create or replace package Log_Utility as
 -------------------------------------------------------------------------
-- IMPORTANT NOTE !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
-- Please increment the version_no variable value when you make changes to this package 
-- to reflect version changes.
 ------------------------------------------------------------------------
-- description: Package Log_Utility  containing procedures 
--		for Application logs
-- 	
--Database  EAS		| Schema: EASADM	
-- Date	   		Developer     	  	  Modifications
-- 01-Nov-2000		Ludmila Pomirche	  created
--  May-11-2012     Naresh Annabathula     Added getVersion function for shakedown
-----------------------------------------------------------------------
ActivationLogNotFound	Exception;
PRAGMA			EXCEPTION_INIT(ActivationLogNotFound, -20400);
-- package version constant
   version_no          CONSTANT VARCHAR2(10)       := '3.19.4';
----------------------------------------------------------------------------
-- description: Procedure LogActivation to log  Activation Info
-- Paramaters : (IN) pi_statusCD - Status of the Activation
--		     pi_clientID - Portal User ID in CRDB
--		     pi_dealerID - Dealer ID
--		     pi_customerID - Customer ID in BSCS
--		     pi_activationData - activation Data in XML format
--		     pi_userID	- user ID
-----------------------------------------------------------------------------

Procedure LogActivation  ( pi_act_log_id		number
			      ,pi_statusCD		varchar2
  			      ,pi_clientID		number
  			      ,pi_dealerID		number
  			      ,pi_customerID		number
  			      ,pi_activationData	varchar2
  			      ,pi_userID		varchar2
			     );
			     

----------------------------------------------------------------------------
-- description: Procedure GetActivationLog to get  Activation log Info
-- Paramaters : (IN)  pi_customerID - Customer ID in BSCS
--		    
--		(OUT) pi_activationData - activation Data in XML format
--
-----------------------------------------------------------------------------

Procedure GetActivationLogbyCustID   (  pi_customerID		IN number
					,pi_offset		IN number default 1
					,pi_amount		IN number default 32000
			       		,pi_statusCD		OUT varchar2
  			       		,pi_activationData	OUT varchar2
			     		);
			     

Procedure GetActivationLogbyActLogID (   pi_actLogID		IN number
					,pi_offset		IN number default 1
					,pi_amount		IN number default 32000
			       		,pi_statusCD		OUT varchar2
  			       		,pi_activationData	OUT varchar2
			     		);	
			     		
FUNCTION getVersion RETURN VARCHAR2;
			     		
Procedure GetActivationLogbyClientID (   pi_ClientID		IN number
					,pi_offset		IN number default 1
					,pi_amount		IN number default 32000
			       		,pi_statusCD		OUT varchar2
  			       		,pi_activationData	OUT varchar2
			     		);				    
End;
/

Create or replace package body Log_Utility  as 	


 FUNCTION getVersion RETURN VARCHAR2
   IS
   BEGIN
	   RETURN version_no;
	END getVersion;
	
Procedure LogActivation  ( pi_act_log_id		number
			      ,pi_statusCD		varchar2
  			      ,pi_clientID		number
  			      ,pi_dealerID		number
  			      ,pi_customerID		number
  			      ,pi_activationData	varchar2
  			      ,pi_userID		varchar2
			     ) is
cursor c_log is
	select rowid 
	from activation_log
	where act_log_id=pi_act_log_id
	for update of status_cd nowait;				     
v_rowid	 	rowid;			

cursor c_log_act is
	select rowid , activation_info
	from activation_log_info
	where act_log_id=pi_act_log_id
	for update of activation_info nowait;				     
v_rowid_act	 	rowid;			
v_locator		CLOB;    
Begin
open c_log;
fetch c_log into v_rowid;
If c_log%NotFound Then
	insert into activation_log
			(act_log_id
			 ,status_CD
			 ,client_ID
			 ,dealer_ID
			 ,customer_ID
			 ,activation_data
			 ,create_date
			 ,create_userid)
		values
			(pi_act_log_id	
			 ,pi_statusCD		
  			 ,pi_clientID		
  			 ,pi_dealerID	
  			 ,pi_customerID	
  			 ,' '   --pi_activationData	
  			 ,sysdate
  			 ,nvl(pi_userID,user));
  	insert into activation_log_info 
  			( act_log_id
  			 ,activation_info
  			 ,create_date
			 ,create_userid)
		values
			(pi_act_log_id	
			 ,empty_clob()
			 ,sysdate
  			 ,nvl(pi_userID,user))
  	returning activation_info into v_locator;
  	dbms_lob.write(v_locator, length(pi_activationData),1,pi_activationData);
Else
	update	activation_log
	set 	status_CD=pi_statusCD
		,client_ID=pi_clientID
		,dealer_ID=pi_dealerID
		,customer_ID=pi_customerID
	--	,activation_Data=pi_activationData
		,create_date=sysdate
		,create_userid=nvl(pi_userID,user)	
	where 	rowid=v_rowid;
	open c_log_act;
	fetch c_log_act into v_rowid_act,v_locator;
	If c_log_act%NotFound Then
		insert into activation_log_info 
  			( act_log_id
  			 ,activation_info
  			 ,create_date
			 ,create_userid)
		values
			(pi_act_log_id	
			 ,empty_clob()
			 ,sysdate
  			 ,nvl(pi_userID,user))
  	returning activation_info into v_locator;
  	dbms_lob.write(v_locator, length(pi_activationData),1,pi_activationData);
  	Else
  	dbms_lob.write(v_locator, length(pi_activationData),1,pi_activationData);
  	update	activation_log_info 
  	set	create_date=sysdate
  		,create_userid=nvl(pi_userID,user)
  	where rowid=v_rowid_act;	
  	End If;
  	close c_log_act;
End If;
  			 
close c_log;
commit;	
Exception
When Others Then
null;
--raise_application_error(-20000,SQLERRM);	     
End LogActivation;


Procedure GetActivationLogbyCustID  (   pi_customerID		IN number
					,pi_offset		IN number default 1
					,pi_amount		IN number default 32000
			       		,pi_statusCD		OUT varchar2
  			       		,pi_activationData	OUT varchar2
			     		) is
			     
cursor c_act is 
		select	al.status_cd, ali.activation_info
		from	activation_log al, activation_log_info ali
		where	al.customer_id=	pi_customerID	
		and 	al.act_log_id=ali.act_log_id
		order 	by al.create_date desc;
				     
v_locator		CLOB;
v_amount		integer:=pi_amount;
v_offset		integer:=pi_offset;
v_activationData	varchar2(32000);
v_status		varchar2(1);	     
Begin
open c_act;
fetch c_act into v_status, v_locator;
If c_act%NotFound Then
	raise ActivationLogNotFound;
Else
dbms_lob.read(v_locator, v_amount, v_offset,v_activationData);
pi_statusCD:=v_status;
pi_activationData:= v_activationData;
End If;
close c_act;
Exception
When ActivationLogNotFound Then
	Raise_Application_Error(-20400,'Activation Log Not Found');
End GetActivationLogbyCustID;


Procedure GetActivationLogbyActLogID (   pi_actLogID		IN number
					,pi_offset		IN number default 1
					,pi_amount		IN number default 32000
			       		,pi_statusCD		OUT varchar2
  			       		,pi_activationData	OUT varchar2
			     	      ) is
			     
cursor c_act is 
		select	al.status_cd, ali.activation_info
		from	activation_log al, activation_log_info ali
		where	al.act_log_id=	pi_actLogID	
		and 	al.act_log_id=ali.act_log_id;
				     
v_locator		CLOB;
v_amount		integer:=pi_amount;
v_offset		integer:=pi_offset;
v_activationData	varchar2(32000);
v_status		varchar2(1);	     
Begin
open c_act;
fetch c_act into v_status, v_locator;
If c_act%NotFound Then
	raise ActivationLogNotFound;
Else
dbms_lob.read(v_locator, v_amount, v_offset,v_activationData);
pi_statusCD:=v_status;
pi_activationData:= v_activationData;
End If;
close c_act;
Exception
When ActivationLogNotFound Then
	Raise_Application_Error(-20400,'Activation Log Not Found');
End GetActivationLogbyActLogID;


Procedure GetActivationLogbyClientID (   pi_ClientID		IN number
					,pi_offset		IN number default 1
					,pi_amount		IN number default 32000
			       		,pi_statusCD		OUT varchar2
  			       		,pi_activationData	OUT varchar2
  			       		) is	
cursor c_act is 
		select	al.status_cd, ali.activation_info
		from	activation_log al, activation_log_info ali
		where	al.client_id =	pi_ClientID	
		and 	al.act_log_id=ali.act_log_id;
				     
v_locator		CLOB;
v_amount		integer:=pi_amount;
v_offset		integer:=pi_offset;
v_activationData	varchar2(32000);
v_status		varchar2(1);	     
Begin
open c_act;
fetch c_act into v_status, v_locator;
If c_act%NotFound Then
	raise ActivationLogNotFound;
Else
dbms_lob.read(v_locator, v_amount, v_offset,v_activationData);
pi_statusCD:=v_status;
pi_activationData:= v_activationData;
End If;
close c_act;
Exception
When ActivationLogNotFound Then
	Raise_Application_Error(-20400,'Activation Log Not Found');
End GetActivationLogbyClientID;			     					    
End;
/

create public synonym Log_Utility for EASADM.Log_Utility;
grant execute on EASADM.Log_Utility to TARGYSSERVERM3;
