package wint.mvc.restful.method.impl;

import wint.mvc.flow.FlowData;
import wint.mvc.restful.method.PutMethod;

/**
 * Created by songlihuang on 2018/3/7.
 */
public class PutMethodSupport extends BodySupportMethodFlowDataSupport implements PutMethod {
    public PutMethodSupport(FlowData flowData) {
        super(flowData);
    }
}
