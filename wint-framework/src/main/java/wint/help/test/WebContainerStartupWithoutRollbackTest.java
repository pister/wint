package wint.help.test;

import junit.framework.TestCase;
import org.springframework.context.ApplicationContext;
import wint.core.config.Constants;
import wint.core.service.ServiceContext;
import wint.core.service.bean.BeanFactoryService;
import wint.lang.magic.MagicMap;
import wint.lang.magic.MagicObject;
import wint.lang.magic.Property;
import wint.mvc.DispatcherServlet;
import wint.mvc.servlet.mock.ServletConfigMock;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

/**
 * User: huangsongli
 * Date: 15/8/18
 * Time: 上午10:08
 */
public abstract class WebContainerStartupWithoutRollbackTest extends TestCase {

    private DispatcherServlet dispatcherServlet;
    private ServletConfig servletConfig;

    private ServiceContext serviceContext;

    private ApplicationContext applicationContext;

    @Override
    protected final void setUp() throws Exception {
        super.setUp();

        dispatcherServlet = new DispatcherServlet();
        MagicMap initParameters = getInitParameters();

        servletConfig = new ServletConfigMock(initParameters, initParameters);
        dispatcherServlet.init(servletConfig);

        this.serviceContext = dispatcherServlet.getServiceContext();

        BeanFactoryService beanFactoryService = this.serviceContext.getService(BeanFactoryService.class);
        ApplicationContext applicationContext = (ApplicationContext)beanFactoryService.getApplicationContext();

        injectProperties(applicationContext);

        this.applicationContext = applicationContext;

        onSetUp();
    }

    private void injectProperties(ApplicationContext applicationContext) {
        MagicObject thisObject = MagicObject.wrap(this);
        Map<String, Property> propertyMap =  thisObject.getMagicClass().getWritableProperties();
        for (Map.Entry<String, Property> propertyEntry : propertyMap.entrySet()) {
            String name = propertyEntry.getKey();
            if (applicationContext.containsBean(name)) {
                Object bean = applicationContext.getBean(name);
                propertyEntry.getValue().setValue(this, bean);
            }
        }
    }

    protected MagicMap getInitParameters() {
        MagicMap magicMap = MagicMap.newMagicMap();
        magicMap.put(Constants.PropertyKeys.SPRING_CONTEXT_FILE, getStringContextLocation());
        magicMap.put(Constants.PropertyKeys.APP_PACKAGE, getAppPackage());
        return magicMap;
    }

    protected String getAppPackage() {
        return "com.yourcompackage.yourproject";
    }

    protected String getStringContextLocation() {
        return "testApplicationContext.xml";
    }

    protected final void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        dispatcherServlet.service(request, response);
    }

    protected ServiceContext getServiceContext() {
        return serviceContext;
    }

    protected ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    @Override
    protected final void tearDown() throws Exception {
        onTearDown();
        dispatcherServlet.destroy();
        super.tearDown();
    }

    protected void onSetUp() {

    }

    protected void onTearDown() {

    }
}
