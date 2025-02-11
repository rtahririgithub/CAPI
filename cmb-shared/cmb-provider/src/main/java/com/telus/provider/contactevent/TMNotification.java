package com.telus.provider.contactevent;

import java.util.Date;
import com.telus.api.reference.NotificationType;
import com.telus.api.contactevent.*;
import com.telus.provider.*;
import com.telus.eas.contactevent.info.*;
import com.telus.eas.utility.info.*;


public class TMNotification extends BaseProvider implements Notification {

  private NotificationInfo delegate;

  public TMNotification(TMProvider provider, NotificationInfo delegate) {
    super(provider);
    this.delegate = delegate;
  }

  public NotificationInfo getDelegate() {
    return delegate;
  }

  public int getBanId() {
    return delegate.getBanId();
  }
  public void setBanId(int ban) {
    delegate.setBanId(ban);
  }
  public String getSubscriberNumber() {
    return delegate.getSubscriberNumber();
  }
  public void setSubscriberNumber(String subscriberNumber) {
    delegate.setSubscriberNumber(subscriberNumber);
  }
  public String getProductType() {
    return delegate.getProductType();
  }
  public void setProductType(String productType) {
    delegate.setProductType(productType);
  }
  public String getLanguage() {
    return delegate.getLanguage();
  }
  public void setLanguage(String language) {
    delegate.setLanguage(language);
  }
  public String[] getContentParameters() {
    return delegate.getContentParameters();
  }
  public void setContentParameters(String[] contentParameters) {
    delegate.setContentParameters(contentParameters);
  }
  public NotificationType getNotificationType() {
    return delegate.getNotificationTypeInfo();
  }
  public void setNotificationType(NotificationType notificationType) {
    delegate.setNotificationTypeInfo((NotificationTypeInfo)notificationType);
  }
  public Date getDeliveryDate() {
    return delegate.getDeliveryDate();
  }
  public void setDeliveryDate(Date deliveryDate) {
    delegate.setDeliveryDate(deliveryDate);
  }
  public long getTimeToLive() {
    return delegate.getTimeToLive();
  }
  public void setTimeToLive(long timeToLive) {
    delegate.setTimeToLive(timeToLive);
  }
  public int getPriority() {
    return delegate.getPriority();
  }
  public void setPriority(int priority) {
    delegate.setPriority(priority);
  }
  public boolean isValidatingInputRequest() {
    return delegate.isValidatingInputRequest();
  }
  public void setValidatingInputRequest(boolean validatingInputRequest) {
    delegate.setValidatingInputRequest(validatingInputRequest);
  }
  public boolean isPreventingDuplicate() {
    return delegate.isPreventingDuplicate();
  }
  public void setPreventingDuplicate(boolean preventingDuplicate) {
    delegate.setPreventingDuplicate(preventingDuplicate);
  }

  public String getApplication() {
    return delegate.getApplication();
  }

}
