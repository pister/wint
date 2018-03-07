package wint.mvc.restful.method.impl;

import wint.mvc.flow.FlowData;
import wint.mvc.restful.method.TraceMethod;

/**
 * Created by songlihuang on 2018/3/7.
 */
public class TraceMethodSupport extends ResultfulMethodFlowDataSupport implements TraceMethod {
    public TraceMethodSupport(FlowData flowData) {
        super(flowData);
    }
}
