package wint.core.serivce.config.parser;

import junit.framework.Assert;
import junit.framework.TestCase;
import wint.core.config.Configuration;
import wint.core.io.resource.Resource;
import wint.core.io.resource.loader.DefaultResourceLoader;
import wint.core.io.resource.loader.ResourceLoader;
import wint.core.serivce.supports.demo.FirstService;
import wint.core.service.definition.ServiceDefinition;
import wint.core.service.parser.XmlServiceDefinitionParser;
import wint.core.service.supports.ServiceContextSupport;

public class XmlServiceDefinitionParserTest extends TestCase {

	public void testParser() {
		ResourceLoader resourceLoader = new DefaultResourceLoader();
		Resource resource = resourceLoader.getResource("test.xml");
		XmlServiceDefinitionParser xmlServiceDefinitionParser = new XmlServiceDefinitionParser();
		ServiceDefinition serviceDefinition = xmlServiceDefinitionParser.parse(resource);
		Assert.assertNotNull(serviceDefinition);
		System.out.println(serviceDefinition);
	}

	public void testLoadConfig() {
		ResourceLoader resourceLoader = new DefaultResourceLoader();
		Resource resource = resourceLoader.getResource("test.xml");
		XmlServiceDefinitionParser xmlServiceDefinitionParser = new XmlServiceDefinitionParser();
		ServiceDefinition serviceDefinition = xmlServiceDefinitionParser.parse(resource);
		Configuration configuration = new Configuration();
		
		configuration.setServiceDefinition(serviceDefinition);
		ServiceContextSupport serviceContextSupport = new ServiceContextSupport();
		serviceContextSupport.init(configuration);
		FirstService service = (FirstService)serviceContextSupport.getService(FirstService.class);
		Assert.assertNotNull(service);
		System.out.println(service);
	}
	
}
