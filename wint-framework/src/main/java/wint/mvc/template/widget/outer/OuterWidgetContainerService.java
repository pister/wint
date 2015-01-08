package wint.mvc.template.widget.outer;

import wint.core.service.Service;
import wint.mvc.flow.InnerFlowData;
import wint.mvc.template.Context;

/**
 * User: huangsongli
 * Date: 15/1/8
 * Time: 上午10:30
 */
public interface OuterWidgetContainerService extends Service {

    OuterWidgetContainer createContainer(InnerFlowData flowData, Context context);


}
