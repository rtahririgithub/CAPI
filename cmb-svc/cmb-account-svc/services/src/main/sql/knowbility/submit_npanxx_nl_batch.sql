variable jobno number;

begin
	dbms_job.submit(:jobno, 'nl_ngp_pkg.NPANXX_NL_Batch;',to_date(to_char(sysdate,'mmddyyyy')||' 01:30','mmddyyyy hh24:mi'),'sysdate + 1/24');
commit;
end;
/

print jobno