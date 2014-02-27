package wint.mvc.pipeline.valves;

import wint.core.config.Constants;
import wint.core.service.env.Environment;
import wint.help.mvc.security.csrf.CsrfTokenUtil;
import wint.lang.WintException;
import wint.lang.utils.StringUtil;
import wint.mvc.flow.InnerFlowData;
import wint.mvc.flow.StatusCodes;
import wint.mvc.module.Module;
import wint.mvc.pipeline.PipelineContext;
import wint.mvc.view.types.ViewTypes;

import java.io.Writer;

/**
 * User: longyi
 * Date: 14-2-13
 * Time: 上午10:57
 */
public class CheckCsrfTokenValve extends AbstractValve {

    private String tokenName;

    private Environment environment;

    private String redirectModule;

    private String redirectTarget;

    @Override
    public void init() {
        super.init();
        environment = serviceContext.getConfiguration().getEnvironment();
        tokenName = serviceContext.getConfiguration().getProperties().getString(Constants.PropertyKeys.CSRF_TOKEN_NAME, Constants.Defaults.CSRF_TOKEN_NAME);
    }

    public void invoke(PipelineContext pipelineContext, InnerFlowData innerFlowData) {
        Module module = innerFlowData.getModule();
        if (!module.isDoAction()) {
            pipelineContext.invokeNext(innerFlowData);
            return;
        }
        // 针对do-action做检查
        if (!CsrfTokenUtil.checkCsrfByParameter(innerFlowData, tokenName)) {
            if (Environment.DEV == environment) {
                handleMessage(innerFlowData);
            } else {
                handleCsrf(innerFlowData);
            }
        } else {
            pipelineContext.invokeNext(innerFlowData);
        }
    }

    private void handleCsrf(InnerFlowData innerFlowData) {
        if (!StringUtil.isEmpty(redirectModule) && !StringUtil.isEmpty(redirectTarget)) {
            innerFlowData.redirectTo(redirectModule, redirectTarget);
        } else {
            innerFlowData.setStatusCode(StatusCodes.SC_FORBIDDEN);
        }

    }

    private void handleMessage(InnerFlowData innerFlowData) {
        try {
            innerFlowData.setViewType(ViewTypes.NOP_VIEW_TYPE);
            Writer out = innerFlowData.getWriter();
            out.write("miss " + tokenName + " value for doAction.");
            out.close();
        } catch (Exception e) {
            throw new WintException(e);
        }
    }

    public void setRedirectModule(String redirectModule) {
        this.redirectModule = redirectModule;
    }

    public void setRedirectTarget(String redirectTarget) {
        this.redirectTarget = redirectTarget;
    }
}
