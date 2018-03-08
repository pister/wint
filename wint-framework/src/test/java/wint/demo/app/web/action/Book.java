package wint.demo.app.web.action;

import wint.mvc.flow.FlowData;
import wint.mvc.restful.method.GetFlowData;
import wint.mvc.restful.method.PostFlowData;
import wint.mvc.restful.method.PutFlowData;
import wint.mvc.template.Context;

/**
 * Created by songlihuang on 2018/3/7.
 */
public class Book {

    public void list(GetFlowData flowData, Context context) {
        System.out.println("Book list");
    }

    public void execute(PostFlowData flowData, Context context) {
        // for create
        System.out.println("Book execute post");

    }

    public void execute(PutFlowData flowData, Context context) {
        // for update
        System.out.println("Book execute put");
    }

    public void execute(FlowData flowData, Context context) {
        // for update
        System.out.println("Book execute default");
    }

    public void execute(GetFlowData flowData, Context context, int arg) {
        // for update
        System.out.println("Book execute get:" + arg);

    }

}
