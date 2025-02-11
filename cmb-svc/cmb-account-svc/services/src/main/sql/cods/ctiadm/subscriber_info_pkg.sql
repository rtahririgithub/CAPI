CREATE OR REPLACE PACKAGE subscriber_info_pkg
AS
 ------------------------------------------------------------------------
-- description: Package subscriber_info_pkg containing procedures
--    for data retrieval from CODS database
--
-- Date           Developer           Modifications
-- 05-25-2006     Kuhan Paramsothy    creation of package
-------------------------------------------------------------------------
   numeric_true    CONSTANT NUMBER (1) := 1;
   numeric_false   CONSTANT NUMBER (1) := 0;

   TYPE refcursor IS REF CURSOR;

   FUNCTION selectsubscriptionid (
      pi_banid         IN   NUMBER,
      pi_phonenumber   IN   VARCHAR2,
      pi_status        IN   VARCHAR2
   )
      RETURN NUMBER;
END subscriber_info_pkg;
/
CREATE OR REPLACE PACKAGE BODY subscriber_info_pkg
AS
   FUNCTION selectsubscriptionid (
      pi_banid         IN   NUMBER,
      pi_phonenumber   IN   VARCHAR2,
      pi_status        IN   VARCHAR2
   )
      RETURN NUMBER
   IS
      p_result   NUMBER;
   BEGIN
      IF pi_status IN ('A', 'S')
      THEN
         SELECT subscription_id
           INTO p_result
           FROM (SELECT   subscription_id
                     FROM subscription
                    WHERE cell_phone_no = pi_phonenumber
                      AND current_status_cd = pi_status
                 ORDER BY subscription_id DESC)
          WHERE ROWNUM <= 1;
      ELSE
         SELECT subscription_id
           INTO p_result
           FROM (SELECT   s.subscription_id
                     FROM client_account ca,
                          account_subscription_assoc asa,
                          subscription s
                    WHERE ca.ban = pi_banid
                      AND asa.client_account_id = ca.client_account_id
                      AND s.subscription_id = asa.subscription_id
                      AND s.cell_phone_no = pi_phonenumber
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
         raise_application_error (-20160,
                                     'Find CC Event Failed. Oracle:(['
                                  || SQLCODE
                                  || '] ['
                                  || SQLERRM
                                  || '])'
                                 );
   END selectsubscriptionid;
END subscriber_info_pkg;
/
