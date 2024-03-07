package wint.mvc.restful.method.impl;

import wint.mvc.flow.FlowData;
import wint.mvc.restful.method.PostFlowData;
import wint.mvc.restful.method.ResultfulFlowData;

/**
 * Created by songlihuang on 2018/3/7.
 */
public class PostFlowDataSupport extends ResultfulFlowDataSupport implements PostFlowData, ResultfulFlowData {
    public PostFlowDataSupport(FlowData flowData) {
        super(flowData);
    }
}
