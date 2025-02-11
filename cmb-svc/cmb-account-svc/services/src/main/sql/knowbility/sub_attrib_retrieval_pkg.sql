CREATE OR REPLACE PACKAGE SUB_ATTRIB_RETRIEVAL_PKG
AS
   ----------------------------------------------------------------------------------------------------------------------------------------------------------
   -- IMPORTANT NOTE !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
   -- Please increment the version_no variable value when you make changes to this package
   -- to reflect version changes.
   --------------------------------------------------------------------------
   -- Date           Version Developer           Modifications
   -- Apr 10, 2013   3.23.1 Tsz Chung Tong        Creation of package
   -- May 22, 2013   3.23.2 Tsz Chung Tong      Added /*+ cardinality( t 10 ) */ hint to getSubIDListByBanAndOrPhone
   -- May 22, 2013   3.23.3 Tsz Chung Tong      Overloaded getSubIDListByBanAndOrPhone with single phone # as the original stored proc in SUBSCRIBER_PKG
   -- May 24, 2013   3.23.4 Tsz Chung Tong      Added index hint
   -- May 31, 2013   3.23.5 Tsz Chung Tong      Removed the hints
   -- Jun 13, 2013   3.23.6 Tsz Chung Tong      Added hints back to stored procs to ensure proper execution plan
   -- March 1, 2014  3.28.1 Naresh Annabathula  Added new function getSubIdsByBanAndOrSeatNum  to get SubscriberId's by Ban and seat resource Number(VOIP/TOLLFREE)
   -- March 1, 2014  3.28.1 Naresh Annabathula  updated getIdentifierListByBanAndPhone to retrive seatType and seatGroup and brandId
   -- March 24, 2014 3.28.2 Naresh Annabathula   Added new procedure getIdentifierByBanAndSeatNum to retrieve SubscriberIdentifier by seat number
   -- May 24,2014    3.28.3 Naresh Annabathula   fixed seatnumber search issue 
   -- May 24, 2018   201807.1 Tsz Chung Tong     Changed getSubIDListByBanAndOrPhone query to remove unnecessary sorting and column retrieval, and to return distinct values only
   -- May 25, 2018   201807.2 Tsz Chung Tong     Removed all hints from this package. Per DBA "Please remove both hints /*+ INDEX(sr SUBSCRIBER_RSOURCE_1IX) */  and  /*+ INDEX(sr2 SUBSCRIBER_RSOURCE_4IX) */  from queries in 12c  and test it without hints.  Since 12c optimizer is much different than 9i, hints can be added only if needed. The same practise we applied to KB queries."
   --------------------------------------------------------------------------------------------------------------------------------------------------------------

   TYPE REFCURSOR IS REF CURSOR;

   version_no          CONSTANT VARCHAR2 (10) := '201807.2';

   -- result constants
   NUMERIC_TRUE        CONSTANT NUMBER (1) := 0;
   NUMERIC_FALSE       CONSTANT NUMBER (1) := 1;

   -- error messages
   err_invalid_input   CONSTANT VARCHAR2 (50) := 'Input parameters are invalid or NULL.' ;
   err_no_data_found   CONSTANT VARCHAR2 (30) := 'No data found.';
   err_other           CONSTANT VARCHAR2 (30) := 'Other PL/SQL error.';

   FUNCTION getVersion
      RETURN VARCHAR2;

      PROCEDURE getIdentifierByBanAndPhone (
      i_ban                 IN       NUMBER,
      v_phone_no            IN       VARCHAR2,
      i_include_cancelled   IN       NUMBER,
      c_sub_identifier      OUT      REFCURSOR
   );
   
   FUNCTION getSubIDListByPhoneNumbers (
      a_phone_numbers       IN       t_phone_num_array,
      i_include_cancelled   IN       NUMBER,
      a_sub_ids             OUT      NOCOPY t_subscriber_array,
      v_error_message       OUT      NOCOPY VARCHAR2
    )RETURN NUMBER;
  
    FUNCTION getSubIDListByBanAndPhone (
   	  i_ban                 IN       NUMBER,
      v_phone_number       	IN       VARCHAR2,
      i_include_cancelled   IN       NUMBER,
      a_sub_ids             OUT      NOCOPY t_subscriber_array,
      v_error_message       OUT      NOCOPY VARCHAR2
   )RETURN NUMBER;
   
    FUNCTION getSubIdsByBanAndOrSeatNum(
      i_ban                 IN       NUMBER,
      i_seat_number         IN       VARCHAR2,
      i_include_cancelled   IN       NUMBER,
      a_sub_ids             OUT      NOCOPY t_subscriber_array,
      v_error_message       OUT      NOCOPY VARCHAR2
   )RETURN NUMBER;
   
    PROCEDURE getIdentifierByBanAndSeatNum (
      i_ban                 IN       NUMBER,
      i_seat_number         IN       VARCHAR2,
      i_include_cancelled   IN       NUMBER,
      c_sub_identifier      OUT      REFCURSOR
   );
END SUB_ATTRIB_RETRIEVAL_PKG;
/

SHO ERR


CREATE OR REPLACE PACKAGE BODY SUB_ATTRIB_RETRIEVAL_PKG
AS

/**
 * This function is created for reference only and not supposed to be in use.
 */
    FUNCTION getSubIDListByBanAndOrPhone (
      i_ban                 IN       NUMBER,
      a_phone_numbers       IN       t_phone_num_array,
      i_include_cancelled   IN       NUMBER,
      a_sub_ids             OUT      NOCOPY t_subscriber_array,
      v_error_message       OUT      NOCOPY VARCHAR2
   )RETURN NUMBER;
   
   FUNCTION getSubIDListByBanAndOrPhone (
      i_ban                 IN       NUMBER,
      v_phone_number        IN       VARCHAR2,
      i_include_cancelled   IN       NUMBER,
      a_sub_ids             OUT      NOCOPY t_subscriber_array,
      v_error_message       OUT      NOCOPY VARCHAR2
   )RETURN NUMBER;

   FUNCTION getVersion
      RETURN VARCHAR2
   IS
   BEGIN
      RETURN version_no;
   END getVersion;
  
   
   /**
    * BAN is optional
    */
   PROCEDURE getIdentifierListByBanAndPhone (
      i_ban                 IN       NUMBER,
      v_phone_no            IN       VARCHAR2,
      i_include_cancelled   IN       NUMBER,
      i_maximumRow          IN       NUMBER,
      c_sub_identifier      OUT      REFCURSOR
   )
   IS
   BEGIN
         IF v_phone_no IS NOT NULL AND LENGTH (RTRIM (LTRIM (v_phone_no))) > 0
         THEN
         OPEN c_sub_identifier FOR
         SELECT * FROM (
             SELECT s1.subscriber_no, s1.external_id , s1.sub_status, s1.customer_id,s1.seat_type, s1.seat_group,s1.brand_id,
                    DECODE (s1.sub_status, 'S', 'B', s1.sub_status) decodedStatus 
             FROM subscriber s1 
             WHERE s1.subscriber_no = v_phone_no
                   AND (s1.customer_id = i_ban OR i_ban is NULL OR i_ban = 0)
             AND s1.product_type <> 'I'                   
             AND (s1.sub_status != 'C' OR i_include_cancelled = numeric_true)
             UNION
             SELECT  
             s2.subscriber_no, s2.external_id, sr.resource_status, s2.customer_id,s2.seat_type, s2.seat_group,s2.brand_id,
                    DECODE (s2.sub_status, 'S', 'B', s2.sub_status) decodedStatus
                    FROM subscriber s2,
                            subscriber_rsource sr
                    WHERE sr.resource_number = v_phone_no
                            AND (sr.ban = i_ban OR i_ban is NULL OR i_ban = 0)
                            AND (sr.resource_status != 'C' OR i_include_cancelled = numeric_true)
                            AND sr.resource_type = 'N'
                           AND sr.resource_seq = (SELECT MAX(sr2.resource_seq)
                                                  FROM subscriber_rsource sr2
                                                  WHERE sr2.subscriber_no = sr.subscriber_no
                                                    AND sr2.ban = sr.ban 
                                                    AND sr2.resource_type = 'N')
            AND s2.subscriber_no = sr.subscriber_no
            AND (s2.sub_status != 'C'  OR i_include_cancelled = numeric_true)
           ORDER BY decodedStatus, 2 DESC
          ) WHERE ROWNUM <= i_maximumRow;

        END IF;
      EXCEPTION
         WHEN OTHERS
         THEN
            RAISE;
      END getIdentifierListByBanAndPhone;

      /**
       * This procedure always returns only one and the latest one (top record)
       */
    PROCEDURE getIdentifierByBanAndPhone (
      i_ban                 IN       NUMBER,
      v_phone_no            IN       VARCHAR2,
      i_include_cancelled   IN       NUMBER,
      c_sub_identifier      OUT      REFCURSOR
   )
   IS
   BEGIN
       getIdentifierListByBanAndPhone (i_ban, v_phone_no, i_include_cancelled, 1, c_sub_identifier);
       
   END getIdentifierByBanAndPhone;


    FUNCTION getSubIDListByPhoneNumbers (
      a_phone_numbers       IN       t_phone_num_array,
      i_include_cancelled   IN       NUMBER,
      a_sub_ids             OUT      NOCOPY t_subscriber_array,
      v_error_message       OUT      NOCOPY VARCHAR2
   )
      RETURN NUMBER
   IS
      i_result              NUMBER (1);
   BEGIN
       i_result := NUMERIC_FALSE;
       
       IF a_phone_numbers.COUNT = 1 THEN --This should be the only case as of July 2013
            i_result := getSubIDListByBanAndOrPhone (0, a_phone_numbers(1), i_include_cancelled, a_sub_ids, v_error_message);
       ELSIF a_phone_numbers.COUNT > 1 THEN
            i_result := getSubIDListByBanAndOrPhone (0, a_phone_numbers, i_include_cancelled, a_sub_ids, v_error_message);
       END IF;
       
       RETURN i_result;
   END getSubIDListByPhoneNumbers;
   
   FUNCTION getSubIDListByBanAndPhone (
       i_ban                     IN       NUMBER,
      v_phone_number           IN       VARCHAR2,
      i_include_cancelled   IN       NUMBER,
      a_sub_ids             OUT      NOCOPY t_subscriber_array,
      v_error_message       OUT      NOCOPY VARCHAR2
   )
      RETURN NUMBER
   IS
       i_result              NUMBER (1) := NUMERIC_FALSE;
   BEGIN
      IF     v_phone_number IS NOT NULL AND LENGTH (RTRIM (LTRIM (v_phone_number))) > 0
      THEN
          i_result := getSubIDListByBanAndOrPhone (i_ban, v_phone_number, i_include_cancelled, a_sub_ids, v_error_message);
       
      END IF;
      
      RETURN i_result;
   END getSubIDListByBanAndPhone;
   
  /**
   * BAN is optional. This proecdure needs review when if to be used. (July 2013)
   */
  FUNCTION getSubIDListByBanAndOrPhone (
      i_ban                 IN       NUMBER,
      a_phone_numbers       IN       t_phone_num_array,
      i_include_cancelled   IN       NUMBER,
      a_sub_ids             OUT      NOCOPY t_subscriber_array,
      v_error_message       OUT      NOCOPY VARCHAR2
   )
      RETURN NUMBER
   IS
      c_cursor        refcursor;
      i_result        NUMBER (1);
   BEGIN
      BEGIN
         IF a_phone_numbers IS NOT NULL AND a_phone_numbers.COUNT > 0 THEN
             OPEN c_cursor FOR
				SELECT SUBSCRIBER_NO FROM (SELECT s1.subscriber_no
                FROM subscriber s1 
                WHERE s1.subscriber_no in (SELECT /*+ cardinality( t 1 ) */ * FROM TABLE (CAST (a_phone_numbers AS t_phone_num_array)) t where rownum >= 0)
                     AND (s1.customer_id = i_ban OR i_ban is NULL OR i_ban = 0)
                     AND s1.product_type <> 'I'                   
                     AND (s1.sub_status != 'C' OR i_include_cancelled = numeric_true)
                UNION
                SELECT s2.subscriber_no
                FROM subscriber s2,
                     subscriber_rsource sr
                WHERE sr.resource_number in (SELECT /*+ cardinality( t 1 ) */ * FROM TABLE (CAST (a_phone_numbers AS t_phone_num_array)) t where rownum >= 0)
                      AND (sr.ban = i_ban OR i_ban is NULL OR i_ban = 0)
                      AND (sr.resource_status != 'C' OR i_include_cancelled = numeric_true)
                      AND sr.resource_type = 'N'
                      AND sr.resource_seq = (SELECT MAX(sr2.resource_seq)
                                             FROM subscriber_rsource sr2
                                             WHERE sr2.subscriber_no = sr.subscriber_no
                                                   AND sr2.ban = sr.ban 
                                                  AND sr2.resource_type = 'N')
                AND s2.subscriber_no = sr.subscriber_no
                AND (s2.sub_status != 'C'  OR i_include_cancelled = numeric_true)
                );
                       
               FETCH c_cursor BULK COLLECT INTO a_sub_ids;
           
            IF c_cursor%ISOPEN
            THEN
               CLOSE c_cursor;
            END IF;

            IF a_sub_ids.COUNT > 0
            THEN
               i_result := numeric_true;
            ELSE
               v_error_message := err_no_data_found;
               i_result := numeric_false;
            END IF;
         ELSE
            v_error_message := err_invalid_input;
            i_result := numeric_false;
         END IF;
      EXCEPTION
         WHEN NO_DATA_FOUND
         THEN
            v_error_message := err_no_data_found;
            i_result := numeric_false;
         WHEN OTHERS
         THEN
            v_error_message := SQLERRM;
            i_result := numeric_false;
      END;

      RETURN i_result;
   END getSubIDListByBanAndOrPhone;
   
   FUNCTION getSubIDListByBanAndOrPhone (
      i_ban                 IN       NUMBER,
      v_phone_number        IN       VARCHAR2,
      i_include_cancelled   IN       NUMBER,
      a_sub_ids             OUT      NOCOPY t_subscriber_array,
      v_error_message       OUT      NOCOPY VARCHAR2
   )
      RETURN NUMBER
   IS
      c_cursor        refcursor;
      i_result        NUMBER (1);
   BEGIN
      BEGIN
         IF v_phone_number IS NOT NULL AND LENGTH (RTRIM (LTRIM (v_phone_number))) > 0 THEN
             OPEN c_cursor FOR
             	SELECT SUBSCRIBER_NO FROM (SELECT s1.subscriber_no 
                FROM subscriber s1 
                WHERE s1.subscriber_no = v_phone_number
                     AND (s1.customer_id = i_ban OR i_ban is NULL OR i_ban = 0)
                     AND s1.product_type <> 'I'                   
                     AND (s1.sub_status != 'C' OR i_include_cancelled = numeric_true)
                UNION
                SELECT s2.subscriber_no
                FROM subscriber s2,
                     subscriber_rsource sr
                WHERE sr.resource_number = v_phone_number
                      AND (sr.ban = i_ban OR i_ban is NULL OR i_ban = 0)
                      AND (sr.resource_status != 'C' OR i_include_cancelled = numeric_true)
                      AND sr.resource_type = 'N'
                      AND sr.resource_seq = (SELECT MAX(sr2.resource_seq)
                                             FROM subscriber_rsource sr2
                                             WHERE sr2.subscriber_no = sr.subscriber_no
                                                   AND sr2.ban = sr.ban 
                                                   AND sr2.resource_type = 'N')
                      AND s2.subscriber_no = sr.subscriber_no
                     AND (s2.sub_status != 'C'  OR i_include_cancelled = numeric_true)
               );
                       
               FETCH c_cursor BULK COLLECT INTO a_sub_ids;
           
            IF c_cursor%ISOPEN
            THEN
               CLOSE c_cursor;
            END IF;

            IF a_sub_ids.COUNT > 0
            THEN
               i_result := numeric_true;
            ELSE
               v_error_message := err_no_data_found;
               i_result := numeric_false;
            END IF;
         ELSE
            v_error_message := err_invalid_input;
            i_result := numeric_false;
         END IF;
      EXCEPTION
         WHEN NO_DATA_FOUND
         THEN
            v_error_message := err_no_data_found;
            i_result := numeric_false;
         WHEN OTHERS
         THEN
            v_error_message := SQLERRM;
            i_result := numeric_false;
      END;

      RETURN i_result;
   END getSubIDListByBanAndOrPhone;
   
  FUNCTION getSubIdsByBanAndOrSeatNum(
      i_ban                 IN       NUMBER,
      i_seat_number         IN       VARCHAR2,
      i_include_cancelled   IN       NUMBER,
      a_sub_ids             OUT      NOCOPY t_subscriber_array,
      v_error_message       OUT      NOCOPY VARCHAR2
   )
      RETURN NUMBER
   IS
      c_cursor        refcursor;
      i_result        NUMBER (1);
   BEGIN
      BEGIN
                  IF i_seat_number IS NOT NULL AND LENGTH (RTRIM (LTRIM (i_seat_number))) > 0 THEN
             OPEN c_cursor FOR
                     SELECT SUBSCRIBER_NO FROM ( 
                     SELECT 
                     s2.subscriber_no, s2.external_id, sr.resource_status, s2.customer_id,
                            DECODE (s2.sub_status, 'S', 'B', s2.sub_status) decodedStatus
                            FROM subscriber s2,
                                    subscriber_rsource sr
                            WHERE sr.resource_number = i_seat_number
                                    AND (sr.ban = i_ban OR i_ban is NULL OR i_ban = 0)
                                    AND sr.ban = s2.customer_ban
                                    AND (sr.resource_status != 'C' OR i_include_cancelled = numeric_true)
                                    AND sr.resource_type in ( 'V','L','I','O')
                                   AND sr.resource_seq = (SELECT 
                                                       MAX(sr2.resource_seq)
                                                          FROM subscriber_rsource sr2
                                                          WHERE sr2.subscriber_no = sr.subscriber_no
                                                            AND sr2.resource_number = sr.resource_number 
                                                            AND sr2.ban = sr.ban 
                                                            AND sr2.resource_type  in ( 'V','L','I','O'))
                    AND s2.subscriber_no = sr.subscriber_no
                    AND (s2.sub_status != 'C'  OR i_include_cancelled = numeric_true)
                   ORDER BY decodedStatus, 2 DESC
                   );
         
                       
               FETCH c_cursor BULK COLLECT INTO a_sub_ids;
           
            IF c_cursor%ISOPEN
            THEN
               CLOSE c_cursor;
            END IF;

            IF a_sub_ids.COUNT > 0
            THEN
               i_result := numeric_true;
            ELSE
               v_error_message := err_no_data_found;
               i_result := numeric_false;
            END IF;
         ELSE
            v_error_message := err_invalid_input;
            i_result := numeric_false;
         END IF;
      EXCEPTION
         WHEN NO_DATA_FOUND
         THEN
            v_error_message := err_no_data_found;
            i_result := numeric_false;
         WHEN OTHERS
         THEN
            v_error_message := SQLERRM;
            i_result := numeric_false;
      END;

      RETURN i_result;
   END getSubIdsByBanAndOrSeatNum;
   
    /**
    * BAN is optional
    */
   PROCEDURE getIdentifierByBanAndSeatNum (
      i_ban                 IN       NUMBER,
      i_seat_number         IN       VARCHAR2,
      i_include_cancelled   IN       NUMBER,
      c_sub_identifier      OUT      REFCURSOR
   )
   IS
   BEGIN
          IF i_seat_number IS NOT NULL AND LENGTH (RTRIM (LTRIM (i_seat_number))) > 0 THEN
             OPEN c_sub_identifier FOR
                     SELECT * FROM ( 
                     SELECT s2.subscriber_no, s2.external_id, sr.resource_status, s2.customer_id,s2.seat_type, s2.seat_group,s2.brand_id,
                    DECODE (s2.sub_status, 'S', 'B', s2.sub_status) decodedStatus
                            FROM subscriber s2,
                                    subscriber_rsource sr
                            WHERE sr.resource_number = i_seat_number
                                    AND (sr.ban = i_ban OR i_ban is NULL OR i_ban = 0)
                                    AND sr.ban = s2.customer_ban
                                    AND (sr.resource_status != 'C' OR i_include_cancelled = numeric_true)
                                    AND sr.resource_type in ( 'V','L','I','O')
                                   AND sr.resource_seq = (SELECT 
                                                       MAX(sr2.resource_seq)
                                                          FROM subscriber_rsource sr2
                                                          WHERE sr2.subscriber_no = sr.subscriber_no
                                                            AND sr2.resource_number = sr.resource_number 
                                                            AND sr2.ban = sr.ban 
                                                            AND sr2.resource_type  in ( 'V','L','I','O'))
                    AND s2.subscriber_no = sr.subscriber_no
                    AND (s2.sub_status != 'C'  OR i_include_cancelled = numeric_true)
                   ORDER BY decodedStatus, 2 DESC
          );

        END IF;
      EXCEPTION
         WHEN OTHERS
         THEN
            RAISE;
      END getIdentifierByBanAndSeatNum;
      
END SUB_ATTRIB_RETRIEVAL_PKG;
/

SHO ERR