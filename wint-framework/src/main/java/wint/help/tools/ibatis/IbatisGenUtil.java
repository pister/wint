package wint.help.tools.ibatis;

import java.io.OutputStreamWriter;

public class IbatisGenUtil {
	
/*	public static void genToConsole(String prefix, Class<?> clazz) {
		SourceGenerator ibatisGenerator = new SourceGenerator();
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
		SourceGenerator sourceGenerator = new SourceGenerator();
		sourceGenerator.setIdName(idName);
		sourceGenerator.setTablePrefix(prefix);
        System.out.println();
        System.out.println();
        System.out.println();
		sourceGenerator.genSqlMap(clazz,  new OutputStreamWriter(System.out));
	}
	
	public static void genCreateTableSqlToConsole(String prefix, Class<?> clazz, boolean notNull, String idName) {
		SourceGenerator sourceGenerator = new SourceGenerator();
		sourceGenerator.setIdName(idName);
		sourceGenerator.setTablePrefix(prefix);
        System.out.println();
        System.out.println();
        System.out.println();
		sourceGenerator.genCreateTableSql(clazz,  new OutputStreamWriter(System.out), notNull);
	}
	
	public static void genIbatisDaoToConsole(String prefix, Class<?> clazz, String idName) {
		SourceGenerator sourceGenerator = new SourceGenerator();
		sourceGenerator.setIdName(idName);
		sourceGenerator.setTablePrefix(prefix);
        System.out.println();
        System.out.println();
        System.out.println();
		sourceGenerator.genIbatisDao(clazz,  new OutputStreamWriter(System.out), null);
	}

    public static void genDaoToConsole(String prefix, Class<?> clazz, String idName) {
        SourceGenerator sourceGenerator = new SourceGenerator();
        sourceGenerator.setIdName(idName);
        sourceGenerator.setTablePrefix(prefix);
        System.out.println();
        System.out.println();
        System.out.println();
        sourceGenerator.genDAO(clazz,  new OutputStreamWriter(System.out));
    }

    public static void genTestsToConsole(String prefix, Class<?> clazz, String idName) {
        SourceGenerator sourceGenerator = new SourceGenerator();
        sourceGenerator.setIdName(idName);
        sourceGenerator.setTablePrefix(prefix);
        System.out.println();
        System.out.println();
        System.out.println();
        sourceGenerator.genDaoTests(clazz,  new OutputStreamWriter(System.out));
    }

}
