package com.telus.api.account;



/**
 * This interface contains the result of the call to the getSubscribersByDataSharingGroup
 * method.
 */
public interface SubscribersByDataSharingGroupResult{
	  
	    /**
	    * This returns the data sharing group code
	    */
	    public String getDataSharingGroupCode();
	   
	    /**
	    * An array of all the data sharing group subscribers in this same
	    * data sharing group
	    */
	    public DataSharingSubscribers[] getDataSharingSubscribers();	
		
	    /**
	    * The subscribers in the data sharing group.  Contains information on the
	    * subscriberId and whether it is contributing to the data sharing group.
	    * If isContributing is false, then the subscriber just has access.
	    */
	    public interface DataSharingSubscribers {
	        
	    	/**
	         * The subscriberId of the Subscriber
	         */
	        public String getSubscriberId();
	        
	        /**
	         * True if the subscriber is both contributing and has access to the data
	         * sharing group.  False if subscriber just has access.
	         */
	         public boolean isContributing();
	     }
}

