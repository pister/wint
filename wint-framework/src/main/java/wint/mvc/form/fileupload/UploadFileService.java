package wint.mvc.form.fileupload;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import wint.core.service.Service;
import wint.mvc.flow.InnerFlowData;

public interface UploadFileService extends Service {
	
	InnerFlowData getUploadFileFlowData(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse);	
	
	boolean isUploadFile(HttpServletRequest httpServletRequest);
	
}
