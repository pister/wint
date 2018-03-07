package wint.mvc.restful.method.impl;

import wint.mvc.flow.FlowData;
import wint.mvc.restful.method.PatchMethod;

/**
 * Created by songlihuang on 2018/3/7.
 */
public class PatchMethodSupport extends BodySupportMethodFlowDataSupport implements PatchMethod {
    public PatchMethodSupport(FlowData flowData) {
        super(flowData);
    }
}
