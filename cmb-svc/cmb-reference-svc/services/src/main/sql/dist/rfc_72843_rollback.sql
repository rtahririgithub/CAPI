DELETE FROM swap_group_type where swap_group_id  >= 10106 and swap_group_id <= 10125 AND swap_type_id='*' AND INCLUSIVE_IND='0' AND SWAP_PARAMETER_CD='*';

DELETE FROM swap_group where swap_group_id  >= 10106 and swap_group_id <= 10125;



COMMIT;