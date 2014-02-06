package wint.mvc.form.validator;

import wint.lang.utils.StringUtil;

/**
 * User: longyi
 * Date: 14-2-6
 * Time: 下午2:28
 */
public class BooleanValidator extends AbstractValidator {
    @Override
    protected boolean validate(String fieldValue) {
        if (StringUtil.isEmpty(fieldValue)) {
            return true;
        }
        return fieldValue.equalsIgnoreCase(Boolean.TRUE.toString());
    }
}
