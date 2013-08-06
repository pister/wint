package wint.core.service.parser;


import wint.core.io.resource.Resource;
import wint.core.service.definition.ServiceDefinition;

public interface ServiceDefinitionParser {
	
	ServiceDefinition parse(Resource resource);

}
