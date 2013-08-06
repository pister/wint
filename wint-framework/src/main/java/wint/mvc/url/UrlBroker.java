package wint.mvc.url;

import wint.core.config.Constants;
import wint.help.mvc.security.csrf.CsrfTokenUtil;
import wint.lang.exceptions.UrlException;
import wint.lang.magic.MagicMap;
import wint.lang.utils.AutoFillArray;
import wint.lang.utils.NumberUtil;
import wint.lang.utils.StringUtil;
import wint.mvc.view.Render;

/**
 * @author pister 2012-3-2 07:50:40
 */
public class UrlBroker implements Render {

	private UrlBrokerService urlBrokerService;
	
	private String path;
	
	private String target;
	
	private MagicMap queryData = MagicMap.newMagicMap();
	
	private AutoFillArray<Object> arguments = new AutoFillArray<Object>();
	
	private String anchor;
	
	private String tokenName;
	
	public UrlBroker(UrlBrokerService urlBrokerService, String path, String target, String tokenName) {
		super();
		this.urlBrokerService = urlBrokerService;
		this.path = UrlBrokerUtil.normalizePath(path);
		target = StringUtil.getLastBefore(target, ".");
		this.target = StringUtil.camelToFixedString(target, "-");;
		this.tokenName = tokenName;
	}
	
	public UrlBroker param(String name, Object value) {
		return parameter(name, value);
	}

	public UrlBroker withToken() {
		return parameter(tokenName, CsrfTokenUtil.token());
	}
	
	public UrlBroker parameter(String name, Object value) {
		name = StringUtil.camelToFixedString(name, "-");
		queryData.put(name, value);
		return this;
	}

	public UrlBroker arg(int index, Object value) {
		return argument(index, value);
	}

	public UrlBroker argument(int index, Object value) {
		if (index > Constants.Url.MAX_ARGUMENT_COUNT) {
			throw new UrlException("url argument index is too large: " + index);
		}
		if (index == 0) {
			if (!NumberUtil.isPositiveInteger(value)) {
				throw new UrlException("the first argument for url["+ target +"] must be a Positive Integer.");
			}
		}
		arguments.set(index, ArgumentEncoder.transformStringArgument(value));
		return this;
	}
	
	public UrlBroker arguments(Object ...values) {
		if (values == null) {
			return this;
		}
		int index = 0;
		for (Object object : values) {
			argument(index, object);
			++index;
		}
		return this;
	}
	
	public UrlBroker args(Object ...values) {
		return arguments(values);
	}

	public String render() {
		return urlBrokerService.render(this);
	}

	public MagicMap getQueryData() {
		return queryData;
	}

	public AutoFillArray<Object> getArguments() {
		return arguments;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getTarget() {
		return target;
	}

	public void setTarget(String target) {
		this.target = target;
	}

	public String getAnchor() {
		return anchor;
	}

	public UrlBroker setAnchor(String anchor) {
		this.anchor = anchor;
		return this;
	}

}
