package wint.lang.utils;

import wint.help.mvc.security.csrf.CsrfTokenUtil;
import wint.mvc.template.filters.CvsFilterRender;
import wint.mvc.template.filters.HtmlFilterRender;
import wint.mvc.template.filters.JavaScriptFilterRender;
import wint.mvc.template.filters.RawContentFilterRender;
import wint.mvc.template.filters.XmlFilterRender;
import wint.mvc.view.Render;

public class SecurityUtil {
	
	public static Render rawContent(Object o) {
		return new RawContentFilterRender(o);
	}
	
	public static Render rawText(Object o) {
		return rawContent(o);
	}

	public static Render raw(Object o) {
		return rawContent(o);
	}

	public static Render escapeHtml(Object o) {
		return new HtmlFilterRender(o);
	}
	
	public static Render escapeJavaScript(Object o) {
		return new JavaScriptFilterRender(o);
	}
	
	public static Render escapeJs(Object o) {
		return escapeJavaScript(o);
	}
	
	public static Render escapeXml(Object o) {
		return new XmlFilterRender(o);
	}
	
	public static Render escapeCvs(Object o) {
		return new CvsFilterRender(o);
	}
	
	public static String csrfToken() {
		return getCsrfToken();
	}

    public static String xToken() {
        return getCsrfToken();
    }
	
	public static String getCsrfToken() {
		return CsrfTokenUtil.token();
	}

    public static Render tokenHtml() {
        return rawContent(CsrfTokenUtil.tokenHtml());
    }

    public static Render getTokenHtml() {
        return tokenHtml();
    }

}
