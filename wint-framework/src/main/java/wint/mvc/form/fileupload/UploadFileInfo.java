package wint.mvc.form.fileupload;

import java.util.Map;

import wint.mvc.parameters.Parameters;

public class UploadFileInfo {
	
	private Map<String, UploadFile> uploadFiles;
	
	private Parameters parameters;

	public UploadFileInfo(Map<String, UploadFile> uploadFiles, Parameters parameters) {
		super();
		this.uploadFiles = uploadFiles;
		this.parameters = parameters;
	}

	public Map<String, UploadFile> getUploadFiles() {
		return uploadFiles;
	}

	public void setUploadFiles(Map<String, UploadFile> uploadFiles) {
		this.uploadFiles = uploadFiles;
	}

	public Parameters getParameters() {
		return parameters;
	}

	public void setParameters(Parameters parameters) {
		this.parameters = parameters;
	}

}
