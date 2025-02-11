package com.telus.eas.account.info;


import com.telus.api.account.CollectionStep;
import com.telus.eas.framework.info.Info;

public class CollectionStepInfo extends Info implements CollectionStep {
   
   static final long serialVersionUID = 1L;

  private int step;
  private String collectionActivityCode;
  private java.util.Date treatmentDate;
  private String path;


  public int getStep() {
    return step;
  }
  public void setStep(int step) {
    this.step = step;
  }
  public String getCollectionActivityCode() {
    return collectionActivityCode;
  }
  public void setCollectionActivityCode(String collectionActivityCode) {
    this.collectionActivityCode = collectionActivityCode;
  }
  public java.util.Date getTreatmentDate() {
    return treatmentDate;
  }
  public void setTreatmentDate(java.util.Date treatmentDate) {
    this.treatmentDate = treatmentDate;
  }

  public void copyFrom(CollectionStepInfo o) {
     step = o.step;
     collectionActivityCode   = o.collectionActivityCode;
     treatmentDate = cloneDate(o.treatmentDate);
     path = o.path;
   }

   public String toString() {
     StringBuffer s = new StringBuffer();

     s.append("CollectionStateInfo:{\n");
     s.append("    path=[").append(path).append("]\n");          
     s.append("    step=[").append(step).append("]\n");
     s.append("    collectionActivityCode=[").append(collectionActivityCode).append("]\n");
     s.append("    treatmentDate=[").append(treatmentDate).append("]\n");
     s.append("}");

     return s.toString();
   }
  public String getPath() {
    return path;
  }
  public void setPath(String path) {
    this.path = path;
  }

  public boolean isMatch( CollectionStepInfo aStep )
  {
	  boolean result = step == aStep.getStep()	&& 
	  	( treatmentDate != null 
	  		&& aStep.treatmentDate != null 
			&& treatmentDate.equals(aStep.treatmentDate )
			);
	  return result;
  }
}
