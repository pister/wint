package wint.mvc.template.engine.velocity;

import java.util.Set;

import org.apache.velocity.app.event.ReferenceInsertionEventHandler;

import wint.lang.utils.CollectionUtil;
import wint.lang.utils.EscapeUtil;
import wint.lang.utils.SecurityUtil;
import wint.mvc.view.Render;

/**
 * @author pister
 *
 */
public class WintEventHandler implements ReferenceInsertionEventHandler {
	
	private Set<String> nofilterNames = CollectionUtil.newHashSet();
	
	private Set<String> innerNames = CollectionUtil.newHashSet();
	
	public WintEventHandler(Set<String> innerNames, Set<String> nofilters) {
		super();
		this.innerNames.addAll(innerNames);
		this.nofilterNames.addAll(nofilters);
	}

	public Object referenceInsert(String reference, Object value) {
		String name = VelocityEventHandlerUtil.toVariableName(reference);
		return doFilter(name, value);
	}
	
	protected Object doFilter(String name, Object value) {
		if (value instanceof Render) {
			return ((Render)value).render();
		}
		if (innerNames.contains(name)) {
			return value;
		}
		if (nofilterNames.contains(name)) {
			return value;
		}
		
		if (!(value instanceof String)) {
			return value;
		}
		
		String stringValue = (String)value;
		if (SecurityUtil.isRawString(stringValue)) {
			return SecurityUtil.tryExtractRawString(stringValue);
		}
		
		return EscapeUtil.escapeHtmlSimple(stringValue);
	}
	

	
	public Set<String> getNofilterNames() {
		return nofilterNames;
	}

	public void setNofilterNames(Set<String> nofilterNames) {
		this.nofilterNames = nofilterNames;
	}


}
