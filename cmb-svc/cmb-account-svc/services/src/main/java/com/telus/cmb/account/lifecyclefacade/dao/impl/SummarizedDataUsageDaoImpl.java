package com.telus.cmb.account.lifecyclefacade.dao.impl;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.telus.api.ApplicationException;
import com.telus.cmb.account.lifecyclefacade.dao.SummarizedDataUsageDao;
import com.telus.cmb.common.dao.soa.spring.SoaBaseSvcClient;
import com.telus.cmb.common.dao.soa.spring.SoaCallback;
import com.telus.cmb.wsclient.SummaryDataServicesUsageServicePort;
import com.telus.tmi.xmlschema.srv.cmo.billinginquirymgmt.summarydatausageservicerequestresponse_v5.SearchOutstandingAmount;
import com.telus.tmi.xmlschema.srv.cmo.billinginquirymgmt.summarydatausageservicerequestresponse_v5.SearchUnbilledAmount;
import com.telus.tmi.xmlschema.xsd.enterprise.basetypes.types.ping_v1.Ping;

/**
 * @author Michael Liao
 *
 */
public class SummarizedDataUsageDaoImpl extends SoaBaseSvcClient implements SummarizedDataUsageDao {

	private static final Log logger = LogFactory.getLog(SummarizedDataUsageDaoImpl.class);

	@Autowired
	private SummaryDataServicesUsageServicePort dataUsageService;

	@Override
	public double getTotalOutstandingAmount(final int banId, final Date fromDate) throws ApplicationException {

		return execute(new SoaCallback<Double>() {

			/*
			 * (non-Javadoc)
			 * 
			 * @see com.telus.cmb.common.dao.soa.spring.SoaCallback#doCallback()
			 */
			@Override
			public Double doCallback() throws Exception {

				SearchOutstandingAmount request = new SearchOutstandingAmount();
				request.setBillingAccountNum(String.valueOf(banId));
				request.setFromDate(fromDate);

				return dataUsageService.getTotalOutstandingAmountByAccount(request).getTotalAmount().doubleValue();
			}
		});
	}

	@Override
	public double getTotalUnbilledAmount(final int banId, final int billCycleYear, final int billCycleMonth, final int billCycle) throws ApplicationException {

		return execute(new SoaCallback<Double>() {

			/*
			 * (non-Javadoc)
			 * 
			 * @see com.telus.cmb.common.dao.soa.spring.SoaCallback#doCallback()
			 */
			@Override
			public Double doCallback() throws Exception {

				String cycleCode = billCycle < 10 ? "0" + billCycle : Integer.toString(billCycle);

				logger.debug("total unbilled data usage: input[ban=" + banId + ", year=" + billCycleYear + ", month=" + billCycleMonth + ", cycleCode=" + cycleCode + "]");

				SearchUnbilledAmount request = new SearchUnbilledAmount();

				request.setBillingAccountNum(Integer.toString(banId));
				request.setBillingCycleCd(cycleCode);
				request.setBillingCycleMonthNum(BigInteger.valueOf(billCycleMonth));
				request.setBillingCycleYearNum(BigInteger.valueOf(billCycleYear));

				BigDecimal response = dataUsageService.getTotalUnbilledAmountByAccount(request).getTotalAmount();

				logger.debug("total unbilled data response: " + response);

				return response.doubleValue();
			}
		});
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.telus.cmb.common.dao.soa.spring.SoaBaseSvcClient#ping()
	 */
	@Override
	public String ping() throws ApplicationException {

		return execute(new SoaCallback<String>() {

			@Override
			public String doCallback() throws Exception {
				return dataUsageService.ping(new Ping()).getVersion();
			}
		});
	}

}