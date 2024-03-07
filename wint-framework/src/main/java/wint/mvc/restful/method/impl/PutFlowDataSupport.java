package wint.mvc.restful.method.impl;

import wint.mvc.flow.FlowData;
import wint.mvc.restful.method.PutFlowData;
import wint.mvc.restful.method.ResultfulFlowData;

/**
 * Created by songlihuang on 2018/3/7.
 */
public class PutFlowDataSupport extends ResultfulFlowDataSupport implements PutFlowData, ResultfulFlowData {
    public PutFlowDataSupport(FlowData flowData) {
        super(flowData);
    }
}
