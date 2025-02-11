/* Formatted on 2009/04/14 17:46 (Formatter Plus v4.8.5) */
CREATE OR REPLACE PACKAGE subscriber_pref_pkg
AS
-------------------------------------------------------------------------
-- IMPORTANT NOTE !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
-- Please increment the version_no variable value when you make changes to this package 
-- to reflect version changes.
------------------------------------------------------------------------
-- description: Package subscriber_pref_pkgg containing procedures
--    for retrieve/update subscriber preference in CODS database
--
-- Date           Developer           Modifications
-- Apr-14-2009    Michael Liao    Added getsubscriberpreference savesubscriberpreference updatesubscriberpreference
-- Feb-18-2010    Daniel Canelea  Added new functions :
--                                - updatesubscriberphonedirectory
--                                - deletesubscriberphonedirectoryentries
--                                Added 2 new constants , user_id and calling_list_type_pd
-- Mar-24-2010    Daniel Canelea  Updated the Phone Directory procs to update effective ts when 
--                                updating a nickname and to resurrect a deleted nickname
-- Apr-08-2010    Tsz Chung Tong  Renamed pd_entry_creation_date to pd_entry_effective_date
--  May-30-2012     Naresh Annbathula     Added getVersion function for shakedown
-------------------------------------------------------------------------
   TYPE refcursor IS REF CURSOR;

   user_id                    CONSTANT VARCHAR2 (9)  := 'CLIENTAPI';
   contact_mechanism_type_pd  CONSTANT NUMBER        := 4;
   version_no   CONSTANT VARCHAR2(10) := '3.19.4';

   FUNCTION getVersion RETURN VARCHAR2; 
   
   FUNCTION getsubscriberpreference (
      pi_subscription_id   IN       NUMBER,
      pi_pref_topic_id     IN       NUMBER,
      po_preference        OUT      refcursor
   )
      RETURN NUMBER;

   PROCEDURE savesubscriberpreference (
      pi_subscription_id       IN   NUMBER,
      pi_pref_topic_id         IN   NUMBER,
      pi_pref_choice_seq_num   IN   NUMBER,
      pi_pref_value_txt        IN   VARCHAR2,
      pi_update_user           IN   VARCHAR2
   );

   PROCEDURE updatesubscriberpreference (
      pi_subs_pref_id          IN   NUMBER,
      pi_subscription_id       IN   NUMBER,
      pi_pref_topic_id         IN   NUMBER,
      pi_pref_choice_seq_num   IN   NUMBER,
      pi_pref_value_txt        IN   VARCHAR2,
      pi_update_user           IN   VARCHAR2
   );

   PROCEDURE updatesubscriber_ph_dir (
      pi_subscription_id       IN   NUMBER,
      pi_phone_dir_entries     IN   pd_entries_t
   );

   PROCEDURE deletesubscriber_pd_entries (
      pi_subscription_id       IN   NUMBER,
      pi_phone_dir_entries     IN   pd_entries_t
   );

END subscriber_pref_pkg;
/

SHO err

CREATE OR REPLACE PACKAGE BODY subscriber_pref_pkg
AS   
   FUNCTION getVersion RETURN VARCHAR2
   IS
   BEGIN
	   RETURN version_no;
	END getVersion;
	
   FUNCTION getsubscriberpreference (
      pi_subscription_id   IN       NUMBER,
      pi_pref_topic_id     IN       NUMBER,
      po_preference        OUT      refcursor
   )
      RETURN NUMBER
   IS
      i_count   NUMBER;
   BEGIN
      SELECT COUNT (*)
        INTO i_count
        FROM subscription
       WHERE subscription_id = pi_subscription_id;

      IF i_count > 0
      THEN
         OPEN po_preference FOR
         
            SELECT   subscriber_preference_id, subscription_id,
                     preference_topic_id, subscr_pref_choice_seq_num,
                     preference_value_txt
                FROM (
	            SELECT   subscriber_preference_id, subscription_id,
	                     preference_topic_id, subscr_pref_choice_seq_num,
	                     preference_value_txt
	                FROM subscriber_preference
	               WHERE subscription_id = pi_subscription_id
	                 AND preference_topic_id = pi_pref_topic_id
	                 AND effective_end_ts = TO_DATE ('99991231000000', 'YYYYMMDDHH24MISS')
	            ORDER BY subscriber_preference_id desc )
            WHERE ROWNUM<2;
            

         RETURN 0;  --success
      ELSE
         RETURN 1;  --wrong: no subscription exist
      END IF;
   END getsubscriberpreference;

   PROCEDURE savesubscriberpreference (
      pi_subscription_id       IN   NUMBER,
      pi_pref_topic_id         IN   NUMBER,
      pi_pref_choice_seq_num   IN   NUMBER,
      pi_pref_value_txt        IN   VARCHAR2,
      pi_update_user           IN   VARCHAR2
   )
   IS
   BEGIN
      INSERT INTO subscriber_preference
                  (subscriber_preference_id, subscription_id,
                   preference_topic_id, effective_start_ts,
                   effective_end_ts,
                   subscr_pref_choice_seq_num, preference_value_txt,
                   load_dt, update_dt, user_last_modify
                  )
           VALUES (subscriber_preference_seq.NEXTVAL, pi_subscription_id,
                   pi_pref_topic_id, SYSDATE,
                   TO_DATE ('99991231000000', 'YYYYMMDDHH24MISS'),
                   pi_pref_choice_seq_num, pi_pref_value_txt,
                   SYSDATE, SYSDATE, pi_update_user
                  );
   EXCEPTION
      WHEN OTHERS
      THEN
         raise_application_error
                            (-20160,
                                'Save subscriber preference Failed. Oracle:(['
                             || SQLCODE
                             || '] ['
                             || SQLERRM
                             || '])'
                            );
   END savesubscriberpreference;

   PROCEDURE updatesubscriberpreference (
      pi_subs_pref_id          IN   NUMBER,
      pi_subscription_id       IN   NUMBER,
      pi_pref_topic_id         IN   NUMBER,
      pi_pref_choice_seq_num   IN   NUMBER,
      pi_pref_value_txt        IN   VARCHAR2,
      pi_update_user           IN   VARCHAR2
   )
   IS
   BEGIN
      --expire the current ones
      UPDATE subscriber_preference
         SET effective_end_ts = SYSDATE,
             update_dt = SYSDATE,
             user_last_modify = pi_update_user
       WHERE 
       		subscription_id = pi_subscription_id
            and preference_topic_id = pi_pref_topic_id
            and effective_end_ts  = TO_DATE ('99991231000000', 'YYYYMMDDHH24MISS');

      --insert new records
      savesubscriberpreference (pi_subscription_id,
                                pi_pref_topic_id,
                                pi_pref_choice_seq_num,
                                pi_pref_value_txt,
                                pi_update_user
                               );
   EXCEPTION
      WHEN OTHERS
      THEN
         raise_application_error
                            (-20160,
                                'update subscriber preference Failed. Oracle:(['
                             || SQLCODE
                             || '] ['
                             || SQLERRM
                             || '])'
                            );
   END updatesubscriberpreference;

------------------------------------------------------------------------------------------------------------------------
   PROCEDURE updatesubscriber_ph_dir (
      pi_subscription_id       IN   NUMBER,
      pi_phone_dir_entries     IN   pd_entries_t
   )
   IS
   v_client_social_contact_seq NUMBER;
   v_client_social_contact_id  NUMBER;
   v_count                     NUMBER;

   BEGIN
      IF  pi_phone_dir_entries IS NOT NULL AND pi_phone_dir_entries.COUNT > 0
      THEN
         FOR i IN 1 .. pi_phone_dir_entries.COUNT
         LOOP
		    SELECT count(*)
		      INTO v_count
		      FROM social_contact_address
		     WHERE subscription_id = pi_subscription_id
		       AND contact_address_txt = pi_phone_dir_entries (i).pd_entry_phone_number
   			   AND contact_mechanism_type_id = contact_mechanism_type_pd
		       AND effective_stop_ts < TO_DATE ('99991231000000', 'YYYYMMDDHH24MISS');

		    IF pi_phone_dir_entries (i).pd_entry_exists = 'N' AND v_count = 0
			THEN
			    SELECT client_social_contact_seq.NEXTVAL
				  INTO v_client_social_contact_seq
				  FROM dual;
			
				INSERT INTO client_social_contact
				           (client_social_contact_id,
				            subscription_id,
				            contact_nickname_txt,
				            effective_start_ts,
				            effective_stop_ts,
				            create_user_id,
				            create_ts,
				            last_updt_user_id,
				            last_updt_ts
				           )
				    VALUES (v_client_social_contact_seq,
				            pi_subscription_id,
				            pi_phone_dir_entries (i).pd_entry_nickname,
				            pi_phone_dir_entries (i).pd_entry_effective_date,
				            TO_DATE ('99991231000000', 'YYYYMMDDHH24MISS'),
				            user_id,
				            SYSDATE,
				            user_id,
				            SYSDATE
                           );

                INSERT INTO social_contact_address
				           (social_contact_address_id,
				            client_social_contact_id,
				            subscription_id,
				            contact_mechanism_type_id,
				            contact_address_txt,
				            effective_start_ts,
				            effective_stop_ts,
				            create_user_id,
				            create_ts,
				            last_updt_user_id,
				            last_updt_ts
				           )
				    VALUES (social_contact_address_seq.NEXTVAL,
				            v_client_social_contact_seq,
				            pi_subscription_id,
				            contact_mechanism_type_pd,
				            pi_phone_dir_entries (i).pd_entry_phone_number,
				            pi_phone_dir_entries (i).pd_entry_effective_date,
				            TO_DATE ('99991231000000', 'YYYYMMDDHH24MISS'),
				            user_id,
				            SYSDATE,
				            user_id,
				            SYSDATE
                           );
      		ELSE
	      		IF v_count>0
	      		THEN
	      			SELECT client_social_contact_id
	      			  INTO v_client_social_contact_id
	      			  FROM social_contact_address
	      			 WHERE contact_address_txt = pi_phone_dir_entries (i).pd_entry_phone_number
	      			   AND subscription_id = pi_subscription_id
	      			   AND contact_mechanism_type_id = contact_mechanism_type_pd
	      			   AND effective_stop_ts < TO_DATE ('99991231000000', 'YYYYMMDDHH24MISS');

		  			UPDATE social_contact_address
		               SET effective_start_ts = pi_phone_dir_entries (i).pd_entry_effective_date,
	                       effective_stop_ts = TO_DATE ('99991231000000', 'YYYYMMDDHH24MISS'),
		                   last_updt_ts = SYSDATE,
		                   last_updt_user_id = user_id
		  			 WHERE contact_address_txt = pi_phone_dir_entries (i).pd_entry_phone_number
		  			   AND subscription_id = pi_subscription_id
		  			   AND contact_mechanism_type_id = contact_mechanism_type_pd
		   			   AND effective_stop_ts < TO_DATE ('99991231000000', 'YYYYMMDDHH24MISS');

		   			UPDATE client_social_contact
	                   SET contact_nickname_txt = pi_phone_dir_entries (i).pd_entry_nickname,
	                       effective_start_ts = pi_phone_dir_entries (i).pd_entry_effective_date,
	                       effective_stop_ts = TO_DATE ('99991231000000', 'YYYYMMDDHH24MISS'),
	                       last_updt_ts = SYSDATE,
	                       last_updt_user_id = user_id
	                 WHERE client_social_contact_id = v_client_social_contact_id;
	      		ELSE
	      			SELECT client_social_contact_id
	      			  INTO v_client_social_contact_id
	      			  FROM social_contact_address
	      			 WHERE contact_address_txt = pi_phone_dir_entries (i).pd_entry_phone_number
	      			   AND subscription_id = pi_subscription_id
	      			   AND contact_mechanism_type_id = contact_mechanism_type_pd
	      			   AND effective_stop_ts > SYSDATE;
	      		
	                UPDATE client_social_contact
	                   SET contact_nickname_txt = pi_phone_dir_entries (i).pd_entry_nickname,
	                       effective_start_ts = pi_phone_dir_entries (i).pd_entry_effective_date,
	                       last_updt_ts = SYSDATE,
	                       last_updt_user_id = user_id
	                 WHERE client_social_contact_id = v_client_social_contact_id;
			    END IF;
			END IF;
         END LOOP;
      END IF;

      COMMIT;
   EXCEPTION
      WHEN OTHERS
      THEN
         raise_application_error (
            -20160,
               'Update Subscriber Phone Directory Failed. Oracle:(['
            || SQLCODE
            || '] ['
            || SQLERRM
            || '])'
         );
   END updatesubscriber_ph_dir;

------------------------------------------------------------------------------------------------------------------------
   PROCEDURE deletesubscriber_pd_entries (
      pi_subscription_id       IN   NUMBER,
      pi_phone_dir_entries     IN   pd_entries_t
   )
   IS
   v_client_social_contact_id  NUMBER;
   v_count                     NUMBER;

   BEGIN
      IF  pi_phone_dir_entries IS NOT NULL AND pi_phone_dir_entries.COUNT > 0
      THEN
         FOR i IN 1 .. pi_phone_dir_entries.COUNT
         LOOP
  			SELECT COUNT(*)
  			  INTO v_count
  			  FROM social_contact_address
  			 WHERE contact_address_txt = pi_phone_dir_entries (i).pd_entry_phone_number
  			   AND subscription_id = pi_subscription_id
  			   AND contact_mechanism_type_id = contact_mechanism_type_pd
   			   AND effective_stop_ts > SYSDATE;

   			IF v_count > 0 
   			THEN
	  			SELECT client_social_contact_id
	  			  INTO v_client_social_contact_id
	  			  FROM social_contact_address
	  			 WHERE contact_address_txt = pi_phone_dir_entries (i).pd_entry_phone_number
	  			   AND subscription_id = pi_subscription_id
	  			   AND contact_mechanism_type_id = contact_mechanism_type_pd
	   			   AND effective_stop_ts > SYSDATE;
	
	  			UPDATE social_contact_address
	               SET effective_stop_ts = SYSDATE,
	                   last_updt_ts = SYSDATE,
	                   last_updt_user_id = user_id
	  			 WHERE contact_address_txt = pi_phone_dir_entries (i).pd_entry_phone_number
	  			   AND subscription_id = pi_subscription_id
	  			   AND contact_mechanism_type_id = contact_mechanism_type_pd
	   			   AND effective_stop_ts > SYSDATE;
	
	            UPDATE client_social_contact
	               SET effective_stop_ts = SYSDATE,
	                   last_updt_ts = SYSDATE,
	                   last_updt_user_id = user_id
	             WHERE client_social_contact_id = v_client_social_contact_id;
	        END IF;
         END LOOP;
      END IF;

      COMMIT;
   EXCEPTION
      WHEN OTHERS
      THEN
         raise_application_error (
            -20160,
               'Delete Subscriber Phone Directory Entries Failed. Oracle:(['
            || SQLCODE
            || '] ['
            || SQLERRM
            || '])'
         );
   END deletesubscriber_pd_entries;

--------------------------------------------------------------------------------------
END subscriber_pref_pkg;
/

SHO err
