package wint.core.serivce.supports;

import java.util.Date;

import junit.framework.TestCase;
import org.junit.Assert;
import wint.core.config.Configuration;
import wint.core.serivce.supports.demo.FirstService;
import wint.core.serivce.supports.demo.FirstServiceImpl;
import wint.core.serivce.supports.demo.Person;
import wint.core.service.definition.ClassDefinitionNode;
import wint.core.service.definition.ListDefinitionNode;
import wint.core.service.definition.ObjectDefinitionNode;
import wint.core.service.definition.PropertyDefinitionNode;
import wint.core.service.definition.ServiceDefinition;
import wint.core.service.supports.ServiceContextSupport;
import wint.lang.magic.MagicList;

public class ServiceContextSupportTest extends TestCase {
	
	public void testServiceContext() {
		Configuration configuration = new Configuration();
		ServiceDefinition serviceDefinition = new ServiceDefinition();
		PropertyDefinitionNode rootNode = new PropertyDefinitionNode("services22");
		serviceDefinition.setRootNode(rootNode);
		
		ClassDefinitionNode serviceDefinitionNode = new ClassDefinitionNode(FirstServiceImpl.class);
		
		rootNode.addProperty(serviceDefinitionNode);
		
		serviceDefinitionNode.addProperty(new ObjectDefinitionNode("strValue", "this is a string"));
		serviceDefinitionNode.addProperty(new ObjectDefinitionNode("intValue", "456"));
		
		serviceDefinitionNode.addProperty(new ListDefinitionNode("persons", MagicList.newList(new ClassDefinitionNode(Person.class), new ClassDefinitionNode(Person.class))));

		PropertyDefinitionNode propertyDefinitionNode = new PropertyDefinitionNode("prop");
		propertyDefinitionNode.addProperty(new ObjectDefinitionNode("aaaa", "bbb"));
		propertyDefinitionNode.addProperty(new ObjectDefinitionNode("cccc", 1234));
		propertyDefinitionNode.addProperty(new ClassDefinitionNode("date", Date.class));
		
		serviceDefinitionNode.addProperty(propertyDefinitionNode);
		
		configuration.setServiceDefinition(serviceDefinition);
		ServiceContextSupport serviceContextSupport = new ServiceContextSupport();
		serviceContextSupport.init(configuration);
		
		FirstService service = (FirstService)serviceContextSupport.getService(FirstService.class);
		Assert.assertNotNull(service);
	}
	

}
