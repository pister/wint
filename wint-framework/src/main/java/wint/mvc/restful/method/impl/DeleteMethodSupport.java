package wint.mvc.restful.method.impl;

import wint.mvc.flow.FlowData;
import wint.mvc.restful.method.DeleteMethod;

/**
 * Created by songlihuang on 2018/3/7.
 */
public class DeleteMethodSupport extends ResultfulMethodFlowDataSupport implements DeleteMethod {
    public DeleteMethodSupport(FlowData flowData) {
        super(flowData);
    }
}
