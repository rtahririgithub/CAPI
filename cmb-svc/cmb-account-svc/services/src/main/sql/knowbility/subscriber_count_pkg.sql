CREATE OR REPLACE PACKAGE SUBSCRIBER_COUNT_PKG
AS
-------------------------------------------------------------------------
-- IMPORTANT NOTE !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
-- Please increment the version_no variable value when you make changes to this package 
-- to reflect version changes.
--------------------------------------------------------------------------
-- Date           Version	Developer           Modifications
-- Mar 26, 2013   3.23.1	Tsz Chung Tong      Migrated getSubscriberCounts from RA_UTILITY_PKG to this package

-------------------------------------------------------------------------------------------------------
	version_no          CONSTANT VARCHAR2(10)       := '3.23.1';
	
	FUNCTION getVersion RETURN VARCHAR2; 
	
PROCEDURE getSubscriberCounts(
		pi_ban						IN				NUMBER,
		p_account_type  		 	IN				VARCHAR2,
    p_account_sub_type   			IN				VARCHAR2,
		po_active_subs                 OUT      NUMBER,
		po_reserved_subs               OUT      NUMBER,
		po_suspended_subs              OUT      NUMBER,
		po_cancelled_subs              OUT      NUMBER,
		po_active_subs_all             OUT      NUMBER,
		po_reserved_subs_all           OUT      NUMBER,
		po_suspended_subs_all          OUT      NUMBER,
		po_cancelled_subs_all          OUT      NUMBER
	 );
END SUBSCRIBER_COUNT_PKG;
/

SHO err

CREATE OR REPLACE PACKAGE BODY SUBSCRIBER_COUNT_PKG
AS
FUNCTION getVersion RETURN VARCHAR2
   IS
   BEGIN
	   RETURN version_no;
	END getVersion;
	
PROCEDURE getSubscriberCounts(
		pi_ban						IN				NUMBER,
		p_account_type  		 	IN				VARCHAR2,
    p_account_sub_type   			IN				VARCHAR2,
		po_active_subs                 OUT      NUMBER,
		po_reserved_subs               OUT      NUMBER,
		po_suspended_subs              OUT      NUMBER,
		po_cancelled_subs              OUT      NUMBER,
		po_active_subs_all             OUT      NUMBER,
		po_reserved_subs_all           OUT      NUMBER,
		po_suspended_subs_all          OUT      NUMBER,
		po_cancelled_subs_all          OUT      NUMBER
	 )
	 IS
   CURSOR c_sub_summary_all  /* query for getAllActiveSubscriberCount(), getAllSuspendedSubscriberCount() and getAllCancelledSubscriberCount()*/
   IS
    SELECT SUM (DECODE (sub_status,
                        'A', 1,
                             0
                        )) a, 
           SUM (DECODE (sub_status,
                        'S', 1,
                             0
                        )) s,
           SUM (DECODE (sub_status,
                        'C', 1,
                             0
                         )) c, 
           COUNT (*) total
    FROM subscriber
    WHERE customer_id = pi_ban;
               
   CURSOR c_sub_summary /* query for getActiveSubscriberCount(), getSuspendedSubscriberCount() and getCancelledSubscriberCount()*/
   IS
    SELECT SUM (DECODE (sub_status,
                        'A', 1,
                             0
                        )) a, 
           SUM (DECODE (sub_status,
                        'S', 1,
                             0
                        )) s,
           SUM (DECODE (sub_status,
                        'C', 1,
                             0
                         )) c, 
           COUNT (*) total
    FROM subscriber
    WHERE customer_id = pi_ban
          AND (   product_type IN ('C', 'I')
                 OR (p_account_type IN ('I', 'B') AND p_account_sub_type = 'M'
                    )
                 OR (p_account_type = 'I' AND p_account_sub_type = 'J')
               );

    CURSOR c_sub_summary_rsrv_all 
    IS         
			SELECT   COUNT (*) total
      FROM subscriber s, physical_device p
      WHERE s.customer_id = pi_ban
            AND s.sub_status = 'R'
            AND p.customer_id = s.customer_id
            AND p.subscriber_no = s.subscriber_no
            AND p.product_type = s.product_type
            AND p.esn_level = 1
            AND p.expiration_date IS NULL;
                           
    CURSOR c_sub_summary_rsrv 
      IS
         SELECT COUNT (*) total
           FROM subscriber s, physical_device p
          WHERE s.customer_id = pi_ban
            AND (   s.product_type IN ('C', 'I') AND s.sub_status = 'R'
                 OR (p_account_type IN ('I', 'B') AND p_account_sub_type = 'M'
                    )
                 OR (p_account_type = 'I' AND p_account_sub_type = 'J')
                )
            AND p.customer_id = s.customer_id
            AND p.subscriber_no = s.subscriber_no
            AND p.product_type = s.product_type
            AND p.esn_level = 1
            AND p.expiration_date IS NULL;	
            
    ss_rec                 c_sub_summary%ROWTYPE;
    ss_all_rec             c_sub_summary_all%ROWTYPE;

    v_rsrv                 NUMBER (4);
    v_rsrv_all             NUMBER (4);
 
	BEGIN
			OPEN c_sub_summary_all;
      FETCH c_sub_summary_all
       INTO ss_all_rec;
      po_active_subs_all := NVL (ss_all_rec.a, 0);
      po_suspended_subs_all := NVL (ss_all_rec.s, 0);
      po_cancelled_subs_all := NVL (ss_all_rec.c, 0);
      CLOSE c_sub_summary_all;

      
      OPEN c_sub_summary;
      FETCH c_sub_summary
       INTO ss_rec;
      po_active_subs := NVL (ss_rec.a, 0);
      po_suspended_subs := NVL (ss_rec.s, 0);
      po_cancelled_subs := NVL (ss_rec.c, 0);
      CLOSE c_sub_summary;

      
      OPEN c_sub_summary_rsrv_all;
      FETCH c_sub_summary_rsrv_all
       INTO v_rsrv_all;
      po_reserved_subs_all := v_rsrv_all;
      CLOSE c_sub_summary_rsrv_all;


      OPEN c_sub_summary_rsrv;
      FETCH c_sub_summary_rsrv
       INTO v_rsrv;
      po_reserved_subs := v_rsrv;

      CLOSE c_sub_summary_rsrv;	
   EXCEPTION
      WHEN OTHERS
      THEN
      IF c_sub_summary_all%ISOPEN
      THEN
      		CLOSE c_sub_summary_all;
      END IF;
      
      IF c_sub_summary%ISOPEN
      THEN
      		CLOSE c_sub_summary;
      END IF;

      IF c_sub_summary_rsrv_all%ISOPEN
      THEN
      		CLOSE c_sub_summary_rsrv_all;
      END IF;      
      IF c_sub_summary_rsrv%ISOPEN
      THEN
      		CLOSE c_sub_summary_rsrv;
      END IF;
      
      RAISE;

	END getSubscriberCounts;
END SUBSCRIBER_COUNT_PKG;
/

SHO err

