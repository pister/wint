package wint.mvc.pipeline.valves;

import wint.mvc.flow.InnerFlowData;
import wint.mvc.pipeline.PipelineContext;
import wint.mvc.view.types.ViewTypes;

/**
 * User: huangsongli
 * Date: 15/1/2
 * Time: 上午8:10
 */
public class InitJsonValve extends AbstractValve {
    @Override
    public void invoke(PipelineContext pipelineContext, InnerFlowData innerFlowData) {
        innerFlowData.setViewType(ViewTypes.JSON_VIEW_TYPE);
        pipelineContext.invokeNext(innerFlowData);
    }
}
