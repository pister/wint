package wint.mvc.template.filters;

import wint.lang.utils.UrlUtil;

/**
 * User: huangsongli
 * Date: 16/11/2
 * Time: 下午3:22
 */
public class UrlEncodeRender extends AbstractFilterRender {

    private static final String ENCODE = "utf-8";

    public UrlEncodeRender(Object content) {
        super(content);
    }

    @Override
    public String render() {
        return UrlUtil.encode(content, ENCODE);
    }
}
