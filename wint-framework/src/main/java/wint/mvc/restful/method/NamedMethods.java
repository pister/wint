package wint.mvc.restful.method;

import wint.lang.utils.MapUtil;
import wint.mvc.flow.FlowData;
import wint.mvc.restful.method.impl.*;

import java.util.Map;

/**
 * Created by songlihuang on 2018/3/7.
 */
public class NamedMethods {

    private static Map<String, NamedMethod> namedMethods = MapUtil.newHashMap();

    static {
        namedMethods.put("GET", new NamedMethod(GetFlowData.class, new FlowDataCreator() {
            @Override
            public ResultfulFlowData create(FlowData flowData) {
                return new GetFlowDataSupport(flowData);
            }
        }));
        namedMethods.put("DELETE", new NamedMethod(DeleteFlowData.class, new FlowDataCreator() {
            @Override
            public ResultfulFlowData create(FlowData flowData) {
                return new DeleteFlowDataSupport(flowData);
            }
        }));
        namedMethods.put("HEAD", new NamedMethod(HeadFlowData.class, new FlowDataCreator() {

            @Override
            public ResultfulFlowData create(FlowData flowData) {
                return new HeadFlowDataSupport(flowData);
            }
        }));
        namedMethods.put("OPTIONS", new NamedMethod(OptionsFlowData.class, new FlowDataCreator() {
            @Override
            public ResultfulFlowData create(FlowData flowData) {
                return new OptionsFlowDataSupport(flowData);
            }
        }));
        namedMethods.put("PATCH", new NamedMethod(PatchFlowData.class, new FlowDataCreator() {
            @Override
            public ResultfulFlowData create(FlowData flowData) {
                return new PatchFlowDataSupport(flowData);
            }
        }));
        namedMethods.put("POST", new NamedMethod(PostFlowData.class, new FlowDataCreator() {
            @Override
            public ResultfulFlowData create(FlowData flowData) {
                return new PostFlowDataSupport(flowData);
            }
        }));
        namedMethods.put("PUT", new NamedMethod(PutFlowData.class, new FlowDataCreator() {
            @Override
            public ResultfulFlowData create(FlowData flowData) {
                return new PutFlowDataSupport(flowData);
            }
        }));
        namedMethods.put("Trace", new NamedMethod(TraceFlowData.class, new FlowDataCreator() {
            @Override
            public ResultfulFlowData create(FlowData flowData) {
                return new TraceFlowDataSupport(flowData);
            }
        }));
    }

    public static class NamedMethod {
        public Class<? extends ResultfulFlowData> restfulMethodClass;
        public FlowDataCreator creator;

        public NamedMethod(Class<? extends ResultfulFlowData> restfulMethodClass, FlowDataCreator creator) {
            this.restfulMethodClass = restfulMethodClass;
            this.creator = creator;
        }
    }

    public static NamedMethod getMethod(String name) {
        return namedMethods.get(name.toUpperCase());
    }

}
