package com.telus.perf.report;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Random;

import javax.naming.Context;
import javax.naming.InitialContext;

import com.telus.api.account.ServiceSubscriberCount;
import com.telus.api.reference.ServiceSummary;
import com.telus.api.util.JNDINames;
import com.telus.cmb.account.informationhelper.svc.AccountInformationHelper;
import com.telus.cmb.reference.svc.ReferenceDataHelper;
import com.telus.cmb.subscriber.lifecyclehelper.svc.SubscriberLifecycleHelper;
//import com.telus.cmb.account.lifecyclefacade.svc.AccountLifecycleFacade;
//import com.telus.cmb.account.lifecyclemanager.svc.AccountLifecycleManager;
import com.telus.eas.account.info.AccountInfo;
import com.telus.eas.account.info.BillParametersInfo;
import com.telus.eas.account.info.PhoneNumberSearchOptionInfo;
import com.telus.eas.account.info.SubscribersByDataSharingGroupResultInfo;
import com.telus.eas.subscriber.info.SubscriberInfo;

/**
 * Domain:   ST101BCustomerManagementOSRSvc
 * Console:   https://ln99239.corp.ads:43020/console

Cluster:     CustomerInformationManagement2
Servers:     ln99244:43022, ln99245:43022, ln99582:43022, ln99781:43022

Cluster:     CustomerInformationManagement3
Servers:     ln99246:43023, ln99247:43023

Cluster:     CustomerInformationManagementBatch
Servers:     ln99244:43024, ln99245:43024
 *
 */
public class EJBLoadTest {
	// ACCOUNT EJB
	private static AccountInformationHelper  accountInformationHelper;
	private static  SubscriberLifecycleHelper subscriberLifecycleHelper;
	private static ReferenceDataHelper refDataHelper;
	//private AccountLifecycleManager accountLifecycleManager;
	//private AccountLifecycleFacade accountLifecycleFacade;
	private static final int loops = 5000;
	private static final String PROJECT_FOLDER = System.getProperty("user.dir");
	private static final String PROVIDER_URL_ST101B_CIM2 = "t3://ln99244:43022";
	private static final String PROVIDER_URL_ST101B_CIM3 = "t3://ln99246:43023";
	private static final String PROVIDER_URL_ST101B_GU = "t3://ln99240:43152";
	private static final String PROVIDER_URL_ST101B_GU2 = "t3://ln99241:43153";
	
	private static final String PROVIDER_URL_ST101A_CIM2 = "t3://ln98937:42022";
	private static final String LOCALHOST = "t3://localhost:7001";




	
	
	public void testretrieveAccountsByPostalCode() throws Exception {
		FileInputStream fstream = new FileInputStream("C:/TFS/CMB/main_dev/cmb-test/src/main/java/com/telus/perf/report/retrieveAccountsByPostalCode_data.txt");
		DataInputStream din = new DataInputStream(fstream);
		BufferedReader in = new BufferedReader(new InputStreamReader(din));
		String line = null;
		final List<String> testData = new ArrayList<String>();
		//Map<String, String> dataMap = new HashMap<String, String>();
		while ((line = in.readLine()) != null) {
			if (line.trim().isEmpty() == false) {
				testData.add(line);

			}
		}
		in.close();
		din.close();

		for (int i = 0; i < loops; i++) {
			final int threadIndex = i;
			Thread t = new Thread() {
				public void run() {
					Random r = new Random();
					int randomIndex = r.nextInt(testData.size());

					String[] tokens = testData.get(randomIndex).split(",");

					String ban = tokens[0];
					String[] lastnameAndFirstName = tokens[1].split("#");
					String postalCode = tokens[2];

					// String combinedKey =
					// ppCode+","+equipmentType+","+networkType+","+provinceCode+","+term;
					// System.out.println(threadIndex+":"+randomIndex+":"+combinedKey);
					try {
						@SuppressWarnings("unchecked")
						List<AccountInfo> accounts = accountInformationHelper.retrieveAccountsByPostalCode(lastnameAndFirstName[0], postalCode,100);
						for (AccountInfo account : accounts) {
							System.out.println("current threadIndex " +threadIndex +"ban number ... " + account.getBanId());
							if(ban.equals(account.getBanId())){
								System.out.println("####### something went wrong #########");
							}
						}
						
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}

			};
                  t.start();
           }
           
        //   Thread.sleep(100L * 1000L);
    }

	
	public void testretrievePCSNetworkCountByBan() throws Exception {
		FileInputStream fstream = new FileInputStream("C:/TFS/CMB/main_dev/cmb-test/src/main/java/com/telus/perf/report/retrieveAccountsByPostalCode_data.txt");
		DataInputStream din = new DataInputStream(fstream);
		BufferedReader in = new BufferedReader(new InputStreamReader(din));
		String line = null;
		final List<String> testData = new ArrayList<String>();
		//Map<String, String> dataMap = new HashMap<String, String>();
		while ((line = in.readLine()) != null) {
			if (line.trim().isEmpty() == false) {
				testData.add(line);

			}
		}
		in.close();
		din.close();

		for (int i = 0; i < loops; i++) {
			final int threadIndex = i;
			Thread t = new Thread() {
				public void run() {
					Random r = new Random();
					int randomIndex = r.nextInt(testData.size());
					String[] tokens = testData.get(randomIndex).split(",");
					int ban = Integer.parseInt(tokens[0]);
					try {
						@SuppressWarnings("unchecked")

						HashMap<String,Integer> networkCountbyBan = accountInformationHelper.retrievePCSNetworkCountByBan(ban);
						System.out.println("current threadIndex " + threadIndex+ "networkCountbyBan key set ... " + networkCountbyBan.keySet());
	
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}

			};
                  t.start();
           }
           
        //   Thread.sleep(100L * 1000L);
    }
	
	
	
	public void testretrieveServiceSubscriberCounts() throws Exception {
		FileInputStream fstream = new FileInputStream("C:/TFS/CMB/main_dev/cmb-test/src/main/java/com/telus/perf/report/retrieveServiceSubscriberCounts_data.txt");
		DataInputStream din = new DataInputStream(fstream);
		BufferedReader in = new BufferedReader(new InputStreamReader(din));
		String line = null;
		final List<String> testData = new ArrayList<String>();
		//Map<String, String> dataMap = new HashMap<String, String>();
		while ((line = in.readLine()) != null) {
			if (line.trim().isEmpty() == false) {
				testData.add(line);

			}
		}
		in.close();
		din.close();

		for (int i = 0; i < loops; i++) {
			final int threadIndex = i;
			Thread t = new Thread() {
				public void run() {
					Random r = new Random();
					int randomIndex = r.nextInt(testData.size());
					String[] tokens = testData.get(randomIndex).split(",");
					int ban = Integer.parseInt(tokens[0]);
					String [] serviceCodes = new String[]{ tokens[1],tokens[2],tokens[3]};
					try {
						@SuppressWarnings("unchecked")
						List<ServiceSubscriberCount> subscroberCounts = accountInformationHelper.retrieveServiceSubscriberCounts(ban,serviceCodes,true);
						for (ServiceSubscriberCount subscroberCount : subscroberCounts) {
							System.out.println("Active subscriber Found "+ subscroberCount.getActiveSubscribers().length);
						}
						System.out.println("current threadIndex " + threadIndex+ "subscroberCounts length ... " + subscroberCounts.size());
	
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}

			};
                  t.start();
           }
           
        //   Thread.sleep(100L * 1000L);
    }
	
	
	public void testisFeatureCategoryExistOnSubscribers() throws Exception {
		FileInputStream fstream = new FileInputStream("C:/TFS/CMB/main_dev/cmb-test/src/main/java/com/telus/perf/report/isFeatureCategoryExistOnSubscribers_data.txt");
		DataInputStream din = new DataInputStream(fstream);
		BufferedReader in = new BufferedReader(new InputStreamReader(din));
		String line = null;
		final List<String> testData = new ArrayList<String>();
		//Map<String, String> dataMap = new HashMap<String, String>();
		while ((line = in.readLine()) != null) {
			if (line.trim().isEmpty() == false) {
				testData.add(line);

			}
		}
		in.close();
		din.close();

		for (int i = 0; i < loops; i++) {
			final int threadIndex = i;
			Thread t = new Thread() {
				public void run() {
					Random r = new Random();
					int randomIndex = r.nextInt(testData.size());

					String[] tokens = testData.get(randomIndex).split(",");

					int ban = Integer.parseInt(tokens[0]);
					String pCategoryCode = tokens[1];

					try {
						boolean isExists = accountInformationHelper.isFeatureCategoryExistOnSubscribers(ban,pCategoryCode);
						System.out.println("current threadIndex " + threadIndex+ "isExists ... " + isExists);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}

			};
                  t.start();
           }
           
       //    Thread.sleep(100L * 1000L);
    }
	
	
	public void testretrieveAccountByImsi() throws Exception {
		FileInputStream fstream = new FileInputStream("C:/TFS/CMB/main_dev/cmb-test/src/main/java/com/telus/perf/report/retrieveAccountByImsi_data.txt");
		DataInputStream din = new DataInputStream(fstream);
		BufferedReader in = new BufferedReader(new InputStreamReader(din));
		String line = null;
		final List<String> testData = new ArrayList<String>();
		//Map<String, String> dataMap = new HashMap<String, String>();
		while ((line = in.readLine()) != null) {
			if (line.trim().isEmpty() == false) {
				testData.add(line);

			}
		}
		in.close();
		din.close();

		for (int i = 0; i < loops; i++) {
			final int threadIndex = i;
			Thread t = new Thread() {
				public void run() {
					Random r = new Random();
					int randomIndex = r.nextInt(testData.size());

					String[] tokens = testData.get(randomIndex).split(",");

					String imsi = tokens[1];
					try {
						AccountInfo account = accountInformationHelper.retrieveAccountByImsi(imsi);
						if(account!=null){
							System.out.println("current threadIndex " + threadIndex+ "account.ban  found... " + account.getBanId());
						} else{
							System.out.println("current threadIndex " + threadIndex+ "account.ban not fount for IMSI ... " + imsi);

						}
						
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}

			};
                  t.start();
           }
           
       //    Thread.sleep(100L * 1000L);
    }
	
	
	public void testretrieveCorporateName() throws Exception {
		FileInputStream fstream = new FileInputStream("C:/TFS/CMB/main_dev/cmb-test/src/main/java/com/telus/perf/report/retrieveCorporateName_data.txt");
		DataInputStream din = new DataInputStream(fstream);
		BufferedReader in = new BufferedReader(new InputStreamReader(din));
		String line = null;
		final List<String> testData = new ArrayList<String>();
		//Map<String, String> dataMap = new HashMap<String, String>();
		while ((line = in.readLine()) != null) {
			if (line.trim().isEmpty() == false) {
				testData.add(line);

			}
		}
		in.close();
		din.close();

		for (int i = 0; i < loops; i++) {
			final int threadIndex = i;
			Thread t = new Thread() {
				public void run() {
					Random r = new Random();
					int randomIndex = r.nextInt(testData.size());

					String[] tokens = testData.get(randomIndex).split(",");

					int corporateId = Integer.parseInt(tokens[0]);
					try {
						String corporateName = accountInformationHelper.retrieveCorporateName(corporateId);
						if(corporateName!=null){
							System.out.println("current threadIndex " + threadIndex+ "corporateName is ... " + corporateName);
						} else{
							System.out.println("current threadIndex " + threadIndex+ "corporateName is not found for id ... " + corporateId);
						}
						
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}

			};
                  t.start();
           }
           
       //    Thread.sleep(100L * 1000L);
    }
	
	
	public void retrieveSubscriberIdsByServiceFamily() throws Exception {
		FileInputStream fstream = new FileInputStream("C:/TFS/CMB/main_dev/cmb-test/src/main/java/com/telus/perf/report/retrieveAccountsByPostalCode_data.txt");
		DataInputStream din = new DataInputStream(fstream);
		BufferedReader in = new BufferedReader(new InputStreamReader(din));
		String line = null;
		final String[] soc_family_types =  new String[]{ServiceSummary.FAMILY_TYPE_CODE_BUSINESS_ANYWHERE,ServiceSummary.FAMILY_TYPE_CODE_MANDATORY_ADDON,ServiceSummary.FAMILY_TYPE_CODE_PPS_ADDON,
				ServiceSummary.FAMILY_TYPE_CODE_DATA_DOLLAR_POOLING,ServiceSummary.FAMILY_TYPE_CODE_FLEX_PLAN};
		final List<String> testData = new ArrayList<String>();
		while ((line = in.readLine()) != null) {
			if (line.trim().isEmpty() == false) {
				testData.add(line);

			}
		}
		in.close();
		din.close();

		for (int i = 0; i < loops; i++) {
			final int threadIndex = i;
			Thread t = new Thread() {
				public void run() {
					int randomIndex = new Random().nextInt(testData.size());
					String[] tokens = testData.get(randomIndex).split(",");
					int bandId = Integer.parseInt(tokens[0]);					
					String familyTypeCode = soc_family_types[new Random().nextInt(soc_family_types.length)];
					try {
						String[] subscriberIdArray = accountInformationHelper.retrieveSubscriberIdsByServiceFamily(bandId,familyTypeCode,new Date());
						System.out.println("current threadIndex " + threadIndex+ "subscriberIdArray length is ... " + subscriberIdArray.length + " for ban "+bandId  );
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}

			};
                  t.start();
           }
           
       //    Thread.sleep(100L * 1000L);
    }
	
	
	public void testretrieveSubscribersByDataSharingGroupCodes() throws Exception {
		FileInputStream fstream = new FileInputStream("C:/TFS/CMB/main_dev/cmb-test/src/main/java/com/telus/perf/report/retrieveAccountsByPostalCode_data.txt");
		DataInputStream din = new DataInputStream(fstream);
		BufferedReader in = new BufferedReader(new InputStreamReader(din));
		String line = null;
		final String[] dataSharingGroupCodes =  new String[]{"CAD_DATA","US_DATA","CAD_CORPDATA","CAD_SHARE","CAD_TXT"};
		final List<String> testData = new ArrayList<String>();
		while ((line = in.readLine()) != null) {
			if (line.trim().isEmpty() == false) {
				testData.add(line);

			}
		}
		in.close();
		din.close();

		for (int i = 0; i < loops; i++) {
			final int threadIndex = i;
			Thread t = new Thread() {
				public void run() {
					int randomIndex = new Random().nextInt(testData.size());
					String[] tokens = testData.get(randomIndex).split(",");
					int bandId = Integer.parseInt(tokens[0]);					
					try {
						SubscribersByDataSharingGroupResultInfo[] SubscribersByDataSharingGroupResultInfoArray = accountInformationHelper.retrieveSubscribersByDataSharingGroupCodes(bandId, dataSharingGroupCodes, new Date());
						for (SubscribersByDataSharingGroupResultInfo data : SubscribersByDataSharingGroupResultInfoArray) {
							System.out.println(data.getDataSharingGroupCode());
							System.out.println(bandId);

						}
					   System.out.println("current threadIndex " + threadIndex+ "\t\tSubscribersByDataSharingGroupResultInfoArray length is ... " + SubscribersByDataSharingGroupResultInfoArray.length + ",	 for ban "+bandId  );

					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}

			};
                  t.start();
           }
           
       //    Thread.sleep(100L * 1000L);
    }
	
	
	public void testretrieveAccountByPhoneNumber() throws Exception {
		FileInputStream fstream = new FileInputStream("C:/TFS/CMB/main_dev/cmb-test/src/main/java/com/telus/perf/report/retrieveAccountByPhoneNumber_data.txt");
		DataInputStream din = new DataInputStream(fstream);
		BufferedReader in = new BufferedReader(new InputStreamReader(din));
		String line = null;
		final List<String> testData = new ArrayList<String>();
		while ((line = in.readLine()) != null) {
			if (line.trim().isEmpty() == false) {
				testData.add(line);

			}
		}
		in.close();
		din.close();

		for (int i = 0; i < loops; i++) {
			final int threadIndex = i;
			Thread t = new Thread() {
				public void run() {
					int randomIndex = new Random().nextInt(testData.size());
					String[] tokens = testData.get(randomIndex).split(",");
					String phoneNumber = tokens[0];					
					try {
					  AccountInfo account = accountInformationHelper.retrieveAccountByPhoneNumber(phoneNumber);
						System.out.println("current threadIndex " + threadIndex+ "\taccount ban is ... \t" + account.getBanId() );
					} catch (Exception e) {
						System.out.println("current threadIndex " + threadIndex+ "\t Account is not found for phone number ... \t" + phoneNumber );
						// TODO Auto-generated catch block
					}

				}

			};
                  t.start();
           }
           
       //    Thread.sleep(100L * 1000L);
    }
	
	
	
	public void testretrieveAccountByPhoneNumber_ForSeatData() throws Exception {
		FileInputStream fstream = new FileInputStream("C:/TFS/CMB/main_dev/cmb-test/src/main/java/com/telus/perf/report/retrieveAccountBySeatNumber_data.txt");
		DataInputStream din = new DataInputStream(fstream);
		BufferedReader in = new BufferedReader(new InputStreamReader(din));
		String line = null;
		final List<String> testData = new ArrayList<String>();
		while ((line = in.readLine()) != null) {
			if (line.trim().isEmpty() == false) {
				testData.add(line);

			}
		}
		in.close();
		din.close();

		for (int i = 0; i < loops; i++) {
			final int threadIndex = i;
			Thread t = new Thread() {
				public void run() {
					int randomIndex = new Random().nextInt(testData.size());
					String[] tokens = testData.get(randomIndex).split(",");
					String seatNumber = tokens[0];					
					try {
					  AccountInfo account = accountInformationHelper.retrieveAccountByPhoneNumber(seatNumber);
						System.out.println("current threadIndex " + threadIndex+ "\taccount ban is ... \t" + account.getBanId() );
					} catch (Exception e) {
						System.out.println("current threadIndex " + threadIndex+ "\t Account is not found for seat number ... \t" + seatNumber );
						// TODO Auto-generated catch block
					}

				}

			};
                  t.start();
           }
           
       //    Thread.sleep(100L * 1000L);
    }
	
	

	public void testretrieveBillParamsInfo() throws Exception {
		FileInputStream fstream = new FileInputStream("C:/TFS/CMB/main_dev/cmb-test/src/main/java/com/telus/perf/report/retrieveAccountsByPostalCode_data.txt");
		DataInputStream din = new DataInputStream(fstream);
		BufferedReader in = new BufferedReader(new InputStreamReader(din));
		String line = null;
		final List<String> testData = new ArrayList<String>();
		while ((line = in.readLine()) != null) {
			if (line.trim().isEmpty() == false) {
				testData.add(line);

			}
		}
		in.close();
		din.close();

		for (int i = 0; i < loops; i++) {
			final int threadIndex = i;
			Thread t = new Thread() {
				public void run() {
					int randomIndex = new Random().nextInt(testData.size());
					String[] tokens = testData.get(randomIndex).split(",");
					int bandId = Integer.parseInt(tokens[0]);					
					try {
					  BillParametersInfo billInfo = accountInformationHelper.retrieveBillParamsInfo(bandId);
						System.out.println("current threadIndex " + threadIndex+ "\t BillParametersInfo ban is ... \t" + billInfo.getBillFormat());
					} catch (Exception e) {
						// TODO Auto-generated catch block
					}

				}

			};
                  t.start();
           }
           
       //    Thread.sleep(100L * 1000L);
    }
	
	
	public void testisPortOutAllowed() throws Exception {
		FileInputStream fstream = new FileInputStream("C:/TFS/CMB/main_dev/cmb-test/src/main/java/com/telus/perf/report/isPortOutAllowed_data.txt");
		DataInputStream din = new DataInputStream(fstream);
		BufferedReader in = new BufferedReader(new InputStreamReader(din));
		String line = null;
		final List<String> testData = new ArrayList<String>();
		while ((line = in.readLine()) != null) {
			if (line.trim().isEmpty() == false) {
				testData.add(line);

			}
		}
		in.close();
		din.close();
		for (int i = 0; i < loops; i++) {
			final int threadIndex = i;
			Thread t = new Thread() {
				public void run() {
					int randomIndex = new Random().nextInt(testData.size());
					String[] tokens = testData.get(randomIndex).split(",");
					String status = tokens[0];
					String activityCode = tokens[1];
					String activityReasonCode = tokens[2];

					try {
						boolean isAllowed = refDataHelper.isPortOutAllowed(status, activityCode, activityReasonCode);
						System.out.println("current threadIndex " + threadIndex+ "\t isAllowed is ... \t" + isAllowed);
					} catch (Exception e) {
						e.printStackTrace();
						// TODO Auto-generated catch block
					}

				}

			};
			t.start();
		}

		// Thread.sleep(100L * 1000L);
	}
	
	
	public void testretrieveLwAccountByBan() throws Exception {
		FileInputStream fstream = new FileInputStream("C:/TFS/CMB/main_dev/cmb-test/src/main/java/com/telus/perf/report/retrieveAccountsByPostalCode_data.txt");
		DataInputStream din = new DataInputStream(fstream);
		BufferedReader in = new BufferedReader(new InputStreamReader(din));
		String line = null;
		final List<String> testData = new ArrayList<String>();
		while ((line = in.readLine()) != null) {
			if (line.trim().isEmpty() == false) {
				testData.add(line);

			}
		}
		in.close();
		din.close();

		for (int i = 0; i < loops; i++) {
			final int threadIndex = i;
			Thread t = new Thread() {
				public void run() {
					int randomIndex = new Random().nextInt(testData.size());
					String[] tokens = testData.get(randomIndex).split(",");
					int bandId = Integer.parseInt(tokens[0]);					
					try {
					  AccountInfo account = accountInformationHelper.retrieveLwAccountByBan(bandId);
						System.out.println("current threadIndex " + threadIndex+ "\t account found for ban  ... \t" + account.getBanId());
					} catch (Exception e) {
						// TODO Auto-generated catch block
					}

				}

			};
                  t.start();
           }
           
       //    Thread.sleep(100L * 1000L);
    }
	
	
	public void testretrieveSubscriberListByBAN() throws Exception {
		FileInputStream fstream = new FileInputStream("C:/TFS/CMB/main_dev/cmb-test/src/main/java/com/telus/perf/report/retrieveSubscriberListByBAN_data.txt");
		DataInputStream din = new DataInputStream(fstream);
		BufferedReader in = new BufferedReader(new InputStreamReader(din));
		String line = null;
		final List<String> testData = new ArrayList<String>();
		while ((line = in.readLine()) != null) {
			if (line.trim().isEmpty() == false) {
				testData.add(line);

			}
		}
		in.close();
		din.close();

		for (int i = 0; i < loops; i++) {
			final int threadIndex = i;
			Thread t = new Thread() {
				public void run() {
					int randomIndex = new Random().nextInt(testData.size());
					String[] tokens = testData.get(randomIndex).split(",");
					int bandId = Integer.parseInt(tokens[0]);					
					try {
						@SuppressWarnings("unchecked")
						Collection<SubscriberInfo> subscriberList = subscriberLifecycleHelper.retrieveSubscriberListByBAN(bandId,1000,true);
						System.out.println("current threadIndex " + threadIndex+ "\t subscriberList list size  ... \t" + subscriberList.size());
					} catch (Exception e) {
					e.printStackTrace();
					}

				}

			};
                  t.start();
           }
           
       //    Thread.sleep(100L * 1000L);
    }
	
	
	public void testretrieveSubscriberByBanAndPhoneNumber() throws Exception {
		FileInputStream fstream = new FileInputStream("C:/TFS/CMB/main_dev/cmb-test/src/main/java/com/telus/perf/report/retrieveSubscriber_BanAndPhoneNumberSpikes_data.txt");
		DataInputStream din = new DataInputStream(fstream);
		BufferedReader in = new BufferedReader(new InputStreamReader(din));
		String line = null;
		final List<String> testData = new ArrayList<String>();
		while ((line = in.readLine()) != null) {
			if (line.trim().isEmpty() == false) {
				testData.add(line);

			}
		}
		in.close();
		din.close();

		for (int i = 0; i < loops; i++) {
			final int threadIndex = i;
			Thread t = new Thread() {
				public void run() {
					int randomIndex = new Random().nextInt(testData.size());
					String[] tokens = testData.get(randomIndex).split(",");
					String phoneNumber = tokens[1];	
					int bandId = Integer.parseInt(tokens[0]);		
					try {
						@SuppressWarnings("unchecked")
						SubscriberInfo subscriberInfo = subscriberLifecycleHelper.retrieveSubscriberByBanAndPhoneNumber(bandId,phoneNumber);
						System.out.println("current threadIndex " + threadIndex+ "\t subscriberInfo ban ... \t" + subscriberInfo.getBanId());
					} catch (Exception e) {
					e.printStackTrace();
					}

				}

			};
                  t.start();
           }
           
       //    Thread.sleep(100L * 1000L);
    }
	
	public void testretrieveSubscriberListBySerialNumber() throws Exception {
		FileInputStream fstream = new FileInputStream("C:/TFS/CMB/main_dev/cmb-test/src/main/java/com/telus/perf/report/retrieveSubscriberListBySerialNumber_data.txt");
		DataInputStream din = new DataInputStream(fstream);
		BufferedReader in = new BufferedReader(new InputStreamReader(din));
		String line = null;
		final List<String> testData = new ArrayList<String>();
		while ((line = in.readLine()) != null) {
			if (line.trim().isEmpty() == false) {
				testData.add(line);

			}
		}
		in.close();
		din.close();

		for (int i = 0; i < loops; i++) {
			final int threadIndex = i;
			Thread t = new Thread() {
				public void run() {
					int randomIndex = new Random().nextInt(testData.size());
					String[] tokens = testData.get(randomIndex).split(",");
					String usim = tokens[0];	
					try {
						@SuppressWarnings("unchecked")
						 Collection<SubscriberInfo> subscriberInfoList = subscriberLifecycleHelper.retrieveSubscriberListBySerialNumber(usim,true);
						if(subscriberInfoList.isEmpty()){
							System.out.println("current threadIndex " + threadIndex+ "\t SubscriberInfo not found for usim ... \t" + usim);

						}
						for (SubscriberInfo subscriberInfo : subscriberInfoList) {
							System.out.println(usim);
							System.out.println("current threadIndex " + threadIndex+ "\t subscriberInfo ban ... \t" + subscriberInfo.getBanId());
						}
					} catch (Exception e) {
						e.printStackTrace();
					}

				}

			};
			t.start();
		}
           
       //    Thread.sleep(100L * 1000L);
    }
	
	
	public void testretrieveSubscriberListByPhoneNumbersNew() throws Exception {
		FileInputStream fstream = new FileInputStream("C:/TFS/CMB/main_dev/cmb-test/src/main/java/com/telus/perf/report/retrieveSubscriberByBanAndPhoneNumber_data.txt.txt");
		DataInputStream din = new DataInputStream(fstream);
		BufferedReader in = new BufferedReader(new InputStreamReader(din));
		String line = null;
		final List<String> testData = new ArrayList<String>();
		while ((line = in.readLine()) != null) {
			if (line.trim().isEmpty() == false) {
				testData.add(line);

			}
		}
		in.close();
		din.close();

		for (int i = 0; i < 3000; i++) {
			final int threadIndex = i;
			Thread t = new Thread() {
				public void run() {
					int randomIndex = new Random().nextInt(testData.size());
					String[] tokens = testData.get(randomIndex).split(",");		
					try {
						List<String> phoneNumberList =  new ArrayList<String>();
						phoneNumberList.add(tokens[0]);
						Collection<SubscriberInfo> subscriberInfoList = null;//subscriberLifecycleHelper.retrieveSubscriberListByPhoneNumbersNew(phoneNumberList,true);
						if(subscriberInfoList.isEmpty()){
							System.out.println("current threadIndex " + threadIndex+ "\t SubscriberInfo not found for phone number ... \t" + tokens[0]);

						}
						for (SubscriberInfo subscriberInfo : subscriberInfoList) {
							System.out.println(tokens[0]);
							System.out.println("current threadIndex "+ threadIndex+ "\t subscriberInfo found , ban is ... \t"+ subscriberInfo.getBanId());
						}
					} catch (Exception e) {
						e.printStackTrace();
					}

				}

			};
                  t.start();
           }
       //    Thread.sleep(100L * 1000L);
    }
	
	
	public void testretrieveSubscriberListByPhoneNumber() throws Exception {
		FileInputStream fstream = new FileInputStream("C:/TFS/CMB/main_dev/cmb-test/src/main/java/com/telus/perf/report/st101a_wireless_data.txt");
		DataInputStream din = new DataInputStream(fstream);
		BufferedReader in = new BufferedReader(new InputStreamReader(din));
		String line = null;
		final List<String> testData = new ArrayList<String>();
		while ((line = in.readLine()) != null) {
			if (line.trim().isEmpty() == false) {
				testData.add(line);

			}
		}
		in.close();
		din.close();

		for (int i = 0; i < 10; i++) {
			final int threadIndex = i;
			Thread t = new Thread() {
				public void run() {
					int randomIndex = new Random().nextInt(testData.size());
					String[] tokens = testData.get(randomIndex).split(",");
					String phoneNumber = tokens[0];	
					//int bandId = Integer.parseInt(tokens[1]);		
					try {
						@SuppressWarnings("unchecked")
						Collection<SubscriberInfo> subscriberInfoList = subscriberLifecycleHelper.retrieveSubscriberListByPhoneNumber(phoneNumber,100,false);
						if(subscriberInfoList.isEmpty()){
							System.out.println("current threadIndex " + threadIndex+ "\t SubscriberInfo not found for phone number ... \t" + tokens[0]);

						}
						for (SubscriberInfo subscriberInfo : subscriberInfoList) {
							System.out.println(tokens[0]);
							System.out.println("current threadIndex "+ threadIndex+ "\t subscriberInfo found , ban is ... \t"+ subscriberInfo.getBanId());
						}
					} catch (Exception e) {
						e.printStackTrace();
					}

				}

			};
                  t.start();
           }
           
       //    Thread.sleep(100L * 1000L);
    }
	
	
	public void retrieveSubscriberByPhoneNumber_VOIP() throws Exception {
		FileInputStream fstream = new FileInputStream("C:/TFS/CMB/main_dev/cmb-test/src/main/java/com/telus/perf/report/st101a_voip_data.txt");
		DataInputStream din = new DataInputStream(fstream);
		BufferedReader in = new BufferedReader(new InputStreamReader(din));
		String line = null;
		final List<String> testData = new ArrayList<String>();
		while ((line = in.readLine()) != null) {
			if (line.trim().isEmpty() == false) {
				testData.add(line);

			}
		}
		in.close();
		din.close();

		for (int i = 0; i < 10; i++) {
			final int threadIndex = i;
			Thread t = new Thread() {
				public void run() {
					int randomIndex = new Random().nextInt(testData.size());
					String[] tokens = testData.get(randomIndex).split(",");
					String phoneNumber = tokens[0];	
					//int bandId = Integer.parseInt(tokens[1]);		
					try {
						@SuppressWarnings("unchecked")
						PhoneNumberSearchOptionInfo searchInfo=  new PhoneNumberSearchOptionInfo();
						searchInfo.setSearchVOIP(true);
						searchInfo.setSearchWirelessNumber(false);
						SubscriberInfo subscriberInfo = subscriberLifecycleHelper.retrieveSubscriberByPhoneNumber(phoneNumber,searchInfo);
						if(subscriberInfo==null){
							System.out.println("current threadIndex " + threadIndex+ "\t SubscriberInfo not found for phone number ... \t" + tokens[0]);
						}else{
							System.out.println("current threadIndex "+ threadIndex+ "\t subscriberInfo found , ban is ... \t"+ subscriberInfo.getBanId());

						}
						
					} catch (Exception e) {
						e.printStackTrace();
					}

				}

			};
                  t.start();
           }
           
       //    Thread.sleep(100L * 1000L);
    }
	
	private void loadTest() {
		try {
			for (int i = 0; i < 50; i++) {
				init(PROVIDER_URL_ST101A_CIM2);
				
				// ST101-A Inline testing.
				
				testretrieveSubscriberListByPhoneNumber();
				//retrieveSubscriberByPhoneNumber_VOIP();
				
				//SUB_EJB_METHODS
				//testretrieveSubscriberListByBAN();
				//testretrieveSubscriberByBanAndPhoneNumber();
//				testretrieveSubscriberListBySerialNumber();
//				
				//testretrieveSubscriberListByPhoneNumbers();
//				testretrieveSubscriberListByPhoneNumbersNew();
				
//				testretrieveAccountsByPostalCode(); 
//				testisFeatureCategoryExistOnSubscribers();
//				testretrievePCSNetworkCountByBan();
//				testretrieveServiceSubscriberCounts();
//				testretrieveAccountByImsi();
//				testretrieveCorporateName();
//				retrieveSubscriberIdsByServiceFamily();
//				testretrieveSubscribersByDataSharingGroupCodes();
//				testretrieveAccountByPhoneNumber();
//				testretrieveAccountByPhoneNumber_ForSeatData();
//				testretrieveBillParamsInfo();
//				testretrieveLwAccountByBan(); 
//				init(PROVIDER_URL_ST101B_GU);
				//init(PROVIDER_URL_ST101B_GU2);
				//testisPortOutAllowed();

			}

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	
	
	private void init(String providerUrl) throws Exception {
		Hashtable<String, String> env = new Hashtable<String, String>();
		env.put(Context.INITIAL_CONTEXT_FACTORY, "weblogic.jndi.WLInitialContextFactory");
		env.put(Context.SECURITY_PRINCIPAL, "weblogic");
		env.put(Context.SECURITY_CREDENTIALS, "weblogic1");
		env.put(Context.PROVIDER_URL, providerUrl);
		Context ctx = new InitialContext(env);
		System.out.println("Initial context created with URL: " + providerUrl);
		accountInformationHelper = (AccountInformationHelper) ctx.lookup(JNDINames.TELUS_CMBSERVICE_ACCOUNT_INFORMATION_HELPER);
		//refDataHelper = (ReferenceDataHelper) ctx.lookup(JNDINames.TELUS_CMBSERVICE_REFERENCE_DATA_HELPER);
		subscriberLifecycleHelper = (SubscriberLifecycleHelper) ctx.lookup(JNDINames.TELUS_CMBSERVICE_SUBSCRIBER_LIFECYCLE_HELPER);


	//	accountLifecycleFacade = (AccountLifecycleFacade) ctx.lookup(JNDINames.TELUS_CMBSERVICE_ACCOUNT_LIFECYCLE_FACADE);
		//accountLifecycleManager = (AccountLifecycleManager) ctx.lookup(JNDINames.TELUS_CMBSERVICE_ACCOUNT_LIFECYCLE_MANAGER);

		System.out.println("Lookup Subscriber EJBs successful");
	}
	
	public static void main(String[] args) {
		EJBLoadTest test = new EJBLoadTest();
		System.out.println("##############################");
		System.out.println("Environment:  ST101A");
		System.out.println("Account EJB");
		System.out.println("##############################");
		test.loadTest();
	}
	
	
	
}
