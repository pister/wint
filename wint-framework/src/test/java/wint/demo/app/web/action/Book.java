package wint.demo.app.web.action;

import wint.mvc.restful.method.GetMethod;
import wint.mvc.restful.method.PostMethod;
import wint.mvc.restful.method.PutMethod;
import wint.mvc.template.Context;

/**
 * Created by songlihuang on 2018/3/7.
 */
public class Book {

    public void list(GetMethod method, Context context) {
        System.out.println("Book list");
    }

    public void execute(PostMethod method, Context context) {
        // for create
        System.out.println("Book execute post");

    }

    public void execute(PutMethod method, Context context) {
        // for update
        System.out.println("Book execute put");

    }

    public void execute(GetMethod method, Context context) {
        // for update
        System.out.println("Book execute get");

    }

}
