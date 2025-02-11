package com.telus.eas.account.info;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.telus.api.account.SubscribersByDataSharingGroupResult;
import com.telus.eas.framework.info.Info;

public class SubscribersByDataSharingGroupResultInfo extends Info implements SubscribersByDataSharingGroupResult {

	private static final long serialVersionUID = 1L;

	private String dataSharingGroupCode;
	private List dataSharingSubscribers=new ArrayList();
	
	public String getDataSharingGroupCode() {
		return dataSharingGroupCode;
	}
	public DataSharingSubscribers[] getDataSharingSubscribers() {
		return (DataSharingSubscriberInfo[]) dataSharingSubscribers.toArray( new DataSharingSubscriberInfo[ dataSharingSubscribers.size()]);
	}

	public void setDataSharingGroupCode( String code ) {
		this.dataSharingGroupCode = code;
	}

	public void setDataSharingSubscribers( DataSharingSubscribers[] dataSharingSubs) {
		this.dataSharingSubscribers.clear();
		if ( dataSharingSubs!=null && dataSharingSubs.length>0 )	{
			this.dataSharingSubscribers.addAll ( Arrays.asList( dataSharingSubs) );
		}
	}
	public void addDataSharingSubscriber( DataSharingSubscribers dataSharingSub) {
		this.dataSharingSubscribers.add( dataSharingSub );
	}
	
	public String toString() {
	    StringBuffer s = new StringBuffer();
	    s.append("SubscribersByDataSharingGroupResultInfo:{\n");
	    s.append("    dataSharingGroupCode=[").append(dataSharingGroupCode).append("]\n");
	    if (dataSharingSubscribers == null) {
			s.append("    dataSharingSubscribers=[null]\n");
		} else if (dataSharingSubscribers.size() == 0) {
			s.append("    dataSharingSubscribers={}\n");
		} else {
			for (int i = 0; i < dataSharingSubscribers.size(); i++) {
				s.append("    dataSharingSubscribers[" + i + "]=[").append(dataSharingSubscribers.get(i)).append("]\n");
			}
		}
	  
	    s.append("}");

	    return s.toString();
	}

	public static class DataSharingSubscriberInfo extends Info implements DataSharingSubscribers{
		
		private static final long serialVersionUID = 1L;
		private String subscriberId;
		private boolean contributing;
		
		public String getSubscriberId() {
			return subscriberId;
		}
		public void setSubscriberId(String subscriberId) {
			this.subscriberId = subscriberId;
		}
		public boolean isContributing() {
			return contributing;
		}
		public void setContributing(boolean contributing) {
			this.contributing = contributing;
		}
		
		public String toString() {
		    StringBuffer s = new StringBuffer();
		    s.append("DataSharingSubscribersInfo:{\n");
		    s.append("    subscriberId=[").append(subscriberId).append("]\n");
		    s.append("    contributing=[").append(contributing).append("]\n");
		    s.append("}");
		   
		    return s.toString();
		  }
	}

}
