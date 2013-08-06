package wint.core.service.definition;

import wint.core.service.Service;
import wint.core.service.ServiceContext;
import wint.core.service.aop.ProxyInterceptors;
import wint.core.service.util.ServiceUtil;
import wint.lang.exceptions.ConfigException;
import wint.lang.magic.MagicClass;
import wint.lang.magic.MagicObject;
import wint.lang.magic.Property;
import wint.lang.utils.ClassUtil;
import wint.lang.utils.StringUtil;

public class ClassDefinitionNode extends PropertyDefinitionNode {
	
	private Class<?> clazz;
	
	public ClassDefinitionNode(String name, Class<?> clazz) {
		super(name);
		this.clazz = clazz;
		if (StringUtil.isEmpty(name) && (Service.class.isAssignableFrom(clazz))) {
			this.name = ServiceUtil.getSerivceName(clazz);
		}
	}
	
	public ClassDefinitionNode(String name, String className) {
		this(name, ClassUtil.forName(className));
	}
	
	public ClassDefinitionNode(Class<?> clazz) {
		this(null, clazz);
	}
	
	public ClassDefinitionNode(String className) {
		this(null, className);
	}
	
	public Class<?> getTargetClass() {
		return clazz;
	}
	
	@Override
	public Object createObject(ServiceContext serviceContext, ObjectCreateObsever objectCreateObsever) {
		MagicClass magicClass = MagicClass.wrap(clazz);
		MagicObject magicObject = magicClass.newInstance();
		
		objectCreateObsever.onCreate(magicObject.getObject());
		for (DefinitionNode definitionNode : properties) {
			String propertyName = definitionNode.getName();
			Object propertyValue = definitionNode.createObject(serviceContext, objectCreateObsever);
			if (propertyValue != null) {
				Property property = magicObject.getMagicClass().getProperty(propertyName);
				if (property == null) {
					throw new ConfigException("can not find writable property for \"" + propertyName + "\" at " + magicObject);
				}
				magicObject.setPropertyValueExt(propertyName, propertyValue);
			}
		}
		if (magicObject.getObject() instanceof Service) {
			magicObject = magicObject.asProxyObject(ProxyInterceptors.getInterceptors());
		}
		return magicObject.getObject();
	}

}
