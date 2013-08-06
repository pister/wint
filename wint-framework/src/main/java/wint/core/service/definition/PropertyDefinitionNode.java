package wint.core.service.definition;

import java.util.List;
import java.util.Map;

import wint.core.service.ServiceContext;
import wint.lang.utils.CollectionUtil;
import wint.lang.utils.MapUtil;

public class PropertyDefinitionNode extends DefinitionNode {
	
	protected List<DefinitionNode> properties = CollectionUtil.newArrayList();

	public PropertyDefinitionNode() {}
	
	public PropertyDefinitionNode(String name) {
		super();
		this.name = name;
	}

	public void addProperty(DefinitionNode definitionNode) {
		properties.add(definitionNode);
	}

	public List<DefinitionNode> getProperties() {
		return properties;
	}

	public void addProperties(List<DefinitionNode> properties) {
		properties.addAll(properties);
	}

	@Override
	public Object createObject(ServiceContext serviceContext, ObjectCreateObsever objectCreateObsever) {
		Map<String, Object> ret = MapUtil.newHashMap();
		for (DefinitionNode node : properties) {
			String name = node.getName();
			Object object = node.createObject(serviceContext, objectCreateObsever);
			ret.put(name, object);
		}
		return ret;
	}
	

}
