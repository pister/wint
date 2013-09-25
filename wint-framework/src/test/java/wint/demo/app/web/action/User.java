package wint.demo.app.web.action;

import wint.mvc.flow.FlowData;
import wint.mvc.template.Context;

public class User {

    public void reg(FlowData flowData) {
        System.out.println("in user/reg");
    }

    public void execute(FlowData flowData, Context context) {
        System.out.println("use execute");
        context.put("name", "Jack");
    }

    public void $_friends(FlowData flowData, Context context) {

    }


}
