package wint.core.service.definition;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import wint.core.service.Service;
import wint.core.service.util.ServiceUtil;
import wint.lang.utils.CollectionUtil;
import wint.lang.utils.StringUtil;


public class ServiceDefinition {
	
	private static final Logger log = LoggerFactory.getLogger(ServiceDefinition.class);
	
	private PropertyDefinitionNode rootNode;
	
	public PropertyDefinitionNode getRootNode() {
		return rootNode;
	}

	public void setRootNode(PropertyDefinitionNode rootNode) {
		this.rootNode = rootNode;
	}
	
	/**
	 * @param serviceDefinition
	 */
	public void overWriteServicesFrom(ServiceDefinition serviceDefinition) {
		if (serviceDefinition == null || this == serviceDefinition) {
			return;
		}
		PropertyDefinitionNode propertyDefinitionNode = serviceDefinition.getRootNode();
		List<DefinitionNode> definitionNodes = propertyDefinitionNode.getProperties();
		if (CollectionUtil.isEmpty(definitionNodes)) {
			return;
		}
		if (log.isDebugEnabled()) {
			log.debug("user define wint file start to overwrite...");
		}
		for (DefinitionNode definitionNode : definitionNodes) {
			if (!(definitionNode instanceof ClassDefinitionNode)) {
				continue;
			}
			ClassDefinitionNode classDefinitionNode = (ClassDefinitionNode)definitionNode;
			Class<?> targetClass = classDefinitionNode.getTargetClass();
			if (!Service.class.isAssignableFrom(targetClass)) {
				continue;
			}
			if (log.isDebugEnabled()) {
				log.debug("user define service class: " + targetClass);
			}
			overWriteService(classDefinitionNode, rootNode.getProperties());
		}
		if (log.isDebugEnabled()) {
			log.debug("user define wint file overwrite finish.");
		}
		
	}
	
	private void overWriteService(ClassDefinitionNode newClassDefinitionNode, List<DefinitionNode> properties) {
		String targetServiceName = ServiceUtil.getSerivceName(newClassDefinitionNode.getTargetClass());
		if (StringUtil.isEmpty(targetServiceName)) {
			return;
		}
		for (DefinitionNode definitionNode : properties) {
			if (!(definitionNode instanceof ClassDefinitionNode)) {
				continue;
			}
			ClassDefinitionNode classDefinitionNode = (ClassDefinitionNode)definitionNode;
			if (targetServiceName.equals(ServiceUtil.getSerivceName(classDefinitionNode.getTargetClass()))) {
				properties.remove(definitionNode);
				properties.add(newClassDefinitionNode);
				if (log.isInfoEnabled()) {
					log.info("service " + targetServiceName + " has been overwrite by " + newClassDefinitionNode.getTargetClass());
				}
				return;
			}
		}
	}
	
}
