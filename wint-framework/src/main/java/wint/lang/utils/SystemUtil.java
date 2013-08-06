package wint.lang.utils;

import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;

public class SystemUtil {
	
	public static final String LINE_SEPARATOR = System.getProperty("line.separater", "\n");

    public static int getPid() {  
        RuntimeMXBean runtime = ManagementFactory.getRuntimeMXBean();  
        String name = runtime.getName(); // format: "pid@hostname"  
        System.out.println(name);
        try {  
            return Integer.parseInt(name.substring(0, name.indexOf('@')));  
        } catch (Exception e) {  
            return -1;  
        }  
    }
    
    public static String getSystemInfo() {  
       RuntimeMXBean runtime = ManagementFactory.getRuntimeMXBean();  
       return runtime.getName();
    }
	
}
