package wint.core.service.bean.spring;

import wint.core.config.Constants;
import wint.core.io.resource.Resource;
import wint.core.io.resource.loader.ResourceLoader;
import wint.core.service.AbstractService;
import wint.core.service.ServiceContext;
import wint.core.service.bean.BeanFactory;
import wint.core.service.bean.BeanFactoryService;
import wint.core.service.bean.WintBeanFactory;
import wint.lang.magic.MagicClass;
import wint.lang.magic.MagicObject;
import wint.lang.utils.LibUtil;
import wint.lang.utils.StringUtil;

/**
 * @author pister 2012-3-4 01:14:24
 */
public class SpringSupportBeanFactoryService extends AbstractService implements BeanFactoryService {

	private BeanFactory beanFactory;
	
	private String springContextFile;
	
	private ResourceLoader resourceLoader;

	@Override
	public void init() {
		super.init();
		if (StringUtil.isEmpty(springContextFile)) {
			springContextFile = serviceContext.getConfiguration().getProperties().getString(Constants.PropertyKeys.SPRING_CONTEXT_FILE, Constants.Defaults.SPRING_CONTEXT_FILE);
		}
		resourceLoader = serviceContext.getResourceLoader();
		if (needUseSpring()) {
			if (log.isInfoEnabled()) {
				log.info("Spring library used, initialzing it...");
			}
			MagicClass wintResourceXmlApplicationContextClass = MagicClass.forName("wint.core.service.bean.spring.WintResourceXmlApplicationContext");
			MagicObject applicationContext = wintResourceXmlApplicationContextClass.newInstance(new Class<?>[] { String.class, ServiceContext.class }, new Object[] { springContextFile, serviceContext });
			beanFactory = new SpringBeanFactory(applicationContext);
			if (log.isInfoEnabled()) {
				log.info("Spring library has been initialzed.");
			}
		} else {
			beanFactory = new WintBeanFactory();
		}
	}

	protected boolean needUseSpring() {
		boolean springExist = LibUtil.isSpringExist();
		if (!springExist) {
			if (log.isDebugEnabled()) {
				log.debug("org.springframework.context.ApplicationContext class does not exist.");
			}
			return false;
		}
		Resource springResourceFile =  resourceLoader.getResource(springContextFile);
		if (springResourceFile == null || !springResourceFile.exist()) {
			if (log.isDebugEnabled()) {
				log.debug("org.springframework.context.ApplicationContext class exist, but " + springContextFile + " does not exist.");
			}
			return false;
		}
		return true;
	}

	public BeanFactory getBeanFactory() {
		return beanFactory;
	}

	public void setSpringContextFile(String springContextFile) {
		this.springContextFile = springContextFile;
	}

	public String getSpringContextFile() {
		return springContextFile;
	}

}
