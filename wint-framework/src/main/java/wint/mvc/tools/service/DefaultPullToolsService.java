package wint.mvc.tools.service;

import java.util.Map;

import wint.core.service.AbstractService;
import wint.lang.utils.MapUtil;

/**
 * @author pister 2012-3-4 04:20:40
 */
public class DefaultPullToolsService extends AbstractService implements PullToolsService {

	private Map<String, Object> tools = MapUtil.newHashMap();
	
	public Map<String, Object> getPullTools() {
		return tools;
	}

	public void setTools(Map<String, String> inputTools) {
		Map<String, Object> tools = MapUtil.newHashMap();
		tools.putAll(inputTools);
		//dateUtil
		/*for (Map.Entry<String, String> entry : inputTools.entrySet()) {
			String name = entry.getKey();
			String className = entry.getValue();
			MagicClass magicClass = MagicClass.forName(className);
			MagicObject object = magicClass.newInstance();
			tools.put(name, object.getObject());
		}*/
		this.tools = tools;
	}

}
