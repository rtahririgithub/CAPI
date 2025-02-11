package com.telus.eas.subscriber.info;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.telus.api.UnknownObjectException;
import com.telus.eas.framework.info.Info;


public class BaseAgreementInfo extends Info {
  static final long serialVersionUID = 1L;


  public static final byte ADD    = (byte) 'I';
  public static final byte DELETE = (byte) 'R';
  public static final byte UPDATE = (byte) 'U';
  public static final byte NO_CHG = (byte) 'N';

  private byte transaction = NO_CHG;
  private Date expiryDate;
  private Date effectiveDate;
  private String code;
//  private transient BaseAgreementInfo parent;
  private String parentCode;
  private String serviceSequenceNo;
  private String serviceVersionNo;



  public BaseAgreementInfo() {
  }

//  public BaseAgreementInfo getParent() {
//    return parent;
//  }
//
//  public void setParent(BaseAgreementInfo parent) {
//    this.parent = parent;
//  }

  public String getParentCode() {
    return parentCode;
  }

  public void setParent(BaseAgreementInfo parent) {
    this.parentCode = parent.getCode();
//    System.err.println("parent.getCode()=[" +parent.getCode()+ "]");
  }

  public void setThisAsParent(Map children) {
    BaseAgreementInfo[] info = (BaseAgreementInfo[])children.values().toArray(new BaseAgreementInfo[children.size()]);
    for (int i = 0; i < info.length; i++) {
      info[i].setParent(this);
    }
  }


  public byte getTransaction0() {
    return transaction;
  }

  public byte getTransaction() {
    byte t = transaction;
    if (t == NO_CHG && isModified()) {
      t = UPDATE;
    }
    return t;
  }

  public void setTransaction(byte newTransaction) {
    transaction = newTransaction;
  }

  public Date getExpiryDate() {
    return expiryDate;
  }

  public void setExpiryDate(Date expiryDate) {
    this.expiryDate = expiryDate;
    setChanged();
  }

  public Date getEffectiveDate() {
    return effectiveDate;
  }

  public void setEffectiveDate(Date newEffectiveDate) {
    this.effectiveDate = newEffectiveDate;
    setChanged();
  }

  public boolean intersects(Date effectiveDate, Date expiryDate) {
    return intersects(effectiveDate, expiryDate, this.effectiveDate, this.expiryDate);
  }

  public String getCode() {
    return code;
  }
  
	// protected void setCode(String code) {
	// this.code = code;
	// }

	/**
	 * @return the serviceSequenceNo
	 */
	public String getServiceSequenceNo() {
		return serviceSequenceNo;
	}

	/**
	 * @param serviceSequenceNo
	 *            the serviceSequenceNo to set
	 */
	public void setServiceSequenceNo(String serviceSequenceNo) {
		this.serviceSequenceNo = serviceSequenceNo;
	}

	/**
	 * @return the serviceVersionNo
	 */
	public String getServiceVersionNo() {
		return serviceVersionNo;
	}

	/**
	 * @param serviceVersionNo
	 *            the serviceVersionNo to set
	 */
	public void setServiceVersionNo(String serviceVersionNo) {
		this.serviceVersionNo = serviceVersionNo;
	}

	protected void setCode(String code, int length) {
		this.code = Info.padTo(code, ' ', length);
	}

  public void setChanged() {
    // TODO: if(transaction == NO_CHG || transaction == DELETE) {
    if(transaction == NO_CHG) {
      transaction = UPDATE;
    }
  }

  public boolean isModified() {
    return transaction != NO_CHG;
  }

  public void commit() {
    transaction = NO_CHG;
  }

  public String toString() {
    StringBuffer s = new StringBuffer(128);

    s.append("BaseAgreementInfo:[\n");
    s.append("    transaction=[").append(transactionToString(transaction)).append("]\n");
    s.append("    expiryDate=[").append(expiryDate).append("]\n");
    s.append("    effectiveDate=[").append(effectiveDate).append("]\n");
    s.append("    code=[").append(code).append("]\n");
    s.append("    serviceSequenceNo=[").append(serviceSequenceNo).append("]\n");
    s.append("    serviceVersionNo=[").append(serviceVersionNo).append("]\n");
    s.append("]");

    return s.toString();
  }


  //========================================================================
  //  Helper methods
  //========================================================================
  protected static final String transactionToString(int transaction) {
    switch (transaction) {
      case ADD:
        return "ADD";
      case DELETE:
        return "DELETE";
      case UPDATE:
        return "UPDATE";
      case NO_CHG:
        return "NO_CHANGE";
      default:
        return "?" + transaction + "?";
    }
  }


  /**
   * The method is trying to lookup by the code as a key, but then, tries to lookup by a portion of the 
   * key as some of supported maps are now converted to "key"+date type of mapping. For example
   * services Map in SubscriberContractInfo.
   * 
   * @param childMap
   * @param childCode
   * @param includeDeleted
   * @return
   */
  protected static final BaseAgreementInfo getChildOrNull(Map childMap, String childCode, boolean includeDeleted) {
    if (childMap == null) {
      return null;
    }

    // get value by an exact key
    BaseAgreementInfo o = (BaseAgreementInfo)childMap.get(childCode);
    if(o == null) {
    	o = findObjectByPrefix(childMap, childCode);
    }
    
    if (o == null || (!includeDeleted && o.getTransaction() == DELETE)) {
    	return null;
    } else {
    	return o;
    }
  }

  protected static final BaseAgreementInfo getChild(Map childMap, String childCode, boolean includeDeleted) throws UnknownObjectException {
    BaseAgreementInfo o = getChildOrNull(childMap, childCode, includeDeleted);
    if(o == null) {
    	o = findObjectByPrefix(childMap, childCode);
    	if(o == null) {
    		throw new UnknownObjectException("code=[" + childCode + "]");
    	}
    }

    return o;
  }

  protected static final int getChildCount(Map childMap, boolean includeDeleted) {
    if (childMap != null) {
      if (includeDeleted) {
        return childMap.size();
      } else {
        int count = 0;
                                Iterator i = childMap.values().iterator();
        while (i.hasNext()) {
          BaseAgreementInfo o = (BaseAgreementInfo) i.next();
          if (o.getTransaction() != DELETE) {
            count++;
          }
        }
        return count;
      }
    }

    return 0;
  }

  protected static final List getChildren(Map childMap, boolean includeDeleted, List destinationlist) {
    if (childMap != null) {
      Iterator i = childMap.values().iterator();
      while (i.hasNext()) {
        BaseAgreementInfo o = (BaseAgreementInfo) i.next();
        if (includeDeleted || o.getTransaction() != DELETE) {
          destinationlist.add(o);
        }
      }
    }

    return destinationlist;
  }

  protected static final BaseAgreementInfo[] getChildren(Map childMap, Class arrayComponentType, boolean includeDeleted) {
    List list = new ArrayList(childMap.size());

    getChildren(childMap, includeDeleted, list);

    BaseAgreementInfo[] array = (BaseAgreementInfo[])java.lang.reflect.Array.newInstance(arrayComponentType, list.size());
    return (BaseAgreementInfo[])list.toArray(array);
  }

  protected static final BaseAgreementInfo[] getChildrenByTransaction(Map childMap, int transaction, Class arrayComponentType) {
    List list = new ArrayList(childMap.size());

    if (childMap != null) {
                        Iterator i = childMap.values().iterator();
      while (i.hasNext()) {
        BaseAgreementInfo o = (BaseAgreementInfo) i.next();
        if (o.getTransaction() == transaction) {
          list.add(o);
        }
      }
    }

    BaseAgreementInfo[] array = (BaseAgreementInfo[])java.lang.reflect.Array.newInstance(arrayComponentType, list.size());
    return (BaseAgreementInfo[])list.toArray(array);
  }

  protected static final BaseAgreementInfo[] getModifiedChildren(Map childMap, Class arrayComponentType) {
    List list = new ArrayList(childMap.size());

    if (childMap != null) {
                        Iterator i = childMap.values().iterator();
      while (i.hasNext()) {
        BaseAgreementInfo o = (BaseAgreementInfo) i.next();
        if (o.getTransaction() != NO_CHG) {
          list.add(o);
        }
      }
    }

    BaseAgreementInfo[] array = (BaseAgreementInfo[])java.lang.reflect.Array.newInstance(arrayComponentType, list.size());
    return (BaseAgreementInfo[])list.toArray(array);
  }

  protected static final boolean isModified(BaseAgreementInfo parent, Map childMap) {
    if(parent.getTransaction0() != NO_CHG) {
      return true;
    } else {
      if (childMap != null) {
                                Iterator i = childMap.values().iterator();
        while (i.hasNext()) {
          BaseAgreementInfo o = (BaseAgreementInfo) i.next();
          if(o.isModified()) {
            return true;
          }
        }
      }
    }
    return false;
  }

  protected static final void commit(BaseAgreementInfo parent, Map childMap) {
    parent.setTransaction(NO_CHG);

    if (childMap != null) {
                        Iterator i = childMap.values().iterator();
      while (i.hasNext()) {
        BaseAgreementInfo o = (BaseAgreementInfo) i.next();
        if (o.getTransaction() == DELETE) {
          //childMap.remove(o.getCode());
          i.remove();
        } else {
          o.commit();
        }
      }
    }
  }

  protected static final void setEffectiveDate(Map childMap, Date newEffectiveDate) {
    if (childMap != null) {
                        Iterator i = childMap.values().iterator();
      while (i.hasNext()) {
        BaseAgreementInfo o = (BaseAgreementInfo) i.next();
        if (o.getTransaction() != DELETE) {
          o.setEffectiveDate(newEffectiveDate);
        }
      }
    }
  }

  protected static final void setExpiryDate(Map childMap, Date newExpiryDate) {
    if (childMap != null) {
                        Iterator i = childMap.values().iterator();
      while (i.hasNext()) {
        BaseAgreementInfo o = (BaseAgreementInfo) i.next();
        if (o.getTransaction() != DELETE) {
          o.setExpiryDate(newExpiryDate);
        }
      }
    }
  }

  protected static final boolean containsChild(Map childMap, String childCode, Date effectiveDate, Date expiryDate, boolean includeDeleted) {
    if (childMap != null) {
      BaseAgreementInfo o = (BaseAgreementInfo) childMap.get(childCode);
      if(o == null) {
    	  o = findObjectByPrefix(childMap, childCode);
      }
      if (o != null && (includeDeleted || o.getTransaction() != DELETE) && o.intersects(effectiveDate, expiryDate)) {
        return true;
      }
    }
    return false;
  }

  protected static final BaseAgreementInfo removeChildOrNull(Map childMap, String childCode) {
    BaseAgreementInfo o = (BaseAgreementInfo)childMap.get(childCode);
    if(o == null) {
    	o = findObjectByPrefix(childMap, childCode);
    }

    if (o != null) {
      if (o.getTransaction() == ADD) {
        childMap.remove(childCode);
      } else {
        o.setTransaction(DELETE);
      }
    }

    return o;
  }

  protected static final BaseAgreementInfo undoRemoveChildOrNull(Map childMap, String childCode) {
    BaseAgreementInfo o = (BaseAgreementInfo)childMap.get(childCode);
    if(o == null) {
    	o = findObjectByPrefix(childMap, childCode);
    }
    
    if ((o != null)&&(o.getTransaction() == DELETE)) {
      o.setTransaction(NO_CHG);
    }

    return o;
  }

  protected static final BaseAgreementInfo removeChild(Map childMap, String childCode) throws UnknownObjectException {
    BaseAgreementInfo o = removeChildOrNull(childMap, childCode);

    if (o == null) {
      throw new UnknownObjectException("Object does not exist.", childCode);
    }

    return o;
  }

  protected static final BaseAgreementInfo removeFeatureChild(Map childMap, String childCode) throws UnknownObjectException {
      BaseAgreementInfo o = removeFeatureChildOrNull(childMap, childCode);

      if (o == null) {
        throw new UnknownObjectException("Object does not exist.", childCode);
      }

      return o;
    }

  protected static final BaseAgreementInfo removeFeatureChildOrNull(Map childMap, String childCode) {
      BaseAgreementInfo o = (BaseAgreementInfo)childMap.get(childCode);
      if (o != null)
    	  o.setTransaction(DELETE);

      return o;
    }
  
  public Object clone() {
	  BaseAgreementInfo o = (BaseAgreementInfo ) super.clone();
 	  o.effectiveDate = cloneDate( effectiveDate );
	  o.expiryDate = cloneDate( expiryDate );
	  return o;
  }
  
  /**
   * Returns all elements of the input map which mapped to keys starting with 
   * the 'childCodePrefix'.
   * 
   * @param childMap
   * @param childCodePrefix
   * @return
   */
  protected static final List getChildren(Map childMap, String childCodePrefix) throws UnknownObjectException {
	  if (childMap == null || childCodePrefix == null) {
	      return null;
	  }
	  
	  List result = new ArrayList();
	  
	  // try to find a FIRST appearance of the key as a prefix
	  for(Iterator keyIter = childMap.keySet().iterator(); keyIter.hasNext();) {
		  String key = (String)keyIter.next();
		  if(key.startsWith(childCodePrefix)) {
			  result.add((BaseAgreementInfo)childMap.get(key));
		  }
	  }
	  if(result.size() == 0) {
		  throw new UnknownObjectException("No objects found for key prefix: ", childCodePrefix);
	  }
	  return result;
  }
  
  /**
   * Finds the first appearance of the prefix in a key portion.
   * Returns null if not found.
   * @param childMap
   * @param codePrefix
   * @return
   */
  private static BaseAgreementInfo findObjectByPrefix(Map childMap, String codePrefix) {
	  if (childMap == null || codePrefix == null) {
	      return null;
	  }
	  // try to find a FIRST appearance of the key as a prefix
  	  for(Iterator keyIter = childMap.keySet().iterator(); keyIter.hasNext();) {
  		  String key = (String)keyIter.next();
  		  if(key.startsWith(codePrefix)) {
  			  return (BaseAgreementInfo)childMap.get(key);
  		  }
  	  }
  	  return null;
  }
  
}

