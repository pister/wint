package wint.help.tools.ibatis;

public class IbatisGenUtil {
	
/*	public static void genToConsole(String prefix, Class<?> clazz) {
		IbatisGenerator ibatisGenerator = new IbatisGenerator();
		ibatisGenerator.setTablePrefix(prefix);
		ibatisGenerator.genToConsole(clazz);
	}*/
	
	public static void genSqlMapToConsole(String prefix, Class<?> clazz) {
		genSqlMapToConsole(prefix, clazz, "id");
	}
	
	public static void genCreateTableSqlToConsole(String prefix, Class<?> clazz) {
		genCreateTableSqlToConsole(prefix, clazz, true);
	}
	public static void genCreateTableSqlToConsole(String prefix, Class<?> clazz, boolean notNull) {
		genCreateTableSqlToConsole(prefix, clazz, notNull, "id");
	}
	public static void genCreateTableSqlToConsole(String prefix, Class<?> clazz, String idName) {
		genCreateTableSqlToConsole(prefix, clazz, true, idName);
	}
	
	public static void genIbatisDaoToConsole(String prefix, Class<?> clazz) {
		genIbatisDaoToConsole(prefix, clazz, "id");
	}
	
	public static void genSqlMapToConsole(String prefix, Class<?> clazz, String idName) {
		IbatisGenerator ibatisGenerator = new IbatisGenerator();
		ibatisGenerator.setIdName(idName);
		ibatisGenerator.setTablePrefix(prefix);
		ibatisGenerator.genSqlMap(clazz);
	}
	
	public static void genCreateTableSqlToConsole(String prefix, Class<?> clazz, boolean notNull, String idName) {
		IbatisGenerator ibatisGenerator = new IbatisGenerator();
		ibatisGenerator.setIdName(idName);
		ibatisGenerator.setTablePrefix(prefix);
		ibatisGenerator.genCreateTableSql(clazz, notNull);
	}
	
	public static void genIbatisDaoToConsole(String prefix, Class<?> clazz, String idName) {
		IbatisGenerator ibatisGenerator = new IbatisGenerator();
		ibatisGenerator.setIdName(idName);
		ibatisGenerator.setTablePrefix(prefix);
		ibatisGenerator.genIbatisDao(clazz);
	}

}
