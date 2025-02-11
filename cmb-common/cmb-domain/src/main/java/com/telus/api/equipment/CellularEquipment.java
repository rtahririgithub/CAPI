package com.telus.api.equipment;

/**
 * Title:        Telus Domain Project -KB61
 * Description:
 * Copyright:    Copyright (c) 2002
 * Company:
 * @author
 * @version 1.0
 */
import com.telus.api.*;

public interface CellularEquipment  extends Equipment  {

  boolean isPrimary();

  boolean isValidForPrepaid();

  boolean isValidForPrepaidActivationWithoutPin() throws TelusAPIException;

}