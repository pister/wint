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

    public void genDAOAndForm(Class<?> clazz, String idName) {
        autoGenDAO.gen(clazz, idName);
        autoGenView.genForm(clazz, idName, false);
    }

    public void forceGenDAOAndForm(Class<?> clazz, String idName) {
        autoGenDAO.forceGen(clazz, idName);
        autoGenView.genForm(clazz, idName, true);
    }

    public void genDAOAndForm(Class<?> clazz) {
        autoGenDAO.gen(clazz);
        autoGenView.genForm(clazz, "id", false);
    }

    public void forceGenDAOAndForm(Class<?> clazz) {
        autoGenDAO.forceGen(clazz);
        autoGenView.genForm(clazz, "id", true);
    }

}
