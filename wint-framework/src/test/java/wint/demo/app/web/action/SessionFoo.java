package wint.demo.app.web.action;

import java.util.Date;

import javax.servlet.http.HttpSession;

import wint.mvc.flow.FlowData;
import wint.mvc.flow.Session;
import wint.mvc.template.Context;

public class SessionFoo {

	public void first(FlowData flowData, Context context) {
		Session session = flowData.getSession();
		System.out.println(session);
		Integer count = (Integer)session.getAttribute("coun|t");
		if (count == null) {
			count = 1;
		} else {
			count = count + 1;
		}
		session.setAttribute("coun|t", count);
		context.put("count", count);
		
		String appName = (String)session.getAttribute("app_nam;e");
		if (appName == null) {
			appName = "app_name";
		} else {
			appName = appName + "_" + count;
		}
		if (appName.length() > 20) {
			session.removeAttribute("app_nam;e");
		} else {
			session.setAttribute("app_nam;e", appName);
		}

		TheObject theObject = (TheObject)session.getAttribute("theObject");
		if (theObject == null) {
			theObject = new TheObject();
			theObject.setName("aaaa");
			theObject.setId(33L);
		} else {
			theObject.setAge(theObject.getAge() + 1);
		}
		
		session.setAttribute("theObject", theObject);
		
		Object hello = session.getAttribute("hello");
		if (hello == null) {
			session.setAttribute("hello", "ha中午啦ha");
		} else {
			session.setAttribute("hello", null);
		}
		
		
		Date lastDate = (Date)session.getAttribute("lastDate");
		session.setAttribute("lastDate", new Date());
		
		//session.invalidate();
		
		context.put("hello", hello);
		context.put("lastDate", lastDate);
		context.put("appName", appName);
		context.put("theObject", theObject);
		context.put("session", session);
	}
	
}
