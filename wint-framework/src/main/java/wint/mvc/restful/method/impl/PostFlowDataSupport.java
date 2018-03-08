package wint.mvc.restful.method.impl;

import wint.mvc.flow.FlowData;
import wint.mvc.restful.method.PostFlowData;

/**
 * Created by songlihuang on 2018/3/7.
 */
public class PostFlowDataSupport extends BodySupportFlowDataSupport implements PostFlowData {
    public PostFlowDataSupport(FlowData flowData) {
        super(flowData);
    }
}
