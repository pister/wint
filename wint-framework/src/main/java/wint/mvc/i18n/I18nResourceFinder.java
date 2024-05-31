package wint.mvc.i18n;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import wint.lang.utils.CollectionUtil;
import wint.lang.utils.MapUtil;
import wint.lang.utils.StringUtil;

import java.io.File;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URL;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * @author songlihuang
 * @date 2024/5/31 16:04
 */
public class I18nResourceFinder {

    private static final Logger log = LoggerFactory.getLogger(I18nResourceFinder.class);

    private Map<String, Class<?>> resultCodeClassMap;

    public void loadResources(String base) throws IOException, ClassNotFoundException {
        Map<String, Class<?>> resultCodeClassMap = MapUtil.newHashMap();
        List<String> resourceNameList = loadResourcesImpl(base);
        log.warn("loading i18n resource from: " + base);
        for (String resourceName : resourceNameList) {
            if (!resourceName.endsWith(".properties")) {
                continue;
            }
            String className = getClassNameByResource(base, resourceName);

            Class<?> clazz = Class.forName(className);
            String simpleName = clazz.getSimpleName();
            Class<?> existClass = resultCodeClassMap.get(simpleName);
            if (existClass == null) {
                resultCodeClassMap.put(simpleName, clazz);
                log.warn("===>>> loaded i18n class: " + className);
                continue;
            }
            if (existClass != clazz) {
                throw new IOException("ResultCode has same simple name for: " + clazz + " and " + existClass);
            }
        }
        this.resultCodeClassMap = resultCodeClassMap;
    }

    private static boolean isSuffixByDotArea(String fullName) {
        String lastName = StringUtil.getLastAfter(fullName, ".");
        if (lastName.length() != 2) {
            return false;
        }
        for (int i = 0; i < lastName.length(); i++) {
            char c = lastName.charAt(i);
            if (!Character.isUpperCase(c)) {
                return false;
            }
        }
        return true;
    }

    private String getClassNameByResource(String base, String resourceName) {
        // TheClassName_zh_CN.properties
        String fullName = resourceName.substring(base.length() + 1); // i18n.
        String fullNameWithoutSuffix = StringUtil.getLastBefore(fullName, ".");

        if (isSuffixByDotArea(fullNameWithoutSuffix)) {
            // 兼容 TheClassName_zh.CN.properties
            fullNameWithoutSuffix = StringUtil.getLastBefore(fullNameWithoutSuffix, ".");
        }

        String packageName = StringUtil.getLastBefore(fullNameWithoutSuffix, ".");
        String classNameWithLang = StringUtil.getLastAfter(fullNameWithoutSuffix, ".");


        String className = StringUtil.getFirstBefore(classNameWithLang, "_");
        return packageName + "." + className;
    }

    public Class<?> findClassBySimpleName(String simpleClassName) {
        return resultCodeClassMap.get(simpleClassName);
    }

    private static List<String> loadResourcesImpl(String packageName) throws IOException {
        List<String> resources = CollectionUtil.newArrayList();
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        String path = packageName.replace('.', '/');
        Enumeration<URL> urls = classLoader.getResources(path);

        while (urls.hasMoreElements()) {
            URL url = urls.nextElement();
            String protocol = url.getProtocol();

            if ("file".equals(protocol)) {
                File directory = new File(url.getFile());
                if (directory.exists() && directory.isDirectory()) {
                    findResources(directory, packageName, resources);
                }
            } else if ("jar".equals(protocol)) {
                JarURLConnection jarURLConnection = (JarURLConnection) url.openConnection();
                JarFile jarFile = jarURLConnection.getJarFile();
                Enumeration<JarEntry> entries = jarFile.entries();

                while (entries.hasMoreElements()) {
                    JarEntry entry = entries.nextElement();
                    String name = entry.getName();
                    if (name.startsWith(path) && !entry.isDirectory()) {
                        resources.add(name.replace('/', '.'));
                    }
                }
            }
        }
        return resources;
    }

    private static void findResources(File directory, String packageName, List<String> resources) {
        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    findResources(file, packageName + "." + file.getName(), resources);
                } else {
                    String resource = packageName + "." + file.getName();
                    resources.add(resource);
                }
            }
        }
    }


}
