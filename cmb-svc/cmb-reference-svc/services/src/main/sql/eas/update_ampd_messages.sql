update adapted_message
   set audience_type_id = null, 
       brand_id = 2
 where audience_type_id is not null;
 
commit;                                      

