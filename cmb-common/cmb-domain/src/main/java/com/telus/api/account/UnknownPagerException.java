/* Generated by Together */

package com.telus.api.account;

import com.telus.api.TelusAPIException;
public class UnknownPagerException extends TelusAPIException {

String capCode;
String coverageRegion;

 public UnknownPagerException(String message, Throwable exception, String capCode, String coverageRegion) {
      super(message, exception);
      this.capCode = capCode;
      this.coverageRegion = coverageRegion;
    }

    public UnknownPagerException(Throwable exception, String capCode, String coverageRegion) {
      super(exception);
      this.capCode = capCode;
      this.coverageRegion = coverageRegion;
    }

}
