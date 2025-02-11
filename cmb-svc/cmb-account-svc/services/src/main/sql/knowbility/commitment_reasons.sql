delete from generic_codes_ext where generic_type = 'COMM_REASON';

insert into generic_codes_ext 
values ('COMM_REASON', 'EQU', 'Equipment Upgrade', 'Renouvellement d''appareil');

insert into generic_codes_ext 
values ('COMM_REASON', 'PPD', 'Standard', 'Standard');

insert into generic_codes_ext 
values ('COMM_REASON', 'TTL', 'Talk Total Contract', 'Contrat Tout à Parler');

insert into generic_codes_ext 
values ('COMM_REASON', 'VRT', 'Virtual Contract no Paperwork', 'Contrat virtuel sans papier');

insert into generic_codes_ext 
values ('COMM_REASON', 'CRD', 'Bill Credit', 'Crédit sur le relevé');

insert into generic_codes_ext 
values ('COMM_REASON', 'FTR', 'Feature', 'Service optionnel');

insert into generic_codes_ext 
values ('COMM_REASON', 'HDW', 'Hardware', 'Appareil');



