package wint.mvc.form.fileupload;

import javax.servlet.http.HttpServletRequest;

public interface UploadFileFactory {
	
	boolean isUploadFile(HttpServletRequest request);
	
	UploadFileInfo getUploadFiles(HttpServletRequest request, UploadFileProgressListener uploadFileProgressListener);

}
