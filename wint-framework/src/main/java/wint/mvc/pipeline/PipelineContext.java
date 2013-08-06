package wint.mvc.pipeline;

import java.util.List;
import java.util.Map;

import wint.lang.WintException;
import wint.lang.exceptions.FlowDataException;
import wint.lang.misc.profiler.Profiler;
import wint.lang.utils.CollectionUtil;
import wint.lang.utils.MapUtil;
import wint.lang.utils.StringUtil;
import wint.mvc.flow.InnerFlowData;
import wint.mvc.pipeline.valves.Valve;

public class PipelineContext {
	
	private static final int MAX_REDIRECT_TIMES = 9;
	
	private List<Valve> valves;
	
	private Map<String, Integer> label2Valves;
	
	private boolean broken = false;
	
	private int currentValveIndex = 0;
	
	private int gotoTimes = 0;
	
	private final int MAX_VALVES;
	
	public PipelineContext(List<Valve> inputValves) {
		super();
		MAX_VALVES = inputValves.size();
		valves = CollectionUtil.newArrayList(inputValves);
		label2Valves = MapUtil.newHashMap();
		int i = -1;
		for (Valve valve : inputValves) {
			++i;
			String label = valve.getLabel();
			if (StringUtil.isEmpty(label)) {
				continue;
			}
			label2Valves.put(valve.getLabel(), i);
		}
	}
	
	/**
	 * 执行特定label的一个valve
	 * @param label
	 */
	public void invokeTo(String label, InnerFlowData innerFlowData) {
		if (broken) {
			return;
		}
		if (gotoTimes >= MAX_REDIRECT_TIMES) {
			throw new FlowDataException("too many redirect times {"+ gotoTimes +"} !");
		}
		++gotoTimes;
		Integer valveIndex = label2Valves.get(label);
		if (valveIndex == null) {
			throw new WintException("can not find valve for label: " + label);
		}
		currentValveIndex = valveIndex;
		Valve valve = getCurrent();
		if (valve == null) {
			return;
		}
		try {
			Profiler.enter("invoke valve " + valve.getName());
			valve.invoke(this, innerFlowData);
		} finally {
			Profiler.release();
		}
	}

	private Valve getCurrent() {
		if (broken) {
			return null;
		}
		if (currentValveIndex >= MAX_VALVES) {
			return null;
		}
		Valve valve = valves.get(currentValveIndex);
		++currentValveIndex;
		return valve;
	}
	
	/**
	 * 执行下一个valve
	 * @param innerFlowData
	 */
	public void invokeNext(InnerFlowData innerFlowData) {
		Valve valve = getCurrent();
		if (valve == null) {
			return;
		}
		try {
			Profiler.enter("invoke valve " + valve.getName());
			valve.invoke(this, innerFlowData);
		} finally {
			Profiler.release();
		}
	}
	
	public void breakPipeline() {
		broken = true;
	}


}
