package com.telus.api;

import com.telus.api.account.AccountSummary;
import com.telus.api.account.Subscriber;

public class BrandNotSupportedException extends TelusAPIException {

    private AccountSummary accountSummary;
    private Subscriber subscriber;
    private int[] supportedBrandIds;
    
    public BrandNotSupportedException(int invalidBrandId) {
        super("brand ID [" + invalidBrandId + "] not supported");
    }
    
    public BrandNotSupportedException(AccountSummary accountSummary,
            int[] supportedBrandIds) {
        super("account is not supported by brand ID");
        this.accountSummary = accountSummary;
        this.supportedBrandIds = supportedBrandIds;
    }

    public BrandNotSupportedException(Subscriber subscriber,
            int[] supportedBrandIds) {
        super("subscriber is not supported by brand ID");
        this.subscriber = subscriber;
        this.supportedBrandIds = supportedBrandIds;
    }

    public AccountSummary getAccountSummary() {
        try {
            if (subscriber != null)
                return subscriber.getAccount();
        } catch (TelusAPIException e) {
            e.printStackTrace();
        }
        return accountSummary;
    }

    public Subscriber getSubscriber() {
        return subscriber;
    }

    public String toString() {
        StringBuffer s = new StringBuffer(128);
        int banId = 0;
        String subscriberId = null;
        int supportedBrand = 0;
        if (accountSummary != null) {
            banId = accountSummary.getBanId();
            supportedBrand = accountSummary.getBrandId();
        } else if (subscriber != null) {
            banId = subscriber.getBanId();
            subscriberId = subscriber.getSubscriberId();
            supportedBrand = subscriber.getBrandId();
        }
        s.append("BrandNotSupportedException: " + (getMessage() != null ? getMessage() : "") + "\n");
        s.append("    ban=[").append(banId).append("]\n");
        s.append("    subscriber ID=[").append(subscriberId).append("]\n");
        s.append("    supported brands=[").append(supportedBrand).append("]\n");
        s.append("    user ID supports [");
        if (supportedBrandIds != null) {
            for (int i = 0; i < supportedBrandIds.length; i++) {
                s.append(supportedBrandIds[i]).append('\n');
            }
        }
        s.append("]");
        return s.toString();
    }

    public int[] getSupportedBrandIds() {
        return supportedBrandIds;
    }

}
