package wint.mvc.restful.method.impl;

import wint.mvc.flow.FlowData;
import wint.mvc.restful.method.PatchFlowData;

/**
 * Created by songlihuang on 2018/3/7.
 */
public class PatchFlowDataSupport extends BodySupportFlowDataSupport implements PatchFlowData {
    public PatchFlowDataSupport(FlowData flowData) {
        super(flowData);
    }
}
