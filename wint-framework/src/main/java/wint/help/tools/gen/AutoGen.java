package wint.help.tools.gen;

import wint.help.tools.ibatis.AutoGenDAO;

/**
 * User: longyi
 * Date: 13-12-22
 * Time: 下午3:02
 */
public class AutoGen {

    private AutoGenDAO autoGenDAO;

    private AutoGenView autoGenView;

    public AutoGen(String prefix) {
        autoGenDAO = new AutoGenDAO(prefix);
        autoGenView = new AutoGenView(prefix);
    }

    public void gen(Class<?> clazz, String idName, String actionContext) {
        autoGenDAO.gen(clazz, idName);
        autoGenView.gen(clazz, idName, actionContext);
    }

    public void forceGen(Class<?> clazz, String idName, String actionContext) {
        autoGenDAO.forceGen(clazz, idName);
        autoGenView.forceGen(clazz, idName, actionContext);
    }

    public void gen(Class<?> clazz, String actionContext) {
        autoGenDAO.gen(clazz);
        autoGenView.gen(clazz, actionContext);
    }

    public void forceGen(Class<?> clazz, String actionContext) {
        autoGenDAO.forceGen(clazz);
        autoGenView.forceGen(clazz, actionContext);
    }

}
