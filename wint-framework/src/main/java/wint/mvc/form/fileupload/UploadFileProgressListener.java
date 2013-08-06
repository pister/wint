package wint.mvc.form.fileupload;

/**
 * @author pister 2012-3-11 下午05:30:41
 */
public interface UploadFileProgressListener {
	
	void onProcess(long bytesRead, long contentLength, int items);

}
