CREATE OR REPLACE
PACKAGE BODY CHANNEL#
/*
|| Overview:        Package containing procedures to interface with the DIST database
||
|| Author:          Scott Lapish
||
|| Modifications:
||
*/
IS
FUNCTION ValidUser(chnl_org_cd_in VARCHAR2,user_code_in VARCHAR2, password_in VARCHAR2)
RETURN INTEGER
/*
|| Overview:        Function to determine if user is a valid channel/user
||
|| Author:          Scott Lapish
||
|| Modifications:
||
*/
IS
   --cursor for CHANNEL_ORGANIZATION
   CURSOR chnl_org_cur IS
      SELECT *
        FROM CHANNEL_ORGANIZATION
       WHERE CHNL_ORG_CD = chnl_org_cd_in AND
             TRUNC(EFFECTIVE_DT) <= TRUNC(SYSDATE) AND
             EXPIRATION_DT IS NULL;
   --Record to hold chnl_org_cur rows
   chnl_org_rec chnl_org_cur%ROWTYPE;
   --cursor for OUTLET
   CURSOR outlet_cur IS
      SELECT *
        FROM OUTLET
       WHERE CHNL_ORG_ID = chnl_org_rec.CHNL_ORG_ID AND
             TRUNC(EFFECTIVE_DT) <= TRUNC(SYSDATE) AND
             EXPIRATION_DT IS NULL AND
             OUTLET_CODE = user_code_in AND
             OUTLET_IVR_PASSWORD = password_in AND
             OUTLET_IVR_PASSWORD IS NOT NULL;
   --Record to hold outlet_cur rows
   outlet_rec outlet_cur%ROWTYPE;
   --cursor for SALES_REPRESENTATIVE
   CURSOR sales_rep_cur IS
      SELECT a.*
        FROM SALES_REPRESENTATIVE a,SALES_REP_CHNL_ORG b
       WHERE a.SALES_REP_ID = b.SALES_REP_ID AND
             b.CHNL_ORG_ID = chnl_org_rec.CHNL_ORG_ID AND
             b.EXPIRATION_DT IS NULL AND
             TRUNC(b.EFFECTIVE_DT) <= TRUNC(SYSDATE) AND
             a.IVR_PASSWORD = password_in AND
             a.SALES_REP_CD = user_code_in AND
             a.IVR_PASSWORD IS NOT NULL;
   --Record to hold sales_rep_cur rows
   sales_rep_rec sales_rep_cur%ROWTYPE;
   --EXCEPTIONS
BEGIN
    /*Find CHANNEL_ORGANIZATION ENTRY*/
   OPEN chnl_org_cur;
   FETCH chnl_org_cur INTO chnl_org_rec;
   IF chnl_org_cur%NOTFOUND
   THEN
     CLOSE chnl_org_cur;
     RETURN -1;
   END IF;
   CLOSE chnl_org_cur;
   /*If this is a dealer then look for sales rep otherwise look for outlet */
      /*Find SALES_REP*/
      OPEN sales_rep_cur;
      FETCH sales_rep_cur INTO sales_rep_rec;
      IF sales_rep_cur%FOUND
      THEN
         CLOSE sales_rep_cur;
         RETURN 0;
      END IF;
      CLOSE sales_rep_cur;
      /*Find OUTLET*/
      OPEN outlet_cur;
      FETCH outlet_cur INTO outlet_rec;
      IF outlet_cur%NOTFOUND
      THEN
         CLOSE outlet_cur;
         RETURN -2;
      END IF;
      CLOSE outlet_cur;
   RETURN 0;
EXCEPTION
   WHEN OTHERS
   THEN
      CLOSE chnl_org_cur;
      CLOSE outlet_cur;
      CLOSE sales_rep_cur;
      RETURN -4;
END ValidUser;
FUNCTION ChangeUserPassword(chnl_org_cd_in VARCHAR2,user_code_in VARCHAR2, old_password_in VARCHAR2,new_password_in VARCHAR2)
RETURN INTEGER
/*
|| Overview:        Function to change a user password
||
|| Author:          Scott Lapish
||
|| Modifications:
||
*/
IS
   --cursor for CHANNEL_ORGANIZATION
   CURSOR chnl_org_cur IS
      SELECT *
        FROM CHANNEL_ORGANIZATION
       WHERE CHNL_ORG_CD = chnl_org_cd_in AND
             TRUNC(EFFECTIVE_DT) <= TRUNC(SYSDATE) AND
             EXPIRATION_DT IS NULL;
   --Record to hold chnl_org_cur rows
   chnl_org_rec chnl_org_cur%ROWTYPE;
   --cursor for OUTLET
   CURSOR outlet_cur IS
      SELECT *
        FROM OUTLET
       WHERE CHNL_ORG_ID = chnl_org_rec.CHNL_ORG_ID AND
             TRUNC(EFFECTIVE_DT) <= TRUNC(SYSDATE) AND
             EXPIRATION_DT IS NULL AND
             OUTLET_CODE = user_code_in AND
             OUTLET_IVR_PASSWORD = old_password_in AND
             OUTLET_IVR_PASSWORD IS NOT NULL;
   --Record to hold outlet_cur rows
   outlet_rec outlet_cur%ROWTYPE;
   --cursor for SALES_REPRESENTATIVE
   CURSOR sales_rep_cur IS
      SELECT a.*
        FROM SALES_REPRESENTATIVE a,SALES_REP_CHNL_ORG b
       WHERE a.SALES_REP_ID = b.SALES_REP_ID AND
             b.CHNL_ORG_ID = chnl_org_rec.CHNL_ORG_ID AND
             b.EXPIRATION_DT IS NULL AND
             TRUNC(b.EFFECTIVE_DT) <= TRUNC(SYSDATE) AND
             a.IVR_PASSWORD = old_password_in AND
             a.SALES_REP_CD = user_code_in AND
             a.IVR_PASSWORD IS NOT NULL;
   --Record to hold sales_rep_cur rows
   sales_rep_rec sales_rep_cur%ROWTYPE;
   --EXCEPTIONS
BEGIN
    /*Find CHANNEL_ORGANIZATION ENTRY*/
   OPEN chnl_org_cur;
   FETCH chnl_org_cur INTO chnl_org_rec;
   IF chnl_org_cur%NOTFOUND
   THEN
     CLOSE chnl_org_cur;
     RETURN -1;
   END IF;
   CLOSE chnl_org_cur;
   /*If this is a dealer then look for sales rep otherwise look for outlet */
      /*Find SALES_REP*/
      OPEN sales_rep_cur;
      FETCH sales_rep_cur INTO sales_rep_rec;
   IF sales_rep_cur%FOUND
   THEN
      CLOSE sales_rep_cur;
      UPDATE SALES_REPRESENTATIVE SET IVR_PASSWORD = new_password_in where
          SALES_REPRESENTATIVE.SALES_REP_ID = sales_rep_rec.SALES_REP_ID;
   ELSE
      CLOSE sales_rep_cur;
      /*Find OUTLET*/
      OPEN outlet_cur;
      FETCH outlet_cur INTO outlet_rec;
      IF outlet_cur%NOTFOUND
      THEN
         CLOSE outlet_cur;
         RETURN -2;
      END IF;
      CLOSE outlet_cur;
      UPDATE OUTLET SET OUTLET_IVR_PASSWORD = new_password_in where
          OUTLET.OUTLET_ID = outlet_rec.OUTLET_ID;
   END IF;
   commit;
   RETURN 0;
EXCEPTION
   WHEN OTHERS
   THEN
      rollback;
      CLOSE chnl_org_cur;
      CLOSE outlet_cur;
      CLOSE sales_rep_cur;
      RETURN -4;
END ChangeUserPassword;
FUNCTION ResetPassword(chnl_org_cd_in VARCHAR2,user_code_in VARCHAR2,new_password_in VARCHAR2)
RETURN INTEGER
/*
|| Overview:        Function to reset a user password
||
|| Author:          Scott Lapish
||
|| Modifications:
||
*/
IS
   --cursor for CHANNEL_ORGANIZATION
   CURSOR chnl_org_cur IS
      SELECT *
        FROM CHANNEL_ORGANIZATION
       WHERE CHNL_ORG_CD = chnl_org_cd_in AND
             TRUNC(EFFECTIVE_DT) <= TRUNC(SYSDATE) AND
             EXPIRATION_DT IS NULL;
   --Record to hold chnl_org_cur rows
   chnl_org_rec chnl_org_cur%ROWTYPE;
   --cursor for OUTLET
   CURSOR outlet_cur IS
      SELECT *
        FROM OUTLET
       WHERE CHNL_ORG_ID = chnl_org_rec.CHNL_ORG_ID AND
             TRUNC(EFFECTIVE_DT) <= TRUNC(SYSDATE) AND
             EXPIRATION_DT IS NULL AND
             OUTLET_CODE = user_code_in;
   --Record to hold outlet_cur rows
   outlet_rec outlet_cur%ROWTYPE;
   --cursor for SALES_REPRESENTATIVE
   CURSOR sales_rep_cur IS
      SELECT a.*
        FROM SALES_REPRESENTATIVE a,SALES_REP_CHNL_ORG b
       WHERE a.SALES_REP_ID = b.SALES_REP_ID AND
             b.CHNL_ORG_ID = chnl_org_rec.CHNL_ORG_ID AND
             b.EXPIRATION_DT IS NULL AND
             TRUNC(b.EFFECTIVE_DT) <= TRUNC(SYSDATE) AND
             a.SALES_REP_CD = user_code_in;
   --Record to hold sales_rep_cur rows
   sales_rep_rec sales_rep_cur%ROWTYPE;
   --EXCEPTIONS
BEGIN
    /*Find CHANNEL_ORGANIZATION ENTRY*/
   OPEN chnl_org_cur;
   FETCH chnl_org_cur INTO chnl_org_rec;
   IF chnl_org_cur%NOTFOUND
   THEN
     CLOSE chnl_org_cur;
     RETURN -1;
   END IF;
   CLOSE chnl_org_cur;
   /*If this is a dealer then look for sales rep otherwise look for outlet */
   /*Find SALES_REP*/
   OPEN sales_rep_cur;
   FETCH sales_rep_cur INTO sales_rep_rec;
   IF sales_rep_cur%FOUND
      THEN
      CLOSE sales_rep_cur;
      UPDATE SALES_REPRESENTATIVE SET IVR_PASSWORD = new_password_in where
          SALES_REPRESENTATIVE.SALES_REP_ID = sales_rep_rec.SALES_REP_ID;
   ELSE
      CLOSE sales_rep_cur;
      /*Find OUTLET*/
      OPEN outlet_cur;
      FETCH outlet_cur INTO outlet_rec;
      IF outlet_cur%NOTFOUND
      THEN
         CLOSE outlet_cur;
         RETURN -2;
      END IF;
      CLOSE outlet_cur;
      UPDATE OUTLET SET OUTLET_IVR_PASSWORD = new_password_in where
          OUTLET.OUTLET_ID = outlet_rec.OUTLET_ID;
   END IF;
   commit;
   RETURN 0;
EXCEPTION
   WHEN OTHERS
   THEN
      rollback;
      CLOSE chnl_org_cur;
      CLOSE outlet_cur;
      CLOSE sales_rep_cur;
      RETURN -4;
END ResetPassword;
PROCEDURE GetUserInformation(chnl_org_cd_in VARCHAR2, user_code_in VARCHAR2, chnl_org_desc_out OUT VARCHAR2, user_desc_out OUT VARCHAR2)
/*
|| Overview:        Procedure to get user information
||
|| Author:          Scott Lapish
||
|| Modifications:
||
*/
IS
   --cursor for CHANNEL_ORGANIZATION
   CURSOR chnl_org_cur IS
      SELECT *
        FROM CHANNEL_ORGANIZATION
       WHERE CHNL_ORG_CD = chnl_org_cd_in AND
             TRUNC(EFFECTIVE_DT) <= TRUNC(SYSDATE) AND
             EXPIRATION_DT IS NULL;
   --Record to hold chnl_org_cur rows
   chnl_org_rec chnl_org_cur%ROWTYPE;
   --cursor for OUTLET
   CURSOR outlet_cur IS
      SELECT *
        FROM OUTLET
       WHERE CHNL_ORG_ID = chnl_org_rec.CHNL_ORG_ID AND
             TRUNC(EFFECTIVE_DT) <= TRUNC(SYSDATE) AND
             EXPIRATION_DT IS NULL AND
             OUTLET_CODE = user_code_in;
   --Record to hold outlet_cur rows
   outlet_rec outlet_cur%ROWTYPE;
   --cursor for SALES_REPRESENTATIVE
   CURSOR sales_rep_cur IS
      SELECT a.*
        FROM SALES_REPRESENTATIVE a,SALES_REP_CHNL_ORG b
       WHERE a.SALES_REP_ID = b.SALES_REP_ID AND
             b.CHNL_ORG_ID = chnl_org_rec.CHNL_ORG_ID AND
             b.EXPIRATION_DT IS NULL AND
             TRUNC(b.EFFECTIVE_DT) <= TRUNC(SYSDATE) AND
             a.SALES_REP_CD = user_code_in;
   --Record to hold sales_rep_cur rows
   sales_rep_rec sales_rep_cur%ROWTYPE;
   --EXCEPTIONS
   channel_org_not_found EXCEPTION;
   outlet_not_found EXCEPTION;
   sales_rep_not_found EXCEPTION;
BEGIN
    /*Find CHANNEL_ORGANIZATION ENTRY*/
   OPEN chnl_org_cur;
   FETCH chnl_org_cur INTO chnl_org_rec;
   IF chnl_org_cur%NOTFOUND
   THEN
      RAISE channel_org_not_found;
   END IF;
   CLOSE chnl_org_cur;
   chnl_org_desc_out := chnl_org_rec.CHNL_ORG_DES;
   /*If this is a dealer then look for sales rep otherwise look for outlet */
   /*Find SALES_REP*/
   OPEN sales_rep_cur;
   FETCH sales_rep_cur INTO sales_rep_rec;
   IF sales_rep_cur%FOUND
      THEN
      CLOSE sales_rep_cur;
      user_desc_out := sales_rep_rec.first_name || ' ' || sales_rep_rec.last_name;
      RETURN;
   END IF;
    CLOSE sales_rep_cur;
    /*Find OUTLET*/
    OPEN outlet_cur;
    FETCH outlet_cur INTO outlet_rec;
    IF outlet_cur%NOTFOUND
    THEN
       RAISE outlet_not_found;
    END IF;
    CLOSE outlet_cur;
    user_desc_out := outlet_rec.outlet_des;
    RETURN;
EXCEPTION
   WHEN channel_org_not_found
  THEN
      CLOSE chnl_org_cur;
      RAISE_APPLICATION_ERROR(-20000,'Package CHANNEL#.GetUserInformation: No channel_organization record found.');
   WHEN outlet_not_found
  THEN
      CLOSE outlet_cur;
      RAISE_APPLICATION_ERROR(-20001,'Package CHANNEL#.GetUserInformation: No sales rep or outlet record found.');
   WHEN sales_rep_not_found
   THEN
      CLOSE sales_rep_cur;
      RAISE_APPLICATION_ERROR(-20002,'Package CHANNEL#.GetUserInformation: No sales_representativet record found.');
   WHEN OTHERS
   THEN
      CLOSE chnl_org_cur;
      CLOSE outlet_cur;
      CLOSE sales_rep_cur;
      RAISE;
END GetUserInformation;

--------------------------------------------------------------------------------------------------------------------------------
PROCEDURE GetMoreUserInformation(chnl_org_cd_in VARCHAR2
				, user_code_in VARCHAR2
				, chnl_org_desc_out OUT VARCHAR2
				, user_desc_out OUT VARCHAR2
				, address_province_cd OUT VARCHAR2
				, chnl_org_type_cd OUT VARCHAR2
				, ivr_privilege_routing_ind OUT  VARCHAR2)
/*
|| Overview:        Procedure to get user information (same as GetUserInformation plus additional attributes)
||
|| Author:          Peter Frei
||
|| Modifications:
||
|| 12/08/2003      Ludmila Pomirche - Added IVR privilege routing indicator
*/
IS
   --cursor for CHANNEL_ORGANIZATION
   CURSOR chnl_org_cur IS
      SELECT *
        FROM CHANNEL_ORGANIZATION
       WHERE CHNL_ORG_CD = chnl_org_cd_in AND
             TRUNC(EFFECTIVE_DT) <= TRUNC(SYSDATE) AND
             EXPIRATION_DT IS NULL;
   --Record to hold chnl_org_cur rows
   chnl_org_rec chnl_org_cur%ROWTYPE;
   --cursor for OUTLET
   CURSOR outlet_cur IS
      SELECT *
        FROM OUTLET
       WHERE CHNL_ORG_ID = chnl_org_rec.CHNL_ORG_ID AND
             TRUNC(EFFECTIVE_DT) <= TRUNC(SYSDATE) AND
             EXPIRATION_DT IS NULL AND
             OUTLET_CODE = user_code_in;
   --Record to hold outlet_cur rows
   outlet_rec outlet_cur%ROWTYPE;
   --cursor for SALES_REPRESENTATIVE
   CURSOR sales_rep_cur IS
      SELECT a.*
        FROM SALES_REPRESENTATIVE a,SALES_REP_CHNL_ORG b
       WHERE a.SALES_REP_ID = b.SALES_REP_ID AND
             b.CHNL_ORG_ID = chnl_org_rec.CHNL_ORG_ID AND
             b.EXPIRATION_DT IS NULL AND
             TRUNC(b.EFFECTIVE_DT) <= TRUNC(SYSDATE) AND
             a.SALES_REP_CD = user_code_in;
   --Record to hold sales_rep_cur rows
   sales_rep_rec sales_rep_cur%ROWTYPE;
   --cursor for 'Location' address (if sales rep)
   CURSOR address_location_cur IS
      SELECT a.*
        FROM address a,contact_address b
       WHERE a.ADDRESS_ID = b.ADDRESS_ID AND
             b.CONTACTING_ID =  chnl_org_rec.CHNL_ORG_ID AND
             b.CONTACT_GROUP_CD = 'CO' AND
             b.REASON_TYPE_CD = 'Location' AND
             (b.EXPIRATION_DT is null or b.EXPIRATION_DT > TRUNC(sysdate)) ;
   --cursor for 'Ship to' address (if sales rep)
   CURSOR address_shipTo_cur IS
      SELECT a.*
        FROM address a,contact_address b
       WHERE a.ADDRESS_ID = b.ADDRESS_ID AND
             b.CONTACTING_ID =  chnl_org_rec.CHNL_ORG_ID AND
             b.CONTACT_GROUP_CD = 'CO' AND
             b.REASON_TYPE_CD = 'Ship to' AND
             (b.EXPIRATION_DT is null or b.EXPIRATION_DT > TRUNC(sysdate));
   --cursor for 'Bill to' address (if sales rep)
   CURSOR address_billTo_cur IS
      SELECT a.*
        FROM address a,contact_address b
       WHERE a.ADDRESS_ID = b.ADDRESS_ID AND
             b.CONTACTING_ID =  chnl_org_rec.CHNL_ORG_ID AND
             b.CONTACT_GROUP_CD = 'CO' AND
             b.REASON_TYPE_CD = 'Bill to' AND
             (b.EXPIRATION_DT is null or b.EXPIRATION_DT > TRUNC(sysdate));
   --cursor for 'Location' address (if outlet)
   CURSOR outlet_address_location_cur IS
      SELECT a.*
        FROM address a,contact_address b
       WHERE a.ADDRESS_ID = b.ADDRESS_ID AND
             b.CONTACTING_ID =  outlet_rec.outlet_ID AND
             b.CONTACT_GROUP_CD = 'OT'         AND
             b.REASON_TYPE_CD = 'Location' AND
             (b.EXPIRATION_DT is null or b.EXPIRATION_DT > TRUNC(sysdate));
   --cursor for 'Ship to' address (if outlet)
   CURSOR outlet_address_shipTo_cur IS
      SELECT a.*
        FROM address a,contact_address b
       WHERE a.ADDRESS_ID = b.ADDRESS_ID AND
             b.CONTACTING_ID =  outlet_rec.outlet_ID AND
             b.CONTACT_GROUP_CD = 'OT'         AND
             b.REASON_TYPE_CD = 'Ship to' AND
             (b.EXPIRATION_DT is null or b.EXPIRATION_DT > TRUNC(sysdate));
   --cursor for 'Bill to' address (if outlet)
   CURSOR outlet_address_billTo_cur IS
      SELECT a.*
        FROM address a,contact_address b
       WHERE a.ADDRESS_ID = b.ADDRESS_ID AND
             b.CONTACTING_ID =  outlet_rec.outlet_ID AND
             b.CONTACT_GROUP_CD = 'OT'         AND
             b.REASON_TYPE_CD = 'Bill to' AND
             (b.EXPIRATION_DT is null or b.EXPIRATION_DT > TRUNC(sysdate));
      --Record to hold address_location_cur rows
   address_rec address_location_cur%ROWTYPE;
    CURSOR chnl_org_ivr_priority IS
      SELECT 'Y'
        FROM  CHANNEL_ORG_ATTRIBUTE
       WHERE CHNL_ORG_ID = chnl_org_rec.CHNL_ORG_ID AND
             BUS_PARTNER_ATTRIBUTE_ID = 7 ;
v_chnl_org_ivr_priority_ind  CHAR(1):= 'N';

   --EXCEPTIONS
   channel_org_not_found EXCEPTION;
   outlet_not_found EXCEPTION;
   sales_rep_not_found EXCEPTION;
   location_address_not_found EXCEPTION;
   shipTo_address_not_found EXCEPTION;
   billTo_address_not_found EXCEPTION;
BEGIN
    /*Find CHANNEL_ORGANIZATION ENTRY*/
   OPEN chnl_org_cur;
   FETCH chnl_org_cur INTO chnl_org_rec;
   IF chnl_org_cur%NOTFOUND
   THEN
      RAISE channel_org_not_found;
   END IF;
   CLOSE chnl_org_cur;
   chnl_org_desc_out := chnl_org_rec.CHNL_ORG_DES;
   chnl_org_type_cd := chnl_org_rec.CHNL_ORG_TYPE_CD;
   /* Check for IVR routing priority */
   OPEN chnl_org_ivr_priority ;
   FETCH  chnl_org_ivr_priority  into v_chnl_org_ivr_priority_ind ;
   CLOSE chnl_org_ivr_priority;
   ivr_privilege_routing_ind := v_chnl_org_ivr_priority_ind;
   /* pass the org type code for Quebec dealers */
   /*If this is a dealer then look for sales rep otherwise look for outlet */
   /*Find SALES_REP*/
   OPEN sales_rep_cur;
   FETCH sales_rep_cur INTO sales_rep_rec;
   IF sales_rep_cur%FOUND
      THEN
        CLOSE sales_rep_cur;
        user_desc_out := sales_rep_rec.first_name || ' ' || sales_rep_rec.last_name;
        /* Find 'Province Code' for this dealer */
        OPEN address_location_cur;
        FETCH address_location_cur INTO address_rec;
        IF address_location_cur%FOUND THEN
            CLOSE address_location_cur;
            address_province_cd := address_rec.province_cd;
        ELSE
            /* - look for 'Ship To' address second */
            OPEN address_shipTo_cur;
            FETCH address_shipTo_cur INTO address_rec;
            IF address_shipTo_cur%FOUND THEN
                CLOSE address_shipTo_cur;
                address_province_cd := address_rec.province_cd;
            ELSE
                /* - look for 'Bill To' address second */
                OPEN address_billTo_cur;
                FETCH address_billTo_cur INTO address_rec;
                IF address_billTo_cur%FOUND THEN
                    CLOSE address_billTo_cur;
                    address_province_cd := address_rec.province_cd;
                END IF;
            END IF;
        END IF;
        RETURN;
   END IF;
    CLOSE sales_rep_cur;
    /*Find OUTLET*/
    OPEN outlet_cur;
    FETCH outlet_cur INTO outlet_rec;
    IF outlet_cur%NOTFOUND
    THEN
       RAISE outlet_not_found;
    END IF;
    CLOSE outlet_cur;
    user_desc_out := outlet_rec.outlet_des;
    /* Find 'Province Code' for this outlet */
    OPEN outlet_address_location_cur;
    FETCH outlet_address_location_cur INTO address_rec;
    IF outlet_address_location_cur%FOUND THEN
        CLOSE outlet_address_location_cur;
        address_province_cd := address_rec.province_cd;
    ELSE
        /* - look for 'Ship To' address second */
        OPEN outlet_address_shipTo_cur;
        FETCH outlet_address_shipTo_cur INTO address_rec;
        IF outlet_address_shipTo_cur%FOUND THEN
            CLOSE outlet_address_shipTo_cur;
            address_province_cd := address_rec.province_cd;
        ELSE
            /* - look for 'Bill To' address second */
            OPEN outlet_address_billTo_cur;
            FETCH outlet_address_billTo_cur INTO address_rec;
            IF outlet_address_billTo_cur%FOUND THEN
                CLOSE outlet_address_billTo_cur;
                address_province_cd := address_rec.province_cd;
            END IF;
        END IF;
    END IF;
    RETURN;
EXCEPTION
   WHEN channel_org_not_found
  THEN
      CLOSE chnl_org_cur;
      RAISE_APPLICATION_ERROR(-20000,'Package CHANNEL#.GetMoreUserInformation: No channel_organization record found.');
   WHEN outlet_not_found
  THEN
      CLOSE outlet_cur;
      RAISE_APPLICATION_ERROR(-20001,'Package CHANNEL#.GetMoreUserInformation: No sales rep or outlet record found.');
   WHEN sales_rep_not_found
   THEN
      CLOSE sales_rep_cur;
      RAISE_APPLICATION_ERROR(-20002,'Package CHANNEL#.GetMoreUserInformation: No sales_representativet record found.');
   WHEN OTHERS
   THEN
      CLOSE chnl_org_cur;
      CLOSE outlet_cur;
      CLOSE sales_rep_cur;
      RAISE;
END GetMoreUserInformation;

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
                , phone_number OUT VARCHAR2)
/*
|| Overview:        Procedure to get user information (same as GetUserInformation plus additional attributes)
||
|| Author:          Peter Frei
||
|| Modifications:
||
|| 12/08/2003      Ludmila Pomirche - Added IVR privilege routing indicator
||
|| 20/10/2004      Marina Kuper - Changed query chnl_org_code , added condition : if  the expiration date is null OR in the future
*/
IS
   --cursor for CHANNEL_ORGANIZATION
   CURSOR chnl_org_cur IS
      SELECT *
        FROM CHANNEL_ORGANIZATION
       WHERE CHNL_ORG_CD = chnl_org_cd_in AND
             TRUNC(EFFECTIVE_DT) <= TRUNC(SYSDATE) AND
            ( EXPIRATION_DT IS NULL OR EXPIRATION_DT > SYSDATE);
   --Record to hold chnl_org_cur rows
   chnl_org_rec chnl_org_cur%ROWTYPE;
   --cursor for OUTLET
   CURSOR outlet_cur IS
      SELECT *
        FROM OUTLET
       WHERE CHNL_ORG_ID = chnl_org_rec.CHNL_ORG_ID AND
             TRUNC(EFFECTIVE_DT) <= TRUNC(SYSDATE) AND
             EXPIRATION_DT IS NULL AND
             OUTLET_CODE = user_code_in;
   --Record to hold outlet_cur rows
   outlet_rec outlet_cur%ROWTYPE;
    --cursor for OUTLET ADDRESS AND HOURS OF OPERATION
   CURSOR outlet_address_hours_cur IS
      SELECT  a.province_cd, a.city_name, a.postal_cd, a.street_number || ' ' ||
              a.street_name || ' ' || a.street_type_cd  str, oot.close_time,
              oot.open_time, oot.sequence_value, cp.npa || cp.nxx || cp.nnnn phone
        FROM contact_address ca, address a, outlet_open_time oot, contact_phone cp
        WHERE ca.contacting_id = outlet_rec.outlet_ID
        and   ca.contact_group_cd = 'OT'
        and   ca.reason_type_cd = 'Location'
        and   nvl(ca.expiration_dt, sysdate+1) > sysdate
        and   a.address_id = ca.address_id
        and   oot.outlet_id = ca.contacting_id
        and   cp.contacting_id(+) = oot.outlet_id
        and   cp.contact_group_cd(+) = 'OT'
        and   cp.reason_type_cd(+) = 'Location'
        and   cp.phone_type_cd(+) = 'LandLine'
        and   nvl(cp.expiration_dt, sysdate+1) > sysdate
        order by 8;
      --cursor for SALES_REPRESENTATIVE
   CURSOR sales_rep_cur IS
      SELECT a.*
        FROM SALES_REPRESENTATIVE a,SALES_REP_CHNL_ORG b
       WHERE a.SALES_REP_ID = b.SALES_REP_ID AND
             b.CHNL_ORG_ID = chnl_org_rec.CHNL_ORG_ID AND
             b.EXPIRATION_DT IS NULL AND
             TRUNC(b.EFFECTIVE_DT) <= TRUNC(SYSDATE) AND
             a.SALES_REP_CD = user_code_in;
   --Record to hold sales_rep_cur rows
   sales_rep_rec sales_rep_cur%ROWTYPE;
   --cursor for 'Location' address (if sales rep)
   CURSOR address_location_cur IS
      SELECT a.*
        FROM address a,contact_address b
       WHERE a.ADDRESS_ID = b.ADDRESS_ID AND
             b.CONTACTING_ID =  chnl_org_rec.CHNL_ORG_ID AND
             b.CONTACT_GROUP_CD = 'CO' AND
             b.REASON_TYPE_CD = 'Location' AND
             (b.EXPIRATION_DT is null or b.EXPIRATION_DT > TRUNC(sysdate)) ;
   --cursor for 'Ship to' address (if sales rep)
   CURSOR address_shipTo_cur IS
      SELECT a.*
        FROM address a,contact_address b
       WHERE a.ADDRESS_ID = b.ADDRESS_ID AND
             b.CONTACTING_ID =  chnl_org_rec.CHNL_ORG_ID AND
             b.CONTACT_GROUP_CD = 'CO' AND
             b.REASON_TYPE_CD = 'Ship to' AND
             (b.EXPIRATION_DT is null or b.EXPIRATION_DT > TRUNC(sysdate));
   --cursor for 'Bill to' address (if sales rep)
   CURSOR address_billTo_cur IS
      SELECT a.*
        FROM address a,contact_address b
       WHERE a.ADDRESS_ID = b.ADDRESS_ID AND
             b.CONTACTING_ID =  chnl_org_rec.CHNL_ORG_ID AND
             b.CONTACT_GROUP_CD = 'CO' AND
             b.REASON_TYPE_CD = 'Bill to' AND
             (b.EXPIRATION_DT is null or b.EXPIRATION_DT > TRUNC(sysdate));
   --cursor for 'Location' address (if outlet)
   CURSOR outlet_address_location_cur IS
      SELECT a.*
        FROM address a,contact_address b
       WHERE a.ADDRESS_ID = b.ADDRESS_ID AND
             b.CONTACTING_ID =  outlet_rec.outlet_ID AND
             b.CONTACT_GROUP_CD = 'OT'         AND
             b.REASON_TYPE_CD = 'Location' AND
             (b.EXPIRATION_DT is null or b.EXPIRATION_DT > TRUNC(sysdate));
   --cursor for 'Ship to' address (if outlet)
   CURSOR outlet_address_shipTo_cur IS
      SELECT a.*
        FROM address a,contact_address b
       WHERE a.ADDRESS_ID = b.ADDRESS_ID AND
             b.CONTACTING_ID =  outlet_rec.outlet_ID AND
             b.CONTACT_GROUP_CD = 'OT'         AND
             b.REASON_TYPE_CD = 'Ship to' AND
             (b.EXPIRATION_DT is null or b.EXPIRATION_DT > TRUNC(sysdate));
   --cursor for 'Bill to' address (if outlet)
   CURSOR outlet_address_billTo_cur IS
      SELECT a.*
        FROM address a,contact_address b
       WHERE a.ADDRESS_ID = b.ADDRESS_ID AND
             b.CONTACTING_ID =  outlet_rec.outlet_ID AND
             b.CONTACT_GROUP_CD = 'OT'         AND
             b.REASON_TYPE_CD = 'Bill to' AND
             (b.EXPIRATION_DT is null or b.EXPIRATION_DT > TRUNC(sysdate));
      --Record to hold address_location_cur rows
   address_rec address_location_cur%ROWTYPE;
    CURSOR chnl_org_ivr_priority IS
      SELECT 'Y'
        FROM  CHANNEL_ORG_ATTRIBUTE
       WHERE CHNL_ORG_ID = chnl_org_rec.CHNL_ORG_ID AND
             BUS_PARTNER_ATTRIBUTE_ID = 7 ;
v_chnl_org_ivr_priority_ind  CHAR(1):= 'N';

   --EXCEPTIONS
   channel_org_not_found EXCEPTION;
   outlet_not_found EXCEPTION;
   sales_rep_not_found EXCEPTION;
   location_address_not_found EXCEPTION;
   shipTo_address_not_found EXCEPTION;
   billTo_address_not_found EXCEPTION;
BEGIN
    /*Find CHANNEL_ORGANIZATION ENTRY*/
   OPEN chnl_org_cur;
   FETCH chnl_org_cur INTO chnl_org_rec;
   IF chnl_org_cur%NOTFOUND
   THEN
      RAISE channel_org_not_found;
   END IF;
   CLOSE chnl_org_cur;
   chnl_org_desc_out := chnl_org_rec.CHNL_ORG_DES;
   chnl_org_type_cd := chnl_org_rec.CHNL_ORG_TYPE_CD;
   /* Check for IVR routing priority */
   OPEN chnl_org_ivr_priority ;
   FETCH  chnl_org_ivr_priority  into v_chnl_org_ivr_priority_ind ;
   CLOSE chnl_org_ivr_priority;
   ivr_privilege_routing_ind := v_chnl_org_ivr_priority_ind;
   /* pass the org type code for Quebec dealers */
   /*If this is a dealer then look for sales rep otherwise look for outlet */
   /*Find SALES_REP*/
   OPEN sales_rep_cur;
   FETCH sales_rep_cur INTO sales_rep_rec;
   IF sales_rep_cur%FOUND
      THEN
        CLOSE sales_rep_cur;
        user_desc_out := sales_rep_rec.first_name || ' ' || sales_rep_rec.last_name;
        /* Find 'Province Code' for this dealer */
        OPEN address_location_cur;
        FETCH address_location_cur INTO address_rec;
        IF address_location_cur%FOUND THEN
            CLOSE address_location_cur;
            address_province_cd := address_rec.province_cd;
        ELSE
            /* - look for 'Ship To' address second */
            OPEN address_shipTo_cur;
            FETCH address_shipTo_cur INTO address_rec;
            IF address_shipTo_cur%FOUND THEN
                CLOSE address_shipTo_cur;
                address_province_cd := address_rec.province_cd;
            ELSE
                /* - look for 'Bill To' address second */
                OPEN address_billTo_cur;
                FETCH address_billTo_cur INTO address_rec;
                IF address_billTo_cur%FOUND THEN
                    CLOSE address_billTo_cur;
                    address_province_cd := address_rec.province_cd;
                END IF;
            END IF;
        END IF;
        RETURN;
   END IF;
    CLOSE sales_rep_cur;
    /*Find OUTLET*/
    OPEN outlet_cur;
    FETCH outlet_cur INTO outlet_rec;
    IF outlet_cur%NOTFOUND
    THEN
       RAISE outlet_not_found;
    END IF;
    CLOSE outlet_cur;
    user_desc_out := outlet_rec.outlet_des;
    /* Find 'address and hours of operation' for this outlet */
    for outlet_address_hours_rec in outlet_address_hours_cur loop
        If  outlet_address_hours_rec.sequence_value='1' Then
		    sequence_value_1 := outlet_address_hours_rec.sequence_value;
            outlet_open_time_1 := outlet_address_hours_rec.open_time;
            outlet_close_time_1 := outlet_address_hours_rec.close_time;
            ---street_name := outlet_address_hours_rec.street_number || ' ' || outlet_address_hours_rec.street_name  || ' ' || outlet_address_hours_rec.street_type_cd;
            street_name := outlet_address_hours_rec.str;
            city_name := outlet_address_hours_rec.city_name;
            location_province_cd := outlet_address_hours_rec.province_cd;
            postal_cd := outlet_address_hours_rec.postal_cd;
            ---phone_number := outlet_address_hours_rec.npa || outlet_address_hours_rec.nxx || outlet_address_hours_rec.nnnn;
            phone_number := outlet_address_hours_rec.phone;
        Elsif outlet_address_hours_rec.sequence_value='2' Then
	 	    sequence_value_2 := outlet_address_hours_rec.sequence_value;
            outlet_open_time_2 := outlet_address_hours_rec.open_time;
            outlet_close_time_2 := outlet_address_hours_rec.close_time;
        Elsif outlet_address_hours_rec.sequence_value='3' Then
	 	    sequence_value_3 := outlet_address_hours_rec.sequence_value;
            outlet_open_time_3 := outlet_address_hours_rec.open_time;
            outlet_close_time_3 := outlet_address_hours_rec.close_time;
        Elsif outlet_address_hours_rec.sequence_value='4' Then
	 	    sequence_value_4 := outlet_address_hours_rec.sequence_value;
            outlet_open_time_4 := outlet_address_hours_rec.open_time;
            outlet_close_time_4 := outlet_address_hours_rec.close_time;
        Elsif outlet_address_hours_rec.sequence_value='5' Then
	 	    sequence_value_5 := outlet_address_hours_rec.sequence_value;
            outlet_open_time_5 := outlet_address_hours_rec.open_time;
            outlet_close_time_5 := outlet_address_hours_rec.close_time;
        Elsif outlet_address_hours_rec.sequence_value='6' Then
	 	    sequence_value_6 := outlet_address_hours_rec.sequence_value;
            outlet_open_time_6 := outlet_address_hours_rec.open_time;
            outlet_close_time_6 := outlet_address_hours_rec.close_time;
        Elsif outlet_address_hours_rec.sequence_value='7' Then
	 	    sequence_value_7 := outlet_address_hours_rec.sequence_value;
            outlet_open_time_7 := outlet_address_hours_rec.open_time;
            outlet_close_time_7 := outlet_address_hours_rec.close_time;
       End If;
    end loop;
    /* Find 'Province Code' for this outlet */
    OPEN outlet_address_location_cur;
    FETCH outlet_address_location_cur INTO address_rec;
    IF outlet_address_location_cur%FOUND THEN
        CLOSE outlet_address_location_cur;
        address_province_cd := address_rec.province_cd;
    ELSE
        /* - look for 'Ship To' address second */
        OPEN outlet_address_shipTo_cur;
        FETCH outlet_address_shipTo_cur INTO address_rec;
        IF outlet_address_shipTo_cur%FOUND THEN
            CLOSE outlet_address_shipTo_cur;
            address_province_cd := address_rec.province_cd;
        ELSE
            /* - look for 'Bill To' address second */
            OPEN outlet_address_billTo_cur;
            FETCH outlet_address_billTo_cur INTO address_rec;
            IF outlet_address_billTo_cur%FOUND THEN
                CLOSE outlet_address_billTo_cur;
                address_province_cd := address_rec.province_cd;
            END IF;
        END IF;
    END IF;
    RETURN;
EXCEPTION
   WHEN channel_org_not_found
  THEN
      CLOSE chnl_org_cur;
      RAISE_APPLICATION_ERROR(-20000,'Package CHANNEL#.GetMoreUserInformation: No channel_organization record found.');
   WHEN outlet_not_found
  THEN
      CLOSE outlet_cur;
      RAISE_APPLICATION_ERROR(-20001,'Package CHANNEL#.GetMoreUserInformation: No sales rep or outlet record found.');
   WHEN sales_rep_not_found
   THEN
      CLOSE sales_rep_cur;
      RAISE_APPLICATION_ERROR(-20002,'Package CHANNEL#.GetMoreUserInformation: No sales_representativet record found.');
   WHEN OTHERS
   THEN
      CLOSE chnl_org_cur;
      CLOSE outlet_cur;
      CLOSE sales_rep_cur;
      RAISE;
END GetMoreUserInformation;

END CHANNEL#;
/
