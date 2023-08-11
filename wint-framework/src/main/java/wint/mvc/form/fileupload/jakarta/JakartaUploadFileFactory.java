package wint.mvc.form.fileupload.jakarta;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUpload;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.fileupload.servlet.ServletRequestContext;

import wint.lang.exceptions.FlowDataException;
import wint.lang.utils.CollectionUtil;
import wint.lang.utils.LibUtil;
import wint.lang.utils.MapUtil;
import wint.mvc.form.fileupload.FileUploadParameters;
import wint.mvc.form.fileupload.UploadFile;
import wint.mvc.form.fileupload.UploadFileFactory;
import wint.mvc.form.fileupload.UploadFileInfo;
import wint.mvc.form.fileupload.UploadFileProgressListener;

/**
 * @author pister 2012-3-11 下午08:42:38
 */
public class JakartaUploadFileFactory implements UploadFileFactory {

	/**
	 * 文件大小在 100k 以下，直接存放在内存
	 */
	private int SIZE_THRESHOLD = 100 * 1024;
	
	private FileItemFactory fileItemFactory;
	
	private String charset;
	
	private long maxSize;
	
	public JakartaUploadFileFactory(String charset, long maxSize) {
		super();
		this.charset = charset;
		this.maxSize = maxSize;
		this.fileItemFactory = new DiskFileItemFactory(SIZE_THRESHOLD, null);
		
	}

	public boolean enable() {
		return LibUtil.isCommonsUploadFileExist();
	}

	@SuppressWarnings("unchecked")
	public UploadFileInfo getUploadFiles(HttpServletRequest request, UploadFileProgressListener uploadFileProgressListener) {
		FileUpload fileUpload = new FileUpload(fileItemFactory);
		fileUpload.setFileSizeMax(maxSize);
		if (uploadFileProgressListener != null) {
			fileUpload.setProgressListener(new JakartaUploadFileProgressListener(uploadFileProgressListener));
		}
		Map<String, UploadFile> uploadFiles = MapUtil.newHashMap();
		try {
			List<FileItem> fileItems = fileUpload.parseRequest(new ServletRequestContext(request));
            Map<String, List<String>> parameters = MapUtil.newHashMap();
			for (FileItem fileItem : fileItems) {
				if (fileItem.isFormField()) {
					String name = fileItem.getFieldName();
					String value = fileItem.getString(charset);

                    List<String> values = parameters.get(name);
                    if (values == null) {
                        values = CollectionUtil.newArrayList(1);
                        parameters.put(name, values);
                    }
                    values.add(value);
				} else {
					String name = fileItem.getFieldName();
					uploadFiles.put(name, new JakartaUploadFile(fileItem));
				}
			}
            Map<String, String[]> parametersMap = MapUtil.newHashMap();
            for (Map.Entry<String, List<String>> entry : parameters.entrySet()) {
                parametersMap.put(entry.getKey(), entry.getValue().toArray(new String[0]));
            }
            return new UploadFileInfo(uploadFiles, new FileUploadParameters(parametersMap, uploadFiles));
		} catch (Exception e) {
			throw new FlowDataException(e);
		}
	}

	public boolean isUploadFile(HttpServletRequest request) {
		return ServletFileUpload.isMultipartContent(request);
	}

}
