package com.telus.eas.utility.info;

/**
 * <p>Title: Telus Domain Project</p>
 * <p>Description: Collection State Info - combination of collection path and step</p>
 * <p>Copyright: Copyright (c) 2004</p>
 *
 */
import com.telus.eas.framework.info.*;
import com.telus.api.reference.Reference;

public class CollectionStateInfo extends Info implements Reference{

  static final long serialVersionUID = 1L;

  private String path;
  private int step;
  private String  collectionActivityCode;


  public String getCode() {
    return path + step;
  }

  public String getDescription() {
     return collectionActivityCode;
   }

 public String getDescriptionFrench() {
    return collectionActivityCode;
  }


  public String getPath() {
    return path;
  }
  public void setPath(String path) {
    this.path = path;

  }
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
    this.collectionActivityCode =  collectionActivityCode;
  }


}