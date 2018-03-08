package wint.mvc.restful.method.impl;

import wint.mvc.flow.FlowData;
import wint.mvc.restful.method.DeleteFlowData;

/**
 * Created by songlihuang on 2018/3/7.
 */
public class DeleteFlowDataSupport extends ResultfulFlowDataSupport implements DeleteFlowData {
    public DeleteFlowDataSupport(FlowData flowData) {
        super(flowData);
    }
}
