package wint.mvc.restful.method.impl;

import wint.mvc.flow.FlowData;
import wint.mvc.restful.method.PostMethod;

/**
 * Created by songlihuang on 2018/3/7.
 */
public class PostMethodSupport extends BodySupportMethodFlowDataSupport implements PostMethod {
    public PostMethodSupport(FlowData flowData) {
        super(flowData);
    }
}
