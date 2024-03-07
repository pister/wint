package wint.mvc.pipeline.valves;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.ref.SoftReference;
import java.util.List;
import java.util.Map;
import java.util.Set;

import wint.core.io.resource.Resource;
import wint.core.io.resource.loader.ResourceLoader;
import wint.core.service.env.Environment;
import wint.help.http.MimeTypeEnum;
import wint.lang.exceptions.WebResourceException;
import wint.lang.io.FastByteArrayInputStream;
import wint.lang.io.FastByteArrayOutputStream;
import wint.lang.io.FileNameUtil;
import wint.lang.utils.CollectionUtil;
import wint.lang.utils.IoUtil;
import wint.lang.utils.MapUtil;
import wint.lang.utils.StringUtil;
import wint.mvc.flow.InnerFlowData;
import wint.mvc.flow.StatusCodes;
import wint.mvc.holder.WintContext;
import wint.mvc.pipeline.PipelineContext;
import wint.mvc.view.types.ViewTypes;

import javax.servlet.http.HttpServletResponse;

public class FileResourceValve extends AbstractValve {
	
	private static final Map<String, String> mappedContentTypes = MapUtil.newHashMap();

    private Map<String, SoftReference<byte[]>> resourceCache;

    private Environment environment;

	static {
		// css
		registerContentType(MimeTypeEnum.CSS);

		// js
		registerContentType(MimeTypeEnum.JS);

		// icon
		registerContentType(MimeTypeEnum.ICO);

		// image
		registerContentType(MimeTypeEnum.GIF);
		registerContentType(MimeTypeEnum.BMP);
		registerContentType(MimeTypeEnum.PNG);
		registerContentType(MimeTypeEnum.JPEG);

		// media
		registerContentType(MimeTypeEnum.SWF);
		registerContentType(MimeTypeEnum.MP3);
		registerContentType(MimeTypeEnum.MP4);

		// document
		registerContentType(MimeTypeEnum.DOC);
		registerContentType(MimeTypeEnum.DOCX);
		registerContentType(MimeTypeEnum.PPT);
		registerContentType(MimeTypeEnum.PPTX);
		registerContentType(MimeTypeEnum.XLS);
		registerContentType(MimeTypeEnum.XLSX);

		// font
		registerContentType(MimeTypeEnum.WOFF);
		registerContentType(MimeTypeEnum.TTF);
	}

	private static void registerContentType(MimeTypeEnum mimeTypeEnum) {
		for (String suffix  : mimeTypeEnum.getSuffixList()) {
			mappedContentTypes.put(suffix, mimeTypeEnum.getMimeType());
		}
	}

	private Set<String> resourceTypes = CollectionUtil.newHashSet();
	
	private ResourceLoader resourceLoader;
	
	public void init() {
		super.init();
		resourceLoader = serviceContext.getResourceLoader();
		if (CollectionUtil.isEmpty(resourceTypes)) {
			resourceTypes.addAll(mappedContentTypes.keySet());
		}
        environment = serviceContext.getConfiguration().getEnvironment();
        if (environment == Environment.TEST) {
            resourceCache = MapUtil.newConcurrentHashMap();
        }
	}

	public void invoke(PipelineContext pipelineContext, InnerFlowData flowData) {
		String targetSuffix = getTargetSuffix(flowData);
		if (isResourceTarget(targetSuffix)) {
			try {
				String contentType = mappedContentTypes.get(targetSuffix);
				flowData.setResponseContentType(contentType);

				HttpServletResponse response = WintContext.getResponse();
				response.addHeader("Access-Control-Allow-Origin", "*");
				response.addHeader("Access-Control-Allow-Headers", "*");
				response.addHeader("Access-Control-Allow-Methods", "GET,POST,OPTIONS");

				performResource(pipelineContext, flowData);
			} catch (IOException e) {
				throw new WebResourceException(e);
			}
		} else {
			pipelineContext.invokeNext(flowData);
		}
	}
	
	protected void performResource(PipelineContext pipelineContext, InnerFlowData flowData) throws IOException {
		String target = flowData.getTarget();
		Resource targetResource = resourceLoader.getResource(target);
		if (targetResource == null || !targetResource.exist()) {
			flowData.setStatusCode(StatusCodes.SC_NOT_FOUND);
			return;
		}

		InputStream is = getInputStream(targetResource);
		flowData.setViewType(ViewTypes.NOP_VIEW_TYPE);
		OutputStream os = flowData.getOutputStream();
		IoUtil.copyAndClose(is, os);
		pipelineContext.breakPipeline();
	}

    protected InputStream getInputStream(Resource targetResource) throws IOException {
        if (environment != Environment.TEST) {
            return targetResource.getInputStream();
        }
        String key = targetResource.getURL().toExternalForm();
        SoftReference<byte[]> dataRef = resourceCache.get(key);
        if (dataRef != null) {
            byte[] data = dataRef.get();
            if (data != null) {
                return new FastByteArrayInputStream(data);
            }
        }
        InputStream is = targetResource.getInputStream();
        if (is == null) {
            return null;
        }
        FastByteArrayOutputStream os = new FastByteArrayOutputStream();
        IoUtil.copyAndClose(is, os);
        byte[] data = os.toByteArray();
        resourceCache.put(key, new SoftReference<byte[]>(data));
        return new FastByteArrayInputStream(data);
    }

	protected String getTargetSuffix(InnerFlowData flowData) {
		String target = flowData.getTarget();
		if (StringUtil.isEmpty(target)) {
			return null;
		}
		String suffix = FileNameUtil.getFileNameSuffix(target);
		if (suffix == null) {
			return null;
		}
		if (target.contains("WEB-INF")) {
			return null;
		}
		String lowerSuffix = suffix.toLowerCase();
		return lowerSuffix;
	}
	
	protected boolean isResourceTarget(String targetSuffix) {
		if (StringUtil.isEmpty(targetSuffix)) {
			return false;
		}
		return resourceTypes.contains(targetSuffix);
	}
	
	public void setResourceTypes(List<String> suffixes) {
		if (CollectionUtil.isEmpty(suffixes)) {
			resourceTypes = CollectionUtil.newHashSet();
		} else {
			Set<String> types = CollectionUtil.newHashSet();
			for (String suffix : suffixes) {
				if (StringUtil.isEmpty(suffix)) {
					continue;
				}
				types.add(suffix.toLowerCase());
			}
			resourceTypes = types;
		}
	}

}
