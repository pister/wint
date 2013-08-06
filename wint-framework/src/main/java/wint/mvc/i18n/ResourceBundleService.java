package wint.mvc.i18n;

import java.util.Locale;
import java.util.ResourceBundle;

import wint.core.service.Service;

public interface ResourceBundleService extends Service {
	
	ResourceBundle getResourceBundle(String baseName, Locale locale);
	
	ResourceBundle getResourceBundle(String baseName);

}
