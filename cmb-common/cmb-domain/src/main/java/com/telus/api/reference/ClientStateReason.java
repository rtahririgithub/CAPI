/**
 * Title:        ClientStateReason<p>
 * Description:  Represent the Activity/Reason Codes  mapped in CRDB as ClientStateReasonID<p>
 * Copyright:    Copyright (c)<p>
 * Company:      Telus Mobility INC. <p>
 * @author       Carlos Manjarres
 * @version      1.0
 */

package com.telus.api.reference;

/**
 * getCode() is actually the combination of ActivityCode and ReasonCode
 * getReasonId() is the CRDB CLIENT_STATE_REASON_ID which corresponds to a unique
 *         combination of  KB_ACTIVTY_CD and KB_ACTIVTY_REASON_CD
 */
public interface ClientStateReason extends Reference{

   long   getReasonId() ;
}
