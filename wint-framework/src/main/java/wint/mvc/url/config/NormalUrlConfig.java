package wint.mvc.url.config;

import java.util.Set;

import wint.lang.exceptions.ResourceException;
import wint.lang.utils.CollectionUtil;
import wint.mvc.url.UrlBrokerUtil;

public class NormalUrlConfig extends AbstractUrlConfig {

	private String path;
	
	private String contextPath;
	
	private String extendsName;
	
	private AbstractUrlConfig extendsUrlConfig;
	
	private boolean hasBuilt = false;
	
	public NormalUrlConfig(String name, String contextPath) {
		super(name);
		this.contextPath = UrlBrokerUtil.normalizePath(contextPath);;
	}
	
	@Override
	public String getPath() {
		return path;
	}

	public String getExtendsName() {
		return extendsName;
	}

	public void setExtendsName(String extendsName) {
		this.extendsName = extendsName;
	}

	@Override
	public void buildPath() {
		if (hasBuilt) {
			return;
		}
		AbstractUrlConfig targetUrlConfig = extendsUrlConfig;
		Set<AbstractUrlConfig> checked = CollectionUtil.newHashSet();
		checked.add(this);
		while (true) {
			if (targetUrlConfig == null) {
				break;
			}
			targetUrlConfig.buildPath();
			if (targetUrlConfig instanceof BaseUrlConfig) {
				break;
			}
			if (checked.contains(targetUrlConfig)) {
				throw new ResourceException("url can not extends itself: " + targetUrlConfig.getName());
			}
			checked.add(targetUrlConfig);
			NormalUrlConfig normalUrlConfig = (NormalUrlConfig)targetUrlConfig;
			targetUrlConfig = normalUrlConfig.getExtendsUrlConfig();
		}
		
		String basePath = extendsUrlConfig.getPath();
		path = basePath + contextPath;
		hasBuilt = true;
	}

	public AbstractUrlConfig getExtendsUrlConfig() {
		return extendsUrlConfig;
	}

	public void setExtendsUrlConfig(AbstractUrlConfig extendsUrlConfig) {
		this.extendsUrlConfig = extendsUrlConfig;
	}


}
