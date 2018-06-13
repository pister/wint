package wint.demo.app.web.action;

import wint.demo.app.biz.domain.UserDO;
import wint.demo.app.biz.results.HelloResultCodes;
import wint.help.biz.result.ResultCode;
import wint.lang.utils.MapUtil;
import wint.mvc.flow.FlowData;
import wint.mvc.form.Form;
import wint.mvc.module.annotations.Action;
import wint.mvc.module.annotations.LimitedAction;
import wint.mvc.template.Context;

import java.util.Date;
import java.util.Map;

public class Hello {

    private Date initDate;
    private Date myDate;

    public void execute(Context context, FlowData flowData, int id, int type, boolean sex) {
        System.out.println("execute id: " + id + ", type: " + type + ", sex:" + sex);
        flowData.setTarget("hello/default");
        context.put("from", "hello execute!~!~");
    }

    @LimitedAction(allowSuffix = {"php", ".jsp"})
    public void helloLimit(FlowData flowData, Context context) {
        System.out.println("hello in helloLimit");
    }


    @Action(defaultTarget = "hello/sayWords")
    public void doTheJson(FlowData flowData, Context context) {
        context.put("name", "hsl");
        context.put("age", 31);

        flowData.setViewType("json");
    }

    public void theJson(FlowData flowData, Context context) {
        context.put("name", "hsl");
        context.put("age", 31);
        context.put("weight", 73.5);
        context.put("books", new String[]{"java", "c++"});
        context.put("fav", new String[]{"programming", "swimming"});

        TheObject theObject = new TheObject();
        theObject.setAge(66);
        theObject.setId(32);
        theObject.setName("myObject");

        TheOtherObject theOtherObject = new TheOtherObject();
        theOtherObject.setDescription("dddddd");
        theOtherObject.setType(3);
        theObject.setTheOtherObject(theOtherObject);

        context.put("theObject", theObject);
        Map<String, Object> others = MapUtil.newHashMap();
        others.put("country", "china");
        others.put("city", "hangzhou");
        context.put("others", others);
        context.put("now", new Date());

        flowData.setViewType("json");
    }

    public void sayWords(Context context, FlowData flowData, int a, int b, int c, int d) {
        System.out.println(" world!: " + a + ", " + b + ", " + c + ", " + d);
        System.out.println("initDate: " + initDate);
        System.out.println("myDate: " + myDate);
        //	flowData.setViewType("json");
        context.put("name", "hsl");
        context.put("age", 123);
        context.put("myDate", myDate);
    }

    @Action(defaultTarget = "user/reg")
    public void doRegister(FlowData flowData) {
        UserDO userDO = new UserDO();
        Form form = flowData.getForm("user.reg");
        if (!form.apply(userDO)) {
            return;
        }
        System.out.println("doRegister!!!! " + userDO);
        //	flowData.redirectTo("http://baibutao.com");
    }

    public void helloResult(Context context, FlowData flowData) {
        ResultCode resultCode = HelloResultCodes.FIRST_NAME;
        System.out.println(resultCode.getMessage());
        context.put("resultCode", resultCode);
    }

    public void theRawText(FlowData flowData, Context context) {
        String s = "hello<span style=\"color: red;\">world</span>";
        context.put("s", s);
    }

    public void hello2(FlowData flowData, Context context) {

        System.out.println("in hello2 !!!");
        UserDO userDO = new UserDO();

        userDO.setName("张三");
        userDO.setUserId(344);

        context.put("user", userDO);
        context.put("theName", "另一个中文 <script>alert(33);</script>");
    }

    @Action
    public String toString() {
        System.out.println("xxxxx!!!!!!!!!!!!!!!!");
        return "hello/sayWords";
    }

    public void setInitDate(Date initDate) {
        this.initDate = initDate;
    }

    public void setMyDate(Date myDate) {
        this.myDate = myDate;
    }

}
