package wint.tools.serialize;

import junit.framework.TestCase;

public class WintSerializeServiceTest extends TestCase {
	
	private SerializeService wintSerializeService;
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		wintSerializeService = new WintSerializeService();
	}

	public void testXX() {
		byte[] data = wintSerializeService.serialize(66);
		Object o = wintSerializeService.deserialize(data);
		System.out.println(data.length);
		System.out.println(o);
	}

}
