package wint.mvc.restful.method.impl;

import wint.mvc.flow.FlowData;
import wint.mvc.restful.method.PatchFlowData;
import wint.mvc.restful.method.ResultfulFlowData;

/**
 * Created by songlihuang on 2018/3/7.
 */
public class PatchFlowDataSupport extends ResultfulFlowDataSupport implements PatchFlowData, ResultfulFlowData {
    public PatchFlowDataSupport(FlowData flowData) {
        super(flowData);
    }
}
