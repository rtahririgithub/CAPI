CREATE OR REPLACE PACKAGE INVOICE_PKG
AS
-------------------------------------------------------------------------
-- IMPORTANT NOTE !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
-- Please increment the version_no variable value when you make changes to this package 
-- to reflect version changes.
--------------------------------------------------------------------------
-- Date           Version	Developer           Modifications
-- Mar 26, 2013   3.23.1	Tsz Chung Tong      Refactored query from getAccountInfoByBAN from RA_UTILITY_PKG to this package

-------------------------------------------------------------------------------------------------------
	TYPE refcursor IS REF CURSOR;
	version_no          CONSTANT VARCHAR2(10)       := '3.23.1';
	
	FUNCTION getVersion RETURN VARCHAR2; 
	
	PROCEDURE getLatestBillDueDate(
		pi_ban	NUMBER,
		po_bill_due_date    OUT  bill.bill_due_date%type
	);
	
	

END INVOICE_PKG;
/

SHO err

CREATE OR REPLACE PACKAGE BODY INVOICE_PKG
AS

FUNCTION getVersion RETURN VARCHAR2
   IS
   BEGIN
	   RETURN version_no;
	END getVersion;

PROCEDURE getLatestBillDueDate(
		pi_ban	NUMBER,
		po_bill_due_date    OUT  bill.bill_due_date%type
	)
	IS
	CURSOR c_bill
      IS
         SELECT BILL_DUE_DATE 
		FROM (
         SELECT   bill_due_date 
             FROM bill
            WHERE ban = pi_ban
              AND bill_conf_status = 'C'
              AND TO_CHAR (bill_due_date, 'yyyy') != '4700'
         ORDER BY bill_due_date DESC)
		WHERE ROWNUM <= 1;
	BEGIN
	OPEN c_bill;
	FETCH c_bill INTO po_bill_due_date;
	CLOSE c_bill;
	
	END getLatestBillDueDate;
END INVOICE_PKG;
/

SHO err