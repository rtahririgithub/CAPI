
-- Start of DDL script for CHANNEL#
-- Generated 17-Jan-02  1:22:31 pm
-- from ddist-IVRADM:2

-- Package body CHANNEL#

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
             EFFECTIVE_DT <= TRUNC(SYSDATE) AND
             EXPIRATION_DT IS NULL;
   --Record to hold chnl_org_cur rows
   chnl_org_rec chnl_org_cur%ROWTYPE;
   --cursor for OUTLET
   CURSOR outlet_cur IS
      SELECT *
        FROM OUTLET
       WHERE CHNL_ORG_ID = chnl_org_rec.CHNL_ORG_ID AND
             EFFECTIVE_DT <= TRUNC(SYSDATE) AND
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
             b.EFFECTIVE_DT <= TRUNC(SYSDATE) AND
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
             EFFECTIVE_DT <= TRUNC(SYSDATE) AND
             EXPIRATION_DT IS NULL;
   --Record to hold chnl_org_cur rows
   chnl_org_rec chnl_org_cur%ROWTYPE;
   --cursor for OUTLET
   CURSOR outlet_cur IS
      SELECT *
        FROM OUTLET
       WHERE CHNL_ORG_ID = chnl_org_rec.CHNL_ORG_ID AND
             EFFECTIVE_DT <= TRUNC(SYSDATE) AND
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
             b.EFFECTIVE_DT <= TRUNC(SYSDATE) AND
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
             EFFECTIVE_DT <= TRUNC(SYSDATE) AND
             EXPIRATION_DT IS NULL;
   --Record to hold chnl_org_cur rows
   chnl_org_rec chnl_org_cur%ROWTYPE;
   --cursor for OUTLET
   CURSOR outlet_cur IS
      SELECT *
        FROM OUTLET
       WHERE CHNL_ORG_ID = chnl_org_rec.CHNL_ORG_ID AND
             EFFECTIVE_DT <= TRUNC(SYSDATE) AND
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
             b.EFFECTIVE_DT <= TRUNC(SYSDATE) AND
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
             EFFECTIVE_DT <= TRUNC(SYSDATE) AND
             EXPIRATION_DT IS NULL;
   --Record to hold chnl_org_cur rows
   chnl_org_rec chnl_org_cur%ROWTYPE;
   --cursor for OUTLET
   CURSOR outlet_cur IS
      SELECT *
        FROM OUTLET
       WHERE CHNL_ORG_ID = chnl_org_rec.CHNL_ORG_ID AND
             EFFECTIVE_DT <= TRUNC(SYSDATE) AND
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
             b.EFFECTIVE_DT <= TRUNC(SYSDATE) AND
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
PROCEDURE GetMoreUserInformation(chnl_org_cd_in VARCHAR2, user_code_in VARCHAR2, chnl_org_desc_out OUT VARCHAR2, user_desc_out OUT VARCHAR2, address_province_cd OUT VARCHAR2, chnl_org_type_cd OUT VARCHAR2)
/*
|| Overview:        Procedure to get user information (same as GetUserInformation plus additional attributes)
||
|| Author:          Peter Frei
||
|| Modifications:
||
|| 01/01/2001       Shawn Alipanah  Add Organization type code for QuebecTel dealer to be used in IVR Channel care
*/
IS
   --cursor for CHANNEL_ORGANIZATION
   CURSOR chnl_org_cur IS
      SELECT *
        FROM CHANNEL_ORGANIZATION
       WHERE CHNL_ORG_CD = chnl_org_cd_in AND
             EFFECTIVE_DT <= TRUNC(SYSDATE) AND
             EXPIRATION_DT IS NULL;
   --Record to hold chnl_org_cur rows
   chnl_org_rec chnl_org_cur%ROWTYPE;
   --cursor for OUTLET
   CURSOR outlet_cur IS
      SELECT *
        FROM OUTLET
       WHERE CHNL_ORG_ID = chnl_org_rec.CHNL_ORG_ID AND
             EFFECTIVE_DT <= TRUNC(SYSDATE) AND
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
             b.EFFECTIVE_DT <= TRUNC(SYSDATE) AND
             a.SALES_REP_CD = user_code_in;
   --Record to hold sales_rep_cur rows
   sales_rep_rec sales_rep_cur%ROWTYPE;
   --cursor for 'Location' address (if sales rep)
   CURSOR address_location_cur IS
      SELECT a.*
        FROM address a,contact_address b
       WHERE a.ADDRESS_ID = b.ADDRESS_ID AND
             b.REASON_TYPE_CD = 'Location' AND
             b.CONTACTING_ID =  chnl_org_rec.CHNL_ORG_ID;
   --cursor for 'Ship to' address (if sales rep)
   CURSOR address_shipTo_cur IS
      SELECT a.*
        FROM address a,contact_address b
       WHERE a.ADDRESS_ID = b.ADDRESS_ID AND
             b.REASON_TYPE_CD = 'Ship to' AND
             b.CONTACTING_ID =  chnl_org_rec.CHNL_ORG_ID;
   --cursor for 'Bill to' address (if sales rep)
   CURSOR address_billTo_cur IS
      SELECT a.*
        FROM address a,contact_address b
       WHERE a.ADDRESS_ID = b.ADDRESS_ID AND
             b.REASON_TYPE_CD = 'Bill to' AND
             b.CONTACTING_ID =  chnl_org_rec.CHNL_ORG_ID;
   --cursor for 'Location' address (if outlet)
   CURSOR outlet_address_location_cur IS
      SELECT a.*
        FROM address a,contact_address b
       WHERE a.ADDRESS_ID = b.ADDRESS_ID AND
             b.REASON_TYPE_CD = 'Location' AND
             b.CONTACTING_ID =  outlet_rec.outlet_ID;
   --cursor for 'Ship to' address (if outlet)
   CURSOR outlet_address_shipTo_cur IS
      SELECT a.*
        FROM address a,contact_address b
       WHERE a.ADDRESS_ID = b.ADDRESS_ID AND
             b.REASON_TYPE_CD = 'Ship to' AND
             b.CONTACTING_ID =  outlet_rec.outlet_ID;
   --cursor for 'Bill to' address (if outlet)
   CURSOR outlet_address_billTo_cur IS
      SELECT a.*
        FROM address a,contact_address b
       WHERE a.ADDRESS_ID = b.ADDRESS_ID AND
             b.REASON_TYPE_CD = 'Bill to' AND
             b.CONTACTING_ID =  outlet_rec.outlet_ID;
   --Record to hold address_location_cur rows
   address_rec address_location_cur%ROWTYPE;
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

END CHANNEL#;
/

-- End of DDL script for CHANNEL#

-- Start of DDL script for CHANNEL#
-- Generated 17-Jan-02  1:23:24 pm
-- from ddist-IVRADM:2

-- Package CHANNEL#

CREATE OR REPLACE
PACKAGE CHANNEL#

/*

|| Overview:        Package containing procedures to interface to the DIST database

||

|| Author:          Scott Lapish

||

|| Modifications:
|| 01/01/2001       Shawn Alipanah  Add Organization type code for QuebecTel dealer to be used in IVR Channel care
||
*/



IS

   FUNCTION ValidUser(chnl_org_cd_in VARCHAR2,user_code_in VARCHAR2, password_in VARCHAR2) RETURN INTEGER;

   FUNCTION ChangeUserPassword(chnl_org_cd_in VARCHAR2,user_code_in VARCHAR2, old_password_in VARCHAR2,new_password_in VARCHAR2) RETURN INTEGER;

   FUNCTION ResetPassword(chnl_org_cd_in VARCHAR2,user_code_in VARCHAR2, new_password_in VARCHAR2) RETURN INTEGER;

   PROCEDURE GetUserInformation(chnl_org_cd_in VARCHAR2, user_code_in VARCHAR2, chnl_org_desc_out OUT VARCHAR2, user_desc_out OUT VARCHAR2);

   PROCEDURE GetMoreUserInformation(chnl_org_cd_in VARCHAR2, user_code_in VARCHAR2, chnl_org_desc_out OUT VARCHAR2, user_desc_out OUT VARCHAR2, address_province_cd OUT VARCHAR2, chnl_org_type_cd OUT VARCHAR2);



END CHANNEL#;
/

-- End of DDL script for CHANNEL#
