package wint.mvc.form.fileupload.jakarta;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.fileupload.FileItem;

import wint.lang.exceptions.FlowDataException;
import wint.lang.utils.IoUtil;
import wint.mvc.form.fileupload.UploadFile;

/**
 * @author pister 2012-3-11 下午05:42:48
 */
public class JakartaUploadFile implements UploadFile {

	private static final int BUF_SIZE = 1024 * 8;
	
	private FileItem fileItem;
	
	public JakartaUploadFile(FileItem fileItem) {
		super();
		this.fileItem = fileItem;
	}

	public InputStream getInputStream() {
		try {
			return fileItem.getInputStream();
		} catch (IOException e) {
			throw new FlowDataException(e);
		}
	}

	public String getName() {
		return fileItem.getName();
	}

	public String getFieldName() {
		return fileItem.getFieldName();
	}

	public long getSize() {
		return fileItem.getSize();
	}

	public void writeTo(OutputStream os) {
		byte[] buf = new byte[BUF_SIZE];
		InputStream is = null;
		try {
			is = fileItem.getInputStream();
			while (true) {
				int len = is.read(buf, 0, BUF_SIZE);
				if (len < 0) {
					break;
				}
				os.write(buf, 0, len);
			}
		} catch (Exception e) {
			throw new FlowDataException(e);
		} finally {
			IoUtil.close(is);
		}
	}

	public void writeTo(File file) {
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(file);
			writeTo(fos);
		} catch (Exception e) {
			throw new FlowDataException(e);
		} finally {
			IoUtil.close(fos);
		}
	}

}
