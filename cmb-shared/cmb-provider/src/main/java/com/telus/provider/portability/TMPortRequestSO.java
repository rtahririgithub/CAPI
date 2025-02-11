package com.telus.provider.portability;

import java.util.Hashtable;
import java.util.Locale;
import com.telus.api.TelusAPIException;
import com.telus.api.message.ApplicationMessage;
import com.telus.api.portability.PRMReferenceData;
import com.telus.api.portability.PortOutEligibility;
import com.telus.api.portability.PortRequestException;
import com.telus.api.reference.ApplicationSummary;
import com.telus.eas.portability.info.PRMReferenceDataInfo;
import com.telus.provider.BaseProvider;
import com.telus.provider.TMProvider;

/**
 * @author Vladimir Tsitrin
 * @version 1.0, 5-Sep-2006
 */
public final class TMPortRequestSO extends BaseProvider {

	private static final long serialVersionUID = 1L;

	private static final Hashtable prmReferenceData = new Hashtable();
	public TMPortRequestSO(TMProvider provider) {
		super(provider);

		try {
			loadPRMReferenceData();
		}
		catch(Exception e) {
			throw new ExceptionInInitializerError(e);
		}
	}


	/**
	 * This method functions the same as getReferenceData(String) except the brandId parameter is used instead of provider.getContextBrandId()
	 * @param code
	 * @param brandId
	 * @return
	 */
	public PRMReferenceData getReferenceData(String code, int brandId) {
		PRMReferenceDataInfo prmReferenceDataInfo = null;

		if (code == null)
			throw new NullPointerException("Input parameter <code> cannot be null...");

		int categoriesSz = PRMReferenceData.allCategories != null ? PRMReferenceData.allCategories.length : 0;

		for (int i = 0; i < categoriesSz; i++) {
			PRMReferenceData prmReferenceData = getReferenceData(PRMReferenceData.allCategories[i], code);

			if (prmReferenceData != null) {
				int prmBrandId = (brandId < 0 ? provider.getContextBrandId() : brandId);
				prmReferenceDataInfo = new PRMReferenceDataInfo();
				prmReferenceDataInfo.setCode(prmReferenceData.getCode());
				prmReferenceDataInfo.setCategory(prmReferenceData.getCategory());
				prmReferenceDataInfo.setDescription(provider.getApplicationMessage(ApplicationSummary.APP_PRM, prmReferenceData.getCode(), prmBrandId).getText(Locale.CANADA));
				prmReferenceDataInfo.setDescriptionFrench(provider.getApplicationMessage(ApplicationSummary.APP_PRM, prmReferenceData.getCode(), prmBrandId).getText(Locale.CANADA_FRENCH));
				prmReferenceDataInfo.setCategory(prmReferenceData.getCategory());
				break;
			}
		}

		return prmReferenceDataInfo;
	}

	private PRMReferenceData getReferenceData(String category, String code) {
		if (category == null || code == null)
			throw new NullPointerException("Input parameters <category> and <code> cannot be null.");

		Hashtable refDataByCode = (Hashtable) prmReferenceData.get(category);

		return refDataByCode != null ? (PRMReferenceData) refDataByCode.get(code) : null;
	}


	private void loadPRMReferenceData() throws TelusAPIException {
		if (prmReferenceData.size() > 0) {
			return;
		}

		try {
			int categoriesSz = PRMReferenceData.allCategories != null ? PRMReferenceData.allCategories.length : 0;

			for (int i = 0; i < categoriesSz; i++) {
				String category = PRMReferenceData.allCategories[i];
				Hashtable refDataByCode = new Hashtable();

				PRMReferenceData[] referenceData = provider.getSubscriberLifecycleFacade().retrieveReferenceData(category);

				int prmReferenceDataSz = referenceData != null ? referenceData.length : 0;
				for (int j = 0; j < prmReferenceDataSz; j++)
					if (referenceData[j] != null) {
						String refCode = referenceData[j].getCode();
						ApplicationMessage mappedAppMessage = refCode != null ? provider.getApplicationMessage(ApplicationSummary.APP_PRM, refCode) : null;

						if (mappedAppMessage != null) {
							PRMReferenceDataInfo mappedReferenceData = new PRMReferenceDataInfo();
							mappedReferenceData.setCategory(PRMReferenceData.allCategories[i]);
							mappedReferenceData.setCode(refCode);
							mappedReferenceData.setDescription(mappedAppMessage.getText(Locale.ENGLISH));
							mappedReferenceData.setDescriptionFrench(mappedAppMessage.getText(Locale.FRENCH));

							refDataByCode.put(referenceData[j].getCode(), mappedReferenceData);
						}
						else
							refDataByCode.put(referenceData[j].getCode(), referenceData[j]);
					}

				prmReferenceData.put(category, refDataByCode);
			}
		}
		catch(Throwable e) {
			throw new TelusAPIException("Cannot load PRM reference data.", e);
		}
	}

}