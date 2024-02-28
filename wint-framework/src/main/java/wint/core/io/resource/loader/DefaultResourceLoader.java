package wint.core.io.resource.loader;

import wint.core.io.resource.NotExistResource;
import wint.core.io.resource.Resource;
import wint.core.io.resource.URLResource;

import java.io.File;
import java.net.URL;

public class DefaultResourceLoader implements ResourceLoader {

    private static final String FILE_PROTOCOL = "file://";

    public Resource getResource(String name) {
        if (name.startsWith(FILE_PROTOCOL)) {
            try {
                URL fileUrl = new URL(name);
                File file = new File(fileUrl.toURI());
                FileSystemResourceLoader fileSystemResourceLoader = new FileSystemResourceLoader(file.getParentFile());
                return new URLResource(fileUrl, fileSystemResourceLoader);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        URL url = DefaultResourceLoader.class.getResource(name);
        if (url != null) {
            return new URLResource(url, this);
        }
        ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
        if (contextClassLoader != null) {
            url = contextClassLoader.getResource(name);
            if (url != null) {
                return new URLResource(url, this);
            }
        }
        url = DefaultResourceLoader.class.getClassLoader().getResource(name);
        if (url != null) {
            return new URLResource(url, this);
        }
        return new NotExistResource();
    }

}
