package com.telus.api.account;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import com.telus.api.ApplicationException;
import com.telus.api.BaseTest;
import com.telus.api.ErrorCodes;
import com.telus.api.InvalidServiceChangeException;
import com.telus.api.SystemCodes;
import com.telus.api.TelusAPIException;
import com.telus.api.equipment.Equipment;
import com.telus.api.reference.MemoType;
import com.telus.api.util.ToStringBuilder;
import com.telus.eas.account.info.ActivationOptionInfo;
import com.telus.eas.account.info.ActivationOptionTypeInfo;
import com.telus.eas.account.info.AuditHeaderInfo;
import com.telus.eas.account.info.PhoneNumberSearchOptionInfo;
import com.telus.provider.account.TMAccount;
import com.telus.provider.account.TMActivationOption;
import com.telus.provider.account.TMContract;
import com.telus.provider.account.TMPostpaidBusinessRegularAccount;
import com.telus.provider.account.TMPostpaidConsumerAccount;
import com.telus.provider.account.TMSubscriber;
import com.telus.provider.util.ProviderServiceChangeExceptionTranslator;

public class SimpleAPITests extends BaseTest {

	static {
		// Setup Environment
		// setupD1();
		// setupEASECA_D3();
		// setupCHNLECA_PT168();
		 setupEASECA_CSI();
		// localhostWithPT148Ldap();
		// localhostWithPT168Ldap();
		// localhostWithPT140Ldap();
		// setupCHNLECA_QA();
		// setupP();
		// setupT();

		// System.setProperty("cmb.services.AccountLifecycleFacade.url", "t3://localhost:7001");
		// System.setProperty("cmb.services.AccountLifecycleManager.url", "t3://localhost:7001");
		// System.setProperty("cmb.services.AccountInformationHelper.url", "t3://localhost:7001");
		// System.setProperty("cmb.services.SubscriberLifecycleHelper.url", "t3://localhost:7001");
		// System.setProperty("cmb.services.SubscriberLifecycleFacade.url", "t3://localhost:7001");
		// System.setProperty("cmb.services.SubscriberLifecycleManager.url", "t3://localhost:7001");
		// System.setProperty("cmb.services.ReferenceDataHelper.url", "t3://localhost:7001");
		// System.setProperty("cmb.services.ReferenceDataFacade.url", "t3://localhost:7001");
		// System.setProperty("cmb.services.ProductEquipmentHelper.url", "t3://localhost:7001");
		// System.setProperty("cmb.services.ProductEquipmentManager.url", "t3://localhost:7001");
		// System.setProperty("cmb.services.ProductEquipmentLifecycleFacade.url", "t3://localhost:7001");
	}

	public SimpleAPITests(String name) throws Throwable {
		super(name);
	}
	
	public void test_API_instance() throws TelusAPIException {
		System.out.println("Start test_API_instance...");
		System.out.println("ClientAPI version: " + api.getVersion());
		System.out.println("End test_API_instance.");
	}
	
	public void test_retrieve_business_credit_identities() throws TelusAPIException, ApplicationException {
		
		System.out.println("start test_retrieve_business_credit_identities...");
		
		Account account = api.getAccountManager().findAccountByBAN(44134194);
		System.out.println(account.toString());
		
		if (account.isPostpaidBusinessRegular()) {
			BusinessCreditIdentity[] bciArray = ((PostpaidBusinessRegularAccount) account).getBusinessCreditIdentities();
			for (BusinessCreditIdentity bci : bciArray) {
				System.out.println(bci.toString());
			}
		} else {
			System.out.println("invalid account type/subtype");
		}
		
		System.out.println("end test_retrieve_business_credit_identities.");
	}
	
	public void test_retrieve_account_by_account_number() throws TelusAPIException {
	
		System.out.println("Start test_retrieve_account_by_account_number...");
		
		Account account = api.getAccountManager().findAccountByBAN(8); // 8 70761464 70783350 70779204 70784088 660647 70783409
		System.out.println(account.toString());
				
		System.out.println("End test_retrieve_account_by_account_number.");
	}
	
	public void test_update_credit_class() throws TelusAPIException {
		
		System.out.println("Start test_update_credit_class...");
		
		Account account = api.getAccountManager().findAccountByBAN(70720638); // 70761464 70720638 8
		System.out.println(account.toString());
		
		CreditCheckResult creditCheckResult = account.getCreditCheckResult();
		String creditClass = "D";
		try {
			creditCheckResult.updateCreditClass(account.getBanId(), creditClass, "CAPI test: changing credit class from " + creditCheckResult.getCreditClass() + " to " + creditClass + ".");
		} catch (TelusAPIException tapie) {
			if (tapie.getApplicationMessage() != null) {
				System.out.println("TelusAPIException applicationMessageInfo: ID=[" + tapie.getApplicationMessage().getId() + "].");
				System.out.println("TelusAPIException applicationMessageInfo: code=[" + tapie.getApplicationMessage().getCode() + "].");
				System.out.println("TelusAPIException applicationMessageInfo: message=[" + tapie.getApplicationMessage().getText(Locale.ENGLISH) + "].");
			}
			System.out.println(tapie.toString());
		}
		
		System.out.println("End test_update_credit_class.");
	}
	
	public void test_update_credit_class_and_limit() throws TelusAPIException {
		
		System.out.println("Start test_update_credit_class_and_limit...");
		
		TMAccount account = (TMAccount) api.getAccountManager().findAccountByBAN(70576393);
		System.out.println(account.toString());
		
		CreditCheckResult creditCheckResult = account.getCreditCheckResult();
		String creditClass = "B";
		double creditLimit = 0.00;
		try {
			creditCheckResult.updateCreditProfile(account.getBanId(), creditClass, creditLimit, "CAPI test: changing credit class from " + creditCheckResult.getCreditClass() + " to " + creditClass
					+ " and credit limit from " + creditCheckResult.getLimit() + " to " + creditLimit + ".");
		} catch (TelusAPIException tapie) {
			if (tapie.getApplicationMessage() != null) {
				System.out.println("TelusAPIException applicationMessageInfo: ID=[" + tapie.getApplicationMessage().getId() + "].");
				System.out.println("TelusAPIException applicationMessageInfo: code=[" + tapie.getApplicationMessage().getCode() + "].");
				System.out.println("TelusAPIException applicationMessageInfo: message=[" + tapie.getApplicationMessage().getText(Locale.ENGLISH) + "].");
			}
			System.out.println(tapie.toString());
		}
		
		System.out.println("End test_update_credit_class_and_limit.");
	}
	
	public void test_clp_client() throws TelusAPIException {
		
		System.out.println("Start test_clp_client...");
		
		TMAccount account = (TMAccount) api.getAccountManager().findAccountByBAN(70617358); // 1905137 70617358
		System.out.println(account.toString());
		
		TMSubscriber subscriber = (TMSubscriber) account.getSubscriberByPhoneNumber("7781761242"); // 5196350743
		System.out.println(subscriber.toString());

		ActivationOptionInfo activationOption = new ActivationOptionInfo(new ActivationOptionTypeInfo(ActivationOptionType.CREDIT_LIMIT_AND_DEPOSIT), 100, 200, "X", 24);

		try {
			subscriber.setActivationOption(activationOption, false);
			subscriber.getActivationOption().apply();			
		} catch (TelusAPIException tapie) {
			if (tapie.getApplicationMessage() != null) {
				System.out.println("TelusAPIException applicationMessageInfo: ID=[" + tapie.getApplicationMessage().getId() + "].");
				System.out.println("TelusAPIException applicationMessageInfo: code=[" + tapie.getApplicationMessage().getCode() + "].");
				System.out.println("TelusAPIException applicationMessageInfo: message=[" + tapie.getApplicationMessage().getText(Locale.ENGLISH) + "].");
			}
			System.out.println(tapie.toString());
		}

		System.out.println("End test_clp_client.");
	}

	public void test_town_clp_client() throws TelusAPIException {
		
		System.out.println("Start test_town_clp_client...");
		
		TMAccount account = (TMAccount) api.getAccountManager().findAccountByBAN(70617358); // 1905137 70617358
		System.out.println(account.toString());
		
		TMSubscriber subscriber = (TMSubscriber) account.getSubscriberByPhoneNumber("7781761242"); // 5196350743
		System.out.println(subscriber.toString());

		ActivationOptionInfo activationOption = new ActivationOptionInfo(new ActivationOptionTypeInfo(ActivationOptionType.CREDIT_LIMIT), 100, 200, "X", 24);

		try {
			subscriber.setActivationOption(activationOption, true);
			subscriber.getActivationOption().apply();
		} catch (TelusAPIException tapie) {
			if (tapie.getApplicationMessage() != null) {
				System.out.println("TelusAPIException applicationMessageInfo: ID=[" + tapie.getApplicationMessage().getId() + "].");
				System.out.println("TelusAPIException applicationMessageInfo: code=[" + tapie.getApplicationMessage().getCode() + "].");
				System.out.println("TelusAPIException applicationMessageInfo: message=[" + tapie.getApplicationMessage().getText(Locale.ENGLISH) + "].");
			}
			System.out.println(tapie.toString());
		}

		System.out.println("End test_town_clp_client.");
	}
	
	public void test_dealer_deposit() throws TelusAPIException {
		
		System.out.println("Start test_dealer_deposit...");
		
		TMAccount account = (TMAccount) api.getAccountManager().findAccountByBAN(70617358); // 1905137 70617358
		System.out.println(account.toString());
		
		TMSubscriber subscriber = (TMSubscriber) account.getSubscriberByPhoneNumber("7781761242"); // 5196350743
		System.out.println(subscriber.toString());

		ActivationOptionInfo activationOption = new ActivationOptionInfo(new ActivationOptionTypeInfo(ActivationOptionType.DEALER_DEPOSIT_CHANGE), 50, 100, account.getCreditCheckResult().getCreditClass(), 24);

		try {
			subscriber.setActivationOption(activationOption, false);
			subscriber.getActivationOption().apply();
		} catch (TelusAPIException tapie) {
			if (tapie.getApplicationMessage() != null) {
				System.out.println("TelusAPIException applicationMessageInfo: ID=[" + tapie.getApplicationMessage().getId() + "].");
				System.out.println("TelusAPIException applicationMessageInfo: code=[" + tapie.getApplicationMessage().getCode() + "].");
				System.out.println("TelusAPIException applicationMessageInfo: message=[" + tapie.getApplicationMessage().getText(Locale.ENGLISH) + "].");
			}
			System.out.println(tapie.toString());
		}

		System.out.println("End test_dealer_deposit.");
	}

	public void test_differentiated_credit() throws  ApplicationException, Throwable  {

		System.out.println("Start test_differentiated_credit...");
		
		TMAccount account = (TMAccount) api.getAccountManager().findAccountByBAN(70617358); // 1905137 70617358
		System.out.println(account.toString());
		
		TMSubscriber subscriber = (TMSubscriber) account.getSubscriberByPhoneNumber("7781761242"); // 5196350743
		System.out.println(subscriber.toString());

		ActivationOptionInfo activationOption = new ActivationOptionInfo(new ActivationOptionTypeInfo(ActivationOptionType.DIFFERENTIATED_CREDIT), 100, 200, account.getCreditCheckResult().getCreditClass(), 24);
		
		try {
			subscriber.setActivationOption(activationOption, false);
			subscriber.getActivationOption().apply();
			subscriber.getActivationOption().setBackOriginalDepositIfDifferentiated();
		} catch (TelusAPIException tapie) {
			if (tapie.getApplicationMessage() != null) {
				System.out.println("TelusAPIException applicationMessageInfo: ID=[" + tapie.getApplicationMessage().getId() + "].");
				System.out.println("TelusAPIException applicationMessageInfo: code=[" + tapie.getApplicationMessage().getCode() + "].");
				System.out.println("TelusAPIException applicationMessageInfo: message=[" + tapie.getApplicationMessage().getText(Locale.ENGLISH) + "].");
			}
			System.out.println(tapie.toString());
		}
		
		System.out.println("End test_differentiated_credit.");
	}
	
	public void test_ndp_client() throws TelusAPIException {
		
		System.out.println("Start test_ndp_client...");
		
		TMAccount account = (TMAccount) api.getAccountManager().findAccountByBAN(1905137); // 1905137 70617358 70437625 70786605 70816737
		System.out.println(account.toString());
		
		TMSubscriber subscriber = (TMSubscriber) account.getSubscriberByPhoneNumber("5196350743"); // 5196350743 7781761242 7808863968 4161534102
		System.out.println(subscriber.toString());

		ActivationOptionInfo activationOption = new ActivationOptionInfo(new ActivationOptionTypeInfo(ActivationOptionType.NDP), 0, 200, "C", 24);

		try {
			subscriber.setActivationOption(activationOption, false);
			subscriber.getActivationOption().apply();			
		} catch (TelusAPIException tapie) {
			if (tapie.getApplicationMessage() != null) {
				System.out.println("TelusAPIException applicationMessageInfo: ID=[" + tapie.getApplicationMessage().getId() + "].");
				System.out.println("TelusAPIException applicationMessageInfo: code=[" + tapie.getApplicationMessage().getCode() + "].");
				System.out.println("TelusAPIException applicationMessageInfo: message=[" + tapie.getApplicationMessage().getText(Locale.ENGLISH) + "].");
			}
			System.out.println(tapie.toString());
		}

		System.out.println("End test_ndp_client.");
	}
	
	public void test_declined() throws TelusAPIException {
		
		System.out.println("Start test_declined...");
		
		TMAccount account = (TMAccount) api.getAccountManager().findAccountByBAN(70437625); // 1905137 70617358 70437625
		System.out.println(account.toString());
		
		TMSubscriber subscriber = (TMSubscriber) account.getSubscriberByPhoneNumber("7808863968"); // 5196350743 7781761242 7808863968
		System.out.println(subscriber.toString());

		ActivationOptionInfo activationOption = new ActivationOptionInfo(new ActivationOptionTypeInfo(ActivationOptionType.DECLINED), 100, 200, "X", 24);

		try {
			subscriber.setActivationOption(activationOption, false);
			subscriber.getActivationOption().apply();			
		} catch (TelusAPIException tapie) {
			if (tapie.getApplicationMessage() != null) {
				System.out.println("TelusAPIException applicationMessageInfo: ID=[" + tapie.getApplicationMessage().getId() + "].");
				System.out.println("TelusAPIException applicationMessageInfo: code=[" + tapie.getApplicationMessage().getCode() + "].");
				System.out.println("TelusAPIException applicationMessageInfo: message=[" + tapie.getApplicationMessage().getText(Locale.ENGLISH) + "].");
			}
			System.out.println(tapie.toString());
		}

		System.out.println("End test_declined.");
	}
	
	public void test_retrieve_voice_usage_summary() throws TelusAPIException {
		
		System.out.println("Start test_retrieve_voice_usage_summary...");
		
		TMAccount account = (TMAccount) api.getAccountManager().findAccountByBAN(70779041);
		System.out.println(account.toString());
		
		TMSubscriber subscriber = (TMSubscriber) account.getSubscriber("4161302874");
		System.out.println(subscriber.toString());
		
		try {
			VoiceUsageSummary summary = subscriber.getVoiceUsageSummary();
			System.out.println(summary.toString());
		} catch (TelusAPIException tapie) {
			System.out.println(tapie.toString());
		}

		System.out.println("End test_retrieve_voice_usage_summary.");
	}
	
	public void test_retrieve_lightweight_subscriber_list() throws TelusAPIException {
		
		System.out.println("Start test_retrieve_lightweight_subscriber_list...");
		
		TMAccount account = (TMAccount) api.getAccountManager().findAccountByBAN(14237264);
		System.out.println(account.toString());
		System.out.println("Account isIDEN=[" + account.isIDEN() + "].");
		
		try {
			LightWeightSubscriber[] lwSubscribers = account.getLightWeightSubscribers(100, true);
			for (LightWeightSubscriber lwSubscriber : lwSubscribers) {
				System.out.println(lwSubscriber.toString());
			}
		} catch (TelusAPIException tapie) {
			System.out.println(tapie.toString());
		}		

		System.out.println("End test_retrieve_lightweight_subscriber_list.");
	}
	
	public void test_credit_check() throws TelusAPIException {
		
		System.out.println("Start test_credit_check...");
		
		TMPostpaidConsumerAccount account = (TMPostpaidConsumerAccount) api.getAccountManager().findAccountByBAN(70783350); // 70782678 70783350
		System.out.println(account.toString());
		
		try {            
            CreditCheckResult creditCheckResult = account.checkCredit(new AuditHeaderInfo());
            System.out.println(creditCheckResult.toString());

		} catch (TelusAPIException tapie) {
			System.out.println(tapie.toString());
		}		

		System.out.println("End test_credit_check.");
	}
	
	/* PT140 Data [Account:Type:Subtype:BAN]
	 * [ConsumerBox:I:J:70392999] [BusinessPersonal:B:P:70691788] [BCPersonal:B:G:70806016] [BusinessPersonalIden:B:3:70063918] [BusinessAnywhere:B:N:70814063]
	 * [CorpIndividual:C:I:70604688] [CorpEmployee:C:E:70061524] [PostpaidEmployee:I:E:70592150] [PostpaidIdenEmployee:I:2:70075220]
	 */ 
	public void test_manual_credit_check_request() throws TelusAPIException {
		
		System.out.println("Start test_manual_credit_check_request...");
		
		TMPostpaidConsumerAccount account = (TMPostpaidConsumerAccount) api.getAccountManager().findAccountByBAN(70782678); // 70782678 70783350 70792542 6001554 70653926 70761200
		System.out.println(account.toString());
		
		try {
			ManualCreditCheckRequest manualCreditCheckRequest = account.getManualCreditCheckRequest();
            manualCreditCheckRequest.setBusinessPhone(account.getBusinessPhone());
            manualCreditCheckRequest.setHomePhone(account.getHomePhone());
            manualCreditCheckRequest.getConsumerName().setFirstName("John");
            manualCreditCheckRequest.getConsumerName().setAdditionalLine(account.getContactName().getAdditionalLine());
            manualCreditCheckRequest.getConsumerName().setGeneration(account.getContactName().getGeneration());
            manualCreditCheckRequest.getConsumerName().setLastName("Smith");
            manualCreditCheckRequest.getConsumerName().setMiddleInitial("Q");
    		manualCreditCheckRequest.getConsumerName().setNameFormat(account.getContactName().getNameFormat());
    		manualCreditCheckRequest.getConsumerName().setTitle(account.getContactName().getTitle());
    		
    		Calendar birthdate = Calendar.getInstance();
    		birthdate.set(1978, 12, 12);
    		manualCreditCheckRequest.getPersonalCreditInformation().setBirthDate(birthdate.getTime());
    		manualCreditCheckRequest.getPersonalCreditInformation().setDriversLicense("DL2017092701");
    		Calendar dlExpiry = Calendar.getInstance();
    		dlExpiry.set(2020, 07, 12);
    		manualCreditCheckRequest.getPersonalCreditInformation().setDriversLicenseExpiry(dlExpiry.getTime());
    		manualCreditCheckRequest.getPersonalCreditInformation().setSin(account.getPersonalCreditInformation().getSin());
    		manualCreditCheckRequest.getPersonalCreditInformation().getCreditCard().copyFrom(account.getPersonalCreditInformation().getCreditCard());
            
            if (account.getAddress() != null) {
            	manualCreditCheckRequest.getAddress().copyFrom(account.getAddress());
            }
                        
            PostpaidConsumerAccount manualCreditCheckAccount = manualCreditCheckRequest.transformToPostpaidConsumerAccount();
                        
            System.out.println(manualCreditCheckAccount.toString());
            
            CreditCheckResult creditCheckResult = manualCreditCheckAccount.checkCredit(new AuditHeaderInfo());
            System.out.println(creditCheckResult.toString());

		} catch (TelusAPIException tapie) {
			System.out.println(tapie.toString());
		}		

		System.out.println("End test_manual_credit_check_request.");
	}
	
	public void test_manual_business_credit_check_request() throws TelusAPIException {
		
		System.out.println("Start test_manual_business_credit_check_request...");
		
		TMPostpaidBusinessRegularAccount account = (TMPostpaidBusinessRegularAccount) api.getAccountManager().findAccountByBAN(70792245); // 70792542 70792245
		System.out.println(account.toString());
		
		try {
			ManualCreditCheckRequest manualCreditCheckRequest = account.getManualCreditCheckRequest();
            manualCreditCheckRequest.setCompanyName(account.getLegalBusinessName());
            manualCreditCheckRequest.setIncorporationNumber("1234567-123");
            manualCreditCheckRequest.setIncorporationDate(new Date());
            manualCreditCheckRequest.setBusinessPhone(account.getBusinessPhone());
            
            if (account.getAddress() != null) {
            	manualCreditCheckRequest.getAddress().copyFrom(account.getAddress());
            }
                        
            PostpaidBusinessRegularAccount manualCreditCheckBusinessAccount = manualCreditCheckRequest.transformToBusinessRegularAccount();
            System.out.println(manualCreditCheckBusinessAccount.toString());
            
            CreditCheckResult creditCheckResult = manualCreditCheckBusinessAccount.checkCredit(manualCreditCheckBusinessAccount.getBusinessCreditIdentities()[0]);
            System.out.println(creditCheckResult.toString());

		} catch (TelusAPIException tapie) {
			System.out.println(tapie.toString());
		}		

		System.out.println("End test_manual_business_credit_check_request.");
	}
		
	public void test_retrieve_available_phone_number() throws TelusAPIException {
		
		System.out.println("Start test_retrieve_available_phone_number...");
		
		AvailablePhoneNumber phoneNumber = api.getReferenceDataManager().getAvailablePhoneNumber("2042356748", "C", "1100036021");
		System.out.println(phoneNumber.toString());
				
		System.out.println("End test_retrieve_available_phone_number.");
	}
	
	public void test_create_duplicate_account() throws TelusAPIException {
		
		System.out.println("Start test_create_duplicate_account...");
		
		TMAccount account = (TMAccount) api.getAccountManager().findAccountByBAN(70780702);
		System.out.println(account.toString());
		
		Account duplicateAccount = account.createDuplicateAccount();
		System.out.println(duplicateAccount.toString());
				
		System.out.println("End test_create_duplicate_account.");
	}
	
	public void test_find_subscribers_by_phone_number() throws TelusAPIException {
		
		System.out.println("Start test_find_subscribers_by_phone_number...");
		
		PhoneNumberSearchOptionInfo phoneNumberSearchOptionInfo = new PhoneNumberSearchOptionInfo();
		phoneNumberSearchOptionInfo.setSearchVOIP(true);
		
		List<Subscriber> subscriberList = Arrays.asList(api.getAccountManager().findSubscribersByPhoneNumber("", phoneNumberSearchOptionInfo, 100, true));
		for (Subscriber subscriber : subscriberList) {
			System.out.println(subscriber.toString());
		}
				
		System.out.println("End test_find_subscribers_by_phone_number.");
	}	
	
	public void test_get_credit_check_result() throws TelusAPIException {
	
		System.out.println("Start test_get_credit_check_result...");
		
		Account account = api.getAccountManager().findAccountByBAN(70653926); // 8 70761464 70783350 70779204 70784088 660647 70783409 70790441
		System.out.println(account.toString());
		
		CreditCheckResult creditCheckResult = account.getCreditCheckResult();
		System.out.println(creditCheckResult.toString());
		
		System.out.println("End test_get_credit_check_result.");
	}
	
	public void test_refresh_credit_check_result() throws TelusAPIException {
		
		System.out.println("Start test_refresh_credit_check_result...");
		
		Account account = api.getAccountManager().findAccountByBAN(70790441); // 8 70761464 70783350 70779204 70784088 660647 70783409 70790441
		System.out.println(account.toString());
		
		account.refreshCreditCheckResult();
		System.out.println(account.getCreditCheckResult());
		
		System.out.println("End test_refresh_credit_check_result.");
	}
	
	public void test_get_credit_check_result_bureau_file() throws TelusAPIException {
		
		System.out.println("Start test_get_credit_check_result_bureau_file...");
		
		Account account = api.getAccountManager().findAccountByBAN(70782678); // 70653926 70782678
		System.out.println(account.toString());
		
		CreditCheckResult creditCheckResult = account.getCreditCheckResult();
		System.out.println(creditCheckResult.toString());	
		System.out.println("Bureau file [\n" + creditCheckResult.getBureauFile().toString() + "\n].");
		
		System.out.println("End test_get_credit_check_result_bureau_file.");
	}
	
	public void test_wcc_breach_exception() throws TelusAPIException {
		
		System.out.println("Start test_wcc_breach_exception...");
		
		String domesticBreach = "zone=DOMESTIC,chargedAmount=49.00,purchaseAmount=20.00,lastConsentAmount=0.00,thresholdType=TEMPORARY,thresholdLimitAmount=50.00";
		String roamingBreach = "zone=ROAMING,chargedAmount=44.00,purchaseAmount=20.00,lastConsentAmount=0.00,thresholdType=TEMPORARY,thresholdLimitAmount=-1";
		String delimiter = "|";
		
		String breachMessage = domesticBreach + delimiter + roamingBreach;
		System.out.println("Breach message [" + breachMessage + "].");
		
		ApplicationException ae = new ApplicationException(SystemCodes.CMB_OCSSAMS_DAO, ErrorCodes.OCS_BAN_PPU_BREACH, breachMessage, "");
		System.out.println(ae.toString());
		
		ProviderServiceChangeExceptionTranslator translator = new ProviderServiceChangeExceptionTranslator();
		TelusAPIException tapie = translator.translateException(ae);
		if (tapie instanceof WCCAccountThresholdBreachException) {
			WCCAccountThresholdBreachException wccatbe = (WCCAccountThresholdBreachException) tapie;
			System.out.println(ToStringBuilder.toString(wccatbe.getBreachErrors()[0]));
			System.out.println(ToStringBuilder.toString(wccatbe.getBreachErrors()[1]));
		}
				
		System.out.println("End test_wcc_breach_exception.");
	}	

	public void test_migrate() throws TelusAPIException {
		
    	System.out.println("Start test_migrate...");
    	
		TMSubscriber subscriber = (TMSubscriber) api.getAccountManager().findSubscriberByPhoneNumber("4161536250");
		TMAccount account = (TMAccount) api.getAccountManager().findAccountByBAN(70815717);
		Equipment hspaEquipment = api.getEquipmentManager().getEquipment("8912239900002935387");
		
		MigrationRequest migrationRequest = subscriber.newMigrationRequest(account, hspaEquipment, "PYC300LPR");
		ActivationOptionInfo activationOption = new ActivationOptionInfo(new ActivationOptionTypeInfo(ActivationOptionType.DIFFERENTIATED_CREDIT), 100, 200, account.getCreditCheckResult().getCreditClass(), 24);
		migrationRequest.setActivationOption(activationOption);
		subscriber.testMigrate(migrationRequest, null, null, "10443317");
    	
    	System.out.println("End test_migrate.");
	}
	
	public void test_activation_option() throws TelusAPIException {
		
		System.out.println("Start test_activation_option...");
		
		TMAccount account = (TMAccount) api.getAccountManager().findAccountByBAN(70816737); // 1905137 70617358 70437625 70786605 70816737
		System.out.println(account.toString());
		
		ActivationOptionInfo activationOption = new ActivationOptionInfo(new ActivationOptionTypeInfo(ActivationOptionType.NDP), 0, 200, "C", 24);
		TMActivationOption tmActivationOption = new TMActivationOption(provider, activationOption, account, null, false);

		try {
			tmActivationOption.apply();			
		} catch (TelusAPIException tapie) {
			if (tapie.getApplicationMessage() != null) {
				System.out.println("TelusAPIException applicationMessageInfo: ID=[" + tapie.getApplicationMessage().getId() + "].");
				System.out.println("TelusAPIException applicationMessageInfo: code=[" + tapie.getApplicationMessage().getCode() + "].");
				System.out.println("TelusAPIException applicationMessageInfo: message=[" + tapie.getApplicationMessage().getText(Locale.ENGLISH) + "].");
			}
			System.out.println(tapie.toString());
		}

		System.out.println("End test_activation_option.");
	}
	
	public void test_business_connect_licenses() throws TelusAPIException {
		
		System.out.println("Start test_business_connect_licenses...");
		
		TMSubscriber subscriber = (TMSubscriber) api.getAccountManager().findSubscriberByPhoneNumber("5871257453");
		TMContract contract = (TMContract) subscriber.getContract();
		try {
			contract.removeService("SBCXLR35");
			contract.removeService("SBCLRC35");
			contract.save();
		} catch (TelusAPIException tapie) {
			if (tapie instanceof InvalidServiceChangeException) {
				InvalidServiceChangeException isce = (InvalidServiceChangeException) tapie;
				System.out.println(isce.toString());
			} else {
				System.out.println(tapie.toString());
			}
		}
				
		System.out.println("End test_business_connect_licenses.");
	}
	
	public void test_get_memo_types() throws TelusAPIException {
		
		System.out.println("Start test_get_memo_types...");
		
		try {
			MemoType[] memoTypes = api.getReferenceDataManager().getAllMemoTypes();
			for (MemoType memoType : memoTypes) {
				System.out.println(ToStringBuilder.toString(memoType));
			}
		} catch (TelusAPIException tapie) {
			System.out.println(tapie.toString());
		}
				
		System.out.println("End test_get_memo_types.");
	}	
	
}