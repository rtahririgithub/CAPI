package com.telus.provider.util;

import java.io.Serializable;

import com.telus.api.TelusAPIException;
import com.telus.eas.account.info.PostpaidBusinessPersonalAccountInfo;
import com.telus.eas.account.info.PostpaidBusinessRegularAccountInfo;
import com.telus.eas.account.info.PostpaidConsumerAccountInfo;
import com.telus.provider.account.TMAccount;

public class MikeSharedFleetApplication implements Serializable {
    
    private String legalBusinessName = "";
    private String ban = "";
    private String contactName = "";
    private String businessPhoneNumber = "";
    private String faxNumber = "";
    private String primaryLine = "";
    private String secondaryLine = "";
    private String city = "";
    private String province = "";
    private String postalCode = "";
    
    public MikeSharedFleetApplication(TMAccount tmAccount) throws TelusAPIException {
        
        if (tmAccount == null) {
            throw new java.lang.IllegalArgumentException("Illegal argument: [TMAccount tmAccount] cannot be null.");
        }
        
        // Based on account type, get the business / contact phone number and extension.
        String phoneNumber, extension;
        if (tmAccount.isPostpaidBusinessRegular() || tmAccount.isCorporate()) {
            legalBusinessName = ((PostpaidBusinessRegularAccountInfo)tmAccount.getDelegate0()).getLegalBusinessName();
            phoneNumber = ((PostpaidBusinessRegularAccountInfo)tmAccount.getDelegate0()).getContactPhone();
            extension = ((PostpaidBusinessRegularAccountInfo)tmAccount.getDelegate0()).getContactPhoneExtension();
        } else if (tmAccount.isPostpaidBusinessPersonal()) {
            legalBusinessName = ((PostpaidBusinessPersonalAccountInfo)tmAccount.getDelegate0()).getLegalBusinessName();
            phoneNumber = ((PostpaidBusinessPersonalAccountInfo)tmAccount.getDelegate0()).getBusinessPhone();
            extension = ((PostpaidBusinessPersonalAccountInfo)tmAccount.getDelegate0()).getBusinessPhoneExtension();
        } else if (tmAccount.isPostpaidConsumer()) {
            phoneNumber = ((PostpaidConsumerAccountInfo)tmAccount.getDelegate0()).getBusinessPhone();
            extension = ((PostpaidConsumerAccountInfo)tmAccount.getDelegate0()).getBusinessPhoneExtension();
        } else {
            phoneNumber = tmAccount.getContactPhone();
            extension = tmAccount.getContactPhoneExtension();
        }
        businessPhoneNumber = formatPhoneNumber(phoneNumber, extension);
        ban = String.valueOf(tmAccount.getBanId());
        contactName = formatNullString(tmAccount.getContactName().getTitle(), " ") 
        	+ formatNullString(tmAccount.getContactName().getFirstName(), " ") 
        	+ formatNullString(tmAccount.getContactName().getMiddleInitial(), " ") 
        	+ formatNullString(tmAccount.getContactName().getLastName());
        
        // Parse the address to get the primary line.
       	primaryLine = formatNullString(tmAccount.getAddress0().getPrimaryLine());
       	secondaryLine = formatNullString(tmAccount.getAddress0().getSecondaryLine());
       	if (!primaryLine.equals("") && !secondaryLine.equals("")) {
        	primaryLine += ", " + secondaryLine;
        }
       	if (primaryLine.equals("") && !secondaryLine.equals("")) {
        	primaryLine = secondaryLine;
        }
        city = tmAccount.getAddress0().getCity();
        province = tmAccount.getAddress0().getProvince();
        postalCode = tmAccount.getAddress0().getPostalCode();
    }
    
    public String getFaxNumber() {
        return faxNumber;
    }
    
    public void setFaxNumber(String faxNumber) {
        this.faxNumber = formatPhoneNumber(faxNumber);
    }
   
    public String getBan() {
        return ban;
    }
 
    public String getBusinessPhoneNumber() {
        return businessPhoneNumber;
    }

    public String getContactName() {
        return contactName;
    }
    
    public String getLegalBusinessName() {
        return legalBusinessName;
    }
    
    public String getCity() {
        return city;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public String getPrimaryLine() {
        return primaryLine;
    }

    public String getProvince() {
        return province;
    }
    
    private String formatPhoneNumber(String phoneNumber) {
        return formatPhoneNumber(phoneNumber, null);
    }
    
    private String formatPhoneNumber(String phoneNumber, String extension) {
        
        if (phoneNumber == null || phoneNumber.trim().length() == 0) {
            return "";
        }
        
        String _1, _2, _3;
        
        _1 = phoneNumber.substring(0, 3);
        _2 = phoneNumber.substring(3, 6);
        _3 = phoneNumber.substring(6);

        StringBuffer strBuf = new StringBuffer();
        strBuf.append("(" + _1 + ") " + _2 + "-" + _3);
        
        if (extension != null) {
            strBuf.append(" x").append(extension);
        }
        
        return strBuf.toString();
    }
       
    private String formatNullString(String param) {
        return param == null ? "" : param.trim();
    }
    
    private String formatNullString(String param, String appendIfNotNull) {
        return param == null ? "" : param.trim() + appendIfNotNull;
    }
}
