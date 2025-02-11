package com.telus.api.account;

public interface CollectionStep {
  int getStep();
  String getCollectionActivityCode() ;
  java.util.Date getTreatmentDate();
  String getPath();  
}