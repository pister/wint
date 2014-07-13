package wint.mvc.flow;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import wint.core.config.Constants;
import wint.core.service.AbstractService;
import wint.core.service.ServiceContext;
import wint.mvc.form.fileupload.UploadFileService;

/**
 * @author pister
 * 2012-1-11 02:37:54
 */
public class ServletFlowDataService extends AbstractService implements FlowDataService {

	private UploadFileService uploadFileService;

    private String requestContextPath;

	@Override
	public void init() {
		super.init();
		uploadFileService = serviceContext.getService(UploadFileService.class);
        requestContextPath = serviceContext.getConfiguration().getProperties().getString(Constants.PropertyKeys.WINT_REQUEST_CONTEXT_PATH, Constants.Defaults.WINT_REQUEST_CONTEXT_PATH);


        if (uploadFileService == null) {
			uploadFileService = new UploadFileService() {
				
				public InnerFlowData getUploadFileFlowData(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
					return ServletFlowDataService.this.createFlowData(httpServletRequest, httpServletResponse);
				}
				
				public boolean isUploadFile(HttpServletRequest httpServletRequest) {
					return false;
				}
				
				public void destroy() {
				}
				
				public String getName() {
					return null;
				}
				
				public ServiceContext getServiceContext() {
					return null;
				}
				
				public void init() {
				}
				
			};
		}
	}

	public InnerFlowData createFlowData(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
		if (uploadFileService.isUploadFile(httpServletRequest)) {
			return uploadFileService.getUploadFileFlowData(httpServletRequest, httpServletResponse);
		} else {
			return new ServletFlowData(httpServletRequest, httpServletResponse, serviceContext, requestContextPath);
		}
	}

}
