package wint.mvc.form.fileupload;

import wint.mvc.parameters.MapParameters;

import java.util.Map;
import java.util.Set;

/**
 * User: longyi
 * Date: 14-3-4
 * Time: 下午1:13
 */
public class FileUploadParameters extends MapParameters {

    private Map<String, UploadFile> uploadFiles;

    public FileUploadParameters(Map<String, String[]> mapParameters, Map<String, UploadFile> uploadFiles) {
        super(mapParameters);
        this.uploadFiles = uploadFiles;
    }

    @Override
    public UploadFile getUploadFile(String name) {
        return uploadFiles.get(name);
    }

    @Override
    public Set<String> getUploadFileNames() {
        return uploadFiles.keySet();
    }
}
