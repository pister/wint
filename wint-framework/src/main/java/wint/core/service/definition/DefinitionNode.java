package wint.core.service.definition;

import wint.core.service.ServiceContext;


public abstract class DefinitionNode {

	protected String name;
	
	public DefinitionNode() {
		super();
	}

	public String getName() {
		return name;
	}
	
	public abstract Object createObject(ServiceContext serviceContext, ObjectCreateObsever objectCreateObsever);
	
}
