/* Formatted on 2007/01/10 14:48 (Formatter Plus v4.8.0) */
CREATE OR REPLACE PACKAGE eas_utility_pkg
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
-- 01-05-2007     Marina Kuper    creation of package
-- 01-30-2007     Marina Kuper    Added getPortProtection
-- 02-08-2007     Marina Kuper   Changed getportprotectionforban
-- 03-06-2007     Marina Kuper   Changed isportrestricted
--  May-11-2012     Naresh Annabathula     Added getVersion function for shakedown
-------------------------------------------------------------------------
   active      CONSTANT VARCHAR2 (1) := 'A';
   suspended   CONSTANT VARCHAR2 (1) := 'S';
   reserved    CONSTANT VARCHAR2 (1) := 'R';
   cancelled   CONSTANT VARCHAR2 (1) := 'C';
   -- package version constant
   version_no          CONSTANT VARCHAR2(10)       := '3.19.4';

  FUNCTION getVersion RETURN VARCHAR2;
  
   PROCEDURE isportrestricted (
      pi_ban            IN       NUMBER,
      pi_phone_number   IN       VARCHAR2,
      pi_subscriber     IN       VARCHAR2,
      pi_status         IN       VARCHAR2,
      po_block_ind      OUT      VARCHAR2
   );

   PROCEDURE getportprotectionforsubscriber (
      pi_ban            IN       NUMBER,
      pi_phone_number   IN       VARCHAR2,
      pi_subscriber     IN       VARCHAR2,
      pi_status         IN       VARCHAR2,
      po_block_ind      OUT      VARCHAR2
   );

   PROCEDURE getportprotectionforban (
      pi_ban         IN       NUMBER,
      po_block_ind   OUT      VARCHAR2
   );

     PROCEDURE updateportrestriction (
      pi_ban              IN   NUMBER,
      pi_subscriber       IN   VARCHAR2,
      pi_block_ind        IN   VARCHAR2,
      pi_kb_operator_id   IN   NUMBER
   );
   PROCEDURE getportprotection (
      pi_ban            IN       NUMBER,
      pi_phone_number   IN       VARCHAR2,
      pi_subscriber     IN       VARCHAR2,
      pi_status         IN       VARCHAR2,
      po_block_ind      OUT      VARCHAR2
   );
END eas_utility_pkg;
/

SHO err

CREATE OR REPLACE PACKAGE BODY eas_utility_pkg
AS
 FUNCTION getVersion RETURN VARCHAR2
   IS
   BEGIN
	   RETURN version_no;
	END getVersion;
	
   PROCEDURE isportrestricted (
      pi_ban            IN       NUMBER,
      pi_phone_number   IN       VARCHAR2,
      pi_subscriber     IN       VARCHAR2,
      pi_status         IN       VARCHAR2,
      po_block_ind      OUT      VARCHAR2
   )
   IS
     BEGIN
        getportprotectionforsubscriber (pi_ban,
                                         pi_phone_number,
                                         pi_subscriber,
                                         pi_status,
                                         po_block_ind
                                        );

         IF po_block_ind IS NULL
         THEN
            getportprotectionforban (pi_ban, po_block_ind);
         END IF;
            
      po_block_ind := NVL( po_block_ind,'N');


   EXCEPTION
      WHEN OTHERS
      THEN
         raise_application_error (-20160,
                                     'Is Port Restricted Failed. Oracle:(['
                                  || SQLCODE
                                  || '] ['
                                  || SQLERRM
                                  || '])');
   END isportrestricted;

---------------------------------------------------------------------------------------------------------------
   PROCEDURE getportprotectionforban (
      pi_ban         IN       NUMBER,
      po_block_ind   OUT      VARCHAR2
   )
   IS
      v_block_ind   VARCHAR2 (1);

      CURSOR c_staging
      IS
         SELECT xfer_block_ind
           FROM xfer_block_pref_stg
          WHERE kb_ban = pi_ban AND kb_subscriber_no IS NULL AND effective_start_ts <= SYSDATE order by effective_start_ts desc ;

      CURSOR c_acct
      IS
         SELECT xfer_block_ind
           FROM acct_xfer_block_pref axbp
          WHERE axbp.client_account_id IN (SELECT ca.client_account_id
                                             FROM client_account ca
                                            WHERE ca.ban = pi_ban)
            AND axbp.effective_start_ts <= SYSDATE order by effective_start_ts desc;
   BEGIN
      OPEN c_staging;

      FETCH c_staging
       INTO v_block_ind;

      CLOSE c_staging;

      IF v_block_ind IS NOT NULL
      THEN
         po_block_ind := v_block_ind;
      ELSE
         OPEN c_acct;

         FETCH c_acct
          INTO po_block_ind;

         CLOSE c_acct;
      END IF;
   EXCEPTION
      WHEN OTHERS
      THEN
         raise_application_error
                          (-20160,
                              'Get port protection For Ban Failed. Oracle:(['
                           || SQLCODE
                           || '] ['
                           || SQLERRM
                           || '])');
   END getportprotectionforban;

------------------------------------------------------------------------------------------------------------------
   PROCEDURE getportprotectionforsubscriber (
      pi_ban            IN       NUMBER,
      pi_phone_number   IN       VARCHAR2,
      pi_subscriber     IN       VARCHAR2,
      pi_status         IN       VARCHAR2,
      po_block_ind      OUT      VARCHAR2
   )
   IS
      v_subscription_id   subscn_xfer_block_pref.subscription_id%TYPE;
      v_block_ind         VARCHAR2 (1);

      CURSOR c_staging
      IS
         SELECT xfer_block_ind
           FROM xfer_block_pref_stg
          WHERE kb_ban = pi_ban
            AND kb_subscriber_no = pi_subscriber
            AND effective_start_ts <= SYSDATE order by effective_start_ts desc;

      CURSOR c_subscriber (p_subscription NUMBER)
      IS
         SELECT xfer_block_ind
           FROM subscn_xfer_block_pref sxbp
          WHERE sxbp.subscription_id = p_subscription
            AND sxbp.effective_start_ts <= SYSDATE order by effective_start_ts desc;

      CURSOR c_active_suspended
      IS
         SELECT subscription_id
           FROM (SELECT   subscription_id
                     FROM subscription
                    WHERE cell_phone_no = pi_phone_number
                      AND current_status_cd = pi_status
                 ORDER BY subscription_id DESC)
          WHERE ROWNUM <= 1;

      CURSOR c_cancelled
      IS
         SELECT subscription_id
           FROM (SELECT   s.subscription_id
                     FROM client_account ca,
                          account_subscription_assoc asa,
                          subscription s
                    WHERE ca.ban = pi_ban
                      AND asa.client_account_id = ca.client_account_id
                      AND s.subscription_id = asa.subscription_id
                      AND s.cell_phone_no = pi_phone_number
                 ORDER BY asa.acct_subs_assoc_seq_no DESC)
          WHERE ROWNUM <= 1;
   BEGIN
      v_block_ind := NULL;

      OPEN c_staging;

      FETCH c_staging
       INTO v_block_ind;

      CLOSE c_staging;

      IF v_block_ind IS NOT NULL
      THEN
         po_block_ind := v_block_ind;
      ELSE
         CASE
            WHEN pi_status = reserved
            THEN
               v_subscription_id := NULL;
            WHEN pi_status = active OR pi_status = suspended
            THEN
               OPEN c_active_suspended;

               FETCH c_active_suspended
                INTO v_subscription_id;

               CLOSE c_active_suspended;
            ELSE
               OPEN c_cancelled;

               FETCH c_cancelled
                INTO v_subscription_id;

               CLOSE c_cancelled;
         END CASE;

         IF v_subscription_id IS NOT NULL
         THEN
            OPEN c_subscriber (v_subscription_id);

            FETCH c_subscriber
             INTO v_block_ind;

            CLOSE c_subscriber;

            po_block_ind := v_block_ind;
         END IF;
      END IF;                                      -- v_block_ind IS NOT  NULL
   EXCEPTION
      WHEN OTHERS
      THEN
         raise_application_error
                   (-20160,
                       'Get Port Protection for subscriber Failed. Oracle:(['
                    || SQLCODE
                    || '] ['
                    || SQLERRM
                    || '])');
   END getportprotectionforsubscriber;

------------------------------------------------------------------------------------------------------------------------
   PROCEDURE updateportrestriction (
      pi_ban              IN   NUMBER,
      pi_subscriber       IN   VARCHAR2,
      pi_block_ind        IN   VARCHAR2,
      pi_kb_operator_id   IN   NUMBER
   )
   IS
   BEGIN
      INSERT INTO xfer_block_pref_stg
                  (xfer_block_pref_stg_id, kb_ban, effective_start_ts,
                   kb_subscriber_no, xfer_block_ind,
                   agent_kb_operator_id
                  )
           VALUES (XFER_BLOCK_PREF_STG_SEQ.NEXTVAL, pi_ban, SYSDATE,
                   pi_subscriber, UPPER (pi_block_ind),
                   UPPER (pi_kb_operator_id)
                  );

      COMMIT;
   EXCEPTION
      WHEN OTHERS
      THEN
         raise_application_error
                              (-20160,
                                  'Update Port Restriction Failed. Oracle:(['
                               || SQLCODE
                               || '] ['
                               || SQLERRM
                               || '])');
   END updateportrestriction;
   --------------------------------------------------------------------------------------
   PROCEDURE getportprotection (
      pi_ban            IN       NUMBER,
      pi_phone_number   IN       VARCHAR2,
      pi_subscriber     IN       VARCHAR2,
      pi_status         IN       VARCHAR2,
      po_block_ind      OUT      VARCHAR2
   )
   IS
    BEGIN
      IF pi_phone_number IS NULL
      THEN
         getportprotectionforban (pi_ban, po_block_ind);
      ELSE
         getportprotectionforsubscriber (pi_ban,
                                         pi_phone_number,
                                         pi_subscriber,
                                         pi_status,
                                         po_block_ind
                                        );
     END IF;
                                         
   EXCEPTION
      WHEN OTHERS
      THEN
         raise_application_error (-20160,
                                     'Get Port Protection Failed. Oracle:(['
                                  || SQLCODE
                                  || '] ['
                                  || SQLERRM
                                  || '])');
 END getportprotection;                                                       
END eas_utility_pkg;
/

