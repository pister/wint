package wint.mvc.pipeline.valves;

import java.util.List;

import wint.core.config.Constants;
import wint.lang.magic.MagicList;
import wint.lang.utils.CollectionUtil;
import wint.lang.utils.TargetUtil;
import wint.lang.utils.TextUtil;
import wint.mvc.flow.InnerFlowData;
import wint.mvc.parameters.ListArguments;
import wint.mvc.pipeline.PipelineContext;

/**
 *  /xxx/yyy				=>	/xxx/yyy		[]
 *  /xxx/yyy/444			=>	/xxx/yyy		[444]
 *  /xxx/yyy-444			=>	/xxx/yyy		[444]
 *  /xxx/yyy/444-555-666	=>	/xxx/yyy 		[444,555,666]
 *  /xxx/yyy-444-555_666	=>	/xxx/yyy		[444,555,666]
 *  /xxx/yyy-zzz-444-555	=> 	/xxx/yyyZzz 	[444,555]
 *  /xxx/yyy/444-zzz-555	=>  /xxx/yyy 		[444,zzz,555]
 *  /xxx/yyy/444--555-666	=>  /xxx/yyy 		[444,'',555,666]
 *  /xxx/yyy/444/zzz		=>	/xxx/yyy 		[444,zzz]
 *
 * @author pister
 * 2011-12-30 03:09:41
 */
public class AnalyzeUrlValve extends AbstractValve {

	private char[] tokens = {'/', '-', '_'};

	public void invoke(PipelineContext pipelineContext, InnerFlowData flowData) {
		String target = flowData.getTarget();
		target = TargetUtil.normalizePath(target);
		List<StringToken> tokens = split(target);
		MagicList<StringToken> pathParts = MagicList.newList();
		MagicList<String> parameterParts = MagicList.newList();
		
		boolean addingPath = true;
		for (StringToken stringToken : tokens) {
			if (!addingPath) {
				parameterParts.add(stringToken.string);
				continue;
			}
			if (!TextUtil.startWithDigit(stringToken.string)) {
				pathParts.add(stringToken);
			} else {
				addingPath = false;
				parameterParts.add(stringToken.string);
			}
		}
		
		target = joinForTarget(pathParts);
		flowData.setTarget(TargetUtil.normalizeTarget(target));
		flowData.setAttribute(Constants.FlowDataAttributeKeys.INDEXED_PARAMETERS, parameterParts);
		flowData.setArguments(new ListArguments(parameterParts));
		
		pipelineContext.invokeNext(flowData);
	}
	
	private String joinForTarget(MagicList<StringToken> pathParts) {
		StringBuilder sb = new StringBuilder();
		for (StringToken stringToken : pathParts) {
			sb.append(stringToken.string);
			sb.append(stringToken.token);
		}
		return sb.toString();
	}
	
	private List<StringToken> split(String target) {
		List<StringToken> ret = CollectionUtil.newArrayList();
		StringBuilder sb = new StringBuilder();
		for (int i = 0, len = target.length(); i < len; ++i) {
			char c = target.charAt(i);
			if (isToken(c)) {
				StringToken stringToken = new StringToken(sb.toString(), String.valueOf(c));
				ret.add(stringToken);
				sb = new StringBuilder();
			} else {
				sb.append(c);
			}
		}
		StringToken stringToken = new StringToken(sb.toString(), "");
		ret.add(stringToken);
		return ret;
	}
	
	private boolean isToken(char c) {
		for (char x : tokens) {
			if (c == x) {
				return true;
			}
		}
		return false;
	}

	static class StringToken {
		String string;
		String token;
		public StringToken(String string, String token) {
			super();
			this.string = string;
			this.token = token;
		}
		@Override
		public String toString() {
			return  string + "["+ token +"]";
		}
	}
	
}
