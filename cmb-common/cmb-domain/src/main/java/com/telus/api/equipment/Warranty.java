package com.telus.api.equipment;

import java.util.*;

public interface Warranty {

Date   getWarrantyExpiryDate();
Date   getInitialActivationDate();
Date   getDOAExpiryDate();

/**
 * @deprecated
 */
Date   getInitialManufactureDate();

/**
 * @deprecated
 */
Date   getLatestPendingDate();

/**
 * @deprecated
 */
String getLatestPendingModel();

/**
 * @deprecated
 */
Date   getWarrantyExtensionDate();

String getMessage();
}