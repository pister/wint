package wint.mvc.form.fileupload;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import wint.core.service.ServiceContext;
import wint.mvc.flow.ServletFlowData;
import wint.mvc.parameters.Parameters;

public class UploadFileFlowData extends ServletFlowData {
	
	private Map<String, UploadFile> uploadFiles;
	
	private Parameters uploadFileParameters;

	public UploadFileFlowData(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, ServiceContext serviceContext, Map<String, UploadFile> uploadFiles, Parameters uploadFileParameters) {
		super(httpServletRequest, httpServletResponse, serviceContext);
		this.uploadFiles = uploadFiles;
		this.uploadFileParameters = uploadFileParameters;
	}

	public Parameters getParameters() {
		return uploadFileParameters;
	}

	public Map<String, UploadFile> getUploadFiles() {
		return uploadFiles;
	}


}
