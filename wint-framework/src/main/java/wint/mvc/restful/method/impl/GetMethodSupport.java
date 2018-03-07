package wint.mvc.restful.method.impl;

import wint.mvc.flow.FlowData;
import wint.mvc.restful.method.GetMethod;

/**
 * Created by songlihuang on 2018/3/7.
 */
public class GetMethodSupport extends ResultfulMethodFlowDataSupport implements GetMethod {
    public GetMethodSupport(FlowData flowData) {
        super(flowData);
    }
}
