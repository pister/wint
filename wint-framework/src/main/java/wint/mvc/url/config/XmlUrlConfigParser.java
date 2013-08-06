package wint.mvc.url.config;

import java.io.InputStream;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import wint.core.service.parser.XMLParseUtil;
import wint.lang.exceptions.FormConfigException;
import wint.lang.exceptions.ResourceException;
import wint.lang.utils.MapUtil;
import wint.lang.utils.StringUtil;

public class XmlUrlConfigParser implements UrlConfigParser {

	private XPathExpression urlConfigExpr;
	private XPathExpression serverUrlExpr;
	private XPathExpression pathExpr;
	
	public XmlUrlConfigParser() {
		try {
			XPathFactory factory = XPathFactory.newInstance();
			XPath xpath = factory.newXPath();
			urlConfigExpr = xpath.compile("/url-config/url");
			serverUrlExpr = xpath.compile("server-url");
			pathExpr = xpath.compile("path");
		} catch (XPathExpressionException e) {
			throw new FormConfigException(e);
		}
	}
	
	public Map<String, AbstractUrlConfig> parse(InputStream is) {
		try {
			return parseImpl(is);
		} catch (Exception e) {
			throw new ResourceException(e);
		}
	}
	
	private Map<String, AbstractUrlConfig> parseImpl(InputStream is) throws Exception {
		DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        documentBuilderFactory.setNamespaceAware(true);
        DocumentBuilder builder = documentBuilderFactory.newDocumentBuilder();
        Document document = builder.parse(is);
        NodeList urlConfigNodes = (NodeList)urlConfigExpr.evaluate(document, XPathConstants.NODESET);

        Map<String, AbstractUrlConfig> urlConfigs = MapUtil.newHashMap();
        // for all
        for (int i = 0, len = urlConfigNodes.getLength(); i < len; ++i) {
        	Node node = urlConfigNodes.item(i);
        	String name = XMLParseUtil.getAttribute(node, "name");
        	if (StringUtil.isEmpty(name)) {
        		throw new ResourceException("url config's name can not be empty!");
        	}
        	AbstractUrlConfig urlConfig = null;
        	String extendsName = XMLParseUtil.getAttribute(node, "extends");
        	if (StringUtil.isEmpty(extendsName)) {
        		// base
        		Node serverUrlNode = (Node)serverUrlExpr.evaluate(node, XPathConstants.NODE);
        		if (serverUrlNode == null) {
        			throw new ResourceException("url config["+ name +"]'s server-url can not be empty!");
        		}
    			String serverUrl = XMLParseUtil.getText(serverUrlNode);
    			if (serverUrl == null) {
        			throw new ResourceException("url config["+ name +"]'s server-url can not be empty!");
        		}
    			urlConfig = new BaseUrlConfig(name, serverUrl);
        	} else {
        		// normal
        		Node pathNode = (Node)pathExpr.evaluate(node, XPathConstants.NODE);
        		String path = null;
        		if (pathNode != null) {
        			path = XMLParseUtil.getText(pathNode);
        		}
        		NormalUrlConfig normalUrlConfig = new NormalUrlConfig(name, path);
        		normalUrlConfig.setExtendsName(extendsName);
        		urlConfig = normalUrlConfig;
        	}
        	urlConfigs.put(urlConfig.getName(), urlConfig);
        }
        
        // for extends
        for (Map.Entry<String, AbstractUrlConfig> entry : urlConfigs.entrySet()) {
        	AbstractUrlConfig urlConfig = entry.getValue();
        	if (urlConfig instanceof NormalUrlConfig) {
        		NormalUrlConfig normalUrlConfig = (NormalUrlConfig)urlConfig;
        		String extendsName = normalUrlConfig.getExtendsName();
        		AbstractUrlConfig parentUrlConfig =  urlConfigs.get(extendsName);
        		if (parentUrlConfig == null) {
        			throw new ResourceException("url config["+ normalUrlConfig.getName() +"]'s extends config["+extendsName +"] can not be found!");
        		}
        		normalUrlConfig.setExtendsUrlConfig(parentUrlConfig);
        	}
        }
        
        // build path
        for (Map.Entry<String, AbstractUrlConfig> entry : urlConfigs.entrySet()) {
        	AbstractUrlConfig urlConfig = entry.getValue();
        	urlConfig.buildPath();
        }
        
        return urlConfigs;
	}
	
}
