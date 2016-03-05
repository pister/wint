package wint.core.service.supports;

import java.util.List;
import java.util.Map;

import wint.core.config.Configuration;
import wint.core.io.resource.loader.ResourceLoader;
import wint.core.service.Service;
import wint.core.service.ServiceContext;
import wint.core.service.definition.DefinitionNode;
import wint.core.service.definition.ObjectCreateObsever;
import wint.core.service.definition.PropertyDefinitionNode;
import wint.core.service.definition.ServiceDefinition;
import wint.core.service.env.Environment;
import wint.core.service.initial.ConfigurationAwire;
import wint.core.service.initial.EnvironmentAwire;
import wint.core.service.initial.Initializor;
import wint.core.service.initial.ServiceContextAwire;
import wint.core.service.util.ServiceUtil;
import wint.lang.utils.CollectionUtil;
import wint.lang.utils.MapUtil;
import wint.mvc.holder.WintContext;

public class ServiceContextSupport extends AbstractServiceContext {

	private Map<String, Object> namedObjects = MapUtil.newConcurrentHashMap();
	
	private ResourceLoader resourceLoader;
	
	public ServiceContextSupport() {
		super();
	}

	public synchronized void init(Configuration configuration) {
		this.configuration = configuration;
		ServiceDefinition serviceDefinition = configuration.getServiceDefinition();
		List<Object> createdObjects = loadObjects(serviceDefinition);
		afterObjectsCreate(createdObjects);
		
		WintContext.setServiceContext(this);
	}
	
	private void afterObjectsCreate(List<Object> createdObjects) {
		for (Object obj : createdObjects) {
			beforeInitializeObject(obj);
		}
		for (Object obj : createdObjects) {
			initializeObjects(obj);
		}
		for (Object obj : createdObjects) {
			afterInitializeObjecs(obj);
		}
	}
	
	protected void beforeInitializeObject(Object object) {
		appyWires(object, this);
	}
	
	protected void afterInitializeObjecs(Object object) {
	}
	
	protected void initializeObjects(Object obj) {
		if (obj instanceof Initializor) {
			((Initializor)obj).init();
		}
	}
	
	
	protected List<Object> loadObjects(ServiceDefinition serviceDefinition) {
		PropertyDefinitionNode definitionNode = serviceDefinition.getRootNode();
		Map<String, Object> services = MapUtil.newHashMap();
		RecordWhenCreateObsever recordWhenCreateObsever = new RecordWhenCreateObsever();
		for (DefinitionNode serviceNode : definitionNode.getProperties()) {
			loadObject(serviceNode, services, recordWhenCreateObsever);
		}
		namedObjects = services;
		return recordWhenCreateObsever.getCreatedObjects();
	}
	
	private void loadObject(DefinitionNode definitionNode, Map<String, Object> services, RecordWhenCreateObsever recordWhenCreateObsever) {
		String name = definitionNode.getName();
		Object object = definitionNode.createObject(this, recordWhenCreateObsever);
		services.put(name, object);
	}
	
	public Object getObject(String name) {
		ServiceUtil.ensureServiceName(name);
		return namedObjects.get(name);
	}

    @Override
    public void registerService(Service service) {
        if (service == null) {
            return;
        }
        namedObjects.put(service.getName(), service);
    }
	
	public ResourceLoader getResourceLoader() {
		return resourceLoader;
	}

	public void setResourceLoader(ResourceLoader resourceLoader) {
		this.resourceLoader = resourceLoader;
	}

	protected void appyWires(Object object, ServiceContext serviceContext) {
		if (object instanceof ServiceContextAwire) {
			((ServiceContextAwire)object).setServiceContext(serviceContext);
		}
		Configuration configuration = serviceContext.getConfiguration();
		if (object instanceof ConfigurationAwire) {
			((ConfigurationAwire)object).setConfiguration(configuration);
		}
		if (object instanceof EnvironmentAwire) {
			if (configuration != null) {
				((EnvironmentAwire)object).setEnvironment(configuration.getEnvironment());
			} else {
				((EnvironmentAwire)object).setEnvironment(Environment.DEV);
			}
		}
	}
	
	private static class RecordWhenCreateObsever implements ObjectCreateObsever {

		private List<Object> createdObjects = CollectionUtil.newArrayList();
		
		public void onCreate(Object object) {
			createdObjects.add(object);
		}

		public List<Object> getCreatedObjects() {
			return createdObjects;
		}
		
	}
	
}
