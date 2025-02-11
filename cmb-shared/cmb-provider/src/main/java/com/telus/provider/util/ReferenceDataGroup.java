/*
 * $Id$
 * %E% %W%
 * Copyright (c) Telus Mobility Inc. All Rights Reserved.
 */

package com.telus.provider.util;

import com.telus.api.*;
import com.telus.api.reference.*;
import java.util.*;
import java.io.*;

public interface ReferenceDataGroup {
  Reference get(String code) throws TelusAPIException;

  Reference[] getAll() throws TelusAPIException;

  int getSize();

  void reload();

  int getOrder();

  Throwable getException();

}


