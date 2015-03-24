package wint.mvc.url.rewrite.mapping;

/**
 * User: huangsongli
 * Date: 15/3/24
 * Time: 下午7:43
 */
public class Item {

    private String name;

    private boolean base64;

    public Item(String name, boolean base64) {
        this.name = name;
        this.base64 = base64;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isBase64() {
        return base64;
    }

    public void setBase64(boolean base64) {
        this.base64 = base64;
    }
}
