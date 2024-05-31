package wint.help.biz.result;

/**
 * @author songlihuang
 * @date 2024/5/31 15:37
 */
public class StringMessage extends MessageRender {

    private String message;

    public StringMessage(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
