package wint.lang.magic;

import junit.framework.TestCase;

public class MagicListTest extends TestCase {
	
	
	@SuppressWarnings("unchecked")
	public void testWrap() {
		System.out.println(MagicList.newList(MagicList.wrap(new String[] {"aa", "bb"}), MagicList.wrap(new String[] {"aa", "bb"})));
	}

}
