package wint.mvc.restful.method.impl;

import wint.mvc.flow.FlowData;
import wint.mvc.restful.method.TraceFlowData;

/**
 * Created by songlihuang on 2018/3/7.
 */
public class TraceFlowDataSupport extends ResultfulFlowDataSupport implements TraceFlowData {
    public TraceFlowDataSupport(FlowData flowData) {
        super(flowData);
    }
}
