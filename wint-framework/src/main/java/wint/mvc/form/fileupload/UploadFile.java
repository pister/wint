package wint.mvc.form.fileupload;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * @author pister 2012-3-11 下午08:39:23
 */
public interface UploadFile {

	void writeTo(OutputStream os);
	
	void writeTo(File file);
	
	long getSize();
	
	InputStream getInputStream();
	
	String getName();
	
	String getFieldName();
}
