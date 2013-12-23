package wint.help.tools.gen.common;

import wint.lang.template.SimpleVelocityEngine;
import wint.lang.utils.MapUtil;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.Map;

/**
 * User: longyi
 * Date: 13-12-22
 * Time: 上午11:04
 */
public class TemplateSourceGenator {

    private String encoding = "utf-8";

    private SimpleVelocityEngine simpleTemplateEngine = new SimpleVelocityEngine();

    public TemplateSourceGenator() {
        simpleTemplateEngine.init(encoding);
    }

    protected void genSource(Map<String, Object> context, String templateName, File targetFile, FileWriter fileWriter) {
        StringWriter contentWriter = new StringWriter();
        genSource(context, contentWriter, templateName);
        fileWriter.writeToFile(contentWriter.toString(), targetFile);
    }

    protected void genSource(Map<String, Object> context, Writer out, String templateName) {
        try {
            Reader reader = getTemplateReader(templateName);
            Map<String, Object> targetContext = MapUtil.newHashMap(context);
            simpleTemplateEngine.merge(reader, out, targetContext);
            reader.close();
            out.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private Reader getTemplateReader(String templateName) throws UnsupportedEncodingException {
        InputStream is = TemplateSourceGenator.class.getResourceAsStream(templateName);
        if (is != null) {
            return new InputStreamReader(is, encoding);
        }
        is = Thread.currentThread().getContextClassLoader().getResourceAsStream(templateName);
        if (is != null) {
            return new InputStreamReader(is, encoding);
        }
        is = TemplateSourceGenator.class.getClassLoader().getResourceAsStream(templateName);
        if (is != null) {
            return new InputStreamReader(is, encoding);
        }
        return null;
    }


}
