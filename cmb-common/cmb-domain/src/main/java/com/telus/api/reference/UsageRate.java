package com.telus.api.reference;


public interface UsageRate {

public final static int TO_UNLIMITED     = 99999999;
int getFrom();
int getTo();
double getRate();
String getUsageUnitCode();
}