package wint.mvc.template.engine.velocity;

import wint.lang.utils.StringUtil;

public class VelocityEventHandlerUtil {
	
	private static final String VAR_TAG = "$";
	
	public static String toVariableName(String reference) {
		if (StringUtil.isEmpty(reference)) {
			return reference;
		}
		if (!reference.startsWith(VAR_TAG)) {
			return reference;
		}
		String referenceName = reference.substring(1);
		if (referenceName.startsWith("!")) {
			referenceName = referenceName.substring(1);
		}
		if (referenceName.startsWith("{")) {
			referenceName = referenceName.substring(1);
			referenceName = StringUtil.getLastBefore(referenceName, "}");
		}
		if (referenceName.startsWith("!")) {
			referenceName = referenceName.substring(1);
		}
		referenceName = StringUtil.getFirstBefore(referenceName, ".");
		
		return referenceName;
	}

}
