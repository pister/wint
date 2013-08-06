package wint.core.service.definition;

import wint.core.service.ServiceContext;

public class ObjectDefinitionNode extends DefinitionNode {
	
	private final Object value;

	public ObjectDefinitionNode(String name, Object value) {
		super();
		this.name = name;
		this.value = value;
	}

	public Object getValue() {
		return value;
	}

	@Override
	public Object createObject(ServiceContext serviceContext, ObjectCreateObsever objectCreateObsever) {
		return value;
	}


}
