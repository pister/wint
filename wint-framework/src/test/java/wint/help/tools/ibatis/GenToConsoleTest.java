package wint.help.tools.ibatis;

import junit.framework.TestCase;
import wint.demo.app.biz.domain.UserDO;

public class GenToConsoleTest extends TestCase {
	
	public void testGen() {
		IbatisGenUtil.genSqlMapToConsole("aaa_", UserDO.class, "userId");
	}
	
	public void testGenCreateTable() {
		IbatisGenUtil.genCreateTableSqlToConsole("bbb_", UserDO.class, "userId");
	}

	public void testGenIbatisDaoToConsole() {
		IbatisGenUtil.genIbatisDaoToConsole("bbb_", UserDO.class, "userId");
	}

}
