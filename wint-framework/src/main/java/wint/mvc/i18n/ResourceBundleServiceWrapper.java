package wint.mvc.i18n;

import wint.mvc.flow.FlowData;

import java.util.ResourceBundle;

/**
 * User: longyi
 * Date: 13-8-28
 * Time: 上午10:03
 */
public class ResourceBundleServiceWrapper {

    private ResourceBundleService resourceBundleService;

    private FlowData flowData;

    public ResourceBundleServiceWrapper(FlowData flowData, ResourceBundleService resourceBundleService) {
        this.flowData = flowData;
        this.resourceBundleService = resourceBundleService;
    }

    public ResourceBundleWrapper get(String name) {
        ResourceBundle resourceBundle = resourceBundleService.getResourceBundle(name, flowData.getLocale());
        if (resourceBundle == null) {
            return null;
        }
        return new ResourceBundleWrapper(resourceBundle);
    }

}
