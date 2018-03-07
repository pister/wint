package wint.mvc.restful.method.impl;

import wint.mvc.flow.FlowData;
import wint.mvc.restful.method.HeadMethod;

/**
 * Created by songlihuang on 2018/3/7.
 */
public class HeadMethodSupport extends ResultfulMethodFlowDataSupport implements HeadMethod {
    public HeadMethodSupport(FlowData flowData) {
        super(flowData);
    }
}
