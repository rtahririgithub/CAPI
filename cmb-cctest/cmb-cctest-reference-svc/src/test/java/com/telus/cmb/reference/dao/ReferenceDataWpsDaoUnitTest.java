package com.telus.cmb.reference.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import mockit.Deencapsulation;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.telus.api.reference.NetworkType;
import com.telus.eas.utility.info.ServiceEquipmentTypeInfo;
import com.telus.eas.utility.info.ServiceInfo;
//import com.telus.prepaid.winpas.server.subscriber.info.Category;
//import com.telus.prepaid.winpas.server.subscriber.info.Feature;
//import com.telus.prepaid.winpas.server.subscriber.info.ProductEquipmentType;
//import com.telus.prepaid.winpas.server.subscriber.info.ProductNetworkType;
//import com.telus.prepaid.winpas.server.subscriber.info.ProductSpecification;

public class ReferenceDataWpsDaoUnitTest {
//	
//	@Mock Feature mockFeature;
//	@Mock Category mockCategory;
//	
//	ReferenceDataWpsDao refDao;
//	
//	@Before
//	public void setUp() throws Exception {
//		MockitoAnnotations.initMocks(this);
//		refDao = new ReferenceDataWpsDao();
//		Mockito.when(mockFeature.getCategory()).thenReturn(mockCategory);
//	}
//
//	@After
//	public void tearDown() throws Exception {
//	}
//
//	@Test
//	public void testPrivateMapPrepaidFeatureToServiceInfo() throws Exception {
//		
//		//invoke private method
//		ServiceInfo serviceInfo = (ServiceInfo) Deencapsulation.invoke(refDao, "mapPrepaidFeatureToServiceInfo", mockFeature);
//		assertNotNull(serviceInfo);						
//		assertEquals(0, serviceInfo.getServiceEquipmentTypeInfo().size());
//	}
//	
//	/**
//	 * The naming of these tests are bad convention.  It should describe
//	 * what the test is doing.
//	 * @throws Exception
//	 */
//	@Test 
//	public void  testPrivateMapPrepaidFeatureToServiceInfo1() throws Exception {
//		ProductSpecification mockProductSpec = Mockito.mock(ProductSpecification.class);
//		Mockito.when(mockFeature.getProductSpecification()).thenReturn(mockProductSpec);
//		
//		ArrayList<ProductEquipmentType> productEquipList = new ArrayList<ProductEquipmentType>(); 
//		ProductEquipmentType productEquipType = new ProductEquipmentType();
//		productEquipList.add(productEquipType);
//		Mockito.when(mockFeature.getProductSpecification().getProductEquipmentTypeArr()).thenReturn(productEquipList);
//		
//		ServiceInfo serviceInfo = (ServiceInfo) Deencapsulation.invoke(refDao, "mapPrepaidFeatureToServiceInfo", mockFeature);
//		
//		assertNotNull(serviceInfo);
//		assertEquals(2, serviceInfo.getServiceEquipmentTypeInfo().size());
//
//		List<String> networkTypeList = Arrays.asList(serviceInfo.getAllNetworkTypes());
//		assertTrue(networkTypeList.contains(NetworkType.NETWORK_TYPE_CDMA));
//		assertTrue(networkTypeList.contains(NetworkType.NETWORK_TYPE_HSPA));
//	}
//	
//	@Test 
//	public void  testPrivateMapPrepaidFeatureToServiceInfo2() throws Exception {
//		ProductSpecification mockProductSpec = Mockito.mock(ProductSpecification.class);
//		Mockito.when(mockFeature.getProductSpecification()).thenReturn(mockProductSpec);
//		
//		ArrayList<ProductEquipmentType> productEquipList = new ArrayList<ProductEquipmentType>(); 
//		ProductEquipmentType productEquipType = Mockito.mock(ProductEquipmentType.class);
//		productEquipList.add(productEquipType);
//		Mockito.when(mockFeature.getProductSpecification().getProductEquipmentTypeArr()).thenReturn(productEquipList);
//		ArrayList<ProductNetworkType> productNetworkList = new ArrayList<ProductNetworkType>();
//		productNetworkList.add(new ProductNetworkType());
//		Mockito.when(productEquipType.getProductNetworkTypeArr()).thenReturn(productNetworkList);
//
//		ServiceInfo serviceInfo = (ServiceInfo) Deencapsulation.invoke(refDao, "mapPrepaidFeatureToServiceInfo", mockFeature);
//		assertNotNull(serviceInfo);
//		assertEquals(1, serviceInfo.getServiceEquipmentTypeInfo().size());
//		List<String> networkTypeList = Arrays.asList(serviceInfo.getAllNetworkTypes());
//		assertTrue(networkTypeList.contains(NetworkType.NETWORK_TYPE_HSPA));		
//	
//	}	
//	
//	@Test 
//	public void  testPrivateMapPrepaidFeatureToServiceInfo3() throws Exception {
//		ProductSpecification mockProductSpec = Mockito.mock(ProductSpecification.class);
//		Mockito.when(mockFeature.getProductSpecification()).thenReturn(mockProductSpec);
//		
//		ArrayList<ProductEquipmentType> productEquipList = new ArrayList<ProductEquipmentType>(); 
//		ProductEquipmentType productEquipType = Mockito.mock(ProductEquipmentType.class);
//		productEquipList.add(productEquipType);
//		Mockito.when(mockFeature.getProductSpecification().getProductEquipmentTypeArr()).thenReturn(productEquipList);
//		ArrayList<ProductNetworkType> productNetworkList = new ArrayList<ProductNetworkType>();
//		ProductNetworkType prodNetworkType = new ProductNetworkType();
//		prodNetworkType.setProductNetworkType("CDMA");
//		productNetworkList.add(prodNetworkType);
//		Mockito.when(productEquipType.getProductNetworkTypeArr()).thenReturn(productNetworkList);
//
//		ServiceInfo serviceInfo = (ServiceInfo) Deencapsulation.invoke(refDao, "mapPrepaidFeatureToServiceInfo", mockFeature);		
//		assertNotNull(serviceInfo);
//		assertEquals(1, serviceInfo.getServiceEquipmentTypeInfo().size());
//		List<String> networkTypeList = Arrays.asList(serviceInfo.getAllNetworkTypes());
//		assertTrue(networkTypeList.contains(NetworkType.NETWORK_TYPE_CDMA));		
//	
//	}
//	
//	@Test 
//	public void  testPrivateMapPrepaidFeatureToServiceInfo4() throws Exception {
//		ProductSpecification mockProductSpec = Mockito.mock(ProductSpecification.class);
//		Mockito.when(mockFeature.getProductSpecification()).thenReturn(mockProductSpec);
//		
//		ArrayList<ProductEquipmentType> productEquipList = new ArrayList<ProductEquipmentType>(); 
//		ProductEquipmentType productEquipType = Mockito.mock(ProductEquipmentType.class);
//		productEquipList.add(productEquipType);
//		Mockito.when(mockFeature.getProductSpecification().getProductEquipmentTypeArr()).thenReturn(productEquipList);
//
//		ArrayList<ProductNetworkType> productNetworkList = new ArrayList<ProductNetworkType>();
//		ProductNetworkType prodNetworkType = new ProductNetworkType();
//		prodNetworkType.setProductNetworkType("CDMA");
//		productNetworkList.add(prodNetworkType);
//		prodNetworkType = new ProductNetworkType();
//		prodNetworkType.setProductNetworkType("NONSENSICAL");
//		productNetworkList.add(prodNetworkType);		
//		Mockito.when(productEquipType.getProductNetworkTypeArr()).thenReturn(productNetworkList);
//
//		ServiceInfo serviceInfo = (ServiceInfo) Deencapsulation.invoke(refDao, "mapPrepaidFeatureToServiceInfo", mockFeature);		
//		assertNotNull(serviceInfo);
//		assertEquals(2, serviceInfo.getServiceEquipmentTypeInfo().size());
//		List<String> networkTypeList = Arrays.asList(serviceInfo.getAllNetworkTypes());
//		assertTrue(networkTypeList.contains(NetworkType.NETWORK_TYPE_CDMA));
//		assertTrue(networkTypeList.contains(NetworkType.NETWORK_TYPE_HSPA));
//	
//	}		
//
//	
//	@Test 
//	public void  testPrivateMapPrepaidFeatureToServiceInfo5() throws Exception {
//		ProductSpecification mockProductSpec = Mockito.mock(ProductSpecification.class);
//		Mockito.when(mockFeature.getProductSpecification()).thenReturn(mockProductSpec);
//		
//		ArrayList<ProductEquipmentType> productEquipList = new ArrayList<ProductEquipmentType>(); 
//		ProductEquipmentType productEquipType = Mockito.mock(ProductEquipmentType.class);
//		productEquipList.add(productEquipType);
//		Mockito.when(mockFeature.getProductSpecification().getProductEquipmentTypeArr()).thenReturn(productEquipList);
//
//		ArrayList<ProductNetworkType> productNetworkList = new ArrayList<ProductNetworkType>();
//		ProductNetworkType prodNetworkType = new ProductNetworkType();
//		prodNetworkType.setProductNetworkType("CDMA");
//		productNetworkList.add(prodNetworkType);
//		prodNetworkType = new ProductNetworkType();
//		prodNetworkType.setProductNetworkType("CDMA");
//		productNetworkList.add(prodNetworkType);
//		Mockito.when(productEquipType.getProductNetworkTypeArr()).thenReturn(productNetworkList);
//
//		ServiceInfo serviceInfo = (ServiceInfo) Deencapsulation.invoke(refDao, "mapPrepaidFeatureToServiceInfo", mockFeature);		
//		assertNotNull(serviceInfo);
//		assertEquals(1, serviceInfo.getServiceEquipmentTypeInfo().size());
//		List<String> networkTypeList = Arrays.asList(serviceInfo.getAllNetworkTypes());
//		assertTrue(networkTypeList.contains(NetworkType.NETWORK_TYPE_CDMA));
//	}		
//			
//	@Test 
//	public void  testPrivateMapPrepaidFeatureToServiceInfo6() throws Exception {
//		ProductSpecification mockProductSpec = Mockito.mock(ProductSpecification.class);
//		Mockito.when(mockFeature.getProductSpecification()).thenReturn(mockProductSpec);
//		
//		ArrayList<ProductEquipmentType> productEquipList = new ArrayList<ProductEquipmentType>(); 
//		ProductEquipmentType productEquipType1 = Mockito.mock(ProductEquipmentType.class);
//		productEquipList.add(productEquipType1);
//		ProductEquipmentType productEquipType2 = Mockito.mock(ProductEquipmentType.class);
//		productEquipList.add(productEquipType2);
//		Mockito.when(mockFeature.getProductSpecification().getProductEquipmentTypeArr()).thenReturn(productEquipList);
//
//		ArrayList<ProductNetworkType> productNetworkList = new ArrayList<ProductNetworkType>();
//		ProductNetworkType prodNetworkType = new ProductNetworkType();
//		prodNetworkType.setProductNetworkType("CDMA");
//		productNetworkList.add(prodNetworkType);
//		prodNetworkType = new ProductNetworkType();
//		prodNetworkType.setProductNetworkType("CDMA");
//		productNetworkList.add(prodNetworkType);
//		Mockito.when(productEquipType1.getProductNetworkTypeArr()).thenReturn(productNetworkList);
//		
//		productNetworkList = new ArrayList<ProductNetworkType>();
//		prodNetworkType = new ProductNetworkType();
//		prodNetworkType.setProductNetworkType("HSPA");
//		productNetworkList.add(prodNetworkType);
//		Mockito.when(productEquipType2.getProductNetworkTypeArr()).thenReturn(productNetworkList);
//
//		ServiceInfo serviceInfo = (ServiceInfo) Deencapsulation.invoke(refDao, "mapPrepaidFeatureToServiceInfo", mockFeature);
//		assertNotNull(serviceInfo);
//		assertEquals(2, serviceInfo.getServiceEquipmentTypeInfo().size());
//		List<String> networkTypeList = Arrays.asList(serviceInfo.getAllNetworkTypes());
//		assertTrue(networkTypeList.contains(NetworkType.NETWORK_TYPE_CDMA));
//		assertTrue(networkTypeList.contains(NetworkType.NETWORK_TYPE_HSPA));
//	}		
//	
//	@Test 
//	public void  testPrivateMapPrepaidFeatureToServiceInfo7() throws Exception {
//		int featureId = 123;
//		
//		ProductSpecification mockProductSpec = Mockito.mock(ProductSpecification.class);
//		Mockito.when(mockFeature.getProductSpecification()).thenReturn(mockProductSpec);
//		Mockito.when(mockFeature.getId()).thenReturn(featureId);
//		
//		ArrayList<ProductEquipmentType> productEquipList = new ArrayList<ProductEquipmentType>(); 
//		ProductEquipmentType productEquipType1 = Mockito.mock(ProductEquipmentType.class);
//		productEquipList.add(productEquipType1);
//		ProductEquipmentType productEquipType2 = Mockito.mock(ProductEquipmentType.class);
//		productEquipList.add(productEquipType2);
//		Mockito.when(mockFeature.getProductSpecification().getProductEquipmentTypeArr()).thenReturn(productEquipList);
//
//		ArrayList<ProductNetworkType> productNetworkList = new ArrayList<ProductNetworkType>();
//		ProductNetworkType prodNetworkType = new ProductNetworkType();
//		prodNetworkType.setProductNetworkType("CDMA");
//		productNetworkList.add(prodNetworkType);
//		prodNetworkType = new ProductNetworkType();
//		prodNetworkType.setProductNetworkType("CDMA");
//		productNetworkList.add(prodNetworkType);
//		Mockito.when(productEquipType1.getProductNetworkTypeArr()).thenReturn(productNetworkList);
//		
//		productNetworkList = new ArrayList<ProductNetworkType>();
//		prodNetworkType = new ProductNetworkType();
//		prodNetworkType.setProductNetworkType("HSPA");
//		productNetworkList.add(prodNetworkType);
//		Mockito.when(productEquipType2.getProductNetworkTypeArr()).thenReturn(productNetworkList);
//
//		ServiceInfo serviceInfo = (ServiceInfo) Deencapsulation.invoke(refDao, "mapPrepaidFeatureToServiceInfo", mockFeature);		
//		assertNotNull(serviceInfo);
//		assertEquals(2, serviceInfo.getServiceEquipmentTypeInfo().size());
//		List<String> networkTypeList = Arrays.asList(serviceInfo.getAllNetworkTypes());
//		assertTrue(networkTypeList.contains(NetworkType.NETWORK_TYPE_CDMA));
//		assertTrue(networkTypeList.contains(NetworkType.NETWORK_TYPE_HSPA));
//		assertEquals(String.valueOf(featureId), serviceInfo.getServiceEquipmentTypeInfo(NetworkType.NETWORK_TYPE_CDMA).getCode().trim());
//		assertEquals(NetworkType.NETWORK_TYPE_CDMA, serviceInfo.getServiceEquipmentTypeInfo(NetworkType.NETWORK_TYPE_CDMA).getNetworkType());
//		assertEquals(String.valueOf(featureId), serviceInfo.getServiceEquipmentTypeInfo(NetworkType.NETWORK_TYPE_HSPA).getCode().trim());
//		assertEquals(NetworkType.NETWORK_TYPE_HSPA, serviceInfo.getServiceEquipmentTypeInfo(NetworkType.NETWORK_TYPE_HSPA).getNetworkType());
//
//	}	
//	
//	
//	
//	@SuppressWarnings("unchecked")
//	@Test
//	public void testEquipmentTypeMapping() throws Exception {
//		String code = "123";
//
//		List<ProductEquipmentType> equipmentTypes = new ArrayList<ProductEquipmentType>();
//		equipmentTypes.add( newProductEquipmentType("D", null )); //null 
//		equipmentTypes.add( newProductEquipmentType("Z", new String[0] ) ); // empty list
//		equipmentTypes.add( newProductEquipmentType("P", new String[]{"CDMA"} ) ); //single network
//		equipmentTypes.add( newProductEquipmentType("3", new String[]{"HSPA","CDMA"} ) ); //both network
//		
//		equipmentTypes.add( newProductEquipmentType("H", new String[]{"HSPA"} ) ); // HSPA
//		equipmentTypes.add( newProductEquipmentType("UNKNOWN", new String[]{"unknown"} ) ); // unknown
//				
//		Collection<ServiceEquipmentTypeInfo> serviceEquipTypeInfoList = (Collection<ServiceEquipmentTypeInfo>) Deencapsulation.invoke(refDao, "mapPrepaidEquipmentType", code, equipmentTypes);		
//		assertEquals(2, serviceEquipTypeInfoList.size());
//		
//		String[] expectedCDMA = new String[] {"D", "Z", "P", "3"};
//		String[] expectedHSPA = new String[] {"D", "Z", "3", "H", "UNKNOWN"};
//		
//		for (ServiceEquipmentTypeInfo info : serviceEquipTypeInfoList) {
//			assertEquals(code, info.getCode());
//			if(NetworkType.NETWORK_TYPE_CDMA.equals(info.getNetworkType())) {
//				assertTrue(info.getEquipmentTypes().containsAll(Arrays.asList(expectedCDMA)));
//			} else if (NetworkType.NETWORK_TYPE_HSPA.equals(info.getNetworkType())) {
//				assertTrue(info.getEquipmentTypes().containsAll(Arrays.asList(expectedHSPA)));
//			}
//		}		
//	}
//	
//	private ProductEquipmentType newProductEquipmentType( String equiptType, String[] networks ) {
//		ProductEquipmentType e = new ProductEquipmentType();
//		e.setEquipmentType(equiptType);
//		if ( networks==null )
//			e.setProductNetworkTypeArr( null );
//		else {
//			ArrayList<ProductNetworkType> l  = new ArrayList<ProductNetworkType>();
//			for ( String n : networks ) {
//				ProductNetworkType pnt = new ProductNetworkType();
//				pnt.setProductNetworkType(n);
//				l.add( pnt );
//			}
//			e.setProductNetworkTypeArr(l);
//		}
//		return e;
//	}
//
}
