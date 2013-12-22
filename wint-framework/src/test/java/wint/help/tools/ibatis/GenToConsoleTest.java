package wint.help.tools.ibatis;

import junit.framework.TestCase;
import wint.demo.app.biz.domain.UserDO;
import wint.help.tools.gen.dao.IbatisGenUtil;

public class GenToConsoleTest extends TestCase {
	
	public void testGenSqlmap() {
		IbatisGenUtil.genSqlMapToConsole("aaa_", UserDO.class, "userId");
	}
	
	public void testGenCreateTable() {
		IbatisGenUtil.genCreateTableSqlToConsole("bbb_", UserDO.class, "userId");
	}

	public void testGenIbatisDaoToConsole() {
		IbatisGenUtil.genIbatisDaoToConsole("bbb_", UserDO.class, "userId");
	}

    public void testGenDaoToConsole() {
		IbatisGenUtil.genDaoToConsole("bbb_", UserDO.class, "userId");
	}

    public void testGenTestToConsole() {
		IbatisGenUtil.genTestsToConsole("bbb_", UserDO.class, "userId");
	}

}
