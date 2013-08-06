package wint.mvc.form.fileupload.jakarta;

import org.apache.commons.fileupload.ProgressListener;

import wint.mvc.form.fileupload.UploadFileProgressListener;

public class JakartaUploadFileProgressListener implements ProgressListener {

	private UploadFileProgressListener uploadFileProgressListener;
	
	public JakartaUploadFileProgressListener(UploadFileProgressListener uploadFileProgressListener) {
		super();
		this.uploadFileProgressListener = uploadFileProgressListener;
	}

	public void update(long pBytesRead, long pContentLength, int pItems) {
		if (uploadFileProgressListener != null) {
			uploadFileProgressListener.onProcess(pBytesRead, pContentLength, pItems);
		}
	}

}
