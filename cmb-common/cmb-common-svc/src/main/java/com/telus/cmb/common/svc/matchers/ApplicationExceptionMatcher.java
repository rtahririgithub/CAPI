package com.telus.cmb.common.svc.matchers;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

import com.telus.api.ApplicationException;

public class ApplicationExceptionMatcher extends TypeSafeMatcher<ApplicationException> {  
	  
    public static ApplicationExceptionMatcher hasErrorCode(String item) {  
        return new ApplicationExceptionMatcher(item);  
    }  
  
    private String foundErrorCode;  
    private final String expectedErrorCode;  
  
    private ApplicationExceptionMatcher(String expectedErrorCode) {  
        this.expectedErrorCode = expectedErrorCode;  
    }  
  
    @Override  
    protected boolean matchesSafely(final ApplicationException exception) {  
        foundErrorCode = exception.getErrorCode();  
        return foundErrorCode.equalsIgnoreCase(expectedErrorCode);  
    }  
  
    @Override  
    public void describeTo(Description description) {  
        description.appendValue(foundErrorCode)  
                .appendText(" was not found instead of ")  
                .appendValue(expectedErrorCode);  
    }  
}  
