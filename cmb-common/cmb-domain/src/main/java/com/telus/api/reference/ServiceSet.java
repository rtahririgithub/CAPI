/**
 * Title:        <p>
 * Description:  <p>
 * Copyright:    Copyright (c) <p>
 * Company:      <p>
 * @author
 * @version 1.0
 */
 package com.telus.api.reference;

public interface ServiceSet extends Reference {

   public static final String CODE_RIM = "RIM";
   public static final String CODE_PDA = "PDA";

   Service[] getServices();

   boolean contains(String seviceCode);

}
