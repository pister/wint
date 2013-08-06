package wint.core.service.parser;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import wint.core.io.resource.Resource;
import wint.core.service.definition.ClassDefinitionNode;
import wint.core.service.definition.DefinitionNode;
import wint.core.service.definition.ListDefinitionNode;
import wint.core.service.definition.ObjectDefinitionNode;
import wint.core.service.definition.PropertyDefinitionNode;
import wint.core.service.definition.ServiceDefinition;
import wint.lang.exceptions.ParseException;
import wint.lang.utils.CollectionUtil;
import wint.lang.utils.StringUtil;

public class XmlServiceDefinitionParser implements ServiceDefinitionParser {

	public ServiceDefinition parse(Resource resource) {
		try {
			return parseImpl(resource.getInputStream());
		} catch (Exception e) {
			throw new ParseException(e);
		}
	}
	
	private ServiceDefinition parseImpl(InputStream is) throws ParserConfigurationException, SAXException, IOException {
		DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        documentBuilderFactory.setNamespaceAware(true);
        DocumentBuilder builder = documentBuilderFactory.newDocumentBuilder();
        Document document = builder.parse(is);
        Node root = XMLParseUtil.getRootNode(document);
        ServiceDefinition ret = new ServiceDefinition();
        DefinitionNode rootNode = createDefinitionNode(root);
        ret.setRootNode((PropertyDefinitionNode)rootNode);
        return ret;
	}
	
	private DefinitionNode createDefinitionNode(Node node) {
		String nodeName = node.getNodeName();
		String name = XMLParseUtil.getAttribute(node, "name");
		if ("object".equals(nodeName)) {
			PropertyDefinitionNode ret = null;
			String className = XMLParseUtil.getAttribute(node, "class");
			if (StringUtil.isEmpty(className)) {
				ret = new PropertyDefinitionNode(name);
			} else {
				ret = new ClassDefinitionNode(name, className);
			}
			
			NodeList childNodes = node.getChildNodes();
			for (int i = 0, len = childNodes.getLength(); i < len; ++i) {
				Node childNode = childNodes.item(i);
				if (!XMLParseUtil.isElement(childNode)) {
					continue;
				}
				DefinitionNode childDefinitionNode = createDefinitionNode(childNode);
				ret.addProperty(childDefinitionNode);
			}
			return ret;
		} else if ("value".equals(nodeName)) {
			String value = XMLParseUtil.getText(node);
			return new ObjectDefinitionNode(name, value);
		} else if ("list".equals(nodeName)) {
			List<DefinitionNode> definitionNodeElements = CollectionUtil.newArrayList();
			NodeList childNodes = node.getChildNodes();
			for (int i = 0, len = childNodes.getLength(); i < len; ++i) {
				Node childNode = childNodes.item(i);
				if (!XMLParseUtil.isElement(childNode)) {
					continue;
				}
				DefinitionNode childDefinitionNode = createDefinitionNode(childNode);
				definitionNodeElements.add(childDefinitionNode);
			}
			
			return new ListDefinitionNode(name, definitionNodeElements);
		} else {
			throw new ParseException("unkown node tag: " + nodeName);
		}
	}
	
	static enum DefinitionType {
		PROPERTY,
		CLASS,
		OBJECT,
		LIST
	}
	
}
