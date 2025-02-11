CREATE OR REPLACE package Fleet_Utility_pkg as
-------------------------------------------------------------------------
-- IMPORTANT NOTE !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
-- Please increment the version_no variable value when you make changes to this package 
-- to reflect version changes.
 ------------------------------------------------------------------------
-- description: Package Fleet_Utility_pkg containing procedures
--		for Fleets and TalkGroups data retrieval from Knowbility database
-- NOTE: Application (Oracle) Error Codes reserved for these package are:
--       -20160 to -20169   ( 10 codes )
--
--
-- Date	   		Developer     	  	  Modifications
-------------------------------------------------------------------------
FleetNotFound	Exception;
PRAGMA							EXCEPTION_INIT(FleetNotFound, -20160);
-- package version constant
   version_no          CONSTANT VARCHAR2(10)       := '3.19.4';
   
     FUNCTION getVersion RETURN VARCHAR2;
     
-- Reference to a Cursor
TYPE RefCursor Is Ref Cursor;
-------------------------------------------------------------------------
-- Get Fleets By Ban
--------------------------------------------------------------------------
Procedure GetFleetsByBan(
		  		  pi_ban				IN	    number
				 ,po_fleet    			OUT     RefCursor) ;
--------------------------------------------------------------------------
-- Get Fleets By Fleet Type
--------------------------------------------------------------------------
Procedure GetFleetsByFleetType(
		  		  pi_fleet_type			IN	    char
				 ,po_fleet    			OUT     RefCursor);
---------------------------------------------------------------------------
-- Get Fleets By FleetIdentity
--------------------------------------------------------------------------
Procedure GetFleetsByFleetIdentity(
		  		  pi_urban_id			IN    	number
				 ,pi_fleet_id          	IN   	number
				 ,po_fleet    			OUT     RefCursor);
---------------------------------------------------------------------------
-- Get Number Attached Subscribers
--------------------------------------------------------------------------
 Procedure GetNumberAttachedSubscribers(
		  		   pi_urban_id		      	IN    	number
				  ,pi_fleet_id          	IN   	number
                  ,pi_ban                   IN      number
				  ,po_nbr_subscribers    	OUT     number);
------------------------------------------------------------------------
-- Get Number Accociated Talk Groups
--------------------------------------------------------------------------
 Procedure GetNumberAccociatedTalkGroups(
                   pi_urban_id		      	IN    	number
				  ,pi_fleet_id          	IN   	number
                  ,pi_ban                   IN      number
                  ,po_nbr_talkgroups    	OUT     number);
---------------------------------------------------------------------------
-- Get Number Associated Accounts
--------------------------------------------------------------------------
 Procedure GetNumberAssociatedAccounts(
		  		   pi_urban_id		      	IN    	number
				  ,pi_fleet_id          	IN   	number
                  ,po_nbr_accounts      	OUT     number);
-------------------------------------------------------------------------
-- Get TalkGroups By Ban
--------------------------------------------------------------------------
Procedure GetTalkGroupsByBan(
		  		  pi_ban				IN	    number
				 ,po_talkgroup 			OUT     RefCursor) ;
-------------------------------------------------------------------------
-- Get TalkGroups By Subscriber
--------------------------------------------------------------------------
Procedure GetTalkGroupsBySubscriber(
		  		  pi_subscriber_no		IN	    varchar2
				 ,po_talkgroup 			OUT     RefCursor) ;
---------------------------------------------------------------------------
-- Get TalkGroups By FleetIdentity
--------------------------------------------------------------------------
Procedure GetTalkGroupsByFleetIdentity(
		  		  pi_urban_id			IN    	number
				 ,pi_fleet_id          	IN   	number
				 ,po_talkgroup 			OUT     RefCursor) ;
---------------------------------------------------------------------------
-- Get Attched Subscriber Count for Talkgroup and Ban
--------------------------------------------------------------------------
 Procedure GetAttachedSubsCountForTgBan(
		  		   pi_urban_id		      	IN    	number
				  ,pi_fleet_id          	IN   	number
				  ,pi_tg_id					IN   	number
                  ,pi_ban                   IN      number
				  ,po_nbr_subscribers    	OUT     number);
End FLEET_UTILITY_PKG;
/
CREATE OR REPLACE Package Body FLEET_UTILITY_PKG As
--################################################################
-- Procedures/Functions
--################################################################
--------------------------------------------------------------------------
 FUNCTION getVersion RETURN VARCHAR2
   IS
   BEGIN
	   RETURN version_no;
	END getVersion;
	
Procedure GetFleetsByBan(
		  		  pi_ban				IN	number
				 ,po_fleet    			OUT     RefCursor)
Is
Begin
	 ------------------------------------------------------------------
	 -- Create reference to a cursor  using SQL statement
	 ------------------------------------------------------------------
  	 open  po_fleet for
     select	bf.ban, bf.urban_id, bf.fleet_id, bf.exp_no_of_sub, bf.exp_no_of_tg, ui.fleet_alias,
            ui.fleet_type, ui.n_dap_id, ui.fleet_class, ui.dap_number,
            (select bf2.ban
              from	ban_fleet bf2
              where	bf.urban_id = bf2.urban_id
              and	bf.fleet_id = bf2.fleet_id
              and	bf2.owner_ind = 'Y'
              and	bf2.exp_date is null) owner,
            fo.txt1 owner_name
     from	ban_fleet bf, uf_inv ui, fleet_owner fo
     where	bf.ban = pi_ban
     and	bf.urban_id = ui.urban_id
     and	bf.fleet_id = ui.fleet_id
     and	ui.owner_seq_no = fo.txt_seq_no(+);
Exception
When NO_DATA_FOUND Then
	If ( po_fleet%ISOPEN ) Then
	   close po_fleet;
	End If;
     -- return NULL cursor
     open po_fleet for
	 	  select NULL from dual where 1=0;
When Others Then
	If ( po_fleet%ISOPEN ) Then
	   close po_fleet;
	End If;
	Raise_Application_Error(-20160, 'Fleet Query Failed. Oracle:(['
    							|| sqlcode || '] [' || sqlerrm  || '])'  );
End GetFleetsByBan;
--------------------------------------------------------------------------
Procedure GetFleetsByFleetType(
		  		  pi_fleet_type			IN	    char
				 ,po_fleet    			OUT     RefCursor)
Is
 Begin
	------------------------------------------------------------------
	-- Create reference to a cursor  using SQL statement
	------------------------------------------------------------------
  	open  po_fleet for
    select	ui.urban_id, ui.fleet_id, ui.fleet_alias, ui.fleet_type, nvl(ui.exp_no_of_ufmi,0), nvl(ui.exp_no_of_tg,0),
            bf.ban, ui.n_dap_id, ui.ufmi_counter, ui.dap_number, fo.txt1
    from 	uf_inv ui, ban_fleet bf, fleet_owner fo
    where	ui.fleet_type = pi_fleet_type
    and		ui.resource_status in ('AI','AR')
    and		bf.urban_id(+) = ui.urban_id
    and		bf.fleet_id(+) = ui.fleet_id
    and		(bf.owner_ind = 'Y' or bf.owner_ind is null)
    and		ui.owner_seq_no = fo.txt_seq_no(+);
Exception
When NO_DATA_FOUND Then
	If ( po_fleet%ISOPEN ) Then
	   close po_fleet;
	End If;
     -- return NULL cursor
     open po_fleet for
	 	  select NULL from dual where 1=0;
When Others Then
	If ( po_fleet%ISOPEN ) Then
	   close po_fleet;
	End If;
	Raise_Application_Error(-20160, 'Fleet Query Failed. Oracle:(['
    							|| sqlcode || '] [' || sqlerrm  || '])'  );
End GetFleetsByFleetType;
-----------------------------------------------------------------------------------------------------------------------------------------
Procedure GetFleetsByFleetIdentity(
		  		  pi_urban_id			IN    	number
				 ,pi_fleet_id          	IN   	number
				 ,po_fleet    			OUT     RefCursor)
Is
 Begin
	 ------------------------------------------------------------------
	 -- Create reference to a cursor  using SQL statement
	 ------------------------------------------------------------------
  	 open  po_fleet for
     select ui.urban_id, ui.fleet_id, ui.fleet_alias, ui.fleet_type, nvl(ui.exp_no_of_ufmi,0), nvl(ui.exp_no_of_tg,0),
            bf.ban, ui.n_dap_id, ui.ufmi_counter, ui.dap_number ,fc.fleet_class, nvl(m.npa,'N'),
            fo.txt1  
     from 	uf_inv ui, ban_fleet bf, fleet_class fc, market_npa_nxx_lr m, fleet_owner fo
     where  ui.urban_id = pi_urban_id
     and    ui.fleet_id = pi_fleet_id
     and	bf.urban_id(+) = ui.urban_id
     and 	bf.fleet_id(+) = ui.fleet_id
     and 	ui.fleet_id >= fc.from_fleet_id
     and 	ui.fleet_id <= fc.to_fleet_id
     and 	m.npa(+) = to_char(ui.urban_id)
     and 	m.nxx(+) = to_char(ui.fleet_id)
     and	ui.owner_seq_no = fo.txt_seq_no(+)
     and 	rownum < 2;
Exception
When NO_DATA_FOUND Then
	If ( po_fleet%ISOPEN ) Then
	   close po_fleet;
	End If;
     -- return NULL cursor
     open po_fleet for
	 	  select NULL from dual where 1=0;
When Others Then
	If ( po_fleet%ISOPEN ) Then
	   close po_fleet;
	End If;
	Raise_Application_Error(-20160, 'Fleet Query Failed. Oracle:(['
    							|| sqlcode || '] [' || sqlerrm  || '])'  );
End GetFleetsByFleetIdentity;
--------------------------------------------------------------------------------------------------------------------------
Procedure GetNumberAttachedSubscribers(
		  		   pi_urban_id		      	IN    	number
				  ,pi_fleet_id          	IN   	number
                  ,pi_ban                   IN      number
				  ,po_nbr_subscribers    	OUT     number)
Is
cursor c_subscribers_counter is
	select count(*)
	from    subscriber_rsource sr
	where sr.fleet_id = pi_fleet_id
	and sr.urban_id = pi_urban_id
	and sr.ban = pi_ban
	and sr.resource_type ='H'
	and sr.resource_status = 'A'
	and sr.resource_seq =
	( select  max(sr2.resource_seq)
	from   subscriber_rsource   sr2
	where  sr2.subscriber_no = sr.subscriber_no
	and   sr2.resource_type = sr.resource_type
	and   sr2.ban = sr.ban ) ;
 Begin
  open c_subscribers_counter ;
  fetch c_subscribers_counter into po_nbr_subscribers;
  close c_subscribers_counter;
Exception
When Others Then
raise;
End GetNumberAttachedSubscribers;
------------------------------------------------------------------------------------------------------------------------
Procedure GetNumberAccociatedTalkGroups(
                   pi_urban_id		      	IN    	number
				  ,pi_fleet_id          	IN   	number
                  ,pi_ban                   IN      number
                  ,po_nbr_talkgroups    	OUT     number)
Is
cursor c_talkgroups_counter is
select count(*)
from ban_tg_matrix btm , logical_date ld
where btm.ban = pi_ban
and btm.urban_id = pi_urban_id
and btm.fleet_id = pi_fleet_id
and trunc(btm.eff_date ) <= trunc(ld.logical_date)
and (trunc(btm.exp_date ) > trunc(ld.logical_date) or btm.exp_date  is null)
and     ld.logical_date_type='O';
 Begin
  open c_talkgroups_counter ;
  fetch c_talkgroups_counter into po_nbr_talkgroups;
  close c_talkgroups_counter;
Exception
When Others Then
raise;
End  GetNumberAccociatedTalkGroups;
--------------------------------------------------------------------------------------------------------------------------
Procedure GetNumberAssociatedAccounts(
		  		   pi_urban_id		      	IN    	number
				  ,pi_fleet_id          	IN   	number
                  ,po_nbr_accounts      	OUT     number)
Is
cursor c_accounts_counter is
select count(*)
from ban_fleet
where urban_id = pi_urban_id
and fleet_id = pi_fleet_id ;
 Begin
  open c_accounts_counter ;
  fetch c_accounts_counter into po_nbr_accounts;
  close c_accounts_counter;
Exception
When Others Then
raise;
End  GetNumberAssociatedAccounts;
--------------------------------------------------------------------------------------------------------------------------
Procedure GetTalkGroupsByBan(
		  		  pi_ban				IN	number
				 ,po_talkgroup    			OUT     RefCursor)
Is
Begin
	 ------------------------------------------------------------------
	 -- Create reference to a cursor  using SQL statement
	 ------------------------------------------------------------------
  	 open  po_talkgroup for
--     select  btg.ban, btg.urban_id, btg.fleet_id, btg.tg_id, ti.tg_alias, ti.priority_id, 0
--     from    ban_tg_matrix btg, tg_inv ti
--     where   btg.ban = pi_ban
--     and   	 btg.urban_id = ti.urban_id
--     and   	 btg.fleet_id = ti.fleet_id
--	 and   	 btg.tg_id = ti.tg_id ;
     select  btg.ban, btg.urban_id, btg.fleet_id, btg.tg_id, ti.tg_alias, ti.priority_id, btm.ban
     from    ban_tg_matrix btg, tg_inv ti, ban_tg_matrix btm, billing_account ba
     where btg.ban = pi_ban
     and   btg.urban_id = ti.urban_id
     and   btg.fleet_id = ti.fleet_id
	 and   btg.tg_id 	= ti.tg_id
	 and   ti.urban_id 	= btm.urban_id
	 and   ti.fleet_id  = btm.fleet_id
	 and   ti.tg_id     = btm.tg_id
	 and   btm.ban      = ba.ban
	 and   ba.ban =
	 	  (select ibtm.ban
	  	   from ban_tg_matrix ibtm
	  	   where ti.urban_id = ibtm.urban_id
		   and   ti.fleet_id = ibtm.fleet_id
	  	   and   ti.tg_id    = ibtm.tg_id
	  	   and   rownum < 2
	  	   and ibtm.SYS_CREATION_DATE =
	 	   	  (select min(iibtm.SYS_CREATION_DATE)
			   from ban_tg_matrix iibtm
			   where ibtm.urban_id = iibtm.urban_id
			   and   ibtm.fleet_id = iibtm.fleet_id
			   and   ibtm.tg_id    = iibtm.tg_id
			  )
	 	  );
Exception
When NO_DATA_FOUND Then
	If ( po_talkgroup%ISOPEN ) Then
	   close po_talkgroup;
	End If;
     -- return NULL cursor
     open po_talkgroup for
	 	  select NULL from dual where 1=0;
When Others Then
	If ( po_talkgroup%ISOPEN ) Then
	   close po_talkgroup;
	End If;
	Raise_Application_Error(-20161, 'TalkGroup Query Failed. Oracle:(['
    							|| sqlcode || '] [' || sqlerrm  || '])'  );
End GetTalkGroupsByBan;
--------------------------------------------------------------------------------------------------------------------------
Procedure GetTalkGroupsBySubscriber(
		  		  pi_subscriber_no			IN	varchar2
				 ,po_talkgroup    			OUT     RefCursor)
Is
Begin
	 ------------------------------------------------------------------
	 -- Create reference to a cursor  using SQL statement
	 ------------------------------------------------------------------
  	 open  po_talkgroup for
--     select  stg.ban, stg.urban_id, stg.fleet_id, stg.tg_id, ti.tg_alias, ti.priority_id, 0
--    from    subscriber_tg_mtrx stg, tg_inv ti
--     where   stg.subscriber_no = pi_subscriber_no
--     and   	 stg.urban_id = ti.urban_id
--     and   	 stg.fleet_id = ti.fleet_id
--	 and   	 stg.tg_id = ti.tg_id ;
     select stm.ban, stm.urban_id, stm.fleet_id, stm.tg_id, ti.tg_alias, ti.priority_id, btm.ban
     from  subscriber_tg_mtrx stm, tg_inv ti, ban_tg_matrix btm, billing_account ba
     where stm.subscriber_no = pi_subscriber_no
     and   stm.urban_id = ti.urban_id
     and   stm.fleet_id = ti.fleet_id
	 and   stm.tg_id 	= ti.tg_id
	 and   ti.urban_id 	= btm.urban_id
	 and   ti.fleet_id  = btm.fleet_id
	 and   ti.tg_id     = btm.tg_id
	 and   btm.ban      = ba.ban
	 and   ba.ban =
	 	  (select ibtm.ban
	  	   from ban_tg_matrix ibtm
	  	   where ti.urban_id = ibtm.urban_id
		   and   ti.fleet_id = ibtm.fleet_id
	  	   and   ti.tg_id    = ibtm.tg_id
	  	   and   rownum < 2
	  	   and ibtm.SYS_CREATION_DATE =
	 	   	  (select min(iibtm.SYS_CREATION_DATE)
			   from ban_tg_matrix iibtm
			   where ibtm.urban_id = iibtm.urban_id
			   and   ibtm.fleet_id = iibtm.fleet_id
			   and   ibtm.tg_id    = iibtm.tg_id
			  )
	 	  );
Exception
When NO_DATA_FOUND Then
	If ( po_talkgroup%ISOPEN ) Then
	   close po_talkgroup;
	End If;
     -- return NULL cursor
     open po_talkgroup for
	 	  select NULL from dual where 1=0;
When Others Then
	If ( po_talkgroup%ISOPEN ) Then
	   close po_talkgroup;
	End If;
	Raise_Application_Error(-20161, 'TalkGroup Query Failed. Oracle:(['
    							|| sqlcode || '] [' || sqlerrm  || '])'  );
End GetTalkGroupsBySubscriber;
--------------------------------------------------------------------------------------------------------------------------
Procedure GetTalkGroupsByFleetIdentity(
		  		  pi_urban_id			IN	number
				 ,pi_fleet_id			IN	number
				 ,po_talkgroup    		OUT     RefCursor)
Is
Begin
	 ------------------------------------------------------------------
	 -- Create reference to a cursor  using SQL statement
	 ------------------------------------------------------------------
  	 open  po_talkgroup for
--     select  ti.tg_id, ti.tg_alias, ti.priority_id, 0
--     from    tg_inv ti
--     where   ti.urban_id = pi_urban_id
--     and   	 ti.fleet_id = pi_fleet_id
--	 and   	 ti.resource_status <> 'AG' ;
	 select ti.tg_id, ti.tg_alias, ti.priority_id, ba.ban
	 from tg_inv ti, ban_tg_matrix btm, billing_account ba
	 where ti.urban_id = pi_urban_id
	 and   ti.fleet_id = pi_fleet_id
	 and   ti.resource_status != 'AG'
	 and   ti.urban_id = btm.urban_id
	 and   ti.fleet_id = btm.fleet_id
	 and   ti.tg_id    = btm.tg_id
	 and   btm.ban     = ba.ban
	 and   ba.ban =
	 	  (select ibtm.ban
	  	   from ban_tg_matrix ibtm
	  	   where ti.urban_id = ibtm.urban_id
		   and   ti.fleet_id = ibtm.fleet_id
	  	   and   ti.tg_id    = ibtm.tg_id
	  	   and   rownum < 2
	  	   and ibtm.SYS_CREATION_DATE =
	 	   	  (select min(iibtm.SYS_CREATION_DATE)
			   from ban_tg_matrix iibtm
			   where ibtm.urban_id = iibtm.urban_id
			   and   ibtm.fleet_id = iibtm.fleet_id
			   and   ibtm.tg_id    = iibtm.tg_id
			  )
	 	  );
Exception
When NO_DATA_FOUND Then
	If ( po_talkgroup%ISOPEN ) Then
	   close po_talkgroup;
	End If;
     -- return NULL cursor
     open po_talkgroup for
	 	  select NULL from dual where 1=0;
When Others Then
	If ( po_talkgroup%ISOPEN ) Then
	   close po_talkgroup;
	End If;
	Raise_Application_Error(-20161, 'TalkGroup Query Failed. Oracle:(['
    							|| sqlcode || '] [' || sqlerrm  || '])'  );
End GetTalkGroupsByFleetIdentity;
--------------------------------------------------------------------------------------------------------------------------
Procedure GetAttachedSubsCountForTgBan(
		  		   pi_urban_id		      	IN    	number
				  ,pi_fleet_id          	IN   	number
				  ,pi_tg_id					IN   	number
                  ,pi_ban                   IN      number
				  ,po_nbr_subscribers    	OUT     number)
Is
  cursor c_subscribers_counter is

  select count(*)
  from   subscriber_tg_mtrx stm, logical_date ld
  where  stm.urban_id = pi_urban_id
  and 	 stm.fleet_id = pi_fleet_id
  and 	 stm.tg_id 	  = pi_tg_id
  and 	 stm.ban 	  = pi_ban
  and 	 trunc(stm.eff_date ) <= trunc(ld.logical_date)
  and 	(trunc(stm.exp_date ) > trunc(ld.logical_date) or stm.exp_date  is null)
  and    ld.logical_date_type='O';

Begin
  open c_subscribers_counter ;
  fetch c_subscribers_counter into po_nbr_subscribers;
  close c_subscribers_counter;
Exception
		 When Others Then
		 Raise_Application_Error(-20161, 'TalkGroup Query Failed. Oracle:(['
    							|| sqlcode || '] [' || sqlerrm  || '])'  );
End GetAttachedSubsCountForTgBan;
--------------------------------------------------------------------------------------------------------------------------
End;
-- FLEET_UTILITY_PKG
/


