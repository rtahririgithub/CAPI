package com.telus.provider.account;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import mockit.Mocked;
import mockit.NonStrictExpectations;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.telus.api.ApplicationException;
import com.telus.api.ClientAPI;
import com.telus.api.TelusAPIException;
import com.telus.api.UnknownObjectException;
import com.telus.api.account.InternationalServiceEligibilityCheckResult;
import com.telus.api.config1.Configuration;
import com.telus.api.reference.ApplicationSummary;
import com.telus.api.reference.Brand;
import com.telus.cmb.utility.configurationmanager.svc.ConfigurationManager;
import com.telus.eas.framework.exception.TelusException;
import com.telus.provider.TMProvider;
import com.telus.provider.eligibility.interservice.InternationalServiceEligibilityCheckCriteria;
import com.telus.provider.monitoring.MethodMonitorFlagReader;
import com.telus.provider.monitoring.aspectj.ProviderAspect;

/**
 * 
 * This class tests the checkInternationalServiceEligibility method in TMAccount.
 * It was created for the Scorpion and Penguin projects that added new values
 * for deposit, international dialling and international roaming rules added into
 * LDAP.  
 * <p/>
 * This class is not a true stand-alone unit test in that it is dependent on reading the 
 * entry from LDAP.  There is no sense in reproducing the same XML entry within in this
 * class otherwise any changes and fixes will have to be made both in the LDAP entry
 * and within this class leading to possible errors.
 * <p/>
 * This test requires JMockit to perform a partial mock and private method mocking.
 * <p/>
 * Open the javadoc view in Eclipse to see the following formatted table.
 * <p/>
 * <h1>Project Scorpion</h1>
<table class=MsoNormalTable border=0 cellspacing=0 cellpadding=0 width=980
 style='width:735.0pt;border-collapse:collapse;mso-yfti-tbllook:1184;
 mso-padding-alt:0cm 0cm 0cm 0cm'>
 <tr style='mso-yfti-irow:0;mso-yfti-firstrow:yes;height:48.0pt'>
  <td width=79 valign=top style='width:59.0pt;border:solid windowtext 1.0pt;
  border-right:solid black 1.0pt;background:#FCD5B4;padding:0cm 0cm 0cm 0cm;
  height:48.0pt'>
  <p class=MsoNormal><b><span style='font-size:10.0pt;color:black'><o:p>&nbsp;</o:p></span></b></p>
  </td>
  <td width=519 colspan=4 style='width:389.0pt;border-top:solid windowtext 1.0pt;
  border-left:none;border-bottom:solid windowtext 1.0pt;border-right:solid black 1.0pt;
  mso-border-left-alt:solid windowtext 1.0pt;background:#FCD5B4;padding:0cm 0cm 0cm 0cm;
  height:48.0pt'>
  <p class=MsoNormal><b><span style='font-size:10.0pt;color:black'>Inputs:&nbsp;<br>
  -Credit Class retrieved from HCD<br>
  -Account Tenure, Delinquency and Collection Treatment retrieved from KB&nbsp;</span></b><span
  style='font-size:10.0pt;font-family:"Arial","sans-serif";color:black'><o:p></o:p></span></p>
  </td>
  <td width=383 colspan=3 style='width:287.0pt;border-top:solid windowtext 1.0pt;
  border-left:none;border-bottom:solid windowtext 1.0pt;border-right:solid black 1.0pt;
  background:#B6DDE8;padding:0cm 0cm 0cm 0cm;height:48.0pt'>
  <p class=MsoNormal><b><span style='font-size:10.0pt;color:black'>Outputs:<br>
  - Configuration managed in LDAP</span></b><span style='font-size:10.0pt;
  font-family:"Arial","sans-serif";color:black'><o:p></o:p></span></p>
  </td>
 </tr>
 <tr style='mso-yfti-irow:1;height:31.5pt'>
  <td width=79 valign=top style='width:59.0pt;border:solid windowtext 1.0pt;
  border-top:none;background:#FCD5B4;padding:0cm 0cm 0cm 0cm;height:31.5pt'>
  <p class=MsoNormal><b><span style='font-size:10.0pt;color:black'>Test Case Id<o:p></o:p></span></b></p>
  </td>
  <td width=79 style='width:59.0pt;border-top:none;border-left:none;border-bottom:
  solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;mso-border-left-alt:
  solid windowtext 1.0pt;background:#FCD5B4;padding:0cm 0cm 0cm 0cm;height:
  31.5pt'>
  <p class=MsoNormal><b><span style='font-size:10.0pt;color:black'>Credit Class</span></b><span
  style='font-size:10.0pt;font-family:"Arial","sans-serif";color:black'><o:p></o:p></span></p>
  </td>
  <td width=103 style='width:77.0pt;border-top:none;border-left:none;
  border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  background:#FCD5B4;padding:0cm 0cm 0cm 0cm;height:31.5pt'>
  <p class=MsoNormal><b><span style='font-size:10.0pt;color:black'>New
  Activation</span></b><span style='font-size:10.0pt;font-family:"Arial","sans-serif";
  color:black'><o:p></o:p></span></p>
  </td>
  <td width=251 style='width:188.0pt;border-top:none;border-left:none;
  border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  background:#FCD5B4;padding:0cm 0cm 0cm 0cm;height:31.5pt'>
  <p class=MsoNormal><b><span style='font-size:10.0pt;color:black'>Collection
  Treatment (within the last 6 months) OR Account is Delinquent</span></b><span
  style='font-size:10.0pt;font-family:"Arial","sans-serif";color:black'><o:p></o:p></span></p>
  </td>
  <td width=87 style='width:65.0pt;border-top:none;border-left:none;border-bottom:
  solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;background:#FCD5B4;
  padding:0cm 0cm 0cm 0cm;height:31.5pt'>
  <p class=MsoNormal><b><span style='font-size:10.0pt;color:black'>Tenure</span></b><span
  style='font-size:10.0pt;font-family:"Arial","sans-serif";color:black'><o:p></o:p></span></p>
  </td>
  <td width=100 style='width:75.0pt;border-top:none;border-left:none;
  border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  background:#B6DDE8;padding:0cm 0cm 0cm 0cm;height:31.5pt'>
  <p class=MsoNormal><b><span style='font-size:10.0pt;color:black'>Deposit</span></b><span
  style='font-size:10.0pt;font-family:"Arial","sans-serif";color:black'><o:p></o:p></span></p>
  </td>
  <td width=136 style='width:102.0pt;border-top:none;border-left:none;
  border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  background:#B6DDE8;padding:0cm 0cm 0cm 0cm;height:31.5pt'>
  <p class=MsoNormal><b><span style='font-size:10.0pt;color:black'>Eligible for
  International Dialing</span></b><span style='font-size:10.0pt;font-family:
  "Arial","sans-serif";color:black'><o:p></o:p></span></p>
  </td>
  <td width=147 style='width:110.0pt;border-top:none;border-left:none;
  border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  background:#B6DDE8;padding:0cm 0cm 0cm 0cm;height:31.5pt'>
  <p class=MsoNormal><b><span style='font-size:10.0pt;color:black'>Eligible for
  International Roaming</span></b><span style='font-size:10.0pt;font-family:
  "Arial","sans-serif";color:black'><o:p></o:p></span></p>
  </td>
 </tr>
 <tr style='mso-yfti-irow:2;height:15.0pt'>
  <td valign=top style='border:solid windowtext 1.0pt;border-top:none;
  padding:0cm 0cm 0cm 0cm;height:15.0pt'>
  <p class=MsoNormal><span style='font-size:10.0pt;color:black'>1<o:p></o:p></span></p>
  </td>
  <td style='border-top:none;border-left:none;border-bottom:solid windowtext 1.0pt;
  border-right:solid windowtext 1.0pt;mso-border-left-alt:solid windowtext 1.0pt;
  padding:0cm 0cm 0cm 0cm;height:15.0pt'>
  <p class=MsoNormal><span style='font-size:10.0pt;color:black'>B</span><span
  style='font-size:10.0pt;font-family:"Arial","sans-serif";color:black'><o:p></o:p></span></p>
  </td>
  <td style='border-top:none;border-left:none;border-bottom:solid windowtext 1.0pt;
  border-right:solid windowtext 1.0pt;padding:0cm 0cm 0cm 0cm;height:15.0pt'>
  <p class=MsoNormal><span style='font-size:10.0pt;color:black'>Yes</span><span
  style='font-size:10.0pt;font-family:"Arial","sans-serif";color:black'><o:p></o:p></span></p>
  </td>
  <td style='border-top:none;border-left:none;border-bottom:solid windowtext 1.0pt;
  border-right:solid windowtext 1.0pt;padding:0cm 0cm 0cm 0cm;height:15.0pt'>
  <p class=MsoNormal><span style='font-size:10.0pt;color:black'>Not Checked</span><span
  style='font-size:10.0pt;font-family:"Arial","sans-serif";color:black'><o:p></o:p></span></p>
  </td>
  <td style='border-top:none;border-left:none;border-bottom:solid windowtext 1.0pt;
  border-right:solid windowtext 1.0pt;padding:0cm 0cm 0cm 0cm;height:15.0pt'>
  <p class=MsoNormal><span style='font-size:10.0pt;color:black'>Not Checked</span><span
  style='font-size:10.0pt;font-family:"Arial","sans-serif";color:black'><o:p></o:p></span></p>
  </td>
  <td style='border-top:none;border-left:none;border-bottom:solid windowtext 1.0pt;
  border-right:solid windowtext 1.0pt;padding:0cm 0cm 0cm 0cm;height:15.0pt'>
  <p class=MsoNormal><span style='font-size:10.0pt;color:black'>0</span><span
  style='font-size:10.0pt;font-family:"Arial","sans-serif";color:black'><o:p></o:p></span></p>
  </td>
  <td style='border-top:none;border-left:none;border-bottom:solid windowtext 1.0pt;
  border-right:solid windowtext 1.0pt;padding:0cm 0cm 0cm 0cm;height:15.0pt'>
  <p class=MsoNormal><span style='font-size:10.0pt;color:black'>Yes</span><span
  style='font-size:10.0pt;font-family:"Arial","sans-serif";color:black'><o:p></o:p></span></p>
  </td>
  <td style='border-top:none;border-left:none;border-bottom:solid windowtext 1.0pt;
  border-right:solid windowtext 1.0pt;padding:0cm 0cm 0cm 0cm;height:15.0pt'>
  <p class=MsoNormal><span style='font-size:10.0pt;color:black;mso-bidi-font-weight:
  bold'>No</span><span style='font-size:10.0pt;font-family:"Arial","sans-serif";
  color:black;mso-bidi-font-weight:bold'><o:p></o:p></span></p>
  </td>
 </tr>
 <tr style='mso-yfti-irow:3;height:15.0pt'>
  <td valign=top style='border:solid windowtext 1.0pt;border-top:none;
  padding:0cm 0cm 0cm 0cm;height:15.0pt'>
  <p class=MsoNormal><span style='font-size:10.0pt;color:black'>2<o:p></o:p></span></p>
  </td>
  <td style='border-top:none;border-left:none;border-bottom:solid windowtext 1.0pt;
  border-right:solid windowtext 1.0pt;mso-border-left-alt:solid windowtext 1.0pt;
  padding:0cm 0cm 0cm 0cm;height:15.0pt'>
  <p class=MsoNormal><span style='font-size:10.0pt;color:black'>B</span><span
  style='font-size:10.0pt;font-family:"Arial","sans-serif";color:black'><o:p></o:p></span></p>
  </td>
  <td style='border-top:none;border-left:none;border-bottom:solid windowtext 1.0pt;
  border-right:solid windowtext 1.0pt;padding:0cm 0cm 0cm 0cm;height:15.0pt'>
  <p class=MsoNormal><span style='font-size:10.0pt;color:black'>No</span><span
  style='font-size:10.0pt;font-family:"Arial","sans-serif";color:black'><o:p></o:p></span></p>
  </td>
  <td style='border-top:none;border-left:none;border-bottom:solid windowtext 1.0pt;
  border-right:solid windowtext 1.0pt;padding:0cm 0cm 0cm 0cm;height:15.0pt'>
  <p class=MsoNormal><span style='font-size:10.0pt;color:black'>Yes</span><span
  style='font-size:10.0pt;font-family:"Arial","sans-serif";color:black'><o:p></o:p></span></p>
  </td>
  <td style='border-top:none;border-left:none;border-bottom:solid windowtext 1.0pt;
  border-right:solid windowtext 1.0pt;padding:0cm 0cm 0cm 0cm;height:15.0pt'>
  <p class=MsoNormal><span style='font-size:10.0pt;color:black'>Not Checked</span><span
  style='font-size:10.0pt;font-family:"Arial","sans-serif";color:black'><o:p></o:p></span></p>
  </td>
  <td style='border-top:none;border-left:none;border-bottom:solid windowtext 1.0pt;
  border-right:solid windowtext 1.0pt;padding:0cm 0cm 0cm 0cm;height:15.0pt'>
  <p class=MsoNormal><span style='font-size:10.0pt;color:black'>0</span><span
  style='font-size:10.0pt;font-family:"Arial","sans-serif";color:black'><o:p></o:p></span></p>
  </td>
  <td style='border-top:none;border-left:none;border-bottom:solid windowtext 1.0pt;
  border-right:solid windowtext 1.0pt;padding:0cm 0cm 0cm 0cm;height:15.0pt'>
  <p class=MsoNormal><span style='font-size:10.0pt;color:black'>Yes</span><span
  style='font-size:10.0pt;font-family:"Arial","sans-serif";color:black'><o:p></o:p></span></p>
  </td>
  <td style='border-top:none;border-left:none;border-bottom:solid windowtext 1.0pt;
  border-right:solid windowtext 1.0pt;padding:0cm 0cm 0cm 0cm;height:15.0pt'>
  <p class=MsoNormal><span style='font-size:10.0pt;color:black'>No</span><span
  style='font-size:10.0pt;font-family:"Arial","sans-serif";color:black'><o:p></o:p></span></p>
  </td>
 </tr>
 <tr style='mso-yfti-irow:4;height:15.0pt'>
  <td valign=top style='border:solid windowtext 1.0pt;border-top:none;
  padding:0cm 0cm 0cm 0cm;height:15.0pt'>
  <p class=MsoNormal><span style='font-size:10.0pt;color:black'>3<o:p></o:p></span></p>
  </td>
  <td style='border-top:none;border-left:none;border-bottom:solid windowtext 1.0pt;
  border-right:solid windowtext 1.0pt;mso-border-left-alt:solid windowtext 1.0pt;
  padding:0cm 0cm 0cm 0cm;height:15.0pt'>
  <p class=MsoNormal><span style='font-size:10.0pt;color:black'>B</span><span
  style='font-size:10.0pt;font-family:"Arial","sans-serif";color:black'><o:p></o:p></span></p>
  </td>
  <td style='border-top:none;border-left:none;border-bottom:solid windowtext 1.0pt;
  border-right:solid windowtext 1.0pt;padding:0cm 0cm 0cm 0cm;height:15.0pt'>
  <p class=MsoNormal><span style='font-size:10.0pt;color:black'>No</span><span
  style='font-size:10.0pt;font-family:"Arial","sans-serif";color:black'><o:p></o:p></span></p>
  </td>
  <td style='border-top:none;border-left:none;border-bottom:solid windowtext 1.0pt;
  border-right:solid windowtext 1.0pt;padding:0cm 0cm 0cm 0cm;height:15.0pt'>
  <p class=MsoNormal><span style='font-size:10.0pt;color:black'>No</span><span
  style='font-size:10.0pt;font-family:"Arial","sans-serif";color:black'><o:p></o:p></span></p>
  </td>
  <td style='border-top:none;border-left:none;border-bottom:solid windowtext 1.0pt;
  border-right:solid windowtext 1.0pt;padding:0cm 0cm 0cm 0cm;height:15.0pt'>
  <p class=MsoNormal><span style='font-size:10.0pt;color:black'>Not Checked</span><span
  style='font-size:10.0pt;font-family:"Arial","sans-serif";color:black'><o:p></o:p></span></p>
  </td>
  <td style='border-top:none;border-left:none;border-bottom:solid windowtext 1.0pt;
  border-right:solid windowtext 1.0pt;padding:0cm 0cm 0cm 0cm;height:15.0pt'>
  <p class=MsoNormal><span style='font-size:10.0pt;color:black'>0</span><span
  style='font-size:10.0pt;font-family:"Arial","sans-serif";color:black'><o:p></o:p></span></p>
  </td>
  <td style='border-top:none;border-left:none;border-bottom:solid windowtext 1.0pt;
  border-right:solid windowtext 1.0pt;padding:0cm 0cm 0cm 0cm;height:15.0pt'>
  <p class=MsoNormal><span style='font-size:10.0pt;color:black'>Yes</span><span
  style='font-size:10.0pt;font-family:"Arial","sans-serif";color:black'><o:p></o:p></span></p>
  </td>
  <td style='border-top:none;border-left:none;border-bottom:solid windowtext 1.0pt;
  border-right:solid windowtext 1.0pt;padding:0cm 0cm 0cm 0cm;height:15.0pt'>
  <p class=MsoNormal><span style='font-size:10.0pt;color:black'>Yes</span><span
  style='font-size:10.0pt;font-family:"Arial","sans-serif";color:black'><o:p></o:p></span></p>
  </td>
 </tr>
 <tr style='mso-yfti-irow:5;height:15.0pt'>
  <td valign=top style='border:solid windowtext 1.0pt;border-top:none;
  padding:0cm 0cm 0cm 0cm;height:15.0pt'>
  <p class=MsoNormal><span style='font-size:10.0pt;color:black'>4<o:p></o:p></span></p>
  </td>
  <td style='border-top:none;border-left:none;border-bottom:solid windowtext 1.0pt;
  border-right:solid windowtext 1.0pt;mso-border-left-alt:solid windowtext 1.0pt;
  padding:0cm 0cm 0cm 0cm;height:15.0pt'>
  <p class=MsoNormal><span style='font-size:10.0pt;color:black'>D</span><span
  style='font-size:10.0pt;font-family:"Arial","sans-serif";color:black'><o:p></o:p></span></p>
  </td>
  <td style='border-top:none;border-left:none;border-bottom:solid windowtext 1.0pt;
  border-right:solid windowtext 1.0pt;padding:0cm 0cm 0cm 0cm;height:15.0pt'>
  <p class=MsoNormal><span style='font-size:10.0pt;color:black'>Yes</span><span
  style='font-size:10.0pt;font-family:"Arial","sans-serif";color:black'><o:p></o:p></span></p>
  </td>
  <td style='border-top:none;border-left:none;border-bottom:solid windowtext 1.0pt;
  border-right:solid windowtext 1.0pt;padding:0cm 0cm 0cm 0cm;height:15.0pt'>
  <p class=MsoNormal><span style='font-size:10.0pt;color:black'>Not Checked</span><span
  style='font-size:10.0pt;font-family:"Arial","sans-serif";color:black'><o:p></o:p></span></p>
  </td>
  <td style='border-top:none;border-left:none;border-bottom:solid windowtext 1.0pt;
  border-right:solid windowtext 1.0pt;padding:0cm 0cm 0cm 0cm;height:15.0pt'>
  <p class=MsoNormal><span style='font-size:10.0pt;color:black'>Not Checked</span><span
  style='font-size:10.0pt;font-family:"Arial","sans-serif";color:black'><o:p></o:p></span></p>
  </td>
  <td style='border-top:none;border-left:none;border-bottom:solid windowtext 1.0pt;
  border-right:solid windowtext 1.0pt;padding:0cm 0cm 0cm 0cm;height:15.0pt'>
  <p class=MsoNormal><span style='font-size:10.0pt;color:black'>0</span><span
  style='font-size:10.0pt;font-family:"Arial","sans-serif";color:black'><o:p></o:p></span></p>
  </td>
  <td style='border-top:none;border-left:none;border-bottom:solid windowtext 1.0pt;
  border-right:solid windowtext 1.0pt;padding:0cm 0cm 0cm 0cm;height:15.0pt'>
  <p class=MsoNormal><span style='font-size:10.0pt;color:black'>Yes</span><span
  style='font-size:10.0pt;font-family:"Arial","sans-serif";color:black'><o:p></o:p></span></p>
  </td>
  <td style='border-top:none;border-left:none;border-bottom:solid windowtext 1.0pt;
  border-right:solid windowtext 1.0pt;padding:0cm 0cm 0cm 0cm;height:15.0pt'>
  <p class=MsoNormal><span style='font-size:10.0pt;color:black;mso-bidi-font-weight:
  bold'>No</span><span style='font-size:10.0pt;font-family:"Arial","sans-serif";
  color:black;mso-bidi-font-weight:bold'><o:p></o:p></span></p>
  </td>
 </tr>
 <tr style='mso-yfti-irow:6;height:15.0pt'>
  <td valign=top style='border:solid windowtext 1.0pt;border-top:none;
  padding:0cm 0cm 0cm 0cm;height:15.0pt'>
  <p class=MsoNormal><span style='font-size:10.0pt;color:black'>5<o:p></o:p></span></p>
  </td>
  <td style='border-top:none;border-left:none;border-bottom:solid windowtext 1.0pt;
  border-right:solid windowtext 1.0pt;mso-border-left-alt:solid windowtext 1.0pt;
  padding:0cm 0cm 0cm 0cm;height:15.0pt'>
  <p class=MsoNormal><span style='font-size:10.0pt;color:black'>D</span><span
  style='font-size:10.0pt;font-family:"Arial","sans-serif";color:black'><o:p></o:p></span></p>
  </td>
  <td style='border-top:none;border-left:none;border-bottom:solid windowtext 1.0pt;
  border-right:solid windowtext 1.0pt;padding:0cm 0cm 0cm 0cm;height:15.0pt'>
  <p class=MsoNormal><span style='font-size:10.0pt;color:black'>No</span><span
  style='font-size:10.0pt;font-family:"Arial","sans-serif";color:black'><o:p></o:p></span></p>
  </td>
  <td style='border-top:none;border-left:none;border-bottom:solid windowtext 1.0pt;
  border-right:solid windowtext 1.0pt;padding:0cm 0cm 0cm 0cm;height:15.0pt'>
  <p class=MsoNormal><span style='font-size:10.0pt;color:black'>Yes</span><span
  style='font-size:10.0pt;font-family:"Arial","sans-serif";color:black'><o:p></o:p></span></p>
  </td>
  <td style='border-top:none;border-left:none;border-bottom:solid windowtext 1.0pt;
  border-right:solid windowtext 1.0pt;padding:0cm 0cm 0cm 0cm;height:15.0pt'>
  <p class=MsoNormal><span style='font-size:10.0pt;color:black'>Not Checked</span><span
  style='font-size:10.0pt;font-family:"Arial","sans-serif";color:black'><o:p></o:p></span></p>
  </td>
  <td style='border-top:none;border-left:none;border-bottom:solid windowtext 1.0pt;
  border-right:solid windowtext 1.0pt;padding:0cm 0cm 0cm 0cm;height:15.0pt'>
  <p class=MsoNormal><span style='font-size:10.0pt;color:black'>0</span><span
  style='font-size:10.0pt;font-family:"Arial","sans-serif";color:black'><o:p></o:p></span></p>
  </td>
  <td style='border-top:none;border-left:none;border-bottom:solid windowtext 1.0pt;
  border-right:solid windowtext 1.0pt;padding:0cm 0cm 0cm 0cm;height:15.0pt'>
  <p class=MsoNormal><span style='font-size:10.0pt;color:black'>Yes</span><span
  style='font-size:10.0pt;font-family:"Arial","sans-serif";color:black'><o:p></o:p></span></p>
  </td>
  <td style='border-top:none;border-left:none;border-bottom:solid windowtext 1.0pt;
  border-right:solid windowtext 1.0pt;padding:0cm 0cm 0cm 0cm;height:15.0pt'>
  <p class=MsoNormal><span style='font-size:10.0pt;color:black'>No</span><span
  style='font-size:10.0pt;font-family:"Arial","sans-serif";color:black'><o:p></o:p></span></p>
  </td>
 </tr>
 <tr style='mso-yfti-irow:7;height:15.0pt'>
  <td valign=top style='border:solid windowtext 1.0pt;border-top:none;
  padding:0cm 0cm 0cm 0cm;height:15.0pt'>
  <p class=MsoNormal><span style='font-size:10.0pt;color:black'>6<o:p></o:p></span></p>
  </td>
  <td style='border-top:none;border-left:none;border-bottom:solid windowtext 1.0pt;
  border-right:solid windowtext 1.0pt;mso-border-left-alt:solid windowtext 1.0pt;
  padding:0cm 0cm 0cm 0cm;height:15.0pt'>
  <p class=MsoNormal><span style='font-size:10.0pt;color:black'>D</span><span
  style='font-size:10.0pt;font-family:"Arial","sans-serif";color:black'><o:p></o:p></span></p>
  </td>
  <td style='border-top:none;border-left:none;border-bottom:solid windowtext 1.0pt;
  border-right:solid windowtext 1.0pt;padding:0cm 0cm 0cm 0cm;height:15.0pt'>
  <p class=MsoNormal><span style='font-size:10.0pt;color:black'>No</span><span
  style='font-size:10.0pt;font-family:"Arial","sans-serif";color:black'><o:p></o:p></span></p>
  </td>
  <td style='border-top:none;border-left:none;border-bottom:solid windowtext 1.0pt;
  border-right:solid windowtext 1.0pt;padding:0cm 0cm 0cm 0cm;height:15.0pt'>
  <p class=MsoNormal><span style='font-size:10.0pt;color:black'>No</span><span
  style='font-size:10.0pt;font-family:"Arial","sans-serif";color:black'><o:p></o:p></span></p>
  </td>
  <td style='border-top:none;border-left:none;border-bottom:solid windowtext 1.0pt;
  border-right:solid windowtext 1.0pt;padding:0cm 0cm 0cm 0cm;height:15.0pt'>
  <p class=MsoNormal><span style='font-size:10.0pt;color:black'>Not Checked</span><span
  style='font-size:10.0pt;font-family:"Arial","sans-serif";color:black'><o:p></o:p></span></p>
  </td>
  <td style='border-top:none;border-left:none;border-bottom:solid windowtext 1.0pt;
  border-right:solid windowtext 1.0pt;padding:0cm 0cm 0cm 0cm;height:15.0pt'>
  <p class=MsoNormal><span style='font-size:10.0pt;color:black'>0</span><span
  style='font-size:10.0pt;font-family:"Arial","sans-serif";color:black'><o:p></o:p></span></p>
  </td>
  <td style='border-top:none;border-left:none;border-bottom:solid windowtext 1.0pt;
  border-right:solid windowtext 1.0pt;padding:0cm 0cm 0cm 0cm;height:15.0pt'>
  <p class=MsoNormal><span style='font-size:10.0pt;color:black'>Yes</span><span
  style='font-size:10.0pt;font-family:"Arial","sans-serif";color:black'><o:p></o:p></span></p>
  </td>
  <td style='border-top:none;border-left:none;border-bottom:solid windowtext 1.0pt;
  border-right:solid windowtext 1.0pt;padding:0cm 0cm 0cm 0cm;height:15.0pt'>
  <p class=MsoNormal><span style='font-size:10.0pt;color:black'>Yes</span><span
  style='font-size:10.0pt;font-family:"Arial","sans-serif";color:black'><o:p></o:p></span></p>
  </td>
 </tr>
 <tr style='mso-yfti-irow:8;height:15.0pt'>
  <td valign=top style='border:solid windowtext 1.0pt;border-top:none;
  padding:0cm 0cm 0cm 0cm;height:15.0pt'>
  <p class=MsoNormal><span style='font-size:10.0pt;color:black'>7<o:p></o:p></span></p>
  </td>
  <td style='border-top:none;border-left:none;border-bottom:solid windowtext 1.0pt;
  border-right:solid windowtext 1.0pt;mso-border-left-alt:solid windowtext 1.0pt;
  padding:0cm 0cm 0cm 0cm;height:15.0pt'>
  <p class=MsoNormal><span style='font-size:10.0pt;color:black'>C</span><span
  style='font-size:10.0pt;font-family:"Arial","sans-serif";color:black'><o:p></o:p></span></p>
  </td>
  <td style='border-top:none;border-left:none;border-bottom:solid windowtext 1.0pt;
  border-right:solid windowtext 1.0pt;padding:0cm 0cm 0cm 0cm;height:15.0pt'>
  <p class=MsoNormal><span style='font-size:10.0pt;color:black'>Yes</span><span
  style='font-size:10.0pt;font-family:"Arial","sans-serif";color:black'><o:p></o:p></span></p>
  </td>
  <td style='border-top:none;border-left:none;border-bottom:solid windowtext 1.0pt;
  border-right:solid windowtext 1.0pt;padding:0cm 0cm 0cm 0cm;height:15.0pt'>
  <p class=MsoNormal><span style='font-size:10.0pt;color:black'>Not Checked</span><span
  style='font-size:10.0pt;font-family:"Arial","sans-serif";color:black'><o:p></o:p></span></p>
  </td>
  <td style='border-top:none;border-left:none;border-bottom:solid windowtext 1.0pt;
  border-right:solid windowtext 1.0pt;padding:0cm 0cm 0cm 0cm;height:15.0pt'>
  <p class=MsoNormal><span style='font-size:10.0pt;color:black'>Not Checked</span><span
  style='font-size:10.0pt;font-family:"Arial","sans-serif";color:black'><o:p></o:p></span></p>
  </td>
  <td style='border-top:none;border-left:none;border-bottom:solid windowtext 1.0pt;
  border-right:solid windowtext 1.0pt;padding:0cm 0cm 0cm 0cm;height:15.0pt'>
  <p class=MsoNormal><span style='font-size:10.0pt;color:black'>0</span><span
  style='font-size:10.0pt;font-family:"Arial","sans-serif";color:black'><o:p></o:p></span></p>
  </td>
  <td style='border-top:none;border-left:none;border-bottom:solid windowtext 1.0pt;
  border-right:solid windowtext 1.0pt;padding:0cm 0cm 0cm 0cm;height:15.0pt'>
  <p class=MsoNormal><span style='font-size:10.0pt;color:black'>Yes</span><span
  style='font-size:10.0pt;font-family:"Arial","sans-serif";color:black'><o:p></o:p></span></p>
  </td>
  <td style='border-top:none;border-left:none;border-bottom:solid windowtext 1.0pt;
  border-right:solid windowtext 1.0pt;padding:0cm 0cm 0cm 0cm;height:15.0pt'>
  <p class=MsoNormal><span style='font-size:10.0pt;color:black'>No</span><span
  style='font-size:10.0pt;font-family:"Arial","sans-serif";color:black'><o:p></o:p></span></p>
  </td>
 </tr>
 <tr style='mso-yfti-irow:9;height:15.0pt'>
  <td valign=top style='border:solid windowtext 1.0pt;border-top:none;
  padding:0cm 0cm 0cm 0cm;height:15.0pt'>
  <p class=MsoNormal><span style='font-size:10.0pt;color:black'>8<o:p></o:p></span></p>
  </td>
  <td style='border-top:none;border-left:none;border-bottom:solid windowtext 1.0pt;
  border-right:solid windowtext 1.0pt;mso-border-left-alt:solid windowtext 1.0pt;
  padding:0cm 0cm 0cm 0cm;height:15.0pt'>
  <p class=MsoNormal><span style='font-size:10.0pt;color:black'>C</span><span
  style='font-size:10.0pt;font-family:"Arial","sans-serif";color:black'><o:p></o:p></span></p>
  </td>
  <td style='border-top:none;border-left:none;border-bottom:solid windowtext 1.0pt;
  border-right:solid windowtext 1.0pt;padding:0cm 0cm 0cm 0cm;height:15.0pt'>
  <p class=MsoNormal><span style='font-size:10.0pt;color:black'>No</span><span
  style='font-size:10.0pt;font-family:"Arial","sans-serif";color:black'><o:p></o:p></span></p>
  </td>
  <td style='border-top:none;border-left:none;border-bottom:solid windowtext 1.0pt;
  border-right:solid windowtext 1.0pt;padding:0cm 0cm 0cm 0cm;height:15.0pt'>
  <p class=MsoNormal><span style='font-size:10.0pt;color:black'>Yes</span><span
  style='font-size:10.0pt;font-family:"Arial","sans-serif";color:black'><o:p></o:p></span></p>
  </td>
  <td style='border-top:none;border-left:none;border-bottom:solid windowtext 1.0pt;
  border-right:solid windowtext 1.0pt;padding:0cm 0cm 0cm 0cm;height:15.0pt'>
  <p class=MsoNormal><span style='font-size:10.0pt;color:black'>&lt;= 3 months</span><span
  style='font-size:10.0pt;font-family:"Arial","sans-serif";color:black'><o:p></o:p></span></p>
  </td>
  <td style='border-top:none;border-left:none;border-bottom:solid windowtext 1.0pt;
  border-right:solid windowtext 1.0pt;padding:0cm 0cm 0cm 0cm;height:15.0pt'>
  <p class=MsoNormal><span style='font-size:10.0pt;color:black'>0</span><span
  style='font-size:10.0pt;font-family:"Arial","sans-serif";color:black'><o:p></o:p></span></p>
  </td>
  <td style='border-top:none;border-left:none;border-bottom:solid windowtext 1.0pt;
  border-right:solid windowtext 1.0pt;padding:0cm 0cm 0cm 0cm;height:15.0pt'>
  <p class=MsoNormal><span style='font-size:10.0pt;color:black'>Yes</span><span
  style='font-size:10.0pt;font-family:"Arial","sans-serif";color:black'><o:p></o:p></span></p>
  </td>
  <td style='border-top:none;border-left:none;border-bottom:solid windowtext 1.0pt;
  border-right:solid windowtext 1.0pt;padding:0cm 0cm 0cm 0cm;height:15.0pt'>
  <p class=MsoNormal><span style='font-size:10.0pt;color:black'>No</span><span
  style='font-size:10.0pt;font-family:"Arial","sans-serif";color:black'><o:p></o:p></span></p>
  </td>
 </tr>
 <tr style='mso-yfti-irow:10;height:15.0pt'>
  <td valign=top style='border:solid windowtext 1.0pt;border-top:none;
  padding:0cm 0cm 0cm 0cm;height:15.0pt'>
  <p class=MsoNormal><span style='font-size:10.0pt;color:black'>9<o:p></o:p></span></p>
  </td>
  <td style='border-top:none;border-left:none;border-bottom:solid windowtext 1.0pt;
  border-right:solid windowtext 1.0pt;mso-border-left-alt:solid windowtext 1.0pt;
  padding:0cm 0cm 0cm 0cm;height:15.0pt'>
  <p class=MsoNormal><span style='font-size:10.0pt;color:black'>C</span><span
  style='font-size:10.0pt;font-family:"Arial","sans-serif";color:black'><o:p></o:p></span></p>
  </td>
  <td style='border-top:none;border-left:none;border-bottom:solid windowtext 1.0pt;
  border-right:solid windowtext 1.0pt;padding:0cm 0cm 0cm 0cm;height:15.0pt'>
  <p class=MsoNormal><span style='font-size:10.0pt;color:black'>No</span><span
  style='font-size:10.0pt;font-family:"Arial","sans-serif";color:black'><o:p></o:p></span></p>
  </td>
  <td style='border-top:none;border-left:none;border-bottom:solid windowtext 1.0pt;
  border-right:solid windowtext 1.0pt;padding:0cm 0cm 0cm 0cm;height:15.0pt'>
  <p class=MsoNormal><span style='font-size:10.0pt;color:black'>Yes</span><span
  style='font-size:10.0pt;font-family:"Arial","sans-serif";color:black'><o:p></o:p></span></p>
  </td>
  <td style='border-top:none;border-left:none;border-bottom:solid windowtext 1.0pt;
  border-right:solid windowtext 1.0pt;padding:0cm 0cm 0cm 0cm;height:15.0pt'>
  <p class=MsoNormal><span style='font-size:10.0pt;color:black'>&gt; 3 months</span><span
  style='font-size:10.0pt;font-family:"Arial","sans-serif";color:black'><o:p></o:p></span></p>
  </td>
  <td style='border-top:none;border-left:none;border-bottom:solid windowtext 1.0pt;
  border-right:solid windowtext 1.0pt;padding:0cm 0cm 0cm 0cm;height:15.0pt'>
  <p class=MsoNormal><span style='font-size:10.0pt;color:black'>0</span><span
  style='font-size:10.0pt;font-family:"Arial","sans-serif";color:black'><o:p></o:p></span></p>
  </td>
  <td style='border-top:none;border-left:none;border-bottom:solid windowtext 1.0pt;
  border-right:solid windowtext 1.0pt;padding:0cm 0cm 0cm 0cm;height:15.0pt'>
  <p class=MsoNormal><span style='font-size:10.0pt;color:black'>Yes</span><span
  style='font-size:10.0pt;font-family:"Arial","sans-serif";color:black'><o:p></o:p></span></p>
  </td>
  <td style='border-top:none;border-left:none;border-bottom:solid windowtext 1.0pt;
  border-right:solid windowtext 1.0pt;padding:0cm 0cm 0cm 0cm;height:15.0pt'>
  <p class=MsoNormal><span style='font-size:10.0pt;color:black'>No</span><span
  style='font-size:10.0pt;font-family:"Arial","sans-serif";color:black'><o:p></o:p></span></p>
  </td>
 </tr>
 <tr style='mso-yfti-irow:11;height:15.0pt'>
  <td valign=top style='border:solid windowtext 1.0pt;border-top:none;
  padding:0cm 0cm 0cm 0cm;height:15.0pt'>
  <p class=MsoNormal><span style='font-size:10.0pt;color:black'>10<o:p></o:p></span></p>
  </td>
  <td style='border-top:none;border-left:none;border-bottom:solid windowtext 1.0pt;
  border-right:solid windowtext 1.0pt;mso-border-left-alt:solid windowtext 1.0pt;
  padding:0cm 0cm 0cm 0cm;height:15.0pt'>
  <p class=MsoNormal><span style='font-size:10.0pt;color:black'>C</span><span
  style='font-size:10.0pt;font-family:"Arial","sans-serif";color:black'><o:p></o:p></span></p>
  </td>
  <td style='border-top:none;border-left:none;border-bottom:solid windowtext 1.0pt;
  border-right:solid windowtext 1.0pt;padding:0cm 0cm 0cm 0cm;height:15.0pt'>
  <p class=MsoNormal><span style='font-size:10.0pt;color:black'>No</span><span
  style='font-size:10.0pt;font-family:"Arial","sans-serif";color:black'><o:p></o:p></span></p>
  </td>
  <td style='border-top:none;border-left:none;border-bottom:solid windowtext 1.0pt;
  border-right:solid windowtext 1.0pt;padding:0cm 0cm 0cm 0cm;height:15.0pt'>
  <p class=MsoNormal><span style='font-size:10.0pt;color:black'>No</span><span
  style='font-size:10.0pt;font-family:"Arial","sans-serif";color:black'><o:p></o:p></span></p>
  </td>
  <td style='border-top:none;border-left:none;border-bottom:solid windowtext 1.0pt;
  border-right:solid windowtext 1.0pt;padding:0cm 0cm 0cm 0cm;height:15.0pt'>
  <p class=MsoNormal><span style='font-size:10.0pt;color:black'>&lt;= 3 months</span><span
  style='font-size:10.0pt;font-family:"Arial","sans-serif";color:black'><o:p></o:p></span></p>
  </td>
  <td style='border-top:none;border-left:none;border-bottom:solid windowtext 1.0pt;
  border-right:solid windowtext 1.0pt;padding:0cm 0cm 0cm 0cm;height:15.0pt'>
  <p class=MsoNormal><span style='font-size:10.0pt;color:black'>0</span><span
  style='font-size:10.0pt;font-family:"Arial","sans-serif";color:black'><o:p></o:p></span></p>
  </td>
  <td style='border-top:none;border-left:none;border-bottom:solid windowtext 1.0pt;
  border-right:solid windowtext 1.0pt;padding:0cm 0cm 0cm 0cm;height:15.0pt'>
  <p class=MsoNormal><span style='font-size:10.0pt;color:black'>Yes</span><span
  style='font-size:10.0pt;font-family:"Arial","sans-serif";color:black'><o:p></o:p></span></p>
  </td>
  <td style='border-top:none;border-left:none;border-bottom:solid windowtext 1.0pt;
  border-right:solid windowtext 1.0pt;padding:0cm 0cm 0cm 0cm;height:15.0pt'>
  <p class=MsoNormal><span style='font-size:10.0pt;color:black'>No</span><span
  style='font-size:10.0pt;font-family:"Arial","sans-serif";color:black'><o:p></o:p></span></p>
  </td>
 </tr>
 <tr style='mso-yfti-irow:12;height:15.0pt'>
  <td valign=top style='border:solid windowtext 1.0pt;border-top:none;
  padding:0cm 0cm 0cm 0cm;height:15.0pt'>
  <p class=MsoNormal><span style='font-size:10.0pt;color:black'>11<o:p></o:p></span></p>
  </td>
  <td style='border-top:none;border-left:none;border-bottom:solid windowtext 1.0pt;
  border-right:solid windowtext 1.0pt;mso-border-left-alt:solid windowtext 1.0pt;
  padding:0cm 0cm 0cm 0cm;height:15.0pt'>
  <p class=MsoNormal><span style='font-size:10.0pt;color:black'>C</span><span
  style='font-size:10.0pt;font-family:"Arial","sans-serif";color:black'><o:p></o:p></span></p>
  </td>
  <td style='border-top:none;border-left:none;border-bottom:solid windowtext 1.0pt;
  border-right:solid windowtext 1.0pt;padding:0cm 0cm 0cm 0cm;height:15.0pt'>
  <p class=MsoNormal><span style='font-size:10.0pt;color:black'>No</span><span
  style='font-size:10.0pt;font-family:"Arial","sans-serif";color:black'><o:p></o:p></span></p>
  </td>
  <td style='border-top:none;border-left:none;border-bottom:solid windowtext 1.0pt;
  border-right:solid windowtext 1.0pt;padding:0cm 0cm 0cm 0cm;height:15.0pt'>
  <p class=MsoNormal><span style='font-size:10.0pt;color:black'>No</span><span
  style='font-size:10.0pt;font-family:"Arial","sans-serif";color:black'><o:p></o:p></span></p>
  </td>
  <td style='border-top:none;border-left:none;border-bottom:solid windowtext 1.0pt;
  border-right:solid windowtext 1.0pt;padding:0cm 0cm 0cm 0cm;height:15.0pt'>
  <p class=MsoNormal><span style='font-size:10.0pt;color:black'>&gt; 3 months</span><span
  style='font-size:10.0pt;font-family:"Arial","sans-serif";color:black'><o:p></o:p></span></p>
  </td>
  <td style='border-top:none;border-left:none;border-bottom:solid windowtext 1.0pt;
  border-right:solid windowtext 1.0pt;padding:0cm 0cm 0cm 0cm;height:15.0pt'>
  <p class=MsoNormal><span style='font-size:10.0pt;color:black'>0</span><span
  style='font-size:10.0pt;font-family:"Arial","sans-serif";color:black'><o:p></o:p></span></p>
  </td>
  <td style='border-top:none;border-left:none;border-bottom:solid windowtext 1.0pt;
  border-right:solid windowtext 1.0pt;padding:0cm 0cm 0cm 0cm;height:15.0pt'>
  <p class=MsoNormal><span style='font-size:10.0pt;color:black'>Yes</span><span
  style='font-size:10.0pt;font-family:"Arial","sans-serif";color:black'><o:p></o:p></span></p>
  </td>
  <td style='border-top:none;border-left:none;border-bottom:solid windowtext 1.0pt;
  border-right:solid windowtext 1.0pt;padding:0cm 0cm 0cm 0cm;height:15.0pt'>
  <p class=MsoNormal><span style='font-size:10.0pt;color:black'>Yes</span><span
  style='font-size:10.0pt;font-family:"Arial","sans-serif";color:black'><o:p></o:p></span></p>
  </td>
 </tr>
 <tr style='mso-yfti-irow:13;height:15.0pt'>
  <td valign=top style='border:solid windowtext 1.0pt;border-top:none;
  padding:0cm 0cm 0cm 0cm;height:15.0pt'>
  <p class=MsoNormal><span style='font-size:10.0pt;color:black'>12<o:p></o:p></span></p>
  </td>
  <td style='border-top:none;border-left:none;border-bottom:solid windowtext 1.0pt;
  border-right:solid windowtext 1.0pt;mso-border-left-alt:solid windowtext 1.0pt;
  padding:0cm 0cm 0cm 0cm;height:15.0pt'>
  <p class=MsoNormal><span style='font-size:10.0pt;color:black'>X</span><span
  style='font-size:10.0pt;font-family:"Arial","sans-serif";color:black'><o:p></o:p></span></p>
  </td>
  <td style='border-top:none;border-left:none;border-bottom:solid windowtext 1.0pt;
  border-right:solid windowtext 1.0pt;padding:0cm 0cm 0cm 0cm;height:15.0pt'>
  <p class=MsoNormal><span style='font-size:10.0pt;color:black'>Yes</span><span
  style='font-size:10.0pt;font-family:"Arial","sans-serif";color:black'><o:p></o:p></span></p>
  </td>
  <td style='border-top:none;border-left:none;border-bottom:solid windowtext 1.0pt;
  border-right:solid windowtext 1.0pt;padding:0cm 0cm 0cm 0cm;height:15.0pt'>
  <p class=MsoNormal><span style='font-size:10.0pt;color:black'>Not Checked</span><span
  style='font-size:10.0pt;font-family:"Arial","sans-serif";color:black'><o:p></o:p></span></p>
  </td>
  <td style='border-top:none;border-left:none;border-bottom:solid windowtext 1.0pt;
  border-right:solid windowtext 1.0pt;padding:0cm 0cm 0cm 0cm;height:15.0pt'>
  <p class=MsoNormal><span style='font-size:10.0pt;color:black'>Not Checked</span><span
  style='font-size:10.0pt;font-family:"Arial","sans-serif";color:black'><o:p></o:p></span></p>
  </td>
  <td style='border-top:none;border-left:none;border-bottom:solid windowtext 1.0pt;
  border-right:solid windowtext 1.0pt;padding:0cm 0cm 0cm 0cm;height:15.0pt'>
  <p class=MsoNormal><span style='font-size:10.0pt;color:black'>0</span><span
  style='font-size:10.0pt;font-family:"Arial","sans-serif";color:black'><o:p></o:p></span></p>
  </td>
  <td style='border-top:none;border-left:none;border-bottom:solid windowtext 1.0pt;
  border-right:solid windowtext 1.0pt;padding:0cm 0cm 0cm 0cm;height:15.0pt'>
  <p class=MsoNormal><span style='font-size:10.0pt;color:black'>Yes</span><span
  style='font-size:10.0pt;font-family:"Arial","sans-serif";color:black'><o:p></o:p></span></p>
  </td>
  <td style='border-top:none;border-left:none;border-bottom:solid windowtext 1.0pt;
  border-right:solid windowtext 1.0pt;padding:0cm 0cm 0cm 0cm;height:15.0pt'>
  <p class=MsoNormal><span style='font-size:10.0pt;color:black'>No</span><span
  style='font-size:10.0pt;font-family:"Arial","sans-serif";color:black'><o:p></o:p></span></p>
  </td>
 </tr>
 <tr style='mso-yfti-irow:14;height:15.0pt'>
  <td valign=top style='border:solid windowtext 1.0pt;border-top:none;
  padding:0cm 0cm 0cm 0cm;height:15.0pt'>
  <p class=MsoNormal><span style='font-size:10.0pt;color:black'>13<o:p></o:p></span></p>
  </td>
  <td style='border-top:none;border-left:none;border-bottom:solid windowtext 1.0pt;
  border-right:solid windowtext 1.0pt;mso-border-left-alt:solid windowtext 1.0pt;
  padding:0cm 0cm 0cm 0cm;height:15.0pt'>
  <p class=MsoNormal><span style='font-size:10.0pt;color:black'>X</span><span
  style='font-size:10.0pt;font-family:"Arial","sans-serif";color:black'><o:p></o:p></span></p>
  </td>
  <td style='border-top:none;border-left:none;border-bottom:solid windowtext 1.0pt;
  border-right:solid windowtext 1.0pt;padding:0cm 0cm 0cm 0cm;height:15.0pt'>
  <p class=MsoNormal><span style='font-size:10.0pt;color:black'>No</span><span
  style='font-size:10.0pt;font-family:"Arial","sans-serif";color:black'><o:p></o:p></span></p>
  </td>
  <td style='border-top:none;border-left:none;border-bottom:solid windowtext 1.0pt;
  border-right:solid windowtext 1.0pt;padding:0cm 0cm 0cm 0cm;height:15.0pt'>
  <p class=MsoNormal><span style='font-size:10.0pt;color:black'>Yes</span><span
  style='font-size:10.0pt;font-family:"Arial","sans-serif";color:black'><o:p></o:p></span></p>
  </td>
  <td style='border-top:none;border-left:none;border-bottom:solid windowtext 1.0pt;
  border-right:solid windowtext 1.0pt;padding:0cm 0cm 0cm 0cm;height:15.0pt'>
  <p class=MsoNormal><span style='font-size:10.0pt;color:black'>&lt;= 6 months</span><span
  style='font-size:10.0pt;font-family:"Arial","sans-serif";color:black'><o:p></o:p></span></p>
  </td>
  <td style='border-top:none;border-left:none;border-bottom:solid windowtext 1.0pt;
  border-right:solid windowtext 1.0pt;padding:0cm 0cm 0cm 0cm;height:15.0pt'>
  <p class=MsoNormal><span style='font-size:10.0pt;color:black'>0</span><span
  style='font-size:10.0pt;font-family:"Arial","sans-serif";color:black'><o:p></o:p></span></p>
  </td>
  <td style='border-top:none;border-left:none;border-bottom:solid windowtext 1.0pt;
  border-right:solid windowtext 1.0pt;padding:0cm 0cm 0cm 0cm;height:15.0pt'>
  <p class=MsoNormal><span style='font-size:10.0pt;color:black'>Yes</span><span
  style='font-size:10.0pt;font-family:"Arial","sans-serif";color:black'><o:p></o:p></span></p>
  </td>
  <td style='border-top:none;border-left:none;border-bottom:solid windowtext 1.0pt;
  border-right:solid windowtext 1.0pt;padding:0cm 0cm 0cm 0cm;height:15.0pt'>
  <p class=MsoNormal><span style='font-size:10.0pt;color:black'>No</span><span
  style='font-size:10.0pt;font-family:"Arial","sans-serif";color:black'><o:p></o:p></span></p>
  </td>
 </tr>
 <tr style='mso-yfti-irow:15;height:15.0pt'>
  <td valign=top style='border:solid windowtext 1.0pt;border-top:none;
  padding:0cm 0cm 0cm 0cm;height:15.0pt'>
  <p class=MsoNormal><span style='font-size:10.0pt;color:black'>14<o:p></o:p></span></p>
  </td>
  <td style='border-top:none;border-left:none;border-bottom:solid windowtext 1.0pt;
  border-right:solid windowtext 1.0pt;mso-border-left-alt:solid windowtext 1.0pt;
  padding:0cm 0cm 0cm 0cm;height:15.0pt'>
  <p class=MsoNormal><span style='font-size:10.0pt;color:black'>X</span><span
  style='font-size:10.0pt;font-family:"Arial","sans-serif";color:black'><o:p></o:p></span></p>
  </td>
  <td style='border-top:none;border-left:none;border-bottom:solid windowtext 1.0pt;
  border-right:solid windowtext 1.0pt;padding:0cm 0cm 0cm 0cm;height:15.0pt'>
  <p class=MsoNormal><span style='font-size:10.0pt;color:black'>No</span><span
  style='font-size:10.0pt;font-family:"Arial","sans-serif";color:black'><o:p></o:p></span></p>
  </td>
  <td style='border-top:none;border-left:none;border-bottom:solid windowtext 1.0pt;
  border-right:solid windowtext 1.0pt;padding:0cm 0cm 0cm 0cm;height:15.0pt'>
  <p class=MsoNormal><span style='font-size:10.0pt;color:black'>Yes</span><span
  style='font-size:10.0pt;font-family:"Arial","sans-serif";color:black'><o:p></o:p></span></p>
  </td>
  <td style='border-top:none;border-left:none;border-bottom:solid windowtext 1.0pt;
  border-right:solid windowtext 1.0pt;padding:0cm 0cm 0cm 0cm;height:15.0pt'>
  <p class=MsoNormal><span style='font-size:10.0pt;color:black'>&gt; 6 months</span><span
  style='font-size:10.0pt;font-family:"Arial","sans-serif";color:black'><o:p></o:p></span></p>
  </td>
  <td style='border-top:none;border-left:none;border-bottom:solid windowtext 1.0pt;
  border-right:solid windowtext 1.0pt;padding:0cm 0cm 0cm 0cm;height:15.0pt'>
  <p class=MsoNormal><span style='font-size:10.0pt;color:black'>0</span><span
  style='font-size:10.0pt;font-family:"Arial","sans-serif";color:black'><o:p></o:p></span></p>
  </td>
  <td style='border-top:none;border-left:none;border-bottom:solid windowtext 1.0pt;
  border-right:solid windowtext 1.0pt;padding:0cm 0cm 0cm 0cm;height:15.0pt'>
  <p class=MsoNormal><span style='font-size:10.0pt;color:black'>Yes</span><span
  style='font-size:10.0pt;font-family:"Arial","sans-serif";color:black'><o:p></o:p></span></p>
  </td>
  <td style='border-top:none;border-left:none;border-bottom:solid windowtext 1.0pt;
  border-right:solid windowtext 1.0pt;padding:0cm 0cm 0cm 0cm;height:15.0pt'>
  <p class=MsoNormal><span style='font-size:10.0pt;color:black'>No</span><span
  style='font-size:10.0pt;font-family:"Arial","sans-serif";color:black'><o:p></o:p></span></p>
  </td>
 </tr>
 <tr style='mso-yfti-irow:16;height:15.0pt'>
  <td valign=top style='border:solid windowtext 1.0pt;border-top:none;
  padding:0cm 0cm 0cm 0cm;height:15.0pt'>
  <p class=MsoNormal><span style='font-size:10.0pt;color:black'>15<o:p></o:p></span></p>
  </td>
  <td style='border-top:none;border-left:none;border-bottom:solid windowtext 1.0pt;
  border-right:solid windowtext 1.0pt;mso-border-left-alt:solid windowtext 1.0pt;
  padding:0cm 0cm 0cm 0cm;height:15.0pt'>
  <p class=MsoNormal><span style='font-size:10.0pt;color:black'>X</span><span
  style='font-size:10.0pt;font-family:"Arial","sans-serif";color:black'><o:p></o:p></span></p>
  </td>
  <td style='border-top:none;border-left:none;border-bottom:solid windowtext 1.0pt;
  border-right:solid windowtext 1.0pt;padding:0cm 0cm 0cm 0cm;height:15.0pt'>
  <p class=MsoNormal><span style='font-size:10.0pt;color:black'>No</span><span
  style='font-size:10.0pt;font-family:"Arial","sans-serif";color:black'><o:p></o:p></span></p>
  </td>
  <td style='border-top:none;border-left:none;border-bottom:solid windowtext 1.0pt;
  border-right:solid windowtext 1.0pt;padding:0cm 0cm 0cm 0cm;height:15.0pt'>
  <p class=MsoNormal><span style='font-size:10.0pt;color:black'>No</span><span
  style='font-size:10.0pt;font-family:"Arial","sans-serif";color:black'><o:p></o:p></span></p>
  </td>
  <td style='border-top:none;border-left:none;border-bottom:solid windowtext 1.0pt;
  border-right:solid windowtext 1.0pt;padding:0cm 0cm 0cm 0cm;height:15.0pt'>
  <p class=MsoNormal><span style='font-size:10.0pt;color:black'>&lt;= 6 months</span><span
  style='font-size:10.0pt;font-family:"Arial","sans-serif";color:black'><o:p></o:p></span></p>
  </td>
  <td style='border-top:none;border-left:none;border-bottom:solid windowtext 1.0pt;
  border-right:solid windowtext 1.0pt;padding:0cm 0cm 0cm 0cm;height:15.0pt'>
  <p class=MsoNormal><span style='font-size:10.0pt;color:black'>0</span><span
  style='font-size:10.0pt;font-family:"Arial","sans-serif";color:black'><o:p></o:p></span></p>
  </td>
  <td style='border-top:none;border-left:none;border-bottom:solid windowtext 1.0pt;
  border-right:solid windowtext 1.0pt;padding:0cm 0cm 0cm 0cm;height:15.0pt'>
  <p class=MsoNormal><span style='font-size:10.0pt;color:black'>Yes</span><span
  style='font-size:10.0pt;font-family:"Arial","sans-serif";color:black'><o:p></o:p></span></p>
  </td>
  <td style='border-top:none;border-left:none;border-bottom:solid windowtext 1.0pt;
  border-right:solid windowtext 1.0pt;padding:0cm 0cm 0cm 0cm;height:15.0pt'>
  <p class=MsoNormal><span style='font-size:10.0pt;color:black'>No</span><span
  style='font-size:10.0pt;font-family:"Arial","sans-serif";color:black'><o:p></o:p></span></p>
  </td>
 </tr>
 <tr style='mso-yfti-irow:17;height:15.0pt'>
  <td valign=top style='border:solid windowtext 1.0pt;border-top:none;
  padding:0cm 0cm 0cm 0cm;height:15.0pt'>
  <p class=MsoNormal><span style='font-size:10.0pt;color:black'>16<o:p></o:p></span></p>
  </td>
  <td style='border-top:none;border-left:none;border-bottom:solid windowtext 1.0pt;
  border-right:solid windowtext 1.0pt;mso-border-left-alt:solid windowtext 1.0pt;
  padding:0cm 0cm 0cm 0cm;height:15.0pt'>
  <p class=MsoNormal><span style='font-size:10.0pt;color:black'>X</span><span
  style='font-size:10.0pt;font-family:"Arial","sans-serif";color:black'><o:p></o:p></span></p>
  </td>
  <td style='border-top:none;border-left:none;border-bottom:solid windowtext 1.0pt;
  border-right:solid windowtext 1.0pt;padding:0cm 0cm 0cm 0cm;height:15.0pt'>
  <p class=MsoNormal><span style='font-size:10.0pt;color:black'>No</span><span
  style='font-size:10.0pt;font-family:"Arial","sans-serif";color:black'><o:p></o:p></span></p>
  </td>
  <td style='border-top:none;border-left:none;border-bottom:solid windowtext 1.0pt;
  border-right:solid windowtext 1.0pt;padding:0cm 0cm 0cm 0cm;height:15.0pt'>
  <p class=MsoNormal><span style='font-size:10.0pt;color:black'>No</span><span
  style='font-size:10.0pt;font-family:"Arial","sans-serif";color:black'><o:p></o:p></span></p>
  </td>
  <td style='border-top:none;border-left:none;border-bottom:solid windowtext 1.0pt;
  border-right:solid windowtext 1.0pt;padding:0cm 0cm 0cm 0cm;height:15.0pt'>
  <p class=MsoNormal><span style='font-size:10.0pt;color:black'>&gt; 6 months</span><span
  style='font-size:10.0pt;font-family:"Arial","sans-serif";color:black'><o:p></o:p></span></p>
  </td>
  <td style='border-top:none;border-left:none;border-bottom:solid windowtext 1.0pt;
  border-right:solid windowtext 1.0pt;padding:0cm 0cm 0cm 0cm;height:15.0pt'>
  <p class=MsoNormal><span style='font-size:10.0pt;color:black'>0</span><span
  style='font-size:10.0pt;font-family:"Arial","sans-serif";color:black'><o:p></o:p></span></p>
  </td>
  <td style='border-top:none;border-left:none;border-bottom:solid windowtext 1.0pt;
  border-right:solid windowtext 1.0pt;padding:0cm 0cm 0cm 0cm;height:15.0pt'>
  <p class=MsoNormal><span style='font-size:10.0pt;color:black'>Yes</span><span
  style='font-size:10.0pt;font-family:"Arial","sans-serif";color:black'><o:p></o:p></span></p>
  </td>
  <td style='border-top:none;border-left:none;border-bottom:solid windowtext 1.0pt;
  border-right:solid windowtext 1.0pt;padding:0cm 0cm 0cm 0cm;height:15.0pt'>
  <p class=MsoNormal><span style='font-size:10.0pt;color:black'>Yes</span><span
  style='font-size:10.0pt;font-family:"Arial","sans-serif";color:black'><o:p></o:p></span></p>
  </td>
 </tr>
 <tr style='mso-yfti-irow:18;mso-yfti-lastrow:yes;height:15.0pt'>
  <td valign=top style='border:solid windowtext 1.0pt;border-top:none;
  padding:0cm 0cm 0cm 0cm;height:15.0pt'>
  <p class=MsoNormal><span style='font-size:10.0pt;color:black'>17<o:p></o:p></span></p>
  </td>
  <td style='border-top:none;border-left:none;border-bottom:solid windowtext 1.0pt;
  border-right:solid windowtext 1.0pt;mso-border-left-alt:solid windowtext 1.0pt;
  padding:0cm 0cm 0cm 0cm;height:15.0pt'>
  <p class=MsoNormal><span style='font-size:10.0pt;color:black'>Other</span><span
  style='font-size:10.0pt;font-family:"Arial","sans-serif";color:black'><o:p></o:p></span></p>
  </td>
  <td style='border-top:none;border-left:none;border-bottom:solid windowtext 1.0pt;
  border-right:solid windowtext 1.0pt;padding:0cm 0cm 0cm 0cm;height:15.0pt'>
  <p class=MsoNormal><span style='font-size:10.0pt;color:black'>Not Checked</span><span
  style='font-size:10.0pt;font-family:"Arial","sans-serif";color:black'><o:p></o:p></span></p>
  </td>
  <td style='border-top:none;border-left:none;border-bottom:solid windowtext 1.0pt;
  border-right:solid windowtext 1.0pt;padding:0cm 0cm 0cm 0cm;height:15.0pt'>
  <p class=MsoNormal><span style='font-size:10.0pt;color:black'>Not Checked</span><span
  style='font-size:10.0pt;font-family:"Arial","sans-serif";color:black'><o:p></o:p></span></p>
  </td>
  <td style='border-top:none;border-left:none;border-bottom:solid windowtext 1.0pt;
  border-right:solid windowtext 1.0pt;padding:0cm 0cm 0cm 0cm;height:15.0pt'>
  <p class=MsoNormal><span style='font-size:10.0pt;color:black'>Not Checked</span><span
  style='font-size:10.0pt;font-family:"Arial","sans-serif";color:black'><o:p></o:p></span></p>
  </td>
  <td style='border-top:none;border-left:none;border-bottom:solid windowtext 1.0pt;
  border-right:solid windowtext 1.0pt;padding:0cm 0cm 0cm 0cm;height:15.0pt'>
  <p class=MsoNormal><span style='font-size:10.0pt;color:black'>0</span><span
  style='font-size:10.0pt;font-family:"Arial","sans-serif";color:black'><o:p></o:p></span></p>
  </td>
  <td style='border-top:none;border-left:none;border-bottom:solid windowtext 1.0pt;
  border-right:solid windowtext 1.0pt;padding:0cm 0cm 0cm 0cm;height:15.0pt'>
  <p class=MsoNormal><span style='font-size:10.0pt;color:black'>Yes</span><span
  style='font-size:10.0pt;font-family:"Arial","sans-serif";color:black'><o:p></o:p></span></p>
  </td>
  <td style='border-top:none;border-left:none;border-bottom:solid windowtext 1.0pt;
  border-right:solid windowtext 1.0pt;padding:0cm 0cm 0cm 0cm;height:15.0pt'>
  <p class=MsoNormal><span style='font-size:10.0pt;color:black'>No</span><span
  style='font-size:10.0pt;font-family:"Arial","sans-serif";color:black'><o:p></o:p></span></p>
  </td>
 </tr>
</table>
 * 
 * <h1>Project Penguin</h1>
 *  
<p class=MsoNormal><span style='font-size:12.0pt;font-family:"Times New Roman","serif";
color:black'>Assumptions:<o:p></o:p></span></p>

<p class=MsoNormal><span style='font-size:12.0pt;font-family:"Times New Roman","serif";
color:black'>- No checks for US dialing or roaming<o:p></o:p></span></p>

<p class=MsoNormal><span style='font-size:12.0pt;font-family:"Times New Roman","serif";
color:black'>- No credit check<o:p></o:p></span></p>

<table class=MsoNormalTable border=0 cellspacing=0 cellpadding=0 width=1006
 style='width:754.7pt;border-collapse:collapse;mso-yfti-tbllook:1184;
 mso-padding-alt:0cm 0cm 0cm 0cm'>
 <tr style='mso-yfti-irow:0;mso-yfti-firstrow:yes;height:48.0pt'>
  <td width=90 valign=top style='width:67.7pt;border:solid windowtext 1.0pt;
  border-right:solid black 1.0pt;background:#FCD5B4;padding:0cm 0cm 0cm 0cm;
  height:48.0pt'>
  <p class=MsoNormal><b><span style='font-size:12.0pt;color:black'><o:p>&nbsp;</o:p></span></b></p>
  </td>
  <td width=545 colspan=4 style='width:409.1pt;border-top:solid windowtext 1.0pt;
  border-left:none;border-bottom:solid windowtext 1.0pt;border-right:solid black 1.0pt;
  mso-border-left-alt:solid windowtext 1.0pt;background:#FCD5B4;padding:0cm 0cm 0cm 0cm;
  height:48.0pt'>
  <p class=MsoNormal><b><span style='font-size:12.0pt;color:black'>Inputs:&nbsp;<br>
  -Credit Class retrieved from HCD<br>
  -Account Tenure, Delinquency and Collection Treatment retrieved from KB&nbsp;</span></b><span
  style='font-size:12.0pt;font-family:"Times New Roman","serif";color:black'><o:p></o:p></span></p>
  </td>
  <td width=371 colspan=3 style='width:277.9pt;border-top:solid windowtext 1.0pt;
  border-left:none;border-bottom:solid windowtext 1.0pt;border-right:solid black 1.0pt;
  background:#B6DDE8;padding:0cm 0cm 0cm 0cm;height:48.0pt'>
  <p class=MsoNormal><b><span style='font-size:12.0pt;color:black'>Outputs:<br>
  - Configuration managed in LDAP</span></b><span style='font-size:12.0pt;
  font-family:"Times New Roman","serif";color:black'><o:p></o:p></span></p>
  </td>
 </tr>
 <tr style='mso-yfti-irow:1;height:31.5pt'>
  <td width=90 valign=top style='width:67.7pt;border:solid windowtext 1.0pt;
  border-top:none;background:#FCD5B4;padding:0cm 0cm 0cm 0cm;height:31.5pt'>
  <p class=MsoNormal><b><span style='font-size:12.0pt;color:black'>Test Case Id<o:p></o:p></span></b></p>
  </td>
  <td width=90 style='width:67.7pt;border-top:none;border-left:none;border-bottom:
  solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;mso-border-left-alt:
  solid windowtext 1.0pt;background:#FCD5B4;padding:0cm 0cm 0cm 0cm;height:
  31.5pt'>
  <p class=MsoNormal><b><span style='font-size:12.0pt;color:black'>Credit Class</span></b><span
  style='font-size:12.0pt;font-family:"Times New Roman","serif";color:black'><o:p></o:p></span></p>
  </td>
  <td width=100 style='width:75.1pt;border-top:none;border-left:none;
  border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  background:#FCD5B4;padding:0cm 0cm 0cm 0cm;height:31.5pt'>
  <p class=MsoNormal><b><span style='font-size:12.0pt;color:black'>New
  Activation</span></b><span style='font-size:12.0pt;font-family:"Times New Roman","serif";
  color:black'><o:p></o:p></span></p>
  </td>
  <td width=236 style='width:177.25pt;border-top:none;border-left:none;
  border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  background:#FCD5B4;padding:0cm 0cm 0cm 0cm;height:31.5pt'>
  <p class=MsoNormal><b><span style='font-size:12.0pt;color:black'>Collection
  Treatment (within the last 6 months) OR Account is Delinquent</span></b><span
  style='font-size:12.0pt;font-family:"Times New Roman","serif";color:black'><o:p></o:p></span></p>
  </td>
  <td width=119 style='width:89.05pt;border-top:none;border-left:none;
  border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  background:#FCD5B4;padding:0cm 0cm 0cm 0cm;height:31.5pt'>
  <p class=MsoNormal><b><span style='font-size:12.0pt;color:black'>Tenure</span></b><span
  style='font-size:12.0pt;font-family:"Times New Roman","serif";color:black'><o:p></o:p></span></p>
  </td>
  <td width=96 style='width:72.1pt;border-top:none;border-left:none;border-bottom:
  solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;background:#B6DDE8;
  padding:0cm 0cm 0cm 0cm;height:31.5pt'>
  <p class=MsoNormal><b><span style='font-size:12.0pt;color:black'>Deposit</span></b><span
  style='font-size:12.0pt;font-family:"Times New Roman","serif";color:black'><o:p></o:p></span></p>
  </td>
  <td width=132 style='width:99.25pt;border-top:none;border-left:none;
  border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  background:#B6DDE8;padding:0cm 0cm 0cm 0cm;height:31.5pt'>
  <p class=MsoNormal><b><span style='font-size:12.0pt;color:black'>Eligible for
  International Dialing</span></b><span style='font-size:12.0pt;font-family:
  "Times New Roman","serif";color:black'><o:p></o:p></span></p>
  </td>
  <td width=142 style='width:106.55pt;border-top:none;border-left:none;
  border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  background:#B6DDE8;padding:0cm 0cm 0cm 0cm;height:31.5pt'>
  <p class=MsoNormal><b><span style='font-size:12.0pt;color:black'>Eligible for
  International Roaming</span></b><span style='font-size:12.0pt;font-family:
  "Times New Roman","serif";color:black'><o:p></o:p></span></p>
  </td>
 </tr>
 <tr style='mso-yfti-irow:2;height:15.0pt'>
  <td valign=top style='border:solid windowtext 1.0pt;border-top:none;
  padding:0cm 0cm 0cm 0cm;height:15.0pt'>
  <p class=MsoNormal><span style='font-size:12.0pt;color:black'>1<o:p></o:p></span></p>
  </td>
  <td style='border-top:none;border-left:none;border-bottom:solid windowtext 1.0pt;
  border-right:solid windowtext 1.0pt;mso-border-left-alt:solid windowtext 1.0pt;
  padding:0cm 0cm 0cm 0cm;height:15.0pt'>
  <p class=MsoNormal><span style='font-size:12.0pt;color:black'>Not Checked</span><span
  style='font-size:12.0pt;font-family:"Times New Roman","serif";color:black'><o:p></o:p></span></p>
  </td>
  <td style='border-top:none;border-left:none;border-bottom:solid windowtext 1.0pt;
  border-right:solid windowtext 1.0pt;padding:0cm 0cm 0cm 0cm;height:15.0pt'>
  <p class=MsoNormal><span style='font-size:12.0pt;color:black'>Yes</span><span
  style='font-size:12.0pt;font-family:"Times New Roman","serif";color:black'><o:p></o:p></span></p>
  </td>
  <td style='border-top:none;border-left:none;border-bottom:solid windowtext 1.0pt;
  border-right:solid windowtext 1.0pt;padding:0cm 0cm 0cm 0cm;height:15.0pt'>
  <p class=MsoNormal><span style='font-size:12.0pt;color:black'>Not Checked</span><span
  style='font-size:12.0pt;font-family:"Times New Roman","serif";color:black'><o:p></o:p></span></p>
  </td>
  <td style='border-top:none;border-left:none;border-bottom:solid windowtext 1.0pt;
  border-right:solid windowtext 1.0pt;padding:0cm 0cm 0cm 0cm;height:15.0pt'>
  <p class=MsoNormal><span style='font-size:12.0pt;color:black'>Not Checked</span><span
  style='font-size:12.0pt;font-family:"Times New Roman","serif";color:black'><o:p></o:p></span></p>
  </td>
  <td style='border-top:none;border-left:none;border-bottom:solid windowtext 1.0pt;
  border-right:solid windowtext 1.0pt;padding:0cm 0cm 0cm 0cm;height:15.0pt'>
  <p class=MsoNormal><span style='font-size:12.0pt;color:black'>200</span><span
  style='font-size:12.0pt;font-family:"Times New Roman","serif";color:black'><o:p></o:p></span></p>
  </td>
  <td style='border-top:none;border-left:none;border-bottom:solid windowtext 1.0pt;
  border-right:solid windowtext 1.0pt;padding:0cm 0cm 0cm 0cm;height:15.0pt'>
  <p class=MsoNormal><span style='font-size:12.0pt;color:black'>No</span><span
  style='font-size:12.0pt;font-family:"Times New Roman","serif";color:black'><o:p></o:p></span></p>
  </td>
  <td style='border-top:none;border-left:none;border-bottom:solid windowtext 1.0pt;
  border-right:solid windowtext 1.0pt;padding:0cm 0cm 0cm 0cm;height:15.0pt'>
  <p class=MsoNormal><span style='font-size:12.0pt;color:black'>No</span><span
  style='font-size:12.0pt;font-family:"Times New Roman","serif";color:black'><o:p></o:p></span></p>
  </td>
 </tr>
 <tr style='mso-yfti-irow:3;height:15.0pt'>
  <td valign=top style='border:solid windowtext 1.0pt;border-top:none;
  padding:0cm 0cm 0cm 0cm;height:15.0pt'>
  <p class=MsoNormal><span style='font-size:12.0pt;color:black'>2<o:p></o:p></span></p>
  </td>
  <td style='border-top:none;border-left:none;border-bottom:solid windowtext 1.0pt;
  border-right:solid windowtext 1.0pt;mso-border-left-alt:solid windowtext 1.0pt;
  padding:0cm 0cm 0cm 0cm;height:15.0pt'>
  <p class=MsoNormal><span style='font-size:12.0pt;color:black'>Not Checked</span><span
  style='font-size:12.0pt;font-family:"Times New Roman","serif";color:black'><o:p></o:p></span></p>
  </td>
  <td style='border-top:none;border-left:none;border-bottom:solid windowtext 1.0pt;
  border-right:solid windowtext 1.0pt;padding:0cm 0cm 0cm 0cm;height:15.0pt'>
  <p class=MsoNormal><span style='font-size:12.0pt;color:black'>No</span><span
  style='font-size:12.0pt;font-family:"Times New Roman","serif";color:black'><o:p></o:p></span></p>
  </td>
  <td style='border-top:none;border-left:none;border-bottom:solid windowtext 1.0pt;
  border-right:solid windowtext 1.0pt;padding:0cm 0cm 0cm 0cm;height:15.0pt'>
  <p class=MsoNormal><span style='font-size:12.0pt;color:black'>No</span><span
  style='font-size:12.0pt;font-family:"Times New Roman","serif";color:black'><o:p></o:p></span></p>
  </td>
  <td style='border-top:none;border-left:none;border-bottom:solid windowtext 1.0pt;
  border-right:solid windowtext 1.0pt;padding:0cm 0cm 0cm 0cm;height:15.0pt'>
  <p class=MsoNormal><span style='font-size:12.0pt;color:black'>0 &lt;= x &lt;
  6 months</span><span style='font-size:12.0pt;font-family:"Times New Roman","serif";
  color:black'><o:p></o:p></span></p>
  </td>
  <td style='border-top:none;border-left:none;border-bottom:solid windowtext 1.0pt;
  border-right:solid windowtext 1.0pt;padding:0cm 0cm 0cm 0cm;height:15.0pt'>
  <p class=MsoNormal><span style='font-size:12.0pt;color:black'>200</span><span
  style='font-size:12.0pt;font-family:"Times New Roman","serif";color:black'><o:p></o:p></span></p>
  </td>
  <td style='border-top:none;border-left:none;border-bottom:solid windowtext 1.0pt;
  border-right:solid windowtext 1.0pt;padding:0cm 0cm 0cm 0cm;height:15.0pt'>
  <p class=MsoNormal><span style='font-size:12.0pt;color:black'>Yes</span><span
  style='font-size:12.0pt;font-family:"Times New Roman","serif";color:black'><o:p></o:p></span></p>
  </td>
  <td style='border-top:none;border-left:none;border-bottom:solid windowtext 1.0pt;
  border-right:solid windowtext 1.0pt;padding:0cm 0cm 0cm 0cm;height:15.0pt'>
  <p class=MsoNormal><span style='font-size:12.0pt;color:black'>No</span><span
  style='font-size:12.0pt;font-family:"Times New Roman","serif";color:black'><o:p></o:p></span></p>
  </td>
 </tr>
 <tr style='mso-yfti-irow:4;height:15.0pt'>
  <td valign=top style='border:solid windowtext 1.0pt;border-top:none;
  padding:0cm 0cm 0cm 0cm;height:15.0pt'>
  <p class=MsoNormal><span style='font-size:12.0pt;color:black'>3<o:p></o:p></span></p>
  </td>
  <td style='border-top:none;border-left:none;border-bottom:solid windowtext 1.0pt;
  border-right:solid windowtext 1.0pt;mso-border-left-alt:solid windowtext 1.0pt;
  padding:0cm 0cm 0cm 0cm;height:15.0pt'>
  <p class=MsoNormal><span style='font-size:12.0pt;color:black'>Not Checked</span><span
  style='font-size:12.0pt;font-family:"Times New Roman","serif";color:black'><o:p></o:p></span></p>
  </td>
  <td style='border-top:none;border-left:none;border-bottom:solid windowtext 1.0pt;
  border-right:solid windowtext 1.0pt;padding:0cm 0cm 0cm 0cm;height:15.0pt'>
  <p class=MsoNormal><span style='font-size:12.0pt;color:black'>No</span><span
  style='font-size:12.0pt;font-family:"Times New Roman","serif";color:black'><o:p></o:p></span></p>
  </td>
  <td style='border-top:none;border-left:none;border-bottom:solid windowtext 1.0pt;
  border-right:solid windowtext 1.0pt;padding:0cm 0cm 0cm 0cm;height:15.0pt'>
  <p class=MsoNormal><span style='font-size:12.0pt;color:black'>No</span><span
  style='font-size:12.0pt;font-family:"Times New Roman","serif";color:black'><o:p></o:p></span></p>
  </td>
  <td style='border-top:none;border-left:none;border-bottom:solid windowtext 1.0pt;
  border-right:solid windowtext 1.0pt;padding:0cm 0cm 0cm 0cm;height:15.0pt'>
  <p class=MsoNormal><span style='font-size:12.0pt;color:black'>6 &lt;= x &lt;
  12 months</span><span style='font-size:12.0pt;font-family:"Times New Roman","serif";
  color:black'><o:p></o:p></span></p>
  </td>
  <td style='border-top:none;border-left:none;border-bottom:solid windowtext 1.0pt;
  border-right:solid windowtext 1.0pt;padding:0cm 0cm 0cm 0cm;height:15.0pt'>
  <p class=MsoNormal><span style='font-size:12.0pt;color:black'>50</span><span
  style='font-size:12.0pt;font-family:"Times New Roman","serif";color:black'><o:p></o:p></span></p>
  </td>
  <td style='border-top:none;border-left:none;border-bottom:solid windowtext 1.0pt;
  border-right:solid windowtext 1.0pt;padding:0cm 0cm 0cm 0cm;height:15.0pt'>
  <p class=MsoNormal><span style='font-size:12.0pt;color:black'>Yes</span><span
  style='font-size:12.0pt;font-family:"Times New Roman","serif";color:black'><o:p></o:p></span></p>
  </td>
  <td style='border-top:none;border-left:none;border-bottom:solid windowtext 1.0pt;
  border-right:solid windowtext 1.0pt;padding:0cm 0cm 0cm 0cm;height:15.0pt'>
  <p class=MsoNormal><span style='font-size:12.0pt;color:black'>No</span><span
  style='font-size:12.0pt;font-family:"Times New Roman","serif";color:black'><o:p></o:p></span></p>
  </td>
 </tr>
 <tr style='mso-yfti-irow:5;height:15.0pt'>
  <td valign=top style='border:solid windowtext 1.0pt;border-top:none;
  padding:0cm 0cm 0cm 0cm;height:15.0pt'>
  <p class=MsoNormal><span style='font-size:12.0pt;color:black'>4<o:p></o:p></span></p>
  </td>
  <td style='border-top:none;border-left:none;border-bottom:solid windowtext 1.0pt;
  border-right:solid windowtext 1.0pt;mso-border-left-alt:solid windowtext 1.0pt;
  padding:0cm 0cm 0cm 0cm;height:15.0pt'>
  <p class=MsoNormal><span style='font-size:12.0pt;color:black'>Not Checked</span><span
  style='font-size:12.0pt;font-family:"Times New Roman","serif";color:black'><o:p></o:p></span></p>
  </td>
  <td style='border-top:none;border-left:none;border-bottom:solid windowtext 1.0pt;
  border-right:solid windowtext 1.0pt;padding:0cm 0cm 0cm 0cm;height:15.0pt'>
  <p class=MsoNormal><span style='font-size:12.0pt;color:black'>No</span><span
  style='font-size:12.0pt;font-family:"Times New Roman","serif";color:black'><o:p></o:p></span></p>
  </td>
  <td style='border-top:none;border-left:none;border-bottom:solid windowtext 1.0pt;
  border-right:solid windowtext 1.0pt;padding:0cm 0cm 0cm 0cm;height:15.0pt'>
  <p class=MsoNormal><span style='font-size:12.0pt;color:black'>No</span><span
  style='font-size:12.0pt;font-family:"Times New Roman","serif";color:black'><o:p></o:p></span></p>
  </td>
  <td style='border-top:none;border-left:none;border-bottom:solid windowtext 1.0pt;
  border-right:solid windowtext 1.0pt;padding:0cm 0cm 0cm 0cm;height:15.0pt'>
  <p class=MsoNormal><span style='font-size:12.0pt;color:black'>x &gt;= 12
  months</span><span style='font-size:12.0pt;font-family:"Times New Roman","serif";
  color:black'><o:p></o:p></span></p>
  </td>
  <td style='border-top:none;border-left:none;border-bottom:solid windowtext 1.0pt;
  border-right:solid windowtext 1.0pt;padding:0cm 0cm 0cm 0cm;height:15.0pt'>
  <p class=MsoNormal><span style='font-size:12.0pt;color:black'>0</span><span
  style='font-size:12.0pt;font-family:"Times New Roman","serif";color:black'><o:p></o:p></span></p>
  </td>
  <td style='border-top:none;border-left:none;border-bottom:solid windowtext 1.0pt;
  border-right:solid windowtext 1.0pt;padding:0cm 0cm 0cm 0cm;height:15.0pt'>
  <p class=MsoNormal><span style='font-size:12.0pt;color:black'>Yes</span><span
  style='font-size:12.0pt;font-family:"Times New Roman","serif";color:black'><o:p></o:p></span></p>
  </td>
  <td style='border-top:none;border-left:none;border-bottom:solid windowtext 1.0pt;
  border-right:solid windowtext 1.0pt;padding:0cm 0cm 0cm 0cm;height:15.0pt'>
  <p class=MsoNormal><span style='font-size:12.0pt;color:black'>No</span><span
  style='font-size:12.0pt;font-family:"Times New Roman","serif";color:black'><o:p></o:p></span></p>
  </td>
 </tr>
 <tr style='mso-yfti-irow:6;height:15.0pt'>
  <td valign=top style='border:solid windowtext 1.0pt;border-top:none;
  padding:0cm 0cm 0cm 0cm;height:15.0pt'>
  <p class=MsoNormal><span style='font-size:12.0pt;color:black'>5<o:p></o:p></span></p>
  </td>
  <td style='border-top:none;border-left:none;border-bottom:solid windowtext 1.0pt;
  border-right:solid windowtext 1.0pt;mso-border-left-alt:solid windowtext 1.0pt;
  padding:0cm 0cm 0cm 0cm;height:15.0pt'>
  <p class=MsoNormal><span style='font-size:12.0pt;color:black'>Not Checked</span><span
  style='font-size:12.0pt;font-family:"Times New Roman","serif";color:black'><o:p></o:p></span></p>
  </td>
  <td style='border-top:none;border-left:none;border-bottom:solid windowtext 1.0pt;
  border-right:solid windowtext 1.0pt;padding:0cm 0cm 0cm 0cm;height:15.0pt'>
  <p class=MsoNormal><span style='font-size:12.0pt;color:black'>No</span><span
  style='font-size:12.0pt;font-family:"Times New Roman","serif";color:black'><o:p></o:p></span></p>
  </td>
  <td style='border-top:none;border-left:none;border-bottom:solid windowtext 1.0pt;
  border-right:solid windowtext 1.0pt;padding:0cm 0cm 0cm 0cm;height:15.0pt'>
  <p class=MsoNormal><span style='font-size:12.0pt;color:black'>Yes</span><span
  style='font-size:12.0pt;font-family:"Times New Roman","serif";color:black'><o:p></o:p></span></p>
  </td>
  <td style='border-top:none;border-left:none;border-bottom:solid windowtext 1.0pt;
  border-right:solid windowtext 1.0pt;padding:0cm 0cm 0cm 0cm;height:15.0pt'>
  <p class=MsoNormal><span style='font-size:12.0pt;color:black'>0 &lt;= x &lt;
  6 months</span><span style='font-size:12.0pt;font-family:"Times New Roman","serif";
  color:black'><o:p></o:p></span></p>
  </td>
  <td style='border-top:none;border-left:none;border-bottom:solid windowtext 1.0pt;
  border-right:solid windowtext 1.0pt;padding:0cm 0cm 0cm 0cm;height:15.0pt'>
  <p class=MsoNormal><span style='font-size:12.0pt;color:black'>200</span><span
  style='font-size:12.0pt;font-family:"Times New Roman","serif";color:black'><o:p></o:p></span></p>
  </td>
  <td style='border-top:none;border-left:none;border-bottom:solid windowtext 1.0pt;
  border-right:solid windowtext 1.0pt;padding:0cm 0cm 0cm 0cm;height:15.0pt'>
  <p class=MsoNormal><span style='font-size:12.0pt;color:black'>Yes</span><span
  style='font-size:12.0pt;font-family:"Times New Roman","serif";color:black'><o:p></o:p></span></p>
  </td>
  <td style='border-top:none;border-left:none;border-bottom:solid windowtext 1.0pt;
  border-right:solid windowtext 1.0pt;padding:0cm 0cm 0cm 0cm;height:15.0pt'>
  <p class=MsoNormal><span style='font-size:12.0pt;color:black'>No</span><span
  style='font-size:12.0pt;font-family:"Times New Roman","serif";color:black'><o:p></o:p></span></p>
  </td>
 </tr>
 <tr style='mso-yfti-irow:7;height:15.0pt'>
  <td valign=top style='border:solid windowtext 1.0pt;border-top:none;
  padding:0cm 0cm 0cm 0cm;height:15.0pt'>
  <p class=MsoNormal><span style='font-size:12.0pt;color:black'>6<o:p></o:p></span></p>
  </td>
  <td style='border-top:none;border-left:none;border-bottom:solid windowtext 1.0pt;
  border-right:solid windowtext 1.0pt;mso-border-left-alt:solid windowtext 1.0pt;
  padding:0cm 0cm 0cm 0cm;height:15.0pt'>
  <p class=MsoNormal><span style='font-size:12.0pt;color:black'>Not Checked</span><span
  style='font-size:12.0pt;font-family:"Times New Roman","serif";color:black'><o:p></o:p></span></p>
  </td>
  <td style='border-top:none;border-left:none;border-bottom:solid windowtext 1.0pt;
  border-right:solid windowtext 1.0pt;padding:0cm 0cm 0cm 0cm;height:15.0pt'>
  <p class=MsoNormal><span style='font-size:12.0pt;color:black'>No</span><span
  style='font-size:12.0pt;font-family:"Times New Roman","serif";color:black'><o:p></o:p></span></p>
  </td>
  <td style='border-top:none;border-left:none;border-bottom:solid windowtext 1.0pt;
  border-right:solid windowtext 1.0pt;padding:0cm 0cm 0cm 0cm;height:15.0pt'>
  <p class=MsoNormal><span style='font-size:12.0pt;color:black'>Yes</span><span
  style='font-size:12.0pt;font-family:"Times New Roman","serif";color:black'><o:p></o:p></span></p>
  </td>
  <td style='border-top:none;border-left:none;border-bottom:solid windowtext 1.0pt;
  border-right:solid windowtext 1.0pt;padding:0cm 0cm 0cm 0cm;height:15.0pt'>
  <p class=MsoNormal><span style='font-size:12.0pt;color:black'>6 &lt;= x &lt;
  12 months</span><span style='font-size:12.0pt;font-family:"Times New Roman","serif";
  color:black'><o:p></o:p></span></p>
  </td>
  <td style='border-top:none;border-left:none;border-bottom:solid windowtext 1.0pt;
  border-right:solid windowtext 1.0pt;padding:0cm 0cm 0cm 0cm;height:15.0pt'>
  <p class=MsoNormal><span style='font-size:12.0pt;color:black'>200</span><span
  style='font-size:12.0pt;font-family:"Times New Roman","serif";color:black'><o:p></o:p></span></p>
  </td>
  <td style='border-top:none;border-left:none;border-bottom:solid windowtext 1.0pt;
  border-right:solid windowtext 1.0pt;padding:0cm 0cm 0cm 0cm;height:15.0pt'>
  <p class=MsoNormal><span style='font-size:12.0pt;color:black'>Yes</span><span
  style='font-size:12.0pt;font-family:"Times New Roman","serif";color:black'><o:p></o:p></span></p>
  </td>
  <td style='border-top:none;border-left:none;border-bottom:solid windowtext 1.0pt;
  border-right:solid windowtext 1.0pt;padding:0cm 0cm 0cm 0cm;height:15.0pt'>
  <p class=MsoNormal><span style='font-size:12.0pt;color:black'>No</span><span
  style='font-size:12.0pt;font-family:"Times New Roman","serif";color:black'><o:p></o:p></span></p>
  </td>
 </tr>
 <tr style='mso-yfti-irow:8;mso-yfti-lastrow:yes;height:15.0pt'>
  <td valign=top style='border:solid windowtext 1.0pt;border-top:none;
  padding:0cm 0cm 0cm 0cm;height:15.0pt'>
  <p class=MsoNormal><span style='font-size:12.0pt;color:black'>7<o:p></o:p></span></p>
  </td>
  <td style='border-top:none;border-left:none;border-bottom:solid windowtext 1.0pt;
  border-right:solid windowtext 1.0pt;mso-border-left-alt:solid windowtext 1.0pt;
  padding:0cm 0cm 0cm 0cm;height:15.0pt'>
  <p class=MsoNormal><span style='font-size:12.0pt;color:black'>Not Checked</span><span
  style='font-size:12.0pt;font-family:"Times New Roman","serif";color:black'><o:p></o:p></span></p>
  </td>
  <td style='border-top:none;border-left:none;border-bottom:solid windowtext 1.0pt;
  border-right:solid windowtext 1.0pt;padding:0cm 0cm 0cm 0cm;height:15.0pt'>
  <p class=MsoNormal><span style='font-size:12.0pt;color:black'>No</span><span
  style='font-size:12.0pt;font-family:"Times New Roman","serif";color:black'><o:p></o:p></span></p>
  </td>
  <td style='border-top:none;border-left:none;border-bottom:solid windowtext 1.0pt;
  border-right:solid windowtext 1.0pt;padding:0cm 0cm 0cm 0cm;height:15.0pt'>
  <p class=MsoNormal><span style='font-size:12.0pt;color:black'>Yes</span><span
  style='font-size:12.0pt;font-family:"Times New Roman","serif";color:black'><o:p></o:p></span></p>
  </td>
  <td style='border-top:none;border-left:none;border-bottom:solid windowtext 1.0pt;
  border-right:solid windowtext 1.0pt;padding:0cm 0cm 0cm 0cm;height:15.0pt'>
  <p class=MsoNormal><span style='font-size:12.0pt;color:black'>X &gt;= 12
  months</span><span style='font-size:12.0pt;font-family:"Times New Roman","serif";
  color:black'><o:p></o:p></span></p>
  </td>
  <td style='border-top:none;border-left:none;border-bottom:solid windowtext 1.0pt;
  border-right:solid windowtext 1.0pt;padding:0cm 0cm 0cm 0cm;height:15.0pt'>
  <p class=MsoNormal><span style='font-size:12.0pt;color:black'>50</span><span
  style='font-size:12.0pt;font-family:"Times New Roman","serif";color:black'><o:p></o:p></span></p>
  </td>
  <td style='border-top:none;border-left:none;border-bottom:solid windowtext 1.0pt;
  border-right:solid windowtext 1.0pt;padding:0cm 0cm 0cm 0cm;height:15.0pt'>
  <p class=MsoNormal><span style='font-size:12.0pt;color:black'>Yes</span><span
  style='font-size:12.0pt;font-family:"Times New Roman","serif";color:black'><o:p></o:p></span></p>
  </td>
  <td style='border-top:none;border-left:none;border-bottom:solid windowtext 1.0pt;
  border-right:solid windowtext 1.0pt;padding:0cm 0cm 0cm 0cm;height:15.0pt'>
  <p class=MsoNormal><span style='font-size:12.0pt;color:black'>No</span><span
  style='font-size:12.0pt;font-family:"Times New Roman","serif";color:black'><o:p></o:p></span></p>
  </td>
 </tr>
</table>
 *
 * @author Canh Tran
 *
 */
public class TMAccountCheckInternationalServiceEligibility {
	@Mocked TMProvider providerMock;
	@Mocked TMAccountSummary accountSummaryMock;
	@Mocked ProviderAspect providerAspectMock;
	@Mocked MethodMonitorFlagReader methodMonitorFlagReaderMock;	
	static com.telus.eas.config1.info.ConfigurationInfo configuration = null;
	
	ConfigurationManager configurationManager;

	@BeforeClass
	public static void beforeClass() {
		dv103();		
	}
	public static void dv103() {
		System.setProperty("com.telusmobility.config.java.naming.factory.initial", "com.sun.jndi.ldap.LdapCtxFactory");
		System.setProperty("com.telus.provider.initialContextFactory", "weblogic.jndi.WLInitialContextFactory");
		System.setProperty("com.telusmobility.config.java.naming.provider.url", "ldap://ldapread-d3.tmi.telus.com:489/cn=development3_81,o=telusconfiguration");
		
	}
	@Before
	public void setUp() throws Exception {		
	}

	@After
	public void tearDown() throws Exception {
	}

	/**
	 * This test corresponds to the input described in the formatted table javadoc'ed
	 * for this class.  The method name suffix follows the same sequence as the table. 
	 * 
	 * @param account - partial mock class
	 * @throws Exception
	 */

	@Test
	public void testScorpion1(@Mocked(methods = {"(TMProvider, AccountInfo)", "getInternationalServiceEligibilityCheckCriteria"}) final TMAccount account) throws Exception {		
		final InternationalServiceEligibilityCheckCriteria criteria = new InternationalServiceEligibilityCheckCriteria();
		criteria.setAccountCombinedType("CI");
		criteria.setBrandId(Brand.BRAND_ID_CLEARNET);
		criteria.setCreditClass("B");
		criteria.setNewAccount(true);		

		new NonStrictExpectations() {
			{
				invoke(ProviderAspect.class, "aspectOf"); returns(providerAspectMock);
				providerAspectMock.getMethodMonitorFlagReader(); returns(methodMonitorFlagReaderMock);
				invoke(account, "getInternationalServiceEligibilityCheckCriteria"); returns(criteria);

			}
		};	
		
		InternationalServiceEligibilityCheckResult checkResult = account.checkInternationalServiceEligibility();
		assertEquals(checkResult.getDepositAmount(), 0, 0);
		assertTrue(checkResult.isEligibleForInternationalDialing());
		assertFalse(checkResult.isEligibleForInternationalRoaming());		
	}

	/**
	 * This test corresponds to the input described in the formatted table javadoc'ed
	 * for this class.  The method name suffix follows the same sequence as the table. 
	 * 
	 * @param account - partial mock class
	 * @throws Exception
	 */

	@Test
	public void testScorpion2(@Mocked(methods = {"(TMProvider, AccountInfo)", "getInternationalServiceEligibilityCheckCriteria"}) final TMAccount account) throws Exception {
		final InternationalServiceEligibilityCheckCriteria criteria = new InternationalServiceEligibilityCheckCriteria();
		criteria.setAccountCombinedType("CI");		
		criteria.setBrandId(Brand.BRAND_ID_CLEARNET);
		criteria.setCreditClass("B");
		criteria.setNewAccount(false);
		criteria.setCollectionActivityPresent(true);

		new NonStrictExpectations() {
			{
				invoke(ProviderAspect.class, "aspectOf"); returns(providerAspectMock);
				providerAspectMock.getMethodMonitorFlagReader(); returns(methodMonitorFlagReaderMock);
				invoke(account, "getInternationalServiceEligibilityCheckCriteria"); returns(criteria);

			}
		};	
		
		InternationalServiceEligibilityCheckResult checkResult = account.checkInternationalServiceEligibility();
		assertEquals(checkResult.getDepositAmount(), 0, 0);
		assertTrue(checkResult.isEligibleForInternationalDialing());
		assertFalse(checkResult.isEligibleForInternationalRoaming());		
	}	
	
	/**
	 * This test corresponds to the input described in the formatted table javadoc'ed
	 * for this class.  The method name suffix follows the same sequence as the table. 
	 * 
	 * @param account - partial mock class
	 * @throws Exception
	 */

	@Test
	public void testScorpion3(@Mocked(methods = {"(TMProvider, AccountInfo)", "getInternationalServiceEligibilityCheckCriteria"}) final TMAccount account) throws Exception {
		final InternationalServiceEligibilityCheckCriteria criteria = new InternationalServiceEligibilityCheckCriteria();
		criteria.setAccountCombinedType("CI");		
		criteria.setBrandId(Brand.BRAND_ID_CLEARNET);
		criteria.setCreditClass("B");
		criteria.setNewAccount(false);
		criteria.setCollectionActivityPresent(false);

		new NonStrictExpectations() {
			{
				invoke(ProviderAspect.class, "aspectOf"); returns(providerAspectMock);
				providerAspectMock.getMethodMonitorFlagReader(); returns(methodMonitorFlagReaderMock);
				invoke(account, "getInternationalServiceEligibilityCheckCriteria"); returns(criteria);

			}
		};	
		
		InternationalServiceEligibilityCheckResult checkResult = account.checkInternationalServiceEligibility();
		assertEquals(checkResult.getDepositAmount(), 0, 0);
		assertTrue(checkResult.isEligibleForInternationalDialing());
		assertTrue(checkResult.isEligibleForInternationalRoaming());		
	}		
	
	/**
	 * This test corresponds to the input described in the formatted table javadoc'ed
	 * for this class.  The method name suffix follows the same sequence as the table. 
	 * 
	 * @param account - partial mock class
	 * @throws Exception
	 */

	@Test
	public void testScorpion4(@Mocked(methods = {"(TMProvider, AccountInfo)", "getInternationalServiceEligibilityCheckCriteria"}) final TMAccount account) throws Exception {
		final InternationalServiceEligibilityCheckCriteria criteria = new InternationalServiceEligibilityCheckCriteria();
		criteria.setAccountCombinedType("CI");		
		criteria.setBrandId(Brand.BRAND_ID_CLEARNET);
		criteria.setCreditClass("D");
		criteria.setNewAccount(true);		

		new NonStrictExpectations() {
			{
				invoke(ProviderAspect.class, "aspectOf"); returns(providerAspectMock);
				providerAspectMock.getMethodMonitorFlagReader(); returns(methodMonitorFlagReaderMock);
				invoke(account, "getInternationalServiceEligibilityCheckCriteria"); returns(criteria);

			}
		};	
		
		InternationalServiceEligibilityCheckResult checkResult = account.checkInternationalServiceEligibility();
		assertEquals(checkResult.getDepositAmount(), 0, 0);
		assertTrue(checkResult.isEligibleForInternationalDialing());
		assertFalse(checkResult.isEligibleForInternationalRoaming());		
	}		
	
	/**
	 * This test corresponds to the input described in the formatted table javadoc'ed
	 * for this class.  The method name suffix follows the same sequence as the table. 
	 * 
	 * @param account - partial mock class
	 * @throws Exception
	 */

	@Test
	public void testScorpion5(@Mocked(methods = {"(TMProvider, AccountInfo)", "getInternationalServiceEligibilityCheckCriteria"}) final TMAccount account) throws Exception {
		final InternationalServiceEligibilityCheckCriteria criteria = new InternationalServiceEligibilityCheckCriteria();
		criteria.setAccountCombinedType("CI");		
		criteria.setBrandId(Brand.BRAND_ID_CLEARNET);
		criteria.setCreditClass("D");
		criteria.setNewAccount(false);
		criteria.setCollectionActivityPresent(true);

		new NonStrictExpectations() {
			{
				invoke(ProviderAspect.class, "aspectOf"); returns(providerAspectMock);
				providerAspectMock.getMethodMonitorFlagReader(); returns(methodMonitorFlagReaderMock);
				invoke(account, "getInternationalServiceEligibilityCheckCriteria"); returns(criteria);

			}
		};	
		
		InternationalServiceEligibilityCheckResult checkResult = account.checkInternationalServiceEligibility();
		assertEquals(checkResult.getDepositAmount(), 0, 0);
		assertTrue(checkResult.isEligibleForInternationalDialing());
		assertFalse(checkResult.isEligibleForInternationalRoaming());		
	}			
	
	/**
	 * This test corresponds to the input described in the formatted table javadoc'ed
	 * for this class.  The method name suffix follows the same sequence as the table. 
	 * 
	 * @param account - partial mock class
	 * @throws Exception
	 */

	@Test
	public void testScorpion6(@Mocked(methods = {"(TMProvider, AccountInfo)", "getInternationalServiceEligibilityCheckCriteria"}) final TMAccount account) throws Exception {
		final InternationalServiceEligibilityCheckCriteria criteria = new InternationalServiceEligibilityCheckCriteria();
		criteria.setAccountCombinedType("CI");		
		criteria.setBrandId(Brand.BRAND_ID_CLEARNET);
		criteria.setCreditClass("D");
		criteria.setNewAccount(false);
		criteria.setCollectionActivityPresent(false);

		new NonStrictExpectations() {
			{
				invoke(ProviderAspect.class, "aspectOf"); returns(providerAspectMock);
				providerAspectMock.getMethodMonitorFlagReader(); returns(methodMonitorFlagReaderMock);
				invoke(account, "getInternationalServiceEligibilityCheckCriteria"); returns(criteria);

			}
		};	
		
		InternationalServiceEligibilityCheckResult checkResult = account.checkInternationalServiceEligibility();
		assertEquals(checkResult.getDepositAmount(), 0, 0);
		assertTrue(checkResult.isEligibleForInternationalDialing());
		assertTrue(checkResult.isEligibleForInternationalRoaming());		
	}			

	/**
	 * This test corresponds to the input described in the formatted table javadoc'ed
	 * for this class.  The method name suffix follows the same sequence as the table. 
	 * 
	 * @param account - partial mock class
	 * @throws Exception
	 */

	@Test
	public void testScorpion7(@Mocked(methods = {"(TMProvider, AccountInfo)", "getInternationalServiceEligibilityCheckCriteria"}) final TMAccount account) throws Exception {
		final InternationalServiceEligibilityCheckCriteria criteria = new InternationalServiceEligibilityCheckCriteria();
		criteria.setAccountCombinedType("CI");		
		criteria.setBrandId(Brand.BRAND_ID_CLEARNET);
		criteria.setCreditClass("C");
		criteria.setNewAccount(true);		

		new NonStrictExpectations() {
			{
				invoke(ProviderAspect.class, "aspectOf"); returns(providerAspectMock);
				providerAspectMock.getMethodMonitorFlagReader(); returns(methodMonitorFlagReaderMock);
				invoke(account, "getInternationalServiceEligibilityCheckCriteria"); returns(criteria);

			}
		};	
		
		InternationalServiceEligibilityCheckResult checkResult = account.checkInternationalServiceEligibility();
		assertEquals(checkResult.getDepositAmount(), 0, 0);
		assertTrue(checkResult.isEligibleForInternationalDialing());
		assertFalse(checkResult.isEligibleForInternationalRoaming());		
	}			
		
	/**
	 * This test corresponds to the input described in the formatted table javadoc'ed
	 * for this class.  The method name suffix follows the same sequence as the table. 
	 * 
	 * @param account - partial mock class
	 * @throws Exception
	 */

	@Test
	public void testScorpion8(@Mocked(methods = {"(TMProvider, AccountInfo)", "getInternationalServiceEligibilityCheckCriteria"}) final TMAccount account) throws Exception {
		final InternationalServiceEligibilityCheckCriteria criteria = new InternationalServiceEligibilityCheckCriteria();
		criteria.setAccountCombinedType("CI");		
		criteria.setBrandId(Brand.BRAND_ID_CLEARNET);
		criteria.setCreditClass("C");
		criteria.setNewAccount(false);
		criteria.setCollectionActivityPresent(true);
		criteria.setTenure(2);

		new NonStrictExpectations() {
			{
				invoke(ProviderAspect.class, "aspectOf"); returns(providerAspectMock);
				providerAspectMock.getMethodMonitorFlagReader(); returns(methodMonitorFlagReaderMock);
				invoke(account, "getInternationalServiceEligibilityCheckCriteria"); returns(criteria);

			}
		};	
		
		InternationalServiceEligibilityCheckResult checkResult = account.checkInternationalServiceEligibility();
		assertEquals(checkResult.getDepositAmount(), 0, 0);
		assertTrue(checkResult.isEligibleForInternationalDialing());
		assertFalse(checkResult.isEligibleForInternationalRoaming());		
	}			
		
	/**
	 * This test corresponds to the input described in the formatted table javadoc'ed
	 * for this class.  The method name suffix follows the same sequence as the table. 
	 * 
	 * @param account - partial mock class
	 * @throws Exception
	 */

	@Test
	public void testScorpion8_1(@Mocked(methods = {"(TMProvider, AccountInfo)", "getInternationalServiceEligibilityCheckCriteria"}) final TMAccount account) throws Exception {
		final InternationalServiceEligibilityCheckCriteria criteria = new InternationalServiceEligibilityCheckCriteria();
		criteria.setAccountCombinedType("CI");		
		criteria.setBrandId(Brand.BRAND_ID_CLEARNET);
		criteria.setCreditClass("C");
		criteria.setNewAccount(false);
		criteria.setCollectionActivityPresent(true);
		criteria.setTenure(3);

		new NonStrictExpectations() {
			{
				invoke(ProviderAspect.class, "aspectOf"); returns(providerAspectMock);
				providerAspectMock.getMethodMonitorFlagReader(); returns(methodMonitorFlagReaderMock);
				invoke(account, "getInternationalServiceEligibilityCheckCriteria"); returns(criteria);

			}
		};	
		
		InternationalServiceEligibilityCheckResult checkResult = account.checkInternationalServiceEligibility();
		assertEquals(checkResult.getDepositAmount(), 0, 0);
		assertTrue(checkResult.isEligibleForInternationalDialing());
		assertFalse(checkResult.isEligibleForInternationalRoaming());		
	}		
	
	/**
	 * This test corresponds to the input described in the formatted table javadoc'ed
	 * for this class.  The method name suffix follows the same sequence as the table. 
	 * 
	 * @param account - partial mock class
	 * @throws Exception
	 */

	@Test
	public void testScorpion9(@Mocked(methods = {"(TMProvider, AccountInfo)", "getInternationalServiceEligibilityCheckCriteria"}) final TMAccount account) throws Exception {
		final InternationalServiceEligibilityCheckCriteria criteria = new InternationalServiceEligibilityCheckCriteria();
		criteria.setAccountCombinedType("CI");		
		criteria.setBrandId(Brand.BRAND_ID_CLEARNET);
		criteria.setCreditClass("C");
		criteria.setNewAccount(false);
		criteria.setCollectionActivityPresent(true);
		criteria.setTenure(4);

		new NonStrictExpectations() {
			{
				invoke(ProviderAspect.class, "aspectOf"); returns(providerAspectMock);
				providerAspectMock.getMethodMonitorFlagReader(); returns(methodMonitorFlagReaderMock);
				invoke(account, "getInternationalServiceEligibilityCheckCriteria"); returns(criteria);

			}
		};	
		
		InternationalServiceEligibilityCheckResult checkResult = account.checkInternationalServiceEligibility();
		assertEquals(checkResult.getDepositAmount(), 0, 0);
		assertTrue(checkResult.isEligibleForInternationalDialing());
		assertFalse(checkResult.isEligibleForInternationalRoaming());		
	}	
	
	/**
	 * This test corresponds to the input described in the formatted table javadoc'ed
	 * for this class.  The method name suffix follows the same sequence as the table. 
	 * 
	 * @param account - partial mock class
	 * @throws Exception
	 */

	@Test
	public void testScorpion10(@Mocked(methods = {"(TMProvider, AccountInfo)", "getInternationalServiceEligibilityCheckCriteria"}) final TMAccount account) throws Exception {
		final InternationalServiceEligibilityCheckCriteria criteria = new InternationalServiceEligibilityCheckCriteria();
		criteria.setAccountCombinedType("CI");		
		criteria.setBrandId(Brand.BRAND_ID_CLEARNET);
		criteria.setCreditClass("C");
		criteria.setNewAccount(false);
		criteria.setCollectionActivityPresent(false);
		criteria.setTenure(2);

		new NonStrictExpectations() {
			{
				invoke(ProviderAspect.class, "aspectOf"); returns(providerAspectMock);
				providerAspectMock.getMethodMonitorFlagReader(); returns(methodMonitorFlagReaderMock);
				invoke(account, "getInternationalServiceEligibilityCheckCriteria"); returns(criteria);

			}
		};	
		
		InternationalServiceEligibilityCheckResult checkResult = account.checkInternationalServiceEligibility();
		assertEquals(checkResult.getDepositAmount(), 0, 0);
		assertTrue(checkResult.isEligibleForInternationalDialing());
		assertFalse(checkResult.isEligibleForInternationalRoaming());		
	}	
	
	/**
	 * This test corresponds to the input described in the formatted table javadoc'ed
	 * for this class.  The method name suffix follows the same sequence as the table. 
	 * 
	 * @param account - partial mock class
	 * @throws Exception
	 */

	@Test
	public void testScorpion10_1(@Mocked(methods = {"(TMProvider, AccountInfo)", "getInternationalServiceEligibilityCheckCriteria"}) final TMAccount account) throws Exception {
		final InternationalServiceEligibilityCheckCriteria criteria = new InternationalServiceEligibilityCheckCriteria();
		criteria.setAccountCombinedType("CI");		
		criteria.setBrandId(Brand.BRAND_ID_CLEARNET);
		criteria.setCreditClass("C");
		criteria.setNewAccount(false);
		criteria.setCollectionActivityPresent(false);
		criteria.setTenure(3);

		new NonStrictExpectations() {
			{
				invoke(ProviderAspect.class, "aspectOf"); returns(providerAspectMock);
				providerAspectMock.getMethodMonitorFlagReader(); returns(methodMonitorFlagReaderMock);
				invoke(account, "getInternationalServiceEligibilityCheckCriteria"); returns(criteria);

			}
		};	
		
		InternationalServiceEligibilityCheckResult checkResult = account.checkInternationalServiceEligibility();
		assertEquals(checkResult.getDepositAmount(), 0, 0);
		assertTrue(checkResult.isEligibleForInternationalDialing());
		assertFalse(checkResult.isEligibleForInternationalRoaming());		
	}		
	/**
	 * This test corresponds to the input described in the formatted table javadoc'ed
	 * for this class.  The method name suffix follows the same sequence as the table. 
	 * 
	 * @param account - partial mock class
	 * @throws Exception
	 */

	@Test
	public void testScorpion11(@Mocked(methods = {"(TMProvider, AccountInfo)", "getInternationalServiceEligibilityCheckCriteria"}) final TMAccount account) throws Exception {
		final InternationalServiceEligibilityCheckCriteria criteria = new InternationalServiceEligibilityCheckCriteria();
		criteria.setAccountCombinedType("CI");		
		criteria.setBrandId(Brand.BRAND_ID_CLEARNET);
		criteria.setCreditClass("C");
		criteria.setNewAccount(false);
		criteria.setCollectionActivityPresent(false);
		criteria.setTenure(4);

		new NonStrictExpectations() {
			{
				invoke(ProviderAspect.class, "aspectOf"); returns(providerAspectMock);
				providerAspectMock.getMethodMonitorFlagReader(); returns(methodMonitorFlagReaderMock);
				invoke(account, "getInternationalServiceEligibilityCheckCriteria"); returns(criteria);

			}
		};	
		
		InternationalServiceEligibilityCheckResult checkResult = account.checkInternationalServiceEligibility();
		assertEquals(checkResult.getDepositAmount(), 0, 0);
		assertTrue(checkResult.isEligibleForInternationalDialing());
		assertTrue(checkResult.isEligibleForInternationalRoaming());		
	}		
	
	/**
	 * This test corresponds to the input described in the formatted table javadoc'ed
	 * for this class.  The method name suffix follows the same sequence as the table. 
	 * 
	 * @param account - partial mock class
	 * @throws Exception
	 */

	@Test
	public void testScorpion12(@Mocked(methods = {"(TMProvider, AccountInfo)", "getInternationalServiceEligibilityCheckCriteria"}) final TMAccount account) throws Exception {
		final InternationalServiceEligibilityCheckCriteria criteria = new InternationalServiceEligibilityCheckCriteria();
		criteria.setAccountCombinedType("CI");		
		criteria.setBrandId(Brand.BRAND_ID_CLEARNET);
		criteria.setCreditClass("X");
		criteria.setNewAccount(true);

		new NonStrictExpectations() {
			{
				invoke(ProviderAspect.class, "aspectOf"); returns(providerAspectMock);
				providerAspectMock.getMethodMonitorFlagReader(); returns(methodMonitorFlagReaderMock);
				invoke(account, "getInternationalServiceEligibilityCheckCriteria"); returns(criteria);

			}
		};	
		
		InternationalServiceEligibilityCheckResult checkResult = account.checkInternationalServiceEligibility();
		assertEquals(checkResult.getDepositAmount(), 0, 0);
		assertTrue(checkResult.isEligibleForInternationalDialing());
		assertFalse(checkResult.isEligibleForInternationalRoaming());		
	}			
	
	/**
	 * This test corresponds to the input described in the formatted table javadoc'ed
	 * for this class.  The method name suffix follows the same sequence as the table. 
	 * 
	 * @param account - partial mock class
	 * @throws Exception
	 */

	@Test
	public void testScorpion13(@Mocked(methods = {"(TMProvider, AccountInfo)", "getInternationalServiceEligibilityCheckCriteria"}) final TMAccount account) throws Exception {
		final InternationalServiceEligibilityCheckCriteria criteria = new InternationalServiceEligibilityCheckCriteria();
		criteria.setAccountCombinedType("CI");		
		criteria.setBrandId(Brand.BRAND_ID_CLEARNET);
		criteria.setCreditClass("X");
		criteria.setNewAccount(true);
		criteria.setCollectionActivityPresent(true);
		criteria.setTenure(5);

		new NonStrictExpectations() {
			{
				invoke(ProviderAspect.class, "aspectOf"); returns(providerAspectMock);
				providerAspectMock.getMethodMonitorFlagReader(); returns(methodMonitorFlagReaderMock);
				invoke(account, "getInternationalServiceEligibilityCheckCriteria"); returns(criteria);

			}
		};	
		
		InternationalServiceEligibilityCheckResult checkResult = account.checkInternationalServiceEligibility();
		assertEquals(checkResult.getDepositAmount(), 0, 0);
		assertTrue(checkResult.isEligibleForInternationalDialing());
		assertFalse(checkResult.isEligibleForInternationalRoaming());		
	}			
	
	/**
	 * This test corresponds to the input described in the formatted table javadoc'ed
	 * for this class.  The method name suffix follows the same sequence as the table. 
	 * 
	 * @param account - partial mock class
	 * @throws Exception
	 */

	@Test
	public void testScorpion13_1(@Mocked(methods = {"(TMProvider, AccountInfo)", "getInternationalServiceEligibilityCheckCriteria"}) final TMAccount account) throws Exception {
		final InternationalServiceEligibilityCheckCriteria criteria = new InternationalServiceEligibilityCheckCriteria();
		criteria.setAccountCombinedType("CI");		
		criteria.setBrandId(Brand.BRAND_ID_CLEARNET);
		criteria.setCreditClass("X");
		criteria.setNewAccount(true);
		criteria.setCollectionActivityPresent(true);
		criteria.setTenure(6);

		new NonStrictExpectations() {
			{
				invoke(ProviderAspect.class, "aspectOf"); returns(providerAspectMock);
				providerAspectMock.getMethodMonitorFlagReader(); returns(methodMonitorFlagReaderMock);
				invoke(account, "getInternationalServiceEligibilityCheckCriteria"); returns(criteria);

			}
		};	
		
		InternationalServiceEligibilityCheckResult checkResult = account.checkInternationalServiceEligibility();
		assertEquals(checkResult.getDepositAmount(), 0, 0);
		assertTrue(checkResult.isEligibleForInternationalDialing());
		assertFalse(checkResult.isEligibleForInternationalRoaming());		
	}		

	/**
	 * This test corresponds to the input described in the formatted table javadoc'ed
	 * for this class.  The method name suffix follows the same sequence as the table. 
	 * 
	 * @param account - partial mock class
	 * @throws Exception
	 */

	@Test
	public void testScorpion14(@Mocked(methods = {"(TMProvider, AccountInfo)", "getInternationalServiceEligibilityCheckCriteria"}) final TMAccount account) throws Exception {
		final InternationalServiceEligibilityCheckCriteria criteria = new InternationalServiceEligibilityCheckCriteria();
		criteria.setAccountCombinedType("CI");		
		criteria.setBrandId(Brand.BRAND_ID_CLEARNET);
		criteria.setCreditClass("X");
		criteria.setNewAccount(true);
		criteria.setCollectionActivityPresent(true);
		criteria.setTenure(7);

		new NonStrictExpectations() {
			{
				invoke(ProviderAspect.class, "aspectOf"); returns(providerAspectMock);
				providerAspectMock.getMethodMonitorFlagReader(); returns(methodMonitorFlagReaderMock);
				invoke(account, "getInternationalServiceEligibilityCheckCriteria"); returns(criteria);

			}
		};	
		
		InternationalServiceEligibilityCheckResult checkResult = account.checkInternationalServiceEligibility();
		assertEquals(checkResult.getDepositAmount(), 0, 0);
		assertTrue(checkResult.isEligibleForInternationalDialing());
		assertFalse(checkResult.isEligibleForInternationalRoaming());		
	}		
		
	/**
	 * This test corresponds to the input described in the formatted table javadoc'ed
	 * for this class.  The method name suffix follows the same sequence as the table. 
	 * 
	 * @param account - partial mock class
	 * @throws Exception
	 */

	@Test
	public void testScorpion15(@Mocked(methods = {"(TMProvider, AccountInfo)", "getInternationalServiceEligibilityCheckCriteria"}) final TMAccount account) throws Exception {
		final InternationalServiceEligibilityCheckCriteria criteria = new InternationalServiceEligibilityCheckCriteria();
		criteria.setAccountCombinedType("CI");		
		criteria.setBrandId(Brand.BRAND_ID_CLEARNET);
		criteria.setCreditClass("X");
		criteria.setNewAccount(false);
		criteria.setCollectionActivityPresent(false);
		criteria.setTenure(5);

		new NonStrictExpectations() {
			{
				invoke(ProviderAspect.class, "aspectOf"); returns(providerAspectMock);
				providerAspectMock.getMethodMonitorFlagReader(); returns(methodMonitorFlagReaderMock);
				invoke(account, "getInternationalServiceEligibilityCheckCriteria"); returns(criteria);

			}
		};	
		
		InternationalServiceEligibilityCheckResult checkResult = account.checkInternationalServiceEligibility();
		assertEquals(checkResult.getDepositAmount(), 0, 0);
		assertTrue(checkResult.isEligibleForInternationalDialing());
		assertFalse(checkResult.isEligibleForInternationalRoaming());		
	}		

	/**
	 * This test corresponds to the input described in the formatted table javadoc'ed
	 * for this class.  The method name suffix follows the same sequence as the table. 
	 * 
	 * @param account - partial mock class
	 * @throws Exception
	 */

	@Test
	public void testScorpion15_1(@Mocked(methods = {"(TMProvider, AccountInfo)", "getInternationalServiceEligibilityCheckCriteria"}) final TMAccount account) throws Exception {
		final InternationalServiceEligibilityCheckCriteria criteria = new InternationalServiceEligibilityCheckCriteria();
		criteria.setAccountCombinedType("CI");		
		criteria.setBrandId(Brand.BRAND_ID_CLEARNET);
		criteria.setCreditClass("X");
		criteria.setNewAccount(false);
		criteria.setCollectionActivityPresent(false);
		criteria.setTenure(6);

		new NonStrictExpectations() {
			{
				invoke(ProviderAspect.class, "aspectOf"); returns(providerAspectMock);
				providerAspectMock.getMethodMonitorFlagReader(); returns(methodMonitorFlagReaderMock);
				invoke(account, "getInternationalServiceEligibilityCheckCriteria"); returns(criteria);

			}
		};	
		
		InternationalServiceEligibilityCheckResult checkResult = account.checkInternationalServiceEligibility();
		assertEquals(checkResult.getDepositAmount(), 0, 0);
		assertTrue(checkResult.isEligibleForInternationalDialing());
		assertFalse(checkResult.isEligibleForInternationalRoaming());		
	}			
	
	/**
	 * This test corresponds to the input described in the formatted table javadoc'ed
	 * for this class.  The method name suffix follows the same sequence as the table. 
	 * 
	 * @param account - partial mock class
	 * @throws Exception
	 */

	@Test
	public void testScorpion16(@Mocked(methods = {"(TMProvider, AccountInfo)", "getInternationalServiceEligibilityCheckCriteria"}) final TMAccount account) throws Exception {
		final InternationalServiceEligibilityCheckCriteria criteria = new InternationalServiceEligibilityCheckCriteria();
		criteria.setAccountCombinedType("CI");		
		criteria.setBrandId(Brand.BRAND_ID_CLEARNET);
		criteria.setCreditClass("X");
		criteria.setNewAccount(false);
		criteria.setCollectionActivityPresent(false);
		criteria.setTenure(7);

		new NonStrictExpectations() {
			{
				invoke(ProviderAspect.class, "aspectOf"); returns(providerAspectMock);
				providerAspectMock.getMethodMonitorFlagReader(); returns(methodMonitorFlagReaderMock);
				invoke(account, "getInternationalServiceEligibilityCheckCriteria"); returns(criteria);

			}
		};	
		
		InternationalServiceEligibilityCheckResult checkResult = account.checkInternationalServiceEligibility();
		assertEquals(checkResult.getDepositAmount(), 0, 0);
		assertTrue(checkResult.isEligibleForInternationalDialing());
		assertTrue(checkResult.isEligibleForInternationalRoaming());		
	}			
	
	/**
	 * This test corresponds to the input described in the formatted table javadoc'ed
	 * for this class.  The method name suffix follows the same sequence as the table. 
	 * 
	 * @param account - partial mock class
	 * @throws Exception
	 */

	@Test
	public void testScorpion17(@Mocked(methods = {"(TMProvider, AccountInfo)", "getInternationalServiceEligibilityCheckCriteria"}) final TMAccount account) throws Exception {
		final InternationalServiceEligibilityCheckCriteria criteria = new InternationalServiceEligibilityCheckCriteria();
		criteria.setAccountCombinedType("CI");		
		criteria.setBrandId(Brand.BRAND_ID_CLEARNET);

		new NonStrictExpectations() {
			{
				invoke(ProviderAspect.class, "aspectOf"); returns(providerAspectMock);
				providerAspectMock.getMethodMonitorFlagReader(); returns(methodMonitorFlagReaderMock);
				invoke(account, "getInternationalServiceEligibilityCheckCriteria"); returns(criteria);

			}
		};	
		
		InternationalServiceEligibilityCheckResult checkResult = account.checkInternationalServiceEligibility();
		assertEquals(checkResult.getDepositAmount(), 0, 0);
		assertTrue(checkResult.isEligibleForInternationalDialing());
		assertFalse(checkResult.isEligibleForInternationalRoaming());		
	}	
	
	/**
	 * This test corresponds to the input described in the formatted table javadoc'ed
	 * for this class.  The method name suffix follows the same sequence as the table. 
	 * 
	 * @param account - partial mock class
	 * @throws Exception
	 */

	@Test
	public void testPenguinX(@Mocked(methods = {"(TMProvider, AccountInfo)", "getInternationalServiceEligibilityCheckCriteria"}) final TMAccount account) throws Exception {
		final InternationalServiceEligibilityCheckCriteria criteria = new InternationalServiceEligibilityCheckCriteria();
		criteria.setAccountCombinedType("CI");		
		criteria.setBrandId(Brand.BRAND_ID_WALMART);

		new NonStrictExpectations() {
			{
				invoke(ProviderAspect.class, "aspectOf"); returns(providerAspectMock);
				providerAspectMock.getMethodMonitorFlagReader(); returns(methodMonitorFlagReaderMock);
				invoke(account, "getInternationalServiceEligibilityCheckCriteria"); returns(criteria);

			}
		};	
		
		InternationalServiceEligibilityCheckResult checkResult = account.checkInternationalServiceEligibility();
		assertEquals(checkResult.getDepositAmount(), 200, 0);
		assertTrue(checkResult.isEligibleForInternationalDialing());
		assertFalse(checkResult.isEligibleForInternationalRoaming());		
	}		
	
	/**
	 * This test corresponds to the input described in the formatted table javadoc'ed
	 * for this class.  The method name suffix follows the same sequence as the table. 
	 * 
	 * @param account - partial mock class
	 * @throws Exception
	 */

	@Test
	public void testPenguin1(@Mocked(methods = {"(TMProvider, AccountInfo)", "getInternationalServiceEligibilityCheckCriteria"}) final TMAccount account) throws Exception {
		final InternationalServiceEligibilityCheckCriteria criteria = new InternationalServiceEligibilityCheckCriteria();
		criteria.setAccountCombinedType("CI");		
		criteria.setBrandId(Brand.BRAND_ID_WALMART);
		criteria.setNewAccount(true);

		new NonStrictExpectations() {
			{
				invoke(ProviderAspect.class, "aspectOf"); returns(providerAspectMock);
				providerAspectMock.getMethodMonitorFlagReader(); returns(methodMonitorFlagReaderMock);
				invoke(account, "getInternationalServiceEligibilityCheckCriteria"); returns(criteria);

			}
		};	
		
		InternationalServiceEligibilityCheckResult checkResult = account.checkInternationalServiceEligibility();
		assertEquals(checkResult.getDepositAmount(), 200, 0);
		assertFalse(checkResult.isEligibleForInternationalDialing());
		assertFalse(checkResult.isEligibleForInternationalRoaming());		
	}		
		
	/**
	 * This test corresponds to the input described in the formatted table javadoc'ed
	 * for this class.  The method name suffix follows the same sequence as the table. 
	 * 
	 * @param account - partial mock class
	 * @throws Exception
	 */

	@Test
	public void testPenguin2(@Mocked(methods = {"(TMProvider, AccountInfo)", "getInternationalServiceEligibilityCheckCriteria"}) final TMAccount account) throws Exception {
		final InternationalServiceEligibilityCheckCriteria criteria = new InternationalServiceEligibilityCheckCriteria();
		criteria.setAccountCombinedType("CI");		
		criteria.setBrandId(Brand.BRAND_ID_WALMART);
		criteria.setNewAccount(false);
		criteria.setCollectionActivityPresent(false);
		criteria.setTenure(0);

		new NonStrictExpectations() {
			{
				invoke(ProviderAspect.class, "aspectOf"); returns(providerAspectMock);
				providerAspectMock.getMethodMonitorFlagReader(); returns(methodMonitorFlagReaderMock);
				invoke(account, "getInternationalServiceEligibilityCheckCriteria"); returns(criteria);

			}
		};	
		
		InternationalServiceEligibilityCheckResult checkResult = account.checkInternationalServiceEligibility();
		assertEquals(checkResult.getDepositAmount(), 200, 0);
		assertTrue(checkResult.isEligibleForInternationalDialing());
		assertFalse(checkResult.isEligibleForInternationalRoaming());		
	}	
	
	/**
	 * This test corresponds to the input described in the formatted table javadoc'ed
	 * for this class.  The method name suffix follows the same sequence as the table. 
	 * 
	 * @param account - partial mock class
	 * @throws Exception
	 */

	@Test
	public void testPenguin2_1(@Mocked(methods = {"(TMProvider, AccountInfo)", "getInternationalServiceEligibilityCheckCriteria"}) final TMAccount account) throws Exception {
		final InternationalServiceEligibilityCheckCriteria criteria = new InternationalServiceEligibilityCheckCriteria();
		criteria.setAccountCombinedType("CI");		
		criteria.setBrandId(Brand.BRAND_ID_WALMART);
		criteria.setNewAccount(false);
		criteria.setCollectionActivityPresent(false);
		criteria.setTenure(5);

		new NonStrictExpectations() {
			{
				invoke(ProviderAspect.class, "aspectOf"); returns(providerAspectMock);
				providerAspectMock.getMethodMonitorFlagReader(); returns(methodMonitorFlagReaderMock);
				invoke(account, "getInternationalServiceEligibilityCheckCriteria"); returns(criteria);

			}
		};	
		
		InternationalServiceEligibilityCheckResult checkResult = account.checkInternationalServiceEligibility();
		assertEquals(checkResult.getDepositAmount(), 200, 0);
		assertTrue(checkResult.isEligibleForInternationalDialing());
		assertFalse(checkResult.isEligibleForInternationalRoaming());		
	}		
	
	/**
	 * This test corresponds to the input described in the formatted table javadoc'ed
	 * for this class.  The method name suffix follows the same sequence as the table. 
	 * 
	 * @param account - partial mock class
	 * @throws Exception
	 */

	@Test
	public void testPenguin3(@Mocked(methods = {"(TMProvider, AccountInfo)", "getInternationalServiceEligibilityCheckCriteria"}) final TMAccount account) throws Exception {
		final InternationalServiceEligibilityCheckCriteria criteria = new InternationalServiceEligibilityCheckCriteria();
		criteria.setAccountCombinedType("CI");		
		criteria.setBrandId(Brand.BRAND_ID_WALMART);
		criteria.setNewAccount(false);
		criteria.setCollectionActivityPresent(false);
		criteria.setTenure(6);

		new NonStrictExpectations() {
			{
				invoke(ProviderAspect.class, "aspectOf"); returns(providerAspectMock);
				providerAspectMock.getMethodMonitorFlagReader(); returns(methodMonitorFlagReaderMock);
				invoke(account, "getInternationalServiceEligibilityCheckCriteria"); returns(criteria);

			}
		};	
		
		InternationalServiceEligibilityCheckResult checkResult = account.checkInternationalServiceEligibility();
		assertEquals(checkResult.getDepositAmount(), 50, 0);
		assertTrue(checkResult.isEligibleForInternationalDialing());
		assertFalse(checkResult.isEligibleForInternationalRoaming());		
	}	
	
	/**
	 * This test corresponds to the input described in the formatted table javadoc'ed
	 * for this class.  The method name suffix follows the same sequence as the table. 
	 * 
	 * @param account - partial mock class
	 * @throws Exception
	 */

	@Test
	public void testPenguin3_1(@Mocked(methods = {"(TMProvider, AccountInfo)", "getInternationalServiceEligibilityCheckCriteria"}) final TMAccount account) throws Exception {
		final InternationalServiceEligibilityCheckCriteria criteria = new InternationalServiceEligibilityCheckCriteria();
		criteria.setAccountCombinedType("CI");		
		criteria.setBrandId(Brand.BRAND_ID_WALMART);
		criteria.setNewAccount(false);
		criteria.setCollectionActivityPresent(false);
		criteria.setTenure(11);

		new NonStrictExpectations() {
			{
				invoke(ProviderAspect.class, "aspectOf"); returns(providerAspectMock);
				providerAspectMock.getMethodMonitorFlagReader(); returns(methodMonitorFlagReaderMock);
				invoke(account, "getInternationalServiceEligibilityCheckCriteria"); returns(criteria);

			}
		};	
		
		InternationalServiceEligibilityCheckResult checkResult = account.checkInternationalServiceEligibility();
		assertEquals(checkResult.getDepositAmount(), 50, 0);
		assertTrue(checkResult.isEligibleForInternationalDialing());
		assertFalse(checkResult.isEligibleForInternationalRoaming());		
	}		
	
	/**
	 * This test corresponds to the input described in the formatted table javadoc'ed
	 * for this class.  The method name suffix follows the same sequence as the table. 
	 * 
	 * @param account - partial mock class
	 * @throws Exception
	 */

	@Test
	public void testPenguin4(@Mocked(methods = {"(TMProvider, AccountInfo)", "getInternationalServiceEligibilityCheckCriteria"}) final TMAccount account) throws Exception {
		final InternationalServiceEligibilityCheckCriteria criteria = new InternationalServiceEligibilityCheckCriteria();
		criteria.setAccountCombinedType("CI");		
		criteria.setBrandId(Brand.BRAND_ID_WALMART);
		criteria.setNewAccount(false);
		criteria.setCollectionActivityPresent(false);
		criteria.setTenure(12);

		new NonStrictExpectations() {
			{
				invoke(ProviderAspect.class, "aspectOf"); returns(providerAspectMock);
				providerAspectMock.getMethodMonitorFlagReader(); returns(methodMonitorFlagReaderMock);
				invoke(account, "getInternationalServiceEligibilityCheckCriteria"); returns(criteria);

			}
		};	
		
		InternationalServiceEligibilityCheckResult checkResult = account.checkInternationalServiceEligibility();
		assertEquals(checkResult.getDepositAmount(), 0, 0);
		assertTrue(checkResult.isEligibleForInternationalDialing());
		assertFalse(checkResult.isEligibleForInternationalRoaming());		
	}		
	
	/**
	 * This test corresponds to the input described in the formatted table javadoc'ed
	 * for this class.  The method name suffix follows the same sequence as the table. 
	 * 
	 * @param account - partial mock class
	 * @throws Exception
	 */

	@Test
	public void testPenguin4_1(@Mocked(methods = {"(TMProvider, AccountInfo)", "getInternationalServiceEligibilityCheckCriteria"}) final TMAccount account) throws Exception {
		final InternationalServiceEligibilityCheckCriteria criteria = new InternationalServiceEligibilityCheckCriteria();
		criteria.setAccountCombinedType("CI");		
		criteria.setBrandId(Brand.BRAND_ID_WALMART);
		criteria.setNewAccount(false);
		criteria.setCollectionActivityPresent(false);
		criteria.setTenure(13);

		new NonStrictExpectations() {
			{
				invoke(ProviderAspect.class, "aspectOf"); returns(providerAspectMock);
				providerAspectMock.getMethodMonitorFlagReader(); returns(methodMonitorFlagReaderMock);
				invoke(account, "getInternationalServiceEligibilityCheckCriteria"); returns(criteria);

			}
		};	
		
		InternationalServiceEligibilityCheckResult checkResult = account.checkInternationalServiceEligibility();
		assertEquals(checkResult.getDepositAmount(), 0, 0);
		assertTrue(checkResult.isEligibleForInternationalDialing());
		assertFalse(checkResult.isEligibleForInternationalRoaming());		
	}		
	
	/**
	 * This test corresponds to the input described in the formatted table javadoc'ed
	 * for this class.  The method name suffix follows the same sequence as the table. 
	 * 
	 * @param account - partial mock class
	 * @throws Exception
	 */

	@Test
	public void testPenguin5(@Mocked(methods = {"(TMProvider, AccountInfo)", "getInternationalServiceEligibilityCheckCriteria"}) final TMAccount account) throws Exception {
		final InternationalServiceEligibilityCheckCriteria criteria = new InternationalServiceEligibilityCheckCriteria();
		criteria.setAccountCombinedType("CI");		
		criteria.setBrandId(Brand.BRAND_ID_WALMART);
		criteria.setNewAccount(false);
		criteria.setCollectionActivityPresent(true);
		criteria.setTenure(0);

		new NonStrictExpectations() {
			{
				invoke(ProviderAspect.class, "aspectOf"); returns(providerAspectMock);
				providerAspectMock.getMethodMonitorFlagReader(); returns(methodMonitorFlagReaderMock);
				invoke(account, "getInternationalServiceEligibilityCheckCriteria"); returns(criteria);

			}
		};	
		
		InternationalServiceEligibilityCheckResult checkResult = account.checkInternationalServiceEligibility();
		assertEquals(checkResult.getDepositAmount(), 200, 0);
		assertTrue(checkResult.isEligibleForInternationalDialing());
		assertFalse(checkResult.isEligibleForInternationalRoaming());		
	}		

	/**
	 * This test corresponds to the input described in the formatted table javadoc'ed
	 * for this class.  The method name suffix follows the same sequence as the table. 
	 * 
	 * @param account - partial mock class
	 * @throws Exception
	 */

	@Test
	public void testPenguin5_1(@Mocked(methods = {"(TMProvider, AccountInfo)", "getInternationalServiceEligibilityCheckCriteria"}) final TMAccount account) throws Exception {
		final InternationalServiceEligibilityCheckCriteria criteria = new InternationalServiceEligibilityCheckCriteria();
		criteria.setAccountCombinedType("CI");		
		criteria.setBrandId(Brand.BRAND_ID_WALMART);
		criteria.setNewAccount(false);
		criteria.setCollectionActivityPresent(true);
		criteria.setTenure(5);

		new NonStrictExpectations() {
			{
				invoke(ProviderAspect.class, "aspectOf"); returns(providerAspectMock);
				providerAspectMock.getMethodMonitorFlagReader(); returns(methodMonitorFlagReaderMock);
				invoke(account, "getInternationalServiceEligibilityCheckCriteria"); returns(criteria);

			}
		};	
		
		InternationalServiceEligibilityCheckResult checkResult = account.checkInternationalServiceEligibility();
		assertEquals(checkResult.getDepositAmount(), 200, 0);
		assertTrue(checkResult.isEligibleForInternationalDialing());
		assertFalse(checkResult.isEligibleForInternationalRoaming());		
	}		
	
	/**
	 * This test corresponds to the input described in the formatted table javadoc'ed
	 * for this class.  The method name suffix follows the same sequence as the table. 
	 * 
	 * @param account - partial mock class
	 * @throws Exception
	 */

	@Test
	public void testPenguin6(@Mocked(methods = {"(TMProvider, AccountInfo)", "getInternationalServiceEligibilityCheckCriteria"}) final TMAccount account) throws Exception {
		final InternationalServiceEligibilityCheckCriteria criteria = new InternationalServiceEligibilityCheckCriteria();
		criteria.setAccountCombinedType("CI");		
		criteria.setBrandId(Brand.BRAND_ID_WALMART);
		criteria.setNewAccount(false);
		criteria.setCollectionActivityPresent(true);
		criteria.setTenure(6);

		new NonStrictExpectations() {
			{
				invoke(ProviderAspect.class, "aspectOf"); returns(providerAspectMock);
				providerAspectMock.getMethodMonitorFlagReader(); returns(methodMonitorFlagReaderMock);
				invoke(account, "getInternationalServiceEligibilityCheckCriteria"); returns(criteria);

			}
		};	
		
		InternationalServiceEligibilityCheckResult checkResult = account.checkInternationalServiceEligibility();
		assertEquals(checkResult.getDepositAmount(), 200, 0);
		assertTrue(checkResult.isEligibleForInternationalDialing());
		assertFalse(checkResult.isEligibleForInternationalRoaming());		
	}		
	
	/**
	 * This test corresponds to the input described in the formatted table javadoc'ed
	 * for this class.  The method name suffix follows the same sequence as the table. 
	 * 
	 * @param account - partial mock class
	 * @throws Exception
	 */

	@Test
	public void testPenguin6_1(@Mocked(methods = {"(TMProvider, AccountInfo)", "getInternationalServiceEligibilityCheckCriteria"}) final TMAccount account) throws Exception {
		final InternationalServiceEligibilityCheckCriteria criteria = new InternationalServiceEligibilityCheckCriteria();
		criteria.setAccountCombinedType("CI");		
		criteria.setBrandId(Brand.BRAND_ID_WALMART);
		criteria.setNewAccount(false);
		criteria.setCollectionActivityPresent(true);
		criteria.setTenure(11);

		new NonStrictExpectations() {
			{
				invoke(ProviderAspect.class, "aspectOf"); returns(providerAspectMock);
				providerAspectMock.getMethodMonitorFlagReader(); returns(methodMonitorFlagReaderMock);
				invoke(account, "getInternationalServiceEligibilityCheckCriteria"); returns(criteria);

			}
		};	
		
		InternationalServiceEligibilityCheckResult checkResult = account.checkInternationalServiceEligibility();
		assertEquals(checkResult.getDepositAmount(), 200, 0);
		assertTrue(checkResult.isEligibleForInternationalDialing());
		assertFalse(checkResult.isEligibleForInternationalRoaming());		
	}	
	
	/**
	 * This test corresponds to the input described in the formatted table javadoc'ed
	 * for this class.  The method name suffix follows the same sequence as the table. 
	 * 
	 * @param account - partial mock class
	 * @throws Exception
	 */

	@Test
	public void testPenguin7(@Mocked(methods = {"(TMProvider, AccountInfo)", "getInternationalServiceEligibilityCheckCriteria"}) final TMAccount account) throws Exception {
		final InternationalServiceEligibilityCheckCriteria criteria = new InternationalServiceEligibilityCheckCriteria();
		criteria.setAccountCombinedType("CI");		
		criteria.setBrandId(Brand.BRAND_ID_WALMART);
		criteria.setNewAccount(false);
		criteria.setCollectionActivityPresent(true);
		criteria.setTenure(12);

		new NonStrictExpectations() {
			{
				invoke(ProviderAspect.class, "aspectOf"); returns(providerAspectMock);
				providerAspectMock.getMethodMonitorFlagReader(); returns(methodMonitorFlagReaderMock);
				invoke(account, "getInternationalServiceEligibilityCheckCriteria"); returns(criteria);

			}
		};	
		
		InternationalServiceEligibilityCheckResult checkResult = account.checkInternationalServiceEligibility();
		assertEquals(checkResult.getDepositAmount(), 50, 0);
		assertTrue(checkResult.isEligibleForInternationalDialing());
		assertFalse(checkResult.isEligibleForInternationalRoaming());		
	}	
	
	/**
	 * This test corresponds to the input described in the formatted table javadoc'ed
	 * for this class.  The method name suffix follows the same sequence as the table. 
	 * 
	 * @param account - partial mock class
	 * @throws Exception
	 */

	@Test
	public void testPenguin7_1(@Mocked(methods = {"(TMProvider, AccountInfo)", "getInternationalServiceEligibilityCheckCriteria"}) final TMAccount account) throws Exception {
		final InternationalServiceEligibilityCheckCriteria criteria = new InternationalServiceEligibilityCheckCriteria();
		criteria.setAccountCombinedType("CI");		
		criteria.setBrandId(Brand.BRAND_ID_WALMART);
		criteria.setNewAccount(false);
		criteria.setCollectionActivityPresent(true);
		criteria.setTenure(13);

		new NonStrictExpectations() {
			{
				invoke(ProviderAspect.class, "aspectOf"); returns(providerAspectMock);
				providerAspectMock.getMethodMonitorFlagReader(); returns(methodMonitorFlagReaderMock);
				invoke(account, "getInternationalServiceEligibilityCheckCriteria"); returns(criteria);

			}
		};	
		
		InternationalServiceEligibilityCheckResult checkResult = account.checkInternationalServiceEligibility();
		assertEquals(checkResult.getDepositAmount(), 50, 0);
		assertTrue(checkResult.isEligibleForInternationalDialing());
		assertFalse(checkResult.isEligibleForInternationalRoaming());		
	}		
	
	/**
	 * This test corresponds to the input described in the formatted table javadoc'ed
	 * for this class.  The method name suffix follows the same sequence as the table. 
	 * 
	 * @param account - partial mock class
	 * @throws Exception
	 */

	@Test
	public void testTBSBA(@Mocked(methods = {"(TMProvider, AccountInfo)", "getInternationalServiceEligibilityCheckCriteria"}) final TMAccount account) throws Exception {		
		final InternationalServiceEligibilityCheckCriteria criteria = new InternationalServiceEligibilityCheckCriteria();
		criteria.setAccountCombinedType("BA");
		criteria.setBrandId(Brand.BRAND_ID_KOODO);
		criteria.setCreditClass("B");
		criteria.setNewAccount(true);

		new NonStrictExpectations() {
			{
				invoke(ProviderAspect.class, "aspectOf"); returns(providerAspectMock);
				providerAspectMock.getMethodMonitorFlagReader(); returns(methodMonitorFlagReaderMock);
				invoke(account, "getInternationalServiceEligibilityCheckCriteria"); returns(criteria);

			}
		};	
		
		InternationalServiceEligibilityCheckResult checkResult = account.checkInternationalServiceEligibility();
		assertEquals(checkResult.getDepositAmount(), 0, 0);
		assertTrue(checkResult.isEligibleForInternationalDialing());
		assertTrue(checkResult.isEligibleForInternationalRoaming());		
	}



	@Test
	public void testTBSBA0(@Mocked(methods = {"(TMProvider, AccountInfo)", "getInternationalServiceEligibilityCheckCriteria"}) final TMAccount account) throws Exception {		
		final InternationalServiceEligibilityCheckCriteria criteria = new InternationalServiceEligibilityCheckCriteria();
		criteria.setAccountCombinedType("BA");
		criteria.setBrandId(Brand.BRAND_ID_TELUS);
		criteria.setCreditClass("B");
		criteria.setNewAccount(false);
		criteria.setCollectionActivityPresent(true);

		new NonStrictExpectations() {
			{
				invoke(ProviderAspect.class, "aspectOf"); returns(providerAspectMock);
				providerAspectMock.getMethodMonitorFlagReader(); returns(methodMonitorFlagReaderMock);
				invoke(account, "getInternationalServiceEligibilityCheckCriteria"); returns(criteria);

			}
		};	
		
		InternationalServiceEligibilityCheckResult checkResult = account.checkInternationalServiceEligibility();
		assertEquals(checkResult.getDepositAmount(), 200, 0);
		assertTrue(checkResult.isEligibleForInternationalDialing());
		assertTrue(checkResult.isEligibleForInternationalRoaming());		
	}


	@Test
	public void testTBSBA1(@Mocked(methods = {"(TMProvider, AccountInfo)", "getInternationalServiceEligibilityCheckCriteria"}) final TMAccount account) throws Exception {		
		final InternationalServiceEligibilityCheckCriteria criteria = new InternationalServiceEligibilityCheckCriteria();
		criteria.setAccountCombinedType("BA");
		criteria.setBrandId(Brand.BRAND_ID_TELUS);
		criteria.setCreditClass("C");
		criteria.setNewAccount(false);
		criteria.setCollectionActivityPresent(true);

		new NonStrictExpectations() {
			{
				invoke(ProviderAspect.class, "aspectOf"); returns(providerAspectMock);
				providerAspectMock.getMethodMonitorFlagReader(); returns(methodMonitorFlagReaderMock);
				invoke(account, "getInternationalServiceEligibilityCheckCriteria"); returns(criteria);

			}
		};	
		
		InternationalServiceEligibilityCheckResult checkResult = account.checkInternationalServiceEligibility();
		assertEquals(checkResult.getDepositAmount(), 200, 0);
		assertTrue(checkResult.isEligibleForInternationalDialing());
		assertTrue(checkResult.isEligibleForInternationalRoaming());		
	}



	@Test
	public void testTBSBA2(@Mocked(methods = {"(TMProvider, AccountInfo)", "getInternationalServiceEligibilityCheckCriteria"}) final TMAccount account) throws Exception {		
		final InternationalServiceEligibilityCheckCriteria criteria = new InternationalServiceEligibilityCheckCriteria();
		criteria.setAccountCombinedType("BA");
		criteria.setBrandId(Brand.BRAND_ID_TELUS);
		criteria.setCreditClass("B");
		criteria.setNewAccount(true);
		criteria.setCollectionActivityPresent(true);

		new NonStrictExpectations() {
			{
				invoke(ProviderAspect.class, "aspectOf"); returns(providerAspectMock);
				providerAspectMock.getMethodMonitorFlagReader(); returns(methodMonitorFlagReaderMock);
				invoke(account, "getInternationalServiceEligibilityCheckCriteria"); returns(criteria);

			}
		};	
		
		InternationalServiceEligibilityCheckResult checkResult = account.checkInternationalServiceEligibility();
		assertEquals(checkResult.getDepositAmount(), 0, 0);
		assertTrue(checkResult.isEligibleForInternationalDialing());
		assertTrue(checkResult.isEligibleForInternationalRoaming());		
	}


	@Test
	public void testTBSBA3(@Mocked(methods = {"(TMProvider, AccountInfo)", "getInternationalServiceEligibilityCheckCriteria"}) final TMAccount account) throws Exception {		
		final InternationalServiceEligibilityCheckCriteria criteria = new InternationalServiceEligibilityCheckCriteria();
		criteria.setAccountCombinedType("BA");
		criteria.setBrandId(Brand.BRAND_ID_KOODO);
		criteria.setCreditClass("B");
		criteria.setNewAccount(true);
		criteria.setCollectionActivityPresent(true);

		new NonStrictExpectations() {
			{
				invoke(ProviderAspect.class, "aspectOf"); returns(providerAspectMock);
				providerAspectMock.getMethodMonitorFlagReader(); returns(methodMonitorFlagReaderMock);
				invoke(account, "getInternationalServiceEligibilityCheckCriteria"); returns(criteria);

			}
		};	
		
		InternationalServiceEligibilityCheckResult checkResult = account.checkInternationalServiceEligibility();
		assertEquals(checkResult.getDepositAmount(), 0, 0);
		assertTrue(checkResult.isEligibleForInternationalDialing());
		assertTrue(checkResult.isEligibleForInternationalRoaming());		
	}


	@Test
	public void testTBSBN(@Mocked(methods = {"(TMProvider, AccountInfo)", "getInternationalServiceEligibilityCheckCriteria"}) final TMAccount account) throws Exception {		
		final InternationalServiceEligibilityCheckCriteria criteria = new InternationalServiceEligibilityCheckCriteria();
		criteria.setAccountCombinedType("BN");
		criteria.setBrandId(Brand.BRAND_ID_TELUS);
		criteria.setCreditClass("B");
		criteria.setNewAccount(true);

		new NonStrictExpectations() {
			{
				invoke(ProviderAspect.class, "aspectOf"); returns(providerAspectMock);
				providerAspectMock.getMethodMonitorFlagReader(); returns(methodMonitorFlagReaderMock);
				invoke(account, "getInternationalServiceEligibilityCheckCriteria"); returns(criteria);

			}
		};	
		
		InternationalServiceEligibilityCheckResult checkResult = account.checkInternationalServiceEligibility();
		assertEquals(checkResult.getDepositAmount(), 0, 0);
		assertTrue(checkResult.isEligibleForInternationalDialing());
		assertTrue(checkResult.isEligibleForInternationalRoaming());		
	}



	@Test
	public void testTBSBN0(@Mocked(methods = {"(TMProvider, AccountInfo)", "getInternationalServiceEligibilityCheckCriteria"}) final TMAccount account) throws Exception {		
		final InternationalServiceEligibilityCheckCriteria criteria = new InternationalServiceEligibilityCheckCriteria();
		criteria.setAccountCombinedType("BN");
		criteria.setBrandId(Brand.BRAND_ID_TELUS);
		criteria.setCreditClass("B");
		criteria.setNewAccount(false);
		criteria.setCollectionActivityPresent(true);

		new NonStrictExpectations() {
			{
				invoke(ProviderAspect.class, "aspectOf"); returns(providerAspectMock);
				providerAspectMock.getMethodMonitorFlagReader(); returns(methodMonitorFlagReaderMock);
				invoke(account, "getInternationalServiceEligibilityCheckCriteria"); returns(criteria);

			}
		};	
		
		InternationalServiceEligibilityCheckResult checkResult = account.checkInternationalServiceEligibility();
		assertEquals(checkResult.getDepositAmount(), 200, 0);
		assertTrue(checkResult.isEligibleForInternationalDialing());
		assertTrue(checkResult.isEligibleForInternationalRoaming());		
	}


	@Test
	public void testTBSBN1(@Mocked(methods = {"(TMProvider, AccountInfo)", "getInternationalServiceEligibilityCheckCriteria"}) final TMAccount account) throws Exception {		
		final InternationalServiceEligibilityCheckCriteria criteria = new InternationalServiceEligibilityCheckCriteria();
		criteria.setAccountCombinedType("BN");
		criteria.setBrandId(Brand.BRAND_ID_TELUS);
		criteria.setCreditClass("C");
		criteria.setNewAccount(false);
		criteria.setCollectionActivityPresent(true);

		new NonStrictExpectations() {
			{
				invoke(ProviderAspect.class, "aspectOf"); returns(providerAspectMock);
				providerAspectMock.getMethodMonitorFlagReader(); returns(methodMonitorFlagReaderMock);
				invoke(account, "getInternationalServiceEligibilityCheckCriteria"); returns(criteria);

			}
		};	
		
		InternationalServiceEligibilityCheckResult checkResult = account.checkInternationalServiceEligibility();
		assertEquals(checkResult.getDepositAmount(), 200, 0);
		assertTrue(checkResult.isEligibleForInternationalDialing());
		assertTrue(checkResult.isEligibleForInternationalRoaming());		
	}



	@Test
	public void testTBSBN2(@Mocked(methods = {"(TMProvider, AccountInfo)", "getInternationalServiceEligibilityCheckCriteria"}) final TMAccount account) throws Exception {		
		final InternationalServiceEligibilityCheckCriteria criteria = new InternationalServiceEligibilityCheckCriteria();
		criteria.setAccountCombinedType("BN");
		criteria.setBrandId(Brand.BRAND_ID_TELUS);
		criteria.setCreditClass("B");
		criteria.setNewAccount(true);
		criteria.setCollectionActivityPresent(true);

		new NonStrictExpectations() {
			{
				invoke(ProviderAspect.class, "aspectOf"); returns(providerAspectMock);
				providerAspectMock.getMethodMonitorFlagReader(); returns(methodMonitorFlagReaderMock);
				invoke(account, "getInternationalServiceEligibilityCheckCriteria"); returns(criteria);

			}
		};	
		
		InternationalServiceEligibilityCheckResult checkResult = account.checkInternationalServiceEligibility();
		assertEquals(checkResult.getDepositAmount(), 0, 0);
		assertTrue(checkResult.isEligibleForInternationalDialing());
		assertTrue(checkResult.isEligibleForInternationalRoaming());		
	}


	@Test
	public void testTBSBN3(@Mocked(methods = {"(TMProvider, AccountInfo)", "getInternationalServiceEligibilityCheckCriteria"}) final TMAccount account) throws Exception {		
		final InternationalServiceEligibilityCheckCriteria criteria = new InternationalServiceEligibilityCheckCriteria();
		criteria.setAccountCombinedType("BN");
		criteria.setBrandId(Brand.BRAND_ID_KOODO);
		criteria.setCreditClass("B");
		criteria.setNewAccount(true);
		criteria.setCollectionActivityPresent(true);

		new NonStrictExpectations() {
			{
				invoke(ProviderAspect.class, "aspectOf"); returns(providerAspectMock);
				providerAspectMock.getMethodMonitorFlagReader(); returns(methodMonitorFlagReaderMock);
				invoke(account, "getInternationalServiceEligibilityCheckCriteria"); returns(criteria);

			}
		};	
		
		InternationalServiceEligibilityCheckResult checkResult = account.checkInternationalServiceEligibility();
		assertEquals(checkResult.getDepositAmount(), 0, 0);
		assertTrue(checkResult.isEligibleForInternationalDialing());
		assertTrue(checkResult.isEligibleForInternationalRoaming());		
	}


	@Test
	public void testTBSBR(@Mocked(methods = {"(TMProvider, AccountInfo)", "getInternationalServiceEligibilityCheckCriteria"}) final TMAccount account) throws Exception {		
		final InternationalServiceEligibilityCheckCriteria criteria = new InternationalServiceEligibilityCheckCriteria();
		criteria.setAccountCombinedType("BR");
		criteria.setBrandId(Brand.BRAND_ID_TELUS);
		criteria.setCreditClass("B");
		criteria.setNewAccount(false);
		criteria.setCollectionActivityPresent(true);

		//criteria.setNewAccount(false);

		new NonStrictExpectations() {
			{
				invoke(ProviderAspect.class, "aspectOf"); returns(providerAspectMock);
				providerAspectMock.getMethodMonitorFlagReader(); returns(methodMonitorFlagReaderMock);
				invoke(account, "getInternationalServiceEligibilityCheckCriteria"); returns(criteria);

			}
		};	
		
		InternationalServiceEligibilityCheckResult checkResult = account.checkInternationalServiceEligibility();
		assertEquals(checkResult.getDepositAmount(), 200, 0);
		assertTrue(checkResult.isEligibleForInternationalDialing());
		assertTrue(checkResult.isEligibleForInternationalRoaming());		
	}

}
