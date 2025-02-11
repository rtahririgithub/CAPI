package com.telus.api;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import com.telus.api.account.Contract;
import com.telus.api.account.ContractFeature;
import com.telus.api.account.ContractService;
import com.telus.api.account.DepositHistory;
import com.telus.api.account.Subscriber;
import com.telus.api.config0.Configuration;
import com.telus.api.reference.ApplicationSummary;
import com.telus.api.reference.Feature;
import com.telus.api.reference.InvoiceSuppressionLevel;
import com.telus.api.reference.NumberGroup;
import com.telus.api.reference.PricePlan;
import com.telus.api.reference.PricePlanSummary;
import com.telus.api.reference.Reference;
import com.telus.api.reference.Service;
import com.telus.api.reference.ShareablePricePlan;
import com.telus.api.task.ContractChangeTask;
import com.telus.eas.equipment.info.EquipmentInfo;
import com.telus.eas.framework.info.Info;
import com.telus.eas.subscriber.info.ServiceAgreementInfo;
import com.telus.eas.subscriber.info.ServiceFeatureInfo;
import com.telus.eas.utility.info.LineRangeInfo;
import com.telus.eas.utility.info.NumberGroupInfo;
import com.telus.eas.utility.info.NumberRangeInfo;
import com.telus.provider.TMProvider;
import com.telus.provider.account.TMContract;

import junit.framework.TestCase;

public class BaseTest extends TestCase {

	static {
		System.out.println("===========================================================================================================");
		System.out.println("===========================================================================================================");
		System.out.println("===========================================================================================================");
		System.out.println("===========================================================================================================");

		System.out.println("---------------------------------------------");
		System.setProperty("java.monitoring.agent", "false");
		print("SYSTEM PROPERTY", System.getProperties());
		System.out.println("---------------------------------------------");
	}

	protected final ClientAPI api;
	protected final TMProvider provider;

	public BaseTest(String name) throws Throwable {
		super(name);
		try {
			api = ClientAPI.getInstance("18654", "apollo", ApplicationSummary.APP_SD);
			provider = (TMProvider) api.getProvider();
			Runtime.getRuntime().addShutdownHook(new Destructor());
			System.out.println("JavaDate=[" + new Date() + "]");
			System.out.println("Application Name[" + provider.getApplication() + "]");
			System.out.println("SystemDate=[" + api.getReferenceDataManager().getSystemDate() + "]");
			System.out.println("LogicalDate=[" + api.getReferenceDataManager().getLogicalDate() + "]");
		} catch (Throwable e) {
			System.err.println(e);
			throw e;
		}
	}

	class Destructor extends Thread {
		public void run() {
			api.destroy();
		}
	}

	public static void assertContains(String message, Object[] list, Object object) {
		if (list == null && object == null) {
			return;
		}

		if (list == null) {
			System.err.println("list == null: " + message);
		}

		for (int i = 0; i < list.length; i++) {
			Object o = list[i];
			if (o == object || (o != null && o.equals(object))) {
				return;
			}
		}

		System.err.println("object not in list: " + message);
	}

	public final synchronized long getObjectSize(Object object) throws IOException {
		ByteArrayOutputStream out1 = new ByteArrayOutputStream();
		ObjectOutputStream out2 = new ObjectOutputStream(out1);
		out2.writeObject(object);
		out2.flush();
		out2.close();
		return out1.size();
	}

	public static void print(String prefix, Map map) {
		System.out.println(prefix + ":");
		Iterator i = new TreeSet(map.keySet()).iterator();
		while (i.hasNext()) {
			String name = (String) i.next();
			System.out.println("    " + prefix + ":  [" + name + "]  =  [" + map.get(name) + "]");
		}
	}

	public static void print(String prefix, Object[] o) throws Throwable {
		print(prefix, o, -1);
	}

	public static void print(String prefix, Object[] o, int maximum) throws Throwable {
		System.out.println(prefix + ":");
		for (int i = 0; i < o.length && (maximum < 0 || i < maximum); i++) {
			// System.out.println(" " + prefix + "["+i+"]=" + o[i]);
			System.out.println("    " + prefix + "[" + i + "]=\n" + toString(o[i]));
		}
	}

	public static void print(String prefix, int[] o) {
		System.out.println(prefix + ":");
		for (int i = 0; i < o.length; i++) {
			System.out.println("    " + prefix + "[" + i + "]=" + o[i]);
		}
	}

	public static void print(String prefix, PricePlanSummary[] o) throws TelusAPIException {
		System.out.println(prefix + ":-----------------------------------------------------");
		for (int i = 0; i < o.length; i++) {
			System.out.println("    " + prefix + "[" + i + "] " + "[Sharable=" + o[i].isSharable() + "] " + "[AvailableForChange=" + o[i].isAvailableForChange() + "] " + "[TermMonths="
					+ o[i].getTermMonths() + "] " + "[" + o[i].getCode() + "]=\"" + o[i].getDescription() + "\"");

		}
	}

	public static String arrayToString(Object[] array) throws Throwable {
		StringBuffer s = new StringBuffer(64);
		if (array.length > 0) {
			s.append(array[0]);
		}
		for (int i = 1; i < array.length; i++) {
			s.append(", ").append(array[i]);
		}
		return s.toString();
	}

	public static void print(String prefix, Service[] o) throws Throwable {
		print(prefix, o, false);
	}

	public static void print(String prefix, Service[] o, boolean includeFeatures) throws Throwable {
		System.out.println(prefix + ":-----------------------------------------------------");
		for (int i = 0; i < o.length; i++) {
			System.out.println("    " + prefix + "[" + i + "]=[" + o[i].getCode() + "]=\"" + o[i].getDescription() + "\";" + " hasAlternateRecurringCharge=[" + o[i].hasAlternateRecurringCharge()
					+ "];" + " Sharable=[" + o[i].isSharable() + "];" + "  EquipmentTypes={" + arrayToString(o[i].getEquipmentTypes()) + "}" + "  Provinces={" + arrayToString(o[i].getProvinces())
					+ "}");
			if (includeFeatures) {
				Feature[] f = o[i].getFeatures();
				for (int j = 0; j < f.length; j++) {
					System.out.println("        feature[" + j + "]=[" + f[j].getCode() + "];" + " SwitchCode=[" + f[j].getSwitchCode() + "];" + " CategoryCode=[" + f[j].getCategoryCode() + "];"
							+ " AdditionalNumberRequired=[" + f[j].isAdditionalNumberRequired() + "];" + " ParameterRequired=[" + f[j].isParameterRequired() + "];" + " \"" + f[j].getDescription()
							+ "\"");
				}
			}
		}
	}

	public static void print(Object o) throws Throwable {
		System.out.println("---------------------------------------------------------------");
		System.out.println(toString(o));
		System.out.println("---------------------------------------------------------------");
	}

	public static void print(String prefix, Reference[] o) throws Throwable {
		System.out.println(prefix + ":-----------------------------------------------------");
		for (int i = 0; i < o.length; i++) {
			System.out.println("    " + prefix + "[" + i + "]=[" + o[i].getCode() + "]=\"" + o[i].getDescription() + "\" - \"" + o[i].getDescriptionFrench() + "\"");
		}
	}

	public static void print(String prefix, InvoiceSuppressionLevel[] o) throws Throwable {
		System.out.println(prefix + ":-----------------------------------------------------");
		for (int i = 0; i < o.length; i++) {
			System.out.println("    " + prefix + "[" + i + "]=[" + o[i].getCode() + "] AvailableForUpdate=[" + o[i].isAvailableForUpdate() + "]\"" + o[i].getDescription() + "\" - \""
					+ o[i].getDescriptionFrench() + "\"");
		}
	}

	public static void print(String prefix, ContractFeature[] o) throws Throwable {
		System.out.println(prefix + ":");
		for (int i = 0; i < o.length; i++) {
			System.out.println("    " + prefix + "[" + i + "]=[" + o[i].getFeature().getCode() + "]=\"" + o[i].getFeature().getDescription() + "\"");
		}
	}

	public static void print(String prefix, ContractService[] o) throws Throwable {
		System.out.println(prefix + ":");
		for (int i = 0; i < o.length; i++) {
			System.out.println("    " + prefix + "[" + i + "]=[" + o[i].getService().getCode() + "]=\"" + o[i].getService().getDescription() + "\"");
		}
	}

	public static void print(String prefix, Feature[] o) throws Throwable {
		System.out.println(prefix + ":");
		for (int i = 0; i < o.length; i++) {
			System.out.println("    " + prefix + "[" + i + "]=[" + o[i].getCode() + "];" + " SwitchCode=[" + o[i].getSwitchCode() + "];" + " CategoryCode=[" + o[i].getCategoryCode() + "];"
					+ " AdditionalNumberRequired=[" + o[i].isAdditionalNumberRequired() + "];" + " ParameterRequired=[" + o[i].isParameterRequired() + "];" + " \"" + o[i].getDescription() + "\"");
		}
	}

	public static void printFull(Contract contract) throws Throwable {
		String prefix = "contract.getServices()";
		ContractService[] o = contract.getServices();
		System.out.println(prefix + ":");
		for (int i = 0; i < o.length; i++) {
			System.out.println("    " + prefix + "[" + i + "]=[" + o[i]);
		}
	}

	public static void printFull(String prefix, Reference[] o) throws Throwable {
		System.out.println(prefix + ":");
		for (int i = 0; i < o.length; i++) {
			System.out.println("    " + prefix + "[" + i + "]=[" + o[i].getCode() + "]=\"" + o[i].getDescription() + "\"");
			System.out.println(o[i]);
		}
	}

	public static void print(String prefix, Configuration[] o) throws Throwable {
		System.out.println(prefix + ":");
		for (int i = 0; i < o.length; i++) {
			System.out.println("    " + prefix + "[" + i + "]=[" + o[i].getId() + "]=\"" + o[i].getName() + "\"");
		}
	}

	public static String arrayToString(String[] array, String separator) throws Throwable {
		StringBuffer s = new StringBuffer(array.length * 32);
		if (array.length > 0) {
			s.append(array[0]);
		}

		for (int i = 1; i < array.length; i++) {
			s.append(separator).append(array[i]);
		}

		return s.toString();
	}

	public static void print(String prefix, ContractChangeTask[] o) throws Throwable {
		// System.out.println(prefix +
		// ":-----------------------------------------------------");
		for (int i = 0; i < o.length; i++) {
			System.out
					.println("    ContractChangeTask[" + i + "] " + "[PhoneNumber=" + o[i].getSubscriber().getPhoneNumber() + "] " + "[PricePlan=" + o[i].getContract().getPricePlan().getCode() + "]");
			print("    " + prefix + "ServicesToAdd", o[i].getServicesToAdd());
			print("    " + prefix + "ServicesToDelete", o[i].getServicesToDelete());
			if (o[i].getException() != null) {
				System.out.println(o[i].getException());
			}
		}
	}

	public static void print(Configuration o) throws Throwable {
		System.out.println("Configuration: [" + o.getId() + "]=\"" + o.getName() + "\"");
		System.out.println("  path=[" + arrayToString(o.getPath(), "][") + "]");
		String[] names = o.getPropertyNames();
		for (int i = 0; i < names.length; i++) {
			System.out.println("    [" + names[i] + "]=[" + o.getPropertyAsString(names[i]) + "]");
		}
	}

	public static void printContract(String message, Subscriber subscriber) throws Throwable {
		System.out.println("=======================================================================================");
		System.out.println(subscriber);
		print("Subscriber " + subscriber.getPhoneNumber() + " - " + message, subscriber.getContract());
	}

	public static void print(Contract contract) throws Throwable {
		print("", contract);
	}

	public static void print(String message, Contract contract) throws Throwable {
		TMContract contract0 = (TMContract) contract;

		System.out.println("=======================================================================================");
		System.out.println("====[" + message + "] (" + contract0.getSubscriber().getPhoneNumber() + ")" + Info.repeat("=", 68 - (message == null ? 0 : message.length())));
		System.out.println("=======================================================================================");
		if (contract0.getPricePlan().isSharable()) {
			System.out.println("    Contract.PricePlan=[" + contract0.getPricePlan().getCode() + "];\n" + "    isSuppressPricePlanRecurringCharge=[" + contract0.isSuppressPricePlanRecurringCharge()
					+ "];\n" + "    RecurringCharge=[" + contract0.getRecurringCharge() + "];\n" + "    CascadeShareableServiceChanges=[" + contract0.getCascadeShareableServiceChanges() + "];\n"
					+ "    isSharable=[" + contract0.getPricePlan().isSharable() + "];\n" + "    isShareablePricePlanPrimary=[" + contract.isShareablePricePlanPrimary() + "];\n"
					+ "    isShareablePricePlanSecondary=[" + contract.isShareablePricePlanSecondary() + "];\n" + "    EffectiveDate=[" + contract0.getEffectiveDate() + "];\n" + "    ExpiryDate=["
					+ contract0.getExpiryDate() + "];\n" + "    CommitmentStartDate=[" + contract0.getCommitmentStartDate() + "];\n" + "    CommitmentEndDate=[" + contract0.getCommitmentEndDate()
					+ "];\n" + "    isTelephonyEnabled=[" + contract0.isTelephonyEnabled() + "];\n" + "    isDispatchEnabled=[" + contract0.isDispatchEnabled() + "];\n");

		} else {
			System.out.println("    Contract.PricePlan=[" + contract0.getPricePlan().getCode() + "];\n" + "    isSuppressPricePlanRecurringCharge=[" + contract0.isSuppressPricePlanRecurringCharge()
					+ "];\n" + "    RecurringCharge=[" + contract0.getRecurringCharge() + "];\n" + "    CascadeShareableServiceChanges=[" + contract0.getCascadeShareableServiceChanges() + "];\n"
					+ "    isSharable=[" + contract0.getPricePlan().isSharable() + "];\n" + "    EffectiveDate=[" + contract0.getEffectiveDate() + "];\n" + "    ExpiryDate=["
					+ contract0.getExpiryDate() + "];\n" + "    CommitmentStartDate=[" + contract0.getCommitmentStartDate() + "];\n" + "    CommitmentEndDate=[" + contract0.getCommitmentEndDate()
					+ "];\n" + "    isTelephonyEnabled=[" + contract0.isTelephonyEnabled() + "];\n" + "    isDispatchEnabled=[" + contract0.isDispatchEnabled() + "];\n");
		}

		System.out.println("Added Features--------------------------------------------------------");
		print(contract0, contract.getAddedFeatures());
		System.out.println("Changed Features------------------------------------------------------");
		print(contract0, contract.getChangedFeatures());
		System.out.println("Deleted Features------------------------------------------------------");
		print(contract0, contract.getDeletedFeatures());

		System.out.println("Added Services--------------------------------------------------------");
		print(contract0, contract.getAddedServices());
		System.out.println("Changed Services------------------------------------------------------");
		print(contract0, contract.getChangedServices());
		System.out.println("Deleted Services------------------------------------------------------");
		print(contract0, contract.getDeletedServices());

		System.out.println("Included Features-----------------------------------------------------");
		print(contract0, contract.getFeatures());
		System.out.println("Included Services-----------------------------------------------------");
		print(contract0, contract.getIncludedServices());
		System.out.println("Optional Services-----------------------------------------------------");
		print(contract0, contract.getOptionalServices());
		System.out.println("All Services----------------------------------------------------------");
		print(contract0, contract.getServices());
		System.out.println("All Features----------------------------------------------------------");
		print(contract0, contract.getFeatures(true));
		System.out.println("Cascading Changes (from " + contract0.getSubscriber().getPhoneNumber() + ")-----------------------------------");
		print("    ", contract.getCascadingContractChanges());
		System.out.println("======================================================================");
	}

	public static void print(PricePlan pricePlan) throws Throwable {
		print("", pricePlan);
	}

	public static void print(String message, PricePlan pricePlan) throws Throwable {
		System.out.println("====[" + message + "]======================================================================");
		System.out.println("PricePlan=[" + pricePlan.getCode() + "]  -  [" + pricePlan.getDescription() + "];  Sharable=[" + pricePlan.isSharable() + "]");

		if (pricePlan.isSharable()) {
			ShareablePricePlan s = (ShareablePricePlan) pricePlan;

			System.out.println("    ShareablePricePlan.MaximumSubscriberCount=[" + s.getMaximumSubscriberCount() + "]");
			System.out.println("    ShareablePricePlan.SecondarySubscriberService=[" + s.getSecondarySubscriberService() + "]");
		}

		System.out.println("    Sharable=[" + pricePlan.isSharable() + "]");
		System.out.println("    SuspensionPricePlan=[" + pricePlan.isSuspensionPricePlan() + "]");
		System.out.println("    AvailableForActivation=[" + pricePlan.isAvailableForActivation() + "]");
		System.out.println("    AvailableForChange=[" + pricePlan.isAvailableForChange() + "]");
		System.out.println("    AvailableForChangeByDealer=[" + pricePlan.isAvailableForChangeByDealer() + "]");
		System.out.println("    AvailableForChangeByClient=[" + pricePlan.isAvailableForChangeByClient() + "]");
		System.out.println("    AvailableToModifyByDealer=[" + pricePlan.isAvailableToModifyByDealer() + "]");
		System.out.println("    AvailableToModifyByClient=[" + pricePlan.isAvailableToModifyByClient() + "]");
		System.out.println("    AvailableForNonCorporateRenewal=[" + pricePlan.isAvailableForNonCorporateRenewal() + "]");
		System.out.println("    AvailableForCorporateRenewal=[" + pricePlan.isAvailableForCorporateRenewal() + "]");
		System.out.println("    AvailableForCorporateStoreActivation=[" + pricePlan.isAvailableForCorporateStoreActivation() + "]");
		System.out.println("    AvailableForRetailStoreActivation=[" + pricePlan.isAvailableForRetailStoreActivation() + "]");
		System.out.println("    FidoPricePlan=[" + pricePlan.isFidoPricePlan() + "]");

		System.out.println("    EquipmentTypes={" + arrayToString(pricePlan.getEquipmentTypes()) + "}");
		System.out.println("    Provinces={" + arrayToString(pricePlan.getProvinces()) + "}");

		print("Included Features", pricePlan.getFeatures());
		print("Included Services", pricePlan.getIncludedServices());
		print("Optional Services", pricePlan.getOptionalServices());
		System.out.println("======================================================================");
	}

	public static void print(TMContract contract, ContractService[] contractService) throws Throwable {
		for (int i = 0; i < contractService.length; i++) {
			ContractService cs = contractService[i];
			ServiceAgreementInfo info = (ServiceAgreementInfo) contract.undecorate(cs);

			System.out.println("    ContractService[" + Info.padTo("" + i, ' ', 3) + "][" + cs.getService().getCode() + "][" + ((char) info.getTransaction()) + "]: " + cs.getEffectiveDate() + "  to  "
					+ cs.getExpiryDate() + ";" + "   --  [" + (cs.getService().isSharable() ? "    sharable" : "non-sharable") + "]" + " [\"" + cs.getService().getDescription() + "\"]"
					+ s(cs.getService().getRecurringCharge() > 0, " - $" + cs.getService().getRecurringCharge()));
		}
	}

	public static void print(TMContract contract, ContractFeature[] contractFeature) throws Throwable {
		for (int i = 0; i < contractFeature.length; i++) {
			ContractFeature cf = contractFeature[i];
			ServiceFeatureInfo info = (ServiceFeatureInfo) cf;

			System.out.println("    ContractFeature[" + Info.padTo("" + i, ' ', 3) + "][" + cf.getFeature().getCode() + "][" + ((char) info.getTransaction()) + "]: " + cf.getEffectiveDate() + "  to  "
					+ cf.getExpiryDate() + ";" + " ServiceCode=[" + Info.padTo(cf.getServiceCode(), ' ', 9) + "];" + " SwitchCode=[" + Info.padTo(cf.getFeature().getSwitchCode(), ' ', 6) + "];"
					+ " AdditionalNumber=[" + Info.padTo(cf.getAdditionalNumber(), ' ', 10) + "];" + " Parameter=[" + Info.padTo(cf.getParameter(), ' ', 20) + "];" + " CategoryCode=["
					+ Info.padTo(cf.getFeature().getCategoryCode(), ' ', 3) + "];" + " AdditionalNumberRequired=[" + (cf.getFeature().isAdditionalNumberRequired() ? "X" : " ") + "];"
					+ " ParameterRequired=[" + (cf.getFeature().isParameterRequired() ? "X" : " ") + "];" +

					/*
					 * 
					 * s(cf.getAdditionalNumber(), " AdditionalNumber=[" + cf.getAdditionalNumber()
					 * + "];") + s(cf.getParameter(), " Parameter=[" + cf.getParameter() + "];") +
					 * s(cf.getFeature().getSwitchCode(), " SwitchCode=[" +
					 * cf.getFeature().getSwitchCode() + "];") +
					 * s(cf.getFeature().getCategoryCode(), " CategoryCode=[" +
					 * cf.getFeature().getCategoryCode() + "];") +
					 * s(cf.getFeature().isAdditionalNumberRequired(),
					 * " [AdditionalNumberRequired]")+ s(cf.getFeature().isParameterRequired(),
					 * " [ParameterRequired]")+
					 */
					/*
					 * " AdditionalNumber=[" + cf.getAdditionalNumber() + "];"+ " Parameter=[" +
					 * cf.getParameter() + "];"+ " SwitchCode=[" + cf.getFeature().getSwitchCode() +
					 * "];"+ " CategoryCode=[" + cf.getFeature().getCategoryCode() + "];"+
					 * " AdditionalNumberRequired=[" + cf.getFeature().isAdditionalNumberRequired()
					 * + "];"+ " ParameterRequired=[" + cf.getFeature().isParameterRequired() + "];"
					 * + s
					 */
					"   --  [\"" + cf.getFeature().getDescription() + "\"]" + s(cf.getFeature().getRecurringCharge() > 0, " - $" + cf.getFeature().getRecurringCharge()));
		}
	}

	public static String s(boolean test, String s) {
		return (test) ? s : "";
	}

	public static String s(String value, String s) {
		return (value != null && value.trim().length() > 0) ? s : "";
	}

	public static void print(String prefix, NumberGroup[] o) throws Throwable {
		System.out.println(prefix + ":" + o.length + ":");

		int lineRangeCount = 0;

		NumberGroupInfo[] info = (NumberGroupInfo[]) Info.convertArrayType(o, NumberGroupInfo[].class);
		for (int i = 0; i < info.length; i++) {
			System.out.println("    " + prefix + "[" + i + "]=[" + info[i].getCode() + "]" + " numberLocation=[" + info[i].getNumberLocation() + "]" + " networkId=[" + info[i].getNetworkId() + "]"
					+ " provinceCode=[" + info[i].getProvinceCode() + "]" + " defaultDealerCode=[" + info[i].getDefaultDealerCode() + "]" + " defaultSalesCode=[" + info[i].getDefaultSalesCode() + "]"
					+ " description=[" + info[i].getDescription() + "]");

			NumberRangeInfo[] numberRanges = info[i].getNumberRanges();
			if (numberRanges != null) {
				for (int j = 0; numberRanges != null && j < numberRanges.length; j++) {
					System.out.println("        NumberRange[" + j + "]=[" + numberRanges[j].getCode() + "]" +
					// " nPANXX=[" + numberRanges[j].getNPANXX() + "]" +
							" nPA=[" + numberRanges[j].getNPA() + "]" + " nXX=[" + numberRanges[j].getNXX() + "]");

					LineRangeInfo[] lineRanges = numberRanges[j].getLineRanges();
					for (int k = 0; lineRanges != null && k < lineRanges.length; k++) {
						System.out.println("            LineRange[" + k + "] [" + lineRangeCount + "]" + " provisioningPlatformId=[" + lineRanges[k].getProvisioningPlatformId() + "]" + " ["
								+ lineRanges[k].getStart() + "..." + lineRanges[k].getEnd() + "]");
						lineRangeCount++;
					}
				}
			} /* else */
			{
				String[] npanxx = info[i].getNpaNXX();
				for (int j = 0; npanxx != null && j < npanxx.length; j++) {
					System.out.println("        npanxx[" + j + "]=[" + npanxx[j] + "]");
				}
			}
		}
	}

	public void printDepositHistory(String prefix, Subscriber subscriber) throws TelusAPIException {
		System.out.println(prefix + ":DepositHistory:" + subscriber.getPhoneNumber() + ":-----------------------------------------------------");

		DepositHistory[] depositHistory = subscriber.getAccount().getDepositHistory(new Date(0), new Date());

		// double outstandingDeposit = 0.0;
		for (int i = 0; i < depositHistory.length; i++) {
			String subscriberId = depositHistory[i].getSubscriberId();
			// System.out.println("subscriberId=[" + subscriberId + "]");
			if (subscriberId != null && subscriberId.equals(subscriber.getSubscriberId())) {
				System.out.println("    DepositHistory[" + i + "]");
				System.out.println("        InvoiceCreationDate=[" + depositHistory[i].getInvoiceCreationDate() + "]");
				System.out.println("        InvoiceDueDate=[" + depositHistory[i].getInvoiceDueDate() + "]");
				System.out.println("        InvoiceStatus=[" + depositHistory[i].getInvoiceStatus() + "]");
				System.out.println("        ChargesAmount=[" + depositHistory[i].getChargesAmount() + "]");
				System.out.println("        DepositPaidAmount=[" + depositHistory[i].getDepositPaidAmount() + "]");
				System.out.println("        DepositPaidDate=[" + depositHistory[i].getDepositPaidDate() + "]");
				System.out.println("        DepositReturnDate=[" + depositHistory[i].getDepositReturnDate() + "]");
				System.out.println("        DepositReturnMethod=[" + depositHistory[i].getDepositReturnMethod() + "]");
				System.out.println("        DepositTermsCode=[" + depositHistory[i].getDepositTermsCode() + "]");
				System.out.println("        CancellationDate=[" + depositHistory[i].getCancellationDate() + "]");
				System.out.println("        CancellationReasonCode=[" + depositHistory[i].getCancellationReasonCode() + "]");
				System.out.println("        PaymentExpIndicator=[" + depositHistory[i].getPaymentExpIndicator() + "]");
				System.out.println("        SubscriberId=[" + depositHistory[i].getSubscriberId() + "]");
				System.out.println("        OperatorId=[" + depositHistory[i].getOperatorId() + "]");
			}
		}

		System.out.println("--------------------------------------------------------------------------");
	}

//	public void printLetterRequests(String prefix, Subscriber subscriber)
//			throws Throwable {
//		System.out.println(prefix + ":LMSLetterRequests:"
//				+ subscriber.getPhoneNumber()
//				+ ":-----------------------------------------------------");
//
//		SearchResults letterRequest = subscriber.getAccount()
//				.getLMSLetterRequests(new Date(0), new Date(), 'a',
//						subscriber.getSubscriberId(), 10);
//
//		// double outstandingDeposit = 0.0;
//		for (int i = 0; i < letterRequest.getCount(); i++) {
//			// String subscriberId = letterRequest[i].getSubscriberId();
//			// System.out.println("subscriberId=[" + subscriberId + "]");
//			// if (subscriberId != null &&
//			// subscriberId.equals(subscriber.getSubscriberId())) {
//			System.out.println("letterRequest[" + i + "]");
//			// print(letterRequest.g);
//			// }
//		}
//
//		System.out
//				.println("--------------------------------------------------------------------------");
//	}

	public static NumberGroup[] retainNumberGroupByProvince(NumberGroup[] numberGroups, String provinceCode) throws Throwable {
		List list = new ArrayList();
		for (int i = 0; i < numberGroups.length; i++) {
			if (provinceCode.equals(numberGroups[i].getProvinceCode())) {
				list.add(numberGroups[i]);
			}
		}
		return (NumberGroup[]) list.toArray(new NumberGroup[list.size()]);
	}

	public static NumberGroup[] retainNumberGroupByPrefix(NumberGroup[] numberGroups, String prefix) throws Throwable {

		// retain only the npanxx part
		if (prefix.length() > 6) {
			prefix = prefix.substring(0, 6);

		}

		List list = new ArrayList();
		NUMBER_GROUP_LOOP: for (int i = 0; i < numberGroups.length; i++) {
			if ("TR1".equals(numberGroups[i].getCode())) { // TR1 - TOR #'S FOR
															// TMW ONLY!!
				continue NUMBER_GROUP_LOOP;
			}

			String[] npanxx = numberGroups[i].getNpaNXX();
			for (int j = 0; j < npanxx.length; j++) {
				if (npanxx[j].startsWith(prefix)) {
					list.add(numberGroups[i]);
					continue NUMBER_GROUP_LOOP;
				}
			}
		}
		return (NumberGroup[]) list.toArray(new NumberGroup[list.size()]);
	}

	public String[] getSerialNumbers(String productType, boolean inUse, int count) throws Throwable {
		TMProvider provider = (TMProvider) api.getProvider();

		if (productType.equals(Subscriber.PRODUCT_TYPE_PCS)) {
			return provider.getProductEquipmentHelper().getEquipmentList(EquipmentInfo.PCSTECHTYPECLASS, count, inUse, null);
		} else if (productType.equals(Subscriber.PRODUCT_TYPE_IDEN)) {
			// return
			// provider.getEquipmentHelperEJB().getEquipmentList(EquipmentInfo.IDENTECHTYPECLASS,
			// count, inUse, null); // serialNumbers[0]=[110502327384100]
			return provider.getProductEquipmentHelper().getEquipmentList(EquipmentInfo.SIMTECHTYPECLASS, count, inUse, null); // serialNumbers[0]=[110502327384100]
			// return
			// provider.getEquipmentHelperEJB().getEquipmentList(EquipmentInfo.IDENTECHTYPECLASS,
			// count, inUse, "110502327384100"); //
			// serialNumbers[0]=[110502327384100]
		} else {
			throw new RuntimeException("unknown productType: [" + productType + "]");
		}
	}

	public static Reference[] removeByCode(Reference[] o, String code) throws Throwable {
		List list = new ArrayList(o.length);

		code = code.trim();

		for (int i = 0; i < o.length; i++) {
			if (!o[i].getCode().trim().equals(code)) {
				list.add(o[i]);
			}
		}
		o = (Reference[]) java.lang.reflect.Array.newInstance(o.getClass().getComponentType(), list.size());
		return (Reference[]) list.toArray(o);
	}

	public static PricePlanSummary[] retainShareablePricePlans(PricePlanSummary[] pricePlans) throws TelusAPIException {
		List list = new ArrayList(pricePlans.length);

		for (int i = 0; i < pricePlans.length; i++) {
			PricePlanSummary o = pricePlans[i];
			if (o.isSharable()) {
				list.add(o);
			}
		}
		pricePlans = (PricePlanSummary[]) java.lang.reflect.Array.newInstance(pricePlans.getClass().getComponentType(), list.size());
		return (PricePlanSummary[]) list.toArray(pricePlans);
	}

	// =======================================================================

	// ----------------------------------------------------
	// Environment setup methods.
	// ----------------------------------------------------

	public static void setCommonProperties() {
		System.setProperty("com.telus.provider.initialContextFactory", "weblogic.jndi.WLInitialContextFactory");
		System.setProperty("com.telusmobility.config.java.naming.factory.initial", "com.sun.jndi.ldap.LdapCtxFactory");
		System.setProperty("com.telusmobility.config.provider", "com.telus.provider.config.PropertiesOverridingConfigurationProvider");
		System.setProperty("com.telusmobility.config.propertiesFile", "configuration.properties");
		System.setProperty("com.telusmobility.config.java.naming.security.principal", "uid=armxnonpr,ou=administrators,o=telusconfiguration");
		System.setProperty("com.telusmobility.config.java.naming.security.credentials", "armxnonpr");

	}

//	private static void setProviderProperties(String host, int port) {
//		System.setProperty("com.telus.provider.providerURL", "t3://" + host + ":" + port);
//	}

	private static void setLDAPProperties(String environment, String envladap, int ldapPort) {
		System.setProperty("com.telusmobility.config.java.naming.provider.url", "ldaps://ldapread-" + environment + ".tmi.telus.com:" + ldapPort + "/cn=" + envladap + ",o=telusconfiguration");
	}

	private static void setLDAPProperties(String environment, String envladap, int ldapPort, String configuration) {
		System.setProperty("com.telusmobility.config.java.naming.provider.url", "ldaps://ldapconfigaccess-" + environment + ".tsl.telus.com:" + ldapPort + "/cn=" + envladap + ",o=" + configuration);
	}

//	private static void setHCDProperties(String host, int port) {
//		System.setProperty("com.telus.credit.providerURL", "t3://" + host + ":" + port);
//		System.setProperty("com.telusmobility.config.hcd.ldapEntryNameCommon", "HCD");
//	}

	private static void setCMBProperties(String url) {
		System.setProperty("cmb.services.SubscriberInformationHelper.url", url);
		System.setProperty("cmb.services.SubscriberLifecycleFacade.url", url);
		System.setProperty("cmb.services.SubscriberLifecycleManager.url", url);
		System.setProperty("cmb.services.AccountInformationHelper.url", url);
		System.setProperty("cmb.services.AccountLifecycleManager.url", url);
		System.setProperty("cmb.services.AccountLifecycleFacade.url", url);
		// System.setProperty("cmb.services.ReferenceDataFacade.url", url);
		// System.setProperty("cmb.services.ReferenceDataHelper.url", url);
		// System.setProperty("cmb.services.ConfigurationManager.url", url);
	}

	public static void setupD1() {
		setCommonProperties();
		setLDAPProperties("d1", "development1_81", 289);
	}

	public static void setupD2() {
		setCommonProperties();
		setLDAPProperties("d2", "development2_81", 389);
	}

	public static void localhost() {
		localhostWithD3Ldap();
	}

	public static void localhostWithD1Ldap() {
		setCommonProperties();
		setLDAPProperties("d1", "development1_81", 489);
	}

	public static void localhostWithD2Ldap() {
		setCommonProperties();
		setLDAPProperties("d2", "development2_81", 389);
	}

	public static void localhostWithD3Ldap() {
		setCommonProperties();
		setLDAPProperties("d3", "development3_81", 489);
	}

	public static void localhostWithPT148Ldap() {
		setCommonProperties();
		setLDAPProperties("qa", "qa_81", 589);
	}

	public static void localhostWithPT168Ldap() {
		setCommonProperties();
		setLDAPProperties("pt168", "pt168_81", 589);
	}

	public static void localhostWithPT140Ldap() {
		setCommonProperties();
		setLDAPProperties("qa2", "qa2_81", 1489);
	}

	/*------------------- D3 SETTINGS -----------------------*/
	public static void setupD3() {
		setCommonProperties();
		setLDAPProperties("d3", "development3_81", 489);
	}

	public static void setupEASECA_D3() {
		setCommonProperties();
		setLDAPProperties("d3", "development3_81", 489);
	}

	public static void setupSMARTDESKTOP_D3() {
		setCommonProperties();
		setLDAPProperties("d3", "development3_81", 489);
	}

	/*------------------- SIT SETTINGS -----------------------*/
	public static void setupJ() {
		setCommonProperties();
		setLDAPProperties("sit", "sit_81", 1489);
		}

	public static void setupEASECA_SIT() {
		setCommonProperties();
		setLDAPProperties("sit", "sit_81", 1489);
	}

	public static void setupINTECA_SIT() {
		setCommonProperties();
		setLDAPProperties("sit", "sit_81", 1489);
	}

	public static void setupCHNLECA_SIT() {
		setCommonProperties();
		setLDAPProperties("sit", "sit_81", 1489);
	}

	public static void setupSMARTDESKTOP_SIT() {
		setCommonProperties();
		setLDAPProperties("sit", "sit_81", 1489);
	}

	/*------------------- PT168 SETTINGS -----------------------*/
	public static void setupEASECA_PT168() {
		setCommonProperties();
		setLDAPProperties("pt168", "pt168_81", 589);
	}

	public static void setupINTECA_PT168() {
		setCommonProperties();
		setLDAPProperties("pt168", "pt168_81", 636);
	}

	public static void setupCHNLECA_PT168() {
		setCommonProperties();
		setLDAPProperties("pt168", "pt168_81", 636);
	}

	public static void setupSMARTDESKTOP_PT168() {
		setCommonProperties();
		setLDAPProperties("pt168", "pt168_81", 636);
	}

	/*------------------- QA SETTINGS -----------------------*/
	public static void setupK() {
		setCommonProperties();
		setLDAPProperties("qa", "qa_81", 589);

		System.setProperty("com.telusmobility.config.java.naming.provider.url", "ldaps://ldapread-qa.tmi.telus.com:636/cn=qa_81,o=telusconfiguration ");
		System.setProperty("com.telusmobility.config.java.naming.security.principal", "uid=telusAdmin,ou=administrators,o=telusconfiguration");
		System.setProperty("com.telusmobility.config.java.naming.security.credentials", "ptEM148");
	}

	public static void setupEASECA_QA() {
		setCommonProperties();
		setLDAPProperties("qa", "qa_81", 589);
	}

	public static void setupINTECA_QA() {
		setCommonProperties();
		setLDAPProperties("qa", "qa_81", 589);
	}

	public static void setupCHNLECA_QA() {
		setCommonProperties();
		setLDAPProperties("qa", "qa_81", 589);
	}

	public static void setupSMARTDESKTOP_QA() {
		setCommonProperties();
		setLDAPProperties("qa", "qa_81", 589);
	}

	/*------------------- STAGING SETTINGS -----------------------*/
	public static void setupS() {
		setCommonProperties();
		setLDAPProperties("s", "s_81", 1589);
	}

	public static void setupEASECA_STG() {
		setCommonProperties();
		setLDAPProperties("s", "s_81", 1589);
	}

	public static void setupINTECA_STG() {
		setCommonProperties();
		setLDAPProperties("s", "s_81", 1589);
	}

	public static void setupCHNLECA_STG() {
		setCommonProperties();
		setLDAPProperties("s", "s_81", 1589);
	}

	public static void setupSMARTDESKTOP_STG() {
		setCommonProperties();
		setLDAPProperties("s", "s_81", 1589);
	}

	/*------------------- CSI SETTINGS -----------------------*/
	public static void setupCSI() {
		setCommonProperties();
		setLDAPProperties("qa2", "qa2_81", 636);
	}

	public static void setupLocalhostWithCSI() {
		setCommonProperties();
		setLDAPProperties("qa2", "qa2_81", 636);
	}

	public static void setupEASECA_CSI() {
		setCommonProperties();
		setLDAPProperties("qa2", "qa2_81", 636);
	}

	public static void setupINTECA_CSI() {
		setCommonProperties();
		setLDAPProperties("qa2", "qa2_81", 636);
	}

	public static void setupCHNLECA_CSI() {
		setCommonProperties();
		setLDAPProperties("qa2", "qa2_81", 636);
	}

	public static void setupSMARTDESKTOP_CSI() {
		setCommonProperties();
		setLDAPProperties("qa2", "qa2_81", 636);
	}

	public static void setupT() {
		setCommonProperties();
		setLDAPProperties("t", "t_81", 789);
	}

	public static void setupP() {
		setCommonProperties();
		setLDAPProperties("p", "prod_81", 389);
	}

	public static void setupPA() {

		setCommonProperties();
		setLDAPProperties("pra", "prod_81", 389, "telusconfiguration_a");
		System.setProperty("com.telusmobility.config.java.naming.provider.url", "ldaps://ldapread-pra.tmi.telus.com:636/cn=prod_81,o=telusconfiguration_a ");
		System.setProperty("com.telusmobility.config.java.naming.security.principal", "uid=telusAdmin,ou=administrators,o=telusconfiguration_a");
		System.setProperty("com.telusmobility.config.java.naming.security.credentials", "EMprod123");
	}

	public static void setupPB() {
		setCommonProperties();
		setLDAPProperties("prb", "prod_81", 636, "telusconfiguration_b");

		System.setProperty("com.telusmobility.config.java.naming.provider.url", "ldaps://ldapread-prb.tmi.telus.com:636/cn=prod_81,o=telusconfiguration_b");
		System.setProperty("com.telusmobility.config.java.naming.security.principal", "uid=telusAdmin,ou=administrators,o=telusconfiguration_b");
		System.setProperty("com.telusmobility.config.java.naming.security.credentials", "EMprod123");
		}

	public static void setupPwithStagingLDAP() {
		setCommonProperties();
		// setLDAPProperties("p","prod_81",389);
		setLDAPProperties("s", "s_81", 1589);
	}

	public static void setuplocalHost() {
		setCommonProperties();
		setLDAPProperties("d3", "development3_81", 489);
	}

	// =======================================================================

	private static final Class objectClass = Object.class;
	private static final String INDENT = "  ";
	private static final Set skipClassesSet = new HashSet();
	private static final String[] skipPackageSet;
	private static final FieldComparator fieldComparator = new FieldComparator();

	static {
		skipClassesSet.add("com.telus.eas.framework.info.Info");
		skipClassesSet.add("com.telus.provider.TMProvider");
		skipClassesSet.add("com.telus.provider.BaseProvider");
		skipClassesSet.add("com.telus.eas.utility.info.PricePlanInfo");
		skipClassesSet.add("com.telus.eas.account.info.ChequeInfo");
		skipClassesSet.add("com.telus.eas.account.info.BankAccountInfo");
		skipClassesSet.add("java.util.HashMap");
		skipClassesSet.add("com.inprise.security.EJBsec");
		skipClassesSet.add("com.inprise.security.EJBsec.Current");
		skipClassesSet.add("com.telus.activations.web.form.BaseRequestData");
		skipClassesSet.add("com.telus.provider.reference.TMReferenceDataManager");

		skipPackageSet = new String[] { "com.inprise.", "com.sun.", "weblogic.", "com.telus.activations.web.state." };
	}

	public static final void skipClass(String className, boolean skip) {
		if (skip) {
			skipClassesSet.add(className);
		} else {
			skipClassesSet.remove(className);
		}
	}

	public static final void skipClass(Class clazz, boolean skip) {
		skipClass(clazz.getName(), skip);
	}

	public static final void skipClass(Class clazz) {
		skipClass(clazz, true);
	}

	public static final void skipClass(String className) {
		skipClass(className, true);
	}

	public static final boolean checkSkipClass(String className) {
		if (skipClassesSet.contains(className)) {
			return true;
		}

		for (int i = 0; i < skipPackageSet.length; i++) {

			if (className.startsWith(skipPackageSet[i])) {
				return true;
			}
		}
		return false;
	}

	public static final String toString(Object o) {
		Set objectSet = new HashSet(256);
		try {
			if (o == null) {
				return "";
			} else {
				return toString(objectSet, new StringBuffer(10 * 1024), o.getClass(), null, o, 0).toString();
			}
		} catch (Throwable e) {
			System.err.println(e);
			throw new RuntimeException(e.getMessage());
		} finally {
			objectSet.clear();
		}
	}

	public static final StringBuffer toString(Set objectSet, StringBuffer s, Class clazz, String oName, Object o, int depth) throws Throwable {
		// System.out.println("toString(Set objectSet, Set skipClassesSet, StringBuffer
		// s, "+clazz.getName()+", \""+oName+"\", Object o, "+depth+")");

		try {
			String className = clazz.getName();
			if (checkSkipClass(className)) {
				return s;
			}

			String indent = repeat(INDENT, depth);
			String key = className + "-" + System.identityHashCode(o);
			if (objectSet.contains(key)) {
				s.append(indent).append(oName).append("=[...]\n");
				return s;
			}
			objectSet.add(key);
			// System.out.println(key);

			Field[] fields = clazz.getDeclaredFields();
			Arrays.sort(fields, fieldComparator);

			if (oName != null) {
				s.append(indent).append(oName).append('=').append(className).append("[\n");
			} else {
				s.append(indent).append(className).append("[\n");
			}

			for (int i = 0; i < fields.length; i++) {
				Field f = fields[i];
				f.setAccessible(true);

				// System.out.println();
				Object fieldObject = f.get(o);
				Class t = (fieldObject == null) ? f.getType() : fieldObject.getClass(); // f.getType();
				// Class t = f.getType();
				String fieldName = f.getName();

				// System.out.println(fieldName);

				// if(isPrimitive(t) || fieldObject == null)
				// {
				// s.append(indent).append(INDENT).append(fieldName).append("=[").append(fieldObject).append("]\n");
				// }
				if (fieldObject == null) {
					s.append(indent).append(INDENT).append(fieldName).append("=[null]\n");
				} else if (isPrimitive(t)) {
					String value = fieldObject.toString();
					if (t == String.class || t == Character.TYPE || t == Character.class) {
						value = "\"" + value + "\"";
					}
					s.append(indent).append(INDENT).append(fieldName).append("=[").append(value).append("]\n");
				} else if (t.isArray()) {
					Class componentType = t.getComponentType();
					int length = Array.getLength(fieldObject);

					if (length == 0) {
						s.append(indent).append(INDENT).append(fieldName).append("={}\n");
					} else {
						if (isPrimitive(componentType)) {
							for (int j = 0; j < length; j++) {
								s.append(indent).append(INDENT).append(fieldName + "[" + j + "]").append("=[").append(Array.get(fieldObject, j)).append("]\n");
							}
						} else {
							for (int j = 0; j < length; j++) {
								Object arrayElement = Array.get(fieldObject, j);
								if (arrayElement == null) {
									s.append(indent).append(INDENT).append(fieldName + "[" + j + "]").append("=[null]\n");
								} else {
									toString(objectSet, s, arrayElement.getClass(), fieldName + "[" + j + "]", arrayElement, depth + 1);
								}
							}
						}
					}
				} else {
					toString(objectSet, s, t, fieldName, fieldObject, depth + 1);
				}
			}

			Class superClass = clazz.getSuperclass();
			if (superClass != null && superClass != objectClass) {
				toString(objectSet, s, superClass, "super", o, depth + 1);
			}

			s.append(indent).append("]\n");

		} catch (Throwable e) {
			System.err.println("Error in toString(Set objectSet, StringBuffer s, " + clazz.getName() + ", \"" + oName + "\", Object o, " + depth + ")");
			System.err.println(e);
		}
		return s;
	}

	private static final boolean isPrimitive(Class clazz) throws Throwable {
		String name = clazz.getName();
		return !checkSkipClass(name) && (clazz.isPrimitive() || name.startsWith("java.") || name.startsWith("javax."));
		// return clazz.isPrimitive() || name.startsWith("java.lang.") ||
		// name.startsWith("java.sql.") || name.equals("java.util.Date");
	}

	public static String repeat(String string, int count) {
		StringBuffer s = new StringBuffer(string.length() * count);

		for (int i = 0; i < count; i++) {
			s.append(string);
		}

		return s.toString();
	}

	public static class FieldComparator implements Comparator {

		public int compare(Object o1, Object o2) {
			Field f1 = (Field) o1;
			Field f2 = (Field) o2;

			if (f1 == f2) {
				return 0;
			} else if (f1 == null) {
				return -1;
			} else if (f2 == null) {
				return 1;
			} else {
				// return f1.getName().compareTo(f2.getName());
				return f1.getName().compareToIgnoreCase(f2.getName());
			}
		}

		public boolean equals(Object o) {
			return this == o;
		}
	}

}
