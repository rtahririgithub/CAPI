package com.telus.eas.subscriber.info;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import com.telus.api.account.CallingCircleCommitmentAttributeData;
import com.telus.api.account.CallingCircleParameters;
import com.telus.api.account.ContractFeature;
import com.telus.api.account.DurationServiceCommitmentAttributeData;
import com.telus.api.account.VoiceToTextOptions;
import com.telus.api.reference.Feature;
import com.telus.api.reference.RatedFeature;
import com.telus.eas.utility.info.FeatureInfo;
import com.telus.eas.utility.info.RatedFeatureInfo;


/**
 * Title:        Telus - Amdocs Domain Beans
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      Telus Mobility
 * @author Michael Lapish
 * @version 1.0
 */

public class ServiceFeatureInfo extends BaseAgreementInfo implements ContractFeature {

  private static final String PARAM_KEY_CALLING_CIRCLE = "CALLING-CIRCLE=";
  public static final String PARAM_KEY_CALL_HOME_FREE = "CALLHOMEFREE=";

  private boolean ccParameterChanged = false;
  static final long serialVersionUID = 1L;

  public transient RatedFeatureInfo feature; //it was transient
  public String featureParam = "";
  private String additionalNumber;
  private CallingCircleParametersInfo callingCircleParamters;
  private CallingCircleCommitmentAttributeDataInfo callingCircleCommitmentAttributeData;
  
  private String categoryCode;
  private String switchCode;
  private int callingCircleSize;
  private String parameterDefault;
  private boolean wps;
  private String featureSequenceNo;
  private DurationServiceCommitmentAttributeData durationServiceCommitmentAttributeData;

  private double recurringCharge=0;
  private double additionalCharge=0;
  
  public ServiceFeatureInfo() {
  }

  public ServiceFeatureInfo(RatedFeatureInfo feature) {
    setFeature(feature);
    setTransaction(ADD);
  }

  public void setFeature(RatedFeatureInfo feature) {
    this.feature = feature;
    setFeatureCode(feature.getCode());
    this.switchCode = feature.getSwitchCode();
    this.categoryCode = feature.getCategoryCode();
    this.callingCircleSize = feature.getCallingCircleSize();
    this.parameterDefault = feature.getParameterDefault();
    this.wps= feature.isWPS();
    this.recurringCharge = feature.getRecurringCharge();
    this.additionalCharge = feature.getAdditionalCharge();
  }

  public RatedFeature getFeature() {
    return feature;
  }

  public RatedFeatureInfo getFeature0() {
    return feature;
  }

  public void setParameter(String newFeatureParam) {
    featureParam = newFeatureParam;
    setChanged();
    if (isCallingCircle() || isPrepaidCallingCircle())
    	ccParameterChanged=true;
  }

  public String getParameter() {
    return featureParam;
  }

  public String getFeatureCode() {
    return getCode();
  }

  public void setFeatureCode(String featureCode) {
    setCode(featureCode, 6);
  }

  public String getAdditionalNumber() {
    return additionalNumber;
  }

  public void setAdditionalNumber(String additionalNumber) {
    this.additionalNumber = additionalNumber;
    setChanged();
  }

  public String toString() {

    StringBuffer s = new StringBuffer(128);

    s.append("ServiceFeatureInfo:[\n");

    s.append("    super=[").append(super.toString()).append("]\n");
    s.append("    feature=[").append(feature).append("]\n");
    s.append("    featureParam=[").append(featureParam).append("]\n");
    s.append("    additionalNumber=[").append(additionalNumber).append("]\n");
    s.append("    featureSequenceNo=[").append(featureSequenceNo).append("]\n");
    s.append("]");

    return s.toString();

  }

  public String getDescription() {
    return feature.getDescription();
  }

  public String getDescriptionFrench() {
    return feature.getDescriptionFrench();
  }

  public String getServiceCode() {
    return getParentCode();
  }

  public String getParameterDefault() {
	  return parameterDefault;
  }

  public void setParameterDefault(String parameterDefault) {
	  this.parameterDefault = parameterDefault;
  }

  /*
  public String getServiceCode() {
    return (getParent() != null)?getParent().getCode():null;
  }
  */

  public void setCallingCirclParameters( CallingCircleParameters ccp ) {
	  callingCircleParamters = (CallingCircleParametersInfo)ccp;
  }
  public CallingCircleParameters getCallingCircleParameters() {
	  return callingCircleParamters;
  }
  public void setCallingCirclePhoneNumberList( String[] phoneNumbers ) {
    if ( getFeature().isCallingCircle() || getFeature().isPrepaidCallingCircle()) {
      setParameter( buildCallingCircleFeatureParameter( phoneNumbers ) ); 
      setCcParameterChanged(true);
	} else {
      throw new UnsupportedOperationException("This feature["+ getFeature().getCode()+"] is not a Calling circle feature, cannot set calling circle phone number list");
	}
  }

  
  public VoiceToTextOptions getVoiceToTextOptions() {
    VoiceToTextOptions options = new VoiceToTextOptionsInfo();
    
    Map<String, String> params = parseNameValuePairs(this.getParameter(), "@");
    
    options.setSMSORMMSDelivery(mapValToBoolean((String)params.get(VoiceToTextOptionsInfo.SMS_OR_MMS_DELIVERY_KEY)));
    options.setEmailDelivery(mapValToBoolean((String)params.get(VoiceToTextOptionsInfo.EMAIL_DELIVERY_KEY)));
    options.setEmailName((String)params.get(VoiceToTextOptionsInfo.EMAIL_NAME_KEY));
    options.setEmailDomain((String)params.get(VoiceToTextOptionsInfo.EMAIL_DOMAIN_KEY));
    options.setRollingVoiceMail(mapValToBoolean((String)params.get(VoiceToTextOptionsInfo.ROLLING_VM_KEY)));
    options.setVoiceFileAttached(mapValToBoolean((String)params.get(VoiceToTextOptionsInfo.VOICE_FILE_KEY)));
    
	return options;
  }
  
  
  public void setVoiceToTextOptions(VoiceToTextOptions vttOption) {
	  StringBuffer buffer = new StringBuffer();
	  
	  // only add email name & domain parameters if email delivery is set to true and name and domain parameters are provided
	  boolean emailDeliveryInd = vttOption.isEmailDelivery() && (vttOption.getEmailName() != null && !vttOption.getEmailName().trim().equals("")) &&
	  (vttOption.getEmailDomain() != null && !vttOption.getEmailDomain().trim().equals(""));
	  
	  if (emailDeliveryInd == false && vttOption.isSMSORMMSDelivery() == false) {
		//override the defaults and set to SMS if no delivery channel was set. CR requirement per TS for Apr 2019 release
		  vttOption.setSMSORMMSDelivery(true);
		  vttOption.setRollingVoiceMail(true); //rolling should be set to true together
	  }
	  
	  buffer.append(VoiceToTextOptionsInfo.SMS_OR_MMS_DELIVERY_KEY + "=" + mapBooleanToVal(vttOption.isSMSORMMSDelivery()) + "@");
	  
	  // only add email name & domain parameters if email delivery is set to true and name and domain parameters are provided
	  if (emailDeliveryInd) {
		  buffer.append(VoiceToTextOptionsInfo.EMAIL_DELIVERY_KEY + "=" + mapBooleanToVal(vttOption.isEmailDelivery()) + "@");
		  buffer.append(VoiceToTextOptionsInfo.EMAIL_NAME_KEY + "=" + vttOption.getEmailName() + "@");
		  buffer.append(VoiceToTextOptionsInfo.EMAIL_DOMAIN_KEY + "=" + vttOption.getEmailDomain() + "@");
	  }
    
	  buffer.append(VoiceToTextOptionsInfo.ROLLING_VM_KEY + "=" + mapBooleanToVal(vttOption.isRollingVoiceMail()) + "@");
	  buffer.append(VoiceToTextOptionsInfo.VOICE_FILE_KEY + "=" + mapBooleanToVal(vttOption.isVoiceFileAttached()) + "@");
        
	  this.setParameter(buffer.toString());
  }
  
  private String buildCallingCircleFeatureParameter(String[] phoneNumbers) {
    StringBuffer sb = new StringBuffer();
    if (isCallingCircle()) {
    	sb.append(PARAM_KEY_CALLING_CIRCLE);
    }else {
    	sb.append(PARAM_KEY_CALL_HOME_FREE);
    }
    
	if ( phoneNumbers!=null) { 
	  for( int i=0; i<phoneNumbers.length; i++ ) {
	    sb.append( phoneNumbers[i]);

	    if(isCallingCircle() || phoneNumbers.length > 1) { //CALLHOME FREE (aka Prepaid calling circle) parameters do not need ";" at the end
	    	sb.append(";");
	    }
	  }
	}
	sb.append("@");
	return sb.toString();
  }
  /*
  private void initFeatureParameter() {
	if ( isParameterEmpty() ) {
      if ( getFeature().isCallingCircle() ) {
    	  //init the empty calling circle list parameter
    	  setParameter( buildCallingCircleFeatureParameter( null ) );
      }
    }
  }*/

  private boolean isParameterEmpty() {
	return (featureParam==null) || (featureParam.trim().length()==0);
  }
  
  
  private boolean mapValToBoolean(String val){
	if ("Y".equals(val)) {
	  return true;
	}
	
	return false; //default will be false
  }
  
  private String mapBooleanToVal(boolean val){
	if (val) {
	  return "Y";
	}
	
	return "N"; //default is false, therefore return "N" if condition is not met
  }
  
  private Map<String, String> parseNameValuePairs(String pairs, String delim) {
	  Map<String, String> values = new HashMap<String, String>();
	    
	  if (pairs != null) {    	
		  String[] params = pairs.split(delim);
		  for (int i=0; i < params.length; i ++) {
			  String token = params[i];
			  if (token != null) {
				  String[] tokens = token.split("=");  //parse name value pairs  i.e. ABC=Y will be split as tokens[0]=ABC, tokens[1]=Y
				  if (tokens != null) {
					  if (tokens.length > 1) {
						  values.put(tokens[0], tokens[1]);
					  }else {
						  values.put(tokens[0], "");
					  }
				  }
			  }
		  }
	  }
	  
	  return values;
  }
  
	public int getCallingCircleSize() {
		return callingCircleSize;
	}

	public String getCategoryCode() {
		return categoryCode;
	}

	public double getRecurringCharge() {
		return recurringCharge;
	}

	public double getAdditionalCharge() {
		return additionalCharge;
	}

	public String getSwitchCode() {
		return switchCode;
	}
	
	public boolean isCallingCircle() {
		boolean result = false;
		if (categoryCode != null) {
			result = Feature.CATEGORY_CODE_CALLING_CIRCLE.equalsIgnoreCase(categoryCode.trim());
		}
		return result;
	}

	public boolean isNonAirtimeCallingCircle() {
		boolean result = false;
		if ( switchCode!=null ) {
			result = FeatureInfo.SWITCH_CODE_CALLING_CIRCLE.equalsIgnoreCase(switchCode.trim())
				&& isCallingCircle() == false;
		}
		return result;
	}
	
	public static String[] parseCallingCircleParameter(String param ) {
		List<String> result = new ArrayList<String>();
		
		if ( param!=null ) {
			int start = param.indexOf(PARAM_KEY_CALLING_CIRCLE);
			if(start != -1) {
				int keyLen = PARAM_KEY_CALLING_CIRCLE.length();
				int end = param.indexOf("@");
				if ( end>start && start>=0 && end>=keyLen) {
					String str = param.substring( keyLen, end );
					StringTokenizer st  = new StringTokenizer(str,";");
					while( st.hasMoreTokens()) {
						result.add(st.nextToken());
					}
				}
			}
			else {
				int startCHF = param.indexOf(PARAM_KEY_CALL_HOME_FREE);
				int end = param.indexOf("@");
				if (startCHF != -1) {
					result.add(param.substring(startCHF+PARAM_KEY_CALL_HOME_FREE.length(),end));
				}
			}
		}

		return (String[]) result.toArray( new String[ result.size()] );
	}
	
	public String[] getCallingCirclePhoneNumbersFromParam() {
		return parseCallingCircleParameter( featureParam );
	}

	public boolean isCcParameterChanged() {
		return ccParameterChanged;
	}

	public void setCcParameterChanged(boolean ccParameterChanged) {
		this.ccParameterChanged = ccParameterChanged;
	}
	
	public Object clone() {
		ServiceFeatureInfo o = (ServiceFeatureInfo) super.clone();
		o.callingCircleParamters = (CallingCircleParametersInfo) clone (callingCircleParamters );
		o.callingCircleCommitmentAttributeData = (CallingCircleCommitmentAttributeDataInfo) clone(callingCircleCommitmentAttributeData);

		//feature is a pure reference data, no one is supposed to modify it, no need to clone it
		//o.feature = (RatedFeatureInfo) clone (feature);
		return o;
	}

	//CDR calling-circle BR changes begin
	public void syncParameter( ServiceFeatureInfo o ) {
		this.featureParam = o.featureParam; 
	}
	
	public void setCallingCircleCommitmentAttributeData(
			CallingCircleCommitmentAttributeDataInfo callingCircleCommitmentAttributeData) {
		this.callingCircleCommitmentAttributeData = callingCircleCommitmentAttributeData;
	}

	public CallingCircleCommitmentAttributeDataInfo getCallingCircleCommitmentAttributeData0() {
		return callingCircleCommitmentAttributeData;
	}
	
	public CallingCircleCommitmentAttributeData getCallingCircleCommitmentAttributeData() {
		return getCallingCircleCommitmentAttributeData0();
	}
	public void copyCallingCircleInfo( ServiceFeatureInfo serviceFeatureInfo ) {
		if ( serviceFeatureInfo.featureParam!=null && serviceFeatureInfo.featureParam.equals( this.featureParam) ==false) {
			setParameter( serviceFeatureInfo.featureParam );
			setCcParameterChanged(true);
		}
		this.callingCircleCommitmentAttributeData = serviceFeatureInfo.callingCircleCommitmentAttributeData;
		this.callingCircleParamters = serviceFeatureInfo.callingCircleParamters;
	}

	//indicate whether this feature is Prepaid feature
	public boolean isWPS() {
		return this.wps;
	}
	public boolean isPrepaidCallingCircle() {
		return isWPS() && 
			(categoryCode != null && 
				(FeatureInfo.CATEGORY_CODE_CALLING_CIRCLE.equalsIgnoreCase(categoryCode.trim()) || 
				FeatureInfo.CATEGORY_CODE_CALL_HOME_FREE.equalsIgnoreCase(categoryCode.trim() )
				)
			);
	}
	public String getFeatureSequenceNo() {
		return featureSequenceNo;
	}

	public void setFeatureSequenceNo(String featureSequenceNo) {
		this.featureSequenceNo = featureSequenceNo;
	}
	//CDR calling-circle BR changes end

	public DurationServiceCommitmentAttributeData getDurationServiceCommitmentAttributeData() {
		return durationServiceCommitmentAttributeData;
	}

	public void setDurationServiceCommitmentAttributeData(
			DurationServiceCommitmentAttributeData durationServiceCommitmentAttributeData) {
		this.durationServiceCommitmentAttributeData = durationServiceCommitmentAttributeData;
		
	}


}
