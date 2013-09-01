package wint.help.mvc.valves;

import java.util.List;
import java.util.Set;

import wint.help.mvc.DefaultSessionKeys;
import wint.help.mvc.WebConstants;
import wint.lang.utils.CollectionUtil;
import wint.lang.utils.StringUtil;
import wint.lang.utils.TargetUtil;
import wint.lang.utils.UrlUtil;
import wint.mvc.flow.FlowData;
import wint.mvc.flow.InnerFlowData;
import wint.mvc.holder.WintContext;
import wint.mvc.module.Module;
import wint.mvc.pipeline.PipelineContext;
import wint.mvc.pipeline.valves.AbstractValve;

public class AuthLoginValve extends AbstractValve {

	private String userIdKey = DefaultSessionKeys.USER_ID_KEY;
	
	private String loginLocation;
	
	private String loginModule = "baseModule";
	
	private String doLoginTarget = TargetUtil.normalizeTarget("user/doLogin");
	
	private String loginTarget = TargetUtil.normalizeTarget("login");
	
	private Set<String> protectedUrls = CollectionUtil.newHashSet();
	
	private Set<String> unprotectedUrls = CollectionUtil.newHashSet();
	
	public void invoke(PipelineContext pipelineContext, InnerFlowData flowData) {
		if (canVisit(pipelineContext, flowData)) {
			pipelineContext.invokeNext(flowData);
		} else {
			// 重定向到登陆页面
			redirectToLoginPage(flowData);
			pipelineContext.breakPipeline();
		}
	}
	
	protected void redirectToLoginPage(InnerFlowData flowData) {
		if (StringUtil.isEmpty(loginLocation)) {
			flowData.redirectTo(loginModule, loginTarget).param(WebConstants.REDIRECT_PARAM_NAME, getRequestURL());
		} else {
			String targetLocation;
			if (loginLocation.indexOf('?') > 0) {
				targetLocation = loginLocation + "&" + WebConstants.REDIRECT_PARAM_NAME + "=" + getRequestURL();
			} else {
				targetLocation = loginLocation + "?" + WebConstants.REDIRECT_PARAM_NAME + "=" + getRequestURL();
			}
			flowData.redirectTo(targetLocation);
		}
	}
	
	private String getRequestURL() {
		return UrlUtil.getRequestURL(WintContext.getRequest());
	}
	
	protected boolean canVisit(PipelineContext pipelineContext, InnerFlowData flowData) {
		Module module = flowData.getModule();
		if (module.getModuleInfo() == null) {
			// 如果仅仅是template，则不判断登录
			return true;
		}
		String target = flowData.getTarget();
		target = TargetUtil.normalizeTarget(target);
		if (StringUtil.equals(target, loginTarget) || StringUtil.equals(target, doLoginTarget)) {
			return true;
		}
		if (protectedUrls.contains(target)) {
			return isUserLogin(flowData);
		} else if (unprotectedUrls.contains(target)) {
			return true;
		} else {
			return isUserLogin(flowData);
		}
	}
	
	protected boolean isUserLogin(FlowData flowData) {
		Object userIdValue = flowData.getSession().getAttribute(userIdKey);
		if (userIdValue == null) {
			return false;
		}
		String userIdStr = userIdValue.toString();
		if (StringUtil.isEmpty(userIdStr)) {
			return false;
		}
		if ("0".equals(userIdStr)) {
			return false;
		}
		return true;
	}

	public void setProtectedUrls(List<String> inputUrls) {
		for (String inputUrl : inputUrls) {
			String target = TargetUtil.normalizeTarget(inputUrl);
			protectedUrls.add(target);
		}
	}

	public void setUnprotectedUrls(List<String> inputUrls) {
		for (String inputUrl : inputUrls) {
			String target = TargetUtil.normalizeTarget(inputUrl);
			unprotectedUrls.add(target);
		}
	}

	public String getUserIdKey() {
		return userIdKey;
	}

	public void setUserIdKey(String userIdKey) {
		this.userIdKey = userIdKey;
	}

	public String getLoginLocation() {
		return loginLocation;
	}

	public void setLoginLocation(String loginLocation) {
		this.loginLocation = loginLocation;
	}

	public String getLoginModule() {
		return loginModule;
	}

	public void setLoginModule(String loginModule) {
		this.loginModule = loginModule;
	}

	public String getLoginTarget() {
		return loginTarget;
	}

	public void setLoginTarget(String loginTarget) {
		this.loginTarget = TargetUtil.normalizeTarget(loginTarget);
	}

	public void setDoLoginTarget(String doLoginTarget) {
		this.doLoginTarget =  TargetUtil.normalizeTarget(doLoginTarget);
	}
	
}
