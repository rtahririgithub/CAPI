package com.telus.cmb.productequipment.helper.dao.impl;

import java.math.BigDecimal;
import java.sql.Array;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.StringTokenizer;

import oracle.jdbc.OracleCallableStatement;
import oracle.jdbc.OracleTypes;
import oracle.sql.ARRAY;
import oracle.sql.ArrayDescriptor;

import org.apache.log4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.UncategorizedSQLException;
import org.springframework.jdbc.core.CallableStatementCallback;
import org.springframework.jdbc.core.ConnectionCallback;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.jdbc.core.ResultSetExtractor;

import com.telus.api.ApplicationException;
import com.telus.api.ErrorCodes;
import com.telus.api.SystemCodes;
import com.telus.api.account.AccountManager;
import com.telus.api.account.Subscriber;
import com.telus.api.equipment.Equipment;
import com.telus.api.equipment.EquipmentManager;
import com.telus.api.reference.Province;
import com.telus.cmb.productequipment.EquipmentErrorMessages;
import com.telus.cmb.productequipment.helper.dao.EquipmentHelperDao;
import com.telus.eas.equipment.info.EquipmentInfo;
import com.telus.eas.equipment.info.EquipmentModeInfo;
import com.telus.eas.equipment.info.ProfileInfo;
import com.telus.eas.equipment.info.WarrantyInfo;
import com.telus.eas.subscriber.info.SubscriberInfo;



public class EquipmentHelperDaoImpl implements EquipmentHelperDao{
	private static final Logger LOGGER = Logger.getLogger(EquipmentHelperDaoImpl.class);
	
	private JdbcTemplate knowbilityJdbcTemplate;
	private JdbcTemplate distJdbcTemplate;

	public JdbcTemplate getKnowbilityJdbcTemplate() {
		return knowbilityJdbcTemplate;
	}

	public void setKnowbilityJdbcTemplate(JdbcTemplate knowbilityJdbcTemplate) {
		this.knowbilityJdbcTemplate = knowbilityJdbcTemplate;
	}

	public JdbcTemplate getDistJdbcTemplate() {
		return distJdbcTemplate;
	}
	public void setDistJdbcTemplate(JdbcTemplate distJdbcTemplate) {
		this.distJdbcTemplate = distJdbcTemplate;
	}

	
	

	@Override
	public EquipmentInfo getEquipmentInfobyCapCode(final String capCode,
			final String encodingFormat) throws ApplicationException {
		
		String call = "{call Client_Equipment.GetEquipmentInfoByCapCode(?,?,?,?,?,?,?,?,?,?," +
                                                                          "?,?,?,?,?,?,?,?,?,?, " +
                                                                          "?,?,?,?,?,?,?,?,?,?, " +
                                                                          "?,?,?,?,?,?,?,?,?,?, " +
                                                                          "?,?,?,?,?,?,?, " +
                                                                          "?,?,?,?,?,?,?,?,? )}";
		try{
		return getDistJdbcTemplate().execute(call, new CallableStatementCallback<EquipmentInfo>() {
			
			@Override
			public EquipmentInfo doInCallableStatement(CallableStatement callable) throws SQLException, DataAccessException {

				  EquipmentInfo equipInfo= null;
				  long[] productPromoTypeList = new long[0];
				  String[] coverageRegionList = new String[0];
				  String[] firmwareFeatureList = new String[0];
				  int i=0, j=0;
				  boolean  virtualEquipment = false;
				  boolean  stolenEquipment = false;
				
					callable.setString(1, capCode);
			        callable.setString(2, encodingFormat);
			         callable.registerOutParameter(3,Types.VARCHAR );
			        callable.registerOutParameter(4,Types.VARCHAR );
			        callable.registerOutParameter(5,Types.VARCHAR);
			        callable.registerOutParameter(6,Types.VARCHAR);
			        callable.registerOutParameter(7,Types.VARCHAR);
			        callable.registerOutParameter(8,Types.VARCHAR);
			        callable.registerOutParameter(9,Types.NUMERIC);
			        callable.registerOutParameter(10,Types.NUMERIC);
			        callable.registerOutParameter(11,Types.NUMERIC);
			        callable.registerOutParameter(12,Types.VARCHAR);
			        callable.registerOutParameter(13,Types.NUMERIC);
			        callable.registerOutParameter(14,Types.VARCHAR);
			        callable.registerOutParameter(15,Types.VARCHAR);
			        callable.registerOutParameter(16,Types.NUMERIC);
			        callable.registerOutParameter(17,Types.VARCHAR);
			        callable.registerOutParameter(18,Types.NUMERIC);
			        callable.registerOutParameter(19,Types.VARCHAR);
			        callable.registerOutParameter(20,Types.VARCHAR);
			         callable.registerOutParameter(21,Types.NUMERIC);
			        callable.registerOutParameter(22,Types.VARCHAR);
			        callable.registerOutParameter(23,Types.VARCHAR);
			        callable.registerOutParameter(24,Types.VARCHAR);
			        callable.registerOutParameter(25,Types.VARCHAR);
			        callable.registerOutParameter(26,Types.VARCHAR);
			        callable.registerOutParameter(27,Types.VARCHAR);
			        callable.registerOutParameter(28,Types.VARCHAR);
			        callable.registerOutParameter(29,Types.VARCHAR);
			        callable.registerOutParameter(30,Types.VARCHAR);
			        callable.registerOutParameter(31,Types.VARCHAR);
			        callable.registerOutParameter(32,Types.NUMERIC);
			        callable.registerOutParameter(33,Types.VARCHAR);
			        callable.registerOutParameter(34,Types.VARCHAR);
			        callable.registerOutParameter(35,Types.NUMERIC);
			        callable.registerOutParameter(36,Types.VARCHAR);
			        callable.registerOutParameter(37,Types.VARCHAR);
			        callable.registerOutParameter(38,Types.VARCHAR);
			        callable.registerOutParameter(39,Types.VARCHAR);
			        callable.registerOutParameter(40,Types.VARCHAR);
			        callable.registerOutParameter(41,Types.NUMERIC);
			        callable.registerOutParameter(42,Types.VARCHAR); // pager - cap code
			        callable.registerOutParameter(43,Types.VARCHAR); // pager - coverage region list
			        callable.registerOutParameter(44,Types.VARCHAR); // pager - encoding format code
			        callable.registerOutParameter(45,Types.VARCHAR); // pager - ownership code
			        callable.registerOutParameter(46,Types.VARCHAR); // pager - prepaid
			        callable.registerOutParameter(47,Types.VARCHAR); // pager - frequency cd
			        callable.registerOutParameter(48,Types.VARCHAR); // firmware feature code list
			        callable.registerOutParameter(49,Types.VARCHAR); // browser_protocol
			        callable.registerOutParameter(50,Types.NUMERIC);
			        callable.registerOutParameter(51,Types.DATE);    //equipment status date
			        callable.registerOutParameter(52,Types.VARCHAR);  // puk code
			        callable.registerOutParameter(53,Types.NUMERIC);  // product_category_id
			        callable.registerOutParameter(54,Types.NUMERIC);  // virtual
			        callable.registerOutParameter(55,Types.ARRAY, "BRAND_ARRAY");  // brand indicator
			        callable.registerOutParameter(56,Types.VARCHAR);
	
	
			        callable. execute();
			        if (callable.getInt(11)==1)
			         {stolenEquipment=true;}
			         else
			         {stolenEquipment=false;}
	
			        if (callable.getInt(54)==1)
			        {virtualEquipment=true;}
			        else
			        {virtualEquipment=false;}
	
			          equipInfo= new EquipmentInfo();
			          equipInfo.setSerialNumber(callable.getString(3));
			          equipInfo.setTechType(callable.getString(4));
			          equipInfo.setProductCode(callable.getString(5));
			          equipInfo.setProductStatusCode(callable.getString(6));
			          equipInfo.setVendorName(callable.getString(7));
			          equipInfo.setVendorNo(callable.getString(8));
			          equipInfo.setEquipmentStatusTypeID(callable.getLong(9));
			          equipInfo.setEquipmentStatusID(callable.getLong(10));
			          equipInfo.setSublock1(callable.getString(12));
			          equipInfo.setStolen(stolenEquipment);
			          equipInfo.setProductGroupTypeID(callable.getLong(13));
			          equipInfo.setProductGroupTypeCode(callable.getString(14));
			          equipInfo.setProductGroupTypeDescription(callable.getString(15));
			          equipInfo.setProductTypeID(callable.getLong(16));
			          equipInfo.setProductTypeDescription(callable.getString(17));
			          equipInfo.setProductClassID(callable.getLong(18));
			          equipInfo.setProductClassCode(callable.getString(19));
			          equipInfo.setProductClassDescription(callable.getString(20));
			          equipInfo.setProviderOwnerID(callable.getLong(21));
			          equipInfo.setLastMuleIMEI(callable.getString(22));
			          equipInfo.setProductName(callable.getString(23));
			          equipInfo.setProductNameFrench(callable.getString(24));
			          equipInfo.setBrowserVersion(callable.getString(25));
			          equipInfo.setFirmwareVersion(callable.getString(26));
			          equipInfo.setPRLCode(callable.getString(27));
			          equipInfo.setPRLDescription(callable.getString(28));
			          equipInfo.setProductGroupTypeDescriptionFrench(callable.getString(30)==null ? callable.getString(15) : callable.getString(30).equals("") ? callable.getString(15) : callable.getString(30));
			          equipInfo.setProductTypeDescriptionFrench(callable.getString(29)==null ? callable.getString(17) : callable.getString(29).equals("") ? callable.getString(17) : callable.getString(29));
			         // equipInfo.setSubscriberNumber(callable.getString(30));
			          //equipInfo.setBanID(callable.getInt(31));
			          if (!(callable.getString(33)==null))
			          { i =callable.getString(33).length()/8;
			            productPromoTypeList = new long[i];
			            for (j=0; j< i; j++)
			            { productPromoTypeList[j]=Long.parseLong(callable.getString(33).substring(j*8,j*8+8));
			            }
			          }
			          equipInfo.setProductPromoTypeList(productPromoTypeList);
			          equipInfo.setInitialActivation(callable.getString(34)==null ? false :callable.getString(34).equals("Y") ? true :false );
			          equipInfo.setModeCode(callable.getLong(35));
			          equipInfo.setModeDescription(callable.getString(36));
			          equipInfo.setProductType(callable.getString(37)==null ? "" : callable.getString(37));
			          equipInfo.setEquipmentType(callable.getString(38)==null ? "" : callable.getString(38));
			          equipInfo.setModelType(callable.getString(38)==null ? "" : callable.getString(38));
			          equipInfo.setEquipmentTypeClass(callable.getString(39)==null ? "" : callable.getString(39));
			          equipInfo.setLegacy(equipInfo.isSIMCard() ? false : equipInfo.isMule() ? false : callable.getString(40)==null ? true : callable.getString(40).equals("N") ? true : false );
	
			         // equipInfo.setContractTermCredits(retrieveContractTermCredits(equipInfo.getProductGroupTypeID(),equipInfo.getProductTypeID()));
	
			          //firmwareVersionFeatureCodes
			          if (!(callable.getString(48)==null))
			          {
			            StringTokenizer strToken = new StringTokenizer(callable.getString(48),"|");
			            firmwareFeatureList = new String[strToken.countTokens()];
			            i = 0;
			            while (strToken.hasMoreTokens()) {
			              firmwareFeatureList[i] = strToken.nextToken();
			              i++;
			            }
			          }
			          equipInfo.setFirmwareVersionFeatureCodes(firmwareFeatureList);
	
			          // browserProtocol
			          equipInfo.setBrowserProtocol(callable.getString(49));
			          equipInfo.setUnscanned(callable.getInt(50)== 1 ? true : false);
			          equipInfo.setEquipmentStatusDate(callable.getDate(51));
			          equipInfo.setPUKCode(callable.getString(52)==null ? "" : callable.getString(52));
			          equipInfo.setProductCategoryId(callable.getLong(53));
			          // Pager info
			          if (equipInfo.isPager())
			          { equipInfo.setCapCode(callable.getString(42));
	
			          if (!(callable.getString(43)==null))
			          {
			            StringTokenizer strToken = new StringTokenizer(callable.getString(44),
			                "|");
			            coverageRegionList = new String[strToken.countTokens()];
			            i = 0;
			            while (strToken.hasMoreTokens()) {
			              coverageRegionList[i] = strToken.nextToken();
			              i++;
			            }
			          }
	
			          equipInfo.setCoverageRegionCodes(coverageRegionList);
			          equipInfo.setEncodingFormat(callable.getString(44));
			          equipInfo.setPossession(callable.getString(45));
			          equipInfo.setPrepaid(callable.getString(46)==null ? false :callable.getString(46).equals("Y") ? true :false );
			          equipInfo.setFrequencyCode(callable.getString(47));
			          }
			          if (equipInfo.isMule())
			           {equipInfo.setEquipmentType(Equipment.EQUIPMENT_TYPE_DIGITAL);
			           }
			          equipInfo.setVirtual(virtualEquipment);
			          equipInfo.setBrandIds(getBrandArrays(callable.getArray(55)));
			          equipInfo.setNetworkType(callable.getString(56)==null ? "" : callable.getString(56));
			          
			          return equipInfo;
				
		        }
		   });
		}catch(UncategorizedSQLException  e) {
			SQLException sqe = e.getSQLException();
		    if (sqe != null && sqe.getErrorCode() == 20310) {
		    	throw new ApplicationException (SystemCodes.CMB_PEH_DAO,ErrorCodes.UNKNOWN_SERIAL_NUMBER, 
		    			EquipmentErrorMessages.UNKNOWN_SERIAL_NUMBER_EN,"" ,sqe);
		    }
		    throw e;
		} 
	}
	
	

	@Override
	public EquipmentInfo getEquipmentInfobyProductCode(final String productCode)
			throws ApplicationException {

		String call = "{call Client_Equipment.GetEquipmentInfoByProductCode(?,?,?,?,?,?,?,?,?,?," +
                                                                             "?,?,?,?,?,?,?,?,?,?, " +
                                                                             "?,?,?,?,?,?,?,?,?,?, " +
                                                                             "?,?,?,?,?,?,?,?,? )}";
		try{
		return getDistJdbcTemplate().execute(call, new CallableStatementCallback<EquipmentInfo>() {
			
			@Override
			public EquipmentInfo doInCallableStatement(CallableStatement callable) throws SQLException, DataAccessException {
				
				EquipmentInfo equipInfo = null;
				long[] productPromoTypeList = new long[0];
				String[] firmwareFeatureList = new String[0];
				int i=0, j=0;
				boolean stolenEquipment = false;
				  
				callable.setString(1,productCode);
		        callable.registerOutParameter(2,Types.VARCHAR );
		        callable.registerOutParameter(3,Types.NUMERIC );
		        callable.registerOutParameter(4,Types.NUMERIC);
		        callable.registerOutParameter(5,Types.VARCHAR);
		        callable.registerOutParameter(6,Types.NUMERIC);
		        callable.registerOutParameter(7,Types.NUMERIC);
		        callable.registerOutParameter(8,Types.VARCHAR);
		        callable.registerOutParameter(9,Types.VARCHAR);
		        callable.registerOutParameter(10,Types.VARCHAR);
		        callable.registerOutParameter(11,Types.NUMERIC);
		        callable.registerOutParameter(12,Types.VARCHAR);
		        callable.registerOutParameter(13,Types.VARCHAR);
		        callable.registerOutParameter(14,Types.VARCHAR);
		        callable.registerOutParameter(15,Types.VARCHAR);
		        callable.registerOutParameter(16,Types.VARCHAR);
		        callable.registerOutParameter(17,Types.VARCHAR);
		        callable.registerOutParameter(18,Types.VARCHAR);
		        callable.registerOutParameter(19,Types.VARCHAR);
		        callable.registerOutParameter(20,Types.NUMERIC);
		        callable.registerOutParameter(21,Types.VARCHAR);
		        callable.registerOutParameter(22,Types.VARCHAR);
		        callable.registerOutParameter(23,Types.VARCHAR);
		        callable.registerOutParameter(24,Types.NUMERIC);
		        callable.registerOutParameter(25,Types.VARCHAR);
		        callable.registerOutParameter(26,Types.VARCHAR);
		        callable.registerOutParameter(27,Types.VARCHAR);
		        callable.registerOutParameter(28,Types.NUMERIC);
		        callable.registerOutParameter(29,Types.NUMERIC);
		        callable.registerOutParameter(30,Types.DATE);
		        callable.registerOutParameter(31,Types.VARCHAR);
		        callable.registerOutParameter(32,Types.VARCHAR);
		        callable.registerOutParameter(33,Types.VARCHAR);
		        callable.registerOutParameter(34,Types.VARCHAR);
		        callable.registerOutParameter(35,Types.VARCHAR);
		        callable.registerOutParameter(36,Types.VARCHAR);
		        callable.registerOutParameter(37,Types.ARRAY, "BRAND_ARRAY"); //brand indicator
		        callable.registerOutParameter(38,Types.VARCHAR);
		        callable.registerOutParameter(39,Types.VARCHAR);

		        callable.execute();

		        	equipInfo= new EquipmentInfo();
		        	equipInfo.setSerialNumber("0");
		        	equipInfo.setInitialActivation(true);
		        	equipInfo.setPhoneNumber("0");
		            equipInfo.setProductCode(callable.getString(2));
		        	// 3- ProductId -?
		        	equipInfo.setProductCategoryId(callable.getLong(4));
		        	equipInfo.setVendorName(callable.getString(5));
		            equipInfo.setVendorNo(callable.getString(6)); // string from num
		            equipInfo.setStolen(stolenEquipment);
		            equipInfo.setProductGroupTypeID(callable.getLong(7));
		            equipInfo.setProductGroupTypeCode(callable.getString(8));
		            equipInfo.setProductGroupTypeDescription(callable.getString(9));
		            equipInfo.setProductGroupTypeDescriptionFrench(callable.getString(10)==null ? callable.getString(9) : callable.getString(10).equals("") ? callable.getString(9) : callable.getString(10));
		            equipInfo.setProductTypeID(callable.getLong(11));
		            equipInfo.setProductTypeDescription(callable.getString(12));
		            equipInfo.setProductTypeDescriptionFrench(callable.getString(13)==null ? callable.getString(12) : callable.getString(13).equals("") ? callable.getString(12) : callable.getString(13));
		            equipInfo.setProductType(callable.getString(14)==null ? "" : callable.getString(14));
		            equipInfo.setEquipmentType(callable.getString(15)==null ? "" : callable.getString(15));
		            equipInfo.setEquipmentTypeClass(callable.getString(16)==null ? "" : callable.getString(16));
		            equipInfo.setTechType(callable.getString(17));
		            equipInfo.setProductName(callable.getString(18));
		            equipInfo.setProductNameFrench(callable.getString(19));
		            equipInfo.setProductClassID(callable.getLong(20));
		            equipInfo.setProductStatusCode(callable.getString(21));
		            equipInfo.setProductClassCode(callable.getString(22));
		            equipInfo.setProductClassDescription(callable.getString(23));
		            equipInfo.setModeCode(callable.getLong(24));
		            equipInfo.setModeDescription(callable.getString(25));
		            if (!(callable.getString(26)==null))
		            { i =callable.getString(26).length()/8;
		              productPromoTypeList = new long[i];
		              for (j=0; j< i; j++)
		              { productPromoTypeList[j]=Long.parseLong(callable.getString(26).substring(j*8,j*8+8));
		              }
		            }
		            equipInfo.setProductPromoTypeList(productPromoTypeList);
		            // 27 cross-fleet
		            equipInfo.setEquipmentStatusTypeID(callable.getLong(28));
		            equipInfo.setEquipmentStatusID(callable.getLong(29));
		            equipInfo.setEquipmentStatusDate(callable.getDate(30));
		           // equipInfo.setContractTermCredits(retrieveContractTermCredits(equipInfo.getProductGroupTypeID(),equipInfo.getProductTypeID()));
		            equipInfo.setBrowserVersion(callable.getString(31));
		            equipInfo.setFirmwareVersion(callable.getString(32));
		            equipInfo.setPRLCode(callable.getString(33));
		            equipInfo.setPRLDescription(callable.getString(34));
		            // browserProtocol
		            equipInfo.setBrowserProtocol(callable.getString(35));
		            //firmwareVersionFeatureCodes
		            if (!(callable.getString(36)==null))
		            {
		              StringTokenizer strToken = new StringTokenizer(callable.getString(36),"|");
		              firmwareFeatureList = new String[strToken.countTokens()];
		              i = 0;
		              while (strToken.hasMoreTokens()) {
		                firmwareFeatureList[i] = strToken.nextToken();
		                i++;
		              }
		            }
		            equipInfo.setFirmwareVersionFeatureCodes(firmwareFeatureList);
		            if (equipInfo.isMule())
		            {equipInfo.setEquipmentType(Equipment.EQUIPMENT_TYPE_DIGITAL);
		            }
		            equipInfo.setBrandIds(getBrandArrays(callable.getArray(37)));
		            equipInfo.setEquipmentGroup(callable.getString(38));
		            equipInfo.setNetworkType(callable.getString(39)==null ? "" : callable.getString(39));
				return equipInfo;
		
			}
		   });
		}catch(UncategorizedSQLException  e) {
			SQLException sqe = e.getSQLException();
		    if (sqe != null && sqe.getErrorCode() == 20318) {
		    	throw new ApplicationException (SystemCodes.CMB_PEH_DAO,
		    			ErrorCodes.UNKNOWN_PRODUCT, EquipmentErrorMessages.UNKNOWN_PRODUCT_EN,"" ,sqe);
		    }
		    throw e;
		} 
	}
	
	
	

	@Override
	public String[] getEquipmentList(String pTechTypeClass, final int n,
			final String startSerialNo) throws ApplicationException {
		
		String sql=null;
		String    PCSQuery = "select  serial_no " +
        						" from pcs_equipment  " +
        						" where  serial_no > ? " +
        						" and length(serial_no) = 11  " +
        						" and rownum < ? " ;

		String    IDENQuery = "select serial_no " +
		        				" from IDEN_equipment i, product p" +
		        				" where p.technology_type ='mike' " +
		        				" and p.product_class_id=10000100 " +
		        				" and i.product_id=p.product_id  " +
		        				" and serial_no > ? " +
		        				"  and length(serial_no) =15  " +
		        				" and rownum < ? " ;
		
		String    SIMQuery = "select  sim_id " +
								" from sim s, product p " +
								" where p.technology_type ='mike' " +
								" and p.product_class_id=10000106 " +
								" and s.product_id=p.product_id  " +
								" and sim_id > ? " +
								" and length(sim_id) =15  " +
								" and rownum < ? " ;
		
		String    AnalogQuery = "select serial_no " +
		        				" from analog_equip " +
		        				" where  serial_no > ? " +
		        				" and length(serial_no) = 11  " +
		        				" and rownum < ? " ;
		
		if (pTechTypeClass==null || pTechTypeClass.equals(EquipmentInfo.PCSTECHTYPECLASS)){
				sql=PCSQuery;
		}else if (pTechTypeClass==null || pTechTypeClass.equals(EquipmentInfo.IDENTECHTYPECLASS)) {
				sql=IDENQuery;
	    }else if (pTechTypeClass==null || pTechTypeClass.equals(EquipmentInfo.ANALOGTECHTYPECLASS)) {
	    		sql=AnalogQuery;
	    }else if (pTechTypeClass==null || pTechTypeClass.equals(EquipmentInfo.SIMTECHTYPECLASS)){
	    		sql=SIMQuery;
	    }
		
		return getDistJdbcTemplate().execute(sql, new PreparedStatementCallback<String[]>() {
			@Override
			public String[] doInPreparedStatement(PreparedStatement pstmt)
			throws SQLException, DataAccessException {

				 ResultSet rset = null;
				 ArrayList<String> equipList = new ArrayList<String>();
				try{
					 pstmt.setString(1,startSerialNo);
					 pstmt.setInt(2,n);
					 rset = pstmt.executeQuery();
					 while (rset.next())
					 {
						 equipList.add(rset.getString(1));
					 }
				} finally {
					if (rset != null) {
						rset.close();
					}
				}
				return (String[])equipList.toArray(new String[equipList.size()]);
			}
		});
	}

	@Override
	public EquipmentModeInfo[] getEquipmentModes(final String pProductCode)
			throws ApplicationException {
		
		String call="{call Client_Equipment.GetProductModes(?,?) }";
		
		return getDistJdbcTemplate().execute(call, new CallableStatementCallback<EquipmentModeInfo[]>() {
			
			@Override
			public EquipmentModeInfo[] doInCallableStatement(CallableStatement callable) throws SQLException, DataAccessException {
				 List<EquipmentModeInfo> list = new ArrayList<EquipmentModeInfo>();
				 ResultSet result = null;
				 try{
					 callable.setString(1, pProductCode);
				     callable.registerOutParameter(2, OracleTypes.CURSOR);
				     callable.execute();
				     result = (ResultSet)callable.getObject(2);
	
				      while (result.next()) {
				      	EquipmentModeInfo modeInfo = new EquipmentModeInfo();
				      	modeInfo.setCode(result.getString(1));
				      	modeInfo.setDescription(result.getString(2));
				      	modeInfo.setDescriptionFrench(result.getString(3));
				       list.add(modeInfo);
				      }
				 } finally {
						if (result != null) {
							result.close();
						}
					}
			    return (EquipmentModeInfo[])list.toArray(new EquipmentModeInfo[list.size()]);
			}
		});

	}

	@Override
	public long getIDENShippedToLocation(final String serialNumber, final int locationType)
			throws ApplicationException {
		
		String sql="select coie.channel_organization_id from channel_org_iden_equip coie "+
						"where coie.serial_no = ? "+
						"and coie.seq_no = 0 "+
						"and coie.channel_org_equip_type_id = ? "+
						"and effective_dt = (select max(coie2.effective_dt) "+
						"from channel_org_iden_equip coie2 "+
						"where coie.serial_no = coie2.serial_no "+
						" and coie.seq_no = coie2.seq_no "+
						"and coie.channel_org_equip_type_id = coie2.channel_org_equip_type_id) ";
		
		return getDistJdbcTemplate().execute(sql, new PreparedStatementCallback<Long>() {
			
			@Override
			public Long doInPreparedStatement(PreparedStatement pstmt)
			throws SQLException, DataAccessException {
				 long locationChnlOrgId = 0;
				 ResultSet result = null;
				 try
			      {
					 pstmt.setString(1, serialNumber);
					 pstmt.setInt(2, locationType);

			         result = pstmt.executeQuery();

			        if (result.next())
			          locationChnlOrgId = result.getLong(1);
			      } finally {
						if (result != null) {
							result.close();
						}
					}
				return locationChnlOrgId;
			}
		});
	}

	@Override
	public String[] getKBEquipmentList(final String productType, final  int no)
			throws ApplicationException {
		
		String queryStr = " select unit_esn " +
        					" from physical_device pd " +
        					" ,subscriber s " +
        					" where pd.expiration_date is null " +
        					" and pd.product_type=" + "'" + productType + "'"+
        					" and s.subscriber_no=pd.subscriber_no " +
        					" and s.sub_status_rsn_code in ('CAPO','CMER','CGEM','CMPT')" +
        					" and rownum < 1 + " + no  ;
		
		return getKnowbilityJdbcTemplate().execute(queryStr, new PreparedStatementCallback<String[]>() {

			@Override
			public String[] doInPreparedStatement(
					PreparedStatement stmt) throws SQLException,
					DataAccessException {
				 
				ResultSet result = null;
				List<String> serialNumberList = new ArrayList<String>();
				try{
					result = stmt.executeQuery();
				    while (result.next())
				    {  serialNumberList.add(result.getString(1));
				    }
				} finally {
					if (result != null) {
						result.close();
					}
				}
			    return (String[])serialNumberList.toArray(new String[serialNumberList.size()]);
			}
		});
	}

	@Override
	public long getPCSShippedToLocation(final String serialNumber, final int locationType)
			throws ApplicationException {
		String queryStr = "select cope.channel_organization_id from channel_org_pcs_equip cope "+
							"where cope.serial_no = ? "+
							"and cope.seq_no = 0 "+
							"and cope.channel_org_equip_type_id = ? "+
							"and effective_dt = (select max(cope2.effective_dt) "+
							" from channel_org_pcs_equip cope2 "+
							" where cope.serial_no = cope2.serial_no "+
							" and cope.seq_no = cope2.seq_no "+
							"  and cope.channel_org_equip_type_id = cope2.channel_org_equip_type_id) ";
		
		return getDistJdbcTemplate().execute(queryStr, new PreparedStatementCallback<Long>() {

			@Override
			public Long doInPreparedStatement(PreparedStatement stmt) 
			          throws SQLException,DataAccessException {
				
				long locationChnlOrgId = 0;
				ResultSet result = null;
				try{
					stmt.setString(1, serialNumber);
					stmt.setInt(2, locationType);
					result = stmt.executeQuery();
   			        if (result.next())
				        locationChnlOrgId = result.getLong(1);
				} finally {
					if (result != null) {
						result.close();
					}
				}
			    return locationChnlOrgId;
			}
		});
	}

	@Override
	public long getProductIdByProductCode(final String productCode) throws ApplicationException {
		String call = "{call Client_Equipment.GetProductIdByProductCode(?,?) }";

		try {
			return getDistJdbcTemplate().execute(call, new CallableStatementCallback<Long>() {

				@Override
				public Long doInCallableStatement(CallableStatement callable) throws SQLException, DataAccessException {
					callable.setString(1, productCode);
					callable.registerOutParameter(2, Types.NUMERIC);
					callable.execute();
					return callable.getLong(2);
				}
			});
		} catch (UncategorizedSQLException e) {
			SQLException sqe = e.getSQLException();
			if (sqe != null && sqe.getErrorCode() == 20318) {
				throw new ApplicationException(SystemCodes.CMB_PEH_DAO, ErrorCodes.UNKNOWN_PRODUCT_CODE, EquipmentErrorMessages.UNKNOWN_PRODUCT_CODE_EN + productCode, "", sqe);
			}
			throw e;
		}
	}
	
	@Override
	public String[] getProductFeatures(final String pProductCode)throws ApplicationException {
		
		String call="{call Client_Equipment.GetFeatureCodes(?,?) }";
		
		return getDistJdbcTemplate().execute(call, new CallableStatementCallback<String[]>() {
			
			@Override
			public String[] doInCallableStatement(CallableStatement callable) throws SQLException, DataAccessException {
				 List<String> list = new ArrayList<String>();
				 ResultSet result = null;
				 try{
					 callable.setString(1, pProductCode);
				      callable.registerOutParameter(2, OracleTypes.CURSOR);
				      callable.execute();
				      result = (ResultSet)callable.getObject(2);

				      while (result.next()) {
				       list.add(result.getString(1));
				      }

				 } finally {
						if (result != null) {
							result.close();
						}
					}
				 return (String[])list.toArray(new String[list.size()]);
			}
		});
	}

	@Override
	public long getShippedToLocation(final String serialNumber, final int locationType)
			throws ApplicationException {
				String queryStr = " select cope.channel_organization_id from channel_org_pcs_equip cope "+
								    " where cope.serial_no = ? "+
								    " and cope.seq_no = 0 "+
								    " and cope.channel_org_equip_type_id = ? "+
								    " and effective_dt = (select max(cope2.effective_dt) "+
								    " from channel_org_pcs_equip cope2 "+
								    " where cope.serial_no = cope2.serial_no "+
								    " and cope.seq_no = cope2.seq_no "+
								    " and cope.channel_org_equip_type_id = cope2.channel_org_equip_type_id) "+
								    " union "+
								    " select coie.channel_organization_id from channel_org_iden_equip coie "+
								    " where coie.serial_no = ? "+
								    " and coie.seq_no = 0 "+
								    " and coie.channel_org_equip_type_id = ? "+
								    " and effective_dt = (select max(coie2.effective_dt) "+
								    " from channel_org_iden_equip coie2 "+
								    " where coie.serial_no = coie2.serial_no "+
								    " and coie.seq_no = coie2.seq_no "+
								    " and coie.channel_org_equip_type_id = coie2.channel_org_equip_type_id) ";

			return getDistJdbcTemplate().execute(queryStr, new PreparedStatementCallback<Long>() {
			
			@Override
			public Long doInPreparedStatement(PreparedStatement stmt) 
			  throws SQLException,DataAccessException {
			
			long locationChnlOrgId = 0;
			ResultSet result = null;
			try{
				stmt.setString(1, serialNumber);
				stmt.setInt(2, locationType);
				stmt.setString(3, serialNumber);
				stmt.setInt(4, locationType);

			    result = stmt.executeQuery();

			    if (result.next())
			        locationChnlOrgId = result.getLong(1);
			} finally {
			if (result != null) {
				result.close();
			}
			}
			return locationChnlOrgId;
			}
			});
	}

	@Override
	public EquipmentInfo getVirtualEquipment(final String pSerialNo,
			final String techTypeClass) throws ApplicationException {
		String call="{call Client_Equipment.GetVirtualEquipmentInfo(?,?,?,?,?,?,?,?,?,?," +
                                                                     "?,?,?,?,?,?,?,?,?,?, " +
                                                                     "?,?,?,?,?,?,?,?,?,?, " +
                                                                     "?,?,?,?,?,?,?,?,?,?, " +
                                                                     "?,?,?,?,?,?,?,?,?,?, " +
                                                                     "?,?,?,?,?,?,?,?)}";
		try{
		return getDistJdbcTemplate().execute(call, new CallableStatementCallback<EquipmentInfo>() {
			
			@Override
			public EquipmentInfo doInCallableStatement(CallableStatement callable) throws SQLException, DataAccessException {
				
				EquipmentInfo equipInfo;
				long[] productPromoTypeList = new long[0];
				String[] coverageRegionList = new String[0];
				String[] firmwareFeatureList = new String[0];
				int i=0, j=0;
				boolean  virtualEquipment = false;
				boolean  stolenEquipment = false;
				
				    callable.setString(1, pSerialNo);
			        callable.setString(2, techTypeClass);
			        callable.registerOutParameter(3,Types.VARCHAR );
			        callable.registerOutParameter(4,Types.VARCHAR );
			        callable.registerOutParameter(5,Types.VARCHAR);
			        callable.registerOutParameter(6,Types.VARCHAR);
			        callable.registerOutParameter(7,Types.VARCHAR);
			        callable.registerOutParameter(8,Types.VARCHAR);
			        callable.registerOutParameter(9,Types.NUMERIC);
			        callable.registerOutParameter(10,Types.NUMERIC);
			        callable.registerOutParameter(11,Types.NUMERIC);
			        callable.registerOutParameter(12,Types.VARCHAR);
			        callable.registerOutParameter(13,Types.NUMERIC);
			        callable.registerOutParameter(14,Types.VARCHAR);
			        callable.registerOutParameter(15,Types.VARCHAR);
			        callable.registerOutParameter(16,Types.NUMERIC);
			        callable.registerOutParameter(17,Types.VARCHAR);
			        callable.registerOutParameter(18,Types.NUMERIC);
			        callable.registerOutParameter(19,Types.VARCHAR);
			        callable.registerOutParameter(20,Types.VARCHAR);
			        callable.registerOutParameter(21,Types.NUMERIC);
			        callable.registerOutParameter(22,Types.VARCHAR);
			        callable.registerOutParameter(23,Types.VARCHAR);
			        callable.registerOutParameter(24,Types.VARCHAR);
			        callable.registerOutParameter(25,Types.VARCHAR);
			        callable.registerOutParameter(26,Types.VARCHAR);
			        callable.registerOutParameter(27,Types.VARCHAR);
			        callable.registerOutParameter(28,Types.VARCHAR);
			        callable.registerOutParameter(29,Types.VARCHAR);
			        callable.registerOutParameter(30,Types.VARCHAR);
			        callable.registerOutParameter(31,Types.VARCHAR);
			        callable.registerOutParameter(32,Types.NUMERIC);
			        callable.registerOutParameter(33,Types.VARCHAR);
			        callable.registerOutParameter(34,Types.VARCHAR);
			        callable.registerOutParameter(35,Types.NUMERIC);
			        callable.registerOutParameter(36,Types.VARCHAR);
			        callable.registerOutParameter(37,Types.VARCHAR); // product type
			        callable.registerOutParameter(38,Types.VARCHAR); // equipment type
			        callable.registerOutParameter(39,Types.VARCHAR); // equipment type class
			        callable.registerOutParameter(40,Types.VARCHAR); 
			        callable.registerOutParameter(41,Types.NUMERIC);
			/* commented for pager
			        callable.registerOutParameter(42,Types.VARCHAR);
			        callable.registerOutParameter(43,Types.VARCHAR);
			        callable.registerOutParameter(44,Types.NUMERIC);
			*/
			//replace 42, 43 and 44 by the following :
			        callable.registerOutParameter(42,Types.VARCHAR); // pager - cap code
			        callable.registerOutParameter(43,Types.VARCHAR); // pager - coverage region list
			        callable.registerOutParameter(44,Types.VARCHAR); // pager - encoding format code
			        callable.registerOutParameter(45,Types.VARCHAR); // pager - ownership code
			        callable.registerOutParameter(46,Types.VARCHAR); // pager - prepaid
			        callable.registerOutParameter(47,Types.VARCHAR); // pager - frequency cd
			        callable.registerOutParameter(48,Types.VARCHAR); // firmware feature code list
			        callable.registerOutParameter(49,Types.VARCHAR); // browser_protocol
			        callable.registerOutParameter(50,Types.NUMERIC);
			        callable.registerOutParameter(51,Types.DATE);    //equipment status date
			        callable.registerOutParameter(52,Types.VARCHAR);  // puk code
			        callable.registerOutParameter(53,Types.NUMERIC);  // product_category_id
			        callable.registerOutParameter(54,Types.NUMERIC);  // virtual
			        callable.registerOutParameter(55,Types.VARCHAR);  // rim pin
			        callable.registerOutParameter(56,Types.ARRAY, "BRAND_ARRAY");  // brand indicator
			        callable.registerOutParameter(57,Types.VARCHAR); // equipment_group
			        callable.registerOutParameter(58,Types.VARCHAR);
			        callable. execute();
			        if (callable.getInt(11)==1)
			         {stolenEquipment=true;}
			         else
			         {stolenEquipment=false;}

			        if (callable.getInt(54)==1)
			        {virtualEquipment=true;}
			        else
			        {virtualEquipment=false;}

			          equipInfo= new EquipmentInfo();
			          equipInfo.setSerialNumber(callable.getString(3));
			          equipInfo.setTechType(callable.getString(4));
			          equipInfo.setProductCode(callable.getString(5));
			          equipInfo.setProductStatusCode(callable.getString(6));
			          equipInfo.setVendorName(callable.getString(7));
			          equipInfo.setVendorNo(callable.getString(8));
			          equipInfo.setEquipmentStatusTypeID(callable.getLong(9));
			          equipInfo.setEquipmentStatusID(callable.getLong(10));
			          equipInfo.setSublock1(callable.getString(12));
			          equipInfo.setStolen(stolenEquipment);
			          equipInfo.setProductGroupTypeID(callable.getLong(13));
			          equipInfo.setProductGroupTypeCode(callable.getString(14));
			          equipInfo.setProductGroupTypeDescription(callable.getString(15));
			          equipInfo.setProductTypeID(callable.getLong(16));
			          equipInfo.setProductTypeDescription(callable.getString(17));
			          equipInfo.setProductClassID(callable.getLong(18));
			          equipInfo.setProductClassCode(callable.getString(19));
			          equipInfo.setProductClassDescription(callable.getString(20));
			          equipInfo.setProviderOwnerID(callable.getLong(21));
			          equipInfo.setLastMuleIMEI(callable.getString(22));
			          equipInfo.setProductName(callable.getString(23));
			          equipInfo.setProductNameFrench(callable.getString(24));
			          equipInfo.setBrowserVersion(callable.getString(25));
			          equipInfo.setFirmwareVersion(callable.getString(26));
			          equipInfo.setPRLCode(callable.getString(27));
			          equipInfo.setPRLDescription(callable.getString(28));
			          equipInfo.setProductGroupTypeDescriptionFrench(callable.getString(30)==null ? callable.getString(15) : callable.getString(30).equals("") ? callable.getString(15) : callable.getString(30));
			          equipInfo.setProductTypeDescriptionFrench(callable.getString(29)==null ? callable.getString(17) : callable.getString(29).equals("") ? callable.getString(17) : callable.getString(29));
			         // equipInfo.setSubscriberNumber(callable.getString(31));
			          //equipInfo.setBanID(callable.getInt(32));
			          if (!(callable.getString(33)==null))
			          { i =callable.getString(33).length()/8;
			            productPromoTypeList = new long[i];
			            for (j=0; j< i; j++)
			            { productPromoTypeList[j]=Long.parseLong(callable.getString(33).substring(j*8,j*8+8));
			            }
			          }
			          equipInfo.setProductPromoTypeList(productPromoTypeList);
			          equipInfo.setInitialActivation(callable.getString(34)==null ? false :callable.getString(34).equals("Y") ? true :false );
			          equipInfo.setModeCode(callable.getLong(35));
			          equipInfo.setModeDescription(callable.getString(36));
			          equipInfo.setProductType(callable.getString(37)==null ? "" : callable.getString(37));
			          equipInfo.setEquipmentType(callable.getString(38)==null ? "" : callable.getString(38));
			          equipInfo.setModelType(callable.getString(38)==null ? "" : callable.getString(38));
			          equipInfo.setEquipmentTypeClass(callable.getString(39)==null ? "" : callable.getString(39));
			          equipInfo.setLegacy(equipInfo.isSIMCard() ? false : equipInfo.isMule() ? false : callable.getString(40)==null ? true : callable.getString(40).equals("N") ? true : false );
			         
			         // TODO : set setContractTermCredits() in new EJB SubscriberHelplerImpl.retrieveVirtualEquipment()
			         //  equipInfo.setContractTermCredits(retrieveContractTermCredits(equipInfo.getProductGroupTypeID(),equipInfo.getProductTypeID()));

			          //firmwareVersionFeatureCodes
			          if (!(callable.getString(48)==null))
			          {
			            StringTokenizer strToken = new StringTokenizer(callable.getString(48),"|");
			            firmwareFeatureList = new String[strToken.countTokens()];
			            i = 0;
			            while (strToken.hasMoreTokens()) {
			              firmwareFeatureList[i] = strToken.nextToken();
			              i++;
			            }
			          }
			          equipInfo.setFirmwareVersionFeatureCodes(firmwareFeatureList);

			          // browserProtocol
			          equipInfo.setBrowserProtocol(callable.getString(49));

			          equipInfo.setEquipmentStatusDate(callable.getDate(51));
			          equipInfo.setPUKCode(callable.getString(52)==null ? "" : callable.getString(52));
			          equipInfo.setProductCategoryId(callable.getLong(53));

			          equipInfo.setRIMPin(callable.getString(55));

			          // Pager info
			          if (equipInfo.isPager())
			          { equipInfo.setCapCode(callable.getString(42));

			          if (!(callable.getString(43)==null))
			          {
			            StringTokenizer strToken = new StringTokenizer(callable.getString(43),
			                "|");
			            coverageRegionList = new String[strToken.countTokens()];
			            i = 0;
			            while (strToken.hasMoreTokens()) {
			              coverageRegionList[i] = strToken.nextToken();
			              i++;
			            }
			          }

			          equipInfo.setCoverageRegionCodes(coverageRegionList);
			          equipInfo.setEncodingFormat(callable.getString(44));
			          equipInfo.setPossession(callable.getString(45));
			          equipInfo.setPrepaid(callable.getString(46)==null ? false :callable.getString(46).equals("Y") ? true :false );
			          equipInfo.setFrequencyCode(callable.getString(47));
			          }
			          if (equipInfo.isMule())
			           {equipInfo.setEquipmentType(Equipment.EQUIPMENT_TYPE_DIGITAL);
			           }
			          equipInfo.setVirtual(virtualEquipment);
			          equipInfo.setBrandIds(getBrandArrays(callable.getArray(56)));
			          equipInfo.setEquipmentGroup(callable.getString(57));
			          equipInfo.setNetworkType(callable.getString(58)==null ? "": callable.getString(58));
			          
				 return equipInfo;
			}
		});
		}catch(UncategorizedSQLException  e) {
			SQLException sqe = e.getSQLException();
		    if (sqe != null && sqe.getErrorCode() == 20310) {
		    	throw new ApplicationException (SystemCodes.CMB_PEH_DAO,
		    			ErrorCodes.UNKNOWN_SERIAL_NUMBER, EquipmentErrorMessages.UNKNOWN_SERIAL_NUMBER_EN,"" ,sqe);
		    }
		    throw e;
		} 
	}
	
	

	@Override
	public boolean isNewPrepaidHandset(final String serialNo, final String productCode)
			throws ApplicationException {
		String call="{call Client_Equipment.isNewPrepaidHandset(?,?,?)}";
		
		return getDistJdbcTemplate().execute(call, new CallableStatementCallback<Boolean>() {
			
			@Override
			public Boolean doInCallableStatement(CallableStatement callable) throws SQLException, DataAccessException {
				 
				 boolean isNewPrepaid;
				
				 callable.setString(1, serialNo);
			     callable.setString(2, productCode);
			     callable.registerOutParameter(3,Types.NUMERIC);
			     callable. execute();
			     
			     int newPrepaid = callable.getInt(3);
			     isNewPrepaid = newPrepaid == 1?true:false;
				 return isNewPrepaid;
			}
		});
	}

	@Override
	public boolean isValidESNPrefix(String pSerialNo) throws ApplicationException {
		String queryStr = " select  manf_esn_prefix" +
		" from manufacturer_esn" +
		" where manf_esn_prefix = substr('"+ pSerialNo + "',1,3)" +
		" and substr('"+ pSerialNo + "',4) >= esn_low_no " +
		" and substr('"+ pSerialNo + "',4) <= esn_high_no ";

		return getKnowbilityJdbcTemplate().query(queryStr, new ResultSetExtractor <Boolean>() {

			@Override
			public Boolean extractData(ResultSet result) throws SQLException,	DataAccessException {
				String  validESNPrefix = null;
				while (result.next()) { 
					validESNPrefix=result.getString(1);
				}
				return  (validESNPrefix == null ? false :true) ;
			}
		});
	}

	@Override
	public boolean isVirtualESN(final String pSerialNo) throws ApplicationException {
		String call = "{call Client_Equipment.checkifesnvirtual(?,?)}";
		
		return getDistJdbcTemplate().execute(call, new CallableStatementCallback<Boolean>() {
			
			@Override
			public Boolean doInCallableStatement(CallableStatement callable) throws SQLException, DataAccessException {
				boolean isVirtual;
				callable.setString(1, pSerialNo);
				callable.registerOutParameter(2,Types.NUMERIC);

				callable. execute();

				int virtual = callable.getInt(2);
				isVirtual = virtual == 1 ? true :false;

				return isVirtual;
			}
		} );
	}
	
	private int[] getBrandArrays(Array brandArray) throws SQLException {
		BigDecimal[] brandIds = null;
		if (brandArray != null) {
			brandIds = (BigDecimal[]) brandArray.getArray();
		}

		if (brandIds == null) {
			brandIds = new BigDecimal[0];
		}
		int[] brands = new int[brandIds.length];
		for (int brandCount = 0; brandCount < brandIds.length; brandCount++) {
			brands[brandCount] = brandIds[brandCount].intValue();
		}
		return brands;
	}
	

	
	@Override
	public String[] getIMSIsBySerialNumber(final String serialNumber)	throws ApplicationException {
		String call = "{call Client_Equipment.getusimbyimei(?,?)}";
		
		return getDistJdbcTemplate().execute(call, new CallableStatementCallback<String[]>() {
			
			@Override
			public String[] doInCallableStatement(CallableStatement callable)throws SQLException, DataAccessException {
				String[] listofIMSIs = null;
				try {
					// assuming serial number is USIM 
					listofIMSIs = getIMSIsByUSIM(serialNumber); 
	
					//Serial Number is EMEI if found nothing
					if(listofIMSIs.length == 0) {
						//it means serial number is EMEI
						callable.setString(1, serialNumber);
						callable.registerOutParameter(2, Types.VARCHAR);
				
						callable. execute();
				        
						String usimID = callable.getString(2);
						listofIMSIs = getIMSIsByUSIM(usimID);
					}
				}catch (SQLException se){
			       	listofIMSIs = new String[0];
				}  catch (ApplicationException e) {
					LOGGER.error("ApplicationException occured [serialNumber="+serialNumber+"]. "+e.getMessage());
					LOGGER.debug(e.getMessage(), e);
				}
		    	return listofIMSIs;
			}
		} );
	}
	
	@Override
	public String[] getIMSIsByUSIM(final String pUSIMId) throws ApplicationException {
		String call = "{call Client_Equipment.getIMSIsByUSIM(?,?)}";
		
		return getDistJdbcTemplate().execute(call, new CallableStatementCallback<String[]>() {
			
			@Override
			public String[] doInCallableStatement(CallableStatement callable) throws SQLException, DataAccessException {
				List<String> result = new ArrayList<String>();
				ResultSet rs = null;
				try{
					callable.registerOutParameter(2, OracleTypes.CURSOR);
					callable.setString(1, pUSIMId);
					callable. execute();
				        
					rs = (ResultSet) callable.getObject(2);
					while (rs.next()) {
						result.add(rs.getString(1));
					}
				}finally{
					if(rs!=null){
						rs.close();
					}
				}
				return (String[]) result.toArray( new String[ result.size()] );
			}
		}); 
	
	}
	


	@Override
	public EquipmentInfo getSubscriberByEquipment(final EquipmentInfo equipmentInfo) throws ApplicationException {
		if (equipmentInfo != null) {
			if (EquipmentInfo.DUMMY_ESN_FOR_USIM.equals(equipmentInfo.getSerialNumber())) {
				return equipmentInfo;
			}
			
			if (equipmentInfo.isUSIMCard() ) {
				return getSubscriberByIMSI(equipmentInfo);
			}
			
			return getKnowbilityJdbcTemplate().execute( new ConnectionCallback<EquipmentInfo>() {
				@Override
				public EquipmentInfo doInConnection(Connection connection) throws SQLException,DataAccessException {
					OracleCallableStatement callable = null;
					ResultSet result = null;
					try{


					// prepare callable statement
					callable = (OracleCallableStatement) connection.prepareCall("{? = call SUBSCRIBER_PKG.GetEquipmentSubscribers(?, ?, ?)}");
					// create array descriptor
					ArrayDescriptor serialNumbersArrayDesc = ArrayDescriptor.createDescriptor("T_SUBSCRIBER_ARRAY", connection);
					// create Oracle array of serial numbers
					String[] serialNumbers;
					if (equipmentInfo.isPager()) {
						serialNumbers = new String[] {EquipmentManager.Helper.getFormattedCapCode(equipmentInfo.getCapCode(), Province.PROVINCE_AB, equipmentInfo.getEncodingFormat(), equipmentInfo.getEquipmentType()),
								EquipmentManager.Helper.getFormattedCapCode(equipmentInfo.getCapCode(), Province.PROVINCE_BC, equipmentInfo.getEncodingFormat(), equipmentInfo.getEquipmentType())};
					}
					else
						serialNumbers = new String[] {equipmentInfo.getSerialNumber()};

					ARRAY serialNumbersArray = new ARRAY(serialNumbersArrayDesc, connection, serialNumbers);

					// set/register input/output parameters
					callable.registerOutParameter(1, OracleTypes.NUMBER);
					callable.setARRAY(2, serialNumbersArray);
					callable.registerOutParameter(3, OracleTypes.CURSOR);
					callable.registerOutParameter(4, OracleTypes.VARCHAR);

					callable.execute();

					boolean success = callable.getInt(1) == AccountManager.NUMERIC_TRUE;

					if (success) {
						result = (ResultSet) callable.getObject(3);

						if (result.next()) {
							equipmentInfo.setPhoneNumber(result.getString(1));
							equipmentInfo.setBanID(result.getInt(2));
						}
					}
					else {
						LOGGER.error("Stored procedure failed: " + callable.getString(4));
					}
					
				}finally{
					if(result!=null){
						result.close();
					}
					if(callable!=null){
						callable.close();
					}
				}
					return equipmentInfo;
				}
			}); 
		} else {
			LOGGER.debug("equipmentInfo is null.");
			return null;
		}

	}	
	
	@Override
	public EquipmentInfo getSubscriberByIMSI(final EquipmentInfo equipmentInfo) throws ApplicationException {
		if (equipmentInfo != null) {
			return getKnowbilityJdbcTemplate().execute( new ConnectionCallback<EquipmentInfo>() {

				@Override
				public EquipmentInfo doInConnection(Connection connection) throws SQLException, DataAccessException {
					// method's name
					OracleCallableStatement callable = null;
					ResultSet result = null;
					try{
						callable = (OracleCallableStatement) connection.prepareCall("{? = call SUBSCRIBER_PKG.getBanAndPhoneNoByIMSI(?, ?, ?)}");
	
						// set/register input/output parameters
						callable.registerOutParameter(1, OracleTypes.NUMBER);
						callable.setString(2, equipmentInfo.getProfile().getLocalIMSI() );
						callable.registerOutParameter(3, OracleTypes.CURSOR);
						callable.registerOutParameter(4, OracleTypes.VARCHAR);
	
						callable.execute();
	
						boolean success = callable.getInt(1) == AccountManager.NUMERIC_TRUE;
	
						if (success) {
							result = (ResultSet) callable.getObject(3);
	
							if (result.next()) {
								equipmentInfo.setPhoneNumber(result.getString(1));
								equipmentInfo.setBanID(result.getInt(2));
							} 
							else {
								//no subscriber is currently using this USIMCard
							}
						}
						else {
							LOGGER.error("Stored procedure failed: " + callable.getString(4));
						}
					}finally{
						if(result!=null){
							result.close();
						}
						if(callable!=null){
							callable.close();
						}
					}
					return equipmentInfo;
				}
			});
		} else {
			LOGGER.debug("equipmentInfo is null.");
			return null;
		}
	}	
	
	@Override
	public EquipmentInfo getAssociatedHandsetByUSIMID(final String pUSimID)
			throws ApplicationException {
		
		String call="{call Client_Equipment.getAssociatedHandsetbyUSIMID(?,?,?,?,?,?,?,?,?,?," +
		                                                               "?,?,?,?,?,?,?,?,?,?, " +
		                                                               "?,?,?,?,?,?,?,?,?,?, " +
		                                                               "?,?,?,?,?,?,?,?,?,?, " +
		                                                               "?,?,?,?,?,?,?,?,?,?, " +
		                                                               "?,?,?,?,? ,?)}";
		try{
		return getDistJdbcTemplate().execute(call, new CallableStatementCallback<EquipmentInfo>() {
			
			@Override
			public EquipmentInfo doInCallableStatement(CallableStatement callable) throws SQLException, DataAccessException {
				
				long[] productPromoTypeList = new long[0];
				String[] firmwareFeatureList = new String[0];
				int i=0, j=0;
				EquipmentInfo equipInfo =null ;
				
				callable.setString(1, pUSimID);
				callable.registerOutParameter(2,Types.VARCHAR );
				callable.registerOutParameter(3,Types.VARCHAR );
				callable.registerOutParameter(4,Types.VARCHAR);
				callable.registerOutParameter(5,Types.VARCHAR);
				callable.registerOutParameter(6,Types.VARCHAR);
				callable.registerOutParameter(7,Types.VARCHAR);
				callable.registerOutParameter(8,Types.NUMERIC);
				callable.registerOutParameter(9,Types.NUMERIC);
				callable.registerOutParameter(10,Types.NUMERIC);
				callable.registerOutParameter(11,Types.VARCHAR);
				callable.registerOutParameter(12,Types.NUMERIC);
				callable.registerOutParameter(13,Types.VARCHAR);
				callable.registerOutParameter(14,Types.VARCHAR);
				callable.registerOutParameter(15,Types.NUMERIC);
				callable.registerOutParameter(16,Types.VARCHAR);
				callable.registerOutParameter(17,Types.NUMERIC);
				callable.registerOutParameter(18,Types.VARCHAR);
				callable.registerOutParameter(19,Types.VARCHAR);
				callable.registerOutParameter(20,Types.NUMERIC);
				callable.registerOutParameter(21,Types.VARCHAR);
				callable.registerOutParameter(22,Types.VARCHAR);
				callable.registerOutParameter(23,Types.VARCHAR);
				callable.registerOutParameter(24,Types.VARCHAR);
				callable.registerOutParameter(24,Types.VARCHAR);
				callable.registerOutParameter(25,Types.VARCHAR);
				callable.registerOutParameter(26,Types.VARCHAR);
				callable.registerOutParameter(27,Types.VARCHAR);
				callable.registerOutParameter(28,Types.VARCHAR);
				callable.registerOutParameter(29,Types.VARCHAR);
				callable.registerOutParameter(30,Types.VARCHAR);
				callable.registerOutParameter(31,Types.NUMERIC);
				callable.registerOutParameter(32,Types.VARCHAR);
				callable.registerOutParameter(33,Types.VARCHAR);
				callable.registerOutParameter(34,Types.NUMERIC);
				callable.registerOutParameter(35,Types.VARCHAR);
				callable.registerOutParameter(36,Types.VARCHAR);//product type
				callable.registerOutParameter(37,Types.VARCHAR);// equipment type
				callable.registerOutParameter(38,Types.VARCHAR); //equipment type class
				callable.registerOutParameter(39,Types.VARCHAR); //
				callable.registerOutParameter(40,Types.NUMERIC);
				callable.registerOutParameter(41,Types.VARCHAR); // pager - cap code
				callable.registerOutParameter(42,Types.VARCHAR); // pager - coverage region list
				callable.registerOutParameter(43,Types.VARCHAR); // pager - encoding format code
				callable.registerOutParameter(44,Types.VARCHAR); // pager - ownership code
				callable.registerOutParameter(45,Types.VARCHAR); // pager - prepaid
				callable.registerOutParameter(46,Types.VARCHAR); // pager - frequency cd
				callable.registerOutParameter(47,Types.VARCHAR); // firmware feature code list
				callable.registerOutParameter(48,Types.VARCHAR); // browser_protocol
				callable.registerOutParameter(49,Types.NUMERIC);
				callable.registerOutParameter(50,Types.DATE);    // equipment status date
				callable.registerOutParameter(51,Types.VARCHAR); // PUK Code
				callable.registerOutParameter(52,Types.NUMERIC);  // ppoduct_category_id
				callable.registerOutParameter(53,Types.VARCHAR); // rim pin
				callable.registerOutParameter(54,Types.ARRAY, "BRAND_ARRAY"); // brand indicator
				callable.registerOutParameter(55,Types.VARCHAR); // equipment_group
				callable.registerOutParameter(56,Types.VARCHAR);
				callable. execute();
				if (callable.getString(2)!=null)
				{ 
					boolean stolenEquipment;
					if (callable.getInt(10)==1)
					{ 
					  stolenEquipment=true;
					}else{
					  stolenEquipment=false;
					}
					
					equipInfo= new EquipmentInfo();
					equipInfo.setSerialNumber(callable.getString(2));
					equipInfo.setTechType(callable.getString(3));
					equipInfo.setProductCode(callable.getString(4));
					equipInfo.setProductStatusCode(callable.getString(5));
					equipInfo.setVendorName(callable.getString(6));
					equipInfo.setVendorNo(callable.getString(7));
					equipInfo.setEquipmentStatusTypeID(callable.getLong(8));
					equipInfo.setEquipmentStatusID(callable.getLong(9));
					equipInfo.setSublock1(callable.getString(11));
					equipInfo.setStolen(stolenEquipment);
					equipInfo.setProductGroupTypeID(callable.getLong(12));
					equipInfo.setProductGroupTypeCode(callable.getString(13));
					equipInfo.setProductGroupTypeDescription(callable.getString(14));
					equipInfo.setProductTypeID(callable.getLong(15));
					equipInfo.setProductTypeDescription(callable.getString(16));
					equipInfo.setProductClassID(callable.getLong(17));
					equipInfo.setProductClassCode(callable.getString(18));
					equipInfo.setProductClassDescription(callable.getString(19));
					equipInfo.setProviderOwnerID(callable.getLong(20));
					equipInfo.setLastMuleIMEI(callable.getString(21));
					equipInfo.setProductName(callable.getString(22));
					equipInfo.setProductNameFrench(callable.getString(23));
					equipInfo.setBrowserVersion(callable.getString(24));
					equipInfo.setFirmwareVersion(callable.getString(25));
					equipInfo.setPRLCode(callable.getString(26));
					equipInfo.setPRLDescription(callable.getString(27));
					equipInfo.setProductGroupTypeDescriptionFrench(callable.getString(29)==null ? callable.getString(14) : callable.getString(29).equals("") ? callable.getString(14) : callable.getString(29));
					equipInfo.setProductTypeDescriptionFrench(callable.getString(28)==null ? callable.getString(16) : callable.getString(28).equals("") ? callable.getString(16) : callable.getString(28));
					// equipInfo.setSubscriberNumber(callable.getString(30));
					//equipInfo.setBanID(callable.getInt(31));
					if (!(callable.getString(32)==null))
					{ i =callable.getString(32).length()/8;
					productPromoTypeList = new long[i];
					for (j=0; j< i; j++)
					{ productPromoTypeList[j]=Long.parseLong(callable.getString(32).substring(j*8,j*8+8));
					}
					}
					equipInfo.setProductPromoTypeList(productPromoTypeList);
					equipInfo.setInitialActivation(callable.getString(33)==null ? false :callable.getString(33).equals("Y") ? true :false );
					equipInfo.setModeCode(callable.getLong(34));
					equipInfo.setModeDescription(callable.getString(35));
					equipInfo.setProductType(callable.getString(36)==null ? "" : callable.getString(36));
					equipInfo.setEquipmentType(callable.getString(37)==null ? "" : callable.getString(37));
					equipInfo.setEquipmentTypeClass(callable.getString(38)==null ? "" : callable.getString(38));
					equipInfo.setLegacy(equipInfo.isSIMCard() ? false : equipInfo.isMule() ? false : callable.getString(39)==null ? true : callable.getString(39).equals("N") ? true : false );
					
					//equipInfo.setContractTermCredits(retrieveContractTermCredits(equipInfo.getProductGroupTypeID(),
					//equipInfo.getProductTypeID()));
					
					//firmwareVersionFeatureCodes
					if (!(callable.getString(47)==null))
					{
					StringTokenizer strToken = new StringTokenizer(callable.getString(47),"|");
					firmwareFeatureList = new String[strToken.countTokens()];
					i = 0;
					while (strToken.hasMoreTokens()) {
					firmwareFeatureList[i] = strToken.nextToken();
					i++;
					}
					}
					equipInfo.setFirmwareVersionFeatureCodes(firmwareFeatureList);
					
					// browserProtocol
					equipInfo.setBrowserProtocol(callable.getString(48));
					equipInfo.setEquipmentStatusDate(callable.getDate(50));
					equipInfo.setPUKCode(callable.getString(51)==null ? "" : callable.getString(51));
					equipInfo.setProductCategoryId(callable.getLong(52));
					
					equipInfo.setRIMPin(callable.getString(53));
					equipInfo.setBrandIds(getBrandArrays(callable.getArray(54)));
					equipInfo.setEquipmentGroup(callable.getString(55));
					equipInfo.setNetworkType(callable.getString(56)==null ? "" : callable.getString(56));
				
				}
			 return equipInfo;
			}
			
		});
		}catch (UncategorizedSQLException e) {
			SQLException sqe = e.getSQLException();
			if (sqe != null) {
				if (sqe.getErrorCode() == 20310) {
					throw new ApplicationException (SystemCodes.CMB_PEH_DAO,
							ErrorCodes.UNKNOWN_SERIAL_NUMBER, EquipmentErrorMessages.UNKNOWN_ASSOCIATED_HANDSET_FOR_USIM_EN +"["+pUSimID+"]","" ,sqe);
				}else if (sqe.getErrorCode() == 20315) {
					throw new ApplicationException(SystemCodes.CMB_PEH_DAO, 
							ErrorCodes.MULTIPLE_ASSOCIATED_HANDSETS , EquipmentErrorMessages.MULTIPLE_ASSOCIATED_HANDSETS_EN,""  ,e);
				}	
			}
			throw e;
		}
	}
	

	@Override
	public EquipmentInfo getEquipmentInfobyPhoneNo(final String phoneNumber) throws ApplicationException {

		String call = "{call Client_Equipment.GetEquipmentInfoByPhoneNo(?,?,?,?,?,?,?,?,?,?," + "?,?,?,?,?,?,?,?,?,?, " + "?,?,?,?,?,?,?,?,?,?, " + "?,?,?,?,?,?,?,?,?,?, "
				+ "?,?,?,?,?,?,?,?,?,?, " + "?,?,?,?,?,?,?,?)}";
		try {
			return getDistJdbcTemplate().execute(call, new CallableStatementCallback<EquipmentInfo>() {

				@Override
				public EquipmentInfo doInCallableStatement(CallableStatement callable) throws SQLException, DataAccessException {

					callable.setString(1, phoneNumber);
					callable.registerOutParameter(2, Types.VARCHAR);
					callable.registerOutParameter(3, Types.VARCHAR);
					callable.registerOutParameter(4, Types.VARCHAR);
					callable.registerOutParameter(5, Types.VARCHAR);
					callable.registerOutParameter(6, Types.VARCHAR);
					callable.registerOutParameter(7, Types.VARCHAR);
					callable.registerOutParameter(8, Types.NUMERIC);
					callable.registerOutParameter(9, Types.NUMERIC);
					callable.registerOutParameter(10, Types.NUMERIC);
					callable.registerOutParameter(11, Types.VARCHAR);
					callable.registerOutParameter(12, Types.NUMERIC);
					callable.registerOutParameter(13, Types.VARCHAR);
					callable.registerOutParameter(14, Types.VARCHAR);
					callable.registerOutParameter(15, Types.NUMERIC);
					callable.registerOutParameter(16, Types.VARCHAR);
					callable.registerOutParameter(17, Types.NUMERIC);
					callable.registerOutParameter(18, Types.VARCHAR);
					callable.registerOutParameter(19, Types.VARCHAR);
					callable.registerOutParameter(20, Types.NUMERIC);
					callable.registerOutParameter(21, Types.VARCHAR);
					callable.registerOutParameter(22, Types.VARCHAR);
					callable.registerOutParameter(23, Types.VARCHAR);
					callable.registerOutParameter(24, Types.VARCHAR);
					callable.registerOutParameter(24, Types.VARCHAR);
					callable.registerOutParameter(25, Types.VARCHAR);
					callable.registerOutParameter(26, Types.VARCHAR);
					callable.registerOutParameter(27, Types.VARCHAR);
					callable.registerOutParameter(28, Types.VARCHAR);
					callable.registerOutParameter(29, Types.VARCHAR);
					callable.registerOutParameter(30, Types.VARCHAR);
					callable.registerOutParameter(31, Types.NUMERIC);
					callable.registerOutParameter(32, Types.VARCHAR);
					callable.registerOutParameter(33, Types.VARCHAR);
					callable.registerOutParameter(34, Types.NUMERIC);
					callable.registerOutParameter(35, Types.VARCHAR);
					callable.registerOutParameter(36, Types.VARCHAR);
					callable.registerOutParameter(37, Types.VARCHAR);
					callable.registerOutParameter(38, Types.VARCHAR);
					callable.registerOutParameter(39, Types.VARCHAR);
					callable.registerOutParameter(40, Types.NUMERIC);
					callable.registerOutParameter(41, Types.VARCHAR);
					callable.registerOutParameter(42, Types.VARCHAR);
					callable.registerOutParameter(43, Types.NUMERIC);
					callable.registerOutParameter(44, Types.DATE); // po_equip_status_dt
					callable.registerOutParameter(45, Types.VARCHAR); // po_puk
					callable.registerOutParameter(46, Types.NUMERIC); // ppoduct_category_id
					callable.registerOutParameter(47, Types.NUMERIC); // virtual
					callable.registerOutParameter(48, Types.VARCHAR); // rim pin
					callable.registerOutParameter(49, Types.ARRAY, "BRAND_ARRAY"); // brand id
					// added for USIM
					callable.registerOutParameter(50, Types.VARCHAR); // assohandsetimei_for_usim
					callable.registerOutParameter(51, Types.VARCHAR); // local_imsi
					callable.registerOutParameter(52, Types.VARCHAR); // remote_imsi
					callable.registerOutParameter(53, Types.NUMERIC); // assignable
					callable.registerOutParameter(54, Types.NUMERIC); // previously_activated
					callable.registerOutParameter(55, Types.VARCHAR); // equipment_group
					callable.registerOutParameter(56, Types.VARCHAR); // last_event_type
					callable.registerOutParameter(57, Types.VARCHAR); // network type
					callable.registerOutParameter(58, Types.VARCHAR); // pre_post_paid_flag
					callable.execute();
					
					EquipmentInfo equipmentInfo = new EquipmentInfo();
					long[] productPromoTypeList = new long[0];
					
					equipmentInfo.setSerialNumber(callable.getString(2));
					equipmentInfo.setTechType(callable.getString(3));
					equipmentInfo.setProductCode(callable.getString(4));
					equipmentInfo.setProductStatusCode(callable.getString(5));
					equipmentInfo.setVendorName(callable.getString(6));
					equipmentInfo.setVendorNo(callable.getString(7));
					equipmentInfo.setEquipmentStatusTypeID(callable.getLong(8));
					equipmentInfo.setEquipmentStatusID(callable.getLong(9));
					equipmentInfo.setSublock1(callable.getString(11));
					equipmentInfo.setStolen(callable.getInt(10) == 1);
					equipmentInfo.setProductGroupTypeID(callable.getLong(12));
					equipmentInfo.setProductGroupTypeCode(callable.getString(13));
					equipmentInfo.setProductGroupTypeDescription(callable.getString(14));
					equipmentInfo.setProductTypeID(callable.getLong(15));
					equipmentInfo.setProductTypeDescription(callable.getString(16));
					equipmentInfo.setProductClassID(callable.getLong(17));
					equipmentInfo.setProductClassCode(callable.getString(18));
					equipmentInfo.setProductClassDescription(callable.getString(19));
					equipmentInfo.setProviderOwnerID(callable.getLong(20));
					equipmentInfo.setLastMuleIMEI(callable.getString(21));
					// equipmentInfo.setEquipmentType(callable.getString(3).equals("ANA") ? 'A' : 'D');
					equipmentInfo.setProductName(callable.getString(22));
					equipmentInfo.setProductNameFrench(callable.getString(23));
					equipmentInfo.setBrowserVersion(callable.getString(24));
					equipmentInfo.setFirmwareVersion(callable.getString(25));
					equipmentInfo.setPRLCode(callable.getString(26));
					equipmentInfo.setPRLDescription(callable.getString(27));
					equipmentInfo.setProductGroupTypeDescriptionFrench(callable.getString(29) == null ? callable.getString(14) 
							: callable.getString(29).equals("")	? callable.getString(14) : callable.getString(29));					
					equipmentInfo.setProductTypeDescriptionFrench(callable.getString(28) == null ? callable.getString(16) : 
						callable.getString(28).equals("") ? callable.getString(16) : callable.getString(28));
					equipmentInfo.setPhoneNumber(phoneNumber);
					equipmentInfo.setBanID(callable.getInt(31));
					if (!(callable.getString(32) == null)) {
						int i = callable.getString(32).length() / 8;
						productPromoTypeList = new long[i];
						for (int j = 0; j < i - 1; j++) {
							productPromoTypeList[j] = Long.parseLong(callable.getString(32).substring(j * 8, j * 8 + 8));
						}
					}
					equipmentInfo.setProductPromoTypeList(productPromoTypeList);
					equipmentInfo.setInitialActivation("Y".equals(callable.getString(33)));
					equipmentInfo.setModeCode(callable.getLong(34));
					equipmentInfo.setModeDescription(callable.getString(35));
					equipmentInfo.setProductType(callable.getString(36) == null ? "" : callable.getString(36));
					equipmentInfo.setEquipmentType(callable.getString(37) == null ? "" : callable.getString(37));
					equipmentInfo.setModelType(callable.getString(37) == null ? "" : callable.getString(37));
					equipmentInfo.setEquipmentTypeClass(callable.getString(38) == null ? "" : callable.getString(38));
					equipmentInfo.setLegacy(equipmentInfo.isSIMCard() ? false : equipmentInfo.isMule() ? false : callable.getString(39) == null ? true 
							: "N".equals(callable.getString(39)));
					equipmentInfo.setUnscanned(callable.getInt(43) == 1);
					equipmentInfo.setEquipmentStatusDate(callable.getDate(44));
					equipmentInfo.setPUKCode(callable.getString(45) == null ? "" : callable.getString(45));
					equipmentInfo.setProductCategoryId(callable.getLong(46));
					equipmentInfo.setRIMPin(callable.getString(48));
					equipmentInfo.setBrandIds(getBrandArrays(callable.getArray(49)));
					// added for USIM
					equipmentInfo.setAssociatedHandsetIMEI(callable.getString(50));
					ProfileInfo profile = new ProfileInfo();
					profile.setLocalIMSI(callable.getString(51));
					profile.setRemoteIMSI(callable.getString(52));
					equipmentInfo.setProfile(profile);
					equipmentInfo.setAvailableUSIM(callable.getInt(53) == 1);
					equipmentInfo.setVirtual(callable.getInt(47) == 1);
					equipmentInfo.setPreviouslyActivated(callable.getInt(54) == 1);
					equipmentInfo.setEquipmentGroup(callable.getString(55));
					equipmentInfo.setLastAssociatedHandsetEventType(callable.getString(56));
					equipmentInfo.setNetworkType(callable.getString(57) == null ? "" : callable.getString(57));
					equipmentInfo.setProductPrePostpaidFlag(callable.getString(58));

					return equipmentInfo;
				}
			});
			
		} catch (UncategorizedSQLException usqle) {
			SQLException sqle = usqle.getSQLException();
			if (sqle != null) {
				if (sqle.getErrorCode() == 20317) {
					throw new ApplicationException(SystemCodes.CMB_PEH_DAO, ErrorCodes.PHONE_NUMBER_NOT_EXIST, EquipmentErrorMessages.PHONE_NUMBER_NOT_EXIST_EN, "", sqle);
				}
			}
			throw usqle;
		}
	}

	@Override
	public String getIMEIBySIM(final String pSimID) throws ApplicationException {
		String call = "{call Client_Equipment.GetIMEIBySIM(?,?)}";
		try{
		String imeiID=null;
		imeiID=getDistJdbcTemplate().execute(call, new CallableStatementCallback<String>() {
			
			@Override
			public String doInCallableStatement(CallableStatement callable) throws SQLException, DataAccessException {
				String imeiID="";
				
				callable.setString(1, pSimID);
		        callable.registerOutParameter(2,Types.VARCHAR);
		        callable. execute();
		        imeiID=callable.getString(2);
		        return imeiID;
			}
		});
		
		if (imeiID== null)
			throw new ApplicationException (SystemCodes.CMB_PEH_DAO,
					ErrorCodes.MULE_NOT_FOUND, EquipmentErrorMessages.MULE_NOT_FOUND_EN,"");
		
		return imeiID;
		
		}catch (UncategorizedSQLException e) {
			SQLException sqe = e.getSQLException();
			if (sqe != null) {
				if (sqe.getErrorCode()==20315)
		        {
					throw new ApplicationException (SystemCodes.CMB_PEH_DAO,ErrorCodes.MULTIPLE_MULE_FOUND,
							EquipmentErrorMessages.MULTIPLE_MULE_FOUND_EN,"", sqe);
		        }
				
			}
			throw e;
		}
	}

	@Override
	public String getIMEIByUSIMID(final String pUSimID) throws ApplicationException {
		String call = "{call Client_Equipment.GetIMEIByUSIMID(?,?,?)}";
		try{
		String imeiID=null;
		imeiID=getDistJdbcTemplate().execute(call, new CallableStatementCallback<String>() {
			
			@Override
			public String doInCallableStatement(CallableStatement callable) throws SQLException, DataAccessException {
				String imeiID="";
				
				callable.setString(1, pUSimID);
				callable.registerOutParameter(2,Types.VARCHAR);
				callable.registerOutParameter(3,Types.VARCHAR);
				
				callable. execute();
				
				imeiID=callable.getString(2);
		        return imeiID;
			}
		});
		if (imeiID== null)
			throw new ApplicationException (SystemCodes.CMB_PEH_DAO,
					ErrorCodes.MULE_NOT_FOUND, EquipmentErrorMessages.MULE_NOT_FOUND_EN,"");
		return imeiID;
		}catch (UncategorizedSQLException e) {
			SQLException sqe = e.getSQLException();
			if (sqe != null) {
				if (sqe.getErrorCode()==20315)
		        {	//old Error code: VAL20028
					throw new ApplicationException (SystemCodes.CMB_PEH_DAO,ErrorCodes.MULTIPLE_MULE_FOUND,
							EquipmentErrorMessages.MULTIPLE_ASSOCIATED_HANDSETS_EN,"", sqe);
		        }
				
			}
			throw e;
		}
	}

	@Override
	public EquipmentInfo getMuleBySIM(final String pSimID)
			throws ApplicationException {
		String call="{call Client_Equipment.GetMuleBySIM(?,?,?,?,?,?,?,?,?,?," +
                                                        "?,?,?,?,?,?,?,?,?,?, " +
                                                        "?,?,?,?,?,?,?,?,?,?, " +
                                                        "?,?,?,?,?,?,?,?,?,?, " +
                                                        "?,?,?,?,?,?,?,?,?,?, " +
                                                        "?,?,?,?,?,? )}";
		try{
		 return getDistJdbcTemplate().execute(call, new CallableStatementCallback<EquipmentInfo>() {
			
			@Override
			public EquipmentInfo doInCallableStatement(CallableStatement callable) throws SQLException, DataAccessException {
				EquipmentInfo     equipInfo =null ;
				long[] productPromoTypeList = new long[0];
				String[] firmwareFeatureList = new String[0];
				int i=0, j=0;
			
				callable.setString(1, pSimID);
		         callable.registerOutParameter(2,Types.VARCHAR );
		        callable.registerOutParameter(3,Types.VARCHAR );
		        callable.registerOutParameter(4,Types.VARCHAR);
		        callable.registerOutParameter(5,Types.VARCHAR);
		        callable.registerOutParameter(6,Types.VARCHAR);
		        callable.registerOutParameter(7,Types.VARCHAR);
		        callable.registerOutParameter(8,Types.NUMERIC);
		        callable.registerOutParameter(9,Types.NUMERIC);
		        callable.registerOutParameter(10,Types.NUMERIC);
		        callable.registerOutParameter(11,Types.VARCHAR);
		        callable.registerOutParameter(12,Types.NUMERIC);
		        callable.registerOutParameter(13,Types.VARCHAR);
		        callable.registerOutParameter(14,Types.VARCHAR);
		        callable.registerOutParameter(15,Types.NUMERIC);
		        callable.registerOutParameter(16,Types.VARCHAR);
		        callable.registerOutParameter(17,Types.NUMERIC);
		        callable.registerOutParameter(18,Types.VARCHAR);
		        callable.registerOutParameter(19,Types.VARCHAR);
		         callable.registerOutParameter(20,Types.NUMERIC);
		        callable.registerOutParameter(21,Types.VARCHAR);
		        callable.registerOutParameter(22,Types.VARCHAR);
		        callable.registerOutParameter(23,Types.VARCHAR);
		        callable.registerOutParameter(24,Types.VARCHAR);
		        callable.registerOutParameter(24,Types.VARCHAR);
		        callable.registerOutParameter(25,Types.VARCHAR);
		        callable.registerOutParameter(26,Types.VARCHAR);
		        callable.registerOutParameter(27,Types.VARCHAR);
		        callable.registerOutParameter(28,Types.VARCHAR);
		        callable.registerOutParameter(29,Types.VARCHAR);
		        callable.registerOutParameter(30,Types.VARCHAR);
		        callable.registerOutParameter(31,Types.NUMERIC);
		        callable.registerOutParameter(32,Types.VARCHAR);
		        callable.registerOutParameter(33,Types.VARCHAR);
		        callable.registerOutParameter(34,Types.NUMERIC);
		        callable.registerOutParameter(35,Types.VARCHAR);
		        callable.registerOutParameter(36,Types.VARCHAR);//product type
		        callable.registerOutParameter(37,Types.VARCHAR);// equipment type
		        callable.registerOutParameter(38,Types.VARCHAR); //equipment type class
		        callable.registerOutParameter(39,Types.VARCHAR); //
		        callable.registerOutParameter(40,Types.NUMERIC);
		        callable.registerOutParameter(41,Types.VARCHAR); // pager - cap code
		        callable.registerOutParameter(42,Types.VARCHAR); // pager - coverage region list
		        callable.registerOutParameter(43,Types.VARCHAR); // pager - encoding format code
		        callable.registerOutParameter(44,Types.VARCHAR); // pager - ownership code
		        callable.registerOutParameter(45,Types.VARCHAR); // pager - prepaid
		        callable.registerOutParameter(46,Types.VARCHAR); // pager - frequency cd
		        callable.registerOutParameter(47,Types.VARCHAR); // firmware feature code list
		        callable.registerOutParameter(48,Types.VARCHAR); // browser_protocol
		        callable.registerOutParameter(49,Types.NUMERIC);
		        callable.registerOutParameter(50,Types.DATE);    // equipment status date
		        callable.registerOutParameter(51,Types.VARCHAR); // PUK Code
		        callable.registerOutParameter(52,Types.NUMERIC);  // ppoduct_category_id
		        callable.registerOutParameter(53,Types.VARCHAR); // rim pin
		        callable.registerOutParameter(54,Types.ARRAY, "BRAND_ARRAY"); // brand indicator
		        callable.registerOutParameter(55,Types.VARCHAR); // equipment_group
		        callable.registerOutParameter(56,Types.VARCHAR);
		        
		        callable. execute();
		        if (callable.getString(2)!=null)
		       { boolean stolenEquipment;
		          if (callable.getInt(10)==1)
		         {stolenEquipment=true;}
		          else
		         {stolenEquipment=false;}

		          equipInfo= new EquipmentInfo();
		          equipInfo.setSerialNumber(callable.getString(2));
		          equipInfo.setTechType(callable.getString(3));
		          equipInfo.setProductCode(callable.getString(4));
		          equipInfo.setProductStatusCode(callable.getString(5));
		          equipInfo.setVendorName(callable.getString(6));
		          equipInfo.setVendorNo(callable.getString(7));
		          equipInfo.setEquipmentStatusTypeID(callable.getLong(8));
		          equipInfo.setEquipmentStatusID(callable.getLong(9));
		          equipInfo.setSublock1(callable.getString(11));
		          equipInfo.setStolen(stolenEquipment);
		          equipInfo.setProductGroupTypeID(callable.getLong(12));
		          equipInfo.setProductGroupTypeCode(callable.getString(13));
		          equipInfo.setProductGroupTypeDescription(callable.getString(14));
		          equipInfo.setProductTypeID(callable.getLong(15));
		          equipInfo.setProductTypeDescription(callable.getString(16));
		          equipInfo.setProductClassID(callable.getLong(17));
		          equipInfo.setProductClassCode(callable.getString(18));
		          equipInfo.setProductClassDescription(callable.getString(19));
		          equipInfo.setProviderOwnerID(callable.getLong(20));
		          equipInfo.setLastMuleIMEI(callable.getString(21));
		          equipInfo.setProductName(callable.getString(22));
		          equipInfo.setProductNameFrench(callable.getString(23));
		          equipInfo.setBrowserVersion(callable.getString(24));
		          equipInfo.setFirmwareVersion(callable.getString(25));
		          equipInfo.setPRLCode(callable.getString(26));
		          equipInfo.setPRLDescription(callable.getString(27));
		          equipInfo.setProductGroupTypeDescriptionFrench(callable.getString(29)==null ? callable.getString(14) : callable.getString(29).equals("") ? callable.getString(14) : callable.getString(29));
		          equipInfo.setProductTypeDescriptionFrench(callable.getString(28)==null ? callable.getString(16) : callable.getString(28).equals("") ? callable.getString(16) : callable.getString(28));
		         // equipInfo.setSubscriberNumber(callable.getString(30));
		          //equipInfo.setBanID(callable.getInt(31));
		          if (!(callable.getString(32)==null))
		          { i =callable.getString(32).length()/8;
		            productPromoTypeList = new long[i];
		            for (j=0; j< i; j++)
		            { productPromoTypeList[j]=Long.parseLong(callable.getString(32).substring(j*8,j*8+8));
		            }
		          }
		          equipInfo.setProductPromoTypeList(productPromoTypeList);
		          equipInfo.setInitialActivation(callable.getString(33)==null ? false :callable.getString(33).equals("Y") ? true :false );
		          equipInfo.setModeCode(callable.getLong(34));
		          equipInfo.setModeDescription(callable.getString(35));
		          equipInfo.setProductType(callable.getString(36)==null ? "" : callable.getString(36));
		          equipInfo.setEquipmentType(callable.getString(37)==null ? "" : callable.getString(37));
		          equipInfo.setEquipmentTypeClass(callable.getString(38)==null ? "" : callable.getString(38));
		          equipInfo.setLegacy(equipInfo.isSIMCard() ? false : equipInfo.isMule() ? false : callable.getString(39)==null ? true : callable.getString(39).equals("N") ? true : false );

		          //equipInfo.setContractTermCredits(retrieveContractTermCredits(equipInfo.getProductGroupTypeID(),equipInfo.getProductTypeID()));

		          //firmwareVersionFeatureCodes
		          if (!(callable.getString(47)==null))
		          {
		            StringTokenizer strToken = new StringTokenizer(callable.getString(47),"|");
		            firmwareFeatureList = new String[strToken.countTokens()];
		            i = 0;
		            while (strToken.hasMoreTokens()) {
		              firmwareFeatureList[i] = strToken.nextToken();
		              i++;
		            }
		          }
		          equipInfo.setFirmwareVersionFeatureCodes(firmwareFeatureList);

		          // browserProtocol
		          equipInfo.setBrowserProtocol(callable.getString(48));
		          equipInfo.setEquipmentStatusDate(callable.getDate(50));
		          equipInfo.setPUKCode(callable.getString(51)==null ? "" : callable.getString(51));
		          equipInfo.setProductCategoryId(callable.getLong(52));

		          equipInfo.setRIMPin(callable.getString(53));
		          equipInfo.setBrandIds(getBrandArrays(callable.getArray(54)));
		          equipInfo.setEquipmentGroup(callable.getString(55));
		          equipInfo.setNetworkType(callable.getString(56)==null ? "" :callable.getString(56));
		          equipInfo.setEquipmentType(Equipment.EQUIPMENT_TYPE_DIGITAL);
		          
			   }
		        return equipInfo;
		       }
			});
			}catch (UncategorizedSQLException e) {
			SQLException sqe = e.getSQLException();
			if (sqe != null) {
				if ((!(sqe.getErrorCode()==20315))&&(!(sqe.getErrorCode()==20310)))
				{  throw new ApplicationException(SystemCodes.CMB_PEH_DAO, sqe.getMessage(), "");
				}
			}
			throw e;
			}
	}


	

	@Override
	public String getSIMByIMEI(final String pImeiID) throws ApplicationException {
		String call = "{call Client_Equipment.GetSIMByIMEI(?,?)}";
		try{
		String simid=null;
		simid=getDistJdbcTemplate().execute(call, new CallableStatementCallback<String>() {
			
			@Override
			public String doInCallableStatement(CallableStatement callable) throws SQLException, DataAccessException {
				String simID=null;
				callable.setString(1, pImeiID);
		        callable.registerOutParameter(2,Types.VARCHAR);

		        callable. execute();

		        simID=callable.getString(2);
				return simID;
			}
		}); 
		if (simid== null)
			throw new ApplicationException (SystemCodes.CMB_PEH_DAO,ErrorCodes.SIM_NOT_FOUND, "SIM card is not found");
	        
		return simid;
		}catch (UncategorizedSQLException e) {
			SQLException sqe = e.getSQLException();
			if (sqe != null) {
				 if ((sqe.getErrorCode()==20314)||(sqe.getErrorCode()==20316))
		        {	
					throw new ApplicationException (SystemCodes.CMB_PEH_DAO,
							ErrorCodes.SIM_NOT_FOUND,EquipmentErrorMessages.SIM_NOT_FOUND_EN,"", sqe);
		        }
				
			}
			throw e;
		}
	  }

	@Override
	public String getUSIMByIMSI(final String pIMSI) throws ApplicationException {
		String call = "{call Client_Equipment.getUSIMByIMSI(?,?)}";
		
		return getDistJdbcTemplate().execute(call, new CallableStatementCallback<String>() {
			
			@Override
			public String doInCallableStatement(CallableStatement callable) throws SQLException, DataAccessException {
				 String USIMId ="";
				 callable.setString(1, pIMSI);
			     callable.registerOutParameter(2,Types.VARCHAR);
 		         callable. execute();
 		         USIMId = callable.getString(2);
				return USIMId;
			}
		}); 
	}

	@Override
	public Hashtable<String, String> getUSIMListByIMSIs(final String[] IMISIs)
			throws ApplicationException {
		
		return getDistJdbcTemplate().execute( new ConnectionCallback<Hashtable<String, String>>() {
			
			@Override
			public Hashtable<String, String> doInConnection(Connection connection)
				throws SQLException, DataAccessException {
				
				// return object
				Hashtable<String, String> USIMList = new Hashtable<String, String>();
				ResultSet result = null;
				OracleCallableStatement callable = null;
				
				try{
				   // prepare callable statement
				   callable = (OracleCallableStatement) connection.prepareCall("{ call Client_Equipment.getUSIMListByIMSIs(?,?) }");
					
				   // create array descriptor
				   ArrayDescriptor IMSIsArrayDesc = ArrayDescriptor.createDescriptor("T_IMSI_ARRAY", connection);
	
				   // create Oracle array of IMSIs
				   ARRAY IMSIsArray = new ARRAY(IMSIsArrayDesc, connection, IMISIs);
					  
					// set/register input/output parameters
				   callable.registerOutParameter(2, OracleTypes.CURSOR);
				   callable.setARRAY(1, IMSIsArray);
				   callable. execute();
				   result = (ResultSet)callable.getObject(2);
						
				   while (result.next()) {
				      USIMList.put(result.getString(1),result.getString(2));
				   }
			   
			   } finally {
					if (result != null) {
						result.close();
					}
					if (callable != null) {
						callable.close();
					}
					
			   }
				
			  return USIMList;
			}
		}); 
	}

	@Override
	public WarrantyInfo getWarrantyInfo(final String pSerialNo)
			throws ApplicationException {
		String call = "{call Client_Equipment.GetWarranty(?,?,?,?,?,?,?,?,?)}";
		
		return getDistJdbcTemplate().execute(call, new CallableStatementCallback<WarrantyInfo>() {
			
			@SuppressWarnings("deprecation")
			@Override
			public WarrantyInfo doInCallableStatement(CallableStatement callable) throws SQLException, DataAccessException {
				  
				  WarrantyInfo warranty = new WarrantyInfo();
				  
				  callable.setString(1, pSerialNo);
			      callable.registerOutParameter(2,Types.DATE );
			      callable.registerOutParameter(3,Types.DATE );
			      callable.registerOutParameter(4,Types.DATE);
			      callable.registerOutParameter(5,Types.DATE);
			      callable.registerOutParameter(6,Types.VARCHAR);
			      callable.registerOutParameter(7,Types.VARCHAR);
			      callable.registerOutParameter(8,Types.DATE);
			      callable.registerOutParameter(9,Types.DATE);

			      callable. execute();
			      warranty.setWarrantyExpiryDate(callable.getDate(2));
			      warranty.setInitialActivationDate(callable.getDate(3));
			      warranty.setInitialManufactureDate(callable.getDate(4));
			      warranty.setLatestPendingDate(callable.getDate(5));
			      warranty.setLatestPendingModel(callable.getString(6));
			      warranty.setMessage(callable.getString(7));
			      warranty.setWarrantyExtensionDate(callable.getDate(8));
			      warranty.setDOAExpiryDate(callable.getDate(9));
				return warranty;
			}
		}); 
	}
	

	@Override
	public boolean isInUse(final String pSerialNo) throws ApplicationException {
		
	    String queryStr = " select  subscriber_no" +
	                        " from physical_device pd" +
	                        " where pd.unit_esn = ? " +
	                        " and pd.expiration_date is null " ;
	    
		return getKnowbilityJdbcTemplate().execute(queryStr, new PreparedStatementCallback<Boolean>() {
			@Override
			public Boolean doInPreparedStatement(PreparedStatement pstmt)
			throws SQLException, DataAccessException {

				ResultSet result = null;
				boolean   inUse = false;
				try{
					pstmt.setString(1,pSerialNo);
				    result = pstmt.executeQuery();
				    while (result.next())
				    {
				    	inUse = true ;
				    }
				} finally {
					if (result != null) {
						result.close();
					}
				}
				return inUse;
			}
		});
		
	}


	@Override
	public EquipmentInfo retrievePagerEquipmentInfo(final String serialNo)
			throws ApplicationException {
		String call = "{? = call SUBSCRIBER_PKG.GetPagerEquipmentInfo(?, ?, ?)}";
		
		return getKnowbilityJdbcTemplate().execute(call, new CallableStatementCallback<EquipmentInfo>() {
			
			@Override
			public EquipmentInfo doInCallableStatement(CallableStatement callable) throws SQLException, DataAccessException {
				
				EquipmentInfo equipmentInfo = null;
				ResultSet result = null;
				try{

				  equipmentInfo = new EquipmentInfo();
				  equipmentInfo.setProductType(Subscriber.PRODUCT_TYPE_PAGER);
			     
			      callable.registerOutParameter(1, OracleTypes.NUMBER);
			      callable.setString(2, serialNo);
			      callable.registerOutParameter(3, OracleTypes.CURSOR);
			      callable.registerOutParameter(4, OracleTypes.VARCHAR);

			      callable.execute();

			      boolean success = callable.getInt(1) == AccountManager.NUMERIC_TRUE;

			      if (success) {
			        result = (ResultSet) callable.getObject(3);

			        if (result.next()) {
			          equipmentInfo.setCurrentCoverageRegionCode(result.getString(1));
			          equipmentInfo.setEncodingFormat(result.getString(2));
			          equipmentInfo.setPossession(result.getString(3));
			          equipmentInfo.setEquipmentType(result.getString(4));
			        }
			      }else{
			    	  LOGGER.error("Stored procedure failed: " + callable.getString(4));
			      }
			    
				} finally {
					if (result != null) {
						result.close();
					}
				}
				return equipmentInfo;

			}
		}); 

	}

	@Override
	public SubscriberInfo[] retrieveHSPASubscriberIdsByIMSI(final String IMSI	) throws ApplicationException {
		  String queryStr = " SELECT   sr1.subscriber_no, sr1.ban"+
		  					" FROM subscriber_rsource sr1"+
		  					" WHERE sr1.imsi_number = ?"+
		  					" AND sr1.resource_type = 'Q'"+
		  					" AND sr1.record_exp_date = TO_DATE ('12/31/4700', 'mm/dd/yyyy')"+
		  					" AND sr1.resource_status != 'C'";
 

		  return getKnowbilityJdbcTemplate().execute(queryStr, new PreparedStatementCallback<SubscriberInfo[]>() {
			@Override
			public SubscriberInfo[] doInPreparedStatement(PreparedStatement pstmt)
			throws SQLException, DataAccessException {
				
				ArrayList<SubscriberInfo> subscriberList = new ArrayList<SubscriberInfo>();
			    ResultSet result = null;
			    SubscriberInfo subscriberInfo = null;

				 try
			      {
					 pstmt.setString(1, IMSI);
					 result = pstmt.executeQuery();

			         while (result.next()){
			        	subscriberInfo= new SubscriberInfo();
			        	subscriberInfo.setPhoneNumber(result.getString(1));
			        	subscriberInfo.setBanId(result.getInt(2));
			        	subscriberList.add(subscriberInfo);
			         }
			         
			         return (SubscriberInfo[])subscriberList.toArray(new SubscriberInfo[subscriberList.size()]);
			      } finally {
						if (result != null) {
							result.close();
						}
				  }
			}
			});
		  
		  
		  
    	}		  	
	
}
