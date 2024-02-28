package wint.mvc.form.fileupload;

import wint.core.config.Constants;
import wint.core.config.property.PropertiesMap;
import wint.core.service.AbstractService;
import wint.lang.magic.MagicClass;
import wint.lang.utils.LibUtil;
import wint.lang.utils.StringUtil;
import wint.mvc.flow.InnerFlowData;
import wint.mvc.parameters.Parameters;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class DefaultUploadFileService extends AbstractService implements UploadFileService {

	private UploadFileFactory uploadFileFactory;
	
	private String charset;
	
	private long uploadFileMaxSize = 0;

    private String requestContextPath;

	@Override
	public void init() {
		super.init();
		PropertiesMap properties = this.getServiceContext().getConfiguration().getProperties();
        requestContextPath = properties.getString(Constants.PropertyKeys.WINT_REQUEST_CONTEXT_PATH, Constants.Defaults.WINT_REQUEST_CONTEXT_PATH);

        if (StringUtil.isEmpty(charset)) {
			charset = properties.getString(Constants.PropertyKeys.CHARSET_ENCODING, Constants.Defaults.CHARSET_ENCODING);
		}
		if (uploadFileMaxSize == 0) {
			uploadFileMaxSize = properties.getLong(Constants.PropertyKeys.UPLOAD_FILE_MAX_SIZE, Constants.Defaults.UPLOAD_FILE_MAX_SIZE);
		}
		if (LibUtil.isCommonsUploadFileExist()) {
			MagicClass uploadFileFactoryClass = MagicClass.forName("wint.mvc.form.fileupload.jakarta.JakartaUploadFileFactory");
			uploadFileFactory = (UploadFileFactory)uploadFileFactoryClass.newInstance(new Class<?>[] {String.class, Long.TYPE}, new Object[] {charset, uploadFileMaxSize}).getObject();
			if (log.isInfoEnabled()) {
				log.info("Jakarta upload-file library loaded.");
			}
		} else {
			uploadFileFactory = new UploadFileFactory() {
				public UploadFileInfo getUploadFiles(HttpServletRequest request, UploadFileProgressListener uploadFileProgressListener) {
					return null;
				}
				public boolean isUploadFile(HttpServletRequest request) {
					return false;
				}
			};
			if (log.isInfoEnabled()) {
				log.info("no upload-file library loaded.");
			}
		}
	}

	public boolean isUploadFile(HttpServletRequest httpServletRequest) {
		return uploadFileFactory.isUploadFile(httpServletRequest);
	}

	public InnerFlowData getUploadFileFlowData(HttpServletRequest request, HttpServletResponse httpServletResponse) {
		UploadFileInfo uploadFileInfo = uploadFileFactory.getUploadFiles(request, null);
		Parameters parameters = uploadFileInfo.getParameters();
		return new UploadFileFlowData(request, httpServletResponse, serviceContext, requestContextPath, parameters);
	}

	public void setCharset(String charset) {
		this.charset = charset;
	}

	public void setUploadFileMaxSize(long uploadFileMaxSize) {
		this.uploadFileMaxSize = uploadFileMaxSize;
	}

	
}
