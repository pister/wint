package wint.mvc.template.engine.freemark;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Map;

import wint.core.config.Constants;
import wint.core.service.ServiceContext;
import wint.core.service.env.Environment;
import wint.lang.exceptions.ViewException;
import wint.lang.io.FastStringWriter;
import wint.lang.magic.MagicClass;
import wint.mvc.template.Context;
import wint.mvc.template.TemplateRender;
import wint.mvc.template.engine.AbstractTemplateEngine;
import wint.mvc.template.engine.TemplateEngine;
import wint.mvc.view.Render;
import freemarker.ext.beans.BeansWrapper;
import freemarker.ext.beans.StringModel;
import freemarker.template.Configuration;
import freemarker.template.ObjectWrapper;
import freemarker.template.Template;
import freemarker.template.TemplateModel;
import freemarker.template.TemplateModelException;

public class FreeMarkerTemplateEngine extends AbstractTemplateEngine implements TemplateEngine {
	
	private Configuration freemarkerConfiguration = new Configuration();
	
	@Override
	public void init(ServiceContext serviceContext) {
		super.init(serviceContext);
		try {
			this.freemarkerConfiguration.setDirectoryForTemplateLoading(baseFile);
			freemarkerConfiguration.setOutputEncoding(encoding);
			freemarkerConfiguration.setDefaultEncoding(encoding);
			
			if (serviceContext.getConfiguration().getEnvironment().isSupportDev()) {
				freemarkerConfiguration.setTemplateUpdateDelay(0);
			} else {
                int checkInterval = serviceContext.getConfiguration().getProperties().getInt(Constants.PropertyKeys.TEMPLATE_MODIFICATION_CHECK_INTERVAL, Constants.Defaults.TEMPLATE_MODIFICATION_CHECK_INTERVAL);
                freemarkerConfiguration.setTemplateUpdateDelay(checkInterval);
			}
			
			final ObjectWrapper objectWrapper = freemarkerConfiguration.getObjectWrapper();
			
			freemarkerConfiguration.setObjectWrapper(new BeansWrapper() {
				public TemplateModel wrap(Object obj) throws TemplateModelException {
					return wrapObject(obj);
				}
				
				@SuppressWarnings("unchecked")
				private TemplateModel wrapObject(Object obj) throws TemplateModelException {
					if (obj == null) {
						return objectWrapper.wrap(obj);
					}
					if (obj instanceof Render) {
						return new RenderStringModel(this, (Render)obj);
					}
					if (obj instanceof Map<?, ?>) {
						MagicClass clazz = MagicClass.wrap(obj.getClass());
						Map<Object, Object> m = (Map<Object, Object>)clazz.newInstance().getObject();
						for (Map.Entry<?, Object> entry : ((Map<?, Object>)obj).entrySet()) {
							Object key = entry.getKey();
							Object val = entry.getValue();
							m.put(key, wrapObject(val));
						}
						return objectWrapper.wrap(m);
					} 
					if (obj instanceof Collection<?>) {
						MagicClass clazz = MagicClass.wrap(obj.getClass());
						Collection<TemplateModel> c = (Collection<TemplateModel>)clazz.newInstance().getObject();
						for (Object v : (Collection<?>)obj) {
							c.add(wrapObject(v));
						}
						return objectWrapper.wrap(c);
					} 
					if (obj.getClass().isArray()) {
						Class<?> comType = obj.getClass().getComponentType();
						int len = Array.getLength(obj);
						Object ret = Array.newInstance(comType, len);
						for (int i = 0; i < len; ++i) {
							Object o = Array.get(obj, i);
							Array.set(ret, i, wrapObject(o));
						}
						return objectWrapper.wrap(ret);
					} 
					return objectWrapper.wrap(obj);
				}
				
			});
			
		} catch (IOException e) {
			log.error("initialize freemark failed", e);
		}
	}
	
	static class RenderStringModel extends StringModel {

		private Render render;
		
		public RenderStringModel(BeansWrapper wrapper, Render render) {
			super(render, wrapper);
			this.render = render;
		}

		@Override
		public String getAsString() {
			return render.render();
		}

	}

	public String getName() {
		return "ftl";
	}

	@Override
	protected String renderTemplate(TemplateRender templateRender, Context context) {
		try {
			Template template = freemarkerConfiguration.getTemplate(templateRender.getPath());
			FastStringWriter writer = new FastStringWriter();
			template.process(context.getAll(), writer);
			return writer.toString();
		} catch (Exception e) {
			throw new ViewException(e);
		}
	}

}
