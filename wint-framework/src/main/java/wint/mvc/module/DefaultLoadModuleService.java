package wint.mvc.module;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import wint.core.config.Configuration;
import wint.core.config.Constants;
import wint.core.service.AbstractService;
import wint.core.service.bean.BeanFactory;
import wint.core.service.bean.BeanFactoryService;
import wint.core.service.env.Environment;
import wint.core.service.initial.ConfigurationAwire;
import wint.lang.magic.MagicClass;
import wint.lang.magic.MagicList;
import wint.lang.magic.MagicMethod;
import wint.lang.magic.MagicObject;
import wint.lang.magic.MagicPackage;
import wint.lang.utils.ClassUtil;
import wint.lang.utils.CollectionUtil;
import wint.lang.utils.MapUtil;
import wint.lang.utils.TargetUtil;
import wint.mvc.flow.FlowData;
import wint.mvc.module.annotations.Action;

/**
 * @author pister 2012-1-2 03:16:57
 */
public class DefaultLoadModuleService extends AbstractService implements LoadModuleService, ConfigurationAwire {

	private Configuration configuration;
	private String moduleMethod;
	private MagicPackage basePackage;
	private Environment environment;
	private BeanFactoryService beanFactoryService;
	private ConcurrentMap<String, ExecutionModule> cachedModules = MapUtil.newConcurrentHashMap();

	@Override
	public void init() {
		super.init();
		String appPackage = configuration.getProperties().getString(Constants.PropertyKeys.APP_PACKAGE, Constants.Defaults.APP_PACKAGE);
		basePackage = new MagicPackage(appPackage);
		moduleMethod = configuration.getProperties().getString(Constants.PropertyKeys.APP_MODULE_METHOD, Constants.Defaults.MODULE_METHOD);
		environment = configuration.getEnvironment();
		beanFactoryService = this.serviceContext.getService(BeanFactoryService.class);
	}

	protected String makeModuleCacheKey(String target, String moduleType) {
		return target + "." + moduleType;
	}

	public ExecutionModule loadModule(final String target, final String moduleType) {
		if (environment == Environment.DEV) {
			ExecutionModule module = loadModuleImpl(target, moduleType);
			if (module != null) {
				return module;
			} else {
				return new NopModule(target, moduleType);
			}
		} else {
			String key = makeModuleCacheKey(target, moduleType);;
			ExecutionModule module = cachedModules.get(key);
			if (module != null) {
				return module;
			}

            module = loadModuleImpl(target, moduleType);
            if (module == null) {
                return new NopModule(target, moduleType);
            } else {
                cachedModules.put(key, module);
                return module;
            }
        }
	}

	private ExecutionModule loadModuleImpl(String target, String moduleType) {
		List<ModuleInfo> moduleInfos = getModuleInfos(normalizeTarget(target), moduleType);
		for (ModuleInfo moduleInfo : moduleInfos) {
			MagicObject magicObject = createTargetObject(moduleInfo.getTargetClass());
			return new DefaultModule(magicObject, moduleInfo, moduleType);
		}
		return null;
	}

	protected String normalizeTarget(String target) {
		return TargetUtil.normalizeTarget(target);
	}

	protected MagicObject createTargetObject(MagicClass magicClass) {
		MagicObject magicObject = magicClass.newInstance();
		BeanFactory beanFactory = beanFactoryService.getBeanFactory();
		beanFactory.injectProperties(magicObject.getObject());
		return magicObject;
	}

	protected String getMethodName(String name) {
		return name;
	}

 	protected List<ModuleInfo> getModuleInfos(String target, String moduleType) {
		List<ModuleInfo> moduleInfos = CollectionUtil.newArrayList(2);

		// 1
		String classTarget = target.replace('/', '.');
		MagicPackage moduleTypePackage = new MagicPackage(basePackage, moduleType);
		MagicPackage targetPackage = new MagicPackage(moduleTypePackage, classTarget);
		String className = targetPackage.toClassName();
		if (ClassUtil.classExist(className)) {
			MagicClass magicClass = MagicClass.forName(className);
			MagicMethod magicMethod = magicClass.getMethod(moduleMethod);
			if (magicMethod != null && isModuleMethod(magicMethod)) {
				moduleInfos.add(new ModuleInfo(magicClass, magicMethod));
			}
		}

		// 2
		MagicPackage parentPackage = targetPackage.getParent();
		if (parentPackage != null) {
			String classClass = parentPackage.toClassName();
			String methodName = targetPackage.getFileName();
			String targetMethodName = getMethodName(methodName);
			if (ClassUtil.classExist(classClass)) {
				MagicClass magicClass = MagicClass.forName(classClass);
				MagicMethod magicMethod = magicClass.getMethod(targetMethodName);
				if (magicMethod != null && isModuleMethod(magicMethod)) {
					moduleInfos.add(new ModuleInfo(magicClass, magicMethod));
				}
			}
		}

		return moduleInfos;
	}

 	protected boolean isModuleMethod(MagicMethod magicMethod) {
 		Method targetMethod = magicMethod.getTargetMethod();
 		Action actionAnnotation = targetMethod.getAnnotation(Action.class);
 		if (actionAnnotation != null) {
 			return true;
 		}
 		if (!Modifier.isPublic(targetMethod.getModifiers())) {
 			return false;
 		}
 		MagicList<MagicClass> parameterTypes = magicMethod.getParameterTypes();
 		if (CollectionUtil.isEmpty(parameterTypes)) {
 			return false;
 		}
 		for (MagicClass parameterType : parameterTypes) {
 			if (parameterType.isAssignableFrom(FlowData.class)) {
 				return true;
 			}
 		}
 		return false;
 	}

	public void setConfiguration(Configuration configuration) {
		this.configuration = configuration;
	}


}
