/* Formatted on 2007/07/09 10:51 (Formatter Plus v4.5.2) */
CREATE OR REPLACE PACKAGE portal_notification_pkg
AS   
-------------------------------------------------------------------------
-- IMPORTANT NOTE !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
-- Please increment the version_no variable value when you make changes to this package 
-- to reflect version changes.
 ------------------------------------------------------------------------
-- description: Package eas_utility_pkg containing procedures
--    for porting data retrieval/update from CODS database
--
-- Date           Developer           Modifications

-- 07-27-2007     Marina Kuper    Added saveregreminderdetails, getsuppratactivationind, isregistrationreminderexist, 
--                                getlastebillnotificationsent, getlastregremindersent
-- 04-24-2009     Tsz Chung TOng  Fixed getbillnotificationcontacts to return records with contact_status_cd = NULL (defect PROD00138014)
-- 09-02-2009     Mujeeb Waraich  Added getBillNotifiConDetForEPost, getBillNotifiHistoryForEPost
--                                    createBillNotifiConDetForEPost, cancelBillNotifiConDetForEPost
--                                    updateBillNotifiConDetForEPost, expireBillNotificationDetails       

-- 10-14-2009     Daniel Canelea  When inserting in PORTAL_USER_CONTACT_ADDR and PORTAL_USER_NOTIF_ADDR
--                                the expiration date will be set to SYSDATE+60 and the validation process 
--                                will set it to NULL upon validation . This is applicable only for new
--                                email addresses that will have to be validated
-- 11-20-2009     Tsz Chung Tong  Fixed getBillNotifiConDetForEPost to return only non-expired contacts
--				  Daniel Canelea  Rolled back changes to saveBillNotificationDetails - N/A for EPOST
-- 11-26-2009     Daniel Canelea  Updated createBillNotifiConDetForEPost to accept portal user ID as 
--                                input parameter ; ONLY if -1 will look it up
-- 12-04-2009     Daniel Canelea  Updated getBillNotifiConDetForEPost to avoid returning multiple
--                                portal user id contacts for a single BAN 
-- 12-05-2009     Daniel Canelea  Fixed getbillnotificationcontacts to return same names for the columns
--                                in the 2 different SELECT statements
-- 12-07-2009     Daniel Canelea  Fixed saveBillNotificationDetails to not insert duplicates (possible when
--                                creating/cancelling EPOST and creating EBILL are executed in rapid sequence
--                                and CRDB Loader doesn't kick in to clean the staging table)
--                                Fixed createBillNotifiConDetForEPost to update MOST_RECENT_IND to 'N' for
--                                any previous records of the same CLIENT_ACCOUNT_ID
-- 02-03-2010     Daniel Canelea  Rolled back EPOST changes . The Following functions were removed :
--                                    - createBillNotifiConDetForEPost
--                                    - cancelBillNotifiConDetForEPost
--                                To be removed in subsequent releases :
--                                    - updateBillNotifiConDetForEPost
--                                    - getBillNotifiConDetForEPost
--                                    - getBillNotifiHistoryForEPost
-- 03-19-2010     Tsz chung Tong  Fixed getbillnotificationcontacts for defect PROD00165657. %NOTFOUND cursor attribute did not work because
--                                cursor was not fetched yet. Added a pre-check query to check if there exists any record.
-- 04-09-2010     Tsz Chung Tong  Put back what Daniel had removed on 02-03-2010 to make our ePost support available post-May release.
--  May-11-2012     Naresh Annabathula     Added getVersion function for shakedown
-- May 14, 2020  Biao Wang  Decomissioned unused store procedures saveregreminderdetails, isregistrationreminderexist and getlastregremindersent
-------------------------------------------------------------------------
   TYPE refcursor IS REF CURSOR;

   unprocessed               CONSTANT VARCHAR2 (2)  := 'UP';
   successfully_processed    CONSTANT VARCHAR2 (2)  := 'SP';
   in_error                  CONSTANT VARCHAR2 (2)  := 'ER';
   invoice                   CONSTANT VARCHAR2 (2)  := 'IN';
   e_bill_stage              CONSTANT VARCHAR2 (10) := 'EBILLSTAGE';
   e_mail_status_pending     CONSTANT VARCHAR2 (2)  := 'P';
   e_mail_status_valid       CONSTANT VARCHAR2 (2)  := 'V';
   e_mail_status_new         CONSTANT VARCHAR2 (1)  := NULL;
   notification_type_sms     CONSTANT NUMBER        := 1;
   notification_type_email   CONSTANT NUMBER        := 5;
   most_recent_indicator     CONSTANT VARCHAR2 (1)  := 'Y';
   user_id                   CONSTANT VARCHAR2 (9)  := 'CLIENTAPI';
   email_status_gp_cd        CONSTANT VARCHAR2 (9)  := 'EMAILADDR';
   cont_point_purp_cd        CONSTANT VARCHAR2 (2)  := 'EP';
   -- package version constant
   version_no          CONSTANT VARCHAR2(10)       := '2020.05.01';

     FUNCTION getVersion RETURN VARCHAR2;
     
   PROCEDURE getbillnotificationcontacts (
      pi_ban        IN       NUMBER,
      po_contacts   OUT      refcursor
   );

   PROCEDURE savebillnotificationdetails (
      pi_ban                IN   NUMBER,
      pi_portal_user_id     IN   NUMBER,
      pi_contacts           IN   contacts_t,
      pi_status_group_cd    IN   VARCHAR2,
      pi_application_code   IN   VARCHAR2
   );

   PROCEDURE getsuppratactivationind (
      pi_ban                      IN       NUMBER,
      po_supr_at_activation_ind   OUT      VARCHAR2
   );

   PROCEDURE getlastebillnotificationsent (
      pi_ban            IN       NUMBER,
      po_last_contact   OUT      refcursor
   );

   FUNCTION getphonenumberbysubscriptionid (
      pi_ban               IN   NUMBER,
      pi_subscription_id   IN   VARCHAR2
   )
      RETURN NUMBER;
   
   PROCEDURE getBillNotifiConDetForEPost (
      pi_ban        IN       NUMBER,
      po_contacts   OUT      refcursor
   ); 
  
   PROCEDURE getBillNotifiHistoryForEPost (
      pi_ban        IN       NUMBER,
      po_history   OUT      refcursor
   );
   
	PROCEDURE createBillNotifiConDetForEPost (
       pi_ban                       IN  NUMBER,
       pi_act_type_epost            IN  NUMBER,
       pi_act_action_registration   IN  NUMBER,
       pi_act_reason_cus_req        IN  NUMBER,
       pi_act_type_paper_inv_sup    IN  NUMBER,
       pi_act_action_activation     IN  NUMBER,
       pi_act_reason_epost_reg      IN  NUMBER,
       pi_source_ref_id             IN  VARCHAR2,
       pi_notification_address      IN  VARCHAR2,
       pi_portal_user_id            IN  NUMBER
   );
   
   PROCEDURE cancelBillNotifiConDetForEPost (
       pi_ban                       IN  NUMBER,
       pi_act_type_epost            IN  NUMBER,
       pi_act_action_cancellation   IN  NUMBER,
       pi_act_reason_cus_req        IN  NUMBER,
       pi_act_type_paper_inv_sup    IN  NUMBER,
       pi_act_action_cancel         IN  NUMBER,
       pi_act_reason_epost_cancel   IN  NUMBER,
       pi_source_ref_id             IN  VARCHAR2 
   );

   PROCEDURE updateBillNotifiConDetForEPost (
        pi_ban                      IN  NUMBER,
        pi_bill_notify_enabled      IN  NUMBER,
        pi_notification_address     IN  VARCHAR2
   	
   );
   
   PROCEDURE expireBillNotificationDetails (
        pi_ban       IN  NUMBER
   );
     
END portal_notification_pkg;
/

SHO err

CREATE OR REPLACE PACKAGE BODY portal_notification_pkg
AS
 FUNCTION getVersion RETURN VARCHAR2
   IS
   BEGIN
	   RETURN version_no;
	END getVersion;
   PROCEDURE getbillnotificationcontacts (
      pi_ban        IN       NUMBER,
      po_contacts   OUT      refcursor
   )
   IS
	  --check if there exists record first in staging table first
	  --This is the same query as query A except it returns only the count
	CURSOR c_contactsCount
	IS
	  	SELECT COUNT(*)
	  	FROM stage_ebill_notif_pref
            WHERE ban = pi_ban
              AND status_gp_cd = e_bill_stage
              AND status_cd IN (unprocessed, in_error)
         ORDER BY load_dt DESC;
      
       mContactsCount  NUMBER;
   BEGIN
       OPEN c_contactsCount;
       FETCH c_contactsCount INTO mContactsCount;
       IF c_contactsCount%ISOPEN THEN
            CLOSE c_contactsCount;
       END IF;
       
       IF mContactsCount > 0 THEN
	      OPEN po_contacts FOR --query A
	         SELECT   contact_mechanism_type_id, contact_address_txt,
	                  e_mail_status_pending AS contact_status_cd
	             FROM stage_ebill_notif_pref
	            WHERE ban = pi_ban
	              AND status_gp_cd = e_bill_stage
	              AND status_cd IN (unprocessed, in_error)
	         ORDER BY load_dt DESC;
		ELSE
         OPEN po_contacts FOR --query B
            SELECT   contact_mechanism_type_id, contact_address_txt,
                     contact_status_cd
                FROM portal_user_notification_addr na,
                     portal_user_contact_addr pa
               WHERE na.client_account_id IN (SELECT client_account_id
                                                FROM client_account
                                               WHERE ban = pi_ban)
                 AND na.effective_dt <= SYSDATE
                 AND (   na.expiration_dt > SYSDATE
                      OR na.expiration_dt IS NULL
                     )
                 AND na.portal_user_contact_addr_id =
                                               pa.portal_user_contact_addr_id
                 AND pa.contact_point_purpose_cd = invoice
                 AND (   pa.contact_mechanism_type_id = notification_type_sms
                      OR (    pa.contact_mechanism_type_id =
                                                      notification_type_email
                          AND (contact_status_cd IN (e_mail_status_pending,
                                                    e_mail_status_valid
                                                   )
                               OR contact_status_cd IS NULL --equivalent to e_mail_status_new
                               ) 
                         )
                     )
            ORDER BY contact_mechanism_type_id;
      	END IF;
   EXCEPTION
      WHEN NO_DATA_FOUND
      THEN
         OPEN po_contacts FOR
            SELECT NULL
              FROM DUAL;
      WHEN OTHERS
      THEN
         IF (po_contacts%ISOPEN)
         THEN
            CLOSE po_contacts;
         END IF;

         raise_application_error (
            -20160,
               'Get Bill Notification Contacts Failed. Oracle:(['
            || SQLCODE
            || '] ['
            || SQLERRM
            || '])'
         );
   END getbillnotificationcontacts;

   
------------------------------------------------------------------------------------------------------------------------
   PROCEDURE savebillnotificationdetails (
      pi_ban                IN   NUMBER,
      pi_portal_user_id     IN   NUMBER,
      pi_contacts           IN   contacts_t,
      pi_status_group_cd    IN   VARCHAR2,
      pi_application_code   IN   VARCHAR2
   )
   IS
      v_count NUMBER;

   BEGIN
      IF  pi_contacts IS NOT NULL AND pi_contacts.COUNT > 0
      THEN
         FOR i IN 1 .. pi_contacts.COUNT
         LOOP
         	SELECT COUNT(*) INTO v_count
         	  FROM stage_ebill_notif_pref
         	 WHERE ban = pi_ban
         	   AND portal_user_id = pi_portal_user_id
         	   AND contact_mechanism_type_id = pi_contacts (i).contact_mechanism_id
         	   AND UPPER(contact_address_txt) = UPPER(pi_contacts (i).contact_address)
         	   AND status_gp_cd = pi_status_group_cd;
         	
			IF v_count = 0
			THEN
	            INSERT INTO stage_ebill_notif_pref
	                        (stage_ebill_notif_pref_id,
	                         portal_user_id, ban,
	                         contact_mechanism_type_id,
	                         contact_address_txt, status_gp_cd,
	                         status_cd, user_last_modify)
	                 VALUES (stage_ebill_notif_pref_seq.NEXTVAL,
	                         pi_portal_user_id, pi_ban,
	                         pi_contacts (i).contact_mechanism_id,
	                         pi_contacts (i).contact_address, pi_status_group_cd,
	                         unprocessed, UPPER (pi_application_code));
      		END IF;
         END LOOP;
      END IF;

      COMMIT;
   EXCEPTION
      WHEN OTHERS
      THEN
         raise_application_error (
            -20160,
               'SaveBillNotificationDetails Failed. Oracle:(['
            || SQLCODE
            || '] ['
            || SQLERRM
            || '])'
         );
   END savebillnotificationdetails;


----------------------------------------------------------------------------------------------------------------------------------------------

   PROCEDURE getsuppratactivationind (
      pi_ban                      IN       NUMBER,
      po_supr_at_activation_ind   OUT      VARCHAR2
   )
   IS
   BEGIN
      SELECT suppress_paper_at_actvn_ind
        INTO po_supr_at_activation_ind
        FROM client_account
       WHERE ban = pi_ban;
   EXCEPTION
      WHEN NO_DATA_FOUND
      THEN
         po_supr_at_activation_ind := NULL;
      WHEN OTHERS
      THEN
         raise_application_error (
            -20160,
               'Get Suppression At Activation Indicator Failed. Oracle:(['
            || SQLCODE
            || '] ['
            || SQLERRM
            || '])'
         );
   END getsuppratactivationind;


----------------------------------------------------------------------------------------------------------------------
   PROCEDURE getlastebillnotificationsent (
      pi_ban            IN       NUMBER,
      po_last_contact   OUT      refcursor
   )
   IS
   BEGIN
      OPEN po_last_contact FOR
         SELECT   contact_mechanism_type_id, contact_address_txt,
                  contact_status_cd, last_notification_date
             FROM portal_user_notification_addr na,
                  portal_user_contact_addr pa
            WHERE na.client_account_id IN (SELECT client_account_id
                                             FROM client_account
                                            WHERE ban = pi_ban)
              AND na.portal_user_contact_addr_id =
                                               pa.portal_user_contact_addr_id
              AND na.last_notification_date IS NOT NULL
              AND pa.contact_point_purpose_cd = invoice
              AND ( pa.contact_mechanism_type_id = notification_type_sms
              OR  pa.contact_mechanism_type_id = notification_type_email )
                   /*    AND contact_status_cd IN (e_mail_status_pending,
                                                 e_mail_status_valid,
                                                 e_mail_status_new
                                                )
                      )
                  ) */
         ORDER BY last_notification_date DESC;
   EXCEPTION
      WHEN NO_DATA_FOUND
      THEN
         OPEN po_last_contact FOR
            SELECT NULL
              FROM DUAL;
      WHEN OTHERS
      THEN
         IF (po_last_contact%ISOPEN)
         THEN
            CLOSE po_last_contact;
         END IF;

         raise_application_error (
            -20160,
               'Get Last EBill Notification Sent Failed. Oracle:(['
            || SQLCODE
            || '] ['
            || SQLERRM
            || '])'
         );
   END getlastebillnotificationsent;


----------------------------------------------------------------------------------------------------------------------
   FUNCTION getphonenumberbysubscriptionid (
      pi_ban               IN   NUMBER,
      pi_subscription_id   IN   VARCHAR2
   )
      RETURN NUMBER
   IS
      p_result   VARCHAR2 (16);
   BEGIN
      IF pi_subscription_id IS NULL
      THEN
         RETURN 0;
      ELSE
         SELECT cell_phone_no
           INTO p_result
           FROM (SELECT   s.cell_phone_no
                     FROM client_account ca,
                          account_subscription_assoc asa,
                          subscription s
                    WHERE ca.ban = pi_ban
                      AND asa.client_account_id = ca.client_account_id
                      AND s.subscription_id = asa.subscription_id
                      AND s.subscription_id = pi_subscription_id
                 ORDER BY asa.acct_subs_assoc_seq_no DESC)
          WHERE ROWNUM <= 1;
      END IF;

      RETURN p_result;
   EXCEPTION
      WHEN NO_DATA_FOUND
      THEN
         RETURN 0;
      WHEN OTHERS
      THEN
         raise_application_error (
            -20160,
               'Get Phone Number by Subscription Id Failed. Oracle:(['
            || SQLCODE
            || '] ['
            || SQLERRM
            || '])'
         );
   END getphonenumberbysubscriptionid;
   
   ----------------------------------------------------------------------------------------------------------------------
   
   PROCEDURE getBillNotifiConDetForEPost (
      pi_ban        IN       NUMBER,
      po_contacts   OUT      refcursor
   )
   IS
   BEGIN
    OPEN po_contacts FOR
    SELECT  DISTINCT puca.contact_address_txt, puna.expiration_dt, caa.source_reference_id,
            puca.portal_user_id, st.des
        FROM client_account ca, 
            client_account_activity caa, 
            portal_user_notification_addr puna,
            portal_user_contact_addr puca,
            email_address ea,
            status_type st
        WHERE ca.ban = pi_ban
            AND ca.client_account_id = caa.client_account_id
            AND ca.client_account_id = puna.client_account_id
            AND puca.portal_user_contact_addr_id = puna.portal_user_contact_addr_id
            AND puca.contact_address_txt = ea.email_address_txt 
            AND ea.addr_validation_status_gp_cd = st.status_gp_cd 
            AND ea.addr_validation_status_cd = st.status_cd
            AND (puna.expiration_dt > SYSDATE
              OR puna.expiration_dt IS NULL
             )
            ORDER BY puna.expiration_dt DESC;
   EXCEPTION
      WHEN NO_DATA_FOUND
      THEN
         OPEN po_contacts FOR
            SELECT NULL
              FROM DUAL;
      WHEN OTHERS
      THEN
         IF (po_contacts%ISOPEN)
         THEN
            CLOSE po_contacts;
         END IF;

         raise_application_error (
            -20160,
               'Get Bill Notification Contact Detail For EPost Failed. Oracle:(['
            || SQLCODE
            || '] ['
            || SQLERRM
            || '])'
         ); 
     
   END getBillNotifiConDetForEPost;
   
   ----------------------------------------------------------------------------------------------------------------------

   PROCEDURE getBillNotifiHistoryForEPost (
      pi_ban        IN       NUMBER,
      po_history   OUT      refcursor
   )
   IS
      v_count   NUMBER (4);
   BEGIN
     SELECT count(*)
     INTO   v_count
     FROM client_account ca,
          portal_user_notification_addr puna,
          portal_user_contact_addr puca
     WHERE ca.ban = pi_ban
          AND ca.client_account_id = puna.client_account_id
          AND puca.portal_user_contact_addr_id = puna.portal_user_contact_addr_id;
     
     
    IF v_count > 0
     THEN
            OPEN po_history FOR
            SELECT DISTINCT caa.source_reference_id, caa.most_recent_ind, puca.contact_address_txt, 
                   caa.effective_start_ts, caa.effective_end_ts, act.activity_type_txt,  
                   act.activity_type_fr_txt, ar.activity_reason_txt , ar.activity_reason_fr_txt, 
                   ac.activity_action_txt, ac.activity_action_fr_txt  
               FROM client_account_activity caa,
                    client_account ca,
                    activity_type act,
                    activity_action ac,
                    activity_reason ar,
                    portal_user_notification_addr puna,
                    portal_user_contact_addr puca
               WHERE ca.ban = pi_ban 
                    AND ca.client_account_id = caa.client_account_id 
                    AND caa.activity_type_id = act.activity_type_id
                    AND caa.activity_action_id = ac.activity_action_id 
                    AND caa.activity_reason_id = ar.activity_reason_id
                    AND ca.client_account_id = puna.client_account_id
                    AND puca.portal_user_contact_addr_id = puna.portal_user_contact_addr_id
                    ORDER BY caa.effective_start_ts DESC;
     ELSE
        OPEN po_history FOR
        SELECT DISTINCT caa.source_reference_id, caa.most_recent_ind, null, 
                   caa.effective_start_ts, caa.effective_end_ts, act.activity_type_txt,  
                   act.activity_type_fr_txt, ar.activity_reason_txt , ar.activity_reason_fr_txt, 
                   ac.activity_action_txt, ac.activity_action_fr_txt  
               FROM client_account_activity caa,
                    client_account ca,
                    activity_type act,
                    activity_action ac,
                    activity_reason ar
               WHERE ca.ban = pi_ban 
                    AND ca.client_account_id = caa.client_account_id 
                    AND caa.activity_type_id = act.activity_type_id
                    AND caa.activity_action_id = ac.activity_action_id 
                    AND caa.activity_reason_id = ar.activity_reason_id
                    ORDER BY caa.effective_start_ts DESC;
     END IF; 
   EXCEPTION
      WHEN NO_DATA_FOUND
      THEN
         OPEN po_history FOR
            SELECT NULL
              FROM DUAL;
      WHEN OTHERS
      THEN
         IF (po_history%ISOPEN)
         THEN
            CLOSE po_history;
         END IF;

         raise_application_error (
            -20160,
               'Get Bill Notification History For EPost Failed. Oracle:(['
            || SQLCODE
            || '] ['
            || SQLERRM
            || '])'
         ); 
     
   END getBillNotifiHistoryForEPost;
   
PROCEDURE createBillNotifiConDetForEPost (
       pi_ban                       IN  NUMBER,
       pi_act_type_epost            IN  NUMBER,
       pi_act_action_registration   IN  NUMBER,
       pi_act_reason_cus_req        IN  NUMBER,
       pi_act_type_paper_inv_sup    IN  NUMBER,
       pi_act_action_activation     IN  NUMBER,
       pi_act_reason_epost_reg      IN  NUMBER,
       pi_source_ref_id             IN  VARCHAR2,
       pi_notification_address      IN  VARCHAR2,
       pi_portal_user_id            IN  NUMBER
   )
   IS
        v_client_account_id NUMBER;
        v_portal_user_contact_addr_seq NUMBER;
        v_portal_user_id NUMBER;
        v_count NUMBER;
   BEGIN
        
        SELECT client_account_id
            INTO v_client_account_id
            FROM client_account
            WHERE ban = pi_ban;

        UPDATE client_account_activity
            SET most_recent_ind = 'N'
            WHERE client_account_id = v_client_account_id;

        INSERT INTO client_account_activity
                    (client_account_activity_id,
                    client_account_id,
                    activity_type_id,
                    activity_action_id,
                    activity_reason_id,
                    source_reference_id,
                    effective_start_ts,
                    most_recent_ind,
                    create_ts,
                    last_updt_ts,
                    create_user_id,
                    last_updt_user_id)
               VALUES (client_account_activity_seq.NEXTVAL,
                    v_client_account_id,
                    pi_act_type_epost,
                    pi_act_action_registration,
                    pi_act_reason_cus_req,
                    pi_source_ref_id,
                    sysdate,
                    most_recent_indicator,
                    sysdate,
                    sysdate,
                    user_id,
                    user_id);  
                    
         INSERT INTO client_account_activity
                    (client_account_activity_id,
                    client_account_id,
                    activity_type_id,
                    activity_action_id,
                    activity_reason_id,
                    source_reference_id,
                    effective_start_ts,
                    most_recent_ind,
                    create_ts,
                    last_updt_ts,
                    create_user_id,
                    last_updt_user_id)
               VALUES (client_account_activity_seq.NEXTVAL,
                    v_client_account_id,
                    pi_act_type_paper_inv_sup,
                    pi_act_action_activation,
                    pi_act_reason_epost_reg,
                    pi_source_ref_id,
                    sysdate,
                    most_recent_indicator,
                    sysdate,
                    sysdate,
                    user_id,
                    user_id);  
                    
            IF(pi_portal_user_id = -1)
	            THEN
		            SELECT portal_user_id
		                INTO v_portal_user_id
		                    FROM (SELECT portal_user_id
		                        FROM portal_administered_account
		                        WHERE client_account_id = v_client_account_id
		                        ORDER BY effective_dt DESC)
		                    WHERE ROWNUM = 1;
                ELSE
                	v_portal_user_id := pi_portal_user_id;
            END IF;
            
            SELECT portal_user_contact_addr_seq.nextval 
                INTO v_portal_user_contact_addr_seq
                FROM dual;
                
            SELECT count(*)
                INTO v_count
                FROM email_address
                WHERE upper(email_address_txt) = upper(pi_notification_address);
                
            IF (v_count =0) 
                -- only add new email address 
                THEN  
                
                INSERT INTO email_address
                            (email_address_txt,
                            addr_validation_status_gp_cd,
                            addr_validation_status_cd,
                            load_dt,
                            update_dt,
                            user_last_modify
                            )
                       VALUES (pi_notification_address,
                            email_status_gp_cd,
                            e_mail_status_pending,
                            sysdate,
                            sysdate,
                            user_id 
                            );
           END IF;
                
            INSERT INTO portal_user_contact_addr
                        (portal_user_contact_addr_id,
                        portal_user_id,
                        contact_address_txt,
                        contact_point_purpose_cd,
                        effective_dt,
                        expiration_dt,
                        load_dt,
                        update_dt,
                        user_last_modify,
                        contact_mechanism_type_id,
                        contact_status_gp_cd,
                        contact_status_cd
                        )
                   VALUES (v_portal_user_contact_addr_seq,
                         v_portal_user_id,
                         pi_notification_address,
                         cont_point_purp_cd,
                         sysdate,
                         to_date(to_char(sysdate + 60, 'mm/dd/yyyy'), 'mm/dd/yyyy'),
                         sysdate,
                         sysdate,
                         user_id,
                         notification_type_email,
                         email_status_gp_cd,
                         e_mail_status_pending
                         );
                           
            INSERT INTO portal_user_notification_addr
                       (portal_user_notif_addr_id,
                       portal_user_contact_addr_id,
                       client_account_id,
                       effective_dt,
                       expiration_dt,
                       load_dt,
                       update_dt,
                       user_last_modify
                       )
                   VALUES (portal_user_notif_addr_seq.NEXTVAL,
                        v_portal_user_contact_addr_seq,
                        v_client_account_id,
                        sysdate,
                        to_date(to_char(sysdate + 60, 'mm/dd/yyyy'), 'mm/dd/yyyy'),
                        sysdate,
                        sysdate,
                        user_id
                        );
                            
            
         COMMIT;   
        
   EXCEPTION
      WHEN NO_DATA_FOUND
      THEN
         raise_application_error (
            -20160,
               'Data not found in Client Account or Portal Administered Account table. Oracle:(['
            || SQLCODE
            || '] ['
            || SQLERRM
            || '])'
         );
      WHEN OTHERS
      THEN
         raise_application_error (
            -20160,
               'Create Bill Notification ContactDetails For EPost Failed. Oracle:(['
            || SQLCODE
            || '] ['
            || SQLERRM
            || '])'
         );
   END createBillNotifiConDetForEPost; 
   
   ----------------------------------------------------------------------------------------------------------------------
   
   PROCEDURE cancelBillNotifiConDetForEPost (
       pi_ban                       IN  NUMBER,
       pi_act_type_epost            IN  NUMBER,
       pi_act_action_cancellation   IN  NUMBER,
       pi_act_reason_cus_req        IN  NUMBER,
       pi_act_type_paper_inv_sup    IN  NUMBER,
       pi_act_action_cancel         IN  NUMBER,
       pi_act_reason_epost_cancel   IN  NUMBER,
       pi_source_ref_id             IN  VARCHAR2 
   )
   IS
        v_client_account_id NUMBER;
        --v_portal_user_contact_addr_id NUMBER;
   BEGIN
        SELECT client_account_id
            INTO v_client_account_id
            FROM client_account
            WHERE ban = pi_ban;
            
        UPDATE client_account_activity
            SET most_recent_ind = 'N'
            WHERE client_account_id = v_client_account_id;
        
        INSERT INTO client_account_activity
                    (client_account_activity_id,
                    client_account_id,
                    activity_type_id,
                    activity_action_id,
                    activity_reason_id,
                    source_reference_id,
                    effective_start_ts,
                    most_recent_ind,
                    create_ts,
                    last_updt_ts,
                    create_user_id,
                    last_updt_user_id)
               VALUES (client_account_activity_seq.NEXTVAL,
                    v_client_account_id,
                    pi_act_type_epost,
                    pi_act_action_cancellation,
                    pi_act_reason_cus_req,
                    pi_source_ref_id,
                    sysdate,
                    most_recent_indicator,
                    sysdate,
                    sysdate,
                    user_id,
                    user_id);  
                    
         INSERT INTO client_account_activity
                    (client_account_activity_id,
                    client_account_id,
                    activity_type_id,
                    activity_action_id,
                    activity_reason_id,
                    source_reference_id,
                    effective_start_ts,
                    most_recent_ind,
                    create_ts,
                    last_updt_ts,
                    create_user_id,
                    last_updt_user_id)
               VALUES (client_account_activity_seq.NEXTVAL,
                    v_client_account_id,
                    pi_act_type_paper_inv_sup,
                    pi_act_action_cancel,
                    pi_act_reason_epost_cancel,
                    pi_source_ref_id,
                    sysdate,
                    most_recent_indicator,
                    sysdate,
                    sysdate,
                    user_id,
                    user_id);
                    
        UPDATE portal_user_notification_addr
            SET expiration_dt = sysdate
            WHERE client_account_id =  v_client_account_id
                AND  (expiration_dt IS NULL OR expiration_dt > sysdate);
       
        COMMIT;          
   EXCEPTION
      WHEN NO_DATA_FOUND
      THEN
         raise_application_error (
            -20160,
               'BAN not found in Client Account table. Oracle:(['
            || SQLCODE
            || '] ['
            || SQLERRM
            || '])'
         );
      WHEN OTHERS
      THEN
         raise_application_error (
            -20160,
               'Cancel Bill Notification ContactDetails For EPost Failed. Oracle:(['
            || SQLCODE
            || '] ['
            || SQLERRM
            || '])'
         ); 
   END cancelBillNotifiConDetForEPost;

   ----------------------------------------------------------------------------------------------------------------------
   
   PROCEDURE updateBillNotifiConDetForEPost (
    pi_ban                       IN  NUMBER,
    pi_bill_notify_enabled       IN  NUMBER,
    pi_notification_address      IN  VARCHAR2
   )
   IS
        v_email_address     VARCHAR2(60);
        v_portal_user_contact_addr_id  NUMBER;
        v_client_account_id NUMBER;
        v_portal_user_contact_addr_seq NUMBER;
        v_portal_user_id NUMBER;
        v_count NUMBER;
        v_email_count NUMBER;
   BEGIN
        SELECT COUNT(*)
            INTO v_count
            FROM portal_user_contact_addr puca 
            WHERE UPPER(contact_address_txt) = UPPER(pi_notification_address)
                AND (expiration_dt IS NULL OR expiration_dt > sysdate);
            
        SELECT client_account_id
            INTO v_client_account_id
            FROM client_account
            WHERE ban = pi_ban;
            
        SELECT  puca.portal_user_contact_addr_id
            INTO  v_portal_user_contact_addr_id  
            FROM  portal_user_contact_addr puca,
                  portal_user_notification_addr puna
            WHERE puna.client_account_id =  v_client_account_id
                  AND puna.portal_user_contact_addr_id = puca.portal_user_contact_addr_id
                  AND (puca.expiration_dt IS NULL OR puca.expiration_dt > sysdate);
       
        IF (v_count > 0)
        --email address are same
        THEN
            IF (pi_bill_notify_enabled = 1)  -- true
            THEN
                INSERT INTO portal_user_notification_addr
                       (portal_user_notif_addr_id,
                       portal_user_contact_addr_id,
                       client_account_id,
                       effective_dt,
                       load_dt,
                       update_dt,
                       user_last_modify
                       )
                   VALUES (portal_user_notif_addr_seq.NEXTVAL,
                        v_portal_user_contact_addr_id,
                        v_client_account_id,
                        sysdate,
                        sysdate,
                        sysdate,
                        user_id
                        );
            ELSE
                UPDATE portal_user_notification_addr
                SET expiration_dt = sysdate
                WHERE client_account_id =  v_client_account_id
                    AND  (expiration_dt IS NULL OR expiration_dt > sysdate);
                
            END IF;
        ELSE  -- new email address
        --update with new email adress

            SELECT portal_user_id
                INTO v_portal_user_id
                    FROM (SELECT portal_user_id
                        FROM portal_administered_account
                        WHERE client_account_id = v_client_account_id
                        ORDER BY effective_dt DESC)
                    WHERE ROWNUM = 1;
                
            SELECT portal_user_contact_addr_seq.nextval 
                    INTO v_portal_user_contact_addr_seq
                    FROM dual;
                    
            UPDATE portal_user_notification_addr
                SET expiration_dt = sysdate
                WHERE client_account_id =  v_client_account_id
                AND  (expiration_dt IS NULL OR expiration_dt > sysdate);
                
            UPDATE portal_user_contact_addr 
                SET expiration_dt = sysdate
                WHERE portal_user_contact_addr_id = v_portal_user_contact_addr_id
                AND  (expiration_dt IS NULL OR expiration_dt > sysdate);   
                
                  
                SELECT count(*)
                INTO v_email_count
                FROM email_address
                WHERE upper(email_address_txt) = upper(pi_notification_address);
                
                IF (v_email_count = 0)  
                THEN 
                    -- add only new email address   
                    INSERT INTO email_address
                                (email_address_txt,
                                addr_validation_status_gp_cd,
                                addr_validation_status_cd,
                                load_dt,
                                update_dt,
                                user_last_modify
                                )
                           VALUES (pi_notification_address,
                                email_status_gp_cd,
                                e_mail_status_pending,
                                sysdate,
                                sysdate,
                                user_id 
                                );
                END IF;
                INSERT INTO portal_user_contact_addr
                            (portal_user_contact_addr_id,
                            portal_user_id,
                            contact_address_txt,
                            contact_point_purpose_cd,
                            effective_dt,
                            expiration_dt,
                            load_dt,
                            update_dt,
                            user_last_modify,
                            contact_mechanism_type_id,
                            contact_status_gp_cd,
                            contact_status_cd
                            )
                       VALUES (v_portal_user_contact_addr_seq,
                             v_portal_user_id,
                             pi_notification_address,
                             cont_point_purp_cd,
                             sysdate,
                             to_date(to_char(sysdate + 60, 'mm/dd/yyyy'), 'mm/dd/yyyy'),
                             sysdate,
                             sysdate,
                             user_id,
                             notification_type_email,
                             email_status_gp_cd,
                             e_mail_status_pending 
                             );
                           
                INSERT INTO portal_user_notification_addr
                           (portal_user_notif_addr_id,
                           portal_user_contact_addr_id,
                           client_account_id,
                           effective_dt,
                           expiration_dt,
                           load_dt,
                           update_dt,
                           user_last_modify
                           )
                       VALUES (portal_user_notif_addr_seq.NEXTVAL,
                            v_portal_user_contact_addr_seq,
                            v_client_account_id,
                            sysdate,
                            to_date(to_char(sysdate + 60, 'mm/dd/yyyy'), 'mm/dd/yyyy'),
                            sysdate,
                            sysdate,
                            user_id
                            );
            
        END IF;
        
        COMMIT;
        
   EXCEPTION
      WHEN NO_DATA_FOUND
      THEN
         raise_application_error (
            -20160,
               'Email not found. Oracle:(['
            || SQLCODE
            || '] ['
            || SQLERRM
            || '])'
         );
      WHEN OTHERS
      THEN
         raise_application_error (
            -20160,
               'Cancel Bill Notification ContactDetails For EPost Failed. Oracle:(['
            || SQLCODE
            || '] ['
            || SQLERRM
            || '])'
         );      
   END updateBillNotifiConDetForEPost;
   
   ----------------------------------------------------------------------------------------------------------------------
   
   PROCEDURE expireBillNotificationDetails (
        pi_ban       IN  NUMBER
   )
   IS
   BEGIN
        UPDATE portal_user_contact_addr
            SET expiration_dt = sysdate
            WHERE portal_user_contact_addr_id IN (SELECT puna.portal_user_contact_addr_id
                                    FROM portal_user_notification_addr puna,
                                        client_account ca
                                    WHERE ca.ban = pi_ban
                                        AND ca.client_account_id = puna.client_account_id
                                        )
            AND (expiration_dt IS NULL OR expiration_dt > sysdate);
                
        UPDATE portal_user_notification_addr
            SET expiration_dt = sysdate
            WHERE portal_user_contact_addr_id IN (SELECT puna.portal_user_contact_addr_id
                                    FROM portal_user_notification_addr puna,
                                        client_account ca
                                    WHERE ca.ban = pi_ban
                                        AND ca.client_account_id = puna.client_account_id
                                        AND (puna.expiration_dt IS NULL OR puna.expiration_dt > sysdate));
        COMMIT;
        
   END expireBillNotificationDetails;
----------------------------------------------------------------------------------------------------------------------

END portal_notification_pkg;
/
SHO err
