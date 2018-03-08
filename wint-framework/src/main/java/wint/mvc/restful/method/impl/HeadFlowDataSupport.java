package wint.mvc.restful.method.impl;

import wint.mvc.flow.FlowData;
import wint.mvc.restful.method.HeadFlowData;

/**
 * Created by songlihuang on 2018/3/7.
 */
public class HeadFlowDataSupport extends ResultfulFlowDataSupport implements HeadFlowData {
    public HeadFlowDataSupport(FlowData flowData) {
        super(flowData);
    }
}
