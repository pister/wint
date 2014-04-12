package wint.mvc.parameters;

import wint.lang.WintException;
import wint.lang.utils.MapUtil;
import wint.lang.utils.StringUtil;
import wint.mvc.form.fileupload.UploadFile;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ServletParameters extends AbstractParameters {

    private Map<String, String[]> parameters = MapUtil.newHashMap();

    @SuppressWarnings("unchecked")
    public ServletParameters(HttpServletRequest httpServletRequest) {
        super();
        for (String name : (List<String>) Collections.list(httpServletRequest.getParameterNames())) {
            String paramName = normalizeName(name);
            String[] values = httpServletRequest.getParameterValues(name);
            parameters.put(paramName, values);
        }
    }

    public String getStringImpl(String name, String defaultValue) {
        name = normalizeName(name);
        String[] values = getStringArray(name, null);
        if (values == null || values.length == 0) {
            return defaultValue;
        }
        return StringUtil.trimToEmpty(values[0]);
    }

    public String[] getStringArrayImpl(String name, String[] defaultArray) {
        name = normalizeName(name);
        String[] values = parameters.get(name);
        if (values == null || values.length == 0) {
            return defaultArray;
        }
        return values;
    }

    public Set<String> getNames() {
        return parameters.keySet();
    }

    @Override
    public UploadFile getUploadFile(String name) {
        throw new WintException("Unsupport operation on normal flowdata: \r\n" +
                "Please add the property  enctype=\"multipart/form-data\"  to your form, \r\n" +
                "Add commons-uploadfile library to your classpath (optional: commons-io).\r\n" +
                "a suggest maven dependencies are:\r\n\r\n" +
                "<dependency>\r\n\t<groupId>commons-fileupload</groupId>\r\n\t<artifactId>commons-fileupload</artifactId>\r\n\t<version>1.3.1</version>\r\n</dependency>\r\n\r\n" +
                "<dependency>\r\n\t<groupId>commons-io</groupId>\r\n\t<artifactId>commons-io</artifactId>\r\n\t<version>2.1</version>\r\n</dependency>\r\n\r\n");
    }

    @Override
    public Set<String> getUploadFileNames() {
        throw new WintException("Unsupport operation on normal flowdata: \r\n" +
                "Please add the property  enctype=\"multipart/form-data\"  to your form, \r\n" +
                "Add commons-uploadfile library to your classpath (optional: commons-io).\r\n" +
                "a suggest maven dependencies are:\r\n\r\n" +
                "<dependency>\r\n\t<groupId>commons-fileupload</groupId>\r\n\t<artifactId>commons-fileupload</artifactId>\r\n\t<version>1.3.1</version>\r\n</dependency>\r\n\r\n" +
                "<dependency>\r\n\t<groupId>commons-io</groupId>\r\n\t<artifactId>commons-io</artifactId>\r\n\t<version>2.1</version>\r\n</dependency>\r\n\r\n");
    }
}
