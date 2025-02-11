CREATE OR REPLACE
PACKAGE CHANNEL#

/*

|| Overview:        Package containing procedures to interface to the DIST database

||

|| Author:          Scott Lapish

||

|| Modifications:
|| 01/01/2001       Shawn Alipanah  Add Organization type code for QuebecTel dealer to be used in IVR Channel care
|| 28/09/2004       Marina Kuper    Added output parameters for outlet address, outlet open time, outlet close time, sequence value,
||                                  outlet phone number 
*/



IS

   FUNCTION ValidUser(chnl_org_cd_in VARCHAR2,user_code_in VARCHAR2, password_in VARCHAR2) RETURN INTEGER;

   FUNCTION ChangeUserPassword(chnl_org_cd_in VARCHAR2,user_code_in VARCHAR2, old_password_in VARCHAR2,new_password_in VARCHAR2) RETURN INTEGER;

   FUNCTION ResetPassword(chnl_org_cd_in VARCHAR2,user_code_in VARCHAR2, new_password_in VARCHAR2) RETURN INTEGER;

   PROCEDURE GetUserInformation(chnl_org_cd_in VARCHAR2
   				, user_code_in VARCHAR2
   				, chnl_org_desc_out OUT VARCHAR2
   				, user_desc_out OUT VARCHAR2);

   PROCEDURE GetMoreUserInformation(chnl_org_cd_in VARCHAR2
				, user_code_in VARCHAR2
				, chnl_org_desc_out OUT VARCHAR2
				, user_desc_out OUT VARCHAR2
				, address_province_cd OUT VARCHAR2
				, chnl_org_type_cd OUT VARCHAR2
				, ivr_privilege_routing_ind OUT  VARCHAR2);

 PROCEDURE GetMoreUserInformation(chnl_org_cd_in VARCHAR2
				, user_code_in VARCHAR2
				, chnl_org_desc_out OUT VARCHAR2
				, user_desc_out OUT VARCHAR2
				, address_province_cd OUT VARCHAR2
				, chnl_org_type_cd OUT VARCHAR2
				, ivr_privilege_routing_ind OUT  VARCHAR2
                , street_name OUT VARCHAR2
                , city_name OUT VARCHAR2
                , location_province_cd OUT VARCHAR2
                , postal_cd OUT VARCHAR2
                , outlet_close_time_1 OUT VARCHAR2
                , outlet_close_time_2 OUT VARCHAR2
                , outlet_close_time_3 OUT VARCHAR2
                , outlet_close_time_4 OUT VARCHAR2
                , outlet_close_time_5 OUT VARCHAR2
                , outlet_close_time_6 OUT VARCHAR2
                , outlet_close_time_7 OUT VARCHAR2
                , outlet_open_time_1 OUT VARCHAR2
                , outlet_open_time_2 OUT VARCHAR2
                , outlet_open_time_3 OUT VARCHAR2
                , outlet_open_time_4 OUT VARCHAR2
                , outlet_open_time_5 OUT VARCHAR2
                , outlet_open_time_6 OUT VARCHAR2
                , outlet_open_time_7 OUT VARCHAR2
                , sequence_value_1 OUT VARCHAR2
                , sequence_value_2 OUT VARCHAR2
                , sequence_value_3 OUT VARCHAR2
                , sequence_value_4 OUT VARCHAR2
                , sequence_value_5 OUT VARCHAR2
                , sequence_value_6 OUT VARCHAR2
                , sequence_value_7 OUT VARCHAR2
                , phone_number OUT VARCHAR2);


END CHANNEL#;
/
