CREATE OR REPLACE PACKAGE CONTACT_EVENT_PKG AS
-------------------------------------------------------------------------
-- IMPORTANT NOTE !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
-- Please increment the version_no variable value when you make changes to this package 
-- to reflect version changes.
/******************************************************************************
   NAME:	CONTACT_EVENT_PKG
   PURPOSE:	Offers Application Programming Interfaces
   			for creating and querying contact events.
   NOTE:	Application (Oracle) Error Codes reserved for this package are:
       		   -20160 -20180

   REVISIONS:
   Ver        Date        Author           Description
   ---------  ----------  ---------------  ------------------------------------
   1.0        07/Dec/2004 Carlos Manjarres Created Package for TELUS-API- ContactEvent
   1.1        16/Aug/2005 Carlos Manjarres Updated insert_parameter_value(..) to isolate column sizes
   1.2        25/Aug/2005 Carlos Manjarres Updated internal function to get software ID by name
   1.3	      27/Apr/2006 R. Fong	   Modified insertSMSCE_NotificationReq
   1.4	      02/Aug/2006 R. Fong	   Modified get_sw_application_id_byname
   1.5	      26/Jun/2011 Brandon Wen  Added optional parameter to insertSMSCE_NotificationReq()
   1.6	      06/Jul/2011 Brandon Wen  Updated pi_content_params type to T_CONTACT_PARAMETER_ARRAY
   1.7	      20/Jul/2011 Brandon Wen  Add constraint check before insert_parameter_values
   1.8        May-11-2012     Naresh Annabathula     Added getVersion function for shakedown
   3.22.1     Dec-18-2012 Tsz Chung Tong  Changed try_chnl_outlet_salesrep parameter to refer to the proper %TYPE
   3.22.2     24/Sep/2013 Mahan Razagh  Added new procedure insertReplied_RawInboundSMS() 
******************************************************************************/

------------------------------------------------------------------
-- Exceptions
------------------------------------------------------------------

Contact_Event_DB_Exception	Exception;
PRAGMA				EXCEPTION_INIT(Contact_Event_DB_Exception, -20160);

------------------------------------------------------------------
-- User Defined Types
------------------------------------------------------------------

-- Reference to a Cursor
TYPE RefCursor Is Ref Cursor;

------------------------------------------------------------------
-- Contants
------------------------------------------------------------------

PACKAGE_ID                    VARCHAR2(20) := 'CONTACT_EVENT_PKG';
CE_TYPE_ID_NOTIFICATION 	  NUMBER := 1;
-- package version constant
   version_no          CONSTANT VARCHAR2(10)       := '3.22.2';

------------------------------------------------------------------
-- Insert SMS Contact Events and Notification Request records.
------------------------------------------------------------------
 FUNCTION getVersion RETURN VARCHAR2;
 
Procedure insertSMSCE_NotificationReq
(
	 pi_contact_content_type_id	 		in number
	,pi_kb_ban							in number
	,pi_kb_subscriber_no		 		in varchar2
	,pi_kb_product_type			 		in varchar2
	,pi_language_cd				 		in varchar2
	,pi_destination_phone_no	 		in varchar
	,pi_billable_indicator		 		in varchar2
	,pi_originating_user_name	 		in varchar2
	,pi_scheduled_delivery_datetime		in date
	,pi_time_to_live			 		in number
	,pi_priority_number			 		in number
	,pi_delivery_receipt_req_ind		in varchar2
	,pi_message_text			 		in varchar2
	-- to populate  CONTACT_EVNT_CONTENT_PARAM
	,pi_content_params			 		in T_CONTACT_PARAMETER_ARRAY
	,pi_application		 		 		in varchar2
	,pi_user							in varchar2
	-- to populate  CONTACT_EVENT_TEAM_MEMBER
	,pi_team_member_id					in number
	,pi_contact_event_type_id			in number default CE_TYPE_ID_NOTIFICATION
);


------------------------------------------------------------------
-- Insert Subscriber Authentication Contact Events
------------------------------------------------------------------
Procedure insertSubAuthenticationCE
(
 	 pi_subscription_id          in number
	,pi_authentication_succeeded in varchar2
 	,pi_chnl_org_id				 in varchar2
	,pi_outlet_id				 in varchar2
	,pi_sales_rep_id			 in varchar2
	,pi_application		 		 in varchar2
	,pi_user					 in varchar2
);


------------------------------------------------------------------
-- Insert Account Authentication Contact Events
------------------------------------------------------------------
Procedure insertAccAuthenticationCE
(
 	 pi_account_id          	 in number
	,pi_authentication_succeeded in varchar2
 	,pi_chnl_org_id				 in varchar2
	,pi_outlet_id				 in varchar2
	,pi_sales_rep_id			 in varchar2
	,pi_application		 		 in varchar2
	,pi_user					 in varchar2
);


------------------------------------------------------------------
-- Check if given SMS contact event already exists in the database.
------------------------------------------------------------------
Function assertSMSClientNotification (
	 pi_kb_ban 				         SMS_CLIENT_NOTIFICATION.kb_ban%type
	,pi_kb_subscriber_no 			 SMS_CLIENT_NOTIFICATION.kb_subscriber_no%type
	,pi_contact_content_type_id 	 CONTACT_EVENT.contact_content_type_id%type
	,pi_scheduled_delivery_datetime  SMS_CLIENT_NOTIFICATION.scheduled_delivery_datetime%type
)
Return boolean;

------------------------------------------------------------------
-- Insert replied inbound raw SMS content records
------------------------------------------------------------------
Procedure insertReplied_RawInboundSMS
(
     pi_contact_content_type_id             in number
    ,pi_subscription_id                          in number
     -- to populate  CONTACT_EVNT_CONTENT_PARAM
    ,pi_content_params                        in T_CONTACT_PARAMETER_ARRAY
    ,pi_application                               in varchar2
    ,pi_user                                        in varchar2
    ,pi_contact_event_type_id               in number default CE_TYPE_ID_NOTIFICATION
);

End CONTACT_EVENT_PKG;
/


CREATE OR REPLACE PACKAGE BODY CONTACT_EVENT_PKG AS

/***************************************************************************************
   REVISIONS:
   Ver        Date        Author           Description
   ---------  ----------  ---------------  ------------------------------------
   1.0        07/Dec/2004 Carlos Manjarres Created Package for TELUS-API- ContactEvent
   1.1        16/Aug/2005 Carlos Manjarres Updated insert_parameter_value(..) to isolate column sizes
   1.2        25/Aug/2005 Carlos Manjarres Updated get_sw_application_id_byname(..)
   1.3	      27/Apr/2006 R. Fong	   	   Modified insertSMSCE_NotificationReq and added insert_ContactEventTeamMember
   1.4        24/Sep/2013 Mahan Razagh  Added new procedure insertReplied_RawInboundSMS() 
***************************************************************************************/

--################################################################
-- Private Constants
--################################################################


-- SMS Notification
CE_MECHANISM_ID_SMS 					NUMBER := 1;
SMS_STATUS_TYPE_CD_INITIAL 				VARCHAR2(20) := 'INITIAL';
SMS_GATEWAY_MESSAGE_ID_UNKNOWN 			NUMBER := 0;

-- Client Authentication
CE_TYPE_ID_SUBSCRIBER_AUTHENT           NUMBER := 7;
CE_TYPE_ID_ACCOUNT_AUTHENT     	        NUMBER := 8;
CE_MECHANISM_ID_WEBPORTAL     			NUMBER := 2;

-- Common
CE_STATUS_CODE_SCHEDULED				VARCHAR2(1) := 'S';
CE_STATUS_CODE_COMPLETED				VARCHAR2(1) := 'C';
CE_STATUS_CODE_FAILED					VARCHAR2(1) := 'F';

MAX_CONTENT_PARAMS_SIZE		  			NUMBER 	:= 2000;

--################################################################
-- Private Procedures/Functions
--################################################################

-- getVesrion function to return version value
 FUNCTION getVersion RETURN VARCHAR2
   IS
   BEGIN
	   RETURN version_no;
	END getVersion;
--
-- These are re-usable INSERT statements for other applications.
-- NOTE:  These statements does not COMMIT
--


------------------------------------------------------------------
-- Insert CONTACT_EVENT record
------------------------------------------------------------------

Procedure insert_ContactEvent
(
			 pi_contact_event_id               in contact_event.contact_event_id%type,
			 pi_contact_event_type_id 		   in contact_event.contact_event_type_id%type,
			 pi_start_timestamp 			   in contact_event.start_timestamp%type,
			 pi_end_timestamp 				   in contact_event.end_timestamp%type,
			 pi_contact_event_status_code 	   in contact_event.contact_event_status_code%type,
			 pi_status_timestamp 			   in contact_event.status_timestamp%type,
			 pi_comments 					   in contact_event.comments%type,
			 pi_contact_mechanism_id 		   in contact_event.contact_mechanism_id%type,
			 pi_contact_content_type_id 	   in contact_event.contact_content_type_id%type,
			 pi_load_dt 					   in contact_event.load_dt%type,
			 pi_update_dt 					   in contact_event.update_dt%type,
			 pi_user_last_modify 			   in contact_event.user_last_modify%type,
			 pi_status_confirmed_ind 		   in contact_event.status_confirmed_ind%type,
			 pi_processing_sw_applicationid	   in contact_event.processing_sw_application_id%type,
			 pi_associated_contact_event_id    in contact_event.associated_contact_event_id%type
)
As

Begin
	 insert into CONTACT_EVENT
            (contact_event_id,
			 contact_event_type_id,
			 start_timestamp,
             end_timestamp,
			 contact_event_status_code,
             status_timestamp,
			 comments,
			 contact_mechanism_id,
             contact_content_type_id,
			 load_dt,
			 update_dt,
             user_last_modify,
             status_confirmed_ind,
			 processing_sw_application_id,
             associated_contact_event_id
            )
     values (
			 pi_contact_event_id,
			 pi_contact_event_type_id,
             pi_start_timestamp,
             pi_end_timestamp,
			 pi_contact_event_status_code,
             pi_status_timestamp,
			 pi_comments,
			 pi_contact_mechanism_id,
             pi_contact_content_type_id,
			 pi_load_dt,
			 pi_update_dt,
             pi_user_last_modify,
             pi_status_confirmed_ind,
			 pi_processing_sw_applicationid,
             pi_associated_contact_event_id
            );
Exception
   When others then
	  Raise_Application_Error(-20160, 'Failed inserting CONTACT_EVENT record. Oracle:(['
    							|| sqlcode || '] [' || sqlerrm  || '])'
	 							|| ' ['
								|| ',application_id=' || pi_processing_sw_applicationid
								|| ',user_id=' || pi_user_last_modify
								|| ']'
								);
End insert_ContactEvent;


------------------------------------------------------------------
-- Insert CLIENT_ACCOUNT_CONTACT_EVENT record
------------------------------------------------------------------

Procedure insert_ClientAccountCE
(
			 pi_contact_event_id 			   in client_account_contact_event.contact_event_id%type,
			 pi_contact_event_type_id 		   in client_account_contact_event.contact_event_type_id%type,
			 pi_client_account_id 			   in client_account_contact_event.client_account_id%type,
			 pi_contact_event_status_code 	   in client_account_contact_event.contact_event_status_code%type,
			 pi_status_timestamp 			   in client_account_contact_event.status_timestamp%type,
			 pi_status_confirmed_ind 		   in client_account_contact_event.status_confirmed_ind%type,
			 pi_language_cd 				   in client_account_contact_event.language_cd%type,
			 pi_load_dt 					   in client_account_contact_event.load_dt%type,
			 pi_update_dt 					   in client_account_contact_event.update_dt%type,
			 pi_user_last_modify 			   in client_account_contact_event.user_last_modify%type
)
As

Begin
	 insert into CLIENT_ACCOUNT_CONTACT_EVENT
	 		(
			 contact_event_id,
			 contact_event_type_id,
			 client_account_id,
			 contact_event_status_code,
			 status_timestamp,
			 status_confirmed_ind,
			 language_cd,
			 load_dt,
			 update_dt,
			 user_last_modify
            )
     values (
			 pi_contact_event_id,
			 pi_contact_event_type_id,
			 pi_client_account_id,
			 pi_contact_event_status_code,
			 pi_status_timestamp,
			 pi_status_confirmed_ind,
			 pi_language_cd,
			 pi_load_dt,
			 pi_update_dt,
			 pi_user_last_modify
            );

Exception
   When others then
	  Raise_Application_Error(-20160, 'Failed inserting CLIENT_ACCOUNT_CONTACT_EVENT record. Oracle:(['
    							|| sqlcode || '] [' || sqlerrm  || '])'
	 							|| ' ['
								|| ',client_account_id=' || pi_client_account_id
								|| ',user_id=' || pi_user_last_modify
								|| ']'
								);


End insert_ClientAccountCE;


------------------------------------------------------------------
-- Insert SUBSCRIBER_CONTACT_EVENT record
------------------------------------------------------------------

Procedure insert_SubscriberCE
(
			 pi_contact_event_id 			   in subscriber_contact_event.contact_event_id%type,
			 pi_contact_event_type_id 		   in subscriber_contact_event.contact_event_type_id%type,
			 pi_subscription_id 			   in subscriber_contact_event.subscription_id%type,
			 pi_contact_event_status_code 	   in subscriber_contact_event.contact_event_status_code%type,
			 pi_status_timestamp 			   in subscriber_contact_event.status_timestamp%type,
			 pi_status_confirmed_ind 		   in subscriber_contact_event.status_confirmed_ind%type,
			 pi_language_cd 				   in subscriber_contact_event.language_cd%type,
			 pi_load_dt 					   in subscriber_contact_event.load_dt%type,
			 pi_update_dt 					   in subscriber_contact_event.update_dt%type,
			 pi_user_last_modify 			   in subscriber_contact_event.user_last_modify%type
)
As

Begin
	 insert into SUBSCRIBER_CONTACT_EVENT
	 		(
			 contact_event_id,
			 contact_event_type_id,
			 subscription_id,
			 contact_event_status_code,
			 status_timestamp,
			 status_confirmed_ind,
			 language_cd,
			 load_dt,
			 update_dt,
			 user_last_modify
            )
     values (
			 pi_contact_event_id,
			 pi_contact_event_type_id,
			 pi_subscription_id,
			 pi_contact_event_status_code,
			 pi_status_timestamp,
			 pi_status_confirmed_ind,
			 pi_language_cd,
			 pi_load_dt,
			 pi_update_dt,
			 pi_user_last_modify
            );

Exception
   When others then
	  Raise_Application_Error(-20160, 'Failed inserting SUBSCRIBER_CONTACT_EVENT record. Oracle:(['
    							|| sqlcode || '] [' || sqlerrm  || '])'
	 							|| ' ['
								|| ',subscription_id=' || pi_subscription_id
								|| ',user_id=' || pi_user_last_modify
								|| ']'
								);

End insert_SubscriberCE;


------------------------------------------------------------------
-- Insert CONTACT_EVENT_CHANNEL_ORG record
------------------------------------------------------------------

Procedure insert_ContactEventChannelOrg
(
			 pi_contact_event_id 			   in contact_event_channel_org.contact_event_id%type,
			 pi_contact_event_type_id 		   in contact_event_channel_org.contact_event_type_id%type,
			 pi_chnl_org_id 				   in contact_event_channel_org.chnl_org_id%type,
			 pi_load_dt 					   in contact_event_channel_org.load_dt%type,
			 pi_update_dt 					   in contact_event_channel_org.update_dt%type,
			 pi_user_last_modify 			   in contact_event_channel_org.user_last_modify%type
)
As

Begin
	 insert into CONTACT_EVENT_CHANNEL_ORG
	 		(
			 contact_event_id,
			 contact_event_type_id,
			 chnl_org_id,
			 load_dt,
			 update_dt,
			 user_last_modify
            )
     values (
			 pi_contact_event_id,
			 pi_contact_event_type_id,
			 pi_chnl_org_id,
			 pi_load_dt,
			 pi_update_dt,
			 pi_user_last_modify
            );

Exception
   When others then
	  Raise_Application_Error(-20160, 'Failed inserting CONTACT_EVENT_CHANNEL_ORG record. Oracle:(['
    							|| sqlcode || '] [' || sqlerrm  || '])'
	 							|| ' ['
								|| ',chnl_org_id=' || pi_chnl_org_id
								|| ',user_id=' || pi_user_last_modify
								|| ']'
								);

End insert_ContactEventChannelOrg;


------------------------------------------------------------------
-- Insert CONTACT_EVENT_OUTLET record
------------------------------------------------------------------

Procedure insert_ContactEventOutlet
(
			 pi_contact_event_id 			   in contact_event_outlet.contact_event_id%type,
			 pi_contact_event_type_id 		   in contact_event_outlet.contact_event_type_id%type,
			 pi_outlet_id 					   in contact_event_outlet.outlet_id%type,
			 pi_load_dt 					   in contact_event_outlet.load_dt%type,
			 pi_update_dt 					   in contact_event_outlet.update_dt%type,
			 pi_user_last_modify 			   in contact_event_outlet.user_last_modify%type
)
As

Begin
	 insert into CONTACT_EVENT_OUTLET
	 		(
			 contact_event_id,
			 contact_event_type_id,
			 outlet_id,
			 load_dt,
			 update_dt,
			 user_last_modify
            )
     values (
			 pi_contact_event_id,
			 pi_contact_event_type_id,
			 pi_outlet_id,
			 pi_load_dt,
			 pi_update_dt,
			 pi_user_last_modify
            );

Exception
   When others then
	  Raise_Application_Error(-20160, 'Failed inserting CONTACT_EVENT_OUTLET record. Oracle:(['
    							|| sqlcode || '] [' || sqlerrm  || '])'
	 							|| ' ['
								|| ',outlet_id=' || pi_outlet_id
								|| ',user_id=' || pi_user_last_modify
								|| ']'
								);

End insert_ContactEventOutlet;


------------------------------------------------------------------
-- Insert CONTACT_EVENT_SALES_REP record
------------------------------------------------------------------

Procedure insert_ContactEventSalesRep
(
			 pi_contact_event_id 			   in contact_event_sales_rep.contact_event_id%type,
			 pi_contact_event_type_id 		   in contact_event_sales_rep.contact_event_type_id%type,
			 pi_sales_rep_id 				   in contact_event_sales_rep.sales_rep_id%type,
			 pi_load_dt 					   in contact_event_sales_rep.load_dt%type,
			 pi_update_dt 					   in contact_event_sales_rep.update_dt%type,
			 pi_user_last_modify 			   in contact_event_sales_rep.user_last_modify%type
)
As

Begin
	 insert into CONTACT_EVENT_SALES_REP
	 		(
			 contact_event_id,
			 contact_event_type_id,
			 sales_rep_id,
			 load_dt,
			 update_dt,
			 user_last_modify
            )
     values (
			 pi_contact_event_id,
			 pi_contact_event_type_id,
			 pi_sales_rep_id,
			 pi_load_dt,
			 pi_update_dt,
			 pi_user_last_modify
            );

Exception
   When others then
	  Raise_Application_Error(-20160, 'Failed inserting CONTACT_EVENT_SALES_REP record. Oracle:(['
    							|| sqlcode || '] [' || sqlerrm  || '])'
	 							|| ' ['
								|| ',sales_rep_id=' || pi_sales_rep_id
								|| ',user_id=' || pi_user_last_modify
								|| ']'
								);

End insert_ContactEventSalesRep;


------------------------------------------------------------------
-- select SOFTWARE_APPLICTION id by name
------------------------------------------------------------------
Function get_sw_application_id_byname(pi_name varchar2 )
Return number
As
  	-- local variables v_xxxxx
  	 v_id number :=0 ;

Begin
	 select software_application_id
	 		into v_id
	 		from SOFTWARE_APPLICATION where lower(name) = lower(pi_name)
			and rownum <=1;
	 return v_id;

Exception
   When no_data_found then
	  Raise_Application_Error(-20160, 'Failed retrieving software_application_id by NAME. Oracle:(['
    							|| sqlcode || '] [' || sqlerrm  || '])'
	 							|| ' ['
								|| ',name=' || pi_name
								|| ']'
								);

End get_sw_application_id_byname;


-- *** SMS Notification Details
------------------------------------------------------------------
-- Insert SUBSCRIBER_CONTACT_EVENT_OLD record
------------------------------------------------------------------

Procedure insert_SubscriberCEOld
(
			 pi_contact_event_id 			   in subscriber_contact_event_old.contact_event_id%type,
			 pi_contact_event_type_id 		   in subscriber_contact_event_old.contact_event_type_id%type,
			 pi_kb_subscriber_no 			   in subscriber_contact_event_old.kb_subscriber_no%type,
			 pi_kb_product_type 			   in subscriber_contact_event_old.kb_product_type%type,
			 pi_kb_ban 						   in subscriber_contact_event_old.kb_ban%type,
			 pi_contact_event_status_code 	   in subscriber_contact_event_old.contact_event_status_code%type,
			 pi_status_timestamp 			   in subscriber_contact_event_old.status_timestamp%type,
			 pi_subscription_id 			   in subscriber_contact_event_old.subscription_id%type,
			 pi_load_dt 					   in subscriber_contact_event_old.load_dt%type,
			 pi_update_dt 					   in subscriber_contact_event_old.update_dt%type,
			 pi_user_last_modify 			   in subscriber_contact_event_old.user_last_modify%type,
			 pi_status_confirmed_ind 		   in subscriber_contact_event_old.status_confirmed_ind%type,
			 pi_language_cd 				   in subscriber_contact_event_old.language_cd%type
)
As

Begin
	 insert into SUBSCRIBER_CONTACT_EVENT_OLD
	 		(
			 contact_event_id,
			 contact_event_type_id,
			 kb_subscriber_no,
			 kb_product_type,
			 kb_ban,
			 contact_event_status_code,
			 status_timestamp,
			 subscription_id,
			 load_dt,
			 update_dt,
			 user_last_modify,
			 status_confirmed_ind,
			 language_cd
            )
     values (
			 pi_contact_event_id,
			 pi_contact_event_type_id,
			 pi_kb_subscriber_no,
			 pi_kb_product_type,
			 pi_kb_ban,
			 pi_contact_event_status_code,
			 pi_status_timestamp,
			 pi_subscription_id,
			 pi_load_dt,
			 pi_update_dt,
			 pi_user_last_modify,
			 pi_status_confirmed_ind,
			 pi_language_cd
            );
Exception
   When others then
	  Raise_Application_Error(-20160, 'Failed inserting SUBSCRIBER_CONTACT_EVENT_OLD record. Oracle:(['
    							|| sqlcode || '] [' || sqlerrm  || '])'
	 							|| ' ['
								|| ',kb_subscriber_no=' || pi_kb_subscriber_no
								|| ',kb_ban=' || pi_kb_ban
								|| ',user_id=' || pi_user_last_modify
								|| ']'
								);


End insert_SubscriberCEOld;


------------------------------------------------------------------
--  Common routine shared by Insert Account/Subscriober Authentication Contact Events
--  to optinaly insert chanel_org_id, outlet_id or sales_rep_id
------------------------------------------------------------------
procedure try_chnl_outlet_salesrep
(
 	 pi_sequence          	 	 in number
	,pi_contact_event_type_id	 in number
 	,pi_chnl_org_id				 in contact_event_channel_org.chnl_org_id%type
	,pi_outlet_id				 in contact_event_outlet.outlet_id%type
	,pi_sales_rep_id			 in contact_event_sales_rep.sales_rep_id%type
	,pi_user					 in varchar2
)
Is
Begin
	  if ( pi_chnl_org_id is not null)
	  then
		  insert_ContactEventChannelOrg
		  (
				 pi_sequence,
				 pi_contact_event_type_id,
				 pi_chnl_org_id,
				 sysdate,	   		-- load_dt
				 sysdate,			-- update_dt
				 pi_user
		  );
	  end if;

	  if ( pi_outlet_id is not null)
	  then
		  insert_ContactEventOutlet
		  (
				 pi_sequence,
				 pi_contact_event_type_id,
				 pi_outlet_id,
				 sysdate,	   		-- load_dt
				 sysdate,			-- update_dt
				 pi_user
		  );
	  end if;

	  if ( pi_sales_rep_id is not null)
	  then
		  insert_ContactEventSalesRep
		  (
				 pi_sequence,
				 pi_contact_event_type_id,
				 pi_sales_rep_id,
				 sysdate,	   		-- load_dt
				 sysdate,			-- update_dt
				 pi_user
		  );
	  end if;

End try_chnl_outlet_salesrep;


------------------------------------------------------------------
-- Insert SMS_CLIENT_NOTIFICATION record
------------------------------------------------------------------

Procedure insert_SMSClientNotification
(
			 pi_contact_event_id 			   in sms_client_notification.contact_event_id%type,
			 pi_contact_event_type_id 		   in sms_client_notification.contact_event_type_id%type,
			 pi_kb_subscriber_no 			   in sms_client_notification.kb_subscriber_no%type,
			 pi_kb_product_type 			   in sms_client_notification.kb_product_type%type,
			 pi_kb_ban 						   in sms_client_notification.kb_ban%type,
			 pi_scheduled_delivery_datetime    in sms_client_notification.scheduled_delivery_datetime%type,
			 pi_priority_number 			   in sms_client_notification.priority_number%type,
			 pi_time_to_live 				   in sms_client_notification.time_to_live%type,
			 pi_billable_indicator 			   in sms_client_notification.billable_indicator%type,
			 pi_originating_user_name 		   in sms_client_notification.originating_user_name%type,
			 pi_message_text 				   in sms_client_notification.message_text%type,
			 pi_sms_gateway_message_id 		   in sms_client_notification.sms_gateway_message_id%type,
			 pi_sms_status_type_cd 			   in sms_client_notification.sms_status_type_cd%type,
			 pi_status_reason_code 			   in sms_client_notification.status_reason_code%type,
			 pi_load_dt 					   in sms_client_notification.load_dt%type,
			 pi_update_dt 					   in sms_client_notification.update_dt%type,
			 pi_user_last_modify 			   in sms_client_notification.user_last_modify%type,
			 pi_destination_phone_no 		   in sms_client_notification.destination_phone_no%type,
			 pi_delivery_receipt_req_ind 	   in sms_client_notification.delivery_receipt_req_ind%type
)
As

Begin
	 insert into SMS_CLIENT_NOTIFICATION
	 		(
			 contact_event_id,
			 contact_event_type_id,
			 kb_subscriber_no,
			 kb_product_type,
			 kb_ban,
			 scheduled_delivery_datetime,
			 priority_number,
			 time_to_live,
			 billable_indicator,
			 originating_user_name,
			 message_text,
			 sms_gateway_message_id,
			 sms_status_type_cd,
			 status_reason_code,
			 load_dt,
			 update_dt,
			 user_last_modify,
			 destination_phone_no,
			 delivery_receipt_req_ind
            )
     values (
			 pi_contact_event_id,
			 pi_contact_event_type_id,
			 pi_kb_subscriber_no,
			 pi_kb_product_type,
			 pi_kb_ban,
			 pi_scheduled_delivery_datetime,
			 pi_priority_number,
			 pi_time_to_live,
			 pi_billable_indicator,
			 pi_originating_user_name,
			 pi_message_text,
			 pi_sms_gateway_message_id,
			 pi_sms_status_type_cd,
			 pi_status_reason_code,
			 pi_load_dt,
			 pi_update_dt,
			 pi_user_last_modify,
			 pi_destination_phone_no,
			 pi_delivery_receipt_req_ind
            );

Exception
   When others then
	  Raise_Application_Error(-20160, 'Failed inserting SMS_CLIENT_NOTIFICATION record. Oracle:(['
    							|| sqlcode || '] [' || sqlerrm  || '])'
	 							|| ' ['
								|| ',destination_phone_no=' || pi_destination_phone_no
								|| ',user_id=' || pi_user_last_modify
								|| ']'
								);

End insert_SMSClientNotification;


------------------------------------------------------------------
-- Insert CONTACT_EVNT_CONTENT_PARAM record
------------------------------------------------------------------

Procedure insert_ContactEvntContentParam
(
			 pi_contact_event_id 			   in contact_evnt_content_param.contact_event_id%type,
			 pi_contact_event_type_id 		   in contact_evnt_content_param.contact_event_type_id%type,
			 pi_contact_content_type_id 	   in contact_evnt_content_param.contact_content_type_id%type,
			 pi_contact_content_param_no 	   in contact_evnt_content_param.contact_content_param_no%type,
			 pi_parameter_value 			   in contact_evnt_content_param.parameter_value%type,
			 pi_load_dt 					   in contact_evnt_content_param.load_dt%type,
			 pi_update_dt 					   in contact_evnt_content_param.update_dt%type,
			 pi_user_last_modify 			   in contact_evnt_content_param.user_last_modify%type
)
As

Begin
	 insert into CONTACT_EVNT_CONTENT_PARAM
	 		(
			 contact_event_id,
			 contact_event_type_id,
			 contact_content_type_id,
			 contact_content_param_no,
			 parameter_value,
			 load_dt,
			 update_dt,
			 user_last_modify
            )
     values (
			 pi_contact_event_id,
			 pi_contact_event_type_id,
			 pi_contact_content_type_id,
			 pi_contact_content_param_no,
			 pi_parameter_value,
			 pi_load_dt,
			 pi_update_dt,
			 pi_user_last_modify
            );

Exception
   When others then
	  Raise_Application_Error(-20160, 'Failed inserting CONTACT_EVNT_CONTENT_PARAM record. Oracle:(['
    							|| sqlcode || '] [' || sqlerrm  || '])'
	 							|| ' ['
								|| ',contact_content_param_no=' || pi_contact_content_param_no
								|| ',parameter_value=' || pi_parameter_value
								|| ',user_id=' || pi_user_last_modify
								|| ']'
								);

End insert_ContactEvntContentParam;


------------------------------------------------------------------
-- Insert CONTACT_EVENT_TEAM_MEMBER record
------------------------------------------------------------------

Procedure insert_ContactEventTeamMember
(
	pi_contact_event_id 			in contact_event_team_member.contact_event_id%type,
	pi_contact_event_type_id 		in contact_event_team_member.contact_event_type_id%type,
	pi_team_member_id 				in contact_event_team_member.team_member_id%type,
	pi_load_dt 						in contact_event_team_member.load_dt%type,
	pi_update_dt 					in contact_event_team_member.update_dt%type,
	pi_user_last_modify 			in contact_event_team_member.user_last_modify%type
)
As

Begin
	insert into CONTACT_EVENT_TEAM_MEMBER 
		(
		 contact_event_id,
		 contact_event_type_id,
		 team_member_id,
		 load_dt,
		 update_dt,
		 user_last_modify
		)
	values	(
		 pi_contact_event_id,
		 pi_contact_event_type_id,
		 pi_team_member_id,
		 pi_load_dt,
		 pi_update_dt,
		 pi_user_last_modify
		);
		
Exception
   When others then
	  Raise_Application_Error(-20160, 'Failed inserting CONTACT_EVENT_TEAM_MEMBER record. Oracle:(['
    							|| sqlcode || '] [' || sqlerrm  || '])'
	 							|| ' [' 
								|| ',team_member_id=' || pi_team_member_id
								|| ',user_id=' || pi_user_last_modify
								|| ']'
								);
			
End insert_ContactEventTeamMember;


--################################################################
-- Public Procedures/Functions
--################################################################


------------------------------------------------------------------
-- Insert Subscriber Authentication Contact Events
------------------------------------------------------------------
procedure insertSubAuthenticationCE
(
 	 pi_subscription_id          in number
	,pi_authentication_succeeded in varchar2
 	,pi_chnl_org_id				 in varchar2
	,pi_outlet_id				 in varchar2
	,pi_sales_rep_id			 in varchar2
	,pi_application		 		 in varchar2
	,pi_user					 in varchar2
)
Is
  	  -- local variables v_xxxxx
  	  v_sequence 	   number :=0 ;
	  v_status_code	   varchar2(1) :='?';
	  v_application_id number :=0;

Begin

	  -- get next sequence number into v_sequence
	  select CONTACT_EVENT_SEQ.nextval into v_sequence from dual;

	  -- map (Y--> C), (N-->F)
	  if ( pi_authentication_succeeded = 'Y' ) then
	  	 v_status_code := CE_STATUS_CODE_COMPLETED;
	  else
	  	 v_status_code := CE_STATUS_CODE_FAILED;
	  end if;

	  -- lookup applicationID using application name
	  v_application_id := get_sw_application_id_byname(pi_application);

	  insert_ContactEvent
	  (
			 v_sequence,
			 CE_TYPE_ID_SUBSCRIBER_AUTHENT,
			 sysdate,
			 null,
			 v_status_code,
			 sysdate,
			 'SubscriberClientAuthentication',  -- I just made it
			 CE_MECHANISM_ID_WEBPORTAL,
			 null,
			 sysdate,
			 sysdate,
			 pi_user,
			 'N',
			 v_application_id,
			 null
	  );

	  insert_SubscriberCE
	  (
			 v_sequence,
			 CE_TYPE_ID_SUBSCRIBER_AUTHENT,
			 pi_subscription_id,
			 v_status_code,
			 sysdate,	   		-- status_timestamp
			 'N', 		   		-- status_confirmed_inf
			 null,
			 sysdate,	   		-- load_dt
			 sysdate,			-- update_dt
			 pi_user
	  );

	  try_chnl_outlet_salesrep
	  (
 	    v_sequence
	   ,CE_TYPE_ID_SUBSCRIBER_AUTHENT
 	   ,pi_chnl_org_id
	   ,pi_outlet_id
	   ,pi_sales_rep_id
	   ,pi_user
	);

	commit;

Exception
   When others then
      Rollback;
	  Raise_Application_Error(-20160, 'Failed while inserting Subscriber Authentication Contact Event. Oracle:(['
    							|| sqlcode || '] [' || sqlerrm  || '])'
	 							|| ' [subscription_id=' || pi_subscription_id
								|| ',succeeded=' || pi_authentication_succeeded
								|| ',chnl_org_id=' || pi_chnl_org_id
								|| ',outlet_id=' || pi_outlet_id
								|| ',sales_rep_id=' || pi_sales_rep_id
								|| ',application_id=' || pi_application
								|| ',user_id=' || pi_user
								|| ']'
								);

End insertSubAuthenticationCE;


------------------------------------------------------------------
-- Insert Account Authentication Contact Events
------------------------------------------------------------------
procedure insertAccAuthenticationCE
(
 	 pi_account_id          	 in number
	,pi_authentication_succeeded in varchar2
 	,pi_chnl_org_id				 in varchar2
	,pi_outlet_id				 in varchar2
	,pi_sales_rep_id			 in varchar2
	,pi_application		 		 in varchar2
	,pi_user					 in varchar2
)
Is
  	  -- local variables v_xxxxx
  	  v_sequence 	   number :=0 ;
	  v_status_code	   varchar2(1) :='?';
	  v_application_id number :=0;

Begin

	  -- get next sequence number into v_sequence
	  select CONTACT_EVENT_SEQ.nextval into v_sequence from dual;

	  -- map (Y--> C), (N-->F)
	  if ( pi_authentication_succeeded = 'Y' ) then
	  	 v_status_code := CE_STATUS_CODE_COMPLETED;
	  else
	  	 v_status_code := CE_STATUS_CODE_FAILED;
	  end if;

	  -- lookup applicationID using application name
	  v_application_id := get_sw_application_id_byname(pi_application);

	  insert_ContactEvent
	  (
			 v_sequence,
			 CE_TYPE_ID_ACCOUNT_AUTHENT,
			 sysdate,
			 null,
			 v_status_code,
			 sysdate,
			 'AccountClientAuthentication',  -- I just made it
			 CE_MECHANISM_ID_WEBPORTAL,
			 null,
			 sysdate,
			 sysdate,
			 pi_user,
			 'N',
			 v_application_id,
			 null
	  );

	  insert_ClientAccountCE
	  (
			 v_sequence,
			 CE_TYPE_ID_ACCOUNT_AUTHENT,
			 pi_account_id,
			 v_status_code,
			 sysdate,	   		-- status_timestamp
			 'N', 		   		-- status_confirmed_inf
			 null,
			 sysdate,	   		-- load_dt
			 sysdate,			-- update_dt
			 pi_user
	  );

	  try_chnl_outlet_salesrep
	  (
 	    v_sequence
	   ,CE_TYPE_ID_ACCOUNT_AUTHENT
 	   ,pi_chnl_org_id
	   ,pi_outlet_id
	   ,pi_sales_rep_id
	   ,pi_user
	);

	commit;

Exception
   When others then
      Rollback;

	  Raise_Application_Error(-20160, 'Failed while inserting Account Authentication Contact Event. Oracle:(['
    							|| sqlcode || '] [' || sqlerrm  || '])'
	 							|| ' [client_account_id=' || pi_account_id
								|| ',succeeded=' || pi_authentication_succeeded
								|| ',chnl_org_id=' || pi_chnl_org_id
								|| ',outlet_id=' || pi_outlet_id
								|| ',sales_rep_id=' || pi_sales_rep_id
								|| ',application_id=' || pi_application
								|| ',user_id=' || pi_user
								|| ']'
								);


End insertAccAuthenticationCE;


------------------------------------------------------------------
-- Check if given SMS contact event already exists in the database.
------------------------------------------------------------------
Function assertSMSClientNotification (
	   	 	 pi_kb_ban 				         SMS_CLIENT_NOTIFICATION.kb_ban%type
	  		,pi_kb_subscriber_no 			 SMS_CLIENT_NOTIFICATION.kb_subscriber_no%type
	  		,pi_contact_content_type_id 	 CONTACT_EVENT.contact_content_type_id%type
	  		,pi_scheduled_delivery_datetime  SMS_CLIENT_NOTIFICATION.scheduled_delivery_datetime%type
)
Return boolean
Is
  	  -- local variables v_xxxxx
  	  v_return_code number :=0 ;

Begin

	select
	  1
	into v_return_code
	from
	  SMS_CLIENT_NOTIFICATION sms,
	  CONTACT_EVENT ce
	where
	  (
	     (   sms.CONTACT_EVENT_ID = ce.CONTACT_EVENT_ID
	     and sms.CONTACT_EVENT_TYPE_ID = ce.CONTACT_EVENT_TYPE_ID )
	  )
	  and (
	  kb_ban = pi_kb_ban and
	  kb_subscriber_no = pi_kb_subscriber_no and
	  contact_content_type_id = pi_contact_content_type_id and
	  scheduled_delivery_datetime = pi_scheduled_delivery_datetime
	  );

	return true;

Exception
   When no_data_found then
    return false;

End assertSMSClientNotification;


------------------------------------------------------------------
-- insert parameter values given as a comma separted list of values.
------------------------------------------------------------------
Procedure insert_parameter_values(
	 pi_sequence number
	,pi_contact_event_type_id number
	,pi_contact_content_type_id number
	,pi_user varchar2
	,pi_content_params T_CONTACT_PARAMETER_ARRAY
)
Is
  	  -- local variables v_xxxxx
  	  v_param_no    number :=0 ;
	  v_param_value contact_evnt_content_param.parameter_value%type;
Begin

    FOR i IN pi_content_params.first..pi_content_params.last LOOP
		EXIT WHEN (i > MAX_CONTENT_PARAMS_SIZE);

		IF (pi_content_params(i) is not null) THEN
			v_param_no := i;
			v_param_value := pi_content_params(i);
	
			insert_ContactEvntContentParam
			(
				 pi_sequence, 	  			   	   --contact_event_id
				 pi_contact_event_type_id,		   --contact_event_type_id
				 pi_contact_content_type_id,
				 v_param_no, 	   				   --contact_content_param_no
				 v_param_value,					   --parameter_value
				 sysdate,						   --load_dt
				 sysdate,						   --update_dt
				 pi_user						   --user_last_modify
			);
		END IF;
    END LOOP;

Exception
   When others then
	  Raise_Application_Error(-20160, 'Failed inserting content parameter value list. Oracle:(['
    							|| sqlcode || '] [' || sqlerrm  || '])'
	 							|| ' ['
								|| ',pi_sequence=' || pi_sequence
								|| ',lastParam_no=' || v_param_no
								|| ',lastParam_value=' || v_param_value
								|| ']'
								);

End insert_parameter_values;


------------------------------------------------------------------
-- Insert SMS Contact Events
------------------------------------------------------------------
Procedure insertSMSCE_NotificationReq
(
	 pi_contact_content_type_id	 		in number
	,pi_kb_ban							in number
	,pi_kb_subscriber_no		 		in varchar2
	,pi_kb_product_type			 		in varchar2
	,pi_language_cd				 		in varchar2
	,pi_destination_phone_no	 		in varchar
	,pi_billable_indicator		 		in varchar2
	,pi_originating_user_name	 		in varchar2
	,pi_scheduled_delivery_datetime		in date
	,pi_time_to_live			 		in number
	,pi_priority_number			 		in number
	,pi_delivery_receipt_req_ind		in varchar2
	,pi_message_text			 		in varchar2
	-- to populate  CONTACT_EVNT_CONTENT_PARAM
	,pi_content_params			 		in T_CONTACT_PARAMETER_ARRAY
	,pi_application		 		 		in varchar2
	,pi_user							in varchar2
	,pi_team_member_id					in number
	,pi_contact_event_type_id			in number default CE_TYPE_ID_NOTIFICATION
)
Is
  	  -- local variables v_xxxxx
  	  v_sequence 	        number :=0;
	  v_application_id      number :=0;
	  v_content_params_size number :=0;

	  v_param_no		number :=0;
Begin

	  -- get next sequence number into v_sequence
	  select CONTACT_EVENT_SEQ.nextval into v_sequence from dual;

	  -- lookup applicationID using application name
	  v_application_id := get_sw_application_id_byname(pi_application);

	  insert_ContactEvent
	  (
			 v_sequence,
			 pi_contact_event_type_id,
			 sysdate,
			 null,
			 CE_STATUS_CODE_SCHEDULED,
			 sysdate,
			 'SMSNotification',  -- I just made it
			 CE_MECHANISM_ID_SMS,
			 pi_contact_content_type_id,
			 sysdate,
			 sysdate,
			 pi_user,
			 'N',
			 v_application_id,
			 null
	  );

	  insert_SubscriberCEOld
	  (
			 v_sequence, 	  			   	   --contact_event_id
			 pi_contact_event_type_id,		   --contact_event_type_id
			 pi_kb_subscriber_no,
			 pi_kb_product_type,
			 pi_kb_ban,
			 CE_STATUS_CODE_SCHEDULED,
			 sysdate,				  		   --status_timestamp
			 null,							   --subscription_id
			 sysdate,						   --load_dt
			 sysdate,						   --update_dt
			 pi_user,						   --user_last_modify
			 'N',							   --status_confirmed_ind
			 pi_language_cd
	  );

	  insert_SMSClientNotification
	  (
			 v_sequence, 	  			   	   --contact_event_id
			 pi_contact_event_type_id,		   --contact_event_type_id
			 pi_kb_subscriber_no,
			 pi_kb_product_type,
			 pi_kb_ban,
			 pi_scheduled_delivery_datetime,
			 pi_priority_number,
			 pi_time_to_live,
			 pi_billable_indicator,
			 pi_originating_user_name,
			 pi_message_text,
			 SMS_GATEWAY_MESSAGE_ID_UNKNOWN,   --sms_gateway_message_id
			 SMS_STATUS_TYPE_CD_INITIAL,	   --sms_status_type_cd
			 null,							   --status_reason_code
			 sysdate,						   --load_dt
			 sysdate,						   --update_dt
			 pi_user,						   --user_last_modify
			 pi_destination_phone_no,
			 pi_delivery_receipt_req_ind
	  );

      if (pi_content_params is not null) and (pi_content_params.count > 0) 
      then
		  insert_parameter_values(
				 v_sequence, 	  			   	   --contact_event_id
				 pi_contact_event_type_id,		   --contact_event_type_id
				 pi_contact_content_type_id,
				 pi_user,
				 pi_content_params
		  );
	  end if;
	  
	  -- only insert into CONTACT_EVENT_TEAM_MEMBER if pi_team_member_id is valid
	  if (pi_team_member_id is not null) and (pi_team_member_id > 0)
	  then
		  insert_ContactEventTeamMember(
		  		 v_sequence, 	  		   		--contact_event_id 
				 pi_contact_event_type_id,	   	--contact_event_type_id 
				 pi_team_member_id,		  		--team_member_id
				 sysdate,			   			--load_dt 
				 sysdate,			   			--update_dt 
				 pi_user			   			--user_last_modify
		  );
	  end if;
	  
	  commit;

Exception
   When others then
      Rollback;
	  Raise_Application_Error(-20160, 'Failed while inserting SMS Contact Event and Request. Oracle:(['
    							|| sqlcode || '] [' || sqlerrm  || '])'
	 							|| ' [kb_ban=' || pi_kb_ban
								|| ',kb_subscriber_no=' || pi_kb_subscriber_no
								|| ',phone_no=' || pi_destination_phone_no
								|| ',application_id=' || pi_application
								|| ',user_id=' || pi_user
								|| ',pi_message_text=' || pi_message_text
								|| ']'
								);

End insertSMSCE_NotificationReq;

------------------------------------------------------------------
-- Insert Replied inbound raw SMS content Events
------------------------------------------------------------------
Procedure insertReplied_RawInboundSMS
(
     pi_contact_content_type_id             in number
    ,pi_subscription_id                          in number
     -- to populate  CONTACT_EVNT_CONTENT_PARAM
    ,pi_content_params                        in T_CONTACT_PARAMETER_ARRAY
    ,pi_application                               in varchar2
    ,pi_user                                        in varchar2
    ,pi_contact_event_type_id               in number default CE_TYPE_ID_NOTIFICATION
)
Is
      -- local variables v_xxxxx
      v_sequence             number :=0;
      v_application_id      number :=0;

Begin

      -- get next sequence number into v_sequence
      select CONTACT_EVENT_SEQ.nextval into v_sequence from dual;

      -- lookup applicationID using application name
      v_application_id := get_sw_application_id_byname(pi_application);

      insert_ContactEvent
      (
             v_sequence,
             pi_contact_event_type_id,
             sysdate,
             null,
             CE_STATUS_CODE_SCHEDULED,
             sysdate,
             'SMSNotification',  -- I just made it
             CE_MECHANISM_ID_SMS,
             pi_contact_content_type_id,
             sysdate,
             sysdate,
             pi_user,
             'N',
             v_application_id,
             null
      );

         insert_SubscriberCE
      (
             v_sequence,
             pi_contact_event_type_id,
             pi_subscription_id,
             CE_STATUS_CODE_COMPLETED,
             sysdate,               -- status_timestamp
             'N',                    -- status_confirmed_inf
             null,
             sysdate,               -- load_dt
             sysdate,            -- update_dt
             pi_user
      );

   
      if (pi_content_params is not null) and (pi_content_params.count > 0)
      then
          insert_parameter_values(
                 v_sequence,                             --contact_event_id
                 pi_contact_event_type_id,           --contact_event_type_id
                 pi_contact_content_type_id,
                 pi_user,
                 pi_content_params
          );
      end if;

     
      commit;

Exception
   When others then
      Rollback;
      Raise_Application_Error(-20160, 'Failed while Inserting replied inbound raw SMS content Event and Request. Oracle:(['
                                || sqlcode || '] [' || sqlerrm  || '])'
                                || ',subscription_id=' || pi_subscription_id
                                || ',application_id=' || pi_application
                                || ',user_id=' || pi_user
                                || ']'
                                );

End insertReplied_RawInboundSMS;


------------------------------------------------------------------
-- Retrieve SMS Contact Event History by Event Type and Subscriber
------------------------------------------------------------------
Procedure get_sms_contact_event_history
(
	 pi_contact_content_type_id	in	Number   -- contact event type
	,pi_kb_subscriber_no		in	Varchar2 -- subscriber no
	,pi_kb_ban					in	Number	 -- ban
	,pi_from_date				in	Date	 -- delivery_date
	,pi_to_date					in	Date	 -- delivery_date
	,po_contact_event_history	out	RefCursor-- ref to cursor  will become ResultSet
)
Is
Begin

/*
Return the list of contact events by subscriber within the given date period for the given contact content type id.
Select the following columns:
	 		   CONTACT_EVENT
					CONTACT_CONTENT_TYPE_ID 	= pi_contact_content_type_id
	 		   		SMS_CLIENT_NOTIFICATION (SMS)
					KB_SUBSCRIBER_NO	   	= pi_kb_subscriber_no
					KB_PRODUCT_TYPE
					KB_BAN			   	= pi_kb_ban
					SCHEDULED_DELIVERY_DATETIME between (from_date, to_date)
					PRIORITY_NUMBER
					TIME_TO_LIVE
					DELIVERY_RECIEPT_REQ_IND
					BILLABLE_INDICATOR
					ORIGINATING_USER_NAME
					SMS_STATUS_TYPE_CD
					LOAD_DT
					UPDATE_DT
					USER_LAST_MODIFY
	 		   		SMS_STATUS_TYPE
					CONTACT_EVENT_STATUS_CODE

			Link to
	 		   SMS_STATUS_TYPE
			Where
					SMS_STATUS_TYPE_CD		= SMS.SMS_STATUS_TYPE_CD
			Link to
	 		   CONTACT_EVENT
			Where
			   		CONTACT_EVENT_TYPE_ID  	= SMS.CONTACT_EVENT_TYPE_ID
					CONTACT_CONTENT_TYPE_ID   	= pi_contact_content_type_id
*/

	 open po_contact_event_history for
	 	  select
		  		 *
				 from
				 	  sms_client_notification sms
				 where
				 	      sms.KB_BAN = pi_kb_ban
					  and sms.KB_SUBSCRIBER_NO = pi_kb_subscriber_no;

End get_sms_contact_event_history;


------------------------------------------------------------------
-- Retrieve SMS Contact Event History by Subscriber
------------------------------------------------------------------
Procedure get_sms_contact_event_history
(
	 pi_kb_subscriber_no		in	Varchar2 -- subscriber no
	,pi_kb_ban					in	Number	 -- ban
	,pi_from_date				in	Date	 -- delivery_date
	,pi_to_date					in	Date	 -- delivery_date
	,po_contact_event_history	out	RefCursor-- ref to cursor  will become ResultSet
)
Is
Begin

/*
Return the list of all types of contact events by subscriber within the given date period Select the following columns:

	 		   CONTACT_EVENT
					CONTACT_CONTENT_TYPE_ID
	 		   		SMS_CLIENT_NOTIFICATION (SMS)
					KB_SUBSCRIBER_NO	   	= pi_kb_subscriber_no
					KB_PRODUCT_TYPE
					KB_BAN			   	= pi_kb_ban
					SCHEDULED_DELIVERY_DATETIME between (from_date, to_date)
					PRIORITY_NUMBER
					TIME_TO_LIVE
					DELIVERY_RECIEPT_REQ_IND
					BILLABLE_INDICATOR
					ORIGINATING_USER_NAME
					SMS_STATUS_TYPE_CD
					LOAD_DT
					UPDATE_DT
					USER_LAST_MODIFY
	 		   		SMS_STATUS_TYPE
					CONTACT_EVENT_STATUS_CODE

			Link to
	 		   SMS_STATUS_TYPE
			Where
					SMS_STATUS_TYPE_CD		= SMS.SMS_STATUS_TYPE_CD
			Link to
	 		   CONTACT_EVENT
			Where
			   		CONTACT_EVENT_TYPE_ID  	= SMS.CONTACT_EVENT_TYPE_ID
*/

	 open po_contact_event_history for
	 	  select
		  		 *
				 from
				 	  sms_client_notification sms
				 where
				 	      sms.KB_BAN = pi_kb_ban
					  and sms.KB_SUBSCRIBER_NO = pi_kb_subscriber_no;

End get_sms_contact_event_history;

End CONTACT_EVENT_PKG;
/