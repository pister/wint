package wint.mvc.i18n;

import java.util.Locale;
import java.util.ResourceBundle;

import wint.core.config.Configuration;
import wint.core.config.Constants;
import wint.core.service.AbstractService;
import wint.lang.magic.MagicPackage;
import wint.lang.utils.ClassUtil;
import wint.mvc.holder.WintContext;

public class DefaultResourceBundleService extends AbstractService implements ResourceBundleService {

	private MagicPackage i18nBasePackage;
	
	@Override
	public void init() {
		super.init();
		Configuration configuration = serviceContext.getConfiguration();
		String i18nName = configuration.getProperties().getString(Constants.PropertyKeys.APP_I18N, Constants.Defaults.APP_I18N);
		i18nBasePackage = new MagicPackage(i18nName);
	}

	public ResourceBundle getResourceBundle(String baseName) {
		return getResourceBundle(baseName, WintContext.getLocale());
	}

	public ResourceBundle getResourceBundle(String baseName, Locale locale) {
		if (locale == null) {
			locale = Locale.getDefault();
		}
		MagicPackage targetName = new MagicPackage(i18nBasePackage, baseName);
		ResourceBundle resourceBundle = ResourceBundle.getBundle(targetName.getName(), locale, ClassUtil.getClassLoader());
		return resourceBundle;
	}
	
}
