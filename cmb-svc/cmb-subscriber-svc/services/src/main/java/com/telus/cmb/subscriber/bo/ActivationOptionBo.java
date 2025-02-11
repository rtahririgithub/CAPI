package com.telus.cmb.subscriber.bo;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.telus.api.ApplicationException;
import com.telus.api.ErrorCodes;
import com.telus.api.SystemCodes;
import com.telus.api.TelusAPIException;
import com.telus.api.account.ActivationOption;
import com.telus.api.account.ActivationOptionType;
import com.telus.api.account.CreditCheckResultDeposit;
import com.telus.api.account.Subscriber;
import com.telus.api.reference.Brand;
import com.telus.cmb.subscriber.utilities.ActivatePortinContext;
import com.telus.cmb.subscriber.utilities.ActivationChangeContext;
import com.telus.cmb.subscriber.utilities.BaseChangeContext;
import com.telus.cmb.subscriber.utilities.MigrateSeatChangeContext;
import com.telus.cmb.subscriber.utilities.MigrationChangeContext;
import com.telus.eas.account.info.CreditCheckResultDepositInfo;
import com.telus.eas.account.info.CreditCheckResultInfo;
import com.telus.eas.framework.info.MemoInfo;
import com.telus.eas.subscriber.info.ActivationChangeInfo;
import com.telus.eas.subscriber.info.BaseChangeInfo;

public class ActivationOptionBo {

	private static final Log logger = LogFactory.getLog(ActivationOptionBo.class);

	private static final String ACTIVATION_MEMO_TYPE = "ACTR";
	private static final String CLP_ACTIVATION_MEMO_TYPE = "CLMA";
	private static final String DEPOSIT_CHANGE_REASON_CODE = "01";
	private static final String CLP_DEPOSIT_CHANGE_REASON_CODE = "14";
	private static final String[] CLP_CREDIT_CLASSES = { "X", "L" };

	private BaseChangeContext<? extends BaseChangeInfo> context;
	private ActivationOption activationOption;
	private SubscriberBo subscriber;
	private AccountBo account;
	
	private boolean isDifferentiated;
	private CreditCheckResultDepositInfo[] originalDeposits;
	
	private boolean isTown;
	private boolean limitChanged;
	private boolean depositChanged;
	
	private static final NumberFormat formatter = new DecimalFormat("0.00");

	public <T extends BaseChangeInfo> ActivationOptionBo(BaseChangeContext<T> context) throws ApplicationException {

		this.context = context;
		this.subscriber = context.getCurrentSubscriber();
		if (context instanceof ActivationChangeContext || context instanceof ActivatePortinContext) {
			this.activationOption = ((ActivationChangeInfo) context.getChangeInfo()).getActivationOption();
			this.account = context.getCurrentAccount();
		} else if (context instanceof MigrationChangeContext) {
			this.activationOption = ((MigrationChangeContext) context).getChangeInfo().getActivationOptionInfo();
			this.account = ((MigrationChangeContext) context).getNewAccount();
		} else if (context instanceof MigrateSeatChangeContext) {
			// Seat migrations are actually moves, not migrations
			String errorMessage = "Invalid context [MigrateSeatChangeContext]; activation option is not supported for seat migrations.";
			logger.error(errorMessage);
			throw new ApplicationException(SystemCodes.CMB_SLF_EJB, ErrorCodes.INVALID_CONTEXT, errorMessage, "");
		} else {
			String errorMessage = "Invalid context [" + context.getClass() + "]; activation option is not supported for this flow.";
			logger.error(errorMessage);
			throw new ApplicationException(SystemCodes.CMB_SLF_EJB, ErrorCodes.INVALID_CONTEXT, errorMessage, "");
		}
		if (activationOption != null && account.getCreditCheckResult() != null) {
			this.limitChanged = activationOption.getCreditLimit() != account.getCreditCheckResult().getLimit()
					|| !StringUtils.equalsIgnoreCase(activationOption.getCreditClass(), account.getCreditCheckResult().getCreditClass());
			this.depositChanged = ArrayUtils.isEmpty(account.getCreditCheckResult().getDeposits()) || activationOption.getDeposit() != account.getCreditCheckResult().getDeposit();
		}
	}

	public void apply() throws TelusAPIException, ApplicationException {
		
		if (activationOption == null || activationOption.getOptionType() == null) {
			if (context.getRefDataFacade().isCDASupportedAccountTypeSubType(String.valueOf(account.getAccountType()) + String.valueOf(account.getAccountSubType()))) {
				// In the CDA 1B world, activation option cannot be null - throw an exception
				String errorMessage = "Invalid or null activation option.";
				logger.error(errorMessage);
				throw new ApplicationException(SystemCodes.CMB_SLF_EJB, ErrorCodes.INVALID_ACTIVATION_OPTION, errorMessage, "");
			} else {
				// Otherwise, if this isn't a CDA-supported account type or the CDA 1B dormant flag is 'ON', null values are acceptable so do nothing and return
				return;
			}			
		}
		if (StringUtils.equalsIgnoreCase(ActivationOptionType.DEPOSIT, activationOption.getOptionType().getName())) {
			updateDeposit();
			return;
		}
		if (StringUtils.equalsIgnoreCase(ActivationOptionType.CREDIT_LIMIT, activationOption.getOptionType().getName())
				|| StringUtils.equalsIgnoreCase(ActivationOptionType.CREDIT_LIMIT_AND_DEPOSIT, activationOption.getOptionType().getName())) {
			updateCLP();
			return;
		}
		if (StringUtils.equalsIgnoreCase(ActivationOptionType.DEALER_DEPOSIT_CHANGE, activationOption.getOptionType().getName())) {
			updateDealerDeposit();
			return;
		}
		if (!isDifferentiated && StringUtils.equalsIgnoreCase(ActivationOptionType.DIFFERENTIATED_CREDIT, activationOption.getOptionType().getName())) {
			updateDifferentiate();
			return;
		}
		if (StringUtils.equalsIgnoreCase(ActivationOptionType.NDP, activationOption.getOptionType().getName())) {
			updateNDP();
			return;
		}
		if (StringUtils.equalsIgnoreCase(ActivationOptionType.DECLINED, activationOption.getOptionType().getName())) {
			updateDeclined();
			return;
		}
		// If the activation option is none of the above, throw an exception
		String errorMessage = "Unknown activation option type [" + activationOption.getOptionType().getName() + "].";
		logger.error(errorMessage);
		throw new ApplicationException(SystemCodes.CMB_SLF_EJB, ErrorCodes.INVALID_ACTIVATION_OPTION, errorMessage, "");
	}

	private void validateUpdate(String creditProfileChangeText, String creditResultChangeText, String depositChangeReasonCode, CreditCheckResultDepositInfo[] deposits) throws ApplicationException {
		context.getAccountLifecycleFacade().updateCreditWorthiness(account.getBanId(), activationOption.getOptionType().getName(), activationOption.getCreditClass(), activationOption.getCreditLimit(),
				creditProfileChangeText, limitChanged, deposits, depositChangeReasonCode, creditResultChangeText, depositChanged, false, context.getAuditInfo(), null,
				context.getAccountLifecycleFacadeSessionId());
	}
	
	private void updateDeposit() throws ApplicationException {
		
		StringBuilder messageText = new StringBuilder();
		messageText.append("Activation option = Deposit client activation.");
		
		validateUpdate(messageText.toString(), getCreditResultChangeText(messageText), DEPOSIT_CHANGE_REASON_CODE, updateCreditCheckDeposits(activationOption.getDeposit(), account.getCreditCheckResult().getDeposits()));
	}
	
	private void updateDeclined() throws ApplicationException {
		
		StringBuilder messageText = new StringBuilder();
		messageText.append("Activation option = Declined client activation.");
		
		validateUpdate(messageText.toString(), getCreditResultChangeText(messageText), DEPOSIT_CHANGE_REASON_CODE, updateCreditCheckDeposits(activationOption.getDeposit(), account.getCreditCheckResult().getDeposits()));
	}
	
	private void updateNDP() throws ApplicationException {
		
		StringBuilder messageText = new StringBuilder();
		messageText.append("Activation option = NDP client activation.");
		
		validateUpdate(messageText.toString(), getCreditResultChangeText(messageText), DEPOSIT_CHANGE_REASON_CODE, updateCreditCheckDeposits(activationOption.getDeposit(), account.getCreditCheckResult().getDeposits()));
	}
	
	private void updateDealerDeposit() throws ApplicationException {

		if (depositChanged) {
			MemoInfo memo = new MemoInfo(account.getBanId(), ACTIVATION_MEMO_TYPE, subscriber.getSubscriberId(), subscriber.getProductType(), "Deposit change due to activation option type = [DealerDepositChange].");
			context.getAccountLifecycleFacade().asyncCreateMemo(memo, context.getAccountLifecycleFacadeSessionId());
		} 

		StringBuilder messageText = new StringBuilder();
		messageText.append("Activation option = Dealer Deposit Change.");
		String creditProfileChangeText = messageText.toString();
		
		messageText = activationOption.getDeposit() > account.getCreditCheckResult().getDeposit() ? messageText.append(" Deposit increase from ") : messageText.append(" Deposit restore from ");
		messageText.append(formatter.format(account.getCreditCheckResult().getDeposit()));
		messageText.append(" to ");
		messageText.append(formatter.format(activationOption.getDeposit())).append(".");
		String creditResultChangeText = messageText.toString();

		validateUpdate(creditProfileChangeText, creditResultChangeText, DEPOSIT_CHANGE_REASON_CODE, updateCreditCheckDeposits(activationOption.getDeposit(), account.getCreditCheckResult().getDeposits()));
	}
	
	private void updateCLP() throws TelusAPIException, ApplicationException {

		StringBuilder messageText = new StringBuilder();
		messageText = isTown ? messageText.append("Activation option = CLP client TOWN.") :	messageText.append("Activation option = CLP client activation.");
		
		validateUpdate(messageText.toString(), getCreditResultChangeText(messageText), CLP_DEPOSIT_CHANGE_REASON_CODE, updateCreditCheckDeposits(activationOption.getDeposit(), account.getCreditCheckResult().getDeposits()));

		// Create CLP memo
		StringBuilder memoText = new StringBuilder();
		if (Arrays.asList(CLP_CREDIT_CLASSES).contains(activationOption.getCreditClass())) {
			memoText.append("Credit Limit client activation:");
			memoText.append(" original deposit assessed: " + account.getCreditCheckResult().getDeposit());
			memoText.append(" original credit class assessed: " + account.getCreditCheckResult().getCreditClass());
			memoText.append(" original credit limit assessed: " + account.getCreditCheckResult().getLimit());
			memoText.append(" deposit assigned: " + activationOption.getDeposit());
			memoText.append(" credit class assigned: " + activationOption.getCreditClass());
			memoText.append(" credit limit assigned: " + activationOption.getCreditLimit()).append(".");
			MemoInfo memo = new MemoInfo(account.getBanId(), CLP_ACTIVATION_MEMO_TYPE, subscriber.getSubscriberId(), subscriber.getProductType(), memoText.toString());
			context.getAccountLifecycleFacade().asyncCreateMemo(memo, context.getAccountLifecycleFacadeSessionId());
		
			// Create CLP letter
			// TODO do we still need Amp'd and Clearnet?
			String letterCategory, letterCode;
			switch (account.getBrandId()) {
			case Brand.BRAND_ID_AMPD:
				letterCategory = "ACL";
				letterCode = StringUtils.equalsIgnoreCase(Subscriber.LANGUAGE_FRENCH, subscriber.getLanguage()) ? "CLMF" : "CLME";
				break;
			case Brand.BRAND_ID_CLEARNET:
				letterCategory = "DCL";
				letterCode = StringUtils.equalsIgnoreCase(Subscriber.LANGUAGE_FRENCH, subscriber.getLanguage()) ? "DLPF" : "DLPE";
				break;
			case Brand.BRAND_ID_KOODO:
				letterCategory = "KCL";
				letterCode = StringUtils.equalsIgnoreCase(Subscriber.LANGUAGE_FRENCH, subscriber.getLanguage()) ? "CLMF" : "CLME";
				break;
			case Brand.BRAND_ID_TELUS:
				letterCategory = "CLP";
				letterCode = StringUtils.equalsIgnoreCase(Subscriber.LANGUAGE_FRENCH, subscriber.getLanguage()) ? "CLMF" : "CLME";
				break;			
			default:
				return;
			}
//			// Create the letter request
//			Letter letter = context.getRefDataHelper().retrieveLetter(letterCategory, letterCode);			
//			account.createManualLetterRequest(letter);
		}
	}

	private void updateDifferentiate() throws ApplicationException {

		// Transform and store the current account credit check deposits for use in the setBackOrginalDeposit method later
		originalDeposits = (CreditCheckResultDepositInfo[]) account.getCreditCheckResult().getDeposits();

		StringBuilder messageText = new StringBuilder();
		messageText.append("Activation option = Differentiated Credit.");

		validateUpdate(messageText.toString(), getCreditResultChangeText(messageText), DEPOSIT_CHANGE_REASON_CODE, updateCreditCheckDeposits(activationOption.getDeposit(), account.getCreditCheckResult().getDeposits()));
		
		// Set the isDifferentiated flag to track the change
		isDifferentiated = originalDeposits != null && depositChanged;
	}

	public void setBackOriginalDepositIfDifferentiated() throws ApplicationException {

		if (isDifferentiated) {

			double amount = 0;
			for (CreditCheckResultDepositInfo info : originalDeposits) {
				if (StringUtils.equalsIgnoreCase(info.getProductType(), subscriber.getProductType())) {
					amount = info.getDeposit();
				}
			}

			StringBuilder messageText = new StringBuilder();
			messageText.append("Activation option = Differentiated Credit. Set back deposit from ");
			messageText.append(formatter.format(activationOption.getDeposit()));
			messageText.append(" to ");
			messageText.append(formatter.format(amount)).append(".");

			context.getAccountLifecycleFacade().updateCreditCheckResult(account.getBanId(), activationOption.getCreditClass(), originalDeposits, CLP_DEPOSIT_CHANGE_REASON_CODE,
					messageText.toString(), context.getAccountLifecycleFacadeSessionId());
		}
	}

	private CreditCheckResultDepositInfo[] updateCreditCheckDeposits(double newDeposit, CreditCheckResultDeposit[] creditCheckDeposits) {

		List<CreditCheckResultDepositInfo> updatedDepositsList = new ArrayList<CreditCheckResultDepositInfo>();
		if (creditCheckDeposits != null && ArrayUtils.isNotEmpty(creditCheckDeposits)) {
			// Add the product types of the old deposits array, but with the new deposit value
			for (CreditCheckResultDeposit deposit : creditCheckDeposits) {
				CreditCheckResultDepositInfo info = new CreditCheckResultDepositInfo();
				info.setDeposit(newDeposit);
				info.setProductType(deposit.getProductType());
				updatedDepositsList.add(info);
			}
		} else {
			// Create a default deposit to hold the new deposit value
			CreditCheckResultDepositInfo info = new CreditCheckResultDepositInfo();
			info.setDeposit(newDeposit);
			info.setProductType(CreditCheckResultInfo.PRODUCT_TYPE_CELLULAR);
			updatedDepositsList.add(info);
		}

		return updatedDepositsList.toArray(new CreditCheckResultDepositInfo[0]);
	}
	
	private String getCreditResultChangeText(StringBuilder messageText) {
		
		messageText.append(" Deposit changed from ");
		messageText.append(formatter.format(account.getCreditCheckResult().getDeposit()));
		messageText.append(" to ");
		messageText.append(formatter.format(activationOption.getDeposit())).append(".");
		
		return messageText.toString();
	}

	public boolean validate() throws ApplicationException {

		if (activationOption == null || activationOption.getOptionType() == null) {
			// If this isn't a CDA-supported account type or the CDA 1B dormant flag is 'ON', return true
			if (!context.getRefDataFacade().isCDASupportedAccountTypeSubType(String.valueOf(account.getAccountType()) + String.valueOf(account.getAccountSubType()))) {
				return true;
			}
			// In the CDA world, activation option cannot be null - return false
			return false;
		}
		// Check to see if the activation option is one of the valid types
		return (StringUtils.equalsIgnoreCase(ActivationOptionType.DEPOSIT, activationOption.getOptionType().getName())
				|| StringUtils.equalsIgnoreCase(ActivationOptionType.CREDIT_LIMIT, activationOption.getOptionType().getName())
				|| StringUtils.equalsIgnoreCase(ActivationOptionType.CREDIT_LIMIT_AND_DEPOSIT, activationOption.getOptionType().getName())
				|| StringUtils.equalsIgnoreCase(ActivationOptionType.DEALER_DEPOSIT_CHANGE, activationOption.getOptionType().getName())
				|| StringUtils.equalsIgnoreCase(ActivationOptionType.DIFFERENTIATED_CREDIT, activationOption.getOptionType().getName())
				|| StringUtils.equalsIgnoreCase(ActivationOptionType.NDP, activationOption.getOptionType().getName())
				|| StringUtils.equalsIgnoreCase(ActivationOptionType.DECLINED, activationOption.getOptionType().getName()));
	}

}