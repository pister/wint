package wint.mvc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import wint.core.config.Configuration;
import wint.core.config.Constants;
import wint.core.config.Constants.PropertyKeys;
import wint.core.service.ServiceContext;
import wint.core.service.env.Environment;
import wint.core.service.supports.ServiceContextSupport;
import wint.lang.WintException;
import wint.lang.magic.MagicMap;
import wint.lang.magic.config.MagicConfig;
import wint.lang.magic.config.MagicType;
import wint.lang.misc.profiler.Profiler;
import wint.lang.utils.StringUtil;
import wint.mvc.flow.FlowDataService;
import wint.mvc.flow.InnerFlowData;
import wint.mvc.flow.StatusCodes;
import wint.mvc.holder.WinContextNames;
import wint.mvc.holder.WintContext;
import wint.mvc.init.DispatcherInitializor;
import wint.mvc.pipeline.Pipeline;
import wint.mvc.pipeline.PipelineService;
import wint.mvc.servlet.ServletUtil;
import wint.sessionx.WintSessionProcessor;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

/**
 * wint框架初始化及请求派发类
 *
 * @author pister
 *         2012-1-11 02:29:22
 */
public class Dispatcher {

    private static final Logger log = LoggerFactory.getLogger(Dispatcher.class);
    private ServiceContext serviceContext;
    private PipelineService pipelineService;
    private FlowDataService flowDataService;
    private WintSessionProcessor wintSessionProcessor;
    private WintSessionProcessor.ProcessorHandler processorHandler;
    private String charset;
    private String requestContextPath;
    private boolean wintSessionUse;

    /**
     * 初始化
     *
     * @param dispatcherInitializor
     */
    public void init(DispatcherInitializor dispatcherInitializor) throws ServletException {
        try {
            Configuration configuration = dispatcherInitializor.loadConfiguration();
            String objectMagicType = configuration.getProperties().getString(Constants.PropertyKeys.OBJECT_MAGIC_TYPE, Constants.Defaults.OBJECT_MAGIC_TYPE);

            charset = configuration.getProperties().getString(PropertyKeys.CHARSET_ENCODING, Constants.Defaults.CHARSET_ENCODING);
            requestContextPath = configuration.getProperties().getString(PropertyKeys.WINT_REQUEST_CONTEXT_PATH, Constants.Defaults.WINT_REQUEST_CONTEXT_PATH);

            MagicType magicType = MagicType.fromName(objectMagicType);
            MagicConfig.getMagicConfig().setMagicType(magicType);
            String magicName = MagicConfig.getMagicConfig().getMagicFactory().getName();

            ServiceContextSupport serviceContextSupport = new ServiceContextSupport();
            serviceContextSupport.setResourceLoader(dispatcherInitializor.getResourceLoader());
            serviceContextSupport.init(configuration);

            pipelineService = (PipelineService) serviceContextSupport.getService(PipelineService.class);
            flowDataService = (FlowDataService) serviceContextSupport.getService(FlowDataService.class);

            MagicMap properties = serviceContextSupport.getConfiguration().getProperties();
            Environment env = serviceContextSupport.getConfiguration().getEnvironment();
            this.serviceContext = serviceContextSupport;
            this.wintSessionUse = properties.getBoolean(Constants.PropertyKeys.WINT_SESSION_USE, Constants.Defaults.WINT_SESSION_USE);

            // wintSessionProcessor = new WintSessionProcessor(serviceContext);

            dispatcherInitializor.getLogger().log("==================================================");
            dispatcherInitializor.getLogger().log("Wint framework initializing info:");
            dispatcherInitializor.getLogger().log("Magic Type: " + magicName);
            dispatcherInitializor.getLogger().log("Environment: " + env.getName());
            for (Map.Entry<String, Object> entry : properties.entrySet()) {
                dispatcherInitializor.getLogger().log(entry.getKey() + " = " + entry.getValue());
            }
            if (wintSessionUse) {
                dispatcherInitializor.getLogger().log("Wint Session Use: " + wintSessionUse);

                processorHandler = new WintSessionProcessor.ProcessorHandler() {
                    public void onProcess(HttpServletRequest httpRequest, HttpServletResponse httpResponse) throws ServletException, IOException {
                        executeImpl(httpRequest, httpResponse);
                    }
                };

                wintSessionProcessor = new WintSessionProcessor(serviceContext);
                wintSessionProcessor.init(properties, dispatcherInitializor.getServletContext());
            }
            dispatcherInitializor.getLogger().log("Wint framework has been initialized.");
            dispatcherInitializor.getLogger().log("==================================================");
        } catch (Exception e) {
            log.error("init wint-framework error", e);
            throw new ServletException(e);
        }
    }

    public void destroy() {
        if (wintSessionProcessor != null) {
            wintSessionProcessor.destroy();
        }
    }

    protected void executeImpl(HttpServletRequest request, HttpServletResponse response) {
        try {
            request.setCharacterEncoding(charset);

            String path = ServletUtil.getServletPathWithRequestContext(request, requestContextPath);

            Profiler.enter("process action: " + path);
            InnerFlowData flowData = flowDataService.createFlowData(request, response);
            Pipeline pipeline = pipelineService.getPipeline(getPipelineName(request, flowData));

            WintContext.setVariable(WinContextNames.FLOW_DATA, flowData);
            WintContext.setVariable(WinContextNames.LOCALE, flowData.getLocale());
            WintContext.setVariable(WinContextNames.REQUEST, request);
            WintContext.setVariable(WinContextNames.RESPONSE, response);

            pipeline.execute(flowData);

            flowData.commitData();
        } catch (Throwable e) {
            if (serviceContext.getConfiguration().getEnvironment().isSupportDev()) {
                if (e instanceof RuntimeException) {
                    throw (RuntimeException) e;
                } else {
                    throw new WintException(e);
                }
            } else {
                try {
                    response.sendError(StatusCodes.SC_INTERNAL_SERVER_ERROR);
                } catch (IOException e1) {
                    log.error("error", e1);
                }
            }
        } finally {
            WintContext.clear();
            Profiler.release();
        }
    }

    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (wintSessionUse) {
            wintSessionProcessor.process(request, response, processorHandler);
        } else {
            executeImpl(request, response);
        }
    }

    protected String getPipelineName(HttpServletRequest request, InnerFlowData flowData) {
        String suffix = flowData.getSuffix();
        if (!StringUtil.isEmpty(suffix)) {
            return suffix.toLowerCase();
        }
        return Constants.Defaults.PIPELINE_NAME;
    }

    public ServiceContext getServiceContext() {
        return serviceContext;
    }


}
