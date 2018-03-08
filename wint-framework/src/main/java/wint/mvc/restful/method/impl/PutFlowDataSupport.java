package wint.mvc.restful.method.impl;

import wint.mvc.flow.FlowData;
import wint.mvc.restful.method.PutFlowData;

/**
 * Created by songlihuang on 2018/3/7.
 */
public class PutFlowDataSupport extends BodySupportFlowDataSupport implements PutFlowData {
    public PutFlowDataSupport(FlowData flowData) {
        super(flowData);
    }
}
