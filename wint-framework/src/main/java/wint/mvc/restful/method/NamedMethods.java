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
        namedMethods.put("GET", new NamedMethod(GetMethod.class, new MethodCreator() {
            @Override
            public ResultfulMethodFlowData create(FlowData flowData) {
                return new GetMethodSupport(flowData);
            }
        }));
        namedMethods.put("DELETE", new NamedMethod(DeleteMethod.class, new MethodCreator() {
            @Override
            public ResultfulMethodFlowData create(FlowData flowData) {
                return new DeleteMethodSupport(flowData);
            }
        }));
        namedMethods.put("HEAD", new NamedMethod(HeadMethod.class, new MethodCreator() {

            @Override
            public ResultfulMethodFlowData create(FlowData flowData) {
                return new HeadMethodSupport(flowData);
            }
        }));
        namedMethods.put("OPTIONS", new NamedMethod(OptionsMethod.class, new MethodCreator() {
            @Override
            public ResultfulMethodFlowData create(FlowData flowData) {
                return new OptionsMethodSupport(flowData);
            }
        }));
        namedMethods.put("PATCH", new NamedMethod(PatchMethod.class, new MethodCreator() {
            @Override
            public ResultfulMethodFlowData create(FlowData flowData) {
                return new PatchMethodSupport(flowData);
            }
        }));
        namedMethods.put("POST", new NamedMethod(PostMethod.class, new MethodCreator() {
            @Override
            public ResultfulMethodFlowData create(FlowData flowData) {
                return new PostMethodSupport(flowData);
            }
        }));
        namedMethods.put("PUT", new NamedMethod(PutMethod.class, new MethodCreator() {
            @Override
            public ResultfulMethodFlowData create(FlowData flowData) {
                return new PutMethodSupport(flowData);
            }
        }));
        namedMethods.put("Trace", new NamedMethod(TraceMethod.class, new MethodCreator() {
            @Override
            public ResultfulMethodFlowData create(FlowData flowData) {
                return new TraceMethodSupport(flowData);
            }
        }));
    }

    public static class NamedMethod {
        public Class<? extends ResultfulMethodFlowData> restfulMethodClass;
        public MethodCreator creator;

        public NamedMethod(Class<? extends ResultfulMethodFlowData> restfulMethodClass, MethodCreator creator) {
            this.restfulMethodClass = restfulMethodClass;
            this.creator = creator;
        }
    }

    public static NamedMethod getMethod(String name) {
        return namedMethods.get(name.toUpperCase());
    }

}
