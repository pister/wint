package wint.core.io.resource.loader;

import wint.core.io.resource.Resource;
import wint.core.io.resource.URLResource;
import wint.lang.exceptions.ResourceException;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * @author songlihuang
 * @date 2024/2/28 11:17
 */
public class FileSystemResourceLoader implements ResourceLoader {

    private File parentDirectory;

    public FileSystemResourceLoader(File parentDirectory) {
        this.parentDirectory = parentDirectory;
    }

    @Override
    public Resource getResource(String name) {
        File file = new File(parentDirectory, name);
        URL fileURL = null;
        try {
            fileURL = file.toURI().toURL();
        } catch (MalformedURLException e) {
            throw new ResourceException(e);
        }
        return new URLResource(fileURL, this);
    }
}
