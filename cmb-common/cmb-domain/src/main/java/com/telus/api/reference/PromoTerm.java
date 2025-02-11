package com.telus.api.reference;


public interface PromoTerm {

String getRegularServiceCode();

int getTermMonth();

boolean isAvailableForActivation();

 boolean isAvailableForChange();
}