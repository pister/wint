package wint.sessionx;

import wint.lang.magic.MagicMap;
import wint.sessionx.provider.SessionProvider;
import wint.sessionx.provider.cookie.CookieSessionProvider;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class WintSessionProcessor {

    private SessionContainer sessionContainer;

    public void init(MagicMap initParamters, ServletContext servletContext) {
        sessionContainer = new SessionContainer();
        SessionProvider sessionProvider = new CookieSessionProvider();
        sessionProvider.init(initParamters, servletContext);
        sessionContainer.init(sessionProvider, initParamters, servletContext);
        servletContext.log("Wint SessionX has been initialized.");
    }

    public void destroy() {

    }

    public void process(HttpServletRequest httpRequest, HttpServletResponse httpResponse, ProcessorHandler processorHandler) throws ServletException, IOException {
        sessionContainer.handleRequest(httpRequest, httpResponse, processorHandler);
    }

    public static interface ProcessorHandler {
        void onProcess(HttpServletRequest httpRequest, HttpServletResponse httpResponse) throws ServletException, IOException;
    }

}
