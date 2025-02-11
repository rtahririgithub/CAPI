------------------------------------------------------------------------
-- description: Package data_usage_pkg containing procedures
--    for data retrieval from SERV database
--
-- Date           Developer           Modifications
-- 05-25-2006     Kuhan Paramsothy    creation of package
-------------------------------------------------------------------------
CREATE OR REPLACE PACKAGE cc_event_pkg
AS
-------------------------------------------------------------------------
-- IMPORTANT NOTE !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
-- Please increment the version_no variable value when you make changes to this package 
-- to reflect version changes.
--------------------------------------------------------------------------
   numeric_true    CONSTANT NUMBER (1) := 1;
   numeric_false   CONSTANT NUMBER (1) := 0;
   version_no          CONSTANT VARCHAR2(10)       := '3.19.4';

   TYPE refcursor IS REF CURSOR;

   PROCEDURE createnewevent (
      pi_connectionid_hex   IN   VARCHAR2,
      pi_phonenumber        IN   VARCHAR2,
      pi_subscriptionid     IN   NUMBER,
      pi_userid             IN   VARCHAR2,
      pi_queuename          IN   VARCHAR2,
      pi_thresholdseconds   IN   NUMBER
   );
   
   FUNCTION getVersion RETURN VARCHAR2; 

   PROCEDURE updateevent (
      pi_interactionid    IN   NUMBER,
      pi_subscriptionid   IN   NUMBER,
      pi_phonenumber      IN   VARCHAR2,
      pi_teammemberid     IN   VARCHAR2,
      pi_userid           IN   VARCHAR2
   );

   PROCEDURE selecteventsbysubscriptionid (
      pi_subscriptionid   IN       NUMBER,
      pi_datefrom         IN       VARCHAR2,
      pi_dateto           IN       VARCHAR2,
      o_events            OUT      refcursor
   );

   PROCEDURE selecteventsbysubscriptionid (
      pi_subscriptionid   IN       NUMBER,
      pi_dateto           IN       VARCHAR2,
      o_events            OUT      refcursor
   );

   PROCEDURE selecteventsbyconnectionid (
      pi_connectionid   IN       NUMBER,
      o_events          OUT      refcursor
   );
END cc_event_pkg;
/
CREATE OR REPLACE PACKAGE BODY cc_event_pkg
AS
   PROCEDURE createnewevent (
      pi_connectionid_hex   IN   VARCHAR2,
      pi_phonenumber        IN   VARCHAR2,
      pi_subscriptionid     IN   NUMBER,
      pi_userid             IN   VARCHAR2,
      pi_queuename          IN   VARCHAR2,
      pi_thresholdseconds   IN   NUMBER
   )
   IS
      v_found_connection_id   INTEGER;
      v_todays_date           DATE;
   BEGIN
      DBMS_OUTPUT.put_line ('begining createnewevent');

      SELECT connection_id
        INTO v_found_connection_id
        FROM call_centre_interaction
       WHERE connection_id =
                TO_NUMBER (pi_connectionid_hex, 'xxxxxxxxxxxxxxxxxxxxxxxxxxx');

      DBMS_OUTPUT.put_line
                     ('found duplicate connection id, ignoring insert request');
   EXCEPTION
      WHEN NO_DATA_FOUND
      THEN
         DBMS_OUTPUT.put_line
                  ('found no duplicate connection id, proceeding with insert');
         v_todays_date := SYSDATE;

         INSERT INTO call_centre_interaction
                     (cc_interaction_id, cc_interaction_create_ts,
                      connection_id,
                      client_supplied_phone_num, target_subscription_id,
                      create_user_id, create_ts, last_updt_user_id
                     )
              VALUES (cc_interaction_seq.NEXTVAL, v_todays_date,
                      TO_NUMBER (pi_connectionid_hex,
                                 'xxxxxxxxxxxxxxxxxxxxxxxxxxx'
                                ),
                      pi_phonenumber, pi_subscriptionid,
                      pi_userid, SYSDATE, pi_userid
                     );

         INSERT INTO queue_wait_threshold_event
                     (cc_interaction_id, cc_interaction_create_ts,
                      queue_nm, queue_wait_thrshld_secs_cnt,
                      assigned_to_subscription_id, assigned_to_phone_num,
                      create_user_id, last_updt_user_id
                     )
              VALUES (cc_interaction_seq.CURRVAL, v_todays_date,
                      pi_queuename, pi_thresholdseconds,
                      pi_subscriptionid, pi_phonenumber,
                      pi_userid, pi_userid
                     );

         COMMIT;
   END createnewevent;

   FUNCTION getVersion RETURN VARCHAR2
   IS
   BEGIN
	   RETURN version_no;
	END getVersion;
	
   PROCEDURE updateevent (
      pi_interactionid    IN   NUMBER,
      pi_subscriptionid   IN   NUMBER,
      pi_phonenumber      IN   VARCHAR2,
      pi_teammemberid     IN   VARCHAR2,
      pi_userid           IN   VARCHAR2
   )
   IS
      v_found_connection_id   INTEGER;
   BEGIN
      DBMS_OUTPUT.put_line ('begining updateevent');

      UPDATE queue_wait_threshold_event
         SET assigned_to_subscription_id = pi_subscriptionid,
             assigned_by_team_member_id = pi_teammemberid,
             assigned_to_phone_num = pi_phonenumber,
             last_updt_ts = SYSDATE,
             last_updt_user_id = pi_userid
       WHERE cc_interaction_id = pi_interactionid;

      COMMIT;
   END updateevent;

   PROCEDURE selecteventsbysubscriptionid (
      pi_subscriptionid   IN       NUMBER,
      pi_datefrom         IN       VARCHAR2,
      pi_dateto           IN       VARCHAR2,
      o_events            OUT      refcursor
   )
   IS
   BEGIN
      OPEN o_events FOR
         SELECT qe.cc_interaction_id, connection_id, queue_nm,
                qe.cc_interaction_create_ts, assigned_to_phone_num,
                queue_start_ts, queue_end_ts, evaluated_for_credit_ts,
                assigned_by_team_member_id
           FROM call_centre_interaction cc, queue_wait_threshold_event qe
          WHERE cc.cc_interaction_id = qe.cc_interaction_id
            AND assigned_to_subscription_id = pi_subscriptionid
            AND qe.cc_interaction_create_ts >=
                                           TO_DATE (pi_datefrom, 'MM/dd/yyyy HH24:MI:SS')
            AND qe.cc_interaction_create_ts <=
                                             TO_DATE (pi_dateto, 'MM/dd/yyyy HH24:MI:SS');
   EXCEPTION
      WHEN NO_DATA_FOUND
      THEN
         IF (o_events%ISOPEN)
         THEN
            CLOSE o_events;
         END IF;

         -- return NULL cursor
         OPEN o_events FOR
            SELECT NULL
              FROM DUAL
             WHERE 1 = 0;
      WHEN OTHERS
      THEN
         IF (o_events%ISOPEN)
         THEN
            CLOSE o_events;
         END IF;

         raise_application_error (-20160,
                                     'Find CC Event Failed. Oracle:(['
                                  || SQLCODE
                                  || '] ['
                                  || SQLERRM
                                  || '])'
                                 );
   END selecteventsbysubscriptionid;

   PROCEDURE selecteventsbysubscriptionid (
      pi_subscriptionid   IN       NUMBER,
      pi_dateto           IN       VARCHAR2,
      o_events            OUT      refcursor
   )
   IS
   BEGIN
      OPEN o_events FOR
         SELECT qe.cc_interaction_id, connection_id, queue_nm,
                qe.cc_interaction_create_ts, assigned_to_phone_num,
                queue_start_ts, queue_end_ts, evaluated_for_credit_ts,
                assigned_by_team_member_id
           FROM call_centre_interaction cc, queue_wait_threshold_event qe
          WHERE cc.cc_interaction_id = qe.cc_interaction_id
            AND assigned_to_subscription_id = pi_subscriptionid
            AND qe.cc_interaction_create_ts <=
                                             TO_DATE (pi_dateto, 'MM/dd/yyyy HH24:MI:SS');
   EXCEPTION
      WHEN NO_DATA_FOUND
      THEN
         IF (o_events%ISOPEN)
         THEN
            CLOSE o_events;
         END IF;

         -- return NULL cursor
         OPEN o_events FOR
            SELECT NULL
              FROM DUAL
             WHERE 1 = 0;
      WHEN OTHERS
      THEN
         IF (o_events%ISOPEN)
         THEN
            CLOSE o_events;
         END IF;

         raise_application_error (-20160,
                                     'Find CC Event Failed. Oracle:(['
                                  || SQLCODE
                                  || '] ['
                                  || SQLERRM
                                  || '])'
                                 );
   END selecteventsbysubscriptionid;

   PROCEDURE selecteventsbyconnectionid (
      pi_connectionid   IN       NUMBER,
      o_events          OUT      refcursor
   )
   IS
   BEGIN
      OPEN o_events FOR
         SELECT   qe.cc_interaction_id, connection_id, queue_nm,
                  qe.cc_interaction_create_ts, assigned_to_phone_num,
                  queue_start_ts, queue_end_ts, evaluated_for_credit_ts,
                  assigned_by_team_member_id
             FROM call_centre_interaction cc, queue_wait_threshold_event qe
            WHERE cc.cc_interaction_id = qe.cc_interaction_id
              AND connection_id = pi_connectionid
         ORDER BY cc_interaction_create_ts DESC;
   EXCEPTION
      WHEN NO_DATA_FOUND
      THEN
         IF (o_events%ISOPEN)
         THEN
            CLOSE o_events;
         END IF;

         -- return NULL cursor
         OPEN o_events FOR
            SELECT NULL
              FROM DUAL
             WHERE 1 = 0;
      WHEN OTHERS
      THEN
         IF (o_events%ISOPEN)
         THEN
            CLOSE o_events;
         END IF;

         raise_application_error (-20160,
                                     'Find CC Event Failed. Oracle:(['
                                  || SQLCODE
                                  || '] ['
                                  || SQLERRM
                                  || '])'
                                 );
   END selecteventsbyconnectionid;
END cc_event_pkg;
/
