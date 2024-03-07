package wint.mvc.restful.request;

import wint.help.json.wrapper.JsonList;
import wint.help.json.wrapper.JsonObject;

import java.util.List;

/**
 * @author songlihuang
 * @date 2024/3/7 10:14
 */
public class NotSupportRequestBody implements RequestBody{

    private String notSupportMessage;

    public NotSupportRequestBody(String notSupportMessage) {
        this.notSupportMessage = notSupportMessage;
    }

    @Override
    public byte[] getData() {
        throw new UnsupportedOperationException(notSupportMessage);
    }

    @Override
    public String getString() {
        throw new UnsupportedOperationException(notSupportMessage);
    }

    @Override
    public JsonObject getJsonObject() {
        throw new UnsupportedOperationException(notSupportMessage);
    }

    @Override
    public <T> T getJsonObject(Class<T> theType) {
        throw new UnsupportedOperationException(notSupportMessage);
    }

    @Override
    public JsonList getJsonList() {
        throw new UnsupportedOperationException(notSupportMessage);
    }

    @Override
    public <T> List<T> getJsonList(Class<T> elementType) {
        throw new UnsupportedOperationException(notSupportMessage);
    }
}
