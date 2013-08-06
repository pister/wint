package wint.core.service.parser;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class XMLParseUtil {
	
	public static String getAttribute(Node node, String name) {
		NamedNodeMap namedNodeMap = node.getAttributes();
		if (namedNodeMap == null) {
			return null;
		}
		Node attrNode = namedNodeMap.getNamedItem(name);
		if (attrNode == null) {
			return null;
		}
		return attrNode.getNodeValue();
	}
	
	public static boolean isElement(Node node) {
		short nodeType = node.getNodeType();
		if (Node.ELEMENT_NODE == nodeType || Node.DOCUMENT_NODE == nodeType) {
			return true;
		}
		return false;
	}
	
	public static Node getRootNode(Document document) {
		NodeList nodeList = document.getChildNodes();
		return nodeList.item(0);
	}
	
	public static String getText(Node node) {
		if (node == null) {
			return null;
		}
		return node.getTextContent();
	}

}
