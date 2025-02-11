CREATE OR REPLACE PACKAGE CRDCHECK_RESULT_PKG
AS
-------------------------------------------------------------------------
-- IMPORTANT NOTE !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
-- Please increment the version_no variable value when you make changes to this package 
-- to reflect version changes.
--------------------------------------------------------------------------
-- Date           Version	Developer           Modifications
-- Mar 26, 2013   3.23.1	Tsz Chung Tong      Migrated getLastCreditCheckResultByBan from RA_UTILITY_PKG to this package

-------------------------------------------------------------------------------------------------------
   TYPE refcursor IS REF CURSOR;

-- result constants
   numeric_true        CONSTANT NUMBER (1)     := 0;
   numeric_false       CONSTANT NUMBER (1)     := 1;
   version_no          CONSTANT VARCHAR2(10)       := '3.23.1';
	
	FUNCTION getVersion RETURN VARCHAR2; 
   
	FUNCTION getLastCreditCheckResultByBan (
		pi_ban         							IN  	NUMBER,
		pi_product_type 						IN 		CHAR,
		po_credit_class 						OUT		VARCHAR2,
		po_credit_limit 						OUT		NUMBER,
		po_deposit_amt  						OUT		NUMBER,
		po_credit_result2 						OUT		VARCHAR2,
		po_beacon_score 						OUT		NUMBER,
		po_credit_referral_flag 				OUT		VARCHAR2,
		po_french_message 					OUT		VARCHAR2,
		po_credit_req_sts 					OUT		VARCHAR2,
		po_sin  							OUT		VARCHAR2,
		po_drivr_licns_no 					OUT		VARCHAR2,
		po_date_of_birth  					OUT		DATE,
		po_credit_card_no  					OUT		VARCHAR2,
		po_crd_card_exp_date 				OUT		DATE,
		po_incorporation_no  				OUT		VARCHAR2,
		po_incorporation_date 				OUT		DATE,
		po_credit_date  					OUT		DATE,
		po_credit_param_type 				OUT		VARCHAR2,
		po_dep_chg_rsn_cd  					OUT		VARCHAR2,
		po_selected_market_account 			OUT		NUMBER,
		po_selected_company_name 			OUT		VARCHAR2,
		po_credit_card_first6               OUT VARCHAR2,
		po_credit_card_last4                OUT VARCHAR2
		) RETURN NUMBER;



END CRDCHECK_RESULT_PKG;


/

SHO err

CREATE OR REPLACE PACKAGE BODY CRDCHECK_RESULT_PKG
AS
	FUNCTION getVersion RETURN VARCHAR2
   IS
   BEGIN
	   RETURN version_no;
	END getVersion;
	
	FUNCTION getLastCreditCheckResultByBan (
		pi_ban         							IN  	NUMBER,
		pi_product_type 						IN 		CHAR,
		po_credit_class 						OUT		VARCHAR2,
		po_credit_limit 						OUT		NUMBER,
		po_deposit_amt  						OUT		NUMBER,
		po_credit_result2 						OUT		VARCHAR2,
		po_beacon_score 						OUT		NUMBER,
		po_credit_referral_flag 			OUT		VARCHAR2,
		po_french_message 					OUT		VARCHAR2,
		po_credit_req_sts 					OUT		VARCHAR2,
		po_sin  							OUT		VARCHAR2,
		po_drivr_licns_no 					OUT		VARCHAR2,
		po_date_of_birth  					OUT		DATE,
		po_credit_card_no  					OUT		VARCHAR2,
		po_crd_card_exp_date 				OUT		DATE,
		po_incorporation_no  				OUT		VARCHAR2,
		po_incorporation_date 				OUT		DATE,
		po_credit_date  					OUT		DATE,
		po_credit_param_type 				OUT		VARCHAR2,
		po_dep_chg_rsn_cd  					OUT		VARCHAR2,
		po_selected_market_account 			OUT		NUMBER,
		po_selected_company_name 			OUT		VARCHAR2,
		po_credit_card_first6               OUT VARCHAR2,
		po_credit_card_last4                OUT VARCHAR2
		)
		
		RETURN NUMBER
		
		IS
		business_seq_no  NUMBER(9);
		business_line    NUMBER(9);
		CURSOR c_credit_result
		IS
			SELECT ch.credit_class, ch.credit_limit, cd.deposit_amt, ch.credit_result_2,
       				beacon_score, ccd.credit_referral_flg, ccd.french_message,
       				ch.credit_req_sts, ch.SIN, ch.drivr_licns_no, ch.date_of_birth,
       				ch.credit_card_no, ch.crd_card_exp_date, ch.incorporation_no,
       				ch.incorporation_date, ch.credit_date, ch.credit_param_type,
       				ch.dep_chg_rsn_cd, cbl.market_account, cbl.company_name, ch.business_l_seq_no, ch.business_l_line,
       				ch.pymt_card_first_six_str,ch.pymt_card_last_four_str
  		FROM credit_history ch, crd_deposit cd, credit_class_decision ccd, crd_business_list cbl
 			WHERE ch.ban = pi_ban
   					AND ch.crd_seq_no =
          			(SELECT MAX (crd_seq_no)
             			FROM credit_history ch2
            			WHERE ch2.ban = ch.ban
              					AND (ch2.credit_req_sts = 'D' OR ch2.credit_req_sts IS NULL))
   					AND (ch.credit_req_sts = 'D' OR ch.credit_req_sts IS NULL)
   					AND cd.deposit_seq_no(+) = ch.deposit_seq_no
   					AND cd.product_type(+) = pi_product_type
   					AND ccd.credit_message_cd(+) = SUBSTR (ch.credit_result_2, 1, 3) 
   					AND cbl.business_l_seq_no(+) = ch.business_l_seq_no 
   					AND cbl.crd_seq_no(+) = ch.crd_seq_no;

     credit_rec  c_credit_result%ROWTYPE;
     
     
     CURSOR c_selected_crd_business
     IS
     	 SELECT market_account, company_name
     	 FROM crd_business_list
     	 WHERE business_l_seq_no = business_seq_no AND business_l_line = business_line;
     	 
     credit_business_rec c_selected_crd_business%ROWTYPE;

		BEGIN
      OPEN c_credit_result;
      FETCH c_credit_result
       INTO credit_rec;

      IF c_credit_result%FOUND
      THEN
      	po_credit_class := credit_rec.credit_class;
      	po_credit_limit := credit_rec.credit_limit;
      	po_deposit_amt := credit_rec.deposit_amt;
				po_credit_result2  := credit_rec.credit_result_2;
				po_beacon_score := credit_rec.beacon_score;
				po_credit_referral_flag := credit_rec.credit_referral_flg;
				po_french_message := credit_rec.french_message;
				po_credit_req_sts := credit_rec.credit_req_sts;
				po_sin := credit_rec.sin;
				po_drivr_licns_no := credit_rec.drivr_licns_no;
				po_date_of_birth  := credit_rec.date_of_birth;
				po_credit_card_no := credit_rec.credit_card_no;
				po_crd_card_exp_date  := credit_rec.crd_card_exp_date;
				po_incorporation_no := credit_rec.incorporation_no;
				po_incorporation_date := credit_rec.incorporation_date;
				po_credit_date := credit_rec.credit_date;
				po_credit_param_type := credit_rec.credit_param_type;
				po_dep_chg_rsn_cd := credit_rec.dep_chg_rsn_cd;
				po_selected_market_account := credit_rec.market_account;
				po_selected_company_name := credit_rec.company_name;
				business_seq_no := credit_rec.business_l_seq_no;
				business_line := credit_rec.business_l_line;
				po_credit_card_first6 := credit_rec.pymt_card_first_six_str;
				po_credit_card_last4 := credit_rec.pymt_card_last_four_str;
				
		IF business_seq_no IS NOT NULL AND po_selected_market_account IS NULL
				THEN
					IF business_line IS NULL
					THEN
						business_line := 0;
					END IF;
					
					OPEN 	c_selected_crd_business;
					FETCH c_selected_crd_business
						INTO credit_business_rec;
						
					IF c_selected_crd_business%FOUND
					THEN
						po_selected_market_account := credit_business_rec.market_account;
						po_selected_company_name := credit_business_rec.company_name;
					END IF;     
      	END IF;
      		RETURN numeric_true;
      END IF;

			RETURN numeric_false;   		
		EXCEPTION
   WHEN OTHERS
   THEN
       raise_application_error
                      (-20102,
                          'getLastCreditCheckResultByBan Query Failed. Oracle:(['
                       || SQLCODE
                       || '] ['
                       || SQLERRM
                       || '])');

	END getLastCreditCheckResultByBan;
	
END CRDCHECK_RESULT_PKG;
/


SHO err
