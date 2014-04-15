package wint.mvc.form.validator;

import wint.mvc.flow.InnerFlowData;
import wint.mvc.form.config.FormConfig;

/**
 * User: huangsongli
 * Date: 14-4-15
 * Time: 上午9:48
 */
public class ValuesValidator implements Validator {

    private Integer min;

    private Integer max ;

    public boolean validate(InnerFlowData flowData, FormConfig formConfig, String fieldName, String fieldValue) {
        String[] values = flowData.getParameters().getStringArray(fieldName);
        int len = values == null ? 0 : values.length;
        if (min != null && len < min) {
            return false;
        }
        if (max != null && len > max) {
            return false;
        }

        return true;
    }

    public void init() {

    }

    public void setMin(Integer min) {
        this.min = min;
    }

    public void setMax(Integer max) {
        this.max = max;
    }
}
