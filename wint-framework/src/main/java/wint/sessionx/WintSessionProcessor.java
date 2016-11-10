package wint.sessionx;

import wint.core.config.Constants;
import wint.core.service.ServiceContext;
import wint.lang.magic.MagicMap;
import wint.lang.misc.profiler.Profiler;
import wint.sessionx.provider.SessionProvider;
import wint.sessionx.provider.SessionProviderFactory;
import wint.sessionx.service.DefaultSessionStoreService;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class WintSessionProcessor {

    private ServiceContext serviceContext;

    public WintSessionProcessor(ServiceContext serviceContext) {
        this.serviceContext = serviceContext;
    }

    private SessionContainer sessionContainer;

    public void init(MagicMap initParameters, ServletContext servletContext) {
        sessionContainer = new SessionContainer();
        String sessionType = initParameters.getString(Constants.PropertyKeys.WINT_SESSION_TYPE, Constants.Defaults.WINT_SESSION_TYPE);
        servletContext.log("Wint sessionType: " + sessionType);
        SessionProvider sessionProvider = SessionProviderFactory.getSessionProvider(sessionType);
        sessionProvider.init(initParameters, servletContext);
        sessionContainer.init(sessionProvider, initParameters, servletContext);

        DefaultSessionStoreService defaultSessionStoreService = new DefaultSessionStoreService(sessionProvider.getSessionStoreCreator());
        serviceContext.registerService(defaultSessionStoreService);

        servletContext.log("Wint SessionX has been initialized.");
    }

    public void destroy() {

    }

    public void process(HttpServletRequest httpRequest, HttpServletResponse httpResponse, ProcessorHandler processorHandler) throws ServletException, IOException {
       try {
           Profiler.enter("WintSessionProcessor process");
           sessionContainer.handleRequest(httpRequest, httpResponse, processorHandler);
       } finally {
           Profiler.release();
       }
    }

    public static interface ProcessorHandler {
        void onProcess(HttpServletRequest httpRequest, HttpServletResponse httpResponse) throws ServletException, IOException;
    }

}
