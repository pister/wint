package wint.mvc.restful.method.impl;

import wint.mvc.flow.FlowData;
import wint.mvc.restful.method.OptionsMethod;

/**
 * Created by songlihuang on 2018/3/7.
 */
public class OptionsMethodSupport extends ResultfulMethodFlowDataSupport implements OptionsMethod {
    public OptionsMethodSupport(FlowData flowData) {
        super(flowData);
    }
}
