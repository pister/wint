package wint.help.biz.result;

import wint.mvc.view.Render;

/**
 * @author songlihuang
 * @date 2024/5/31 15:33
 */
public abstract class MessageRender implements Render {

    public abstract String getMessage();

    @Override
    public String render() {
        return getMessage();
    }
}
