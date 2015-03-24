package wint.mvc.url.rewrite;

import wint.mvc.parameters.Parameters;

/**
 * User: huangsongli
 * Date: 15/3/24
 * Time: 下午4:47
 */
public class RequestData {

    private String target;
    private Parameters parameters;

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public Parameters getParameters() {
        return parameters;
    }

    public void setParameters(Parameters parameters) {
        this.parameters = parameters;
    }

    @Override
    public String toString() {
        return "RequestData{" +
                "target='" + target + '\'' +
                ", parameters=" + parameters +
                '}';
    }
}
