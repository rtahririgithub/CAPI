package com.telus.provider.account;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */
import java.util.Date;
import com.telus.api.account.*;
import com.telus.eas.account.info.*;

public class TMMemoCriteria implements MemoCriteria {
  MemoCriteriaInfo delegate;

  public TMMemoCriteria() {
     delegate = new MemoCriteriaInfo();
  }

  public TMMemoCriteria getMemoCriteria() {
    return this;
  }

  public MemoCriteriaInfo getMemoCriteria0() {
    return delegate;
  }

  public int getBanId() {
    return delegate.getBanId();
  }
  
  public String getSubscriberId() {
    return delegate.getSubscriberId();
  }
  
  public String[] getSubscriberIds(){
    return delegate.getSubscriberIds();
  }
  
  public String getSystemText() {
    return delegate.getSystemText();
  }
  
  public String getManualText() {
    return delegate.getManualText();
  }
  
  public Date getDateFrom() {
    return delegate.getDateFrom();
  }
  
  public Date getDateTo() {
    return delegate.getDateTo();
  }
  
  public String getType() {
    return delegate.getType();
  }
  
  public String[] getTypes() {
    return delegate.getTypes();
  }
  
  
  public int getSearchLimit() {
    return delegate.getSearchLimit();
  }
  
  public void setBanId(int value) {
    delegate.setBanId(value);
  }
  
  public void setSubscriberId(String value) {
    delegate.setSubscriberId(value);
  }
  
  public void setSubscriberIds(String[] value) {
    delegate.setSubscriberIds(value);
  }
  
  public void setSystemText(String value) {
    delegate.setSystemText(value);
  }
  
  public void setManualText(String value) {
    delegate.setManualText(value);
  }
  
  public void setDateFrom(Date value) {
    delegate.setDateFrom(value);
  }
  
  public void setDateTo(Date value) {
    delegate.setDateTo(value);
  }
  
  public void setType(String value) {
    delegate.setType(value);
  }
  
  public void setTypes(String[] value) {
    delegate.setTypes(value);
       
  }
  
  public void setSearchLimit(int value) {
    delegate.setSearchLimit(value);
       
  }

  public void clear(){
    delegate.clear();
  }
}
