package com.telus.provider.account.prepaid;


import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import com.telus.api.TelusAPIException;
import com.telus.api.account.CallingCircleParameters;
import com.telus.api.account.CallingCirclePhoneList;
import com.telus.api.account.Contract;
import com.telus.api.account.ContractFeature;
import com.telus.api.account.ContractService;
import com.telus.api.account.FeatureParameterHistory;
import com.telus.api.reference.RatedFeature;
import com.telus.api.reference.Service;
import com.telus.provider.account.TMContractFeature;

public class ContractTestHelper {
	

	public static boolean isCallingCircleFeature(ContractFeature contractFeature) {
		return isCallingCircleFeature( contractFeature.getFeature() );
	}

	
	public static boolean isCallingCircleFeature(RatedFeature feature) {
		return feature.isCallingCircle() || feature.isPrepaidCallingCircle();
	}
	
	static void showCallingCircleParameters(String category, ContractService[] cServices) throws TelusAPIException {
		for( int i=0; i<cServices.length; i++ ) {
			if (cServices[i].getService().hasCallingCircleFeatures() ) {
				ContractFeature[] cFeatures = cServices[i].getFeatures();
				showCallingCircleParameters( category, cFeatures );
			}
		}
	}

	static void showCallingCircleParameters(String category, ContractFeature[] cFeatures) throws TelusAPIException {
		System.out.println("====="+category+"=======");
		for ( int i=0; i<cFeatures.length; i++ ) {
			if ( isCallingCircleFeature( cFeatures[i]) ) {
				ContractFeature contractFeature = cFeatures[i];
				showCallingCircleParameters(contractFeature);
			}
		}
		
	}

	static void showCallingCircleParameters(ContractFeature contractFeature) throws TelusAPIException {
		showFeatureParameter(contractFeature);
	}
	

	static void showFeatureParameter(ContractFeature contractFeature) {
		System.out.println( "  feature[ '"+contractFeature.getServiceCode()+ "' / '" +contractFeature.getCode()+"' ]"
				+", size:" + contractFeature.getFeature().getCallingCircleSize() 
				+ "; param: " + contractFeature.getParameter());
	}
	
	static void prepopulateAndAdjustCallingCirclePhoneNumbers(Contract contract) throws TelusAPIException {
		contract.prepopulateCallingCircleList();
		ContractFeature[] cFeatures = getContractAllCallingCircleFeatures( contract );
		
		List emptyCCList = new ArrayList();
		for ( int i=0; i<cFeatures.length; i++ ) {
			if (cFeatures[i] instanceof TMContractFeature ) {
				if ( ((TMContractFeature) cFeatures[i]).getDelegate().getCallingCirclePhoneNumbersFromParam().length==0 ) {
					emptyCCList.add( cFeatures[i] );
				}
			}
		}
		if ( emptyCCList.isEmpty()==false ) {
			adjustCallingCirclePhoneNumbers ((ContractFeature[]) emptyCCList.toArray( new ContractFeature[emptyCCList.size() ])  );
		}
	}
	
	public static void adjustCallingCirclePhoneNumbers(Contract contract) throws TelusAPIException {
		adjustCallingCirclePhoneNumbers(contract, null );
	}
	public static void adjustCallingCirclePhoneNumbers(Contract contract, String[] numberList ) throws TelusAPIException {
		adjustCallingCirclePhoneNumbers ( getContractAllCallingCircleFeatures( contract ), numberList );
		
	}
	static void adjustCallingCirclePhoneNumbers(ContractFeature[] cFeatures ) throws TelusAPIException {
		adjustCallingCirclePhoneNumbers(cFeatures, null );
	}
	static void adjustCallingCirclePhoneNumbers(ContractFeature[] cFeatures, String[] numberList ) throws TelusAPIException {
		for ( int i=0; i<cFeatures.length; i++ ) {
			
			if ( isCallingCircleFeature( cFeatures[i])) {
				
				ContractFeature contractFeature = cFeatures[i];
				showFeatureParameter(contractFeature);
				
				String[] list = numberList;
				
				if (list==null) 
					list = newPhoneList(contractFeature.getFeature().getCallingCircleSize());
				
				System.out.println( "update " + getFeatureInfoStr(cFeatures[i].getServiceCode(), cFeatures[i].getFeature())
						+ "; with phone numbers: " + Arrays.asList( list ) );
				
				contractFeature.setCallingCirclePhoneNumberList( list );
				showFeatureParameter(contractFeature);
			}
		}
	}

	static void adjustCallingCirclePhoneNumbers(ContractService[] cServices) throws TelusAPIException {
		for( int i=0; i<cServices.length; i++ ) {
			if (cServices[i].getService().hasCallingCircleFeatures() ) {
				ContractFeature[] cFeatures = cServices[i].getFeatures();
				adjustCallingCirclePhoneNumbers( cFeatures);
			}
		}
	}
	
	static ContractFeature[] getContractAllCallingCircleFeatures( Contract c ) throws TelusAPIException {
		List result = new ArrayList();

		extractCallingCircleFeature(result, c.getFeatures());
		
		extractCallingCircleFeature(result, c.getIncludedServices());

		extractCallingCircleFeature(result, c.getOptionalServices());

		return (ContractFeature[]) result.toArray( new ContractFeature[result.size()] );
	}

	public static void extractCallingCircleFeature(List result, ContractService[] cs)throws TelusAPIException {
		for ( int i=0; i<cs.length; i++ ) {
			if ( cs[i].getService().hasCallingCircleFeatures() ) {
				extractCallingCircleFeature(result, cs[i].getFeatures());
			}
		}
	}

	public static void extractCallingCircleFeature(List result,	ContractFeature[] cFeatures) {
		for ( int i=0; i<cFeatures.length; i++ ) {
			if ( isCallingCircleFeature( cFeatures[i]) ) {
				result.add(  cFeatures[i] );
			}
		}
	}
	
	static void resetCallingCirclePhoneNumbers(ContractFeature[] cFeatures) throws TelusAPIException {
		for ( int i=0; i<cFeatures.length; i++ ) {
			if ( isCallingCircleFeature( cFeatures[i]) ) {
				ContractFeature contractFeature = cFeatures[i];
				showFeatureParameter(contractFeature);
				
				contractFeature.setParameter( contractFeature.getParameter() );
				showFeatureParameter(contractFeature);
			}
		}
	}

	

	static void showContractCallingCircleInfo(Contract c) throws TelusAPIException {

		showCallingCircleParameters( "includedFeatures", c.getFeatures() );
		showCallingCircleParameters( "includedService", c.getIncludedServices() );
		showCallingCircleParameters( "optionalService", c.getOptionalServices() );
	}
	

	static void showAllCallingCircleParameter(Contract c) throws TelusAPIException {
		ContractFeature[] features   = ContractTestHelper.getContractAllCallingCircleFeatures(c);
//		ObjectIntrospector oi = new ObjectIntrospector();
		if (features == null || features.length == 0) 
			System.out.println( "Feature is empty for contract.");
		for ( ContractFeature cf: features ) {
			System.out.println( "cc feature[" + cf.getServiceCode() + "/" + cf.getCode() + "]");
			CallingCircleParameters ccp = cf.getCallingCircleParameters();
			CallingCirclePhoneList ccpList = ccp.getCallingCircleCurrentPhoneNumberList();
			if (ccpList != null) {
				String[] phoneList = ccpList.getPhoneNumberList();
				if (phoneList != null && phoneList.length>0) {
					for (int i=0; i<phoneList.length; i++) {
						System.out.println( "Phone :" + phoneList[i]);
					}
				} else {
					System.out.println( "Phone List is empty for feature " + cf.getServiceCode() + "/" + cf.getCode());
				}
			}
			//TODO - show cf.getCallingCircleCommitmentAttributeData()
		}
	}
	
		
	
	static int x = 1;
	static String npanxx=null;
	static private String[] newPhoneList( int ammount ) {
		String[] phoneNumbers = new String[ammount];
		String phoneNumberPrefix = getNpanxx();
		for( int i=0; i<phoneNumbers.length; i++) {
			phoneNumbers[i] =  phoneNumberPrefix + formatNumber( x++ ) ;
		}
		return phoneNumbers;
	}

	private static String getNpanxx() {
		if ( npanxx==null ) {
			String n = new SimpleDateFormat("hhmmss").format(new Date() );
			if (n.startsWith("0")) {
				n = "4" + n.substring(1);
			}
			return n;
		}
		return npanxx;
	}


	static String formatNumber(int i) {
		NumberFormat nf = new DecimalFormat("0000");
		return nf.format(i);
	}

	void showMaps(Map map) {
		if (map.isEmpty()==false) {
			Iterator it = map.keySet().iterator();
			while (it.hasNext() ) {
				Object ppName = it.next();
				System.out.println( ppName );
				Map category = (Map) map.get(ppName);
				Iterator cit = category.keySet().iterator();
				while (cit.hasNext() ) {
					Object catName = cit.next();
					System.out.println("   "+ catName );;
					List list = (List) category.get( catName);
					for( int i=0; i<list.size(); i++ ) {
						System.out.println ( "      " + list.get(i) );
					}
				}
			}
		}
	}



	static String getCallingCircleSizeInfo(Service service) {
		RatedFeature[] ratedFeatures = service.getFeatures();
		for( int i=0; i< ratedFeatures.length; i++ ) {
			RatedFeature rateFeature = ratedFeatures[i]; 
			if ( isCallingCircleFeature( rateFeature )) {
				StringBuffer sb = getFeatureInfoStr(service.getCode(), rateFeature);
				
				return sb.toString();
			}
		}
		return null;
		
	}

	static StringBuffer getFeatureInfoStr(String serviceCode, RatedFeature rateFeature) {
		StringBuffer sb  = new StringBuffer( "  Calling circle feature[");
		sb.append("soc:").append( serviceCode )
			.append(", ftr:").append(rateFeature.getCode().trim())
			.append(", sw:").append(rateFeature.getSwitchCode() )
			.append(", cat: ").append(rateFeature.getCategoryCode())
			.append("], size:").append( rateFeature.getCallingCircleSize() );
		return sb;
	}

	
	public static String[] getLastCallingCircleList(String serviceCode, String featureCode, FeatureParameterHistory[] parameterHistorys) {
		
		for( int i=0; i<parameterHistorys.length; i++) {
			if ( featureCode.equals( parameterHistorys[i].getFeatureCode()) &&
					serviceCode.equals(parameterHistorys[i].getServiceCode()) &&
					parameterHistorys[i].getExpirationDate()!=null) {
				if ( parameterHistorys[i].getParameterValue()!=null ) {
					return parseCallingCircleList(parameterHistorys[i].getParameterValue() );
				}
			}
		}
		return new String[0];
	}
	
	public static String[] parseCallingCircleList(String parameterValue) {
		List result = new ArrayList();
		
		if ( parameterValue!=null ) {
				StringTokenizer st  = new StringTokenizer(parameterValue,";");
				while( st.hasMoreTokens()) {
					result.add(st.nextToken());
				}
		}
		return (String[]) result.toArray( new String[ result.size()] );
	}
	
	
}
