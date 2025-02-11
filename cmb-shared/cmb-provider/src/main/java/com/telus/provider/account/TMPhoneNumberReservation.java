package com.telus.provider.account;


/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */
import com.telus.api.account.*;
import com.telus.eas.account.info.*;
import com.telus.api.reference.NumberGroup;

public class TMPhoneNumberReservation implements PhoneNumberReservation{

  PhoneNumberReservationInfo delegate;

  public TMPhoneNumberReservation() {
     delegate = new PhoneNumberReservationInfo();
  }

  public TMPhoneNumberReservation getPhonenumberReservation(){
    return this;
  }

  public PhoneNumberReservationInfo getPhonenumberReservation0(){
    return delegate;
  }

  public void setNumberGroup(NumberGroup numberGroup){
    delegate.setNumberGroup(numberGroup);
  }

  public NumberGroup getNumberGroup(){
   return delegate.getNumberGroup();
  }

  public String getProductType(){
   return delegate.getProductType();
  }

  public void setProductType(String productType){
   delegate.setProductType(productType);
  }

  public String getPhoneNumberPattern(){
   return delegate.getPhoneNumberPattern();
  }

  public void setPhoneNumberPattern(String phoneNumberPattern){
      delegate.setPhoneNumberPattern(phoneNumberPattern);
    }

  public boolean isAsian(){
   return delegate.isAsian();
  }

  public void setAsian(boolean asian){
    delegate.setAsian(asian);
  }

  public boolean isLikeMatch(){
   return delegate.isLikeMatch();
  }

  public void setLikeMatch(boolean likeMatch){
      delegate.setLikeMatch(likeMatch);
  }

  public boolean getWaiveSearchFee(){
   return delegate.getWaiveSearchFee();
  }

  public void setWaiveSearchFee(boolean waiveSearchFee){
   delegate.setWaiveSearchFee(waiveSearchFee);
  }

  public void clear(){
    delegate.clear();
  }


}