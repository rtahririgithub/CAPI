package com.telus.provider.equipment;


import com.telus.api.BaseTest;
import com.telus.api.TelusAPIException;
import com.telus.api.equipment.Equipment;
import com.telus.api.reference.Brand;
import com.telus.eas.equipment.info.EquipmentInfo;
import com.telus.provider.NewEjbTestMethods;
import com.telus.provider.TestTMProvider;
public class TMUSIMCardEquipmentIntTest extends BaseTest {
	
	static {
		setupEASECA_QA();
		//setupD3();
	}
	
	public TMUSIMCardEquipmentIntTest(String name) throws Throwable {
		super(name);
	}

	private TestTMProvider testTMProvider;
	private TMEquipmentManager equipmentManager;
	private TMUSIMCardEquipment tmUSIMCardEquip;
	
	public void setUp() throws Exception{
		super.setUp();
		
		equipmentManager =  (TMEquipmentManager) super.provider.getEquipmentManager();
		testTMProvider = new TestTMProvider("18654", "apollo", "", new int[]{Brand.BRAND_ID_TELUS});
		tmUSIMCardEquip = new TMUSIMCardEquipment(testTMProvider,new EquipmentInfo());
	}

	public void testGetLastAssociatedHandset() throws TelusAPIException, SecurityException, NoSuchMethodException {
		
		TMUSIMCardEquipment usimEquip = (TMUSIMCardEquipment) equipmentManager.getEquipment("8912230000002011815");
		System.out.println("usimEquip"+usimEquip.toString());
		Equipment equip = usimEquip.getLastAssociatedHandset();
		assertEquals("H",equip.getNetworkType());
		assertEquals("Z",equip.getEquipmentType());
		
		}

		
}
