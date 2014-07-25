package wint.core.service.bean;

import java.util.List;

import wint.core.config.Constants;
import wint.core.service.AbstractService;
import wint.lang.magic.MagicList;
import wint.lang.magic.MagicMap;
import wint.lang.magic.MagicPackage;
import wint.lang.utils.CollectionUtil;

/**
 * 
 * @author pister 2012-3-4 04:03:58
 * 
 * @deprecated
 */
public class WintBeanFactoryService extends AbstractService implements BeanFactoryService {

	private MagicPackage basePackage;
	
	private BeanFactory beanFactory;
	
	private List<MagicPackage> autoLoadPackages = MagicList.newList();
	
	@Override
	public void init() {
		super.init();
		MagicMap properties =  this.serviceContext.getConfiguration().getProperties();
		basePackage = new MagicPackage(properties.getString(Constants.PropertyKeys.APP_PACKAGE, Constants.Defaults.APP_PACKAGE));
		autoLoadPackages.add(new MagicPackage(basePackage, properties.getString(Constants.PropertyKeys.APP_PACKAGE_BIZ_AO, Constants.Defaults.APP_PACKAGE_BIZ_AO)));
		autoLoadPackages.add(new MagicPackage(basePackage, properties.getString(Constants.PropertyKeys.APP_PACKAGE_BIZ_MANAGER, Constants.Defaults.APP_PACKAGE_BIZ_MANAGER)));
		autoLoadPackages.add(new MagicPackage(basePackage, properties.getString(Constants.PropertyKeys.APP_PACKAGE_BIZ_DAO, Constants.Defaults.APP_PACKAGE_BIZ_DAO)));
		autoLoadPackages.add(new MagicPackage(basePackage, properties.getString(Constants.PropertyKeys.APP_PACKAGE_BIZ_MISC, Constants.Defaults.APP_PACKAGE_BIZ_MISC)));
	
		refresh();
	}
	
	protected void refresh() {
		
	}

	public BeanFactory getBeanFactory() {
		return beanFactory;
	}

	public void setAutoLoadPackages(List<String> autoLoadPackages) {
		if (CollectionUtil.isEmpty(autoLoadPackages)) {
			return;
		}
		for (String autoLoadPackage : autoLoadPackages) {
			this.autoLoadPackages.add(new MagicPackage(autoLoadPackage));
		}
	}

    public Object getApplicationContext() {
        return null;
    }
}
