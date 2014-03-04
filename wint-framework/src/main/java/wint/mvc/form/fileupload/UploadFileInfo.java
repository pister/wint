package wint.mvc.form.fileupload;

import java.util.Map;

import wint.mvc.parameters.Parameters;

public class UploadFileInfo {
	
	private Map<String, UploadFile> uploadFiles;
	
	private FileUploadParameters parameters;

	public UploadFileInfo(Map<String, UploadFile> uploadFiles, FileUploadParameters parameters) {
		super();
		this.uploadFiles = uploadFiles;
		this.parameters = parameters;
	}

	public Map<String, UploadFile> getUploadFiles() {
		return uploadFiles;
	}

	public FileUploadParameters getParameters() {
		return parameters;
	}

}
