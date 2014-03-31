package wint.mvc.template.engine.httl;

import httl.Engine;
import httl.Resource;
import httl.Template;
import httl.spi.Loader;
import httl.spi.loaders.AbstractLoader;
import wint.core.config.Configuration;
import wint.core.io.resource.loader.ResourceLoader;
import wint.core.service.ServiceContext;
import wint.core.service.env.Environment;
import wint.lang.exceptions.ViewException;
import wint.lang.io.FastStringWriter;
import wint.lang.utils.StringUtil;
import wint.mvc.template.Context;
import wint.mvc.template.TemplateRender;
import wint.mvc.template.engine.AbstractTemplateEngine;
import wint.mvc.template.engine.TemplateEngine;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;
import java.util.Locale;
import java.util.Properties;

/**
 * User: longyi
 * Date: 13-8-16
 * Time: 下午4:10
 */
public class HttlTemplateEngine extends AbstractTemplateEngine implements TemplateEngine {

    private Engine engine;

    @Override
    public void init(ServiceContext serviceContext) {
        super.init(serviceContext);

        Configuration wintConfiguration = serviceContext.getConfiguration();


        Properties properties = new Properties();
        properties.setProperty("input.encoding", encoding);
        properties.setProperty("output.encoding", encoding);
        properties.setProperty("localized", "false");

        if (wintConfiguration.getEnvironment().isSupportDev()) {
            properties.setProperty("reloadable", "true");
            properties.setProperty("precompiled", "false");
        } else {
            properties.setProperty("reloadable", "false");
            properties.setProperty("precompiled", "true");
        }

        properties.setProperty("loaders", httl.spi.loaders.FileLoader.class.getName());
        properties.setProperty("template.directory", getAbsoluteTemplatePath(templatePath));

        engine = Engine.getEngine(properties);

        /*
        import.packages+=com.your.domain
template.directory=/WEB-INF/templates
message.basename=/WEB-INF/messages
input.encoding=UTF-8
output.encoding=UTF-8
reloadable=false
precompiled=false
localized=false
         */
    }

    @Override
    protected String renderTemplate(TemplateRender templateRender, Context context) {
        try {
            Template template = engine.getTemplate(templateRender.getPath());
            FastStringWriter writer = new FastStringWriter();
            template.render(context.getAll(), writer);
            return writer.toString();
        } catch (IOException e) {
            throw new ViewException(e);
        } catch (ParseException e) {
            throw new ViewException(e);
        }
    }

    public String getName() {
        return "httl";
    }
}
