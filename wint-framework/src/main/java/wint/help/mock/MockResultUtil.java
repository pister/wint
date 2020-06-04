package wint.help.mock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import wint.core.config.Constants;
import wint.help.biz.result.Result;
import wint.help.biz.result.ResultSupport;
import wint.help.json.GsonUtil;
import wint.lang.WintException;
import wint.lang.utils.FileUtil;
import wint.lang.utils.SystemUtil;
import wint.lang.utils.TargetUtil;
import wint.mvc.flow.FlowData;

import java.io.File;
import java.io.IOException;
import java.net.URL;

/**
 * User: longyi
 * Date: 14-3-31
 * Time: 上午7:49
 */
public class MockResultUtil {

    private static final Logger log = LoggerFactory.getLogger(MockResultUtil.class);

    public static Result loadMockResult(FlowData flowData) {
        String xx = getProjectName();
        log.warn("================");
        log.warn(xx);
        log.warn("================");
        String data = getMockDataFileData(flowData);

        ResultSupport resultSupport = GsonUtil.formJsonString(data, ResultSupport.class);
        return resultSupport;
    }

    private static String getMockDataFileData(FlowData flowData) {
        String target = flowData.getTarget();
        String mockNamePrefix = flowData.getServiceContext().getConfiguration().getProperties().getString(Constants.PropertyKeys.MOCK_DATA_PREFIX, Constants.Defaults.MOCK_DATA_PREFIX);
        String userHomePath = SystemUtil.getUserHome();
        File userHome = new File(userHomePath);
        if (!userHome.exists()) {
            throw new WintException("user home:" + userHomePath + " not exist!");
        }
        if (!userHome.isDirectory()) {
            throw new WintException("user home:" + userHomePath + " is not a directory!");
        }
        File mockPath = new File(userHome, mockNamePrefix);
        if (!mockPath.exists()) {
            mockPath.mkdirs();
        }

        String filePath = TargetUtil.normalizeTarget(target);
        filePath = filePath + ".json";
        File targetPath = new File(mockPath, filePath);
        File parent = targetPath.getParentFile();
        if (!parent.exists()) {
            parent.mkdirs();
        }
        if (!targetPath.exists()) {
            return initTargetPath(targetPath);
        } else {
            try {
                return FileUtil.readAsString(targetPath);
            } catch (IOException e) {
                throw new WintException(e);
            }
        }
    }

    private static String initTargetPath(File targetPath) {
        String defaultData = "{'success': true, 'models': {}}";
        try {
            FileUtil.writeContent(targetPath, defaultData);
            return defaultData;
        } catch (IOException e) {
            throw new WintException(e);
        }
    }

    private static String getProjectName() {
//        ResourceLoader resourceLoader = flowData.getServiceContext().getResourceLoader();
        URL url = MockResultUtil.class.getResource(MockResultUtil.class.getSimpleName() + ".class");
        return url.toString();
    }

    public static void main(String[] args) {
        String a = getProjectName();
        System.out.println(a);
    }

}
