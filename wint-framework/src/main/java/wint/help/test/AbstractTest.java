package wint.help.test;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.test.AbstractTransactionalDataSourceSpringContextTests;

import wint.help.sql.TypeUtil;
import wint.help.sql.TypeUtil.MethodPair;
import wint.lang.WintException;
import wint.lang.utils.MapUtil;

public abstract class AbstractTest extends AbstractTransactionalDataSourceSpringContextTests {

		protected String[] getConfigLocations() {
			return new String[] {"applicationContext.xml"};
		}
		
		protected int executeUpdate(String sql, Object... args) {
			return this.getJdbcTemplate().update(sql, args);
		}
		
		protected Map<String, Object> queryFields(String sql, Collection<NameAndClass> nameAndClasses, Object ...args) {
			SimpleRowCallbackHandler simpleRowCallbackHandler = new SimpleRowCallbackHandler(nameAndClasses);
			this.getJdbcTemplate().query(sql, args, simpleRowCallbackHandler);
			return simpleRowCallbackHandler.getResultObjects();
		}
		
		@SuppressWarnings("unchecked")
		protected <T> T queryField(String sql, String name, Class<T> clazz, Object ...args) {
			NameAndClass nac = new NameAndClass(name, clazz);
			List<NameAndClass> nameAndClassList = new ArrayList<NameAndClass>(1);
			nameAndClassList.add(nac);
			Map<String, Object> ret = queryFields(sql, nameAndClassList, args);
			return (T)ret.get(name);
		}

		protected static class NameAndClass {
			private String fieldName;
			private Class<?> resultType;
			public NameAndClass(String fieldName, Class<?> resultType) {
				super();
				this.fieldName = fieldName;
				this.resultType = resultType;
			}
			public String getFieldName() {
				return fieldName;
			}
			public void setFieldName(String fieldName) {
				this.fieldName = fieldName;
			}
			public Class<?> getResultType() {
				return resultType;
			}
			public void setResultType(Class<?> resultType) {
				this.resultType = resultType;
			}
		}
		
		static class SimpleRowCallbackHandler implements RowCallbackHandler {

			private Collection<NameAndClass> nameAndClasses;
			
			private Map<String, Object> resultObjects = MapUtil.newHashMap();
			
			private boolean firstRow = true;
			
			private SimpleRowCallbackHandler(Collection<NameAndClass> nameAndClasses) {
				super();
				this.nameAndClasses = nameAndClasses;
			}

			public void processRow(ResultSet rs) throws SQLException {
				if (firstRow) {		
					firstRow = false;
				} else {
					return;
				}
				for (NameAndClass nameAndClass : nameAndClasses) {
					String name = nameAndClass.getFieldName();
					Class<?> clazz = nameAndClass.getResultType();
					// MethodPair methodPair = TypeUtil.getMethodPair(clazz);
					try {
						Object value = rs.getObject(name, clazz);
					//	Object value = methodPair.getGetter().invoke(rs, name);
						resultObjects.put(name, value);
					} catch (Exception e) {
						throw new WintException(e);
					}
				}
			}

			public Map<String, Object> getResultObjects() {
				return resultObjects;
			}

		}
		

}
