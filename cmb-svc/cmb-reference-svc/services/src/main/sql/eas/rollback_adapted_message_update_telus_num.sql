--Jul 30, 2015
--Rollback TELUS number from 1-877-868-3587 to 1-877-TO-TELUS

update adapted_message set message_text = 'Your transfer request is active.  Please call us at 1-877-TO-TELUS and select the ''finish my transfer'' option.' where APPLICATION_MESSAGE_ID = '103' and ADAPTED_MESSAGE_SEQ = '1';

update adapted_message set message_text = 'We are experiencing difficulties in processing your transfer request. Please call us at 1-877-TO-TELUS and select the ''resolve an issue'' option. Thank you.' where APPLICATION_MESSAGE_ID = '105' and ADAPTED_MESSAGE_SEQ = '1';