package wint.mvc.i18n;

import wint.lang.utils.CollectionUtil;

import java.util.Collections;
import java.util.ResourceBundle;

/**
 * User: longyi
 * Date: 13-8-28
 * Time: 上午10:06
 */
public class ResourceBundleWrapper {

    private ResourceBundle resourceBundle;

    public ResourceBundleWrapper(ResourceBundle resourceBundle) {
        this.resourceBundle = resourceBundle;
    }

    public String get(String name) {
        return resourceBundle.getString(name);
    }

    @Override
    public String toString() {
        if (resourceBundle == null) {
            return "null";
        }
        return CollectionUtil.join(Collections.list(resourceBundle.getKeys()), ",");
    }
}
