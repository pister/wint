package wint.mvc.pipeline.samplevalves;

import junit.framework.TestCase;
import org.junit.Assert;
import wint.core.config.Configuration;
import wint.core.io.resource.Resource;
import wint.core.io.resource.loader.DefaultResourceLoader;
import wint.core.io.resource.loader.ResourceLoader;
import wint.core.service.definition.ServiceDefinition;
import wint.core.service.parser.XmlServiceDefinitionParser;
import wint.core.service.supports.ServiceContextSupport;
import wint.mvc.pipeline.Pipeline;
import wint.mvc.pipeline.PipelineService;

public class PipelineTest extends TestCase {
	
	public void testLoadConfig() {
		ResourceLoader resourceLoader = new DefaultResourceLoader();
		Resource resource = resourceLoader.getResource("pipeline.xml");
		XmlServiceDefinitionParser xmlServiceDefinitionParser = new XmlServiceDefinitionParser();
		ServiceDefinition serviceDefinition = xmlServiceDefinitionParser.parse(resource);
		Configuration configuration = new Configuration();
		
		configuration.setServiceDefinition(serviceDefinition);
		ServiceContextSupport serviceContext = new ServiceContextSupport();
		serviceContext.init(configuration);
		
		PipelineService pipelineService = (PipelineService)serviceContext.getService(PipelineService.class);
		//FlowDataService flowDataService = (FlowDataService)serviceContext.getService(FlowDataService.class);
		Assert.assertNotNull(pipelineService);
		Pipeline pipeline = pipelineService.getPipeline("default");
		pipeline.execute(null);
	}

}
