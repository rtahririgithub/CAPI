package com.telus.provider.equipment;



import com.telus.api.BaseTest;
import com.telus.api.TelusAPIException;
import com.telus.api.account.Subscriber;
import com.telus.api.account.UnknownSubscriberException;
import com.telus.api.equipment.Card;
import com.telus.api.equipment.Equipment;
import com.telus.api.reference.Brand;
import com.telus.provider.TestTMProvider;
import com.telus.provider.account.TMSubscriber;

public class TMEquipmentManagerIntTest extends BaseTest {
	
	static {
		setupEASECA_QA();
		//setupD3();
	}
	
	public TMEquipmentManagerIntTest(String name) throws Throwable {
		super(name);
	}

	private TestTMProvider testTMProvider;
	private TMEquipmentManager equipmentManager;
	private TMEquipmentManager testEquipmentManager;
	
	public void setUp() throws Exception{
		super.setUp();
		
		equipmentManager =  (TMEquipmentManager) super.provider.getEquipmentManager0();
		testTMProvider = new TestTMProvider("18654", "apollo", "", new int[]{Brand.BRAND_ID_TELUS});
		testEquipmentManager = (TMEquipmentManager) testTMProvider.getEquipmentManager0();	
	}

	public void testGetEquipment0() throws SecurityException, NoSuchMethodException, UnknownSubscriberException, TelusAPIException   {	
		TMEquipment tmequipment = (TMEquipment) equipmentManager.getEquipment("14115899420");
		assertEquals(1916609,tmequipment.getBanID());
				
	}
	
	public void testGetEquipment1() throws SecurityException, NoSuchMethodException, UnknownSubscriberException, TelusAPIException   {	
		TMEquipment tmequipment = (TMEquipment) equipmentManager.getEquipment("14115899420",false);
		assertEquals(1916609,tmequipment.getBanID());
				
	}
	
	public void testGetEquipmentByCapCode() throws SecurityException, NoSuchMethodException, UnknownSubscriberException, TelusAPIException   {	
		TMEquipment tmequipment = (TMEquipment) equipmentManager.getEquipmentByCapCode("E1614803", "F");
		assertEquals("B600H06743",tmequipment.getSerialNumber());
				
	}

	public void testGetEquipmentByPhoneNumber() throws SecurityException, NoSuchMethodException, UnknownSubscriberException, TelusAPIException   {	
		TMEquipment tmequipment = (TMEquipment) equipmentManager.getEquipmentByPhoneNumber("2047980182");
		assertEquals(816166,tmequipment.getBanID());
	}
	
	public void testGetMasterLockbySerialNo0() throws SecurityException, NoSuchMethodException, UnknownSubscriberException, TelusAPIException   {	
		String serialNo =  equipmentManager.getMasterLockbySerialNo("21101883164", 999, "OGHIAPH");
		assertEquals("963212",serialNo);
				
	}
	
	public void testGetMasterLockbySerialNo1() throws SecurityException, NoSuchMethodException, UnknownSubscriberException, TelusAPIException   {
		try{
		String serialNo =  equipmentManager.getMasterLockbySerialNo("24113041693", 7,10000214,10000211, "NRT_APP");
		assertEquals("016522",serialNo);
		}catch(Throwable t){
			assertTrue(t instanceof TelusAPIException);
			assertTrue(((TelusAPIException)t).getStackTrace0().contains("id=SYS00008"));
		}
	}
	
	public void testAddAnalogSerialNumber() throws SecurityException, NoSuchMethodException, UnknownSubscriberException, TelusAPIException   {	
		try{
		equipmentManager.addAnalogSerialNumber("25300917930");
		
		}catch(Throwable t){
			System.out.println("Error"+t.getMessage());
			assertTrue(t instanceof TelusAPIException);
			assertTrue(((TelusAPIException)t).getStackTrace0().contains("id=SYS00008"));
		}
		
	}
	
	public void testSetSIMMule() throws SecurityException, NoSuchMethodException, UnknownSubscriberException, TelusAPIException   {	
		try{
		equipmentManager.setSIMMule("000800052620300", "", null, "ACT");
		}catch(Throwable t){
			System.out.println("Error"+t.getMessage());
		}
				
	}
	
	public void testAddPagerSerialNumber() throws SecurityException, NoSuchMethodException, UnknownSubscriberException, TelusAPIException   {	
		try{
		equipmentManager.addPagerSerialNumber("519J65334", "E1765567", "F", "1", "N", "SilverRo");
		}catch(Throwable t){
			System.out.println("Error"+t.getMessage());
			assertEquals("No message found for this code [0].", ((TelusAPIException)t).getApplicationMessage().getText("en"));
		}
		
	}
	
	public void testGetCardBySerialNumber() throws SecurityException, NoSuchMethodException, UnknownSubscriberException, TelusAPIException   {	
		Card card = equipmentManager.getCardBySerialNumber("2041702082");
		assertEquals(103,card.getStatus());
				
	}
	public void testGetCardByCardNumber() throws SecurityException, NoSuchMethodException, UnknownSubscriberException, TelusAPIException   {
		
		try{
		Subscriber subscriber = provider.getAccountManager().findSubscriberByPhoneNumber("2041702082");
		Card card = equipmentManager.getCardByCardNumber("20800006196", subscriber);
		assertEquals(1,card.getStatus());
		}catch(Throwable t){
			System.out.println(((TelusAPIException)t).getStackTrace0());
			assertTrue(t instanceof TelusAPIException);
			assertTrue((((TelusAPIException)t).getStackTrace0().contains("Card Pin challenge failure") || ((TelusAPIException)t).getStackTrace0().contains("Prepaid System not available") || ((TelusAPIException)t).getStackTrace0().contains("Too many failed PIN Attempts") ));
		}
	}
	public void testGetAirCardByCardNumber0() throws SecurityException, NoSuchMethodException, UnknownSubscriberException, TelusAPIException   {
		try{
		Subscriber subscriber = provider.getAccountManager().findSubscriberByPhoneNumber("4169948298");
		Card card = equipmentManager.getAirCardByCardNumber("030001371234", subscriber);
		assertEquals(1,card.getStatus());
		}catch(Throwable t){
			System.out.println("Error"+t.getMessage());
			assertTrue(t instanceof TelusAPIException);
			assertTrue((((TelusAPIException)t).getStackTrace0().contains("id=SYS00008") || ((TelusAPIException)t).getStackTrace0().contains("Prepaid System not available")));
		}
		
	}
	
	public void testGetAirCardByCardNumber1() throws SecurityException, NoSuchMethodException, UnknownSubscriberException, TelusAPIException   {
		
		try{
		Card card = equipmentManager.getAirCardByCardNumber("030001371234", "4162060215","21101120760");
		assertEquals(1,card.getStatus());
		}catch(Throwable t){
			
			assertTrue(t instanceof TelusAPIException);
			assertTrue((((TelusAPIException)t).getStackTrace0().contains("id=SYS00008") || ((TelusAPIException)t).getStackTrace0().contains("Prepaid System not available")));
		}
		
	}
	public void testGetCards0() throws SecurityException, NoSuchMethodException, UnknownSubscriberException, TelusAPIException   {
		Card[] cards = equipmentManager.getCards("9058019871");
		for(int i= 0;i<cards.length;i++)
			System.out.println("Cards"+cards[i]+""+i);
		assertEquals(103,cards[0].getStatus());
		
	}
	public void testGetCards1() throws SecurityException, NoSuchMethodException, UnknownSubscriberException, TelusAPIException   {
		Card[] cards = equipmentManager.getCards("9058019871","FEA");
		assertEquals(103,cards[0].getStatus());
		
	}
	
	public void testGetBaseProductPrice() throws SecurityException, NoSuchMethodException, UnknownSubscriberException, TelusAPIException   {
		double price = equipmentManager.getBaseProductPrice("14115899420", "ON", "416");
		assertEquals(249.99,price,0);
	}

	public void testGetShippedToLocation() throws SecurityException, NoSuchMethodException, UnknownSubscriberException, TelusAPIException   {
		long location = equipmentManager.getShippedToLocation("14115899420");
		assertEquals(0,location);
		
	}
	
	public void testGetEquipmentbyProductCode() throws SecurityException, NoSuchMethodException, UnknownSubscriberException, TelusAPIException   {
		Equipment equipment = equipmentManager.getEquipmentbyProductCode("TM520");
		assertEquals("HAND",equipment.getProductClassCode());
		
	}

	public void testGetBaseProductPriceByProductCode() throws SecurityException, NoSuchMethodException, UnknownSubscriberException, TelusAPIException   {
		double price = equipmentManager.getBaseProductPriceByProductCode("TM520", "ON", "416");
		assertEquals(249.99,price,0);
	}
		
}
