CREATE OR REPLACE TRIGGER  tr_activation_log_bur
BEFORE UPDATE or INSERT
ON activation_log
REFERENCING NEW AS NEW OLD AS OLD
FOR EACH ROW
WHEN ((old.customer_id=0 or old.customer_id is null) and new.customer_id != 0 and new.client_id not in (0,27,910,2497,14625))
declare
transfer_succeed	boolean:=false;
begin
EAS_Portal.PU_BA_Link_Transfer(:new.client_id,:new.customer_id,transfer_succeed);
If transfer_succeed Then
  :new.transferred_to_CRDB:='Y';
End If;
Exception
When Others Then
 declare
   v_ecode number(8) :=  SQLCODE;
   v_emsg  varchar2(2000) := SQLERRM;
   begin
   EAS_Portal.Write_Error_Log ('Trigger tr_activation_log_bur'
			   	,v_ecode
			        ,v_emsg );
  end;
end;
/