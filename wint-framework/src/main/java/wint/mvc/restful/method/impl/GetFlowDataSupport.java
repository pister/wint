package wint.mvc.restful.method.impl;

import wint.mvc.flow.FlowData;
import wint.mvc.restful.method.GetFlowData;

/**
 * Created by songlihuang on 2018/3/7.
 */
public class GetFlowDataSupport extends ResultfulFlowDataSupport implements GetFlowData {
    public GetFlowDataSupport(FlowData flowData) {
        super(flowData);
    }
}
