package com.telus.provider.account;


import java.util.Date;

import com.telus.api.account.CollectionStep;
import com.telus.eas.account.info.CollectionStepInfo;
import com.telus.provider.BaseProvider;
import com.telus.provider.TMProvider;

public class TMCollectionStep  extends BaseProvider implements CollectionStep{

	private static final long serialVersionUID = 1L;
	private final CollectionStepInfo delegate;

 public TMCollectionStep(TMProvider provider, CollectionStepInfo delegate) {
   super(provider);
   this.delegate = delegate;
 }

 public CollectionStepInfo getDelegate() {
   return delegate;
 }


 //--------------------------------------------------------------------
 //  Decorative Methods
 //--------------------------------------------------------------------
 public void setPath(String newPath) {
   delegate.setPath(newPath);
 }

 public String getPath() {
   return delegate.getPath();
 }


 public void setStep(int newStep) {
   delegate.setStep(newStep);
 }

 public int getStep() {
   return delegate.getStep();
 }

 public void setTreatmentDate(Date newTreatmentDate) {
   delegate.setTreatmentDate(newTreatmentDate);
 }

 public Date getTreatmentDate() {
   return delegate.getTreatmentDate();
 }

 public void setCollectionActivityCode(String newCollectionActivityCode) {
   delegate.setCollectionActivityCode(newCollectionActivityCode);
 }

 public String getCollectionActivityCode() {
   String  collectionActivityCode = null;

 try {
   if (! (delegate.getCollectionActivityCode() == null))
   {
    collectionActivityCode= delegate.getCollectionActivityCode();
   }
   else
   {
     collectionActivityCode = (provider.getReferenceDataManager0().getCollectionState(getPath() +
         getStep())).getCollectionActivityCode();
   }
 } catch (Throwable e) {
    }
return collectionActivityCode;
 }



}