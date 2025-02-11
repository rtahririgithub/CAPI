package com.telus.api.account;

import java.util.Date;

public interface AddressHistory {

    Date getEffectiveDate();

    Date getExpirationDate();

    Address getAddress();
}
